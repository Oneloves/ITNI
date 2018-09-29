package detection.rec;

import java.util.ArrayList;
import java.util.List;

public class HoughRec  {
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
	int rowJitter;
	int colJitter;
	
	public List<int[]> line;
	
	
	public HoughRec(int[] image, int threshold, int width, int height, int accSize, int minTheta, int maxTheta, int rowJitter, int colJitter) {
		this.minTheta = minTheta;
		this.maxTheta = maxTheta;
		this.width = width;
		this.height = height;
		this.input = image;
		this.accSize = accSize;
		this.threshold = threshold;
		this.rowJitter = rowJitter;
		this.colJitter = colJitter;
		
		line = new ArrayList<>();
		output = new int[height*width];
	}
	

	public void processForRec() {
		int rmax = (int)Math.sqrt(width*width + height*height);
		acc = new int[rmax*180];
		int r;
			
		for(int x=0;x<width/2;x=x+rowJitter) {
			for(int y=0;y<height;y=y+colJitter) {
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
		
		findMaximaForRec();
	}
	

	public int[] output1;
	public int[] output2;
	
	
	private void findMaximaForRec() {
		int rmax = (int)Math.sqrt(width*width + height*height);
		int[] firstResult = new int[1*3];
		int[] secondResult = new int[1*3];

		for(int r=0; r<rmax; r++) {
			for(int theta=minTheta; theta<maxTheta; theta++) {
				int value = (acc[r*180+theta] & 0xff);
				if (value > firstResult[(1-1)*3]) {
					firstResult[0] = value;
					firstResult[1] = r;
					firstResult[2] = theta;
					int i = (1-2)*3;
					while ((i >= 0) && (firstResult[i+3] > firstResult[i])) {
						for(int j=0; j<3; j++) {
							int temp = firstResult[i+j];
							firstResult[i+j] = firstResult[i+3+j];
							firstResult[i+3+j] = temp;
						}
						i = i - 3;
						if (i < 0) break;
					}
				}
			}
		}
		
		for(int r=0; r<rmax; r++) {
				if((firstResult[1]-20>0 && r<firstResult[1]-20) || (r>firstResult[1]+20 && firstResult[1]+20<rmax)) {
					for(int theta=minTheta; theta<maxTheta; theta++) {
						int value = (acc[r*180+theta] & 0xff);
						if (value > secondResult[(1-1)*3]) {
							secondResult[(1-1)*3] = value;
							secondResult[(1-1)*3+1] = r;
							secondResult[(1-1)*3+2] = theta;
							int i = (1-2)*3;
							while ((i >= 0) && (secondResult[i+3] > secondResult[i])) {
								for(int j=0; j<3; j++) {
									int temp = secondResult[i+j];
									secondResult[i+j] = secondResult[i+3+j];
									secondResult[i+3+j] = temp;
								}
								i = i - 3;
								if (i < 0) break;
							}
						}
					}
				}
		}
		
		results = new int[2*3];
		results[0] = firstResult[0];
		results[1] = firstResult[1];
		results[2] = firstResult[2];
		results[3] = secondResult[0];
		results[4] = secondResult[1];
		results[5] = secondResult[2];

		output1 = new int[height*width];
		output2 = new int[height*width];
		
		if(results[0]>0) {
			output1 = drawPolarLineRec(results[0], results[1], results[2]);
			line.add(new int[]{results[0], results[1], results[2],});
		}
		if(results[3]>0) {
			output2 = drawPolarLineRec(results[3], results[4], results[5]);
			line.add(new int[]{results[3], results[4], results[5],});
		}
	}
	
	private int[] drawPolarLineRec(int value, int r, int theta) {
		int[] array = new int[height*width];
		for(int x=0;x<width;x++) {	
			for(int y=0;y<height;y++) {
				double temp = x*Math.cos(((theta)*Math.PI)/180) + y*Math.sin(((theta)*Math.PI)/180);
				if(temp - (double)r >= -1.0 && temp - (double)r <= 1.0)
					array[y*width+x]++;				
			}
		}
		return array;
	}

}