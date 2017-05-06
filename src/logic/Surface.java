package logic;

public class Surface {
	
	public static final float[] DEFAULT_AMBIENT={0.2f,0.2f,0.2f,1};
	public static final float[] DEFAULT_DIFFUSE={0.8f,0.8f,0.8f,1};
	public static final float[] DEFAULT_SPECULAR={0,0,0,1};
	public static final float[] DEFAULT_SHININESS={0};
	
	public static final int AMBIENT_COMPONENT=0;
	public static final int DIFFUSE_COMPONENT=1;
	public static final int SPECULAR_COMPONENT=2;
	public static final int SHININESS_COMPONENT=3;
	
	
	private float[] ambient;
	private float[] diffuse;
	private float[] specular;
	private float[] shininess;
	
	public Surface()
	{
		this.ambient=DEFAULT_AMBIENT;
		this.diffuse=DEFAULT_DIFFUSE;
		this.specular=DEFAULT_SPECULAR;
		this.shininess=DEFAULT_SHININESS;
		
	}
	public Surface(float[] ambient, float[] diffuse, float[] specular, float[] shininess)
	{
		this.ambient=ambient==null ? DEFAULT_AMBIENT : ambient;
		this.diffuse=diffuse==null ? DEFAULT_DIFFUSE : diffuse;
		this.specular=specular==null ? DEFAULT_SPECULAR : specular;
		this.shininess=shininess==null ? DEFAULT_SHININESS : shininess;
	}
	
	public float[] getAmbient() {
		return ambient;
	}
	public void setAmbient(float[] ambient) {
		this.ambient = ambient;
	}
	public float[] getDiffuse() {
		return diffuse;
	}
	public void setDiffuse(float[] diffuse) {
		this.diffuse = diffuse;
	}
	public float[] getSpecular() {
		return specular;
	}
	public void setSpecular(float[] specular) {
		this.specular = specular;
	}
	public float[] getShininess() {
		return shininess;
	}
	public void setShininess(float[] shininess) {
		this.shininess = shininess;
	}
	
	public  String toString() {
		return diffuse[0]+" "+diffuse[1]+" "+diffuse[2]+" "+diffuse[3];
	}

}
