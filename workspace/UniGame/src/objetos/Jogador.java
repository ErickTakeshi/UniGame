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
	private Animacao jogadorAndandotras;
	private Animacao jogadorAndandofrente;

	public Jogador(float _x, float _y, Controlador _controlador, ObjectId _id) {
		super(_x, _y, _id);
		this.controlador = _controlador;
		// imagens do jogador andando
		jogadorAndandotras = new Animacao(4, tex.jogador[3]);
		jogadorAndandofrente = new Animacao(4,tex.jogador[0], tex.jogador[2]);
	}

	public void tick(LinkedList<Objeto_Jogo> objeto) {
		x += velX;
		y += velY;
		if (velX < 0) {// andando para tras
			velTiro = -1;// seta direcao do tiro - para tras
		} else {
			velTiro = 1; // seta direcao do tiro - para frente
		}
		if (queda || pulo) {
			velY += gravity;
			if (velY > VelocidadeMaxima) {
				velY = VelocidadeMaxima;
			}
		}
		colisoes(objeto);
		jogadorAndandofrente.corridaAnimacao();
		jogadorAndandotras.corridaAnimacao();
	}

	private void colisoes(LinkedList<Objeto_Jogo> objeto) {

		for (int i = 0; i < controlador.objeto.size(); i++) {
			Objeto_Jogo tempObjeto = controlador.objeto.get(i);

			if (tempObjeto.getId() == ObjectId.Bloco) {

				if (getBlocosTopo().intersects(tempObjeto.getBlocos())) {
					y = tempObjeto.getY() + 32;
					velY = 0;
				}
				if (getBlocos().intersects(tempObjeto.getBlocos())) {
					y = tempObjeto.getY() - height;
					velY = 0;
					queda = false;
					pulo = false;
				} else {
					queda = true;
					if (getBlocosDireita().intersects(tempObjeto.getBlocos())) {
						x = tempObjeto.getX() - width;
					}

					if (getBlocosEsquerda().intersects(tempObjeto.getBlocos())) {
						x = tempObjeto.getX() + width - 17;
					}
				}

			}

			/*
			 * if (tempObjeto.getId()== ObjectId.Bala) {}{ Bala_Tiro tempBala =
			 * (Bala_Tiro) controlador.objeto.get(i); if
			 * (getBlocosTopo().intersects(tempBala.getBalaTopo())) { y =
			 * tempObjeto.getY() + 32; velY = 0;
			 * System.out.println("////////////////   MATOU ////////////////");
			 * } if (getBlocos().intersects(tempBala.getDownBalas())) { y =
			 * tempObjeto.getY() - height; velY = 0;
			 * System.out.println("////////////////   MATOU ////////////////");
			 * } else { queda = true; if
			 * (getBlocosDireita().intersects(tempBala.getBalaDireita())) { x =
			 * tempObjeto.getX() - width;
			 * System.out.println("////////////////   MATOU ////////////////");
			 * }
			 * 
			 * if (getBlocosEsquerda().intersects(tempBala.getBalaEsquerda())) {
			 * x = tempObjeto.getX() + width - 17;
			 * System.out.println("////////////////   MATOU ////////////////");
			 * } }
			 * 
			 * 
			 * }
			 */
		}
	}

	public void render(Graphics g) {
		g.setColor(Color.BLUE);

		// de acordo com o metodo tick() acima, podemos saber para que lado está
		// o tiro e mandar desenhar a imagem correspondente

		if (velX < 0) {// ANDANDO para trás
			System.out.println("-0--------------------------anadando tras");
			jogadorAndandotras.drawAnimacao(g, (int) x, (int) y, 48, 64);

		} else if (velX > 0) {// para frente
			jogadorAndandofrente.drawAnimacao(g, (int) x, (int) y, 48, 64);
			System.out.println("------------------------------anadando frente");

		} else { // parado
			if(dir==1 && esq==0){
				g.drawImage(tex.jogador[0], (int) x, (int) y, 48, 64, null);
				System.out.println("oawrbgnrehtsejhtarshaerhareharwehare");
			} else{
				g.drawImage(tex.jogador[3], (int) x, (int) y, 48, 64, null);
			}
			
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
