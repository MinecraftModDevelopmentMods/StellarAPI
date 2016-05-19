package stellarapi.lib.gui.spacing;

import stellarapi.lib.gui.IRenderer;
import stellarapi.lib.gui.simple.ISimpleController;

public interface ISimpleSpacingController extends ISimpleController {

	/**
	 * Sets up and give model name.
	 * */
	public String setupSpacingRenderer(IRenderer renderer);

	public float getSpacingX();
	public float getSpacingY();

}
