package events;

import java.awt.AWTEvent;
import java.awt.Event;

import logic.Vector;

public class CameraPositionEvent extends AWTEvent {
	
	private Vector eye, at;
	
	public CameraPositionEvent(Object sourse, Vector eye, Vector at)
	{
		super(sourse,0);
		this.setEye(eye);
		this.setAt(at);
	}

	public Vector getAt() {
		return at;
	}

	public void setAt(Vector at) {
		this.at = at;
	}

	public Vector getEye() {
		return eye;
	}

	public void setEye(Vector eye) {
		this.eye = eye;
	}
}
