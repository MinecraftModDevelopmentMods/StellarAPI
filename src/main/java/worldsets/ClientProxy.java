package worldsets;

import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ClientProxy extends CommonProxy {
	@Override
	public World getDefaultWorld() {
		return Minecraft.getMinecraft().world;
	}

	@Override
	public IThreadListener getClientListener() {
		return Minecraft.getMinecraft();
	}
}
