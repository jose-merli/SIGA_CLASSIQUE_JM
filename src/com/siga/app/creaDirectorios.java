/*
 * Created on Jun 15, 2005 Version 1.0.
 * Modified on Jan 29, 2007 Cambio en procedimiento y parametros. Version 2.0. 
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
//import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
//import java.io.FileReader;
//import java.io.FileWriter;
import java.io.IOException;
//import java.io.PrintWriter;
import java.util.ResourceBundle;

//import com.atos.utils.ClsConstants;
//import com.atos.utils.ClsExceptions;
//import com.atos.utils.ReadProperties;
import com.siga.beans.*;
//import com.siga.certificados.Plantilla;
//import com.siga.general.SIGAException;

//import java.sql.*;

/**
 * Aplicacion para la creacion de directorios y copia de plantillas para la aplicacion SIGA. Se lanza con el script directorios.sh<br>
 * Depende del fichero de configuracion SIGA.properties<br>
 * Funciona con parametros:<br>
 * &nbsp;<b>directorios</b>: Crea todos los directorios necesarios para las instituciones configuradas en propiedades.<br> 
 * &nbsp;<b>plantillas</b>: Copia y por lo tanto reescribe todas las plantillas para las institucion configuradas en propiedades.<br> 
 * &nbsp;<b>actualiza</b>: Copia y por lo tanto machaca para todas las instituciones creadas las plantillas entregadas como modificadas en el directorio /plantillas_modificadas/.<br>
 * &nbsp;<b>personaliza</b>: Copia y por lo tanto machaca las plantillas entregadas como personalizadas en el directorio /plantillas_personalizadas/. Estas plantillas ya van creadas para cada institucion por lo que no se duplican en las instituciones.<br>
 * 
 * @author raul.ggonzalez
 * 
 */
public class creaDirectorios {

	/** Path origen de las plantillas */
	private static String pathOrigenPlantillas = "";
	private static String pathModelo = "";
	private static String pathModificadas = "";
	private static String pathPersonalizadas = "";
	/** vector con el conjunto de instituciones a tratar */
	private static Vector instituciones = new Vector();
	/** vector con el conjunto de instituciones a tratar dadas de alta */
	private static Vector institucionesAlta = new Vector();
	/** booleano que dice si hay que hacerlo para todas las instituciones */
	private static boolean todasInstituciones = false;
	/** Conexion a base de datos */
	private static Connection con = null;
	/** Statement para base de datos */
	private static Statement st = null;
	/** clase de conexion */
	private static String claseConexion = null;
	/** cadena de conexion */
	private static String cadenaConexion = null;
	/** usuario de conexion */
	private static String usuario = null;
	/** clave de conexion */
	private static String clave = null;
	/** Institucion usada por defecto cuando no exista la tratada */
	private static String institucionDefecto = null;
	
	/** si es el usuario Directorios */
	private static boolean bDirectorios = false;
	/** si es el usuario Plantillas */
	private static boolean bPlantillas = false;
	/** si es el usuario Actualiza */
	private static boolean bActualiza = false;
	/** si es el usuario Personaliza */
	private static boolean bPersonaliza = false;

	private static ResultSet rs = null; 

	
	/**
	 * Obtiene los parametros y ejecuta el proceso
	 * @param args
	 */
	public static void main(String[] args) {
		
		 try {

		 	System.out.println(" ");
		 	System.out.println("--- SIGA DIRECTORIOS 2.0 ---");
		 	System.out.println("Aplicacion de creacion de directorios y copia de plantillas para aplicacion SIGA. Version 2.0");
		 	System.out.println(" ");

		 	String argumento = "";
		 	
		 	if (args!=null && args.length>0) {
		 		if (args[0].trim().equals("?")) {
		 			System.out.println("AYUDA:");
		 			System.out.println("-----");
		 			System.out.println("Esta aplicación crea el arbol de directorios adicionales para informes y descargas.");
				 	System.out.println("Ademas crea los directorios de las instituciones introducidas como parametro.");
		 			System.out.println("Posteriormente copia el arbol de plantillas al destino en el despliegue SIGA.");
		 			System.out.println(" PARAMETROS: (Utilizar uno por cada ejecución) ");
		 			System.out.println(" directorios .- Crea todos los directorios necesarios para las instituciones configuradas en propiedades.");
		 			System.out.println(" plantillas  .- Copia y por lo tanto reescribe todas las plantillas para las institucion configuradas en propiedades.");
		 			System.out.println(" actualiza   .- Copia y por lo tanto machaca para todas las instituciones creadas las plantillas entregadas como modificadas en el directorio /plantillas_modificadas/.");
		 			System.out.println(" personaliza .- Copia y por lo tanto machaca las plantillas entregadas como personalizadas en el directorio /plantillas_personalizadas/. Estas plantillas ya van creadas para cada institucion por lo que no se duplican en las instituciones.");
		 			System.out.println(" ");
		 			System.exit(0);
		 		} else {
		 			argumento = args[0].trim();
				 	System.out.println(" argumento: "+argumento);
				 	System.out.println(" ----------------------");
		 		}
		 	}
		 	
		 	if (argumento==null || argumento.trim().equals("")) {
		 		throw new Exception("Es necesario indicar uno de los parametro posibles. consule la ayuda con parámetro ?.");
		 	}
		 	
			// obtengo los nombres de directorios de plantillas desde properties
		 	
		 	ResourceBundle rp=ResourceBundle.getBundle("SIGA");
		 	pathOrigenPlantillas = rp.getString("directorios.path.OrigenPlantillas");
		 	pathModelo = rp.getString("directorios.path.modelo");
		 	pathModificadas = rp.getString("directorios.path.modificadas");
		 	pathPersonalizadas = rp.getString("directorios.path.personalizadas");
		 	
		 	claseConexion = rp.getString("directorios.bd.clase");
		 	cadenaConexion = rp.getString("directorios.bd.cadenaConexion");
		 	usuario = rp.getString("directorios.bd.usu");
		 	clave = rp.getString("directorios.bd.pwd");
		 	institucionDefecto = rp.getString("directorios.institucion.defecto");
		 	String auxInstitucion = rp.getString("directorios.institucion.identificadores");

		 	
		 	if (argumento.equals("directorios")) {
		 		bDirectorios = true;
			 	System.out.println(" Crea todos los directorios necesarios para las instituciones configuradas en propiedades. ");
			 	System.out.println(" ");

		 	} else
		 		if (argumento.equals("plantillas")) {
			 		bPlantillas = true;
				 	System.out.println(" Copia y por lo tanto reescribe todas las plantillas para las institucion configuradas en propiedades. ");
				 	System.out.println(" ");
		 	} else
		 		if (argumento.equals("actualiza")) {
			 		bActualiza = true;
				 	System.out.println(" Copia y por lo tanto machaca para todas las instituciones creadas las plantillas entregadas como modificadas en el directorio /plantillas_modificadas/. ");
				 	System.out.println(" ");
		 	} else
		 		if (argumento.equals("personaliza")) {
			 		bPersonaliza = true;
				 	System.out.println(" Copia y por lo tanto machaca las plantillas entregadas como personalizadas en el directorio /plantillas_personalizadas/. Estas plantillas ya van creadas para cada institucion por lo que no se duplican en las instituciones. ");
				 	System.out.println(" ");
		 	} else {
		 		throw new Exception("Es necesario indicar uno de los parametro posibles. consule la ayuda con parámetro ?.");
		 	}

		 	StringTokenizer tokenizer = new StringTokenizer(auxInstitucion,",");
		 	while (tokenizer.hasMoreTokens()) {
		 		String insti = tokenizer.nextToken();
		 		CenInstitucionBean instiBean = new CenInstitucionBean();
				instiBean.setIdInstitucion(new Integer(insti));
				instituciones.add(instiBean);
	        }

			
		 	
		 	conecta();
		 	
		 	
		 	
		 	// compruebo que la institucion es cero
		 	if (instituciones.size()==1) {
		 		CenInstitucionBean instiBean = (CenInstitucionBean) instituciones.get(0);
		 		String inst = instiBean.getIdInstitucion().toString();
		 		if (inst.equals("0")) {
		 			todasInstituciones = true;
		 		}
		 	}

			if (todasInstituciones) {
				instituciones = obtenerTodasInstituciones();
			}
			institucionesAlta = obtenerInstitucionesAlta();
		 	
			if (bDirectorios) {
			 	//	calculo los directorios a crear y los creo
			 	Vector directorios = obtenerDirectorios();
				if (directorios!=null && directorios.size()>0) {
					crearDirectorios(directorios);
					System.out.println(" ");
					System.out.println("ATENCION: NO OLVIDE MODIFICAR LOS PERMISOS EN LOS DIRECTORIOS CREADOS.");
				}
			}
			
			if (bPlantillas) {
				// copio las plantillas todas
				if ((pathOrigenPlantillas+pathModelo)!=null && !(pathOrigenPlantillas+pathModelo).trim().equals("")) {
					copiaRecursivo(pathOrigenPlantillas+pathModelo,instituciones);
				}
			}
			
			if (bActualiza) {
				// copio las plantillas modificadas
				if ((pathOrigenPlantillas+pathModificadas)!=null && !(pathOrigenPlantillas+pathModificadas).trim().equals("")) {
					copiaRecursivo(pathOrigenPlantillas+pathModificadas,institucionesAlta);
				}
			}
			
			if (bPersonaliza) {
				// copio las plantillas personalizadas
				if ((pathOrigenPlantillas+pathPersonalizadas)!=null && !(pathOrigenPlantillas+pathPersonalizadas).trim().equals("")) {
					// recorro las instituciones que hay debajo.
					for (int h=0;h<institucionesAlta.size();h++) {
						CenInstitucionBean insti = (CenInstitucionBean) institucionesAlta.get(h); 
						copiaRecursivoModificadas(pathOrigenPlantillas+pathPersonalizadas,insti.getIdInstitucion().toString());
					}
				}
			}
			
			System.out.println(" ");
			System.out.println("FIN CORRECTO");
			
		 } catch (Exception e) {
		 	System.out.println ("ERROR EN APP CREADIRECTORIOS. SIGA.");
		 	System.out.println ("----- -- --- ---------------  ---- ");
		 	e.printStackTrace();
			System.out.println(" ");
		 	System.out.println("FIN ERRONEO");
		 } finally {
		 	try {
		 		con.close();
		 	} catch (Exception e1) {}
		 }
	}
	

	
	
