package tool;

public class ColorSelection {

	
	int m_height;
	int m_width;
	Pixel[] m_pixels;
	
	
	public ColorSelection(Pixel[] pixels, int width, int height) {
		m_height = height;
		m_width  = width;
		m_pixels = pixels;
	}
	
	
	public int[] select(float minH, float maxH, float minS, float maxS, float minV, float maxV) {
		int[] imageArray = new int[m_width * m_height];
        for (int raw=0; raw<m_height; raw++) {
            for (int col=0; col<m_width; col++) {
            	Pixel pixel = m_pixels[raw*m_width+col];          	
            	if(pixel.m_h>=minH && pixel.m_h<=maxH) {
            		if(pixel.m_s>=minS && pixel.m_s<=maxS)
                		if(pixel.m_v>=minV && pixel.m_v<=maxV)
                			imageArray[raw*m_width+col] = 255;
            	}
                else
                	imageArray[raw*m_width+col] = 0;
            }
        }
        return imageArray;
	}
	
	
	public int[] selectRed() {		
		float minH1=0;
		float maxH1=13;

		float minH2=340;
		float maxH2=360;
		
		float minS=20;
		float maxS=100;
		float minV=20;
		float maxV=100;
		
		
		int[] imageArray = new int[m_width * m_height];
        for(int raw=0; raw<m_height; raw++) {
            for(int col=0; col<m_width; col++) {
            	Pixel pixel = m_pixels[raw*m_width+col];          	
            	if((pixel.m_h>=minH1 && pixel.m_h<=maxH1) || (pixel.m_h>=minH2 && pixel.m_h<=maxH2)) {
            		if(pixel.m_s>=minS && pixel.m_s<=maxS)
                		if(pixel.m_v>=minV && pixel.m_v<=maxV)
                			imageArray[raw*m_width+col] = 255;
            	}
                else
                	imageArray[raw*m_width+col] = 0;
            }
        }
        return imageArray;
	}
}
