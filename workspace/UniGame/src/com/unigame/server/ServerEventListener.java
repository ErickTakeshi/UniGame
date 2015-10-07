package com.unigame.server;

public abstract class ServerEventListener {
	protected abstract void clientConnected(ClientModel client);

	protected abstract void clientDisconnected(ClientModel client);

	protected abstract void packetReceived(ClientModel client,
			com.unigame.packet.Packet packet);

	protected abstract void debugMessage(String msg);

	protected abstract void errorMessage(String msg);
}