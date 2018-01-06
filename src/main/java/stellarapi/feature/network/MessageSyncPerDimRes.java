package stellarapi.feature.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import stellarapi.StellarAPI;
import stellarapi.feature.perdimres.PerDimensionResourceData;

public class MessageSyncPerDimRes implements IMessage {
	private NBTTagCompound compoundInfo;

	public MessageSyncPerDimRes() {
	}

	public MessageSyncPerDimRes(NBTTagCompound commonInfo) {
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

	public static class MessageSyncCommonHandler implements IMessageHandler<MessageSyncPerDimRes, IMessage> {

		@Override
		public IMessage onMessage(final MessageSyncPerDimRes message, MessageContext ctx) {
			StellarAPI.PROXY.registerTask(new Runnable() {
				@Override
				public void run() {
					PerDimensionResourceData data = PerDimensionResourceData.getData(StellarAPI.PROXY.getClientWorld());
					data.readFromNBT(message.compoundInfo);
				}
			});

			return null;
		}

	}

}
