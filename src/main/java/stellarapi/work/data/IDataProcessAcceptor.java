package stellarapi.work.data;

/**
 * Data process acceptor to build result data.
 * */
public interface IDataProcessAcceptor<F extends IDataFormat> {
	public IDataResult<F> getResult();
}