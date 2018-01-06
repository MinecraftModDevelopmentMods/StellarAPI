package stellarapi.feature.celestial.tweakable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import stellarapi.api.ICelestialPack;
import stellarapi.api.ICelestialScene;

public class SAPICelestialPack implements ICelestialPack {

	private double dayLength = 24000.0;
	private double monthInDay = 8.0;

	public SAPICelestialPack() { }

	public SAPICelestialPack(double day, double month) {
		this.dayLength = day;
		this.monthInDay = month;
	}

	@Override
	public String getPackName() {
		return "Stellar API";
	}

	@Override
	public ICelestialScene getScene(World world) {
		return new SAPICelestialScene(world, this.dayLength, this.monthInDay);
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setDouble("day", this.dayLength);
		nbt.setDouble("month", this.monthInDay);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		this.dayLength = nbt.getDouble("day");
		this.monthInDay = nbt.getDouble("month");
	}
}
