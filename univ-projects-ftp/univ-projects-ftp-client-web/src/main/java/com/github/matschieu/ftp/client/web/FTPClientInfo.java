
package com.github.matschieu.ftp.client.web;

/*
 * @author Matschieu
 */
public abstract class FTPClientInfo {

	protected static final Integer MAGIC = new Integer(0xDEADBEEF); 

	public static final String APP_PATH = "tpcar";

	public static final int    DEFAULT_PORT = 5287;
	public static final String DEFAULT_ADDR = "127.0.0.1";

	public static final String CMD_USER = "USER";
	public static final String CMD_PASS = "PASS";
	public static final String CMD_LIST = "LIST";
	public static final String CMD_RETR = "RETR";
	public static final String CMD_STOR = "STOR";
	public static final String CMD_QUIT = "QUIT";
	public static final String CMD_PWD  = "PWD";
	public static final String CMD_CWD  = "CWD";
	public static final String CMD_CDUP = "CDUP";
	public static final String CMD_HELP = "HELP";

	public static final String LIST_TAG = "<LIST>";
	public static final String RETR_TAG = "<RETR>";
	public static final String STOR_TAG = "<STOR>";

}

