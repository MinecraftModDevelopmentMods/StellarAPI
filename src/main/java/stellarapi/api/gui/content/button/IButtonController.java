package stellarapi.api.gui.content.button;

import stellarapi.api.gui.content.IElementController;
import stellarapi.api.gui.content.IRenderer;

public interface IButtonController extends IElementController {

	public boolean canClick(int eventButton);
	
	public void onClicked(int eventButton);
	public void onClickEnded(int eventButton);
	
	public void setupRenderer(boolean mouseOver, IRenderer renderer);
	public String setupOverlay(boolean mouseOver, IRenderer renderer);
	public String setupMain(boolean mouseOver, IRenderer renderer);

}
