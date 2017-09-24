package stellarapi.api.instruments;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stellarapi.api.lib.math.SpCoord;
import stellarapi.api.region.IRegion;

// TODO Client registrable - accepted?
public abstract class FOVType {
	/**
	 * Gets the field of view.
	 * @param center the center location on the celestial sphere
	 * @param fov the evaluated sky field of view, usually the length of longest side.
	 * */
	public abstract IRegion getFOV(SpCoord center, double skyFOV);

	/**
	 * Renders the FOV on the screen.
	 * @param detectorFOV the detector field of view, usually the length of longest side.
	 * */
	@SideOnly(Side.CLIENT)
	public abstract void handleTypeRender(double detectorFOV);
}