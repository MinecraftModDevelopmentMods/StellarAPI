package stellarapi.lib.gui.text;

import stellarapi.lib.gui.IElementController;
import stellarapi.lib.gui.simple.ISimpleRenderController;

public interface ITextFieldController extends IElementController {

	public ITextInternalController getTextController();
	public ISimpleRenderController getBackground();

	public float getSpacingX();
	public float getSpacingY();

}
