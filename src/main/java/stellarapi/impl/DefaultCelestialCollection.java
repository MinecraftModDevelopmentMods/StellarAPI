package stellarapi.impl;

import com.google.common.collect.ImmutableSet;

import stellarapi.api.celestials.EnumCelestialCollectionType;
import stellarapi.api.celestials.ICelestialCollection;
import stellarapi.api.celestials.ICelestialObject;
import stellarapi.api.lib.math.SpCoord;

public class DefaultCelestialCollection implements ICelestialCollection {

	@Override
	public String getName() {
		return "Vanilla";
	}

	@Override
	public ImmutableSet<? extends ICelestialObject> getObjects() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImmutableSet<? extends ICelestialObject> getObjectInRange(SpCoord pos, double radius) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ICelestialObject getNearerObject(SpCoord pos, ICelestialObject obj1, ICelestialObject obj2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int searchOrder() {
		return 0;
	}

	@Override
	public boolean isBackground() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public EnumCelestialCollectionType getCollectionType() {
		// TODO Auto-generated method stub
		return null;
	}

}
