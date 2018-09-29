package detection.rec;

import java.awt.Color;
import java.awt.Graphics2D;

import image.Image;

public class RecRoad {
	public int m_width;
	public int m_height;
	public int type;

	public int[] m_imageArrayResult;
	public int[] m_imageArrayLeftVerticalLine1;
	public int[] m_imageArrayLeftVerticalLine2;
	public int[] m_imageArrayLeftHorizontalLine1;
	public int[] m_imageArrayLeftHorizontalLine2;
	public int[] m_imageArrayRighttVerticalLine1;
	public int[] m_imageArrayRighttVerticalLine2;
	public int[] m_imageArrayRightHorizontalLine1;
	public int[] m_imageArrayRightHorizontalLine2;

	int[] pointLeft1 = null;
	int[] pointLeft2 = null;
	int[] pointLeft3 = null;
	int[] pointLeft4 = null;

	int[] pointRight1 = null;
	int[] pointRight2 = null;
	int[] pointRight3 = null;
	int[] pointRight4 = null;


	public RecRoad(Image image) {
    	m_width = image.m_width;
    	m_height = image.m_height;
    	
    	
// LEFT PART    	
    	// Hough transform left line
    	HoughRec houghLeftHorizontal = new HoughRec(image.m_imageArraySobelLeftBlue, 15, m_width, m_height, 2, 85, 95, 1, 2);
    	houghLeftHorizontal.processForRec();
    	m_imageArrayLeftHorizontalLine1 = houghLeftHorizontal.output1;
    	m_imageArrayLeftHorizontalLine2 = houghLeftHorizontal.output2;
    	
    	// Hough transform right line
    	HoughRec houghLeftVertical = new HoughRec(image.m_imageArraySobelLeftBlue, 15, m_width, m_height, 2, 0, 10, 2, 1);
    	houghLeftVertical.processForRec();
    	m_imageArrayRighttVerticalLine1 = houghLeftVertical.output1;
    	m_imageArrayRighttVerticalLine2 = houghLeftVertical.output2;
    	    	
// RIGHT PART
    	// Hough transform left line
    	HoughRec houghRightHorizontal = new HoughRec(image.m_imageArraySobelRightBlue, 15, m_width, m_height, 2, 85, 95, 1, 2);
    	houghRightHorizontal.processForRec();
    	m_imageArrayRightHorizontalLine1 = houghRightHorizontal.output1;
    	m_imageArrayRightHorizontalLine2 = houghRightHorizontal.output2;
    	
    	// Hough transform right line
    	HoughRec houghRightVertical = new HoughRec(image.m_imageArraySobelRightBlue, 15, m_width, m_height, 2, 0, 10, 2, 1);
    	houghRightVertical.processForRec();
    	m_imageArrayLeftVerticalLine1 = houghRightVertical.output1;
    	m_imageArrayLeftVerticalLine2 = houghRightVertical.output2;
    	

// Points
    	intersectionPoint();
	    Graphics2D g = image.m_bufferImage.createGraphics();
	    
	    if(pointLeft1 != null && pointLeft2 != null && pointLeft3 != null && pointLeft4 != null) {
	    	double area = trapezoidArea(pointLeft1, pointLeft2, pointLeft3,  pointLeft4);
	    	if(area>400 && area<8500) {
	    		double side1 = Math.sqrt((pointLeft1[0]-pointLeft2[0])*(pointLeft1[0]-pointLeft2[0]) + (pointLeft1[1]-pointLeft2[1])*(pointLeft1[1]-pointLeft2[1]));
	    		double side2 = Math.sqrt((pointLeft1[0]-pointLeft3[0])*(pointLeft1[0]-pointLeft3[0]) + (pointLeft1[1]-pointLeft3[1])*(pointLeft1[1]-pointLeft3[1]));
	    		double side3 = Math.sqrt((pointLeft4[0]-pointLeft2[0])*(pointLeft4[0]-pointLeft2[0]) + (pointLeft4[1]-pointLeft2[1])*(pointLeft4[1]-pointLeft2[1]));
	    		double side4 = Math.sqrt((pointLeft4[0]-pointLeft3[0])*(pointLeft4[0]-pointLeft3[0]) + (pointLeft4[1]-pointLeft3[1])*(pointLeft4[1]-pointLeft3[1]));
	    		
	    		if(side1*0.5<side2 && side1*0.5<side3 && side1*0.5<side4)
	    			if(side2*0.5<side1 && side2*0.5<side3 && side2*0.5<side4)
	    				if(side3*0.5<side1 && side3*0.5<side2 && side3*0.5<side4)
	    					if(side4*0.5<side1 && side4*0.5<side2 && side4*0.5<side3) {
			    			    g.setColor(Color.green);
			    			    g.drawLine(pointLeft1[0], pointLeft1[1], pointLeft2[0], pointLeft2[1]);
			    			    g.drawLine(pointLeft1[0], pointLeft1[1], pointLeft3[0], pointLeft3[1]);
			    			    g.drawLine(pointLeft4[0], pointLeft4[1], pointLeft2[0], pointLeft2[1]);
			    			    g.drawLine(pointLeft3[0], pointLeft3[1], pointLeft4[0], pointLeft4[1]);	
		    			}	    			
	    	}
	    }
	    
	    if(pointRight1 != null && pointRight2 != null && pointRight3 != null && pointRight4 != null) {
	    	double area = trapezoidArea(pointRight1, pointRight2, pointRight3,  pointRight4);
	    	if(area>300 && area<8500) {
	    		double side1 = Math.sqrt((pointRight1[0]-pointRight2[0])*(pointRight1[0]-pointRight2[0]) + (pointRight1[1]-pointRight2[1])*(pointRight1[1]-pointRight2[1]));
	    		double side2 = Math.sqrt((pointRight1[0]-pointRight3[0])*(pointRight1[0]-pointRight3[0]) + (pointRight1[1]-pointRight3[1])*(pointRight1[1]-pointRight3[1]));
	    		double side3 = Math.sqrt((pointRight4[0]-pointRight2[0])*(pointRight4[0]-pointRight2[0]) + (pointRight4[1]-pointRight2[1])*(pointRight4[1]-pointRight2[1]));
	    		double side4 = Math.sqrt((pointRight4[0]-pointRight3[0])*(pointRight4[0]-pointRight3[0]) + (pointRight4[1]-pointRight3[1])*(pointRight4[1]-pointRight3[1]));
	    		
	    		if(side1*0.5<side2 && side1*0.5<side3 && side1*0.5<side4)
	    			if(side2*0.5<side1 && side2*0.5<side3 && side2*0.5<side4)
	    				if(side3*0.5<side1 && side3*0.5<side2 && side3*0.5<side4)
	    					if(side4*0.5<side1 && side4*0.5<side2 && side4*0.5<side3) {
							    g.setColor(Color.green);
							    g.drawLine(m_width-pointRight1[0]-1, pointRight1[1], m_width-pointRight2[0]-1, pointRight2[1]);
							    g.drawLine(m_width-pointRight1[0]-1, pointRight1[1], m_width-pointRight3[0]-1, pointRight3[1]);
							    g.drawLine(m_width-pointRight4[0]-1, pointRight4[1], m_width-pointRight2[0]-1, pointRight2[1]);
							    g.drawLine(m_width-pointRight3[0]-1, pointRight3[1], m_width-pointRight4[0]-1, pointRight4[1]);
    			}
	    	}
	    }
	}
	
	
	//-------------------------------------------------------------
	public void intersectionPoint() {
	    for(int row=0; row<m_height; row++){
	    	for(int col=0; col<m_width/2; col++) {
	    		if(m_imageArrayLeftHorizontalLine1[row*m_width+col] + m_imageArrayRighttVerticalLine1[row*m_width+col] >= 2)
	    			pointLeft1 = new int[]{col, row};
	    		if(m_imageArrayLeftHorizontalLine1[row*m_width+col] + m_imageArrayRighttVerticalLine2[row*m_width+col] >= 2)
	    			pointLeft2 = new int[]{col, row};
	    		if(m_imageArrayLeftHorizontalLine2[row*m_width+col] + m_imageArrayRighttVerticalLine1[row*m_width+col] >= 2)
	    			pointLeft3 = new int[]{col, row};
	    		if(m_imageArrayLeftHorizontalLine2[row*m_width+col] + m_imageArrayRighttVerticalLine2[row*m_width+col] >= 2)
	    			pointLeft4 = new int[]{col, row};

	    		if(m_imageArrayRightHorizontalLine1[row*m_width+col] + m_imageArrayLeftVerticalLine1[row*m_width+col] >= 2)
	    			pointRight1 = new int[]{col, row};
	    		if(m_imageArrayRightHorizontalLine1[row*m_width+col] + m_imageArrayLeftVerticalLine2[row*m_width+col] >= 2)
	    			pointRight2 = new int[]{col, row};
	    		if(m_imageArrayRightHorizontalLine2[row*m_width+col] + m_imageArrayLeftVerticalLine1[row*m_width+col] >= 2)
	    			pointRight3 = new int[]{col, row};
	    		if(m_imageArrayRightHorizontalLine2[row*m_width+col] + m_imageArrayLeftVerticalLine2[row*m_width+col] >= 2)
	    			pointRight4 = new int[]{col, row};
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
	
	
	//-------------------------------------------------------------
	public double trapezoidArea(int[] point1, int[] point2, int[] point3, int[] point4) {
		double area = triangleArea(point1, point2, point3);
		area = area + triangleArea(point4, point1, point3);
		return area;
	}
	
}



