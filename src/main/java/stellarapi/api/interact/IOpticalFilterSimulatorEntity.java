package stellarapi.api.interact;

import net.minecraft.entity.EntityLivingBase;
import stellarapi.api.optics.IOpticalFilter;

/**
 * Interface for Optical simulator entity as filter.
 * <p>
 * To make a filter as simulator entity ridden by the player, override this
 * interface.
 */
public interface IOpticalFilterSimulatorEntity {

	/**
	 * Gets the filter for this entity.
	 * 
	 * @param simulated
	 *            the simulated viewer
	 */
	public IOpticalFilter getFilter(EntityLivingBase simulated);

}
