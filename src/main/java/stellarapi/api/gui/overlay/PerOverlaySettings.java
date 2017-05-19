package stellarapi.api.gui.overlay;

import net.minecraftforge.common.config.Configuration;
import stellarapi.api.gui.pos.EnumHorizontalPos;
import stellarapi.api.gui.pos.EnumVerticalPos;
import stellarapi.api.lib.config.SimpleConfigHandler;
import stellarapi.api.lib.config.property.ConfigPropertyBoolean;
import stellarapi.api.lib.config.property.ConfigPropertyString;

public class PerOverlaySettings extends SimpleConfigHandler {

	private EnumHorizontalPos horizontal;
	private EnumVerticalPos vertical;
	private boolean visibleOnMain;
	private boolean canOnMain;

	private ConfigPropertyString propHorizontal;
	private ConfigPropertyString propVertical;
	private ConfigPropertyBoolean propVisibleOnMain;

	void initializeSettings(EnumHorizontalPos horizontal, EnumVerticalPos vertical, boolean canOnMain) {
		this.setHorizontal(horizontal);
		this.setVertical(vertical);
		this.visibleOnMain = this.canOnMain;

		this.propHorizontal = new ConfigPropertyString("Horizontal_Position", "", horizontal.name());
		this.propVertical = new ConfigPropertyString("Vertical_Position", "", vertical.name());
		this.propVisibleOnMain = new ConfigPropertyBoolean("Visible_On_Main", "", canOnMain);

		this.addConfigProperty(this.propHorizontal);
		this.addConfigProperty(this.propVertical);
		this.addConfigProperty(this.propVisibleOnMain);
	}

	@Override
	public void setupConfig(Configuration config, String category) {
		super.setupConfig(config, category);

		propHorizontal.setValidValues(EnumHorizontalPos.names);
		propHorizontal.setComment("Horizontal Position on the Overlay.");
		propHorizontal.setRequiresMcRestart(false);
		// propHorizontal.setLanguageKey("config.property.gui.pos.horizontal");

		propVertical.setValidValues(EnumVerticalPos.names);
		propVertical.setComment("Vertical Position on the Overlay.");
		propVertical.setRequiresMcRestart(false);
		// propVertical.setLanguageKey("config.property.gui.pos.vertical");

		propVisibleOnMain.setComment("Visibility on Main Overlay.");
		propVisibleOnMain.setRequiresMcRestart(false);
	}

	@Override
	public void loadFromConfig(Configuration config, String category) {
		super.loadFromConfig(config, category);
		this.setHorizontal(EnumHorizontalPos.valueOf(propHorizontal.getString()));
		this.setVertical(EnumVerticalPos.valueOf(propVertical.getString()));
		this.visibleOnMain = propVisibleOnMain.getBoolean();
	}

	@Override
	public void saveToConfig(Configuration config, String category) {
		propHorizontal.setString(getHorizontal().name());
		propVertical.setString(getVertical().name());
		propVisibleOnMain.setBoolean(this.visibleOnMain);
		super.saveToConfig(config, category);
	}

	public EnumHorizontalPos getHorizontal() {
		return horizontal;
	}

	public void setHorizontal(EnumHorizontalPos horizontal) {
		this.horizontal = horizontal;
	}

	public EnumVerticalPos getVertical() {
		return vertical;
	}

	public void setVertical(EnumVerticalPos vertical) {
		this.vertical = vertical;
	}

	public boolean isVisibleOnMain() {
		return this.visibleOnMain;
	}

	public void setVisibleOnMain(boolean visible) {
		this.visibleOnMain = visible;
	}

}
