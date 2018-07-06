package stellarapi.reference;

import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import stellarapi.api.IPerEntityReference;
import stellarapi.api.SAPICapabilities;
import stellarapi.api.SAPIReferences;
import stellarapi.api.event.UpdateFilterEvent;
import stellarapi.api.interact.IOpticalProperties;
import stellarapi.api.optics.IOpticalProp;
import stellarapi.api.optics.IOpticalViewer;
import stellarapi.api.optics.NakedFilter;

/**
 * Per entity manager to contain the per-entity objects, Which gets event
 * callbacks.
 */
public final class OpticalViewerEventCallback implements IPerEntityReference, IOpticalViewer {

	private Entity entity;
	private Entity ridingEntity;

	private IOpticalProp filter = null;

	public OpticalViewerEventCallback(Entity entity) {
		this.entity = entity;
		this.ridingEntity = entity.getRidingEntity();
	}

	public void updateFilter(Object... additionalParams) {
		UpdateFilterEvent filterEvent = new UpdateFilterEvent(this.entity, new NakedFilter(), additionalParams);
		SAPIReferences.getEventBus().post(filterEvent);
		this.filter = filterEvent.getFilter();
	}

	public IOpticalProp getFilter() {
		if (this.filter == null)
			this.updateFilter();
		return this.filter;
	}

	public void update() {
		if (this.ridingEntity != entity.getRidingEntity()) {
			boolean updateFilter = false;

			if (this.ridingEntity != null && ridingEntity.hasCapability(SAPICapabilities.OPTICAL_PROPERTY, EnumFacing.UP)) {
				IOpticalProperties property = ridingEntity.getCapability(SAPICapabilities.OPTICAL_PROPERTY, EnumFacing.UP);
				updateFilter = updateFilter || property.isFilter();
			}

			if (entity.getRidingEntity() != null && entity.getRidingEntity().hasCapability(SAPICapabilities.OPTICAL_PROPERTY, EnumFacing.UP)) {
				IOpticalProperties property = entity.getRidingEntity().getCapability(SAPICapabilities.OPTICAL_PROPERTY, EnumFacing.UP);
				updateFilter = updateFilter || property.isFilter();
			}

			if (updateFilter)
				this.updateFilter();

			this.ridingEntity = entity.getRidingEntity();
		}
	}

}
