package image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import tool.ColorSelection;
import tool.Pixel;


public class Image {
	
	public int m_type;
	public int m_width;
	public int m_height;

	// Pixel array original image with cut
	public Pixel[] m_imageArrayPixel;
	public Pixel[] m_imageArrayLeft;
	public Pixel[] m_imageArrayRight;
	public Pixel[] m_imageArrayMiddleLeft;
	public Pixel[] m_imageArrayMiddleRight;

	public Pixel[] m_imageArrayRightFlip;
	//public Pixel[] m_imageArrayMiddleRightFlip;
	public Pixel[] m_imageArrayPixelFlip;
	
	// Array color selection 
	public int[] m_imageArrayColorSelectionLeftBlue;
	public int[] m_imageArrayColorSelectionRightBlue;
	public int[] m_imageArrayColorSelectionLeftRed;
	public int[] m_imageArrayColorSelectionRightRed;
	public int[] m_imageArrayColorSelectionMiddleLeftWithe;
	public int[] m_imageArrayColorSelectionMiddleRightWithe;
	
	// Array Sobel 
	public int[] m_imageArraySobelLeftRed;
	public int[] m_imageArraySobelRightRed;
	public int[] m_imageArraySobelLeftBlue;
	public int[] m_imageArraySobelRightBlue;
	public int[] m_imageArraySobelMiddleLeftWithe;
	public int[] m_imageArraySobelMiddleRightWithe;

	public BufferedImage m_bufferImage;

	public BufferedImage m_bufferImageLeftRed;
	public BufferedImage m_bufferImageRightRed;
	public BufferedImage m_bufferImageLeftBlue;
	public BufferedImage m_bufferImageRightBlue;
	public BufferedImage m_bufferImageMiddleLeftWithe;
	public BufferedImage m_bufferImageMiddleRightWithe;
	

