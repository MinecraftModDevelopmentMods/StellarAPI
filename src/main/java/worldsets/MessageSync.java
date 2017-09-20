package worldsets;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageSync implements IMessage {

	private NBTTagCompound content;

	public MessageSync() {}
	public MessageSync(NBTTagCompound content) { this.content = content; }

	@Override
	public void fromBytes(ByteBuf buf) { this.content = ByteBufUtils.readTag(buf); }
	@Override
	public void toBytes(ByteBuf buf) { ByteBufUtils.writeTag(buf, this.content); }

	public static class MessageSyncHandler implements IMessageHandler<MessageSync, IMessage> {
		@Override
		public IMessage onMessage(MessageSync message, MessageContext ctx) {
			WorldSetAPI.proxy.getListener().addScheduledTask(new Runnable() {
				@Override
				public void run() {
					WorldSetAPI.getNetHandler().onSyncPacket(message.content);
				}
			});
			return null;
		}
	}
}
