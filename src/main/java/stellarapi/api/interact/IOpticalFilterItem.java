package stellarapi.api.interact;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import stellarapi.api.optics.IOpticalFilter;

/**
 * Interface for Optical item as filter.
 * <p>
 * To make a filter as held item, either override this interface, or use
 * {@link stellarapi.api.event.interact.ApplyOpticalItemEvent} and
 * {@link stellarapi.api.event.UpdateFilterEvent} to identify your filter item.
 */
public interface IOpticalFilterItem {
	/**
	 * Gets the filter for this item.
	 * 
	 * @param viewer
	 *            the viewer
	 * @param item
	 *            the item
	 */
	public IOpticalFilter getFilter(EntityLivingBase viewer, ItemStack item);

	/**
	 * Checks if two items are same.
	 * 
	 * @param instance
	 *            the item with <code>this</code> type
	 * @param another
	 *            the another item, which won't be <code>null</code>
	 */
	public boolean isSame(ItemStack instance, ItemStack another);
}
