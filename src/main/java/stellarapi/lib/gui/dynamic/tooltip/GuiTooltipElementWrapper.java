package stellarapi.lib.gui.dynamic.tooltip;

import stellarapi.lib.gui.GuiElement;
import stellarapi.lib.gui.GuiPositionHierarchy;
import stellarapi.lib.gui.IGuiElementType;
import stellarapi.lib.gui.IGuiPosition;
import stellarapi.lib.gui.IRenderer;

/**
 * Tooltip Element Wrapper.
 */
public class GuiTooltipElementWrapper implements IGuiElementType<ITooltipElementController> {

	private GuiElement<?> wrapped;
	private IGuiPosition position;
	private ITooltipElementController controller;

	private GuiHasTooltip parent;

	private long hoverStart = -1;

	protected GuiTooltipElementWrapper(GuiElement<?> wrapped, GuiHasTooltip parent) {
		this.wrapped = wrapped;
		this.parent = parent;
	}

	@Override
	public void initialize(GuiPositionHierarchy positions, ITooltipElementController controller) {
		this.position = positions.getPosition();
		this.controller = controller;

		wrapped.initialize(positions);
	}

	@Override
	public void updateElement() {
		wrapped.getType().updateElement();
	}

	@Override
	public void mouseClicked(float mouseX, float mouseY, int eventButton) {
		wrapped.getType().mouseClicked(mouseX, mouseY, eventButton);
	}

	@Override
	public void mouseClickMove(float mouseX, float mouseY, int eventButton, long timeSinceLastClick) {
		wrapped.getType().mouseClickMove(mouseX, mouseY, eventButton, timeSinceLastClick);
	}

	@Override
	public void mouseReleased(float mouseX, float mouseY, int eventButton) {
		wrapped.getType().mouseReleased(mouseX, mouseY, eventButton);
	}

	@Override
	public void keyTyped(char eventChar, int eventKey) {
		wrapped.getType().keyTyped(eventChar, eventKey);
	}

	@Override
	public void checkMousePosition(float mouseX, float mouseY) {
		boolean canHover = controller.canDisplayTooltip();
		if (canHover && hoverStart == -1 && position.getClipBound().isInBound(mouseX, mouseY))
			hoverStart = System.currentTimeMillis();
		else if (!canHover || !position.getClipBound().isInBound(mouseX, mouseY))
			this.hoverStart = -1;

		if (canHover && hoverStart != -1
				&& System.currentTimeMillis() - hoverStart >= controller.getTooltipDisplayWaitTime()) {
			float ratioX = position.getElementBound().getRatioX(mouseX);
			float ratioY = position.getElementBound().getRatioY(mouseY);
			parent.notifyRenderTooltip(mouseX, mouseY, controller.getTooltipInfo(ratioX, ratioY));
		}

		wrapped.getType().checkMousePosition(mouseX, mouseY);
	}

	@Override
	public void render(IRenderer renderer) {
		wrapped.getType().render(renderer);
	}

}
