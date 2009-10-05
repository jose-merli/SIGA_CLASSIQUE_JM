package com.siga.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsLogging;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.ReadProperties;
import com.siga.Utilidades.SIGAReferences;


public class SIGASvlChkFolders extends SIGAServletAdapter {
	private static final long serialVersionUID = -7382682789248251947L;

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setHeader("Cache-Control","no-store"); //HTTP 1.1
		res.setHeader("Pragma","no-cache"); //HTTP 1.0
		res.setDateHeader ("Expires", -1); //prevents caching at the proxy server
	}

	public void doGet (HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setHeader("Cache-Control","no-store"); //HTTP 1.1
		res.setHeader("Pragma","no-cache"); //HTTP 1.0
		res.setDateHeader ("Expires", -1); //prevents caching at the proxy server

		doPost(req, res);
	}

	public void init(ServletConfig cfg) throws javax.servlet.ServletException {
		super.init(cfg);

		/**********************************/
		/*   CHECKING DB PSSC PARAMETERS  */
		/**********************************/

		String queryParam1="select value as dboption from pssc_parameters where parameter='DBOPTION'";
		String queryParam2="select value as otime from pssc_parameters where parameter='SIGA_TIMEZONE'";
		String otime="";
		String dboption="";

		Connection con= null;
		Statement stmtParam1=null;
		Statement stmtParam2=null;
		ResultSet rsParam1=null;
		ResultSet rsParam2=null;
		try{
			con= ClsMngBBDD.getConnection();
			stmtParam1=con.createStatement();
			rsParam1=stmtParam1.executeQuery(queryParam1);

			while(rsParam1.next()){
				dboption=rsParam1.getString("dboption");        			
			}

			stmtParam2=con.createStatement();
			rsParam2=stmtParam2.executeQuery(queryParam2);

			while(rsParam2.next()){
				otime=rsParam2.getString("otime");       			
			}

			rsParam1.close();
			rsParam1=null;
			rsParam2.close();
			rsParam2=null;
		} catch (Exception e){      		      
			System.out.println("ERROR CHECKING DB PSSC PARAMETERS: "+e.toString());
		} finally {
			try {
				if (stmtParam1!=null)
					stmtParam1.close();
				if (stmtParam2!=null)
					stmtParam2.close();
				if (rsParam1!=null)
					rsParam1.close();
				if (rsParam2!=null)
					rsParam2.close();
				ClsMngBBDD.closeConnection(con);
			} catch (Exception ex) {      				
				System.out.println("ERROR closing connection: "+ex.toString());
			}
		}

		boolean outexit=false;
		boolean dbexit=false;

		System.out.println("");
		System.out.println("*********************************************");
		System.out.println("******** CHECKING DB PSSC PARAMETERS ********");
		System.out.println("*********************************************");
		System.out.println("");

		if (otime==null){
			outexit=true;
			//ClsLogging.writeFileLogWithoutSession("SIGA_TIMEZONE, DB Parameter NOT EXIST! ",1);    			
			System.out.println("SIGA_TIMEZONE, DB Parameter NOT EXIST!");
			System.out.println("");
		} else {
			if (!otime.equals(""))
				System.out.println("SIGA_TIMEZONE: "+otime);
		}
		if (dboption==null){
			dbexit=true;
			//ClsLogging.writeFileLogWithoutSession("DBOPTION, DB Parameter NOT EXIST! ",1);
			System.out.println("DBOPTION, DB Parameter NOT EXIST!");    			
			System.out.println("");
		} else {
			if (!dboption.equals(""))
				System.out.println("DBOPTION: "+dboption);
		}    
		if ((outexit)||(dbexit)){
			//ClsLogging.writeFileLogWithoutSession("Exiting ...",1);
			System.out.println("Exiting ...");    			
			System.out.println("");
		} else {
			if ((otime!=null)&&(otime.equals(""))){
				outexit=true;
				//ClsLogging.writeFileLogWithoutSession("SIGA_TIMEZONE, DB Parameter IS BLANK! ",1);    			
				System.out.println("SIGA_TIMEZONE, DB Parameter IS BLANK! ");    			
				System.out.println("");
			}
			if ((dboption!=null)&&(dboption.equals(""))){
				dbexit=true;
				//ClsLogging.writeFileLogWithoutSession("DBOPTION, DB Parameter IS BLANK! ",1);    			
				System.out.println("DBOPTION, DB Parameter IS BLANK! ");    			
				System.out.println("");
			}
		}

		if ((outexit)||(dbexit)) {
			//ClsLogging.writeFileLogWithoutSession("BYE, BYE!",1);
			System.out.println("**********************************************");
			System.out.println("** YOU HAVE NOT CONFIGURED CORRECTLY PSSC !!**");
			System.out.println("** Please, refer to the Installation notes ***");
			System.out.println("**********************************************");
			System.out.println("Exiting...");

			for (int i=0;i<90000000;i++)
				System.out.print("");

			System.exit(-1);
		} else {
			System.out.println("");
			System.out.println("** Done **");
		}

		/**********************************/
		//ClsLogging.writeFileLogWithoutSession("");
		//ClsLogging.writeFileLogWithoutSession("Checking PSSC Folders ...",3);
		//ClsLogging.writeFileLogWithoutSession("Checking PSSC necessary folders ...",1);
		/*******************************************/
		/*   BEGIN PREV. GENERATING OF PROPERTIES  */
		/*******************************************/

		//String pathLogs = ClsConstants.RES_DIR +ClsConstants.RES_PROP_DOMAIN + ClsConstants.FILE_SEP + "Log";
		//File fileResourcesDir = new File(pathProperties);
		//File fileResourcesDirCDS = new File(RESOURCES_DIR + ClsConstants.FILE_SEP + "cds");
		try {
			//ClsLogging.writeFileLogWithoutSession("Exists PSSC necessary folder? : "+pathLogs+"---> "+directoryLogExists);
			File fileLogs = SIGAReferences.getFileReference(SIGAReferences.RESOURCE_FILES.LOG_DIR);
			String pathLogs = fileLogs.getAbsolutePath(); 
			if(!fileLogs.exists()) {
				if (fileLogs.mkdirs()){
					//ClsLogging.writeFileLogWithoutSession("<***> Creating PSSC necessary folder: "+pathLogs,3);
				} else
					ClsLogging.writeFileLogWithoutSession("<***> Error Creating PSSC necessary folder: "+pathLogs);
			}

			//ClsLogging.writeFileLogWithoutSession("Exists PSSC necessary folder? : "+pathProperties+"---> "+directoryResourcesDirExists);
/* Comentado por que no tiene sentido comprobar si existe el directorio properties o si su contenido en valido.
  			File fileResourcesDir = SIGAReferences.getFileReference(SIGAReferences.RESOURCE_FILES.PROPERTIES_DIR);
			String pathProperties=fileResourcesDir.getAbsolutePath();
			if(!fileResourcesDir.exists()) {
				if (fileResourcesDir.mkdirs()){
					//ClsLogging.writeFileLogWithoutSession("<***> Creating PSSC necessary folder: "+pathProperties,3);
				} else
					ClsLogging.writeFileLogWithoutSession("<***> Error Creating PSSC necessary folder: "+pathProperties,1);
			}

			this.copyFileWithinJar("SIGA.properties",pathProperties+ ClsConstants.FILE_SEP +"Log.properties");
			this.copyFileWithinJar("ldap.properties",pathProperties+ ClsConstants.FILE_SEP +"ldap.properties");
			this.copyFileWithinJar("Icons.properties",pathProperties+ ClsConstants.FILE_SEP +"Icons.properties");
			this.copyFileWithinJar("pki.properties",pathProperties+ ClsConstants.FILE_SEP +"pki.properties");
			this.copyFileWithinJar("Upload.properties",pathProperties+ ClsConstants.FILE_SEP +"Upload.properties");
			this.copyFileWithinJar("crystal.properties",pathProperties+ ClsConstants.FILE_SEP +"crystal.properties");
			this.copyFileWithinJar("jndi.properties",pathProperties+ ClsConstants.FILE_SEP +"jndi.properties");
			this.copyFileWithinJar("URLConnections.properties",pathProperties+ ClsConstants.FILE_SEP +"URLConnections.properties");
			this.copyFileWithinJar("logbook.properties",pathProperties+ ClsConstants.FILE_SEP +"logbook.properties");
			this.copyFileWithinJar("AdvancedSearch.properties",pathProperties+ ClsConstants.FILE_SEP +"AdvancedSearch.properties");
			this.copyFileWithinJar("AdvancedSearch.xsd",pathProperties+ ClsConstants.FILE_SEP +"AdvancedSearch.xsd");
			this.copyFileWithinJar("AdvancedSearch.xsl",pathProperties+ ClsConstants.FILE_SEP +"AdvancedSearch.xsl");
			this.copyFileWithinJar("AdvancedSearchJRules.css",pathProperties+ ClsConstants.FILE_SEP +"AdvancedSearchJRules.css");

		    ReadProperties rpr= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties rpr=new ReadProperties("Log.properties");
			int loglevel = Integer.parseInt(rpr.returnProperty("LOG.level"));
			System.out.println(" ");
			System.out.println("************************************************");
			System.out.println("****************** P S S C *********************");
			if (rpr.returnProperty("LOG.run").equals("1")){
				System.out.println("**** Trace ON forwarding to File, Level: "+loglevel+" *****");
				System.out.println("************************************************");
			} else{
				System.out.println("*** Trace ON forwarding to Console, Level: "+loglevel+" ***");
				System.out.println("************************************************");
			}
			System.out.println(" ");

			ClsLogging.writeFileLogWithoutSession("Checking PSSC necessary folders ...",1);
			ClsLogging.writeFileLogWithoutSession("",1);

			//ClsLogging.writeFileLogWithoutSession("Exists PSSC necessary folder? : "+pathProperties+ ClsConstants.FILE_SEP + "cds, "+directoryResourcesDirCDSExists);
			File fileResourcesDirCDS = SIGAReferences.getFileReference(SIGAReferences.RESOURCE_FILES.CDS_DIR);
			if(!fileResourcesDirCDS.exists()) {
				if (fileResourcesDirCDS.mkdirs())
					ClsLogging.writeFileLogWithoutSession("<***> Creating PSSC necessary folder: "+pathProperties+ ClsConstants.FILE_SEP + "cds",3);
				else
					ClsLogging.writeFileLogWithoutSession("<***> Error Creating PSSC necessary folder: "+pathProperties+ ClsConstants.FILE_SEP + "cds",1);
			}

			// CDS
			this.copyFileWithinJar("/cds/AETS_Failure_Event_Codes.properties",pathProperties+ ClsConstants.FILE_SEP + "cds" + ClsConstants.FILE_SEP + "AETS_Failure_Event_Codes.properties");
			this.copyFileWithinJar("/cds/Maintenance_LRI_Codes.properties",pathProperties+ ClsConstants.FILE_SEP + "cds" + ClsConstants.FILE_SEP + "Maintenance_LRI_Codes.properties");
			this.copyFileWithinJar("/cds/SIGA_CDS_TABLES.xml",pathProperties+ ClsConstants.FILE_SEP + "cds" + ClsConstants.FILE_SEP + "SIGA_CDS_TABLES.xml");
			this.copyFileWithinJar("/cds/SIGA_SPECIFIC_CDS_TABLES.xml",pathProperties+ ClsConstants.FILE_SEP + "cds" + ClsConstants.FILE_SEP + "SIGA_SPECIFIC_CDS_TABLES.xml");
*/
		} catch (IOException ex) {
			ClsLogging.writeFileLogWithoutSession("Error: "+ex.toString());
			ex.printStackTrace();
		} catch (URISyntaxException ex) {
			ClsLogging.writeFileLogWithoutSession("Error: "+ex.toString());
			ex.printStackTrace();
		}

		/******************************************/
		/*   END PREV. GENERATING OF PROPERTIES   */
		/******************************************/

	    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.UPLOAD);
