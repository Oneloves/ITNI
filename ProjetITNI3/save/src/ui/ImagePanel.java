package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import javax.swing.JPanel;

public class ImagePanel extends JPanel implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	public BufferedImage image;
	public String tag = "";
	
	
	public ImagePanel(String tag) {
        this.image = null;
        this.tag = tag;
    }
	
    
    public void setImage(BufferedImage image){
        this.image = image;
        repaint();
    }
    

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            int height = this.getSize().height;
            int width = this.getSize().width;
            
            g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
            g.drawImage(image,0,0, width, height, this);
            g.setColor(Color.red);  
            g.drawString(tag, 10, 20);
        }
    }
    
}