	private static Vector obtenerDirectorios() throws Exception {
		
	  try {
			
		Vector salida = new Vector();

	
		///////////////////////
		///   PARAMETROS    ///
		///////////////////////

	//	String pathApp = valorParam("0","CEN",ClsConstants.PATH_APP,"");
		// esta no la creamos porque tiene que existir, por narices.

		if (bDirectorios) {
			
			//impreso190, ademas creamos un directorio por cada institucion
			if (instituciones!=null && instituciones.size()>0) {
				for (int j=0;j<instituciones.size();j++) {
					CenInstitucionBean insti = (CenInstitucionBean) instituciones.get(j); 
					String path = valorParam(insti.getIdInstitucion().toString(),"FCS","PATH_IMPRESO190",null);
					if (path!=null) salida.add(path+ File.separator + insti.getIdInstitucion().toString());
				}
			}

			//previsiones
			if (instituciones!=null && instituciones.size()>0) {
				for (int j=0;j<instituciones.size();j++) {
					CenInstitucionBean insti = (CenInstitucionBean) instituciones.get(j); 
					String path = valorParam(insti.getIdInstitucion().toString(),"FCS","PATH_PREVISIONES",null);
					if (path!=null) salida.add(path);
				}
			}
	
			//previsiones BD
			if (instituciones!=null && instituciones.size()>0) {
				for (int j=0;j<instituciones.size();j++) {
					CenInstitucionBean insti = (CenInstitucionBean) instituciones.get(j); 
					String path = valorParam(insti.getIdInstitucion().toString(),"FCS","PATH_PREVISIONES_BD",null);
					if (path!=null) salida.add(path);
				}
			}
		
			//documentos adjuntos, ademas creamos un directorio por cada institucion
			if (instituciones!=null && instituciones.size()>0) {
				for (int j=0;j<instituciones.size();j++) {
					CenInstitucionBean insti = (CenInstitucionBean) instituciones.get(j); 
					String path = valorParam(insti.getIdInstitucion().toString(),"ENV","PATH_DOCUMENTOSADJUNTOS",null);
					if (path!=null) salida.add(path+ File.separator + insti.getIdInstitucion().toString());
				}
			}

			//certificados , ademas creamos un directorio por cada institucion
			if (instituciones!=null && instituciones.size()>0) {
				for (int j=0;j<instituciones.size();j++) {
					CenInstitucionBean insti = (CenInstitucionBean) instituciones.get(j); 
					String path = valorParam(insti.getIdInstitucion().toString(),"CER","PATH_CERTIFICADOS",null);
					if (path!=null) salida.add(path + File.separator + insti.getIdInstitucion().toString());
				}
			}

			//certificados digitales , ademas creamos un directorio por cada institucion
			if (instituciones!=null && instituciones.size()>0) {
				for (int j=0;j<instituciones.size();j++) {
					CenInstitucionBean insti = (CenInstitucionBean) instituciones.get(j); 
					String path = valorParam(insti.getIdInstitucion().toString(),"CER","PATH_CERTIFICADOS_DIGITALES",null);
					if (path!=null) salida.add(path+ File.separator + insti.getIdInstitucion().toString());
				}
			}

			//plantillas , ademas creamos un directorio por cada institucion
			if (instituciones!=null && instituciones.size()>0) {
				for (int j=0;j<instituciones.size();j++) {
					CenInstitucionBean insti = (CenInstitucionBean) instituciones.get(j); 
					String path = valorParam(insti.getIdInstitucion().toString(),"CER","PATH_PLANTILLAS",null);
					if (path!=null) salida.add(path+ File.separator + insti.getIdInstitucion().toString());
				}
			}

			//informes plantilla , ademas creamos un directorio por cada institucion
			if (instituciones!=null && instituciones.size()>0) {
				for (int j=0;j<instituciones.size();j++) {
					CenInstitucionBean insti = (CenInstitucionBean) instituciones.get(j); 
					String path = valorParam(insti.getIdInstitucion().toString(),"INF","PATH_INFORMES_PLANTILLA",null);
					if (path!=null) salida.add(path + File.separator + insti.getIdInstitucion().toString());
				}
			}

			//informes descarga , ademas creamos un directorio por cada institucion
			if (instituciones!=null && instituciones.size()>0) {
				for (int j=0;j<instituciones.size();j++) {
					CenInstitucionBean insti = (CenInstitucionBean) instituciones.get(j); 
					String path = valorParam(insti.getIdInstitucion().toString(),"INF","PATH_INFORMES_DESCARGA",null);
					if (path!=null) salida.add(path + File.separator + insti.getIdInstitucion().toString());
				}
			}
		
			//desacarga de envio ordinarios
			if (instituciones!=null && instituciones.size()>0) {
				for (int j=0;j<instituciones.size();j++) {
					CenInstitucionBean insti = (CenInstitucionBean) instituciones.get(j); 
					String path = valorParam(insti.getIdInstitucion().toString(),"ENV","PATH_DESCARGA_ENVIOS_ORDINARIOS",null);
					if (path!=null) salida.add(path+ File.separator + insti.getIdInstitucion().toString());
				}
			}

			//descarga de envios fax
			if (instituciones!=null && instituciones.size()>0) {
				for (int j=0;j<instituciones.size();j++) {
					CenInstitucionBean insti = (CenInstitucionBean) instituciones.get(j); 
					String path = valorParam(insti.getIdInstitucion().toString(),"ENV","PATH_DESCARGA_ENVIOS_FAX",null);
					if (path!=null) salida.add(path+ File.separator + insti.getIdInstitucion().toString());
				}
			}

		
			String path = valorParam("0","ENV","PATH_ZSUMBIT",null);
			if (path!=null) salida.add(path);

		}
		
		
		///////////////////////
		/// SIGA PROPERTIES ///
		///////////////////////

		// obtengo los nombres de paths de siga.properties

		String path = ""; 
		ResourceBundle rp=ResourceBundle.getBundle("SIGA");
		
		if (bDirectorios) {
		
			// previsiones
			path = rp.getString("facturacion.directorioFisicoPrevisionesJava");
		    path += File.separator + rp.getString("facturacion.directorioPrevisionesJava");
			if (instituciones!=null && instituciones.size()>0) {
				for (int j=0;j<instituciones.size();j++) {
					CenInstitucionBean insti = (CenInstitucionBean) instituciones.get(j); 
					if (path!=null) salida.add(path + File.separator + insti.getIdInstitucion().toString());
				}
			}
			
			// CAJG
			path = rp.getString("cajg.directorioFisicoCAJG");
		    path += File.separator + rp.getString("cajg.directorioCAJGJava");
			if (instituciones!=null && instituciones.size()>0) {
				for (int j=0;j<instituciones.size();j++) {
					CenInstitucionBean insti = (CenInstitucionBean) instituciones.get(j); 
					if (path!=null) salida.add(path + File.separator + insti.getIdInstitucion().toString());
				}
			}

			// CAJG
			path = rp.getString("cajg.directorioPlantillaCAJG");
		    path += File.separator + rp.getString("cajg.directorioCAJGJava");
			if (instituciones!=null && instituciones.size()>0) {
				for (int j=0;j<instituciones.size();j++) {
					CenInstitucionBean insti = (CenInstitucionBean) instituciones.get(j); 
					if (path!=null) salida.add(path + File.separator + insti.getIdInstitucion().toString());
				}
			}
			
			// previsiones Oracle
		    path = rp.getString("facturacion.directorioPrevisionesOracle");
			if (instituciones!=null && instituciones.size()>0) {
				for (int j=0;j<instituciones.size();j++) {
					CenInstitucionBean insti = (CenInstitucionBean) instituciones.get(j); 
					if (path!=null) salida.add(path + File.separator + insti.getIdInstitucion().toString());
				}
			}

			// pagos Banco
		    path = rp.getString("facturacion.directorioFisicoPagosBancosJava");
		    path += File.separator + rp.getString("facturacion.directorioPagosBancosJava");
			if (instituciones!=null && instituciones.size()>0) {
				for (int j=0;j<instituciones.size();j++) {
					CenInstitucionBean insti = (CenInstitucionBean) instituciones.get(j); 
					if (path!=null) salida.add(path + File.separator + insti.getIdInstitucion().toString());
				}
			}
	
			// 
		    path = rp.getString("facturacion.directorioBancosOracle");
			if (instituciones!=null && instituciones.size()>0) {
				for (int j=0;j<instituciones.size();j++) {
					CenInstitucionBean insti = (CenInstitucionBean) instituciones.get(j); 
					if (path!=null) salida.add(path + File.separator + insti.getIdInstitucion().toString());
				}
			}

			path = rp.getString("facturacion.directorioFisicoDevolucionesJava");
			path += File.separator + rp.getString("facturacion.directorioDevolucionesJava");
			if (instituciones!=null && instituciones.size()>0) {
				for (int j=0;j<instituciones.size();j++) {
					CenInstitucionBean insti = (CenInstitucionBean) instituciones.get(j); 
					if (path!=null) salida.add(path + File.separator + insti.getIdInstitucion().toString());
				}
			}

			//
			path = rp.getString("facturacion.directorioDevolucionesOracle");
			if (instituciones!=null && instituciones.size()>0) {
				for (int j=0;j<instituciones.size();j++) {
					CenInstitucionBean insti = (CenInstitucionBean) instituciones.get(j); 
					if (path!=null) salida.add(path + File.separator + insti.getIdInstitucion().toString());
				}
			}
		
			//
			path = rp.getString("facturacion.directorioFisicoAbonosBancosJava");
			path += File.separator + rp.getString("facturacion.directorioAbonosBancosJava");
			if (instituciones!=null && instituciones.size()>0) {
				for (int j=0;j<instituciones.size();j++) {
					CenInstitucionBean insti = (CenInstitucionBean) instituciones.get(j); 
					if (path!=null) salida.add(path + File.separator + insti.getIdInstitucion().toString());
				}
			}

			//
			path = rp.getString("contabilidad.directorioFisicoContabilidad");
			if (instituciones!=null && instituciones.size()>0) {
				for (int j=0;j<instituciones.size();j++) {
					CenInstitucionBean insti = (CenInstitucionBean) instituciones.get(j); 
					if (path!=null) salida.add(path + File.separator + insti.getIdInstitucion().toString());
				}
			}

			// temporal facturas
			path = rp.getString("facturacion.directorioFisicoTemporalFacturasJava");
			path += File.separator + rp.getString("facturacion.directorioTemporalFacturasJava");
			if (instituciones!=null && instituciones.size()>0) {
				for (int j=0;j<instituciones.size();j++) {
					CenInstitucionBean insti = (CenInstitucionBean) instituciones.get(j); 
					if (path!=null) salida.add(path + File.separator + insti.getIdInstitucion().toString());
				}
			}

			// plantilla factura
			path = rp.getString("facturacion.directorioFisicoPlantillaFacturaJava");
			path += File.separator + rp.getString("facturacion.directorioPlantillaFacturaJava");
			if (instituciones!=null && instituciones.size()>0) {
				for (int j=0;j<instituciones.size();j++) {
					CenInstitucionBean insti = (CenInstitucionBean) instituciones.get(j); 
					if (path!=null) salida.add(path + File.separator + insti.getIdInstitucion().toString());
				}
			}

			String pathMor = "";
			pathMor = rp.getString("facturacion.directorioFisicoPlantillaMorososJava");
		
			// plantilla morosos
			path = pathMor + File.separator + rp.getString("facturacion.directorioPlantillaListaMorososJava");
			if (instituciones!=null && instituciones.size()>0) {
				for (int j=0;j<instituciones.size();j++) {
					CenInstitucionBean insti = (CenInstitucionBean) instituciones.get(j); 
					if (path!=null) salida.add(path + File.separator + insti.getIdInstitucion().toString());
				}
			}

			path = pathMor + File.separator + rp.getString("facturacion.directorioPlantillaComunicacionMorososJava");
			if (instituciones!=null && instituciones.size()>0) {
				for (int j=0;j<instituciones.size();j++) {
					CenInstitucionBean insti = (CenInstitucionBean) instituciones.get(j); 
					if (path!=null) salida.add(path + File.separator + insti.getIdInstitucion().toString());
				}
			}

			// PDF
			path = rp.getString("facturacion.directorioFisicoFacturaPDFJava");
			path += File.separator + rp.getString("facturacion.directorioFacturaPDFJava");
			if (instituciones!=null && instituciones.size()>0) {
				for (int j=0;j<instituciones.size();j++) {
					CenInstitucionBean insti = (CenInstitucionBean) instituciones.get(j); 
					if (path!=null) salida.add(path + File.separator + insti.getIdInstitucion().toString());
				}
			}
	
			// Solicitud asistencia
			path = rp.getString("sjcs.directorioFisicoSolicitudAsistenciaJava");
			path += File.separator + rp.getString("sjcs.directorioSolicitudAsistenciaJava");
			if (instituciones!=null && instituciones.size()>0) {
				for (int j=0;j<instituciones.size();j++) {
					CenInstitucionBean insti = (CenInstitucionBean) instituciones.get(j); 
					if (path!=null) salida.add(path + File.separator + insti.getIdInstitucion().toString());
				}
			}

			// carta asistencia
			path = rp.getString("sjcs.directorioFisicoCartaAsistenciaJava");
			path += File.separator + rp.getString("sjcs.directorioCartaAsistenciaJava");
			if (instituciones!=null && instituciones.size()>0) {
				for (int j=0;j<instituciones.size();j++) {
					CenInstitucionBean insti = (CenInstitucionBean) instituciones.get(j); 
					if (path!=null) salida.add(path + File.separator + insti.getIdInstitucion().toString());
				}
			}

			// carta EJG
			path = rp.getString("sjcs.directorioFisicoCartaEJGJava");
			path += File.separator + rp.getString("sjcs.directorioCartaEJGJava");
			if (instituciones!=null && instituciones.size()>0) {
				for (int j=0;j<instituciones.size();j++) {
					CenInstitucionBean insti = (CenInstitucionBean) instituciones.get(j); 
					if (path!=null) salida.add(path + File.separator + insti.getIdInstitucion().toString());
				}
			}

			// carta Oficio
			path = rp.getString("sjcs.directorioFisicoCartaOficioJava");
			path += File.separator + rp.getString("sjcs.directorioCartaOficioJava");
			if (instituciones!=null && instituciones.size()>0) {
				for (int j=0;j<instituciones.size();j++) {
					CenInstitucionBean insti = (CenInstitucionBean) instituciones.get(j); 
					if (path!=null) salida.add(path + File.separator + insti.getIdInstitucion().toString());
				}
			}

			// lista guardias
			path = rp.getString("sjcs.directorioFisicoListaGuardiasJava");
			path += File.separator + rp.getString("sjcs.directorioListaGuardiasJava");
			if (instituciones!=null && instituciones.size()>0) {
				for (int j=0;j<instituciones.size();j++) {
					CenInstitucionBean insti = (CenInstitucionBean) instituciones.get(j); 
					if (path!=null) salida.add(path + File.separator + insti.getIdInstitucion().toString());
				}
			}

			// Informes genéricos
			path = rp.getString("informes.directorioFisicoSalidaInformesJava");
			path += File.separator + rp.getString("informes.directorioPlantillaInformesJava");
			if (instituciones!=null && instituciones.size()>0) {
				for (int j=0;j<instituciones.size();j++) {
					CenInstitucionBean insti = (CenInstitucionBean) instituciones.get(j); 
					if (path!=null) salida.add(path + File.separator + insti.getIdInstitucion().toString());
				}
			}

			// SJCS
			path = rp.getString("sjcs.directorioFisicoSJCSJava");
			path += File.separator + rp.getString("sjcs.directorioSJCSJava");
			if (instituciones!=null && instituciones.size()>0) {
				for (int j=0;j<instituciones.size();j++) {
					CenInstitucionBean insti = (CenInstitucionBean) instituciones.get(j); 
					if (path!=null) salida.add(path + File.separator + insti.getIdInstitucion().toString());
				}
			}
			
			//
			path = rp.getString("facturacion.directorioFisicoComunicacionesPDFJava");
			path += File.separator + rp.getString("facturacion.directorioComunicacionesPDFJava");
			if (instituciones!=null && instituciones.size()>0) {
				for (int j=0;j<instituciones.size();j++) {
					CenInstitucionBean insti = (CenInstitucionBean) instituciones.get(j); 
					if (path!=null) salida.add(path + File.separator + insti.getIdInstitucion().toString());
				}
			}

			// Temporal SJCS
			path = rp.getString("sjcs.directorioFisicoTemporalSJCSJava");
			path +=  File.separator + rp.getString("sjcs.directorioTemporalSJCSJava");
			if (instituciones!=null && instituciones.size()>0) {
				for (int j=0;j<instituciones.size();j++) {
					CenInstitucionBean insti = (CenInstitucionBean) instituciones.get(j); 
					if (path!=null) salida.add(path + File.separator + insti.getIdInstitucion().toString());
				}
			}

			// LOG Facturas 
			path = rp.getString("facturacion.directorioFisicoLogProgramacion");
			if (instituciones!=null && instituciones.size()>0) {
				for (int j=0;j<instituciones.size();j++) {
					CenInstitucionBean insti = (CenInstitucionBean) instituciones.get(j); 
					if (path!=null) salida.add(path + File.separator + insti.getIdInstitucion().toString());
				}
			}
			
			// FACTURAS EMITIDAS 
			path = rp.getString("facturacion.directorioFisicoSalidaListadoFacturaEmitidasJava");
			path += File.separator + rp.getString("facturacion.directorioPlantillaListadoFacturaEmitidaJava");
			if (instituciones!=null && instituciones.size()>0) {
				for (int j=0;j<instituciones.size();j++) {
					CenInstitucionBean insti = (CenInstitucionBean) instituciones.get(j); 
					if (path!=null) salida.add(path + File.separator + insti.getIdInstitucion().toString());
				}
			}
			
			path = rp.getString("facturacion.directorioFisicoPlantillaListadoFacturaEmitidasJava");
			path += File.separator + rp.getString("facturacion.directorioPlantillaListadoFacturaEmitidaJava");
			if (instituciones!=null && instituciones.size()>0) {
				for (int j=0;j<instituciones.size();j++) {
					CenInstitucionBean insti = (CenInstitucionBean) instituciones.get(j); 
					if (path!=null) salida.add(path + File.separator + insti.getIdInstitucion().toString());
				}
			}
			

		}
/*
		if (bWeblogic) {
		
			// ESTE SOLO SE CREA UNA CARPETA (NO INSTITUCION)
	
			// envios.correoordinario.comandoshell
			path = rp.getString("envios.correoordinario.comandoshell");
			if (path!=null) {
				// hay que ver si tiene nombre de fichero al final
				String nombreFichero = path.substring(path.lastIndexOf(File.separator)+1,path.length());
				if (nombreFichero!=null && nombreFichero.indexOf(".")>0) {
					// tiene punto: Se quita el nombre de fichero
					path = path.substring(0,path.indexOf(nombreFichero));
				}
				salida.add(path);
			}
	
		}
		if (bWeblogic) {
		
			// envios.fax.comandoshell
			path = rp.getString("envios.fax.comandoshell");
			if (path!=null) {
				// hay que ver si tiene nombre de fichero al final
				String nombreFichero = path.substring(path.lastIndexOf(File.separator)+1,path.length());
				if (nombreFichero!=null && nombreFichero.indexOf(".")>0) {
					// tiene punto: Se quita el nombre de fichero
					path = path.substring(0,path.indexOf(nombreFichero));
				}
				salida.add(path);
			}
		
		}
*/
		
		return salida;
    
	  } finally 
 	  {
    	try {
        	rs.close();
       	} catch (Exception e) {}
    	try {
        	st.close();
       	} catch (Exception e) {}
      }
	}
	
