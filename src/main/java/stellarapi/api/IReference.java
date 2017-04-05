package stellarapi.api;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import stellarapi.api.celestials.ICelestialUniverse;

public interface IReference {
	public IUpdatedOpticalViewer getUpdatedViewerSafe(Entity entity); 

	public IClientReference getPerClientReference();

}