package stellarapi.feature.celestial.tweakable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import stellarapi.StellarAPI;
import stellarapi.api.ICelestialCoordinates;
import stellarapi.api.ICelestialHelper;
import stellarapi.api.ICelestialScene;
import stellarapi.api.IAtmosphereEffect;
import stellarapi.api.celestials.ICelestialCollection;
import stellarapi.api.celestials.ICelestialObject;
import stellarapi.api.celestials.IEffectorType;
import stellarapi.api.render.IAdaptiveRenderer;
import stellarapi.example.CelestialHelper;

public class SAPICelestialScene implements ICelestialScene {
	private final World world;

	private boolean sunExist;
	private boolean moonExist;

	double dayLength;
	double dayOffset;

	double monthInDay;
	double monthOffset;

	double yearInDay;
	double yearOffset;

	private boolean yearlyChangeEnabled;
	double latitude;
	double angleAxialTilt;


	private float minimumSkyBrightness;

	public SAPICelestialScene(World world, SAPIWorldCfgHandler config) {
		this.world = world;

		this.sunExist = config.sunExist;
		this.moonExist = config.moonExist;

		this.dayLength = config.dayLength;
		this.monthInDay = config.monthInDay;
		this.dayOffset = config.dayOffset;
		this.monthOffset = config.monthOffset;
		this.yearInDay = config.yearInDay;
		this.yearOffset = config.yearOffset;

		this.yearlyChangeEnabled = config.yearlyChangeEnabled;
		this.latitude = config.latitude;
		this.angleAxialTilt = config.angleAxialTilt;

		this.minimumSkyBrightness = config.minimumSkyBrightness;
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
		nbt.setDouble("year", this.yearInDay);
		nbt.setDouble("yearOffset", this.yearOffset);

		nbt.setBoolean("yearlyChange", this.yearlyChangeEnabled);
		nbt.setDouble("latitude", this.latitude);
		nbt.setDouble("axialTilt", this.angleAxialTilt);

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
		this.yearInDay = nbt.getDouble("year");
		this.yearOffset = nbt.getDouble("yearOffset");

		this.yearlyChangeEnabled = nbt.getBoolean("yearlyChange");
		this.latitude = nbt.getDouble("latitude");
		this.angleAxialTilt = nbt.getDouble("axialTilt");

		this.minimumSkyBrightness = nbt.getFloat("minSkyBrightness");
	}


	private CelestialHelper helper;
	private SAPICollection collection;
	private ICelestialCoordinates coordinate;
	private IAtmosphereEffect skyEffect;

	@Override
	public void prepare() {
		if(!this.yearlyChangeEnabled) {
			this.latitude = 0.0;
			this.angleAxialTilt = 0.0;
		}

		this.collection = new SAPICollection(this.world, this.sunExist, this.moonExist,
				this.dayLength, this.monthInDay, this.dayOffset, this.monthOffset);
		this.coordinate = new SAPICoordinates(this.world, this.dayLength, this.dayOffset);
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
	public IAtmosphereEffect createSkyEffect() {
		return this.skyEffect;
	}

	@Override
	public ICelestialHelper createCelestialHelper() {
		return this.helper;
	}

	@Override
	public IAdaptiveRenderer createSkyRenderer() {
		if(this.yearlyChangeEnabled) {
			return StellarAPI.PROXY.getRenderer(this);
		} else return null;
	}
}
