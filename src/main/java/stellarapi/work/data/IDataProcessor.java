package stellarapi.work.data;

public interface IDataProcessor {

	/**
	 * Gets the next data processor.
	 * */
	public IDataProcessor nextProcessor();

	public void processRestriction(IDataRestriction restriction);

	public void processBuilder(IDataBuilder builder);

}