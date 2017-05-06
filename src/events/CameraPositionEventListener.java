package events;

import java.util.EventListener;

public interface CameraPositionEventListener extends EventListener{
	public void onCameraPositionEvent(CameraPositionEvent e);

}
