
package com.github.matschieu.ftp.server;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * @author Matschieu 
 */
public class FTPRequestProcess extends FTPRequest {

	public static final int DATA_SERVER_SCK_TIMEOUT = 3000;

	private final String[] users = {"anonymous", "mathieu"};
	private final String[] pwds  = {null, "1234"};

	private ServerSocket dataServerSocket;
	private Socket controlSocket;
	private PrintStream controlOut;
	private File rootDir;
	private File currentDir;
	private int currentUser;
	private boolean userConnected;
	private FTPServerMessage ftpMessages;

	/**
	 * Create an object to process all command send by the client
	 * @param socket the socket with the client
	 * @param rootDir the rootDir of the server to store files
	 */
	public FTPRequestProcess(Socket socket, ServerSocket dataServerSocket, File rootDir) {
		try {
			this.controlSocket = socket;
			this.dataServerSocket = dataServerSocket;
			this.controlOut = new PrintStream(socket.getOutputStream(), true);
			this.rootDir = rootDir;
			this.currentDir = rootDir;
			this.currentUser = -1;
			this.ftpMessages = new FTPServerMessage();
			this.dataServerSocket.setSoTimeout(DATA_SERVER_SCK_TIMEOUT);
			this.controlOut.println(this.ftpMessages.welcome());
		}
		catch(IOException e) {
			System.err.println("> Error : " + e.getMessage());
		}
	}

	/**
	 * Test if a command is valid
	 * @param cmd the command
	 * @param regexp a regular expression that represent the correct command
	 * @return true if the command is valid
	 */
	private boolean isValidSyntax(String cmd, String regexp) {
		if (!cmd.matches(regexp))
			return false;
		return true;
	}

	/**
	 * Replace the root directory path in the curreny directory path by /
	 * @return String the path on the server
	 */
	private String getCurrentDirPath() {
		try {
			String currentDirPath = this.currentDir.getCanonicalPath();
			String rootDirPath = this.rootDir.getCanonicalPath();
			if (currentDirPath.matches("^" + rootDirPath + "$"))
				return currentDirPath.replace(rootDirPath, FTPServerInfo.ROOT_DIR_ALIAS);
			else 
				return currentDirPath.replace(rootDirPath, "");
		}
		catch(IOException e) {
			return this.currentDir.getPath();
		}
	}

	/**
	 * Process called when the client sent a command
	 * @param cmd the command sent by the client
	 * @return true if the command is correct and the process ends correctly
	 */
	public boolean processRequest(String cmd) {
		if (cmd.startsWith(USER))
			return this.processUSER(cmd);
		else if (cmd.startsWith(PASS))
			return this.processPASS(cmd);
		else if (cmd.startsWith(LIST))
			return this.processLIST(cmd);
		else if (cmd.startsWith(RETR))
			return this.processRETR(cmd);
		else if (cmd.startsWith(STOR))
			return this.processSTOR(cmd);
		else if (cmd.startsWith(QUIT)) 
			return this.processQUIT(cmd);
		else if (cmd.startsWith(CWD)) 
			return this.processCWD(cmd);
		else if (cmd.startsWith(PWD)) 
			return this.processPWD(cmd);
		else if (cmd.startsWith(CDUP)) 
			return this.processCDUP(cmd);
		else if (cmd.startsWith(HELP)) 
			return this.processHELP(cmd);
		else {
			this.controlOut.println(this.ftpMessages.unknownCmd(cmd));	
			return false;
		}
	}

