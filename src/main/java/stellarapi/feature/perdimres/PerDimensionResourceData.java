package stellarapi.feature.perdimres;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import stellarapi.StellarAPI;

/**
 * Per dimension resource data.
 * */
public class PerDimensionResourceData extends WorldSavedData {
	
	private static final String ID  = "perdimensionresourcedata";
	
	public static PerDimensionResourceData getData(World world) {
		PerDimensionResourceData result;
		WorldSavedData data = world.perWorldStorage.loadData(PerDimensionResourceData.class, ID);
		if(!(data instanceof PerDimensionResourceData)) {
			result = new PerDimensionResourceData(ID);
			world.perWorldStorage.setData(ID, result);
		} else result = (PerDimensionResourceData) data;
		
		result.world = world;
		
		return result;
	}
	
	private final Map<String, ResourceLocation> resourceMap = Maps.newHashMap();
	private World world;

	private PerDimensionResourceData(String id) {
		super(id);
	}
	
	public ImmutableMap<String, ResourceLocation> getResourceMap() {
		return ImmutableMap.copyOf(this.resourceMap);
	}
	
	public void addToResourceMap(String id, ResourceLocation location) {
		resourceMap.put(id, location);
		this.markDirty();
	}
	
	public void removeFromResourceMap(String id) {
		resourceMap.remove(id);
		this.markDirty();
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
	
	@Override
	public void markDirty() {
		StellarAPI.instance.getNetworkManager().onSyncToAll(this.world);
		super.markDirty();
	}

}
