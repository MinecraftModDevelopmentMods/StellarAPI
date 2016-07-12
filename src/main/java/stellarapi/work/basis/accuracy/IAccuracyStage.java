package stellarapi.work.basis.accuracy;

/**
 * Discrete State which reflects the accuracy.
 * <code>null</code> means the unprocessed stage.
 * */
public interface IAccuracyStage<F extends IAccuracyFactor> {

	/**
	 * Accuracy factor class
	 * */
	public Class<? extends F> factorClass();

	/**
	 * The limit for the accuracy
	 * */
	public F limit();
}