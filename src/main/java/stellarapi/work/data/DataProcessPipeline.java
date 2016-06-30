package stellarapi.work.data;

import stellarapi.work.celestial.ICelestialUnit;

public class DataProcessPipeline {
	
	public void process(IDataProcessor processor, IDataRestriction restriction, IDataBuilder builder) {
		processor.processRestriction(restriction);
		this.process(processor.nextProcessor(), restriction, builder);
		processor.processBuilder(builder);
	}

}