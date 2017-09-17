package stellarapi.api.event.settings;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.GenericEvent;

/**
 * Apply world settings of specific class.
 * Always check for the identifier if it's right event.
 * */
public class ApplyWorldSettingsEvent<S> extends GenericEvent<S> {
	public final Object settings;
	public final S identifier;
	public final World world;

	public ApplyWorldSettingsEvent(Class<S> baseClass, S identifier, Object settings, World world) {
		super(baseClass);
		this.identifier = identifier;
		this.settings = settings;
		this.world = world;
	}
}