package stellarapi.reference;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import stellarapi.api.IPerEntityReference;
import stellarapi.api.IPerWorldReference;
import stellarapi.api.IReference;

public class StellarAPIReferenceHandler implements IReference {

	@Override
	public IPerWorldReference getPerWorldReference(World world) {
		return PerWorldManager.getPerWorldManager(world);
	}

	@Override
	public IPerEntityReference getPerEntityReference(Entity entity) {
		if(!PerEntityManager.hasEntityManager(entity))
			PerEntityManager.registerEntityManager(entity);
		
		return PerEntityManager.getEntityManager(entity);
	}

}
