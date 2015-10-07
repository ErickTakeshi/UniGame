package com.unigame;

import com.unigame.client.ClientEventListener;
import com.unigame.client.GNetClient;
import com.unigame.client.ServerModel;
import com.unigame.packet.Packet;

public class ClientTest {

	/**
	 * Main entry point into the application.
	 * 
	 * @param args
	 *            Arguments passed to the application.
	 */
	public static void main(final String[] args) {

		// Host to connect to:
		final String host = "127.0.0.1";

		// Port # to connect to the host on:
		final int port = 43594;

		// Setup our client.
		final GNetClient networkedClient = new GNetClient(host, port);

		// Add our event listener to manage events.
		networkedClient.addEventListener(new ClientEventListener() {

			@Override
			protected void clientConnected(final ServerModel server) {
				System.out.println("Conectado ao server: " + server.toString());
			}

			@Override
			protected void clientDisconnected(final ServerModel server) {
				
			}

			@Override
			protected void packetReceived(final ServerModel server,
					final Packet packet) {
			}

			@Override
			protected void debugMessage(final String msg) {
			}

			@Override
			protected void errorMessage(final String msg) {
			}
		});

		// Attempt to bind the client.
		networkedClient.bind();

		// Once binded, finally start our client.
		networkedClient.start();
	}
}