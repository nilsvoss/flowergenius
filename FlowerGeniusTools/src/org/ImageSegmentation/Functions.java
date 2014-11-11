package org.ImageSegmentation;

public class Functions {
	
	public static int red(int color) { return (color >> 16) & 0xFF; }
	public static int green(int color) { return (color >> 8) & 0xFF; }
	public static int blue(int color) { return color & 0xFF; }
	public static int color(int r, int g, int b) { return (r << 16) | (g << 8) | b; }
	public static int x(int i, int width) { return i % width; }
	public static int y(int i, int width) { return i / width; }
	public static int i(int x, int y, int width) { return y*width + x; }
	
	public static double distance(double a1, double a2, double b1, double b2) {
		double one = a1 - b1;
		double two = a2 - b2;
		return Math.sqrt(one * one + two * two);
	}
	
	public static double distance(double a1, double a2, double a3, double b1, double b2, double b3) {
		double one = a1 - b1;
		double two = a2 - b2;
		double three = a3 - b3;
		return Math.sqrt(one * one + two * two + three * three);
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
