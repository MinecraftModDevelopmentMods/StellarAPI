package stellarapi.api.event;

import net.minecraft.entity.Entity;
import stellarapi.api.optics.IOpticalProp;

/**
 * Fired to reset the scope.
 */
public class UpdateFilterEvent extends PerEntityEvent {

	/**
	 * The current optical filter, {@link stellarapi.api.optics.EyeDetector
	 * NakedFilter} by default. Should not be null.
	 */
	private IOpticalProp filter;

	/** Additional parameters, like items which is changed or started using */
	private final Object[] params;

	public UpdateFilterEvent(Entity entity, IOpticalProp defFilter, Object... additionalParams) {
		super(entity);
		this.filter = defFilter;
		this.params = additionalParams;
	}

	public IOpticalProp getFilter() {
		return this.filter;
	}

	public void setFilter(IOpticalProp filter) {
		this.filter = filter;
	}

	public Object[] getAdditionalParams() {
		return this.params;
	}

}
