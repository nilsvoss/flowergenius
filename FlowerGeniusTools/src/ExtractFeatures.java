import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.ImageIO;

import com.tsc.FlowerGenius.brain.ColorDistribution;


public class ExtractFeatures {

	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/fsinb?user=fsinb&password=DxszzrXAwU8GmXmz");
			Statement statement = connect.createStatement();

			ResultSet objects = statement.executeQuery("SELECT DISTINCT objectid FROM weights");
			
			while (objects.next()) {
				int id = objects.getInt("objectid");
				
				ResultSet pictures = statement.executeQuery("SELECT DISTINCT filename "+
						"FROM pictures WHERE objectid="+id+" AND suitability>0");
				
				while (pictures.next()) {
					String filename = "/home/ts/fsinb/pictures/" + pictures.getString("filename");
					try {
						BufferedImage im = ImageIO.read(new File(filename));
						ColorDistribution cd = new ColorDistribution();
						int[] pixels = im.getRGB(0, 0, im.getWidth(), im.getHeight(), null, 0, im.getWidth());
						double[] fv = cd.getFeatureVector(pixels);
						System.out.println(filename);
					} catch (IOException e) {
					}					
				}
				
			}
			
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
