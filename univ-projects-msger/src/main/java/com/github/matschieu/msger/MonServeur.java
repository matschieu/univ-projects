package com.github.matschieu.msger;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author Matschieu
 */
public class MonServeur extends TCPServer {

	public MonServeur(int port) throws IOException {
		super(port, "[MonServeur RSX-TP1]");
	}

	@Override
	public void exec(Socket socket) throws IOException {
		PrintStream out = new PrintStream(socket.getOutputStream(), true);
		Scanner sc = new Scanner(socket.getInputStream());
		String str = "";
		out.println("Bienvenue sur MonServeur !");
		while(!str.equals("bye")) {
			out.print("# ");
			str = sc.nextLine();
			if (str.equals("")) continue;
			Iterator<Socket> it = connected.iterator();
			while(it.hasNext()) {
				Socket sckt = it.next();
				PrintStream ps = new PrintStream(sckt.getOutputStream(), true);
				ps.println("From " + socket.getInetAddress() + " : " + str);
			}
		}
		sc.close();
		out.close();
	}

}

