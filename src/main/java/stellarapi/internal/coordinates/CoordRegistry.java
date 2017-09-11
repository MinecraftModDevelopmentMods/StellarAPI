package stellarapi.internal.coordinates;

import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.RegistryBuilder;
import stellarapi.api.SAPICapabilities;
import stellarapi.api.SAPIReference;
import stellarapi.api.SAPIRegistries;
import stellarapi.api.coordinates.CCoordinates;
import stellarapi.api.coordinates.ICoordHandler;
import stellarapi.api.coordinates.ICoordProvider;
import stellarapi.api.coordinates.ICoordSystem;
import stellarapi.internal.settings.CoordSettings;
import stellarapi.internal.settings.MainSettings;
import worldsets.api.WAPIReference;
import worldsets.api.event.ProviderEvent;
import worldsets.api.provider.IProviderRegistry;
import worldsets.api.provider.ProviderBuilder;
import worldsets.api.worldset.WorldSet;
import worldsets.api.worldset.WorldSetInstance;

@Mod.EventBusSubscriber(modid = SAPIReference.modid)
public class CoordRegistry {

	public static final String DEFAULT_NAME = "celestial";
	public static final ResourceLocation DEFAULT_ID = new ResourceLocation(DEFAULT_NAME);

	public static final ResourceLocation READABLE_NAMES = new ResourceLocation(SAPIReference.modid, "readable_names");
	public static final ResourceLocation FORCED_VANILLA = new ResourceLocation(SAPIReference.modid, "forced_vanilla");

	private static IForgeRegistry<CCoordinates> coordRegistry;
	private static IProviderRegistry<ICoordProvider> providerRegistry;

	@SubscribeEvent
	public static void onRegistryEvent(RegistryEvent.NewRegistry regRegEvent) {
		coordRegistry = new RegistryBuilder<CCoordinates>()
				.setName(SAPIRegistries.COORDS).setType(CCoordinates.class).setIDRange(0, Integer.MAX_VALUE)
				.setDefaultKey(DEFAULT_ID)
				.create();
	}

	@SubscribeEvent
	public static void register(RegistryEvent.Register<CCoordinates> coordsReg) {
		coordsReg.getRegistry().registerAll(
				forName("horizontal"),
				forName("equatorial"),
				forName("celestial"));
	}

	public static void onInit() {
		// TODO CoordSystem log
		for(CCoordinates coordinates : coordRegistry.getValues()) {
			ResourceLocation parentID = coordinates.getDefaultParentID();
			if(parentID != null && coordRegistry.containsKey(parentID)) {
				parentID = DEFAULT_ID;
				// TODO CoordSystem log this replacement
			}
			// TODO CoordSystem check for position-specifics and collect error
			coordinates.injectParent(parentID, parentID == null? null : coordRegistry.getValue(parentID));
		}
	}

	private static CCoordinates forName(String name) {
		return new CCoordinates().setRegistryName(new ResourceLocation(name));
	}


	@SubscribeEvent
	public static void onProvRegistryEvent(ProviderEvent.NewRegistry pregRegEvent) {
		providerRegistry = new ProviderBuilder<ICoordProvider>()
				.setName(SAPIRegistries.COORDS).setType(ICoordProvider.class)
				// TODO CoordRegistry .setDefaultKey(key)
				.add(new CreateCallback()).add(new AddCallback())
				.add(new SubstitutionCallback()).add(new ClearCallback())
				.create();
	}

	@SubscribeEvent
	public static void onApplySettingsEvent(ProviderEvent.ApplySettings<ICoordProvider> applySettingsEvent) {
		World world = WAPIReference.getDefaultWorld();
		for(WorldSet worldSet : WAPIReference.worldSetList()) {
			WorldSetInstance setInstance = WAPIReference.getWorldSetInstance(world, worldSet);

			ICoordSystem system = setInstance.getCapability(SAPICapabilities.COORDINATES_SYSTEM, null);

			if(system.getProviderID() == null) {
				CoordSettings coords = MainSettings.INSTANCE.perWorldSetMap.get(worldSet).coordinates;
				system.setProviderID(coords.getCurrentProviderID());
			}

			// TODO Post apply settings event here

			system.setupPartial();
		}
	}

