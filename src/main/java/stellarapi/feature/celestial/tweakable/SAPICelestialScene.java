package stellarapi.feature.celestial.tweakable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import stellarapi.StellarAPI;
import stellarapi.api.celestials.CelestialCollection;
import stellarapi.api.celestials.CelestialObject;
import stellarapi.api.celestials.IEffectorType;
import stellarapi.api.pack.ICelestialScene;
import stellarapi.api.render.IAdaptiveRenderer;
import stellarapi.api.view.IAtmosphereEffect;
import stellarapi.api.view.ICCoordinates;
import stellarapi.api.world.ICelestialHelper;
import stellarapi.example.CelestialHelperSimple;

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


	private CelestialHelperSimple helper;
	private SAPICollection collection;
	private ICCoordinates coordinate;
	private IAtmosphereEffect skyEffect;

	private CelestialObject sun, moon;

	@Override
	public void prepare() {
		if(!this.yearlyChangeEnabled) {
			this.latitude = 0.0;
			this.angleAxialTilt = 0.0;
		}
		// No complete implementation before coordinates system overhaul.
		// FIXME Coordinates System Overhaul
		// FIXME Clean up codes

		this.sun = this.sunExist? new SAPISun(this.dayLength, this.dayOffset) : null;
		this.moon = this.moonExist? new SAPIMoon(this.world, this.dayLength, this.monthInDay, this.dayOffset, this.monthOffset) : null;

		this.collection = new SAPICollection(this.sun, this.moon);
		this.coordinate = new SAPICoordinates(this.world, this.dayLength, this.dayOffset);
		this.skyEffect = new SAPISky(this.minimumSkyBrightness);
		this.helper = new CelestialHelperSimple(
				1.0f, 1.0f, this.sun, this.moon, this.coordinate, this.skyEffect);
	}

	@Override
	public void onRegisterCollection(Consumer<CelestialCollection> colRegistry,
			BiConsumer<IEffectorType, CelestialObject> effRegistry) {
		colRegistry.accept(this.collection);
		if(this.sun != null)
			effRegistry.accept(IEffectorType.Light, this.sun);
		if(this.moon != null)
			effRegistry.accept(IEffectorType.Tide, this.moon);
	}

	@Override
	public ICCoordinates createCoordinates() {
		return this.coordinate;
	}

	@Override
	public IAtmosphereEffect createAtmosphereEffect() {
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
