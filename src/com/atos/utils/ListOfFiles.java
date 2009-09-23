/**
 * <p>Title: ListOfFiles </p>
 * <p>Description: class that handles a zip file </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: SchLumbergerSema </p>
 * @author 
 * @version 1.0
 */

package com.atos.utils;

import java.util.Enumeration;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.io.FileNotFoundException;
import java.io.FileInputStream;

public class ListOfFiles implements Enumeration {

	private String[] listOfFiles;
	private int current = 0;

	public ListOfFiles	(String[] listOfFiles) {
		this.listOfFiles = listOfFiles;
	}

	public boolean hasMoreElements() {
		if (current < listOfFiles.length)
			return true;
		else
			return false;
	}

	public Object nextElement() {
		InputStream in = null;

		if (!hasMoreElements())
			throw new NoSuchElementException("No more files.");
		else {
			String nextElement = listOfFiles[current];
			current++;
			try {
				in = new FileInputStream(nextElement);
			} catch (FileNotFoundException e) {
				System.err.println("ListOfFiles: Can't open " + nextElement);
			}
		}
		return in;
	}
}