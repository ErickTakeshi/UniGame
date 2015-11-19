package framework;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.temporal.Temporal;

import objetos.Bala_Tiro;
import telas.Controlador;
import telas.Jogo;

public class InputTeclado extends KeyAdapter {

	Controlador controlador;
	Objeto_Jogo tempObjeto;
	public InputTeclado(Controlador _controlador) {
		this.controlador = _controlador;
	}	
	int shift = 16;
	int up = 38;
	int down = 40;	
	boolean shiftPressed = false;
	boolean upPressed = false;
	boolean downPressed = false;
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		for (int i = 0; i < controlador.objeto.size(); i++) {
			tempObjeto = controlador.objeto.get(i);
			if (tempObjeto.getId() == ObjectId.Player) {
				
				if (key == shift) {
					shiftPressed = true;
				}
				if (key == up) {
					upPressed = true;
				}
				
				if (key == down) {
					downPressed = true;
				}
				if (shiftPressed) {
					int anguloAtual = tempObjeto.getAngulo();
					System.out.println("Angulo atual:"+ anguloAtual);
					if (upPressed) {
						anguloAtual++;
						System.out.println("Mira Pra cima!");
						
					} else if (downPressed) {
						anguloAtual--;
						System.out.println("Mira Pra baixo!");
						
					}
					tempObjeto.setAngulo(anguloAtual);
					System.out.println("Angulo atual:"+ anguloAtual);
				} else { //shift nao precionado
					
				
					if ((key == KeyEvent.VK_UP) && (tempObjeto.isPulo()) == false) {
						tempObjeto.setVelY(-0.8f);
						tempObjeto.setQueda(true);
						tempObjeto.setPulo(true);						
					}
					
					if (key == KeyEvent.VK_RIGHT) {
						tempObjeto.setVelX(0.7f);
					}
					if (key == KeyEvent.VK_LEFT) {
						tempObjeto.setVelX(-0.7f);
					}
					if (key == KeyEvent.VK_SPACE) {
						int sentido = 0, xx = 51;
						tempObjeto.atira=1;
						if (tempObjeto.getDir() == 1) {
							sentido = 1;
						} else {
							sentido = -1;
							xx = 0;
						}
						//if (!tempObjeto.isPulo()) {
							Bala_Tiro bala = new Bala_Tiro(tempObjeto.getX() + xx,tempObjeto.getY() + 31, controlador, ObjectId.Bala,
									sentido * 2);
							controlador.addObject(bala);
					//	}							
					}
				}
				
			}
		}
		if (key == KeyEvent.VK_ESCAPE) {
			System.exit(1);
		}
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		for (int i = 0; i < controlador.objeto.size(); i++) {
			Objeto_Jogo tempObjeto = controlador.objeto.get(i);
			if (tempObjeto.getId() == ObjectId.Player) {
				if (key == shift) {
					shiftPressed = false;
				}
				if (key == up) {
					upPressed = false;
				}
				
				if (key == down) {
					downPressed = false;
				}
				
				if (key == KeyEvent.VK_UP) {
					tempObjeto.setVelY(0);
				}
				if (key == KeyEvent.VK_DOWN) {
					tempObjeto.setVelY(0);
				}
				if (key == KeyEvent.VK_RIGHT) {
					tempObjeto.setVelX(0);
				}
				if (key == KeyEvent.VK_LEFT) {
					tempObjeto.setVelX(0);
				}	
				if(key == KeyEvent.VK_SPACE){
					tempObjeto.atira=0;
				}
			}
		}
	}
}