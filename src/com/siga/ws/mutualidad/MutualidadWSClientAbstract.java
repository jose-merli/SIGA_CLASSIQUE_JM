/**
 * 
 */
package com.siga.ws.mutualidad;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlValidationError;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ReadProperties;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.SIGAReferences;
import com.siga.beans.GenParametrosAdm;
import com.siga.ws.pcajg.cat.xsd.TipoIdentificacionIntercambio;

/**
 * @author angelcpe
 *
 */
public abstract class MutualidadWSClientAbstract {
	
	
	protected static String rutaOUT = "xml" + File.separator + "OUT";
	private BufferedWriter bw = null;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
	
	private UsrBean usrBean;
	private int idInstitucion;
	private int idRemesa;	
	private String urlWS;
	private boolean generaTXT;
	private boolean firmarXML;
	private boolean simular;

	//public abstract void execute() throws Exception;
	
		
	/**
	 * Crea una instancia de la clase que implemente el webservice para esta institucion
	 * @param idInstitucion
	 * @return
	 * @throws ClsExceptions
	 */
	public static MutualidadWSClientAbstract getInstance(String clase) throws ClsExceptions {		
		try {
			return (MutualidadWSClientAbstract) Class.forName((String)clase).newInstance();
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
	public static File getErrorFile(int idInstitucion) {
		ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String rutaAlmacen = rp.returnProperty("wsMutualidad.directorioFicheros") + rp.returnProperty("wsMutualidad.directorioLog");
			
		rutaAlmacen += File.separator + idInstitucion;
		
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
	 * @return the urlWS
	 * @throws ClsExceptions 
	 */
	public String getUrlWS(String parametro) throws ClsExceptions {
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
	 * @param bufferedWriter
	 * @param texto
	 * @throws IOException
	 */
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