	public BufferedImage test;
	public BufferedImage test2;

	
	public Image(String filePath){
		try {
			File file = new File(filePath);
	    	m_bufferImage = ImageIO.read(file);

	    	m_type = m_bufferImage.getType();
	    	m_width = m_bufferImage.getWidth();
	    	m_height = m_bufferImage.getHeight();
	    	
// Initialisation
	    	m_bufferImageLeftRed = new BufferedImage(m_width, m_height, m_type);
	    	m_bufferImageRightRed = new BufferedImage(m_width, m_height, m_type);
	    	m_bufferImageLeftBlue = new BufferedImage(m_width, m_height, m_type);
	    	m_bufferImageRightBlue = new BufferedImage(m_width, m_height, m_type);
	    	m_bufferImageMiddleLeftWithe = new BufferedImage(m_width, m_height, m_type);
	    	m_bufferImageMiddleRightWithe = new BufferedImage(m_width, m_height, m_type);

	    	test = new BufferedImage(m_width, m_height, m_type);	
	    	test2 = new BufferedImage(m_width, m_height, m_type);	    	

	    	int size = m_width*m_height;
	    	
			m_imageArrayLeft = new Pixel[size];			
			m_imageArrayRight = new Pixel[size];			
			m_imageArrayMiddleLeft = new Pixel[size];			
			m_imageArrayMiddleRight = new Pixel[size];				
			m_imageArrayRightFlip = new Pixel[size];			
			//m_imageArrayMiddleRightFlip = new Pixel[size];
			m_imageArrayPixelFlip = new Pixel[size];
			
			m_imageArrayColorSelectionLeftRed = new int[size];	
			m_imageArrayColorSelectionLeftBlue = new int[size];
			m_imageArrayColorSelectionRightRed = new int[size];	
			m_imageArrayColorSelectionRightBlue = new int[size];	
			m_imageArrayColorSelectionMiddleLeftWithe = new int[size];	
			m_imageArrayColorSelectionMiddleRightWithe = new int[size];	
			
			m_imageArraySobelLeftRed = new int[size];	
			m_imageArraySobelRightRed = new int[size];
			m_imageArraySobelLeftBlue = new int[size];	
			m_imageArraySobelRightBlue = new int[size];	
			m_imageArraySobelMiddleLeftWithe = new int[size];	
			m_imageArraySobelMiddleRightWithe = new int[size];		
			
// Some treatement 	
			// Get pixel from image and compute is hsv value
	    	m_imageArrayPixel = getPixels(m_bufferImage);
	    	flip();
	    	
	    	// Cute the picture to isolate the middle part, left part, and the right part. 
	    	cutImage();

	    	// Color selection => Blue, Withe, Red
	    	ColorSelection colorSelection = new ColorSelection(this, m_width, m_height);
	    	colorSelection.select();

	    	m_imageArrayColorSelectionRightRed = dilate(m_imageArrayColorSelectionRightRed);
	    	m_imageArrayColorSelectionLeftRed = dilate(m_imageArrayColorSelectionLeftRed);
	    	
	    	m_imageArrayColorSelectionLeftBlue = dilate(m_imageArrayColorSelectionLeftBlue);
	    	m_imageArrayColorSelectionRightBlue = dilate(m_imageArrayColorSelectionRightBlue);
	    	
	    	m_imageArrayColorSelectionMiddleLeftWithe = dilate(m_imageArrayColorSelectionMiddleLeftWithe);
	    	m_imageArrayColorSelectionMiddleRightWithe = dilate(m_imageArrayColorSelectionMiddleRightWithe);
	    	
	    	// Sobel
	    	sobel();

	    	arrayToImage(m_imageArraySobelMiddleLeftWithe, test);
	    	arrayToImage(m_imageArraySobelMiddleRightWithe, test2);
// Array to Image
	    	arrayToImage();
	    	
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	//-------------------------------------------------------------
	public int[] dilate(int[] array) {
		int[] imageArray = new int[m_width * m_height];
	    for (int raw = 1; raw < m_height-2; raw++)
	        for (int col = 1; col < m_width/2-2; col++) {
	        	int pixel = array[raw*m_width+col];	       
	        	if(array[(raw-1)*m_width+col] != 0)
	        		pixel = 255;
	        	else if(array[(raw+1)*m_width+col] != 0)
	        		pixel = 255;
	        	else if(array[raw*m_width+col-1] != 0)
	        		pixel = 255;
	        	else if(array[raw*m_width+col+1] != 0)
	        		pixel = 255;
	        	
		        if(pixel==255) {
			        imageArray[raw*m_width+col] = pixel;
			        imageArray[(raw+1)*m_width+col] = pixel;     	
			        imageArray[(raw-1)*m_width+col] = pixel;
	
			        imageArray[raw*m_width+col-1] = pixel;     	
			        imageArray[(raw+1)*m_width+col-1] = pixel;     	
			        imageArray[(raw-1)*m_width+col-1] = pixel;    
			        
			        imageArray[raw*m_width+col+1] = pixel;     	
			        imageArray[(raw+1)*m_width+col+1] = pixel;     	
			        imageArray[(raw-1)*m_width+col+1] = pixel; 
		        }
	        }
		return imageArray;
	}

	
	//-------------------------------------------------------------
	public void flip() {
	    for(int row=0; row<m_height; row++){
	    	for(int col=0; col<m_width/2; col++) {
	    		m_imageArrayPixelFlip[row*m_width+(m_width-col-1)] = m_imageArrayPixel[row*m_width+col];
	    		m_imageArrayPixelFlip[row*m_width+col] = m_imageArrayPixel[row*m_width+(m_width-col-1)];
	    		
	    		//m_imageArrayMiddleRightFlip[row*m_width+(m_width-col-1)] = m_imageArrayMiddleRight[row*m_width+col];
	    		//m_imageArrayMiddleRightFlip[row*m_width+col] = m_imageArrayMiddleRight[row*m_width+(m_width-col-1)];
	    		
	    		m_imageArrayRightFlip[row*m_width+(m_width-col-1)] = m_imageArrayRight[row*m_width+col];
	    		m_imageArrayRightFlip[row*m_width+col] = m_imageArrayRight[row*m_width+(m_width-col-1)];
	    	}
	    }
	}
	
	
	//-------------------------------------------------------------
	public void arrayToImage(Pixel[] array, BufferedImage image) {
		for (int raw = 0; raw<m_height; raw++) {
		    for (int col = 0; col<m_width; col++) {
		    	Pixel pixel = array[raw*m_width+col];
		    	Color pix = new Color(pixel.m_r,pixel.m_g,pixel.m_b);
		        image.setRGB(col, raw, pix.getRGB());
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
	public void arrayToImage() {
		int pixel;
		Color pix;
		
		for (int raw = 0; raw<m_height; raw++) {
		    for (int col = 0; col<m_width; col++) {

		    	// Sobel left blue cut
		    	pixel = m_imageArraySobelLeftBlue[raw*m_width+col];
		    	pix = new Color(pixel, pixel, pixel);
		    	m_bufferImageLeftBlue.setRGB(col, raw, pix.getRGB());
		        
		    	// Sobel right blue cut
		    	pixel = m_imageArraySobelRightBlue[raw*m_width+col];
		    	pix = new Color(pixel, pixel, pixel);
		    	m_bufferImageRightBlue.setRGB(col, raw, pix.getRGB());
		        
		    	// sobel left red cut
		    	pixel = m_imageArraySobelLeftRed[raw*m_width+col];
		    	pix = new Color(pixel, pixel, pixel);
		    	m_bufferImageLeftRed.setRGB(col, raw, pix.getRGB());
		        
		    	// Sobel right red cut
		    	pixel = m_imageArraySobelRightRed[raw*m_width+col];
		    	pix = new Color(pixel, pixel, pixel);
		    	m_bufferImageRightRed.setRGB(col, raw, pix.getRGB());
		        
		    	// Sobel middle left cut
		    	pixel = m_imageArraySobelMiddleLeftWithe[raw*m_width+col];
		    	pix = new Color(pixel, pixel, pixel);
		    	m_bufferImageMiddleLeftWithe.setRGB(col, raw, pix.getRGB());
		        
		    	// Sobel middle right cut
		    	pixel = m_imageArraySobelMiddleRightWithe[raw*m_width+col];
		    	pix = new Color(pixel, pixel, pixel);
		    	m_bufferImageMiddleRightWithe.setRGB(col, raw, pix.getRGB());
		    }
		}
	}
	
	//-------------------------------------------------------------
	public void sobel() {

		int pixelHorizontal;
		int PixelVertical;
		int pixel;
		
		for (int raw = 1; raw<m_height-1; raw++) {
		    for (int col = 1; col<m_width-1; col++) {		    	
		    	// Middle left cut    
		    	pixelHorizontal = 	
		    			 1 * m_imageArrayColorSelectionMiddleLeftWithe[(raw-1)*m_width+col-1]  +  2 * m_imageArrayColorSelectionMiddleLeftWithe[raw*m_width+col-1] +  1 * m_imageArrayColorSelectionMiddleLeftWithe[(raw+1)*m_width+col-1] +		    			
		    			-1 * m_imageArrayColorSelectionMiddleLeftWithe[(raw-1)*m_width+col+1]  -  2 * m_imageArrayColorSelectionMiddleLeftWithe[raw*m_width+col+1] -  1 * m_imageArrayColorSelectionMiddleLeftWithe[(raw+1)*m_width+col+1];	
		    	PixelVertical =	
		    			-1 * m_imageArrayColorSelectionMiddleLeftWithe[(raw-1)*m_width+col-1] + 1 * m_imageArrayColorSelectionMiddleLeftWithe[(raw+1)*m_width+col-1] +
		    			-2 * m_imageArrayColorSelectionMiddleLeftWithe[(raw-1)*m_width+col]   + 2 * m_imageArrayColorSelectionMiddleLeftWithe[(raw+1)*m_width+col]   +
						-1 * m_imageArrayColorSelectionMiddleLeftWithe[(raw-1)*m_width+col+1] + 1 * m_imageArrayColorSelectionMiddleLeftWithe[(raw+1)*m_width+col+1];		    	
		    	pixel = (int) Math.sqrt(pixelHorizontal * pixelHorizontal + PixelVertical * PixelVertical);
		    	if (pixel > 255)
		    		pixel  = 255;		        
		        else if (pixel < 0)
		        	pixel  = 0;				        
		        m_imageArraySobelMiddleLeftWithe[raw*m_width+col] = pixel;
		        		       
		        // Middle right cut	    	
		    	pixelHorizontal = 	
		    			 1 * m_imageArrayColorSelectionMiddleRightWithe[(raw-1)*m_width+col-1]  +  2 * m_imageArrayColorSelectionMiddleRightWithe[raw*m_width+col-1] +  1 * m_imageArrayColorSelectionMiddleRightWithe[(raw+1)*m_width+col-1] +		    			
		    			-1 * m_imageArrayColorSelectionMiddleRightWithe[(raw-1)*m_width+col+1]  -  2 * m_imageArrayColorSelectionMiddleRightWithe[raw*m_width+col+1] -  1 * m_imageArrayColorSelectionMiddleRightWithe[(raw+1)*m_width+col+1];	
		    	PixelVertical =	
		    			-1 * m_imageArrayColorSelectionMiddleRightWithe[(raw-1)*m_width+col-1] + 1 * m_imageArrayColorSelectionMiddleRightWithe[(raw+1)*m_width+col-1] +
		    			-2 * m_imageArrayColorSelectionMiddleRightWithe[(raw-1)*m_width+col]   + 2 * m_imageArrayColorSelectionMiddleRightWithe[(raw+1)*m_width+col]   +
						-1 * m_imageArrayColorSelectionMiddleRightWithe[(raw-1)*m_width+col+1] + 1 * m_imageArrayColorSelectionMiddleRightWithe[(raw+1)*m_width+col+1];		    			    	
		    	pixel = (int) Math.sqrt(pixelHorizontal * pixelHorizontal + PixelVertical * PixelVertical);
		    	if (pixel > 255)
		    		pixel  = 255;		        
		        else if (pixel < 0)
		        	pixel  = 0;				        
		        m_imageArraySobelMiddleRightWithe[raw*m_width+col] = pixel;
		        
		        
		        // Left Blue cut
		    	pixelHorizontal = 	
		    			 1 * m_imageArrayColorSelectionLeftBlue[(raw-1)*m_width+col-1]  +  2 * m_imageArrayColorSelectionLeftBlue[raw*m_width+col-1] +  1 * m_imageArrayColorSelectionLeftBlue[(raw+1)*m_width+col-1] +		    			
		    			-1 * m_imageArrayColorSelectionLeftBlue[(raw-1)*m_width+col+1]  -  2 * m_imageArrayColorSelectionLeftBlue[raw*m_width+col+1] -  1 * m_imageArrayColorSelectionLeftBlue[(raw+1)*m_width+col+1];	
		    	PixelVertical =	
		    			-1 * m_imageArrayColorSelectionLeftBlue[(raw-1)*m_width+col-1] + 1 * m_imageArrayColorSelectionLeftBlue[(raw+1)*m_width+col-1] +
		    			-2 * m_imageArrayColorSelectionLeftBlue[(raw-1)*m_width+col]   + 2 * m_imageArrayColorSelectionLeftBlue[(raw+1)*m_width+col]   +
						-1 * m_imageArrayColorSelectionLeftBlue[(raw-1)*m_width+col+1] + 1 * m_imageArrayColorSelectionLeftBlue[(raw+1)*m_width+col+1];		    	
		    	pixel = (int) Math.sqrt(pixelHorizontal * pixelHorizontal + PixelVertical * PixelVertical);
		        if (pixel > 255)
		        	pixel  = 255;		        
		        else if (pixel < 0)
		        	pixel  = 0;		
		        m_imageArraySobelLeftBlue[raw*m_width+col] = pixel;
		        
		        
		        // Right Blue cut
		    	pixelHorizontal = 	
		    			 1 * m_imageArrayColorSelectionRightBlue[(raw-1)*m_width+col-1]  +  2 * m_imageArrayColorSelectionRightBlue[raw*m_width+col-1] +  1 * m_imageArrayColorSelectionRightBlue[(raw+1)*m_width+col-1] +		    			
		    			-1 * m_imageArrayColorSelectionRightBlue[(raw-1)*m_width+col+1]  -  2 * m_imageArrayColorSelectionRightBlue[raw*m_width+col+1] -  1 * m_imageArrayColorSelectionRightBlue[(raw+1)*m_width+col+1];	
		    	PixelVertical =	
		    			-1 * m_imageArrayColorSelectionRightBlue[(raw-1)*m_width+col-1] + 1 * m_imageArrayColorSelectionRightBlue[(raw+1)*m_width+col-1] +
		    			-2 * m_imageArrayColorSelectionRightBlue[(raw-1)*m_width+col]   + 2 * m_imageArrayColorSelectionRightBlue[(raw+1)*m_width+col]   +
						-1 * m_imageArrayColorSelectionRightBlue[(raw-1)*m_width+col+1] + 1 * m_imageArrayColorSelectionRightBlue[(raw+1)*m_width+col+1];		    	
		    	pixel = (int) Math.sqrt(pixelHorizontal * pixelHorizontal + PixelVertical * PixelVertical);
		        if (pixel > 255)
		        	pixel  = 255;		        
		        else if (pixel < 0)
		        	pixel  = 0;		
		        m_imageArraySobelRightBlue[raw*m_width+col] = pixel;
		        
		        
		        // Left Red cut
		    	pixelHorizontal = 	
		    			 1 * m_imageArrayColorSelectionLeftRed[(raw-1)*m_width+col-1]  +  2 * m_imageArrayColorSelectionLeftRed[raw*m_width+col-1] +  1 * m_imageArrayColorSelectionLeftRed[(raw+1)*m_width+col-1] +		    			
		    			-1 * m_imageArrayColorSelectionLeftRed[(raw-1)*m_width+col+1]  -  2 * m_imageArrayColorSelectionLeftRed[raw*m_width+col+1] -  1 * m_imageArrayColorSelectionLeftRed[(raw+1)*m_width+col+1];	
		    	PixelVertical =	
		    			-1 * m_imageArrayColorSelectionLeftRed[(raw-1)*m_width+col-1] + 1 * m_imageArrayColorSelectionLeftRed[(raw+1)*m_width+col-1] +
		    			-2 * m_imageArrayColorSelectionLeftRed[(raw-1)*m_width+col]   + 2 * m_imageArrayColorSelectionLeftRed[(raw+1)*m_width+col]   +
						-1 * m_imageArrayColorSelectionLeftRed[(raw-1)*m_width+col+1] + 1 * m_imageArrayColorSelectionLeftRed[(raw+1)*m_width+col+1];		    	
		    	pixel = (int) Math.sqrt(pixelHorizontal * pixelHorizontal + PixelVertical * PixelVertical);
		        if (pixel > 255)
		        	pixel  = 255;		        
		        else if (pixel < 0)
		        	pixel  = 0;		
		        m_imageArraySobelLeftRed[raw*m_width+col] = pixel;
		        
		        
		        // Right Red cut
		    	pixelHorizontal = 	
		    			 1 * m_imageArrayColorSelectionRightRed[(raw-1)*m_width+col-1]  +  2 * m_imageArrayColorSelectionRightRed[raw*m_width+col-1] +  1 * m_imageArrayColorSelectionRightRed[(raw+1)*m_width+col-1] +		    			
		    			-1 * m_imageArrayColorSelectionRightRed[(raw-1)*m_width+col+1]  -  2 * m_imageArrayColorSelectionRightRed[raw*m_width+col+1] -  1 * m_imageArrayColorSelectionRightRed[(raw+1)*m_width+col+1];	
		    	PixelVertical =	
		    			-1 * m_imageArrayColorSelectionRightRed[(raw-1)*m_width+col-1] + 1 * m_imageArrayColorSelectionRightRed[(raw+1)*m_width+col-1] +
		    			-2 * m_imageArrayColorSelectionRightRed[(raw-1)*m_width+col]   + 2 * m_imageArrayColorSelectionRightRed[(raw+1)*m_width+col]   +
						-1 * m_imageArrayColorSelectionRightRed[(raw-1)*m_width+col+1] + 1 * m_imageArrayColorSelectionRightRed[(raw+1)*m_width+col+1];		    	
		    	pixel = (int) Math.sqrt(pixelHorizontal * pixelHorizontal + PixelVertical * PixelVertical);
		        if (pixel > 255)
		        	pixel  = 255;		        
		        else if (pixel < 0)
		        	pixel  = 0;		
		        m_imageArraySobelRightRed[raw*m_width+col] = pixel;		        		       
		    }
		}
	}
	
	
	//-------------------------------------------------------------
	public void cutImage() {
		int xa = 0;
		int ya = m_height;
		int xb = m_width/2;
		int yb = 0;
		int d = 0;
				
		for (int raw = 0; raw<m_height; raw++) {
		    for (int col = 0; col<m_width; col++) {	
				d = (xb-xa)*(raw-ya)-(yb-ya)*(col-xa);
				//Keep road part and delete sides left and right 
				if(d<0 || col>m_width/2 || raw<m_height/3) {
					m_imageArrayMiddleLeft[raw*m_width+col] = new Pixel(0, 0 ,0);
					m_imageArrayMiddleRight[raw*m_width+col] = new Pixel(0, 0 ,0);
				}
				else {
					m_imageArrayMiddleLeft[raw*m_width+col] = m_imageArrayPixel[raw*m_width+col];
					m_imageArrayMiddleRight[raw*m_width+col] = m_imageArrayPixelFlip[raw*m_width+col];
				}
				//Keep side part of the road
				if(d>0 || col>m_width/2) {
					m_imageArrayLeft[raw*m_width+col] = new Pixel(0, 0 ,0);
					m_imageArrayRight[raw*m_width+col] = new Pixel(0, 0 ,0);
				}
				else {
					m_imageArrayLeft[raw*m_width+col] = m_imageArrayPixel[raw*m_width+col];
					m_imageArrayRight[raw*m_width+col] = m_imageArrayPixelFlip[raw*m_width+col];
				}
		    }
		}
	}
	
	
	//-------------------------------------------------------------
	public Pixel[] getPixels(BufferedImage image) {		
		Pixel[] imageArray = new Pixel[m_width * m_height];	
        for (int raw=0; raw<m_height; raw++) {
            for (int col=0; col<m_width; col++) {
                int rgb = m_bufferImage.getRGB(col, raw);
                int red = (rgb >> 16) & 0x000000ff;
                int green = (rgb >> 8)  & 0x000000ff;
                int blue = rgb & 0x000000ff;
                Pixel pixel = new Pixel(red, green, blue);
                imageArray[raw*m_width+col]  = pixel;
            }
        }
		return imageArray;
	}
}








