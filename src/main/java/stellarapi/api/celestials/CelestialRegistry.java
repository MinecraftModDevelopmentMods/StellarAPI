package stellarapi.api.celestials;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.RegistryBuilder;
import stellarapi.StellarAPI;

public enum CelestialRegistry {
	INSTANCE;

	public final ResourceLocation CELESTIALS = new ResourceLocation(StellarAPI.modid, "celestials");
	public final ResourceLocation COORDS = new ResourceLocation(StellarAPI.modid, "coords");

	private final IForgeRegistry<CelestialType> typeRegistry;
	private final IForgeRegistry<Coordinate> coordRegistry;


	private CelestialRegistry() {
		// TODO Implement Callbacks - might not be needed.
		this.typeRegistry = new RegistryBuilder<CelestialType>()
				.setName(CELESTIALS).setType(CelestialType.class).setIDRange(0, Integer.MAX_VALUE)
				.create();
		this.coordRegistry = new RegistryBuilder<Coordinate>()
				.setName(COORDS).setType(Coordinate.class).setIDRange(0, Integer.MAX_VALUE)
				.create();
	}
}