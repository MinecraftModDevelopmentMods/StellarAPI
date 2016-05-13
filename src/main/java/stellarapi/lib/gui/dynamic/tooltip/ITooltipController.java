package stellarapi.lib.gui.dynamic.tooltip;

import java.util.List;

import stellarapi.lib.gui.IElementController;
import stellarapi.lib.gui.IFontHelper;
import stellarapi.lib.gui.IRenderer;

public interface ITooltipController extends IElementController {
	
	/**The tooltip has clip or not*/
	public boolean hasClip();
	
	public IFontHelper getFontHelper();
	
	/**Must have background*/
	public String setupBackground(StringFormat info, IRenderer renderer);
	
	/**Context for each line*/
	public List<String> getRenderContext(StringFormat info);
	
	/**Setup for each line*/
	public void setupTooltip(String context, IRenderer renderer);

	public float getSpacingX();
	public float getSpacingY();

}
