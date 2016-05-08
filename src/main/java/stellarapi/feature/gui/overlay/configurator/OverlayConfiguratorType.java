package stellarapi.feature.gui.overlay.configurator;

import stellarapi.api.gui.overlay.IOverlayType;
import stellarapi.api.gui.overlay.IRawHandler;
import stellarapi.api.gui.overlay.PerOverlaySettings;
import stellarapi.api.gui.pos.EnumHorizontalPos;
import stellarapi.api.gui.pos.EnumVerticalPos;

public class OverlayConfiguratorType implements IOverlayType<OverlayConfigurator, PerOverlaySettings> {
	
	@Override
	public OverlayConfigurator generateElement() {
		return new OverlayConfigurator();
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
		return "Position";
	}

	@Override
	public EnumHorizontalPos defaultHorizontalPos() {
		return EnumHorizontalPos.RIGHT;
	}

	@Override
	public EnumVerticalPos defaultVerticalPos() {
		return EnumVerticalPos.CENTER;
	}

	@Override
	public boolean accepts(EnumHorizontalPos horizontal, EnumVerticalPos vertical) {
		return horizontal == EnumHorizontalPos.RIGHT && vertical == EnumVerticalPos.CENTER;
	}


	@Override
	public IRawHandler<OverlayConfigurator> generateRawHandler() {
		return new OverlayConfiguratorHandler();
	}
	
	@Override
	public boolean isUniversal() {
		return true;
	}

}
