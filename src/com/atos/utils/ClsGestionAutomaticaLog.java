package com.atos.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Esta clase permite gestionar el log de la gestion automatica de solicitudes
 *
 * 
 */

public class ClsGestionAutomaticaLog {
	
	private static final String DATEFORMATLONG = "yyyy/MM/dd HH:mm:ss";
	private static final String DATEFORMATSHORT = "yyyyMMdd";
	private static final String sError = "\n***** ERROR ***** ";
	private static Integer NSEM = new Integer(0);
	private static String fileName;
	private static final String nomFich = "LogAutomatic.out";
	private static File ficLog;
	private static boolean bStoreFile;
	private static int loglevel=10;
	private static long nLastMod = 0;
	private static int contadorLineas = 0;
//	private static String archivolog="admin.log"; // Contiene la ruta del archivo de log
	private static int numentradas; // Número máximo de entradas en el archivo de log
	private static String comandoshell=null;
/**
 * Inicializa el log de la administración según los parámetros establecidos en
 * Log.properties. Si no existe el archivo de log, lo crea.
 */
	public static void init(){
		
		File f = new File(ClsConstants.RESOURCES_DIR+ClsConstants.FILE_SEP+"SIGA.properties");
		long lst = f.lastModified();
		synchronized (NSEM)
		{
			if(nLastMod != lst)
		    {
				nLastMod = lst;
		        ReadProperties rp=new ReadProperties("SIGA.properties");
		        bStoreFile=rp.returnProperty("LogSolicAutomaticas.run").equals("1");
		        fileName=rp.returnProperty("LogSolicAutomaticas.archivo");
		        numentradas=Integer.parseInt(rp.returnProperty("LogSolicAutomaticas.numentradas"));  
		        if(numentradas<100)
		        		numentradas=100;
		        comandoshell = rp.returnProperty("LogSolicAutomaticas.comandoshell");			
		        try
		        {		    
				    loglevel = Integer.parseInt(rp.returnProperty("LOG.level"));
		        }catch (Exception nfe){}
		      }
			if (contadorLineas == 0){
	        	contadorLineas = contarLineas();
	        }else{
	        	contadorLineas ++;
	        }		 
		 }
	}
	/**
	 * Borra todos los registros del archivo de log dejándolo a 0 bytes
	 * 
	 * @return	"true" si todo ha ido bien
	 * 			"false" si se ha producido algún problema
	 * @throws IOException
	 */
	public static boolean borrarTodos(){
		boolean borrado=false;
		if (!ficLog.exists()){
			ClsLogging.writeFileLogWithoutSession("Gestion automatica de logs : Fichero no existe");
		 } else if(ficLog.delete()){		
			contadorLineas = 0;			
			ficLog = new File(fileName+File.separator + nomFich);	
			try {
				borrado=ficLog.createNewFile();
			}catch(Exception _ex) {
				ClsLogging.writeFileLogWithoutSession("Gestion automatica de logs : Excepcion borrando fichero de log " + _ex.toString());
			}			 
		}
		return borrado;
	}


	/**
	 * Devuelve un string con la línea seleccionada
	 * @param 	linea	Número de la línea que queremos recuparar
	 * @return			Contiene la línea solicitada
	 */
	public static String obtenerLinea(int linea){
		return "";		
	}
	/**
	 * Inserta una línea en el log con los campos: identificador institucion, fecha, identificador solicitud, <br>
	 * tipo de solicitud y mensaje.
	 * Los campos se separan por un punto y coma
	 * @param req		Se utiliza para obtener la ip desde la que se ha hecho la peticion
	 * @param ses		Se utiliza para obtener el nombre de usuario
	 * @param mensaje	Mensaje que describe la acción realizada
	 */
	public static void insertar(String institucion, String solicitud, String tipoSolicitud, String mensaje){
		 
		init();
			try
			{
				if(bStoreFile && !mensaje.equals("")) {
					Date dat = Calendar.getInstance().getTime();
					SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMATLONG);
					SimpleDateFormat sdffile = new SimpleDateFormat(DATEFORMATSHORT);
					if(contadorLineas > numentradas) {									
						Process p = Runtime.getRuntime().exec(comandoshell);						
						borrarTodos();
					}
			        PrintWriter printer = new PrintWriter(new BufferedWriter(new FileWriter(ficLog, true)));
			        printer.println(institucion+"; "+sdf.format(dat)+"; "+solicitud+"; "+tipoSolicitud+"; "+mensaje);
			        printer.flush();
			        printer.close();
				} else {
					ClsLogging.writeFileLogWithoutSession(mensaje);					
			    }			    
			} catch(Exception _ex) {
				ClsLogging.writeFileLogWithoutSession("Gestion automatica de logs : Excepcion insertando mensaje " + _ex.toString());
			}			 
}
	
	/**
	 * Permite insertar una nueva línea en el archivo de log
	 * @param mensaje	Contenido de la línea a insertar.
	 */
	public static void insertar(String mensaje){
		
	}
	
	public static int contarLineas(){
		int contador = 0;
		try{			
			ficLog = new File(fileName);
		    if (!ficLog.exists()){
		       	ficLog.mkdirs();	
		       	ficLog = new File(fileName+File.separator+nomFich);			       
	        }
		    else{
		    	ficLog = new File(fileName+File.separator+nomFich);
		    	try {
					if(!ficLog.createNewFile()){
						BufferedReader sarchivo = new BufferedReader(new FileReader(ficLog));
				    	while (sarchivo.readLine() != null){
				    		contador++;
				    	}
				    	sarchivo.close();						
					}
				}catch(Exception _ex) {
					ClsLogging.writeFileLogWithoutSession("Gestion automatica de logs : Excepcion contando lineas" + _ex.toString());
				}				    	
		    }
		}catch(Exception _ex) {
			ClsLogging.writeFileLogWithoutSession("Gestion automatica de logs : Excepcion contando lineas" + _ex.toString());
	    }
		
	return contador++;
	}
	/**
	 * @return Returns the nomFich.
	 */
	public static String getNomFich() {
		return nomFich;
	}
	/**
	 * @return Returns the ficLog.
	 */
	public static File getFicLog() {
		return ficLog;
	}
	
	public static void escribirLogGestionAutomatica(String institucion, String solicitud, String tipoSolicitud, String mensaje)
	{	
		insertar(institucion,solicitud,tipoSolicitud,mensaje);
	}
}

