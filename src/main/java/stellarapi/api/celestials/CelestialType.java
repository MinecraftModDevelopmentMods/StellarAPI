package stellarapi.api.celestials;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import stellarapi.api.celestials.collection.ICollectionPartition;
import stellarapi.api.coordinates.CCoordinates;

/**
 * Certain celestial type.
 * */
public class CelestialType<P> extends IForgeRegistryEntry.Impl<CelestialType<P>> {

	private ResourceLocation parentID;
	private CelestialType parent;
	private List<CelestialType> children = Lists.newArrayList();

	private ICollectionPartition<P> partition;
	private CCoordinates coordinates;

	private int level;

	/** Initiates root celestial type. */
	public CelestialType(ICollectionPartition<P> partition) {
		this.parentID = null;
		this.partition = partition;

		this.level = 0;
	}

	public CelestialType(ResourceLocation parentID) {
		this.parentID = parentID;
	}

	public boolean hasParent() {
		return this.parentID != null;
	}

	public @Nullable ResourceLocation getParentID() {
		return this.parentID;
	}

	public @Nullable CelestialType getParent() {
		return this.parent;
	}

	public ImmutableList<CelestialType> getChildren() {
		return ImmutableList.copyOf(this.children);
	}

	public CCoordinates getCoordinates() {
		return this.coordinates;
	}

	/**
	 * Sets the coordinates if this type is the root type, i.e. does not have a parent.
	 * Set this on init phase.
	 * */
	public void setCoordinates(CCoordinates coordinates) {
		if(!this.hasParent())
			this.coordinates = coordinates;
	}


	/** Just gets internal level */
	@Deprecated
	public int getLevel() {
		return this.level;
	}

	@Deprecated
	public void injectParent(ResourceLocation parentID, CelestialType parent) {
		this.parent = parent;
		this.parentID = parentID;
		if(parent != null)
			parent.children.add(this);
	}

	@Deprecated
	public void evaluateLevel() {
		CelestialType ancestorType = this.parent;
		int i = 0;

		while(ancestorType != null) {
			ancestorType = ancestorType.parent;
			i++;
		}

		this.level = i;
	}
}