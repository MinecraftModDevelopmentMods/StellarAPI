package stellarapi.proxy;

import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import stellarapi.StellarAPI;
import stellarapi.api.lib.math.Spmath;

public class CommonProxy implements IProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		StellarAPI.logger.info("Initializing Math class...");
		// Initializing Spmath
		Spmath.Initialize();
		StellarAPI.logger.info("Math Class Initialized!");
	}

	@Override
	public void load(FMLInitializationEvent event) throws IOException {
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
	}

	@Override
	public void registerClientTask(Runnable runnable) { }

	@Override
	public World getClientWorld() {
		return null;
	}
	@Override
	public EntityPlayer getClientPlayer() {
		return null;
	}
	@Override
	public Entity getRenderViewEntity() {
		return null;
	}
}
