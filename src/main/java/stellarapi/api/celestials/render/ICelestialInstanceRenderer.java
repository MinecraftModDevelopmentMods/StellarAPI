package stellarapi.api.celestials.render;

import stellarapi.api.celestials.CelestialObject;

public interface ICelestialInstanceRenderer {

	public void upload(CelestialObject instance, ICelestialVertexBuffer buffer);

}