/**
 * <p>Title: SIGALogging </p>
 * <p>Description: Implementación de un LOG local. </p>
 * <p>Copyright: Copyright (C) 2007</p>
 * <p>Company: AtosOrigin </p>
 * @author RGG 
 * @version 1.0
 */

package com.siga.Utilidades;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;


import com.atos.utils.ClsConstants;
import com.atos.utils.ExceptionManager;


public class SIGALogging
{
	private SimpleDateFormat sdfLong = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); 
	private SimpleDateFormat sdfShort = new SimpleDateFormat("yyyyMMdd"); 
	private String sError = "[ERROR]";
	private String sInfo = "[INFO]";
	private String separador = ClsConstants.SEPARADOR;
	private File f;
	private boolean iniciado = false;
	// log level 0.- todo 1.- Errores 2.- Errores e info
	private int logLevel = 2;
	public static int ERROR = 1;
	public static int INFO = 2;
	

	/**
	 * Crea el fichero de log y el path en el que se encuentra
	 * @param nombreFichero con el path completo.
	 */
	public SIGALogging(String nombreFichero) {
	
		f = new File(nombreFichero);
		File path = new File(f.getParent());
		if (!path.exists())
		{
			path.mkdirs();
		}
	    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//		ReadProperties rp = new ReadProperties("SIGA.properties");	
		iniciado = false;
		logLevel = new Integer(rp.returnProperty("log.colaLetrados.level")).intValue();
	
	}
 
	/**
	 * Escribe una traza en el fichero creado en el constructor con la hora de la traza 
	 * @param s texto a escribir
	 */
	public void write(String s)
	{
	    PrintWriter printer = null;
		try
		{
			Date dat = Calendar.getInstance().getTime();
	
			printer = new PrintWriter(new BufferedWriter(new FileWriter(f, true)));
			printer.print(sdfLong.format(dat)+separador+s+"\r\n");
			printer.flush();
			printer.close();

		} catch(Exception _ex) {
		    try {
		        printer.close();
		    } catch (Exception eee) {}
			System.out.println("Error Escribiendo SIGALogging: "+_ex.toString());
		}
	}
	
	public void writeLimpio(String s)
	{
	    PrintWriter printer = null;
		try
		{
			printer = new PrintWriter(new BufferedWriter(new FileWriter(f, true)));
			printer.print(s+"\r\n");
			printer.flush();
			printer.close();

		} catch(Exception _ex) {
		    try {
		        printer.close();
		    } catch (Exception eee) {}
			System.out.println("Error Escribiendo SIGALogging: "+_ex.toString());
		}
	}
	
	public void writeLimpioError(Exception e, String idInstitucion, String idUsuario)
	{
	    PrintWriter printer = null;
		try
		{
			printer = new PrintWriter(new BufferedWriter(new FileWriter(f, true)));
			printer.print(ExceptionManager.getCompleteMessageParaLogger(e,idInstitucion,idUsuario));
			printer.flush();
			printer.close();

		} catch(Exception _ex) {
		    try {
		        printer.close();
		    } catch (Exception eee) {}
			System.out.println("Error Escribiendo SIGALogging: "+_ex.toString());
		}
	}
	
	
	/**
	 * Escribe una traza en el fichero creado en el constructor con formato para el log de facturas
	 * <br>El formato es: ERROR, FECHA, TIPO, IDPERSONA, NUMEROFACTURA, DESCRIPCION
	 * @param tipo Tipo de traza: CONFIRMACION, PDF o ENVIO
	 * @param idPersona Identificador de la persona
	 * @param numeroFactura Número de factura
	 * @param descripcion Descripción de la traza
	 */
	public void writeLogFactura(String tipo, String idPersona, String numeroFactura, String descripcion)
	{
	    PrintWriter printer = null;
		try
		{
			Date dat = Calendar.getInstance().getTime();
			printer = new PrintWriter(new BufferedWriter(new FileWriter(f, true)));
	
			// formato: ERROR, FECHA, TIPO, IDPERSONA, NUMEROFACTURA, DESCRIPCION
			if (!iniciado) {
				printer.println("TRAZA"+separador+"FECHA"+separador+"TIPO"+separador+"IDPERSONA"+separador+"NUM. FACTURA"+separador+"DESCRIPCIÓN");
				iniciado=true;
			}

			printer.print(sError+separador+sdfLong.format(dat)+separador+tipo+separador+idPersona+separador+numeroFactura+separador+descripcion+"\r\n");
			printer.flush();
			printer.close();

		} catch(Exception _ex) {
		    try {
		        printer.close();
		    } catch (Exception eee) {}
			System.out.println("Error Escribiendo SIGALogging para log de facturacion: "+_ex.toString());
		}
	}
	
	public void writeLogGestorColaSincronizarDatos(int level, Integer idInstitucionOrigen, Long idPersona, String nombreCliente, String descripcion)
	{
	    PrintWriter printer = null;
		try
		{
			if (level <= this.logLevel) {
				Date dat = Calendar.getInstance().getTime();
				printer = new PrintWriter(new BufferedWriter(new FileWriter(f, true)));
		
				// formato: ERROR, FECHA, TIPO, IDPERSONA, NUMEROFACTURA, DESCRIPCION
				if (!iniciado && f.length() < 1) {
					printer.println("TIPO" + separador +
					        		"FECHA" + separador +
					        		"INSTITUCION ORIGEN" + separador +
					        		"IDPERSONA" + separador + 
					        		"NOMBRE CLIENTE" + separador + 
					        		"DESCRIPCIÓN");
					iniciado = true;
				}
	
				printer.print(((level==ERROR)?sError:sInfo) + separador +
				        	  sdfLong.format(dat) + separador + 
				        	  idInstitucionOrigen + separador + 
				        	  idPersona + separador + 
				        	  nombreCliente + separador +
				        	  descripcion + "\r\n");
				printer.flush();
				printer.close();
			}
		} 
		catch(Exception _ex) {
		    try {
		        printer.close();
		    } catch (Exception eee) {}
			System.out.println("Error Escribiendo SIGALogging para log de facturacion: "+_ex.toString());
		}
	}

}