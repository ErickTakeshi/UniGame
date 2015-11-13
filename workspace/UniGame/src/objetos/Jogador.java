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
	private Animacao jogadorAndando, jogadorAndandoTras, jogadorAndando1,
			jogadorAndandoTras1, jogadorAndando2, jogadorAndando3,
			jogadorAndandoTras2, jogadorAndandoTras3;
	private LinkedList<Animacao> listaFrente = new LinkedList<Animacao>();
	private int tFrente = 0, tTras = 0;

	public Jogador(float _x, float _y, Controlador _controlador, ObjectId _id) {
		super(_x, _y, _id);
		this.controlador = _controlador;
		jogadorAndando = new Animacao(2, tex.jogador[1]);
		jogadorAndando1 = new Animacao(2, tex.jogador[2]);
		jogadorAndando2 = new Animacao(2, tex.jogador[3]);
		jogadorAndando3 = new Animacao(2, tex.jogador[4]);
		jogadorAndandoTras = new Animacao(2, tex.jogador[5]);
		jogadorAndandoTras1 = new Animacao(2, tex.jogador[6]);
		jogadorAndandoTras2 = new Animacao(2, tex.jogador[7]);
		jogadorAndandoTras3 = new Animacao(2, tex.jogador[8]);

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
		jogadorAndando.corridaAnimacao();
		jogadorAndando1.corridaAnimacao();
		jogadorAndando2.corridaAnimacao();
		jogadorAndando3.corridaAnimacao();
		jogadorAndandoTras.corridaAnimacao();
		jogadorAndandoTras1.corridaAnimacao();
		jogadorAndandoTras2.corridaAnimacao();
		jogadorAndandoTras3.corridaAnimacao();
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
						x = tempObjeto.getX() - width-2;
					}

					if (getBlocosEsquerda().intersects(tempObjeto.getBlocos())) {
						x = tempObjeto.getX() + width-16 ;
					}
				}

			}

			/*
			 * COLISÃO TIRO - JOGADOR X JOGADOR if (tempObjeto.getId()==
			 * ObjectId.Bala) {}{ Bala_Tiro tempBala = (Bala_Tiro)
			 * controlador.objeto.get(i); if
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
		if (velX != 0) {
			tFrente++;
			if (velX > 0) {
				dir = 1;
				esq = 0;

				if (tFrente == 1) {
					jogadorAndando.drawAnimacao(g, (int) x, (int) y, 70, 70);
				} else if (tFrente == 2) {
					jogadorAndando1.drawAnimacao(g, (int) x, (int) y, 70, 70);
				} else if (tFrente == 3) {
					jogadorAndando2.drawAnimacao(g, (int) x, (int) y, 70, 70);
				} else if (tFrente == 4) {
					jogadorAndando3.drawAnimacao(g, (int) x, (int) y, 70, 70);
					tFrente = 0;
				}

			} else {
				tFrente = 0;
				tTras++;
				dir = 0;
				esq = 1;

				if (tTras == 1) {
					jogadorAndandoTras
							.drawAnimacao(g, (int) x, (int) y, 70, 70);
				} else if (tTras == 2) {
					jogadorAndandoTras1.drawAnimacao(g, (int) x, (int) y, 70,
							70);
				} else if (tTras == 3) {
					jogadorAndandoTras2.drawAnimacao(g, (int) x, (int) y, 70,
							70);
				} else if (tTras == 4) {
					jogadorAndandoTras3.drawAnimacao(g, (int) x, (int) y, 70,
							70);
					tTras = 0;
				}
			}
		} else {
			if (dir == 1) {
				g.drawImage(tex.jogador[0], (int) x, (int) y, 70, 70, null);
			} else {
				g.drawImage(tex.jogador[9], (int) x, (int) y, 70, 70, null);
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
