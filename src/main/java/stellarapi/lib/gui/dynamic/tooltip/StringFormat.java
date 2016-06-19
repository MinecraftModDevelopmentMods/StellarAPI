package stellarapi.lib.gui.dynamic.tooltip;

import net.minecraft.client.resources.I18n;

public class StringFormat {

	private String main;
	private Object[] args;

	public StringFormat(String main, Object... args) {
		this.main = main;
		this.args = args;
	}

	public String getMain() {
		return this.main;
	}

	public Object[] getArguments() {
		return this.args;
	}

	public String toString() {
		return String.format(this.main, this.args);
	}

	public String getLocalizedString() {
		return I18n.format(this.main, this.args);
	}

}
