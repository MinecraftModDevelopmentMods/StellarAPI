package stellarapi;

import java.io.IOException;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import stellarapi.util.math.Spmath;

public class CommonProxy implements IProxy {
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		StellarAPI.logger.info("Initializing Math class...");
		//Initializing Spmath
		Spmath.Initialize();
		StellarAPI.logger.info("[StellarAPI]: "+"Math Class Initialized!");
	}

	@Override
	public void load(FMLInitializationEvent event) throws IOException { }

	@Override
	public void postInit(FMLPostInitializationEvent event) { }
	
	@Override
	public World getDefWorld() {
		return MinecraftServer.getServer().getEntityWorld();
	}
	
	@Override
	public World getDefWorld(boolean isRemote) {
		return MinecraftServer.getServer().getEntityWorld();
	}
}
