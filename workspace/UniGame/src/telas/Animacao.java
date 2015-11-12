package telas;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class Animacao {
	private int velocidade;
	private int frames;
	
	private BufferedImage[] imagens;
	private BufferedImage imagemAtual;
	private int index=0;
	private int qtd=0;
	
	public Animacao(int _velocidade, BufferedImage... args){
		this.velocidade = _velocidade;
		imagens = new BufferedImage[args.length];
		for (int i = 0; i < args.length; i++) {
			imagens[i] = args[i];
		}
		frames = args.length;
	}
	
	public void corridaAnimacao(){
		index++;
		if(index>velocidade){
			index=0;
			nextFrame();
		}
	}
	
	public void nextFrame(){
		for (int i = 0; i < frames; i++) {
			if(qtd==i){
				imagemAtual = imagens[i];
			}
			qtd++;
			
			if(qtd>frames){
				qtd=0;
			}
		}
	}

	public void drawAnimacao(Graphics g, int x, int y){
		g.drawImage(imagemAtual,x,y,null);
	}
	
	public void drawExplosao(Graphics g,BufferedImage im, float f, float h){
		g.drawImage(im,(int)f,(int)h,null);
	}  
	
	
	public void drawAnimacao(Graphics g, int x, int y, int scaleX, int scaleY){
		g.drawImage(imagemAtual,x,y,scaleX,scaleY,null);
	}
	
	
}
