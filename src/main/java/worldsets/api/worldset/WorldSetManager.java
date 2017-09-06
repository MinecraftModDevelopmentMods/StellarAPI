package worldsets.api.worldset;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import net.minecraft.world.World;

public enum WorldSetManager {
	@Deprecated
	INSTANCE;

	private IWorldReference reference;

	/**
	 * Gets primary worldset for this world.
	 * */
	public static @Nullable WorldSet getPrimaryWorldSet(World world) {
		return INSTANCE.reference.getPrimaryWorldSet(world);
	}

	/**
	 * Gets applied worldsets for this world.
	 * */
	public static ImmutableList<WorldSet> appliedWorldSets(World world) {
		return INSTANCE.reference.appliedWorldSets(world);
	}


	@Deprecated
	public void putReference(IWorldReference reference) {
		this.reference = reference;
	}

}