	private static void crearDirectorios(Vector directorios) {
		if (directorios==null) {
			System.out.println("No hay directorios a crear.");
		} else {
			System.out.println("Creando directorios...");
			for (int i=0;i<directorios.size();i++) {
				String path = (String) directorios.get(i);
				File camino = new File(path);
				if (camino!=null) {
					System.out.print("... "+camino.getAbsolutePath());
					try {
						if (camino.mkdirs()) {
							System.out.println(" ...CREADO.");
						} else {
							System.out.println(" ...Ya existe el path.");
						}
					} catch (SecurityException ex) {
						System.out.println(" ...No se ha creado por problemas de seguridad.");
					}
				}
			}
		}
	}
	
	private static ResultSet consulta (String query) throws Exception {
		st=null;
//        st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        st = con.createStatement();
        rs = st.executeQuery(query);
		return rs;
        
	}

	private static void conecta () throws Exception {
        con = null;
//        Class.forName("oracle.jdbc.driver.OracleDriver");
//        con = DriverManager.getConnection("jdbc:oracle:thin:@172.24.10.199:1521:SIGA", "uscgae", "uscgae");
        Class.forName(claseConexion);
        con = DriverManager.getConnection(cadenaConexion, usuario, clave);
	}

	private static String valorParam (String institucion,String modulo,String nombre,String valorDefecto) throws Exception 
	{
      try{
        String salida = valorDefecto;
		String query = "SELECT " + GenParametrosBean.C_VALOR + " FROM " + GenParametrosBean.T_NOMBRETABLA + " WHERE " + GenParametrosBean.C_MODULO + "='" +modulo+"' AND " + GenParametrosBean.C_PARAMETRO + "='" + nombre  +"' AND " +GenParametrosBean.C_IDINSTITUCION +"=" + institucion; 
		rs = consulta(query);
		if (rs.next()) {
			salida = rs.getString(GenParametrosBean.C_VALOR);
		} else {
	      	try {
	          	rs.close();
	        } catch (Exception e) {}
	    	try {
	        	st.close();
	       	} catch (Exception e) {}
			// si no existe para mi institucion lo busco para la 0
			query = "SELECT " + GenParametrosBean.C_VALOR + " FROM " + GenParametrosBean.T_NOMBRETABLA + " WHERE " + GenParametrosBean.C_MODULO + "='" +modulo+"' AND " + GenParametrosBean.C_PARAMETRO + "='" + nombre  +"' AND " +GenParametrosBean.C_IDINSTITUCION +"=0"; 
			rs = null;
			rs = consulta(query);
			if (rs.next()) {
				salida = rs.getString(GenParametrosBean.C_VALOR);
			}			
		}
		return salida;
      
      } finally 
	  {
      	try {
      		rs.close();
      	} catch (Exception e) {}
    	try {
        	st.close();
       	} catch (Exception e) {}
      	
      }
	}

