package com.siga.ws;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.apache.struts.upload.FormFile;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.GestorContadores;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CajgRemesaResolucionAdm;
import com.siga.beans.CajgRemesaResolucionBean;
import com.siga.beans.CajgRemesaResolucionFicheroAdm;
import com.siga.beans.CajgRemesaResolucionFicheroBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.general.SIGAException;
import com.siga.gratuita.pcajg.resoluciones.ResolucionesFicheroAbstract;
import com.siga.informes.MasterWords;

/**
 * @author mjm
 * @date 20/05/2014
 *
 * Ser Campeón no es una Meta, es una Actitud	
 *
 */
public class SigaWSUploadDocResRem {

	private BufferedWriter bw = null;
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
	private boolean ELIMINA_DATOS_TABLA_TEMPORAL=true;
	private UsrBean usrBean;
	private int idInstitucion;
	private int idTipoRemesa;	
	private String idRemesaResolucion;
	private FormFile file;
	private String fechaResolucion;
	private String fechaCarga;
	private String prefijo;
	private String sufijo;
	private String numero;
	private String mensajeOut;
	private String tipoEjecucion;

	public void execute() throws SIGAException {
		
		UserTransaction tx = null;
		
		try{
			if(tipoEjecucion.equalsIgnoreCase("background"))
					tx = usrBean.getTransactionPesada();
			else
					tx=usrBean.getTransaction();
			
			tx.begin();
					
			CajgRemesaResolucionAdm resolucionAdm = new CajgRemesaResolucionAdm(usrBean);
			
			GestorContadores gcRemesa = new GestorContadores(usrBean);
	
			CajgRemesaResolucionAdm cajgRemesaResolucionAdm = new CajgRemesaResolucionAdm(usrBean);
			String idContador = cajgRemesaResolucionAdm.getIdContador(String.valueOf(idInstitucion), String.valueOf(idTipoRemesa));
			
			if (idContador == null || idContador.trim().equals("")) {
				throw new ClsExceptions("No se ha encontrado el contador para el tipo remesa " + idTipoRemesa);
			}
			
			
			Hashtable contadorTablaHashRemesa = gcRemesa.getContador(idInstitucion, idContador);
			String siguiente = gcRemesa.getNuevoContador(contadorTablaHashRemesa);	
			
			CajgRemesaResolucionBean cajgRemesaResolucionBean = new CajgRemesaResolucionBean();
			
			cajgRemesaResolucionBean.setPrefijo(contadorTablaHashRemesa.get(CajgRemesaResolucionBean.C_PREFIJO).toString());
			cajgRemesaResolucionBean.setSufijo(contadorTablaHashRemesa.get(CajgRemesaResolucionBean.C_SUFIJO).toString());
			cajgRemesaResolucionBean.setNumero(siguiente);
			cajgRemesaResolucionBean.setFechaCarga(GstDate.getApplicationFormatDate("", fechaResolucion));
			cajgRemesaResolucionBean.setFechaResolucion(GstDate.getApplicationFormatDate("", fechaCarga));
	
			gcRemesa.setContador(contadorTablaHashRemesa, siguiente);
	
			cajgRemesaResolucionBean.setIdInstitucion(idInstitucion);
			cajgRemesaResolucionBean.setIdRemesaResolucion(Integer.valueOf(idRemesaResolucion));			
			cajgRemesaResolucionBean.setIdTipoRemesa(Integer.valueOf(idTipoRemesa));
						
			File parentFile = getRutaAlmacenFichero(String.valueOf(idInstitucion), idRemesaResolucion);			
		
	    	InputStream stream = file.getInputStream();
	    	
	    	File fileN = new File(parentFile, file.getFileName());    
	
	    	cajgRemesaResolucionBean.setNombreFichero(fileN.getName());
	    	cajgRemesaResolucionBean.setLogGenerado("1");
			
			resolucionAdm.insert(cajgRemesaResolucionBean);
			
			boolean generaLog = false;
			
			generaLog = createZIP(usrBean, String.valueOf(idInstitucion),String.valueOf(idTipoRemesa), idRemesaResolucion, fileN, stream);
	
			if (!generaLog) {
				cajgRemesaResolucionBean.setLogGenerado("0");
				resolucionAdm.updateDirect(cajgRemesaResolucionBean);
			}
			
			if (!prefijo.equals(contadorTablaHashRemesa.get("PREFIJO")) || 
						!sufijo.equals(contadorTablaHashRemesa.get("SUFIJO")) ||
						!numero.equals(siguiente)) {
							String[] datos = new String[]{contadorTablaHashRemesa.get("PREFIJO")+siguiente+contadorTablaHashRemesa.get("SUFIJO")};						
							setMensajeOut(UtilidadesString.getMensaje("message.cajg.distintoNumRegistro", datos, usrBean.getLanguage()));
			}
			
			tx.commit();
			
		} catch (Exception e) {
			
			try {
				if (tx!=null) {
					tx.rollback();
				}
			} catch (Exception el) {
				//el.printStackTrace();
			}
			if (e!=null && e instanceof SIGAException) {
				((SIGAException)e).setParams(new String[] { "modulo.gratuita" });
				throw (SIGAException)e; 
			}
			if (e!=null) {
				SIGAException se = new SIGAException("messages.general.error",e,new String[] { "modulo.gratuita" });
				// RGG Indico que es clsExceptions para  mostrar el codigo de error  
				se.setClsException(true);
				throw se;
			}
			
		}	
		

	}
	
	
	public static File getRutaAlmacenFichero(String idInstitucion, String idRemesaResolucion) {
		ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String rutaAlmacen = rp.returnProperty("cajg.directorioFisicoCAJG") + rp.returnProperty("cajg.directorioCAJGJava");				
		rutaAlmacen += File.separator + idInstitucion + File.separator + rp.returnProperty("cajg.directorioRemesaResoluciones");
		
		File parentFile = new File(rutaAlmacen, idRemesaResolucion);
		deleteFiles(parentFile);			
		parentFile.mkdirs();
		
		return parentFile;
	} 
	
	
	private static void deleteFiles(File parentFile) {
		if (parentFile != null) {
			File[] files = parentFile.listFiles();
			if (files != null) {
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory()) {
						deleteFiles(files[i]);
					}
					files[i].delete();
				}		
			}
		}
	}
	
	/**
	 * 
	 * @param file
	 * @param stream
	 * @throws IOException
	 * @throws ClsExceptions
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	private boolean createZIP(UsrBean usr, String idInstitucion, String idTipoRemesa, String idRemesaResolucion, File file, InputStream stream) throws IOException, ClsExceptions, SIGAException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		
		OutputStream bos = new FileOutputStream(file);
		int bytesRead = 0;
		byte[] buffer = new byte[8192];
		while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
			bos.write(buffer, 0, bytesRead);
		}
		
		stream.close();
		bos.flush();
		bos.close();
		
		//ver si con el tipo de remesa hay alguna clase java para ejecutar
		String sql = "SELECT T.JAVACLASS FROM CAJG_TIPOREMESA T" +
				" WHERE T.IDINSTITUCION = " + idInstitucion +
				" AND T.IDTIPOREMESA = " + idTipoRemesa;
		
		RowsContainer rc = new RowsContainer();
		if (rc.find(sql)) {
			Row row = (Row) rc.get(0);
			String javaClass = row.getString("JAVACLASS");
			if (javaClass != null && !javaClass.trim().equals("")) {
				Class<ResolucionesFicheroAbstract> clase = (Class<ResolucionesFicheroAbstract>) Class.forName(javaClass);
				file = clase.newInstance().execute(idInstitucion, idRemesaResolucion, file);
			}
		}
		
		boolean generaLog = callProcedure(usr, idInstitucion, idTipoRemesa, idRemesaResolucion, file);		
		
		ArrayList ficheros = new ArrayList();
		ficheros.add(file);
		String nombreZip = file.getAbsolutePath();
		nombreZip = nombreZip.substring(0, nombreZip.lastIndexOf("."));
		MasterWords.doZip(ficheros, nombreZip);
		
		return generaLog;
	}
	
	/**
	 * 
	 * @param file
	 * @throws IOException
	 */
	private boolean callProcedure(UsrBean usr, String idInstitucion, String idTipoRemesa, String idRemesaResolucion, File file) throws IOException, SIGAException, ClsExceptions {
		boolean generaLog = false;
		FileInputStream fileInputStream = new FileInputStream(file);
		InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
		Reader reader = new BufferedReader(inputStreamReader);
				
				
		String sql = "SELECT CONSULTA, CABECERA, DELIMITADOR" +
				" FROM CAJG_PROCEDIMIENTOREMESARESOL" +
				" WHERE IDINSTITUCION = " + idInstitucion +
				" AND IDTIPOREMESA = " + idTipoRemesa;
		RowsContainer rc = new RowsContainer();
		if (!rc.find(sql)) {							
			throw new SIGAException("messages.cajg.funcionNoDefinida");
		}

		
		Row row = null;
		String funcion = null;
		String cabecera = null;
		String delimitador = null;
				
		CajgRemesaResolucionFicheroAdm cajgResolucionFicheroAdm = new CajgRemesaResolucionFicheroAdm(usr);
		int idRemesaResolucionFichero = cajgResolucionFicheroAdm.seleccionarMaximo();
		int primerIdRemesaResolucionFichero = idRemesaResolucionFichero;
		
		CajgRemesaResolucionFicheroBean cajgRemesaResolucionFicheroBean = new CajgRemesaResolucionFicheroBean();
		cajgRemesaResolucionFicheroBean.setIdInstitucion(Integer.valueOf(idInstitucion));
		cajgRemesaResolucionFicheroBean.setIdRemesaResolucion(Integer.valueOf(idRemesaResolucion));
		
		int numLinea = 1;			    
	    int ch = -1;
	    
	    String line = "";
	    while((ch = reader.read()) != -1) {
	    	
	    	if (Character.LINE_SEPARATOR == (byte)ch) {
	    		if (line != null && !line.trim().equals("")) {
		    		cajgRemesaResolucionFicheroBean.setNumeroLinea(new Integer(numLinea++));
					cajgRemesaResolucionFicheroBean.setLinea(line);
					cajgRemesaResolucionFicheroBean.setIdRemesaResolucionFichero(new Integer(idRemesaResolucionFichero++));
					cajgResolucionFicheroAdm.insert(cajgRemesaResolucionFicheroBean);
	    		}
				line = "";	 
	    	} else if (Character.LETTER_NUMBER == (byte)ch) {
	    		
	    	} else {
	    		line += (char)ch;
	    	}
	    }
	    
	    if (line != null && !line.trim().equals("")) {
	    	cajgRemesaResolucionFicheroBean.setNumeroLinea(new Integer(numLinea++));
			cajgRemesaResolucionFicheroBean.setLinea(line);
			cajgRemesaResolucionFicheroBean.setIdRemesaResolucionFichero(new Integer(idRemesaResolucionFichero++));
			cajgResolucionFicheroAdm.insert(cajgRemesaResolucionFicheroBean);
	    }
		
		reader.close();
		inputStreamReader.close();
				
		String nombreFichero = file.getName();
		nombreFichero = nombreFichero.substring(0, nombreFichero.lastIndexOf("."));
    	
		for (int j = 0; j < rc.size(); j++) {		
			row = (Row) rc.get(j);
			funcion = row.getString("CONSULTA");
			cabecera = (String) row.getValue("CABECERA");
			delimitador = row.getString("DELIMITADOR");
						
			if (j == 0 && cabecera != null && (cabecera.trim().equals("1") || cabecera.trim().equals("3"))) {
				cajgRemesaResolucionFicheroBean = new CajgRemesaResolucionFicheroBean();
				cajgRemesaResolucionFicheroBean.setIdRemesaResolucionFichero(new Integer(primerIdRemesaResolucionFichero));
				cajgResolucionFicheroAdm.delete(cajgRemesaResolucionFicheroBean);
			}
			
			Object[] param_in = new String[]{idInstitucion, idRemesaResolucion, delimitador, nombreFichero, usr.getUserName()};
	    	
	    	ClsMngBBDD.callPLProcedure("{call " + funcion + " (?,?,?,?,?)}", 0, param_in);	 	
	    	
		}
		
		String consulta = "SELECT E.CODIGO, E.DESCRIPCION" +
				", R." + CajgRemesaResolucionFicheroBean.C_PARAMETROSERROR +
				", R." + CajgRemesaResolucionFicheroBean.C_NUMEROLINEA +
				" FROM " + CajgRemesaResolucionFicheroBean.T_NOMBRETABLA + " R, CAJG_ERRORESREMESARESOL E" +
				" WHERE R." + CajgRemesaResolucionFicheroBean.C_IDERRORESREMESARESOL + " = E.IDERRORESREMESARESOL" +
				" AND R." + CajgRemesaResolucionFicheroBean.C_IDINSTITUCION + " = E.IDINSTITUCION" +
				" AND R." + CajgRemesaResolucionFicheroBean.C_IDINSTITUCION + " = " + idInstitucion + 
				" AND R." + CajgRemesaResolucionFicheroBean.C_IDREMESARESOLUCION + " = " + idRemesaResolucion +
				" ORDER BY R." + CajgRemesaResolucionFicheroBean.C_NUMEROLINEA;
				
		RowsContainer rowsContainer = new RowsContainer();
		rowsContainer.query(consulta);
				
		if (rowsContainer != null && rowsContainer.size() > 0) {
			generaLog = true;
			File logFile = getLogFile(file.getParentFile(), nombreFichero);
						
			FileWriter fileWriter = new FileWriter(logFile);
			BufferedWriter bw = new BufferedWriter(fileWriter);
			String descripcion, parametrosError, codigo, numeroLinea;
			String[] params;
			MessageFormat messageFormat;
			
			for (int i = 0; i < rowsContainer.size(); i++) {				
				row = (Row)rowsContainer.get(i);
				codigo = row.getString("CODIGO");
				descripcion = row.getString("DESCRIPCION");
				parametrosError = row.getString(CajgRemesaResolucionFicheroBean.C_PARAMETROSERROR);
				numeroLinea = row.getString(CajgRemesaResolucionFicheroBean.C_NUMEROLINEA);
				params = parametrosError.split(",");
				messageFormat = new MessageFormat(descripcion);				
				
				bw.write("[Línea:" + numeroLinea + "] " + "[" + codigo + "] " + messageFormat.format(params));
				bw.newLine();
			}
			
			bw.flush();
			bw.close();
		}
		
		if (ELIMINA_DATOS_TABLA_TEMPORAL && cabecera != null && !cabecera.trim().equals("2") && !cabecera.trim().equals("3")) {
			Hashtable hash = new Hashtable();
			hash.put(CajgRemesaResolucionFicheroBean.C_IDINSTITUCION, idInstitucion);
			hash.put(CajgRemesaResolucionFicheroBean.C_IDREMESARESOLUCION, idRemesaResolucion);
			cajgResolucionFicheroAdm.deleteDirect(hash, new String[]{CajgRemesaResolucionFicheroBean.C_IDINSTITUCION, CajgRemesaResolucionFicheroBean.C_IDREMESARESOLUCION});
		}

    	return generaLog;
	}
	
	public static File getLogFile(File parentFile, String nombreFichero) {
		File logFile = new File(parentFile, "log");
		deleteFiles(logFile);
		logFile.mkdirs();
		logFile = new File(logFile, nombreFichero + "_errores.txt");
		return logFile;
	}
	
	
	public UsrBean getUsrBean() {
		return usrBean;
	}

	public void setUsrBean(UsrBean usrBean) {
		this.usrBean = usrBean;
	}

	public int getIdInstitucion() {
		return idInstitucion;
	}

	public void setIdInstitucion(int idInstitucion) {
		this.idInstitucion = idInstitucion;
	}


	public int getIdTipoRemesa() {
		return idTipoRemesa;
	}

	public void setIdTipoRemesa(int idTipoRemesa) {
		this.idTipoRemesa = idTipoRemesa;
	}
	
	public String getIdRemesaResolucion() {
		return idRemesaResolucion;
	}

	public void setIdRemesaResolucion(String idRemesaResolucion) {
		this.idRemesaResolucion = idRemesaResolucion;
	}
	
	
	
	public String getFechaResolucion() {
		return fechaResolucion;
	}

	public void setFechaResolucion(String fechaResolucion) {
		this.fechaResolucion = fechaResolucion;
	}

	public String getFechaCarga() {
		return fechaCarga;
	}

	public void setFechaCarga(String fechaCarga) {
		this.fechaCarga = fechaCarga;
	}
	
	public void setFile(FormFile file) {
		this.file = file;
		
	}
	
	public FormFile getFile() {
		return file;
	}
	
	public String getPrefijo() {
		return prefijo;
	}


	public void setPrefijo(String prefijo) {
		this.prefijo = prefijo;
	}


	public String getSufijo() {
		return sufijo;
	}


	public void setSufijo(String sufijo) {
		this.sufijo = sufijo;
	}


	public String getNumero() {
		return numero;
	}


	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	public String getMensajeOut() {
		return mensajeOut;
	}


	public void setMensajeOut(String mensajeOut) {
		this.mensajeOut = mensajeOut;
	}
	
	public String getTipoEjecucion() {
		return tipoEjecucion;
	}


	public void setTipoEjecucion(String tipoEjecucion) {
		// TODO Auto-generated method stub
		this.tipoEjecucion=tipoEjecucion;
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


	

}
