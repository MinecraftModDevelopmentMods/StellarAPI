package stellarapi.work.basis.target;

import stellarapi.work.basis.inspect.IInspectionType;

public interface ITargetHandler<T extends ITarget> {

	/**
	 * Gets the internal logic for the target.
	 * @param target the target
	 * @param type the inspection type
	 * */
	public <I,C> IInternalLogic<I,C> getInternalLogic(T target, IInspectionType type);

}