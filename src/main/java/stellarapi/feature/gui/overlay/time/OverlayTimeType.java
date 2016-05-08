package stellarapi.feature.gui.overlay.time;

import stellarapi.api.gui.overlay.IOverlayType;
import stellarapi.api.gui.overlay.IRawHandler;
import stellarapi.api.gui.overlay.PerOverlaySettings;
import stellarapi.api.gui.pos.EnumHorizontalPos;
import stellarapi.api.gui.pos.EnumVerticalPos;

public class OverlayTimeType implements IOverlayType<OverlayTime, PerOverlaySettings> {
	
	@Override
	public OverlayTime generateElement() {
		return new OverlayTime();
	}

	@Override
	public PerOverlaySettings generateSettings() {
		return new PerOverlaySettings();
	}

	@Override
	public String getName() {
		return "Position";
	}
	
	@Override
	public String overlayType() {
		return "Time";
	}

	@Override
	public EnumHorizontalPos defaultHorizontalPos() {
		return EnumHorizontalPos.RIGHT;
	}

	@Override
	public EnumVerticalPos defaultVerticalPos() {
		return EnumVerticalPos.UP;
	}

	@Override
	public boolean accepts(EnumHorizontalPos horizontal, EnumVerticalPos vertical) {
		return true;
	}


	@Override
	public IRawHandler<OverlayTime> generateRawHandler() {
		return null;
	}

}
