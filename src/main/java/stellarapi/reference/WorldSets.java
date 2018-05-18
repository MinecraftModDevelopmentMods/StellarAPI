package stellarapi.reference;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;

import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import stellarapi.api.IReference;
import stellarapi.api.SAPIReferences;
import stellarapi.api.world.worldset.EnumCPriority;
import stellarapi.api.world.worldset.EnumFlag;
import stellarapi.api.world.worldset.WorldSet;
import stellarapi.api.world.worldset.WorldSetFactory;

public class WorldSets {
	public void onPreInit(IReference reference) {
		reference.registerFactory(new VanillaWorldSetFactory());
		reference.registerFactory(new NamedWorldSetFactory());
	}

	// ********************************************* //
	// ****** World/Provider Process Section ******* //
	// ********************************************* //

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void attachWorldCaps(AttachCapabilitiesEvent<World> worldCapsEvent) {
		World world = worldCapsEvent.getObject();
		PerWorldData data = PerWorldData.getWorldSets(world);

		ImmutableList.Builder<WorldSet> appliedWorldSets = ImmutableList.builder();
		for(WorldSet worldSet : SAPIReferences.getAllWorldSets()) {
			if(worldSet.getCondition().test(world)
					|| Sets.newHashSet(worldSet.getExplicitTypes()).contains(world.provider.getDimensionType())) {
				appliedWorldSets.add(worldSet);
			}
		}

		data.populate(appliedWorldSets.build());
	}


	// ********************************************* //
	// ****** WorldSet Implementation Section ****** //
	// ********************************************* //

	private static class VanillaWorldSetFactory extends WorldSetFactory {
		protected VanillaWorldSetFactory() {
			super(SAPIReferences.VANILLA_FACTORY);
		}

		@Override
		public WorldSet[] generate(ConfigCategory category) {
			return new WorldSet[] { new ExactOverworldSet(), new OverworldSet(),
					new EndSet(), new NetherSet() };
		}

		@Override
		public void configure(Configuration config, ConfigCategory category) { }
	}

	/** World set for exact overworld */
	private static class ExactOverworldSet extends WorldSet {
		protected ExactOverworldSet() {
			super("Overworld", EnumCPriority.HIGH,
					world -> false,
					DimensionType.OVERWORLD);
			this.setHasSky(EnumFlag.TRUE);
		}
	}

	/** Those resembling overworld */
	private static class OverworldSet extends WorldSet {
		protected OverworldSet() {
			super("Overworld_Like", EnumCPriority.MODERATE,
					world -> world.provider.hasSkyLight()
					&& !world.provider.isNether()
					&& world.provider.isSurfaceWorld(),
					DimensionType.OVERWORLD);
			this.setHasSky(EnumFlag.TRUE);
		}
	}

	private static class EndSet extends WorldSet {
		protected EndSet() {
			super("EndSet", EnumCPriority.MODERATE, 
					world -> world.provider instanceof WorldProviderEnd
					&& !world.provider.isNether() && !world.provider.isSurfaceWorld(),
					DimensionType.THE_END);
			this.setHasSky(EnumFlag.TRUE);
		}
	}

	private static class NetherSet extends WorldSet {
		protected NetherSet() {
			super("NetherSet", EnumCPriority.MODERATE,
					world -> world.provider.isNether(),
					DimensionType.NETHER);
			this.setHasSky(EnumFlag.FALSE);
		}
	}


	private static class NamedWorldSetFactory extends WorldSetFactory {
		protected NamedWorldSetFactory() {
			super(SAPIReferences.NAMED_WORLDSET_FACTORY, "Named Worlds");
		}

		@Override
		public void configure(Configuration config, ConfigCategory category) {
			category.setComment("Configure named worldsets.");
			category.setLanguageKey("config.category.worldset.named");
			category.setRequiresMcRestart(true);

			Property worldNames = config.get(category.getQualifiedName(), "World_Names", new String[] { });
			worldNames.setComment("This list specifies named worldsets. "
					+ "Each worldset contains one world name in this list.");
			worldNames.setRequiresMcRestart(true);
			worldNames.setLanguageKey("config.property.worldset.namelist");
		}

		@Override
		public WorldSet[] generate(ConfigCategory category) {
			String[] worldNames = category.get("World_Names").getStringList();
			WorldSet[] worldSets = new WorldSet[worldNames.length];
			for(int i = 0; i < worldNames.length; i++) {
				worldSets[i] = new NamedWorldSet(worldNames[i]);
			}
			return worldSets;
		}
	}

	private static class NamedWorldSet extends WorldSet {
		protected NamedWorldSet(String name) {
			super(name, EnumCPriority.HIGH, world -> world.provider.getDimensionType().getName().equals(name));
		}
	}
}