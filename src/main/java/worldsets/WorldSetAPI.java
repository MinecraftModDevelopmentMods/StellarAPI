package worldsets;

import com.google.common.collect.ImmutableList;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import worldsets.api.WAPIReferences;
import worldsets.api.worldset.EnumCPriority;
import worldsets.api.worldset.EnumFlag;
import worldsets.api.worldset.WorldSet;

@Mod(modid = WAPIReferences.modid, version = WAPIReferences.version,
acceptedMinecraftVersions="[1.12.0, 1.13.0)")
@Mod.EventBusSubscriber
public class WorldSetAPI {

	// ********************************************* //
	// **************** Mod Section **************** //
	// ********************************************* //

	@SidedProxy(clientSide = "worldsets.ClientProxy", serverSide = "worldsets.CommonProxy")
	public static CommonProxy proxy;

	private static IForgeRegistry<WorldSet> worldSetRegistry;

	@EventHandler
	public void onPreInit(FMLPreInitializationEvent event) {
		WAPIReferences.putReference(new WReference());
	}

	// ********************************************* //
	// ************** Registry Section ************* //
	// ********************************************* //

	@SubscribeEvent
	public static void onRegRegister(RegistryEvent.NewRegistry regRegEvent) {
		worldSetRegistry = new RegistryBuilder<WorldSet>()
				.setName(WAPIReferences.WORLDSETS).setType(WorldSet.class).setIDRange(0, Integer.MAX_VALUE - 1)
				.create();
	}

	@SubscribeEvent
	public static void onRegister(RegistryEvent.Register<WorldSet> regEvent) {
		IForgeRegistry<WorldSet> registry = regEvent.getRegistry();

		registry.register(new ExactOverworldSet().setRegistryName(new ResourceLocation("overworld")));
		registry.register(new OverworldSet().setRegistryName(new ResourceLocation("overworldtype")));
		registry.register(new EndSet().setRegistryName(new ResourceLocation("endtype")));
		registry.register(new NetherSet().setRegistryName(new ResourceLocation("nethertype")));
	}

	// ********************************************* //
	// ****** World/Provider Process Section ******* //
	// ********************************************* //

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void attachWorldCaps(AttachCapabilitiesEvent<World> worldCapsEvent) {
		World world = worldCapsEvent.getObject();
		PerWorldData data = PerWorldData.getWorldSets(world);

		ImmutableList.Builder<WorldSet> appliedWorldSets = ImmutableList.builder();
		for(WorldSet worldSet : worldSetRegistry.getValues())
			if(worldSet.containsWorld(world))
				appliedWorldSets.add(worldSet);
		data.populate(appliedWorldSets.build());
	}


	// ********************************************* //
	// ****** WorldSet Implementation Section ****** //
	// ********************************************* //

	/** World set for exact overworld */
	private static class ExactOverworldSet extends WorldSet {
		protected ExactOverworldSet() {
			super(EnumCPriority.HIGH, DimensionType.OVERWORLD);
			this.setHasSky(EnumFlag.TRUE);
			this.setHasAtmosphere(EnumFlag.TRUE);
		}

		@Override
		public boolean containsWorld(World world) {
			return world.provider.getDimension() == 0
					&& world.provider.hasSkyLight()
					&& !world.provider.isNether()
					&& world.provider.isSurfaceWorld();
		}
	}

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