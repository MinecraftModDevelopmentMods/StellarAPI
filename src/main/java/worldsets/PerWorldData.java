package worldsets;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import worldsets.api.worldset.EnumCPriority;
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


	ImmutableList<WorldSet> appliedWorldSets;

	void populate(ImmutableList<WorldSet> immutableList) {
		List<WorldSet> worldSets = Lists.newArrayList(immutableList);
		Collections.sort(worldSets, Comparator.<WorldSet, EnumCPriority>comparing(worldSet -> worldSet.getPriority()));
		this.appliedWorldSets = ImmutableList.copyOf(worldSets);
	}

}
