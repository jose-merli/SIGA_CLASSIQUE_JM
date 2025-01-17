/**
 * 
 */
package com.siga.ws;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlValidationError;
import org.redabogacia.sigaservices.app.helper.SIGAServicesHelper;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.FileHelper;
import com.atos.utils.UsrBean;
import com.siga.beans.CajgEJGRemesaAdm;
import com.siga.beans.CajgEJGRemesaBean;
import com.siga.beans.CajgRemesaAdm;
import com.siga.beans.CajgRemesaBean;
import com.siga.beans.CajgRespuestaEJGRemesaAdm;
import com.siga.beans.CajgRespuestaEJGRemesaBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.ws.pcajg.cat.xsd.IntercambioDocument;
import com.siga.ws.pcajg.cat.xsd.TipoIdentificacionIntercambio;

import es.satec.businessManager.BusinessException;

/**
 * @author angelcpe
 *
 */
public abstract class SIGAWSClientAbstract {
	
	
	public static String rutaOUT = "xml" + File.separator + "OUT";
	private BufferedWriter bw = null;
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
	private String namespacePDFs = com.siga.ws.pcajg.cat.xsd.pdf.IntercambioDocument.type.getProperties()[0].getName().getNamespaceURI();
	
	private UsrBean usrBean;
	private int idInstitucion;
	private int idRemesa;	
	private String urlWS;
	private String namespaceWS;
	private String usrWS;
	private String pwdWS;
	private boolean generaTXT;
	private boolean firmarXML;
	private boolean simular;

	public abstract void execute() throws Exception;
	
		
	/**
	 * Crea una instancia de la clase que implemente el webservice para esta institucion
	 * @param idInstitucion
	 * @return
	 * @throws ClsExceptions
	 */
	public static SIGAWSClientAbstract getInstance(String clase) throws ClsExceptions {		
		try {
			return (SIGAWSClientAbstract) Class.forName((String)clase).newInstance();
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al instanciar la clase " + clase);
		}		
	}
	
	/**
	 * 
	 * @param idInstitucion
	 * @param idRemesa
	 * @return
	 */
	public static boolean isRespondida(int idInstitucion, int idRemesa) {
		
		boolean respondida = false;
		
		ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String rutaAlmacen = rp.returnProperty("cajg.directorioFisicoCAJG") + rp.returnProperty("cajg.directorioCAJGJava");
			
		rutaAlmacen += File.separator + idInstitucion;
		rutaAlmacen += File.separator + idRemesa;
		
		File file = new File(rutaAlmacen + File.separator + rutaOUT);
		File[] files = file.listFiles();
		if (files != null) {
			for (File fileXML : files) {
				String extension = fileXML.getName().substring(fileXML.getName().lastIndexOf("."));
				if (extension.equalsIgnoreCase(".xml")){
					respondida = true;
					break;
				}
			}
		}
		
		return respondida;
	}
	
	
	/**
	 * 
	 * @param idInstitucion
	 * @param idRemesa
	 * @return
	 */
	public static File getErrorFile(int idInstitucion, int idRemesa) {
		ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String rutaAlmacen = rp.returnProperty("cajg.directorioFisicoCAJG") + rp.returnProperty("cajg.directorioCAJGJava");
			
		rutaAlmacen += File.separator + idInstitucion;
		rutaAlmacen += File.separator + idRemesa;
		
		File file = new File(rutaAlmacen + File.separator + "log");
		FileHelper.mkdirs(file.getAbsolutePath());
		file = new File(file, "incidencias.log");
		return file;
	}
	
	/**
	 * 
	 * @param idInstitucion
	 * @return
	 */
	public static File getErrorFile(int idInstitucion) {
		ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String rutaAlmacen = rp.returnProperty("wsMutualidad.directorioFicheros") + rp.returnProperty("wsMutualidad.directorioLog");
			
		rutaAlmacen += File.separator + idInstitucion;
		
		File file = new File(rutaAlmacen + File.separator + "log");
		FileHelper.mkdirs(file.getAbsolutePath());
		file = new File(file, "incidencias.log");
		return file;
	}
	
