package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import events.CameraAngleEvent;
import events.CameraPositionEvent;
import events.SaveLoadEvent;
import events.ToolbarEventListener;
import logic.Vector;

public class Toolbar extends JPanel implements ChangeListener, ActionListener{
	public static final int MAX_ANGLE=90;
	public static final int MIN_ANGLE=0;
	public static final int INIT_ANGLE=70;
	
	private ToolbarEventListener toolbarEventListener;
	
	private JTextField eyeX=new JTextField();
	private JTextField eyeY=new JTextField();
	private JTextField eyeZ=new JTextField();
	
	private JTextField atX=new JTextField();
	private JTextField atY=new JTextField();
	private JTextField atZ=new JTextField();
	
	private JSlider angle=new JSlider(JSlider.HORIZONTAL,MIN_ANGLE,MAX_ANGLE,INIT_ANGLE);
	
	private JButton apply=new JButton("Apply");
	private JButton save=new JButton("Save");
	private JButton load=new JButton("Load");
	
	public Toolbar()
	{
		buildGUI();
	}
	
	private void buildGUI()
	{		
		setLayout(new GridBagLayout());
		GridBagConstraints gbc=new GridBagConstraints();
		
		angle.setMinorTickSpacing(5);
		angle.setMajorTickSpacing(10);
		angle.setPaintTicks(true);
		angle.setPaintLabels(true);
		angle.addChangeListener(this);
		
		apply.addActionListener(this);
		save.addActionListener(this);
		load.addActionListener(this);
		
		gbc.insets=new Insets(1, 1, 1, 1);
		gbc.weightx=0.5;
		gbc.weighty=0.5;
		gbc.fill=GridBagConstraints.BOTH;
		gbc.gridheight=1;
		gbc.gridwidth=1;
		
		gbc.gridx=0;
		gbc.gridy=0;
		add(new JLabel("Camera position"), gbc);
		
		gbc.gridx=0;
		gbc.gridy=1;
		add(new JLabel("Look at position"), gbc);
		
		gbc.gridx=0;
		gbc.gridy=2;
		add(new JLabel("Field of view angle"), gbc);
		
		gbc.gridx=1;
		gbc.gridy=0;
		add(eyeX, gbc);
		gbc.gridx=2;
		gbc.gridy=0;
		add(eyeY, gbc);
		gbc.gridx=3;
		gbc.gridy=0;
		add(eyeZ, gbc);
		
		gbc.gridx=1;
		gbc.gridy=1;
		add(atX, gbc);
		gbc.gridx=2;
		gbc.gridy=1;
		add(atY, gbc);
		gbc.gridx=3;
		gbc.gridy=1;
		add(atZ, gbc);
		
		gbc.gridx=1;
		gbc.gridy=2;
		gbc.gridwidth=3;
		add(angle, gbc);
		
		gbc.gridx=4;
		gbc.gridy=0;
		gbc.gridheight=1;
		gbc.gridwidth=1;
		add(save,gbc);
		
		gbc.gridx=4;
		gbc.gridy=1;
		gbc.gridheight=1;
		gbc.gridwidth=1;
		add(load,gbc);
		
		gbc.gridx=4;
		gbc.gridy=2;
		gbc.gridheight=1;
		gbc.gridwidth=1;
		add(apply,gbc);
		
		
		
		
		
	}
	
	public void setEye(Vector eye)
	{
		eyeX.setText(eye.getX()+"");
		eyeY.setText(eye.getY()+"");
		eyeZ.setText(eye.getZ()+"");
	}
	
	public void setAt(Vector at)
	{
		atX.setText(at.getX()+"");
		atY.setText(at.getY()+"");
		atZ.setText(at.getZ()+"");
	}

	public ToolbarEventListener getToolbarEventListener() {
		return toolbarEventListener;
	}

	public void setToolbarEventListener(ToolbarEventListener toolbarEventListener) {
		this.toolbarEventListener = toolbarEventListener;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try
		{
			if(((JButton)e.getSource()).getText()=="Apply")
			{
				Vector eye=new Vector(Float.parseFloat(eyeX.getText()),Float.parseFloat(eyeY.getText()),Float.parseFloat(eyeZ.getText()));
				Vector at=new Vector(Float.parseFloat(atX.getText()),Float.parseFloat(atY.getText()),Float.parseFloat(atZ.getText()));
				toolbarEventListener.onCameraPositionEvent(new CameraPositionEvent(this, eye, at));	
			}
			if(((JButton)e.getSource()).getText()=="Save")
			{
				toolbarEventListener.onSaveLoadEvent(new SaveLoadEvent(this, SaveLoadEvent.SAVE));
			}
			if(((JButton)e.getSource()).getText()=="Load")
			{
				toolbarEventListener.onSaveLoadEvent(new SaveLoadEvent(this, SaveLoadEvent.LOAD));
			}
		}
		catch (Exception exc) {
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		toolbarEventListener.onCameraAngleEvent(new CameraAngleEvent(this, angle.getValue()));
	}
	
}
