package stellarapi.api.celestials.collection.model;

import net.minecraft.nbt.NBTBase;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public interface ICapabilityIndexed<Pn, T extends NBTBase> extends ICapabilitySerializable<T> {
	/**
	 * Approximate distribution of this capability over the whole data as partition.
	 * */
	public Pn distribution(Pn partition);

	/**
	 * Updates fields for the new segment(object).
	 * */
	public void updateFields();
}