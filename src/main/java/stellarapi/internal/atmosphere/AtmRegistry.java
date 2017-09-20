package stellarapi.internal.atmosphere;

import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import stellarapi.api.SAPICapabilities;
import stellarapi.api.SAPIRegistries;
import stellarapi.api.atmosphere.IAtmHolder;
import stellarapi.api.atmosphere.IAtmProvider;
import stellarapi.api.atmosphere.IAtmSetProvider;
import stellarapi.api.coordinates.ICoordProvider;
import stellarapi.api.event.settings.ApplyProviderIDEvent;
import stellarapi.api.event.settings.ApplySettingsEvent;
import stellarapi.internal.settings.AtmSettings;
import stellarapi.internal.settings.AtmWorldSettings;
import stellarapi.internal.settings.MainSettings;
import stellarapi.internal.world.WorldRegistry;
import worldsets.api.WAPIReference;
import worldsets.api.event.ProviderEvent;
import worldsets.api.provider.IProviderRegistry;
import worldsets.api.provider.ProviderBuilder;
import worldsets.api.worldset.WorldSet;

public class AtmRegistry {
	public static final ResourceLocation DEFAULT_ID = new ResourceLocation("overworld_atmosphere");

	private static IProviderRegistry<IAtmProvider> providerRegistry;

	@SubscribeEvent
	public static void onProvRegistryEvent(ProviderEvent.NewRegistry pregRegEvent) {
		providerRegistry = new ProviderBuilder<IAtmProvider>()
				.setName(SAPIRegistries.ATMOSPHERE).setType(IAtmProvider.class)
				// TODO AtmRegistry .setDefaultKey(key)
				.add(new CreateCallback()).add(new AddCallback())
				.add(new SubstitutionCallback()).add(new ClearCallback())
				.create();
	}


	@SubscribeEvent
	public static void onApplySettingsEvent(ProviderEvent.ApplySettings<ICoordProvider> applySettingsEvent) {
		World world = applySettingsEvent.world;
		WorldSet worldSet = WAPIReference.getPrimaryWorldSet(world);
		if(worldSet == null)
			return;

		IAtmHolder holder = world.getCapability(SAPICapabilities.ATMOSPHERE_HOLDER, null);

		// Determine ID from settings
		ResourceLocation settingsID = MainSettings.INSTANCE.perWorldSetMap.get(worldSet.delegate).atmosphere.getCurrentProviderID();
		ResourceLocation prevID = holder.getProviderID() != null? holder.getProviderID() : settingsID;
		ApplyProviderIDEvent.Atmosphere event = new ApplyProviderIDEvent.Atmosphere(world, worldSet, prevID, settingsID);
		MinecraftForge.EVENT_BUS.post(event);
		if(holder.getProviderID() == null || prevID != event.resultID) {
			holder.setProviderID(event.resultID);
		}

		// TODO Atmosphere think about this; Does vanilla mean simpler atmosphere?
		// Determine system ID, and handle vanilla
		// Client Placeholder || Unpatched WorldProvider Handling
		if(world.isRemote || !WorldRegistry.customApplicable(world))
			holder.setProviderID(applySettingsEvent.registry.getDefaultKey());


		AtmSettings atms = MainSettings.INSTANCE.perWorldSetMap.get(worldSet.delegate).atmosphere;
		AtmWorldSettings worldAtm = atms.defaultSettings;
		String worldName = world.provider.getDimensionType().getName();
		if(atms.additionalSettings.containsKey(worldName))
			worldAtm = atms.additionalSettings.get(worldName);

		// Evaluates the atmosphere with this settings
		holder.evaluateAtmosphere(worldAtm.atmSettings);

		// Apply the atmospheric settings, mainly on the local atmosphere.
		MinecraftForge.EVENT_BUS.post(
				new ApplySettingsEvent<IAtmSetProvider>(
						IAtmSetProvider.class, holder.getSetProvider(),
						worldAtm.atmSettings, world));
	}

	@SubscribeEvent
	public static void onCompleteEvent(ProviderEvent.Complete<ICoordProvider> completeEvent) {
		World world = completeEvent.world;
		WorldSet worldSet = WAPIReference.getPrimaryWorldSet(world);
		if(worldSet == null)
			return;

		// Nothing to do here; It's not dependent to others
	}

	@SubscribeEvent
	public static void onSendEvent(ProviderEvent.Send<ICoordProvider> sendEvent) {
		World world = sendEvent.world;
		WorldSet worldSet = WAPIReference.getPrimaryWorldSet(world);
		if(worldSet == null)
			return;

		// Write provider ID and atmosphere
		IAtmHolder holder = world.getCapability(SAPICapabilities.ATMOSPHERE_HOLDER, null);
		sendEvent.compoundToSend.setTag("atmosphere",
				SAPICapabilities.ATMOSPHERE_HOLDER.writeNBT(holder, null));
	}

	@SubscribeEvent
	public static void onReceiveEvent(ProviderEvent.Receive<ICoordProvider> receiveEvent) {
		World world = receiveEvent.world;
		WorldSet worldSet = WAPIReference.getPrimaryWorldSet(world);
		if(worldSet == null)
			return;

		// Read provider ID and atmosphere
		IAtmHolder holder = world.getCapability(SAPICapabilities.ATMOSPHERE_HOLDER, null);
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
				throw new IllegalArgumentException(
						String.format("Registered with duplicate name : %s", obj.getReadableName()));
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
				throw new IllegalArgumentException(
						String.format("Registered with duplicate name : %s", replacement.getReadableName()));
			}

			nameMap.put(replacement.getReadableName(), key);
		}
	}
}