	private static Vector obtenerTodasInstituciones () throws Exception 
	{
	  try{
		
      	Vector salida = new Vector();
		String query = "SELECT " + CenInstitucionBean.C_IDINSTITUCION + " FROM " + CenInstitucionBean.T_NOMBRETABLA + " WHERE " + CenInstitucionBean.C_IDINSTITUCION + " <> 0"; 
		rs = consulta(query);
		while (rs.next()) {
	 		String insti = rs.getString(CenInstitucionBean.C_IDINSTITUCION);
	 		CenInstitucionBean instiBean = new CenInstitucionBean();
			instiBean.setIdInstitucion(new Integer(insti));
			salida.add(instiBean);
		}
		return salida;
      
      } finally 
	  {
      	try {
      	rs.close();
      	} catch (Exception e) {}
    	try {
        	st.close();
       	} catch (Exception e) {}
      }
	}

	private static Vector obtenerInstitucionesAlta () throws Exception 
	{
	  try{
		
      	Vector salida = new Vector();
		String query = "SELECT DISTINCT " + CenInstitucionBean.C_IDINSTITUCION + " FROM " + GenParametrosBean.T_NOMBRETABLA + " WHERE " + GenParametrosBean.C_IDINSTITUCION + " <> 0"; 
		rs = consulta(query);
		while (rs.next()) {
	 		String insti = rs.getString(CenInstitucionBean.C_IDINSTITUCION);
	 		CenInstitucionBean instiBean = new CenInstitucionBean();
			instiBean.setIdInstitucion(new Integer(insti));
			salida.add(instiBean);
		}
		return salida;
      
      } finally 
	  {
      	try {
      	rs.close();
      	} catch (Exception e) {}
    	try {
        	st.close();
       	} catch (Exception e) {}
      }
	}

