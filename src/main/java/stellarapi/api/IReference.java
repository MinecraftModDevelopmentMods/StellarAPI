package stellarapi.api;

import net.minecraft.entity.Entity;

public interface IReference {
	public IUpdatedOpticalViewer getUpdatedViewerSafe(Entity entity); 

	public IClientReference getPerClientReference();

}