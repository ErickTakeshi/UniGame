package framework;

import java.awt.image.BufferedImage;

public class SpriteSheet {
	private BufferedImage image;
	public SpriteSheet(BufferedImage _image){
		this.image = _image;		
	}
	public BufferedImage grabImage(int coluna, int linha, int largura, int altura){
		BufferedImage img =image.getSubimage( (coluna*largura)-largura, (linha*altura)-altura, largura, altura);
		return img;
	}
}