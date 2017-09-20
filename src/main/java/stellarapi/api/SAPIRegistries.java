package stellarapi.api;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Ordering;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;
import stellarapi.api.celestials.CelestialType;
import stellarapi.api.coordinates.CCoordinates;

/**
 * Contains registries and registrants of Stellar API.
 * Registries are only available starting from preInit,
 *  so registration is impossible here.
 * */
public class SAPIRegistries {

	// ********************************************* //
	// ************* Celestial Objects ************* //
	// ********************************************* //

	public static final ResourceLocation CELESTIALS = new ResourceLocation(SAPIReferences.modid, "celestials");

	private static IForgeRegistry<CelestialType> registryCelestialType;
	public static IForgeRegistry<CelestialType> getCelestialTypeRegistry() {
		return registryCelestialType;
	}

	public static final Ordering<CelestialType> typeOrder = Ordering.<Integer>natural()
			.onResultOf(new Function<CelestialType, Integer>() {
				@Override
				public Integer apply(CelestialType input) { return input.getLevel(); }
			});

	public static ImmutableSortedSet<CelestialType> getOrderedTypes() {
		// TODO CelestialTypes store this as slave map
		return ImmutableSortedSet.copyOf(SAPIRegistries.typeOrder, registryCelestialType.getValues());
	}

	// ********************************************* //
	// **************** Coordinates **************** //
	// ********************************************* //

	public static final ResourceLocation COORDS = new ResourceLocation(SAPIReferences.modid, "coordinates");

	private static IForgeRegistry<CCoordinates> registryCoords;
	public static IForgeRegistry<CCoordinates> getCoordRegistry() {
		return registryCoords;
	}

	/**
	 * Base coordinates. All coordinates should have this coordinates as the parent.
	 * This also means the horizontal coordinates, the coordinates of MC world.
	 * */
	@ObjectHolder("horizontal")
	public static final CCoordinates horizontal = null;

	/**
	 * Equatorial Coordiantes. This coordinates rotates for the day cycle.
	 * */
	@ObjectHolder("equatorial")
	public static final CCoordinates equatorial = null;

	/**
	 * Celestial Coordinates. The coordinates of celestial sphere.
	 * */
	@ObjectHolder("celestial")
	public static final CCoordinates celestial = null;

	// ********************************************* //
	// **************** Atmospheres **************** //
	// ********************************************* //

	public static final ResourceLocation ATMOSPHERE = new ResourceLocation(SAPIReferences.modid, "atmosphere");

	// ********************************************* //
	// ****************** Worlds ******************* //
	// ********************************************* //

	public static final ResourceLocation PROVIDER_REPLACER = new ResourceLocation(SAPIReferences.modid, "provider_replacer");


	// ********************************************* //
	// *************** Slavemap Keys *************** //
	// ********************************************* //

	public static final ResourceLocation READABLE_NAMES = new ResourceLocation(SAPIReferences.modid, "readable_names");

	// ********************************************* //
	// ************** General Section ************** //
	// ********************************************* //

	public static void onInit() {
		registryCelestialType = GameRegistry.findRegistry(CelestialType.class);
		registryCoords = GameRegistry.findRegistry(CCoordinates.class);
	}
}
