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

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		for (int i = 0; i < controlador.objeto.size(); i++) {
			Objeto_Jogo tempObjeto = controlador.objeto.get(i);
			if (tempObjeto.getId() == ObjectId.Player) {

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
					int sentido = 0, xx = 36;
					if (tempObjeto.getDir() == 1) {
						sentido = 1;
					} else {
						sentido = -1;
						xx = 0;
					}
					Bala_Tiro bala = new Bala_Tiro(tempObjeto.getX() + xx,
							tempObjeto.getY() + 20, controlador, ObjectId.Bala,
							sentido * 2);
					controlador.addObject(bala);
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
