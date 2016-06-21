package stellarapi.api.interact;

import net.minecraft.entity.EntityLivingBase;
import stellarapi.api.optics.IOpticalFilter;
import stellarapi.api.optics.IViewScope;

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
	public IOpticalFilter getFilter(EntityLivingBase viewer);
	

	/**Check for scope*/
	public boolean isScope();
	
	/**
	 * Gets the optical filter.
	 * @param viewer the viewer
	 * @return the scope, or <code>null</code> if it does not have a scope.
	 */
	public IViewScope getScope(EntityLivingBase viewer);

}
