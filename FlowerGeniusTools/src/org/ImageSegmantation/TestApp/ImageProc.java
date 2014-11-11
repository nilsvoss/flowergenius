package org.ImageSegmantation.TestApp;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class ImageProc {
	
	private int width;
	private int height;
	private int[] pixels;
	private double maxCenterDistance;

	public ImageProc(int width, int height, int[] pixels) {
		this.width = width;
		this.height = height;
		this.pixels = pixels;
		this.maxCenterDistance = distance(new int[]{width/2, height/2}, new int[]{0, 0});
	}
	
	public static int red(int color) { return (color >> 16) & 0xFF; }
	public static int green(int color) { return (color >> 8) & 0xFF; }
	public static int blue(int color) { return color & 0xFF; }
	public static int color(int r, int g, int b) { return (r << 16) | (g << 8) | b; }
	
	public static double distance(int[] a, int[] b) {
		if (a.length != b.length) {
			return 0;
		} else {
			double sum = 0;
			for (int i=0; i<a.length; i++) {
				sum += Math.pow(a[i] - b[i], 2);
			}
			return Math.sqrt(sum);
		}
	}
	
	public int[][][] colorCount(int binsPerChannel, int x1, int y1, int x2, int y2) {
		int n = binsPerChannel;
		int[][][] count = new int[n][n][n];
		for (int i=y1; i<=y2; i++) {
			for (int j=x1; j<=x2; j++) {
				int p = i*width + j;
				int r = red(pixels[p]);
				int g = green(pixels[p]);
				int b = blue(pixels[p]);
				count[(int)(n*r/256.0)][(int)(n*g/256.0)][(int)(n*b/256.0)]++;
				//System.out.print(pixels[p] + " ");
			}
			//System.out.println();
		}
		//System.out.println();
		return count;
	}
	
	public double[][][] colorDist(int binsPerChannel, int x1, int y1, int x2, int y2) {
		int[][][] count = colorCount(binsPerChannel, x1, y1, x2, y2);
		return colorDist(count, (x2 - x1 + 1) * (y2 - y1 + 1));
	}
	
	public double[][][] colorDist(int[][][] count, int pixelCount) {
		System.out.println("colorDist(): start");
		int n = count.length;
		double sum = 0;
		double[][][] dist = new double[n][n][n];
		for (int i=0; i<n; i++) {
			for (int j=0; j<n; j++) {
				for (int k=0; k<n; k++) {
					dist[i][j][k] = ((double) count[i][j][k]) / ((double) pixelCount);
					sum += dist[i][j][k]; 
					System.out.println("colorDist(): "+i+" "+j+" "+k+" = "+dist[i][j][k]);
				}
			}
		}
		System.out.println("colorDist(): sum="+sum);
		return dist;
	}
	
	public int x(int i) { return i % width; }
	public int y(int i) { return i / width; }
	public int i(int x, int y) { return y*width + x; }
	
	public double[][][] bgDist(int binsPerChannel, int top, int right, int bottom, int left) {
		int n = binsPerChannel;
		int[][][] topCount = colorCount(n, 0, 0, width-1, top-1);
		int[][][] rightCount = colorCount(n, width-right, top, width-1, height-bottom-1);
		int[][][] bottomCount = colorCount(n, 0, height-bottom, width-1, height-1);
		int[][][] leftCount = colorCount(n, 0, top, left-1, height-bottom-1);
		int[][][] count = new int[n][n][n];
		for (int i=0; i<n; i++) {
			for (int j=0; j<n; j++) {
				for (int k=0; k<n; k++) {
					count[i][j][k] = topCount[i][j][k] + rightCount[i][j][k] + bottomCount[i][j][k] + leftCount[i][j][k];
				}
			}
		}
		return colorDist(count, width*(top+bottom) + (height-top-bottom)*(left+right));
	}
	
	public double[][][] fgDist(int binsPerChannel, int centerX, int centerY, int sizeX, int sizeY) {
		int[][][] count = colorCount(binsPerChannel, centerX - sizeX/2, centerY - sizeY/2, centerX + sizeX/2, centerY + sizeY/2);
		double[][][] dist = colorDist(count, sizeX * sizeY);
		return dist;
	}
	
	public double[][][] maskDist(int binsPerChannel, int[] mask, int maskValue) {
		int n = binsPerChannel;
		int[][][] count = new int[n][n][n];
		int valCount = 0;
		for (int i=0; i<pixels.length; i++) {
			if (mask[i] == maskValue) {
				valCount++;
				int r = red(pixels[i]);
				int g = green(pixels[i]);
				int b = blue(pixels[i]);
				count[(int)(n*r/256.0)][(int)(n*g/256.0)][(int)(n*b/256.0)]++;
			}
		}
		return colorDist(count, valCount);
	}
	
	private int[] distributionMask(int binsPerChannel, double[][][] fgDist, double[][][] bgDist) {
		return distributionMask(binsPerChannel, fgDist, bgDist, 0.5);
	}
	
	private int[] distributionMask(int binsPerChannel, double[][][] fgDist, double[][][] bgDist, double threshold) {
		int[] mask = new int[width * height];
		for (int i=0; i<mask.length; i++) {
			int r = (int) (binsPerChannel * red(pixels[i]) / 256.0);
			int g = (int) (binsPerChannel * green(pixels[i]) / 256.0);
			int b = (int) (binsPerChannel * blue(pixels[i]) / 256.0);
			double fg = fgDist[r][g][b] / (fgDist[r][g][b] + bgDist[r][g][b]);
			mask[i] = fg > threshold ? 1 : 0;
		}
		return mask;
	}
	
	private double neighborhoodClasses(int i, int winSize, int[] mask) {
		int x = x(i);
		int y = y(i);
		if (x <= winSize/2 || y <= winSize/2 || x >= width-winSize/2-1 || y >= height-winSize/2-1) {
			return 0;
		} else {
			int sum = 0;
			int cnt = 0;
			for (int wx=-winSize/2; wx<=winSize/2; wx++) {
				for (int wy=-winSize/2; wy<=winSize/2; wy++) {
					sum += mask[i(x + wx, y + wy)];
					cnt++;
				}
			}
			return (double) sum / (double) cnt;
		}
	}
	
	private double neighborhoodGradients(int i, int winSize) {
		int x = x(i);
		int y = y(i);
		if (x <= winSize/2 || y <= winSize/2 || x >= width-winSize/2-1 || y >= height-winSize/2-1) {
			return 0;
		} else {
			double sum = 0;
			int cnt = 0;
			for (int wx=-winSize/2; wx<=winSize/2; wx++) {
				for (int wy=-winSize/2; wy<=winSize/2; wy++) {
					int r = Math.abs(red(pixels[i]) - red(pixels[i(x + wx, y + wy)]));
					int g = Math.abs(green(pixels[i]) - green(pixels[i(x + wx, y + wy)]));
					int b = Math.abs(blue(pixels[i]) - blue(pixels[i(x + wx, y + wy)]));
					sum += (r / 255.0) / 3.0 + (g / 255.0) / 3.0 + (b / 255.0) / 3.0; 
					cnt++;
				}
			}
			return 1.0 - sum / (double) cnt;
		}
	}
	
	private int[] weightedMask(int binsPerChannel, double[][][] fgDist, double[][][] bgDist, int centerX, int centerY, int[] mask) {
		int[] newMask = new int[width * height];
		double w1 = 1 / 4.0;
		double w2 = 1 / 4.0;
		double w3 = 1 / 4.0;
		double w4 = 1 / 4.0;
		for (int i=0; i<mask.length; i++) {
			int r = (int) (binsPerChannel * red(pixels[i]) / 256.0);
			int g = (int) (binsPerChannel * green(pixels[i]) / 256.0);
			int b = (int) (binsPerChannel * blue(pixels[i]) / 256.0);
			double nc = neighborhoodClasses(i, 10, mask);
			double ng = neighborhoodGradients(i, 10);
			double fg = fgDist[r][g][b] / (fgDist[r][g][b] + bgDist[r][g][b]);
			double ce = 1 - (distance(new int[]{x(i), y(i)}, new int[]{centerX, centerY}) / maxCenterDistance);
			System.out.print("fg="+fg+"   ");
			System.out.print("nb="+nc+"   ");
			System.out.print("ng="+ng+"   ");
			System.out.println("ce="+ce);
			newMask[i] = w1 * fg + w2 * ce + w3 * nc + w4 * ng > 0.5 ? 1 : 0;
		}
		return newMask;
	}
	
	public int[] segmentation1(int binsPerChannel, double borderLR, double borderTB, double centerX, double centerY, double radius) {
		int dCenterX = (int) (centerX * width);
		int dCenterY = (int) (centerY * height);
		int dRadius = (int) (2 * radius * Math.min(width, height));
		int dBorderLR = (int) (borderLR * width);
		int dBorderTB = (int) (borderTB * height);
		double[][][] fgDist = fgDist(binsPerChannel, dCenterX, dCenterY, dRadius, dRadius);
		double[][][] bgDist = bgDist(binsPerChannel, dBorderTB, dBorderLR, dBorderTB, dBorderLR);
		int[] mask = distributionMask(binsPerChannel, fgDist, bgDist);
		return mask;
	}
	
	public int[] segmentation2(int binsPerChannel, double borderLR, double borderTB, int[] mask, double centerX, double centerY, double radius) {
		int dCenterX = (int) (centerX * width);
		int dCenterY = (int) (centerY * height);
		int dRadius = (int) (2 * radius * Math.min(width, height));
		int dBorderLR = (int) (borderLR * width);
		int dBorderTB = (int) (borderTB * height);
		double[][][] fgDist = fgDist(binsPerChannel, dCenterX, dCenterY, dRadius, dRadius);
		double[][][] bgDist = bgDist(binsPerChannel, dBorderTB, dBorderLR, dBorderTB, dBorderLR);
		int[] newMask = weightedMask(binsPerChannel, fgDist, bgDist, dCenterX, dCenterY, mask);
		return newMask;
	}
	
	public static void main(String[] args) {
		System.out.println("Hallo");
		int color = color(255,0,128);
		System.out.println("" + red(color));
		System.out.println("" + green(color));
		System.out.println("" + blue(color));
//		ImageProc im = new ImageProc(3,3,new int[]{1,2,3,4,5,6,7,8,9});
		BufferedImage img = null;
        try {
            img = ImageIO.read(new File("256.jpg"));
        } catch (IOException e) {}
        int[] pixels = img.getRGB(0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth());
        ImageProc im = new ImageProc(img.getWidth(),img.getHeight(),pixels);
		int n = 2;
		double[][][] r = im.bgDist(n, 10, 10, 10, 10);
		for (int i=0; i<n; i++) {
			for (int j=0; j<n; j++) {
				for (int k=0; k<n; k++) {
					System.out.print(r[i][j][k] + " ");
				}
				System.out.println();
			}
			System.out.println();
		}
	}
	
	


}
