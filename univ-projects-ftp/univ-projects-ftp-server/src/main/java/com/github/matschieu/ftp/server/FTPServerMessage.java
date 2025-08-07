
package com.github.matschieu.ftp.server;

/**
 * @author Matschieu 
 */
public class FTPServerMessage {

	/**
	 * Return a message when the client is connected to the server
	 * @return String the message
	 */
	public String welcome() {
		return "220 Welcome, service ready";
	}
	
	/**
	 * Return a message when the command is unknown
	 * @param cmd the command
	 * @return String the message
	 */
	public String unknownCmd(String cmd) {
		return "500 Syntax error, command unrecognized";
	}

	/**
	 * Return a message when there's a syntax error in the command
	 * @param cmd the command
	 * @return String the message
	 */
	public String syntaxError(String cmd) {
		return "501 Syntax error in parameters or arguments";
	}

	/**
	 * Return a message when the client is not connected
	 * @return String the message
	 */
	public String notConnected() {
		return "530 Not logged in";
	}

	/**
	 * Return a message when the client can't be logged
	 * @return String the message
	 */
	public String cannotConnect() {
		return "332 Need account for login";
	}

	/**
	 * Return a message when the client is already connected
	 * @return String the message
	 */
	public String alreadyConnected() {
		return "503 Bad sequence of commands";
	}

	/**
	 * Return a message when the client is logged in
	 * @param username the user name
	 * @return String the message
	 */
	public String userLoggedIn(String username) {
		return "230 User " + username + " logged in";
	}
	
	/**
	 * Return a message when a password is require for a client user
	 * @param username the user name
	 * @return String the message
	 */
	public String passwordRequired(String username) {
		return "331 Password required for " + username;
	}
	
	/**
	 * Return a message when a file doesn't exist or is unavailable
	 * @param filename the file name
	 * @return String the message
	 */
	public String fileUnavailable(String filename) {
		return "450 Requested file action not taken, file unavailable";
	}

	/**
	 * Return a message when the server open a data connection
	 * @return String the message
	 */
	public String openDataConnection() {
		return "150 File status okay; about to open data connection";
	}
	
	/**
	 * Return a message when the server can't open a data connection
	 * @return String the message
	 */
	public String cannotOpenDataConnection() {
		return "425 Can't open data connection";
	}
	
	/**
	 * Return a message when a transfer is completed
	 * @return String the message
	 */	
	public String transferCompleted() {
		return "226 Closing data connection, transfer completed";
	}
	
	/**
	 * Return a message when a transfer is aborted
	 * @return String the message
	 */
	public String transferAborted() {
		return "426 Connection closed; transfer aborted";
	}
	
	/**
	 * Return a message when a client close the connection
	 * @return String the message
	 */
	public String goodbye() {
		return "221 Goodbye";
	}
	
	/**
	 * Return a message when a the current directory name is asked
	 * @param currentDir the current directory
	 * @return String the message
	 */
	public String currentDirectoryInfo(String currentDir) {
		return "257 " + currentDir + " is current directory";
	}
	
	/**
	 * Return a message when a file doesn't exist
	 * @param file the file
	 * @return String the message
	 */
	public String noSuchFile(String file) {
		return "550 " + file + ": No such file or directory";
	}
	
	/**
	 * Return a message when the current directory changes
	 * @param currentDir the current directory
	 * @return String the message
	 */
	public String changeCurrentDirectory(String currentDir) {
		return "250 directory changed to " + currentDir;
	}
	
	/**
	 * Return a message when a command ends correctly
	 * @param cmd the command 
	 * @return String the message
	 */
	public String commandSuccessful(String cmd) {
		return "200 " + cmd + " command successful";
	}
	
	/**
	 * Return a message when the server close the connection
	 * @return String the message
	 */
	public String closeControlConnection() {
		return "421 Closing control connection";
	}
	
}
