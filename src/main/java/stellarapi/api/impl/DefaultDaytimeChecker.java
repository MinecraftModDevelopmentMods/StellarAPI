package stellarapi.api.impl;

import stellarapi.api.CelestialLightSources;
import stellarapi.api.ICelestialCoordinate;
import stellarapi.api.mc.EnumDaytimeDescriptor;
import stellarapi.api.mc.IDaytimeChecker;

public class DefaultDaytimeChecker implements IDaytimeChecker {

	@Override
	public boolean isDescriptorApply(CelestialLightSources sources, ICelestialCoordinate coordinate,
			EnumDaytimeDescriptor descriptor, long time, int tolerance) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long timeForCertainDescriptor(CelestialLightSources sources, ICelestialCoordinate coordinate,
			EnumDaytimeDescriptor descriptor) {
		// TODO Auto-generated method stub
		return 0;
	}

}
