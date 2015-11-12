package framework;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;

public abstract class Objeto_Jogo {

	protected float x, y;
	protected ObjectId id;
	protected float velX = 0, velY = 0;
	protected boolean queda = true;
	protected boolean pulo = true;
	protected int enfrentando = 1;
	protected int vida = 100;
	protected int angulo = 0;
	
	protected Objeto_Jogo(float _x, float _y, ObjectId _id) {
		this.x = _x;
		this.y = _y;
		this.id = _id;
	}

	public abstract void tick(LinkedList<Objeto_Jogo> objeto);	
	public abstract void render(Graphics g);
	
	public abstract Rectangle getBlocos();
	
	public float getX(){
		return x;
	}
	public float getY(){
		return y;
	}
	public void setX(float _x){
		this.x = _x;
	}
	public void setY(float _y){
		this.y = _y;
	}
	
	public float getVelX(){
		return velX;
	}
	public  float getVelY(){
		return velY;
	}
	public void setVelX(float d){
		this.velX =  d;
	}
	public void setVelY(float _veloY){
		this.velY = _veloY;
	}
	
	public int getAngulo() {
		return angulo;
	}

	public void setAngulo(int _angulo) {
		this.angulo = _angulo; 
	}
	
	public boolean isQueda() {
		return queda;
	}

	public void setQueda(boolean queda) {
		this.queda = queda;
	}

	public boolean isPulo() {
		return pulo;
	}

	public void setPulo(boolean pulo) {
		this.pulo = pulo;
	}

	public int getEnfretando(){
		return this.enfrentando;
	}
	public ObjectId getId(){
		return id;
	}
	
	
}
