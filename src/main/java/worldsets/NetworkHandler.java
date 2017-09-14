package worldsets;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import worldsets.api.WAPIReference;
import worldsets.api.event.ProviderEvent;
import worldsets.api.provider.IProviderRegistry;
import worldsets.api.provider.ProviderRegistry;

public class NetworkHandler {

	/** Server sending the packet */
	public void onPlayerSync(EntityPlayer player) {
		NBTTagCompound compoundToSend = new NBTTagCompound();
		ImmutableMap<ResourceLocation, IProviderRegistry<?>> registryMap = ProviderRegistry.getProviderRegistryMap();

		for(Map.Entry<ResourceLocation, IProviderRegistry<?>> entry : registryMap.entrySet()) {
			ProviderEvent.Send<?> event = new ProviderEvent.Send(entry.getValue(), 
					player.getEntityWorld(),
					compoundToSend.getCompoundTag(entry.getKey().toString()));
			MinecraftForge.EVENT_BUS.post(event);
		}

		// TODO send sync packet
	}

	/** Client receiving the packet */
	public void onSyncPacket(NBTTagCompound syncData) {
		ImmutableMap<ResourceLocation, IProviderRegistry<?>> registryMap = ProviderRegistry.getProviderRegistryMap();

		for(Map.Entry<ResourceLocation, IProviderRegistry<?>> entry : ProviderRegistry.getProviderRegistryMap().entrySet()) {
			ProviderEvent.Receive<?> event = new ProviderEvent.Receive(entry.getValue(),
					WAPIReference.getDefaultWorld(true),
					syncData.getCompoundTag(entry.getKey().toString()));
			MinecraftForge.EVENT_BUS.post(event);
		}

		for(IProviderRegistry<?> registry : ProviderRegistry.getProviderRegistryMap().values()) {
			ProviderEvent.Complete<?> event = new ProviderEvent.Complete(registry, true, true);
			MinecraftForge.EVENT_BUS.post(event);
		}
	}
}
