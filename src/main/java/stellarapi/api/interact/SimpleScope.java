package stellarapi.api.interact;

public class SimpleScope implements IScope {
	private final float mp;

	public SimpleScope(float multiplyingPower) {
		this.mp = multiplyingPower;
	}

	@Override
	public float transformFOV(float prevFOV) {
		return prevFOV / this.mp;
	}

}
