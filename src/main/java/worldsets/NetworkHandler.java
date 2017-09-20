package worldsets;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import worldsets.api.WAPIReference;
import worldsets.api.event.ProviderEvent;
import worldsets.api.provider.IProviderRegistry;
import worldsets.api.provider.ProviderRegistry;

public class NetworkHandler {

	private final SimpleNetworkWrapper wrapper;

	public NetworkHandler() {
		this.wrapper = NetworkRegistry.INSTANCE.newSimpleChannel(WAPIReference.modid);

		wrapper.registerMessage(MessageSync.MessageSyncHandler.class,
				MessageSync.class, 0, Side.CLIENT);
	}

	/** Server sending the packet */
	public void onPlayerSync(EntityPlayerMP player) {
		NBTTagCompound compoundToSend = new NBTTagCompound();
		ImmutableMap<ResourceLocation, IProviderRegistry<?>> registryMap = ProviderRegistry.getProviderRegistryMap();

		for(Map.Entry<ResourceLocation, IProviderRegistry<?>> entry : registryMap.entrySet()) {
			ProviderEvent.Send<?> event = new ProviderEvent.Send(entry.getValue(), 
					player.getEntityWorld(),
					compoundToSend.getCompoundTag(entry.getKey().toString()));
			MinecraftForge.EVENT_BUS.post(event);
		}

		wrapper.sendTo(new MessageSync(compoundToSend), player);
	}

	/** Client receiving the packet */
	public void onSyncPacket(NBTTagCompound syncData) {
		ImmutableMap<ResourceLocation, IProviderRegistry<?>> registryMap = ProviderRegistry.getProviderRegistryMap();
		World world = WAPIReference.getDefaultWorld(true);

		for(Map.Entry<ResourceLocation, IProviderRegistry<?>> entry : ProviderRegistry.getProviderRegistryMap().entrySet()) {
			ProviderEvent.Receive<?> event = new ProviderEvent.Receive(entry.getValue(),
					world, syncData.getCompoundTag(entry.getKey().toString()));
			MinecraftForge.EVENT_BUS.post(event);
		}

		for(IProviderRegistry<?> registry : ProviderRegistry.getProviderRegistryMap().values()) {
			ProviderEvent.Complete<?> event = new ProviderEvent.Complete(registry, world, false);
			MinecraftForge.EVENT_BUS.post(event);
		}
	}
}
