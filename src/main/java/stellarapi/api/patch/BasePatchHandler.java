package stellarapi.api.patch;

import net.minecraft.entity.player.EntityPlayer;
import stellarapi.StellarAPI;

public class BasePatchHandler {
	private static boolean isLocationSpecific;

	public static boolean isLocationSpecific() {
		return isLocationSpecific;
	}

	public static EntityPlayer getDefaultPlayer() {
		// TODO Eliminate StellarAPI direct reference
		return StellarAPI.proxy.getClientPlayer();
	}
}