
package com.github.matschieu.ftp.client.web;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.StringTokenizer;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Matschieu
 */
public class FTPClientHTMLPage extends HTMLPage {

	private StringBuffer error;
	private StringBuffer success;
	private StringBuffer content;
	private CircularStringList history;

	/**
	 * Creates a new FTP HTML page and initializes some values
	 * @param resp
	 * @param session
	 * @param pageTitle
	 * @param showMenu
	 */
	public FTPClientHTMLPage(HttpServletResponse resp, HttpSession session, String pageTitle, boolean showMenu) throws IOException {
		super(resp, false);
		this.error = new StringBuffer();
		this.success = new StringBuffer();
		this.content = new StringBuffer();
		if (session != null && session.getAttribute("history") != null)
			this.history = (CircularStringList)session.getAttribute("history");
		else 
			this.history = new CircularStringList();
		this.htmlCode.append(getHTMLHeader(pageTitle, DEFAULT_AUTHOR, "../../../ftp_styles.css"));
		this.htmlCode.append("\t\t<div id=\"mainPanel\">\n");
		this.htmlCode.append("\t\t\t<div id=\"header\">\n");
		this.addImagePanel();
		if (showMenu)
			this.addMenuPanel();
		this.htmlCode.append("\t\t\t</div>\n");
	}

	/**
	 * Adds the header image to this page
	 */
	public void addImagePanel() {
		StringBuffer str = new StringBuffer();
		str.append("\t\t\t\t<div id=\"imagePanel\">\n");
		str.append("\t\t\t\t\t<img src=\"../../../images/ftp.png\" alt=\"\" />\n");
		str.append("\t\t\t\t</div>\n");
		this.htmlCode.append(str.toString());
	}

	/**
	 * Adds the menu to this page
	 */
	public void addMenuPanel() {
		StringBuffer str = new StringBuffer();
		str.append("\t\t\t\t<div id=\"menuPanel\">\n");
		str.append("\t\t\t\t\t<a href=\"./FTPClientStore\" class=\"link\">Store a file</a> | \n");
		str.append("\t\t\t\t\t<a href=\"./FTPClientList\" class=\"link\">Show current directory</a> | \n");
		str.append("\t\t\t\t\t<a href=\"./FTPClientLogin\" class=\"link\">Change user</a> | \n");
		str.append("\t\t\t\t\t<a href=\"./FTPClientDeconnection\" class=\"link\">Deconnection</a>\n");
		str.append("\t\t\t\t</div>\n");
		this.htmlCode.append(str.toString());
	}

	/**
	 * Adds the history panel to this page
	 */
	public void addHistoryPanel() {
		StringBuffer str = new StringBuffer();
		str.append("\t\t\t<div id=\"historyTitle\">\n");
		str.append("\t\t\tHistory\n");
		str.append("\t\t\t</div>\n");
		str.append("\t\t\t<div id=\"historyPanel\">\n");
		str.append(this.history.toString());
		str.append("\t\t\t</div>\n");
		this.htmlCode.append(str.toString());
	}

	/**
	 * Adds the error message panel to this page
	 */
	public void addErrorPanel() {
		StringBuffer str = new StringBuffer();
		str.append("\t\t\t<div id=\"errorPanel\">\n");
		str.append("\t\t\t<img src=\"../../../images/error.png\" alt=\"\" class=\"icon\" />\n");
		str.append(this.error.toString() + "\n");
		str.append("\t\t\t</div>\n");
		this.htmlCode.append(str.toString());
	}

	/**
	 * Adds the information message panel to this page
	 */
	public void addSuccessPanel() {
		StringBuffer str = new StringBuffer();
		str.append("\t\t\t<div id=\"successPanel\">\n");
		str.append("\t\t\t<img src=\"../../../images/success.png\" alt=\"\" class=\"icon\" />\n");
		str.append(this.success.toString() + "\n");
		str.append("\t\t\t</div>\n");
		this.htmlCode.append(str.toString());
	}

