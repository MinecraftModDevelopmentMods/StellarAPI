package worldsets;

import net.minecraft.util.IThreadListener;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class CommonProxy {
	public World getDefaultWorld() {
		return null;
	}

	public IThreadListener getClientListener() {
		return null;
	}
}