package logic;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import events.CameraAngleEvent;
import events.CameraPositionEvent;
import events.SaveLoadEvent;
import events.ToolbarEventListener;
import gui.Toolbar;
import gui.Viewport;
import renderer.Camera;
import renderer.OrthoRenderer;
import renderer.Renderer;

public class Controller implements ToolbarEventListener, MouseListener, MouseMotionListener{
	private static final float[] verts={1,1,1,-1,-1,-1};
	public static final MeshBox DEFAULT_WORLD_BOX=new MeshBox(verts);
	private MeshBox worldBox=DEFAULT_WORLD_BOX;
	private List<Mesh> meshes;
	private List<Viewport> viewports;
	private Toolbar toolbar;
	private Camera camera;
	private Lighting lighting;
	private ObjManager objManager;
	private Point prevLMB;
	private Point prevRMB;
	
	
	public Controller(JFrame frame)
	{
		meshes=new ArrayList<>();
		viewports=new ArrayList<>();
		objManager=new ObjManager(frame);
		lighting=new Lighting();
	}
	
	public void addViewport(Viewport viewport)
	{
		viewports.add(viewport);
		viewport.getRenderer().setMeshList(meshes);
		viewport.getRenderer().setWorldBox(worldBox);
		viewport.getRenderer().setCamera(camera);
		viewport.getRenderer().setLighting(lighting);
		if(viewport.getRenderer().getViewType()!=Renderer.VIEW_PERSPECTIVE)
		{
			viewport.addMouseListener(this);
			viewport.addMouseMotionListener(this);
		}
	}
	
	public void addMesh(Mesh mesh)
	{
		meshes.add(mesh);
	}
	
	public void clearMeshes()
	{
		meshes.clear();
		worldBox=DEFAULT_WORLD_BOX;
		updateViewports();
	}
	
	private void updateViewports()
	{
 		for (Viewport viewport : viewports) {
			viewport.getRenderer().setForceFullUpdate(true);
			viewport.getRenderer().setLighting(lighting);
			viewport.getRenderer().setCamera(camera);
			viewport.repaint();
		}
	}
	
	private void updateWorldBox()
	{
		if (meshes!=null || meshes.size()>0)
		{
			worldBox.setMaxX(-Float.MAX_VALUE);
			worldBox.setMaxY(-Float.MAX_VALUE);
			worldBox.setMaxZ(-Float.MAX_VALUE);
			
			worldBox.setMinX(Float.MAX_VALUE);
			worldBox.setMinY(Float.MAX_VALUE);
			worldBox.setMinZ(Float.MAX_VALUE);
			
			for (Mesh mesh : meshes) {
				if(worldBox.getMaxX()<mesh.getBox().getMaxX())
					worldBox.setMaxX(mesh.getBox().getMaxX());
				if(worldBox.getMaxY()<mesh.getBox().getMaxY())
					worldBox.setMaxY(mesh.getBox().getMaxY());
				if(worldBox.getMaxZ()<mesh.getBox().getMaxZ())
					worldBox.setMaxZ(mesh.getBox().getMaxZ());
				
				if(worldBox.getMinX()>mesh.getBox().getMinX())
					worldBox.setMinX(mesh.getBox().getMinX());
				if(worldBox.getMinY()>mesh.getBox().getMinY())
					worldBox.setMinY(mesh.getBox().getMinY());
				if(worldBox.getMinZ()>mesh.getBox().getMinZ())
					worldBox.setMinZ(mesh.getBox().getMinZ());
			}
			worldBox.setMaxX(1*Math.max(camera.getEye().getX(), worldBox.getMaxX()));
			worldBox.setMaxY(1*Math.max(camera.getEye().getY(), worldBox.getMaxY()));
			worldBox.setMaxZ(1*Math.max(camera.getEye().getZ(), worldBox.getMaxZ()));
			
			worldBox.setMinX(1*Math.min(camera.getEye().getX(), worldBox.getMinX()));
			worldBox.setMinY(1*Math.min(camera.getEye().getY(), worldBox.getMinY()));
			worldBox.setMinZ(1*Math.min(camera.getEye().getZ(), worldBox.getMinZ()));
		
			worldBox.setMaxX(1*Math.max(camera.getAt().getX(), worldBox.getMaxX()));
			worldBox.setMaxY(1*Math.max(camera.getAt().getY(), worldBox.getMaxY()));
			worldBox.setMaxZ(1*Math.max(camera.getAt().getZ(), worldBox.getMaxZ()));
			
			worldBox.setMinX(Math.min(camera.getAt().getX(), worldBox.getMinX()));
			worldBox.setMinY(Math.min(camera.getAt().getY(), worldBox.getMinY()));
			worldBox.setMinZ(Math.min(camera.getAt().getZ(), worldBox.getMinZ()));
		}
		else
		{
			worldBox=DEFAULT_WORLD_BOX;
		}
		
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
		if(toolbar!=null)
		{
			toolbar.setEye(camera.getEye());
			toolbar.setAt(camera.getAt());
		}
	}
	