	/**
	 * Adds the page content panel to this page
	 */
	public void addContentPanel() {
		StringBuffer str = new StringBuffer();
		str.append("\t\t\t<div id=\"contentPanel\">\n");
		str.append(this.content.toString() + "\n");
		str.append("\t\t\t</div>\n");
		this.htmlCode.append(str.toString());
	}

	/**
	 * Adds a request line to the history panel of this page
	 * @param message the message
	 */
	public void addHistoryRequest(String message) {
		this.history.add("\t\t\t\t<span class=\"historyRequest\">" + message + "</span><br />");
	}

	/**
	 * Adds a success response line to the history panel of this page
	 * @param message the message
	 */
	public void addHistoryResponseSuccess(String message) {
		this.history.add("\t\t\t\t<span class=\"historyResponseSuccess\">" + message + "</span><br />");
	}

	/**
	 * Adds a error response line to the history panel of this page
	 * @param message the message
	 */
	public void addHistoryResponseError(String message) {
		this.history.add("\t\t\t\t<span class=\"historyResponseError\">" + message + "</span><br />");
	}

	/**
	 * Adds an error message with a title to the error panel of this page
	 * @param title the title
	 * @param message the error message
	 * @param serverMessage an error message from the server (can be null)
	 */
	public void addErrorMessage(String title, String message, String serverMessage) {
		this.error.append("\t\t\t\t<span class=\"errorTitle\">" + title + "</span><br />\n");
		this.error.append("\t\t\t\t<span class=\"errorMessage\">" + message + "</span><br />\n");
		if (serverMessage != null)
			this.error.append("\t\t\t\t<span class=\"errorMessage\">Server has returned: " + serverMessage + "</span><br />\n");
	}

	/**
	 * Adds an error message to the error panel of this page
	 * @param message the error message
	 * @param serverMessage an error message from the server (can be null)
	 */
	public void addErrorMessage(String message, String serverMessage) {
		this.addErrorMessage("Error...", message, serverMessage);
	}

	/**
	 * Adds an error message to the error panel of this page
	 * @param message the error message
	 */
	public void addErrorMessage(String message) {
		this.addErrorMessage("Error...", message, null);
	}

	/**
	 * Adds an information message with a title to the information panel of this page
	 * @param title the title
	 * @param message the information message
	 * @param serverMessage an information message from the server (can be null)
	 */
	public void addSuccessMessage(String title, String message, String serverMessage) {
		this.success.append("\t\t\t\t<span class=\"successTitle\">" + title + "</span><br />\n");
		this.success.append("\t\t\t\t<span class=\"successMessage\">" + message + "</span><br />\n");
		if (serverMessage != null)
			this.success.append("\t\t\t\t<span class=\"successMessage\">Server has returned: " + serverMessage + "</span><br />\n");
	}

	/**
	 * Adds an information message to the information panel of this page
	 * @param message the information message
	 * @param serverMessage an information message from the server (can be null)
	 */
	public void addSuccessMessage(String message, String serverMessage) {
		this.addSuccessMessage("Success !", message, serverMessage);
	}

	/**
	 * Adds an information message to the information panel of this page
	 * @param message the information message
	 */
	public void addSuccessMessage(String message) {
		this.addSuccessMessage("Success !", message, null);
	}

	/**
	 * Adds a content to content error panel of this page
	 * @param content a content
	 */
	public void addContentLine(String content) {
		this.content.append(content + "\n");
	}

	/**
	 * Returns the content of this page
	 * @return String the content
	 */
	public String getContent() {
		return this.content.toString();
	}

	/**
	 * Returns the error message of this page
	 * @return String the error message
	 */
	public String getError() {
		return this.error.toString();
	}

	/**
	 * Returns the error information of this page
	 * @return String the information message
	 */
	public String getInfo() {
		return this.success.toString();
	}

