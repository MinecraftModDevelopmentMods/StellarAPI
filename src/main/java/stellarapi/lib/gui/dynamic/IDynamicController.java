package stellarapi.lib.gui.dynamic;

import stellarapi.lib.gui.GuiElement;
import stellarapi.lib.gui.IElementController;

public interface IDynamicController extends IElementController {

	/** Checks if the element hierarchy has to be updated. */
	public boolean needUpdate();

	/**
	 * Generate current element. Called on initialization and update.
	 */
	public GuiElement<?> generateElement();

}