	/**
	 * @return the usrBean
	 */
	public UsrBean getUsrBean() {
		return usrBean;
	}

	/**
	 * @param usrBean the usrBean to set
	 */
	public void setUsrBean(UsrBean usrBean) {
		this.usrBean = usrBean;
	}

	/**
	 * @return the idInstitucion
	 */
	public int getIdInstitucion() {
		return idInstitucion;
	}

	/**
	 * @param idInstitucion the idInstitucion to set
	 */
	public void setIdInstitucion(int idInstitucion) {
		this.idInstitucion = idInstitucion;
	}

	/**
	 * @return the idRemesa
	 */
	public int getIdRemesa() {
		return idRemesa;
	}

	/**
	 * @param idRemesa the idRemesa to set
	 */
	public void setIdRemesa(int idRemesa) {
		this.idRemesa = idRemesa;
	}

	/**
	 * @return the urlWS
	 */
	public String getUrlWS() {
		return urlWS;
	}
	
	/**
	 * @return the urlWS
	 * @throws ClsExceptions 
	 */
	public String getUrlWSParametro(String parametro) throws ClsExceptions {
		GenParametrosAdm paramAdm = new GenParametrosAdm(usrBean);
		return paramAdm.getValor(usrBean.getLocation(),"CEN", parametro, "");
	}

	/**
	 * @param urlWS the urlWS to set
	 */
	public void setUrlWS(String urlWS) {
		this.urlWS = urlWS;
	}

	/**
	 * 
	 * @param idInstitucion
	 * @param idRemesa
	 * @return
	 */
	protected static String getKeyMap (String idInstitucion, String idRemesa) {
		return idInstitucion + "-" + idRemesa;
	}
	
	/**
	 * 
	 * @param bufferedWriter
	 * @param texto
	 * @throws IOException
	 */
	protected void escribeLogRemesa(String texto) throws IOException {
		if (bw == null) {
			File errorFile = getErrorFile(getIdInstitucion(), getIdRemesa());			    
			FileWriter fileWriter = new FileWriter(errorFile, true);
			bw = new BufferedWriter(fileWriter);
		}
		
		String fecha = sdf.format(new Date());		
		
		bw.write(fecha + texto);
		bw.write("\n");
		bw.flush();
	}
	
	protected void escribeLog(String texto) throws IOException {
		if (bw == null) {
			File errorFile = getErrorFile(getIdInstitucion());			    
			FileWriter fileWriter = new FileWriter(errorFile, true);
			bw = new BufferedWriter(fileWriter);
		}
		
		String fecha = sdf.format(new Date());		
		
		bw.write(fecha + texto);
		bw.write("\n");
		bw.flush();
	}
	
	/**
	 * 
	 * @throws IOException
	 */
	public void closeLogFile() {
		if (bw != null) {
			try {
				bw.flush();			
				bw.close();
				bw = null;
			} catch (IOException e) {
				ClsLogging.writeFileLogError("Error al cerrar el fichero de LOG", e, 3);
			}
		}
	}
	
	/**
	 * 
	 * @param xmlObject
	 * @param usr
	 * @param anio
	 * @param numejg
	 * @param numero
	 * @param idTipoEJG
	 * @throws Exception
	 */
	protected boolean validateXML_EJG(XmlObject xmlObject, String anio, String numejg, String numero, String idTipoEJG) throws Exception {				
		return validateXML_EJG(xmlObject, anio, numejg, numero, idTipoEJG, true);
	}
	
	/**
	 * 
	 * @param xmlObject
	 * @param usr
	 * @param anio
	 * @param numejg
	 * @param numero
	 * @param idTipoEJG
	 * @throws Exception
	 */
	protected boolean validateXML_EJG_NoDeleteEmptyNode(XmlObject xmlObject, String anio, String numejg, String numero, String idTipoEJG) throws Exception {				
		return validateXML_EJG(xmlObject, anio, numejg, numero, idTipoEJG, false);
	}
	
