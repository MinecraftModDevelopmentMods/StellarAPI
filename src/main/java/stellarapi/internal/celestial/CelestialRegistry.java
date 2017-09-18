package stellarapi.internal.celestial;

import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.RegistryBuilder;
import stellarapi.api.SAPICapabilities;
import stellarapi.api.SAPIReference;
import stellarapi.api.SAPIRegistries;
import stellarapi.api.celestials.CelestialType;
import stellarapi.api.celestials.ICelestialScene;
import stellarapi.api.celestials.ICelestialSystem;
import stellarapi.api.celestials.collection.CelestialCollection;
import stellarapi.api.celestials.collection.ICelestialProvider;
import stellarapi.api.coordinates.ICoordHandler;
import stellarapi.api.event.settings.ApplyGlobalSettingsEvent;
import stellarapi.internal.settings.CelestialSettings;
import stellarapi.internal.settings.MainSettings;
import worldsets.api.WAPIReference;
import worldsets.api.event.ProviderEvent;
import worldsets.api.provider.IProviderRegistry;
import worldsets.api.provider.ProviderBuilder;
import worldsets.api.worldset.WorldSet;
import worldsets.api.worldset.WorldSetInstance;

@Mod.EventBusSubscriber(modid = SAPIReference.modid)
public class CelestialRegistry {

	private static IForgeRegistry<CelestialType> typeRegistry;
	private static IProviderRegistry<ICelestialProvider> providerRegistry;

	@SubscribeEvent
	public static void onRegistryEvent(RegistryEvent.NewRegistry regRegEvent) {
		typeRegistry = new RegistryBuilder<CelestialType>().setName(SAPIRegistries.CELESTIALS)
				.setType(CelestialType.class).setIDRange(0, Integer.MAX_VALUE)
				.create();
	}


	public static void onInit() {
		// TODO CelestialSystem log
		for(CelestialType type : typeRegistry.getValues()) {
			ResourceLocation parentID = type.getParentID();
			if(parentID != null && !typeRegistry.containsKey(parentID)) {
				// TODO CelestialSystem exception
			}
			// TODO CelestialSystem check for position-specifics and collect error
			type.injectParent(parentID, parentID == null? null : typeRegistry.getValue(parentID));
		}
	}


	@SubscribeEvent
	public static void onProviderRegistryEvent(ProviderEvent.NewRegistry regRegEvent) {
		providerRegistry = new ProviderBuilder<ICelestialProvider>().setName(SAPIRegistries.CELESTIALS)
				.setType(ICelestialProvider.class)
				// TODO CelestialRegistry .setDefaultKey(key)
				.add(new CreateCallback()).add(new AddCallback())
				.add(new SubstitutionCallback()).add(new ClearCallback())
				.create();
	}

	@SubscribeEvent
	public static void onApplySettingsEvent(ProviderEvent.ApplySettings<ICelestialProvider> applySettingsEvent) {
		World world = WAPIReference.getDefaultWorld(applySettingsEvent.isRemote);
		for(WorldSet worldSet : WAPIReference.worldSetList()) {
			WorldSetInstance setInstance = WAPIReference.getWorldSetInstance(world, worldSet);
			ICelestialSystem system = setInstance.getCapability(SAPICapabilities.CELESTIAL_SYSTEM, null);

			for(CelestialType type : SAPIRegistries.getOrderedTypes()) {
				if(system.isAbsent(type)) {
					CelestialSettings settings = MainSettings.INSTANCE.perWorldSetMap.get(worldSet.delegate).celestials;
					system.validateNset(type, settings.celestialMap.get(type.delegate).getCurrentProviderID());
				}
			}
			// TODO CProviders Settings -> ID force?

			for(CelestialType type : SAPIRegistries.getOrderedTypes()) {
				if(!system.isAbsent(type)) {
					CelestialCollection collection = system.getCollection(type);

					CelestialSettings celSettings = MainSettings.INSTANCE.perWorldSetMap.get(worldSet.delegate).celestials;
					Object settings = celSettings.celestialMap.get(type.delegate).collectionSettings;

					MinecraftForge.EVENT_BUS.post(
							new ApplyGlobalSettingsEvent<CelestialCollection>(
									CelestialCollection.class, collection, settings));
				}
			}


			for(CelestialType type : SAPIRegistries.getOrderedTypes())
				if(!system.isAbsent(type))
					system.getCollection(type).setupPartial();
		}
	}

