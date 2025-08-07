
package com.github.matschieu.ftp.server;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * @author Matschieu 
 */
public class FTPServer {

	private ServerSocket controlServerSocket;
	private ServerSocket dataServerSocket;
	private List<Socket> clientsList;
	private File rootDir;

	/**
	 * Construct and initialize new FTP server
	 * If run has a true value, the start method is called
	 * @param rootDirPath the root directory used by the server to store files
	 * @param port the port on which the server listen to
	 * @param run run the start method ?
	 */
	public FTPServer(String rootDirPath, int port, boolean run) {
		try {
			System.out.print("> Starting FTP server on port " + port + "... ");
			this.controlServerSocket = new ServerSocket(port);
			this.dataServerSocket = new ServerSocket(port -1);
			this.clientsList = new LinkedList<Socket>();
			this.rootDir = new File(rootDirPath);
			System.out.println("ok (root directory is " + rootDirPath + ")");
			if (run)
				this.start();
		}
		catch(IOException e) {
			System.err.println("error: " + e.getMessage());
			System.exit(1);
		}
	}
	
	/**
	 * To know if the server is running
	 * @return true if the server is still running
	 */
	public synchronized boolean isRunning() {
		return !this.controlServerSocket.isClosed();
	}

	/**
	 * Display IP address of the client connected to this server
	 */
	public synchronized void displayClientConnected() {
		Iterator<Socket> it = this.clientsList.iterator();
		int idx = 0;
		if (it.hasNext())
			System.out.println("> Clients connected:");
		else
			System.out.println("> No client connected");
		while(it.hasNext()) {
			Socket s = it.next();
			System.out.println("\t%" + (idx++) + "\t" + s.getInetAddress());
		}
	}

	/**
	 * To close the connection for a specific client
	 * @param cid the client id (index in the list of clients)
	 * @param message the message to send t othe client 
	 */
	public synchronized void sendMessage2Client(int cid, String message) {
		if (cid < 0 || cid >= this.clientsList.size()) {
			System.err.println("> Invalid cid");
			return;
		}
		try {
			Socket socket = this.clientsList.get(cid);
			System.out.print("> Sending message to " + socket.getInetAddress() + "... ");
			message += "\n";
			socket.getOutputStream().write(message.getBytes());
			System.out.println("ok");
		}
		catch(IOException e) { 
			System.err.println("Error : " + e.getMessage());
		}
	}
	
	/**
	 * To close the connection for a specific client
	 * @param cid the client id (index in the list of clients)
	 */
	public synchronized void fireClient(int cid) {
		if (cid < 0 || cid >= this.clientsList.size()) {
			System.err.println("> Invalid cid");
			return;
		}
		try {
			Socket socket = this.clientsList.get(cid);
			System.out.print("> closing connection for " + socket.getInetAddress() + "... ");
			FTPServerMessage ftpMsg = new FTPServerMessage();
			socket.getOutputStream().write(ftpMsg.closeControlConnection().getBytes());
			socket.close();
			this.clientsList.remove(socket);
			System.out.println("ok");
		}
		catch(IOException e) { 
			System.err.println("Error : " + e.getMessage());
		}
		
	}

	/**
	 * Stop the server and notify all clients
	 */
	public synchronized void stop() {
		if (!this.isRunning())
			return;
		try {
			Iterator<Socket> it = clientsList.iterator();
			System.out.print("> Notifying all clients... ");
			while(it.hasNext()) {
				Socket socket = it.next();
				if (socket.isClosed())
					continue;
				FTPServerMessage ftpMsg = new FTPServerMessage();
				socket.getOutputStream().write(ftpMsg.closeControlConnection().getBytes());
				socket.close();
			}
		}
		catch(Exception e) {
			System.err.println("Error : " + e.getMessage());
		}
		try {
			System.out.println("ok");
			System.out.print("> Stopping ftp server... ");
			this.controlServerSocket.close();
			System.out.println("ok");
		}
		catch(IOException e) {
			System.err.println("Error : " + e.getMessage());
		}
	}

