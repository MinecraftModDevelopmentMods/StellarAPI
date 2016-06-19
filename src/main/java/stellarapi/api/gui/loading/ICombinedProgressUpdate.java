package stellarapi.api.gui.loading;

import net.minecraft.util.IProgressUpdate;

public interface ICombinedProgressUpdate {

	/**
	 * Gets the progress. Create one if there is no progress for certain id.
	 */
	public IProgressUpdate getProgress(String id);

	/**
	 * Removes the progress.
	 */
	public void removeProgress(String id);

}
