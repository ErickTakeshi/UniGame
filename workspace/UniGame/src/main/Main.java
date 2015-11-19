package main;

import java.awt.Dimension;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JFrame;

import server.TCPServer;

public class Main {

	public static void main(String[] args) {
		
		Tela t = new Tela();
		t.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		t.setVisible(true);
	}
}
