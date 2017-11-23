package game;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageResources {
	private ImageResources(){}
	
	public static ImageIcon getSubimageAsIcon(String imageFile, int x, int y, int width, int height){
		try{
			BufferedImage buffImage = ImageIO.read(ImageResources.class.getResourceAsStream(imageFile));
			BufferedImage a = buffImage.getSubimage(x, y, width, height);
			return new ImageIcon(a.getScaledInstance(width, height, Image.SCALE_FAST));
		} catch (IOException ex){
		    DEBUG.WRITE("Error IO exception", DEBUG.DebugTypes.LOG_ERROR);
			ex.printStackTrace();
		}
		throw new IllegalArgumentException("Something wrong with arguments, couldn't get image file");
	}
	
	public static ImageIcon getSubimageAsIconFromDefault(int x, int y, int width, int height){
		return getSubimageAsIcon("cards3tw0.png", x, y, width, height);
	}

}
