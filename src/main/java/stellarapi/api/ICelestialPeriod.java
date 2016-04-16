package stellarapi.api;

public interface ICelestialPeriod {
	
	/**
	 * Name of this period.
	 * */
	public String getPeriodName();
	
	/**
	 * Length of the period in tick.
	 * */
	public double getPeriodLength();
	
	/**
	 * Calculates offset for certain time.
	 * @param worldTime the time of the world in tick
	 * @param partialTicks the partial tick
	 * @return the offset for this period on the time
	 * */
	public double getOffset(long worldTime, float partialTicks);
	
	/**
	 * Calculates time for certain offset after certain time.
	 * @param timeFrom the certain time before the time with the offset
	 * @param offset the offset for this period
	 * @return time in tick for the offset
	 * */
	public long getTimeForOffset(long timeFrom, double offset);
}
