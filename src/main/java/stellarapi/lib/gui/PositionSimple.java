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

	public abstract RectangleBound getBound();

	public abstract void updateBound(RectangleBound bound);

	@Override
	public void initializeBounds() {
		this.bound = this.getBound();
	}

	@Override
	public void updateBounds() {
		this.updateBound(this.bound);
	}

	@Override
	public void updateAnimation(float partialTicks) {
		this.updateBounds();
	}

}
