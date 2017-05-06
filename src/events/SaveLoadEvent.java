package events;

import java.awt.AWTEvent;

public class SaveLoadEvent extends AWTEvent {
	public final static int SAVE=0;
	public final static int LOAD=1;
	public SaveLoadEvent(Object sourse, int id)
	{
		super(sourse,id);
	}
}
