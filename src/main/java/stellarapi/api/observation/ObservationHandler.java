package stellarapi.api.observation;

import java.util.function.Consumer;

import net.minecraft.entity.Entity;
import stellarapi.api.celestials.ICelestialObject;

public class ObservationHandler {
	/**
	 * @param viewer the observing viewer
	 * @param work the work to do on each celestial object
	 * */
	public static void observer(Entity viewer, Consumer<ICelestialObject> work) {
		//float rotationYaw = player.rotationYaw;
		//float rotationPitch = -player.rotationPitch;

		//SpCoord currentDirection = new SpCoord(( - rotationYaw) % 360.0, rotationPitch);

		/*CelestialCollectionManager manager = SAPIReferences.getCollectionManager(player.world);
		IViewScope scope = SAPIReferences.getScope(player);

		if(manager != null && scope != null) {
			for(ICelestialObject object : manager.findAllObjectsInRange(currentDirection, observeRange)) {
				double magnitude = object.getStandardMagnitude();
				magnitude -= 2.5 * Math.log10(scope.getLGP());
				if(magnitude > 6.0)
					continue;
			}
		}*/
	}
}
