package stellarapi.api.instruments;

/**
 * Interface for optic instrument like mirrors and lenses.
 * */
public interface IOpticInstrument {
	// TODO OpticalInstr fill in these
	/**
	 * Effective diameter in millimeters.
	 * e.g. eye has diameter of 5mm.
	 * 
	 * Only diameter of the farthest instrument from eye counts.
	 * */
	public double effectiveDiameter();

	/**
	 * Magnification factor.
	 * What this means is dependent on its type.
	 * 
	 * It's scale factor for focus,
	 * magnification factor for scope,
	 * 1/(reverse scale factor) for disperse,
	 * ratio of image size for propagate.
	 * */
	public double magFactor();

	/**
	 * Max FOV of this instrument, on the latter side(close to the detector).
	 * The unit depends on the type - parallel : fov , focused : fov .
	 * */
	public double maxFOV();

	/**
	 * FOV type of this instrument.
	 * */
	public FOVType getFOVType();

	/**
	 * Checks if this instrument is mirror or not.
	 * Mirror reflects the image.
	 * */
	public boolean isMirror();

	/**
	 * If the FOV should be manually changed or not.
	 * */
	public boolean needManualFOVChange();

	/**
	 * Gets the instrument type.
	 * */
	public EnumOpticInstrType getType();


	public static IOpticInstrument mergeInstruments(IOpticInstrument farther, IOpticInstrument nearer) {
		// TODO OpticInstrument Fill in this merge method.
		return null;
	}

	static class MergedInstrument implements IOpticInstrument {
	
		private IOpticInstrument instr1, instr2;
		private EnumOpticInstrType type;

		@Override
		public double effectiveDiameter() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public double magFactor() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public double maxFOV() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public FOVType getFOVType() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean isMirror() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean needManualFOVChange() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public EnumOpticInstrType getType() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
}
