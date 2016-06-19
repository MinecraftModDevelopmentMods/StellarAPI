package stellarapi.lib.gui.text;

import stellarapi.lib.gui.IElementController;
import stellarapi.lib.gui.IFontHelper;
import stellarapi.lib.gui.IRenderer;

public interface ITextInternalController extends IElementController {

	public int maxStringLength();

	public IFontHelper getFontHelper();

	public boolean canModify();

	public boolean canLoseFocus();

	public void notifySelection(int cursor, int selection);

	/** Notifies certain text, return true to force unfocus */
	public boolean notifyText(String text, int cursor, int selection, boolean focused);

	public String updateText(String text);

	public float getCursorSpacing();

	public void setupRendererFocused(IRenderer renderer);

	public void setupText(String text, IRenderer renderer);

	public void setupHighlightedText(String selection, IRenderer renderer);

	public String setupHighlightedOverlay(String selection, IRenderer renderer);

	public String setupRendererCursor(int cursorCounter, IRenderer renderer);

	public String setupRendererUnfocused(String text, IRenderer renderer);

}
