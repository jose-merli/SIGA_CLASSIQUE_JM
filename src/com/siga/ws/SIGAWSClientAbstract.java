/**
 * 
 */
package com.siga.ws;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ReadProperties;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.SIGAReferences;

/**
 * @author angelcpe
 *
 */
public abstract class SIGAWSClientAbstract {
	
	
	protected static String rutaOUT = "xml" + File.separator + "OUT";
	private BufferedWriter bw = null;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss ");
	
	private UsrBean usrBean;
	private int idInstitucion;
	private int idRemesa;	
	private String urlWS;
	
			
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

}
