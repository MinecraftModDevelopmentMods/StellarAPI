package stellarapi.api;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import stellarapi.api.optics.IOpticalFilter;
import stellarapi.api.optics.IViewScope;

public interface IReference {

	public IPerWorldReference getPerWorldReference(World world);

	public IPerEntityReference getPerEntityReference(Entity entity);

	public World getClientWorld();
	public IViewScope getDefaultScope();
	public IOpticalFilter getDefaultFilter();
}
