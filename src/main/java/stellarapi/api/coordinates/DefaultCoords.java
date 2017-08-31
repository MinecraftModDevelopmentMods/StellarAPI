package stellarapi.api.coordinates;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@Mod.EventBusSubscriber(modid = "stellarapi")
public class DefaultCoords {

	public static final ResourceLocation base = new ResourceLocation("horizontal");

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

	@SubscribeEvent
	public static void register(RegistryEvent.Register<CCoordinates> coordsReg) {
		coordsReg.getRegistry().registerAll(
				forName("horizontal"),
				forName("equatorial"),
				forName("celestial"));
	}

	private static CCoordinates forName(String name) {
		return new CCoordinates().setRegistryName(new ResourceLocation(name));
	}

}