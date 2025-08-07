
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

import java.io.*;
import java.util.*;
import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.util.*;
import org.apache.commons.fileupload.servlet.*;
import org.apache.commons.fileupload.disk.*;

/**
 * @author Matschieu
 */
public class FTPClientStore extends HttpServlet {

	/**
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
		// Get the socket
		Socket controlSocket = (Socket)session.getAttribute("socket");
		if (controlSocket == null) {
			resp.sendRedirect("./FTPClientConnection");
			return;
		}
		// Build the page to upload a file
		FTPClientHTMLPage page = new FTPClientHTMLPage(resp, session, "FTPClient", true);
		page.addContentLine("<span class=\"bold\">File upload</span><br /><br />");
		page.addContentLine("You have to select a file on your own computer to upload to the server<br /><br />");
		page.addContentLine(page.getUploadForm("FTPClientStore"));
		if (errorMessage != null) {
			page.addErrorMessage(errorMessage, serverMessage);
			page.addErrorPanel();
		}
		page.addHistoryPanel();
		page.addContentPanel();
		page.close();
	}

	/**
	 * @param req
	 * @param resp
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String request, response, currentDir;
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
			// Check that we have a file upload request
			boolean isMultipart = ServletFileUpload.isMultipartContent(req);
			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload();
			// Parse the request
			FileItemIterator iter = upload.getItemIterator(req);
			FileItemStream item = iter.next();
			if (item.isFormField()) {
				resp.sendRedirect("./FTPClientStore");
				return;
			}
			FTPClientHTMLPage page = new FTPClientHTMLPage(resp, session, "FTPClient", true);
			Scanner controlIn = new Scanner(controlSocket.getInputStream());
			PrintStream controlOut = new PrintStream(controlSocket.getOutputStream(), true);
			// Send a STOR command to send the content of the file...
			request = FTPClientInfo.CMD_STOR + " " + item.getName();
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
			InputStream stream = item.openStream();
			// Open data channel and recept the list result
			try {
				dataSocket = new Socket(controlSocket.getInetAddress(), (controlSocket.getPort() - 1));
				PrintStream socketPrintStream = new PrintStream(dataSocket.getOutputStream());
				Scanner socketScanner = new Scanner(dataSocket.getInputStream());
				socketScanner.nextLine();
				byte[] buffer = new byte[4096];
				int bytesRead = 0;
				socketPrintStream.write(-1);
				while(bytesRead != -1) {
					bytesRead = stream.read(buffer, 0, buffer.length);
					if (bytesRead != -1) {
						socketPrintStream.write(buffer, 0, bytesRead);
						socketPrintStream.flush();
					}
				}
				stream.close();
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
			String msg = "File " + item.getName() + " successfully loaded in current directory"; 
			resp.sendRedirect("./FTPClientList?msg=" + msg);
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

