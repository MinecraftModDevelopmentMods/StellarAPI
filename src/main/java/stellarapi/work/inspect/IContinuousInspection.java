package stellarapi.work.inspect;

public interface IContinuousInspection<R, C> extends IInspection<R, C> {

	/**
	 * Begins continuous inspection.
	 * */
	public void begin(R initialRestriction);

	/**
	 * Ends continuous inspection.
	 * */
	public void end();

}