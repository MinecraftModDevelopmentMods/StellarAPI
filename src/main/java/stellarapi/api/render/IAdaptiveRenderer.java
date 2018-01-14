package stellarapi.api.render;

import net.minecraftforge.client.IRenderHandler;

/** Adaptive renderer which adapts previous render handler. */
public abstract class IAdaptiveRenderer extends IRenderHandler {
	/** Sets the replaced renderer. */
	public abstract void setReplacedRenderer(IRenderHandler handler);
}