	/**
	 * Process called when the client sent a USER command
	 * @param cmd the command sent by the client
	 * @return true if the command is correct and the process ends correctly
	 */
	public boolean processUSER(String cmd) {
		StringTokenizer st = new StringTokenizer(cmd, " ", false);
		String usr;
		boolean usrValid = false;
		if (!this.isValidSyntax(cmd, "^" + USER + "[ ]+[A-Za-z0-9]+$")) {
			this.controlOut.println(this.ftpMessages.syntaxError(cmd));
			return false;
		}
		if (this.userConnected) 
			this.userConnected = false;
		st.nextToken();
		usr = st.nextToken();
		for(int i = 0; i < users.length; i++) {
			if (usr.equals(users[i])) {
				this.currentUser = i;
				usrValid = true;
				break;
			}
		}
		if (!usrValid) {
			this.controlOut.println(this.ftpMessages.cannotConnect());
			return false;
		}
		if (this.currentUser == 0) {
			this.userConnected = true;
			this.controlOut.println(this.ftpMessages.userLoggedIn(users[this.currentUser]));
		}
		else
			this.controlOut.println(ftpMessages.passwordRequired(users[this.currentUser]));
		return true;
	}

	/**
	 * Process called when the client sent a PASS command
	 * @param cmd the command sent by the client
	 * @return true if the command is correct and the process ends correctly
	 */
	public boolean processPASS(String cmd) {
		StringTokenizer st = new StringTokenizer(cmd, " ", false);
		String pwd;
		if (!this.isValidSyntax(cmd, "^" + PASS + "[ ]+[A-Za-z0-9]+$")) {
			this.controlOut.println(this.ftpMessages.syntaxError(cmd));
			return false;
		}
		if (this.userConnected) {
			this.controlOut.println(this.ftpMessages.alreadyConnected());
			return false;
		}
		if (this.currentUser < 0) {
			this.controlOut.println(this.ftpMessages.cannotConnect());
			return false;
		}
		st.nextToken();
		pwd = st.nextToken();
		if (!pwd.equals(pwds[this.currentUser])) {
			this.controlOut.println(this.ftpMessages.cannotConnect());
			return false;
		}
		this.userConnected = true;
		this.controlOut.println(this.ftpMessages.userLoggedIn(users[this.currentUser]));
		return true;
	}

	/**
	 * Process called when the client sent a LIST command
	 * @param cmd the command sent by the client
	 * @return true if the command is correct and the process ends correctly
	 */
	public boolean processLIST(String cmd) {
		if (!this.isValidSyntax(cmd, "^" + LIST + "$")) {
			this.controlOut.println(this.ftpMessages.syntaxError(cmd));
			return false;
		}
		if (!this.userConnected) {
			this.controlOut.println(this.ftpMessages.notConnected());
			return false;
		}
		if (!this.currentDir.exists() || !this.currentDir.isDirectory()) {
			this.controlOut.println(this.ftpMessages.fileUnavailable(this.getCurrentDirPath()));
			return false;
		}
		if (dataServerSocket != null && dataServerSocket.isClosed()) {
			this.controlOut.println(this.ftpMessages.cannotOpenDataConnection());
			return false;
		}
		this.controlOut.println(this.ftpMessages.openDataConnection());
		try {
			Socket s = this.dataServerSocket.accept();
			PrintStream ps = new PrintStream(s.getOutputStream());
			File[] directories = currentDir.listFiles(new FileFilter() {
				public boolean accept(File pathname) {
					return pathname.isDirectory();
				}
			});
			File[] files = currentDir.listFiles(new FileFilter() {
				public boolean accept(File pathname) {
					return !pathname.isDirectory();
				}
			});
			try {
				Arrays.sort(directories);
				Arrays.sort(files);
			} catch(NullPointerException e) {
				return this.processLIST(cmd);
			}
			ps.println(LIST_TAG);
			s.getInputStream().read();
			for(int i = 0; i < 2; i++) {
				File[] content;
				if (i == 0)
					content = directories;
				else 
					content = files;
				for(int j = 0; content != null && j < content.length; j++) {
					StringBuffer str = new StringBuffer("");
					File f = content[j];
					Date d = new Date();
					d.setTime(f.lastModified());
					str.append(f.isDirectory() ? "d" : "-");
					str.append(f.canRead() ? "r" : "-");
					str.append(f.canWrite() ? "w" : "-");
					str.append(f.canExecute() ? "x" : "-");
					str.append("\t");
					str.append(f.length());
					str.append("\t");
					str.append(d);
					str.append("\t");
					str.append(f.getName() + "\t");
					ps.println(str.toString());
				}
			}
			s.getOutputStream().close();
			s.close();
			this.controlOut.println(this.ftpMessages.transferCompleted());

		} catch(IOException e) { 
			this.controlOut.println(this.ftpMessages.transferAborted());
			return false;
		}
		return true;
	}

