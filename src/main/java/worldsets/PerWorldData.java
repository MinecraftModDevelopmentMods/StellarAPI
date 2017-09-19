package worldsets;

import com.google.common.collect.ImmutableList;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import worldsets.api.worldset.WorldSet;

public class PerWorldData extends WorldSavedData {

	private static final String ID = "worldset_data";

	public static PerWorldData getWorldSets(World world) {
		WorldSavedData data = world.getPerWorldStorage().getOrLoadData(PerWorldData.class, ID);
		if(data instanceof PerWorldData)
			return (PerWorldData) data;
		else {
			PerWorldData wdata = new PerWorldData(ID);
			world.getPerWorldStorage().setData(ID, wdata);
			return wdata;
		}
	}

	public PerWorldData(String name) {
		super(name);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) { }

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) { return compound; }


	WorldSet primaryWorldSet;
	ImmutableList<WorldSet> appliedWorldSets;

	void populate(ImmutableList<WorldSet> immutableList) {
		this.appliedWorldSets = immutableList;
		for(WorldSet worldSet : this.appliedWorldSets) {
			if(this.primaryWorldSet == null
					|| primaryWorldSet.getPriority().compareTo(worldSet.getPriority()) == 1) {
				this.primaryWorldSet = worldSet;
			}
		}
	}

}
