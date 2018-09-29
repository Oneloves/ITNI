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
	ImagePanel panel1 = new ImagePanel("Original");
	ImagePanel panel3 = new ImagePanel("Left");
	ImagePanel panel4 = new ImagePanel("Mid left");
	ImagePanel panel5 = new ImagePanel("Mid right");
	ImagePanel panel6 = new ImagePanel("Right");
	ImagePanel panel7 = new ImagePanel("Color Left");
	ImagePanel panel8 = new ImagePanel("Color Right");
	ImagePanel panel9 = new ImagePanel("Road Line");
	ImagePanel panel10 = new ImagePanel("Left");
	ImagePanel panel11 = new ImagePanel("Right");
	ImagePanel panel12 = new ImagePanel("Rec");
	ImagePanel panel13 = new ImagePanel("Left");
	ImagePanel panel14 = new ImagePanel("Right");
	ImagePanel panel15 = new ImagePanel("Tri");
	ImagePanel panel16 = new ImagePanel("Left");
	ImagePanel panel17 = new ImagePanel("Right");
	ImagePanel panel18 = new ImagePanel("Cir");
	
	
	public FramePicture() {
		this.setTitle("Mojito");
		this.setSize(400, 500);
		this.setLocation(0, 0);
		
	    panel = new JPanel(new GridLayout(4,3));
	    panel.add(panel1);
	   // panel.add(panel3);
	   // panel.add(panel4);
	   // panel.add(panel5);
	   // panel.add(panel6);
	    panel.add(panel7);
	    panel.add(panel8);
	    panel.add(panel9);
	    panel.add(panel10);
	    panel.add(panel11);
	    panel.add(panel12);
	    panel.add(panel13);
	    panel.add(panel14);
	    panel.add(panel15);
	    panel.add(panel16);
	    panel.add(panel17);
	    panel.add(panel18);
	   
	    this.setContentPane(panel); 
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);             
		this.setVisible(true);
	}
	

	public void setPanels(File[] files, int i) {
		new Thread(){
			public void run(){
				/*
				for(int i=0; i<files.length; i++) {
					Image image = new Image(files[i].getPath());
					RoadLine roadLine = new RoadLine(image);
					panel1.setImage(image.m_bufferImage);
					panel2.setImage(image.m_bufferImageSobel);
					panel3.setImage(image.m_bufferImageLeft);
					panel4.setImage(image.m_bufferImageMiddleLeft);
					panel5.setImage(image.m_bufferImageMiddleRight);
					panel6.setImage(image.m_bufferImageRight);
					panel7.setImage(roadLine.m_bufferImageColorSelectionLeft);
					panel8.setImage(roadLine.m_bufferImageColorSelectionRight);
					panel9.setImage(roadLine.m_bufferImageLineDetected);
				}
				*/
				Image image = new Image(files[i].getPath());
				RoadLine roadLine = new RoadLine(image);
				RecRoad recRoad = new RecRoad(image);
				TriangleRoad triangleRoad = new TriangleRoad(image);
				CircleRoad circleRoad = new CircleRoad(image);
				
				panel1.setImage(image.m_bufferImage);
				//panel3.setImage(image.m_bufferImageLeft);
				//panel4.setImage(image.m_bufferImageMiddleLeft);
				//panel5.setImage(image.m_bufferImageMiddleRight);
				//panel6.setImage(image.m_bufferImageRight);
				panel7.setImage(roadLine.m_bufferImageColorSelectionLeft);
				panel8.setImage(roadLine.m_bufferImageColorSelectionRight);
				panel9.setImage(roadLine.m_bufferImageLineDetected);
				panel10.setImage(recRoad.m_bufferImageColorSelectionLef);
				panel11.setImage(recRoad.m_bufferImageColorSelectionRight);
				panel12.setImage(recRoad.m_bufferImageLineDetected);
				panel13.setImage(triangleRoad.m_bufferImageColorSelectionLef);
				panel14.setImage(triangleRoad.m_bufferImageColorSelectionRight);
				panel15.setImage(triangleRoad.m_bufferImageLineDetected);
				panel16.setImage(circleRoad.m_bufferImageColorSelectionLef);
				panel17.setImage(circleRoad.m_bufferImageColorSelectionRight);
				panel18.setImage(circleRoad.m_bufferImageLineDetected);
			}
		}.start();
	}
}
