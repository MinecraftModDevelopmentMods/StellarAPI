package stellarapi.feature.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import stellarapi.StellarAPI;
import stellarapi.feature.perdimres.PerDimensionResourceData;

public class MessageSync implements IMessage {

	private NBTTagCompound compoundInfo;

	public MessageSync() {
	}

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
		public IMessage onMessage(final MessageSync message, MessageContext ctx) {
			StellarAPI.proxy.registerTask(new Runnable() {
				@Override
				public void run() {
					PerDimensionResourceData data = PerDimensionResourceData.getData(StellarAPI.proxy.getClientWorld());
					data.readFromNBT(message.compoundInfo);
				}
			});

			return null;
		}

	}

}
