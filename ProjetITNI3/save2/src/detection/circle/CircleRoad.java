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
		
//LEFT PART 
		Circle houghCircleLeft = new Circle(image.m_imageArrayColorSelectionLeftRed, m_width, m_height);
		houghCircleLeft.process();	
		
//RIGHT PART    	
		Circle houghCricleRight = new Circle(image.m_imageArrayColorSelectionRightRed, m_width, m_height);
		houghCricleRight.process();
		
//RESULT
	    Graphics2D g = image.m_bufferImage.createGraphics();
	    
	    g.setColor(Color.YELLOW);
	    g.drawOval(houghCircleLeft.betsX-houghCircleLeft.bestR,  houghCircleLeft.bestY-houghCircleLeft.bestR, 2*houghCircleLeft.bestR, 2*houghCircleLeft.bestR);
	    g.drawOval(m_width-houghCricleRight.betsX-1-houghCricleRight.bestR,  houghCricleRight.bestY-houghCricleRight.bestR, 2*houghCricleRight.bestR, 2*houghCricleRight.bestR);
	}
}
