package telas;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Random;

import objetos.Bloco;
import objetos.Jogador;
import framework.InputTeclado;
import framework.ObjectId;
import framework.Objeto_Jogo;
import framework.Texturas;

public class Jogo extends Canvas implements Runnable {
	private static final long serialVersionUID = -8704436632459519948L;
	private boolean JogoRodando = false;
	private Thread thread;
	public static int LARG, ALT;
	private BufferedImage level = null,nuvem=null;

	// objeto
	Controlador controlador;
	Camera cam;
	static Texturas tex;	
	Random rand = new Random();

	private void init() {
		LARG = getWidth();
		ALT = getHeight();
		tex = new Texturas();
		BufferedImageLoader loader = new BufferedImageLoader();		
		level = loader.loadImage("/level.png");//carrega o level
		 //nuvem = loader.loadImage("/nuvem.png");
		//explosao = loader.loadImage("/explosao.gif");
		controlador = new Controlador();
		cam = new Camera(0,0);		
		LoadImageLevel(level);		
		this.addKeyListener(new InputTeclado(controlador));
	}

	public synchronized void start() {
		if (JogoRodando) {
			return;
		}
		JogoRodando = true;
		thread = new Thread(this);
		thread.start();
	}

	public void run() {
		init();
		long ultimoTempo = System.nanoTime();
		double tick = 60;
		double ns = 190000000 / tick;
		double delta = 0;
		long tempo = System.currentTimeMillis();
		int atualizacoes = 0;
		int frames = 0;
		while (JogoRodando) {
			long agoraT = System.nanoTime();
			delta += (agoraT - ultimoTempo) / ns;
			ultimoTempo = agoraT;
			while (delta >= 1) {
				tick();
				atualizacoes++;
				delta--;
			}
			render();
			frames++;
			if (System.currentTimeMillis() - tempo > 1000) {
				tempo += 1000;
				//System.out.println("FPS: " + frames + " Tick: " + atualizacoes);
				frames = 0;
				atualizacoes = 0;
			}
		}
	}

	// escala loop atualiza��es
	private void tick() {
		controlador.tick();
		for (int i = 0; i < controlador.objeto.size(); i++) {
			if(controlador.objeto.get(i).getId()==ObjectId.Player){
				cam.tick(controlador.objeto.get(i));
			}
		}
		//cam.tick();
	}

	// imagem/fundo/grafico
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);// basic 3 windows
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		Graphics2D g2d = (Graphics2D)g; // camera
		
		// ------------ desenha aqui --------------------
		g.setColor(new Color(25,191,224)); // cor de fundo
		g.fillRect(0, 0, LARG, ALT);
		//g.drawImage(nuvem, 0, 0, this); // nuvem
		g2d.translate(cam.getX()+150,cam.getY()); // inicio da camera		
		//for (int xx = 0; xx <nuvem.getWidth()*7; xx+=nuvem.getWidth()) {
		//	g.drawImage(nuvem, xx+xx, 50, this);
		//}
		//g.drawImage(nuvem, 0, 50, this);
		
		controlador.render(g);	
			
		g2d.translate(-cam.getX()+100,-cam.getY()); // fim da camera
		// ///////////////////////////////
		g.dispose();
		bs.show();
	}

	
	
	private void LoadImageLevel(BufferedImage image){
		int w =image.getWidth();
		int h = image.getHeight();
		//System.out.println("largura, altura: "+w+", "+h);
		
		for (int xx = 0; xx < h; xx++) {
			for (int yy = 0; yy < w; yy++) {
				int pixel = image.getRGB(xx, yy);
				int red = (pixel>>16)&0xff;
				int green = (pixel>>8)&0xff;
				int blue = (pixel)&0xff;
				if(red==255 && green ==255 && blue ==255){
					controlador.addObject(new Bloco(xx*32,yy*32,0, ObjectId.Bloco));
				}
				if(red==50 && green ==50 && blue ==50){
					//System.out.println("cria Objeto");
					controlador.addObject(new Jogador(xx*32,yy*32,controlador, ObjectId.Player));
				}							
				
			}
			
		}
	}
	
	public static Texturas getInstance(){
		return tex;
	}
	
	public static void main(String args[]) {
		new Janela(1200,650, "Game Unifil - Grupo UniGame", new Jogo());
	}
}
