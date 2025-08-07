
package com.github.matschieu.ftp.client.cli;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * @author Matschieu 
 */
public class FTPClient {

	private Socket controlSocket;
	private Socket dataSocket;
	private Scanner controlIn;
	private PrintStream controlOut;

	/**
	 * Construct a new FTP client
	 * If run has a true value, the start method is called
	 * @param address the address of the server
	 * @param port the port where the server is listening to
	 * @param run run start method ?
	 */
	public FTPClient(String address, int port, boolean run) {
		try {
			System.out.print("> Connection to " + address + " on port " + port + "... ");
			this.controlSocket = new Socket(address, port);
			this.controlIn = new Scanner(this.controlSocket.getInputStream());
			this.controlOut = new PrintStream(this.controlSocket.getOutputStream(), true);
			System.out.println("ok");
			System.out.println(this.controlIn.nextLine());
			if (run)
				this.start();
		}
		catch(IOException e) {
			System.err.println("error : " + e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * Read data on the input stream of a socket and write data on an output stream 
	 * @param socket a socket to read data
	 * @param outStream an output stream to write data
	 */
	private void readFromSocketToStream(Socket socket, OutputStream outStream) {
		try {
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();
			int nread = 0;
			byte[] buffer = new byte[4096];
			os.write(-1);
			while(nread != -1) {
				nread = is.read(buffer);
				if (nread != -1) {
					outStream.write(buffer, 0, nread);
					outStream.flush();
				}
			}
		}
		catch(IOException e) { }
	}

	/**
	 * Listen to the network waiting for an answer from the server
	 */
	private void listenToAnswer() {
		try {
			String answer = "";
			while(!answer.startsWith("221") || !answer.startsWith("421") && !this.controlSocket.isClosed()) {
				answer = this.controlIn.nextLine();
				System.out.println(answer);
				if (answer.startsWith("125") || answer.startsWith("150")) {
					try {
						dataSocket = new Socket(this.controlSocket.getInetAddress(), (this.controlSocket.getPort() - 1));
						PrintStream socketPrintStream = new PrintStream(dataSocket.getOutputStream());
						InputStream socketInputStream = dataSocket.getInputStream();
						Scanner socketScanner = new Scanner(socketInputStream);
						String type = "";
						type = socketScanner.nextLine();
						if (type.equals(FTPClientInfo.LIST_TAG)) {
							this.readFromSocketToStream(dataSocket, System.out);
						}
						else if (type.equals(FTPClientInfo.RETR_TAG)) {
							File f = new File("." + File.separator + socketScanner.nextLine());
							FileOutputStream fout = new FileOutputStream(f);
							this.readFromSocketToStream(dataSocket, fout);
							fout.close();
						}
						else if (type.equals(FTPClientInfo.STOR_TAG)) {
							File file = new File(socketScanner.nextLine());
							if (file.exists()) {
								FileInputStream fin = new FileInputStream(file);
								byte[] buffer = new byte[4096];
								int bytesRead = 0;
								socketPrintStream.write(-1);
								while(bytesRead != -1) {
									bytesRead = fin.read(buffer, 0, buffer.length);
									if (bytesRead != -1) {
										socketPrintStream.write(buffer, 0, bytesRead);
										socketPrintStream.flush();
									}
								}
								fin.close();
							}
							else {
								socketPrintStream.write(0);
								file.delete();
							}
						}
						dataSocket.close();
					} catch(IOException e) { }
				}
			}
			if (!this.controlSocket.isClosed()) {
				this.close();
			}
		}
		catch(NoSuchElementException e) { 
			this.close();
			System.exit(0);
		}
	}

	/**
	 * Waiting for a command written by user and send it to the network
	 */
	private void sendCommand() {
		String ans = "";
		String cmd = "";
		Scanner stdin = new Scanner(System.in);
		while(!ans.startsWith("221") && !this.controlSocket.isClosed()) {
			//System.out.print("# ");
			//System.out.flush();
			cmd = stdin.nextLine();
			if (!cmd.equals("")) {
				this.controlOut.println(cmd);
			}
		}
		stdin.close();
		if (!this.controlSocket.isClosed()) {
			this.close();
		}
	}

	/**
	 * Launch the client CLI
	 */
	public void start() {
		new Thread() { public void run() { sendCommand(); } }.start();
		new Thread() { public void run() { listenToAnswer(); } }.start();
	}

	/**
	 * Close the connection with the server
	 */
	public synchronized void close() {
		try {
			this.controlSocket.close();
			System.out.println("> Connection closed");
		}
		catch(IOException e) { }
	}

	/*====================================================================================*/

	/**
	 * Main method to start the program
	 */
	public static void main(String[] args) {
		if (args.length == 2)
			new FTPClient(args[0], Integer.parseInt(args[1]), true);
		else
			new FTPClient(FTPClientInfo.DEFAULT_ADDR, FTPClientInfo.DEFAULT_PORT, true);
	}

}