	private static void copiaRecursivo (String pathOrigen, Vector institucionesAux) throws Exception {


		ResourceBundle rp=ResourceBundle.getBundle("SIGA");
		File origen = new File(pathOrigen);
		File hijoOrigen = null;
		File hijoDestino = null;
		
		System.out.println("copiando plantillas...");
	
		if (origen.exists()) {
			if (origen.isDirectory()) {

				for (int i=0;i<institucionesAux.size();i++) {

					// busco el destino por institucion
					// repitiré el proceso por cada una.
					CenInstitucionBean insti = (CenInstitucionBean) institucionesAux.get(i);
					System.out.println("... Institucion " + insti.getIdInstitucion().toString());
					
					// plantilla factura
					String pathDestino = rp.getString("facturacion.directorioFisicoPlantillaFacturaJava");
					pathDestino += File.separator + rp.getString("facturacion.directorioPlantillaFacturaJava");
					File destino = new File(pathDestino);
					destino.mkdirs();
					String carpeta = rp.getString("directorios.carpeta.facturas");
					hijoOrigen = new File(origen.getAbsolutePath() + File.separator + carpeta);   
				 	hijoDestino = new File(destino.getAbsolutePath());
				 	hijoDestino.mkdirs();
				 	if (hijoOrigen.exists()) {
				 		recur(hijoOrigen, hijoDestino,insti.getIdInstitucion().toString(),institucionDefecto);
				 	}
					
				 	//carpeta.lista.morosos
				 	pathDestino = rp.getString("facturacion.directorioFisicoPlantillaMorososJava");
					pathDestino += File.separator + rp.getString("facturacion.directorioPlantillaListaMorososJava");
					destino = new File(pathDestino);
					destino.mkdirs();
				 	carpeta = rp.getString("directorios.carpeta.lista.morosos");
					hijoOrigen = new File(origen.getAbsolutePath() + File.separator + carpeta);   
				 	hijoDestino = new File(destino.getAbsolutePath());
				 	hijoDestino.mkdirs();
				 	if (hijoOrigen.exists()) {
				 		recur(hijoOrigen, hijoDestino,insti.getIdInstitucion().toString(),institucionDefecto);
				 	}

					
				 	//carpeta.solicitud.asistencia
				 	pathDestino = rp.getString("sjcs.directorioFisicoSolicitudAsistenciaJava");
					pathDestino += File.separator + rp.getString("sjcs.directorioSolicitudAsistenciaJava");
					destino = new File(pathDestino);
					destino.mkdirs();
				 	carpeta = rp.getString("directorios.carpeta.solicitud.asistencia");
					hijoOrigen = new File(origen.getAbsolutePath() + File.separator + carpeta);   
				 	hijoDestino = new File(destino.getAbsolutePath());
				 	hijoDestino.mkdirs();
				 	if (hijoOrigen.exists()) {
				 		recur(hijoOrigen, hijoDestino,insti.getIdInstitucion().toString(),institucionDefecto);
				 	}

				 	// carpeta.carta.asistencia
				 	pathDestino = rp.getString("sjcs.directorioFisicoCartaAsistenciaJava");
					pathDestino += File.separator + rp.getString("sjcs.directorioCartaAsistenciaJava");
					destino = new File(pathDestino);
					destino.mkdirs();
					carpeta = rp.getString("directorios.carpeta.carta.asistencia");
					hijoOrigen = new File(origen.getAbsolutePath() + File.separator + carpeta);   
				 	hijoDestino = new File(destino.getAbsolutePath());
				 	hijoDestino.mkdirs();
				 	if (hijoOrigen.exists()) {
				 		recur(hijoOrigen, hijoDestino,insti.getIdInstitucion().toString(),institucionDefecto);
				 	}
					
				 	// carpeta.carta.expediente
				 	pathDestino = rp.getString("sjcs.directorioFisicoCartaEJGJava");
					pathDestino += File.separator + rp.getString("sjcs.directorioCartaEJGJava");
					destino = new File(pathDestino);
					destino.mkdirs();
					carpeta = rp.getString("directorios.carpeta.carta.expediente");
					hijoOrigen = new File(origen.getAbsolutePath() + File.separator + carpeta);   
				 	hijoDestino = new File(destino.getAbsolutePath());
				 	hijoDestino.mkdirs();
				 	if (hijoOrigen.exists()) {
				 		recur(hijoOrigen, hijoDestino,insti.getIdInstitucion().toString(),institucionDefecto);
				 	}
					
				 	//carpeta.carta.oficio
				 	pathDestino = rp.getString("sjcs.directorioFisicoCartaOficioJava");
					pathDestino += File.separator + rp.getString("sjcs.directorioCartaOficioJava");
					destino = new File(pathDestino);
					destino.mkdirs();
				 	carpeta = rp.getString("directorios.carpeta.carta.oficio");
					hijoOrigen = new File(origen.getAbsolutePath() + File.separator + carpeta);   
				 	hijoDestino = new File(destino.getAbsolutePath());
				 	hijoDestino.mkdirs();
				 	if (hijoOrigen.exists()) {
				 		recur(hijoOrigen, hijoDestino,insti.getIdInstitucion().toString(),institucionDefecto);
				 	}
				 	
				 	//cajg.directorioCAJGJava
				 	pathDestino = rp.getString("cajg.directorioPlantillaCAJG");
					pathDestino += File.separator + rp.getString("cajg.directorioCAJGJava");
					destino = new File(pathDestino);
					destino.mkdirs();
				 	carpeta = rp.getString("cajg.directorioCAJGJava");
					hijoOrigen = new File(origen.getAbsolutePath() + File.separator + carpeta);   
				 	hijoDestino = new File(destino.getAbsolutePath());
				 	hijoDestino.mkdirs();
				 	if (hijoOrigen.exists()) {
				 		recur(hijoOrigen, hijoDestino,insti.getIdInstitucion().toString(),institucionDefecto);
				 	}
					
				 	//carpeta.lista.guardias
				 	pathDestino = rp.getString("sjcs.directorioFisicoListaGuardiasJava");
					pathDestino += File.separator + rp.getString("sjcs.directorioListaGuardiasJava");
					destino = new File(pathDestino);
					destino.mkdirs();
				 	carpeta = rp.getString("directorios.carpeta.lista.guardias");
					hijoOrigen = new File(origen.getAbsolutePath() + File.separator + carpeta);   
				 	hijoDestino = new File(destino.getAbsolutePath());
				 	hijoDestino.mkdirs();
				 	if (hijoOrigen.exists()) {
				 		recur(hijoOrigen, hijoDestino,insti.getIdInstitucion().toString(),institucionDefecto);
				 	}
					
				 	//carpeta.comunicacion.morosos
				 	pathDestino = rp.getString("facturacion.directorioFisicoPlantillaMorososJava");
					pathDestino += File.separator + rp.getString("facturacion.directorioPlantillaComunicacionMorososJava");
					destino = new File(pathDestino);
					destino.mkdirs();
				 	carpeta = rp.getString("directorios.carpeta.comunicacion.morosos");
					hijoOrigen = new File(origen.getAbsolutePath() + File.separator + carpeta);   
				 	hijoDestino = new File(destino.getAbsolutePath());
				 	hijoDestino.mkdirs();
				 	if (hijoOrigen.exists()) {
				 		recur(hijoOrigen, hijoDestino,insti.getIdInstitucion().toString(),institucionDefecto);
				 	}
				 	
				 	//carpeta.facturas.emitidas
				 	pathDestino = rp.getString("facturacion.directorioFisicoPlantillaListadoFacturaEmitidasJava");
					pathDestino += File.separator + rp.getString("facturacion.directorioPlantillaListadoFacturaEmitidaJava");
					destino = new File(pathDestino);
					destino.mkdirs();
				 	carpeta = rp.getString("directorios.carpeta.facturas.emitidas");
					hijoOrigen = new File(origen.getAbsolutePath() + File.separator + carpeta);   
				 	hijoDestino = new File(destino.getAbsolutePath());
				 	hijoDestino.mkdirs();
				 	if (hijoOrigen.exists()) {
				 		recur(hijoOrigen, hijoDestino,insti.getIdInstitucion().toString(),institucionDefecto);
				 	}

				 	//carpeta.informes.genericos
				 	pathDestino = rp.getString("informes.directorioFisicoPlantillaInformesJava");
					pathDestino += File.separator + rp.getString("informes.directorioPlantillaInformesJava");
					destino = new File(pathDestino);
					destino.mkdirs();
				 	carpeta = rp.getString("directorios.carpeta.informes.genericos");
					hijoOrigen = new File(origen.getAbsolutePath() + File.separator + carpeta);   
				 	hijoDestino = new File(destino.getAbsolutePath());
				 	hijoDestino.mkdirs();
				 	if (hijoOrigen.exists()) {
				 		recur(hijoOrigen, hijoDestino,insti.getIdInstitucion().toString(),institucionDefecto);
				 	}
				 	
				 	//carpeta.CAJG
				 	pathDestino = rp.getString("cajg.directorioPlantillaCAJG");
				 	pathDestino += File.separator + rp.getString("cajg.directorioCAJGJava");
					destino = new File(pathDestino);
					destino.mkdirs();
					carpeta = rp.getString("directorios.carpeta.cajg");
					hijoOrigen = new File(origen.getAbsolutePath() + File.separator + carpeta);   
				 	hijoDestino = new File(destino.getAbsolutePath());
				 	hijoDestino.mkdirs();
				 	if (hijoOrigen.exists()) {
				 		recur(hijoOrigen, hijoDestino,insti.getIdInstitucion().toString(),institucionDefecto);
				 	}

				 	// A PARTIR DE AQUI HAY QUE BUSCAR CADA PATH POR INSTITUCION Y ADEMAS AÑADIRLE LA INSTITUCION

//				 	for (int k=0;k<institucionesAux.size();k++) {

//				 		CenInstitucionBean instiBean = (CenInstitucionBean)institucionesAux.get(k);
//						String instit = instiBean.getIdInstitucion().toString();

				 	String instit = insti.getIdInstitucion().toString();
/*							
				 		//carpeta.certificados.digitales
					 	pathDestino = valorParam(instit,"CER","PATH_CERTIFICADOS_DIGITALES","");
						destino = new File(pathDestino);
						destino.mkdirs();
					 	carpeta = rp.getString("directorios.carpeta.certificados.firma.digital");
						hijoOrigen = new File(origen.getAbsolutePath() + File.separator + carpeta);   
					 	hijoDestino = new File(destino.getAbsolutePath());
					 	hijoDestino.mkdirs();
					 	if (hijoOrigen.exists()) {
					 		recur(hijoOrigen, hijoDestino,instit,institucionDefecto);
					 	}
*/	
					 	//carpeta.etiquetas
					 	pathDestino = valorParam(instit,"CER","PATH_PLANTILLAS","");
						destino = new File(pathDestino);
						destino.mkdirs();
					 	carpeta = rp.getString("directorios.carpeta.envios.etiquetas");
						hijoOrigen = new File(origen.getAbsolutePath() + File.separator + carpeta);   
					 	hijoDestino = new File(destino.getAbsolutePath());
					 	hijoDestino.mkdirs();
					 	if (hijoOrigen.exists()) {
					 		recur(hijoOrigen, hijoDestino,instit,institucionDefecto);
					 	}
						
					 	//carpeta.informes.sjcs
					 	pathDestino = valorParam(instit,"INF","PATH_INFORMES_PLANTILLA","");
						destino = new File(pathDestino);
						destino.mkdirs();
					 	carpeta = rp.getString("directorios.carpeta.informes.sjcs");
						hijoOrigen = new File(origen.getAbsolutePath() + File.separator + carpeta);   
					 	hijoDestino = new File(destino.getAbsolutePath());
					 	hijoDestino.mkdirs();
					 	if (hijoOrigen.exists()) {
					 		recur(hijoOrigen, hijoDestino,instit,institucionDefecto);
					 	}
//				 	}					
	
				}
		
			} else {
				throw new Exception("El path origen no es un directorio.");
			}
		} else {
			throw new Exception("No existe el path origen de plantillas.");
		}
		
	}