	/**
	 * 
	 * @param xmlObject
	 * @param usr
	 * @param anio
	 * @param numejg
	 * @param numero
	 * @param idTipoEJG
	 * @throws Exception
	 */
	private boolean validateXML_EJG(XmlObject xmlObject, String anio, String numejg, String numero, String idTipoEJG, boolean deleteEmptyNode) throws Exception {
				
		boolean valido = true;
		List<String> list = null;
		if (deleteEmptyNode) {
			list = SIGAServicesHelper.validate(xmlObject);
		} else {
			list = SIGAServicesHelper.validate(xmlObject, false);
		}
		if (list != null && list.size() > 0) {
			valido = false;
			for (String st : list) {
				if (st != null) {
					escribeErrorExpediente(anio, numejg, numero, idTipoEJG, st, CajgRespuestaEJGRemesaBean.TIPO_RESPUESTA_SIGA);
				}
			}
		}
		return valido;
	}
	
	/**
	 * 
	 * @param xmlObject
	 * @return
	 */
	public static StringBuffer validateXML(XmlObject xmlObject) {
		StringBuffer sb = null;
		
		XmlOptions xmlOptions = new XmlOptions();
		List<XmlValidationError> errores = new ArrayList<XmlValidationError>();
		xmlOptions.setErrorListener(errores);
				
		if (!xmlObject.validate(xmlOptions)){
			sb = new StringBuffer();
			for (XmlValidationError error : errores) {
				sb.append(error);
				sb.append("\n");
			}
		}
		return sb;
	}
	
	
	/**
	 * 
	 * @param cajgEJGRemesaAdm
	 * @param cajgRespuestaEJGRemesaAdm
	 * @param anio
	 * @param numEjg
	 * @param numero
	 * @param idTipoEJG
	 * @param descripcionError
	 * @throws IOException
	 * @throws ClsExceptions
	 */
	protected void escribeErrorExpediente(String anio, String numEjg, String numero, String idTipoEJG, String descripcionError, Integer idTipoRespuesta) throws IOException, ClsExceptions {		
		
		CajgEJGRemesaAdm cajgEJGRemesaAdm = new CajgEJGRemesaAdm(getUsrBean());
		CajgRespuestaEJGRemesaAdm cajgRespuestaEJGRemesaAdm = new CajgRespuestaEJGRemesaAdm(getUsrBean());
		
		Hashtable<String, Object> hashEjgRem = new Hashtable<String, Object>();
		hashEjgRem.put(CajgEJGRemesaBean.C_IDINSTITUCION, getIdInstitucion());
		hashEjgRem.put(CajgEJGRemesaBean.C_ANIO, anio);
		hashEjgRem.put(CajgEJGRemesaBean.C_NUMERO, numero);
		hashEjgRem.put(CajgEJGRemesaBean.C_IDTIPOEJG, idTipoEJG);
		
		hashEjgRem.put(CajgEJGRemesaBean.C_IDINSTITUCIONREMESA, getIdInstitucion());
		hashEjgRem.put(CajgEJGRemesaBean.C_IDREMESA, getIdRemesa());
		
		Vector vectorRemesa = cajgEJGRemesaAdm.select(hashEjgRem);
		if (vectorRemesa.size() == 0) {
			escribeLogRemesa("No se ha encontrado el EJG a�o/n�mero = " + anio + "/" + numEjg + " en la remesa " + getIdRemesa());							
		} else if (vectorRemesa.size() > 1) {
			escribeLogRemesa("Se ha encontrado m�s de un EJG a�o/n�mero = " + anio + "/" + numEjg + " en la remesa " + getIdRemesa());							
		} else {									
		
			CajgEJGRemesaBean cajgEJGRemesaBean = (CajgEJGRemesaBean) vectorRemesa.get(0);
			
			CajgRespuestaEJGRemesaBean cajgRespuestaEJGRemesaBean = new CajgRespuestaEJGRemesaBean();			
			cajgRespuestaEJGRemesaBean.setIdEjgRemesa(cajgEJGRemesaBean.getIdEjgRemesa());
			cajgRespuestaEJGRemesaBean.setCodigo("-1");
			if (descripcionError != null && descripcionError.length() >= 1500) {//en la bdd esta definido como varchar de 1500
				descripcionError = descripcionError.substring(0, 1499);
			}
			cajgRespuestaEJGRemesaBean.setDescripcion(descripcionError);
			cajgRespuestaEJGRemesaBean.setFecha("SYSDATE");
			cajgRespuestaEJGRemesaBean.setIdTipoRespuesta(idTipoRespuesta);
			
			cajgRespuestaEJGRemesaAdm.insert(cajgRespuestaEJGRemesaBean);
		}
	}


