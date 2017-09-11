package worldsets.api.worldset;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityDispatcher;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class WorldSetInstance implements ICapabilitySerializable<NBTTagCompound> {
	private WorldSet theSet;
	private CapabilityDispatcher capabilities = null;

	public WorldSetInstance(WorldSet worldSet) {
		// TODO WorldSet gathers capabilities.
	}

	public WorldSet getWorldSet() {
		return this.theSet;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return this.capabilities != null? capabilities.hasCapability(capability, facing) : false;
	}
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return this.capabilities != null? capabilities.getCapability(capability, facing) : null;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		return this.capabilities != null? capabilities.serializeNBT() : new NBTTagCompound();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		if(this.capabilities != null)
			capabilities.deserializeNBT(nbt);
	}
}
