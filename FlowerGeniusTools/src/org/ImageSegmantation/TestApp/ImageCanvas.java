package org.ImageSegmantation.TestApp;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import javax.swing.JPanel;

public class ImageCanvas extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private BufferedImage image;
	private double[] mask;
	private int radius = -1;
	private int centerX = -1;
	private int centerY = -1;

	public void setFg(int centerX, int centerY, int radius) {
		this.radius = radius;
		this.centerX = centerX;
		this.centerY = centerY;
		repaint();
	}

	public double[] getMask() {
		return mask;
	}

	public void setMask(double[] mask) {
		this.mask = mask;
		repaint();
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
		repaint();
	}

	public void update(Graphics g) {
		paint(g);
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		BufferedImage copy;
		if (mask != null) {
			copy = deepCopy(image);
			for (int x=0; x<copy.getWidth(); x++) {
				for (int y=0; y<copy.getHeight(); y++) {
					if (mask[y*copy.getWidth() + x] <= 0) {
						copy.setRGB(x, y, 0);
					}
				}
			}
		} else {
			copy = image;
		}
		if (copy != null) {
			//System.out.println("paint(): image size: " + image.getWidth() + " " + image.getHeight());
			//System.out.println("paint(): copy size: " + copy.getWidth() + " " + copy.getHeight());
			int dx1 = 0;
			int dy1 = 0;
			int dx2 = getWidth();
			int dy2 = getHeight();
			int sx1 = 0;
			int sy1 = 0;
			int sx2 = copy.getWidth();
			int sy2 = copy.getHeight();
			g.drawImage(copy, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, this);
		}
		if (radius != -1 && centerX != -1 && centerY != -1) {
			g.setColor(Color.GREEN);
			g.drawRect(centerX - radius, centerY - radius, 2 * radius, 2 * radius);
		}
	}
	
	static BufferedImage deepCopy(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

}
