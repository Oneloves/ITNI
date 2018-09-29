package detection.rec;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import detection.hough.Line;
import image.Image;
import tool.ColorSelection;

public class RecRoad {
	public int m_width;
	public int m_height;
	public int type;

	public int[] m_imageArrayResult;
	public int[] m_imageArrayColorSelectionLeft;
	public int[] m_imageArrayColorSelectionRight;
	public int[] m_imageArrayLeftVerticalLine1;
	public int[] m_imageArrayLeftVerticalLine2;
	public int[] m_imageArrayLeftHorizontalLine1;
	public int[] m_imageArrayLeftHorizontalLine2;
	public int[] m_imageArrayRighttVerticalLine1;
	public int[] m_imageArrayRighttVerticalLine2;
	public int[] m_imageArrayRightHorizontalLine1;
	public int[] m_imageArrayRightHorizontalLine2;

	int[] pointLeft1;
	int[] pointLeft2;
	int[] pointLeft3;
	int[] pointLeft4;

	int[] pointRight1;
	int[] pointRight2;
	int[] pointRight3;
	int[] pointRight4;
	
	public BufferedImage m_bufferImageLineDetected;
	public BufferedImage m_bufferImageColorSelectionLef;
	public BufferedImage m_bufferImageColorSelectionRight;
	public BufferedImage test;


	public RecRoad(Image image) {
    	m_width = image.m_width;
    	m_height = image.m_height;
    	type = image.m_type;

    	m_bufferImageLineDetected = new BufferedImage(m_width, m_height, type);
    	m_bufferImageColorSelectionLef = new BufferedImage(m_width, m_height, type);
    	m_bufferImageColorSelectionRight = new BufferedImage(m_width, m_height, type);
    	test = new BufferedImage(m_width, m_height, type);
    	
// COLOR SELECTION
    	// COLOR SELECTION LEFT
    	ColorSelection colorSelectionLeft = new ColorSelection(image.m_imageArrayLeft, m_width, m_height);
    	m_imageArrayColorSelectionLeft = colorSelectionLeft.select(185, 290, 40, 100, 40, 100);
    	m_imageArrayColorSelectionLeft =  dilate(m_imageArrayColorSelectionLeft, test);
    	m_imageArrayColorSelectionLeft = sobel(m_imageArrayColorSelectionLeft);
    	
    	// COLOR SELECTION RIGHT
    	ColorSelection colorSelectionRight = new ColorSelection(image.m_imageArrayRight, m_width, m_height);
    	m_imageArrayColorSelectionRight = colorSelectionRight.select(185, 290, 40, 100, 40, 100);
    	m_imageArrayColorSelectionRight =  dilate(m_imageArrayColorSelectionRight, test);
    	m_imageArrayColorSelectionRight = sobel(m_imageArrayColorSelectionRight);
    	
// LEFT PART    	
    	// Hough transform left line
    	Line houghLeftHorizontal = new Line(m_imageArrayColorSelectionLeft, 20, m_width, m_height, 2, 85, 95);
    	houghLeftHorizontal.processForRec();
    	m_imageArrayLeftHorizontalLine1 = houghLeftHorizontal.output1;
    	m_imageArrayLeftHorizontalLine2 = houghLeftHorizontal.output2;
    	
    	// Hough transform right line
    	Line houghLeftVertical = new Line(m_imageArrayColorSelectionLeft, 20, m_width, m_height, 2, 0, 10);
    	houghLeftVertical.processForRec();
    	m_imageArrayRighttVerticalLine1 = houghLeftVertical.output1;
    	m_imageArrayRighttVerticalLine2 = houghLeftVertical.output2;
    	
    	// Get intersection points
    	pointLeft1 = intersectionPoint(m_imageArrayLeftHorizontalLine1, m_imageArrayRighttVerticalLine1);
    	pointLeft2 = intersectionPoint(m_imageArrayLeftHorizontalLine1, m_imageArrayRighttVerticalLine2);
    	pointLeft3 = intersectionPoint(m_imageArrayLeftHorizontalLine2, m_imageArrayRighttVerticalLine1);
    	pointLeft4 = intersectionPoint(m_imageArrayLeftHorizontalLine2, m_imageArrayRighttVerticalLine2);
    	    	
// RIGHT PART
    	// Hough transform left line
    	Line houghRightHorizontal = new Line(m_imageArrayColorSelectionRight, 20, m_width, m_height, 2, 85, 95);
    	houghRightHorizontal.processForRec();
    	m_imageArrayRightHorizontalLine1 = houghRightHorizontal.output1;
    	m_imageArrayRightHorizontalLine2 = houghRightHorizontal.output2;
    	
    	// Hough transform right line
    	Line houghRightVertical = new Line(m_imageArrayColorSelectionRight, 20, m_width, m_height, 2, 0, 10);
    	houghRightVertical.processForRec();
    	m_imageArrayLeftVerticalLine1 = houghRightVertical.output1;
    	m_imageArrayLeftVerticalLine2 = houghRightVertical.output2;
    	
    	// Get intersection points
    	pointRight1 = intersectionPoint(m_imageArrayRightHorizontalLine1, m_imageArrayLeftVerticalLine1);
    	pointRight2 = intersectionPoint(m_imageArrayRightHorizontalLine1, m_imageArrayLeftVerticalLine2);
    	pointRight3 = intersectionPoint(m_imageArrayRightHorizontalLine2, m_imageArrayLeftVerticalLine1);
    	pointRight4 = intersectionPoint(m_imageArrayRightHorizontalLine2, m_imageArrayLeftVerticalLine2);

    	
// RESULT
		arrayToImage(m_imageArrayColorSelectionLeft, m_bufferImageColorSelectionLef);
		m_imageArrayColorSelectionRight = flip(m_imageArrayColorSelectionRight);
		arrayToImage(m_imageArrayColorSelectionRight, m_bufferImageColorSelectionRight);

		m_imageArrayRightHorizontalLine1 = flip(m_imageArrayRightHorizontalLine1);
		m_imageArrayRightHorizontalLine2 = flip(m_imageArrayRightHorizontalLine2);
		m_imageArrayLeftVerticalLine1 = flip(m_imageArrayLeftVerticalLine1);
		m_imageArrayLeftVerticalLine2 = flip(m_imageArrayLeftVerticalLine2);
		
		m_imageArrayResult = lineToArrayImage(m_imageArrayLeftHorizontalLine1, m_imageArrayLeftHorizontalLine2, m_imageArrayRighttVerticalLine1, m_imageArrayRighttVerticalLine2,
				m_imageArrayRightHorizontalLine1, m_imageArrayRightHorizontalLine2, m_imageArrayLeftVerticalLine1, m_imageArrayLeftVerticalLine2);
		
		result(m_imageArrayResult, image.m_bufferImage, m_bufferImageLineDetected);

// Points
	    Graphics2D g = m_bufferImageLineDetected.createGraphics();
	    
	    if(pointLeft1 != null && pointLeft2 != null && pointLeft3 != null && pointLeft4 != null) {
		    g.setColor(Color.RED);
		    g.fillOval(pointLeft1[0]-5, pointLeft1[1]-5, 10, 10);
		    g.fillOval(pointLeft2[0]-5, pointLeft2[1]-5, 10, 10);
		    g.fillOval(pointLeft3[0]-5, pointLeft3[1]-5, 10, 10);
		    g.fillOval(pointLeft4[0]-5, pointLeft4[1]-5, 10, 10);
	    }
	    
	    if(pointRight1 != null && pointRight2 != null && pointRight3 != null && pointRight4 != null) {
		    g.setColor(Color.RED);
		    g.fillOval(m_width-pointRight1[0]-1-5, pointRight1[1]-5, 10, 10);
		    g.fillOval(m_width-pointRight2[0]-1-5, pointRight2[1]-5, 10, 10);
		    g.fillOval(m_width-pointRight3[0]-1-5, pointRight3[1]-5, 10, 10);
		    g.fillOval(m_width-pointRight4[0]-1-5, pointRight4[1]-5, 10, 10);
	    }
	}
	

	
	//-------------------------------------------------------------
	public int[] erode(int[] array, BufferedImage image) {
		
		int[] imageArray = new int[m_width * m_height];
		
	    for (int raw = 1; raw < m_height-1; raw++)
	        for (int col = 1; col < m_width-1; col++) {
	        	int pixel = array[raw*m_width+col];	        			
	        	if(array[(raw-1)*m_width+col] == 0)
	        		pixel = 0;
	        	else if(array[(raw+1)*m_width+col-1] == 0)
	        		pixel = 0;
	        	else if(array[raw*m_width+col-1] == 0)
	        		pixel = 0;
	        	else if(array[raw*m_width+col+1] == 0)
	        		pixel = 0;
	        	
		        imageArray[raw*m_width+col] = pixel;
                Color pix = new Color(pixel,pixel,pixel);
                image.setRGB(col, raw, pix.getRGB());
	        }
	    
		return imageArray;
	}
	
