package stellarapi.lib.gui.simple;

import stellarapi.lib.gui.IRenderer;

public interface ISimpleSpacingController extends ISimpleController {

	/**
	 * Sets up and give model name.
	 * */
	public String setupSpacingRenderer(IRenderer renderer);

	public float getSpacingX();
	public float getSpacingY();

}
