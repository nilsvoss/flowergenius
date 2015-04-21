package com.tsc.FlowerGenius.brain;

import java.util.ArrayList;

public class ImageFeatures {

	public static final int BINS_PER_CHANNEL = 4;

	private int[] pixels;

	public ImageFeatures(int[] pixels) {
		this.pixels = pixels;
		histEq();
	}

	private int[][][] getAbsBins() {
		int n = BINS_PER_CHANNEL;
		int[][][] bins = new int[n][n][n];
		for (int i = 0; i < pixels.length; i++) {
			int r = Functions.red(pixels[i]);
			int g = Functions.green(pixels[i]);
			int b = Functions.blue(pixels[i]);
			bins[(int) (n * r / 256.0)][(int) (n * g / 256.0)][(int) (n * b / 256.0)]++;
		}
		return bins;
	}

	private double[][][] getRelBins() {
		int n = BINS_PER_CHANNEL;
		int[][][] absBins = getAbsBins();
		double[][][] relBins = new double[n][n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				for (int k = 0; k < n; k++) {
					relBins[i][j][k] = (double) absBins[i][j][k]
							/ (double) pixels.length;
				}
			}
		}
		return relBins;
	}

	public int getFvSize() {
		return BINS_PER_CHANNEL * BINS_PER_CHANNEL * BINS_PER_CHANNEL;
	}

	public double[] getFeatureVector() {
		int n = BINS_PER_CHANNEL;
		double[][][] relBins = getRelBins();
		double[] fv = new double[n * n * n];
		int c = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				for (int k = 0; k < n; k++) {
					fv[c] = relBins[i][j][k];
					c++;
				}
			}
		}
		return fv;
	}

	/**
	 * Image histogram equalization
	 *
	 * Author: Bostjan Cigan (http://zerocool.is-a-geek.net)
	 */
	private void histEq() {

		int red;
		int green;
		int blue;
		int alpha;
		int newPixel = 0;

		// Get the Lookup table for histogram equalization
		ArrayList<int[]> histLUT = histEqLUT();

		int[] histogramEQ = new int[pixels.length];

		for (int i = 0; i < pixels.length; i++) {

			// Get pixels by R, G, B
			alpha = Functions.alpha(pixels[i]);
			red = Functions.red(pixels[i]);
			green = Functions.green(pixels[i]);
			blue = Functions.blue(pixels[i]);

			// Set new pixel values using the histogram lookup table
			red = histLUT.get(0)[red];
			green = histLUT.get(1)[green];
			blue = histLUT.get(2)[blue];

			// Return back to original format
			newPixel = Functions.color(alpha, red, green, blue);

			// Write pixels into image
			histogramEQ[i] = newPixel;
		}
		pixels = histogramEQ;
	}

	// Get the histogram equalization lookup table for separate R, G, B channels
	private ArrayList<int[]> histEqLUT() {

		// Get an image histogram - calculated values by R, G, B channels
		ArrayList<int[]> imageHist = imHist();

		// Create the lookup table
		ArrayList<int[]> imageLUT = new ArrayList<int[]>();

		// Fill the lookup table
		int[] rhistogram = new int[256];
		int[] ghistogram = new int[256];
		int[] bhistogram = new int[256];

		for (int i = 0; i < rhistogram.length; i++) rhistogram[i] = 0;
		for (int i = 0; i < ghistogram.length; i++) ghistogram[i] = 0;
		for (int i = 0; i < bhistogram.length; i++) bhistogram[i] = 0;

		long sumr = 0;
		long sumg = 0;
		long sumb = 0;

		// Calculate the scale factor
		float scale_factor = (float) (255.0 / pixels.length);

		for (int i = 0; i < rhistogram.length; i++) {
			sumr += imageHist.get(0)[i];
			int valr = (int) (sumr * scale_factor);
			if (valr > 255) rhistogram[i] = 255;
			else rhistogram[i] = valr;

			sumg += imageHist.get(1)[i];
			int valg = (int) (sumg * scale_factor);
			if (valg > 255) {
				ghistogram[i] = 255;
			} else
				ghistogram[i] = valg;

			sumb += imageHist.get(2)[i];
			int valb = (int) (sumb * scale_factor);
			if (valb > 255) {
				bhistogram[i] = 255;
			} else
				bhistogram[i] = valb;
		}

		imageLUT.add(rhistogram);
		imageLUT.add(ghistogram);
		imageLUT.add(bhistogram);

		return imageLUT;
	}

	// Return an ArrayList containing histogram values for separate R, G, B
	// channels
	ArrayList<int[]> imHist() {
		
		int[] rhistogram = new int[256];
		int[] ghistogram = new int[256];
		int[] bhistogram = new int[256];
		
		for (int i = 0; i < rhistogram.length; i++) rhistogram[i] = 0;
		for (int i = 0; i < ghistogram.length; i++) ghistogram[i] = 0;
		for (int i = 0; i < bhistogram.length; i++) bhistogram[i] = 0;
		
		for (int i = 0; i < pixels.length; i++) {
			int red = Functions.red(pixels[i]);
			int green = Functions.green(pixels[i]);
			int blue = Functions.blue(pixels[i]);
			rhistogram[red]++;
			ghistogram[green]++;
			bhistogram[blue]++;
		}
		
		ArrayList<int[]> hist = new ArrayList<int[]>();
		hist.add(rhistogram);
		hist.add(ghistogram);
		hist.add(bhistogram);
		
		return hist;
	}

}
