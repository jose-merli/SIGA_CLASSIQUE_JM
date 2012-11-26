/**
 * <p>Title: Zipper </p>
 * <p>Description: class that handles a zip file </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: SchLumbergerSema </p>
 * @author 
 * @version 1.0
 */

package com.atos.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletRequest;

import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.siga.Utilidades.UtilidadesString;
//import com.siga.pki.PkiStore;


public class Zipper {

    public Zipper() {
    }


  /**
   * This This method decompresses a file zip in specified path
   *
   * @return   boolean The result of unzip file
   * @param fullname name of zip archive to unzip
   * @param fullpath name of path where the new archive to be created
   * @param req httpServletRequest
   * @throws ClsExceptions  type Exception
   */
  public static boolean unzipFile(String fullname, String fullpath, HttpServletRequest req)throws ClsExceptions {
    boolean bresult=false;
    ZipFile zipFile = null;
    Enumeration entries;

    try{
      zipFile = new ZipFile(fullname);

      entries = zipFile.entries();

      while(entries.hasMoreElements()) {
        ZipEntry entry = (ZipEntry)entries.nextElement();
        if(entry.isDirectory()) {
          // Assume directories are stored parents first then children.
//          ClsLogging.writeFileLog("Extracting directory: " + entry.getName() ,req, 2);
          // This is not robust, just for demonstration purposes.
          (new File(fullpath + entry.getName())).mkdir();
          continue;
        }
//        ClsLogging.writeFileLog("Extracting file: " + entry.getName() ,req, 2);
        copyInputStream(zipFile.getInputStream(entry),
                        new BufferedOutputStream(new FileOutputStream(fullpath + entry.getName())));
      }
      zipFile.close();
      bresult = true;
    } catch (IOException ioe) {
      ClsLogging.writeFileLog("Unhandled ioException:" ,req, 2);
      ioe.printStackTrace();
      throw new ClsExceptions(ioe.toString(),fullname,"00","GEN00","18");
    }catch (Exception e) {
      ClsLogging.writeFileLog("Unhandled generic exception:" ,req, 2);
      e.printStackTrace();
      throw new ClsExceptions(e.toString(),fullname,"00","GEN00","18");
    } finally {
        try {
            zipFile.close();
        } catch (Exception eee) {}
    }
    return bresult;
  }


  /**
   *
   * @param fullname name of zip archive to unzip
   * @param fullpath name of path where the new archive to be created
   * @param req httpServletRequest
   * @param ext extension
   * @return   boolean The result of unzip file
   * @throws ClsExceptions  type Exception
   */
  public static boolean unzipFile(String fullname, String fullpath, HttpServletRequest req, String ext)throws ClsExceptions {
    boolean bresult=false;
    ZipFile zipFile;
    Enumeration entries;
    int len;
    try{
      zipFile = new ZipFile(fullname);

      entries = zipFile.entries();

      while(entries.hasMoreElements()) {
        ZipEntry entry = (ZipEntry)entries.nextElement();

        if(entry.isDirectory()) {
          // Assume directories are stored parents first then children.
//          ClsLogging.writeFileLog("Extracting directory: " + entry.getName() ,req, 2);
          // This is not robust, just for demonstration purposes.
          (new File(fullpath + entry.getName())).mkdir();
          continue;
        }
//        ClsLogging.writeFileLog("Extracting file: " + entry.getName() ,req, 2);
        len=entry.getName().length();
        String sExt=entry.getName().substring(len-3,len);
        if (sExt.equalsIgnoreCase(ext)){
          copyInputStream(zipFile.getInputStream(entry),
                          new BufferedOutputStream(new FileOutputStream(fullpath + entry.getName())));
        }
      }
      zipFile.close();
      bresult = true;
    } catch (IOException ioe) {
      ClsLogging.writeFileLog("Unhandled ioException:" ,req, 2);
      ioe.printStackTrace();
      throw new ClsExceptions(ioe.toString(),fullname,"00","GEN00","18");
    }catch (Exception e) {
      ClsLogging.writeFileLog("Unhandled generic exception:" ,req, 2);
      e.printStackTrace();
      throw new ClsExceptions(e.toString(),fullname,"00","GEN00","18");
    }
    return bresult;
  }


  private static final void copyInputStream(InputStream in, OutputStream out)
          throws IOException
  {
      byte[] buffer = new byte[1024];
      int len;

      while((len = in.read(buffer)) >= 0)
          out.write(buffer, 0, len);

      in.close();
      out.close();
  }

