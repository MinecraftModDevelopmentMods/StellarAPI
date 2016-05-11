package stellarapi.lib.gui;

public abstract class PositionSimple implements IGuiPosition {
	
	private RectangleBound bound;
	
	@Override
	public IRectangleBound getElementBound() {
		return this.bound;
	}

	@Override
	public IRectangleBound getClipBound() {
		return this.bound;
	}

	@Override
	public IRectangleBound getAdditionalBound(String boundName) {
		return null;
	}

	@Override
	public abstract void initializeBounds();

	@Override
	public abstract void updateBounds();

	@Override
	public void updateAnimation(float partialTicks) {
		this.updateBounds();
	}

}
