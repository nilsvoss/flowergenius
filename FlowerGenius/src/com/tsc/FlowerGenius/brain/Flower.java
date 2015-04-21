package com.tsc.FlowerGenius.brain;

import java.io.IOException;
import java.util.List;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Flower {
	
	private static final String ASSETS_DIR = "images/";

	private String[] fields;
	private List<double[]> vectors;
	private int vectorSize = -1;
    
    public Flower(String line) {
        fields = line.split(";");
    }

	public int getId() {
		return Integer.parseInt(fields[0]);
	}

	public String getImageFile() {
		return getId()+".jpg";
	}
	
	public Bitmap getSmallImage(AssetManager assets) throws IOException {
		return BitmapFactory.decodeStream(assets.open(ASSETS_DIR+getId()+"_120.jpg"));
	}
	
	public Bitmap getImage(AssetManager assets) throws IOException {
		return BitmapFactory.decodeStream(assets.open(ASSETS_DIR+getId()+".jpg"));
	}

	public String getBotName() {
		return fields[1];
	}

	public String getName() {
		return fields[2];
	}
	
	public void addVector(double[] v) throws Exception {
		if (vectorSize == -1) {
			vectorSize = v.length;
		} else if (vectorSize != v.length) {
			throw new Exception("wrong vector size");
		}
		vectors.add(v);
	}
	
	public void parseVector(String s) throws Exception {
		int offset = 1;
		fields = s.split(";");
		double[] v = new double[s.length()];
		for (int i=offset; i<s.length(); i++) {
			v[i-offset] = Double.parseDouble(fields[i]);
		}
		addVector(v);
	}
	
	public int countVectors() {
		return vectors.size();
	}
	
	public double[] getVector(int index) {
		return vectors.get(index);
	}
	
	public double[] getMeanVector() {
		int c = vectors.size();
		double[] m = new double[vectorSize];
		for (int i=0; i<c; i++) {
			double[] v = vectors.get(i);
			for (int j=0; j<vectorSize; j++) {
				m[j] += v[j];
			}
		}
		for (int i=0; i<vectorSize; i++) {
			m[i] = m[i] / c;
		}
		return m;
	}
    
    public double[] getFeatureVector() {
        int n = ImageFeatures.BINS_PER_CHANNEL; //number of features
        int o = 3;//offset in fields list
        double[] vector = new double[n*n*n];
        for (int i=0; i<n*n*n; i++) {
            vector[i] = Double.parseDouble(fields[i+o]);   
        }
        return vector;
    }

}
