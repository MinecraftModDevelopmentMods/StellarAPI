package worldsets.api;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

public abstract class WorldSet extends IForgeRegistryEntry.Impl<WorldSet> {

	/** Types of worlds which this set definitely have, to identify the world */
	private DimensionType[] explicitTypes;
	private EnumCPriority priority;
	private EnumFlag hasSky = EnumFlag.UNCERTAIN;
	private EnumFlag hasAtmosphere = EnumFlag.UNCERTAIN;
	private Map<String, EnumFlag> flags = Maps.newHashMap();

	public abstract boolean containsWorld(World world);

	protected WorldSet(EnumCPriority priority, DimensionType... explicitTypes) {
		this.priority = priority;
		this.explicitTypes = explicitTypes;
	}

	protected WorldSet setHasSky(EnumFlag flag) {
		this.hasSky = flag;
		return this;
	}

	protected WorldSet setHasAtmosphere(EnumFlag flag) {
		this.hasAtmosphere = flag;
		return this;
	}

	protected WorldSet set(String property, EnumFlag flag) {
		flags.put(property, flag);
		return this;
	}

	public EnumCPriority getPriority() {
		return this.priority;
	}

	public DimensionType[] getExplicitTypes() {
		return this.explicitTypes;
	}

	/** Gives if worlds in this world set has sky, i.e. any kind of opening on the upside. */
	public EnumFlag hasSky() {
		return this.hasSky;
	}

	/** Gives if worlds in this world set has atmosphere. */
	public EnumFlag hasAtmosphere() {
		return this.hasAtmosphere;
	}

	public EnumFlag getFlag(String property) {
		return flags.get(property);
	}

}