	/**
	 * Process called when the client sent a RETR command
	 * @param cmd the command sent by the client
	 * @return true if the command is correct and the process ends correctly
	 */
	public boolean processRETR(String cmd) {
		if (!cmd.matches("^" + RETR + "[ ]+[A-Za-z0-9/./_///-]+$")) {
			System.out.println("==+> " + cmd);
			this.controlOut.println(this.ftpMessages.syntaxError(cmd));
			return false;
		}
		if (!this.userConnected) {
			this.controlOut.println(this.ftpMessages.notConnected());
			return false;
		}
		StringTokenizer st = new StringTokenizer(cmd, " ", false);
		st.nextToken();
		String filepath = st.nextToken();
		st = new StringTokenizer(filepath, File.separator, false);
		String filename = "";
		try {
			String tmpPath = this.currentDir.getCanonicalPath();
			while(st.hasMoreTokens()) { 
				filename = st.nextToken();
				if (tmpPath.equals(this.rootDir.getCanonicalPath()) && filename.equals("..")) {
					this.controlOut.println(this.ftpMessages.noSuchFile(filepath));
					return false;
				}
				else {
					File f = new File(tmpPath += File.separator + filename);
					tmpPath = f.getCanonicalPath();
				}
			}
		}
		catch(IOException e) { 
			st = new StringTokenizer(filepath, File.separator, false);
			while(st.hasMoreTokens()) 
				filename = st.nextToken();
		}
		File f;
		f = new File(this.currentDir + File.separator + filepath);
		if (dataServerSocket != null && dataServerSocket.isClosed()) {
			this.controlOut.println(this.ftpMessages.cannotOpenDataConnection());
			return false;
		}
		if (!this.currentDir.exists() || !this.currentDir.isDirectory()) {
			this.controlOut.println(this.ftpMessages.fileUnavailable(this.getCurrentDirPath()));
			return false;
		}
		if (!f.exists() || f.isDirectory()) {
			this.controlOut.println(this.ftpMessages.noSuchFile(filepath));
			return false;
		}
		this.controlOut.println(this.ftpMessages.openDataConnection());
		try {
			Socket s = this.dataServerSocket.accept();
			PrintStream ps = new PrintStream(s.getOutputStream());
			ps.println(RETR_TAG);
			ps.println(filename);
			FileInputStream fin = new FileInputStream(f);
			byte[] buffer = new byte[4096];
			int r = 0;
			s.getInputStream().read();
			while(r != -1 && !s.isClosed()) {
				r = fin.read(buffer, 0, buffer.length);
				if (r != -1) {
					ps.write(buffer, 0, r);
					ps.flush();
				}
			}
			this.controlOut.println(this.ftpMessages.transferCompleted());
			fin.close();
			ps.close();
			s.close();
		} catch(IOException e) {
			this.controlOut.println(this.ftpMessages.transferAborted());
			return false;
		}
		return true;
	}

