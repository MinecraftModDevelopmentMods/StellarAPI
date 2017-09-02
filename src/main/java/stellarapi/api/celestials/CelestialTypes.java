package stellarapi.api.celestials;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.RegistryBuilder;
import stellarapi.api.SAPIReference;

@Mod.EventBusSubscriber(modid = SAPIReference.modid)
public class CelestialTypes {

	public static final ResourceLocation CELESTIALS = new ResourceLocation(SAPIReference.modid, "celestials");

	private static IForgeRegistry<CelestialType> typeRegistry;

	@SubscribeEvent
	public static void onRegistryEvent(RegistryEvent.NewRegistry regRegEvent) {
		typeRegistry = new RegistryBuilder<CelestialType>()
				.setName(CELESTIALS).setType(CelestialType.class).setIDRange(0, Integer.MAX_VALUE)
				.create();
	}
}