package com.tsc.FlowerGenius.brain;

import java.util.List;

public class KNNRecognizer implements Recognizer {
	
	List<Flower> flowerList;

    public KNNRecognizer(List<Flower> list) {
        this.list = list;
    }

	@Override
	public List<Flower> recognize(int[] pixels, int width, int height, int centerX, int centerY) {
		// TODO Auto-generated method stub
		return null;
	}

}
