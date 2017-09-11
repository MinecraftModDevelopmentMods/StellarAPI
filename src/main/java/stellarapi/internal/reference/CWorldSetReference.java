package stellarapi.internal.reference;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import stellarapi.api.SAPICapabilities;
import stellarapi.internal.coordinates.CCoordSystem;
import worldsets.api.worldset.WorldSetInstance;

public class CWorldSetReference implements ICapabilitySerializable<NBTTagCompound> {

	private final CCoordSystem system;

	public CWorldSetReference(WorldSetInstance worldSetInstance) {
		this.system = new CCoordSystem(worldSetInstance.getWorldSet());
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == SAPICapabilities.COORDINATES_SYSTEM;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(capability == SAPICapabilities.COORDINATES_SYSTEM)
			return SAPICapabilities.COORDINATES_SYSTEM.cast(this.system);
		else return null;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		// TODO WorldSetReference more
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag("coordinateSystem",
				SAPICapabilities.COORDINATES_SYSTEM.writeNBT(this.system, null));

		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		// TODO WorldSetReference more
		NBTTagCompound coordinates = nbt.getCompoundTag("coordinateSystem");
		SAPICapabilities.COORDINATES_SYSTEM.readNBT(this.system, null, coordinates);
	}
}
