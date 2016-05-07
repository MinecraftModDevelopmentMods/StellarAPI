package stellarapi.api.gui.content;

public interface IGuiElementType<C extends IElementController> {

	/**
	 * Initialize with certain position and the controller for the element.
	 * */
	public void initialize(GuiPositionHierarchy positions, C controller);

	public void updateElement();

	public void mouseClicked(float mouseX, float mouseY, int eventButton);
	public void mouseMovedOrUp(float mouseX, float mouseY, int eventButton);

	public void keyTyped(char eventChar, int eventKey);
	
	/**
	 * Called before rendering passes to check mouse position.
	 * Bounds can be differ here.
	 * */
	public void checkMousePosition(float mouseX, float mouseY);
	
	public void render(IRenderer renderer);

}
