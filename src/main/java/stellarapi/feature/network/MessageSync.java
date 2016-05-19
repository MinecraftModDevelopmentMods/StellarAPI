package stellarapi.feature.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import stellarapi.StellarAPI;
import stellarapi.feature.perdimres.PerDimensionResourceData;

public class MessageSync implements IMessage {

	private NBTTagCompound compoundInfo;
	
	public MessageSync() { }
	
	public MessageSync(NBTTagCompound commonInfo) {
		this.compoundInfo = commonInfo;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.compoundInfo = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, this.compoundInfo);
	}
	
	public static class MessageSyncCommonHandler implements IMessageHandler<MessageSync, IMessage> {

		@Override
		public IMessage onMessage(MessageSync message, MessageContext ctx) {
			PerDimensionResourceData data = PerDimensionResourceData.getData(StellarAPI.proxy.getClientWorld());
			data.readFromNBT(message.compoundInfo);
			
			return null;
		}
		
	}

}
