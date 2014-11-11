package org.ImageSegmentation.GMM;

import java.util.LinkedList;
import java.util.List;

public class Components {
	
	List<Component> comps;
	int size;
	
	public Components(int[] pixels, int K) {
		comps = new LinkedList<Component>();
		gmm(pixels, K);
		size = pixels.length;
		System.out.println("Components:");
		for (Component comp: comps) {
			int[] mean = comp.getMeanColor();
			System.out.print("Component color: "+mean[0]+" "+mean[1]+" "+mean[2]+" ");
			System.out.print("size: "+comp.size());
			System.out.println();
		}
	}

	private void gmm(int[] pixels, int K) {
		comps.add(new Component(pixels));
		for (int i=2; i<=K; i++) {
			System.out.println("calculate component " + i);
			double maxEval = 0;
			Component maxComp = comps.get(0);
			for (Component comp: comps) {
				double eval = comp.maxEigenvalue();
				if (eval > maxEval) {
					maxEval = eval;
					maxComp = comp;
				}
			}
			Component[] splitted = maxComp.split();
			comps.remove(maxComp);
			comps.add(splitted[0]);
			comps.add(splitted[1]);
		}
	}
	
	public int size() {
		return comps.size();
	}
	
	public Component get(int index) {
		return comps.get(index);
	}
	
	public double minDistance(int color) {
		double min = Double.MAX_VALUE;
		for (Component comp: comps) {
			double distance = comp.distance(color) / (double) comp.size();
			if (distance < min) {
				min = distance;
			}
		}
		return min;
	}

}
