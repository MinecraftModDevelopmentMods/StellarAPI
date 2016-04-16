package stellarapi;

import java.io.File;
import java.io.IOException;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import stellarapi.api.StellarAPIReference;
import stellarapi.api.impl.AlarmWakeHandler;
import stellarapi.api.impl.LightWakeHandler;
import stellarapi.api.mc.SleepWakeManager;
import stellarapi.client.ClientSettings;
import stellarapi.common.CommonSettings;
import stellarapi.common.DimensionSettings;
import stellarapi.config.ConfigManager;
import stellarapi.stellars.layer.CelestialManager;
import stellarapi.util.math.Spmath;

public class CommonProxy implements IProxy {

	public DimensionSettings dimensionSettings = new DimensionSettings();
	
	private static final String serverConfigCategory = "serverconfig";
	private static final String serverConfigDimensionCategory = "serverconfig.dimension";
	private static final String serverConfigWakeCategory = "serverconfig.wake";
	
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

	@Override
	public CelestialManager getClientCelestialManager() {
		return null;
	}
}