	//-------------------------------------------------------------
	public int[] dilate(int[] array, BufferedImage image) {
		int[] imageArray = new int[m_width * m_height];
		for(int i=0; i<120; i++)
		    for (int raw = 1; raw < m_height-1; raw++)
		        for (int col = 1; col < m_width-1; col++) {
		        	int pixel = array[raw*m_width+col];	       
		        	if(array[(raw-1)*m_width+col] != 0)
		        		pixel = 255;
		        	else if(array[(raw+1)*m_width+col] != 0)
		        		pixel = 255;
		        	else if(array[raw*m_width+col-1] != 0)
		        		pixel = 255;
		        	else if(array[raw*m_width+col+1] != 0)
		        		pixel = 255;     	
			        imageArray[raw*m_width+col] = pixel;
	                Color pix = new Color(pixel,pixel,pixel);
	                image.setRGB(col, raw, pix.getRGB());
		        }
		return imageArray;
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
			        Color pix = new Color(0,255,0);
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
	public int[] lineToArrayImage(int[] m_imageArrayLeftHorizontalLine1, int[] m_imageArrayLeftHorizontalLine2, int[] m_imageArrayRighttVerticalLine1, int[] m_imageArrayRighttVerticalLine2,
									int[] m_imageArrayRightHorizontalLine1, int[] m_imageArrayRightHorizontalLine2, int[] m_imageArrayLeftVerticalLine1, int[] m_imageArrayLeftVerticalLine2) {
		int[] imageArray = new int[m_width * m_height];
		for (int raw = 0; raw<m_height; raw++) {
		    for (int col = 0; col<m_width; col++) {
		        if(m_imageArrayLeftHorizontalLine1[raw*m_width+col]>0 || m_imageArrayLeftHorizontalLine2[raw*m_width+col]>0 || m_imageArrayRighttVerticalLine1[raw*m_width+col]>0 || m_imageArrayRighttVerticalLine2[raw*m_width+col]>0
	        	   || m_imageArrayRightHorizontalLine1[raw*m_width+col]>0 || m_imageArrayRightHorizontalLine2[raw*m_width+col]>0 || m_imageArrayLeftVerticalLine1[raw*m_width+col]>0 || m_imageArrayLeftVerticalLine2[raw*m_width+col]>0)
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
	
}
