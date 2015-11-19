package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import main.Configuracao;

public class TCPServer implements Runnable {
	
	public TCPServer(Configuracao conf) {
		this.conf = conf;
	}
	
	private Configuracao conf;
	public static List<ThreadSocket> lista;
	
	public static synchronized void enviar(String a) {
		for (ThreadSocket t : lista) {
			PrintWriter out;
			try {
				out = new PrintWriter(t.getCliente().getOutputStream(), true);
				out.print(a);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void run() {
		lista = new ArrayList<ThreadSocket>();
		try {
			ServerSocket srvr = new ServerSocket(conf.getPorta());
			while (true) {
				if (lista.size() < 2) {
					ThreadSocket skt = new ThreadSocket(srvr.accept());
					Thread t = new Thread(skt);
					t.start();
					System.out.print("Server has connected!\n");
					lista.add(skt);
				} else {
					srvr.close();
				}
			}
//				PrintWriter out = new PrintWriter(skt.getOutputStream(), true);
//				System.out.print("Sending string: '" + data + "'\n");
//				out.print(data);
//				out.close();
//				skt.close();
//				srvr.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.print("Whoops! It didn't work!\n");
		}
	}
}
