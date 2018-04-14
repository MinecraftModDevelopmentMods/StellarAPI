package stellarapi.api.coordinates;

public interface ICoordinatesHolder {
	/** Gets the coordinates instance for given coordinates. */
	public ICoordinatesInstance getCoordinates(CCoordinates coordinates);
}
