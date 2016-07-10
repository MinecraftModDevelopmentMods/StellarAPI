package stellarapi.work.basis.target;

public interface IInternalLogic<I,C> {
	// TODO better inspection format, with threading
	public void onInspection(I inspection, C callback);
}