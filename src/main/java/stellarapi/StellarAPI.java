package stellarapi;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.Logger;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import stellarapi.api.SAPIReferences;
import stellarapi.api.daywake.SleepWakeManager;
import stellarapi.api.lib.config.DCfgManager;
import stellarapi.example.item.ItemFilteredTelescopeExample;
import stellarapi.example.item.ItemTelescopeExample;
import stellarapi.feature.command.CommandPerDimensionResource;
import stellarapi.feature.command.FixedCommandTime;
import stellarapi.feature.perdimres.PerDimensionResourceRegistry;
import stellarapi.impl.AlarmWakeHandler;
import stellarapi.impl.DefaultDaytimeChecker;
import stellarapi.impl.SunHeightWakeHandler;
import stellarapi.internal.celestial.CelestialRegistry;
import stellarapi.internal.coordinates.CoordRegistry;
import stellarapi.internal.network.SAPINetworkManager;
import stellarapi.lib.compat.CompatManager;
import stellarapi.proxy.IProxy;

@Mod(modid = SAPIReferences.modid, version = SAPIReferences.version,
acceptedMinecraftVersions="[1.12.0, 1.13.0)",
guiFactory = "stellarapi.feature.config.StellarAPIConfigGuiFactory",
dependencies = "required-after:worldsetapi")
public final class StellarAPI {

	// The instance of Stellarium
	@Instance(SAPIReferences.modid)
	public static StellarAPI instance;

	@SidedProxy(clientSide = "stellarapi.proxy.ClientProxy", serverSide = "stellarapi.proxy.CommonProxy")
	public static IProxy proxy;

	public static Logger logger;

	public static Configuration getConfiguration(File configDir, String subName) {
		return new Configuration(new File(new File(configDir, "stellarapi"), subName));
	}

	private static final String wakeCategory = "wake";

	private SAPIEventHook eventHook = new SAPIEventHook();
	private SAPITickHandler tickHandler = new SAPITickHandler();
	private SAPINetworkManager networkManager = new SAPINetworkManager();

	private Configuration config;
	private DCfgManager cfgManager;

	public Item telescope, filteredTelescope;

	public SAPINetworkManager getNetworkManager() {
		return this.networkManager;
	}

	public DCfgManager getCfgManager() {
		return this.cfgManager;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();

		SAPIReferenceHandler reference = new SAPIReferenceHandler();
		reference.initialize();
		SAPIReferences.INSTANCE.setReference(reference);

		MinecraftForge.EVENT_BUS.register(this.eventHook);
		MinecraftForge.EVENT_BUS.register(this.tickHandler);
		MinecraftForge.EVENT_BUS.register(this.fmlEventHook);
		MinecraftForge.EVENT_BUS.register(this.networkManager);

		this.config = getConfiguration(event.getModConfigurationDirectory(), "MainConfig.cfg");
		this.cfgManager = new DCfgManager(this.config);

		SAPIReferences.getDaytimeChecker().registerDaytimeChecker(new DefaultDaytimeChecker());

		cfgManager.register(wakeCategory, SAPIReferences.getSleepWakeManager());

		SleepWakeManager sleepWake = SAPIReferences.getSleepWakeManager();
		sleepWake.register("wakeBySunHeight", new SunHeightWakeHandler(), true);
		sleepWake.register("wakeByAlarm", new AlarmWakeHandler(), false);

		SAPIReferences.registerPerDimResourceHandler(PerDimensionResourceRegistry.INSTANCE);

		SAPIReferences.getEventBus().register(new StellarAPIOwnEventHook());

		this.telescope = new ItemTelescopeExample().setUnlocalizedName("stellarapi.deftelescope")
				.setCreativeTab(CreativeTabs.TOOLS).setMaxStackSize(1);
		telescope.setRegistryName("defaulttelescope");
		GameRegistry.register(telescope);

		this.filteredTelescope = new ItemFilteredTelescopeExample()
				.setUnlocalizedName("stellarapi.deffilteredtelescope").setCreativeTab(CreativeTabs.TOOLS)
				.setMaxStackSize(1);
		filteredTelescope.setRegistryName("defaultfilteredtelescope");
		GameRegistry.register(filteredTelescope);

		proxy.preInit(event);

		CompatManager.getInstance().onPreInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) throws IOException {
		CoordRegistry.onInit();
		CelestialRegistry.onInit();

		//cfgManager.syncFromFile();

		proxy.load(event);

		CompatManager.getInstance().onInit();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);

		CompatManager.getInstance().onPostInit();
	}

	@EventHandler
	public void serverAboutToStart(FMLServerAboutToStartEvent event) {
		;
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandPerDimensionResource());
		event.registerServerCommand(new FixedCommandTime());
	}
}