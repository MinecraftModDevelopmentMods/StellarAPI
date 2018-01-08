package stellarapi.feature.celestial.tweakable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import stellarapi.api.ICelestialCoordinates;
import stellarapi.api.ICelestialScene;
import stellarapi.api.ISkyEffect;
import stellarapi.api.celestials.ICelestialCollection;
import stellarapi.api.celestials.ICelestialObject;
import stellarapi.api.celestials.IEffectorType;
import stellarapi.example.CelestialHelper;
import stellarapi.example.WorldProviderExample;
import stellarapi.impl.celestial.DefaultSkyVanilla;

public class SAPICelestialScene implements ICelestialScene {
	private final World world;

	private boolean sunExist;
	private boolean moonExist;

	private double dayLength;
	private double dayOffset;

	private double monthInDay;
	private double monthOffset;

	private float minimumSkyBrightness;

	public SAPICelestialScene(World world,
			boolean sunExist, boolean moonExist,
			double dayLength, double monthInDay,
			double dayOffset, double monthOffset, float minBrightness) {
		this.world = world;

		this.sunExist = sunExist;
		this.moonExist = moonExist;
		this.dayLength = dayLength;
		this.monthInDay = monthInDay;
		this.dayOffset = dayOffset;
		this.monthOffset = monthOffset;
		this.minimumSkyBrightness = minBrightness;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setBoolean("sunExist", this.sunExist);
		nbt.setBoolean("moonExist", this.moonExist);
		nbt.setDouble("day", this.dayLength);
		nbt.setDouble("month", this.monthInDay);
		nbt.setDouble("dayOffset", this.dayOffset);
		nbt.setDouble("monthOffset", this.monthOffset);
		nbt.setFloat("minSkyBrightness", this.minimumSkyBrightness);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		if(!nbt.hasKey("sunExist"))
			nbt.setBoolean("sunExist", true);
		if(!nbt.hasKey("moonExist"))
			nbt.setBoolean("moonExist", true);

		this.sunExist = nbt.getBoolean("sunExist");
		this.moonExist = nbt.getBoolean("moonExist");
		this.dayLength = nbt.getDouble("day");
		this.monthInDay = nbt.getDouble("month");
		this.dayOffset = nbt.getDouble("dayOffset");
		this.monthOffset = nbt.getDouble("monthOffset");
		this.minimumSkyBrightness = nbt.getFloat("minSkyBrightness");
	}


	private CelestialHelper helper;
	private SAPICollection collection;
	private ICelestialCoordinates coordinate;
	private ISkyEffect skyEffect;

	@Override
	public void prepare() {
		this.collection = new SAPICollection(this.world, this.sunExist, this.moonExist,
				this.dayLength, this.monthInDay, this.dayOffset, this.monthOffset);
		this.coordinate = new SAPICoordinate(this.world, this.dayLength, this.dayOffset);
		this.skyEffect = new SAPISky(this.minimumSkyBrightness);
		this.helper = new CelestialHelper(
				1.0f, 1.0f, collection.sun, collection.moon, this.coordinate, this.skyEffect);
	}

	@Override
	public void onRegisterCollection(Consumer<ICelestialCollection> colRegistry,
			BiConsumer<IEffectorType, ICelestialObject> effRegistry) {
		colRegistry.accept(this.collection);
		if(collection.sun != null)
			effRegistry.accept(IEffectorType.Light, collection.sun);
		if(collection.moon != null)
			effRegistry.accept(IEffectorType.Tide, collection.moon);
	}

	@Override
	public ICelestialCoordinates createCoordinates() {
		return this.coordinate;
	}

	@Override
	public ISkyEffect createSkyEffect() {
		return this.skyEffect;
	}

	@Override
	public WorldProvider replaceWorldProvider(WorldProvider provider) {
		return new WorldProviderExample(this.world, provider, this.helper);
	}

}