	private static void copiaRecursivoModificadas (String pathOrigen, String idinstitucion) throws Exception {


		ResourceBundle rp=ResourceBundle.getBundle("SIGA");
		File origen = new File(pathOrigen);
		File hijoOrigen = null;
		File hijoDestino = null;
		
		System.out.println("copiando plantillas...");
	
		if (origen.exists()) {
			if (origen.isDirectory()) {

				// busco el destino por institucion
				// repitiré el proceso por cada una.
				System.out.println("... Institucion " + idinstitucion);
				
				// plantilla factura
				String pathDestino = rp.getString("facturacion.directorioFisicoPlantillaFacturaJava");
				pathDestino += File.separator + rp.getString("facturacion.directorioPlantillaFacturaJava");
				File destino = new File(pathDestino);
				destino.mkdirs();
				String carpeta = rp.getString("directorios.carpeta.facturas");
				hijoOrigen = new File(origen.getAbsolutePath() + File.separator + carpeta);   
			 	hijoDestino = new File(destino.getAbsolutePath());
			 	hijoDestino.mkdirs();
			 	if (hijoOrigen.exists()) {
			 		recur(hijoOrigen, hijoDestino,idinstitucion,idinstitucion);
			 	}
				
			 	//carpeta.lista.morosos
			 	pathDestino = rp.getString("facturacion.directorioFisicoPlantillaMorososJava");
				pathDestino += File.separator + rp.getString("facturacion.directorioPlantillaListaMorososJava");
				destino = new File(pathDestino);
				destino.mkdirs();
			 	carpeta = rp.getString("directorios.carpeta.lista.morosos");
				hijoOrigen = new File(origen.getAbsolutePath() + File.separator + carpeta);   
			 	hijoDestino = new File(destino.getAbsolutePath());
			 	hijoDestino.mkdirs();
			 	if (hijoOrigen.exists()) {
			 		recur(hijoOrigen, hijoDestino,idinstitucion,idinstitucion);
			 	}
				
			 	//carpeta.comunicacion.morosos
			 	pathDestino = rp.getString("facturacion.directorioFisicoPlantillaMorososJava");
				pathDestino += File.separator + rp.getString("facturacion.directorioPlantillaComunicacionMorososJava");
				destino = new File(pathDestino);
				destino.mkdirs();
			 	carpeta = rp.getString("directorios.carpeta.comunicacion.morosos");
				hijoOrigen = new File(origen.getAbsolutePath() + File.separator + carpeta);   
			 	hijoDestino = new File(destino.getAbsolutePath());
			 	hijoDestino.mkdirs();
			 	if (hijoOrigen.exists()) {
			 		recur(hijoOrigen, hijoDestino,idinstitucion,idinstitucion);
			 	}
				
			 	//carpeta.solicitud.asistencia
			 	pathDestino = rp.getString("sjcs.directorioFisicoSolicitudAsistenciaJava");
				pathDestino += File.separator + rp.getString("sjcs.directorioSolicitudAsistenciaJava");
				destino = new File(pathDestino);
				destino.mkdirs();
			 	carpeta = rp.getString("directorios.carpeta.solicitud.asistencia");
				hijoOrigen = new File(origen.getAbsolutePath() + File.separator + carpeta);   
			 	hijoDestino = new File(destino.getAbsolutePath());
			 	hijoDestino.mkdirs();
			 	if (hijoOrigen.exists()) {
			 		recur(hijoOrigen, hijoDestino,idinstitucion,idinstitucion);
			 	}

			 	// carpeta.carta.asistencia
			 	pathDestino = rp.getString("sjcs.directorioFisicoCartaAsistenciaJava");
				pathDestino += File.separator + rp.getString("sjcs.directorioCartaAsistenciaJava");
				destino = new File(pathDestino);
				destino.mkdirs();
				carpeta = rp.getString("directorios.carpeta.carta.asistencia");
				hijoOrigen = new File(origen.getAbsolutePath() + File.separator + carpeta);   
			 	hijoDestino = new File(destino.getAbsolutePath());
			 	hijoDestino.mkdirs();
			 	if (hijoOrigen.exists()) {
			 		recur(hijoOrigen, hijoDestino,idinstitucion,idinstitucion);
			 	}
				
			 	// carpeta.carta.expediente
			 	pathDestino = rp.getString("sjcs.directorioFisicoCartaEJGJava");
				pathDestino += File.separator + rp.getString("sjcs.directorioCartaEJGJava");
				destino = new File(pathDestino);
				destino.mkdirs();
				carpeta = rp.getString("directorios.carpeta.carta.expediente");
				hijoOrigen = new File(origen.getAbsolutePath() + File.separator + carpeta);   
			 	hijoDestino = new File(destino.getAbsolutePath());
			 	hijoDestino.mkdirs();
			 	if (hijoOrigen.exists()) {
			 		recur(hijoOrigen, hijoDestino,idinstitucion,idinstitucion);
			 	}
			 	
			 	// cajg.directorioPlantillaCAJG
			 	pathDestino = rp.getString("cajg.directorioPlantillaCAJG");
				pathDestino += File.separator + rp.getString("cajg.directorioCAJGJava");
				destino = new File(pathDestino);
				destino.mkdirs();
				carpeta = rp.getString("cajg.directorioCAJGJava");
				hijoOrigen = new File(origen.getAbsolutePath() + File.separator + carpeta);   
			 	hijoDestino = new File(destino.getAbsolutePath());
			 	hijoDestino.mkdirs();
			 	if (hijoOrigen.exists()) {
			 		recur(hijoOrigen, hijoDestino,idinstitucion,idinstitucion);
			 	}
				
			 	//carpeta.carta.oficio
			 	pathDestino = rp.getString("sjcs.directorioFisicoCartaOficioJava");
				pathDestino += File.separator + rp.getString("sjcs.directorioCartaOficioJava");
				destino = new File(pathDestino);
				destino.mkdirs();
			 	carpeta = rp.getString("directorios.carpeta.carta.oficio");
				hijoOrigen = new File(origen.getAbsolutePath() + File.separator + carpeta);   
			 	hijoDestino = new File(destino.getAbsolutePath());
			 	hijoDestino.mkdirs();
			 	if (hijoOrigen.exists()) {
			 		recur(hijoOrigen, hijoDestino,idinstitucion,idinstitucion);
			 	}
				
			 	//carpeta.lista.guardias
			 	pathDestino = rp.getString("sjcs.directorioFisicoListaGuardiasJava");
				pathDestino += File.separator + rp.getString("sjcs.directorioListaGuardiasJava");
				destino = new File(pathDestino);
				destino.mkdirs();
			 	carpeta = rp.getString("directorios.carpeta.lista.guardias");
				hijoOrigen = new File(origen.getAbsolutePath() + File.separator + carpeta);   
			 	hijoDestino = new File(destino.getAbsolutePath());
			 	hijoDestino.mkdirs();
			 	if (hijoOrigen.exists()) {
			 		recur(hijoOrigen, hijoDestino,idinstitucion,idinstitucion);
			 	}

			 	//carpeta.facturas.emitidas
			 	pathDestino = rp.getString("facturacion.directorioFisicoPlantillaListadoFacturaEmitidasJava");
				pathDestino += File.separator + rp.getString("facturacion.directorioPlantillaListadoFacturaEmitidaJava");
				destino = new File(pathDestino);
				destino.mkdirs();
			 	carpeta = rp.getString("directorios.carpeta.facturas.emitidas");
				hijoOrigen = new File(origen.getAbsolutePath() + File.separator + carpeta);   
			 	hijoDestino = new File(destino.getAbsolutePath());
			 	hijoDestino.mkdirs();
			 	if (hijoOrigen.exists()) {
			 		recur(hijoOrigen, hijoDestino,idinstitucion,idinstitucion);
			 	}

			 	//carpeta.informes.genericos
			 	pathDestino = rp.getString("informes.directorioFisicoPlantillaInformesJava");
				pathDestino += File.separator + rp.getString("informes.directorioPlantillaInformesJava");
				destino = new File(pathDestino);
				destino.mkdirs();
			 	carpeta = rp.getString("directorios.carpeta.informes.genericos");
				hijoOrigen = new File(origen.getAbsolutePath() + File.separator + carpeta);   
			 	hijoDestino = new File(destino.getAbsolutePath());
			 	hijoDestino.mkdirs();
			 	if (hijoOrigen.exists()) {
			 		recur(hijoOrigen, hijoDestino,idinstitucion,idinstitucion);
			 	}
			 	
			 	//carpeta.CAJG
			 	pathDestino = rp.getString("cajg.directorioPlantillaCAJG");
			 	pathDestino += File.separator + rp.getString("cajg.directorioCAJGJava");
				destino = new File(pathDestino);
				destino.mkdirs();
				carpeta = rp.getString("directorios.carpeta.cajg");
				hijoOrigen = new File(origen.getAbsolutePath() + File.separator + carpeta);   
			 	hijoDestino = new File(destino.getAbsolutePath());
			 	hijoDestino.mkdirs();
			 	if (hijoOrigen.exists()) {
			 		recur(hijoOrigen, hijoDestino,idinstitucion,idinstitucion);
			 	}
			 	
			 	// A PARTIR DE AQUI HAY QUE BUSCAR CADA PATH POR INSTITUCION Y ADEMAS AÑADIRLE LA INSTITUCION

				String instit = idinstitucion;
/*
		 		//carpeta.certificados.digitales
			 	pathDestino = valorParam(instit,"CER","PATH_CERTIFICADOS_DIGITALES","");
				destino = new File(pathDestino);
				destino.mkdirs();
			 	carpeta = rp.getString("directorios.carpeta.certificados.firma.digital");
				hijoOrigen = new File(origen.getAbsolutePath() + File.separator + carpeta);   
			 	hijoDestino = new File(destino.getAbsolutePath());
			 	hijoDestino.mkdirs();
			 	if (hijoOrigen.exists()) {
			 		recur(hijoOrigen, hijoDestino,instit,idinstitucion);
			 	}
*/
			 	//carpeta.etiquetas
			 	pathDestino = valorParam(instit,"CER","PATH_PLANTILLAS","");
				destino = new File(pathDestino);
				destino.mkdirs();
			 	carpeta = rp.getString("directorios.carpeta.envios.etiquetas");
				hijoOrigen = new File(origen.getAbsolutePath() + File.separator + carpeta);   
			 	hijoDestino = new File(destino.getAbsolutePath());
			 	hijoDestino.mkdirs();
			 	if (hijoOrigen.exists()) {
			 		recur(hijoOrigen, hijoDestino,instit,idinstitucion);
			 	}
				
			 	//carpeta.informes.sjcs
			 	pathDestino = valorParam(instit,"INF","PATH_INFORMES_PLANTILLA","");
				destino = new File(pathDestino);
				destino.mkdirs();
			 	carpeta = rp.getString("directorios.carpeta.informes.sjcs");
				hijoOrigen = new File(origen.getAbsolutePath() + File.separator + carpeta);   
			 	hijoDestino = new File(destino.getAbsolutePath());
			 	hijoDestino.mkdirs();
			 	if (hijoOrigen.exists()) {
			 		recur(hijoOrigen, hijoDestino,instit,idinstitucion);
			 	}
		
			} else {
				throw new Exception("El path origen no es un directorio.");
			}
		} else {
			throw new Exception("No existe el path origen de plantillas.");
		}
		
	}