	/**
	 * @return the generaTXT
	 */
	public boolean isGeneraTXT() {
		return generaTXT;
	}


	/**
	 * @param generaTXT the generaTXT to set
	 */
	public void setGeneraTXT(boolean generaTXT) {
		this.generaTXT = generaTXT;
	}


	/**
	 * @return the firmarXML
	 */
	public boolean isFirmarXML() {
		return firmarXML;
	}


	/**
	 * @param firmarXML the firmarXML to set
	 */
	public void setFirmarXML(boolean firmarXML) {
		this.firmarXML = firmarXML;
	}


	protected static String getNombreRutaZIPconXMLs(int idInstitucion, int idRemesa) {
		return getDirXML(idInstitucion, idRemesa) + File.separator + idInstitucion + "_" + idRemesa;
	}
	
	public static String getRutaFicheroZIP(int idInstitucion, int idRemesa) {
		return getNombreRutaZIPconXMLs(idInstitucion, idRemesa) + ".zip";
	}

	protected static String getDirXML(int idInstitucion, int idRemesa) {
		String keyPathFicheros = "cajg.directorioFisicoCAJG";		
		String keyPath2 = "cajg.directorioCAJGJava";				
	    ReadProperties p= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String pathFichero = p.returnProperty(keyPathFicheros) + p.returnProperty(keyPath2);
		return pathFichero + File.separator + idInstitucion  + File.separator + idRemesa + File.separator + "xml";
	}
	
	protected String getNombreFicheroIndex(com.siga.ws.pcajg.cat.xsd.pdf.TipoIdentificacionIntercambio tipoIdentificacionIntercambio, int numDetalles) {
		StringBuffer nombreFichero = new StringBuffer();
		nombreFichero.append("IDO");
		nombreFichero.append("_" + tipoIdentificacionIntercambio.getCodOrigenIntercambio());
		nombreFichero.append("_" + tipoIdentificacionIntercambio.getCodDestinoIntercambio());
		nombreFichero.append("_" + tipoIdentificacionIntercambio.getIdentificadorIntercambio());
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String fechaIntercambio = sdf.format(tipoIdentificacionIntercambio.getFechaIntercambio().getTime());
		nombreFichero.append("_" + fechaIntercambio);
		nombreFichero.append("_" + numDetalles);
		nombreFichero.append(".index.xml");
		return nombreFichero.toString();
	}
	
	protected String getNombreFicheroAdjunto(com.siga.ws.pcajg.cat.xsd.pdf.TipoIdentificacionIntercambio tipoIdentificacionIntercambio, String fichero) {
		StringBuffer nombreFichero = new StringBuffer();
		nombreFichero.append("IDO");
		nombreFichero.append("_" + tipoIdentificacionIntercambio.getCodOrigenIntercambio());
		nombreFichero.append("_" + tipoIdentificacionIntercambio.getCodDestinoIntercambio());
		
		
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		//String fechaIntercambio = sdf.format(tipoIdentificacionIntercambio.getFechaIntercambio().getTime());
		//nombreFichero.append("_" + fechaIntercambio);
		nombreFichero.append("_" + fichero);
		return nombreFichero.toString();
	}
	
