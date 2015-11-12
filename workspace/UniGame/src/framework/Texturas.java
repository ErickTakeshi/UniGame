package framework;

import java.awt.image.BufferedImage;

import telas.BufferedImageLoader;

public class Texturas {
	SpriteSheet bs, ps;
	private BufferedImage block_sheet = null;
	private BufferedImage jogador_sheet = null;
	private Objeto_Jogo objetoJogo;
	public BufferedImage[] block = new BufferedImage[3];
	public BufferedImage[] jogador = new BufferedImage[4];

	public Texturas() {
		BufferedImageLoader loader = new BufferedImageLoader();
		try {
			block_sheet = loader.loadImage("/coresDeBlocos.png");
			jogador_sheet = loader.loadImage("/te.png");
		} catch (Exception e) {
			e.printStackTrace();
		}
		bs = new SpriteSheet(block_sheet);
		ps = new SpriteSheet(jogador_sheet);
		getTexturas();
	}

	private void getTexturas() {
		block[0] = bs.grabImage(1, 1, 32, 32);// terra block
		block[1] = bs.grabImage(2, 1, 32, 32);// grama block
		block[2] = bs.grabImage(3, 1, 32, 32);// concreto block
//------------------------////------------------------------------/////////
		jogador[0] = ps.grabImage(2, 1, 54, 58); // personagem inativo
		jogador[1] = ps.grabImage(1, 1, 54, 58); // personagem atirando	
		jogador[2] = ps.grabImage(3, 1, 54, 58);
		jogador[3] = ps.grabImage(2, 2, 54, 58);
	}

}
