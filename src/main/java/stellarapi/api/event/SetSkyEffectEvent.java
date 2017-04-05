package stellarapi.api.event;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import stellarapi.api.ISkyEffect;

/**
 * Fired to set the sky effect.
 * 
 * This event is <code>@Cancelable</code>, and canceling this event will force
 * the sky effect unavailable now.
 */
@Cancelable
public class SetSkyEffectEvent extends PerWorldEvent {

	private ISkyEffect skyEffect = null;

	public SetSkyEffectEvent(World world) {
		super(world);
	}

	public ISkyEffect getSkyEffect() {
		return this.skyEffect;
	}

	public void setSkyEffect(ISkyEffect skyEffect) {
		this.skyEffect = skyEffect;
	}
}
