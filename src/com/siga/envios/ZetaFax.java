/*
 * Created on Jul 12, 2005
 * Autor: david.sanchezp
 * Esta clase permite generar un fichero ne formato especial para la aplicacion ZSUBMIT del ZETAFAX.
 * Permite configurar el envio de faxes a varios usuarios con texto y con varios ficheros por usuario.
 */
package com.siga.envios;

import java.io.File;
import java.io.FileWriter;

import java.io.PrintWriter;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.beans.GenParametrosAdm;


/**
 * @author david.sanchezp
 * Esta clase permite generar un fichero ne formato especial para la aplicacion ZSUBMIT del ZETAFAX.<br>
 * Permite configurar el envio de faxes a varios usuarios con texto y con varios ficheros por usuario.
 */
public class ZetaFax {

	//ATRIBUTOS DEL FAX
	private Vector usuarios = null;
	private Vector ficheros = null;	
	
	private String direccionFrom = null;
	private String asunto = null;
	private String texto = null;	
	private String prioridad = null;
	private String documentoPDF = null;
	private String ficheroAdjunto = null;
	private String usuarioZetaFax = null;
	private String hold = null;
	private String preview = null;
	private String usuarioLAN = null;
	private String organisation = null;
	private String idInstitucion = null;
	private String pathZSUBMIT = null;
	
	
		
	
	//CONSTANTES
	public static final String RUTA_ZETAFAX = "\\\\Dsanchezp-mad\\ZFAX";
	public static final String PRIORIDAD_URGENT = "URGENT";
	public static final String PRIORIDAD_NORMAL = "NORMAL";
	public static final String PRIORIDAD_BACKGROUND = "BACKGROUND";
	public static final int ANCHO_LINEA = 80;//Ancho de la linea a enviar en el fax
	public static final String YES = "YES";
	public static final String NO = "NO";
	
	
	public ZetaFax (String direccionFrom, Vector usuarios, Vector ficheros){
		//Origen
		this.direccionFrom = direccionFrom;
		//Vector de usuarios destinatarios
		this.usuarios = new Vector();
		this.usuarios = (Vector)usuarios.clone();
		//Vector de ficheros PDF
		this.ficheros = new Vector();
		this.ficheros = (Vector)ficheros.clone();
	}
	
	public void crearDocumentoFax(String nombreFichero, UsrBean usr) throws ClsExceptions {
		File ficheroTemp=null, ficheroSalida=null;
		FileWriter writer = null;
		PrintWriter printWriter = null;
		GenParametrosAdm parametrosAdm = new GenParametrosAdm(usr); 
		
		try {
			//--------------------------------------------------
			// FICHEROS
			//--------------------------------------------------

			//PATH donde crear los ficheros temporales usados por el ZSUBMIT:
			String rutaFichero = parametrosAdm.getValor("0","ENV","PATH_ZSUMBIT",null);

			//Creamos si no existe la estructura de directorios para los ficheros del ZSUBMIT:
			File directorioZSUBMIT = new File(rutaFichero);
			directorioZSUBMIT.mkdirs();
			
	        //Fichero temporal del fax a enviar por el ZSUBMIT:			
			ficheroTemp = new File(rutaFichero+File.separator+nombreFichero+".TMP");
			
			//Fichero final del fax a enviar por el ZSUBMIT:
			ficheroSalida = new File(rutaFichero+File.separator+nombreFichero+".SUB");
						
			writer = new FileWriter(ficheroTemp);
			printWriter = new PrintWriter(writer);		
			
			
			//--------------------------------------------------
			// PARAMETROS INICIALES
			//--------------------------------------------------
			
			printWriter.println("%%[MESSAGE]");

			// El usuario Zetafax es configurado por defecto en el SETUP.INI
			// Si se permite sobreescribirlo o no hay uno por defecto podemos indicarlos con este parametro:
			if (this.getUsuarioZetaFax()!=null)
				printWriter.println("USER:"+this.getUsuarioZetaFax());			
			
			//HOLD: El mensaje no se enviara hasta que el cliente lo confirme
			if (this.getHold()!=null)
				printWriter.println("HOLD: "+this.getHold()); //YES o NO
			
			//PREVIEW: Prepara la vista Previa del Fax. Para usarlo debemos poner a YES el parametro HOLD.
			if (this.getPreview()!=null)				
				printWriter.println("PREVIEW: "+this.getPreview()); //YES o NO
			
			//SUBJECT: Asunto del FAX:
			if (this.getAsunto()!=null)
				printWriter.println("SUBJECT: "+this.getAsunto());	

			//Prioridad de envio del FAX:
			if (this.getAsunto()!=null)
				printWriter.println("PRIORITY: "+this.getPrioridad());	

			//USUARIO LAN:
			if (this.getUsuarioLAN()!=null)
				printWriter.println("LAN: "+this.getUsuarioLAN());	
			
			
			//ORIGEN:
			if (this.getDireccionFrom()==null || this.getDireccionFrom().trim().equals("")) {
				printWriter.println("FROM:"+this.getOrganisation());
				} else {
				printWriter.println("FROM:"+this.getDireccionFrom());
			}
			
			//DESTINATARIOS: Recorremos la lista de usuarios destinatarios:
			UsuarioFax usuario = null;
			for (int i=0; i < this.getUsuarios().size(); i++) {
				usuario = (UsuarioFax)this.getUsuarios().get(i);				
				printWriter.println("TO:"+usuario.getNombre());			

				//ORGANISATION del FAX:
				if (this.getOrganisation()!=null) {
					printWriter.println("ORGANISATION: "+this.getOrganisation());
				}
				
				printWriter.println("FAX:"+usuario.getFax());
				printWriter.println("");
			}
			
			
			//--------------------------------------------------
			// CUERPO DEL MENSAJE
			//--------------------------------------------------
			
			//TEXTO: Imprimimos lineas de un ancho de ANCHO_LINEA caracteres
			printWriter.println("");			
			printWriter.println("%%[TEXT]");
			String lineaTexto=null, textoCompleto=null;
			if (this.getTexto() != null){
				textoCompleto=this.getTexto();
				while (textoCompleto!=null && !textoCompleto.equals("")){
					if (textoCompleto.length() < ZetaFax.ANCHO_LINEA)
						lineaTexto = textoCompleto.substring(0);
					else
						lineaTexto = textoCompleto.substring(0,ZetaFax.ANCHO_LINEA);
					textoCompleto = textoCompleto.substring(lineaTexto.length());
					printWriter.println(lineaTexto);
				}
			}
			
			//FICHEROS: Recorremos la lista de ficheros:		
			Vector ficherosPDF = this.getFicheros(); 
			if (ficherosPDF!=null && ficherosPDF.size()>0) {
				for (int i=0; i < ficherosPDF.size(); i++) {					
					printWriter.println("%%[FILE]");
					printWriter.println((String)ficherosPDF.get(i));	
					printWriter.println("");
				}
			}

			//Cerramos el fichero de escritura:
			printWriter.close();	
			
			//Renombramos el fichero con extension .NEW a extension .SUB una vez escrito todo su contenido:
			ficheroTemp.renameTo(ficheroSalida);
			
		} catch (Exception e) {
			e.printStackTrace();
			try {
				//Borramos el Fichero temporal
				ficheroTemp.delete();
			} catch (Exception io){
				//ClsLogging.writeFileLog("Error en la clase Zetafax al crear el fichero temporal de envio de Fax.",10);
			}		
			try {
				// cerramos el fichero
				printWriter.close();				
			} catch (Exception io){
				//ClsLogging.writeFileLog("Error en la clase Zetafax al crear el fichero temporal de envio de Fax.",10);
			}		
			throw new ClsExceptions(e,"Error al enviar fax: " + e.getMessage());
		} 
	}
		
