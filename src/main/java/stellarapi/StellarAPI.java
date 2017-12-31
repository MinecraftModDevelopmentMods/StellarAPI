package stellarapi;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.Logger;

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
import stellarapi.api.SAPIReferences;
import stellarapi.api.daywake.SleepWakeManager;
import stellarapi.api.lib.config.ConfigManager;
import stellarapi.feature.command.CommandPerDimensionResource;
import stellarapi.feature.command.FixedCommandTime;
import stellarapi.feature.network.StellarAPINetworkManager;
import stellarapi.feature.perdimres.PerDimensionResourceRegistry;
import stellarapi.impl.AlarmWakeHandler;
import stellarapi.impl.DefaultDaytimeChecker;
import stellarapi.impl.SunHeightWakeHandler;
import stellarapi.lib.compat.CompatManager;
import stellarapi.reference.StellarAPIReferenceHandler;

@Mod(modid = SAPIReferences.MODID, version = SAPIReferences.VERSION,
acceptedMinecraftVersions="[1.12.0, 1.13.0)",
guiFactory = "stellarapi.feature.config.StellarAPIConfigGuiFactory")
public final class StellarAPI {
	// FIXME License change

	// The instance of Stellarium
	@Instance(SAPIReferences.MODID)
	public static StellarAPI INSTANCE;

	@SidedProxy(clientSide = "stellarapi.ClientProxy", serverSide = "stellarapi.CommonProxy")
	public static IProxy PROXY;

	public static Logger LOGGER;

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

	public StellarAPINetworkManager getNetworkManager() {
		return this.networkManager;
	}

	public ConfigManager getCfgManager() {
		return this.cfgManager;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		LOGGER = event.getModLog();

		StellarAPIReferenceHandler reference = new StellarAPIReferenceHandler();
		reference.initialize();
		SAPIReferences.putReference(reference);

		MinecraftForge.EVENT_BUS.register(this.eventHook);
		MinecraftForge.EVENT_BUS.register(this.tickHandler);
		MinecraftForge.EVENT_BUS.register(this.fmlEventHook);
		MinecraftForge.EVENT_BUS.register(this.networkManager);
		MinecraftForge.EVENT_BUS.register(StellarRegistries.INSTANCE);

		this.config = getConfiguration(event.getModConfigurationDirectory(), "MainConfig.cfg");
		this.cfgManager = new ConfigManager(this.config);

		SAPIReferences.getDaytimeChecker().registerDaytimeChecker(new DefaultDaytimeChecker());

		cfgManager.register(wakeCategory, SAPIReferences.getSleepWakeManager());

		SleepWakeManager sleepWake = SAPIReferences.getSleepWakeManager();
		sleepWake.register("wakeBySunHeight", new SunHeightWakeHandler(), true);
		sleepWake.register("wakeByAlarm", new AlarmWakeHandler(), false);

		SAPIReferences.registerPerDimResourceHandler(PerDimensionResourceRegistry.getInstance());

		SAPIReferences.getEventBus().register(new StellarAPIOwnEventHook());

		PROXY.preInit(event);

		CompatManager.getInstance().onPreInit();
	}

	@EventHandler
	public void load(FMLInitializationEvent event) throws IOException {
		cfgManager.syncFromFile();

		PROXY.load(event);

		CompatManager.getInstance().onInit();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		PROXY.postInit(event);

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