package tool;

import image.Image;

public class ColorSelection {
	
	public int m_height;
	public int m_width;
	public Image m_image;
	
	public ColorSelection(Image image, int width, int height) {
		m_height = height;
		m_width  = width;
		m_image = image;
	}
	
	public void select() {		
		// Red HSV
		float r_minH1=0;
		float r_maxH1=13;
		float r_minH2=340;
		float r_maxH2=360;
		float r_minS=35;
		float r_maxS=100;
		float r_minV=20;
		float r_maxV=100;

		// Blue HSV
		float b_minH = 185;
		float b_maxH = 290; 
		float b_minS = 40;
		float b_maxS = 100;
		float b_minV = 40;
		float b_maxV = 100;
		
		// White HSV
		float w_minH = 0;
		float w_maxH = 360; 
		float w_minS = 0;
		float w_maxS = 5;
		float w_minV = 90;
		float w_maxV = 100;
        
        for(int raw=0; raw<m_height; raw++) {
            for(int col=0; col<m_width/2; col++) {            	
            	// Middle left and right of the road
            	Pixel pixelLeftCut = m_image.m_imageArrayLeft[raw*m_width+col];
            	Pixel pixelRightCut = m_image.m_imageArrayRight[raw*m_width+col];
            	Pixel pixelMiddleLeftCut = m_image.m_imageArrayMiddleLeft[raw*m_width+col];
            	Pixel pixelMiddleRightCut = m_image.m_imageArrayMiddleRight[raw*m_width+col];
            	
            	// Red selection            	
            	if((pixelLeftCut.m_h>=r_minH1 && pixelLeftCut.m_h<=r_maxH1) || (pixelLeftCut.m_h>=r_minH2 && pixelLeftCut.m_h<=r_maxH2)) {
            		if(pixelLeftCut.m_s>=r_minS && pixelLeftCut.m_s<=r_maxS)
        				if(pixelLeftCut.m_v>=r_minV && pixelLeftCut.m_v<=r_maxV)
        					m_image.m_imageArrayColorSelectionLeftRed[raw*m_width+col] = 255;
            	}         		
            	
            	// Red selection
            	if((pixelRightCut.m_h>=r_minH1 && pixelRightCut.m_h<=r_maxH1) || (pixelRightCut.m_h>=r_minH2 && pixelRightCut.m_h<=r_maxH2)) {
            	   if(pixelRightCut.m_s>=r_minS && pixelRightCut.m_s<=r_maxS)
            		   if(pixelRightCut.m_v>=r_minV && pixelRightCut.m_v<=r_maxV)
            			   m_image.m_imageArrayColorSelectionRightRed[raw*m_width+col] = 255;
            	}          
            		           
            	// Withe selection         	
            	if(pixelMiddleLeftCut.m_h>=w_minH && pixelMiddleLeftCut.m_h<=w_maxH && pixelMiddleLeftCut.m_s>=w_minS && pixelMiddleLeftCut.m_s<=w_maxS && pixelMiddleLeftCut.m_v>=w_minV && pixelMiddleLeftCut.m_v<=w_maxV)
            		m_image.m_imageArrayColorSelectionMiddleLeftWithe[raw*m_width+col] = 255;    
            	// Withe selection         	
            	if(pixelMiddleRightCut.m_h>=w_minH && pixelMiddleRightCut.m_h<=w_maxH && pixelMiddleRightCut.m_s>=w_minS && pixelMiddleRightCut.m_s<=w_maxS && pixelMiddleRightCut.m_v>=w_minV && pixelMiddleRightCut.m_v<=w_maxV)
            		m_image.m_imageArrayColorSelectionMiddleRightWithe[raw*m_width+col] = 255;
            	
            	
            	// Blue selection         	
            	if(pixelLeftCut.m_h>=b_minH && pixelLeftCut.m_h<=b_maxH && pixelLeftCut.m_s>=b_minS && pixelLeftCut.m_s<=b_maxS && pixelLeftCut.m_v>=b_minV && pixelLeftCut.m_v<=b_maxV)
            		m_image.m_imageArrayColorSelectionLeftBlue[raw*m_width+col] = 255;            	
            	// Blue selection         	
            	if(pixelRightCut.m_h>=b_minH && pixelRightCut.m_h<=b_maxH && pixelRightCut.m_s>=b_minS && pixelRightCut.m_s<=b_maxS && pixelRightCut.m_v>=b_minV && pixelRightCut.m_v<=b_maxV)
            		m_image.m_imageArrayColorSelectionRightBlue[raw*m_width+col] = 255;        	
            }
        }
        
	}
}
