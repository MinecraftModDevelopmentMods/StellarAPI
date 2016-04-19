package stellarapi.api;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import stellarapi.api.event.UpdateFilterEvent;
import stellarapi.api.event.UpdateScopeEvent;
import stellarapi.api.optics.IOpticalFilter;
import stellarapi.api.optics.IViewScope;
import stellarapi.api.optics.NakedFilter;
import stellarapi.api.optics.NakedScope;

/**
 * Per entity manager to contain the per-entity objects.
 * */
public class PerEntityManager implements IExtendedEntityProperties {
	
	private static final String ID = "stellarapiperplayermanager";
	
	private Entity entity;

	private IViewScope scope = null;
	private IOpticalFilter filter = null;
	
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

	public void updateScope(Object... additionalParams) {
		UpdateScopeEvent scopeEvent = new UpdateScopeEvent(this.entity, new NakedScope(), additionalParams);
		StellarAPIReference.getEventBus().post(scopeEvent);
		this.scope = scopeEvent.getScope();
	}
	
	public void updateFilter(Object... additionalParams) {
		UpdateFilterEvent filterEvent = new UpdateFilterEvent(this.entity, new NakedFilter(), additionalParams);
		StellarAPIReference.getEventBus().post(filterEvent);
		this.filter = filterEvent.getFilter();
	}
	
	public IViewScope getScope() {
		if(this.scope == null)
			this.updateScope();
		return this.scope;
	}
	
	public IOpticalFilter getFilter() {
		if(this.filter == null)
			this.updateFilter();
		return this.filter;
	}
	
	
	@Override
	public void init(Entity entity, World world) { }
	
	@Override
	public void loadNBTData(NBTTagCompound compound) { }
	
	@Override
	public void saveNBTData(NBTTagCompound compound) { }

}
