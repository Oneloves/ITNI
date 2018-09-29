package detection.triangle;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import detection.hough.Line;
import image.Image;
import tool.ColorSelection;

public class TriangleRoad {
	public int m_width;
	public int m_height;
	public int type;

	public int[] m_imageArrayResult;
	public int[] m_imageArrayImageLeftLeftLine;
	public int[] m_imageArrayImageLeftDownLine;
	public int[] m_imageArrayImageLeftRightLine;
	public int[] m_imageArrayImageRightLeftLine;
	public int[] m_imageArrayImageRightDownLine;
	public int[] m_imageArrayColorSelectionLeft;
	public int[] m_imageArrayImageRightRightLine;
	public int[] m_imageArrayColorSelectionRight;

	public BufferedImage m_bufferImageLineDetected;
	public BufferedImage m_bufferImageColorSelectionLef;
	public BufferedImage m_bufferImageColorSelectionRight;

	int[] pointL1;
	int[] pointL2;
	int[] pointL3;
	
	int[] pointR1;
	int[] pointR2;
	int[] pointR3;

	
	public TriangleRoad(Image image) {    			
		m_width = image.m_width;
		m_height = image.m_height;
		type = image.m_type;

		m_bufferImageLineDetected = new BufferedImage(m_width, m_height, type);
		m_bufferImageColorSelectionLef = new BufferedImage(m_width, m_height, type);
		m_bufferImageColorSelectionRight = new BufferedImage(m_width, m_height, type);


	//COLOR SELECTION
		// COLOR SELECTION LEFT
		ColorSelection colorSelectionLeft = new ColorSelection(image.m_imageArrayLeft, m_width, m_height);
		m_imageArrayColorSelectionLeft = colorSelectionLeft.selectRed();
		m_imageArrayColorSelectionLeft = sobel(m_imageArrayColorSelectionLeft);
		
		// COLOR SELECTION RIGHT
		ColorSelection colorSelectionRight = new ColorSelection(image.m_imageArrayRight, m_width, m_height);
		m_imageArrayColorSelectionRight = colorSelectionRight.selectRed();
		m_imageArrayColorSelectionRight = sobel(m_imageArrayColorSelectionRight);


	//LEFT PART    	
		// Hough transform down line
		Line houghLeftHorizontal = new Line(m_imageArrayColorSelectionLeft, 30, m_width, m_height, 1, 85, 95);
		m_imageArrayImageLeftDownLine = houghLeftHorizontal.process();

		// Hough transform left line
		Line houghLeftVertical = new Line(m_imageArrayColorSelectionLeft, 30, m_width, m_height, 1, 10, 30);
		m_imageArrayImageLeftLeftLine = houghLeftVertical.process();

		// Hough transform right line	
		m_imageArrayImageLeftRightLine = flip(m_imageArrayColorSelectionLeft);
		Line houghLeftHorizontal2 = new Line(m_imageArrayImageLeftRightLine, 30, m_width, m_height, 1, 10, 30);
		m_imageArrayImageLeftRightLine = houghLeftHorizontal2.process();
		m_imageArrayImageLeftRightLine = flip(m_imageArrayImageLeftRightLine);

		if(houghLeftHorizontal.line.size()<1 || houghLeftVertical.line.size()<1 || houghLeftHorizontal2.line.size()<1) {
			m_imageArrayImageLeftLeftLine = new int[m_width*m_height];
			m_imageArrayImageLeftDownLine = new int[m_width*m_height];
			m_imageArrayImageLeftRightLine = new int[m_width*m_height];
		}
		else {
		// POINTS INTERSECT
	    	// LEFT PART
	    	pointL1 = intersectionPoint(m_imageArrayImageLeftDownLine, m_imageArrayImageLeftLeftLine);
	    	pointL2 = intersectionPoint(m_imageArrayImageLeftDownLine, m_imageArrayImageLeftRightLine);
	    	pointL3 = intersectionPoint(m_imageArrayImageLeftRightLine, m_imageArrayImageLeftLeftLine);
	    	
	    	if(pointL1==null || pointL2==null || pointL3==null) {
				m_imageArrayImageLeftLeftLine = new int[m_width*m_height];
				m_imageArrayImageLeftDownLine = new int[m_width*m_height];
				m_imageArrayImageLeftRightLine = new int[m_width*m_height];
				pointL1 = null;
				pointL2 = null;
				pointL3 = null;
	    	}

	    	if(pointL1!=null) {
		    	double area = triangleArea(pointL1, pointL2, pointL3);
		    	if(area<500) {
					m_imageArrayImageLeftLeftLine = new int[m_width*m_height];
					m_imageArrayImageLeftDownLine = new int[m_width*m_height];
					m_imageArrayImageLeftRightLine = new int[m_width*m_height];
					pointL1 = null;
					pointL2 = null;
					pointL3 = null;
		    	}
	    	}
		}
		
		
	//RIGHT PART
		// Hough transform down line
		Line houghRightHorizontal = new Line(m_imageArrayColorSelectionRight, 30, m_width, m_height, 1, 85, 95);
		m_imageArrayImageRightDownLine = houghRightHorizontal.process();

		// Hough transform left line
		Line houghRightVertical = new Line(m_imageArrayColorSelectionRight, 30, m_width, m_height, 1, 10, 30);
		m_imageArrayImageRightLeftLine = houghRightVertical.process();

		// Hough transform right line
		m_imageArrayImageRightRightLine = flip(m_imageArrayColorSelectionRight);
		Line houghRightHorizontal2 = new Line(m_imageArrayImageRightRightLine, 30, m_width, m_height, 1, 10, 30);
		m_imageArrayImageRightRightLine = houghRightHorizontal2.process();
		m_imageArrayImageRightRightLine = flip(m_imageArrayImageRightRightLine);

		if(houghRightHorizontal.line.size()<1 || houghRightVertical.line.size()<1 || houghRightHorizontal2.line.size()<1) {
			m_imageArrayImageRightDownLine = new int[m_width*m_height];
			m_imageArrayImageRightLeftLine = new int[m_width*m_height];
			m_imageArrayImageRightRightLine = new int[m_width*m_height];
		}	
		else {
		// POINTS INTERSECT
	    	// RIGHT PART
	    	pointR1 = intersectionPoint(m_imageArrayImageRightDownLine, m_imageArrayImageRightLeftLine);
	    	pointR2 = intersectionPoint(m_imageArrayImageRightDownLine, m_imageArrayImageRightRightLine);
	    	pointR3 = intersectionPoint(m_imageArrayImageRightRightLine, m_imageArrayImageRightLeftLine);
			m_imageArrayImageRightRightLine = flip(m_imageArrayImageRightRightLine);
	    	
	    	if(pointR1==null || pointR2==null || pointR3==null) {
				m_imageArrayImageRightLeftLine = new int[m_width*m_height];
				m_imageArrayImageRightDownLine = new int[m_width*m_height];
				m_imageArrayImageRightRightLine = new int[m_width*m_height];
				pointR1 = null;
				pointR2 = null;
				pointR3 = null;
	    	}
	    	
	    	if(pointR1!=null) {
		    	double area = triangleArea(pointR1, pointR2, pointR3);
		    	if(area<500) {
					m_imageArrayImageRightLeftLine = new int[m_width*m_height];
					m_imageArrayImageRightDownLine = new int[m_width*m_height];
					m_imageArrayImageRightRightLine = new int[m_width*m_height];
					pointR1 = null;
					pointR2 = null;
					pointR3 = null;
		    	}
	    	}
		}
		
		
	//RESULT
		arrayToImage(m_imageArrayColorSelectionLeft, m_bufferImageColorSelectionLef);
		m_imageArrayColorSelectionRight = flip(m_imageArrayColorSelectionRight);
		arrayToImage(m_imageArrayColorSelectionRight, m_bufferImageColorSelectionRight);
	
		m_imageArrayImageRightDownLine = flip(m_imageArrayImageRightDownLine);
		m_imageArrayImageRightLeftLine = flip(m_imageArrayImageRightLeftLine);
		
		m_imageArrayResult = lineToArrayImage(m_imageArrayImageLeftDownLine, m_imageArrayImageLeftLeftLine, m_imageArrayImageLeftRightLine,
											  m_imageArrayImageRightDownLine, m_imageArrayImageRightLeftLine, m_imageArrayImageRightRightLine);
		
		result(m_imageArrayResult, image.m_bufferImage, m_bufferImageLineDetected);
	    Graphics2D g = m_bufferImageLineDetected.createGraphics();
	    
	    if(pointL1 != null) {
		    g.setColor(Color.GREEN);
		    g.fillOval(pointL1[0], pointL1[1], 10, 10);
		    g.fillOval(pointL2[0], pointL2[1], 10, 10);
		    g.fillOval(pointL3[0], pointL3[1], 10, 10);
	    }
	    
	    if(pointR1 != null) {
		    g.setColor(Color.GREEN);
		    g.fillOval(m_width-pointR1[0]-1, pointR1[1], 10, 10);
		    g.fillOval(m_width-pointR2[0]-1, pointR2[1], 10, 10);
		    g.fillOval(m_width-pointR3[0]-1, pointR3[1], 10, 10);
	    }
		
	}
	
	
	//-------------------------------------------------------------
	public int[] sobel(int[] array) {
		int[] imageArray = new int[m_width * m_height];
		for (int raw = 1; raw<m_height-1; raw++) {
		    for (int col = 1; col<m_width-1; col++) {
		    	int pixelHorizontal = 	
		    			 1 * array[(raw-1)*m_width+col-1]  +  2 * array[raw*m_width+col-1] +  1 * array[(raw+1)*m_width+col-1] +		    			
		    			-1 * array[(raw-1)*m_width+col+1]  -  2 * array[raw*m_width+col+1] -  1 * array[(raw+1)*m_width+col+1];	
		    	int PixelVertical =	
		    			-1 * array[(raw-1)*m_width+col-1] + 1 * array[(raw+1)*m_width+col-1] +
		    			-2 * array[(raw-1)*m_width+col]   + 2 * array[(raw+1)*m_width+col]   +
						-1 * array[(raw-1)*m_width+col+1] + 1 * array[(raw+1)*m_width+col+1];
		    	
		    	int pixel = (int) Math.sqrt(pixelHorizontal * pixelHorizontal + PixelVertical * PixelVertical);
		        if (pixel > 255)
		        	pixel  = 255;		        
		        else if (pixel < 0)
		        	pixel  = 0;		
		        imageArray[raw*m_width+col] = pixel;
		    }
		}
		return imageArray;
	}

	
	//-------------------------------------------------------------
	public void result(int[] array, BufferedImage image, BufferedImage imageResult) {
		for (int raw = 0; raw<m_height; raw++) {
		    for (int col = 0; col<m_width; col++) {
		        if(array[raw*m_width+col] == 255) {
			        Color pix = new Color(0,0,255);
			        imageResult.setRGB(col, raw, pix.getRGB());
		        }
		        else {
			        int rgb = image.getRGB(col, raw);
			        imageResult.setRGB(col, raw, rgb);
		        }
		    }
		}		
	}
	
	
	//-------------------------------------------------------------
	public void arrayToImage(int[] array, BufferedImage image) {
		for (int raw = 0; raw<m_height; raw++) {
		    for (int col = 0; col<m_width; col++) {
		    	int pixel = array[raw*m_width+col];
		    	Color pix = new Color(pixel,pixel,pixel);
		        image.setRGB(col, raw, pix.getRGB());
		    }
		}
	}
	
	
	//-------------------------------------------------------------
	public int[] lineToArrayImage(int[] m_imageArrayLeftHorizontal, int[] m_imageArrayLeftVertical, int[] m_imageArrayLeftHorizontal2, 
			int[] m_imageArrayRighttHorizontal, int[] m_imageArrayRightVertical, int[] m_imageArrayRighttHorizontal2) {
		
		int[] imageArray = new int[m_width * m_height];
		for (int raw = 0; raw<m_height; raw++) {
		    for (int col = 0; col<m_width; col++) {
		        if(m_imageArrayLeftHorizontal[raw*m_width+col]>0 || m_imageArrayLeftVertical[raw*m_width+col]>0 || m_imageArrayRighttHorizontal[raw*m_width+col]>0
		        		|| m_imageArrayRightVertical[raw*m_width+col]>0 || m_imageArrayLeftHorizontal2[raw*m_width+col]>0 || m_imageArrayRighttHorizontal2[raw*m_width+col]>0)
		        	imageArray[raw*m_width+col] = 255;
		        else
		        	imageArray[raw*m_width+col] = 0;
		    }
		}
		return imageArray;
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
	public int[] intersectionPoint(int[] array1, int[] array2) {
	    for(int row=0; row<m_height; row++){
	    	for(int col=0; col<m_width; col++) {
	    		if(array1[row*m_width+col] + array2[row*m_width+col] >= 2)
	    			return new int[]{col, row};
	    	}
	    }
	    return null;
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