	/**
	 * Returns the history list of this page
	 * @return CircularStringList the history list
	 */
	public CircularStringList getHistory() {
		return this.history;
	}

	/**
	 * Adds the footer of the page and close the stream
	 */
	public void close() {
		this.htmlCode.append("\t\t\t<div id=\"footer\">\n");
		this.htmlCode.append("\t\t\t\t<a href=\"http://validator.w3.org/check?uri=referer\">\n");
		this.htmlCode.append("\t\t\t\t<img src=\"http://www.w3.org/Icons/valid-xhtml10-blue\" alt=\"Valid XHTML 1.0 Strict\" style=\"border:0;width:88px;height:31px\" />\n");
		this.htmlCode.append("\t\t\t\t</a>\n");
		this.htmlCode.append("\t\t\t\t<a href=\"http://jigsaw.w3.org/css-validator/check/referer\">\n");
		this.htmlCode.append("\t\t\t\t<img src=\"http://jigsaw.w3.org/css-validator/images/vcss-blue\" alt=\"CSS Valide !\" style=\"border:0;width:88px;height:31px\" />\n");
		this.htmlCode.append("\t\t\t\t</a>\n");
		this.htmlCode.append("\t\t\t</div>\n");
		this.htmlCode.append("\t\t</div>\n");
		super.close();
	}

	/**
	 * Returns a connection form
	 * @param action action the url of action
	 * @return String the HTML form
	 */
	public static String getConnectionForm(String action) {
		StringBuffer str = new StringBuffer();
		str.append("\t\t\t\t<form method=\"post\" action=\"./" + action + "\">\n");
		str.append("\t\t\t\t\t<table border=\"0\" cellpadding=\"2\" cellspacing=\"2\">\n");
		str.append("\t\t\t\t\t\t<tr>\n");
		str.append("\t\t\t\t\t\t\t<td>Server : </td>\n");
		str.append("\t\t\t\t\t\t\t<td><input name=\"server\" value=\"" + FTPClientInfo.DEFAULT_ADDR + "\" /></td>\n");
		str.append("\t\t\t\t\t\t</tr>\n");
		str.append("\t\t\t\t\t\t<tr>\n");
		str.append("\t\t\t\t\t\t\t<td>Port : </td>\n");
		str.append("\t\t\t\t\t\t\t<td><input name=\"port\" value=\"" + FTPClientInfo.DEFAULT_PORT + "\" /></td>\n");
		str.append("\t\t\t\t\t\t</tr>\n");
		str.append("\t\t\t\t\t\t<tr>\n");
		str.append("\t\t\t\t\t\t\t<td></td>\n");
		str.append("\t\t\t\t\t\t\t<td><input value=\"Connection\" type=\"submit\" /></td>\n");
		str.append("\t\t\t\t\t\t</tr>\n");
		str.append("\t\t\t\t\t</table>\n");
		str.append("\t\t\t\t</form>\n");
		return str.toString();
	}

	/**
	 * Returns a login form
	 * @param action action the url of action
	 * @return String the HTML form
	 */
	public static String getLoginForm(String action) {
		StringBuffer str = new StringBuffer();
		str.append("\t\t\t\t<form method=\"post\" action=\"./" + action + "\">\n");
		str.append("\t\t\t\t\t<table border=\"0\" cellpadding=\"2\" cellspacing=\"2\">\n");
		str.append("\t\t\t\t\t\t<tr>\n");
		str.append("\t\t\t\t\t\t\t<td>Login : </td>\n");
		str.append("\t\t\t\t\t\t\t<td><input name=\"login\" /></td>\n");
		str.append("\t\t\t\t\t\t</tr>\n");
		str.append("\t\t\t\t\t\t<tr>\n");
		str.append("\t\t\t\t\t\t\t<td>Password : </td>\n");
		str.append("\t\t\t\t\t\t\t<td><input name=\"password\" type=\"password\" /></td>\n");
		str.append("\t\t\t\t\t\t</tr>\n");
		str.append("\t\t\t\t\t\t<tr>\n");
		str.append("\t\t\t\t\t\t\t<td></td>\n");
		str.append("\t\t\t\t\t\t\t<td><input value=\"Send\" type=\"submit\" /></td>\n");
		str.append("\t\t\t\t\t\t</tr>\n");
		str.append("\t\t\t\t\t</table>\n");
		str.append("\t\t\t\t</form>\n");
		return str.toString();
	}

