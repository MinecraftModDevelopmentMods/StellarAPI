package stellarapi.feature.gui.overlay;

import stellarapi.api.SAPIReferences;
import stellarapi.api.gui.overlay.IOverlaySetType;
import stellarapi.api.gui.overlay.IRawOverlayElement;

public class OverlaySetStellarAPI implements IOverlaySetType {

	@Override
	public String getLanguageKey() {
		return "gui.overlay.stellarapi";
	}

	@Override
	public boolean acceptOverlayByDefault(IRawOverlayElement overlay) {
		return overlay.getModId().equals(SAPIReferences.MODID);
	}

	@Override
	public boolean isMain() {
		return false;
	}

}
