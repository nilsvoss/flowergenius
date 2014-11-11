package com.tsc.FlowerGenius.brain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


import android.content.Context;

public class FlowerDatabase {
	
	ArrayList<Flower> list;
	
	private static final String DATAFILE = "objects.data";
	
	private Context context;
	
	public FlowerDatabase(Context context) {
		this.context = context;
	}
	
	public Flower getObject(int id) throws IOException {
		getObjects();
		for (Flower flower: list) {
			if (flower.getId() == id) {
				return flower;
			}
		}
		return null;
	}
	
	public ArrayList<Flower> getObjects() throws IOException {
		if (list != null) {
			return list;
		} else {
			list = new ArrayList<Flower>();
			BufferedReader reader = null;
			try {
			    reader = new BufferedReader(new InputStreamReader(context.getAssets().open(DATAFILE), "UTF-8")); 
			    String line = reader.readLine();
			    while (line != null) {
			    	list.add(new Flower(line));
			        line = reader.readLine();
			    }
			    return list;
			} finally {
				reader.close();
			}
		}
	}

}
