package worldsets;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import worldsets.api.worldset.WorldSet;

public class WorldSetData extends WorldSavedData {

	private static final String ID = "worldset_data";

	public static WorldSetData getWorldSets(World world) {
		WorldSavedData data = world.getPerWorldStorage().getOrLoadData(WorldSetData.class, ID);
		if(data instanceof WorldSetData)
			return (WorldSetData) data;
		else {
			WorldSetData wdata = new WorldSetData(ID);
			world.getPerWorldStorage().setData(ID, wdata);
			return wdata;
		}
	}

	public WorldSetData(String name) {
		super(name);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) { }

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) { return null; }


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
