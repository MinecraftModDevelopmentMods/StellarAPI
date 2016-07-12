package stellarapi.work.basis.target;

import stellarapi.work.basis.accuracy.IAccuracyStage;
import stellarapi.work.basis.compound.ICompound;

/**
 * The target.
 * */
public interface ITarget<S extends IAccuracyStage, D> {
	/**
	 * Process for certain accurate stage.
	 * Any direction can be possible, backward or forward.
	 * */
	public void process(S stage, ICompound inspectCompound);

	/**
	 * Reads this target from the data type.
	 * */
	public void readFrom(D dataType);

	/**
	 * Writes this target to the data type.
	 * */
	public void writeTo(D dataType);

	/**
	 * Current accuracy stage of the target.
	 * */
	public S getCurrentStage();
}