	private static void recur(File origen, File destino, String institucion, String institucionDefectoAux) {
		
		System.out.println("... COPIANDO " + origen.getAbsolutePath() + " A " + destino.getAbsolutePath());

		File hijos[] = origen.listFiles();
		if (hijos!=null && hijos.length>0) {
			// reviso que existe mi institucion
			boolean existe = false;
			for (int i=0;i<hijos.length;i++) {
				if (hijos[i].isDirectory() && hijos[i].getName().equals(institucion)) existe = true;
			}
			String institucionOriginal = institucion;
			// si no existe la institucion le pongo la de por defecto
			if (!existe) institucion = institucionDefectoAux; 
				
			for (int i=0;i<hijos.length;i++) {
				if (hijos[i].isDirectory()) {
					if (hijos[i].getName().equals(institucion)) {
						// creo el directorio y llamo a recursivo
						File aux = new File(destino.getAbsolutePath() + File.separator + institucionOriginal);
						aux.mkdirs();
						System.out.println("		Creado directorio "+aux.getAbsolutePath());
						recur2(hijos[i],aux);
					} else {
						System.out.println("		No se ha creado directorio "+hijos[i].getName());
					}
				} else {
					// copio el fichero al destino
					String linea;
					BufferedInputStream bufferLectura = null;
					BufferedOutputStream writer = null;
					File nuevoFile = null;
					
					try {
					
						nuevoFile = new File(destino.getAbsolutePath() + File.separator + hijos[i].getName());
						writer = new BufferedOutputStream(new FileOutputStream(nuevoFile, false));

						if (!hijos[i].exists())
							throw new Exception("El fichero origen no existe: " + hijos[i].getAbsolutePath());
					    else {
						    if (!hijos[i].canRead())
								throw new Exception("Error de lectura: " + hijos[i].getAbsolutePath());
						    else {
					    		bufferLectura = new BufferedInputStream(new FileInputStream(hijos[i]));												
								// Lectura de la plantilla linea a linea
					    		byte buf[] = new byte[1000];
								int ret=0;
					    		while ((ret=bufferLectura.read(buf))!=-1){
									writer.write(buf,0,ret);
						    	}
								writer.close();
						    	bufferLectura.close();
						    }
							System.out.println("		copiado correctamente "+hijos[i].getName() +" a "+destino.getAbsolutePath());
					    }
					} catch(IOException _ex) {
						try {
							bufferLectura.close();
						} catch (Exception e) {}
						try {
							writer.close();
						} catch (Exception e) {}
						System.out.println("Error de entrada/salida: "+hijos[i].getAbsolutePath());
						_ex.printStackTrace();
						// continuamos
						
					} catch(Exception _ex) {
						try {
							bufferLectura.close();
						} catch (Exception e) {}
						try {
							writer.close();
						} catch (Exception e) {}
						System.out.println(_ex.getMessage());
						_ex.printStackTrace();
						// continuamos
					}
					
				}
			}
		} else {
			System.out.println("		No se ha copiado nada porque no tiene hijos: "+origen.getName());
		}
	}
	