  // Zips archives
  /**
   * <p>This method compresses  files to file zip</p>
   * <p>The <code>pathToGenerate</code> param is an inner folder to be created inside
   * the Zip File, where the files are to be inserted. When no inner path is required,
   * this param can be value <code>null</code>.</p>
   *
   * @return   boolean The result of unzip file
   * @param filenames These are the files to include in the ZIP file
   * @param fullname name of new archive to be created
   * @param pathToGenerate Path where to put the files inside the Zip file.
   * @param req httpServletRequest
   * @throws ClsExceptions  type Exception
   */
  public static boolean zipFiles(String[] filenames, String fullname, String pathToGenerate, HttpServletRequest req)throws ClsExceptions {
      boolean bresult=false;
      if (pathToGenerate == null) pathToGenerate = "";

      // Create a buffer for reading the files
      byte[] buf = new byte[1024];

	  String sFileName="";
      try {
          // Create the ZIP file
          ZipOutputStream out = new ZipOutputStream(new FileOutputStream(fullname));

          // Compress the files
          for (int i=0; i<filenames.length; i++) {
			if (filenames[i]!=null){
				sFileName=filenames[i].toString();
/*
				int len=sFileName.length();
			sFileName=sFileName.indexOf() .substring(  (,len);
*/
              FileInputStream in = new FileInputStream(filenames[i]);

              	String fileName = filenames[i];
					//setting path for concessions and findings..
			  	String path=fileName.substring(0,fileName.lastIndexOf(ClsConstants.FILE_SEP));
				pathToGenerate=getPath(path);

              // Add ZIP entry to output stream.

              if (fileName.indexOf(ClsConstants.FILE_SEP) != -1) {
                  fileName = fileName.substring(
                          fileName.lastIndexOf(ClsConstants.FILE_SEP) + 1);
              }



              if (!pathToGenerate.equals("")) {
                  fileName = pathToGenerate + ClsConstants.FILE_SEP + fileName;
              }

              out.putNextEntry(new ZipEntry(fileName));

              // Transfer bytes from the file to the ZIP file
              int len;
              while ((len = in.read(buf)) > 0) {
                  out.write(buf, 0, len);
              }

              // Complete the entry
              out.closeEntry();
              in.close();
			}
          }
          // Complete the ZIP file
          out.close();
          bresult = true;
	  } catch (java.io.FileNotFoundException fnfe) {
		  ClsLogging.writeFileLog("Unhandled ioException:" ,req, 2);
		  fnfe.printStackTrace();
		  UsrBean us = (UsrBean) req.getAttribute(ClsConstants.USERBEAN);
          throw new ClsExceptions((String)UtilidadesString.getMensajeIdioma(us,"general.filenotfound")+"  "+sFileName,"","00","GEN00","19");

      } catch (IOException ioe) {
          ClsLogging.writeFileLog("Unhandled ioException:" ,req, 2);
          ioe.printStackTrace();
          throw new ClsExceptions(ioe.toString(),"","00","GEN00","19");
      }catch (Exception e) {
          ClsLogging.writeFileLog("Unhandled generic exception:" ,req, 2);
          e.printStackTrace();
          throw new ClsExceptions(e.toString(),"","00","GEN00","19");
      }
      return bresult;
  }



