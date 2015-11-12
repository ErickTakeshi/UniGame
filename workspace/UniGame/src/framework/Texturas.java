package framework;

import java.awt.image.BufferedImage;

import telas.BufferedImageLoader;

public class Texturas {
	SpriteSheet bs, ps;
	private BufferedImage block_sheet = null;
	private BufferedImage jogador_sheet = null;
	private Objeto_Jogo objetoJogo;
	public BufferedImage[] block = new BufferedImage[3];
	public BufferedImage[] jogador = new BufferedImage[9];

	public Texturas() {
		BufferedImageLoader loader = new BufferedImageLoader();
		try {
			block_sheet = loader.loadImage("/coresDeBlocos.png");
			jogador_sheet = loader.loadImage("/player.png");
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
		
		
		jogador[0] = ps.grabImage(5, 2, 70, 70); // personagem inativo do
		// jogador
		
		//ANDANDO PRA FRENTE
		jogador[1] = ps.grabImage(1, 2, 70, 70);
		jogador[2] = ps.grabImage(2, 2, 70, 70); 
		jogador[3] = ps.grabImage(3, 2, 70, 70);
		jogador[4] = ps.grabImage(4, 2, 70, 70);
		//ANDANDO PRA TRAS
		jogador[5] = ps.grabImage(1, 3, 70, 70);
		jogador[6] = ps.grabImage(2, 3, 70, 70); 
		jogador[7] = ps.grabImage(3, 3, 70, 70);
		jogador[8] = ps.grabImage(4, 3, 70, 70);
		
		/*
		 * EXEMPLO - (2, 1, 54, 58); o 2 significa a coluna na imagem, 1
		 * significa a linha 54 significa a largura e 58 altura
		 */
		
		
		/*

		if (objetoJogo.getVelX() == 0) {
			jogador[0] = ps.grabImage(2, 1, 54, 58); // personagem inativo do
			// jogador
		} else if (objetoJogo.getVelX() == -1) {
			// jogandor andando para tras
		} else if (objetoJogo.getVelX() == 1) {
			// andando para frente
		} else {
			if (objetoJogo.getEnfretando() == 1) {
				jogador[2] = ps.grabImage(1, 1, 54, 58); // atirando
			} else if (objetoJogo.pulo == true) {
				// imagem do jogador pulando
			}
		}

		*/
	}

}
