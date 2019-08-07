package stellarapi.feature.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import stellarapi.feature.perdimres.PerDimensionResourceData;

public class StellarAPINetworkManager {

	private SimpleNetworkWrapper wrapper;
	protected String id = "stellarapichannel";

	public StellarAPINetworkManager() {
		this.wrapper = NetworkRegistry.INSTANCE.newSimpleChannel(this.id);

		wrapper.registerMessage(MessageSync.MessageSyncCommonHandler.class, MessageSync.class, 0, Side.CLIENT);
	}

	public void onSyncToAll(World world) {
		PerDimensionResourceData data = PerDimensionResourceData.getData(world);

		NBTTagCompound compound = new NBTTagCompound();
		data.writeToNBT(compound);

		wrapper.sendToDimension(new MessageSync(compound), world.provider.getDimension());
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
		this.onSync(player, player.world);
	}

	@SubscribeEvent
	public void onPlayerJoinDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
		this.onSync((EntityPlayerMP) event.player, event.player.world);
	}

	@SubscribeEvent
	public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
		EntityPlayerMP player = (EntityPlayerMP) event.player;
		this.onSync(player, player.world);
	}

}
