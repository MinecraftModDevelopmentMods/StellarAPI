package stellarapi.api.gui.content.text;

import stellarapi.api.gui.content.IElementController;
import stellarapi.api.gui.content.simple.ISimpleRenderController;

public interface ITextFieldController extends IElementController {

	public ITextInternalController getTextController();
	public ISimpleRenderController getBackground();

	public float getSpacingX();
	public float getSpacingY();

}
