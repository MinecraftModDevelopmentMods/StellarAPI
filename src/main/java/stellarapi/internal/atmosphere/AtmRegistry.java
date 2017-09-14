package stellarapi.internal.atmosphere;

import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.RegistryBuilder;
import net.minecraftforge.fml.common.registry.RegistryDelegate;
import stellarapi.api.SAPICapabilities;
import stellarapi.api.SAPIRegistries;
import stellarapi.api.atmosphere.AtmosphereType;
import stellarapi.api.atmosphere.IAtmProvider;
import stellarapi.api.atmosphere.IAtmSystem;
import stellarapi.api.coordinates.CCoordinates;
import stellarapi.api.coordinates.ICoordHandler;
import stellarapi.api.coordinates.ICoordProvider;
import stellarapi.api.coordinates.ICoordSettings;
import stellarapi.api.coordinates.ICoordSystem;
import stellarapi.api.event.ApplyWorldSettingsEvent;
import stellarapi.internal.settings.AtmSettings;
import stellarapi.internal.settings.CoordSettings;
import stellarapi.internal.settings.CoordWorldSettings;
import stellarapi.internal.settings.MainSettings;
import worldsets.api.WAPIReference;
import worldsets.api.event.ProviderEvent;
import worldsets.api.provider.IProviderRegistry;
import worldsets.api.provider.ProviderBuilder;
import worldsets.api.worldset.WorldSet;
import worldsets.api.worldset.WorldSetInstance;

public class AtmRegistry {
	public static final ResourceLocation DEFAULT_ID = new ResourceLocation("overworld_atmosphere");

	private static IForgeRegistry<AtmosphereType> atmTypeRegistry;
	private static IProviderRegistry<IAtmProvider> providerRegistry;

	@SubscribeEvent
	public static void onRegistryEvent(RegistryEvent.NewRegistry regRegEvent) {
		atmTypeRegistry = new RegistryBuilder<AtmosphereType>()
				.setName(SAPIRegistries.ATMOSPHERES).setType(AtmosphereType.class).setIDRange(0, Integer.MAX_VALUE)
				.setDefaultKey(DEFAULT_ID)
				.create();
	}

	@SubscribeEvent
	public static void onProvRegistryEvent(ProviderEvent.NewRegistry pregRegEvent) {
		providerRegistry = new ProviderBuilder<IAtmProvider>()
				.setName(SAPIRegistries.COORDS).setType(IAtmProvider.class)
				// TODO AtmRegistry .setDefaultKey(key)
				.add(new CreateCallback()).add(new AddCallback())
				.add(new SubstitutionCallback()).add(new ClearCallback())
				.create();
	}

	@SubscribeEvent
	public static void onApplySettingsEvent(ProviderEvent.ApplySettings<ICoordProvider> applySettingsEvent) {
		World world = WAPIReference.getDefaultWorld();
		for(WorldSet worldSet : WAPIReference.worldSetList()) {
			WorldSetInstance setInstance = WAPIReference.getWorldSetInstance(world, worldSet);
			IAtmSystem system = setInstance.getCapability(SAPICapabilities.ATMOSPHERE_SYSTEM, null);

			if(system.getWorldAtmType() == null) {
				AtmSettings settings = MainSettings.INSTANCE.perWorldSetMap.get(worldSet.delegate).atmosphere;
				IAtmProvider provider = settings.getCurrentProvider();
				system.setWorldAtmType(provider.getAtmosphereType(worldSet));
			}
		}
	}

