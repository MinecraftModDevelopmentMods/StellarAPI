package worldsets;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;

import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import worldsets.api.WAPIReferences;
import worldsets.api.lib.config.ConfigManager;
import worldsets.api.worldset.EnumCPriority;
import worldsets.api.worldset.EnumFlag;
import worldsets.api.worldset.WorldSet;
import worldsets.api.worldset.WorldSetFactory;

@Mod(modid = WAPIReferences.MODID, version = WAPIReferences.VERSION,
guiFactory = "worldsets.WorldSetGuiFactory",
acceptedMinecraftVersions="[1.12.0, 1.13.0)")
@Mod.EventBusSubscriber
public class WorldSetAPI {

	// ********************************************* //
	// **************** Mod Section **************** //
	// ********************************************* //

	@Instance(WAPIReferences.MODID)
	public static WorldSetAPI INSTANCE = null;

	@SidedProxy(clientSide = "worldsets.ClientProxy", serverSide = "worldsets.CommonProxy")
	public static CommonProxy PROXY;

	private WReference reference = new WReference();
	// TODO Remove Stellar API dependency?
	private ConfigManager cfgManager;

	public ConfigManager getConfigManager() {
		return this.cfgManager;
	}

	@EventHandler
	public void onPreInit(FMLPreInitializationEvent event) {
		WAPIReferences.putReference(this.reference);
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		this.cfgManager = new ConfigManager(config);
		cfgManager.register(Configuration.CATEGORY_GENERAL, this.reference);

		reference.registerFactory(new VanillaWorldSetFactory());
		reference.registerFactory(new NamedWorldSetFactory());
	}

	@EventHandler
	public void onInit(FMLInitializationEvent event) {
		cfgManager.syncFromFile();
	}

	// ********************************************* //
	// ****** World/Provider Process Section ******* //
	// ********************************************* //

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void attachWorldCaps(AttachCapabilitiesEvent<World> worldCapsEvent) {
		World world = worldCapsEvent.getObject();
		PerWorldData data = PerWorldData.getWorldSets(world);

		ImmutableList.Builder<WorldSet> appliedWorldSets = ImmutableList.builder();
		for(WorldSet worldSet : WAPIReferences.getAllWorldSets()) {
			if(worldSet.getCondition().test(world)
					|| Sets.newHashSet(worldSet.getExplicitTypes()).contains(world.provider.getDimensionType())) {
				appliedWorldSets.add(worldSet);
			}
		}

		data.populate(appliedWorldSets.build());
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent event) {
		if(event.getModID().equals(WAPIReferences.MODID))
			INSTANCE.cfgManager.syncFromGUI();
	}


	// ********************************************* //
	// ****** WorldSet Implementation Section ****** //
	// ********************************************* //

	private static class VanillaWorldSetFactory extends WorldSetFactory {
		protected VanillaWorldSetFactory() {
			super(WAPIReferences.VANILLA_FACTORY);
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
			this.setHasAtmosphere(EnumFlag.TRUE);
		}
	}

	/** Those resembling overworld */
	private static class OverworldSet extends WorldSet {
		protected OverworldSet() {
			super("OverworldSet", EnumCPriority.MODERATE,
					world -> world.provider.hasSkyLight()
					&& !world.provider.isNether()
					&& world.provider.isSurfaceWorld(),
					DimensionType.OVERWORLD);
			this.setHasSky(EnumFlag.TRUE);
			this.setHasAtmosphere(EnumFlag.TRUE);
		}
	}

	private static class EndSet extends WorldSet {
		protected EndSet() {
			super("EndSet", EnumCPriority.MODERATE, 
					world -> world.provider instanceof WorldProviderEnd
					&& !world.provider.isNether() && !world.provider.isSurfaceWorld(),
					DimensionType.THE_END);
			this.setHasSky(EnumFlag.TRUE);
			this.setHasAtmosphere(EnumFlag.FALSE);
		}
	}

	private static class NetherSet extends WorldSet {
		protected NetherSet() {
			super("NetherSet", EnumCPriority.MODERATE,
					world -> world.provider.isNether(),
					DimensionType.NETHER);
			this.setHasSky(EnumFlag.FALSE);
			this.setHasAtmosphere(EnumFlag.FALSE);
		}
	}


	private static class NamedWorldSetFactory extends WorldSetFactory {
		protected NamedWorldSetFactory() {
			super(WAPIReferences.NAMED_WORLDSET_FACTORY, "Named Worlds");
		}

		@Override
		public void configure(Configuration config, ConfigCategory category) {
			category.setComment("Configure named worldsets.");
			category.setLanguageKey("config.category.worldset.named"); // TODO Edit language file to include this
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