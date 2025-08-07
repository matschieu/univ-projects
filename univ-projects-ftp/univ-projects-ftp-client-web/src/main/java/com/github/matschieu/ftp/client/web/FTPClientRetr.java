
package com.github.matschieu.ftp.client.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
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
public class FTPClientRetr extends HttpServlet {

	/**
	 * @param req
	 * @param resp
	 */
	public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String request, response;
		Socket dataSocket = null;
		int returnCode;
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
			FTPClientHTMLPage page = new FTPClientHTMLPage(null, session, "FTPClient", true);
			Scanner controlIn = new Scanner(controlSocket.getInputStream());
			PrintStream controlOut = new PrintStream(controlSocket.getOutputStream(), true);
			// Send a RETR command to get the content of the file...
			request = FTPClientInfo.CMD_RETR + " " + req.getParameter("filename");
			page.addHistoryRequest(request);
			controlOut.println(request);
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
					resp.sendRedirect("./FTPClientList?errormsg=" + errormsg + "&serveurmsg=" +
							response.substring(4, response.length()));
					return;
				}
			}
			page.addHistoryResponseSuccess(response);
			// Get the content of the file
			try {
				dataSocket = new Socket(controlSocket.getInetAddress(), (controlSocket.getPort() - 1));
				InputStream dataInputStream = dataSocket.getInputStream();
				OutputStream dataOutputStream = dataSocket.getOutputStream();
				Scanner dataSocketScanner = new Scanner(dataInputStream);
				String type = "";
				type = dataSocketScanner.nextLine();
				if (type.equals(FTPClientInfo.RETR_TAG)) {
					// Prepare the file
					String filename = dataSocketScanner.nextLine();
					resp.setContentType("application/octet-stream");
					resp.setHeader("Content-Disposition", "attachment;filename=" + filename);
					resp.setHeader("Cache-Control", "private, must-revalidate");
					OutputStream out = resp.getOutputStream();
					int nread = 0;
					dataOutputStream.write(-1);
					while((nread = dataInputStream.read()) != -1) {
						out.write(nread);
					}
					out.flush();
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
				resp.sendRedirect("./FTPClientList?errormsg=" + errormsg + "&serveurmsg=" +
						response.substring(4, response.length()));
				return;
			}
			page.addHistoryResponseSuccess(response);
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

