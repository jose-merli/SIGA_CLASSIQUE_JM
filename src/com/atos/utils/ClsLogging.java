/**
 * <p>Title: ClsLogging </p>
 * <p>Description: Implementación del LOG de SIGA. </p>
 * <p>Copyright: Copyright (C) 2006</p>
 * <p>Company: AtosOrigin </p>
 * @author Luis Miguel Sánchez PIÑA.
 * @version 1.0
 */

package com.atos.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.mail.SendFailedException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.redabogacia.sigaservices.app.util.PropertyReader;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;
import org.redabogacia.sigaservices.app.util.SIGAReferences.RESOURCE_FILES;

import com.siga.eejg.SchedulerException;
import com.sun.mail.smtp.SMTPAddressFailedException;


public class ClsLogging{
	private static final SimpleDateFormat sdfLong = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); 
	private static final SimpleDateFormat sdfShort = new SimpleDateFormat("yyyyMMdd"); 
	private static final String sError = "\n***** ERROR ***** ";
	private static String fileName;
	private static int loglevel=10;
	
	private static boolean iniciado=false;
	private static boolean bStoreFile; //Indica si se escribirá en el fichero de log de la capa básica.
	private static boolean bConsole=true;   //Indica si se escribirá en la consola. 
	private static boolean bLog4j;     //Indica si se escribirá en el fichero de log de log4j.
	private static boolean bLogXeMail;     //Indica si se escribirá en el fichero de log de log4j.
	
	//private static Logger logger=null;
	private static Logger logger = null;
	private static Logger logXeMail = null;
	
		
	
	/**
	 *  Read the configuration parameters of the Logs.
	 */
	private static synchronized void init() {
		//File f = new File(ClsConstants.RESOURCES_DIR+ClsConstants.FILE_SEP+"SIGA.properties");
//		long lst=0;
//		try {
//			File f = SIGAReferences.getFileReference(SIGAReferences.RESOURCE_FILES.INIT);
//			if (f!=null)
//				lst = f.lastModified();
//		} catch (Exception e){
//		}
//		if(nLastMod != lst) {
//			nLastMod = lst;
		if(!iniciado) {
			iniciado=true;
			//ClsLogging.writeFileLog("CLSLOGGING INIT() SE EJECUTA",3);
		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties rp=new ReadProperties("SIGA.properties");

			//LMS 29/08/2006
			//Esto viene de la capa básica, pero se ha adaptado a SIGA.
			//bStoreFile=rp.returnProperty("LOG.run").equals("1");
			bStoreFile=rp.returnProperty("LOG.fichero").equals("1");
			bConsole=rp.returnProperty("LOG.consola").equals("1");
			bLog4j=rp.returnProperty("LOG.log4j").equals("1");
			bLogXeMail=rp.returnProperty("LOG.email").equals("1");
			
			try {
				StringBuilder strBld = new StringBuilder();
				strBld.append(SIGAReferences.RESOURCE_FILES.WEB_INF_DIR.getFileName())
						.append(rp.returnProperty("LOG.dir")).append("/").append(rp.returnProperty("LOG.name"));
				fileName=SIGAReferences.getReference(strBld.toString());
			} catch (Exception e){
				e.printStackTrace();
			}
			
			try{
				loglevel = Integer.parseInt(rp.returnProperty("LOG.level").trim());
			}catch (Exception nfe){
				//nfe.printStackTrace();
				loglevel = 10;
			}
			
			try{
				PropertyConfigurator.configure(PropertyReader.getProperties(RESOURCE_FILES.SIGA.LOG4J));
				logger = Logger.getLogger("SIGA");
			}catch(Exception e){
				logger=null;
				e.printStackTrace();
			}
			
			try{
				logXeMail = Logger.getLogger("EMAIL");
			}catch(Exception e){
				logXeMail=null;
				e.printStackTrace();
			}
			
			//logger.debug("GESTION DEL LOG: logLevel Init="+loglevel);
			ClsLogging.writeFileLog("--------------------",3);
			ClsLogging.writeFileLog("Info del LOG de SIGA",3);
			ClsLogging.writeFileLog("--------------------",3);
			ClsLogging.writeFileLog("- Fichero: " + (bStoreFile?"ACTIVADO":"DESACTIVADO"),3);
			ClsLogging.writeFileLog("- Consola: " + (bConsole?"ACTIVADA":"DESACTIVADA"),3);
			ClsLogging.writeFileLog("- Log4j:   " + (bLog4j?"ACTIVADO":"DESACTIVADO"),3);
			ClsLogging.writeFileLog("- LogXeMail:   " + (bLogXeMail?"ACTIVADO":"DESACTIVADO"),3);
			ClsLogging.writeFileLog("--------------------",3);
		}
	}
	
	public static void reset(){
		iniciado = false;
		init();
	}
	
	public static void writeFileLog(String s, int i) {
		ClsLogging.writeFileLog(s, null, i);  
	}
	
	public static void writeFileLog(String s) {
		init();
		ClsLogging.writeFileLogWithoutSession(s);  
	}
		
	/**
	 *
	 * @param s
	 * @param httpservletrequest
	 * @param i
	 */
	public static void writeFileLog(String s, HttpServletRequest httpservletrequest, int i)
	{
		if (httpservletrequest==null)
		{
			init();
			if(i <= loglevel)  {
				writeFileLogWithoutSession(s,i);
			}
		} else {
			HttpSession httpsession = httpservletrequest.getSession(true);
			writeFileLog(s, httpservletrequest.getHeader("user-agent"), httpsession.getId(),
					(UsrBean)httpsession.getAttribute("USRBEAN"),
					httpservletrequest.getRemoteAddr(), i, false);
		}
	}
	
	/**
	 *
	 * @param s
	 * @param httpservletrequest
	 * @param i
	 */
	public static void writeFileLogError(String s, HttpServletRequest httpservletrequest, int i)
	{
		if (httpservletrequest==null)
		{
			init();
			if(i <= loglevel)
			{
				writeFileLogWithoutSession(s,i);
			}
		} else {
			HttpSession httpsession = httpservletrequest.getSession(true);
			writeFileLogError(s, httpservletrequest.getHeader("user-agent"), httpsession.getId(),
					(UsrBean)httpsession.getAttribute("USRBEAN"),
					httpservletrequest.getRemoteAddr(), i);
		}
	}
	
	/**
	 *
	 * @param s
	 * @param userAgent
	 * @param IDSession
	 * @param usrbean
	 * @param remoteAddress
	 * @param i
	 */
	public static void writeFileLog(String s, String userAgent, String IDSession,
			UsrBean usrbean, String remoteAddress, int i)
	{
		writeFileLog(s, userAgent, IDSession, usrbean, remoteAddress, i, false);
	}
	
	/**
	 *
	 * @param s
	 * @param userAgent
	 * @param IDSession
	 * @param usrbean
	 * @param remoteAddress
	 * @param i
	 */
	public static void writeFileLogError(String s, String userAgent, String IDSession,
			UsrBean usrbean, String remoteAddress, int i)
	{
		writeFileLog(s, userAgent, IDSession, usrbean, remoteAddress, i, true);
	}
	
	/**
	 *
	 * @param s
	 * @param userAgent
	 * @param IDSession
	 * @param usrbean
	 * @param remoteAddress
	 * @param i
	 * @param error
	 */
	private static void writeFileLog(String s, String userAgent,
			String IDSession, UsrBean usrbean,
			String remoteAddress, int i, boolean error)
	{
		init();
		PrintWriter printer = null; 
		boolean isErrorUser=false;
		try
		{
			if(i <= loglevel)
			{
				String snav = "";
				String ssys = "";
				if(userAgent!=null)
				{
					try
					{
						int j = userAgent.indexOf("(compatible");
						if(j != -1)
						{
							snav = "Explorer";
							String aux = userAgent.substring(j, userAgent.indexOf(")")).trim();
							int k = aux.indexOf("MSIE");
							if(k!=-1)
							{
								int z= aux.indexOf(";", k);
								if(z!=-1)
									snav+=aux.substring(k+4,z);
								else
									snav+=aux.substring(k+4);
							}
							k = aux.lastIndexOf(";");
							ssys = k==-1 ? aux : aux.substring(k+1).trim();
						} else {
							int k = userAgent.indexOf(" ");
							snav = k!=-1 ? userAgent.substring(0, k).replace('/', ' ') : userAgent;
							k = userAgent.indexOf("(");
							if(k!=-1)
								ssys = userAgent.substring(k+1, userAgent.indexOf(")",k));
						}
					} catch (Exception e){}
				}
				
				StringBuffer trace2 = new StringBuffer();
				trace2.append((error ? sError.substring(1,13) : "")+ssys);
				trace2.append(",");
				if (usrbean!=null)
				{
					trace2.append(usrbean.getUserName());
					trace2.append(",");
					trace2.append(usrbean.getLanguage());
					trace2.append(",");
				} else
				{
					trace2.append("USER NOT AUTHENTICATED,NO LANGUAGE,");
					isErrorUser=true;
				}
				trace2.append(snav);
				trace2.append(":");
				trace2.append(remoteAddress);
				trace2.append("-");
				trace2.append(IDSession);
				if(error)
				{
					trace2.append("\n"+s);
					trace2.append(sError);
				} else
				{
					trace2.append(","+s);
				}
				
				StringBuffer trace4console = new StringBuffer();
				Date dat = Calendar.getInstance().getTime();
				trace4console.append(sdfLong.format(dat));
				trace4console.append(",");
				trace4console.append(trace2);
				if (bConsole)
				{
					logger.debug(trace4console.toString());
				}
				
				if (bLog4j && logger!=null)
				{			
					logger.info(trace2.toString());
				}
				if (bLogXeMail && logXeMail!=null && !isErrorUser)
				{
					if (error) {
						logXeMail.error(trace2.toString());
					} else {
						logXeMail.info(trace2.toString());
					}
				
				}
				
				if (bLog4j && logger!=null)
				{
					logger.error(",***** ERROR *****,");
					logger.error(s+sError);
				}
				
				if (bStoreFile)
				{
					printer = new PrintWriter(new BufferedWriter(new FileWriter(fileName+sdfShort.format(dat) +".out", true)));
					printer.println(trace4console.toString());
					printer.flush();
					printer.close();
				}
			}
		} catch(Exception _ex) {
			logger.error("Error Escribiendo Log en " + fileName + ":" + _ex.toString());
			_ex.printStackTrace();
		} finally {
		    try {
		        printer.flush();
				printer.close();  
		    } catch (Exception eee) {
		    	logger.error("ERROR: " + eee.getMessage());
		    }
		}
	}
	

	
	public static void writeFileLogError(String s, Exception e, int i)
	{
		writeFileLogError(s, e, "", "", i);
	}
	public static void writeFileLogError(String s, Exception e, UsrBean usr, int i)
	{
		if (usr!=null) {
		    writeFileLogError(s, e, usr.getLocation(), usr.getUserName(), i);
		} else {
		    writeFileLogError(s, e, i);
		}
	}

	/**
	 *
	 * @param s
	 * @param i
	 */
	public static void writeFileLogError(String s, Exception e, String idInstitucion, String idUsuario, int i)
	{
		init();
		PrintWriter printer = null; 
		try
		{
			if (exclusionExcepciones(e)) { // devuelve true si no hay que excluirla
			if(i <= loglevel)
			{
				Date dat = Calendar.getInstance().getTime();

				if (bConsole)
				{
					if(e instanceof SchedulerException){
						logger.debug(sdfLong.format(dat)+",******AVISO******"+e.getMessage());
					}else{
						logger.debug(sdfLong.format(dat)+",***** ERROR *****,");
						logger.debug(s+sError);
						logger.debug(ExceptionManager.getCompleteMessageParaLogger(e,idInstitucion,idUsuario));
					}
						//logger.debug(sdfLong.format(dat)+",***** TRAZA *****,");
						//e.printStackTrace(System.out);
				}

				if (bLog4j && logger!=null)
				{
					if(e instanceof SchedulerException){
						logger.error(sdfLong.format(dat)+",******AVISO******"+e.getMessage());
						
					}else{
						logger.error("***** ERROR *****,");
						logger.error(s+sError);
						logger.error(ExceptionManager.getCompleteMessageParaLogger(e,"",""));
					}
						//logger.error(",***** TRAZA *****,",e);
				}
					//logger.debug("EXCEPTION:***************"+e.getClass().getName());
					if (bLogXeMail && logXeMail!=null)
					{
						if(e instanceof SchedulerException){
							logXeMail.error(sdfLong.format(dat)+"******AVISO******"+e.getMessage());
						}else if(!(e  instanceof SMTPAddressFailedException && e instanceof SendFailedException)){
							logXeMail.error("*****ERROR*****"+s+sError+ExceptionManager.getCompleteMessageParaLogger(e,idInstitucion,idUsuario),e);
						}
					}
				
				if (bStoreFile)
				{
					if(e instanceof SchedulerException){
						printer = new PrintWriter(new BufferedWriter(new FileWriter(fileName+sdfShort.format(dat) +".out", true)));
						printer.println(sdfLong.format(dat)+",******AVISO******"+e.getMessage());
						printer.flush();
						printer.close();
					}else{
						printer = new PrintWriter(new BufferedWriter(new FileWriter(fileName+sdfShort.format(dat) +".out", true)));
						printer.println(sdfLong.format(dat)+",***** ERROR *****,");
						printer.println(s+sError);
							printer.println(ExceptionManager.getCompleteMessageParaLogger(e,idInstitucion,idUsuario));
							//printer.println(sdfLong.format(dat)+",***** TRAZA *****,");
							//e.printStackTrace(printer);
						printer.flush();
						printer.close();
					}
				} 
				
			}
			} // if de control de exclusión de excepciones
			
		} catch(Exception _ex) {
			logger.error("Error Escribiendo Log : "+_ex.toString());
		} finally {
		    try {
		        printer.flush();
				printer.close();
		    } catch (Exception eee) {
		    	logger.error("ERROR: " + eee.getMessage());
		    }
		}
	}
	
	/**
	 * 
	 * @param s
	 * @param i
	 */
	public static void writeFileLogWithoutSession(String s, int i)
	{
		init();
		PrintWriter printer = null;
		try
		{
			if(i <= loglevel)
			{
				Date dat = Calendar.getInstance().getTime();

				if (bConsole)
				{
					logger.debug(sdfLong.format(dat)+","+s);
				}

				if (bLog4j && logger!=null)
				{
					logger.info(s);
				}
				if (bLogXeMail && logXeMail!=null)
				{
					logXeMail.info(s);
				}

				if (bStoreFile)
				{
					FileHelper.mkdirs(fileName.substring(0, fileName.lastIndexOf(System.getProperty("file.separator"))));
					File ficLog = new File(fileName+sdfShort.format(dat) +".out");
					printer = new PrintWriter(new BufferedWriter(new FileWriter(ficLog, true)));
					printer.println(sdfLong.format(dat)+","+s);
					printer.flush();
					printer.close();
				} 
			}
		} catch(Exception _ex) {
			logger.error("Error Escribiendo Log: "+_ex.toString());
		} finally {
		    try {
		        printer.flush();
				printer.close(); 
		    } catch (Exception eee) {
		    	logger.error("ERROR: " + eee.getMessage());
		    }
		}
	}
	
	/**
	 * Deberia ser lo mismo que sin indicar el nivel.
	 * @param s
	 */
	public static void writeFileLogWithoutSession(String s)
	{
		init();
		PrintWriter printer = null;
		try
		{
			Date dat = Calendar.getInstance().getTime();

			if (bConsole)
			{
				logger.debug(sdfLong.format(dat)+s);
			}

			if (bLog4j && logger!=null)
			{
				logger.info(s);
			}

//			if (bLogXeMail && logXeMail!=null)
//			{
//				logXeMail.info(s);
//			}

			if(bStoreFile)
			{
				printer = new PrintWriter(new BufferedWriter(new FileWriter(fileName+sdfShort.format(dat) +".out", true)));
				printer.println(sdfLong.format(dat)+ s);
				printer.flush();
				printer.close();
			} 
		} catch(Exception _ex) {
			logger.error("Error Escribiendo Log :"+_ex.toString());
		} finally {
		    try {
		        printer.flush();
				printer.close();
		    } catch (Exception eee) {
		    	logger.error("ERROR: " + eee.getMessage());
		    }
		}
	}
	
	private static boolean exclusionExcepciones(Exception e) {
		
		if (e==null){ 
			return false;
		}
		else if(e instanceof SocketException){ 
			return false;
		}else if (e.getMessage()!=null && e.getMessage().toUpperCase().indexOf("BROKEN PIPE")>0){
			return false;
		}else if(e instanceof SMTPAddressFailedException){ 
			return false;
		}else if(e instanceof SendFailedException){ 
			return false;
		}
		else if(e instanceof ServletException){ 
			return false;
		}
		
		
		
		return true;
}}