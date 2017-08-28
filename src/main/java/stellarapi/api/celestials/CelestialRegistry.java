package stellarapi.api.celestials;

import java.util.Map;

import com.google.common.collect.BiMap;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import net.minecraftforge.fml.common.registry.RegistryBuilder;
import stellarapi.StellarAPI;
import stellarapi.api.coordinates.Coordinates;

public enum CelestialRegistry {
	INSTANCE;

	public final ResourceLocation CELESTIALS = new ResourceLocation(StellarAPI.modid, "celestials");
	public final ResourceLocation COORDS = new ResourceLocation(StellarAPI.modid, "coords");

	private IForgeRegistry<CelestialType> typeRegistry;
	private IForgeRegistry<Coordinates> coordRegistry;

	public void onRegistryEvent(RegistryEvent.NewRegistry regRegEvent) {
		this.typeRegistry = new RegistryBuilder<CelestialType>()
				.setName(CELESTIALS).setType(CelestialType.class).setIDRange(0, Integer.MAX_VALUE)
				.create();
		this.coordRegistry = new RegistryBuilder<Coordinates>()
				.setName(COORDS).setType(Coordinates.class).setIDRange(0, Integer.MAX_VALUE)
				.add(new Callback())
				.create();
	}

	public static class Callback implements IForgeRegistry.CreateCallback<Coordinates> {

		@Override
		public void onCreate(Map<ResourceLocation, ?> slaveset,
				BiMap<ResourceLocation, ? extends IForgeRegistry<?>> registries) {
			// TODO Put those
			
		}
		
	}
}