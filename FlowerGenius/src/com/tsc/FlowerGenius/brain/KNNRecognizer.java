package com.tsc.FlowerGenius.brain;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import android.util.Log;

public class KNNRecognizer implements Recognizer {
	
	private static final int k = 32;
	
	List<Flower> flowerList;

    public KNNRecognizer(List<Flower> flowerList) {
        this.flowerList = flowerList;
    }
    
    class FlowerDistance {
    	int index;
    	double value;
    }
    
    class FlowerVotings {
    	Flower flower;
    	int votings;
    }

	@Override
	public List<Flower> recognize(int[] pixels, int width, int height, int centerX, int centerY) {
		
		ImageFeatures f = new ImageFeatures(pixels);
		
		List<FlowerDistance> distances = new LinkedList<FlowerDistance>();
		
		double[] sampleFv = f.getFeatureVector();
        
        // iterate flower classes
		for (int i=0; i<flowerList.size(); i++) {
        
        	Flower flower = flowerList.get(i);
        	
        	// iterate feature vectors in flower class
        	for (int j=0; j<flower.countVectors(); j++) {
        		
        		double[] fv = flower.getVector(j);
        		FlowerDistance distance = new FlowerDistance();
        		distance.value = Functions.distance(sampleFv,fv);
        		distance.index = i;
            
        		Log.d("KNN", "number of class i="+i+" features: "+fv.length);
        		Log.d("KNN", "distance to class i="+i+" is "+distance.value);
        		
        	}
        }
		
		// sort all feature vectors by distance to the sample feature vector
		Collections.sort(distances, new Comparator<FlowerDistance>() {
			@Override
			public int compare(FlowerDistance a, FlowerDistance b) {
				return Double.compare(a.value, b.value);
			}
        });
        
		// let the neighbors vote about the right classification
		FlowerVotings[] votings = new FlowerVotings[flowerList.size()];
		// init votings list
		for (int i=0; i<flowerList.size(); i++) {
			votings[i].votings = 0;
			votings[i].flower = flowerList.get(0);
		}
		// count votes
        for (int i=0; i<k; i++) {
        	votings[distances.get(i).index].votings++;
        }
        // sort votings
        Arrays.sort(votings, new Comparator<FlowerVotings>() {
			@Override
			public int compare(FlowerVotings a, FlowerVotings b) {
				if (a.votings < b.votings)
					return -1;
				else if (a.votings > b.votings)
					return 1;
				else
					return 0;
			}
        });
        
        // construct output flower list
        List<Flower> output = new LinkedList<Flower>();
        for (int i=0; i<k; i++) {
        	output.add(votings[i].flower);
        	Log.d("KNN", "Result "+i+" of "+k+": "+votings[i].flower.getBotName()+" with "+votings[i].votings+" votes");
        }
		
		return output;
	}

}
