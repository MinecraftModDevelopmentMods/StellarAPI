package stellarapi.work.basis.inspect;

import stellarapi.work.basis.compound.ICompound;

public interface IInspectBuilder {
	/**
	 * Builds the inspection compound.
	 * */
	public ICompound build();
}