package stellarapi;

import java.io.IOException;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import stellarapi.api.IClientReference;

/**
 * This extends client reference for the role of the proxy.
 */
public interface IProxy {

	public void preInit(FMLPreInitializationEvent event);

	public void load(FMLInitializationEvent event) throws IOException;

	public void postInit(FMLPostInitializationEvent event);

	void registerTask(Runnable runnable);

}
