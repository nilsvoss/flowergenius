package com.tsc.FlowerGenius.brain;

import java.io.IOException;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Flower {
	
	private static final String ASSETS_DIR = "images/";

	private String[] fields;
    
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
    
    public double[] getFeatureVector() {
        int n = ColorDistribution.BINS_PER_CHANNEL; //number of features
        int o = 3;//offset in fields list
        double[] vector = new double[n*n*n];
        for (int i=0; i<n*n*n; i++) {
            vector[i] = Double.parseDouble(fields[i+o]);   
        }
        return vector;
    }

}
