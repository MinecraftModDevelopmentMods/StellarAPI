package stellarapi.api.event;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.Event;
import stellarapi.api.optics.Wavelength;

/**
 * Exists for server approximation of QE. Use this for filters.
 * */
public class QEEvent extends Event {
	private final Entity entity;
	private final Wavelength wavelength;
	private float efficiency;

	public QEEvent(Entity entity, Wavelength wavelengthIn, float initialQE) {
		this.entity = entity;
		this.wavelength = wavelengthIn;
		this.setQE(initialQE);
	}

	public Entity getEntity() {
		return this.entity;
	}

	public Wavelength getWavelength() {
		return this.wavelength;
	}

	public float getQE() {
		return this.efficiency;
	}

	public void setQE(float qe) {
		this.efficiency = qe;
	}
}
