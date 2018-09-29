package detection.triangle;

import java.awt.Color;
import java.awt.Graphics2D;

import image.Image;

public class TriangleRoad {
	public int m_width;
	public int m_height;

	public int[] m_imageArrayResult;
	public int[] m_imageArrayImageLeftLeftLine;
	public int[] m_imageArrayImageLeftDownLine;
	public int[] m_imageArrayImageLeftRightLine;
	public int[] m_imageArrayImageRightLeftLine;
	public int[] m_imageArrayImageRightDownLine;
	public int[] m_imageArrayImageRightRightLine;

	int[] pointL1;
	int[] pointL2;
	int[] pointL3;
	
	int[] pointR1;
	int[] pointR2;
	int[] pointR3;

	
	public TriangleRoad(Image image) {
		m_width = image.m_width;
		m_height = image.m_height;

	//LEFT PART    	
		// Hough transform down line
		Line houghLeftHorizontal = new Line(image.m_imageArraySobelLeftRed, 15, m_width, m_height, 1, 85, 95);
		m_imageArrayImageLeftDownLine = houghLeftHorizontal.process();

		// Hough transform left line
		Line houghLeftVertical = new Line(image.m_imageArraySobelLeftRed, 15, m_width, m_height, 1, 15, 30);
		m_imageArrayImageLeftLeftLine = houghLeftVertical.process();

		// Hough transform right line	
		m_imageArrayImageLeftRightLine = flip(image.m_imageArraySobelLeftRed);
		Line houghLeftHorizontal2 = new Line(m_imageArrayImageLeftRightLine, 15, m_width, m_height, 1, 15, 30);
		m_imageArrayImageLeftRightLine = houghLeftHorizontal2.process();
		m_imageArrayImageLeftRightLine = flip(m_imageArrayImageLeftRightLine);
				
	//RIGHT PART
		// Hough transform down line
		Line houghRightHorizontal = new Line(image.m_imageArraySobelRightRed, 15, m_width, m_height, 1, 85, 95);
		m_imageArrayImageRightDownLine = houghRightHorizontal.process();

		// Hough transform left line
		Line houghRightVertical = new Line(image.m_imageArraySobelRightRed, 15, m_width, m_height, 1, 15, 30);
		m_imageArrayImageRightLeftLine = houghRightVertical.process();

		// Hough transform right line
		m_imageArrayImageRightRightLine = flip(image.m_imageArraySobelRightRed);
		Line houghRightHorizontal2 = new Line(m_imageArrayImageRightRightLine, 15, m_width, m_height, 1, 15, 30);
		m_imageArrayImageRightRightLine = houghRightHorizontal2.process();
		m_imageArrayImageRightRightLine = flip(m_imageArrayImageRightRightLine);
		
	//RESULT
		intersectionPoint();
	    Graphics2D g = image.m_bufferImage.createGraphics();
	    
	    if(pointL1!=null && pointL2!=null && pointL3!=null) {
	    	double area = triangleArea(pointL1, pointL2, pointL3);
	    	if(area>250 && area<3500) {	    	
	    		double side1 = Math.sqrt((pointL1[0]-pointL2[0])*(pointL1[0]-pointL2[0]) + (pointL1[1]-pointL2[1])*(pointL1[1]-pointL2[1]));  //base
	    		double side2 = Math.sqrt((pointL1[0]-pointL3[0])*(pointL1[0]-pointL3[0]) + (pointL1[1]-pointL3[1])*(pointL1[1]-pointL3[1]));
	    		double side3 = Math.sqrt((pointL2[0]-pointL3[0])*(pointL2[0]-pointL3[0]) + (pointL2[1]-pointL3[1])*(pointL2[1]-pointL3[1]));
	    		
	    		if(side2<(side1+side1*0.5) && side3<(side1+side1*0.5)) {
				    g.setColor(Color.GREEN);
				    g.drawLine(pointL1[0], pointL1[1], pointL2[0], pointL2[1]);
				    g.drawLine(pointL1[0], pointL1[1], pointL3[0], pointL3[1]);
				    g.drawLine(pointL2[0], pointL2[1], pointL3[0], pointL3[1]);
	    		}
	    	}
	    }
	    
	    if(pointR1!=null && pointR2!=null && pointR3!=null) {	
	    	double area = triangleArea(pointR1, pointR2, pointR3);    	
	    	if(area>250 && area<3500) {
	    		
	    		double side1 = Math.sqrt((pointR1[0]-pointR2[0])*(pointR1[0]-pointR2[0]) + (pointR1[1]-pointR2[1])*(pointR1[1]-pointR2[1]));  //base
	    		double side2 = Math.sqrt((pointR1[0]-pointR3[0])*(pointR1[0]-pointR3[0]) + (pointR1[1]-pointR3[1])*(pointR1[1]-pointR3[1]));
	    		double side3 = Math.sqrt((pointR2[0]-pointR3[0])*(pointR2[0]-pointR3[0]) + (pointR2[1]-pointR3[1])*(pointR2[1]-pointR3[1]));
	    		
	    		if(side2<(side1+side1*0.5) && side3<(side1+side1*0.5)) {
				    g.setColor(Color.GREEN);
				    g.drawLine(m_width-pointR1[0]-1, pointR1[1], m_width-pointR2[0]-1, pointR2[1]);
				    g.drawLine(m_width-pointR1[0]-1, pointR1[1], m_width-pointR3[0]-1, pointR3[1]);
				    g.drawLine(m_width-pointR2[0]-1, pointR2[1], m_width-pointR3[0]-1, pointR3[1]);
	    		}
	    	}
	    }
		
	}
	
	
	//-------------------------------------------------------------
	public int[] flip(int[] array) {
		int[] imageArray = new int[m_width * m_height];
	    for(int row=0; row<m_height; row++){
	    	for(int col=0; col<m_width/2; col++) {
		         imageArray[row*m_width+(m_width-col-1)] = array[row*m_width+col];
		         imageArray[row*m_width+col] = array[row*m_width+(m_width-col-1)];
	    	}
	    }
	    return imageArray;
	}
	
	
	//-------------------------------------------------------------
	public void intersectionPoint() {
	    for(int row=0; row<m_height; row++){
	    	for(int col=0; col<m_width/2; col++) {
	    		if(m_imageArrayImageLeftDownLine[row*m_width+col] + m_imageArrayImageLeftLeftLine[row*m_width+col] >= 2)
	    			pointL1 = new int[]{col, row};
	    		if(m_imageArrayImageLeftDownLine[row*m_width+col] + m_imageArrayImageLeftRightLine[row*m_width+col] >= 2)
	    			pointL2 = new int[]{col, row};
	    		if(m_imageArrayImageLeftRightLine[row*m_width+col] + m_imageArrayImageLeftLeftLine[row*m_width+col] >= 2)
	    			pointL3 = new int[]{col, row};
	    		
	    		if(m_imageArrayImageRightDownLine[row*m_width+col] + m_imageArrayImageRightLeftLine[row*m_width+col] >= 2)
	    			pointR1 = new int[]{col, row};
	    		if(m_imageArrayImageRightDownLine[row*m_width+col] + m_imageArrayImageRightRightLine[row*m_width+col] >= 2)
	    			pointR2 = new int[]{col, row};
	    		if(m_imageArrayImageRightRightLine[row*m_width+col] + m_imageArrayImageRightLeftLine[row*m_width+col] >= 2)
	    			pointR3 = new int[]{col, row};
	    	}
	    }
	}

	
	//-------------------------------------------------------------
	public double triangleArea(int[] pts1, int[] pts2, int[] pts3) {
		double side1 = Math.sqrt((pts1[0]-pts2[0])*(pts1[0]-pts2[0]) + (pts1[1]-pts2[1])*(pts1[1]-pts2[1]));
		double side2 = Math.sqrt((pts1[0]-pts3[0])*(pts1[0]-pts3[0]) + (pts1[1]-pts3[1])*(pts1[1]-pts3[1]));
		double side3 = Math.sqrt((pts3[0]-pts2[0])*(pts3[0]-pts2[0]) + (pts3[1]-pts2[1])*(pts3[1]-pts2[1]));
        double s = (side1 + side2 + side3)/2;
        return Math.sqrt(s*(s - side1)*(s - side2)*(s - side3));
	}
	
	
}



