package logic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;

import gui.Toolbar;
import gui.Viewport;
import renderer.Camera;
import renderer.OrthoRenderer;
import renderer.PerspectiveRenderer;
import tries.MyGLEventListener;

public class Main {
	public static GLCapabilities CAPS=new GLCapabilities(GLProfile.get(GLProfile.GL2));
	public void GUI(){
		CAPS.setDepthBits(64);
		JFrame frame=new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("OpenGL");
		frame.setLayout(new GridBagLayout());
		GridBagConstraints gbc=new GridBagConstraints();

		Controller controller=new Controller(frame);
		
		Camera camera=new Camera(100, 0.01f, 1000, new Vector(10f,10f,10f), new Vector(0,0,0));
		controller.setCamera(camera);
		
		OrthoRenderer topRenderer=new OrthoRenderer(OrthoRenderer.VIEW_TOP);
		OrthoRenderer frontRenderer=new OrthoRenderer(OrthoRenderer.VIEW_FRONT);
		OrthoRenderer leftRenderer=new OrthoRenderer(OrthoRenderer.VIEW_LEFT);
		PerspectiveRenderer perspectiveRenderer=new PerspectiveRenderer();
		
		Viewport top =new Viewport(topRenderer,"Top");
		controller.addViewport(top);
	
		Viewport front=new Viewport(frontRenderer,"Front");
		controller.addViewport(front);
		
		Viewport left=new Viewport(leftRenderer,"Left");
		controller.addViewport(left);
		
		Viewport perspective=new Viewport(perspectiveRenderer,"Perspective");
		controller.addViewport(perspective);
		
		//controller.addMesh(Mesh.cubeMesh(5));
		
		Toolbar toolbar=new Toolbar();
		toolbar.setPreferredSize(new Dimension(100, 100));
		toolbar.setMinimumSize(toolbar.getPreferredSize());
		controller.setToolbar(toolbar);
		
		
		
		
		gbc.insets=new Insets(5, 5, 5, 5);
		gbc.gridheight=1;
		gbc.gridwidth=1;
		gbc.weightx=0.5;
		gbc.weighty=0.5;
		gbc.fill=GridBagConstraints.BOTH;
		
		gbc.gridx=0;
		gbc.gridy=0;
		frame.getContentPane().add(perspective, gbc);
		
		gbc.gridx=1;
		gbc.gridy=0;
		frame.getContentPane().add(top, gbc);
		
		gbc.gridx=1;
		gbc.gridy=1;
		frame.getContentPane().add(front, gbc);
		
		gbc.gridx=0;
		gbc.gridy=1;
		frame.getContentPane().add(left, gbc);
		
		
		
		gbc.gridx=0;
		gbc.gridy=2;
		gbc.gridwidth=2;
		gbc.weighty=0;
		gbc.fill=GridBagConstraints.HORIZONTAL;
		frame.getContentPane().add(toolbar, gbc);
		
		
		
		frame.setSize(1000, 1000);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		Main main=new Main();
		main.GUI();
	}
}
