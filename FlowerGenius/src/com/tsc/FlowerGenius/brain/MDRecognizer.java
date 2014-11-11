package com.tsc.FlowerGenius.brain;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;




public class MDRecognizer implements Recognizer {

    List<Flower> list;

    public MDRecognizer(List<Flower> list) {
        this.list = list;
    }
    
    class FlowerDistance{
    	Flower flower;
    	double distance;
    }

	@Override
	public List<Flower> recognize(int[] pixels, int width, int height, int centerX, int centerY) {
		
		int n = list.size();
		
        double[] fv = (new ColorDistribution()).getFeatureVector(pixels);
        FlowerDistance[] fd = new FlowerDistance[n];
        
        for (int i=0; i<n; i++) {
            fd[i].distance = Functions.distance(fv,list.get(i).getFeatureVector());    
            fd[i].flower = list.get(i);
        }

        Arrays.sort(fd, new Comparator<FlowerDistance>() {
			@Override
			public int compare(FlowerDistance a, FlowerDistance b) {
				return Double.compare(a.distance, b.distance);
			}
        });
        
        List<Flower> ret = new LinkedList<Flower>();
        for (int i=0; i<n; i++) {
        	ret.add(fd[i].flower);
        }
        
		return ret;
	}

}
