package objetos;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;

import telas.Jogo;
import framework.ObjectId;
import framework.Objeto_Jogo;
import framework.Texturas;

public class Bloco extends Objeto_Jogo {

	Texturas tex = Jogo.getInstance();
	private int type;

	public Bloco(float _x, float _y, int _type, ObjectId _id) {
		super(_x, _y, _id);
		this.type = _type;
	}

	public void tick(LinkedList<Objeto_Jogo> objeto) {

	}

	public void render(Graphics g) {
		if (type == 0) {// Terra
			g.drawImage(tex.block[type], (int) x, (int) y, null);
			System.out.println(" -------------  terra -----------");
		} else if (type == 1) {// grama bloco
			g.drawImage(tex.block[type], (int) x, (int) y, null);
			System.out.println(" -------------  grama -----------");
		} else {// grama bloco
			g.drawImage(tex.block[type], (int) x, (int) y, null);
			System.out.println(" -------------  grama -----------");
		}
	}

	// BLOCOS ABAIXO DA TELA
	public Rectangle getBlocos() {
		return new Rectangle((int) x, (int) y, 32, 32);
	}

}
