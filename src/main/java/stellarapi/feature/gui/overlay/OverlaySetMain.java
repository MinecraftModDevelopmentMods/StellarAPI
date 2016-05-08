package stellarapi.feature.gui.overlay;

import stellarapi.api.gui.overlay.IRawOverlayElement;
import stellarapi.api.gui.overlay.IOverlaySetType;

public class OverlaySetMain implements IOverlaySetType {

	@Override
	public String getLanguageKey() {
		return "gui.overlay.main";
	}

	@Override
	public boolean acceptOverlayByDefault(IRawOverlayElement overlay) {
		return true;
	}

}
