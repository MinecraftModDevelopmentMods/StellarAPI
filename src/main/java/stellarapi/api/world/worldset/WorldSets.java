package stellarapi.api.world.worldset;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import stellarapi.api.IReference;

/**
 * Reference for WorldSets.
 * */
public class WorldSets {
	public static final ResourceLocation VANILLA_FACTORY = new ResourceLocation("basics");
	public static final ResourceLocation NAMED_WORLDSET_FACTORY = new ResourceLocation("named");

	/** Can only be used after Init phase of this API. */
	public static WorldSet exactOverworld() {
		return reference.getGeneratedWorldSets(VANILLA_FACTORY)[0];
	}

	/** Can only be used after Init phase of this API. */
	public static WorldSet overworldType() {
		return reference.getGeneratedWorldSets(VANILLA_FACTORY)[1];
	}

	/** Can only be used after Init phase of this API. */
	public static WorldSet endType() {
		return reference.getGeneratedWorldSets(VANILLA_FACTORY)[2];
	}

	/** Can only be used after Init phase of this API. */
	public static WorldSet netherType() {
		return reference.getGeneratedWorldSets(VANILLA_FACTORY)[3];
	}

	/**
	 * Gets the list of the world sets.
	 * */
	public static ImmutableList<WorldSet> getAllWorldSets() {
		return reference.getAllWorldSets();
	}

	/**
	 * Gets primary WorldSet for this world.
	 * */
	public static @Nullable WorldSet getPrimaryWorldSet(World world) {
		return reference.getPrimaryWorldSet(world);
	}

	/**
	 * Gets applied WorldSets for this world. WorldSet with higher priority comes first.
	 * */
	public static ImmutableList<WorldSet> appliedWorldSets(World world) {
		return reference.appliedWorldSets(world);
	}


	// ********************************************* //
	// ****************** Internal ***************** //
	// ********************************************* //

	private static IWorldSetReference reference;

	@Deprecated
	public static void putReference(IWorldSetReference base) {
		reference = base;
	}
}
