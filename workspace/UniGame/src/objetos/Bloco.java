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
		this.type =_type;
	}

	public void tick(LinkedList<Objeto_Jogo> objeto) {

	}

	public void render(Graphics g) {
		if(type==2){// BLOCO GRAMA
			g.drawImage(tex.block[type],(int)x,(int) y, null);
		}
		
		if(type==1){// Terra bloco
			g.drawImage(tex.block[type],(int)x,(int) y, null);
		}
		if(type==0){// Terra CONCRETO SINZA
			g.drawImage(tex.block[type],(int)x,(int) y, null);
		}

	}

	// BLOCOS ABAIXO DA TELA
	public Rectangle getBlocos() {
		return new Rectangle((int) x, (int) y, 32, 32);
	}

}
