package stellarapi.internal.atmosphere;

import net.minecraft.util.ResourceLocation;
import stellarapi.api.atmosphere.AtmosphereInfo;
import stellarapi.api.atmosphere.AtmosphereType;
import stellarapi.api.atmosphere.IAtmSystem;

public class CAtmSystem implements IAtmSystem {

	@Override
	public void setWorldAtmType(AtmosphereType atmosphereType) {
		// TODO Auto-generated method stub

	}

	@Override
	public AtmosphereType getWorldAtmType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasInfo(ResourceLocation atmId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public AtmosphereInfo getInfo(ResourceLocation atmId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AtmosphereInfo[] getInfos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createAtmosphere(AtmosphereType type, ResourceLocation atmId) {
		// TODO Auto-generated method stub

	}

}
