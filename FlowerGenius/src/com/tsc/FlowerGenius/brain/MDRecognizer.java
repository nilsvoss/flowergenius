package com.tsc.FlowerGenius.brain;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import android.util.Log;

public class MDRecognizer implements Recognizer {
	
    List<Flower> list;

    public MDRecognizer(List<Flower> list) {
        this.list = list;
    }
    
    class FlowerDistance {
    	Flower flower;
    	double distance;
    }

	@Override
	public List<Flower> recognize(int[] pixels, int width, int height, int centerX, int centerY) {
		
		int n = list.size();
		
		ImageFeatures f = new ImageFeatures(pixels);
		
        double[] sampleFv = f.getFeatureVector();
        
        Log.d("MD", "number of sample pixels: "+pixels.length);
        Log.d("MD", "number of sample features: "+sampleFv.length);
        
        FlowerDistance[] fd = new FlowerDistance[n];
        
        for (int i=0; i<n; i++) {
        	
        	double[] classFv = list.get(i).getMeanVector();
        	fd[i] = new FlowerDistance();
            fd[i].distance = Functions.distance(sampleFv,classFv);    
            fd[i].flower = list.get(i);
            
            Log.d("MD", "number of class i="+i+" features: "+classFv.length);
            Log.d("MD", "distance to class i="+i+" is "+fd[i].distance);
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
