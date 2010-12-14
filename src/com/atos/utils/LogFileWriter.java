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
	
	
	/**
	 * Constructor
	 */
	private LogFileWriter(String path, String fileName) throws SIGAException
	{
		this.path = path;
		this.fileName = fileName;

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
	 * This method adds several lines to the file	 * 
	 */
	public void addLog(ArrayList<ArrayList<String>> lines) throws SIGAException
	{
		try {
			// opening file
			ClsLogging.writeFileLog("Writing LOG: " + path + File.separator + fileName + EXTENSION, 7);
			this.bWriter = new BufferedWriter(new FileWriter(path + File.separator + fileName + EXTENSION));

			// writing lines
			for (ArrayList<String> line : lines) {
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

}
