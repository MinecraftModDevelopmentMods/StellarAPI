package stellarapi.feature.gui.overlay;

import stellarapi.api.gui.overlay.IOverlaySetType;
import stellarapi.api.gui.overlay.IRawOverlayElement;
import stellarapi.mod.StellarAPI;

public class OverlaySetStellarAPI implements IOverlaySetType {

	@Override
	public String getLanguageKey() {
		return "gui.overlay.stellarapi";
	}

	@Override
	public boolean acceptOverlayByDefault(IRawOverlayElement overlay) {
		return overlay.getModId().equals(StellarAPI.modid);
	}

	@Override
	public boolean isMain() {
		return false;
	}

}
