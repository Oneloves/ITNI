package tool;

import java.awt.Color;

public class Pixel {

	public int m_r;
	public int m_g;
	public int m_b;
	
	public float m_h;
	public float m_s;
	public float m_v;
	
	
	public Pixel(int r, int g, int b) {
		m_r = r;
		m_g = g;
		m_b = b;
		toHSV();
	}
	
	public void toHSV() {
		float[] hsvVals = new float[3];
		Color.RGBtoHSB(m_r, m_g, m_b, hsvVals);
		m_h = hsvVals[0] * 360;
		m_s = hsvVals[1] * 100;
		m_v = hsvVals[2] * 100;
	}
	
}
