package stellarapi.api.celestials;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.RegistryBuilder;
import stellarapi.StellarAPI;

public enum CelestialRegistry {
	INSTANCE;

	public static final ResourceLocation CELESTIALS = new ResourceLocation(StellarAPI.modid, "celestials");

	private final IForgeRegistry<CelestialType> registry;


	private CelestialRegistry() {
		// TODO Implement Callbacks - might not be needed.
		this.registry = new RegistryBuilder<CelestialType>()
				.setName(CELESTIALS).setType(CelestialType.class).setIDRange(0, Integer.MAX_VALUE)
				.create();
	}
}