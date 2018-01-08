package stellarapi.feature.celestial.tweakable;

import net.minecraft.world.World;
import stellarapi.StellarAPI;
import stellarapi.api.ICelestialPack;
import stellarapi.api.ICelestialScene;
import stellarapi.api.world.worldset.WorldSet;
import stellarapi.impl.celestial.DefaultCelestialScene;

public enum SAPICelestialPack implements ICelestialPack {
	INSTANCE;

	@Override
	public String getPackName() {
		return "Stellar API";
	}

	@Override
	public ICelestialScene getScene(WorldSet worldSet, World world, boolean vanillaServer) {
		if(vanillaServer)
			return new DefaultCelestialScene(world);

		SAPIWorldCfgHandler config = StellarAPI.INSTANCE.getPackCfgHandler().getHandler(worldSet);
		return new SAPICelestialScene(world,
				config.sunExist, config.moonExist,
				config.dayLength, config.monthInDay,
				config.dayOffset, config.monthOffset, config.minimumSkyBrightness);
	}
}
