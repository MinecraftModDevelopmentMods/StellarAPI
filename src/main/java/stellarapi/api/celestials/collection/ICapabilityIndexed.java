package stellarapi.api.celestials.collection;

import net.minecraft.nbt.NBTBase;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public interface ICapabilityIndexed<T extends NBTBase> extends ICapabilitySerializable<T> {
	/**
	 * Approximate occurrence of this capability over the whole data.
	 * */
	public double validOccurrence(double expectedEntryNumber);

	/**
	 * Updates fields for the new segment(object).
	 * */
	public void updateFields();
}