//		ReadProperties rp=new ReadProperties("Upload.properties");
		String drive=(String)rp.returnProperty("UPLOAD.drive");

		System.out.println("REMOTE PSSC RESOURCES DRIVE: "+drive);
		System.out.println("");

		String pathP = drive+ClsConstants.FILE_SEP+ "path" + ClsConstants.FILE_SEP;
		String pathH = drive+ClsConstants.FILE_SEP+ "http" + ClsConstants.FILE_SEP;
		String pathImages = pathH + ClsConstants.IMAGESVIDEOUPLOAD;
		String pathFilesUploaded = pathP + "FilesUploaded";
		String pathFilesUploadedhttp = pathH + "FilesUploaded";
		String pathFilesUploadedDDS = pathFilesUploaded +  ClsConstants.FILE_SEP + "DDS" + ClsConstants.FILE_SEP + "temp";
		String pathFilesUploadedLogbookExp = pathFilesUploaded +  ClsConstants.FILE_SEP + "Logbook" + ClsConstants.FILE_SEP + "Export";
		String pathFilesUploadedLogbookImp = pathFilesUploaded +  ClsConstants.FILE_SEP + "Logbook" + ClsConstants.FILE_SEP + "Import";
		String pathFilesUploadedConcess = pathFilesUploadedhttp + ClsConstants.FILE_SEP + "concessions";
		String pathFilesUploadedXML = pathFilesUploaded + ClsConstants.FILE_SEP + "XML";
		String pathFilesUploadedSign = pathFilesUploaded + ClsConstants.FILE_SEP + "signstore";
		String pathImagesAficons = pathH + ClsConstants.IMAGESVIDEOUPLOAD+ClsConstants.FILE_SEP +"icons"+ClsConstants.FILE_SEP +"afIcons";
		String pathImagesAticons = pathH + ClsConstants.IMAGESVIDEOUPLOAD+ClsConstants.FILE_SEP +"icons"+ClsConstants.FILE_SEP +"atIcons";
		String pathImagesIoicons = pathH + ClsConstants.IMAGESVIDEOUPLOAD+ClsConstants.FILE_SEP +"icons"+ClsConstants.FILE_SEP +"loIcons";
		String pathImagesSticons = pathH + ClsConstants.IMAGESVIDEOUPLOAD+ClsConstants.FILE_SEP +"icons"+ClsConstants.FILE_SEP +"stIcons";
		String pathImagesFindings = pathH + ClsConstants.IMAGESVIDEOUPLOAD+ClsConstants.FILE_SEP +"Findings";
		String pathImagesManActs = pathH + ClsConstants.IMAGESVIDEOUPLOAD+ClsConstants.FILE_SEP +"MaintActions";
		String pathAdvSearch = pathP + "AdvancedSearch";
		String pathAdvSearchJrules = pathAdvSearch + ClsConstants.FILE_SEP + "jRules";
		String pathAdvSearchSql = pathAdvSearch + ClsConstants.FILE_SEP + "sql";

		//File fileResDir = new File(ClsConstants.RES_DIR);
		//File fileResDirStruts = new File(ClsConstants.RESOURCES_DIR_STRUTS);
		//File fileResDirImages = new File(pathImages);
		File fileResDirImagesIconsAf = new File(pathImagesAficons);
		File fileResDirImagesIconsAt = new File(pathImagesAticons);
		File fileResDirImagesIconsIo = new File(pathImagesIoicons);
		File fileResDirImagesIconsSt = new File(pathImagesSticons);
		File fileResDirImagesFindings = new File(pathImagesFindings);
		File fileResDirImagesManActs = new File(pathImagesManActs);
		File fileFilesUploadedConcess = new File(pathFilesUploadedConcess);
		File fileFilesUploadedXML = new File(pathFilesUploadedXML);
		File fileAdvSearchJrules = new File(pathAdvSearchJrules);
		File fileAdvSearchSql = new File(pathAdvSearchSql);
		File fileFilesUploadedDDS = new File(pathFilesUploadedDDS);
		File fileFilesUploadedLogBI = new File(pathFilesUploadedLogbookImp);
		File fileFilesUploadedLogBE = new File(pathFilesUploadedLogbookExp);
		File fileFilesUploadedSign = new File(pathFilesUploadedSign);

		//ClsLogging.writeFileLogWithoutSession("Exists PSSC necessary folder? : "+pathImagesFindings+"---> "+directoryFileFindExists,3);
		if(!fileResDirImagesFindings.exists()) {
			if(fileResDirImagesFindings.mkdirs())
				ClsLogging.writeFileLogWithoutSession("<***> Creating PSSC necessary folder: "+pathImagesFindings,3);
			else
				ClsLogging.writeFileLogWithoutSession("<***> Error Creating PSSC necessary folder: "+pathImagesFindings);
		}

		//ClsLogging.writeFileLogWithoutSession("Exists PSSC necessary folder? : "+pathImagesManActs+"---> "+directoryFileManExists);
		if(!fileResDirImagesManActs.exists()) {
			if(fileResDirImagesManActs.mkdirs())
				ClsLogging.writeFileLogWithoutSession("<***> Creating PSSC necessary folder: "+pathImagesManActs,3);
			else
				ClsLogging.writeFileLogWithoutSession("<***> Error Creating PSSC necessary folder: "+pathImagesManActs);
		}

		//ClsLogging.writeFileLogWithoutSession("Exists PSSC necessary folder? : "+pathFilesUploadedSign+"---> "+directoryFileSignExists);
		if(!fileFilesUploadedSign.exists()) {
			if(fileFilesUploadedSign.mkdirs())
				ClsLogging.writeFileLogWithoutSession("<***> Creating PSSC necessary folder: "+pathFilesUploadedSign,3);
			else
				ClsLogging.writeFileLogWithoutSession("<***> Error Creating PSSC necessary folder: "+pathFilesUploadedSign);
		}

		//ClsLogging.writeFileLogWithoutSession("Exists PSSC necessary folder? : "+pathFilesUploadedLogbookExp+"---> "+directoryFileLogbookExpExists);
		if(!fileFilesUploadedLogBE.exists()) {
			if(fileFilesUploadedLogBE.mkdirs())
				ClsLogging.writeFileLogWithoutSession("<***> Creating PSSC necessary folder: "+pathFilesUploadedLogbookExp,3);
			else
				ClsLogging.writeFileLogWithoutSession("<***> Error Creating PSSC necessary folder: "+pathFilesUploadedLogbookExp);
		}

		//ClsLogging.writeFileLogWithoutSession("Exists PSSC necessary folder? : "+pathFilesUploadedLogbookImp+"---> "+directoryFileLogbookImpExists);
		if(!fileFilesUploadedLogBI.exists()) {
			if (fileFilesUploadedLogBI.mkdirs())
				ClsLogging.writeFileLogWithoutSession("<***> Creating PSSC necessary folder: "+pathFilesUploadedLogbookImp,3);
			else
				ClsLogging.writeFileLogWithoutSession("<***> Error Creating PSSC necessary folder: "+pathFilesUploadedLogbookImp);
		}


		//ClsLogging.writeFileLogWithoutSession("Exists PSSC necessary folder? : "+pathFilesUploadedDDS+"---> "+directoryFileDDSExists);
		if(!fileFilesUploadedDDS.exists()) {
			if (fileFilesUploadedDDS.mkdirs())
				ClsLogging.writeFileLogWithoutSession("<***> Creating PSSC necessary folder: "+pathFilesUploadedDDS,3);
			else
				ClsLogging.writeFileLogWithoutSession("<***> Error Creating PSSC necessary folder: "+pathFilesUploadedDDS);
		}

		//ClsLogging.writeFileLogWithoutSession("Exists PSSC necessary folder? : "+pathAdvSearchJrules+"---> "+directoryAdvSearchExists);
		if(!fileAdvSearchJrules.exists()) {
			if (fileAdvSearchJrules.mkdirs())
				ClsLogging.writeFileLogWithoutSession("<***> Creating PSSC necessary folder: "+pathAdvSearchJrules,3);
			else
				ClsLogging.writeFileLogWithoutSession("<***> Error Creating PSSC necessary folder: "+pathAdvSearchJrules);
		}

		//ClsLogging.writeFileLogWithoutSession("Exists PSSC necessary folder? : "+pathAdvSearchSql+"---> "+directoryAdvSearchsqlExists);
		if(!fileAdvSearchSql.exists()) {
			if (fileAdvSearchSql.mkdirs())
				ClsLogging.writeFileLogWithoutSession("<***> Creating PSSC necessary folder: "+pathAdvSearchSql,3);
			else
				ClsLogging.writeFileLogWithoutSession("<***> Error Creating PSSC necessary folder: "+pathAdvSearchSql);
		}

		//ClsLogging.writeFileLogWithoutSession("Exists PSSC necessary folder? : "+pathFilesUploadedConcess+"---> "+directoryFilesConcessionsExists);
		if(!fileFilesUploadedConcess.exists()) {
			if (fileFilesUploadedConcess.mkdirs())
				ClsLogging.writeFileLogWithoutSession("<***> Creating PSSC necessary folder: "+pathFilesUploadedConcess,3);
			else
				ClsLogging.writeFileLogWithoutSession("<***> Error Creating PSSC necessary folder: "+pathFilesUploadedConcess);
		}

		//ClsLogging.writeFileLogWithoutSession("Exists PSSC necessary folder? : "+pathFilesUploadedXML+"---> "+directoryFilesXMLExists);
		if(!fileFilesUploadedXML.exists()) {
			if (fileFilesUploadedXML.mkdirs())
				ClsLogging.writeFileLogWithoutSession("<***> Creating PSSC necessary folder: "+pathFilesUploadedXML,3);
			else
				ClsLogging.writeFileLogWithoutSession("<***> Error Creating PSSC necessary folder: "+pathFilesUploadedXML);
		}

