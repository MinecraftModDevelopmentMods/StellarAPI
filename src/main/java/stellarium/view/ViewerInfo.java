package stellarium.view;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import stellarapi.api.lib.math.SpCoord;
import stellarapi.api.lib.math.Spmath;
import stellarapi.api.lib.math.Vector3;
import stellarapi.api.optics.IOpticalFilter;
import stellarapi.api.optics.IViewScope;
import stellarapi.api.optics.WaveFilterType;

public class ViewerInfo {
	
	public final SpCoord currentDirection;
	public final double currentFOVRadius;
	
	public final Vector3 currentPosition;
	
	public final ICelestialCoordinate coordinate;
	public final ISkyEffect sky;
	
	
	public final double multiplyingPower;
	
	public final Vector3 colorMultiplier = new Vector3();
	public final double brightnessMultiplier;

	public final Vector3 resolutionColor = new Vector3();
	public final double resolutionGeneral;
	
	public ViewerInfo(ICelestialCoordinate coordinate, ISkyEffect sky, IViewScope scope, IOpticalFilter filter, Entity viewer) {
		this.coordinate = coordinate;
		this.sky = sky;
		
		
		this.currentPosition = new Vector3(viewer.posX, viewer.posY, viewer.posZ);
		
		float rotationYaw = viewer.rotationYaw;
		float rotationPitch = -viewer.rotationPitch;

		this.currentDirection = new SpCoord(180 - rotationYaw, rotationPitch);
		
		
		this.multiplyingPower = scope.getMP();
		this.currentFOVRadius = Spmath.Radians(70.0) / this.multiplyingPower;
		
		
		this.brightnessMultiplier = scope.getLGP() * filter.getFilterEfficiency(WaveFilterType.visible);
		colorMultiplier.set(
				scope.getLGP() * filter.getFilterEfficiency(WaveFilterType.red),
				scope.getLGP() * filter.getFilterEfficiency(WaveFilterType.V),
				scope.getLGP() * filter.getFilterEfficiency(WaveFilterType.B)
				);
		
		
		resolutionColor.set(
				Spmath.Radians(Math.max(scope.getResolution(WaveFilterType.red), sky.getSeeing(WaveFilterType.red))),
				Spmath.Radians(Math.max(scope.getResolution(WaveFilterType.V), sky.getSeeing(WaveFilterType.V))),
				Spmath.Radians(Math.max(scope.getResolution(WaveFilterType.B), sky.getSeeing(WaveFilterType.B)))
				);
		this.resolutionGeneral = Spmath.Radians(Math.max(scope.getResolution(WaveFilterType.visible), sky.getSeeing(WaveFilterType.visible)));
 	}

	public float getHeight(World world) {
		return (float)((currentPosition.getY() - world.getHorizon()) / world.getHeight());
	}

}
