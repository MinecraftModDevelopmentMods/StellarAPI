package stellarapi.stellars.view;

import javax.vecmath.Matrix4d;
import javax.vecmath.Vector3d;

import net.minecraft.world.World;
import stellarapi.util.math.SpCoord;

public interface IStellarViewpoint {
	
	public void update(World world, double year);
	
	public Matrix4d getProjection();
	
	public Matrix4d projectionToEquatorial();
	
	public void applyAtmRefraction(SpCoord coord);
	
	public void disapplyAtmRefraction(SpCoord coord);
	
	public double getAirmass(Vector3d vector, boolean isRefractionApplied);
	
	public boolean hideObjectsUnderHorizon();
}
