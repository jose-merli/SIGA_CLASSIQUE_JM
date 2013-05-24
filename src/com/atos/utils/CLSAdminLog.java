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
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.AdmRolAdm;
import com.siga.beans.AdmRolBean;
import com.siga.beans.AdmUsuariosAdm;
import com.siga.beans.AdmUsuariosBean;



/**
 * Esta clase permite gestionar el log de la administración de la aplicación
 *
 * 
 */
public class CLSAdminLog {
	
	private static final SimpleDateFormat sdfLong = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private static String fileName;
	private static final String nomFich = "LogAdmin.out";
	private static File ficLog;
	private static boolean bStoreFile;
	private static int contadorLineas = 0;
	private static int numentradas; // Número máximo de entradas en el archivo de log
	private static String comandoshell=null;
	
	private static boolean iniciado=false;
	
	/**
	 * Inicializa el log de la administración según los parámetros establecidos en
	 * Log.properties. Si no existe el archivo de log, lo crea.
	 */
	public static synchronized void init(){
		
		if(!iniciado) {
			iniciado=true;
			ClsLogging.writeFileLog("Inicializando Log ...",7);		 	
		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
			bStoreFile=rp.returnProperty("LogAdmin.run").equals("1");
			fileName=rp.returnProperty("LogAdmin.archivo");
			numentradas=Integer.parseInt(rp.returnProperty("LogAdmin.numentradas"));  
			if(numentradas<100)
				numentradas=100;
			comandoshell = rp.returnProperty("LogAdmin.comandoshell");			
		}
		if (contadorLineas == 0){
			contadorLineas = contarLineas();
		}else{
			contadorLineas ++;
		}		 
	}
	
	
	/**
	 * Borra todos los registros del archivo de log dejándolo a 0 bytes
	 * 
	 * @return	"true" si todo ha ido bien
	 * 			"false" si se ha producido algún problema
	 * @throws IOException
	 */
	public static synchronized boolean borrarTodos(){
		boolean borrado=false;
		if (!ficLog.exists()){
			ClsLogging.writeFileLog("Error Borrando Log: No existe el fichero",3);		 	
			
		}else if(ficLog.delete()){		
			contadorLineas = 0;			
			ficLog = new File(fileName+ File.separator + nomFich);
			try {
				borrado=ficLog.createNewFile();
			}catch(Exception _ex) {
				ClsLogging.writeFileLog("Error Borrando Log:"+_ex.toString(),1);
			}			 
		}
		return borrado;
	}
	
	
	/**
	 * Inserta una línea en el log con los campos: dirección ip, usuario, fecha y hora y mensaje.
	 * Los campos se separan por un punto y coma
	 * @param req		Se utiliza para obtener la ip desde la que se ha hecho la peticion
	 * @param ses		Se utiliza para obtener el nombre de usuario
	 * @param mensaje	Mensaje que describe la acción realizada
	 */
	@SuppressWarnings("unchecked")
	protected static void insertar(HttpServletRequest req, String idInstitucion, String idUsuario, String idRol, String mensaje, UsrBean usr){
		init();
		PrintWriter printer = null;
		try	{
			if(bStoreFile && !mensaje.equals("")){
				
				Date dat = Calendar.getInstance().getTime();
				
				if(contadorLineas > numentradas) {			
					if (comandoshell!=null && !comandoshell.equals("")){
				        try {
							Process shell = Runtime.getRuntime().exec(comandoshell);	
							shell.waitFor();
							int exitValue=shell.exitValue();
							if(exitValue>-1){
								borrarTodos();
							}
						} catch (Exception e) {
							ClsLogging.writeFileLog("Error Ejecutando "+comandoshell+": "+e.toString(),1);
						}
					}
				} else{
					ficLog = new File(fileName+ File.separator + nomFich);
				}
				
				printer = new PrintWriter(new BufferedWriter(new FileWriter(ficLog, true)));
				//
				
				//Tratamiento de la direccion IP: SERVIDOR
				//Nota: Si no esta la IP en sesion la toma del request
				String sIPS = "";
				if (req.getSession().getAttribute("IPSERVIDOR")==null)
					sIPS = UtilidadesString.obtenerIPServidor(req);
				else
					sIPS = (String)req.getSession().getAttribute("IPSERVIDOR");
				
				String sUsuario = "";
				String sNIF = "";
				String sRol = "";
				String sFecha = sdfLong.format(dat);
				String sDescripcion = mensaje;
				
				Hashtable<String, String> htUsuario = new Hashtable<String, String>();
				htUsuario.put(AdmUsuariosBean.C_IDUSUARIO, idUsuario);
				htUsuario.put(AdmUsuariosBean.C_IDINSTITUCION, idInstitucion);
				
				AdmUsuariosAdm admUsuario = new AdmUsuariosAdm(usr);
				Vector<AdmUsuariosBean> vUsuario = admUsuario.selectByPK(htUsuario);
				AdmUsuariosBean beanUsuario = (AdmUsuariosBean)vUsuario.elementAt(0);
				sUsuario = beanUsuario.getDescripcion(); 
				sNIF = beanUsuario.getNIF();
				Hashtable<String, String> htRol = new Hashtable<String, String>();
				htRol.put(AdmRolBean.C_IDROL, idRol);
				AdmRolAdm admRol = new AdmRolAdm(usr);
				Vector<AdmRolBean> vRol = admRol.selectByPK(htRol);
				AdmRolBean beanRol = (AdmRolBean)vRol.elementAt(0);
				sRol = beanRol.getDescripcion();
				
				printer.print(idInstitucion + "; ");
				printer.print(sIPS + "; ");
				printer.print(sUsuario + "; ");
				printer.print(sNIF + "; ");
				printer.print(sRol + "; ");
				printer.print(sFecha + "; ");
				printer.print(sDescripcion + "; ");
				printer.println("");
				
				printer.flush();
				printer.close();
			} else {
				ClsLogging.writeFileLog(mensaje,7);
			}			    
		} catch(Exception _ex) {
			ClsLogging.writeFileLogError("Error insertando en Log"+_ex.toString(),_ex,1);
		} finally {
		    try {
		    	if(printer!=null){
			        printer.flush();
					printer.close();
		    	}
		    } catch (Exception eee) {}
		    
		}
	}
	
	protected static int contarLineas(){
		int contador = 0;
		try{			
			ficLog = new File(fileName);
			if (!ficLog.exists()){
				ficLog.mkdirs();	
			
			}else{
				ficLog = new File(fileName+File.separator+nomFich);
				if(!ficLog.createNewFile()){
					BufferedReader sarchivo = new BufferedReader(new FileReader(ficLog));
					while (sarchivo.readLine() != null){
						contador++;
					}
					sarchivo.close();						
				}
			}
		}catch(Exception _ex) {
			ClsLogging.writeFileLogError("Error Contando lineas Log:"+_ex.toString(),_ex,1);
		}
		
		return contador++;
	}
	
	public static synchronized void escribirLogAdmin(HttpServletRequest req,String mensaje){
		UsrBean user=(UsrBean)req.getSession().getAttribute("USRBEAN");	
		String idInstitucion=user.getLocation();
		String idUsuario=user.getUserName();
		String idRol=user.getIdRol();
		insertar(req,idInstitucion,idUsuario,idRol,mensaje,user);
	}
}

