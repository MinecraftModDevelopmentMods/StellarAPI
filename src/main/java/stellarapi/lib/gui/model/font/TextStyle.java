package stellarapi.lib.gui.model.font;

public class TextStyle {
	private boolean shadedStyle = false;
	private boolean boldStyle = false;
	private boolean italicStyle = false;
	private boolean underlineStyle = false;
	private boolean strikethroughStyle = false;

	public TextStyle() { }

	public TextStyle setShaded(boolean shadedStyle) {
		this.shadedStyle = shadedStyle;
		return this;
	}

	public TextStyle setBold(boolean boldStyle) {
		this.boldStyle = boldStyle;
		return this;
	}

	public TextStyle setItalic(boolean italicStyle) {
		this.italicStyle = italicStyle;
		return this;
	}

	public TextStyle setUnderlined(boolean underlineStyle) {
		this.underlineStyle = underlineStyle;
		return this;
	}

	public TextStyle setStrikeThrough(boolean strikethroughStyle) {
		this.strikethroughStyle = strikethroughStyle;
		return this;
	}

	public boolean isShaded() {
		return this.shadedStyle;
	}

	public boolean isBold() {
		return this.boldStyle;
	}

	public boolean isItalic() {
		return this.italicStyle;
	}

	public boolean isUnderlined() {
		return this.underlineStyle;
	}

	public boolean isStrikeThrough() {
		return this.strikethroughStyle;
	}

	public void reset() {
		this.shadedStyle = this.boldStyle = this.italicStyle
				= this.underlineStyle = this.strikethroughStyle = false;
	}

	public void set(TextStyle newStyle) {
		this.shadedStyle = newStyle.shadedStyle;
		this.boldStyle = newStyle.boldStyle;
		this.italicStyle = newStyle.italicStyle;
		this.underlineStyle = newStyle.underlineStyle;
		this.strikethroughStyle = newStyle.strikethroughStyle;
	}
}
