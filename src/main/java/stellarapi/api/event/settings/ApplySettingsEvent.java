package stellarapi.api.event.settings;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.GenericEvent;

/**
 * Apply settings of specific object.
 * Always check for the identifier if it's right event.
 * This can be called for the same identifier many times, which is because of the placeholders.
 * */
public class ApplySettingsEvent<S> extends GenericEvent<S> {
	public final Object settings;
	public final S identifier;
	public final World world;

	public ApplySettingsEvent(Class<S> baseClass, S identifier, Object settings, World world) {
		super(baseClass);
		this.identifier = identifier;
		this.settings = settings;
		this.world = world;
	}
}