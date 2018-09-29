package tool;

import java.awt.Color;

public class Pixel {

	public int m_r;
	public int m_g;
	public int m_b;
	
	public int m_gray;
	
	public float m_h;
	public float m_s;
	public float m_v;
	
	public Pixel() {
		m_r = 0;
		m_g = 0;
		m_b = 0;
		m_h = 0;
		m_s = 0;
		m_v = 0;
	}
	
	public Pixel(int r, int g, int b) {
		m_r = r;
		m_g = g;
		m_b = b;
		toHSV();
		m_gray = ((m_r+m_g+m_b)/3);
	}
	
	public void toHSV() {
		float[] hsvVals = new float[3];
		Color.RGBtoHSB(m_r, m_g, m_b, hsvVals);
		m_h = hsvVals[0] * 360;
		m_s = hsvVals[1] * 100;
		m_v = hsvVals[2] * 100;
	}
	
}
