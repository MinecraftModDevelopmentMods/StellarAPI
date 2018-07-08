package stellarapi.api.observe;

import java.util.function.Consumer;

import net.minecraft.entity.Entity;
import stellarapi.api.IAtmosphereEffect;
import stellarapi.api.SAPIReferences;
import stellarapi.api.celestials.CelestialCollections;
import stellarapi.api.celestials.ICelestialCoordinates;
import stellarapi.api.celestials.ICelestialObject;
import stellarapi.api.lib.math.SpCoord;
import stellarapi.api.lib.math.Vector3;
import stellarapi.api.optics.EyeDetector;
import stellarapi.api.optics.Wavelength;

public class ObservationHandler {
	/**
	 * @param viewer the observing viewer
	 * @param region the search region
	 * @param work the work to do on each celestial object
	 * */
	public static void observe(Entity viewer, SearchRegion region, Consumer<ICelestialObject> work) {
		CelestialCollections manager = SAPIReferences.getCollections(viewer.world);
		if(manager != null) {
			ICelestialCoordinates coordinate = SAPIReferences.getCoordinate(viewer.world);
			IAtmosphereEffect atmosphere = SAPIReferences.getAtmosphereEffect(viewer.world);
			float efficiency = SAPIReferences.estimateQE(viewer, Wavelength.visible);
			float multPower = SAPIReferences.estimateFOV(viewer) / 70.0f;

			SearchRegion.Builder builder = SearchRegion.builder();
			for(SpCoord coord : region.coords) {
				SpCoord abs = new SpCoord().set(coord);
				atmosphere.disapplyAtmRefraction(abs);
				Vector3 absPos = coordinate.getProjectionToGround().transpose().transform(abs.getVec());
				abs.setWithVec(absPos);
				builder.addPos(abs);
			}
			for(int[] trig : region.triangles)
				builder.addTriangle(trig[0], trig[1], trig[2]);

			for(ICelestialObject object : manager.findIn(builder.build(), efficiency, multPower))
				work.accept(object);
		}
	}
}
