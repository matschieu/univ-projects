
package com.github.matschieu.ftp.server;

/**
 * @author Matschieu 
 */
public abstract class FTPRequest {

	public static final String USER = "USER";
	public static final String PASS = "PASS";
	public static final String LIST = "LIST";
	public static final String RETR = "RETR";
	public static final String STOR = "STOR";
	public static final String QUIT = "QUIT";
	public static final String PWD  = "PWD";
	public static final String CWD  = "CWD";
	public static final String CDUP = "CDUP";
	public static final String HELP = "HELP";
	
	public static final String LIST_TAG = "<LIST>";
	public static final String RETR_TAG = "<RETR>";
	public static final String STOR_TAG = "<STOR>";
	
}
