package stellarapi.api.event.settings;

import javax.annotation.Nonnull;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;
import stellarapi.api.celestials.CelestialType;
import worldsets.api.worldset.WorldSet;

/**
 * Event fired to determine what ID to select.
 * */
public class ApplyProviderIDEvent extends Event {

	public final World world;
	public final WorldSet worldSet;

	/**
	 * Previous ID which is either stored in the save, or ID from settings if it's invalid.
	 * */
	public final ResourceLocation previousID;

	/**
	 * The ID as a result. By default this is from settings.
	 * */
	public @Nonnull ResourceLocation resultID;

	public ApplyProviderIDEvent(World world, WorldSet worldSet,
			ResourceLocation current, ResourceLocation settingsID) {
		this.world = world;
		this.worldSet = worldSet;

		this.previousID = current;
		this.resultID = settingsID;
	}

	/** Coordinates case */
	public static class Coordinates extends ApplyProviderIDEvent {
		public Coordinates(World world, WorldSet worldSet,
				ResourceLocation current, ResourceLocation settingsID) {
			super(world, worldSet, current, settingsID);
		}
	}

	/** Celestials case */
	public static class Celestials extends ApplyProviderIDEvent {
		/** The celestial type */
		public final CelestialType celType;
		public Celestials(World world, WorldSet worldSet, CelestialType type,
				ResourceLocation current, ResourceLocation settingsID) {
			super(world, worldSet, current, settingsID);
			this.celType = type;
		}
	}

	/** Atmosphere case */
	public static class Atmosphere extends ApplyProviderIDEvent {
		public Atmosphere(World world, WorldSet worldSet,
				ResourceLocation current, ResourceLocation settingsID) {
			super(world, worldSet, current, settingsID);
		}
	}
}