	protected String getNombreFichero(TipoIdentificacionIntercambio tipoIdentificacionIntercambio) {
		StringBuffer nombreFichero = new StringBuffer();
		nombreFichero.append(tipoIdentificacionIntercambio.getTipoIntercambio());
		nombreFichero.append("_" + tipoIdentificacionIntercambio.getCodOrigenIntercambio());
		nombreFichero.append("_" + tipoIdentificacionIntercambio.getCodDestinoIntercambio());
		nombreFichero.append("_" + tipoIdentificacionIntercambio.getIdentificadorIntercambio());
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String fechaIntercambio = sdf.format(tipoIdentificacionIntercambio.getFechaIntercambio().getTime());
		nombreFichero.append("_" + fechaIntercambio);
		nombreFichero.append("_" + tipoIdentificacionIntercambio.getNumeroDetallesIntercambio());
		nombreFichero.append(".xml");
		return nombreFichero.toString();
	}
	
	protected void guardaFicheroFormatoCatalan(IntercambioDocument intercambioDocument, File file) throws Exception {
		SIGAServicesHelper.deleteEmptyNode(intercambioDocument.getIntercambio().getDomNode());
		
		XmlOptions xmlOptions = new XmlOptions();
		xmlOptions.setSavePrettyPrintIndent(4);
		xmlOptions.setSavePrettyPrint();
		
		xmlOptions.setCharacterEncoding("ISO-8859-15");
		Map<String, String> mapa = new HashMap<String, String>();
		mapa.put("", intercambioDocument.getIntercambio().getDomNode().getNamespaceURI());
		xmlOptions.setSaveImplicitNamespaces(mapa);
		
		ClsLogging.writeFileLog("Guardando fichero generado xml para la Generalitat en " + file.getAbsolutePath(), 3);
		intercambioDocument.save(file, xmlOptions);
		//comprobamos que el fichero generado sea correcto
		StringBuffer sbErrores = SIGAWSClientAbstract.validateXML(intercambioDocument); 
				
//		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//		DocumentBuilder builder = factory.newDocumentBuilder();
//		Document xmldoc = builder.parse(file);
//		//quitamos el namespace. solicitado por ibermatica en correo del 01/02/2011 16:04
//		xmldoc.getDocumentElement().removeAttribute("xmlns");
//		
//		FileOutputStream fos = new FileOutputStream(file);		
//		OutputFormat of = new OutputFormat("XML", "ISO-8859-15", true);				
//		of.setIndent(2);
//		of.setLineWidth(1500);
//		
//		XMLSerializer serializer = new XMLSerializer(fos, of);
//		serializer.asDOMSerializer();
//		serializer.serialize( xmldoc.getDocumentElement() );
//		fos.flush();
//		fos.close();
		
		//si no es correcto lo genero y lo transformo para poder ver por qu� no es correcto
		if (sbErrores != null) {
			throw new Exception("El xml generado no cumple el esquema establecido con la Generalitat: " + sbErrores.toString());
		}
	}
	
	protected void guardaFicheroIndexFormatoCatalan(com.siga.ws.pcajg.cat.xsd.pdf.IntercambioDocument indexDocumentacion, File file) throws Exception {
		SIGAServicesHelper.deleteEmptyNode(indexDocumentacion.getIntercambio().getDomNode());
		
		XmlOptions xmlOptions = new XmlOptions();
		xmlOptions.setSavePrettyPrintIndent(4);
		xmlOptions.setSavePrettyPrint();
		
		xmlOptions.setCharacterEncoding("ISO-8859-15");
		Map<String, String> mapa = new HashMap<String, String>();
		mapa.put(indexDocumentacion.getDomNode().getFirstChild().getNamespaceURI(), namespacePDFs);
		xmlOptions.setLoadSubstituteNamespaces(mapa);
		//xmlOptions.setSaveImplicitNamespaces(mapa);
		
		ClsLogging.writeFileLog("Guardando fichero generado xml para la Generalitat en " + file.getAbsolutePath(), 3);
		indexDocumentacion.save(file, xmlOptions);
		//comprobamos que el fichero generado sea correcto
		StringBuffer sbErrores = SIGAWSClientAbstract.validateXML(indexDocumentacion); 
		
		//si no es correcto lo genero y lo transformo para poder ver por qu� no es correcto
		if (sbErrores != null) {
			throw new Exception("El xml generado no cumple el esquema establecido con la Generalitat: " + sbErrores.toString());
		}
	}
	
