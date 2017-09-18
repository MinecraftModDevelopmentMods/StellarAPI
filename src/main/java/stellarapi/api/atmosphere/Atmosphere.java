package stellarapi.api.atmosphere;

import java.util.Arrays;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public class Atmosphere implements INBTSerializable<NBTTagCompound> {

	public static enum EnumAtmosphereType {
		PLANE, SPHERE;
	}

	public static class LayerBoundary {
		private double height;
		private double density;
	
		public LayerBoundary(double height, double density) {
			this.height = height;
			this.density = density;
		}

		@Override
		public boolean equals(Object o) {
			if(o instanceof LayerBoundary) {
				LayerBoundary boundary = (LayerBoundary) o;
				return this.density == boundary.density && this.height == boundary.height;
			} else return false;
		}
	}

	Atmosphere(EnumAtmosphereType type, List<LayerBoundary> bounds, List<IAtmosphereLayer> layers) {
		this.atmType = type;
		this.boundaries = bounds.toArray(new LayerBoundary[0]);
		this.layers = layers.toArray(new IAtmosphereLayer[0]);
	}

	/** Dummy constructor to read atmosphere */
	@Deprecated
	public Atmosphere() {}

	/** Layer Boundary data - should have one more entry than layers */
	private LayerBoundary[] boundaries;

	/** Layer data */
	private IAtmosphereLayer[] layers;

	/** Atmosphere type */
	private EnumAtmosphereType atmType = EnumAtmosphereType.PLANE;

	@Override
	public NBTTagCompound serializeNBT() {
		// TODO Atmosphere Serialize routine
		return null;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		// TODO Atmosphere Deserialize routine
	}

	/** Value equality check */
	@Override
	public boolean equals(Object o) {
		if(o instanceof Atmosphere) {
			Atmosphere atm = (Atmosphere) o;
			return Arrays.equals(this.boundaries, atm.boundaries)
					&& Arrays.equals(this.layers, atm.layers)
					&& this.atmType == atm.atmType;
		} else return false;
	}

	/** Gets hash for the value, which will be used for atmosphere checking. */
	@Override
	public int hashCode() {
		int code = atmType.hashCode();

		for(LayerBoundary bound : this.boundaries) {
			code = code ^ Float.floatToRawIntBits((float)bound.height)
					^ Float.floatToIntBits((float)bound.density);
		}

		for(IAtmosphereLayer layer : this.layers) {
			code = code ^ layer.hashCode();
		}

		return code;
	}
}