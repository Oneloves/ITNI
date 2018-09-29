package detection.road;

import java.awt.Color;
import java.awt.image.BufferedImage;

import detection.hough.Line;
import image.Image;
import tool.ColorSelection;

public class RoadLine {
	public int m_width;
	public int m_height;
	public int type;

	public int[] m_imageArrayLeftLine;
	public int[] m_imageArrayRightLine;
	
	public int[] m_imageArrayResult;
	public int[] m_imageArrayColorSelection;
	public int[] m_imageArrayColorSelectionLeft;
	public int[] m_imageArrayColorSelectionRight;
	
	public BufferedImage m_bufferImageLineDetected;
	public BufferedImage m_bufferImageColorSelectionLeft;
	public BufferedImage m_bufferImageColorSelectionRight;

	
	public RoadLine(Image image) {		
    	m_width = image.m_width;
    	m_height = image.m_height;
    	type = image.m_type;

    	m_bufferImageLineDetected = new BufferedImage(m_width, m_height, type);
    	m_bufferImageColorSelectionLeft = new BufferedImage(m_width, m_height, type);
    	m_bufferImageColorSelectionRight = new BufferedImage(m_width, m_height, type);

    	
// COLOR SELECTION
    	// COLOR SELECTION MIDLE LEFT
    	ColorSelection colorSelectionLeft = new ColorSelection(image.m_imageArrayMiddleLeft, m_width, m_height);
    	m_imageArrayColorSelectionLeft = colorSelectionLeft.select(0, 360, 0, 15, 50, 100);
    	m_imageArrayColorSelectionLeft = sobel(m_imageArrayColorSelectionLeft,  m_bufferImageColorSelectionLeft);
    	
    	// COLOR SELECTION MIDLE RIGHT
    	ColorSelection colorSelectionRight = new ColorSelection(image.m_imageArrayMiddleRight, m_width, m_height);
    	m_imageArrayColorSelectionRight = colorSelectionRight.select(0, 360, 0, 15, 50, 100);
    	m_imageArrayColorSelectionRight = sobel(m_imageArrayColorSelectionRight,  m_bufferImageColorSelectionRight);

    	
// HOUGH TRANSFORM FOR LINES
    	// Hough transform left road line 20-30
    	Line houghLeft = new Line(m_imageArrayColorSelectionLeft, 10, m_width, m_height, 10, 30, 65);
    	m_imageArrayLeftLine = houghLeft.process();
    	
    	// Hough transform right road line
    	Line houghRight = new Line(m_imageArrayColorSelectionRight, 10, m_width, m_height, 10, 30, 65);
    	m_imageArrayRightLine = houghRight.process();
    	m_imageArrayRightLine = flip(m_imageArrayRightLine);

    	
// RESULT
    	m_imageArrayResult = lineToArrayImage(m_imageArrayLeftLine, m_imageArrayRightLine);
    	result(m_imageArrayResult, image.m_bufferImage, m_bufferImageLineDetected);
    	
    	
// ARRAY TO IMAGE
    	arrayToImage(m_imageArrayColorSelectionLeft, m_bufferImageColorSelectionLeft);
    	m_imageArrayColorSelectionRight = flip(m_imageArrayColorSelectionRight);
    	arrayToImage(m_imageArrayColorSelectionRight, m_bufferImageColorSelectionRight);    	
	}
	
	
	//-------------------------------------------------------------
	public int[] sobel(int[] array, BufferedImage image) {
		int[] imageArray = new int[m_width * m_height];
		for (int raw = 1; raw<m_height-1; raw++) {
		    for (int col = 1; col<m_width-1; col++) {
		    	int pixelHorizontal = 0;
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
		        Color pix = new Color(pixel,pixel,pixel);
		        image.setRGB(col, raw, pix.getRGB());
		    }
		}
		return imageArray;
	}
	
	
	//-------------------------------------------------------------
	public int[] dilate(int[] array, BufferedImage image) {
		int[] imageArray = new int[m_width * m_height];
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
	public boolean[] flip(boolean[] array) {
		boolean[] imageArray = new boolean[m_width * m_height];
	    for(int row=0; row<m_height; row++){
	    	for(int col=0; col<m_width/2; col++) {
		         imageArray[row*m_width+(m_width-col-1)] = array[row*m_width+col];
		         imageArray[row*m_width+col] = array[row*m_width+(m_width-col-1)];
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
