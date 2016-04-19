package stellarapi.api.optics;

/**
 * View scope for the entity.
 * To make an item work like telescope, see {@link stellarapi.example.ItemTelescopeExample}.
 * */
public interface IViewScope {

	/**
	 * Light Gathering Power of this scope, compared to naked eye.
	 * Relative brightness can be seen will be proportional to LGP/MP^2.
	 * */
	public double getLGP();

	/**Resolution of this scope on specific wavelength in degrees.*/
	public double getResolution(Wavelength wl);
	
	/**Magnifying Power of this scope.*/
	public double getMP();
	
	/** Determine if this scope forces change of view. */
	public boolean forceChange();
	
	/**<code>true</code> when FOV of this scope nearly covers the sky.*/
	public boolean isFOVCoverSky();
	
}
