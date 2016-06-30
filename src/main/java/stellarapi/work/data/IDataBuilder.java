package stellarapi.work.data;

/**
 * Data builder to build result data.
 * */
public interface IDataBuilder<F extends IDataFormat> {
	/**
	 * Build result with current data.
	 * */
	public IDataResult<F> buildResult();
}