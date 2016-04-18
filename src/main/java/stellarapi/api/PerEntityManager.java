package stellarapi.api;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import stellarapi.api.event.ResetScopeEvent;

public class PerEntityManager implements IExtendedEntityProperties {
	
	private static final String ID = "stellarapiperplayermanager";
	
	private Entity entity;

	private IViewScope scope = null;
	
	public static void registerEntityManager(Entity entity) {
		entity.registerExtendedProperties(ID, new PerEntityManager(entity));
	}
	
	public static boolean hasEntityManager(Entity entity) {
		return entity.getExtendedProperties(ID) instanceof PerEntityManager;
	}
	
	public static PerEntityManager getEntityManager(Entity entity) {
		return (PerEntityManager) entity.getExtendedProperties(ID);
	}
	
	public PerEntityManager(Entity entity) {
		this.entity = entity;
	}

	public void resetScope(Object... additionalParams) {
		ResetScopeEvent scopeEvent = new ResetScopeEvent(this.entity, new NakedScope(), additionalParams);
		StellarAPIReference.getEventBus().post(scopeEvent);
		this.scope = scopeEvent.getScope();
	}
	
	public IViewScope getScope() {
		if(this.scope == null)
			this.resetScope();
		return this.scope;
	}
	
	
	@Override
	public void init(Entity entity, World world) { }
	
	@Override
	public void loadNBTData(NBTTagCompound compound) { }
	
	@Override
	public void saveNBTData(NBTTagCompound compound) { }

}
