package stellarapi.example;

import net.minecraft.item.Item;
import stellarapi.api.IViewScope;
import stellarapi.api.wavecolor.Wavelength;

public class ItemTelescopeExample extends Item implements IViewScope {

	@Override
	public double getLGP() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getResolution(Wavelength wl) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getMP() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean forceChange() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isFOVCoverSky() {
		// TODO Auto-generated method stub
		return false;
	}

}
