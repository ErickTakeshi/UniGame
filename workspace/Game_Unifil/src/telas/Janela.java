package telas;

import java.awt.Dimension;

import javax.swing.JFrame;

public class Janela {
	
	/**
	 * Este construtor cria uma janela/tela do jogo através dos parâmetros recebidos.
	 * E inicializa o jogo
	 *k */
	
	public Janela(int largura, int altura, String titulo, Jogo jogo) {
		jogo.setPreferredSize(new Dimension(largura,altura));
		jogo.setMaximumSize(new Dimension(largura,altura));
		jogo.setMinimumSize(new Dimension(largura,altura));		
		JFrame frame = new JFrame(titulo);
		frame.add(jogo);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
				
		jogo.start();// inicializa
	}
}
