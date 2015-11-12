package objetos;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;

import telas.Controlador;
import telas.Jogo;
import framework.ObjectId;
import framework.Objeto_Jogo;

public class Bala_Tiro extends Objeto_Jogo {
	private Controlador controlador;
	Objeto_Jogo tempObjeto;
	Jogo jogo;
	int width = 10, height = 10;

	public Bala_Tiro(float _x, float _y, Controlador _controlador,
			ObjectId _id, int _velX) {
		super(_x, _y, _id);
		this.velX = _velX;
		this.controlador = _controlador;
	}

	public void tick(LinkedList<Objeto_Jogo> objeto) {
		x += velX;
		y += 0.1;
		colisoes(objeto);
	}

	public void render(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect((int) x, (int) y, width, height);
	}

	public Rectangle getBlocos() {
		return new Rectangle((int) x, (int) y, width, height);
	}

	public Rectangle getDownBalas() {
		return new Rectangle((int) (x + (width / 2) - (width / 2) / 2),
				(int) (y + (height / 2)), (int) width / 2, (int) height / 2);
	}

	public Rectangle getBalaTopo() {
		return new Rectangle((int) (x + (width / 2) - (width / 2) / 2),
				(int) y, (int) width / 2, (int) height / 2);
	}

	public Rectangle getBalaDireita() {
		return new Rectangle((int) (x + width - 5), (int) y + 5, (int) 5,
				(int) height - 10);
	}

	public Rectangle getBalaEsquerda() {
		return new Rectangle((int) x, (int) y + 5, (int) 5, (int) height - 10);
	}

	private void colisoes(LinkedList<Objeto_Jogo> objeto) {

		for (int i = 0; i < controlador.objeto.size(); i++) {
			Objeto_Jogo tempObjeto = controlador.objeto.get(i);
			if (tempObjeto.getId() == ObjectId.Bloco) {

				if (getBalaTopo().intersects(tempObjeto.getBlocos())) {
					velY = 0;
					controlador.removeObject(tempObjeto);
				}
				if (getDownBalas().intersects(tempObjeto.getBlocos())) {
					velY = 0;
					controlador.removeObject(tempObjeto);
				} else {
					queda = true;
					if (getBalaDireita().intersects(tempObjeto.getBlocos())) {
						velX = 0;
						controlador.removeObject(tempObjeto);
						x = tempObjeto.getX() - width;
					}
					if (getBalaEsquerda().intersects(tempObjeto.getBlocos())) {
						velX = 0;
						x = tempObjeto.getX() + width - 17;
						controlador.removeObject(tempObjeto);
					}
				}
			} else if (tempObjeto.getId() == ObjectId.Bala) {
				if (tempObjeto.getY() > 620 || tempObjeto.getX() > 2000
						|| tempObjeto.getX() < 0) {
					controlador.removeObject(tempObjeto);
					System.out
							.println("bala removida foi para fora do cenario");
				}
			}
		}
	}
}