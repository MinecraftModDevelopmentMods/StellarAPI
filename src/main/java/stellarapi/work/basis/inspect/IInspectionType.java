package stellarapi.work.basis.inspect;

import stellarapi.work.basis.collect.ICallbackBuilder;

public interface IInspectionType<B extends IInspectBuilder, C extends ICallbackBuilder> {

	/**
	 * Creates and gives the inspection compound builder.
	 * */
	public B inspectionBuilder();

	/**
	 * Creates and gives the callback builder.
	 * */
	public C callbackBuilder();

}