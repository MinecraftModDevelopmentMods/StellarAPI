package stellarapi.internal.world;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import stellarapi.api.SAPICapabilities;
import stellarapi.api.SAPIReferences;
import stellarapi.api.SAPIRegistries;
import stellarapi.api.helper.WorldProviderReplaceHelper;
import stellarapi.api.world.IWorldEffectHandler;
import stellarapi.api.world.IWorldProviderReplacer;
import worldsets.api.WAPIReference;
import worldsets.api.event.ProviderEvent;
import worldsets.api.provider.IProviderRegistry;
import worldsets.api.provider.ProviderBuilder;
import worldsets.api.worldset.WorldSet;

@Mod.EventBusSubscriber(modid = SAPIReferences.modid)
public class WorldRegistry {
	public static final ResourceLocation DEFAULT_ID = new ResourceLocation("default_replacer");

	private static IProviderRegistry<IWorldProviderReplacer> providerRegistry;

	@SubscribeEvent
	public static void onProvRegistryEvent(ProviderEvent.NewRegistry pregRegEvent) {
		providerRegistry = new ProviderBuilder<IWorldProviderReplacer>()
				.setName(SAPIRegistries.PROVIDER_REPLACER).setType(IWorldProviderReplacer.class)
				// TODO AtmRegistry .setDefaultKey(key)
				.create();
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onWorldLoad(WorldEvent.Load worldLoadEvent) {
		World world = worldLoadEvent.getWorld();
		WorldSet worldSet = WAPIReference.getPrimaryWorldSet(world);

		if(worldSet == null)
			return;

		WorldProvider custom = customWorldProvider(world);
		if(custom != null)
			WorldProviderReplaceHelper.patchWorldProviderWith(world, custom);
	}

	public static boolean customApplicable(World world) {
		for(IWorldProviderReplacer replacer : providerRegistry)
			if(replacer.accept(world, world.provider))
				return true;
		return false;
	}

	private static WorldProvider customWorldProvider(World world) {
		WorldProvider wrapped = world.provider;
		for(IWorldProviderReplacer replacer : providerRegistry) {
			if(replacer.accept(world, wrapped)) {
				IWorldEffectHandler handler = world.getCapability(SAPICapabilities.WORLD_EFFECT_HANDLER, null);
				return replacer.createWorldProvider(world, wrapped, null);
			}
		}
		return null;
	}

}
