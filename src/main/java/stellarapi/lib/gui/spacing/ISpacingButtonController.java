package stellarapi.lib.gui.spacing;

import stellarapi.lib.gui.IRenderer;
import stellarapi.lib.gui.button.IButtonController;

public interface ISpacingButtonController extends ISpacingController, IButtonController {

	/** Handle mouse interaction the element in the spacing */
	public boolean handleInElement();
	
	public void updateHovering(boolean newHover);
	
	@Override
	@Deprecated
	public String setupSpacingRenderer(IRenderer renderer);

}