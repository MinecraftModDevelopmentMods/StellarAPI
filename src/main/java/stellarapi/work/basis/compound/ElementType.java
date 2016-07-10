package stellarapi.work.basis.compound;

public class ElementType<E> {

	@SuppressWarnings("unchecked")
	public <T> T unsafeCast(E element) {
		return (T) element;
	}

}