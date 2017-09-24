package stellarapi.proxy;

import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * This extends client reference for the role of the proxy.
 */
public interface IProxy {

	public void preInit(FMLPreInitializationEvent event);

	public void load(FMLInitializationEvent event) throws IOException;

	public void postInit(FMLPostInitializationEvent event);

	void registerClientTask(Runnable runnable);

	public World getClientWorld();
	public EntityPlayer getClientPlayer();
	public Entity getRenderViewEntity();

}
