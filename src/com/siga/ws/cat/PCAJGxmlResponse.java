/**
 * 
 */
package com.siga.ws.cat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ReadProperties;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.siga.Utilidades.SIGAReferences;
import com.siga.beans.CajgEJGRemesaAdm;
import com.siga.beans.CajgEJGRemesaBean;
import com.siga.beans.CajgRemesaEstadosAdm;
import com.siga.beans.CajgRespuestaEJGRemesaAdm;
import com.siga.beans.CajgRespuestaEJGRemesaBean;
import com.siga.beans.ScsEJGAdm;
import com.siga.beans.ScsEJGBean;
import com.siga.ws.SIGAWSClientAbstract;
import com.siga.ws.cat.respuesta.IntercambioDocument;
import com.siga.ws.cat.respuesta.IntercambioDocument.Intercambio;
import com.siga.ws.cat.respuesta.IntercambioDocument.Intercambio.InformacionIntercambio;
import com.siga.ws.cat.respuesta.IntercambioDocument.Intercambio.InformacionIntercambio.IntercambioErroneo;
import com.siga.ws.cat.respuesta.IntercambioDocument.Intercambio.InformacionIntercambio.IntercambioErroneo.DatosError;
import com.siga.ws.cat.respuesta.IntercambioDocument.Intercambio.InformacionIntercambio.IntercambioErroneo.IdentificacionIntercambio;
import com.siga.ws.cat.respuesta.IntercambioDocument.Intercambio.InformacionIntercambio.IntercambioErroneo.DatosError.ErrorContenido;
import com.siga.ws.cat.respuesta.IntercambioDocument.Intercambio.InformacionIntercambio.IntercambioErroneo.DatosError.ErrorGeneral;
import com.siga.ws.cat.respuesta.IntercambioDocument.Intercambio.InformacionIntercambio.IntercambioErroneo.DatosError.ErrorContenido.CodigoExpedienteError;
import com.siga.ws.cat.respuesta.IntercambioDocument.Intercambio.InformacionIntercambio.IntercambioErroneo.DatosError.ErrorContenido.DetalleError;



/**
 * @author angelcpe
 * 
 */
public class PCAJGxmlResponse extends SIGAWSClientAbstract implements PCAJGConstantes {

