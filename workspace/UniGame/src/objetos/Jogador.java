package objetos;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.LinkedList;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

import telas.Animacao;
import telas.Controlador;
import telas.Jogo;
import framework.ObjectId;
import framework.Objeto_Jogo;
import framework.Texturas;

public class Jogador extends Objeto_Jogo {

	private float width = 48, height = 64;
	private float gravity = 0.008f;// velocidade da gravidade
	private boolean queda = true;
	private final float VelocidadeMaxima = 8;
	private Controlador controlador;
	Texturas tex = Jogo.getInstance();
	private Animacao jogadorAndando,jogadorAndandoTras;
	public Jogador(float _x, float _y, Controlador _controlador, ObjectId _id) {
		super(_x, _y, _id);
		this.controlador = _controlador;
		jogadorAndando = new Animacao(1, tex.jogador[1],tex.jogador[2],tex.jogador[3], tex.jogador[4]);
		jogadorAndandoTras = new Animacao(1, tex.jogador[5],tex.jogador[6],tex.jogador[7], tex.jogador[8]);
	}

	public void tick(LinkedList<Objeto_Jogo> objeto) {
		x += velX;
		y += velY;
		if(velX<0){
			enfrentando =-1;
		}else{
			enfrentando =1;
		}
		
		if (queda || pulo) {
			velY += gravity;
			if (velY > VelocidadeMaxima) {
				velY = VelocidadeMaxima;
			}
		}
		colisoes(objeto);
		jogadorAndando.corridaAnimacao();
		
	}

	private void colisoes(LinkedList<Objeto_Jogo> objeto) {

		for (int i = 0; i < controlador.objeto.size(); i++) {
			Objeto_Jogo tempObjeto = controlador.objeto.get(i);

			if (tempObjeto.getId() == ObjectId.Bloco) {

				if (getBlocosTopo().intersects(tempObjeto.getBlocos())) {
					y = tempObjeto.getY()+32;
					velY = 0;
				}
				if (getBlocos().intersects(tempObjeto.getBlocos())) {
					y = tempObjeto.getY() - height;
					velY = 0;
					queda = false;
					pulo = false;
				}else{
					queda=true;
					if (getBlocosDireita().intersects(tempObjeto.getBlocos())) {
						x = tempObjeto.getX() - width;
					}

					if (getBlocosEsquerda().intersects(tempObjeto.getBlocos())) {
						x = tempObjeto.getX() + width-17;
					}
				}

			}

		}
	}

	public void render(Graphics g) {
		if (velX!=0) {
			if (velX > 0) {
				jogadorAndando.drawAnimacao(g, (int)x, (int)y,70,70);
			} else {
				jogadorAndandoTras.drawAnimacao(g, (int)x, (int)y,70,70);
			}
			
			
			
		} else {
			g.drawImage(tex.jogador[0], (int)x,(int)y,70,70,null);
		}
		

	}

	public Rectangle getBlocos() {
		return new Rectangle((int) (x + (width / 2) - (width / 2) / 2),
				(int) (y + (height / 2)), (int) width / 2, (int) height / 2);
	}

	public Rectangle getBlocosTopo() {
		return new Rectangle((int) (x + (width / 2) - (width / 2) / 2),
				(int) y, (int) width / 2, (int) height / 2);
	}

	public Rectangle getBlocosDireita() {
		return new Rectangle((int) (x + width - 5), (int) y + 5, (int) 5,
				(int) height - 10);
	}

	public Rectangle getBlocosEsquerda() {
		return new Rectangle((int) x, (int) y + 5, (int) 5, (int) height - 10);
	}

}
