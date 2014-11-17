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
			
			Statement st1 = connect.createStatement();
			ResultSet ids = st1.executeQuery("SELECT DISTINCT w.objectid,o.botname1,o.gername1 FROM weights w JOIN objects o ON (w.objectid=o.id)");
			
			ColorDistribution cd = new ColorDistribution();
			
			while (ids.next()) {
				
				int id = ids.getInt("objectid");
				Statement st2 = connect.createStatement();
				ResultSet pictures = st2.executeQuery("SELECT DISTINCT filename "+
						"FROM pictures WHERE objectid="+id+" AND suitability>0");
				double[] meanFv = new double[cd.getFvSize()];
				int c = 0;
				
				while (pictures.next()) {
					
					String filename = "/home/ts/fsinb/pictures/" + pictures.getString("filename");
					try {
						BufferedImage im = ImageIO.read(new File(filename));
						int[] pixels = im.getRGB(0, 0, im.getWidth(), im.getHeight(), null, 0, im.getWidth());
						double[] fv = cd.getFeatureVector(pixels);
						System.out.println(filename);
						c++;
						for (int i=0; i<fv.length; i++) {
							meanFv[i] += fv[i];
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
				System.out.print(ids.getString("botname1") + "\t");
				System.out.print(ids.getString("gername1") + "\t");
				
				for (int i=0; i<meanFv.length; i++) {
					meanFv[i] = meanFv[i] / (double) c;
					System.out.print(meanFv[i]);
					if (i == meanFv.length - 1) {
						System.out.println();
					} else {
						System.out.print("\t");
					}
				}
				
				st2.close();
				
			}
			
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
