package stellarapi.api.event.settings;

import javax.annotation.Nonnull;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.Event;
import stellarapi.api.celestials.CelestialType;
import worldsets.api.worldset.WorldSet;

/**
 * Event fired to determine what ID to select.
 * */
public class ApplyProviderIDEvent extends Event {

	public final WorldSet theWorldSet;

	/**
	 * Previous ID which is either stored in the save, or ID from settings if it's invalid.
	 * */
	public final ResourceLocation previousID;

	/**
	 * The ID as a result. By default this is from settings.
	 * */
	public @Nonnull ResourceLocation resultID;

	public ApplyProviderIDEvent(WorldSet worldSet, ResourceLocation current, ResourceLocation settingsID) {
		this.theWorldSet = worldSet;

		this.previousID = current;
		this.resultID = settingsID;
	}

	/** Coordinates case */
	public static class Coordinates extends ApplyProviderIDEvent {
		public Coordinates(WorldSet worldSet, ResourceLocation current, ResourceLocation settingsID) {
			super(worldSet, current, settingsID);
		}
	}

	/** Celestials case */
	public static class Celestials extends ApplyProviderIDEvent {
		/** The celestial type */
		public final CelestialType celType;
		public Celestials(WorldSet worldSet, CelestialType type, ResourceLocation current, ResourceLocation settingsID) {
			super(worldSet, current, settingsID);
			this.celType = type;
		}
	}

	/** Atmosphere case */
	public static class Atmosphere extends ApplyProviderIDEvent {
		public Atmosphere(WorldSet worldSet, ResourceLocation current, ResourceLocation settingsID) {
			super(worldSet, current, settingsID);
		}
	}
}
