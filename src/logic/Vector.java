package logic;

public class Vector {
	private float x,y,z;
	
	public Vector( float x, float y, float z)
	{
		this.x=x;
		this.y=y;
		this.z=z;
	}
	
	public Vector(Vector from, Vector to)
	{
		x=to.getX()-from.getX();
		y=to.getY()-from.getY();
		z=to.getZ()-from.getZ();
	}
	
	public float getLen()
	{
		return (float)Math.sqrt(x*x+y*y+z*z);
	}
	
	public void norm()
	{
		float len=getLen();
		x/=len;
		y/=len;
		z/=len;
	}
	
	public Vector copy()
	{
		return new Vector(x,y,z);
	}
	
	public Vector add(Vector v)
	{
		x+=v.getX();
		y+=v.getY();
		z+=v.getZ();
		return this;
	}
	
	public Vector addmul(Vector v, float val)
	{
		x+=v.getX()*val;
		y+=v.getY()*val;
		z+=v.getZ()*val;
		return this;
	}
	
	public Vector mul(float val)
	{
		x*=val;
		y*=val;
		z*=val;
		return this;
	}
		
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}
	
	@Override
	public String toString() {
		return "["+x+", "+y+", "+z+"]";
	}
	
	public static Vector linePlane(Vector lp, Vector pp, Vector lv, Vector pv)
	{
		float t=pv.getX()*(pp.getX()-lp.getX())+pv.getY()*(pp.getY()-lp.getY())+pv.getZ()*(pp.getZ()-lp.getZ());
		t/=pv.getX()*lv.getX()+pv.getY()*lv.getY()+pv.getZ()*lv.getZ();
		return lp.copy().add(lv.copy().mul(t));
	}
	
	public static Vector cross(Vector v0, Vector v1)
	{
		return new Vector(	v0.getY()*v1.getZ()-v0.getZ()*v1.getY(),
							v0.getZ()*v1.getX()-v0.getX()*v1.getZ(),
							v0.getX()*v1.getY()-v0.getY()*v1.getX());
	}
}
