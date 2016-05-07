package stellarapi.lib.gui.animation;

import stellarapi.lib.gui.IElementController;
import stellarapi.lib.gui.IGuiPosition;

public interface IRollableFluentController extends IElementController {

	public boolean isHorizontal();
	public boolean increaseCoordOnRoll();
	public boolean disableControlOnAnimating();
	
	/**
	 * Inverts roll state when actual coordinate decreases.
	 * Setting this to true ensures that roll state is 0 on rolled and 1 on unrolled,
	 * while false ensures that roll state is dependent to the coordinate.
	 * */
	public boolean isRollStateIndependent();
	
	public float rollState();
	public boolean forceState();
	
	public float rollRatePerTick();
	
	public IGuiPosition wrapExcludedPosition(IGuiPosition wrapped, IGuiPosition rollPos);

}