	private String namespace = IntercambioDocument.type.getProperties()[0].getName().getNamespaceURI();//"rp.cat.ws.siga.com";
	private String HIST = "HIST";
	
	
	/**
	 * 
	 * @param idInstitucion
	 * @param idRemesa
	 * @param fileName
	 * @return
	 */
	private File getRespuestaFile(int idInstitucion, int idRemesa, String fileName) {
		
		ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String rutaAlmacen = rp.returnProperty("cajg.directorioFisicoCAJG") + rp.returnProperty("cajg.directorioCAJGJava");
			
		rutaAlmacen += File.separator + idInstitucion;
		rutaAlmacen += File.separator + idRemesa;
		
		File file = new File(rutaAlmacen + File.separator + rutaOUT);
		file.mkdirs();
		file = new File(file, fileName);
		return file;
	}
	
	
	@Override
	public void execute() throws Exception {
				
		SFTPmanager sftpManager = null;
		
					
		try {
			
			ClsLogging.writeFileLog("Ejecutando la clase " + this.getClass(), 3);					
			String comienzoFicheroIEE_GEN = "IEE_GEN_" + getIdInstitucion() + "_";			
			String comienzoFicheroIR_GEN = "IR_GEN_" + getIdInstitucion() + "_";
			
			sftpManager = new SFTPmanager(getUsrBean(), String.valueOf(getIdInstitucion()));	
			escribeLogRemesa("Conectando con el servidor FTP");
			ChannelSftp chan = sftpManager.connect();	
			
			Vector<LsEntry> vectorFiles = chan.ls(sftpManager.getFtpDirectorioOUT());
			List<File> filesIEE = new ArrayList<File>();			
			List<File> filesIR = new ArrayList<File>();
			
			FileOutputStream fileOutputStream = null;
			
			for (LsEntry lsEntry : vectorFiles) {
				String fileName = lsEntry.getFilename();				
				if (fileName.startsWith(comienzoFicheroIEE_GEN) || fileName.startsWith(comienzoFicheroIR_GEN)){
					
					ClsLogging.writeFileLog("Encontrado fichero " + fileName, 3);
					
					chan.cd(sftpManager.getFtpDirectorioIN());
					if (fileName.startsWith(comienzoFicheroIEE_GEN)) {
						File fileIEE = getRespuestaFile(getIdInstitucion(), getIdRemesa(), fileName);					
						fileOutputStream = new FileOutputStream(fileIEE);
						filesIEE.add(fileIEE);
					} else if (fileName.startsWith(comienzoFicheroIR_GEN)) {
						File fileIR = getRespuestaFile(getIdInstitucion(), getIdRemesa(), fileName);
						fileOutputStream = new FileOutputStream(fileIR);
						filesIR.add(fileIR);
					}
					
					chan.get(sftpManager.getFtpDirectorioOUT() + "/" + fileName, fileOutputStream);
					fileOutputStream.flush();
					fileOutputStream.close();
					//break; no hacemos break pq si esta la respuesta y las resoluciones tratamos primero la respuesta
				}
			}
			
			if (filesIEE.size() > 0 || filesIR.size() > 0) {

				for (File file : filesIEE) {
					procesaIEE(chan, sftpManager.getFtpDirectorioOUT(),  file);//fichero de respuesta					
				}				
			
				for (File file : filesIR) {
					procesaIR(chan, sftpManager.getFtpDirectorioOUT(), file);//fichero de resoluciones					
				}
			} else {
				escribeLogRemesa("No se ha encontrado el fichero en el servidor FTP");
			}
		} catch (JSchException e) {
			escribeLogRemesa("Se ha producido un error de conexión con el servidor FTP");
			ClsLogging.writeFileLogError("Error en el envío FTP", e, 3);		
		} finally {
			if (sftpManager != null) {
				sftpManager.disconnect();
			}
		}
		
			
		
	}
	
	
	/**
	 * 
	 * @param chan
	 * @param dirOUT
	 * @param file
	 */
	private void procesaIR(ChannelSftp chan, String dirOUT, File file) {
		if (file != null) {
					
			try {
			
				escribeLogRemesa("Fichero de resoluciones encontrado en el servidor FTP. Analizando ...");
				ClsLogging.writeFileLog("Fichero copiado en el servidor. Ruta = " + file.getAbsolutePath(), 3);
								
				XmlObject xmlObject = XmlObject.Factory.parse(file);				
				XmlCursor xmlCursor = xmlObject.newCursor();
				
																
				while (xmlCursor.hasNextToken()) {
					String anyoExpediente = null;
					String numExpediente = null;
					String fechaEstado = null;
					String identificadorResolucion = null;
					String codTipoResolucion = null;
					String codMotivoResolucion = null;
					String intervaloIngresosRecursos = null;
					String tipoPrestacion = "";
					
					xmlCursor.toNextToken();
					if (xmlCursor.isContainer()) {
						
						if ("Expediente".equals(xmlCursor.getName().getLocalPart())) {
							System.out.println(xmlCursor.getName().getLocalPart());
							XmlCursor xmlCursorExpediente = xmlCursor.getObject().newCursor();
												
							while (xmlCursorExpediente.hasNextToken()) {
								xmlCursorExpediente.toNextToken();
								if (xmlCursorExpediente.isContainer()) {
									if ("Expediente".equals(xmlCursorExpediente.getName().getLocalPart())) {
										break;
									} else if ("NumExpediente".equals(xmlCursorExpediente.getName().getLocalPart())){
										numExpediente = xmlCursorExpediente.getTextValue();
									} else if ("AnyoExpediente".equals(xmlCursorExpediente.getName().getLocalPart())){
										anyoExpediente = xmlCursorExpediente.getTextValue();
									} else if ("FechaEstado".equals(xmlCursorExpediente.getName().getLocalPart())){
										fechaEstado = xmlCursorExpediente.getTextValue();
									} else if ("IdentificadorResolucion".equals(xmlCursorExpediente.getName().getLocalPart())){
										identificadorResolucion = xmlCursorExpediente.getTextValue();
									} else if ("CodTipoResolucion".equals(xmlCursorExpediente.getName().getLocalPart())){
										codTipoResolucion = xmlCursorExpediente.getTextValue();
									} else if ("CodMotivoResolucion".equals(xmlCursorExpediente.getName().getLocalPart())){
										codMotivoResolucion = xmlCursorExpediente.getTextValue();
									} else if ("IntervaloIngresosRecursos".equals(xmlCursorExpediente.getName().getLocalPart())){
										intervaloIngresosRecursos = xmlCursorExpediente.getTextValue();
									} else if ("CodTipoPrestacion".equals(xmlCursorExpediente.getName().getLocalPart())){
										tipoPrestacion += "\n" + xmlCursorExpediente.getTextValue();
									} else if ("DescTipoPrestacion".equals(xmlCursorExpediente.getName().getLocalPart())){
										tipoPrestacion += " " + xmlCursorExpediente.getTextValue();
									}
									
								}
							}
							actualizaExpediente(anyoExpediente, numExpediente, fechaEstado, identificadorResolucion, codTipoResolucion, codMotivoResolucion, intervaloIngresosRecursos, tipoPrestacion);
						}				
					}
				}	
				
				
				CajgRemesaEstadosAdm cajgRemesaEstadosAdm = new CajgRemesaEstadosAdm(getUsrBean());				
				List<Integer> listaIdRemesas = cajgRemesaEstadosAdm.comprobarRemesaRecibida(getIdInstitucion());
				
				if (listaIdRemesas != null) {
					for (Integer idRemesa : listaIdRemesas) {
						cajgRemesaEstadosAdm.nuevoEstadoRemesa(getUsrBean(), getIdInstitucion(), idRemesa, Integer.valueOf("3"));	
					}
				}
				
				chan.rename(dirOUT + "/" + file.getName(), dirOUT + "/" + HIST + "/" + file.getName());
				
			} catch (Exception e) {
								
				try {
					escribeLogRemesa("Se ha producido un error al tratar la resolución");
				} catch (IOException e1) {
					ClsLogging.writeFileLogError("Se ha producido un error al escribir en el log de la remesa " + getIdRemesa(), e1, 3);
				}
				ClsLogging.writeFileLogError("Se ha producido un error al tratar la resolución en la remesa " + getIdRemesa(), e, 3);
			}
		}
	}
	