	@SubscribeEvent
	public static void onCompleteEvent(ProviderEvent.Complete<ICoordProvider> completeEvent) {
		World world = WAPIReference.getDefaultWorld();
		for(WorldSet worldSet : WAPIReference.worldSetList()) {
			WorldSetInstance setInstance = WAPIReference.getWorldSetInstance(world, worldSet);
			IAtmSystem system = setInstance.getCapability(SAPICapabilities.ATMOSPHERE_SYSTEM, null);

			// Client Placeholder Handling
			if(completeEvent.forPlaceholder) {
				IAtmProvider provider = providerRegistry.getProvider(completeEvent.registry.getDefaultKey());
				system.setWorldAtmType(provider.getAtmosphereType(worldSet));
			}
			// TODO Also check for WorldProvider patch
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onWorldLoad(WorldEvent.Load worldLoadEvent) {
		World world = worldLoadEvent.getWorld();
		WorldSet worldSet = WAPIReference.getPrimaryWorldSet(world);

		if(worldSet == null)
			return;

		WorldSetInstance setInstance = WAPIReference.getWorldSetInstance(world, worldSet);
		IAtmSystem system = setInstance.getCapability(SAPICapabilities.ATMOSPHERE_SYSTEM, null);

		AtmSettings coords = MainSettings.INSTANCE.perWorldSetMap.get(worldSet.delegate).atmosphere;

		// TODO AtmSystem fill in this settings handling part

		/*CoordWorldSettings worldCoords = coords.defaultSettings;
		String worldName = world.provider.getDimensionType().getName();

		if(coords.additionalSettings.containsKey(worldName))
			worldCoords = coords.additionalSettings.get(worldName);

		MinecraftForge.EVENT_BUS.post(
				new ApplyWorldSettingsEvent<ICoordHandler>(
						ICoordHandler.class, system.getHandler(), worldCoords.mainSettings, world));

		for(Map.Entry<RegistryDelegate<CCoordinates>, ICoordSettings> entry : worldCoords.specificSettings.entrySet())
			entry.getKey().get().applySettings(entry.getValue(), world);*/
	}

	@SubscribeEvent
	public static void onSendEvent(ProviderEvent.Send<ICoordProvider> sendEvent) {
		World world = WAPIReference.getDefaultWorld();
		for(WorldSet worldSet : WAPIReference.worldSetList()) {
			WorldSetInstance setInstance = WAPIReference.getWorldSetInstance(world, worldSet);
			IAtmSystem system = setInstance.getCapability(SAPICapabilities.ATMOSPHERE_SYSTEM, null);
			sendEvent.compoundToSend.setTag(worldSet.delegate.name().toString(),
					SAPICapabilities.ATMOSPHERE_SYSTEM.writeNBT(system, null));
		}
	}

	@SubscribeEvent
	public static void onReceiveEvent(ProviderEvent.Receive<ICoordProvider> receiveEvent) {
		World world = WAPIReference.getDefaultWorld();
		for(WorldSet worldSet : WAPIReference.worldSetList()) {
			NBTBase worldSetData = receiveEvent.receivedCompound.getTag(
					worldSet.delegate.name().toString());
			WorldSetInstance setInstance = WAPIReference.getWorldSetInstance(world, worldSet);
			IAtmSystem system = setInstance.getCapability(SAPICapabilities.ATMOSPHERE_SYSTEM, null);
			SAPICapabilities.ATMOSPHERE_SYSTEM.readNBT(system, null, worldSetData);
		}
	}

	public static class CreateCallback implements IProviderRegistry.CreateCallback<IAtmProvider> {
		@Override
		public void onCreate(Map<ResourceLocation, ?> slaveset) {
			((Map<ResourceLocation,Object>)slaveset).put(SAPIRegistries.READABLE_NAMES, HashBiMap.<String, ResourceLocation>create());
		}
	}

	public static class AddCallback implements IProviderRegistry.AddCallback<IAtmProvider> {
		@Override
		public void onAdd(ResourceLocation key, IAtmProvider obj, Map<ResourceLocation, ?> slaveset) {
			BiMap<String, ResourceLocation> nameMap = (BiMap<String, ResourceLocation>) slaveset.get(SAPIRegistries.READABLE_NAMES);
			if(nameMap.containsKey(obj.getReadableName())) {
				// TODO AtmosphereSystem illegal arguments exception - duplication
			}

			nameMap.put(obj.getReadableName(), key);
		}
	}

	public static class ClearCallback implements IProviderRegistry.ClearCallback<IAtmProvider> {
		@Override
		public void onClear(IProviderRegistry<IAtmProvider> registry, Map<ResourceLocation, ?> slaveset) {
			BiMap<String, ResourceLocation> nameMap = (BiMap<String, ResourceLocation>) slaveset.get(SAPIRegistries.READABLE_NAMES);
			nameMap.clear();
		}
	}

	public static class SubstitutionCallback implements IProviderRegistry.SubstitutionCallback<IAtmProvider> {
		@Override
		public void onSubstitution(Map<ResourceLocation, ?> slaveset, ResourceLocation key,
				IAtmProvider original, IAtmProvider replacement) {
			BiMap<String, ResourceLocation> nameMap = (BiMap<String, ResourceLocation>) slaveset.get(SAPIRegistries.READABLE_NAMES);

			nameMap.inverse().remove(key);

			if(nameMap.containsKey(replacement.getReadableName())) {
				// TODO AtmosphereSystem illegal arguments exception - duplication
			}

			nameMap.put(replacement.getReadableName(), key);
		}
	}
}
