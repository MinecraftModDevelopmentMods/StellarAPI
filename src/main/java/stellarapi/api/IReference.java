package stellarapi.api;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public interface IReference {

	IPerWorldReference getPerWorldReference(World world);
	IPerEntityReference getPerEntityReference(Entity entity);

}
