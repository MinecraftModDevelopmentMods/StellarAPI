package stellarapi.feature.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import stellarapi.api.ICelestialPack;
import stellarapi.api.ICelestialWorld;
import stellarapi.api.SAPICapabilities;
import stellarapi.api.SAPIReferences;
import stellarapi.reference.CelestialPackManager;

public class MessageSyncPackSettings implements IMessage {
	private String packName;
	private NBTTagCompound compoundInfo;

	public MessageSyncPackSettings() { }

	public MessageSyncPackSettings(ICelestialPack pack) {
		this.packName = pack.getPackName();
		this.compoundInfo = pack.serializeNBT();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.packName = ByteBufUtils.readUTF8String(buf);
		this.compoundInfo = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, this.packName);
		ByteBufUtils.writeTag(buf, this.compoundInfo);
	}

	public static class MessageSyncPackHandler implements IMessageHandler<MessageSyncPackSettings, IMessage> {
		@Override
		public IMessage onMessage(MessageSyncPackSettings message, MessageContext ctx) {
			World world = SAPIReferences.getDefaultWorld(true);
			ICelestialWorld cWorld = world.getCapability(SAPICapabilities.CELESTIAL_CAPABILITY, null);
			if(cWorld instanceof CelestialPackManager) {
				ICelestialPack pack = SAPIReferences.getPackWithName(message.packName);
				if(pack == null)
					throw new IllegalStateException("No pack detected on client side, something should be wrong.");
				pack.deserializeNBT(message.compoundInfo);
				((CelestialPackManager) cWorld).loadPack(pack);
			}

			return null;
		}
	}

}
