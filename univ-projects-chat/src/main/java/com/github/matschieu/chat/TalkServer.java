package com.github.matschieu.chat;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * @author Matschieu
 */
public class TalkServer extends TCPServer {

	public TalkServer(int port) throws IOException {
		super(port, "[Serveur de discussion - Mathieu]");
	}

	@Override
	public void exec(Socket socket) throws IOException {
		PrintStream out = new PrintStream(socket.getOutputStream(), true);
		Scanner sc = new Scanner(socket.getInputStream());
		String str = "";
		out.println("Bienvenue sur MonServeur !");
		while(!str.equals("bye") && !isClosed()) {
			try  {
				str = sc.nextLine();
			}
			catch(NoSuchElementException e) { break; }
			if (str.equals("")) continue;
			Iterator<Socket> it = connected.iterator();
			while(it.hasNext()) {
				Socket sckt = it.next();
				PrintStream ps = new PrintStream(sckt.getOutputStream(), true);
				ps.println("De " + socket.getInetAddress() + " : " + str);
			}
		}
		sc.close();
		out.close();
	}

}

