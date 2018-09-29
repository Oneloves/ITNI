package detection.road;

import java.awt.Color;
import java.awt.image.BufferedImage;

import image.Image;

public class RoadLine {
	public int m_width;
	public int m_height;
	public int type;

	public int[] m_imageArrayLeftLine;
	public int[] m_imageArrayLeftLine2;

	public int[] m_imageArrayRightLine;
	public int[] m_imageArrayRightLine2;
	
	public int[] m_imageArrayResult;

	
	public RoadLine(Image image) {		
    	m_width = image.m_width;
    	m_height = image.m_height;
    	type = image.m_type;
    	
// HOUGH TRANSFORM FOR LINES
    	// Hough transform left road line 20-30
    	HoughRoad houghLeft = new HoughRoad(image.m_imageArraySobelMiddleLeftWithe, 7, m_width, m_height, 1, 30, 60);
    	m_imageArrayLeftLine = houghLeft.process();
    	
    	HoughRoad houghLeft2 = new HoughRoad(flip(image.m_imageArraySobelMiddleLeftWithe), 7, m_width, m_height, 1, 30, 60);
    	m_imageArrayLeftLine2 = houghLeft2.process();
  
		if(houghLeft.bestValue < houghLeft2.bestValue)
			m_imageArrayLeftLine = flip(m_imageArrayLeftLine2);
    	
    	// Hough transform right road line
    	HoughRoad houghRight = new HoughRoad(image.m_imageArraySobelMiddleRightWithe, 7, m_width, m_height, 1, 30, 60);
    	m_imageArrayRightLine = houghRight.process();
    	m_imageArrayRightLine = flip(m_imageArrayRightLine);
    	
    	HoughRoad houghRight2 = new HoughRoad(image.m_imageArraySobelMiddleRightWithe, 7, m_width, m_height, 1, 30, 60);
    	m_imageArrayRightLine2 = houghRight.process();

		if(houghRight.bestValue < houghRight2.bestValue)
			m_imageArrayRightLine = m_imageArrayRightLine2;   	
    	
// RESULT
    	m_imageArrayResult = lineToArrayImage(m_imageArrayLeftLine, m_imageArrayRightLine);
    	result(m_imageArrayResult, image.m_bufferImage, image.m_bufferImage);	
	}
	
	//-------------------------------------------------------------
	public void result(int[] array, BufferedImage image, BufferedImage imageResult) {
		for (int raw = 0; raw<m_height; raw++) {
		    for (int col = 0; col<m_width; col++) {
		        if(array[raw*m_width+col] == 255) {
			        Color pix = new Color(255,0,0);
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
	public int[] lineToArrayImage(int[] m_imageArrayLeftLine, int[] m_imageArrayRightLine) {
		int[] imageArray = new int[m_width * m_height];
		for (int raw = 0; raw<m_height; raw++) {
		    for (int col = 0; col<m_width; col++) {
		        if(m_imageArrayLeftLine[raw*m_width+col] > 0 || m_imageArrayRightLine[raw*m_width+col] >0)
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
}
