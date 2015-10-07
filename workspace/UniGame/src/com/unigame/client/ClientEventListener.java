package com.unigame.client;

public abstract class ClientEventListener {
	protected abstract void clientConnected(ServerModel server);

	protected abstract void clientDisconnected(ServerModel server);

	protected abstract void packetReceived(ServerModel server,
			com.unigame.packet.Packet packet);

	protected abstract void debugMessage(String msg);

	protected abstract void errorMessage(String msg);
}