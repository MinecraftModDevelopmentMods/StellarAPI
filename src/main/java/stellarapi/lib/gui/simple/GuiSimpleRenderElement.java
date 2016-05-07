package stellarapi.lib.gui.simple;

import stellarapi.lib.gui.GuiPositionHierarchy;
import stellarapi.lib.gui.IGuiElementType;
import stellarapi.lib.gui.IGuiPosition;
import stellarapi.lib.gui.IRectangleBound;
import stellarapi.lib.gui.IRenderer;

public class GuiSimpleRenderElement implements IGuiElementType<ISimpleRenderController> {

	private IGuiPosition position;
	private ISimpleRenderController controller;
	
	@Override
	public void initialize(GuiPositionHierarchy positions, ISimpleRenderController controller) {
		this.position = positions.getPosition();
		this.controller = controller;
	}

	@Override
	public void updateElement() { }

	@Override
	public void mouseClicked(float mouseX, float mouseY, int eventButton) { }

	@Override
	public void mouseMovedOrUp(float mouseX, float mouseY, int eventButton) { }

	@Override
	public void keyTyped(char eventChar, int eventKey) { }

	@Override
	public void checkMousePosition(float mouseX, float mouseY) { }
	
	@Override
	public void render(IRenderer renderer) {
		IRectangleBound clipBound = position.getClipBound();
		if(clipBound.isEmpty())
			return;
		
		renderer.startRender();
		String model = controller.setupRenderer(renderer);
		renderer.render(model, position.getElementBound(), clipBound);
		renderer.endRender();
	}

}
