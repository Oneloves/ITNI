package detection.road;

import java.util.ArrayList;
import java.util.List;

public class HoughRoad {
	int[] input;
	int[] output;
	int width;
	int height;
	int[] acc;
	int accSize=5;
	int[] results;
	int minTheta;
	int maxTheta;
	int threshold;
	
	public List<int[]> line;
	
	
	public HoughRoad(int[] image, int threshold, int width, int height, int accSize, int minTheta, int maxTheta) {
		this.minTheta = minTheta;
		this.maxTheta = maxTheta;
		this.width = width;
		this.height = height;
		this.input = image;
		this.accSize = accSize;
		this.threshold = threshold;
		
		line = new ArrayList<>();
		output = new int[height*width];
	}
	

	public int[] process() {
		int rmax = (int)Math.sqrt(width*width + height*height);
		acc = new int[rmax*180];
		int r;
			
		for(int x=0;x<width/2;x++) {
			for(int y=height/3;y<height;y=y+2) {
				if(input[y*width+x]== 255) {			
					for (int theta=minTheta; theta<maxTheta; theta++) {
						r = (int)(x*Math.cos(((theta)*Math.PI)/180) + y*Math.sin(((theta)*Math.PI)/180));
						if ((r > 0) && (r <= rmax))
							acc[r*180+theta] = acc[r*180+theta] + 1;
					}
				}
			}
		}
	
		int max=0;
		for(r=0; r<rmax; r++) {
			for(int theta=minTheta; theta<maxTheta; theta++) {
				if (acc[r*180+theta] < threshold) {
					acc[r*180+theta] = 0;
				}
				if (acc[r*180+theta] > max) {
					max = acc[r*180+theta];
				}
			}
		}
		
		int value;
		for(r=0; r<rmax; r++) {
			for(int theta=minTheta; theta<maxTheta; theta++) {
				value = (int)(((double)acc[r*180+theta]/(double)max)*255.0);
				acc[r*180+theta] = 0xff000000 | (value << 16 | value << 8 | value);
			}
		}
		
		findMaxima();
		return output;
	}
	
	private int[] findMaxima() {
		int rmax = (int)Math.sqrt(width*width + height*height);
		results = new int[accSize*3];
		int[] output = new int[width*height];

		List<Integer> list_r = new ArrayList<>();
		List<Integer> list_t = new ArrayList<>();
		
		for(int r=0; r<rmax; r++) {
			for(int theta=minTheta; theta<maxTheta; theta++) {
				int value = (acc[r*180+theta] & 0xff);
				if (value > results[(accSize-1)*3]) {
					results[(accSize-1)*3] = value;
					results[(accSize-1)*3+1] = r;
					results[(accSize-1)*3+2] = theta;
					list_r.add(r);
					list_t.add(theta);
					int i = (accSize-2)*3;
					while ((i >= 0) && (results[i+3] > results[i])) {
						for(int j=0; j<3; j++) {
							int temp = results[i+j];
							results[i+j] = results[i+3+j];
							results[i+3+j] = temp;
						}
						i = i - 3;
						if (i < 0) break;
					}
				}
			}
		}
		
		
		for(int i=accSize-1; i>=0; i--) {
			if(results[i*3]>0) {
				drawPolarLine(results[i*3], results[i*3+1], results[i*3+2]);
				line.add(new int[]{results[i*3], results[i*3+1], results[i*3+2],});
			}
		}
		
		return output;
	}

	private void drawPolarLine(int value, int r, int theta) {
		for(int x=0;x<width/2;x++) {	
			for(int y=height/2;y<height;y++) {
				double temp = x*Math.cos(((theta)*Math.PI)/180) + y*Math.sin(((theta)*Math.PI)/180);
				if(temp - (double)r >= -1.0 && temp - (double)r <= 1.0)
					output[y*width+x]++;
				
			}
		}
	}

}