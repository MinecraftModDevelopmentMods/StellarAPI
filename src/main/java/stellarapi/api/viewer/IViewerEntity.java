package stellarapi.api.viewer;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;

public interface IViewerEntity {
	/**
	 * Gets the viewer entity.
	 * It's <code>null</code> for client-only entities.
	 * */
	public @Nullable Entity getViewer();

	/**
	 * Finds the viewer entity and sets it.
	 * */
	public void findViewerAndSet(UUID id);
}