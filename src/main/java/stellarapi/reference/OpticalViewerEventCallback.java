package stellarapi.reference;

import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import stellarapi.api.SAPICapabilities;
import stellarapi.api.SAPIReferences;
import stellarapi.api.optics.IOpticalFilter;
import stellarapi.api.optics.IOpticalViewer;
import stellarapi.api.optics.IViewScope;
import stellarapi.api.optics.NakedFilter;
import stellarapi.api.optics.NakedScope;

/**
 * Per entity manager to contain the per-entity objects, Which gets event
 * callbacks.
 */
public final class OpticalViewerEventCallback implements IUpdatedOpticalViewer, IOpticalViewer {

	private Entity entity;
	private Entity ridingEntity;

	private IViewScope scope = null;
	private IOpticalFilter filter = null;

	public OpticalViewerEventCallback(Entity entity) {
		this.entity = entity;
		this.ridingEntity = entity.getRidingEntity();
	}

	public void updateScope(Object... additionalParams) {
		UpdateScopeEvent scopeEvent = new UpdateScopeEvent(this.entity, new NakedScope(), additionalParams);
		SAPIReferences.getEventBus().post(scopeEvent);
		this.scope = scopeEvent.getScope();
	}

	public void updateFilter(Object... additionalParams) {
		UpdateFilterEvent filterEvent = new UpdateFilterEvent(this.entity, new NakedFilter(), additionalParams);
		SAPIReferences.getEventBus().post(filterEvent);
		this.filter = filterEvent.getFilter();
	}

	public IViewScope getScope() {
		if (this.scope == null)
			this.updateScope();
		return this.scope;
	}

	public IOpticalFilter getFilter() {
		if (this.filter == null)
			this.updateFilter();
		return this.filter;
	}

	public void update() {
		if (this.ridingEntity != entity.getRidingEntity()) {
			boolean updateScope = false;
			boolean updateFilter = false;

			if (this.ridingEntity != null && ridingEntity.hasCapability(SAPICapabilities.OPTICAL_PROPERTY, EnumFacing.UP)) {
				IOpticalProperties property = ridingEntity.getCapability(SAPICapabilities.OPTICAL_PROPERTY, EnumFacing.UP);
				updateScope = updateScope || property.isScope();
				updateFilter = updateFilter || property.isFilter();
			}

			if (entity.getRidingEntity() != null && entity.getRidingEntity().hasCapability(SAPICapabilities.OPTICAL_PROPERTY, EnumFacing.UP)) {
				IOpticalProperties property = entity.getRidingEntity().getCapability(SAPICapabilities.OPTICAL_PROPERTY, EnumFacing.UP);
				updateScope = updateScope || property.isScope();
				updateFilter = updateFilter || property.isFilter();
			}

			if (updateScope)
				this.updateScope();
			if (updateFilter)
				this.updateFilter();

			this.ridingEntity = entity.getRidingEntity();
		}
	}

}