	/**
	 * 
	 * @param anyoExpediente
	 * @param numExpediente
	 * @param fechaEstado
	 * @param identificadorResolucion
	 * @param codTipoResolucion
	 * @param codMotivoResolucion
	 * @param intervaloIngresosRecursos
	 * @param tipoPrestacion
	 * @throws Exception
	 */
	private void actualizaExpediente(String anyoExpediente, String numExpediente, String fechaEstado, String identificadorResolucion, String codTipoResolucion, String codMotivoResolucion, String intervaloIngresosRecursos, String tipoPrestacion) throws Exception {		
				
		ScsEJGAdm scsEJGAdm = new ScsEJGAdm(getUsrBean());
		CajgEJGRemesaAdm cajgEJGRemesaAdm = new CajgEJGRemesaAdm(getUsrBean());
		CajgEJGRemesaBean cajgEJGRemesaBean = new CajgEJGRemesaBean();
		String[] claves = new String[]{CajgEJGRemesaBean.C_IDINSTITUCION, CajgEJGRemesaBean.C_ANIO, CajgEJGRemesaBean.C_NUMERO, CajgEJGRemesaBean.C_IDTIPOEJG};
		String[] campos = new String[]{CajgEJGRemesaBean.C_RECIBIDA};
			
		if (numExpediente != null && numExpediente.trim().length() > 5) {
			numExpediente = numExpediente.trim();
			numExpediente = numExpediente.substring(numExpediente.length() - 5);
		}
		
		Hashtable<String, Object> hash = new Hashtable<String, Object>();
		hash.put(ScsEJGBean.C_IDINSTITUCION, getIdInstitucion());
		hash.put(ScsEJGBean.C_ANIO, String.valueOf(anyoExpediente));
		hash.put(ScsEJGBean.C_NUMEJG, numExpediente);							
		Vector vectorEJGs = scsEJGAdm.select(hash);
		if (vectorEJGs != null) {
			if (vectorEJGs.size() == 0) {
				//EJG no encontrado
				escribeLogRemesa("No se ha encontrado el EJG año/número = " + anyoExpediente + "/" + numExpediente);
			} else if (vectorEJGs.size() > 1) {
				//se ha encontrado mas de un EJG
				escribeLogRemesa("Se ha encontrado más de un EJG con el año/número = " + anyoExpediente + "/" + numExpediente);
			} else {
				//EJG encontrado
				ScsEJGBean scsEJGBean = (ScsEJGBean) vectorEJGs.get(0);	
				
				/* NO COMPROBAMOS QUE SEA DE MI REMESA
				Hashtable<String, Object> hashEjgRem = new Hashtable<String, Object>();
				hashEjgRem.put(CajgEJGRemesaBean.C_IDINSTITUCION, getIdInstitucion());
				hashEjgRem.put(CajgEJGRemesaBean.C_ANIO, scsEJGBean.getAnio());
				hashEjgRem.put(CajgEJGRemesaBean.C_NUMERO, scsEJGBean.getNumero());
				hashEjgRem.put(CajgEJGRemesaBean.C_IDTIPOEJG, scsEJGBean.getIdTipoEJG());
				
				hashEjgRem.put(CajgEJGRemesaBean.C_IDINSTITUCIONREMESA, getIdInstitucion());
				hashEjgRem.put(CajgEJGRemesaBean.C_IDREMESA, getIdRemesa());
				CajgEJGRemesaAdm cajgEJGRemesaAdm = new CajgEJGRemesaAdm(getUsrBean());
				
				Vector vectorRemesa = cajgEJGRemesaAdm.select(hashEjgRem);
				if (vectorRemesa.size() == 0) {
					escribeLogRemesa("No se ha encontrado el EJG año/número = " + anyoExpediente + "/" + numExpediente + " en la remesa ");										
				} else if (vectorRemesa.size() > 1) {
					escribeLogRemesa("Se ha encontrado más de un EJG año/número = " + anyoExpediente + "/" + numExpediente + " en la remesa ");
				} else {	*/				
					
					//El estado lo pone el trigger como resuelto
					String observaciones = intervaloIngresosRecursos;
					observaciones = observaciones==null?"":("Intervalo ingresos recursos: " + observaciones);
					
					if (tipoPrestacion != null){
						observaciones += "\nPrestaciones:";
						observaciones += tipoPrestacion;
					}
					
					String update = "UPDATE " + ScsEJGBean.T_NOMBRETABLA + " SET " +
							ScsEJGBean.C_IDTIPORATIFICACIONEJG + " = (SELECT PS.IDTIPORESOLUCION" +
											" FROM PCAJG_TIPO_RESOLUCION P, PCAJG_TIPO_RESOLUCION_SCSTIPOR PS" +
											" WHERE P.IDINSTITUCION = PS.IDINSTITUCION" +
											" AND P.IDENTIFICADOR = PS.IDENTIFICADOR" +
											" AND P.IDINSTITUCION = " + getIdInstitucion() +
											" AND P.CODIGO = '" + codTipoResolucion + "')" +  //tipoResolucion
							", " + ScsEJGBean.C_IDFUNDAMENTOJURIDICO + " = (SELECT T.IDFUNDAMENTO" +
									" FROM SCS_TIPOFUNDAMENTOS T" +
									" WHERE T.IDINSTITUCION = " + getIdInstitucion() +
									" AND T.CODIGO = '" + codMotivoResolucion + "')" + //motivoResolucion
							", " + ScsEJGBean.C_FECHARESOLUCIONCAJG + " = TO_DATE('" + fechaEstado + "', 'YYYY-MM-DD')" +
							", " + ScsEJGBean.C_REFAUTO + " = '" + (identificadorResolucion==null?"":identificadorResolucion) + "'" +
							", " + ScsEJGBean.C_RATIFICACIONDICTAMEN + " = '" + observaciones + "'" +
							" WHERE " + ScsEJGBean.C_IDINSTITUCION + " = " + getIdInstitucion() +
					        " AND " + ScsEJGBean.C_ANIO + " = " + scsEJGBean.getAnio() +
					        " AND " + ScsEJGBean.C_NUMERO + " = " + scsEJGBean.getNumero() +
					        " AND " + ScsEJGBean.C_IDTIPOEJG + " = " + scsEJGBean.getIdTipoEJG();							           
					
					scsEJGAdm.updateSQL(update);
					
					cajgEJGRemesaBean.setIdInstitucion(scsEJGBean.getIdInstitucion());
					cajgEJGRemesaBean.setAnio(scsEJGBean.getAnio());
					cajgEJGRemesaBean.setNumero(scsEJGBean.getNumero());
					cajgEJGRemesaBean.setIdTipoEJG(scsEJGBean.getIdTipoEJG());
					cajgEJGRemesaBean.setRecibida(Integer.valueOf(1));
					
					cajgEJGRemesaAdm.updateDirect(cajgEJGRemesaBean, claves, campos);
				
			}
		}
	
		
	}


