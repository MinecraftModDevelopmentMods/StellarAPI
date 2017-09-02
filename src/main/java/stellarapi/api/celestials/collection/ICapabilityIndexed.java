package stellarapi.api.celestials.collection;

import net.minecraft.nbt.NBTBase;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public interface ICapabilityIndexed<T extends NBTBase> extends ICapabilitySerializable<T> {
	/**
	 * Approximate occurence of this capability over the whole data.
	 * */
	public double validOccurence();
}
