package stellarapi.api.viewer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import stellarapi.api.instruments.IDetector;
import stellarapi.api.instruments.IMount;
import stellarapi.api.instruments.IOpticFilter;
import stellarapi.api.instruments.IOpticInstrument;

/**
 * Capability for the view of the certain player.
 * On server, default implementation regards the spectating entity as the viewer entity.
 * Can only be attached to the server entities and the client main player.
 * */
public interface IPlayerView {
	/**
	 * Gets the scope.
	 * */
	public @Nullable IOpticInstrument getInstrument();

	/**
	 * Gets the filter.
	 * */
	public @Nullable IOpticFilter getFilter();

	/**
	 * Gets the mount.
	 * */
	public @Nonnull IMount getMount();

	/**
	 * Gets the detector.
	 * */
	public @Nonnull IDetector getEyeDetector();
}