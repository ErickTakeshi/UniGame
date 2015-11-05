package telas;

import java.awt.Graphics;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import objetos.Bloco;
import framework.ObjectId;
import framework.Objeto_Jogo;

public class Controlador {

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
	}

	public void removeObject(Objeto_Jogo _objeto) {
		this.objeto.remove(_objeto);
	}
		
}

/*
 * 
	public void criaLevel() {
		for (int YY = 0; YY < Jogo.ALT + 32;YY += 32) { // blocos esquerdo do cenario
			addObject(new Bloco(0, YY, ObjectId.Bloco));			
		}
		
		for (int YY =0 ; YY < Jogo.ALT + 32;YY += 32) { // blocos direitos do cenario
			addObject(new Bloco(Jogo.LARG-32, YY, ObjectId.Bloco));			
		}
		
		
		for (int xx = 0; xx < Jogo.LARG *32; xx += 32) { // blocos do chão do cenario
			addObject(new Bloco(xx, Jogo.ALT - 32, ObjectId.Bloco));						
		}
		
		for (int xx = 200; xx < 600; xx += 32) {
			addObject(new Bloco(xx, 355, ObjectId.Bloco));						
		}


 * */
