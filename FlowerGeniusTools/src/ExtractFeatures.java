import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.ImageIO;

import com.tsc.FlowerGenius.brain.ImageFeatures;


public class ExtractFeatures {

	public static void main(String[] args) {
		try {
			// connect to database
			Class.forName("com.mysql.jdbc.Driver");
			Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/fsinb?user=fsinb&password=DxszzrXAwU8GmXmz");
			
			// load list of classes from database
			Statement st1 = connect.createStatement();
			ResultSet ids = st1.executeQuery("SELECT DISTINCT w.objectid,o.botname1,o.gername1 FROM weights w JOIN objects o ON (w.objectid=o.id) ORDER BY o.botname1");
			
			// iterate classes
			while (ids.next()) {
				
				int id = ids.getInt("objectid");
				
				// print line with class data (cd)
				System.out.print("cd;");
				System.out.print(id + ";");
				System.out.print(ids.getString("botname1") + ";");
				System.out.println(ids.getString("gername1"));
				
				// load list of suitable pictures of the class
				Statement st2 = connect.createStatement();
				ResultSet pictures = st2.executeQuery("SELECT DISTINCT filename FROM pictures WHERE objectid="+id+" AND suitability>0");

				// itrerate pictures of one class
				while (pictures.next()) {
					
					String filename = "/home/ts/fsinb/pictures/" + pictures.getString("filename");
					try {
						
						BufferedImage im = ImageIO.read(new File(filename));
						int[] pixels = im.getRGB(0, 0, im.getWidth(), im.getHeight(), null, 0, im.getWidth());
						ImageFeatures f = new ImageFeatures(pixels);
						
						double[] fv = f.getFeatureVector();
						
						// print feature vector of the picture (fv)
						System.out.print("fv;");
						for (int i=0; i<fv.length; i++) {
							System.out.print(fv[i]);
							if (i < (fv.length - 1)) {
								System.out.print(";");
							}
						}
						System.out.println();
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
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
