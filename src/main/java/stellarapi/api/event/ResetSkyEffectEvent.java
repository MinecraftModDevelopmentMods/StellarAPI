package stellarapi.api.event;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import stellarapi.api.ISkyEffect;

/**
 * Fired to reset the sky effect.
 * 
 * This event is <code>@Cancelable</code>,
 * and canceling this event will force the sky effect unavailable now.
 * */
@Cancelable
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
