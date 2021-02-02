
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class BufferedImageLoader {
	
	public BufferedImage loadImage(String pathRelativeToThis) throws IOException {
		BufferedImage img = null;
		try {
			img = ImageIO.read(getClass().getResource(pathRelativeToThis));
		} catch(Exception e) {
			System.out.println("Couldn't find path: " + img);
		}
		return img;
	}
}
