package detection.hough;


public class Circle {

	public int[] input;
	public int width;
	public int height;
	public int[][][] acc;


	public int max=0;
	public int bestR=0;
	public int betsX=0;
	public int bestY=0;
	
	public Circle(int[] image, int width, int height) {
		this.width = width;
		this.height = height;
		this.input = image;
	}


	// hough transform for lines (polar), returns the accumulator array
	public void process() {
		int rmax = 300;
		acc = new int[width][height][rmax];
		
		int x0, y0;
		for(int x=0;x<width;x++) {
			for(int y=0;y<height;y++) {
				if(input[y*width+x] == 255) {
					for(int ri=10; ri<100; ri++)
						for (int theta=0; theta<360; theta++) {
							x0 = (int)Math.round(x - ri * Math.cos(theta*Math.PI/180));
							y0 = (int)Math.round(y - ri * Math.sin(theta*Math.PI/180));
							if(x0 < width && x0 > 0 && y0 < height && y0 > 0) {
								acc[x0][y0][ri] += 1;
							}
						}
				}
			}
		}
	
		
		for(int x=0;x<width;x++) {
			for(int y=0;y<height;y++) {
				for(int ri=10; ri<100; ri++) {
					if (acc[x][y][ri] > max) {
						max = acc[x][y][ri];
						bestR = ri;
						betsX = x;
						bestY = y;
					}
				}
			}
		}
	}
	
}