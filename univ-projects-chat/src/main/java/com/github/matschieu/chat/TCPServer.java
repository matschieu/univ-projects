package com.github.matschieu.chat;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Matschieu
 */
public abstract class TCPServer {

	protected ServerSocket servSock;
	protected List<Socket> connected;
	protected FileWriter logFileWriter;
	protected final String serverType;
	public static final String SERVER_LOG_FILE = "server.log";
	private static final boolean WRITE_IN_LOG = true;

	public TCPServer(int port) throws IOException { this(port, "[serveur TCP]"); }
	public TCPServer(int port, String type) throws IOException {
		serverType = type;
		try {
			logFileWriter = new FileWriter(new File(SERVER_LOG_FILE), true);
			print(">> " + (new Date()) + ": Chargement du service " + serverType + "... ", WRITE_IN_LOG);
			servSock = new ServerSocket(port);
			connected = new LinkedList<Socket>();
			println("OK", WRITE_IN_LOG);
		}
		catch(IOException e) {
			println("Erreur", WRITE_IN_LOG);
			printError(e.getMessage(), WRITE_IN_LOG);
			e.printStackTrace();
			System.exit(1);
		}
	}

	protected void printError(String str, boolean log) {
		try {
			if (log) {
				logFileWriter.write(str);
				logFileWriter.flush();
			}
		}
		catch(IOException e) { }
		System.err.println(str);
	}

	protected void println(String str, boolean log) {
		print(str + "\n", log);
	}

	protected void print(String str, boolean log) {
		try {
			if (log) {
				logFileWriter.write(str);
				logFileWriter.flush();
			}
		}
		catch(IOException e) { }
		System.out.print(str);
	}

	public void listen() throws IOException, IllegalThreadStateException {
		try {
			final Socket socket = servSock.accept();
			connected.add(socket);
			new Thread() {
				@Override
				public void run() {
					print(">> " + (new Date()) + ": Connexion du client ", WRITE_IN_LOG);
					println("[" + socket.getInetAddress() + ":" + socket.getLocalPort() + "]", WRITE_IN_LOG);
					try {
						exec(socket);
						connected.remove(socket);
						socket.close();
					}
					catch(IOException e) {
						System.err.println(e.getMessage());
						e.printStackTrace();
					}
					print(">> " + (new Date()) + ": Deconnexion du client ", WRITE_IN_LOG);
					println("[" + socket.getInetAddress() + ":" + socket.getLocalPort() + "]", WRITE_IN_LOG);
				}
			}.start();
		}
		catch(IllegalThreadStateException e) {
			printError(e.getMessage(), WRITE_IN_LOG);
			e.printStackTrace();
		}
	}

	public abstract void exec(Socket socket) throws IOException;

	public boolean isClosed() {
		return servSock.isClosed();
	}

	public void close() throws IOException {
		Iterator<Socket> it = connected.iterator();
		while(it.hasNext()) it.next().close();
		println(">> " + (new Date()) + ": Fin du service " + serverType, WRITE_IN_LOG);
		logFileWriter.close();
		servSock.close();
	}

	public void exitPrompt() {
		new Thread() {
			@Override
			public void run() {
				Scanner sc = new Scanner(System.in);
				String str = "";
				while(!str.equals("quit")) {
					System.out.print("# ");
					str = sc.nextLine();
				}
				try {
					sc.close();
					close();
				}
				catch(IOException e) { }
			}
		}.start();
	}

	public static void main(String[] args) {
		try {
			TCPServer s = new TalkServer(50287);
			s.exitPrompt();
			while(!s.isClosed()) s.listen();
		}
		catch(Exception e) { System.exit(1); }
	}

}


