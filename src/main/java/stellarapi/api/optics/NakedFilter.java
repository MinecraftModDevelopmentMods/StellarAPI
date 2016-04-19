package stellarapi.api.optics;

import com.google.common.collect.ImmutableList;

/**
 * Filter settings for naked eye.
 * */
public class NakedFilter extends RGBFilter {

	@Override
	public boolean isRGB() {
		return true;
	}

	@Override
	public double getFilterEfficiency(EnumRGBA color) {
		return 1.0;
	}

}
