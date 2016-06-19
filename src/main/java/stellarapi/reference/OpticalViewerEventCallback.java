package stellarapi.reference;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import stellarapi.api.IPerEntityReference;
import stellarapi.api.StellarAPIReference;
import stellarapi.api.event.UpdateFilterEvent;
import stellarapi.api.event.UpdateScopeEvent;
import stellarapi.api.event.interact.ApplyOpticalEntityEvent;
import stellarapi.api.optics.IOpticalFilter;
import stellarapi.api.optics.IOpticalViewer;
import stellarapi.api.optics.IViewScope;
import stellarapi.api.optics.NakedFilter;
import stellarapi.api.optics.NakedScope;

/**
 * Per entity manager to contain the per-entity objects, Which gets event
 * callbacks.
 */
public final class OpticalViewerEventCallback implements IPerEntityReference, IOpticalViewer {

	private static final String ID = "stellarapiperplayermanager";

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
		StellarAPIReference.getEventBus().post(scopeEvent);
		this.scope = scopeEvent.getScope();
	}

	public void updateFilter(Object... additionalParams) {
		UpdateFilterEvent filterEvent = new UpdateFilterEvent(this.entity, new NakedFilter(), additionalParams);
		StellarAPIReference.getEventBus().post(filterEvent);
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

			if (this.ridingEntity != null) {
				ApplyOpticalEntityEvent applyEvent = new ApplyOpticalEntityEvent((EntityPlayer) this.entity,
						this.ridingEntity);
				StellarAPIReference.getEventBus().post(applyEvent);
				updateScope = updateScope || applyEvent.isViewScope();
				updateFilter = updateFilter || applyEvent.isOpticalFilter();
			}

			if (entity.getRidingEntity() != null) {
				ApplyOpticalEntityEvent applyEvent = new ApplyOpticalEntityEvent((EntityPlayer) this.entity,
						entity.getRidingEntity());
				StellarAPIReference.getEventBus().post(applyEvent);
				updateScope = updateScope || applyEvent.isViewScope();
				updateFilter = updateFilter || applyEvent.isOpticalFilter();
			}

			if (updateScope)
				this.updateScope();
			if (updateFilter)
				this.updateFilter();

			this.ridingEntity = entity.getRidingEntity();
		}
	}

}
