
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
public class FTPClientConnection extends HttpServlet {

	/**
	 * Displays a form to ask the server and port
	 * @param req
	 * @param resp
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String errorMessage = req.getParameter("errormsg");
		String serverMessage = req.getParameter("servermsg");
		FTPClientHTMLPage page;
		// Get the session
		HttpSession session = req.getSession();
		if (FTPClientInfo.MAGIC.equals((Integer)session.getAttribute("magic")) && session.getAttribute("history") != null) {
			// Build the page for a logged user
			page = new FTPClientHTMLPage(resp, session, "FTPClient", true);
			page.addContentLine(page.getConnectionForm("FTPClientConnection"));
			if (errorMessage != null) {
				page.addErrorMessage(errorMessage, serverMessage);
				page.addErrorPanel();
			}
			page.addHistoryPanel();
			page.addContentPanel();
			page.close();
		}
		else {
			// Build a page for an unlogged user
			page = new FTPClientHTMLPage(resp, session, "FTPClient", false);
			page.addContentLine(page.getConnectionForm("FTPClientConnection"));
			if (errorMessage != null) {
				page.addErrorMessage(errorMessage, serverMessage);
				page.addErrorPanel();
			}
			page.addContentPanel();
			page.close();
		}
	}

	/**
	 * Tries to establish a connection with address/port specified by user
	 * @param req
	 * @param resp
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String hostAddress = req.getParameter("server");
		String hostPort = req.getParameter("port");
		HttpSession session = req.getSession();
		try {
			// Get the soket
			Socket controlSocket = new Socket(hostAddress, Integer.parseInt(hostPort));
			Scanner controlIn = new Scanner(controlSocket.getInputStream());
			PrintStream controlOut = new PrintStream(controlSocket.getOutputStream(), true);
			// Get the welcome message from the server
			String response = controlIn.nextLine();
			// Build the page
			FTPClientHTMLPage page = new FTPClientHTMLPage(resp, session, "FTPClient", true);
			page.addHistoryResponseSuccess(response);
			// Add information in the session
			session.setAttribute("socket", controlSocket);
			session.setAttribute("history", page.getHistory());
			session.setAttribute("magic", FTPClientInfo.MAGIC);
			// Now the user can be logged
			resp.sendRedirect("./FTPClientLogin");

		}
		catch(Exception e) {
			// Build an error page
			session.invalidate();
			resp.sendRedirect("./FTPClientConnection?errormsg=Connection impossible&servermsg=" + e.getMessage());
		}
	}

}

