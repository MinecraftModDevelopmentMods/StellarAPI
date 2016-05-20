package stellarapi.api.event.interact;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityEvent;

/**
 * Fired when it needs to check if certain entity is optical simulator, as view scope or optical filter. <p>
 * Especially, when entity ridden by the player is changed. <p>
 *   (Note that entity given by this event can be different from current entity ridden by the player.
 *   The riding entity from this event is not null.) <p>
 * This event enables for vanilla entity to behave as optical simulator.
 * */
public class ApplyOpticalEntityEvent extends EntityEvent {
	private final Entity ridingEntity;
	private boolean isViewScope = false;
	private boolean isOpticalFilter = false;

	public ApplyOpticalEntityEvent(Entity simulated, Entity simulator) {
		super(simulated);
		this.ridingEntity = simulator;
	}
	
	public Entity getSimulatedEntity() {
		return this.getEntity();
	}
	
	public Entity getSimulatorEntity() {
		return this.ridingEntity;
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
