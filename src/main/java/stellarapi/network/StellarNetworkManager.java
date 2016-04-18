package stellarapi.network;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import stellarapi.feature.perdimres.PerDimensionResourceData;

public class StellarNetworkManager {
	
	private SimpleNetworkWrapper wrapper;
	protected String id = "stellarskychannel";
	
	public StellarNetworkManager() {
		this.wrapper = NetworkRegistry.INSTANCE.newSimpleChannel(this.id);
		
		wrapper.registerMessage(MessageSync.MessageSyncCommonHandler.class,
				MessageSync.class, 0, Side.CLIENT);
	}
	
	public void onSync(EntityPlayerMP player, World world) {
		PerDimensionResourceData data = PerDimensionResourceData.getData(world);

		NBTTagCompound compound = new NBTTagCompound();
		data.writeToNBT(compound);
		
		wrapper.sendTo(new MessageSync(compound), player);
	}
	
	@SubscribeEvent
	public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
		EntityPlayerMP player = (EntityPlayerMP) event.player;
		this.onSync(player, player.worldObj);
	}
	
	@SubscribeEvent
	public void onPlayerJoinDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
		this.onSync((EntityPlayerMP) event.player, event.player.worldObj);
	}
	
	@SubscribeEvent
	public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
		EntityPlayerMP player = (EntityPlayerMP) event.player;
		this.onSync(player, player.worldObj);
	}

}
