package stellarapi.work.basis.target;

import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;

import stellarapi.work.basis.accuracy.AccuracyHelper;
import stellarapi.work.basis.accuracy.IAccuracyStage;
import stellarapi.work.basis.compound.ICompound;
import stellarapi.work.basis.compound.IModifiableCompound;

public class TargetLock<S extends IAccuracyStage, T extends ITarget<S>> {

	// TODO implementation of the target lock
	private Ordering<S> comparison = AccuracyHelper.getOrdering();
	private Map<UUID, S> lockedStateMap = Maps.newHashMap();

	private S evaluatedMax;
	private int maxNum = 1;

	private T target;

	/**
	 * Sets up the stage.
	 * Any direction can be possible, backward or forward.
	 * @param inspectionId the unique id for the inspection
	 * @param stage the accuracy stage to be proceeded
	 * @param inspect the inspection compound
	 * @param additional the additional compound containing current information
	 * */
	public void setup(UUID inspectionId, @Nullable S stage, ICompound inspect, IModifiableCompound additional) {
		S previous = lockedStateMap.put(inspectionId, stage);
		int thanMax = comparison.compare(stage, this.evaluatedMax);
		int thanPrevious = comparison.compare(stage, previous);

		if(thanMax >= 0) {
			// TODO interrupt/wait for the former process
			target.process(stage, inspect, additional);
			if(thanMax != 0)
				this.maxNum = 1;

			if(thanPrevious > 0)
				this.maxNum ++;
		}

		if(Objects.equal(previous, this.evaluatedMax) && thanPrevious < 0 && this.maxNum <= 1) {
			this.evaluatedMax = null;
			this.maxNum = 1;
			for(S ite : lockedStateMap.values()) {
				thanMax = comparison.compare(ite, this.evaluatedMax);
				if(thanMax > 0) {
					this.evaluatedMax = ite;
					this.maxNum = 1;
				} else if(thanMax == 0)
					this.maxNum ++;
			}
		}
	}

}