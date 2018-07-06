package stellarapi;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.relauncher.Side;
import stellarapi.api.SAPIReferences;
import stellarapi.api.daywake.SleepWakeManager;
import stellarapi.api.lib.config.ConfigManager;
import stellarapi.example.world.WorldReplacerEnd;
import stellarapi.feature.celestial.tweakable.SAPICelestialPack;
import stellarapi.feature.command.CommandPerDimensionResource;
import stellarapi.feature.command.FixedCommandTime;
import stellarapi.feature.config.SAPIConfigHandler;
import stellarapi.feature.network.StellarAPINetworkManager;
import stellarapi.feature.perdimres.PerDimensionResourceRegistry;
import stellarapi.impl.celestial.DefaultCelestialPack;
import stellarapi.impl.daytime.DefaultDaytimeChecker;
import stellarapi.impl.wake.AlarmWakeHandler;
import stellarapi.impl.wake.SunHeightWakeHandler;
import stellarapi.lib.compat.CompatManager;
import stellarapi.reference.SAPIReferenceHandler;
import stellarapi.reference.WorldSets;

@Mod(modid = SAPIReferences.MODID, version = SAPIReferences.VERSION,
acceptedMinecraftVersions="[1.12.0, 1.13.0)",
guiFactory = "stellarapi.feature.config.StellarAPIConfigGuiFactory")
public final class StellarAPI {
	// FIXME License change

	// The instance of Stellar API
	@Mod.Instance(SAPIReferences.MODID)
	public static StellarAPI INSTANCE = null;

	@SidedProxy(clientSide = "stellarapi.ClientProxy", serverSide = "stellarapi.CommonProxy")
	public static IProxy PROXY;

	public static Configuration getConfiguration(File configDir, String subName) {
		return new Configuration(new File(new File(configDir, "stellarapi"), subName));
	}

	private static final String wakeCategory = "wake";
	private static final String worldSetCategory = "worldsets";
	private static final String worldCfgCategory = "worldconfig";

	private Logger logger;

	private SAPICommonEventHook eventHook = new SAPICommonEventHook();
	private SAPITickHandler tickHandler = new SAPITickHandler();
	private StellarAPINetworkManager networkManager = new StellarAPINetworkManager();

	private Configuration config;
	private ConfigManager cfgManager;

	private SAPIConfigHandler packCfgHandler;

	public Logger getLogger() {
		return this.logger;
	}

	public StellarAPINetworkManager getNetworkManager() {
		return this.networkManager;
	}

	public ConfigManager getCfgManager() {
		return this.cfgManager;
	}

	public SAPIConfigHandler getPackCfgHandler() {
		return this.packCfgHandler;
	}

	@SuppressWarnings("deprecation")
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();

		SAPIReferenceHandler reference = new SAPIReferenceHandler();
		reference.initialize();
		SAPIReferences.putReference(reference);

		MinecraftForge.EVENT_BUS.register(this.eventHook);
		MinecraftForge.EVENT_BUS.register(this.tickHandler);
		MinecraftForge.EVENT_BUS.register(this.networkManager);
		MinecraftForge.EVENT_BUS.register(SAPIItems.INSTANCE);

		this.config = getConfiguration(event.getModConfigurationDirectory(), "MainConfig.cfg");
		this.cfgManager = new ConfigManager(this.config);

		SAPIReferences.getDaytimeChecker().registerDaytimeChecker(new DefaultDaytimeChecker());

		cfgManager.register(wakeCategory, SAPIReferences.getSleepWakeManager());

		SleepWakeManager sleepWake = SAPIReferences.getSleepWakeManager();
		sleepWake.register("wakeBySunHeight", new SunHeightWakeHandler(), true);
		sleepWake.register("wakeByAlarm", new AlarmWakeHandler(), false);

		cfgManager.register(worldSetCategory, reference);
		WorldSets worldSets = new WorldSets();
		worldSets.onPreInit(reference);
		MinecraftForge.EVENT_BUS.register(worldSets);

		// Registers end provider replacer.
		SAPIReferences.registerWorldProviderReplacer(WorldReplacerEnd.INSTANCE);

		SAPIReferences.registerPerDimResourceHandler(PerDimensionResourceRegistry.getInstance());

		PROXY.preInit(event);

		CompatManager.getInstance().onPreInit();
	}

	@Mod.EventHandler
	public void load(FMLInitializationEvent event) throws IOException {
		// First sync from file
		cfgManager.syncFromFile();

		// Second sync from file
		this.packCfgHandler = new SAPIConfigHandler();
		cfgManager.register(worldCfgCategory, this.packCfgHandler);
		cfgManager.syncFromFile();
		// This is for client
		SAPIReferences.registerPack(DefaultCelestialPack.INSTANCE);
		SAPIReferences.registerPack(SAPICelestialPack.INSTANCE);

		PROXY.load(event);

		CompatManager.getInstance().onInit();
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		PROXY.postInit(event);

		CompatManager.getInstance().onPostInit();
	}

	@Mod.EventHandler
	public void serverAboutToStart(FMLServerAboutToStartEvent event) {
		;
	}

	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandPerDimensionResource());
		event.registerServerCommand(new FixedCommandTime());
	}


	private boolean existOnServer = true;

	public boolean existOnServer() { 
		return this.existOnServer;
	}

	@NetworkCheckHandler
	public boolean checkNetwork(Map<String, String> modsNversions, Side from) {
		if(from.isServer())
			this.existOnServer = modsNversions.containsKey(SAPIReferences.MODID);

		return true;
	}
}