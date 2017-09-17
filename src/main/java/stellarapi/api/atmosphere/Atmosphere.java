package stellarapi.api.atmosphere;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.util.INBTSerializable;
import stellarapi.api.lib.math.SpCoord;

public abstract class Atmosphere implements INBTSerializable<NBTTagCompound> {

	public static enum EnumAtmosphereType {
		PLANE, SPHERE;
	}

	public static class LayerBoundaryData {
		private double height;
		private double density;
	}

	// TODO Atmosphere fill in these

	private AtmosphereType<?> theType;

	/** Layer Boundary data - should have one more entry than layers */
	private LayerBoundaryData[] data;

	/** Layer data */
	private IAtmosphereLayer[] layers;

	/** Atmosphere type */
	private EnumAtmosphereType atmType;

	public AtmosphereType<?> getType() {
		return this.theType;
	}
}