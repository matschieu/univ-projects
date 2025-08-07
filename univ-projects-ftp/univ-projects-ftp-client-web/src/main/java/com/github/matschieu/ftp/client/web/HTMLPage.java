
package com.github.matschieu.ftp.client.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.ObjectOutputStream;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Matschieu
 */
public class HTMLPage {

	/** Default values */
	public static final String DEFAULT_AUTHOR     = "Mathieu DIETSCH";
	public static final String DEFAULT_PAGE_TITLE = "FTP client";
	public static final String DEFAULT_CSS_FILE   = "";

	protected StringBuffer htmlCode;
	protected PrintWriter out;
	private boolean headerAdded;

	/**
	 * Create a new HTML page
	 * @param resp HttpServletResponse to get the PrintWriter
	 * @param addHeader true if the HTML header must be added now
	 * @throws IOException
	 */
	public HTMLPage(HttpServletResponse resp, boolean addHeader) throws IOException {
		this.htmlCode = new StringBuffer();
		if (resp != null) {
			resp.setContentType("text/html");
			this.out = resp.getWriter();
		}
		if (addHeader) {
			this.htmlCode.append(getHTMLHeader(DEFAULT_PAGE_TITLE, DEFAULT_AUTHOR, DEFAULT_CSS_FILE));
			this.headerAdded = true;
		}
		else {
			this.headerAdded = false;
		}
	}

	/**
	 * Add the footer to the HTML page and close the HTML page PrintWriter
	 */
	public void close() {
		this.htmlCode.append(getHTMLFooter());
		if (this.out != null) {
			this.out.println(this.htmlCode.toString());
			this.out.close();
		}
	}

	/**
	 * Returns a HTML header with title and metadata
	 * Also open the body tag
	 * @param pageTitle the title of the HTML page
	 * @param authorName the name of the author of the HTML page
	 * @param cssFileName the name of the CSS file used in the HTML page
	 * @return String containing the HTML header
	 */
	public static String getHTMLHeader(String pageTitle, String authorName, String cssFileName) {
		StringBuffer str = new StringBuffer();
		str.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n");
		str.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"fr\" lang=\"fr\">\n");
		str.append("\t<head>\n");
		str.append("\t\t<title>" + pageTitle+ "</title>\n");
		str.append("\t\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n");
		str.append("\t\t<meta name=\"Author\" lang=\"fr\" content=\"" + authorName + "\" />\n");
		if (cssFileName != null) {
			str.append("\t\t<link href=\"" + cssFileName + "\" rel=\"stylesheet\" type=\"text/css\" />\n");
		}
		str.append("\t</head>\n");
		str.append("\t<body>\n");
		return str.toString();
	}

	/**
	 * Returns a correct HTML footer
	 * Also close the body tag
	 * @return String containing the HTML footer
	 */
	public static String getHTMLFooter() {
		StringBuffer str = new StringBuffer();
		str.append("\t</body>\n");
		str.append("</html>\n");
		return str.toString();
	}

	/**
	 * Adds the HTML header with title and metadata to this HTML page
	 * @param pageTitle the title of the HTML page
	 * @param authorName the name of the author of the HTML page
	 * @param cssFileName the name of the CSS file used in the HTML page
	 */
	public void addHTMLHeader(String pageTitle, String authorName, String cssFileName) {
		this.htmlCode.append(getHTMLHeader(pageTitle, authorName, cssFileName));
		this.headerAdded = true;
	}

	/**
	 * Add a comment in this HTML page code
	 * @param comment the comment to add in this HTML page code
	 */
	public void addComment(String comment) {
		this.htmlCode.append(comment + "\n");
	}

	/**
	 * Print a line in this HTML page
	 * @param str the string to add in this HTML page
	 */
	public void println(String str) {
		this.htmlCode.append(str + "\n");
	}

}

