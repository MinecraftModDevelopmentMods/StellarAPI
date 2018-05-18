package stellarapi.feature.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import stellarapi.StellarAPI;
import stellarapi.api.ICelestialWorld;
import stellarapi.api.SAPICapabilities;
import stellarapi.feature.perdimres.PerDimensionResourceData;
import stellarapi.reference.CelestialPackManager;

public class StellarAPINetworkManager {

	private SimpleNetworkWrapper wrapper;
	protected String id = "stellarapichannel";

	public StellarAPINetworkManager() {
		this.wrapper = NetworkRegistry.INSTANCE.newSimpleChannel(this.id);

		wrapper.registerMessage(MessageSyncPerDimRes.MessageSyncCommonHandler.class,
				MessageSyncPerDimRes.class, 0, Side.CLIENT);
		wrapper.registerMessage(MessageSyncPackSettings.MessageSyncPackHandler.class,
				MessageSyncPackSettings.class, 1, Side.CLIENT);
	}

	public void onSyncToAll(World world) {
		PerDimensionResourceData data = PerDimensionResourceData.getData(world);

		NBTTagCompound compound = new NBTTagCompound();
		data.writeToNBT(compound);

		wrapper.sendToDimension(new MessageSyncPerDimRes(compound), world.provider.getDimension());
	}

	public void onSync(EntityPlayerMP player, World world) {
		PerDimensionResourceData data = PerDimensionResourceData.getData(world);

		NBTTagCompound compound = new NBTTagCompound();
		data.writeToNBT(compound);

		wrapper.sendTo(new MessageSyncPerDimRes(compound), player);


		ICelestialWorld cWorld = world.getCapability(SAPICapabilities.CELESTIAL_CAPABILITY, null);
		if(cWorld instanceof CelestialPackManager) {
			IMessage syncMessage = ((CelestialPackManager) cWorld).getSyncMessage();
			if(syncMessage != null)
				wrapper.sendTo(syncMessage, player);
		}
	}

	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load loadEvent) {
		if(loadEvent.getWorld().isRemote && !StellarAPI.INSTANCE.existOnServer()) {
			World world = loadEvent.getWorld();
			ICelestialWorld cWorld = world.getCapability(SAPICapabilities.CELESTIAL_CAPABILITY, null);
			if(cWorld instanceof CelestialPackManager) {
				((CelestialPackManager) cWorld).onLackServerAPI();
			}
		}
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
