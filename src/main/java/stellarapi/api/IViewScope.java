package stellarapi.api;

import stellarapi.api.wavecolor.Wavelength;

public interface IViewScope {

	/**Light Gathering Power of this scope, compared to naked eye.*/
	public double getLGP();

	/**Resolution of this scope on specific wavelength.*/
	public double getResolution(Wavelength wl);
	
	/**Magnifying Power of this scope.*/
	public double getMP();
	
	/**<code>true</code> when FOV of this scope nearly covers the sky.*/
	public boolean isFOVCoverSky();
	
}
