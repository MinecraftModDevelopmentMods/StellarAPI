package stellarapi.work.basis.compound;

public interface IModifiableCompound extends ICompound {

	public <E> E putElement(ElementType<E> type, E element);

}