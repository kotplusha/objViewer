package tries;

import java.nio.FloatBuffer;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.fixedfunc.GLPointerFunc;
import com.sun.prism.impl.BufferUtil;

public class MyGLEventListener implements GLEventListener {
	
	FloatBuffer  vertexData=BufferUtil.newFloatBuffer(18);
	FloatBuffer colorData=BufferUtil.newFloatBuffer(18);

	@Override
	public void display(GLAutoDrawable autoDrawable) {
		GL2 gl2 = autoDrawable.getGL().getGL2();
		gl2.glClear(GL.GL_COLOR_BUFFER_BIT);
		
		gl2.glEnableClientState(GLPointerFunc.GL_VERTEX_ARRAY);
		gl2.glEnableClientState(GLPointerFunc.GL_COLOR_ARRAY);
		gl2.glVertexPointer(3,GL.GL_FLOAT,0,vertexData);
		gl2.glColorPointer(3,GL.GL_FLOAT,0,colorData);
		
		gl2.glDrawArrays(GL.GL_TRIANGLES, 0, 6);
		
		gl2.glDisableClientState(GLPointerFunc.GL_VERTEX_ARRAY);
		gl2.glDisableClientState(GLPointerFunc.GL_COLOR_ARRAY);
		
	}

	@Override
	public void dispose(GLAutoDrawable autoDrawable) {

	}

	@Override
	public void init(GLAutoDrawable autoDrawable) {
		GL2 gl2 = autoDrawable.getGL().getGL2();
		gl2.glClearColor(1f, 1f, 1f, 1f);
		
		//FloatBuffer  vertexData=BufferUtil.newFloatBuffer(9);
		vertexData.put(new float[]{0,0,0});
		vertexData.put(new float[]{1,0,0});
		vertexData.put(new float[]{0,1,0});
		
		vertexData.put(new float[]{0,0,0});
		vertexData.put(new float[]{1,0,0});
		vertexData.put(new float[]{0,-1,0});
		vertexData.flip();
		
		colorData.put(new float[]{1,0,0});
		colorData.put(new float[]{0,1,0});
		colorData.put(new float[]{0,0,1});
		
		colorData.put(new float[]{0,0,1});
		colorData.put(new float[]{1,0,0});
		colorData.put(new float[]{0,1,0});
		colorData.flip();
	}

	@Override
	public void reshape(GLAutoDrawable autoDrawable, int arg1, int arg2, int arg3, int arg4) {
		GL2 gl2 = autoDrawable.getGL().getGL2();

	}

}
