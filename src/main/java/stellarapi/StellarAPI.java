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
import stellarapi.command.CommandLock;
import stellarapi.command.FixedCommandTime;
import stellarapi.compat.CompatManager;
import stellarapi.sync.StellarNetworkEventHandler;
import stellarapi.sync.StellarNetworkFMLEventHandler;
import stellarapi.sync.StellarNetworkManager;

@Mod(modid=StellarAPI.modid, version=StellarAPI.version, guiFactory="stellarapi.config.StellarConfigGuiFactory")
public class StellarAPI {
	
		public static final String modid = "StellarAPI";
		public static final String version = "@VERSION@";

        // The instance of Stellarium
        @Instance(StellarAPI.modid)
        public static StellarAPI instance;
        
        @SidedProxy(clientSide="stellarapi.ClientProxy", serverSide="stellarapi.CommonProxy")
        public static CommonProxy proxy;
        
        public static Logger logger;
        
        private StellarEventHook eventHook = new StellarEventHook();
        private StellarTickHandler tickHandler = new StellarTickHandler();
        private StellarFMLEventHook fmlEventHook = new StellarFMLEventHook();
        private StellarNetworkManager networkManager = new StellarNetworkManager();
        
        public StellarNetworkManager getNetworkManager() {
        	return this.networkManager;
        }
        
        @EventHandler
        public void preInit(FMLPreInitializationEvent event) { 
        	logger = event.getModLog();
        	
        	proxy.preInit(event);
        	
    		MinecraftForge.EVENT_BUS.register(this.eventHook);
    		FMLCommonHandler.instance().bus().register(this.tickHandler);
    		FMLCommonHandler.instance().bus().register(this.fmlEventHook);
    		
    		MinecraftForge.EVENT_BUS.register(new StellarNetworkEventHandler(this.networkManager));
    		FMLCommonHandler.instance().bus().register(new StellarNetworkFMLEventHandler(this.networkManager));
    		
    		CompatManager.getInstance().onPreInit();
        }
        
        @EventHandler
        public void load(FMLInitializationEvent event) throws IOException {
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
        	event.registerServerCommand(new CommandLock());
        	event.registerServerCommand(new FixedCommandTime());
        }
}