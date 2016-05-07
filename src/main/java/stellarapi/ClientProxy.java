package stellarapi;

import java.io.IOException;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import stellarapi.feature.gui.overlay.OverlayHandler;

public class ClientProxy extends CommonProxy implements IProxy {
	
	private static final String clientConfigCategory = "clientconfig";
	private static final String clientConfigOpticsCategory = "clientconfig.optics";
	
	private OverlayHandler overlay;
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		
		this.overlay = new OverlayHandler();
		MinecraftForge.EVENT_BUS.register(new StellarAPIClientForgeEventHook(this.overlay));
		FMLCommonHandler.instance().bus().register(new StellarAPIClientFMLEventHook(this.overlay));
	}

	@Override
	public void load(FMLInitializationEvent event) throws IOException {
		super.load(event);
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
		
		overlay.initialize(Minecraft.getMinecraft());
	}
	
	@Override
	public World getDefWorld() {
		return Minecraft.getMinecraft().theWorld;
	}
	
	@Override
	public World getDefWorld(boolean isRemote) {
		return isRemote? this.getDefWorld() : super.getDefWorld();
	}
}
