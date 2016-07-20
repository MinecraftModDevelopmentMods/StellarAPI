package stellarapi.work.basis.target;

import stellarapi.work.basis.compound.ICompound;

public interface IInspectLogic<T extends ITarget, C> {
	/**
	 * Process inspection.
	 * @param target the inspection target
	 * @param inspectCompound the inspection compound
	 * @param callback the callback to accept the result
	 * */
	public void process(T target, ICompound inspectCompound, C callback);
}