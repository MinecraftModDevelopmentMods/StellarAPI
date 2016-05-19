package stellarapi.lib.gui.dynamic;

import stellarapi.lib.gui.GuiElement;
import stellarapi.lib.gui.GuiPositionHierarchy;
import stellarapi.lib.gui.IGuiElementType;
import stellarapi.lib.gui.IRenderer;

/**
 * Core Gui Element for dynamic structures.
 * Can implement replaceable elements with this Gui.
 * */
public class GuiDynamic implements IGuiElementType<IDynamicController> {
	
	private GuiElement subElement;
	private GuiPositionHierarchy positions;
	private IDynamicController controller;
	
	public static GuiElement<IDynamicController> asElement(IDynamicController controller) {
		return new GuiElement<IDynamicController>(new GuiDynamic(), controller);
	}

	@Override
	public void initialize(GuiPositionHierarchy positions, IDynamicController controller) {
		this.positions = positions;
		this.controller = controller;
		this.subElement = controller.generateElement();
		
		subElement.initialize(this.positions);
	}

	@Override
	public void updateElement() {
		if(controller.needUpdate()) {
			this.subElement = controller.generateElement();
			positions.clearChild();
			
			subElement.initialize(this.positions);
			positions.initializeBounds();
		}
		
		subElement.getType().updateElement();
	}

	@Override
	public void mouseClicked(float mouseX, float mouseY, int eventButton) {
		subElement.getType().mouseClicked(mouseX, mouseY, eventButton);
	}
	
	@Override
	public void mouseClickMove(float mouseX, float mouseY, int eventButton, long timeSinceLastClick) {
		subElement.getType().mouseClickMove(mouseX, mouseY, eventButton, timeSinceLastClick);
	}

	@Override
	public void mouseReleased(float mouseX, float mouseY, int eventButton) {
		subElement.getType().mouseReleased(mouseX, mouseY, eventButton);
	}

	@Override
	public void keyTyped(char eventChar, int eventKey) {
		subElement.getType().keyTyped(eventChar, eventKey);
	}

	@Override
	public void checkMousePosition(float mouseX, float mouseY) {
		subElement.getType().checkMousePosition(mouseX, mouseY);
	}

	@Override
	public void render(IRenderer renderer) {
		subElement.getType().render(renderer);
	}

}
