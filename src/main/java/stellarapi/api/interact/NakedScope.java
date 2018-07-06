package stellarapi.api.interact;

public class NakedScope implements IScope {

	@Override
	public float transformFOV(float prevFOV) {
		return prevFOV;
	}

}
