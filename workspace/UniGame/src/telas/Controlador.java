package telas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;

import javax.swing.JOptionPane;

import objetos.Bloco;
import objetos.Jogador;
import framework.ObjectId;
import framework.Objeto_Jogo;
import framework.SpriteSheet;

public class Controlador {
	private static final Graphics Graphics2D = null;
	private int count;
	public LinkedList<Objeto_Jogo> objeto = new LinkedList<Objeto_Jogo>();
	private Objeto_Jogo tempObjeto;
	private SpriteSheet bs, ps;
	Jogador j;
	Animacao anime;

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
		System.out.println("qtd: tiro: " + objeto.size());
	}

	public void removeObject(Objeto_Jogo _objeto) {
		this.objeto.remove(_objeto);
		this.objeto.removeLast();
	}
}
