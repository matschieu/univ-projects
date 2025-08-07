
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
public class FTPClientDeconnection extends HttpServlet {

	/**
	 * Unlogs an user by sending a QUIT command to the server and delete the session
	 * @param req
	 * @param resp
	 */
	public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Get the session
		HttpSession session = req.getSession();
		if (!FTPClientInfo.MAGIC.equals((Integer)session.getAttribute("magic"))) {
			resp.sendRedirect("FTPClientConnection");
			return;
		}
		// Get the socket
		Socket controlSocket = (Socket)session.getAttribute("socket");
		if (controlSocket == null) {
			resp.sendRedirect("FTPClientConnection");
			return;
		}
		try {
			FTPClientHTMLPage page = new FTPClientHTMLPage(resp, session, "FTPClient", false);
			Scanner controlIn = new Scanner(controlSocket.getInputStream());
			PrintStream controlOut = new PrintStream(controlSocket.getOutputStream(), true);
			// Send a QUIT command to the server to close the connection
			controlOut.println(FTPClientInfo.CMD_QUIT);
			page.addHistoryRequest(FTPClientInfo.CMD_QUIT);
			page.addHistoryResponseSuccess(controlIn.nextLine());
			// Build the HTML page
			page.addSuccessMessage("You're now deconnected");
			page.addContentLine(page.getConnectionForm("FTPClientConnection"));
			page.addSuccessPanel();
			page.addHistoryPanel();
			page.addContentPanel();
			page.close();
			// End of the session
			session.removeAttribute("magic");
			session.invalidate();
		}
		catch(Exception e) {
			// Build an error page
			session.invalidate();
			resp.sendRedirect("./FTPClientConnection?errormsg=An error occured&servermsg=" + e.getMessage());
		}
	}

}