	/**
	 * @return Returns the asunto.
	 */
	public String getAsunto() {
		return asunto;
	}
	/**
	 * @param asunto The asunto to set.
	 */
	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}
	/**
	 * @return Returns the direccionFrom.
	 */
	public String getDireccionFrom() {
		return direccionFrom;
	}
	/**
	 * @param direccionFrom The direccionFrom to set.
	 */
	public void setDireccionFrom(String direccionFrom) {
		this.direccionFrom = direccionFrom;
	}
	/**
	 * @return Returns the texto.
	 */
	public String getTexto() {
		return texto;
	}
	/**
	 * @param texto The texto to set.
	 */
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public Vector getUsuarios() {
		return usuarios;
	}
	public void setUsuarios(Vector usuarios) {
		this.usuarios = usuarios;
	}
	public String getPrioridad() {
		return prioridad;
	}
	public void setPrioridad(String prioridad) {
		this.prioridad = prioridad;
	}
	public String getDocumentoPDF() {
		return documentoPDF;
	}
	public void setDocumentoPDF(String documentoPDF) {
		this.documentoPDF = documentoPDF;
	}
	public String getFicheroAdjunto() {
		return ficheroAdjunto;
	}
	public void setFicheroAdjunto(String ficheroAdjunto) {
		this.ficheroAdjunto = ficheroAdjunto;
	}
	public Vector getFicheros() {
		return ficheros;
	}
	public void setFicheros(Vector ficheros) {
		this.ficheros = ficheros;
	}
	public String getUsuarioZetaFax() {
		return usuarioZetaFax;
	}
	public void setUsuarioZetaFax(String usuarioZetaFax) {
		this.usuarioZetaFax = usuarioZetaFax;
	}
	public String getHold() {
		return hold;
	}
	public void setHold(String hold) {
		this.hold = hold;
	}
	public String getPreview() {
		return preview;
	}
	public void setPreview(String preview) {
		this.preview = preview;
	}
	public String getUsuarioLAN() {
		return usuarioLAN;
	}
	public void setUsuarioLAN(String usuarioLAN) {
		this.usuarioLAN = usuarioLAN;
	}
	public String getOrganisation() {
		return organisation;
	}
	public void setOrganisation(String organisation) {
		this.organisation = organisation;
	}
	public String getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public String getPathZSUBMIT() {
		return pathZSUBMIT;
	}
	public void setPathZSUBMIT(String pathZSUBMIT) {
		this.pathZSUBMIT = pathZSUBMIT;
	}
}

