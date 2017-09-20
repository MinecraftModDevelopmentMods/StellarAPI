package stellarapi.internal.coordinates;

import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IRegistryDelegate;
import net.minecraftforge.registries.RegistryBuilder;
import stellarapi.api.SAPICapabilities;
import stellarapi.api.SAPIReferences;
import stellarapi.api.SAPIRegistries;
import stellarapi.api.coordinates.CCoordinates;
import stellarapi.api.coordinates.ICoordHandler;
import stellarapi.api.coordinates.ICoordProvider;
import stellarapi.api.coordinates.ICoordSettings;
import stellarapi.api.coordinates.ICoordSystem;
import stellarapi.api.event.settings.ApplyProviderIDEvent;
import stellarapi.api.event.settings.ApplySettingsEvent;
import stellarapi.internal.settings.CoordSettings;
import stellarapi.internal.settings.CoordWorldSettings;
import stellarapi.internal.settings.MainSettings;
import stellarapi.internal.world.WorldRegistry;
import worldsets.api.WAPIReference;
import worldsets.api.event.ProviderEvent;
import worldsets.api.provider.IProviderRegistry;
import worldsets.api.provider.ProviderBuilder;
import worldsets.api.worldset.WorldSet;

@Mod.EventBusSubscriber(modid = SAPIReferences.modid)
public class CoordRegistry {

