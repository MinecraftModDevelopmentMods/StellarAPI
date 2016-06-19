package stellarapi.reference;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldSavedData;

/**
 * Per server manager to contain the per-server objects.
 */
public class PerServerManager extends WorldSavedData {

	private static final String ID = "stellarapiperservermanager";

	public static void initiatePerServerManager(MinecraftServer server) {
		server.getEntityWorld().getMapStorage().setData(ID, new PerServerManager());
	}

	public static boolean isInitiated(MinecraftServer server) {
		return server.getEntityWorld().getMapStorage().getOrLoadData(PerServerManager.class,
				ID) instanceof PerServerManager;
	}

	public static PerServerManager getPerServerManager(MinecraftServer server) {
		return (PerServerManager) server.getEntityWorld().getMapStorage().getOrLoadData(PerServerManager.class, ID);
	}

	private PerServerManager() {
		super(ID);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		return compound;
	}

}
