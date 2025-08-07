package com.github.matschieu.chat;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public abstract class TCPClient {

	protected Socket socket;

	public TCPClient(String addr, int port) throws IOException {
		try {
			socket = new Socket(InetAddress.getByName(addr), port);
		}
		catch(UnknownHostException e) {
			throw new IOException(">> Erreur : hote inconnu");
		}
		catch(Exception e) {
			throw new IOException(">> Erreur : impossible de se connecter");
		}
	}

	public void listen() {
		try {
			System.out.println(">> Connecte au serveur");
			exec();
		}
		catch(IOException e) {
		}
	}

	public abstract void exec() throws IOException;

	public boolean isClosed() {
		return socket.isClosed();
	}

	public void close() throws IOException {
		socket.close();
		System.out.println(">> Deconnecte du serveur");
	}

	public static void main(String[] args) {
		try {
			TCPClient c = new TalkClient("localhost", 50287);
			c.listen();
		}
		catch(IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}
}
