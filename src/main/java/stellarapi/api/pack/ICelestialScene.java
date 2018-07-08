package stellarapi.api.pack;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import stellarapi.api.IAtmosphereEffect;
import stellarapi.api.celestials.ICelestialCollection;
import stellarapi.api.celestials.ICelestialCoordinates;
import stellarapi.api.celestials.ICelestialObject;
import stellarapi.api.celestials.IEffectorType;
import stellarapi.api.render.IAdaptiveRenderer;
import stellarapi.api.world.ICelestialHelper;

/**
 * Actual implementation of celestial scene for each world.
 * */
public interface ICelestialScene extends INBTSerializable<NBTTagCompound> {

	/**
	 * Prepare data for collection registry, coordinates and sky effect.
	 * */
	public void prepare();

	/**
	 * Register celestial collections here.
	 * */
	public void onRegisterCollection(Consumer<ICelestialCollection> colRegistry,
			BiConsumer<IEffectorType, ICelestialObject> effRegistry);

	/**
	 * Creates coordinates, or returns <code>null</code> if this pack doesn't provide coordinates.
	 * Nonnull for now.
	 * TODO Refactor on 1.13
	 * */
	public ICelestialCoordinates createCoordinates();

	/**
	 * Creates sky effect, or returns <code>null</code> if this pack doesn't provide sky effect.
	 * Nonnull for now.
	 * */
	public IAtmosphereEffect createAtmosphereEffect();

	/**
	 * Creates celestial helper for world provider.
	 * Return <code>null</code> to not replace the world provider.
	 * */
	public @Nullable ICelestialHelper createCelestialHelper();

	/** Creates the sky renderer after the pack is determined. */
	public @Nullable IAdaptiveRenderer createSkyRenderer();


	/** Gets the update tag for server-client synchronization. */
	default public NBTTagCompound getUpdateTag() {
		return this.serializeNBT();
	}

	/** Handles the update tag for server-client synchronization. */
	default public void handleUpdateTag(NBTTagCompound nbt) {
		this.deserializeNBT(nbt);
	}
}