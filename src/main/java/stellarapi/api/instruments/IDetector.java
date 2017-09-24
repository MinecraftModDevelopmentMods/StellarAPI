package stellarapi.api.instruments;

import java.util.EnumMap;

import javax.annotation.Nullable;

import stellarapi.api.coordinates.CCoordinates;
import stellarapi.api.lib.math.Matrix4;
import stellarapi.api.lib.math.SpCoord;
import stellarapi.api.optics.EnumRGBA;
import stellarapi.api.optics.OpFilter;

/**
 * The detector, which is also an optical instrument by definition.
 * */
public interface IDetector {
	/**
	 * Checks if the display should be in accurate mode
	 *  on this detector with specified instrument.
	 * @throws IllegalArgumentException if the instrument does not fit in this detector.
	 * */
	public boolean isAccurate(@Nullable IOpticInstrument instrument);


	/**
	 * Calculates the detector standard orientation dependent to the horizontal coordinates.
	 * This gives the matrix where the detector coordinates is based on. (with its pitch/yaw applied)
	 * TODO Detector is this right? OR obtain the orientation directly
	 * */
	public Matrix4 standardOrientation(CCoordinates mountCoords);


	/**
	 * Specifies filter type for each of the rgba.
	 * Alpha-only map is provided for monochromatic detector.
	 * */
	public EnumMap<EnumRGBA, OpFilter> filterTypeMap();


	/**
	 * Maximal FOV for this object, on the detector side.
	 * */
	public double maxFOV();

	/**
	 * FOV type of this detector.
	 * */
	public FOVType getFOVType();


	/**
	 * Checks if this detector has internal focus gathering lights.
	 * */
	public boolean hasInternalFocus();

	/**
	 * Gets the internal focus in this detector.
	 * @throws UnsupportedOperationException if there is no internal focus.
	 * */
	public IOpticInstrument getInternalFocus();
}