	public boolean isSimular() {
		return simular;
	}


	public void setSimular(boolean simular) {
		this.simular = simular;
	}

	protected void guardarIdIntercambioRemesa(UsrBean usr, int idIntercambio) throws Exception {
		
		if (!(idIntercambio > 0)) {
			throw new Exception("No se ha podido actualizar la remesa = \"" + getIdRemesa() + "\" de la idinstitucion \"" + getIdInstitucion() + "\" con el idIntercambio = \"" + idIntercambio + "\" porque el valor idintercambio debe ser mayor que cero");
		}
		
		//como la hemos enviado seteamos el identificador de intercambio de envio
		CajgRemesaAdm cajgRemesaAdm = new CajgRemesaAdm(usr);
		Hashtable hash = new Hashtable();
		hash.put(CajgRemesaBean.C_IDINSTITUCION, getIdInstitucion());
		hash.put(CajgRemesaBean.C_IDREMESA, getIdRemesa());
		Vector v = cajgRemesaAdm.selectByPK(hash);
		if (v != null && v.size() == 1) {
			CajgRemesaBean cajgRemesaBean = (CajgRemesaBean)v.get(0);			
			cajgRemesaBean.setIdIntercambio(idIntercambio);
			
			if (!cajgRemesaAdm.update(cajgRemesaBean)) {
				throw new Exception("No se ha podido actualizar la remesa = \"" + getIdRemesa() + "\" de la idinstitucion \"" + getIdInstitucion() + "\" con el idIntercambio = \"" + idIntercambio + "\"");
			}			
		} else {
			throw new Exception("No se ha recuperado la remesa con idinstitucion " + getIdInstitucion() + " e idremesa = " + getIdRemesa());
		}		
	}

	public static File getRespuestaFile(int idInstitucion, String idRemesa, String fileName) {
		
		ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String rutaAlmacen = rp.returnProperty("cajg.directorioFisicoCAJG") + rp.returnProperty("cajg.directorioCAJGJava");
			
		rutaAlmacen += File.separator + idInstitucion;
		rutaAlmacen += File.separator + idRemesa;
		
		File file = new File(rutaAlmacen + File.separator + SIGAWSClientAbstract.rutaOUT);
		FileHelper.mkdirs(file.getAbsolutePath());
		file = new File(file, fileName);
		return file;
	}
	public static List<File> getXmlFile(int idInstitucion, int idRemesa) throws BusinessException {
		List<File> xmlFiles = new ArrayList<File>();
		String pathFichero = getDirXML(idInstitucion, idRemesa);
		File file = new File(pathFichero);
		String[] files = file.list();
		if(files.length>0){
			if(files.length>1){
				for (String pathFile : files) {
					File xmlFile = new File(pathFichero+File.separator+pathFile);	
					xmlFiles.add(xmlFile);
					
				}
			}else{
				File xmlFile = new File(pathFichero+File.separator+files[0]);
				xmlFiles.add(xmlFile);
				
			}
		}else
			throw new BusinessException("Lista vacia");
		return xmlFiles;
		
	}


	public String getUsrWS() {
		return usrWS;
	}


	public void setUsrWS(String usrWS) {
		this.usrWS = usrWS;
	}


	public String getPwdWS() {
		return pwdWS;
	}


	public void setPwdWS(String pwdWS) {
		this.pwdWS = pwdWS;
	}


	public String getNamespaceWS() {
		return namespaceWS;
	}


	public void setNamespaceWS(String namespaceWS) {
		this.namespaceWS = namespaceWS;
	}
}
