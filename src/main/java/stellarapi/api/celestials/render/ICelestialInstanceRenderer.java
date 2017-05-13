package stellarapi.api.celestials.render;

import stellarapi.api.celestials.CelestialInstance;

public interface ICelestialInstanceRenderer {

	public void upload(CelestialInstance instance, ICelestialVertexBuffer buffer);

}