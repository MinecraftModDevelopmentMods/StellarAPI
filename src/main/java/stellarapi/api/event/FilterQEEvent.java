package stellarapi.api.event;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import stellarapi.api.optics.Wavelength;

/**
 * Event that determines quantum efficiency of filter for an entity.
 * */
public class FilterQEEvent extends EntityViewRenderEvent {
	private final Wavelength wavelength;
	private float efficiency;

	public FilterQEEvent(EntityRenderer renderer, Entity entity, IBlockState state, double renderPartialTicks, Wavelength wavelengthIn, float initialQE) {
		super(renderer, entity, state, renderPartialTicks);
		this.wavelength = wavelengthIn;
		this.efficiency = initialQE;
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
