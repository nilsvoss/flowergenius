package com.tsc.FlowerGenius.brain;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;



import android.media.Image;

public class DummyRecognizer implements Recognizer {
	
	FlowerDatabase db;
	
	public DummyRecognizer(FlowerDatabase db) {
		this.db = db;
	}

	public List<Flower> recognize(int[] pixels, int width, int height, int centerX, int centerY) {
		List<Flower> list = new LinkedList<Flower>();
		try {
			list.add(db.getObject(777));
			list.add(db.getObject(4180));
			list.add(db.getObject(4211));
			list.add(db.getObject(4227));
			list.add(db.getObject(4242));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

}
