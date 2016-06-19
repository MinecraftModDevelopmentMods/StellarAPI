package stellarapi.api.event.interact;

import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.EntityEvent;

/**
 * Fired when entity is constructed, to check if certain entity has optical
 * properties, i.e. it has optical capabilities and accepts the optical events.
 * Note that this will be only called once per entity.
 */
public class CheckEntityOpticalViewerEvent extends EntityEvent {
	private boolean isOpticalEntity;

	public CheckEntityOpticalViewerEvent(Entity entity) {
		super(entity);
	}

	public boolean isOpticalEntity() {
		return this.isOpticalEntity;
	}

	public void setIsOpticalEntity(boolean isOpticalEntity) {
		this.isOpticalEntity = isOpticalEntity;
	}

}
