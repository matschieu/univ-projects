package com.github.matschieu.maths;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
*@author Matschieu
*/
public class IOFile {

	private File inputFile;
	private File outputFile;
	private FileReader input;
	private FileWriter output;

	public IOFile() {
		inputFile = null;
		outputFile = null;
		this.input = null;
		this.output = null;
	}

	private String pathConverter(String path) {
		StringBuffer pathBuffer = new StringBuffer("");
		for(int i = 0; i < path.length(); i++)
			if (path.charAt(i) == '/') pathBuffer.append(File.separator);
			else pathBuffer.append(path.charAt(i));
		return pathBuffer.toString();
	}

	public void open(String filePath) throws FileNotFoundException {
		this.inputFile = this.outputFile = new File(pathConverter(filePath));
		if(!inputFile.exists()) throw new FileNotFoundException(filePath);
	}

	public String read() throws ReadFileException {
		StringBuffer strbuf = new StringBuffer("");
		try {
			this.input = new FileReader(inputFile);
			BufferedReader in = new BufferedReader(input);
			boolean eof = false;
			while (!eof) {
				String str = in.readLine();
				if (str != null) strbuf.append(str + "\n");
				else eof = true;
			}
			in.close();
			input = null;
		}
		catch(Exception e) {
			if (inputFile != null && inputFile != null)
				throw new ReadFileException(inputFile.toString());
			else throw new ReadFileException();
		}
		return strbuf.toString();
	}

	public void write(String str) throws WriteFileException {
		try {
			this.output = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(output);
			out.write(str, 0, str.length());
			out.close();
			output = null;
		}
		catch(Exception e) {
			if (outputFile != null && outputFile != null)
				throw new WriteFileException(outputFile.toString());
			else throw new WriteFileException();
		}
	}

	public void close() { inputFile = outputFile = null; }

	public String getInputFilePath() { return inputFile.toString(); }
	public String getOutputFilePath() { return outputFile.toString(); }

	public boolean isOpen() { return inputFile != null && outputFile != null; }

	public void setInputFilePath(String filePath) { inputFile = new File(filePath); }
	public void setOutputFilePath(String filePath) { outputFile = new File(filePath); }

	public boolean exists(String filePath) {
		try { return (new File(filePath)).exists(); }
		catch(Exception e) { }
		return false;
	}
	public boolean create(String filePath) {
		try { return (new File(filePath)).createNewFile(); }
		catch(Exception e) { }
		return false;
	}

	@Override
	public String toString() {
		StringBuffer strbuf = new StringBuffer("");
		strbuf.append(inputFile.toString());
		return strbuf.toString();
	}

}
