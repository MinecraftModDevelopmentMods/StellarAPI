package stellarapi.lib.gui.scroll;

import stellarapi.lib.gui.IElementController;
import stellarapi.lib.gui.IRenderer;

public interface IScrollBarController extends IElementController {

	public boolean isHorizontal();

	/**
	 * Progress in [0,1]
	 */
	public float initialProgress();

	public boolean canHandle(int eventButton);

	public boolean moveCenterOnClick();

	public boolean isRegionCenterToCenter();

	/**
	 * Progress in [0,1]
	 */
	public void progressUpdating(float progress);

	/**
	 * Progress in [0,1]
	 */
	public void progressUpdated(float progress);

	public float getSpacing();

	public String setupBackgroundRenderer(IRenderer renderer);

	public void setupRegionRenderer(boolean mouseOver, IRenderer renderer);

	public String setupRegionOverlay(boolean mouseOver, IRenderer renderer);

	public String setupRegionMain(boolean mouseOver, IRenderer renderer);

	public void setupButtonRenderer(boolean mouseOver, IRenderer renderer);

	public String setupButtonOverlay(boolean mouseOver, IRenderer renderer);

	public String setupButtonMain(boolean mouseOver, IRenderer renderer);

}
