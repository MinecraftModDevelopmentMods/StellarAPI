package stellarapi.api.atmosphere;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import com.google.common.base.Throwables;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.INBTSerializable;

public final class Atmosphere implements INBTSerializable<NBTTagCompound> {

	public static enum EnumAtmosphereType {
		PLANE, SPHERE;
	}

	public static class LayerBoundary {
		/** Relative-scale Height from the origin point */
		private double height;

		/** Density, 1.0 is the default */
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

	/**
	 * Creates empty atmosphere. Deserialization routine will be run on here.
	 * */
	public Atmosphere(Callable<IAtmosphereLayer> layerInit) {
		this.layerInitializer = layerInit;
	}

	/** Layer Boundary data - should have one more entry than layers */
	private LayerBoundary[] boundaries;

	/** Layer data */
	private IAtmosphereLayer[] layers;

	/** Atmosphere type */
	private EnumAtmosphereType atmType = EnumAtmosphereType.PLANE;

	private Callable<IAtmosphereLayer> layerInitializer = null;

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();

		nbt.setInteger("type", atmType.ordinal());

		NBTTagList heights = new NBTTagList();
		NBTTagList densities = new NBTTagList();
		for(LayerBoundary boundary : this.boundaries) {
			heights.appendTag(new NBTTagDouble(boundary.height));
			densities.appendTag(new NBTTagDouble(boundary.density));
		}
		nbt.setTag("heights", heights);
		nbt.setTag("densities", densities);

		NBTTagList layers = new NBTTagList();
		for(IAtmosphereLayer layer : this.layers) {
			layers.appendTag(layer.serializeNBT());
		}
		nbt.setTag("layers", layers);

		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		this.atmType = EnumAtmosphereType.values()[nbt.getInteger("type")];

		int dId = new NBTTagDouble(0.0).getId();
		NBTTagList heights = nbt.getTagList("heights", dId);
		NBTTagList densities = nbt.getTagList("densities", dId);
		this.boundaries = new LayerBoundary[heights.tagCount()];
		for(int i = 0; i < heights.tagCount(); i++) {
			this.boundaries[i] = new LayerBoundary(heights.getDoubleAt(i), densities.getDoubleAt(i));
		}

		NBTTagList layersNBT = nbt.getTagList("layers", nbt.getId());
		this.layers = new IAtmosphereLayer[layersNBT.tagCount()];
		try {
			for(int i = 0; i < layersNBT.tagCount(); i++) {
				this.layers[i] = layerInitializer.call();
				layers[i].deserializeNBT(layersNBT.getCompoundTagAt(i));
			}
		} catch(Exception exc) {
			Throwables.propagate(exc);
		}
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