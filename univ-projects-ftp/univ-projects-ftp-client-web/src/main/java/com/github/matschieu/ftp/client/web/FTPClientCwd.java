
package com.github.matschieu.ftp.client.web;

import java.io.IOException;
import java.io.PrintStream;
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
public class FTPClientCwd extends HttpServlet {

	/**
	 * Executes a CWD commande before to redirect to LIST
	 * @param req
	 * @param resp
	 */
	public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String request = FTPClientInfo.CMD_CWD + " " + req.getParameter("filename");
		String response;
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
			Scanner controlIn = new Scanner(controlSocket.getInputStream());
			PrintStream controlOut = new PrintStream(controlSocket.getOutputStream(), true);
			// Send a CWD command to the server to close the connection...
			page.addHistoryRequest(request);
			controlOut.println(request);
			response = controlIn.nextLine();
			returnCode = Integer.parseInt(response.substring(0, 3));
			// ... and test the result
			if (returnCode != 250) {
				page.addHistoryResponseError(response);
				if (returnCode == 530) {
					String errormsg = "You must be logged"; 
					resp.sendRedirect("./FTPClientLogin?errormsg=" + errormsg + "&serveurmsg=" +
							response.substring(4, response.length()));
				}
				else 
					resp.sendRedirect("./FTPClientList");
				return;
			}
			page.addHistoryResponseSuccess(response);
			resp.sendRedirect("./FTPClientList");
		}
		catch(Exception e) {
			// Build an error page
			session.invalidate();
			resp.sendRedirect("./FTPClientList?errormsg=An error occured&servermsg=" + e.getMessage());
		}
	}

}