  /**
   * This This method decompresses a file logbook zip in specified path
   * the concesions and txt or xml will be located in their folder (Concesions and import)
   *
   * @return   boolean The result of unzip file
   * @param filename name of zip archive to unzip
   * @param req httpServletRequest
   * @throws ClsExceptions  type Exception
   */
  public static boolean unzipFileLogbook(String filename,  HttpServletRequest req)throws ClsExceptions {


	boolean bresult=false;
	ZipFile zipFile;
	Enumeration entries;


//	 ReadProperties prop= new ReadProperties("Upload.properties" , req);
    ReadProperties prop= new ReadProperties(SIGAReferences.RESOURCE_FILES.UPLOAD);

	String sDrive=prop.returnProperty("UPLOAD.drive");

	String pathConcessions=  sDrive +ClsConstants.FILE_SEP +prop.returnProperty("http.path")
								+ClsConstants.FILE_SEP +prop.returnProperty("FilesUploaded.path")
								+ClsConstants.FILE_SEP +prop.returnProperty("concessions.path");

	String pathFindings=  sDrive 	+ClsConstants.FILE_SEP +prop.returnProperty("http.path")
								+ClsConstants.FILE_SEP +prop.returnProperty("ImagesVideoUpload.path")
								+ClsConstants.FILE_SEP +prop.returnProperty("Findings.path");


	String pathMaintAction=  sDrive +ClsConstants.FILE_SEP +prop.returnProperty("http.path")
									+ClsConstants.FILE_SEP +prop.returnProperty("ImagesVideoUpload.path")
									+ClsConstants.FILE_SEP +prop.returnProperty("MaintActions.path");


	String pathImport=  sDrive 		+ClsConstants.FILE_SEP +prop.returnProperty("path.path")
								 +ClsConstants.FILE_SEP +prop.returnProperty("FilesUploaded.path")
								+ClsConstants.FILE_SEP +prop.returnProperty("Logbook.path")
								+ClsConstants.FILE_SEP +prop.returnProperty("Import.path");

	 String pathFilename =pathImport+ ClsConstants.FILE_SEP + filename;



	try{
	  zipFile = new ZipFile(pathImport+ClsConstants.FILE_SEP+filename);

	  entries = zipFile.entries();

	  while(entries.hasMoreElements()) {
		ZipEntry entry = (ZipEntry)entries.nextElement();

		if(entry.isDirectory()) {
		  // Assume directories are stored parents first then children.
		  ClsLogging.writeFileLog("Extracting directory: " + entry.getName() ,req, 2);
		  // This is not robust, just for demonstration purposes.
		  (new File(pathImport + entry.getName())).mkdir();
		  continue;
		}
		String sName=entry.getName();
		int len=sName.length();
		ClsLogging.writeFileLog("Extracting file: " + entry.getName() ,req, 2);
		String sSufijo=sName.substring(len-3,len);
		String sFileName="";
		if (sSufijo.equals("txt")||sSufijo.equals("xml")||sSufijo.equals("xsd")){
			copyInputStream(zipFile.getInputStream(entry),
						new BufferedOutputStream(new FileOutputStream(pathImport + ClsConstants.FILE_SEP + entry.getName())));
		}else{
			String pathToSave=pathConcessions;
			sFileName=sName;
			if(sName.indexOf(prop.returnProperty("Findings.path"))==0){
				sFileName=sName.substring(prop.returnProperty("Findings.path").length()+1,sName.length());
				pathToSave=pathFindings;
			}
			
			/*
			PkiStore pk= new PkiStore();
			if(sName.indexOf(prop.returnProperty("signstore.path"))==0){
				sFileName=sName.substring(prop.returnProperty("signstore.path").length()+1,sName.length());
				pathToSave=pk.getPath();
			}
			*/
			if(sName.indexOf(prop.returnProperty("MaintActions.path"))==0){
				sFileName=sName.substring(prop.returnProperty("MaintActions.path").length()+1,sName.length());
				pathToSave=pathMaintAction;
			}


			copyInputStream(zipFile.getInputStream(entry),
						new BufferedOutputStream(new FileOutputStream(pathToSave + ClsConstants.FILE_SEP + sFileName)));
		}

	  }
	  zipFile.close();
	  bresult = true;
	} catch (IOException ioe) {
	  ClsLogging.writeFileLog("Unhandled ioException:" ,req, 2);
	  ioe.printStackTrace();
	  throw new ClsExceptions(ioe.toString(),pathFilename,"00","GEN00","18");
	}catch (Exception e) {
	  ClsLogging.writeFileLog("Unhandled generic exception:" ,req, 2);
	  e.printStackTrace();
	  throw new ClsExceptions(e.toString(),pathFilename,"00","GEN00","18");
	}
	return bresult;
  }

  //creating ptah to findings file, after the system can be distinguished between concessions

	private static String getPath(String sPath){
		String pathToGenerate="";

	    ReadProperties prop= new ReadProperties(SIGAReferences.RESOURCE_FILES.UPLOAD);
//		ReadProperties prop= new ReadProperties("Upload.properties");

		String sDrive=prop.returnProperty("UPLOAD.drive");
/*
		String pathConcessions=  sDrive +ClsConstants.FILE_SEP +prop.returnProperty("http.path")
										+ClsConstants.FILE_SEP +prop.returnProperty("FilesUploaded.path")
										+ClsConstants.FILE_SEP +prop.returnProperty("concessions.path");
*/
		String pathFindings=  sDrive 	+ClsConstants.FILE_SEP +prop.returnProperty("http.path")
										+ClsConstants.FILE_SEP +prop.returnProperty("ImagesVideoUpload.path")
										+ClsConstants.FILE_SEP +prop.returnProperty("Findings.path");


		String pathMaintAction=  sDrive +ClsConstants.FILE_SEP +prop.returnProperty("http.path")
										+ClsConstants.FILE_SEP +prop.returnProperty("ImagesVideoUpload.path")
										+ClsConstants.FILE_SEP +prop.returnProperty("MaintActions.path");


		if (sPath.equals(pathFindings))pathToGenerate=prop.returnProperty("Findings.path");

		if (sPath.equals(pathMaintAction))pathToGenerate=prop.returnProperty("MaintActions.path");
		/*
		PkiStore pk= new PkiStore();
		*/
		if (!sPath.endsWith(File.separator)){
			sPath=sPath+File.separator;
		}
		/*
		if(sPath.equals((String)pk.getPath())) pathToGenerate=prop.returnProperty("signstore.path");
		*/

		return pathToGenerate;





	}


}


