package stellarapi.work.basis.inspect;

/**
 * Certain type of inspection.
 * */
public interface IInspection<R, C> {
	public void inspect(R restriction, C callback);
}