import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.tsc.FlowerGenius.brain.ColorDistribution;


public class asdf {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			BufferedImage im = ImageIO.read(new File(args[0]));
			ColorDistribution cd = new ColorDistribution();
			double[] fv = cd.getFeatureVector(im.getRGB(0, 0, im.getWidth(), im.getHeight(), null, 0, im.getWidth()));
			double sum = 0.0;
			for (int i=0; i<fv.length; i++) {
				System.out.print(fv[i]);
				System.out.print(" ");
				sum += fv[i];
			}
			System.out.println();
			System.out.println(sum);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
