package server;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ThreadSocket implements Runnable {

	private Socket cliente;
	
	public Socket getCliente() {
		return cliente;
	}

	public ThreadSocket(Socket cliente) {
		this.cliente = cliente;
	}

	@Override
	public void run() {
		System.out.println("Nova conexao com o cliente " + this.cliente.getInetAddress().getHostAddress());

		try {
			Scanner s = null;
			s = new Scanner(this.cliente.getInputStream());

//			// Exibe mensagem no console
//			while (s.hasNextLine()) {
//				
//			}

			// Finaliza objetos
//			s.close();
//			this.cliente.close();
			
			while (true) TCPServer.enviar("asas");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
