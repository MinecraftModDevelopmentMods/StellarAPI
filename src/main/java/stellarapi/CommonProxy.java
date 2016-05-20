package stellarapi;

import java.io.IOException;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import stellarapi.api.gui.loading.ICombinedProgressUpdate;
import stellarapi.api.lib.math.Spmath;

public class CommonProxy implements IProxy {
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		StellarAPI.logger.info("Initializing Math class...");
		//Initializing Spmath
		Spmath.Initialize();
		StellarAPI.logger.info("Math Class Initialized!");
	}

	@Override
	public void load(FMLInitializationEvent event) throws IOException { }

	@Override
	public void postInit(FMLPostInitializationEvent event) { }
	
	@Override
	public World getClientWorld() {
		return null;
	}

	@Override
	public ICombinedProgressUpdate getLoadingProgress() {
		return null;
	}

	@Override
	public void registerTask(Runnable runnable) {
		//MinecraftServer.getServer().addScheduledTask(runnable);
	}
}
