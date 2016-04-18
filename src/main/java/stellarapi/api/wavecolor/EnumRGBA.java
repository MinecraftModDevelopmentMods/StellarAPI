package stellarapi.api.wavecolor;

/**
 * Simple enumeration for RGB colors and alpha.
 * */
public enum EnumRGBA {
	Red,
	Green,
	Blue,
	
	/**
	 * Alpha for overall brightness, avoid duplication with colors when using this.
	 * */
	Alpha;
	
	/**Color part of RGBA*/
	public static final EnumRGBA[] RGB = new EnumRGBA[] {Red, Green, Blue};
}
