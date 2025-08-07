
package com.github.matschieu.ftp.server;

/**
 * @author Matschieu 
 */
public abstract class FTPServerInfo {

	public static final String INTERNAL_CMD_HELP = "help";
	public static final String INTERNAL_CMD_WHO  = "who";
	public static final String INTERNAL_CMD_FIRE = "fire";
	public static final String INTERNAL_CMD_SEND = "send";
	public static final String INTERNAL_CMD_STOP = "stop";
	
	public static final int    DEFAULT_PORT     = 5287;
	public static final String DEFAULT_ROOT_DIR = "/home";
	public static final String ROOT_DIR_ALIAS   = "/";

}

