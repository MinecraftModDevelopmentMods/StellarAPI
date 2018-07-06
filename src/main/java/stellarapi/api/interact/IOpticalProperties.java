package stellarapi.api.interact;

import net.minecraft.entity.EntityLivingBase;
import stellarapi.api.optics.IOpticalProp;

/**
 * Optical Properties of items & simulator entities.
 * */
public interface IOpticalProperties {
	
	/**Checks for filter*/
	public boolean isFilter();

	/**
	 * Gets the optical filter.
	 * @param viewer the viewer
	 * @return the filter, or <code>null</code> if it does not have a filter.
	 */
	public IOpticalProp getFilter(EntityLivingBase viewer);

}
