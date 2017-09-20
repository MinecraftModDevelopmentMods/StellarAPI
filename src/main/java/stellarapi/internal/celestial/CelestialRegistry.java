package stellarapi.internal.celestial;

import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import stellarapi.api.SAPICapabilities;
import stellarapi.api.SAPIReferences;
import stellarapi.api.SAPIRegistries;
import stellarapi.api.celestials.CelestialType;
import stellarapi.api.celestials.ICelestialScene;
import stellarapi.api.celestials.ICelestialSystem;
import stellarapi.api.celestials.collection.CelestialCollection;
import stellarapi.api.celestials.collection.ICelestialProvider;
import stellarapi.api.event.settings.ApplyProviderIDEvent;
import stellarapi.api.event.settings.ApplySettingsEvent;
import stellarapi.internal.settings.CelestialSettings;
import stellarapi.internal.settings.MainSettings;
import stellarapi.internal.world.WorldRegistry;
import worldsets.api.WAPIReference;
import worldsets.api.event.ProviderEvent;
import worldsets.api.provider.IProviderRegistry;
import worldsets.api.provider.ProviderBuilder;
import worldsets.api.worldset.WorldSet;

@Mod.EventBusSubscriber(modid = SAPIReferences.modid)
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
		World world = applySettingsEvent.world;
		WorldSet worldSet = WAPIReference.getPrimaryWorldSet(world);
		if(worldSet == null)
			return;

		ICelestialSystem system = world.getCapability(SAPICapabilities.CELESTIAL_SYSTEM, null);

		// Determine ID from settings
		for(CelestialType type : SAPIRegistries.getOrderedTypes()) {
			CelestialSettings settings = MainSettings.INSTANCE.perWorldSetMap.get(worldSet.delegate).celestials;
			ResourceLocation settingsID = settings.celestialMap.get(type.delegate).getCurrentProviderID();
			ResourceLocation prevID = system.isAbsent(type)? settingsID : system.getProviderID(type);

			ApplyProviderIDEvent.Celestials event = new ApplyProviderIDEvent.Celestials(world, worldSet, type, prevID, settingsID);
			MinecraftForge.EVENT_BUS.post(event);
			if(system.isAbsent(type) || prevID != event.resultID)
				system.validateNset(type, event.resultID);
		}

		// Determine system ID, and handle vanilla
		// Client Placeholder || Unpatched WorldProvider Handling
		if(world.isRemote || !WorldRegistry.customApplicable(world)) {
			for(CelestialType type : SAPIRegistries.getOrderedTypes()) {
				if(!system.isAbsent(type) && !system.getCollection(type).canHandleVanilla(world))
					system.validateNset(type, applySettingsEvent.registry.getDefaultKey());
				// Handles vanilla
				if(!system.isAbsent(type))
					system.handleVanilla(type, world);
			}
		}

		// Apply settings to the collection. This can happen multiple times on worlds of same worldset.
		// Though the result should be consistent as the settings is constant for those worlds.
		for(CelestialType type : SAPIRegistries.getOrderedTypes()) {
			if(!system.isAbsent(type)) {
				CelestialCollection collection = system.getCollection(type);

				CelestialSettings celSettings = MainSettings.INSTANCE.perWorldSetMap.get(worldSet.delegate).celestials;
				Object settings = celSettings.celestialMap.get(type.delegate).collectionSettings;

				MinecraftForge.EVENT_BUS.post(
						new ApplySettingsEvent<CelestialCollection>(
								CelestialCollection.class, collection, settings, world));
			}
		}

		// Partial Setup
		for(CelestialType type : SAPIRegistries.getOrderedTypes())
			if(!system.isAbsent(type))
				system.getCollection(type).setupPartial();
	}

	@SubscribeEvent
	public static void onCompleteEvent(ProviderEvent.Complete<ICelestialProvider> completeEvent) {
		World world = completeEvent.world;
		WorldSet worldSet = WAPIReference.getPrimaryWorldSet(world);
		if(worldSet == null)
			return;

		ICelestialSystem system = world.getCapability(SAPICapabilities.CELESTIAL_SYSTEM, null);

		// Complete Setup
		for(CelestialType type : SAPIRegistries.getOrderedTypes())
			if(!system.isAbsent(type))
				system.getCollection(type).setupComplete();

		// Complete setup of celestial scene
		ICelestialScene scene = world.getCapability(SAPICapabilities.CELESTIAL_SCENE, null);
		scene.setupComplete(system);
	}

	@SubscribeEvent
	public static void onSendEvent(ProviderEvent.Send<ICelestialProvider> sendEvent) {
		World world = sendEvent.world;
		WorldSet worldSet = WAPIReference.getPrimaryWorldSet(world);
		if(worldSet == null)
			return;

		// Write ID and celestial system information
		ICelestialSystem system = world.getCapability(SAPICapabilities.CELESTIAL_SYSTEM, null);
		sendEvent.compoundToSend.setTag("celestialSystem",
				SAPICapabilities.CELESTIAL_SYSTEM.writeNBT(system, null));
	}

	@SubscribeEvent
	public static void onReceiveEvent(ProviderEvent.Receive<ICelestialProvider> receiveEvent) {
		World world = receiveEvent.world;
		WorldSet worldSet = WAPIReference.getPrimaryWorldSet(world);
		if(worldSet == null)
			return;

		// Read ID and celestial system information
		ICelestialSystem system = world.getCapability(SAPICapabilities.CELESTIAL_SYSTEM, null);
		SAPICapabilities.CELESTIAL_SYSTEM.readNBT(system, null,
				receiveEvent.receivedCompound.getTag("celestialSystem"));

		// Partial setup
		for(CelestialType type : SAPIRegistries.getOrderedTypes())
			if(!system.isAbsent(type))
				system.getCollection(type).setupPartial();
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
				throw new IllegalArgumentException(
						String.format("Registered with duplicate name : %s", obj.getReadableName()));
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
				throw new IllegalArgumentException(
						String.format("Registered with duplicate name : %s", replacement.getReadableName()));
			}

			nameMap.put(replacement.getReadableName(), key);
		}
	}
}