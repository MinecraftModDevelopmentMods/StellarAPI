package stellarapi.lib.gui.animation;

import stellarapi.lib.gui.IElementController;

public interface IRollableSimpleController extends IElementController {

	public boolean isHorizontal();

	public boolean increaseCoordOnRoll();

	public boolean shouldBeRolled();

	public boolean forceState();

	/**
	 * @param isRolling
	 *            whether the animation is rolling or not
	 * @return duration of the rolling animation
	 */
	public int onRollingStart(boolean isRolling);

	public void onRollingEnded(boolean isRolling);

}
