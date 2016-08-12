package stellarapi.work.basis.inspect;

import java.util.UUID;

import stellarapi.work.basis.compound.ICompound;
import stellarapi.work.basis.compound.IModifiableCompound;

public class InspectionInfo {
	/** ID for this inspection. */
	private UUID inspectionId;

	/** Inspection Compound. */
	private ICompound inspectCompound;

	/** Compound for Additional Information. */
	private IModifiableCompound additionalCompound;

	public InspectionInfo(UUID id, ICompound inspectInfo, IModifiableCompound additional) {
		this.inspectionId = id;
		this.inspectCompound = inspectInfo;
		this.additionalCompound = additional;
	}

	public UUID getId() {
		return this.inspectionId;
	}

	public ICompound getInfo() {
		return this.inspectCompound;
	}

	public IModifiableCompound getAdditional() {
		return this.additionalCompound;
	}
}
