package stellarapi.api.celestials;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import stellarapi.api.celestials.collection.ICollectionPartition;
import stellarapi.api.coordinates.CCoordinates;

/**
 * Certain celestial type.
 * */
public class CelestialType extends IForgeRegistryEntry.Impl<CelestialType> {

	private final ResourceLocation parentID;
	private CelestialType parent;

	private ICollectionPartition partition;
	private CCoordinates coordinates;

	/** Initiates parent celestial type. */
	public CelestialType(ICollectionPartition partition) {
		this.parentID = null;
		// TODO add coordinate and systems in this case.
		this.partition = partition;
	}

	public CelestialType(ResourceLocation parentID) {
		this.parentID = parentID;
	}

	public CCoordinates getCoordinates() {
		return this.coordinates;
	}

}