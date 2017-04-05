package stellarapi.api;

import net.minecraft.world.World;
import stellarapi.api.gui.loading.ICombinedProgressUpdate;

/**
 * Interface of per client(client-only) reference to improve independence of
 * api.
 */
public interface IClientReference {

	public ICombinedProgressUpdate getLoadingProgress();

	public World getClientWorld();

}
