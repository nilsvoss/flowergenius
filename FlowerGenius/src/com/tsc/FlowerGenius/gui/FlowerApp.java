package com.tsc.FlowerGenius.gui;

import com.tsc.FlowerGenius.brain.FlowerDatabase;

import android.app.Application;

public class FlowerApp extends Application {
	
	static final int IMAGE_HEIGHT = 512;
	
	FlowerDatabase db;

	@Override
	public void onCreate() {
		super.onCreate();
		db = new FlowerDatabase(this.getApplicationContext());
	}
	
	public FlowerDatabase getDb() {
		return db;
	}

}
