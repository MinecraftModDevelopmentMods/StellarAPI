package stellarapi.api.world;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.RegistryBuilder;
import stellarapi.StellarAPI;
import stellarapi.api.celestials.CelestialType;

public class WorldManager {
	public final ResourceLocation WORLDSETS = new ResourceLocation(StellarAPI.modid, "worldsets");

	private IForgeRegistry<WorldSet> worldSetRegistry;

	void onWorldManage(RegistryEvent.NewRegistry regRegEvent) {
		this.worldSetRegistry = new RegistryBuilder<WorldSet>()
				.setName(WORLDSETS).setType(WorldSet.class).setIDRange(0, Integer.MAX_VALUE)
				.create();
	}
}
