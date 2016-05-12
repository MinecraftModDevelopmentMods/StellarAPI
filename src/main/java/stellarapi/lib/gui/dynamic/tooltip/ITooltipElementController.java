package stellarapi.lib.gui.dynamic.tooltip;

import stellarapi.lib.gui.IElementController;

public interface ITooltipElementController extends IElementController {
		
	public boolean canDisplayTooltip();
	public int getTooltipDisplayWaitTime();
	
	public String getTooltipInfo(float ratioX, float ratioY);

}
