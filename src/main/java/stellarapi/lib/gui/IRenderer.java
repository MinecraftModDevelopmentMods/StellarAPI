package stellarapi.lib.gui;

public interface IRenderer {

	/**
	 * Binds render model.
	 * For normal gui rendering case, it will be render model for .
	 * For rendering texts, it will be something like font.
	 * */
	public void bindModel(IRenderModel model);

	/**
	 * Push settings till next render.
	 * */
	public void pushSettingTillNextRender();
	public void translate(float posX, float posY);

	/**
	 * Rotation around the center.
	 * */
	public void rotate(float angle, float x, float y, float z);
	
	/**
	 * Scale around the center.
	 * */
	public void scale(float scaleX, float scaleY);
	
	/**
	 * Multiplies color to current settings.
	 * */
	public void color(float red, float green, float blue, float alpha);

	/**
	 * Render with the settings.
	 * */
	public void render(String info, IRectangleBound totalBound, IRectangleBound clipBound);

	public void startRender();
	public void endRender();
	
	/**
	 * Getter for the partial tick
	 * */
	public float getPartialTicks();

	/**
	 * Before rendering all the contents
	 * */
	public void preRender(float partialTicks);
	
	/**
	 * After rendering all the contents
	 * */
	public void postRender(float partialTicks);

}
