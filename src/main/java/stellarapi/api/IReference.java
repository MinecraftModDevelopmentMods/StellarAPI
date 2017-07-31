package stellarapi.api;

import net.minecraft.entity.Entity;
import stellarapi.api.optics.IOpticalFilter;
import stellarapi.api.optics.IViewScope;

public interface IReference {
	public IUpdatedOpticalViewer getUpdatedViewerSafe(Entity entity); 

	public IClientReference getPerClientReference();

	public IViewScope getDefaultScope();
	public IOpticalFilter getDefaultFilter();

}