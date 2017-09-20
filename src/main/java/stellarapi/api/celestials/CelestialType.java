package stellarapi.api.celestials;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;
import stellarapi.api.celestials.collection.model.TypeModel;
import stellarapi.api.celestials.partition.ICPartitionType;
import stellarapi.api.coordinates.CCoordinates;

/**
 * Certain celestial type.
 * */
public abstract class CelestialType extends IForgeRegistryEntry.Impl<CelestialType> {

	private ResourceLocation parentID;
	private CelestialType parent;
	private List<CelestialType> children = Lists.newArrayList();

	private ICPartitionType<?, ?> partition;
	private CCoordinates coordinates;

	private int level;

	/** Initiates root celestial type. */
	public CelestialType(ICPartitionType<?, ?> partition) {
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

	public ICPartitionType<?, ?> getPartition() {
		return this.partition;
	}

	public abstract TypeModel generateTypeModel();

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