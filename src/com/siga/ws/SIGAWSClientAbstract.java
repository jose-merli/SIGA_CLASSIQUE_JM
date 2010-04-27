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
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlValidationError;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ReadProperties;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.SIGAReferences;
import com.siga.beans.CajgEJGRemesaAdm;
import com.siga.beans.CajgEJGRemesaBean;
import com.siga.beans.CajgRespuestaEJGRemesaAdm;
import com.siga.beans.CajgRespuestaEJGRemesaBean;

/**
 * @author angelcpe
 *
 */
public abstract class SIGAWSClientAbstract {
	
	
	protected static String rutaOUT = "xml" + File.separator + "OUT";
	private BufferedWriter bw = null;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
	
	private UsrBean usrBean;
	private int idInstitucion;
	private int idRemesa;	
	private String urlWS;
	private boolean generaTXT;
	private boolean firmarXML;
	
			
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
		file.mkdirs();
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
		boolean valido = true;
		List<String> list = validate(xmlObject);
		if (list.size() > 0) {
			valido = false;
			for (String st : list) {
				escribeErrorExpediente(anio, numejg, numero, idTipoEJG, st);
			}
		}
		return valido;
	}
	

	/**
	 * 
	 * @param xmlObject
	 * @return
	 * @throws Exception
	 */
	protected List<String> validate(XmlObject xmlObject) throws Exception {
		
		List<String> list = new ArrayList<String>();
		XmlOptions xmlOptions = new XmlOptions();
		List<XmlValidationError> errores = new ArrayList<XmlValidationError>();
		xmlOptions.setErrorListener(errores);
				
		if (!xmlObject.validate(xmlOptions)){
				
			String st = null;
			for (XmlValidationError error : errores) {				
				if (error.getErrorType() == XmlValidationError.INCORRECT_ELEMENT) {
					String campos = "";
					if (error.getExpectedQNames() != null) {
						for (int i = 0; i < error.getExpectedQNames().size(); i++) {
							QName qName = (QName) error.getExpectedQNames().get(i);
							if (qName != null) {
								if (i == 0){
									campos += qName.getLocalPart();
								} else {
									campos += ", " + qName.getLocalPart();
								}
							}
						}
					}
					st = "Debe rellenar el campo o los campos " + campos + " en el apartado " + error.getFieldQName().getLocalPart();					 
				} else {				
					st = "Error de validación: " + error;
				}
				if (!list.contains(st)) {
					list.add(st);
				}
				ClsLogging.writeFileLog(st, 3);
			}		
			
		}
		return list;
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
	protected void escribeErrorExpediente(String anio, String numEjg, String numero, String idTipoEJG, String descripcionError) throws IOException, ClsExceptions {		
		
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
			escribeLogRemesa("No se ha encontrado el EJG año/número = " + anio + "/" + numEjg + " en la remesa " + getIdRemesa());							
		} else if (vectorRemesa.size() > 1) {
			escribeLogRemesa("Se ha encontrado más de un EJG año/número = " + anio + "/" + numEjg + " en la remesa " + getIdRemesa());							
		} else {									
		
			CajgEJGRemesaBean cajgEJGRemesaBean = (CajgEJGRemesaBean) vectorRemesa.get(0);
			
			CajgRespuestaEJGRemesaBean cajgRespuestaEJGRemesaBean = new CajgRespuestaEJGRemesaBean();			
			cajgRespuestaEJGRemesaBean.setIdEjgRemesa(cajgEJGRemesaBean.getIdEjgRemesa());
			cajgRespuestaEJGRemesaBean.setCodigo("-1");
			cajgRespuestaEJGRemesaBean.setDescripcion(descripcionError);
			cajgRespuestaEJGRemesaBean.setFecha("SYSDATE");		
			
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


}
