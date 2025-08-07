
package com.github.matschieu.ftp.client.web;

import java.io.IOException;
import java.io.PrintStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Matschieu
 */
public class FTPClientList extends HttpServlet {

	/**
	 * Executes a LIST command and display the result
	 * @param req
	 * @param resp
	 */
	public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String errorMessage = req.getParameter("errormsg");
		String serverMessage = req.getParameter("servermsg");
		String msg = req.getParameter("msg");
		String response, currentDir;
		Socket dataSocket = null;
		int returnCode;
		// Get the session
		HttpSession session = req.getSession();
		if (!FTPClientInfo.MAGIC.equals((Integer)session.getAttribute("magic"))) {
			resp.sendRedirect("./FTPClientConnection");
			return;
		}
		// Get the socket
		Socket controlSocket = (Socket)session.getAttribute("socket");
		if (controlSocket == null) {
			resp.sendRedirect("./FTPClientConnection");
			return;
		}
		try {
			FTPClientHTMLPage page = new FTPClientHTMLPage(resp, session, "FTPClient", true);
			// Welcoming message if the previous page was the login
			if (msg != null) {
				page.addSuccessMessage(msg);
				page.addSuccessPanel();
			}
			// evantually an error message
			if (errorMessage != null) {
				page.addErrorMessage(errorMessage, serverMessage);
				page.addErrorPanel();
			}
			Scanner controlIn = new Scanner(controlSocket.getInputStream());
			PrintStream controlOut = new PrintStream(controlSocket.getOutputStream(), true);
			// Send a PWD command to get the current working directory...
			page.addHistoryRequest(FTPClientInfo.CMD_PWD);
			controlOut.println(FTPClientInfo.CMD_PWD);
			response = controlIn.nextLine();
			returnCode = Integer.parseInt(response.substring(0, 3));
			// ... and test the result
			if (returnCode == 530) {
				page.addHistoryResponseError(response);
					String errormsg = "You must be logged"; 
					resp.sendRedirect("./FTPClientLogin?errormsg=" + errormsg + "&serveurmsg=" +
							response.substring(4, response.length()));
				return;
			}
			page.addHistoryResponseSuccess(response);
			currentDir = page.getWorkingDir(response);
			page.addContentLine("\t\t\t\t<span class=\"bold\">Navigation in " + currentDir + "</span><br />");
			// Send a LIST command to get the files list...
			page.addHistoryRequest(FTPClientInfo.CMD_LIST);
			controlOut.println(FTPClientInfo.CMD_LIST);
			response = controlIn.nextLine();
			returnCode = Integer.parseInt(response.substring(0, 3));
			// ... and test the result
			if (returnCode != 150) {
				page.addHistoryResponseError(response);
				if (returnCode == 530) {
					String errormsg = "You must be logged"; 
					resp.sendRedirect("./FTPClientLogin?errormsg=" + errormsg + "&serveurmsg=" +
							response.substring(4, response.length()));
					return;
				}
				else {
					String errormsg = "Problem occured"; 
					resp.sendRedirect("./FTPClientLogin?errormsg=" + errormsg + "&serveurmsg=" +
							response.substring(4, response.length()));
					return;
				}
			}
			page.addHistoryResponseSuccess(response);
			// Open data channel and recept the list result
			StringBuffer str = new StringBuffer();
			try {
				dataSocket = new Socket(controlSocket.getInetAddress(), (controlSocket.getPort() - 1));
				OutputStream dataOutputStream = dataSocket.getOutputStream();
				InputStream dataInputStream = dataSocket.getInputStream();
				Scanner dataSocketScanner = new Scanner(dataInputStream);
				String type = "";
				type = dataSocketScanner.nextLine();
				if (type.equals(FTPClientInfo.LIST_TAG)) {
					int nread = 0;
					byte[] buffer = new byte[4096];
					dataOutputStream.write(-1);
					while(nread != -1) {
						nread = dataInputStream.read(buffer);
						if (nread != -1) {
							str.append(new String(buffer, 0, nread - 1));
						}
					}
				}
				dataSocket.close();
			} 
			catch(IOException e) { 
				if (dataSocket != null && !dataSocket.isClosed())
					dataSocket.close();
				page.addErrorMessage("Problem occured: " + e.getMessage());
				page.addErrorPanel();
			}
			// Get the last response
			response = controlIn.nextLine();
			returnCode = Integer.parseInt(response.substring(0, 3));
			if (returnCode != 226) {
				page.addHistoryResponseError(response);
				String errormsg = "Transfer aborted"; 
				resp.sendRedirect("./FTPClientLogin?errormsg=" + errormsg + "&serveurmsg=" +
						response.substring(4, response.length()));
				return;
			}
			page.addHistoryResponseSuccess(response);
			// Build the HTML page
			page.addContentLine(page.getTree(str.toString(), currentDir));
			page.addHistoryPanel();
			page.addContentPanel();
			page.close();
		}
		catch(Exception e) {
			if (dataSocket != null && !dataSocket.isClosed())
				dataSocket.close();
			// Build an error page
			session.invalidate();
			resp.sendRedirect("./FTPClientList?errormsg=An error occured&servermsg=" + e.getMessage());
		}
	}

}