	/**
	 * Process called when the client sent a STOR command
	 * @param cmd the command sent by the client
	 * @return true if the command is correct and the process ends correctly
	 */
	public boolean processSTOR(String cmd) {
		if (!cmd.matches("^" + STOR + "[ ]+[A-Za-z0-9/./_/-//]+$")) {
			this.controlOut.println(this.ftpMessages.syntaxError(cmd));
			return false;
		}
		if (!this.userConnected) {
			this.controlOut.println(this.ftpMessages.notConnected());
			return false;
		}
		if (!this.currentDir.exists() || !this.currentDir.isDirectory()) {
			this.controlOut.println(this.ftpMessages.fileUnavailable(this.getCurrentDirPath()));
			return false;
		}
		StringTokenizer st = new StringTokenizer(cmd, " ", false);
		st.nextToken();
		String filepath = st.nextToken();
		st = new StringTokenizer(filepath, File.separator, false);
		String filename = "";
		while(st.hasMoreTokens())
			filename = st.nextToken();
		if (dataServerSocket != null && dataServerSocket.isClosed()) {
			this.controlOut.println(this.ftpMessages.cannotOpenDataConnection());
			return false;
		}
		this.controlOut.println(this.ftpMessages.openDataConnection());
		try {
			Socket s = this.dataServerSocket.accept();
			InputStream is = s.getInputStream();
			PrintStream ps = new PrintStream(s.getOutputStream());
			File f = new File(this.currentDir + File.separator + filename);
			FileOutputStream fout = new FileOutputStream(f);
			int nread = 0;
			byte[] buffer = new byte[4096];
			ps.println(STOR_TAG);
			ps.println(filepath);
			if (is.read() == 0) {
				f.delete();
				throw new IOException();
			}
			while(nread != -1 && !s.isClosed()) {
				nread = is.read(buffer);
				if (nread != -1) {
					fout.write(buffer, 0, nread);
					fout.flush();
				}
			}
			fout.close();
			this.controlOut.println(this.ftpMessages.transferCompleted());
			s.close();
		}
		catch(IOException e) { 
			this.controlOut.println(this.ftpMessages.transferAborted());
			return false;
		}
		return true;
	}



	/**
	 * Process called when the client sent a QUIT command
	 * @param cmd the command sent by the client
	 * @return true if the command is correct and the process ends correctly
	 */
	public boolean processQUIT(String cmd) {
		if (!this.isValidSyntax(cmd, "^" + QUIT + "$")) {
			this.controlOut.println(this.ftpMessages.syntaxError(cmd));
			return false;
		}
		try {
			System.out.print("> Closing socket[" + this.controlSocket.getInetAddress()  + "]... ");
			this.controlOut.println(this.ftpMessages.goodbye());
			this.controlSocket.close();
			System.out.println("ok");
			return true;
		}
		catch(IOException e) {
			System.err.println("> Error : " + e.getMessage());
			return false;
		}
	}

	/**
	 * Process called when the client sent a HELP command
	 * @param cmd the command sent by the client
	 * @return true if the command is correct and the process ends correctly
	 */
	public boolean processHELP(String cmd) {
		if (this.isValidSyntax(cmd, "^" + HELP + "$")) {
			String msg = "214 " + USER + " | " + PASS + " | " + PWD + " | " + CWD + " | " + CDUP + " | ";
			msg += LIST + " | " + RETR + " | " + STOR + " | " + QUIT;
			this.controlOut.println(msg);
			return true;
		}
		else if (this.isValidSyntax(cmd, "^" + HELP + "[ ]+" + USER + "$")) {
			this.controlOut.println("214 " + USER + " <user_name> : send new user information");
			return true;
		}
		else if (this.isValidSyntax(cmd, "^" + HELP + "[ ]+" + PASS + "$")) {
			this.controlOut.println("214 " + PASS + " <user_password>");
			return true;
		}
		else if (this.isValidSyntax(cmd, "^" + HELP + "[ ]+" + PWD + "$")) {
			this.controlOut.println("214 " + PWD + " : print working directory on remote machine");
			return true;
		}
		else if (this.isValidSyntax(cmd, "^" + HELP + "[ ]+" + CWD + "$")) {
			this.controlOut.println("214 " + CWD + " <directory> : change working directory on remote machine");
			return true;
		}
		else if (this.isValidSyntax(cmd, "^" + HELP + "[ ]+" + CDUP + "$")) {
			this.controlOut.println("214 " + CDUP + " : change remote working directory to parent directory");
			return true;
		}
		else if (this.isValidSyntax(cmd, "^" + HELP + "[ ]+" + LIST + "$")) {
			this.controlOut.println("214 " + LIST + " : list contents of remote directory");
			return true;
		}
		else if (this.isValidSyntax(cmd, "^" + HELP + "[ ]+" + RETR + "$")) {
			this.controlOut.println("214 " + RETR + " <filename> : receive file");
			return true;
		}
		else if (this.isValidSyntax(cmd, "^" + HELP + "[ ]+" + STOR + "$")) {
			this.controlOut.println("214 " + STOR + " <filename> : send one file");
			return true;
		}
		else if (this.isValidSyntax(cmd, "^" + HELP + "[ ]+" + QUIT + "$")) {
			this.controlOut.println("214 " + QUIT + " : nate ftp session and exit");
			return true;
		}
		else {
			this.controlOut.println(this.ftpMessages.syntaxError(cmd));
			return false;
		}
	}

