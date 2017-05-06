package events;

import java.util.EventListener;

public interface ToolbarEventListener extends CameraPositionEventListener{
	public void onCameraAngleEvent(CameraAngleEvent e);
	public void onSaveLoadEvent(SaveLoadEvent e);
}
