package stellarapi.work.basis.compound;

public interface ICompound {

	/**
	 * Checks if certain type is included in this compound.
	 * */
	public <E> boolean hasElement(ElementType<E> type);

	/**
	 * Gets element for certain type.
	 * */
	public <E> E getElement(ElementType<E> type);
}