	/**
	 * Process called when the client sent a PWD command
	 * @param cmd the command sent by the client
	 * @return true if the command is correct and the process ends correctly
	 */
	public boolean processPWD(String cmd) {
		if (!this.isValidSyntax(cmd, "^" + PWD + "$")) {
			this.controlOut.println(this.ftpMessages.syntaxError(cmd));
			return false;
		}
		if (!this.userConnected) {
			this.controlOut.println(this.ftpMessages.notConnected());
			return false;
		}
		this.controlOut.println(this.ftpMessages.currentDirectoryInfo(this.getCurrentDirPath()));
		return true;
	}

	/**
	 * Process called when the client sent a CWD command
	 * @param cmd the command sent by the client
	 * @return true if the command is correct and the process ends correctly
	 */
	public boolean processCWD(String cmd) {
		if (!this.isValidSyntax(cmd, "^" + CWD + "[ ]+[/./-/_//A-Za-z0-9]+$")) {
			this.controlOut.println(this.ftpMessages.syntaxError(cmd));
			return false;
		}
		if (!this.userConnected) {
			this.controlOut.println(this.ftpMessages.notConnected());
			return false;
		}
		try {
			File f;
			StringTokenizer st = new StringTokenizer(cmd, " ", false);
			st.nextToken();
			String param = st.nextToken();
			if (param.charAt(0) != '/') {
				st = new StringTokenizer(param, File.separator, false);
				String tmpPath = this.currentDir.getCanonicalPath();
				while(st.hasMoreTokens()) {
					String tmp = st.nextToken();
					if (tmpPath.equals(this.rootDir.getCanonicalPath()) && tmp.equals("..")) {
						break;
					}
					else {
						f = new File(tmpPath += File.separator + tmp);
						tmpPath = f.getCanonicalPath();
					}
				}
				f = new File(tmpPath);
			}
			else {
				f = new File(this.rootDir.getCanonicalPath() + param);
			}
			if (!f.exists() || !f.isDirectory()) {
				this.controlOut.println(this.ftpMessages.noSuchFile(param));
				return false;
			}
			this.currentDir = new File(f.getCanonicalPath());
			this.controlOut.println(this.ftpMessages.changeCurrentDirectory(this.getCurrentDirPath()));
		} catch(IOException e) { 
			this.controlOut.println(this.ftpMessages.changeCurrentDirectory(this.getCurrentDirPath()));
		}
		return true;
	}

	/**
	 * Process called when the client sent a CDUP command
	 * @param cmd the command sent by the client
	 * @return true if the command is correct and the process ends correctly
	 */
	public boolean processCDUP(String cmd) {
		if (!this.isValidSyntax(cmd, "^" + CDUP + "$")) {
			this.controlOut.println(this.ftpMessages.syntaxError(cmd));
			return false;
		}
		if (!this.userConnected) {
			this.controlOut.println(this.ftpMessages.notConnected());
			return false;
		}
		try {
			String path = this.currentDir.getCanonicalPath();
			if (!path.equals(this.rootDir.getCanonicalPath()))
				this.currentDir = new File(path + File.separator + "..");
		} catch(IOException e) { }
		this.controlOut.println(this.ftpMessages.commandSuccessful(CDUP));
		return true;
	}

}
