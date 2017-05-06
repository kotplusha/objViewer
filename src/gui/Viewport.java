package gui;

import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JLabel;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;

import logic.Main;
import logic.Vector;
import renderer.OrthoRenderer;
import renderer.Renderer;


public class Viewport extends GLJPanel {
	public static  String PROFILE=GLProfile.GL2;

	private static final long serialVersionUID = 1L;
	
	private Renderer renderer;
	private Point startLMB;
	private Point startRMB;
	public Viewport(Renderer renderer,String name)
	{
		//super(new GLCapabilities(GLProfile.get(PROFILE)));
		super(Main.CAPS);
		setLayout(new FlowLayout(FlowLayout.LEADING));
		add(new JLabel(name));
		this.setRenderer(renderer);
		this.addGLEventListener(renderer);
		this.renderer=renderer;
	}

	public Renderer getRenderer() {
		return renderer;
	}

	public void setRenderer(Renderer renderer) {
		this.renderer = renderer;
	}

	
}
