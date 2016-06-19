package stellarapi.impl;

import com.google.common.collect.ImmutableSet;

import net.minecraft.world.World;
import stellarapi.api.celestials.EnumCelestialCollectionType;
import stellarapi.api.celestials.ICelestialCollection;
import stellarapi.api.celestials.ICelestialObject;
import stellarapi.api.lib.math.SpCoord;

public class DefaultCollectionVanilla implements ICelestialCollection {

	public final DefaultSun sun;
	public final DefaultMoon moon;

	public DefaultCollectionVanilla(World world) {
		this.sun = new DefaultSun(world);
		this.moon = new DefaultMoon(world);
	}

	@Override
	public String getName() {
		return "Vanilla";
	}

	@Override
	public ImmutableSet<ICelestialObject> getObjects() {
		return ImmutableSet.of(sun, moon);
	}

	@Override
	public ImmutableSet<ICelestialObject> getObjectInRange(SpCoord pos, double radius) {
		ImmutableSet.Builder<ICelestialObject> builder = ImmutableSet.builder();
		if (pos.distanceTo(sun.getCurrentHorizontalPos()) < radius)
			builder.add(sun);
		if (pos.distanceTo(moon.getCurrentHorizontalPos()) < radius)
			builder.add(moon);
		return builder.build();
	}

	@Override
	public ICelestialObject getNearerObject(SpCoord pos, ICelestialObject obj1, ICelestialObject obj2) {
		return (pos.distanceTo(obj1.getCurrentHorizontalPos()) < pos.distanceTo(obj2.getCurrentHorizontalPos())) ? obj1
				: obj2;
	}

	@Override
	public int searchOrder() {
		return 0;
	}

	@Override
	public boolean isBackground() {
		return false;
	}

	@Override
	public EnumCelestialCollectionType getCollectionType() {
		return EnumCelestialCollectionType.System;
	}

}
