package stellarapi.lib.gui;

public interface IGuiPosition {

	/**
	 * Element bound.
	 */
	public IRectangleBound getElementBound();

	/**
	 * Clipping bound. Must be inside the element bound, or the rendering will
	 * be screwed.
	 */
	public IRectangleBound getClipBound();

	public IRectangleBound getAdditionalBound(String boundName);

	public void initializeBounds();

	public void updateBounds();

	public void updateAnimation(float partialTicks);

}
