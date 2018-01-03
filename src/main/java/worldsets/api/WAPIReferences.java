package worldsets.api;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import worldsets.api.worldset.WorldSet;

/**
 * Central reference for WorldSet API.
 * */
public final class WAPIReferences {

	// ********************************************* //
	// ************** Mod Information ************** //
	// ********************************************* //

	public static final String MODID = "worldsetapi";
	public static final String VERSION = "@WSVERSION@";
	public static final String APIID = "worldsetapi|api";

	// ********************************************* //
	// ************ WorldSet References ************ //
	// ********************************************* //

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

	// ********************************************* //
	// ************* WorldSet API Calls ************ //
	// ********************************************* //
	private static final WAPIReferences INSTANCE = new WAPIReferences();

	/**
	 * Checks if this world is default.
	 * On server, this checks for overworld.
	 * On client, this checks for the main loaded world.
	 * */
	public static boolean isDefaultWorld(World world) {
		if(world.isRemote)
			return true;
		return world.provider.getDimension() == 0;
	}

	/**
	 * Get one of the default worlds.
	 * */
	public static World getDefaultWorld(boolean isRemote) {
		return reference.getDefaultWorld(isRemote);
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

	private static IReference reference;

	@Deprecated
	public static void putReference(IReference base) {
		reference = base;
	}
}