//		try {
//			File fileResDir = SIGAReferences.getFileReference(SIGAReferences.RESOURCE_FILES.WEB_INF);
			//ClsLogging.writeFileLogWithoutSession("Exists PSSC necessary folder? : "+ClsConstants.RES_DIR+"---> "+directoryResDirExists);
//			if(!fileResDir.exists()) {
//				if (fileResDir.mkdirs())
//					ClsLogging.writeFileLogWithoutSession("<***> Creating PSSC necessary folder: "+fileResDir.getAbsolutePath(),3);
					//ClsLogging.writeFileLogWithoutSession("<***> Creating PSSC necessary folder: "+ClsConstants.RES_DIR,3);
//				else
					//ClsLogging.writeFileLogWithoutSession("<***> Error Creating PSSC necessary folder: "+ClsConstants.RES_DIR);
//					ClsLogging.writeFileLogWithoutSession("<***> Error Creating PSSC necessary folder: "+fileResDir.getAbsolutePath());
//			}

/* Comentado por que no tiene sentido comprobar si existe el directorio properties ni su contenido
  			File fileResDirStruts = SIGAReferences.getFileReference(SIGAReferences.RESOURCE_FILES.PROPERTIES_DIR);
			//ClsLogging.writeFileLogWithoutSession("Exists PSSC necessary folder? : "+ClsConstants.RESOURCES_DIR_STRUTS+"---> "+directoryResDirStrutsExists);
			if(!fileResDirStruts.exists()) {
				if (fileResDirStruts.mkdirs())
					ClsLogging.writeFileLogWithoutSession("<***> Creating PSSC necessary folder: "+fileResDirStruts.getAbsolutePath(),3);
					//ClsLogging.writeFileLogWithoutSession("<***> Creating PSSC necessary folder: "+ClsConstants.RESOURCES_DIR_STRUTS,3);
				else
					//ClsLogging.writeFileLogWithoutSession("<***> Error Creating PSSC necessary folder: "+ClsConstants.RESOURCES_DIR_STRUTS);
					ClsLogging.writeFileLogWithoutSession("<***> Error Creating PSSC necessary folder: "+fileResDirStruts.getAbsolutePath());
			}
		} catch (IOException e){
			ClsLogging.writeFileLogWithoutSession("Error: "+e.toString());
			e.printStackTrace();
		} catch (URISyntaxException e){
			ClsLogging.writeFileLogWithoutSession("Error: "+e.toString());
			e.printStackTrace();
		}
*/

		//ClsLogging.writeFileLogWithoutSession("Exists PSSC necessary folder? : "+pathImagesAficons + "---> "+directoryResDirImagesIconsAfExists);
		if(!fileResDirImagesIconsAf.exists()) {
			if (fileResDirImagesIconsAf.mkdirs())
				ClsLogging.writeFileLogWithoutSession("<***> Creating PSSC necessary folder: "+pathImagesAficons,3);
			else
				ClsLogging.writeFileLogWithoutSession("<***> Error Creating PSSC necessary folder: "+pathImagesAficons);
		}

		//ClsLogging.writeFileLogWithoutSession("Exists PSSC necessary folder? : "+pathImagesAticons + "---> "+directoryResDirImagesIconsAtExists);
		if(!fileResDirImagesIconsAt.exists()) {
			if (fileResDirImagesIconsAt.mkdirs())
				ClsLogging.writeFileLogWithoutSession("<***> Creating PSSC necessary folder: "+pathImagesAticons,3);
			else
				ClsLogging.writeFileLogWithoutSession("<***> Error Creating PSSC necessary folder: "+pathImagesAticons);
		}

		//ClsLogging.writeFileLogWithoutSession("Exists PSSC necessary folder? : "+pathImagesIoicons + "---> "+directoryResDirImagesIconsIoExists);
		if(!fileResDirImagesIconsIo.exists()) {
			if (fileResDirImagesIconsIo.mkdirs())
				ClsLogging.writeFileLogWithoutSession("<***> Creating PSSC necessary folder: "+pathImagesIoicons,3);
			else
				ClsLogging.writeFileLogWithoutSession("<***> Error Creating PSSC necessary folder: "+pathImagesIoicons);
		}

		//ClsLogging.writeFileLogWithoutSession("Exists PSSC necessary folder? : "+pathImagesSticons + "---> "+directoryResDirImagesIconsStExists);
		if(!fileResDirImagesIconsSt.exists()) {
			if (fileResDirImagesIconsSt.mkdirs())
				ClsLogging.writeFileLogWithoutSession("<***> Creating PSSC necessary folder: "+pathImagesSticons,3);
			else
				ClsLogging.writeFileLogWithoutSession("<***> Error Creating PSSC necessary folder: "+pathImagesSticons);
		}

		try {
			// icons
			this.copyFileWithinJar("/"+ClsConstants.IMAGESVIDEOUPLOAD+"/icons/MinHot.gif",pathImages+ ClsConstants.FILE_SEP + "icons" + ClsConstants.FILE_SEP +"MinHot.gif");
			this.copyFileWithinJar("/"+ClsConstants.IMAGESVIDEOUPLOAD+"/icons/PlusHot.gif",pathImages+ ClsConstants.FILE_SEP + "icons" + ClsConstants.FILE_SEP +"PlusHot.gif");
			this.copyFileWithinJar("/"+ClsConstants.IMAGESVIDEOUPLOAD+"/icons/PSSC.gif",pathImages+ ClsConstants.FILE_SEP + "icons" + ClsConstants.FILE_SEP +"PSSC.gif");
			this.copyFileWithinJar("/"+ClsConstants.IMAGESVIDEOUPLOAD+"/icons/service.gif",pathImages+ ClsConstants.FILE_SEP + "icons" + ClsConstants.FILE_SEP +"service.gif");
			this.copyFileWithinJar("/"+ClsConstants.IMAGESVIDEOUPLOAD+"/icons/storage.gif",pathImages+ ClsConstants.FILE_SEP + "icons" + ClsConstants.FILE_SEP +"storage.gif");
			this.copyFileWithinJar("/"+ClsConstants.IMAGESVIDEOUPLOAD+"/icons/store.gif",pathImages+ ClsConstants.FILE_SEP + "icons" + ClsConstants.FILE_SEP +"store.gif");
			this.copyFileWithinJar("/"+ClsConstants.IMAGESVIDEOUPLOAD+"/icons/transit.gif",pathImages+ ClsConstants.FILE_SEP + "icons" + ClsConstants.FILE_SEP +"transit.gif");

			this.copyFileWithinJar("/"+ClsConstants.IMAGESVIDEOUPLOAD+"/icons/afIcons/ALEMANIA.gif",pathImages+ ClsConstants.FILE_SEP + "icons" + ClsConstants.FILE_SEP + "afIcons"+ ClsConstants.FILE_SEP +"ALEMANIA.gif");
			this.copyFileWithinJar("/"+ClsConstants.IMAGESVIDEOUPLOAD+"/icons/afIcons/Bola.gif",pathImages+ ClsConstants.FILE_SEP + "icons" + ClsConstants.FILE_SEP + "afIcons"+ ClsConstants.FILE_SEP +"Bola.gif");
			this.copyFileWithinJar("/"+ClsConstants.IMAGESVIDEOUPLOAD+"/icons/afIcons/ESPAnA.gif",pathImages+ ClsConstants.FILE_SEP + "icons" + ClsConstants.FILE_SEP + "afIcons"+ ClsConstants.FILE_SEP +"ESPAnA.gif");
			this.copyFileWithinJar("/"+ClsConstants.IMAGESVIDEOUPLOAD+"/icons/afIcons/INGLATERRA.gif",pathImages+ ClsConstants.FILE_SEP + "icons" + ClsConstants.FILE_SEP + "afIcons"+ ClsConstants.FILE_SEP +"INGLATERRA.gif");
			this.copyFileWithinJar("/"+ClsConstants.IMAGESVIDEOUPLOAD+"/icons/afIcons/ITALIA.gif",pathImages+ ClsConstants.FILE_SEP + "icons" + ClsConstants.FILE_SEP + "afIcons"+ ClsConstants.FILE_SEP +"ITALIA.gif");

			this.copyFileWithinJar("/"+ClsConstants.IMAGESVIDEOUPLOAD+"/icons/atIcons/Bola.gif",pathImages+ ClsConstants.FILE_SEP + "icons" + ClsConstants.FILE_SEP + "atIcons"+ ClsConstants.FILE_SEP +"Bola.gif");
			this.copyFileWithinJar("/"+ClsConstants.IMAGESVIDEOUPLOAD+"/icons/atIcons/aircraft.gif",pathImages+ ClsConstants.FILE_SEP + "icons" + ClsConstants.FILE_SEP + "atIcons"+ ClsConstants.FILE_SEP +"aircraft.gif");
			this.copyFileWithinJar("/"+ClsConstants.IMAGESVIDEOUPLOAD+"/icons/atIcons/ASSEMBLY.gif",pathImages+ ClsConstants.FILE_SEP + "icons" + ClsConstants.FILE_SEP + "atIcons"+ ClsConstants.FILE_SEP +"ASSEMBLY.gif");
			this.copyFileWithinJar("/"+ClsConstants.IMAGESVIDEOUPLOAD+"/icons/atIcons/EMU.gif",pathImages+ ClsConstants.FILE_SEP + "icons" + ClsConstants.FILE_SEP + "atIcons"+ ClsConstants.FILE_SEP +"EMU.gif");
			this.copyFileWithinJar("/"+ClsConstants.IMAGESVIDEOUPLOAD+"/icons/atIcons/ENGINE.gif",pathImages+ ClsConstants.FILE_SEP + "icons" + ClsConstants.FILE_SEP + "atIcons"+ ClsConstants.FILE_SEP +"ENGINE.gif");
			this.copyFileWithinJar("/"+ClsConstants.IMAGESVIDEOUPLOAD+"/icons/atIcons/EX.gif",pathImages+ ClsConstants.FILE_SEP + "icons" + ClsConstants.FILE_SEP + "atIcons"+ ClsConstants.FILE_SEP +"EX.gif");
			this.copyFileWithinJar("/"+ClsConstants.IMAGESVIDEOUPLOAD+"/icons/atIcons/service.gif",pathImages+ ClsConstants.FILE_SEP + "icons" + ClsConstants.FILE_SEP + "atIcons"+ ClsConstants.FILE_SEP +"service.gif");
			this.copyFileWithinJar("/"+ClsConstants.IMAGESVIDEOUPLOAD+"/icons/atIcons/storage.gif",pathImages+ ClsConstants.FILE_SEP + "icons" + ClsConstants.FILE_SEP + "atIcons"+ ClsConstants.FILE_SEP +"storage.gif");
			this.copyFileWithinJar("/"+ClsConstants.IMAGESVIDEOUPLOAD+"/icons/atIcons/store.gif",pathImages+ ClsConstants.FILE_SEP + "icons" + ClsConstants.FILE_SEP + "atIcons"+ ClsConstants.FILE_SEP +"store.gif");
			this.copyFileWithinJar("/"+ClsConstants.IMAGESVIDEOUPLOAD+"/icons/atIcons/transit.gif",pathImages+ ClsConstants.FILE_SEP + "icons" + ClsConstants.FILE_SEP + "atIcons"+ ClsConstants.FILE_SEP +"transit.gif");

			this.copyFileWithinJar("/"+ClsConstants.IMAGESVIDEOUPLOAD+"/icons/loIcons/Bola.gif",pathImages+ ClsConstants.FILE_SEP + "icons" + ClsConstants.FILE_SEP + "loIcons"+ ClsConstants.FILE_SEP +"Bola.gif");
			this.copyFileWithinJar("/"+ClsConstants.IMAGESVIDEOUPLOAD+"/icons/loIcons/ALEMANIA.gif",pathImages+ ClsConstants.FILE_SEP + "icons" + ClsConstants.FILE_SEP + "loIcons"+ ClsConstants.FILE_SEP +"ALEMANIA.gif");
			this.copyFileWithinJar("/"+ClsConstants.IMAGESVIDEOUPLOAD+"/icons/loIcons/ESPAnA.gif",pathImages+ ClsConstants.FILE_SEP + "icons" + ClsConstants.FILE_SEP + "loIcons"+ ClsConstants.FILE_SEP +"ESPAnA.gif");
			this.copyFileWithinJar("/"+ClsConstants.IMAGESVIDEOUPLOAD+"/icons/loIcons/INGLATERRA.gif",pathImages+ ClsConstants.FILE_SEP + "icons" + ClsConstants.FILE_SEP + "loIcons"+ ClsConstants.FILE_SEP +"INGLATERRA.gif");
			this.copyFileWithinJar("/"+ClsConstants.IMAGESVIDEOUPLOAD+"/icons/loIcons/ITALIA.gif",pathImages+ ClsConstants.FILE_SEP + "icons" + ClsConstants.FILE_SEP + "loIcons"+ ClsConstants.FILE_SEP +"ITALIA.gif");

			this.copyFileWithinJar("/"+ClsConstants.IMAGESVIDEOUPLOAD+"/icons/stIcons/Bola.gif",pathImages+ ClsConstants.FILE_SEP + "icons" + ClsConstants.FILE_SEP + "stIcons"+ ClsConstants.FILE_SEP +"Bola.gif");
			this.copyFileWithinJar("/"+ClsConstants.IMAGESVIDEOUPLOAD+"/icons/stIcons/accessory.gif",pathImages+ ClsConstants.FILE_SEP + "icons" + ClsConstants.FILE_SEP + "stIcons"+ ClsConstants.FILE_SEP +"accessory.gif");
			this.copyFileWithinJar("/"+ClsConstants.IMAGESVIDEOUPLOAD+"/icons/stIcons/assembly.gif",pathImages+ ClsConstants.FILE_SEP + "icons" + ClsConstants.FILE_SEP + "stIcons"+ ClsConstants.FILE_SEP +"assembly.gif");
			this.copyFileWithinJar("/"+ClsConstants.IMAGESVIDEOUPLOAD+"/icons/stIcons/emu.gif",pathImages+ ClsConstants.FILE_SEP + "icons" + ClsConstants.FILE_SEP + "stIcons"+ ClsConstants.FILE_SEP +"emu.gif");
			this.copyFileWithinJar("/"+ClsConstants.IMAGESVIDEOUPLOAD+"/icons/stIcons/engine.gif",pathImages+ ClsConstants.FILE_SEP + "icons" + ClsConstants.FILE_SEP + "stIcons"+ ClsConstants.FILE_SEP +"engine.gif");
			this.copyFileWithinJar("/"+ClsConstants.IMAGESVIDEOUPLOAD+"/icons/stIcons/ex.gif",pathImages+ ClsConstants.FILE_SEP + "icons" + ClsConstants.FILE_SEP + "stIcons"+ ClsConstants.FILE_SEP +"ex.gif");
			this.copyFileWithinJar("/"+ClsConstants.IMAGESVIDEOUPLOAD+"/icons/stIcons/service.gif",pathImages+ ClsConstants.FILE_SEP + "icons" + ClsConstants.FILE_SEP + "stIcons"+ ClsConstants.FILE_SEP +"service.gif");
			this.copyFileWithinJar("/"+ClsConstants.IMAGESVIDEOUPLOAD+"/icons/stIcons/storage.gif",pathImages+ ClsConstants.FILE_SEP + "icons" + ClsConstants.FILE_SEP + "stIcons"+ ClsConstants.FILE_SEP +"storage.gif");
			this.copyFileWithinJar("/"+ClsConstants.IMAGESVIDEOUPLOAD+"/icons/stIcons/store.gif",pathImages+ ClsConstants.FILE_SEP + "icons" + ClsConstants.FILE_SEP + "stIcons"+ ClsConstants.FILE_SEP +"store.gif");
			this.copyFileWithinJar("/"+ClsConstants.IMAGESVIDEOUPLOAD+"/icons/stIcons/transit.gif",pathImages+ ClsConstants.FILE_SEP + "icons" + ClsConstants.FILE_SEP + "stIcons"+ ClsConstants.FILE_SEP +"transit.gif");
			this.copyFileWithinJar("/"+ClsConstants.IMAGESVIDEOUPLOAD+"/icons/stIcons/part.gif",pathImages+ ClsConstants.FILE_SEP + "icons" + ClsConstants.FILE_SEP + "stIcons"+ ClsConstants.FILE_SEP +"part.gif");
			this.copyFileWithinJar("/"+ClsConstants.IMAGESVIDEOUPLOAD+"/icons/stIcons/module.gif",pathImages+ ClsConstants.FILE_SEP + "icons" + ClsConstants.FILE_SEP + "stIcons"+ ClsConstants.FILE_SEP +"module.gif");
			this.copyFileWithinJar("/"+ClsConstants.IMAGESVIDEOUPLOAD+"/icons/stIcons/se.gif",pathImages+ ClsConstants.FILE_SEP + "icons" + ClsConstants.FILE_SEP + "stIcons"+ ClsConstants.FILE_SEP +"se.gif");
		} catch (IOException ex) {
			ClsLogging.writeFileLogWithoutSession("Error: "+ex.toString(),1);
			ex.printStackTrace();
		}

		ClsLogging.writeFileLogWithoutSession("** Done **",1);
		ClsLogging.writeFileLogWithoutSession("",1);
	}


	private void copyDir(String srcDir, String dstDir) throws IOException {
		ClsLogging.writeFileLogWithoutSession("src Dir = " + srcDir,3);
		ClsLogging.writeFileLogWithoutSession("dst Dir = " + dstDir,3);

		File fdstDir = new File(dstDir);

		if (! fdstDir.exists()) {
			fdstDir.mkdirs();
			ClsLogging.writeFileLogWithoutSession("creating  " +  dstDir,3);
		}

		String[ ] fileList = new File(srcDir).list();

		boolean dir;
		String sep = File.separator;  // slash or backslash

		for (int i = 0; i < fileList.length ; i++) {
			dir = new File(srcDir+sep+fileList[ i ]).isDirectory();
			if (dir) {
				copyDir (srcDir+sep+fileList[ i ], dstDir+sep+fileList[ i ]);
			} else {
				copyFile(srcDir+sep+fileList[ i ], dstDir+sep+fileList[ i ]);
			}
		}

	}

	private void copyFile(String srcFile, String dstFile) throws IOException {
		FileInputStream inp = new FileInputStream(srcFile) ;
		FileOutputStream out = new FileOutputStream(dstFile) ;

		byte[] buff = new byte[8192];
		int count;
		// read up to buff.length bytes
		while ((count = inp.read(buff)) != -1) {
			out.write(buff, 0, count);
		}

		out.close();
		inp.close();

	}

	private void copyFileWithinJar(String srcFile, String dstFile) throws IOException {
		//ClsLogging.writeFileLogWithoutSession("Dentro de copyFileWithinJar srcFile:  " +  srcFile,3);

		ClassLoader classLoader = getClass().getClassLoader();
		InputStream inp = classLoader.getResourceAsStream(srcFile);

		File fileChk = new File(dstFile);

		boolean fileExists = fileChk.exists();
		//ClsLogging.writeFileLogWithoutSession("File: "+dstFile+", exists?: "+fileExists,3);
		if (!fileExists){
			if (inp != null) {
				FileOutputStream out = new FileOutputStream(dstFile);

				byte[] buff = new byte[8192];
				int count;
				// read up to buff.length bytes
				while ( (count = inp.read(buff)) != -1) {
					out.write(buff, 0, count);
				}

				out.close();
			} else {
				ClsLogging.writeFileLogWithoutSession("***> ERROR: " + srcFile + " not included in compiled PSSC classes",1);
			}
		}
		inp.close();
	}

	public void destroy() {
		ClsLogging.writeFileLogWithoutSession("Destroy servlet Authenticate",1);
	}
}
