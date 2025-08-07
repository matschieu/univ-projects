
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
public class FTPClientLogin extends HttpServlet {

	/**
	 * Displays a form to ask the login and password
	 * @param req
	 * @param resp
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String errorMessage = req.getParameter("errormsg");
		String serverMessage = req.getParameter("servermsg");
		// Get the session
		HttpSession session = req.getSession();
		if (!FTPClientInfo.MAGIC.equals((Integer)session.getAttribute("magic"))) {
			resp.sendRedirect("./FTPClientConnection");
			return;
		}
		// Build the page
		FTPClientHTMLPage page = new FTPClientHTMLPage(resp, session, "FTPClient", true);
		page.addContentLine(page.getLoginForm("FTPClientLogin"));
		if (errorMessage != null) {
			page.addErrorMessage(errorMessage, serverMessage);
			page.addErrorPanel();
		}
		page.addHistoryPanel();
		page.addContentPanel();
		page.close();
	}

	/**
	 * Tries to log the user with a login/password specified by user
	 * @param req
	 * @param resp
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String login = req.getParameter("login");
		String password = req.getParameter("password");
		String requestUser = FTPClientInfo.CMD_USER + " " + login;
		String requestPass = FTPClientInfo.CMD_PASS + " " + password;
		String response;
		int returnCode;
		// Get the session
		HttpSession session = req.getSession();
		if (!FTPClientInfo.MAGIC.equals((Integer)session.getAttribute("magic"))) {
			resp.sendRedirect("./FTPClientConnection");
			return;
		}
		// Get the soket
		Socket controlSocket = (Socket)session.getAttribute("socket");
		if (controlSocket == null) {
			resp.sendRedirect("./FTPClientConnection");
			return;
		}
		try {
			Scanner controlIn = new Scanner(controlSocket.getInputStream());
			PrintStream controlOut = new PrintStream(controlSocket.getOutputStream(), true);
			// Send a USER command to the server...
			FTPClientHTMLPage page = new FTPClientHTMLPage(resp, session, "FTPClient", true);
			page.addHistoryRequest(requestUser);
			controlOut.println(requestUser);
			response = controlIn.nextLine();
			returnCode = Integer.parseInt(response.substring(0, 3));
			// ...and test the result
			if (returnCode != 230 && returnCode != 331) {
				page.addHistoryResponseError(response);
				String errormsg = "Invalid login \"" + login + "\""; 
				resp.sendRedirect("./FTPClientLogin?errormsg=" + errormsg + "&serveurmsg=" +
						response.substring(4, response.length()));
				return;
			}
			page.addHistoryResponseSuccess(response);
			if (returnCode == 230) {
				String msg = "You're now logged as " + login; 
				resp.sendRedirect("./FTPClientList?msg=" + msg);
				session.setAttribute("login", login);
				return;
			}
			// Send a PASS request to the server...
			page.addHistoryRequest(requestPass);
			controlOut.println(requestPass);
			response = controlIn.nextLine();
			returnCode = Integer.parseInt(response.substring(0, 3));
			// ...and test the result
			if (returnCode != 230) {
				page.addHistoryResponseError(response);
				String errormsg = "Invalid password for \"" + login + "\""; 
				resp.sendRedirect("./FTPClientLogin?errormsg=" + errormsg + "&serveurmsg=" +
						response.substring(4, response.length()));
				return;
			}
			page.addHistoryResponseSuccess(response);
			String msg = "You're now logged as " + login; 
			resp.sendRedirect("./FTPClientList?msg=" + msg);
			session.setAttribute("login", login);
		}
		catch(Exception e) {
			// Build an error page
			session.invalidate();
			resp.sendRedirect("./FTPClientLogin?errormsg=An error occured&servermsg=" + e.getMessage());
		}
	}
}

