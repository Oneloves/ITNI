package detection.circle;

import java.awt.Color;
import java.awt.Graphics2D;
import image.Image;

public class CircleRoad {
	public int m_width;
	public int m_height;
	public int type;
	
	public CircleRoad(Image image) {
		m_width = image.m_width;
		m_height = image.m_height;

	    Graphics2D g = image.m_bufferImage.createGraphics();
	    g.setColor(Color.YELLOW);
	    
//LEFT PART 
		// For red circle
		Circle houghCircleLeft = new Circle(image.m_imageArraySobelLeftRed, m_width, m_height);
		houghCircleLeft.process();
		
		// For blue circle
		Circle houghCircleLeft2 = new Circle(image.m_imageArraySobelLeftBlue, m_width, m_height);
		houghCircleLeft2.process(); 
		
		if(houghCircleLeft.max < houghCircleLeft2.max)
			houghCircleLeft = houghCircleLeft2;
		
		
//RIGHT PART    
		// For red circle	
		Circle houghCricleRight = new Circle(image.m_imageArraySobelRightRed, m_width, m_height);
		houghCricleRight.process();
		
		// For blue circle
		Circle houghCricleRight2 = new Circle(image.m_imageArraySobelRightBlue, m_width, m_height);
		houghCricleRight2.process(); 
		
		if(houghCricleRight.max < houghCricleRight2.max)
			houghCricleRight = houghCricleRight2;
		
		
//RESULT
	    
	    g.drawOval(houghCircleLeft.betsX-houghCircleLeft.bestR,  houghCircleLeft.bestY-houghCircleLeft.bestR, 2*houghCircleLeft.bestR, 2*houghCircleLeft.bestR);
	    g.drawOval(m_width-houghCricleRight.betsX-1-houghCricleRight.bestR,  houghCricleRight.bestY-houghCricleRight.bestR, 2*houghCricleRight.bestR, 2*houghCricleRight.bestR);
	}
}
