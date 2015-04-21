import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.ImageIO;

/**
 * Bilder der jeweiligen Klassen anhand Datenbank ermitteln und Bilder in entsprechenden Größen erzeugen.
 */
public class FetchImages {
	
	private static final String SRC_DIR = "/home/ts/fsinb/pictures/";
	private static final String DST_DIR = "/home/ts/flower/FlowerGenius/assets/images/";
	private static final int SMALL_SIZE = 512;
	private static final int TINY_SIZE = 120;

	/**
	 * @param args
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public static void main(String[] args) {
		
		try {
			
			int i = 0;
			
			Class.forName("com.mysql.jdbc.Driver");
		
			Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/fsinb?user=fsinb&password=DxszzrXAwU8GmXmz");

			Statement st1 = connect.createStatement();
			ResultSet ids = st1.executeQuery("SELECT DISTINCT w.objectid FROM weights w");
		
			// Iteriere Klassen
			while (ids.next()) {
				
				i++;
				
				int id = ids.getInt("objectid");
			
				Statement st2 = connect.createStatement();
				ResultSet pictures = st2.executeQuery("SELECT DISTINCT filename FROM pictures WHERE objectid="+id+" ORDER BY suitability DESC LIMIT 1");
			
				// Iteriere Bilder einer Klasse
				while (pictures.next()) {
				
					String filename = pictures.getString("filename");				
					String srcFilename = SRC_DIR + filename;
					String dstFilenameS = DST_DIR + id + ".jpg";
					String dstFilenameT = DST_DIR + id + "_120.jpg";
					
					System.out.println(i+", object: "+id+" image: "+srcFilename+", "+dstFilenameS);
				
					BufferedImage im1 = ImageIO.read(new File(srcFilename));
				
					BufferedImage im2 = new BufferedImage(SMALL_SIZE, SMALL_SIZE, BufferedImage.TYPE_INT_RGB);
					Graphics g2 = im2.createGraphics();
					g2.drawImage(im1, 0, 0, SMALL_SIZE, SMALL_SIZE, null);
					g2.dispose();
					
					BufferedImage im3 = new BufferedImage(TINY_SIZE, TINY_SIZE, BufferedImage.TYPE_INT_RGB);
					Graphics g3 = im3.createGraphics();
					g3.drawImage(im1, 0, 0, TINY_SIZE, TINY_SIZE, null);
					g3.dispose();
				
					ImageIO.write(im2, "jpg", new File(dstFilenameS));
					ImageIO.write(im3, "jpg", new File(dstFilenameT));
				
				}
			
				st2.close();
			
			}
		
			st1.close();
			connect.close();
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
