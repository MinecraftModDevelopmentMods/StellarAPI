package stellarapi.work.basis.compound;

public interface IModifiableCompound extends ICompound {

	public <E> E putElement(ElementType<E> type, E element);
	public <E> E removeElement(ElementType<E> type);

}