	public void setLighting(Lighting lighting) {
		this.lighting=lighting;
	}

	@Override
	public void onCameraPositionEvent(CameraPositionEvent e) {
		camera.setEye(e.getEye());
		camera.setAt(e.getAt());
		updateWorldBox();
		updateViewports();
		if(e.getSource() instanceof Viewport)
		{
			toolbar.setEye(e.getEye());
			toolbar.setAt(e.getAt());
		}
	}

	@Override
	public void onCameraAngleEvent(CameraAngleEvent e) {
		camera.setAngle(e.getAngle());
		updateViewports();
		updateWorldBox();
	}
	
	@Override
	public void onSaveLoadEvent(SaveLoadEvent e) {
		if(e.getID()==SaveLoadEvent.LOAD)
		{
			if(objManager.loadObj())
			{
				setCamera(objManager.getCamera());
				setLighting(objManager.getLighting());
				clearMeshes();
				addMesh(objManager.getMesh());
				updateWorldBox();
				updateViewports();
			}
		}
		if(e.getID()==SaveLoadEvent.SAVE)
		{
			objManager.saveObj(meshes.get(0),lighting,camera);
		}
	}
	
	public void setToolbar(Toolbar toolbar) {
		this.toolbar=toolbar;
		toolbar.setToolbarEventListener(this);
		if(camera!=null)
		{
			toolbar.setEye(camera.getEye());
			toolbar.setAt(camera.getAt());
		}
	}
	
	
	
	
	
	
//VIEWPOTR LISTENER STUFF
	
	@Override
	public void mouseDragged(MouseEvent e) {
		
		Viewport viewport=(Viewport)e.getSource();
		if(viewport.getRenderer() instanceof OrthoRenderer)
		{
			if((e.getModifiersEx()&MouseEvent.BUTTON1_DOWN_MASK)!=0)
			{
				//System.out.println("LMB drag");
				if(prevLMB!=null)
				{
					float dx=(float) (0.1*(e.getPoint().getX()-prevLMB.getX()));
					float dy=(float) (0.1*(e.getPoint().getY()-prevLMB.getY()));
					switch(viewport.getRenderer().getViewType())
					{
					case Renderer.VIEW_FRONT:
						camera.getEye().setX(camera.getEye().getX()+dx);
						camera.getEye().setY(camera.getEye().getY()-dy);
						break;
					case Renderer.VIEW_TOP:
						camera.getEye().setX(camera.getEye().getX()+dx);
						camera.getEye().setZ(camera.getEye().getZ()+dy);
						break;
					case Renderer.VIEW_LEFT:
						camera.getEye().setZ(camera.getEye().getZ()+dx);
						camera.getEye().setY(camera.getEye().getY()-dy);
						break;
					}
					toolbar.setEye(camera.getEye());
				}
				prevLMB=e.getPoint();
			}
			
			if((e.getModifiersEx()&MouseEvent.BUTTON3_DOWN_MASK)!=0)
			{
				//System.out.println("RMB drag");
				if(prevRMB!=null)
				{
					float dx=(float) (0.1*(e.getPoint().getX()-prevRMB.getX()));
					float dy=(float) (0.1*(e.getPoint().getY()-prevRMB.getY()));
					switch(viewport.getRenderer().getViewType())
					{
					case Renderer.VIEW_FRONT:
						camera.getAt().setX(camera.getAt().getX()+dx);
						camera.getAt().setY(camera.getAt().getY()-dy);
						break;
					case Renderer.VIEW_TOP:
						camera.getAt().setX(camera.getAt().getX()+dx);
						camera.getAt().setZ(camera.getAt().getZ()+dy);
						break;
					case Renderer.VIEW_LEFT:
						camera.getAt().setZ(camera.getAt().getZ()+dx);
						camera.getAt().setY(camera.getAt().getY()-dy);
						break;
					}
					toolbar.setAt(camera.getAt());
				}
				prevRMB=e.getPoint();
			}
		}
		updateViewports();
		updateWorldBox();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		Viewport viewport=(Viewport)e.getSource();
		if(viewport.getRenderer() instanceof OrthoRenderer)
		{
			if(e.getButton()==MouseEvent.BUTTON1)
			{
				prevLMB=null;
			}
			if(e.getButton()==MouseEvent.BUTTON3)
			{
				prevRMB=null;
			}
		}
	}

	
}
