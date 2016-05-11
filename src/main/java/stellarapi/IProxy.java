package stellarapi;

import java.io.IOException;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.world.World;
import stellarapi.api.IPerClientReference;
import stellarapi.api.gui.loading.ICombinedProgressUpdate;

/**
 * This extends client reference for the role of the proxy.
 * */
public interface IProxy extends IPerClientReference {
	
	public void preInit(FMLPreInitializationEvent event);
	
    public void load(FMLInitializationEvent event) throws IOException;

    public void postInit(FMLPostInitializationEvent event);
    
}
