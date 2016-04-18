package stellarapi.api.event;

import net.minecraft.entity.Entity;
import stellarapi.api.IViewScope;

/**
 * Fired to reset the scope.
 * */
public class ResetScopeEvent extends PerEntityEvent {

	private IViewScope scope;
	
	/**Additional parameters, like items which is changed*/
	private Object[] params;
	
	public ResetScopeEvent(Entity entity, IViewScope defScope, Object... additionalParams) {
		super(entity);
		this.scope = defScope;
		this.params = additionalParams;
	}
	
	public IViewScope getScope() {
		return this.scope;
	}
	
	public void setScope(IViewScope scope) {
		this.scope = scope;
	}
	
	public Object[] getAdditionalParams() {
		return this.params;
	}

}
