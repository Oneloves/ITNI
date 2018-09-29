package detection.circle;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import detection.hough.Circle;
import image.Image;
import tool.ColorSelection;

public class CircleRoad {
	public int m_width;
	public int m_height;
	public int type;

	public int[] m_imageArrayResult;
	public int[] m_imageArrayColorSelectionLeft;
	public int[] m_imageArrayColorSelectionRight;

	public BufferedImage m_bufferImageLineDetected;
	public BufferedImage m_bufferImageColorSelectionLef;
	public BufferedImage m_bufferImageColorSelectionRight;
	
	public CircleRoad(Image image) {
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
		Circle houghCircleLeft = new Circle(m_imageArrayColorSelectionLeft, m_width, m_height);
		houghCircleLeft.process();		
		
//RIGHT PART    	
		Circle houghCricleRight = new Circle(m_imageArrayColorSelectionRight, m_width, m_height);
		houghCricleRight.process();
		
//RESULT
		arrayToImage(m_imageArrayColorSelectionLeft, m_bufferImageColorSelectionLef);
		m_imageArrayColorSelectionRight = flip(m_imageArrayColorSelectionRight);
		arrayToImage(m_imageArrayColorSelectionRight, m_bufferImageColorSelectionRight);
	
		m_bufferImageLineDetected = image.m_bufferImage;
	    Graphics2D g = m_bufferImageLineDetected.createGraphics();
	    
	    g.setColor(Color.YELLOW);
	    g.drawOval(houghCircleLeft.betsX-houghCircleLeft.bestR,  houghCircleLeft.bestY-houghCircleLeft.bestR, 2*houghCircleLeft.bestR, 2*houghCircleLeft.bestR);
	    g.drawOval(m_width-houghCricleRight.betsX-1-houghCricleRight.bestR,  houghCricleRight.bestY-houghCricleRight.bestR, 2*houghCricleRight.bestR, 2*houghCricleRight.bestR);
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
	public int[] circleToArrayImage(int[] circle1, int[] circle2) {		
		int[] imageArray = new int[m_width * m_height];
		for (int raw = 0; raw<m_height; raw++) {
		    for (int col = 0; col<m_width; col++) {
		        if(circle1[raw*m_width+col]>0 || circle2[raw*m_width+col]>0)
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
