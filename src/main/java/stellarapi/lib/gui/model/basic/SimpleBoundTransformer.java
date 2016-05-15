package stellarapi.lib.gui.model.basic;

public class SimpleBoundTransformer {
	
	private int[] index = {0, 1, 2, 3};
	private float[] cached = new float[4];
	private boolean isRotated = false;
	
	public SimpleBoundTransformer setReflectedX() {
		int temp = index[3];
		index[3] = index[1];
		index[1] = temp;
		return this;
	}
	
	public SimpleBoundTransformer setReflectedY() {
		int temp = index[2];
		index[2] = index[0];
		index[0] = temp;
		return this;
	}
	
	/**Rotated counterclockwise*/
	public SimpleBoundTransformer setRotated() {
		int temp = index[0];
		index[0] = index[3];
		index[3] = index[2];
		index[2] = index[1];
		index[1] = temp;
		this.isRotated = !this.isRotated;
		return this;
	}
	
	public void setBound(float left, float up, float right, float down) {
		cached[0] = up;
		cached[1] = left;
		cached[2] = down;
		cached[3] = right;
	}
	
	public float transformLeft() {
		return cached[index[1]];
	}
	
	public float transformUp() {
		return cached[index[0]];
	}
	
	public float transformRight() {
		return cached[index[3]];
	}
	
	public float transformDown() {
		return cached[index[2]];
	}

	public void reset(SimpleBoundTransformer newTransformer) {
		System.arraycopy(newTransformer.index, 0, this.index, 0, index.length);
		this.isRotated = newTransformer.isRotated;
	}
	
	public void reset() {
		for(int i = 0; i < index.length; i++)
			index[i] = i;
		this.isRotated = false;
	}

	public boolean isRotated() {
		return this.isRotated;
	}
}
