package com.tsc.FlowerGenius.brain;

import java.util.List;



public interface Recognizer {
	
	public List<Flower> recognize(int[] pixels, int width, int height, int centerX, int centerY);

}
