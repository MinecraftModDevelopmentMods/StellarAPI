package stellarapi.work.basis.restriction;

public interface IRestrictionConditioner<R, V> {
	/**
	 * Condition the restriction with certain value.
	 * @param restriction the restriction to condition
	 * @param value the value to be conditioned
	 * @return the conditioned restriction
	 * */
	public R condition(R restriction, V value);

}