package client;

import java.io.*;
import java.net.*;

import javax.swing.JOptionPane;

import main.Configuracao;

public class TCPClient implements Runnable {
	
	private Configuracao conf;
	
	public TCPClient(Configuracao conf) {
		this.conf = conf;
	}
	Socket skt;
   public void run() {
      try {
         skt = new Socket(conf.getIp(), conf.getPorta());
         BufferedReader in = new BufferedReader(new
            InputStreamReader(skt.getInputStream()));
         System.out.print("Received string: '");

         while (!in.ready()) {}
         System.out.println(in.readLine()); // Read one line and output it

         System.out.print("'\n");
      }
      catch(Exception e) {
         JOptionPane.showMessageDialog(null, "Não foi possível estabeçecer conexão");
      }
   }
   
   public Socket getSocket() {
	   return this.skt;
   }
}
