package telas;

import framework.Objeto_Jogo;

public class Camera {
	private float x, y;

	public Camera(float _x, float _y) {
		this.y = _x;
		this.y = _y;
	}

	public void tick(Objeto_Jogo player) {
	//	x = -player.getX() + Jogo.WIDTH + 32;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
}