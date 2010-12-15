package com.atos.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import com.siga.general.SIGAException;

public class LogFileWriter
{
	// Static attributes
	private static String EXTENSION = ".log.xls";
	private static ArrayList<LogFileWriter> logFileWriterList = null;
	
	// Attributes
	private String path;
	private String fileName;
	private BufferedWriter bWriter;
	private ArrayList<ArrayList<String>> buffer;
	
	
	/**
	 * Constructor
	 */
	private LogFileWriter(String path, String fileName) throws SIGAException
	{
		this.path = path;
		this.fileName = fileName;
		this.buffer = new ArrayList<ArrayList<String>>();

		try {
			// creating directory tree
			new File(path).mkdirs();

			// creating file
			File file = new File(path + File.separator + fileName + EXTENSION);
			if (file.exists())
				file.delete();
		} catch (Exception e) {
			throw new SIGAException("messages.general.error", e);
		}
	}
	
	/**
	 * This method creates or returns an instance of LogFileWriter
	 * Two calls with same parameters return the same object
	 */
	public static LogFileWriter getLogFileWriter(String path, String fileName) throws SIGAException
	{
		LogFileWriter newLogFileWriter = null;

		// searching for an existing LogFileWriter
		if (logFileWriterList == null) {
			logFileWriterList = new ArrayList<LogFileWriter>();
		}
		else {
			for (LogFileWriter lfw : logFileWriterList) {
				if (new String(lfw.getPath() + File.separator + lfw.getFileName()).equals(path + File.separator + fileName))
					newLogFileWriter = lfw;
			}
		}

		// creating LogFileWriter whether it doesnt exists
		if (newLogFileWriter == null)
			newLogFileWriter = new LogFileWriter(path, fileName);

		// returning the LogFileWriter
		logFileWriterList.add(newLogFileWriter);
		return (newLogFileWriter);
	}
	
	
	// Setters
	public void setPath(String value) {this.path = value;}
	public void setFileName(String value) {this.fileName = value;}
	
	
	// Getters
	public String getPath() {return this.path;}
	public String getFileName() {return this.fileName;}
	
	
	// Other Methods
	/**
	 * This method adds a line to a buffer.
	 * Note: it doesn't write to file: to do this, use flush()
	 */
	public void addLog(ArrayList<String> line) throws SIGAException
	{
		if (line != null && !line.isEmpty())
			this.buffer.add(line);
	}
	/**
	 * This method adds a line to a buffer.
	 * Note: it doesn't write to file: to do this, use flush()
	 */
	public void addLog(String[] cols) throws SIGAException
	{
		ArrayList<String> line = new ArrayList<String>();
		for (String col : cols) {
			if (col != null)
				line.add(col);
		}
		this.addLog(line);
	}
	
	/**
	 * This method write buffered lines to the file
	 */
	public void flush() throws SIGAException
	{
		try {
			// opening file
			ClsLogging.writeFileLog("Writing LOG: " + path + File.separator + fileName + EXTENSION, 7);
			this.bWriter = new BufferedWriter(new FileWriter(path + File.separator + fileName + EXTENSION));

			// writing lines
			for (ArrayList<String> line : this.buffer) {
				for (String field : line) {
					this.bWriter.write(field);
					this.bWriter.write(ClsConstants.SEPARADOR);
				}
				this.bWriter.newLine();// + "\r\n");
			}

			// closing file
			this.bWriter.close();
		} catch (Exception e) {
			throw new SIGAException("messages.general.error", e);
		}
	}
	
	/**
	 * Clears the contents of the file
	 * @throws SIGAException 
	 */
	public void clear() throws SIGAException {
		try {
			this.buffer.clear();
			
			// creating directory tree
			new File(path).mkdirs();

			// creating file
			File file = new File(path + File.separator + fileName + EXTENSION);
			if (file.exists())
				file.delete();
		} catch (Exception e) {
			throw new SIGAException("messages.general.error", e);
		}
	}

}
