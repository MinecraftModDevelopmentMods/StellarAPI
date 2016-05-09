package stellarapi.api.interact;

import net.minecraft.entity.player.EntityPlayer;
import stellarapi.api.optics.IViewScope;

/**
 * Interface for Optical simulator entity as scope. <p>
 * To make a scope as simulator entity ridden by the player, override this interface.
 * */
public interface IViewScopeSimulatorEntity {

	/**
	 * Gets the scope for this entity.
	 * @param player the player
	 * */
	public IViewScope getScope(EntityPlayer player);

}
