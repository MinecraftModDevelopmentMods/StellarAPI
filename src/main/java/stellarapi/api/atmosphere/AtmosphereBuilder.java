package stellarapi.api.atmosphere;

import java.util.List;

import com.google.common.collect.Lists;

public class AtmosphereBuilder {

	private List<Atmosphere.LayerBoundary> boundaries = Lists.newArrayList();
	private List<IAtmosphereLayer> layers = Lists.newArrayList();
	private Atmosphere.EnumAtmosphereType atmType;

	/**
	 * Sets atmosphere type.
	 * */
	public void setAtmType(Atmosphere.EnumAtmosphereType type) {
		this.atmType = type;
	}

	/**
	 * Appends a layer with its lower bound.
	 * */
	public void appendLayer(Atmosphere.LayerBoundary lowerBound, IAtmosphereLayer layer) {
		boundaries.add(lowerBound);
		layers.add(layer);
	}

	/**
	 * Build atmosphere with its top bound.
	 * */
	public Atmosphere build(Atmosphere.LayerBoundary topBound) {
		boundaries.add(topBound);
		return new Atmosphere(this.atmType, this.boundaries, this.layers);
	}
}
