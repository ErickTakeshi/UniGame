package framework;

import java.awt.image.BufferedImage;
import telas.BufferedImageLoader;

public class Texturas {
	SpriteSheet bs, ps;
	private BufferedImage block_sheet = null;
	private BufferedImage jogador_sheet = null;
	private Objeto_Jogo objetoJogo;
	public BufferedImage[] block = new BufferedImage[3];
	public BufferedImage[] jogador = new BufferedImage[12];
	public BufferedImage TirosDir, tirosEsq;

	public Texturas() {
		BufferedImageLoader loader = new BufferedImageLoader();
		try {
			block_sheet = loader.loadImage("/coresDeBlocos.png");
			jogador_sheet = loader.loadImage("/player.png");
			TirosDir =  loader.loadImage("/tiroDir.png");
			tirosEsq = loader.loadImage("/tiroEsq.png");
			
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

		jogador[0] = ps.grabImage(6, 2, 70, 70); // personagem inativo frente
		jogador[11] = ps.grabImage(6, 3, 70, 70); // personagem inativo traz

		// ANDANDO PRA FRENTE
		jogador[1] = ps.grabImage(1, 2, 70, 70);
		jogador[2] = ps.grabImage(2, 2, 70, 70);
		jogador[3] = ps.grabImage(3, 2, 70, 70);
		jogador[4] = ps.grabImage(4, 2, 70, 70);
		jogador[5] = ps.grabImage(6, 2, 70, 70);
		// ANDANDO PRA TRAS
		jogador[6] = ps.grabImage(1, 3, 70, 70);
		jogador[7] = ps.grabImage(2, 3, 70, 70);
		jogador[8] = ps.grabImage(3, 3, 70, 70);
		jogador[9] = ps.grabImage(4, 3, 70, 70);
		jogador[10] = ps.grabImage(6, 3, 70, 70);

	}

}
