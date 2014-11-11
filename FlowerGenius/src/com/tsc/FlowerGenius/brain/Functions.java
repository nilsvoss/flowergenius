package com.tsc.FlowerGenius.brain;

public class Functions {
	
	public static int red(int color) { return (color >> 16) & 0xFF; }
	public static int green(int color) { return (color >> 8) & 0xFF; }
	public static int blue(int color) { return color & 0xFF; }
	public static int color(int r, int g, int b) { return (r << 16) | (g << 8) | b; }
	public static int x(int i, int width) { return i % width; }
	public static int y(int i, int width) { return i / width; }
	public static int i(int x, int y, int width) { return y*width + x; }
	
	public static double distance(double[] a, double[] b) {
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
	
	public static int[] concat(int[] a, int[] b) {
		int aLen = a.length;
		int bLen = b.length;
		int[] c = new int[aLen+bLen];
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(b, 0, c, aLen, bLen);
		return c;
	}

}
