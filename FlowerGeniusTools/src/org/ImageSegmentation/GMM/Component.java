package org.ImageSegmentation.GMM;

import java.util.ArrayList;
import java.util.List;

import org.ImageSegmentation.Functions;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

public class Component {
	
	private List<Integer> pixels;
	private double[] mean;
	private EigenvalueDecomposition eig;
	
	Component(int[] pixels) {
		init();
		add(pixels);
	}
	
	Component() {
		init();
	}
	
	private void init() {
		pixels = new ArrayList<Integer>();
		mean = null;
	}
	
	public void add(int pixel) {
		pixels.add(pixel);
		mean = null;
	}
	
	public void add(int[] pixels) {
		for (int i=0; i<pixels.length; i++) {
			add(pixels[i]);
		}
	}
	
	private double[] calcMeanColor() {
		if (mean == null) {
			int sumR = 0, sumG = 0, sumB = 0;
			for (int i=0; i<pixels.size(); i++) {
				sumR += Functions.red(pixels.get(i));
				sumG += Functions.green(pixels.get(i));
				sumB += Functions.blue(pixels.get(i));
			}
			double[] m = new double[3];
			m[0] = (double) sumR / (double) pixels.size();
			m[1] = (double) sumG / (double) pixels.size();
			m[2] = (double) sumB / (double) pixels.size();
			mean = m;
			return m;
		} else {
			return mean;
		}
	}
	
	private Matrix covMat() {
		double[] mean = calcMeanColor();
		double[][] sum = new double[3][3];
		for (int i=0; i<pixels.size(); i++) {
			double r = (double) Functions.red(pixels.get(i))- mean[0];
			double g = (double) Functions.green(pixels.get(i)) - mean[1];
			double b = (double) Functions.blue(pixels.get(i)) - mean[2];
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
				mat[i][j] = (double) sum[i][j] / (double) (pixels.size() - 1);
			}
		}
		return new Matrix(mat);
	}
	
	private double[] eigenvalues() {
		if (eig == null) {
			eig = covMat().eig();
		}
		return eig.getRealEigenvalues();
	}
	
	private Matrix eigenvectors() {
		if (eig == null) {
			eig = covMat().eig();
		}
		return eig.getV();
	}
	
	public double maxEigenvalue() {
		double[] eval = eigenvalues();
		double max = -1;
		for (int i=0; i<eval.length; i++) {
			if (eval[i] > max) {
				max = eval[i];
			}
		}
		return max;
	}
	
	public double[] maxEigenvector() {
		double[] eval = eigenvalues();
		Matrix evec = eigenvectors();
		double max = -1;
		double[] maxVec = new double[3];
		for (int i=0; i<eval.length; i++) {
			if (eval[i] > max) {
				max = eval[i];
				maxVec[0] = evec.get(0, i);
				maxVec[1] = evec.get(1, i);
				maxVec[2] = evec.get(2, i);
			}
		}
		return maxVec;
	}
	
	public Component[] split() {
		double[] m = calcMeanColor();
		double[] e = maxEigenvector();
		Component right = new Component();
		Component left = new Component();
		for (int i=0; i<pixels.size(); i++) {
			int p = pixels.get(i);
			int r = Functions.red(p);
			int g = Functions.green(p);
			int b = Functions.blue(p);
			if (e[0]*r + e[1]*g + e[2]*b <= e[0]*m[0] + e[1]*m[1] + e[2]*m[2]) {
				left.add(p);
			} else {
				right.add(p);
			}
		}
		return new Component[]{left, right};
	}
	
	public int size() {
		return pixels.size();
	}
	
	public double distance(int color) {
		calcMeanColor();
		double r = Functions.red(color);
		double g = Functions.green(color);
		double b = Functions.blue(color);
		return Functions.distance(r, g, b, mean[0], mean[1], mean[2]);
	}
	
	public int[] getMeanColor() {
		calcMeanColor();
		return new int[]{(int) mean[0], (int) mean[1], (int) mean[2]};
	}
	
}
