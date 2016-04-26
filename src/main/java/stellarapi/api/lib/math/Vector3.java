package stellarapi.api.lib.math;

/**
 * 3-dimension Mutable Euclidian Vector for double.
 * */
public class Vector3 {
	
	private static final int DIM = 3;
	private double[] value = new double[DIM];
	
	/**
	 * Creates a Vector as Zero Vector.
	 * */
	public Vector3(){
		for(int i = 0; i < DIM; i++)
			value[i] = 0.0;
	}
	
	/**
	 * Creates a Vector via Coordinates.
	 * */
	public Vector3(double... coords){
		for(int i = 0; i < DIM; i++)
			value[i] = coords[i];
	}

	public Vector3(Vector3 vec) {
		for(int i = 0; i < DIM; i++)
			value[i] = vec.value[i];
	}

	public Vector3 set(Vector3 val) {
		for(int i = 0; i < DIM; i++)
			value[i] = val.value[i];
		return this;
	}
	
	public Vector3 set(double... coords) {
		for(int i = 0; i < DIM; i++)
			value[i] = coords[i];
		return this;
	}

	public double getCoord(int N) {
		return value[N];
	}
	
	public void setCoord(int N, double val) {
		value[N] = val;
	}
	
	public double getX() {
		return value[0];
	}
	
	public double getY() {
		return value[1];
	}

	public double getZ() {
		return value[2];
	}

	public Vector3 add(Vector3 par) {		
		for(int i = 0; i < DIM; i++)
			value[i] += par.value[i];

		return this;
	}
	
	public Vector3 setAdd(Vector3 par1, Vector3 par2) {
		for(int i = 0; i < DIM; i++)
			value[i] = par1.value[i] + par2.value[i];
		
		return this;
	}
	
	public Vector3 sub(Vector3 par) {		
		for(int i = 0; i < DIM; i++)
			value[i] -= par.value[i];

		return this;
	}
	
	public Vector3 setSub(Vector3 par1, Vector3 par2) {
		for(int i = 0; i < DIM; i++)
			value[i] = par1.value[i] - par2.value[i];
		
		return this;
	}

	public Vector3 negate() {		
		for(int i = 0; i < DIM; i++)
			value[i] = -value[i];

		return this;
	}
	
	public Vector3 scale(double scale) {
		for(int i = 0; i < DIM; i++)
			value[i] *= scale;
		
		return this;
	}
	
	public double dot(Vector3 vec) {
		double ret = 0.0;
		for(int i = 0; i < DIM; i++)
			ret += value[i] * vec.value[i];
		
		return ret;
	}
	
	public Vector3 setCross(Vector3 par1, Vector3 par2) {
		this.value[0] = par1.value[1]*par2.value[2] - par1.value[2]*par2.value[1];
		this.value[1] = par1.value[2]*par2.value[0] - par1.value[0]*par2.value[2];
		this.value[2] = par1.value[0]*par2.value[1] - par1.value[1]*par2.value[0];
		
		return this;
	}
	
	public double size2() {
		double ret = 0.0;
		for(int i = 0; i < DIM; i++)
			ret += value[i] * value[i];
		
		return ret;
	}

	public double size() {
		return Math.sqrt(this.size2());
	}
	
	public Vector3 normalize() {
		return this.scale(1.0 / this.size());
	}
	
	@Override
	public boolean equals(Object o){
		Vector3 v = (Vector3) o;
		for(int i = 0; i < DIM; i++)
			if(value[i] != v.value[i])
				return false;
		
		return true;
	}
	
	@Override
	public String toString()
	{
		String p = "(";
		for(double ite  : this.value)
		{
			p += String.valueOf(ite);
			p += ",";
		}
		return p + ")";
	}

}