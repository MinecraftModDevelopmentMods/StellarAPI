package stellarapi.api.event;

import net.minecraft.world.World;
import stellarapi.api.ICelestialCoordinate;
import stellarapi.api.ISkyEffect;

/**
 * Fired to reset the sky effect.
 * */
public class ResetSkyEffectEvent extends PerWorldEvent {

	private ISkyEffect skyEffect = null;
	
	public ResetSkyEffectEvent(World world) {
		super(world);
	}
	
	public ISkyEffect getSkyEffect() {
		return this.skyEffect;
	}
	
	public void setSkyEffect(ISkyEffect skyEffect) {
		this.skyEffect = skyEffect;
	}
}
