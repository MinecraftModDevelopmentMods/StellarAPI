package stellarapi.api.event;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Handles FOV direct modification via render view entity. <p>
 * Exists for server approximation of FOV. Use this for scopes. <p>
 * When FOV is approximated, it should go through FOVUpdateEvent first and
 *  supply the result to this event.
 * */
public class FOVEvent extends Event {
	private final Entity viewer;
	private float fov;

	public FOVEvent(Entity viewer, float fov)
	{
		this.viewer = viewer;
		this.setFOV(fov);
	}

	public Entity getEntity()
	{
		return this.viewer;
	}

	public float getFOV()
	{
		return fov;
	}

	public void setFOV(float fov)
	{
		this.fov = fov;
	}
}
