package stellarapi.api.event;

import net.minecraft.entity.Entity;
import stellarapi.api.IViewScope;

/**
 * Fired to reset the scope.
 * */
public class ResetScopeEvent extends PerEntityEvent {

	private IViewScope scope;
	
	public ResetScopeEvent(Entity entity, IViewScope defScope) {
		super(entity);
		this.scope = defScope;
	}
	
	public IViewScope getScope() {
		return this.scope;
	}
	
	public void setScope(IViewScope scope) {
		this.scope = scope;
	}

}
