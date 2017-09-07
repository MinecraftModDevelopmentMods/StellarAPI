package stellarapi.internal.coordinates;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.RegistryBuilder;
import stellarapi.api.SAPIReference;
import stellarapi.api.SAPIRegistries;
import stellarapi.api.coordinates.CCoordinates;
import worldsets.api.event.ProviderEvent;

@Mod.EventBusSubscriber(modid = SAPIReference.modid)
public class CoordRegistry {

	public static final String DEFAULT_NAME = "celestial";
	public static final ResourceLocation DEFAULT_ID = new ResourceLocation(DEFAULT_NAME);

	private static IForgeRegistry<CCoordinates> coordRegistry;

	@SubscribeEvent
	public static void onRegistryEvent(RegistryEvent.NewRegistry regRegEvent) {
		coordRegistry = new RegistryBuilder<CCoordinates>()
				.setName(SAPIRegistries.COORDS).setType(CCoordinates.class).setIDRange(0, Integer.MAX_VALUE)
				.setDefaultKey(DEFAULT_ID)
				.create();
	}

	@SubscribeEvent
	public static void register(RegistryEvent.Register<CCoordinates> coordsReg) {
		coordsReg.getRegistry().registerAll(
				forName("horizontal"),
				forName("equatorial"),
				forName("celestial"));
	}

	@SubscribeEvent
	public static void onProvRegistryEvent(ProviderEvent.NewRegistry pregRegEvent) {
		// TODO CoordSystem build
	}


	public static void onInit() {
		// TODO CoordSystem log
		for(CCoordinates coordinates : coordRegistry.getValues()) {
			ResourceLocation parentID = coordinates.getDefaultParentID();
			if(parentID != null && coordRegistry.containsKey(parentID)) {
				parentID = DEFAULT_ID;
				// TODO CoordSystem log this replacement
			}
			// TODO CoordSystem check for position-specifics and collect error
			coordinates.injectParent(parentID == null? null : coordRegistry.getValue(parentID));
		}
	}

	private static CCoordinates forName(String name) {
		return new CCoordinates().setRegistryName(new ResourceLocation(name));
	}

}