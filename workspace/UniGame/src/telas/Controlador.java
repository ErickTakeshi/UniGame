package telas;

import java.awt.Graphics;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import objetos.Bloco;
import framework.ObjectId;
import framework.Objeto_Jogo;

public class Controlador {
	private int count;
	public LinkedList<Objeto_Jogo> objeto = new LinkedList<Objeto_Jogo>();
	private Objeto_Jogo tempObjeto;

	public void tick() {
		for (int i = 0; i < objeto.size(); i++) {
			tempObjeto = objeto.get(i);
			tempObjeto.tick(objeto);
		}
	}
	
	public void render(Graphics g) {
		for (int i = 0; i < objeto.size(); i++) {
			tempObjeto = objeto.get(i);
			tempObjeto.render(g);
		}
	}

	public void addObject(Objeto_Jogo _objeto) {					
		this.objeto.add(_objeto);
		//System.out.println("qtd: tiro: " + objeto.size());
	}

	public void removeObject(Objeto_Jogo _objeto) {
		this.objeto.remove(_objeto);
		this.objeto.removeLast();
	}
}
