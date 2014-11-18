import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.ImageIO;


public class FetchImages {
	
	private static final String SRC_DIR = "/home/ts/fsinb/pictures/";
	private static final String DST_DIR = "/home/ts/flower/FlowerGenius/assets/images/";
	private static final int SMALL_SIZE = 512;

	/**
	 * @param args
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		
		Class.forName("com.mysql.jdbc.Driver");
		Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/fsinb?user=fsinb&password=DxszzrXAwU8GmXmz");

		Statement st1 = connect.createStatement();
		ResultSet ids = st1.executeQuery("SELECT DISTINCT w.objectid FROM weights w");
		
		while (ids.next()) {
			
			int id = ids.getInt("objectid");
			
			Statement st2 = connect.createStatement();
			ResultSet pictures = st2.executeQuery("SELECT DISTINCT filename FROM pictures WHERE objectid="+id+" ORDER BY suitability DESC");
			
			while (pictures.next()) {
				
				String filename = pictures.getString("filename");
				System.out.println("object: "+id+" image: "+filename);
				
				String srcFilename = SRC_DIR + filename;
				String dstFilename = SRC_DIR + filename.substring(0, filename.lastIndexOf('.')) + ".jpg";
				
				BufferedImage im1 = ImageIO.read(new File(srcFilename));
				
				BufferedImage im2 = new BufferedImage(SMALL_SIZE, SMALL_SIZE, BufferedImage.TYPE_INT_RGB);
				Graphics g = im2.createGraphics();
				g.drawImage(im1, 0, 0, SMALL_SIZE, SMALL_SIZE, null);
				g.dispose();
				
				ImageIO.write(im2, "jpg", new File(dstFilename));
				
			}
			
			st2.close();
			
		}
		
		st1.close();
		connect.close();
		
	}

}
