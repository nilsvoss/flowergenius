package org.ImageSegmantation.TestApp;

import java.util.LinkedList;
import java.util.List;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

public class GrabCut {
	
	private int[] rgb;
	private int width;
	private int height;
	
	GrabCut(int width, int height, int[] rgb) {
		this.width = width;
		this.height = height;
		this.rgb = rgb;
	}
	
	public static int red(int color) { return (color >> 16) & 0xFF; }
	public static int green(int color) { return (color >> 8) & 0xFF; }
	public static int blue(int color) { return color & 0xFF; }
	public static int color(int r, int g, int b) { return (r << 16) | (g << 8) | b; }
	public int x(int i) { return i % width; }
	public int y(int i) { return i / width; }
	public int i(int x, int y) { return y*width + x; }
	
	public static double[] meanColor(int[] rgb) {
		int sumR = 0, sumG = 0, sumB = 0;
		for (int i=0; i<rgb.length; i++) {
			sumR += red(rgb[i]);
			sumG += green(rgb[i]);
			sumB += blue(rgb[i]);
			System.out.println("Pixel #"+i+": "+red(rgb[i])+" "+green(rgb[i])+" "+blue(rgb[i]));
		}
		double[] m = new double[3];
		m[0] = (double) sumR / (double) rgb.length;
		m[1] = (double) sumG / (double) rgb.length;
		m[2] = (double) sumB / (double) rgb.length;
		System.out.println("Mean: "+m[0]+" "+m[1]+" "+m[2]);
		return m;
	}
	
	public static double[][] covMat(int[] rgb, double[] mean) {
		double[][] sum = new double[3][3];
		for (int i=0; i<rgb.length; i++) {
			double r = (double) red(rgb[i])- mean[0];
			double g = (double) green(rgb[i]) - mean[1];
			double b = (double) blue(rgb[i]) - mean[2];
			sum[0][0] += r * r;
			sum[0][1] += r * g;
			sum[0][2] += r * b;
			sum[1][0] += g * r;
			sum[1][1] += g * g;
			sum[1][2] += g * b;
			sum[2][0] += b * r;
			sum[2][1] += b * g;
			sum[2][2] += b * b;
		}
		double[][] mat = new double[3][3];
		for (int i=0; i<3; i++) {
			for (int j=0; j<3; j++) {
				mat[i][j] = (double) sum[i][j] / (double) (rgb.length - 1);
			}
		}
		return mat;
	}
	
	public static double[] eigenvalues3(double[][] A) {
		//Given a real symmetric 3x3 matrix A, compute the eigenvalues
		double[] eig = new double[3];
		double p1 = A[0][1]*A[0][1] + A[0][2]*A[0][2] + A[1][2]*A[1][2];
		if (p1 == 0) {
		   //A is diagonal.
		   eig[0] = A[0][0];
		   eig[1] = A[1][1];
		   eig[2] = A[2][2];
		} else {
		   double q = (A[0][0] + A[1][1] + A[2][2]) / 3.0;
		   double p2 = Math.pow(A[0][0] - q, 2) + Math.pow(A[1][1] - q, 2) + Math.pow(A[2][2] - q, 2) + 2 * p1;
		   double p = Math.sqrt(p2 / 6.0);
		   double[][] B = new double[3][3]; // I is the identity matrix
		   for (int i=0; i<3; i++) {
			   for (int j=0; j<3; j++) {
				   B[i][j] = (A[i][j] - q * (i == j ? 1 : 0)) / p;
			   }
		   }
		   //r=det(B)/2
		   double r =   (B[0][0] * B[1][1] * B[2][2]
				       + B[0][1] * B[1][2] * B[2][0]
				       + B[0][2] * B[1][0] * B[2][1]
				       - B[0][2] * B[1][1] * B[2][0]
				       - B[0][1] * B[1][0] * B[2][2]
				       - B[0][0] * B[1][2] * B[2][1]) / 2.0;
		   //In exact arithmetic for a symmetric matrix  -1 <= r <= 1
		   //but computation error can leave it slightly outside this range.
		   double phi;
		   if (r <= -1) {
		      phi = Math.PI / 3;
		   } else if (r >= 1) {
		      phi = 0;
		   } else {
		      phi = Math.acos(r) / 3.0;
		   }
		   //the eigenvalues satisfy eig3 <= eig2 <= eig1
		   eig[0] = q + 2.0 * p * Math.cos(phi);
		   eig[2] = q + 2 * p * Math.cos(phi + (2 * Math.PI / 3.0));
		   eig[1] = 3 * q - eig[0] - eig[2];     //since trace(A) = eig1 + eig2 + eig3
		}
		System.out.println("Eigenvalues: " + eig[0] + " " + eig[1] + " " + eig[2]);
		return eig;
	}
	
	public static void print(Matrix M) {
		for (int i=0; i<M.getRowDimension(); i++) {
			for (int j=0; j<M.getColumnDimension(); j++) {
				System.out.print(M.get(i, j) + " ");
			}
			System.out.println();
		}
	}
	
	class GMMComp {
		double[] mean;
		Matrix covariance;
		double[] eigenvalues;
		Matrix eigenvectors;
	}
	
	public void gmm(int[] rgb, int n) {
		List<GMMComp> comps = new LinkedList<GMMComp>();
		GMMComp first = new GMMComp();
		first.mean = meanColor(rgb);
		first.covariance = new Matrix(covMat(rgb, first.mean));
		first.eigenvalues = first.covariance.eig().getRealEigenvalues();
		first.eigenvectors = first.covariance.eig().getV();
		for (int i=2; i<=n; i++) {
			GMMComp maxComp;
			double maxEigenvalue = 0;
			int eigenvalueIndex;
			for (GMMComp comp : comps) {
				for (int j=0; i<comp.eigenvalues.length; i++) {
					if (comp.eigenvalues[i] > maxEigenvalue) {
						maxEigenvalue = comp.eigenvalues[i];
						eigenvalueIndex = i;
						maxComp = comp;
					}
				}
				
			}
		}
	}
	
	public static void main(String[] args) {
	}

}