	@SubscribeEvent
	public static void onCompleteEvent(ProviderEvent.Complete<ICelestialProvider> completeEvent) {
		World world = WAPIReference.getDefaultWorld(completeEvent.isRemote);
		for(WorldSet worldSet : WAPIReference.worldSetList()) {
			WorldSetInstance setInstance = WAPIReference.getWorldSetInstance(world, worldSet);
			ICelestialSystem system = setInstance.getCapability(SAPICapabilities.CELESTIAL_SYSTEM, null);

			// Client Placeholder Handling
			if(completeEvent.forPlaceholder) {
				for(CelestialType type : SAPIRegistries.getOrderedTypes()) {
					if(!system.isAbsent(type) && !system.getCollection(type).handleVanilla())
						system.validateNset(type, completeEvent.registry.getDefaultKey());
				}
			}
			// TODO Also check for WorldProvider patch

			for(CelestialType type : SAPIRegistries.getOrderedTypes())
				if(!system.isAbsent(type))
					system.getCollection(type).setupComplete();
		}

		if(completeEvent.isRemote && !completeEvent.forPlaceholder)
			onSetupWorldSpecific(world);
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onWorldLoad(WorldEvent.Load worldLoadEvent) {
		onSetupWorldSpecific(worldLoadEvent.getWorld());
	}

	private static void onSetupWorldSpecific(World world) {
		WorldSet worldSet = WAPIReference.getPrimaryWorldSet(world);

		if(worldSet == null)
			return;

		WorldSetInstance setInstance = WAPIReference.getWorldSetInstance(world, worldSet);

		ICelestialSystem system = setInstance.getCapability(SAPICapabilities.CELESTIAL_SYSTEM, null);
		ICelestialScene scene = world.getCapability(SAPICapabilities.CELESTIAL_SCENE, null);
		scene.setupComplete(system);
	}

	@SubscribeEvent
	public static void onSendEvent(ProviderEvent.Send<ICelestialProvider> sendEvent) {
		World world = WAPIReference.getDefaultWorld(false);
		for(WorldSet worldSet : WAPIReference.worldSetList()) {
			WorldSetInstance setInstance = WAPIReference.getWorldSetInstance(world, worldSet);
			ICelestialSystem system = setInstance.getCapability(SAPICapabilities.CELESTIAL_SYSTEM, null);
			sendEvent.compoundToSend.setTag(worldSet.delegate.name().toString(),
					SAPICapabilities.CELESTIAL_SYSTEM.writeNBT(system, null));
		}
	}

	@SubscribeEvent
	public static void onReceiveEvent(ProviderEvent.Receive<ICelestialProvider> receiveEvent) {
		World world = WAPIReference.getDefaultWorld(true);
		for(WorldSet worldSet : WAPIReference.worldSetList()) {
			NBTTagCompound worldSetData = receiveEvent.receivedCompound.getCompoundTag(
					worldSet.delegate.name().toString());
			WorldSetInstance setInstance = WAPIReference.getWorldSetInstance(world, worldSet);
			ICelestialSystem system = setInstance.getCapability(SAPICapabilities.CELESTIAL_SYSTEM, null);
			SAPICapabilities.CELESTIAL_SYSTEM.readNBT(system, null, worldSetData);

			for(CelestialType type : SAPIRegistries.getOrderedTypes())
				if(!system.isAbsent(type))
					system.getCollection(type).setupPartial();
		}
	}

	public static class CreateCallback implements IProviderRegistry.CreateCallback<ICelestialProvider> {
		@Override
		public void onCreate(Map<ResourceLocation, ?> slaveset) {
			((Map<ResourceLocation,Object>)slaveset).put(SAPIRegistries.READABLE_NAMES, HashBiMap.<String, ResourceLocation>create());
		}
	}

	public static class AddCallback implements IProviderRegistry.AddCallback<ICelestialProvider> {
		@Override
		public void onAdd(ResourceLocation key, ICelestialProvider obj, Map<ResourceLocation, ?> slaveset) {
			BiMap<String, ResourceLocation> nameMap = (BiMap<String, ResourceLocation>) slaveset.get(SAPIRegistries.READABLE_NAMES);
			if(nameMap.containsKey(obj.getReadableName())) {
				// TODO CelestialSystem illegal arguments exception - duplication
			}

			nameMap.put(obj.getReadableName(), key);
		}
	}

	public static class ClearCallback implements IProviderRegistry.ClearCallback<ICelestialProvider> {
		@Override
		public void onClear(IProviderRegistry<ICelestialProvider> registry, Map<ResourceLocation, ?> slaveset) {
			BiMap<String, ResourceLocation> nameMap = (BiMap<String, ResourceLocation>) slaveset.get(SAPIRegistries.READABLE_NAMES);
			nameMap.clear();
		}
	}

	public static class SubstitutionCallback implements IProviderRegistry.SubstitutionCallback<ICelestialProvider> {
		@Override
		public void onSubstitution(Map<ResourceLocation, ?> slaveset, ResourceLocation key,
				ICelestialProvider original, ICelestialProvider replacement) {
			BiMap<String, ResourceLocation> nameMap = (BiMap<String, ResourceLocation>) slaveset.get(SAPIRegistries.READABLE_NAMES);

			nameMap.inverse().remove(key);

			if(nameMap.containsKey(replacement.getReadableName())) {
				// TODO CelestialSystem illegal arguments exception - duplication
			}

			nameMap.put(replacement.getReadableName(), key);
		}
	}
}