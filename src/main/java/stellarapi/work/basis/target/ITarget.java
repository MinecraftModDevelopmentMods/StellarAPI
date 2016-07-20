package stellarapi.work.basis.target;

import stellarapi.work.basis.accuracy.IAccuracyStage;
import stellarapi.work.basis.compound.ICompound;
import stellarapi.work.basis.compound.IModifiableCompound;

/**
 * The target.
 * */
public interface ITarget<S extends IAccuracyStage> {
	/**
	 * Process for certain accurate stage.
	 * Any direction can be possible, backward or forward.
	 * @param stage the accuracy stage to be proceeded
	 * @param inspect the inspection compound
	 * @param additional the additional compound containing current information
	 * */
	public void process(S stage, ICompound inspect, IModifiableCompound additional);

	/**
	 * Current accuracy stage of the target.
	 * */
	public S getCurrentStage();

	/**
	 * Change check flag which might be used for data management.
	 * */
	public boolean isChanged();
}