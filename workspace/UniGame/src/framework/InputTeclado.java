package framework;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.temporal.Temporal;

import objetos.Bala_Tiro;
import telas.Controlador;
import telas.Jogo;

public class InputTeclado extends KeyAdapter {

	Controlador controlador;

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
			Objeto_Jogo tempObjeto = controlador.objeto.get(i);
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
						tempObjeto.setVelY(-1.5f);
						tempObjeto.setPulo(true);
						tempObjeto.setQueda(true);
					}
	
					// if((key == KeyEvent.VK_DOWN||key == KeyEvent.VK_X)){
					// tempObjeto.setVelY(-2);
					// }
	
					if (key == KeyEvent.VK_RIGHT) {
						tempObjeto.setVelX(1f);
					}
					if (key == KeyEvent.VK_LEFT) {
						tempObjeto.setVelX(-1f);
					}
					if (key == KeyEvent.VK_SPACE) {
						if (!tempObjeto.isPulo()) {
							controlador.addObject(new Bala_Tiro(tempObjeto.getX() + 36,
									tempObjeto.getY() + 32, controlador, ObjectId.Bala,
									tempObjeto.getEnfretando() * 2));
						}
						
	
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
				
			}

		}
	}

}