	public static String getUploadForm(String action) {
		StringBuffer str = new StringBuffer();
		str.append("\t\t\t\t<form method=\"post\" action=\"./" + action + "\" enctype=\"multipart/form-data\">\n");
		str.append("\t\t\t\t\t<input type=\"file\" name=\"filename\" />\n");
		str.append("\t\t\t\t\t<input value=\"Upload\" type=\"submit\" />\n");
		str.append("\t\t\t\t</form>\n");
		return str.toString();
	}

	/**
	 * Converts a PWD result to a directory name
	 * @param response the server response of PWD
	 * @return String the current working directory name
	 */
	public static String getWorkingDir(String response) {
		try {
			StringTokenizer stk = new StringTokenizer(response, " ", false);
			stk.nextToken();
			return stk.nextToken();
		}
		catch (NoSuchElementException e) { }
		return null;
	}

	/**
	 * Converts a LIST result to a tree that can be add to a HTML page
	 * @param list the files list return by the server
	 * @return String a HTML version of the list
	 */
	public static String getTree(String list, String currentDir) {
		StringBuffer str = new StringBuffer();
		BufferedReader reader = new BufferedReader(new StringReader(list));
		String tmp = "";
		str.append("\t\t\t\t<div class=\"listRow1\">\n");
		str.append("\t\t\t\t\t<img src=\"../../../images/open_folder.png\" alt=\"\" />");
		str.append("<a href=\"./FTPClientList\" class=\"link\">.</a><br />\n");
		str.append("\t\t\t\t</div>\n");
		str.append("\t\t\t\t<div class=\"listRow2\">\n");
		str.append("\t\t\t\t\t<img src=\"../../../images/folder.png\" alt=\"\" />");
		str.append("<a href=\"./FTPClientCdup\" class=\"link\">..</a><br />\n");
		str.append("\t\t\t\t</div>\n");
		int i = 0;
		while(tmp != null) {
			try {
				tmp = reader.readLine();
				if (tmp == null)
					continue;
				StringTokenizer stk = new StringTokenizer(tmp, "\t", false);
				String type = stk.nextToken();
				int size = Integer.parseInt(stk.nextToken());
				stk.nextToken();
				String fileName = stk.nextToken();
				char c = type.charAt(0);
				str.append("\t\t\t\t<div class=\"listRow" + (i % 2 == 0 ? "1" : "2")  + "\">\n");
				if (c == 'd') {
					str.append("\t\t\t\t\t<img src=\"../../../images/folder.png\" alt=\"\" class=\"icon\" />");
					str.append("<a href=\"./FTPClientCwd?filename=" + fileName + "\" class=\"link\">");
					str.append("<span class=\"bold\">" + fileName + "</span></a><br />" + size + " bytes<br />\n");
				}
				else {
					str.append("\t\t\t\t\t<img src=\"../../../images/file.png\" alt=\"\" class=\"icon\" />");
					str.append("<a href=\"./FTPClientRetr?filename=" + fileName + "\" class=\"link\">");
					str.append("<span class=\"bold\">" + fileName + "</span></a><br />" + size + " bytes<br />\n");
				}
				str.append("\t\t\t\t</div>\n");
				i++;
			}
			catch (IOException e) { }
			catch (NoSuchElementException e) { }
			catch (NumberFormatException e) { }
		}
		return str.toString();
	}

}

