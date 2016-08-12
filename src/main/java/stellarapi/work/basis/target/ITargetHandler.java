package stellarapi.work.basis.target;

import stellarapi.work.basis.data.IDataType;
import stellarapi.work.basis.inspect.IInspectionType;

public interface ITargetHandler<T extends ITarget> {
	// TODO rewrite, instantiation, state lock

	/**
	 * Gets the inspection logic for the target.
	 * @param target the target
	 * @param type the inspection type
	 * */
	public <C> IInspectLogic<T, C> getInternalLogic(T target, IInspectionType type);

	/**
	 * Gets the data logic for the target.
	 * @param target the target
	 * @param type the data type
	 * */
	public <D> IDataLogic<T, D> getDataLogic(T target, IDataType type);

}