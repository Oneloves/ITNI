package ui;

import java.awt.GridLayout;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;

import detection.circle.CircleRoad;
import detection.rec.RecRoad;
import detection.road.RoadLine;
import detection.triangle.TriangleRoad;
import image.Image;

public class FramePicture extends JFrame {

	private static final long serialVersionUID = 1L;
	
	
	JPanel panel;
	ImagePanel panel1 = new ImagePanel("");
	ImagePanel panel2 = new ImagePanel("Test");
	ImagePanel panel3 = new ImagePanel("Test 2");

	
	public FramePicture() {
		this.setTitle("Mojito");
		this.setSize(400, 500);
		this.setLocation(0, 0);
		
	    panel = new JPanel(new GridLayout(1,3));
	    panel.add(panel1);
	    panel.add(panel2);
	    panel.add(panel3);
	   
	    this.setContentPane(panel); 
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);             
		this.setVisible(true);
	}
	

	public void setPanels(File[] files, int i) {
		new Thread(){
			public void run(){
				Image image = new Image(files[i].getPath());
				new RoadLine(image);
				new RecRoad(image);
				new TriangleRoad(image);
				new CircleRoad(image);

				panel1.setImage(image.m_bufferImage);
				panel2.setImage(image.test);
				panel3.setImage(image.test2);
			}
		}.start();
	}
}
