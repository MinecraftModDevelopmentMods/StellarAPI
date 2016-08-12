package stellarapi.work.optics.inspection;

import stellarapi.work.basis.collect.ICallbackBuilder;
import stellarapi.work.basis.compound.ICompound;
import stellarapi.work.basis.inspect.IInspectBuilder;
import stellarapi.work.basis.inspect.IInspectionType;

public class OpticalInspection3d implements IInspectionType<Inspect3dBuilder, Callback3dBuilder> {

	public static final OpticalInspection3d INSPECTION_3D = new OpticalInspection3d();

	private OpticalInspection3d() { }

	@Override
	public Inspect3dBuilder inspectionBuilder() {
		return null;
	}

	@Override
	public Callback3dBuilder callbackBuilder() {
		return null;
	}

}