	/**
	 * 
	 * @param chan 
	 * @param string 
	 * @param file
	 * @throws ClsExceptions
	 */
	private void procesaIEE(ChannelSftp chan, String dirOUT, File file) {
		try {			
			
			if (file != null) {	
				escribeLogRemesa("Fichero de respuesta encontrado en el servidor FTP. Analizando respuesta.");
				ClsLogging.writeFileLog("Fichero copiado en el servidor. Ruta = " + file.getAbsolutePath(), 3);
				XmlOptions xmlOptions = new XmlOptions();
				Map<String, String> map = new HashMap<String, String>();
				map.put("", namespace);
				xmlOptions.setLoadSubstituteNamespaces(map);
				
				
				
				IntercambioDocument intercambioRespuestaDoc = IntercambioDocument.Factory.parse(file, xmlOptions);
				
				Intercambio intercambioRespuesta = intercambioRespuestaDoc.getIntercambio();
				InformacionIntercambio informacionIntercambio = intercambioRespuesta.getInformacionIntercambio();
				IntercambioErroneo intercambioErroneo = informacionIntercambio.getIntercambioErroneo();
				
				DatosError[] datosErrors = intercambioErroneo.getDatosErrorArray();
				IdentificacionIntercambio identificacionIntercambio = intercambioErroneo.getIdentificacionIntercambio();
				String idInstitucion = identificacionIntercambio.getCodOrigenIntercambio().getDomNode().getNodeValue();
				int idRemesa = Integer.parseInt(identificacionIntercambio.getIdentificadorIntercambio().getDomNode().getNodeValue());
				
				if (!String.valueOf(getIdInstitucion()).equals(idInstitucion)) {
					escribeLogRemesa("La institucion del fichero es nula o distinta a la del usuario de SIGA");
					throw new ClsExceptions("La institucion del fichero es distinta a la del usuario de SIGA");
				}
				
				if (getIdRemesa() != idRemesa) {
					//si es distinta remesa ya lo tratará su remesa
					return;
//					escribeLogRemesa("La remesa del fichero es distinta a la del usuario de SIGA");
//					throw new ClsExceptions("La remesa del fichero es distinta a la del usuario de SIGA");
				}
							
			    
				
				CajgEJGRemesaAdm cajgEJGRemesaAdm = new CajgEJGRemesaAdm(getUsrBean());
				ScsEJGAdm scsEJGAdm = new ScsEJGAdm(getUsrBean());
				CajgRespuestaEJGRemesaAdm cajgRespuestaEJGRemesaAdm = new CajgRespuestaEJGRemesaAdm(getUsrBean());
				
				chan.rename(dirOUT + "/" + file.getName(), dirOUT + "/" + HIST + "/" + file.getName());
				
				for (DatosError datosError : datosErrors) {
					
					ErrorGeneral errorGeneral = datosError.getErrorGeneral();
					
					if (errorGeneral != null) {
						escribeLogRemesa(errorGeneral.getDescErrorGen().toString());					
					} else {
						ErrorContenido[] erroresContenido = datosError.getErrorContenidoArray();
						CodigoExpedienteError expedientError = null;
						for (ErrorContenido errorContenido : erroresContenido){
							expedientError = errorContenido.getCodigoExpedienteError();
							int anioExp = expedientError.getAnyoExpediente();
							String numExp = expedientError.getNumExpediente();
							if (numExp != null && numExp.trim().length() > 5) {
								numExp = numExp.trim();
								numExp = numExp.substring(numExp.length() - 5);
							}
							
							Hashtable<String, Object> hash = new Hashtable<String, Object>();
							hash.put(ScsEJGBean.C_IDINSTITUCION, idInstitucion);
							hash.put(ScsEJGBean.C_ANIO, String.valueOf(anioExp));
							hash.put(ScsEJGBean.C_NUMEJG, numExp);							
							Vector vectorEJGs = scsEJGAdm.select(hash);
							if (vectorEJGs != null) {
								if (vectorEJGs.size() == 0) {
									//EJG no encontrado
									escribeLogRemesa("No se ha encontrado el EJG año/número = " + anioExp + "/" + numExp);
								} else if (vectorEJGs.size() > 1) {
									//se ha encontrado mas de un EJG
									escribeLogRemesa("Se ha encontrado más de un EJG con el año/número = " + anioExp + "/" + numExp);
								} else {
									//EJG encontrado
									ScsEJGBean scsEJGBean = (ScsEJGBean) vectorEJGs.get(0);
									DetalleError[] detallErrors = errorContenido.getDetalleErrorArray();
	
									Hashtable<String, Object> hashEjgRem = new Hashtable<String, Object>();
									hashEjgRem.put(CajgEJGRemesaBean.C_IDINSTITUCION, idInstitucion);
									hashEjgRem.put(CajgEJGRemesaBean.C_ANIO, scsEJGBean.getAnio());
									hashEjgRem.put(CajgEJGRemesaBean.C_NUMERO, scsEJGBean.getNumero());
									hashEjgRem.put(CajgEJGRemesaBean.C_IDTIPOEJG, scsEJGBean.getIdTipoEJG());
									
									hashEjgRem.put(CajgEJGRemesaBean.C_IDINSTITUCIONREMESA, idInstitucion);
									hashEjgRem.put(CajgEJGRemesaBean.C_IDREMESA, idRemesa);
									
									Vector vectorRemesa = cajgEJGRemesaAdm.select(hashEjgRem);
									if (vectorRemesa.size() == 0) {
										escribeLogRemesa("No se ha encontrado el EJG año/número = " + anioExp + "/" + numExp + " en la remesa " + idRemesa);										
									} else if (vectorRemesa.size() > 1) {
										escribeLogRemesa("Se ha encontrado más de un EJG año/número = " + anioExp + "/" + numExp + " en la remesa " + idRemesa);
									} else {									
										CajgEJGRemesaBean cajgEJGRemesaBean = (CajgEJGRemesaBean) vectorRemesa.get(0);
										
										for (DetalleError detallError : detallErrors) {
											StringBuffer descripcion = new StringBuffer("<b>Campo:</b> " + detallError.getCampoError());
											if (detallError.getDescError() != null) {
												descripcion.append("<br>");
												descripcion.append("<b>Error:</b> " + detallError.getDescError());
											}
//											if (detallError.getAmpliacionError() != null) {
//												descripcion.append("<br>");
//												descripcion.append("<b>Más información:</b> " + detallError.getAmpliacionError());
//											}
											CajgRespuestaEJGRemesaBean cajgRespuestaEJGRemesaBean = new CajgRespuestaEJGRemesaBean();											
											cajgRespuestaEJGRemesaBean.setIdEjgRemesa(cajgEJGRemesaBean.getIdEjgRemesa());
											cajgRespuestaEJGRemesaBean.setCodigo("-1");
											cajgRespuestaEJGRemesaBean.setDescripcion(descripcion.toString());
											cajgRespuestaEJGRemesaBean.setFecha("SYSDATE");
											
											
											cajgRespuestaEJGRemesaAdm.insert(cajgRespuestaEJGRemesaBean);
										}
									}
								}
							}
						}
					}
					
				}
				
				
				
			} else {
				escribeLogRemesa("No se ha encontrado el fichero de respuesta");
				ClsLogging.writeFileLog("No se ha encontrado respuesta para institucion/remesa = " + getIdInstitucion() + "/" + getIdRemesa(), 3);
			}

		} catch (Exception e) {	
			if (file != null) {
				file.delete();
			}
			try {
				escribeLogRemesa("Se ha producido un error al tratar la respuesta");
			} catch (IOException e1) {
				ClsLogging.writeFileLogError("Se ha producido un error al escribir en el log de la remesa " + getIdRemesa(), e1, 3);
			}
			ClsLogging.writeFileLogError("Se ha producido un error al tratar la respuesta en la remesa " + getIdRemesa(), e, 3);			
		} finally {
			
		}
	}
	

