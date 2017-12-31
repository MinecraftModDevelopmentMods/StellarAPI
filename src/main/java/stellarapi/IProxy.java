package stellarapi;

import java.io.IOException;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * This extends client reference for the role of the proxy.
 */
public interface IProxy {

	public void preInit(FMLPreInitializationEvent event);

	public void registerModels();

	public void load(FMLInitializationEvent event) throws IOException;

	public void postInit(FMLPostInitializationEvent event);

	public void registerTask(Runnable runnable);

	public World getClientWorld();

}
