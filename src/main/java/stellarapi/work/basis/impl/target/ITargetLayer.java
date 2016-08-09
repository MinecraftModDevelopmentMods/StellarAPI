package stellarapi.work.basis.impl.target;

import stellarapi.work.basis.accuracy.IAccuracyStage;
import stellarapi.work.basis.compound.ICompound;
import stellarapi.work.basis.compound.IModifiableCompound;

/**
 * Layer for certain target.
 * */
public interface ITargetLayer<S extends IAccuracyStage, T extends LayeredTarget> {

	/**
	 * Populates the detail for this layer.
	 * @param target the target
	 * @param previousStage the previous stage where it was
	 * @param nextStage the next stage to be
	 * @param inspect the inspection compound
	 * @param additional the additional information about inspection from targets
	 * */
	public void populate(T target, S previousStage, S nextStage,
			ICompound inspect, IModifiableCompound additional);

	/**
	 * Removes the detail added by this layer.
	 * @param target the target
	 * @param previousStage the previous stage where it was
	 * @param nextStage the next stage to be
	 * @param inspect the inspection compound
	 * @param additional the additional information about inspection from targets
	 * */
	public void demolish(T target, S currentStage, S stage,
			ICompound inspect, IModifiableCompound additional);

}