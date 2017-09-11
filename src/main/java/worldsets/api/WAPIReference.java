package worldsets.api;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import stellarapi.api.SAPIReference;
import worldsets.api.worldset.WorldSet;
import worldsets.api.worldset.WorldSetInstance;

public enum WAPIReference {
	@Deprecated
	INSTANCE;

	// ********************************************* //
	// ************** Mod Information ************** //
	// ********************************************* //

	public static final String modid = "worldsetapi";
	public static final String version = "@WSVERSION@";

	// ********************************************* //
	// ************ WorldSet References ************ //
	// ********************************************* //

	public static final ResourceLocation WORLDSETS = new ResourceLocation(SAPIReference.modid, "worldsets");


	@ObjectHolder("overworldtype")
	public static final WorldSet overworldTypeSet = null;

	@ObjectHolder("endtype")
	public static final WorldSet endTypeSet = null;

	@ObjectHolder("nethertype")
	public static final WorldSet NetherTypeSet = null;

	// ********************************************* //
	// ************* WorldSet API Calls ************ //
	// ********************************************* //

	private IReference reference;
	private IForgeRegistry<WorldSet> registry;

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
	public static World getDefaultWorld() {
		return INSTANCE.reference.getDefaultWorld();
	}

	public static ImmutableList<WorldSet> worldSetList() {
		return ImmutableList.copyOf(INSTANCE.registry);
	}

	/**
	 * Gets the corresponding world set instance for the world set.
	 * Specified world is only used for the reference.
	 * */
	public static WorldSetInstance getWorldSetInstance(World base, WorldSet worldSet) {
		return INSTANCE.reference.getWorldInstance(base, worldSet);
	}

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

	// ********************************************* //
	// ****************** Internal ***************** //
	// ********************************************* //

	@Deprecated
	public void putReference(IReference reference) {
		this.reference = reference;
	}
}
