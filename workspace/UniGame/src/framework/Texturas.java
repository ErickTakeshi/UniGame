package framework;

import java.awt.image.BufferedImage;

import telas.BufferedImageLoader;

public class Texturas {
	SpriteSheet bs,ps;
	private BufferedImage block_sheet = null;
	private BufferedImage jogador_sheet = null;
	
	public BufferedImage[] block = new BufferedImage[3];
	public BufferedImage[] jogador = new BufferedImage[4];
	
	public Texturas(){
		BufferedImageLoader loader = new BufferedImageLoader();
		try {
			block_sheet = loader.loadImage("/block_sheet.png");
			jogador_sheet = loader.loadImage("/jogador.png");
		} catch (Exception e) {
			e.printStackTrace();
		}
		bs = new SpriteSheet(block_sheet);
		ps =new SpriteSheet(jogador_sheet);
		
		getTexturas();
	}
	private void getTexturas(){
		block[0] = bs.grabImage(1, 1,32,32);// terra block
		block[1] = bs.grabImage(2, 1,32,32);// grama block
		
		jogador[0]=ps.grabImage(1, 1, 48, 64);// personagem inativo do jogador
		jogador[1]=ps.grabImage(2, 1, 48, 64); // personagem andando
		jogador[2]=ps.grabImage(3, 1, 32, 64); // personagem andando
		jogador[3]=ps.grabImage(4, 1, 32, 64); // personagem andando
		
	}
	
	
}
