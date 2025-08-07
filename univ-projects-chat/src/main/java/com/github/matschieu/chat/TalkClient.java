package com.github.matschieu.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;

public class TalkClient extends TCPClient {

	public TalkClient(String addr, int port) throws IOException { super(addr, port); }

	@Override
	public void exec() throws IOException {
		try {
			new Thread() {
				@Override
				public void run() {
					try {
						BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						while(!isClosed())
							while(in.ready()) {
								String str = in.readLine();
								System.out.println(str);
							}
						System.out.println(in.readLine());
					}
					catch(IOException e) {  }
				}
			}.start();
			new Thread() {
				@Override
				public void run() {
					try  {
						PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
						Scanner kbin = new Scanner(System.in);
						String str = " ";
						while(!str.equals("bye")) {
							System.out.print("#");
							str = kbin.nextLine();
							out.println(str);
							Thread.sleep(200);
						}
						kbin.close();
						close();
					}
					catch(IOException e) {  }
					catch(InterruptedException e) {  }
				}
			}.start();
		}
		catch(IllegalThreadStateException e) { throw e; }
	}

}
