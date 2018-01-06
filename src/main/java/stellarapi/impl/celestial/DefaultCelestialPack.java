package stellarapi.impl.celestial;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import stellarapi.api.ICelestialPack;
import stellarapi.api.ICelestialScene;

public class DefaultCelestialPack implements ICelestialPack {

	@Override
	public String getPackName() {
		return "Default";
	}

	@Override
	public ICelestialScene getScene(World world) {
		return new DefaultCelestialScene(world);
	}

	@Override
	public NBTTagCompound serializeNBT() { return new NBTTagCompound(); }

	@Override
	public void deserializeNBT(NBTTagCompound nbt) { }

}
