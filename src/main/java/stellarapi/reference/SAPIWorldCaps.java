package stellarapi.reference;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import stellarapi.api.SAPICapabilities;

public class SAPIWorldCaps implements ICapabilityProvider, INBTSerializable<NBTTagCompound> {

	private CelestialPackManager celestials;

	public SAPIWorldCaps(World world) {
		this.celestials = new CelestialPackManager(world);
	}

	public RuntimeException getOccuredException() {
		return celestials.exception;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == SAPICapabilities.CELESTIAL_CAPABILITY;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(capability == SAPICapabilities.CELESTIAL_CAPABILITY)
			return SAPICapabilities.CELESTIAL_CAPABILITY.cast(this.celestials);
		else return null;
	}


	@Override
	public NBTTagCompound serializeNBT() {
		return celestials.serializeNBT();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		celestials.deserializeNBT(nbt);
	}

}