	/**
	 * @param clientSocket the socket open by the server with the client
	 */
	private void getRequest(Socket clientSocket) {
		try {
			FTPRequestProcess request = new FTPRequestProcess(clientSocket, this.dataServerSocket, this.rootDir);
			Scanner socketInputStream = new Scanner(clientSocket.getInputStream());
			while(!clientSocket.isClosed()) {
				String cmd = socketInputStream.nextLine();
				System.out.println("> Command [" + cmd + "] received from " + clientSocket.getInetAddress());
				request.processRequest(cmd);	
			}
			socketInputStream.close();
		}
		catch(IOException e) { }
		catch(NoSuchElementException e) { }
		finally {
			synchronized (this.clientsList) {
				this.clientsList.remove(clientSocket);
			}
		}
	}

	/**
	 * Start the server
	 * It will listen to the network to accept new client (new client = new thread)
	 * The process of the commands send by the client is done by an instance of FTPRequest
	 */
	public void start() {
		while(true) {
			Socket socket = null;
			try {
				socket = controlServerSocket.accept();
				final Socket tmpSocket = socket;
				synchronized (this.clientsList) {
					this.clientsList.add(socket);					
				}
				System.out.println("> " + socket.getInetAddress() + " is now connected");
				new Thread() { public void run() { getRequest(tmpSocket); } }.start();
			}
			catch(IOException e) { }
			catch(IllegalThreadStateException e) { 
				synchronized (this.clientsList) {
					this.clientsList.remove(socket);
				}
			}
		}
	}
	
	/**
	 * Run the control prompt of this server
	 */
	public void controlPrompt() {
		Scanner stdin = new Scanner(System.in);
		while(this.isRunning()) {
			System.out.print("# ");
			System.out.flush();
			String cmd = stdin.nextLine();
			if (cmd.equals(FTPServerInfo.INTERNAL_CMD_STOP)) {
				this.stop();
			}
			else if (cmd.equals(FTPServerInfo.INTERNAL_CMD_WHO)) {
				this.displayClientConnected();
			}
			else if (cmd.matches("^" + FTPServerInfo.INTERNAL_CMD_FIRE + "[ ]+[0-9]+$")) {
				StringTokenizer st = new StringTokenizer(cmd, " ");
				st.nextToken();
				this.fireClient(Integer.parseInt(st.nextToken()));
			}
			else if (cmd.matches("^" + FTPServerInfo.INTERNAL_CMD_SEND + "[ ]+[0-9]+[ ]+[A-Za-z0-1/./_/-/?/!/=///\"/ ]+$")) {
				StringTokenizer st = new StringTokenizer(cmd, " ");
				st.nextToken();
				this.sendMessage2Client(Integer.parseInt(st.nextToken()), st.nextToken());
			}
			else if (cmd.equals(FTPServerInfo.INTERNAL_CMD_HELP)) {
				System.out.println("> Server control commands:");
				System.out.println("\t" + FTPServerInfo.INTERNAL_CMD_WHO + ": display who is connected on this server");
				System.out.println("\t" + FTPServerInfo.INTERNAL_CMD_FIRE + " <cid>: fire a client");
				System.out.println("\t" + FTPServerInfo.INTERNAL_CMD_SEND + " <cid> <message>: send a message to a client");
				System.out.println("\t" + FTPServerInfo.INTERNAL_CMD_STOP + ": stop this server and notify all clients");
				System.out.println("\t" + FTPServerInfo.INTERNAL_CMD_HELP + ": display this help message");
			}
			else
				System.err.println("> Invalid command");
		}	
	}

	/*====================================================================================*/

	/**
	 * Main method to start the program
	 */
	public static void main(String[] args) {
		final FTPServer serv;
		if (args.length == 2)
			serv = new FTPServer(args[0], Integer.parseInt(args[1]), false);
		else
			serv = new FTPServer(FTPServerInfo.DEFAULT_ROOT_DIR, FTPServerInfo.DEFAULT_PORT, false);
		new Thread() { public void run() { serv.start(); } }.start();
		serv.controlPrompt();
		System.exit(0);
	}

}