	private static void recur2(File origen, File destino) {
		
		File hijos[] = origen.listFiles();
		if (hijos!=null && hijos.length>0) {
			for (int i=0;i<hijos.length;i++) {
				if (hijos[i].isDirectory()) {
					// creo el directorio y llamo a recursivo
					File aux = new File(destino.getAbsolutePath() + File.separator + hijos[i].getName());
					aux.mkdirs();
					System.out.println("		Creado directorio "+aux.getAbsolutePath());
					recur2(hijos[i],aux);
				} else {
					// copio el fichero al destino
					String linea;
					BufferedInputStream bufferLectura = null;
					BufferedOutputStream writer = null;
					File nuevoFile = null;
					
					try {
					
						nuevoFile = new File(destino.getAbsolutePath() + File.separator + hijos[i].getName());
						writer = new BufferedOutputStream(new FileOutputStream(nuevoFile, false));

						if (!hijos[i].exists())
							throw new Exception("El fichero origen no existe: " + hijos[i].getAbsolutePath());
					    else {
						    if (!hijos[i].canRead())
								throw new Exception("Error de lectura: " + hijos[i].getAbsolutePath());
						    else {
					    		bufferLectura = new BufferedInputStream(new FileInputStream(hijos[i]));												
								// Lectura de la plantilla linea a linea
					    		byte buf[] = new byte[1000];
								int ret=0;
					    		while ((ret=bufferLectura.read(buf))!=-1){
									writer.write(buf,0,ret);
						    	}
								writer.close();
						    	bufferLectura.close();
						    	System.out.println("		copiado correctamente "+hijos[i].getName() +" a "+destino.getAbsolutePath());
						    }
					    }
					} catch(IOException _ex) {
						try {
							bufferLectura.close();
						} catch (Exception e) {}
						try {
							writer.close();
						} catch (Exception e) {}
						System.out.println("Error de entrada/salida: "+hijos[i].getAbsolutePath());
						_ex.printStackTrace();
						// continuamos
						
					} catch(Exception _ex) {
						try {
							bufferLectura.close();
						} catch (Exception e) {}
						try {
							writer.close();
						} catch (Exception e) {}
						System.out.println(_ex.getMessage());
						_ex.printStackTrace();
						// continuamos
					}
					
				}
			}
		}
	}
	
}
