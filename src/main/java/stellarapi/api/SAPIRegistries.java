package stellarapi.api;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import stellarapi.api.coordinates.CCoordinates;

/**
 * Contains registries and registrants of Stellar API.
 * Registries are only available starting from preInit,
 *  so registration is impossible here.
 * */
public class SAPIRegistries {

	// ********************************************* //
	// **************** Coordinates **************** //
	// ********************************************* //

	public static final ResourceLocation COORDS = new ResourceLocation(SAPIReference.modid, "coords");

	private static IForgeRegistry<CCoordinates> registryCoords;
	public IForgeRegistry<CCoordinates> getCoordRegistry() {
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
	// ************** General Section ************** //
	// ********************************************* //

	public static void onInit() {
		registryCoords = GameRegistry.findRegistry(CCoordinates.class);
	}
}
