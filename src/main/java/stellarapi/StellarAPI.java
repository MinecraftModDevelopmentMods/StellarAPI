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
import stellarapi.api.StellarAPIReference;
import stellarapi.api.daywake.SleepWakeManager;
import stellarapi.api.lib.config.ConfigManager;
import stellarapi.example.item.ItemFilteredTelescopeExample;
import stellarapi.example.item.ItemTelescopeExample;
import stellarapi.feature.command.CommandPerDimensionResource;
import stellarapi.feature.command.FixedCommandTime;
import stellarapi.feature.network.StellarAPINetworkManager;
import stellarapi.feature.perdimres.PerDimensionResourceRegistry;
import stellarapi.impl.AlarmWakeHandler;
import stellarapi.impl.DefaultDaytimeChecker;
import stellarapi.impl.SunHeightWakeHandler;
import stellarapi.lib.compat.CompatManager;
import stellarapi.reference.StellarAPIReferenceHandler;

@Mod(modid = StellarAPI.modid, version = StellarAPI.version,
acceptedMinecraftVersions="[1.9.4, 1.11.0)",
guiFactory = "stellarapi.feature.config.StellarAPIConfigGuiFactory")
public final class StellarAPI {

	public static final String modid = "StellarAPI";
	public static final String version = "@VERSION@";
	public static final String apiid = "StellarAPI|API";

	// The instance of Stellarium
	@Instance(StellarAPI.modid)
	public static StellarAPI instance;

	@SidedProxy(clientSide = "stellarapi.ClientProxy", serverSide = "stellarapi.CommonProxy")
	public static IProxy proxy;

	public static Logger logger;

	public static Configuration getConfiguration(File configDir, String subName) {
		return new Configuration(new File(new File(configDir, "stellarapi"), subName));
	}

	private static final String wakeCategory = "wake";

	private StellarAPIForgeEventHook eventHook = new StellarAPIForgeEventHook();
	private StellarAPITickHandler tickHandler = new StellarAPITickHandler();
	private StellarAPIFMLEventHook fmlEventHook = new StellarAPIFMLEventHook();
	private StellarAPINetworkManager networkManager = new StellarAPINetworkManager();

	private Configuration config;
	private ConfigManager cfgManager;

	public Item telescope, filteredTelescope;

	public StellarAPINetworkManager getNetworkManager() {
		return this.networkManager;
	}

	public ConfigManager getCfgManager() {
		return this.cfgManager;
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();

		StellarAPIReferenceHandler reference = new StellarAPIReferenceHandler();
		reference.initialize();
		StellarAPIReference.setReference(reference);

		MinecraftForge.EVENT_BUS.register(this.eventHook);
		MinecraftForge.EVENT_BUS.register(this.tickHandler);
		MinecraftForge.EVENT_BUS.register(this.fmlEventHook);
		MinecraftForge.EVENT_BUS.register(this.networkManager);

		this.config = getConfiguration(event.getModConfigurationDirectory(), "MainConfig.cfg");
		this.cfgManager = new ConfigManager(this.config);

		StellarAPIReference.getDaytimeChecker().registerDaytimeChecker(new DefaultDaytimeChecker());

		cfgManager.register(wakeCategory, StellarAPIReference.getSleepWakeManager());

		SleepWakeManager sleepWake = StellarAPIReference.getSleepWakeManager();
		sleepWake.register("wakeBySunHeight", new SunHeightWakeHandler(), true);
		sleepWake.register("wakeByAlarm", new AlarmWakeHandler(), false);

		StellarAPIReference.registerPerDimResourceHandler(PerDimensionResourceRegistry.getInstance());

		StellarAPIReference.getEventBus().register(new StellarAPIOwnEventHook());

		this.telescope = new ItemTelescopeExample().setUnlocalizedName("stellarapi.deftelescope")
				.setCreativeTab(CreativeTabs.TOOLS).setMaxStackSize(1);
		GameRegistry.registerItem(telescope, "defaulttelescope");

		this.filteredTelescope = new ItemFilteredTelescopeExample()
				.setUnlocalizedName("stellarapi.deffilteredtelescope").setCreativeTab(CreativeTabs.TOOLS)
				.setMaxStackSize(1);
		GameRegistry.registerItem(filteredTelescope, "defaultfilteredtelescope");

		proxy.preInit(event);

		CompatManager.getInstance().onPreInit();
	}

	@EventHandler
	public void load(FMLInitializationEvent event) throws IOException {
		cfgManager.syncFromFile();

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