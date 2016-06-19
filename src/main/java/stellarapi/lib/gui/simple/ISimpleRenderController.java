package stellarapi.lib.gui.simple;

import stellarapi.lib.gui.IRenderer;

public interface ISimpleRenderController extends ISimpleController {

	/**
	 * Sets up and give model name.
	 */
	public String setupRenderer(IRenderer renderer);

}
