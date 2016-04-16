package stellarapi.api.feature;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

public class PerDimensionResourceData extends WorldSavedData {
	
	private static final String ID  = "perdimensionresourcedata";
	
	public static PerDimensionResourceData getData(World world) {
		WorldSavedData data = world.perWorldStorage.loadData(
				PerDimensionResourceData.class, ID);
		if(!(data instanceof WorldSavedData)) {
			data = new PerDimensionResourceData(ID);
			world.perWorldStorage.setData(ID, data);
		}
		
		return (PerDimensionResourceData) data;
	}
	
	public Map<String, ResourceLocation> resourceMap = Maps.newHashMap();

	private PerDimensionResourceData(String id) {
		super(id);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		resourceMap.clear();
		
		for(Object key : compound.func_150296_c()) {
			String keyStr = (String) key;
			resourceMap.put(keyStr,
					new ResourceLocation(compound.getString(keyStr)));
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		for(String key : resourceMap.keySet()) {
			compound.setString(key, resourceMap.get(key).toString());
		}
	}

}
