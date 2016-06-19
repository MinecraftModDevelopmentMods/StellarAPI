package stellarapi.api;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public interface IReference {

	public IPerWorldReference getPerWorldReference(World world);

	public IPerEntityReference getPerEntityReference(Entity entity);

	public IPerClientReference getPerClientReference();

}
