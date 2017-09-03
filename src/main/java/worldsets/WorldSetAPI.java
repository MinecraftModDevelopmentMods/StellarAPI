package worldsets;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.RegistryBuilder;
import stellarapi.api.SAPIReference;
import worldsets.api.EnumCPriority;
import worldsets.api.EnumFlag;
import worldsets.api.WorldSet;
import worldsets.api.WorldSetInstance;

@Mod(modid = WorldSetAPI.modid, version = WorldSetAPI.version,
acceptedMinecraftVersions="[1.11.0, 1.12.0)")
public class WorldSetAPI {

	// ********************************************* //
	// ************** Mod Information ************** //
	// ********************************************* //

	public static final String modid = "worldsetapi";
	public static final String version = "@WSVERSION@";

	// ********************************************* //
	// ************ WorldSet References ************ //
	// ********************************************* //

	public static final ResourceLocation WORLDSETS = new ResourceLocation(SAPIReference.modid, "worldsets");


	@ObjectHolder("overworldtype")
	public static final WorldSet overworldTypeSet = null;

	@ObjectHolder("endtype")
	public static final WorldSet endTypeSet = null;

	@ObjectHolder("nethertype")
	public static final WorldSet NetherTypeSet = null;

	// ********************************************* //
	// ************** Process Section ************** //
	// ********************************************* //

	private static Map<WorldSet, WorldSetInstance> instanceMap = Maps.newHashMap();

	private static IForgeRegistry<WorldSet> worldSetRegistry;

	@SubscribeEvent
	public static void onRegRegister(RegistryEvent.NewRegistry regRegEvent) {
		worldSetRegistry = new RegistryBuilder<WorldSet>()
				.setName(WORLDSETS).setType(WorldSet.class).setIDRange(0, Integer.MAX_VALUE)
				.create();
	}

	@SubscribeEvent
	public static void onRegister(RegistryEvent.Register<WorldSet> regEvent) {
		regEvent.getRegistry().register(new OverworldSet().setRegistryName(new ResourceLocation("overworldtype")));
		regEvent.getRegistry().register(new EndSet().setRegistryName(new ResourceLocation("endtype")));
		regEvent.getRegistry().register(new NetherSet().setRegistryName(new ResourceLocation("nethertype")));
	}

	@SubscribeEvent
	public static void attachWorldCaps(AttachCapabilitiesEvent<World> worldCapsEvent) {
		World world = worldCapsEvent.getObject();
		WorldSet selectedSet = null;
		for(WorldSet worldSet : worldSetRegistry.getValues())
			if(worldSet.containsWorld(world))
				if(selectedSet == null  || selectedSet.getPriority().compareTo(worldSet.getPriority()) == -1)
					selectedSet = worldSet;

		// TODO World Specifics
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