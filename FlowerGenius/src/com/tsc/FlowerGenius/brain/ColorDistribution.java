package com.tsc.FlowerGenius.brain;

public class ColorDistribution {
	
	public static final int BINS_PER_CHANNEL = 7;
	
	public int[][][] getAbsBins(int binsPerChannel, int[] pixels) {
		int n = binsPerChannel;
		int[][][] bins = new int[n][n][n];
		for (int i=0; i<pixels.length; i++) {
			int r = Functions.red(pixels[i]);
			int g = Functions.green(pixels[i]);
			int b = Functions.blue(pixels[i]);
			bins[(int)(n*r/256.0)][(int)(n*g/256.0)][(int)(n*b/256.0)]++;
		}
		return bins;
	}
	
	public double[][][] getRelBins(int binsPerChannel, int[] pixels) {
		int n = binsPerChannel;
		int[][][] absBins = getAbsBins(n, pixels);
		double[][][] relBins = new double[n][n][n];
		for (int i=0; i<n; i++) {
			for (int j=0; j<n; j++) {
				for (int k=0; k<n; k++) {
					relBins[i][j][k] = (double) absBins[i][j][k] / (double) pixels.length;
				}
			}
		}
		return relBins;
	}
	
	public int getFvSize() {
		return BINS_PER_CHANNEL*BINS_PER_CHANNEL*BINS_PER_CHANNEL;
	}
	
	public double[] getFeatureVector(int[] pixels) {
		int n = BINS_PER_CHANNEL;
		double[][][] relBins = getRelBins(n, pixels);
		double[] fv = new double[n*n*n];
		int c = 0;
		for (int i=0; i<n; i++) {
			for (int j=0; j<n; j++) {
				for (int k=0; k<n; k++) {
					fv[c] = relBins[i][j][k];
					c++;
				}
			}
		}
		return fv;
	}

	

}
