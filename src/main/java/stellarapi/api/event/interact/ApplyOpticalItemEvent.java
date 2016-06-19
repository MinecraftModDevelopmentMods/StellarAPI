package stellarapi.api.event.interact;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;

/**
 * Fired when it needs to check if certain item is optical instrument, i.e. view
 * scope or optical filter.
 * <p>
 * Especially, when held item is updated.
 * <p>
 * Since normal optical update logic does not apply for held item, any optical
 * instrument item should be listening for this event instead of calling
 * {@link stellarapi.api.StellarAPIReference#updateScope(net.minecraft.entity.Entity, Object...)
 * updating scope} or
 * {@link stellarapi.api.StellarAPIReference#updateFilter(net.minecraft.entity.Entity, Object...)
 * updating filter}.
 * <p>
 * (Note that item given by this event can be different from current held item
 * of the player. The item from this event is not null.)
 * <p>
 * This event ensures that it's safe to get held item from
 * {@link stellarapi.api.UpdateScopeEvent} or
 * {@link stellarapi.api.UpdateFilterEvent}
 */
public class ApplyOpticalItemEvent extends LivingEvent {

	private final ItemStack item;
	private boolean isViewScope = false;
	private boolean isOpticalFilter = false;

	public ApplyOpticalItemEvent(EntityLivingBase entity, ItemStack item) {
		super(entity);
		this.item = item;
	}

	public ItemStack getItem() {
		return this.item;
	}

	public boolean isViewScope() {
		return this.isViewScope;
	}

	public boolean isOpticalFilter() {
		return this.isOpticalFilter;
	}

	public void setIsViewScope(boolean isViewScope) {
		this.isViewScope = isViewScope;
	}

	public void setIsOpticalFilter(boolean isOpticalFilter) {
		this.isOpticalFilter = isOpticalFilter;
	}

}
