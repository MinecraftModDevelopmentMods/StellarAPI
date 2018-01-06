package stellarapi.api.world.worldset;

import java.util.Map;
import java.util.function.Predicate;

import com.google.common.collect.Maps;

import net.minecraft.world.DimensionType;
import net.minecraft.world.World;

/**
 * Represents certain set of world. There is only one instance of worldset allowed. (It's singleton)
 * Initialized on Init phase.
 * */
public abstract class WorldSet {
	// TODO 1.13 This should be dynamic Predicate<World>
	public final String name;

	/** Types of worlds which this set definitely have, to identify the world */
	private final DimensionType[] explicitTypes;
	private final Predicate<World> predicate;
	private final EnumCPriority priority;
	private EnumFlag hasSky = EnumFlag.UNCERTAIN;
	private EnumFlag hasAtmosphere = EnumFlag.UNCERTAIN;
	private Map<String, EnumFlag> flags = Maps.newHashMap();

	protected WorldSet(String name, EnumCPriority priority, Predicate<World> predicate,
			DimensionType... explicitTypes) {
		this.name = name;
		this.priority = priority;
		this.predicate = predicate;
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

	public Predicate<World> getCondition() {
		return this.predicate;
	}

	public DimensionType[] getExplicitTypes() {
		return this.explicitTypes;
	}

	/** Gives if worlds in this world set has sky, i.e. any kind of opening on the upside. */
	public EnumFlag hasSky() {
		return this.hasSky;
	}

	/** Gives if worlds in this world set has proper atmosphere. */
	public EnumFlag hasAtmosphere() {
		return this.hasAtmosphere;
	}

	public EnumFlag getFlag(String property) {
		return flags.get(property);
	}
}