	public static final String DEFAULT_NAME = "celestial";
	public static final ResourceLocation DEFAULT_ID = new ResourceLocation(DEFAULT_NAME);

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
			if(parentID != null && !coordRegistry.containsKey(parentID)) {
				parentID = DEFAULT_ID;
				// TODO CoordSystem log this replacement
			}
			// TODO CoordSystem check for position-specifics(?) and collect error
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
				.addDependencies(SAPIRegistries.CELESTIALS)
				.create();
	}

	@SubscribeEvent
	public static void onApplySettingsEvent(ProviderEvent.ApplySettings<ICoordProvider> applySettingsEvent) {
		World world = applySettingsEvent.world;
		WorldSet worldSet = WAPIReference.getPrimaryWorldSet(world);
		if(worldSet == null)
			return;

		ICoordSystem system = world.getCapability(SAPICapabilities.COORDINATES_SYSTEM, null);

		ResourceLocation settingsID = MainSettings.INSTANCE.perWorldSetMap.get(worldSet.delegate).coordinates.getCurrentProviderID();
		ResourceLocation prevID = system.getProviderID() != null? system.getProviderID() : settingsID;

		// Determine ID from settings
		ApplyProviderIDEvent.Coordinates event = new ApplyProviderIDEvent.Coordinates(world, worldSet, prevID, settingsID);
		MinecraftForge.EVENT_BUS.post(event);
		if(system.getProviderID() == null || prevID != event.resultID) {
			system.setProviderID(event.resultID);
		}

		// Determine system ID, and handle vanilla
		// Client Placeholder || Unpatched WorldProvider Handling
		if(world.isRemote || !WorldRegistry.customApplicable(world))
			if(!system.getHandler().handleVanilla(world)) {
				system.setProviderID(applySettingsEvent.registry.getDefaultKey());
				system.getHandler().handleVanilla(world);
			}

		CoordWorldSettings worldCoords = getWorldSettings(world, worldSet);

		// Apply independent settings to each coordinates
		for(Map.Entry<IRegistryDelegate<CCoordinates>, ICoordSettings> entry : worldCoords.specificSettings.entrySet())
			entry.getKey().get().applySettings(entry.getValue(), world);

		// Apply dependent settings on the world & coordinates handler
		MinecraftForge.EVENT_BUS.post(
				new ApplySettingsEvent<ICoordHandler>(
						ICoordHandler.class, system.getHandler(), worldCoords.mainSettings, world));

		// Sets up everything partially
		system.setupPartial();
	}

	@SubscribeEvent
	public static void onCompleteEvent(ProviderEvent.Complete<ICoordProvider> completeEvent) {
		World world = completeEvent.world;
		WorldSet worldSet = WAPIReference.getPrimaryWorldSet(world);
		if(worldSet == null)
			return;

		ICoordSystem system = world.getCapability(SAPICapabilities.COORDINATES_SYSTEM, null);

		// Sets up everything completely - using data from partial setup
		system.setupComplete();
	}

	private static CoordWorldSettings getWorldSettings(World world, WorldSet worldSet) {
		CoordSettings coords = MainSettings.INSTANCE.perWorldSetMap.get(worldSet.delegate).coordinates;
		CoordWorldSettings worldCoords = coords.defaultSettings;
		String worldName = world.provider.getDimensionType().getName();
		if(coords.additionalSettings.containsKey(worldName))
			worldCoords = coords.additionalSettings.get(worldName);
		return worldCoords;
	}

	@SubscribeEvent
	public static void onSendEvent(ProviderEvent.Send<ICoordProvider> sendEvent) {
		World world = sendEvent.world;
		WorldSet worldSet = WAPIReference.getPrimaryWorldSet(world);
		if(worldSet == null)
			return;

		// Write ID and handler information
		ICoordSystem system = world.getCapability(SAPICapabilities.COORDINATES_SYSTEM, null);
		sendEvent.compoundToSend.setTag("coordinatesSystem",
				SAPICapabilities.COORDINATES_SYSTEM.writeNBT(system, null));
	}

	@SubscribeEvent
	public static void onReceiveEvent(ProviderEvent.Receive<ICoordProvider> receiveEvent) {
		World world = receiveEvent.world;
		WorldSet worldSet = WAPIReference.getPrimaryWorldSet(world);
		if(worldSet == null)
			return;

		// Read ID and handler information
		ICoordSystem system = world.getCapability(SAPICapabilities.COORDINATES_SYSTEM, null);
		SAPICapabilities.COORDINATES_SYSTEM.readNBT(system, null,
				receiveEvent.receivedCompound.getTag("coordinatesSystem"));

		// Sets up everything partially
		system.setupPartial();
	}


	public static class CreateCallback implements IProviderRegistry.CreateCallback<ICoordProvider> {
		@Override
		public void onCreate(Map<ResourceLocation, ?> slaveset) {
			((Map<ResourceLocation,Object>)slaveset).put(SAPIRegistries.READABLE_NAMES, HashBiMap.<String, ResourceLocation>create());
		}
	}

	public static class AddCallback implements IProviderRegistry.AddCallback<ICoordProvider> {
		@Override
		public void onAdd(ResourceLocation key, ICoordProvider obj, Map<ResourceLocation, ?> slaveset) {
			BiMap<String, ResourceLocation> nameMap = (BiMap<String, ResourceLocation>) slaveset.get(SAPIRegistries.READABLE_NAMES);
			if(nameMap.containsKey(obj.getReadableName())) {
				throw new IllegalArgumentException(
						String.format("Registered with duplicate name : %s", obj.getReadableName()));
			}

			nameMap.put(obj.getReadableName(), key);
		}
	}

	public static class ClearCallback implements IProviderRegistry.ClearCallback<ICoordProvider> {
		@Override
		public void onClear(IProviderRegistry<ICoordProvider> registry, Map<ResourceLocation, ?> slaveset) {
			BiMap<String, ResourceLocation> nameMap = (BiMap<String, ResourceLocation>) slaveset.get(SAPIRegistries.READABLE_NAMES);
			nameMap.clear();
		}
	}

	public static class SubstitutionCallback implements IProviderRegistry.SubstitutionCallback<ICoordProvider> {
		@Override
		public void onSubstitution(Map<ResourceLocation, ?> slaveset, ResourceLocation key,
				ICoordProvider original, ICoordProvider replacement) {
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