package org.ImageSegmantation.TestApp;

import java.awt.EventQueue;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Panel;
import java.awt.GridBagLayout;
import javax.swing.JButton;

import org.ImageSegmentation.Image;

import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JPanel;
import java.awt.FlowLayout;

public class MainWindow {

	private JFrame frame;
	private BufferedImage image;
	private int[] pixels;
	private ImageCanvas imageCanvas;
	private List<String> files;
	private int selectedFile;
	private Image img;
	private double radiusH = 0.1;
	private double radiusV = 0.1;
	private double borderLR = 0.1;
	private double borderTB = 0.1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}
	
	private void load(String fn) {
		try {
			image = ImageIO.read(new File(fn));
			//image = histogramEqualization(image);
			imageCanvas.setImage(image);
			pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
			imageCanvas.setMask(null);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 664, 513);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		final ImageCanvas imageCanvas = new ImageCanvas();
		this.imageCanvas = imageCanvas;
		imageCanvas.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				imageCanvas.setFg(e.getPoint().x, e.getPoint().y, (int) (radiusH * Math.min(imageCanvas.getWidth(), imageCanvas.getHeight())));
			}
		});
		imageCanvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int centerX = (int) ((e.getPoint().x / (double) imageCanvas.getWidth()) * image.getWidth());
				int centerY = (int) ((e.getPoint().y / (double) imageCanvas.getHeight()) * image.getHeight());
				int iRadiusH = (int) (radiusH * image.getWidth()); 
				int iRadiusV = (int) (radiusV * image.getHeight());
				int iBorderTB = (int) (borderTB * image.getHeight());
				int iBorderLR = (int) (borderLR * image.getWidth());
				img = new Image(pixels, image.getWidth(), image.getHeight(), iBorderTB, iBorderLR, iBorderTB, iBorderLR, centerX, centerY, iRadiusH, iRadiusV, 10);
				imageCanvas.setMask(img.getMask());
			}
		});
		frame.getContentPane().add(imageCanvas, BorderLayout.CENTER);

		Panel panelNorth = new Panel();
		frame.getContentPane().add(panelNorth, BorderLayout.NORTH);
		GridBagLayout gbl_panelNorth = new GridBagLayout();
		gbl_panelNorth.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
		gbl_panelNorth.rowHeights = new int[]{0, 0};
		gbl_panelNorth.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panelNorth.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panelNorth.setLayout(gbl_panelNorth); 
		
		JButton btnFolder = new JButton("Open folder");
		btnFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(frame);
				File f = new File(fc.getSelectedFile().getParent());
				String[] filesArr = f.list();
				files = new ArrayList<String>();
				selectedFile = 0;
				System.out.println("FILES: ");
				for (int i= 0; i<filesArr.length; i++) {
					if (filesArr[i].contains(".jpg")) {
						files.add(fc.getSelectedFile().getParent() + "/" + filesArr[i]);
						System.out.println(files.get(files.size()-1));
					}
				}
				load(files.get(0));
			}
		});
		GridBagConstraints gbc_btnFolder = new GridBagConstraints();
		gbc_btnFolder.insets = new Insets(0, 0, 0, 5);
		gbc_btnFolder.gridx = 0;
		gbc_btnFolder.gridy = 0;
		panelNorth.add(btnFolder, gbc_btnFolder);
		
		JButton btnPrev = new JButton("prev");
		btnPrev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				load("371.jpg");
			}
		});
		GridBagConstraints gbc_btnPrev = new GridBagConstraints();
		gbc_btnPrev.insets = new Insets(0, 0, 0, 5);
		gbc_btnPrev.gridx = 1;
		gbc_btnPrev.gridy = 0;
		panelNorth.add(btnPrev, gbc_btnPrev);
		
		JButton btnNext = new JButton("next");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedFile++;
				load(files.get(selectedFile));
			}
		});
		GridBagConstraints gbc_btnNext = new GridBagConstraints();
		gbc_btnNext.insets = new Insets(0, 0, 0, 5);
		gbc_btnNext.gridx = 2;
		gbc_btnNext.gridy = 0;
		panelNorth.add(btnNext, gbc_btnNext);
		
		JButton btnSeg2 = new JButton("improve");
		btnSeg2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				img.improve(10);
				imageCanvas.setMask(img.getMask());
			}
		});
		GridBagConstraints gbc_btnSeg2 = new GridBagConstraints();
		gbc_btnSeg2.gridx = 4;
		gbc_btnSeg2.gridy = 0;
		panelNorth.add(btnSeg2, gbc_btnSeg2);
		
		JPanel panelEast = new JPanel();
		frame.getContentPane().add(panelEast, BorderLayout.EAST);
		FlowLayout fl_panelEast = new FlowLayout(FlowLayout.RIGHT, 5, 5);
		panelEast.setLayout(fl_panelEast);
	}
	
	
	
	
	
	// Return an ArrayList containing histogram values for separate R, G, B channels
    public static ArrayList<int[]> imageHistogram(BufferedImage input) {

        int[] rhistogram = new int[256];
        int[] ghistogram = new int[256];
        int[] bhistogram = new int[256];

        for(int i=0; i<rhistogram.length; i++) rhistogram[i] = 0;
        for(int i=0; i<ghistogram.length; i++) ghistogram[i] = 0;
        for(int i=0; i<bhistogram.length; i++) bhistogram[i] = 0;

        for(int i=0; i<input.getWidth(); i++) {
            for(int j=0; j<input.getHeight(); j++) {

                int red = new Color(input.getRGB (i, j)).getRed();
                int green = new Color(input.getRGB (i, j)).getGreen();
                int blue = new Color(input.getRGB (i, j)).getBlue();

                // Increase the values of colors
                rhistogram[red]++; ghistogram[green]++; bhistogram[blue]++;

            }
        }

        ArrayList<int[]> hist = new ArrayList<int[]>();
        hist.add(rhistogram);
        hist.add(ghistogram);
        hist.add(bhistogram);

        return hist;

    }
 // Get the histogram equalization lookup table for separate R, G, B channels
    private static ArrayList<int[]> histogramEqualizationLUT(BufferedImage input) {

        // Get an image histogram - calculated values by R, G, B channels
        ArrayList<int[]> imageHist = imageHistogram(input);

        // Create the lookup table
        ArrayList<int[]> imageLUT = new ArrayList<int[]>();

        // Fill the lookup table
        int[] rhistogram = new int[256];
        int[] ghistogram = new int[256];
        int[] bhistogram = new int[256];

        for(int i=0; i<rhistogram.length; i++) rhistogram[i] = 0;
        for(int i=0; i<ghistogram.length; i++) ghistogram[i] = 0;
        for(int i=0; i<bhistogram.length; i++) bhistogram[i] = 0;

        long sumr = 0;
        long sumg = 0;
        long sumb = 0;

        // Calculate the scale factor
        float scale_factor = (float) (255.0 / (input.getWidth() * input.getHeight()));

        for(int i=0; i<rhistogram.length; i++) {
            sumr += imageHist.get(0)[i];
            int valr = (int) (sumr * scale_factor);
            if(valr > 255) {
                rhistogram[i] = 255;
            }
            else rhistogram[i] = valr;

            sumg += imageHist.get(1)[i];
            int valg = (int) (sumg * scale_factor);
            if(valg > 255) {
                ghistogram[i] = 255;
            }
            else ghistogram[i] = valg;

            sumb += imageHist.get(2)[i];
            int valb = (int) (sumb * scale_factor);
            if(valb > 255) {
                bhistogram[i] = 255;
            }
            else bhistogram[i] = valb;
        }

        imageLUT.add(rhistogram);
        imageLUT.add(ghistogram);
        imageLUT.add(bhistogram);

        return imageLUT;

    }
    private static BufferedImage histogramEqualization(BufferedImage original) {

        int red;
        int green;
        int blue;
        int alpha;
        int newPixel = 0;

        // Get the Lookup table for histogram equalization
        ArrayList<int[]> histLUT = histogramEqualizationLUT(original);

        BufferedImage histogramEQ = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());

        for(int i=0; i<original.getWidth(); i++) {
            for(int j=0; j<original.getHeight(); j++) {

                // Get pixels by R, G, B
                alpha = new Color(original.getRGB (i, j)).getAlpha();
                red = new Color(original.getRGB (i, j)).getRed();
                green = new Color(original.getRGB (i, j)).getGreen();
                blue = new Color(original.getRGB (i, j)).getBlue();

                // Set new pixel values using the histogram lookup table
                red = histLUT.get(0)[red];
                green = histLUT.get(1)[green];
                blue = histLUT.get(2)[blue];

                // Return back to original format
                newPixel = colorToRGB(alpha, red, green, blue);

                // Write pixels into image
                histogramEQ.setRGB(i, j, newPixel);

            }
        }

        return histogramEQ;

    }
 // Convert R, G, B, Alpha to standard 8 bit
    private static int colorToRGB(int alpha, int red, int green, int blue) {

        int newPixel = 0;
        newPixel += alpha; newPixel = newPixel << 8;
        newPixel += red; newPixel = newPixel << 8;
        newPixel += green; newPixel = newPixel << 8;
        newPixel += blue;

        return newPixel;

    }

}
