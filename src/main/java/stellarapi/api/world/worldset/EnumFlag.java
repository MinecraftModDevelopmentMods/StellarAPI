package stellarapi.api.world.worldset;

/**
 * Represents proposition state with uncertain case included.
 * */
public enum EnumFlag {
	UNCERTAIN(false, false),
	TRUE(true, false),
	FALSE(false, true);

	public final boolean isTrue, isFalse;

	private EnumFlag(boolean isTrue, boolean isFalse) {
		this.isTrue = isTrue;
		this.isFalse = isFalse;
	}
}
