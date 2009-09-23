package com.siga.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.ClsLogging;

public class SIGASvlChkFolders extends HttpServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

          res.setHeader("Cache-Control","no-store"); //HTTP 1.1
          res.setHeader("Pragma","no-cache"); //HTTP 1.0
          res.setDateHeader ("Expires", -1); //prevents caching at the proxy server

    }

    public void doGet (HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

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
      			
		    }catch (Exception e){      		      
      		      System.out.println("ERROR CHECKING DB PSSC PARAMETERS: "+e.toString());
		    }finally{

		     try {
       			if (stmtParam1!=null)     stmtParam1.close();
       			if (stmtParam2!=null)     stmtParam2.close();
       			if (rsParam1!=null)       rsParam1.close();
       			if (rsParam2!=null)       rsParam2.close();
       			ClsMngBBDD.closeConnection(con);
     			}
     			catch (Exception ex) {      				
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
			//com.atos.utils.ClsLogging.writeFileLogWithoutSession("SIGA_TIMEZONE, DB Parameter NOT EXIST! ",1);    			
			System.out.println("SIGA_TIMEZONE, DB Parameter NOT EXIST!");
			System.out.println("");
    		}
    		else
    		{
    			if (!otime.equals(""))
    				System.out.println("SIGA_TIMEZONE: "+otime);
    		}
		if (dboption==null){
    			dbexit=true;
    			//com.atos.utils.ClsLogging.writeFileLogWithoutSession("DBOPTION, DB Parameter NOT EXIST! ",1);
    			System.out.println("DBOPTION, DB Parameter NOT EXIST!");    			
    			System.out.println("");
    		}
    		else
    		{
    			if (!dboption.equals(""))
    				System.out.println("DBOPTION: "+dboption);
    		}    
    		if ((outexit)||(dbexit)){
    			//com.atos.utils.ClsLogging.writeFileLogWithoutSession("Exiting ...",1);
    			System.out.println("Exiting ...");    			
    			System.out.println("");
    		}
    		else
    		   {
    		   	if ((otime!=null)&&(otime.equals(""))){
    		   	   outexit=true;
    		   	   //com.atos.utils.ClsLogging.writeFileLogWithoutSession("SIGA_TIMEZONE, DB Parameter IS BLANK! ",1);    			
    		   	   System.out.println("SIGA_TIMEZONE, DB Parameter IS BLANK! ");    			
    		   	   System.out.println("");
    		   	}
    		   	if ((dboption!=null)&&(dboption.equals(""))){
    		   	   dbexit=true;
    		   	   //com.atos.utils.ClsLogging.writeFileLogWithoutSession("DBOPTION, DB Parameter IS BLANK! ",1);    			
    		   	   System.out.println("DBOPTION, DB Parameter IS BLANK! ");    			
    		   	   System.out.println("");
    		   	}
    		   }
               	
        	        
        	if ((outexit)||(dbexit))
        		{
        		//com.atos.utils.ClsLogging.writeFileLogWithoutSession("BYE, BYE!",1);
        		System.out.println("**********************************************");
          		System.out.println("** YOU HAVE NOT CONFIGURED CORRECTLY PSSC !!**");
          		System.out.println("** Please, refer to the Installation notes ***");
          		System.out.println("**********************************************");
          		
          		System.out.println("Exiting...");
          		
          		for (int i=0;i<90000000;i++)
          			System.out.print("");
          		
        		System.exit(-1);
        		}
        	else
        		{
        			System.out.println("");
        			System.out.println("** Done **");
          		}
        
          /**********************************/
          
        
          //ClsLogging.writeFileLogWithoutSession("");
          //ClsLogging.writeFileLogWithoutSession("Checking PSSC Folders ...",3);
          //com.atos.utils.ClsLogging.writeFileLogWithoutSession("Checking PSSC necessary folders ...",1);

          /*******************************************/
          /*   BEGIN PREV. GENERATING OF PROPERTIES  */
          /*******************************************/

          String pathLogs = com.atos.utils.ClsConstants.RES_DIR +
              com.atos.utils.ClsConstants.RES_PROP_DOMAIN + com.atos.utils.ClsConstants.FILE_SEP + "Log";

          File fileLogs = new File(pathLogs);
          File fileResourcesDir = new File(com.atos.utils.ClsConstants.RESOURCES_DIR);
          File fileResourcesDirCDS = new File(com.atos.utils.ClsConstants.RESOURCES_DIR + com.atos.utils.ClsConstants.FILE_SEP + "cds");

          boolean directoryLogExists = fileLogs.exists() && fileLogs.isDirectory();
          //com.atos.utils.ClsLogging.writeFileLogWithoutSession("Exists PSSC necessary folder? : "+pathLogs+"---> "+directoryLogExists);
          if(!fileLogs.exists())
          {
            if (fileLogs.mkdirs()){
                    String nothing="";
              //com.atos.utils.ClsLogging.writeFileLogWithoutSession("<***> Creating PSSC necessary folder: "+pathLogs,3);
                }
            else
              com.atos.utils.ClsLogging.writeFileLogWithoutSession("<***> Error Creating PSSC necessary folder: "+pathLogs);
          }

          boolean directoryResourcesDirExists = fileResourcesDir.exists() && fileResourcesDir.isDirectory();
          //com.atos.utils.ClsLogging.writeFileLogWithoutSession("Exists PSSC necessary folder? : "+com.atos.utils.ClsConstants.RESOURCES_DIR+"---> "+directoryResourcesDirExists);
          if(!fileResourcesDir.exists())
          {
            if (fileResourcesDir.mkdirs()){
              //com.atos.utils.ClsLogging.writeFileLogWithoutSession("<***> Creating PSSC necessary folder: "+com.atos.utils.ClsConstants.RESOURCES_DIR,3);
                      String nothing="";
                }
            else
              com.atos.utils.ClsLogging.writeFileLogWithoutSession("<***> Error Creating PSSC necessary folder: "+com.atos.utils.ClsConstants.RESOURCES_DIR,1);
          }

          try {

            this.copyFileWithinJar("SIGA.properties",com.atos.utils.ClsConstants.RESOURCES_DIR+ com.atos.utils.ClsConstants.FILE_SEP +"Log.properties");
            this.copyFileWithinJar("ldap.properties",com.atos.utils.ClsConstants.RESOURCES_DIR+ com.atos.utils.ClsConstants.FILE_SEP +"ldap.properties");
            this.copyFileWithinJar("Icons.properties",com.atos.utils.ClsConstants.RESOURCES_DIR+ com.atos.utils.ClsConstants.FILE_SEP +"Icons.properties");
            this.copyFileWithinJar("pki.properties",com.atos.utils.ClsConstants.RESOURCES_DIR+ com.atos.utils.ClsConstants.FILE_SEP +"pki.properties");
            this.copyFileWithinJar("Upload.properties",com.atos.utils.ClsConstants.RESOURCES_DIR+ com.atos.utils.ClsConstants.FILE_SEP +"Upload.properties");
            this.copyFileWithinJar("crystal.properties",com.atos.utils.ClsConstants.RESOURCES_DIR+ com.atos.utils.ClsConstants.FILE_SEP +"crystal.properties");
            this.copyFileWithinJar("jndi.properties",com.atos.utils.ClsConstants.RESOURCES_DIR+ com.atos.utils.ClsConstants.FILE_SEP +"jndi.properties");
            this.copyFileWithinJar("URLConnections.properties",com.atos.utils.ClsConstants.RESOURCES_DIR+ com.atos.utils.ClsConstants.FILE_SEP +"URLConnections.properties");
            this.copyFileWithinJar("logbook.properties",com.atos.utils.ClsConstants.RESOURCES_DIR+ com.atos.utils.ClsConstants.FILE_SEP +"logbook.properties");
            this.copyFileWithinJar("AdvancedSearch.properties",com.atos.utils.ClsConstants.RESOURCES_DIR+ com.atos.utils.ClsConstants.FILE_SEP +"AdvancedSearch.properties");
            this.copyFileWithinJar("AdvancedSearch.xsd",com.atos.utils.ClsConstants.RESOURCES_DIR+ com.atos.utils.ClsConstants.FILE_SEP +"AdvancedSearch.xsd");
            this.copyFileWithinJar("AdvancedSearch.xsl",com.atos.utils.ClsConstants.RESOURCES_DIR+ com.atos.utils.ClsConstants.FILE_SEP +"AdvancedSearch.xsl");
            this.copyFileWithinJar("AdvancedSearchJRules.css",com.atos.utils.ClsConstants.RESOURCES_DIR+ com.atos.utils.ClsConstants.FILE_SEP +"AdvancedSearchJRules.css");
              }
          catch (IOException ex) {
            com.atos.utils.ClsLogging.writeFileLogWithoutSession("Error: "+ex.toString());
            ex.printStackTrace();
          }
          com.atos.utils.ReadProperties rpr=new com.atos.utils.ReadProperties("Log.properties");
          int loglevel = Integer.parseInt(rpr.returnProperty("LOG.level"));
          System.out.println(" ");
          System.out.println("************************************************");
          System.out.println("****************** P S S C *********************");
                if (rpr.returnProperty("LOG.run").equals("1")){
                          System.out.println("**** Trace ON forwarding to File, Level: "+loglevel+" *****");
                          System.out.println("************************************************");
                      }
                else{
                            System.out.println("*** Trace ON forwarding to Console, Level: "+loglevel+" ***");
                            System.out.println("************************************************");
                      }
          System.out.println(" ");

          com.atos.utils.ClsLogging.writeFileLogWithoutSession("Checking PSSC necessary folders ...",1);
          com.atos.utils.ClsLogging.writeFileLogWithoutSession("",1);

          boolean directoryResourcesDirCDSExists = fileResourcesDirCDS.exists() && fileResourcesDirCDS.isDirectory();
          //com.atos.utils.ClsLogging.writeFileLogWithoutSession("Exists PSSC necessary folder? : "+com.atos.utils.ClsConstants.RESOURCES_DIR+ com.atos.utils.ClsConstants.FILE_SEP + "cds, "+directoryResourcesDirCDSExists);
          if(!fileResourcesDirCDS.exists())
          {
            if (fileResourcesDirCDS.mkdirs())
              com.atos.utils.ClsLogging.writeFileLogWithoutSession("<***> Creating PSSC necessary folder: "+com.atos.utils.ClsConstants.RESOURCES_DIR+ com.atos.utils.ClsConstants.FILE_SEP + "cds",3);
            else
              com.atos.utils.ClsLogging.writeFileLogWithoutSession("<***> Error Creating PSSC necessary folder: "+com.atos.utils.ClsConstants.RESOURCES_DIR+ com.atos.utils.ClsConstants.FILE_SEP + "cds",1);
          }

          try {

            // CDS
            this.copyFileWithinJar("/cds/AETS_Failure_Event_Codes.properties",com.atos.utils.ClsConstants.RESOURCES_DIR+ com.atos.utils.ClsConstants.FILE_SEP + "cds" + com.atos.utils.ClsConstants.FILE_SEP + "AETS_Failure_Event_Codes.properties");
            this.copyFileWithinJar("/cds/Maintenance_LRI_Codes.properties",com.atos.utils.ClsConstants.RESOURCES_DIR+ com.atos.utils.ClsConstants.FILE_SEP + "cds" + com.atos.utils.ClsConstants.FILE_SEP + "Maintenance_LRI_Codes.properties");
            this.copyFileWithinJar("/cds/SIGA_CDS_TABLES.xml",com.atos.utils.ClsConstants.RESOURCES_DIR+ com.atos.utils.ClsConstants.FILE_SEP + "cds" + com.atos.utils.ClsConstants.FILE_SEP + "SIGA_CDS_TABLES.xml");
            this.copyFileWithinJar("/cds/SIGA_SPECIFIC_CDS_TABLES.xml",com.atos.utils.ClsConstants.RESOURCES_DIR+ com.atos.utils.ClsConstants.FILE_SEP + "cds" + com.atos.utils.ClsConstants.FILE_SEP + "SIGA_SPECIFIC_CDS_TABLES.xml");
          }
          catch (IOException ex) {
            com.atos.utils.ClsLogging.writeFileLogWithoutSession("Error: "+ex.toString());
            ex.printStackTrace();
          }

          /******************************************/
          /*   END PREV. GENERATING OF PROPERTIES   */
          /******************************************/

          com.atos.utils.ReadProperties rp=new com.atos.utils.ReadProperties("Upload.properties");
          String drive=(String)rp.returnProperty("UPLOAD.drive");
                    
          System.out.println("REMOTE PSSC RESOURCES DRIVE: "+drive);
          System.out.println("");

          String pathP = drive+ClsConstants.FILE_SEP+ "path" + ClsConstants.FILE_SEP;
          String pathH = drive+ClsConstants.FILE_SEP+ "http" + ClsConstants.FILE_SEP;


          String pathImages = pathH +
              com.atos.utils.ClsConstants.IMAGESVIDEOUPLOAD;

          String pathFilesUploaded = pathP +
              "FilesUploaded";

          String pathFilesUploadedhttp = pathH +
              "FilesUploaded";

          String pathFilesUploadedDDS = pathFilesUploaded +  com.atos.utils.ClsConstants.FILE_SEP + "DDS" + com.atos.utils.ClsConstants.FILE_SEP + "temp";

          String pathFilesUploadedLogbookExp = pathFilesUploaded +  com.atos.utils.ClsConstants.FILE_SEP + "Logbook" + com.atos.utils.ClsConstants.FILE_SEP + "Export";

          String pathFilesUploadedLogbookImp = pathFilesUploaded +  com.atos.utils.ClsConstants.FILE_SEP + "Logbook" + com.atos.utils.ClsConstants.FILE_SEP + "Import";

          String pathFilesUploadedConcess = pathFilesUploadedhttp + com.atos.utils.ClsConstants.FILE_SEP + "concessions";
          String pathFilesUploadedXML = pathFilesUploaded + com.atos.utils.ClsConstants.FILE_SEP + "XML";

          String pathFilesUploadedSign = pathFilesUploaded + com.atos.utils.ClsConstants.FILE_SEP + "signstore";

          String pathImagesAficons = pathH +
              com.atos.utils.ClsConstants.IMAGESVIDEOUPLOAD+com.atos.utils.ClsConstants.FILE_SEP +"icons"+com.atos.utils.ClsConstants.FILE_SEP +"afIcons";

          String pathImagesAticons = pathH +
              com.atos.utils.ClsConstants.IMAGESVIDEOUPLOAD+com.atos.utils.ClsConstants.FILE_SEP +"icons"+com.atos.utils.ClsConstants.FILE_SEP +"atIcons";

          String pathImagesIoicons = pathH +
              com.atos.utils.ClsConstants.IMAGESVIDEOUPLOAD+com.atos.utils.ClsConstants.FILE_SEP +"icons"+com.atos.utils.ClsConstants.FILE_SEP +"loIcons";

          String pathImagesSticons = pathH +
              com.atos.utils.ClsConstants.IMAGESVIDEOUPLOAD+com.atos.utils.ClsConstants.FILE_SEP +"icons"+com.atos.utils.ClsConstants.FILE_SEP +"stIcons";

          String pathImagesFindings = pathH +
              com.atos.utils.ClsConstants.IMAGESVIDEOUPLOAD+com.atos.utils.ClsConstants.FILE_SEP +"Findings";

          String pathImagesManActs = pathH +
              com.atos.utils.ClsConstants.IMAGESVIDEOUPLOAD+com.atos.utils.ClsConstants.FILE_SEP +"MaintActions";

          String pathAdvSearch = pathP + "AdvancedSearch";

          String pathAdvSearchJrules = pathAdvSearch + com.atos.utils.ClsConstants.FILE_SEP + "jRules";

          String pathAdvSearchSql = pathAdvSearch + com.atos.utils.ClsConstants.FILE_SEP + "sql";


          File fileResDir = new File(com.atos.utils.ClsConstants.RES_DIR);
          File fileResDirStruts = new File(com.atos.utils.ClsConstants.RESOURCES_DIR_STRUTS);

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

          boolean directoryFileFindExists = fileResDirImagesFindings.exists() && fileResDirImagesFindings.isDirectory();
          //com.atos.utils.ClsLogging.writeFileLogWithoutSession("Exists PSSC necessary folder? : "+pathImagesFindings+"---> "+directoryFileFindExists,3);
          if(!fileResDirImagesFindings.exists())
          {
            if(fileResDirImagesFindings.mkdirs())
              com.atos.utils.ClsLogging.writeFileLogWithoutSession("<***> Creating PSSC necessary folder: "+pathImagesFindings,3);
            else
              com.atos.utils.ClsLogging.writeFileLogWithoutSession("<***> Error Creating PSSC necessary folder: "+pathImagesFindings);
          }

          boolean directoryFileManExists = fileResDirImagesManActs.exists() && fileResDirImagesManActs.isDirectory();
          //com.atos.utils.ClsLogging.writeFileLogWithoutSession("Exists PSSC necessary folder? : "+pathImagesManActs+"---> "+directoryFileManExists);
          if(!fileResDirImagesManActs.exists())
          {
            if(fileResDirImagesManActs.mkdirs())
              com.atos.utils.ClsLogging.writeFileLogWithoutSession("<***> Creating PSSC necessary folder: "+pathImagesManActs,3);
            else
              com.atos.utils.ClsLogging.writeFileLogWithoutSession("<***> Error Creating PSSC necessary folder: "+pathImagesManActs);
          }

          boolean directoryFileSignExists = fileFilesUploadedSign.exists() && fileFilesUploadedSign.isDirectory();
          //com.atos.utils.ClsLogging.writeFileLogWithoutSession("Exists PSSC necessary folder? : "+pathFilesUploadedSign+"---> "+directoryFileSignExists);
          if(!fileFilesUploadedSign.exists())
          {
            if(fileFilesUploadedSign.mkdirs())
              com.atos.utils.ClsLogging.writeFileLogWithoutSession("<***> Creating PSSC necessary folder: "+pathFilesUploadedSign,3);
            else
              com.atos.utils.ClsLogging.writeFileLogWithoutSession("<***> Error Creating PSSC necessary folder: "+pathFilesUploadedSign);
          }

          boolean directoryFileLogbookExpExists = fileFilesUploadedLogBE.exists() && fileFilesUploadedLogBE.isDirectory();
          //com.atos.utils.ClsLogging.writeFileLogWithoutSession("Exists PSSC necessary folder? : "+pathFilesUploadedLogbookExp+"---> "+directoryFileLogbookExpExists);
          if(!fileFilesUploadedLogBE.exists())
          {
            if(fileFilesUploadedLogBE.mkdirs())
              com.atos.utils.ClsLogging.writeFileLogWithoutSession("<***> Creating PSSC necessary folder: "+pathFilesUploadedLogbookExp,3);
            else
              com.atos.utils.ClsLogging.writeFileLogWithoutSession("<***> Error Creating PSSC necessary folder: "+pathFilesUploadedLogbookExp);
          }

          boolean directoryFileLogbookImpExists = fileFilesUploadedLogBI.exists() && fileFilesUploadedLogBI.isDirectory();
          //com.atos.utils.ClsLogging.writeFileLogWithoutSession("Exists PSSC necessary folder? : "+pathFilesUploadedLogbookImp+"---> "+directoryFileLogbookImpExists);
          if(!fileFilesUploadedLogBI.exists())
          {
            if (fileFilesUploadedLogBI.mkdirs())
              com.atos.utils.ClsLogging.writeFileLogWithoutSession("<***> Creating PSSC necessary folder: "+pathFilesUploadedLogbookImp,3);
            else
              com.atos.utils.ClsLogging.writeFileLogWithoutSession("<***> Error Creating PSSC necessary folder: "+pathFilesUploadedLogbookImp);
          }


          boolean directoryFileDDSExists = fileFilesUploadedDDS.exists() && fileFilesUploadedDDS.isDirectory();
          //com.atos.utils.ClsLogging.writeFileLogWithoutSession("Exists PSSC necessary folder? : "+pathFilesUploadedDDS+"---> "+directoryFileDDSExists);
          if(!fileFilesUploadedDDS.exists())
          {
            if (fileFilesUploadedDDS.mkdirs())
            com.atos.utils.ClsLogging.writeFileLogWithoutSession("<***> Creating PSSC necessary folder: "+pathFilesUploadedDDS,3);
            else
              com.atos.utils.ClsLogging.writeFileLogWithoutSession("<***> Error Creating PSSC necessary folder: "+pathFilesUploadedDDS);
          }

          boolean directoryAdvSearchExists = fileAdvSearchJrules.exists() && fileAdvSearchJrules.isDirectory();
          //com.atos.utils.ClsLogging.writeFileLogWithoutSession("Exists PSSC necessary folder? : "+pathAdvSearchJrules+"---> "+directoryAdvSearchExists);
          if(!fileAdvSearchJrules.exists())
          {
            if (fileAdvSearchJrules.mkdirs())
            com.atos.utils.ClsLogging.writeFileLogWithoutSession("<***> Creating PSSC necessary folder: "+pathAdvSearchJrules,3);
            else
              com.atos.utils.ClsLogging.writeFileLogWithoutSession("<***> Error Creating PSSC necessary folder: "+pathAdvSearchJrules);
          }

          boolean directoryAdvSearchsqlExists = fileAdvSearchSql.exists() && fileAdvSearchSql.isDirectory();
          //com.atos.utils.ClsLogging.writeFileLogWithoutSession("Exists PSSC necessary folder? : "+pathAdvSearchSql+"---> "+directoryAdvSearchsqlExists);
          if(!fileAdvSearchSql.exists())
          {
            if (fileAdvSearchSql.mkdirs())
              com.atos.utils.ClsLogging.writeFileLogWithoutSession("<***> Creating PSSC necessary folder: "+pathAdvSearchSql,3);
            else
              com.atos.utils.ClsLogging.writeFileLogWithoutSession("<***> Error Creating PSSC necessary folder: "+pathAdvSearchSql);
          }

          boolean directoryFilesConcessionsExists = fileFilesUploadedConcess.exists() && fileFilesUploadedConcess.isDirectory();
          //com.atos.utils.ClsLogging.writeFileLogWithoutSession("Exists PSSC necessary folder? : "+pathFilesUploadedConcess+"---> "+directoryFilesConcessionsExists);
          if(!fileFilesUploadedConcess.exists())
          {
            if (fileFilesUploadedConcess.mkdirs())
              com.atos.utils.ClsLogging.writeFileLogWithoutSession("<***> Creating PSSC necessary folder: "+pathFilesUploadedConcess,3);
            else
              com.atos.utils.ClsLogging.writeFileLogWithoutSession("<***> Error Creating PSSC necessary folder: "+pathFilesUploadedConcess);
          }

          boolean directoryFilesXMLExists = fileFilesUploadedXML.exists() && fileFilesUploadedXML.isDirectory();
          //com.atos.utils.ClsLogging.writeFileLogWithoutSession("Exists PSSC necessary folder? : "+pathFilesUploadedXML+"---> "+directoryFilesXMLExists);
          if(!fileFilesUploadedXML.exists())
          {
            if (fileFilesUploadedXML.mkdirs())
              com.atos.utils.ClsLogging.writeFileLogWithoutSession("<***> Creating PSSC necessary folder: "+pathFilesUploadedXML,3);
            else
              com.atos.utils.ClsLogging.writeFileLogWithoutSession("<***> Error Creating PSSC necessary folder: "+pathFilesUploadedXML);
          }

          boolean directoryResDirExists = fileResDir.exists() && fileResDir.isDirectory();
          //com.atos.utils.ClsLogging.writeFileLogWithoutSession("Exists PSSC necessary folder? : "+com.atos.utils.ClsConstants.RES_DIR+"---> "+directoryResDirExists);
          if(!fileResDir.exists())
          {
            if (fileResDir.mkdirs())
              com.atos.utils.ClsLogging.writeFileLogWithoutSession("<***> Creating PSSC necessary folder: "+com.atos.utils.ClsConstants.RES_DIR,3);
            else
              com.atos.utils.ClsLogging.writeFileLogWithoutSession("<***> Error Creating PSSC necessary folder: "+com.atos.utils.ClsConstants.RES_DIR);
          }

          boolean directoryResDirStrutsExists = fileResDirStruts.exists() && fileResDirStruts.isDirectory();
          //com.atos.utils.ClsLogging.writeFileLogWithoutSession("Exists PSSC necessary folder? : "+com.atos.utils.ClsConstants.RESOURCES_DIR_STRUTS+"---> "+directoryResDirStrutsExists);
          if(!fileResDirStruts.exists())
          {
            if (fileResDirStruts.mkdirs())
              com.atos.utils.ClsLogging.writeFileLogWithoutSession("<***> Creating PSSC necessary folder: "+com.atos.utils.ClsConstants.RESOURCES_DIR_STRUTS,3);
            else
              com.atos.utils.ClsLogging.writeFileLogWithoutSession("<***> Error Creating PSSC necessary folder: "+com.atos.utils.ClsConstants.RESOURCES_DIR_STRUTS);
          }

          boolean directoryResDirImagesIconsAfExists = fileResDirImagesIconsAf.exists() && fileResDirImagesIconsAf.isDirectory();
          //com.atos.utils.ClsLogging.writeFileLogWithoutSession("Exists PSSC necessary folder? : "+pathImagesAficons + "---> "+directoryResDirImagesIconsAfExists);
          if(!fileResDirImagesIconsAf.exists())
          {
            if (fileResDirImagesIconsAf.mkdirs())
              com.atos.utils.ClsLogging.writeFileLogWithoutSession("<***> Creating PSSC necessary folder: "+pathImagesAficons,3);
            else
              com.atos.utils.ClsLogging.writeFileLogWithoutSession("<***> Error Creating PSSC necessary folder: "+pathImagesAficons);
          }

          boolean directoryResDirImagesIconsAtExists = fileResDirImagesIconsAt.exists() && fileResDirImagesIconsAt.isDirectory();
          //com.atos.utils.ClsLogging.writeFileLogWithoutSession("Exists PSSC necessary folder? : "+pathImagesAticons + "---> "+directoryResDirImagesIconsAtExists);
          if(!fileResDirImagesIconsAt.exists())
          {
            if (fileResDirImagesIconsAt.mkdirs())
              com.atos.utils.ClsLogging.writeFileLogWithoutSession("<***> Creating PSSC necessary folder: "+pathImagesAticons,3);
            else
              com.atos.utils.ClsLogging.writeFileLogWithoutSession("<***> Error Creating PSSC necessary folder: "+pathImagesAticons);
          }

          boolean directoryResDirImagesIconsIoExists = fileResDirImagesIconsIo.exists() && fileResDirImagesIconsIo.isDirectory();
          //com.atos.utils.ClsLogging.writeFileLogWithoutSession("Exists PSSC necessary folder? : "+pathImagesIoicons + "---> "+directoryResDirImagesIconsIoExists);
          if(!fileResDirImagesIconsIo.exists())
          {
            if (fileResDirImagesIconsIo.mkdirs())
              com.atos.utils.ClsLogging.writeFileLogWithoutSession("<***> Creating PSSC necessary folder: "+pathImagesIoicons,3);
            else
              com.atos.utils.ClsLogging.writeFileLogWithoutSession("<***> Error Creating PSSC necessary folder: "+pathImagesIoicons);
          }

          boolean directoryResDirImagesIconsStExists = fileResDirImagesIconsSt.exists() && fileResDirImagesIconsSt.isDirectory();
          //com.atos.utils.ClsLogging.writeFileLogWithoutSession("Exists PSSC necessary folder? : "+pathImagesSticons + "---> "+directoryResDirImagesIconsStExists);
          if(!fileResDirImagesIconsSt.exists())
          {
            if (fileResDirImagesIconsSt.mkdirs())
              com.atos.utils.ClsLogging.writeFileLogWithoutSession("<***> Creating PSSC necessary folder: "+pathImagesSticons,3);
            else
              com.atos.utils.ClsLogging.writeFileLogWithoutSession("<***> Error Creating PSSC necessary folder: "+pathImagesSticons);
          }


          try {

            // icons
            this.copyFileWithinJar("/"+com.atos.utils.ClsConstants.IMAGESVIDEOUPLOAD+"/icons/MinHot.gif",pathImages+ com.atos.utils.ClsConstants.FILE_SEP + "icons" + com.atos.utils.ClsConstants.FILE_SEP +"MinHot.gif");
            this.copyFileWithinJar("/"+com.atos.utils.ClsConstants.IMAGESVIDEOUPLOAD+"/icons/PlusHot.gif",pathImages+ com.atos.utils.ClsConstants.FILE_SEP + "icons" + com.atos.utils.ClsConstants.FILE_SEP +"PlusHot.gif");
            this.copyFileWithinJar("/"+com.atos.utils.ClsConstants.IMAGESVIDEOUPLOAD+"/icons/PSSC.gif",pathImages+ com.atos.utils.ClsConstants.FILE_SEP + "icons" + com.atos.utils.ClsConstants.FILE_SEP +"PSSC.gif");
            this.copyFileWithinJar("/"+com.atos.utils.ClsConstants.IMAGESVIDEOUPLOAD+"/icons/service.gif",pathImages+ com.atos.utils.ClsConstants.FILE_SEP + "icons" + com.atos.utils.ClsConstants.FILE_SEP +"service.gif");
            this.copyFileWithinJar("/"+com.atos.utils.ClsConstants.IMAGESVIDEOUPLOAD+"/icons/storage.gif",pathImages+ com.atos.utils.ClsConstants.FILE_SEP + "icons" + com.atos.utils.ClsConstants.FILE_SEP +"storage.gif");
            this.copyFileWithinJar("/"+com.atos.utils.ClsConstants.IMAGESVIDEOUPLOAD+"/icons/store.gif",pathImages+ com.atos.utils.ClsConstants.FILE_SEP + "icons" + com.atos.utils.ClsConstants.FILE_SEP +"store.gif");
            this.copyFileWithinJar("/"+com.atos.utils.ClsConstants.IMAGESVIDEOUPLOAD+"/icons/transit.gif",pathImages+ com.atos.utils.ClsConstants.FILE_SEP + "icons" + com.atos.utils.ClsConstants.FILE_SEP +"transit.gif");

            this.copyFileWithinJar("/"+com.atos.utils.ClsConstants.IMAGESVIDEOUPLOAD+"/icons/afIcons/ALEMANIA.gif",pathImages+ com.atos.utils.ClsConstants.FILE_SEP + "icons" + com.atos.utils.ClsConstants.FILE_SEP + "afIcons"+ com.atos.utils.ClsConstants.FILE_SEP +"ALEMANIA.gif");
            this.copyFileWithinJar("/"+com.atos.utils.ClsConstants.IMAGESVIDEOUPLOAD+"/icons/afIcons/Bola.gif",pathImages+ com.atos.utils.ClsConstants.FILE_SEP + "icons" + com.atos.utils.ClsConstants.FILE_SEP + "afIcons"+ com.atos.utils.ClsConstants.FILE_SEP +"Bola.gif");
            this.copyFileWithinJar("/"+com.atos.utils.ClsConstants.IMAGESVIDEOUPLOAD+"/icons/afIcons/ESPAnA.gif",pathImages+ com.atos.utils.ClsConstants.FILE_SEP + "icons" + com.atos.utils.ClsConstants.FILE_SEP + "afIcons"+ com.atos.utils.ClsConstants.FILE_SEP +"ESPAnA.gif");
            this.copyFileWithinJar("/"+com.atos.utils.ClsConstants.IMAGESVIDEOUPLOAD+"/icons/afIcons/INGLATERRA.gif",pathImages+ com.atos.utils.ClsConstants.FILE_SEP + "icons" + com.atos.utils.ClsConstants.FILE_SEP + "afIcons"+ com.atos.utils.ClsConstants.FILE_SEP +"INGLATERRA.gif");
            this.copyFileWithinJar("/"+com.atos.utils.ClsConstants.IMAGESVIDEOUPLOAD+"/icons/afIcons/ITALIA.gif",pathImages+ com.atos.utils.ClsConstants.FILE_SEP + "icons" + com.atos.utils.ClsConstants.FILE_SEP + "afIcons"+ com.atos.utils.ClsConstants.FILE_SEP +"ITALIA.gif");

            this.copyFileWithinJar("/"+com.atos.utils.ClsConstants.IMAGESVIDEOUPLOAD+"/icons/atIcons/Bola.gif",pathImages+ com.atos.utils.ClsConstants.FILE_SEP + "icons" + com.atos.utils.ClsConstants.FILE_SEP + "atIcons"+ com.atos.utils.ClsConstants.FILE_SEP +"Bola.gif");
            this.copyFileWithinJar("/"+com.atos.utils.ClsConstants.IMAGESVIDEOUPLOAD+"/icons/atIcons/aircraft.gif",pathImages+ com.atos.utils.ClsConstants.FILE_SEP + "icons" + com.atos.utils.ClsConstants.FILE_SEP + "atIcons"+ com.atos.utils.ClsConstants.FILE_SEP +"aircraft.gif");
            this.copyFileWithinJar("/"+com.atos.utils.ClsConstants.IMAGESVIDEOUPLOAD+"/icons/atIcons/ASSEMBLY.gif",pathImages+ com.atos.utils.ClsConstants.FILE_SEP + "icons" + com.atos.utils.ClsConstants.FILE_SEP + "atIcons"+ com.atos.utils.ClsConstants.FILE_SEP +"ASSEMBLY.gif");
            this.copyFileWithinJar("/"+com.atos.utils.ClsConstants.IMAGESVIDEOUPLOAD+"/icons/atIcons/EMU.gif",pathImages+ com.atos.utils.ClsConstants.FILE_SEP + "icons" + com.atos.utils.ClsConstants.FILE_SEP + "atIcons"+ com.atos.utils.ClsConstants.FILE_SEP +"EMU.gif");
            this.copyFileWithinJar("/"+com.atos.utils.ClsConstants.IMAGESVIDEOUPLOAD+"/icons/atIcons/ENGINE.gif",pathImages+ com.atos.utils.ClsConstants.FILE_SEP + "icons" + com.atos.utils.ClsConstants.FILE_SEP + "atIcons"+ com.atos.utils.ClsConstants.FILE_SEP +"ENGINE.gif");
            this.copyFileWithinJar("/"+com.atos.utils.ClsConstants.IMAGESVIDEOUPLOAD+"/icons/atIcons/EX.gif",pathImages+ com.atos.utils.ClsConstants.FILE_SEP + "icons" + com.atos.utils.ClsConstants.FILE_SEP + "atIcons"+ com.atos.utils.ClsConstants.FILE_SEP +"EX.gif");
            this.copyFileWithinJar("/"+com.atos.utils.ClsConstants.IMAGESVIDEOUPLOAD+"/icons/atIcons/service.gif",pathImages+ com.atos.utils.ClsConstants.FILE_SEP + "icons" + com.atos.utils.ClsConstants.FILE_SEP + "atIcons"+ com.atos.utils.ClsConstants.FILE_SEP +"service.gif");
            this.copyFileWithinJar("/"+com.atos.utils.ClsConstants.IMAGESVIDEOUPLOAD+"/icons/atIcons/storage.gif",pathImages+ com.atos.utils.ClsConstants.FILE_SEP + "icons" + com.atos.utils.ClsConstants.FILE_SEP + "atIcons"+ com.atos.utils.ClsConstants.FILE_SEP +"storage.gif");
            this.copyFileWithinJar("/"+com.atos.utils.ClsConstants.IMAGESVIDEOUPLOAD+"/icons/atIcons/store.gif",pathImages+ com.atos.utils.ClsConstants.FILE_SEP + "icons" + com.atos.utils.ClsConstants.FILE_SEP + "atIcons"+ com.atos.utils.ClsConstants.FILE_SEP +"store.gif");
            this.copyFileWithinJar("/"+com.atos.utils.ClsConstants.IMAGESVIDEOUPLOAD+"/icons/atIcons/transit.gif",pathImages+ com.atos.utils.ClsConstants.FILE_SEP + "icons" + com.atos.utils.ClsConstants.FILE_SEP + "atIcons"+ com.atos.utils.ClsConstants.FILE_SEP +"transit.gif");

            this.copyFileWithinJar("/"+com.atos.utils.ClsConstants.IMAGESVIDEOUPLOAD+"/icons/loIcons/Bola.gif",pathImages+ com.atos.utils.ClsConstants.FILE_SEP + "icons" + com.atos.utils.ClsConstants.FILE_SEP + "loIcons"+ com.atos.utils.ClsConstants.FILE_SEP +"Bola.gif");
            this.copyFileWithinJar("/"+com.atos.utils.ClsConstants.IMAGESVIDEOUPLOAD+"/icons/loIcons/ALEMANIA.gif",pathImages+ com.atos.utils.ClsConstants.FILE_SEP + "icons" + com.atos.utils.ClsConstants.FILE_SEP + "loIcons"+ com.atos.utils.ClsConstants.FILE_SEP +"ALEMANIA.gif");
            this.copyFileWithinJar("/"+com.atos.utils.ClsConstants.IMAGESVIDEOUPLOAD+"/icons/loIcons/ESPAnA.gif",pathImages+ com.atos.utils.ClsConstants.FILE_SEP + "icons" + com.atos.utils.ClsConstants.FILE_SEP + "loIcons"+ com.atos.utils.ClsConstants.FILE_SEP +"ESPAnA.gif");
            this.copyFileWithinJar("/"+com.atos.utils.ClsConstants.IMAGESVIDEOUPLOAD+"/icons/loIcons/INGLATERRA.gif",pathImages+ com.atos.utils.ClsConstants.FILE_SEP + "icons" + com.atos.utils.ClsConstants.FILE_SEP + "loIcons"+ com.atos.utils.ClsConstants.FILE_SEP +"INGLATERRA.gif");
            this.copyFileWithinJar("/"+com.atos.utils.ClsConstants.IMAGESVIDEOUPLOAD+"/icons/loIcons/ITALIA.gif",pathImages+ com.atos.utils.ClsConstants.FILE_SEP + "icons" + com.atos.utils.ClsConstants.FILE_SEP + "loIcons"+ com.atos.utils.ClsConstants.FILE_SEP +"ITALIA.gif");

            this.copyFileWithinJar("/"+com.atos.utils.ClsConstants.IMAGESVIDEOUPLOAD+"/icons/stIcons/Bola.gif",pathImages+ com.atos.utils.ClsConstants.FILE_SEP + "icons" + com.atos.utils.ClsConstants.FILE_SEP + "stIcons"+ com.atos.utils.ClsConstants.FILE_SEP +"Bola.gif");
            this.copyFileWithinJar("/"+com.atos.utils.ClsConstants.IMAGESVIDEOUPLOAD+"/icons/stIcons/accessory.gif",pathImages+ com.atos.utils.ClsConstants.FILE_SEP + "icons" + com.atos.utils.ClsConstants.FILE_SEP + "stIcons"+ com.atos.utils.ClsConstants.FILE_SEP +"accessory.gif");
            this.copyFileWithinJar("/"+com.atos.utils.ClsConstants.IMAGESVIDEOUPLOAD+"/icons/stIcons/assembly.gif",pathImages+ com.atos.utils.ClsConstants.FILE_SEP + "icons" + com.atos.utils.ClsConstants.FILE_SEP + "stIcons"+ com.atos.utils.ClsConstants.FILE_SEP +"assembly.gif");
            this.copyFileWithinJar("/"+com.atos.utils.ClsConstants.IMAGESVIDEOUPLOAD+"/icons/stIcons/emu.gif",pathImages+ com.atos.utils.ClsConstants.FILE_SEP + "icons" + com.atos.utils.ClsConstants.FILE_SEP + "stIcons"+ com.atos.utils.ClsConstants.FILE_SEP +"emu.gif");
            this.copyFileWithinJar("/"+com.atos.utils.ClsConstants.IMAGESVIDEOUPLOAD+"/icons/stIcons/engine.gif",pathImages+ com.atos.utils.ClsConstants.FILE_SEP + "icons" + com.atos.utils.ClsConstants.FILE_SEP + "stIcons"+ com.atos.utils.ClsConstants.FILE_SEP +"engine.gif");
            this.copyFileWithinJar("/"+com.atos.utils.ClsConstants.IMAGESVIDEOUPLOAD+"/icons/stIcons/ex.gif",pathImages+ com.atos.utils.ClsConstants.FILE_SEP + "icons" + com.atos.utils.ClsConstants.FILE_SEP + "stIcons"+ com.atos.utils.ClsConstants.FILE_SEP +"ex.gif");
            this.copyFileWithinJar("/"+com.atos.utils.ClsConstants.IMAGESVIDEOUPLOAD+"/icons/stIcons/service.gif",pathImages+ com.atos.utils.ClsConstants.FILE_SEP + "icons" + com.atos.utils.ClsConstants.FILE_SEP + "stIcons"+ com.atos.utils.ClsConstants.FILE_SEP +"service.gif");
            this.copyFileWithinJar("/"+com.atos.utils.ClsConstants.IMAGESVIDEOUPLOAD+"/icons/stIcons/storage.gif",pathImages+ com.atos.utils.ClsConstants.FILE_SEP + "icons" + com.atos.utils.ClsConstants.FILE_SEP + "stIcons"+ com.atos.utils.ClsConstants.FILE_SEP +"storage.gif");
            this.copyFileWithinJar("/"+com.atos.utils.ClsConstants.IMAGESVIDEOUPLOAD+"/icons/stIcons/store.gif",pathImages+ com.atos.utils.ClsConstants.FILE_SEP + "icons" + com.atos.utils.ClsConstants.FILE_SEP + "stIcons"+ com.atos.utils.ClsConstants.FILE_SEP +"store.gif");
            this.copyFileWithinJar("/"+com.atos.utils.ClsConstants.IMAGESVIDEOUPLOAD+"/icons/stIcons/transit.gif",pathImages+ com.atos.utils.ClsConstants.FILE_SEP + "icons" + com.atos.utils.ClsConstants.FILE_SEP + "stIcons"+ com.atos.utils.ClsConstants.FILE_SEP +"transit.gif");
            this.copyFileWithinJar("/"+com.atos.utils.ClsConstants.IMAGESVIDEOUPLOAD+"/icons/stIcons/part.gif",pathImages+ com.atos.utils.ClsConstants.FILE_SEP + "icons" + com.atos.utils.ClsConstants.FILE_SEP + "stIcons"+ com.atos.utils.ClsConstants.FILE_SEP +"part.gif");
            this.copyFileWithinJar("/"+com.atos.utils.ClsConstants.IMAGESVIDEOUPLOAD+"/icons/stIcons/module.gif",pathImages+ com.atos.utils.ClsConstants.FILE_SEP + "icons" + com.atos.utils.ClsConstants.FILE_SEP + "stIcons"+ com.atos.utils.ClsConstants.FILE_SEP +"module.gif");
            this.copyFileWithinJar("/"+com.atos.utils.ClsConstants.IMAGESVIDEOUPLOAD+"/icons/stIcons/se.gif",pathImages+ com.atos.utils.ClsConstants.FILE_SEP + "icons" + com.atos.utils.ClsConstants.FILE_SEP + "stIcons"+ com.atos.utils.ClsConstants.FILE_SEP +"se.gif");
          }
          catch (IOException ex) {
             com.atos.utils.ClsLogging.writeFileLogWithoutSession("Error: "+ex.toString(),1);
             ex.printStackTrace();
          }
	
        com.atos.utils.ClsLogging.writeFileLogWithoutSession("** Done **",1);
        com.atos.utils.ClsLogging.writeFileLogWithoutSession("",1);
        }


        private void copyDir(String srcDir, String dstDir) throws IOException {

        com.atos.utils.ClsLogging.writeFileLogWithoutSession("src Dir = " + srcDir,3);
        com.atos.utils.ClsLogging.writeFileLogWithoutSession("dst Dir = " + dstDir,3);

        File fdstDir = new File(dstDir);

        if (! fdstDir.exists()) {
          fdstDir.mkdirs();
          com.atos.utils.ClsLogging.writeFileLogWithoutSession("creating  " +  dstDir,3);
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

        out.close( ) ;
        inp.close( ) ;

        }

         private void copyFileWithinJar(String srcFile, String dstFile) throws IOException {
           //com.atos.utils.ClsLogging.writeFileLogWithoutSession("Dentro de copyFileWithinJar srcFile:  " +  srcFile,3);

           ClassLoader classLoader = getClass().getClassLoader();
           InputStream inp = classLoader.getResourceAsStream(srcFile);

           File fileChk = new File(dstFile);

           boolean fileExists = fileChk.exists();
           //com.atos.utils.ClsLogging.writeFileLogWithoutSession("File: "+dstFile+", exists?: "+fileExists,3);
           if (!fileExists)
           {

             if (inp != null) {

               FileOutputStream out = new FileOutputStream(dstFile);

               byte[] buff = new byte[8192];
               int count;
               // read up to buff.length bytes
               while ( (count = inp.read(buff)) != -1) {
                 out.write(buff, 0, count);
               }

               out.close();

             }
             else {
               com.atos.utils.ClsLogging.writeFileLogWithoutSession("***> ERROR: " + srcFile +
                                  " not included in compiled PSSC classes",1);
             }
           }
           inp.close();
        }


	public void destroy() {
		ClsLogging.writeFileLogWithoutSession("Destroy servlet Authenticate",1);
	  }
}

