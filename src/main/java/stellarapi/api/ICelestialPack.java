package stellarapi.api;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import net.minecraft.world.World;
import stellarapi.api.celestials.ICelestialCollection;
import stellarapi.api.celestials.ICelestialObject;
import stellarapi.api.celestials.IEffectorType;

/**
 * Celestial pack which can be provided by mods.
 * This will be integrated into data pack in 1.13 - for now it's actually a placeholder.
 * TODO CelestialPack world-specific configuration.
 * */
public interface ICelestialPack {

	/**
	 * Gets the pack name.
	 * */
	public String getPackName();

	/**
	 * Register celestial collections here.
	 * */
	public void onRegisterCollection(World world, Consumer<ICelestialCollection> colRegistry,
			BiConsumer<IEffectorType, ICelestialObject> effRegistry);

	/**
	 * Creates coordinates, or returns <code>null</code> if this pack doesn't provide coordinates.
	 * Nonnull for now.
	 * TODO Refactor on 1.13
	 * */
	public ICelestialCoordinates createCoordinates(World world);

	/**
	 * Creates sky effect, or returns <code>null</code> if this pack doesn't provide sky effect.
	 * Nonnull for now.
	 * */
	public ISkyEffect createSkyEffect(World world);

}
