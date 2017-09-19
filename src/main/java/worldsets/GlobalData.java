package worldsets;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import worldsets.api.worldset.WorldSet;
import worldsets.api.worldset.WorldSetInstance;

public class GlobalData extends WorldSavedData {

	private static final String ID = "global_worldset_data";

	public static GlobalData getWorldSets(World base) {
		WorldSavedData data = base.getMapStorage().getOrLoadData(GlobalData.class, ID);
		if(data instanceof GlobalData)
			return (GlobalData) data;
		else {
			GlobalData wdata = new GlobalData(ID);
			base.getMapStorage().setData(ID, wdata);
			return wdata;
		}
	}

	public GlobalData(String name) {
		super(name);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		this.setupIfNeeded();
		for(String key : nbt.getKeySet()) {
			ResourceLocation resKey = new ResourceLocation(key);
			if(worldSetInstanceMap.containsKey(resKey)) {
				worldSetInstanceMap.get(resKey).deserializeNBT(nbt.getCompoundTag(key));
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		if(compound == null)
			compound = new NBTTagCompound();

		for(ResourceLocation resKey : worldSetInstanceMap.keySet()) {
			NBTTagCompound tag = worldSetInstanceMap.get(resKey).serializeNBT();
			compound.setTag(resKey.toString(), tag);
		}

		return compound;
	}

	private IForgeRegistry<WorldSet> registry = GameRegistry.findRegistry(WorldSet.class);
	private Map<ResourceLocation, WorldSetInstance> worldSetInstanceMap = Maps.newHashMap();

	void setupIfNeeded() {
		if(worldSetInstanceMap.isEmpty()) {
			for(WorldSet worldSet : this.registry) {
				worldSetInstanceMap.put(worldSet.delegate.name(), new WorldSetInstance(worldSet));
			}
		}
	}

	WorldSetInstance getInstance(WorldSet worldSet) {
		return worldSetInstanceMap.get(worldSet.delegate.name());
	}
}