	/**
	 * @param args
	 * @throws Exception 
	 * @throws ClsExceptions 
	 */
	public static void main2(String[] args) throws Exception {
		JSch jsch = new JSch();
		com.jcraft.jsch.Session session = null;
		ChannelSftp chan = null;		
		
		session = jsch.getSession("weblogic", "192.168.11.50", 22);
		
		session.setPassword("weblogic");
		
		// El SFTP requiere un intercambio de claves
		// con esta propiedad le decimos que acepte la clave
		// sin pedir confirmación
		Properties prop = new Properties();
		prop.put("StrictHostKeyChecking", "no");
		session.setConfig(prop);
		session.connect();

		// Abrimos el canal de sftp y conectamos
		chan = (ChannelSftp) session.openChannel("sftp");
		chan.connect();
		
		chan.cd("/tmp");
		
		Vector<LsEntry> vectorFiles = chan.ls("/tmp");
		for (LsEntry lsEntry : vectorFiles) {
			String fileName = lsEntry.getFilename();
			System.out.println(fileName);
			if (fileName.startsWith("IEE_GEN_2047")){
				System.out.println(fileName);
			}
		}
		chan.disconnect();
		session.disconnect();
		System.out.println();
	}

	public static void main (String[] args) throws XmlException, IOException {
		File file = new File("C:\\proyectos\\CAJG\\CATALUÑA\\IR_GEN_2047_2_20091112_Prueba.xml");
		XmlObject xmlObject = XmlObject.Factory.parse(file);
		
		XmlCursor xmlCursor = xmlObject.newCursor();
		
		getDatos(xmlCursor, "Expediente");
		
		
		
		
		
		
//		xmlObjects = xmlObject.selectChildren(DatosExpediente.type.getDocumentElementName());
		/*for (XmlObject xmlo : xmlObjects) {
			if (xmlo instanceof DatosExpediente) {
				System.out.println("");
			}
			
		}*/
	}


	private static void getDatos(XmlCursor xmlCursor, String string) {
		
		while (xmlCursor.hasNextToken()) {
			xmlCursor.toNextToken();
			if (xmlCursor.isContainer()) {
				
				if ("Expediente".equals(xmlCursor.getName().getLocalPart())) {
					System.out.println(xmlCursor.getName().getLocalPart());
					XmlCursor xmlCursorExpediente = xmlCursor.getObject().newCursor();
										
					while (xmlCursorExpediente.hasNextToken()) {
						xmlCursorExpediente.toNextToken();
						if (xmlCursorExpediente.isContainer()) {
							if ("Expediente".equals(xmlCursorExpediente.getName().getLocalPart())) {
								break;
							} else if ("NumExpediente".equals(xmlCursorExpediente.getName().getLocalPart())){
								System.out.println(xmlCursorExpediente.getTextValue());
							} else if ("AnyoExpediente".equals(xmlCursorExpediente.getName().getLocalPart())){
								System.out.println(xmlCursorExpediente.getTextValue());
							} else if ("FechaEstado".equals(xmlCursorExpediente.getName().getLocalPart())){
								System.out.println(xmlCursorExpediente.getTextValue());
							}
						}
					}
					
				}				
			}
		}
	}

}
