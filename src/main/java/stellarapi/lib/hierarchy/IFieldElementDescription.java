package stellarapi.lib.hierarchy;

import stellarapi.lib.hierarchy.structure.IHierarchyStructure;

public interface IFieldElementDescription {
	public Class<?> getElementType();
	public IHierarchyStructure getStructure();
}