package stellarapi;

import java.io.IOException;

import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import stellarapi.api.StellarAPIReference;
import stellarapi.api.mc.SleepWakeManager;
import stellarapi.feature.command.FixedCommandTime;
import stellarapi.feature.network.StellarAPINetworkManager;
import stellarapi.feature.perdimres.PerDimensionResourceRegistry;
import stellarapi.impl.AlarmWakeHandler;
import stellarapi.impl.DefaultDaytimeChecker;
import stellarapi.impl.SunHeightWakeHandler;
import stellarapi.lib.compat.CompatManager;
import stellarapi.lib.config.ConfigManager;

@Mod(modid=StellarAPI.modid, version=StellarAPI.version, guiFactory="stellarapi.config.StellarConfigGuiFactory")
public final class StellarAPI {
	
		public static final String modid = "StellarAPI";
		public static final String version = "@VERSION@";

        // The instance of Stellarium
        @Instance(StellarAPI.modid)
        public static StellarAPI instance;
        
        @SidedProxy(clientSide="stellarapi.ClientProxy", serverSide="stellarapi.CommonProxy")
        public static CommonProxy proxy;
        
        public static Logger logger;
        
    	private static final String wakeCategory = "wake";
        
        private StellarAPIEventHook eventHook = new StellarAPIEventHook();
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
        
        @EventHandler
        public void preInit(FMLPreInitializationEvent event) { 
        	logger = event.getModLog();
        	
    		MinecraftForge.EVENT_BUS.register(this.eventHook);
    		FMLCommonHandler.instance().bus().register(this.tickHandler);
    		FMLCommonHandler.instance().bus().register(this.fmlEventHook);
    		FMLCommonHandler.instance().bus().register(this.networkManager);
    		
    		    		
    		this.config = new Configuration(event.getSuggestedConfigurationFile());
    		this.cfgManager = new ConfigManager(this.config);
    		
    		StellarAPIReference.getDaytimeChecker().registerDaytimeChecker(new DefaultDaytimeChecker());
    		
            cfgManager.register(wakeCategory, StellarAPIReference.getSleepWakeManager());
    		
    		SleepWakeManager sleepWake = StellarAPIReference.getSleepWakeManager();
    		sleepWake.register("wakeBySunHeight", new SunHeightWakeHandler(), true);
    		sleepWake.register("wakeByAlarm", new AlarmWakeHandler(), false);
    		
    		StellarAPIReference.registerPerDimResourceHandler(PerDimensionResourceRegistry.getInstance());
    		
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
        public void serverStarting(FMLServerStartingEvent event) {
        	event.registerServerCommand(new FixedCommandTime());
        }
}