	@SubscribeEvent
	public static void onCompleteEvent(ProviderEvent.Complete<ICoordProvider> completeEvent) {
		World world = WAPIReference.getDefaultWorld();
		for(WorldSet worldSet : WAPIReference.worldSetList()) {
			WorldSetInstance setInstance = WAPIReference.getWorldSetInstance(world, worldSet);
			ICoordSystem system = setInstance.getCapability(SAPICapabilities.COORDINATES_SYSTEM, null);

			// Client Placeholder Handling
			if(completeEvent.forPlaceholder)
				if(!system.getHandler(ICoordHandler.class).handleVanilla())
					system.setProviderID(completeEvent.registry.getDefaultKey());
			// TODO Also check for WorldProvider patch

			system.setupComplete();
		}
	}

	@SubscribeEvent
	public static void onSendEvent(ProviderEvent.Send<ICoordProvider> sendEvent) {
		World world = WAPIReference.getDefaultWorld();
		for(WorldSet worldSet : WAPIReference.worldSetList()) {
			WorldSetInstance setInstance = WAPIReference.getWorldSetInstance(world, worldSet);

			ICoordSystem system = setInstance.getCapability(SAPICapabilities.COORDINATES_SYSTEM, null);
			sendEvent.compoundToSend.setTag(worldSet.delegate.name().toString(),
					SAPICapabilities.COORDINATES_SYSTEM.writeNBT(system, null));
		}
	}

	@SubscribeEvent
	public static void onReceiveEvent(ProviderEvent.Receive<ICoordProvider> receiveEvent) {
		World world = WAPIReference.getDefaultWorld();
		for(WorldSet worldSet : WAPIReference.worldSetList()) {
			NBTTagCompound worldSetData = receiveEvent.receivedCompound.getCompoundTag(
					worldSet.delegate.name().toString());
			WorldSetInstance setInstance = WAPIReference.getWorldSetInstance(world, worldSet);

			ICoordSystem system = setInstance.getCapability(SAPICapabilities.COORDINATES_SYSTEM, null);
			SAPICapabilities.COORDINATES_SYSTEM.readNBT(system, null, worldSetData);

			system.setupPartial();
		}
	}

	public static class CreateCallback implements IProviderRegistry.CreateCallback<ICoordProvider> {
		@Override
		public void onCreate(Map<ResourceLocation, ?> slaveset) {
			((Map<ResourceLocation,Object>)slaveset).put(READABLE_NAMES, HashBiMap.<String, ResourceLocation>create());
		}
	}

	public static class AddCallback implements IProviderRegistry.AddCallback<ICoordProvider> {
		@Override
		public void onAdd(ResourceLocation key, ICoordProvider obj, Map<ResourceLocation, ?> slaveset) {
			BiMap<String, ResourceLocation> nameMap = (BiMap<String, ResourceLocation>) slaveset.get(READABLE_NAMES);
			if(nameMap.containsKey(obj.getReadableName())) {
				// TODO CoordSystem illegal arguments exception - duplication
			}

			nameMap.put(obj.getReadableName(), key);
		}
	}

	public static class ClearCallback implements IProviderRegistry.ClearCallback<ICoordProvider> {
		@Override
		public void onClear(IProviderRegistry<ICoordProvider> registry, Map<ResourceLocation, ?> slaveset) {
			BiMap<String, ResourceLocation> nameMap = (BiMap<String, ResourceLocation>) slaveset.get(READABLE_NAMES);
			nameMap.clear();
		}
	}

	public static class SubstitutionCallback implements IProviderRegistry.SubstitutionCallback<ICoordProvider> {
		@Override
		public void onSubstitution(Map<ResourceLocation, ?> slaveset, ResourceLocation key,
				ICoordProvider original, ICoordProvider replacement) {
			BiMap<String, ResourceLocation> nameMap = (BiMap<String, ResourceLocation>) slaveset.get(READABLE_NAMES);

			nameMap.inverse().remove(key);

			if(nameMap.containsKey(replacement.getReadableName())) {
				// TODO CoordSystem illegal arguments exception - duplication
			}

			nameMap.put(replacement.getReadableName(), key);
		}
	}
}