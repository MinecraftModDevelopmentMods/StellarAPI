package stellarapi.lib.gui;

/**
 * Main container GUI for elements.
 */
public class GuiContent {
	private IRenderer renderer;
	private GuiElement<?> element;
	private GuiPositionHierarchy positions;

	public GuiContent(IRenderer renderer, GuiElement<?> element, IGuiPosition position) {
		this.renderer = renderer;
		this.element = element;
		this.positions = new GuiPositionHierarchy(position);

		element.initialize(this.positions);
		positions.initializeBounds();
	}

	/** Update every tick */
	public void updateTick() {
		positions.updateBounds();
		element.getType().updateElement();
	}

	/** On mouse clicked */
	public void mouseClicked(float mouseX, float mouseY, int eventButton) {
		element.getType().mouseClicked(mouseX, mouseY, eventButton);
	}

	/** On mouse click move */
	public void mouseClickMove(float mouseX, float mouseY, int eventButton, long timeSinceLastClick) {
		element.getType().mouseClickMove(mouseX, mouseY, eventButton, timeSinceLastClick);
	}

	/** On mouse moved or up */
	public void mouseReleased(float mouseX, float mouseY, int eventButton) {
		element.getType().mouseReleased(mouseX, mouseY, eventButton);
	}

	/** On key typed */
	public void keyTyped(char eventChar, int eventKey) {
		element.getType().keyTyped(eventChar, eventKey);
	}

	/** On render */
	public void render(float mouseX, float mouseY, float partialTicks) {
		element.getType().checkMousePosition(mouseX, mouseY);
		positions.updateAnimation(partialTicks);

		renderer.preRender(partialTicks);
		element.getType().render(this.renderer);
		renderer.postRender(partialTicks);
	}

}
