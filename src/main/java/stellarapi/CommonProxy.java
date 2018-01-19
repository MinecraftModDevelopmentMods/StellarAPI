package stellarapi;

import java.io.IOException;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import stellarapi.api.lib.math.Spmath;
import stellarapi.api.render.IAdaptiveRenderer;
import stellarapi.feature.celestial.tweakable.SAPICelestialScene;

public class CommonProxy implements IProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		StellarAPI.INSTANCE.getLogger().info("Initializing Math class...");
		// Initializing Spmath
		Spmath.Initialize();
		StellarAPI.INSTANCE.getLogger().info("Math Class Initialized!");
	}

	@Override
	public void load(FMLInitializationEvent event) throws IOException { }

	@Override
	public void postInit(FMLPostInitializationEvent event) { }

	@Override
	public void registerModels() { }

	@Override
	public World getClientWorld() {
		return null;
	}

	@Override
	public void registerTask(Runnable runnable) {
		// MinecraftServer.getServer().addScheduledTask(runnable);
	}

	@Override
	public IAdaptiveRenderer getRenderer(SAPICelestialScene sapiCelestialScene) {
		return null;
	}
}
