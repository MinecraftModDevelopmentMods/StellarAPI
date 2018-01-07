package stellarapi.feature.celestial.tweakable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import stellarapi.api.ICelestialPack;
import stellarapi.api.ICelestialScene;

public class SAPICelestialPack implements ICelestialPack {

	private double dayLength = 24000.0;
	private double dayOffset = 7200.0;

	private double monthInDay = 8.0;
	private double monthOffset = 4.0;

	private float minimumSkyBrightness = 0.2f;

	public SAPICelestialPack() { }

	public SAPICelestialPack(double day, double month,
			double dayOffset, double monthOffset, float minBrightness) {
		this.dayLength = day;
		this.monthInDay = month;

		this.dayOffset = dayOffset;
		this.monthOffset = monthOffset;

		this.minimumSkyBrightness = minBrightness;
	}

	@Override
	public String getPackName() {
		return "Stellar API";
	}

	@Override
	public ICelestialScene getScene(World world) {
		return new SAPICelestialScene(world, this.dayLength, this.monthInDay,
				this.dayOffset, this.monthOffset, this.minimumSkyBrightness);
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setDouble("day", this.dayLength);
		nbt.setDouble("month", this.monthInDay);
		nbt.setDouble("dayOffset", this.dayOffset);
		nbt.setDouble("monthOffset", this.monthOffset);
		nbt.setFloat("minSkyBrightness", this.minimumSkyBrightness);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		this.dayLength = nbt.getDouble("day");
		this.monthInDay = nbt.getDouble("month");
		this.dayOffset = nbt.getDouble("dayOffset");
		this.monthOffset = nbt.getDouble("monthOffset");
		this.minimumSkyBrightness = nbt.getFloat("minSkyBrightness");
	}
}
