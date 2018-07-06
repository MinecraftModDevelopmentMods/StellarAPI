package stellarapi.api.interact;

/**
 * Scope interface, which changes FOV.
 * Applied when an item with this capability is activated.
 * */
public interface IScope {
	public float transformFOV(float prevFOV);
}
