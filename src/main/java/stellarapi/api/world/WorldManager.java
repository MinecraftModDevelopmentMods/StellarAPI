package stellarapi.api.world;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.RegistryBuilder;
import stellarapi.StellarAPI;

@Mod.EventBusSubscriber
public class WorldManager {

	public static final ResourceLocation WORLDSETS = new ResourceLocation(StellarAPI.modid, "worldsets");

	@ObjectHolder("overworldtype")
	public static final WorldSet overworldTypeSet = null;

	@ObjectHolder("endtype")
	public static final WorldSet enderSet = null;

	private static IForgeRegistry<WorldSet> worldSetRegistry;

	@SubscribeEvent
	public static void onRegRegister(RegistryEvent.NewRegistry regRegEvent) {
		worldSetRegistry = new RegistryBuilder<WorldSet>()
				.setName(WORLDSETS).setType(WorldSet.class).setIDRange(0, Integer.MAX_VALUE)
				.create();
	}

	@SubscribeEvent
	public void onRegister(RegistryEvent.Register<WorldSet> regEvent) {
		regEvent.getRegistry().register(new OverworldSet().setRegistryName(new ResourceLocation("overworldtype")));
		regEvent.getRegistry().register(new EndSet().setRegistryName(new ResourceLocation("endtype")));
	}

	/** Those resembling overworld */
	private static class OverworldSet extends WorldSet {
		public OverworldSet() {
			super(EnumCPriority.MODERATE, DimensionType.OVERWORLD);
			this.setHasSky(EnumFlag.TRUE);
			this.setHasAtmosphere(EnumFlag.TRUE);
		}

		@Override
		public boolean containsWorld(World world) {
			// TODO is this right?
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
}