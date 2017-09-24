package stellarapi.api.viewer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import stellarapi.api.instruments.IDetector;
import stellarapi.api.instruments.IMount;
import stellarapi.api.instruments.IOpticFilter;
import stellarapi.api.instruments.IOpticInstrument;

/**
 * Optical viewer entity. This determines the optical properties on the viewer side.
 * */
public interface IOpticalViewer {
	/**
	 * Obtain the instruments.
	 * */
	public @Nullable IOpticInstrument getInstrument(EntityPlayer origin);

	/**
	 * Obtain the filter.
	 * */
	public @Nullable IOpticFilter getFilter(EntityPlayer origin);

	/**
	 * Obtain the mount.
	 * */
	public @Nonnull IMount getMount(EntityPlayer origin);

	/**
	 * Obtain the detector.
	 * */
	public @Nonnull IDetector getEyeDetector(EntityPlayer origin);
}