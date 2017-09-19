package stellarapi.internal.atmosphere;

import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import stellarapi.api.SAPICapabilities;
import stellarapi.api.SAPIRegistries;
import stellarapi.api.atmosphere.IAtmHolder;
import stellarapi.api.atmosphere.IAtmProvider;
import stellarapi.api.atmosphere.IAtmSetProvider;
import stellarapi.api.atmosphere.IAtmSystem;
import stellarapi.api.coordinates.ICoordProvider;
import stellarapi.api.event.settings.ApplyProviderIDEvent;
import stellarapi.api.event.settings.ApplyWorldSettingsEvent;
import stellarapi.internal.settings.AtmSettings;
import stellarapi.internal.settings.AtmWorldSettings;
import stellarapi.internal.settings.MainSettings;
import worldsets.api.WAPIReference;
import worldsets.api.event.ProviderEvent;
import worldsets.api.provider.IProviderRegistry;
import worldsets.api.provider.ProviderBuilder;
import worldsets.api.worldset.WorldSet;
import worldsets.api.worldset.WorldSetInstance;

public class AtmRegistry {
	public static final ResourceLocation DEFAULT_ID = new ResourceLocation("overworld_atmosphere");

	private static IProviderRegistry<IAtmProvider> providerRegistry;

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
		World world = WAPIReference.getDefaultWorld(applySettingsEvent.isRemote);
		for(WorldSet worldSet : WAPIReference.worldSetList()) {
			WorldSetInstance setInstance = WAPIReference.getWorldSetInstance(world, worldSet);
			IAtmSystem system = setInstance.getCapability(SAPICapabilities.ATMOSPHERE_SYSTEM, null);

			ResourceLocation settingsID = MainSettings.INSTANCE.perWorldSetMap.get(worldSet.delegate).atmosphere.getCurrentProviderID();
			ResourceLocation prevID = system.getProviderID() != null? system.getProviderID() : settingsID;

			ApplyProviderIDEvent.Atmosphere event = new ApplyProviderIDEvent.Atmosphere(worldSet, prevID, settingsID);
			MinecraftForge.EVENT_BUS.post(event);
			if(system.getProviderID() == null || prevID != event.resultID) {
				system.setProviderID(event.resultID);
			}
		}
	}

	@SubscribeEvent
	public static void onCompleteEvent(ProviderEvent.Complete<ICoordProvider> completeEvent) {
		World world = WAPIReference.getDefaultWorld(completeEvent.isRemote);
		for(WorldSet worldSet : WAPIReference.worldSetList()) {
			WorldSetInstance setInstance = WAPIReference.getWorldSetInstance(world, worldSet);
			IAtmSystem system = setInstance.getCapability(SAPICapabilities.ATMOSPHERE_SYSTEM, null);

			// Client Placeholder Handling
			if(completeEvent.forPlaceholder)
				system.setProviderID(completeEvent.registry.getDefaultKey());
			// TODO Also check for WorldProvider patch - on world load event
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onWorldLoad(WorldEvent.Load worldLoadEvent) {
		onSetupWorldSpecific(worldLoadEvent.getWorld());
	}

	public static void onSetupWorldSpecific(World world) {
		WorldSet worldSet = WAPIReference.getPrimaryWorldSet(world);

		if(worldSet == null)
			return;

		WorldSetInstance setInstance = WAPIReference.getWorldSetInstance(world, worldSet);
		IAtmSystem system = setInstance.getCapability(SAPICapabilities.ATMOSPHERE_SYSTEM, null);
		IAtmHolder holder = world.getCapability(SAPICapabilities.ATMOSPHERE_HOLDER, null);

		AtmSettings atms = MainSettings.INSTANCE.perWorldSetMap.get(worldSet.delegate).atmosphere;

		AtmWorldSettings worldAtm = atms.defaultSettings;
		String worldName = world.provider.getDimensionType().getName();
		if(atms.additionalSettings.containsKey(worldName))
			worldAtm = atms.additionalSettings.get(worldName);

		holder.reevaluateAtmosphere(worldAtm.atmSettings);

		MinecraftForge.EVENT_BUS.post(
				new ApplyWorldSettingsEvent<IAtmSetProvider>(
						IAtmSetProvider.class, system.getSetProvider(),
						worldAtm.atmSettings, world));
	}

	@SubscribeEvent
	public static void onSendEvent(ProviderEvent.Send<ICoordProvider> sendEvent) {
		World world = WAPIReference.getDefaultWorld(false);
		for(WorldSet worldSet : WAPIReference.worldSetList()) {
			WorldSetInstance setInstance = WAPIReference.getWorldSetInstance(world, worldSet);
			IAtmSystem system = setInstance.getCapability(SAPICapabilities.ATMOSPHERE_SYSTEM, null);
			sendEvent.compoundToSend.setTag(worldSet.delegate.name().toString(),
					SAPICapabilities.ATMOSPHERE_SYSTEM.writeNBT(system, null));
		}

		world = sendEvent.world;
		IAtmHolder holder = world.getCapability(SAPICapabilities.ATMOSPHERE_HOLDER, null);
		if(holder == null)
			return;
		sendEvent.compoundToSend.setTag("atmosphere",
				SAPICapabilities.ATMOSPHERE_HOLDER.writeNBT(holder, null));
	}

	@SubscribeEvent
	public static void onReceiveEvent(ProviderEvent.Receive<ICoordProvider> receiveEvent) {
		World world = WAPIReference.getDefaultWorld(true);
		for(WorldSet worldSet : WAPIReference.worldSetList()) {
			NBTBase worldSetData = receiveEvent.receivedCompound.getTag(
					worldSet.delegate.name().toString());
			WorldSetInstance setInstance = WAPIReference.getWorldSetInstance(world, worldSet);
			IAtmSystem system = setInstance.getCapability(SAPICapabilities.ATMOSPHERE_SYSTEM, null);
			SAPICapabilities.ATMOSPHERE_SYSTEM.readNBT(system, null, worldSetData);
		}

		IAtmHolder holder = world.getCapability(SAPICapabilities.ATMOSPHERE_HOLDER, null);
		if(holder == null)
			return;
		SAPICapabilities.ATMOSPHERE_HOLDER.readNBT(holder, null,
				receiveEvent.receivedCompound.getTag("atmosphere"));
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
