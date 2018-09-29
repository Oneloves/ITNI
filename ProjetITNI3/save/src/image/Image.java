package image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import tool.Pixel;

public class Image {
	
	public int m_type;
	public int m_width;
	public int m_height;

	public Pixel[] m_imageArrayPixel;
	
	public Pixel[] m_imageArrayLeft;
	public Pixel[] m_imageArrayRight;
	public Pixel[] m_imageArrayMiddleLeft;
	public Pixel[] m_imageArrayMiddleRight;
	
	public BufferedImage m_bufferImage;
	public BufferedImage m_bufferImageLeft;
	public BufferedImage m_bufferImageRight;
	public BufferedImage m_bufferImageMiddleLeft;
	public BufferedImage m_bufferImageMiddleRight;

	
	public Image(String filePath){
		try {
			File file = new File(filePath);
	    	m_bufferImage = ImageIO.read(file);

	    	m_type = m_bufferImage.getType();
	    	m_width = m_bufferImage.getWidth();
	    	m_height = m_bufferImage.getHeight();
	    	
	    	m_bufferImageLeft = new BufferedImage(m_width, m_height, m_type);
	    	m_bufferImageRight = new BufferedImage(m_width, m_height, m_type);
	    	m_bufferImageMiddleLeft = new BufferedImage(m_width, m_height, m_type);
	    	m_bufferImageMiddleRight = new BufferedImage(m_width, m_height, m_type);


	    	m_imageArrayPixel = getPixels(m_bufferImage);
	    	
// Cut 
	    	// Get the middle left of the image
	    	m_imageArrayMiddleLeft = m_imageArrayPixel.clone();
	    	cutImageLeftAndRight(m_imageArrayMiddleLeft);
	    	
	    	// Get the middle right of the image
	    	m_imageArrayMiddleRight = flip(m_imageArrayPixel);
	    	cutImageLeftAndRight(m_imageArrayMiddleRight);
	    	
	    	// Get the left of the image
	    	m_imageArrayLeft = m_imageArrayPixel.clone();
	    	cutImageRight(m_imageArrayLeft);
	    	
	    	// Get the right of the image
	    	m_imageArrayRight = flip(m_imageArrayPixel);
	    	cutImageRight(m_imageArrayRight);

// Array to Image
	    	arrayToImage(m_imageArrayLeft, m_bufferImageLeft);
	    	arrayToImage(m_imageArrayRight, m_bufferImageRight);
	    	arrayToImage(m_imageArrayMiddleLeft, m_bufferImageMiddleLeft);
	    	arrayToImage(m_imageArrayMiddleRight, m_bufferImageMiddleRight);
	    	
		} catch (IOException e) {
			e.printStackTrace();
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
	
	
	//-------------------------------------------------------------
	public int[] toGrayScale(BufferedImage image) {		
		int[] imageArray = new int[m_width * m_height];
		m_imageArrayPixel = new Pixel[m_width * m_height];		
        for (int raw=0; raw<m_height; raw++) {
            for (int col=0; col<m_width; col++) {
                int rgb = m_bufferImage.getRGB(col, raw);
                int red = (rgb >> 16) & 0x000000ff;
                int green = (rgb >> 8)  & 0x000000ff;
                int blue = rgb & 0x000000ff;
                int average = (red + green + blue)/3;
                imageArray[raw*m_width+col] = average;
                rgb = (average << 24) | (average << 16) | (average << 8) | average;
                image.setRGB(col, raw, rgb);
                
                Pixel pixel = new Pixel(red, green, blue);
                m_imageArrayPixel[raw*m_width+col]  = pixel;
            }
        }
		return imageArray;
	}
	
	
	//-------------------------------------------------------------
	public int[] toSmooth(int[] array, BufferedImage image) {		
		int[] imageArray = array;		
		for (int raw=1; raw<m_height-1; raw++) {
		    for (int col=1; col<m_width-1; col++) {    	
		    	int pixel =	
		    			array[(raw-1)*m_width+col-1] + array[raw*m_width+col-1] + array[(raw+1)*m_width+col-1] +		    			
		    			array[(raw-1)*m_width+col] + array[raw*m_width+col] + array[(raw+1)*m_width+col] +
		    			array[(raw-1)*m_width+col+1] + array[raw*m_width+col+1] + array[(raw+1)*m_width+col+1];
		    	
		    	pixel = pixel/9;
		    	imageArray[raw*m_width+col] = pixel;
		    	Color pix = new Color(pixel,pixel,pixel);
		    	image.setRGB(col, raw, pix.getRGB());
		    }
		}
		return imageArray;		
	}
	
	
	//-------------------------------------------------------------
	public void arrayToImage(Pixel[] array, BufferedImage image) {
		for (int raw = 0; raw<m_height; raw++) {
		    for (int col = 0; col<m_width; col++) {
		    	Pixel pixel = array[raw*m_width+col];
		    	Color pix = new Color(pixel.m_r, pixel.m_g, pixel.m_b);
		        image.setRGB(col, raw, pix.getRGB());
		    }
		}
	}

	
	//-------------------------------------------------------------
	public void cutImageLeftAndRight(Pixel[] array) {		
		int xa = 0;
		int ya = m_height;
		int xb = m_width/2;
		int yb = 0;
		
		// CUT DIAGONALS CUT MIDDLE
		for (int raw = 0; raw<m_height; raw++) {
		    for (int col = 0; col<m_width; col++) {		
				int d = (xb-xa)*(raw-ya)-(yb-ya)*(col-xa);
				if(d<0 || col>m_width/2)
					array[raw*m_width+col] = new Pixel(0, 0 ,0);
		    }
		}			
		// CUT UP
		for (int raw = 0; raw<m_height/3; raw++) {
		    for (int col = 0; col<m_width; col++) {
		    	array[raw*m_width+col] = new Pixel(0, 0 ,0);
		    }
		}		
		// CUT DOWN
		//for (int raw = m_height-1; raw>(m_height-m_height/5); raw--) {
		//    for (int col = 0; col<m_width; col++) {
		//    	array[raw*m_width+col] = 0;
		//    }
		//}
	}
	
	
	//-------------------------------------------------------------
	public void cutImageRight(Pixel[] array) {		
		int xa = 0;
		int ya = m_height;
		int xb = m_width/2;
		int yb = 0;
		
		// CUT DIAGONALS
		for (int raw = 0; raw<m_height; raw++) {
		    for (int col = 0; col<m_width; col++) {		
				int d1 = (xb-xa)*(raw-ya)-(yb-ya)*(col-xa);
				if(d1>0 || col>m_width/2)
					array[raw*m_width+col] = new Pixel(0, 0 ,0);
		    }
		}
	}
	
	
	//-------------------------------------------------------------
	public Pixel[] flip(Pixel[] array) {
		Pixel[] imageArray = array.clone();
	    for(int row=0; row<m_height; row++){
	    	for(int col=0; col<m_width/2; col++) {
		         imageArray[row*m_width+(m_width-col-1)] = array[row*m_width+col];
		         imageArray[row*m_width+col] = array[row*m_width+(m_width-col-1)];
	    	}
	    }
	    return imageArray;
	}
}








