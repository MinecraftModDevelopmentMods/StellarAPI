package worldsets;

import com.google.common.collect.ImmutableList;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import worldsets.api.WAPIReference;
import worldsets.api.event.ProviderEvent;
import worldsets.api.provider.IProviderRegistry;
import worldsets.api.provider.ProviderRegistry;
import worldsets.api.worldset.EnumCPriority;
import worldsets.api.worldset.EnumFlag;
import worldsets.api.worldset.WorldSet;
import worldsets.impl.provider.ProviderRegistries;

@Mod(modid = WAPIReference.modid, version = WAPIReference.version,
acceptedMinecraftVersions="[1.12.0, 1.13.0)")
@Mod.EventBusSubscriber
public class WorldSetAPI {

	// ********************************************* //
	// **************** Mod Section **************** //
	// ********************************************* //

	private static IForgeRegistry<WorldSet> worldSetRegistry;
	private static NetworkHandler netHandler;

	@EventHandler
	public void onPreInit(FMLPreInitializationEvent event) {
		WAPIReference.INSTANCE.putReference(new WReference());
		ProviderRegistry.setHandler(ProviderRegistries.ACTIVE);
		netHandler = new NetworkHandler();
	}

	// ********************************************* //
	// ************** Registry Section ************* //
	// ********************************************* //

	@SubscribeEvent
	public static void onRegRegister(RegistryEvent.NewRegistry regRegEvent) {
		worldSetRegistry = new RegistryBuilder<WorldSet>()
				.setName(WAPIReference.WORLDSETS).setType(WorldSet.class).setIDRange(0, Integer.MAX_VALUE)
				.create();
	}

	@SubscribeEvent
	public static void onRegister(RegistryEvent.Register<WorldSet> regEvent) {
		regEvent.getRegistry().register(new OverworldSet().setRegistryName(new ResourceLocation("overworldtype")));
		regEvent.getRegistry().register(new EndSet().setRegistryName(new ResourceLocation("endtype")));
		regEvent.getRegistry().register(new NetherSet().setRegistryName(new ResourceLocation("nethertype")));
	}

	// ********************************************* //
	// ****** World/Provider Process Section ******* //
	// ********************************************* //

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void attachWorldCaps(AttachCapabilitiesEvent<World> worldCapsEvent) {
		World world = worldCapsEvent.getObject();
		PerWorldData data = PerWorldData.getWorldSets(world);

		if(WAPIReference.isDefaultWorld(world)) {
			// Loads the global world set data as the first.
			GlobalData globalData = GlobalData.getWorldSets(world);
		}

		ImmutableList.Builder<WorldSet> appliedWorldSets = ImmutableList.builder();
		for(WorldSet worldSet : worldSetRegistry.getValues())
			if(worldSet.containsWorld(world))
				appliedWorldSets.add(worldSet);
		data.populate(appliedWorldSets.build());
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void loadWorld(WorldEvent.Load worldLoadEvent) {
		World world = worldLoadEvent.getWorld();
		if(WAPIReference.isDefaultWorld(world)) {
			// fires apply settings event
			for(IProviderRegistry<?> registry : ProviderRegistry.getProviderRegistryMap().values()) {
				ProviderEvent.ApplySettings<?> event = new ProviderEvent.ApplySettings(registry, world.isRemote);
				MinecraftForge.EVENT_BUS.post(event);
			}

			// fires completion event
			for(IProviderRegistry<?> registry : ProviderRegistry.getProviderRegistryMap().values()) {
				ProviderEvent.Complete<?> event = new ProviderEvent.Complete(registry, world.isRemote, true);
				MinecraftForge.EVENT_BUS.post(event);
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent loggedInEvent) {
		netHandler.onPlayerSync(loggedInEvent.player);
	}
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent dimChangeEvent) {
		netHandler.onPlayerSync(dimChangeEvent.player);
	}

	// ********************************************* //
	// ****** WorldSet Implementation Section ****** //
	// ********************************************* //

	/** Those resembling overworld */
	private static class OverworldSet extends WorldSet {
		protected OverworldSet() {
			super(EnumCPriority.MODERATE, DimensionType.OVERWORLD);
			this.setHasSky(EnumFlag.TRUE);
			this.setHasAtmosphere(EnumFlag.TRUE);
		}

		@Override
		public boolean containsWorld(World world) {
			// TODO correct detection of overworld-type worlds
			return world.provider.hasSkyLight()
					&& !world.provider.isNether()
					&& world.provider.isSurfaceWorld();
		}
	}

	private static class EndSet extends WorldSet {
		protected EndSet() {
			super(EnumCPriority.MODERATE, DimensionType.THE_END);
			this.setHasSky(EnumFlag.TRUE);
			this.setHasAtmosphere(EnumFlag.FALSE);
		}

		@Override
		public boolean containsWorld(World world) {
			return world.provider instanceof WorldProviderEnd
					&& !world.provider.isNether() && !world.provider.isSurfaceWorld();
		}
	}

	private static class NetherSet extends WorldSet {
		protected NetherSet() {
			super(EnumCPriority.MODERATE, DimensionType.NETHER);
			this.setHasSky(EnumFlag.FALSE);
			this.setHasAtmosphere(EnumFlag.FALSE);
		}

		@Override
		public boolean containsWorld(World world) {
			return world.provider.isNether();
		}
	}
}