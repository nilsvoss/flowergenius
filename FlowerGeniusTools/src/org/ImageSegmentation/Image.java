package org.ImageSegmentation;

import java.util.LinkedList;
import java.util.List;

import org.ImageSegmentation.GMM.Components;

public class Image {
	
	private int[] pixels;
	private double[] mask;
	private int width;
	private int height;
	private int top;
	private int right;
	private int bottom;
	private int left;
	private int centerX;
	private int centerY;
	private int radiusV;
	private int radiusH;
	private Components fgComps;
	private Components bgComps;
	
	public Image(int[] pixels, int width, int height, int top, int right, int bottom, int left, int centerX, int centerY, int radiusV, int radiusH, int K) {
		this.pixels = pixels;
		this.width = width;
		this.height = height;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.left = left;
		this.centerX = centerX;
		this.centerY = centerY;
		this.radiusV = radiusV;
		this.radiusH = radiusH;
		this.fgComps = fgComps(K);
		this.bgComps = bgComps(K);
		mask = new double[pixels.length];
		seg();
	}
	
	public double[] getMask() {
		return mask;
	}
	
	private void maskAdd(double value, int x1, int y1, int x2, int y2) {
		for (int y=y1; y<=y2; y++) {
			for (int x=x1; x<=x2; x++) {
				mask[Functions.i(x, y, width)] += value;
			}
		}
	}
	
	private double[] windowMean(int x1, int y1, int x2, int y2) {
		int sumR = 0, sumG = 0, sumB = 0, cnt = 0;
		for (int y=y1; y<=y2; y++) {
			for (int x=x1; x<=x2; x++) {
				if (x >= 0 && y >= 0 && x <= width - 1 && y <= height - 1) {
					sumR += Functions.red(pixels[Functions.i(x, y, width)]);
					sumG += Functions.green(pixels[Functions.i(x, y, width)]);
					sumB += Functions.blue(pixels[Functions.i(x, y, width)]);
					cnt++;
				}
			}
		}
		double[] m = new double[3];
		m[0] = (double) sumR / (double) cnt;
		m[1] = (double) sumG / (double) cnt;
		m[2] = (double) sumB / (double) cnt;
		return m;
	}
	
	private int[] collectPixels(int x1, int y1, int x2, int y2) {
		int[] bag = new int[(x2 - x1 + 1) * (y2 - y1 + 1)];
		int pos = 0;
		for (int y=y1; y<=y2; y++) {
			for (int x=x1; x<=x2; x++) {
				bag[pos] = pixels[Functions.i(x, y, width)];
				pos++;
			}
		}
		return bag;
	}
	
	private Components bgComps(int K) {
		int[] bag = collectPixels(0, 0, width-1, top-1);
		bag = Functions.concat(bag, collectPixels(width-right, top, width-1, height-bottom-1));
		bag = Functions.concat(bag, collectPixels(0, height-bottom, width-1, height-1));
		bag = Functions.concat(bag, collectPixels(0, top, left-1, height-bottom-1));
		return new Components(bag, K);
	}
	
	private Components fgComps(int K) {
		int[] bag = collectPixels(centerX-radiusH, centerY-radiusV, centerX+radiusH, centerY+radiusV);
		return new Components(bag, K);
	}
	
	public void resetMask() {
		for (int i=0; i<mask.length; i++) {
			mask[i] = 0;
		}
	}
	
	public void seg() {
		resetMask();
		// Distance of pixel to color components
		for (int i=0; i<pixels.length; i++) {
			mask[i] -= fgComps.minDistance(pixels[i]);
			mask[i] += 6*bgComps.minDistance(pixels[i]);
		}
		// Distance of mean color to color components
		for (int i=0; i<pixels.length; i++) {
			int size = 10;
			int x1 = Functions.x(i, width) - size/2;
			int y1 = Functions.y(i, width) - size/2;
			int x2 = x1 + size;
			int y2 = y1 + size;
			double[] mean = windowMean(x1, y1, x2, y2);
			int iMean = Functions.color((int) mean[0], (int) mean[1], (int) mean[2]);
			mask[i] -= fgComps.minDistance(iMean);
			mask[i] += 6*bgComps.minDistance(iMean);
		}
		for (int i=0; i<pixels.length; i++) {
			int size = 20;
			int x1 = Functions.x(i, width) - size/2;
			int y1 = Functions.y(i, width) - size/2;
			int x2 = x1 + size;
			int y2 = y1 + size;
			double[] mean = windowMean(x1, y1, x2, y2);
			int iMean = Functions.color((int) mean[0], (int) mean[1], (int) mean[2]);
			mask[i] -= fgComps.minDistance(iMean);
			mask[i] += 6*bgComps.minDistance(iMean);
		}
		// Neighborhood
		for (int k=0; k<5; k++) {
			double[] newMask = mask.clone();
			for (int i=width+1; i<pixels.length-width-1; i++) {
				newMask[i] += mask[i-width-1];
				newMask[i] += mask[i-width];
				newMask[i] += mask[i-width+1];
				newMask[i] += mask[i+1];
				newMask[i] += mask[i+width+1];
				newMask[i] += mask[i+width];
				newMask[i] += mask[i+width-1];
				newMask[i] += mask[i-1];
			}
			mask = newMask; 
		}
		// Distance from click point
		for (int i=0; i<pixels.length; i++) {
			double distance = Functions.distance(centerX, centerY, Functions.x(i, width), Functions.y(i, width));
			mask[i] -= 10 * distance;
		}
	}
	
	public void improve(int K) {
		List<Integer> fgPixelsList = new LinkedList<Integer>();
		for (int i=0; i<pixels.length; i++) {
			if (mask[i] > 0) {
				fgPixelsList.add(pixels[i]);
			}
		}
		int[] fgPixels = new int[fgPixelsList.size()];
		for (int i=0; i<fgPixelsList.size(); i++) {
			fgPixels[i] = fgPixelsList.get(i);
		}
		Components fg = new Components(fgPixels, K);
		seg();
	}

}
