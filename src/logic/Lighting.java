package logic;

public class Lighting {
	public static final float[] DEFAULT_POSITION={50,50,50};
	public static final float[] DEFAULT_AMBIENT={0,0,0,1};
	public static final float[] DEFAULT_DIFFUSE={1,1,1,1};
	public static final float[] DEFAULT_SPECULAR={1,1,1,1};
	public static final float[] DEFAULT_LM_AMBIET={0.2f,0.2f,0.2f,1};
	private float[] ambient;
	private float[] diffuse;
	private float[] specular;
	private float[] position;
	private float[] lmAmbient;
	
	public Lighting()
	{
		this.ambient=DEFAULT_AMBIENT;
		this.diffuse=DEFAULT_DIFFUSE;
		this.specular=DEFAULT_SPECULAR;
		this.lmAmbient=DEFAULT_LM_AMBIET;
		this.position=DEFAULT_POSITION;
		
	}
	public Lighting(float [] position, float[] ambient, float[] diffuse, float[] specular, float[] lmAmbient)
	{
		this.ambient=ambient==null ? DEFAULT_AMBIENT : ambient;
		this.diffuse=diffuse==null ? DEFAULT_DIFFUSE : diffuse;
		this.specular=specular==null ? DEFAULT_SPECULAR : specular;
		this.lmAmbient=lmAmbient==null ? DEFAULT_LM_AMBIET : lmAmbient;
		this.position=position==null ? DEFAULT_POSITION : position;
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
	public float[] getLmAmbient() {
		return lmAmbient;
	}
	public void setLmAmbient(float[] lmAmbient) {
		this.lmAmbient = lmAmbient;
	}
	public float[] getPosition() {
		return position;
	}
	public void setPosition(float[] position) {
		this.position = position;
	}
	
}
