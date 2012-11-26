package com.siga.pki;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;



public class SIGAPkiStore
{
  public String dirBase=null;
  public SIGAPkiStore()  {
	if (dirBase==null) {
	    ReadProperties parameters= new ReadProperties(SIGAReferences.RESOURCE_FILES.PKI);
//	  ReadProperties parameters=new ReadProperties("pki.properties");
	  dirBase=parameters.returnProperty("PKI.SINGSTORE");
	  if (dirBase!=null) {
		dirBase=dirBase.trim();
		// check dirBase
		if (!dirBase.endsWith(File.separator))
		  dirBase+=File.separator;
	  }
	}
  }

  public String getPath() {
	return dirBase;
  }

  public void storeSignedFile(String filename,byte[] rawdata)
	  throws Exception {
	if (dirBase==null)
	  throw new Exception("PKI sign : No directory base");

// Si el fichero existe... va a desaparecer. Esto hay que hablarlo.
	  File outputFile = new File(filename);
	  FileOutputStream out = new FileOutputStream(dirBase+filename);
	  out.write(rawdata);
	  out.close();
  }

  public FileInputStream retrieveSignedFile(String filename)
	  throws Exception {
	if (dirBase==null)
	  throw new Exception("PKI sign : No directory base");

	  File inputFile = new File(dirBase+filename);
	  if (!inputFile.exists())
		throw new Exception("PKI sign : file " + filename + " does not exist");

	  FileInputStream in = new FileInputStream(inputFile);
	  return in;
  }

  public byte[] retrieveSignedFileBytes(String filename)
	 throws Exception {
	 FileInputStream in = retrieveSignedFile(filename);
	 int available = in.available();
	 byte[] rawdata = new byte[available];
	 in.read(rawdata);
	 in.close();
	 return rawdata;
  }
}
