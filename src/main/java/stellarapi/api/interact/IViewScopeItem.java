package stellarapi.api.interact;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import stellarapi.api.optics.IViewScope;

/**
 * Interface for Optical item as scope. <p>
 * To make a scope as held item, either override this interface, or use
 *  {@link stellarapi.api.event.interact.ApplyOpticalItemEvent} and
 *  {@link stellarapi.api.event.UpdateScopeEvent} to identify your scope item. <p>
 * Note that this item will be specified as same on held item when the type and damage is same.
 * */
public interface IViewScopeItem {
	/**
	 * Gets the scope for this item.
	 * @param player the player
	 * @param item the item
	 * */
	public IViewScope getScope(EntityPlayer player, ItemStack item);
	
	/**
	 * Checks if two items are same.
	 * @param instance the item with <code>this</code> type
	 * @param another the another item, which won't be <code>null</code>
	 * */
	public boolean isSame(ItemStack instance, ItemStack another);
}
