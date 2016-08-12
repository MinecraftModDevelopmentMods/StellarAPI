package stellarapi.work.basis.target;

import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

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
		if(comparison.compare(stage, lockedStateMap.get(inspectionId)) > 0) {
			lockedStateMap.put(inspectionId, stage);
			if(comparison.compare(stage, target.getCurrentStage()) > 0) {
				// TODO interrupt/wait for the former process
				target.process(stage, inspect, additional);
			}
		}
	}

}