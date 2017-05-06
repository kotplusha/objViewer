package renderer;

import java.nio.FloatBuffer;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.math.VectorUtil;

import logic.Controller;
import logic.Mesh;
import logic.MeshBox;
import logic.Surface;
import logic.Vector;

public class OrthoRenderer extends Renderer {
	public static final float MARGIN_RATE=1.5f;
	public static final float MARKER_RATE=0.05f;
	
	private float markerSize=0.1f;
	
	public OrthoRenderer(byte viewType) {
		this.viewType=viewType;
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl=drawable.getGL().getGL2();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		
		reshape(drawable, 0, 0, drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
		
		renderAxisGizmo(gl);
		//renderCamera(gl, drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
		
		renderMeshes(gl);
		renderMarker(gl, camera.getEye());
		renderMarker(gl, camera.getAt());
		//renderFrustum(gl);
		drawCamera(gl);
		
		
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl=drawable.getGL().getGL2();
		gl.glClearColor(0.5f, 0.5f, 0.5f,5.0f);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		switch (viewType)
		{
		case VIEW_TOP:
			glu.gluLookAt(0,1,0, 0, 0, 0, 0, 0, -1);
			break;
		case VIEW_LEFT:
			glu.gluLookAt(-1,0,0, 0, 0, 0, 0, 1, 0);
			break;
		case VIEW_FRONT:
			glu.gluLookAt(0,0,1, 0, 0, 0, 0, 3, 0);
			break;
		}
		
		setUpDepthTest(gl);
		setUpLighting(gl);
		
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		//System.out.println("Ortho reshape");
		GL2 gl=drawable.getGL().getGL2();
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		
		float maxAbsX, maxAbsY, maxAbsZ=maxAbsY=maxAbsX=0;
		
		switch (viewType)
		{
		case VIEW_TOP:
			maxAbsX=Math.max(Math.abs(worldBox.getMaxX()), Math.abs(worldBox.getMinX()));
			maxAbsY=Math.max(Math.abs(worldBox.getMaxZ()), Math.abs(worldBox.getMinZ()));
			maxAbsZ=Math.max(Math.abs(worldBox.getMaxY()), Math.abs(worldBox.getMinY()));
			break;
		case VIEW_LEFT:
			maxAbsX=Math.max(Math.abs(worldBox.getMaxZ()), Math.abs(worldBox.getMinZ()));
			maxAbsY=Math.max(Math.abs(worldBox.getMaxY()), Math.abs(worldBox.getMinY()));
			maxAbsZ=Math.max(Math.abs(worldBox.getMaxX()), Math.abs(worldBox.getMinX()));
			break;
		case VIEW_FRONT:
			maxAbsX=Math.max(Math.abs(worldBox.getMaxX()), Math.abs(worldBox.getMinX()));
			maxAbsY=Math.max(Math.abs(worldBox.getMaxY()), Math.abs(worldBox.getMinY()));
			maxAbsZ=Math.max(Math.abs(worldBox.getMaxZ()), Math.abs(worldBox.getMinZ()));
			break;
		default:
			break;
		}
		if(maxAbsX*height/width>=maxAbsY)
			maxAbsY=maxAbsX*height/width;
		else
			maxAbsX=maxAbsY*width/height;
		maxAbsX*=MARGIN_RATE;
		maxAbsY*=MARGIN_RATE;
		maxAbsZ*=MARGIN_RATE;
		gl.glOrthof(-maxAbsX,maxAbsX,-maxAbsY, maxAbsY,-maxAbsZ,maxAbsZ);
		
		markerSize=Math.min(maxAbsX,maxAbsY)*MARKER_RATE;

	}
	
	public void setWorldBox(MeshBox worldBox)
	{
		this.worldBox=worldBox;
	}
	
	private void renderMarker(GL2 gl, Vector v)
	{
		gl.glDisable(GL2.GL_LIGHTING);
		gl.glBegin(GL.GL_LINE_LOOP);
		gl.glVertex3f(v.getX()-markerSize,v.getY()-markerSize,v.getZ()+markerSize);
		gl.glVertex3f(v.getX()+markerSize,v.getY()-markerSize,v.getZ()+markerSize);
		gl.glVertex3f(v.getX()+markerSize,v.getY()+markerSize,v.getZ()+markerSize);
		gl.glVertex3f(v.getX()-markerSize,v.getY()+markerSize,v.getZ()+markerSize);
		gl.glEnd();
		
		gl.glBegin(GL.GL_LINE_LOOP);
		gl.glVertex3f(v.getX()-markerSize,v.getY()-markerSize,v.getZ()-markerSize);
		gl.glVertex3f(v.getX()+markerSize,v.getY()-markerSize,v.getZ()-markerSize);
		gl.glVertex3f(v.getX()+markerSize,v.getY()+markerSize,v.getZ()-markerSize);
		gl.glVertex3f(v.getX()-markerSize,v.getY()+markerSize,v.getZ()-markerSize);
		gl.glEnd();
		
		gl.glBegin(GL.GL_LINES);
		gl.glVertex3f(v.getX()-markerSize,v.getY()-markerSize,v.getZ()+markerSize);
		gl.glVertex3f(v.getX()-markerSize,v.getY()-markerSize,v.getZ()-markerSize);
		
		gl.glVertex3f(v.getX()+markerSize,v.getY()-markerSize,v.getZ()+markerSize);
		gl.glVertex3f(v.getX()+markerSize,v.getY()-markerSize,v.getZ()-markerSize);
		
		gl.glVertex3f(v.getX()-markerSize,v.getY()+markerSize,v.getZ()+markerSize);
		gl.glVertex3f(v.getX()-markerSize,v.getY()+markerSize,v.getZ()-markerSize);
		
		gl.glVertex3f(v.getX()+markerSize,v.getY()+markerSize,v.getZ()+markerSize);
		gl.glVertex3f(v.getX()+markerSize,v.getY()+markerSize,v.getZ()-markerSize);
		gl.glEnd();
		
		gl.glFlush();
		
		gl.glEnable(GL2.GL_LIGHTING);
	}
	
	private void renderFrustum(GL2 gl)
	{
		float angle=camera.getAngle()/2.0f;
		Vector eye=camera.getEye();
		Vector at=camera.getAt();
		Vector look=new Vector( eye, at);
		//Vector up=camera.getUp();
		Vector up= new Vector(0,1,0);
		Vector hor=Vector.cross(look, up);
		float len=(float)Math.tan(angle*Math.PI/180f)*look.getLen();
		hor.norm();
		hor.mul(len);
		
		Vector ver=Vector.cross(hor, look);
		ver.norm();
		ver.mul(1/(camera.getAspectRatio())*look.getLen());
		
		Vector ur=new Vector(0,0,0).add(hor).add(ver).add(at);
		Vector ul=new Vector(0, 0,0).add(hor).mul(-1).add(ver).add(at);
		Vector dr=new Vector(0,0,0).add(ver).mul(-1).add(hor).add(at);
		Vector dl=new Vector(0,0,0).add(ur).mul(-1).add(at);
		float[] no_mat={0,0,0,0};
		
		gl.glDisable(GL.GL_DEPTH_TEST);
		gl.glDisable(GL2.GL_LIGHTING);
		gl.glBegin(GL.GL_LINES);
		
		gl.glColor3f(1, 1, 1);
		
		gl.glColor3f(1,1,1);
		gl.glVertex3f(eye.getX(), eye.getY(), eye.getZ());
		gl.glVertex3f(ur.getX(), ur.getY(), ur.getZ());
		gl.glVertex3f(eye.getX(), eye.getY(), eye.getZ());
		gl.glVertex3f(ul.getX(), ul.getY(), ul.getZ());
		gl.glVertex3f(eye.getX(), eye.getY(), eye.getZ());
		gl.glVertex3f(dr.getX(), dr.getY(), dr.getZ());
		gl.glVertex3f(eye.getX(), eye.getY(), eye.getZ());
		gl.glVertex3f(dl.getX(), dl.getY(), dl.getZ());
		
		gl.glVertex3f(dl.getX(), dl.getY(), dl.getZ());
		gl.glVertex3f(dr.getX(), dr.getY(), dr.getZ());
		gl.glVertex3f(dr.getX(), dr.getY(), dr.getZ());
		gl.glVertex3f(ur.getX(), ur.getY(), ur.getZ());
		gl.glVertex3f(ur.getX(), ur.getY(), ur.getZ());
		gl.glVertex3f(ul.getX(), ul.getY(), ul.getZ());
		gl.glVertex3f(ul.getX(), ul.getY(), ul.getZ());
		gl.glVertex3f(dl.getX(), dl.getY(), dl.getZ());
		
		gl.glEnd();
		gl.glFlush();
		
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glEnable(GL2.GL_LIGHTING);
		
	}
	
	
	private void drawCamera(GL2 gl2) {
        double angle = camera.getAngle() / 2.0;
        float[] lookVertex = {(float) camera.getAt().getX(), (float) camera.getAt().getY(), (float) camera.getAt().getZ()};
        float[] cameraVertex = {(float) camera.getEye().getX(), (float) camera.getEye().getY(), (float) camera.getEye().getZ()};
        float[] cameraVec = {lookVertex[0] - cameraVertex[0], lookVertex[1] - cameraVertex[1], lookVertex[2] - cameraVertex[2]};
        float[] OYVec = {0f, 1f, 0f};
        float[] horVec = new float[3];
        VectorUtil.crossVec3(horVec, cameraVec, OYVec);
        float vecLength = ((float) Math.tan(angle * Math.PI / 180.0)) * VectorUtil.normVec3(cameraVec);
        float horVecLength = VectorUtil.normVec3(horVec);
        horVec[0] = vecLength * horVec[0] / horVecLength;
        horVec[1] = vecLength * horVec[1] / horVecLength;
        horVec[2] = vecLength * horVec[2] / horVecLength;
        float[] verVec = new float[3];
        VectorUtil.crossVec3(verVec, horVec, cameraVec);
        float verVecLength = VectorUtil.normVec3(verVec);
        verVec[0] = 1f/camera.getAspectRatio() * vecLength * verVec[0] / verVecLength;
        verVec[1] = 1f/camera.getAspectRatio() * vecLength * verVec[1] / verVecLength;
        verVec[2] = 1f/camera.getAspectRatio() * vecLength * verVec[2] / verVecLength;
        float[][] coneVert = new float[4][3];
       
        coneVert[0][0] = lookVertex[0] + horVec[0] + verVec[0];
        coneVert[0][1] = lookVertex[1] + horVec[1] + verVec[1];
        coneVert[0][2] = lookVertex[2] + horVec[2] + verVec[2];
       
        coneVert[1][0] = lookVertex[0] + horVec[0] - verVec[0];
        coneVert[1][1] = lookVertex[1] + horVec[1] - verVec[1];
        coneVert[1][2] = lookVertex[2] + horVec[2] - verVec[2];
       
        coneVert[2][0] = lookVertex[0] - horVec[0] + verVec[0];
        coneVert[2][1] = lookVertex[1] - horVec[1] + verVec[1];
        coneVert[2][2] = lookVertex[2] - horVec[2] + verVec[2];
       
        coneVert[3][0] = lookVertex[0] - horVec[0] - verVec[0];
        coneVert[3][1] = lookVertex[1] - horVec[1] - verVec[1];
        coneVert[3][2] = lookVertex[2] - horVec[2] - verVec[2];
       
        for(int i = 0; i < coneVert.length; i++) {
            gl2.glBegin(GL2.GL_LINES);
            gl2.glColor3f(0.0f, 0.0f, 0.0f);
            gl2.glVertex3f(cameraVertex[0], cameraVertex[1], cameraVertex[2]);
            gl2.glVertex3f(coneVert[i][0], coneVert[i][1], coneVert[i][2]);
            gl2.glEnd();
        }
       
        gl2.glDisable(GL2.GL_LIGHTING);
        gl2.glBegin(GL2.GL_LINES);
        gl2.glColor3f(0.0f, 0.0f, 0.0f);
        gl2.glVertex3f(coneVert[0][0], coneVert[0][1], coneVert[0][2]);
        gl2.glVertex3f(coneVert[1][0], coneVert[1][1], coneVert[1][2]);
        gl2.glEnd();
       
        gl2.glBegin(GL2.GL_LINES);
        gl2.glColor3f(0.0f, 0.0f, 0.0f);
        gl2.glVertex3f(coneVert[2][0], coneVert[2][1], coneVert[2][2]);
        gl2.glVertex3f(coneVert[3][0], coneVert[3][1], coneVert[3][2]);
        gl2.glEnd();
       
        gl2.glBegin(GL2.GL_LINES);
        gl2.glColor3f(0.0f, 0.0f, 0.0f);
        gl2.glVertex3f(coneVert[0][0], coneVert[0][1], coneVert[0][2]);
        gl2.glVertex3f(coneVert[2][0], coneVert[2][1], coneVert[2][2]);
        gl2.glEnd();
       
        gl2.glBegin(GL2.GL_LINES);
        gl2.glColor3f(0.0f, 0.0f, 0.0f);
        gl2.glVertex3f(coneVert[3][0], coneVert[3][1], coneVert[3][2]);
        gl2.glVertex3f(coneVert[1][0], coneVert[1][1], coneVert[1][2]);
        gl2.glEnd();
        gl2.glFlush();
        gl2.glEnable(GL2.GL_LIGHTING);
 }
	
	
	
	public float getMarkerSize() {
		return markerSize;
	}

	public void setMarkerSize(float markerSize) {
		this.markerSize = markerSize;
	}
	
	public int getViewType()
	{
		return viewType;
	}


	
}
