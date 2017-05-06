package events;

import java.awt.AWTEvent;
import java.awt.Event;

import logic.Vector;

public class CameraAngleEvent extends AWTEvent {
	
	private float angle;
	
	public CameraAngleEvent(Object sourse, float angle)
	{
		super(sourse,0);
		this.angle=angle;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}
}
