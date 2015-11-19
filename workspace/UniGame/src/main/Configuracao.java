package main;

import java.net.InetAddress;

public class Configuracao {


	private InetAddress ip;
	private int porta;
	
	public InetAddress getIp() {
		return ip;
	}

	public void setIp(InetAddress ip) {
		this.ip = ip;
	}


	public int getPorta() {
		return porta;
	}

	public void setPorta(int porta) {
		this.porta = porta;
	}
}
