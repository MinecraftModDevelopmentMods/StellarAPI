package stellarapi.api.view;

import stellarapi.api.lib.math.SpCoord;
import stellarapi.api.lib.math.Vector3;

public class ViewUtil {
	/**
	 * Transform to horizontal coordinates with refraction applied
	 * */
	public static SpCoord transformToHor(ICCoordinates coord, IAtmosphereEffect atm, Vector3 absPos) {
		Vector3 horPos = coord.getProjectionToGround().transform(new Vector3(absPos));
		SpCoord horCoord = new SpCoord().setWithVec(horPos);
		atm.applyAtmRefraction(horCoord);
		return horCoord;
	}

	/**
	 * Transform to absolute coordinates with refraction disapplied
	 * */
	public static SpCoord transformToAbs(ICCoordinates coord, IAtmosphereEffect atm, SpCoord horCoord) {
		SpCoord abs = new SpCoord().set(horCoord);
		atm.disapplyAtmRefraction(abs);
		Vector3 absPos = coord.getProjectionToGround().transpose().transform(abs.getVec());
		abs.setWithVec(absPos);
		return abs;
	}
}
