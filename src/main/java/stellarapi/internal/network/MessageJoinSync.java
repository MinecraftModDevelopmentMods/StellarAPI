package stellarapi.internal.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageJoinSync implements IMessage {

	@Override
	public void fromBytes(ByteBuf buf) {
		// TODO Send join sync messages

	}

	@Override
	public void toBytes(ByteBuf buf) {
		// TODO Receive join sync messages

	}

}
