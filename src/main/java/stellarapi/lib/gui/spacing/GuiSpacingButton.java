package stellarapi.lib.gui.spacing;

import stellarapi.lib.gui.GuiElement;
import stellarapi.lib.gui.GuiPositionHierarchy;
import stellarapi.lib.gui.IGuiElementType;
import stellarapi.lib.gui.IGuiPosition;
import stellarapi.lib.gui.IRectangleBound;
import stellarapi.lib.gui.IRenderer;
import stellarapi.lib.gui.RectangleBound;

public class GuiSpacingButton implements IGuiElementType<ISpacingButtonController> {

	private IGuiPosition position, subPosition;
	private ISpacingButtonController controller;
	private GuiElement subElement;
	private boolean isClicking, mouseOver;
	
	public GuiSpacingButton(GuiElement subElement) {
		this.subElement = subElement;
	}
	
	@Override
	public void initialize(GuiPositionHierarchy positions, ISpacingButtonController controller) {
		this.position = positions.getPosition();
		this.controller = controller;
		subElement.initialize(positions.addChild(this.subPosition = new SpacedPosition()));
	}

	@Override
	public void updateElement() {
		subElement.getType().updateElement();
	}

	@Override
	public void mouseClicked(float mouseX, float mouseY, int eventButton) {
		subElement.getType().mouseClicked(mouseX, mouseY, eventButton);
		IRectangleBound bound = position.getClipBound();
		IRectangleBound subBound = subPosition.getClipBound();

        if(controller.canClick(eventButton))
    		if(bound.isInBound(mouseX, mouseY) && (controller.handleInElement() || !subBound.isInBound(mouseX, mouseY)))
    		{
    			this.isClicking = true;
    			controller.onClicked(eventButton);
    		}
	}

	@Override
	public void mouseMovedOrUp(float mouseX, float mouseY, int eventButton) {
		subElement.getType().mouseMovedOrUp(mouseX, mouseY, eventButton);
		IRectangleBound bound = position.getClipBound();
		IRectangleBound subBound = subPosition.getClipBound();

        if(controller.canClick(eventButton))
    		if(bound.isInBound(mouseX, mouseY) && (controller.handleInElement() || !subBound.isInBound(mouseX, mouseY)))
    		{
    			this.isClicking = false;
    			controller.onClickEnded(eventButton);
    		}
	}

	@Override
	public void keyTyped(char eventChar, int eventKey) {
		subElement.getType().keyTyped(eventChar, eventKey);
	}

	@Override
	public void checkMousePosition(float mouseX, float mouseY) {
		subElement.getType().checkMousePosition(mouseX, mouseY);
		IRectangleBound clipBound = position.getClipBound();
		IRectangleBound subBound = subPosition.getClipBound();

		boolean newHover = clipBound.isInBound(mouseX, mouseY) && (controller.handleInElement() || !subBound.isInBound(mouseX, mouseY));
		controller.updateHovering(newHover);
		this.mouseOver = newHover;
	}
	
	@Override
	public void render(IRenderer renderer) {
		IRectangleBound clipBound = position.getClipBound();
		if(clipBound.isEmpty())
			return;
		
		IRectangleBound elementBound = position.getElementBound();
		
		renderer.startRender();
		controller.setupRenderer(this.mouseOver, renderer);
		String main = controller.setupMain(this.mouseOver, renderer);
		if(main != null)
			renderer.render(main, elementBound, clipBound);

		String overlay = controller.setupOverlay(this.mouseOver, renderer);
		if(overlay != null)
			renderer.render(overlay, elementBound, clipBound);
		renderer.endRender();
		
		subElement.getType().render(renderer);
	}

	public class SpacedPosition implements IGuiPosition {
		private RectangleBound element, clip;

		@Override
		public IRectangleBound getElementBound() {
			return this.element;
		}

		@Override
		public IRectangleBound getClipBound() {
			return this.clip;
		}

		@Override
		public IRectangleBound getAdditionalBound(String boundName) {
			return position.getAdditionalBound(boundName);
		}

		@Override
		public void initializeBounds() {
			this.element = new RectangleBound(position.getElementBound());
			this.clip = new RectangleBound(position.getClipBound());
			element.extend(-controller.getSpacingLeft(), -controller.getSpacingUp(),
					-controller.getSpacingRight(), -controller.getSpacingDown());
			clip.setAsIntersection(this.element);
		}

		@Override
		public void updateBounds() {
			element.set(position.getElementBound());
			clip.set(position.getClipBound());
			element.extend(-controller.getSpacingLeft(), -controller.getSpacingUp(),
					-controller.getSpacingRight(), -controller.getSpacingDown());
			clip.setAsIntersection(this.element);
		}

		@Override
		public void updateAnimation(float partialTicks) {
			this.updateBounds();
		}

	}
}
