package stellarapi.api.event;

import gnu.trove.map.TObjectFloatMap;
import net.minecraft.entity.Entity;
import stellarapi.api.optics.Wavelength;

/**
 * Event that determines quantum efficiency of filter for an entity.
 * */
public class FilterQEEvent extends PerEntityEvent {
	public final Wavelength wavelength;
	private float efficiency;

	public FilterQEEvent(Entity entity, Wavelength wavelengthIn, float initialQE) {
		super(entity);
		this.wavelength = wavelengthIn;
		this.efficiency = initialQE;
	}

	public float getQE() {
		return this.efficiency;
	}

	public void setQE(float qe) {
		this.efficiency = qe;
	}
}
