package stellarapi.work.basis.target;

public interface IDataLogic<T extends ITarget, D> {
	
	/**
	 * Reads data and apply it to the instance.
	 * @param instance the instance
	 * @param data the data to read
	 * */
	public void read(T instance, D data);

	/**
	 * Writes data for the instance.
	 * @param instance the instance
	 * @param data the data to write
	 * */
	public void write(T instance, D data);

}