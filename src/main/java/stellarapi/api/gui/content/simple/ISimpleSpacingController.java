package stellarapi.api.gui.content.simple;

import stellarapi.api.gui.content.IRenderer;

public interface ISimpleSpacingController extends ISimpleController {

	/**
	 * Sets up and give model name.
	 * */
	public String setupSpacingRenderer(IRenderer renderer);

	public float getSpacingX();
	public float getSpacingY();

}
