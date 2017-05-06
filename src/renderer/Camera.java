package renderer;

import logic.Vector;

public class Camera {
	private float angle, near, far, aspectRatio;
	private Vector eye,at;
	
	public Camera(float angle, float near, float far, Vector eye, Vector at)
	{
		this.angle=angle;
		this.near=near;
		this.far=far;
		this.eye=eye;
		this.at=at;
	}
	public Camera()
	{
		eye=new Vector(10, 10, 10);
		at=new Vector(0, 0, 0);
		far=1000;
		near=0.01f;
		angle=100;
	}
	
	public float getAngle() {
		return angle;
	}
	
	public void setAngle(float visionAngle) {
		this.angle =visionAngle<1?1:visionAngle>179?179:visionAngle;
	}
	
	public float getNear() {
		return near;
	}

	public void setNear(float near) {
		this.near = near;
	}

	public float getFar() {
		return far;
	}

	public void setFar(float far) {
		this.far = far;
	}

	public Vector getEye() {
		return eye;
	}
	
	public void setEye(Vector eyePosition) {
		this.eye = eyePosition;
	}
	
	public Vector getAt() {
		return at;
	}
	
	public void setAt(Vector atPosition) {
		this.at = atPosition;
	}
	
	public Vector getUp()
	{
		Vector c=new Vector(eye, at);
		Vector up=Vector.cross(Vector.cross(eye, new Vector(0,1,0)), c);
		if (up.getY()<0)
			up.mul(-1);
		up.norm();
		//up.add(eye);
		return new Vector(0, 1,0);
	}

	public float getAspectRatio() {
		return aspectRatio;
	}

	public void setAspectRatio(float aspectRatio) {
		this.aspectRatio = aspectRatio;
	}
}
