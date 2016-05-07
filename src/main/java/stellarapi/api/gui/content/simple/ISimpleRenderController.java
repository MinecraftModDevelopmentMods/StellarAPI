package stellarapi.api.gui.content.simple;

import stellarapi.api.gui.content.IRenderer;

public interface ISimpleRenderController extends ISimpleController {

	/**
	 * Sets up and give model name.
	 * */
	public String setupRenderer(IRenderer renderer);

}
