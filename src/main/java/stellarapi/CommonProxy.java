package stellarapi;

import java.io.IOException;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.world.World;
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
}
