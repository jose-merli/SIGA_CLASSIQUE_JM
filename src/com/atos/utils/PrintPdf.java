package com.atos.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.util.Date;
import java.util.StringTokenizer;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.Attribute;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;

/**
* <p>Title: PrintPdf.java</p>
* <p>Description: This class converts a PDF format document into PostScript (ps) and prints the same.</p>
* @version 1.0
*/

public class PrintPdf
{
/* Input PDF file name */
private String inputFileName;

/* Autogenerated output PS file name */
private String outputFileName;

/* Path from where the pdftops tool can be accessed */
//private static final String PDF_TO_PS_TOOL_PATH = "\"C:\\Printing in Java\"\\cmd /c pdftops";
private static final String PDF_TO_PS_TOOL_PATH = "cmd /c pdftops";

/**
* Default Constructor.
*/
public PrintPdf()
{
/*****************************/
/* De momento no lo activamos
/*****************************/	
// cleanUpThread = new CleanUpThread();

}

/**
* Constructor, the PDF file to be printed should be passed as parameter to this.
*/
public PrintPdf(String inputFileName)
{
this.inputFileName = inputFileName;

/*****************************/
/* De momento no lo activamos
/*****************************/
//cleanUpThread = new CleanUpThread();

}

/**
* Sets the inputFileName property value
* @param inputFileName
*/
public void setInputFileName(String inputFileName)
{
this.inputFileName = inputFileName;
}

/**
* Accessor method to obtain the value of inputFileName property value.
* @return java.lang.String (inputFileName).
*/
public String getInputFileName()
{
return this.inputFileName;
}

/**
* Sets the outputFileName property value.
* This method sets the generated PostScript file name that is generated for the input PDF file name.
* @param outputFileName
*/
private void setOuputFileName(String outputFileName)
{
this.outputFileName = outputFileName;
}


/**
* Accessor method to obtain the outputFileName name.
* @return outputFileName
*/
public String getOutputFileName()
{
return this.outputFileName;
}

/**
* Core logic to print the input document.
*/
public void printDocument()
{
try
{
generateOuputFileName();
convertPdfToPostScript();
ClsLogging.writeFileLog("CONVERSION PDF TERMINADA!",8);

PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();

DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;

//PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);

PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();

ClsLogging.writeFileLog("Impresora: "+defaultService.getName(),7);

PrintServiceAttributeSet pr = defaultService.getAttributes();

Attribute attr[]=pr.toArray();

ClsLogging.writeFileLog("ATRIBUTOS DE LA IMPRESORA:",7);
ClsLogging.writeFileLog("-> "+attr[0].getName(),7);
ClsLogging.writeFileLog("-> "+attr[1].getName(),7);
ClsLogging.writeFileLog("-> "+attr[2].getName(),7);
ClsLogging.writeFileLog("-> "+attr[3].getName(),7);


//PrintService printPrintService = null;



/**********************************************/
/* Si se quiere sacar el dialogo de impresoras
/**********************************************/
/*PrintService service = ServiceUI.printDialog(null, 200, 200,printService, defaultService, flavor, pras);

ClsLogging.writeFileLog("service: "+service,7);

if (service != null)
{
	*/
//DocPrintJob job = service.createPrintJob();


/**********************************************/



/**************************************************/
/* Imprime directamente en la impresora por defecto 
/**************************************************/

DocPrintJob job = defaultService.createPrintJob();


FileInputStream fis = new FileInputStream(getOutputFileName());
ClsLogging.writeFileLog("IMPORTANDO DATOS ...",7);
DocAttributeSet das = new HashDocAttributeSet();

Doc doc = new SimpleDoc(fis, flavor, das);
ClsLogging.writeFileLog("LANZANDO IMPRESION ...",7);
//job.print(doc, pras);

/**************************************************/

//}
}
catch (Exception e)
{
e.printStackTrace();
}
}

/**
* This method generates the post script output file name. The method obtains the path and PDF fileName and generates
* the post script file name with the same name as PDF file under the same path.
*/
protected void generateOuputFileName()
{
try
{
File file = new File(getInputFileName()); //Create a File Object from the give PDF file.
String filename = file.getName();

String parent = file.getParent();
if (parent==null)
	parent="";
StringTokenizer fileNameTokenizer = new StringTokenizer(filename, ".");
filename = fileNameTokenizer.nextToken(); //Extrace the file name except the extension. ex: if file name is 'xxx.pdf' this code will return 'xxx'

// this will result in a value as, "C:\" + "\" + "xxx" + "1028393030" + ".ps"; The file name generated is concatinated with current date
// time to make it unique, may be there will be situation where two clients may be generating the print output to the same PDF file.

filename = parent + filename + ((new Date()).getTime()) + ".ps";
	
setOuputFileName(filename);
}
catch (Exception e)
{
e.printStackTrace();
}
}

/**
* This method converts the given PDF file to PS file
* http://www.foolabs.com/xpdf/download.html
* This method uses the pdftops.exe tool to pass the input pdf file name and output ps file name to
* generate the Postscript version of the input PDF file.
*/
protected void convertPdfToPostScript()
{
try
{
Runtime runTime = Runtime.getRuntime();
Process process = null;
String command = PDF_TO_PS_TOOL_PATH + " " + getInputFileName() + " " + getOutputFileName();
ClsLogging.writeFileLog("Command: "+command,7);
ClsLogging.writeFileLog("Sistema Operativo: "+System.getProperty("os.name"),7);
process = runTime.exec(command);
process.waitFor();
			 //Chequea el valor de salida
			 int error = process.exitValue();
			 switch (error) {
				 default: ClsLogging.writeFileLog("Error: "+error,1);
			 }

}
catch (Exception e)
{
	ClsLogging.writeFileLog("Excepcion: "+e.toString(),1);	
e.printStackTrace();
}
}

/**
* This method cleans up the generated post script document so that all the generated PS documents
* clog the hard drive space.
*/
protected synchronized void cleanUpPSDocument()
{
try
{
	ClsLogging.writeFileLog("Fichero: "+getOutputFileName(),7);
File file = new File(getOutputFileName());
if (file==null||getOutputFileName()==null)
{
	ClsLogging.writeFileLog("NO EXISTE!!",7);
}
else
{
	ClsLogging.writeFileLog("file: "+file.toString(),7);	
	ClsLogging.writeFileLog("fileList: "+file.listFiles(),7);
	ClsLogging.writeFileLog("file parent: "+file.getParent(),7);

if (file.getParent()==null)
{
	ClsLogging.writeFileLog("SIN DIRECTORIO PADRE!!",7);
}
else 
{

file = new File(file.getParent());

ClsLogging.writeFileLog("fileList: "+file.listFiles(),7);
if (file.listFiles()==null)
{
	ClsLogging.writeFileLog("Sin ficheros!",7);
}
else
{
File[] fileList = file.listFiles(
new FilenameFilter ()
{
public boolean accept(File dir, String name)
{
if (name.endsWith(".ps"))
return true;
else
return false;
}
}
);
if (fileList != null)
{
	ClsLogging.writeFileLog("fileList: "+fileList.toString(),7);
for (int i = 0; i < fileList.length; i++)
{
	ClsLogging.writeFileLog("Dentro del for "+i,7);
long lastModified = fileList[i].lastModified();
if ((lastModified / (60 * 60 * 1000)) > (60 * 60 * 1000)) // If the file is more than ONE hour old then delete it.
fileList[i].delete();
}
}
}
}
}
}
catch (Exception e)
{
e.printStackTrace();
}
}

/**
* Test
*/
public static void main(String args[]) //throws Exception
{
//PrintPdf printPdf = new PrintPdf("\"C:\\Printing in Java\"\\"+args[0]);
PrintPdf printPdf = new PrintPdf(args[0]);
printPdf.printDocument();
ClsLogging.writeFileLog("******** FIN DE LA IMPRESION ********",7);
}
}