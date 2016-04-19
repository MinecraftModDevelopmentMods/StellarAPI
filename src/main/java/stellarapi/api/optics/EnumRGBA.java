package stellarapi.api.optics;

import java.awt.Color;

/**
 * Simple enumeration for RGB colors and alpha.
 * */
public enum EnumRGBA {
	Red(Color.RED),
	Green(Color.GREEN),
	Blue(Color.BLUE),
	
	/**
	 * Alpha for overall brightness, avoid duplication with colors when using this.
	 * */
	Alpha(Color.WHITE);
	
	/**Color part of RGBA*/
	public static final EnumRGBA[] RGB = new EnumRGBA[] {Red, Green, Blue};
	
	private Color color;
	
	EnumRGBA(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return this.color;
	}
}
