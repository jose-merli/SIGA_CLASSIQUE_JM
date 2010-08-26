/**
 * 
 */
package com.siga.ws.cat;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.transaction.UserTransaction;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ReadProperties;
import com.jcraft.jsch.JSchException;
import com.siga.Utilidades.SIGAReferences;
import com.siga.beans.CajgEJGRemesaAdm;
import com.siga.beans.CajgEJGRemesaBean;
import com.siga.beans.CajgRemesaEstadosAdm;
import com.siga.beans.CajgRespuestaEJGRemesaAdm;
import com.siga.beans.CajgRespuestaEJGRemesaBean;
import com.siga.beans.ScsEJGAdm;
import com.siga.beans.ScsEJGBean;
import com.siga.ws.PCAJGConstantes;
import com.siga.ws.SIGAWSClientAbstract;
import com.siga.ws.cat.ftp.FtpPcajgAbstract;
import com.siga.ws.cat.ftp.FtpPcajgFactory;
import com.siga.ws.cat.respuesta.IntercambioDocument;
import com.siga.ws.cat.respuesta.TipoIdentificacionIntercambio;
import com.siga.ws.cat.respuesta.IntercambioDocument.Intercambio;
import com.siga.ws.cat.respuesta.IntercambioDocument.Intercambio.InformacionIntercambio;
import com.siga.ws.cat.respuesta.IntercambioDocument.Intercambio.InformacionIntercambio.IntercambioErroneo;
import com.siga.ws.cat.respuesta.IntercambioDocument.Intercambio.InformacionIntercambio.TipoGenerico;
import com.siga.ws.cat.respuesta.IntercambioDocument.Intercambio.InformacionIntercambio.IntercambioErroneo.DatosError;
import com.siga.ws.cat.respuesta.IntercambioDocument.Intercambio.InformacionIntercambio.IntercambioErroneo.DatosError.ErrorContenido;
import com.siga.ws.cat.respuesta.IntercambioDocument.Intercambio.InformacionIntercambio.IntercambioErroneo.DatosError.ErrorGeneral;
import com.siga.ws.cat.respuesta.IntercambioDocument.Intercambio.InformacionIntercambio.IntercambioErroneo.DatosError.ErrorContenido.CodigoExpedienteError;
import com.siga.ws.cat.respuesta.IntercambioDocument.Intercambio.InformacionIntercambio.IntercambioErroneo.DatosError.ErrorContenido.DetalleError;
import com.siga.ws.cat.respuesta.IntercambioDocument.Intercambio.InformacionIntercambio.TipoGenerico.Expediente;
import com.siga.ws.cat.respuesta.IntercambioDocument.Intercambio.InformacionIntercambio.TipoGenerico.Expediente.DatosExpediente;
import com.siga.ws.cat.respuesta.IntercambioDocument.Intercambio.InformacionIntercambio.TipoGenerico.Expediente.DatosTramitacionExpediente.TramiteResolucion;
import com.siga.ws.cat.respuesta.IntercambioDocument.Intercambio.InformacionIntercambio.TipoGenerico.Expediente.DatosTramitacionExpediente.TramiteResolucion.PrestacionesResolucion;




/**
 * @author angelcpe
 * 
 */
public class PCAJGxmlResponse extends SIGAWSClientAbstract implements PCAJGConstantes {

	private String namespace = IntercambioDocument.type.getProperties()[0].getName().getNamespaceURI();//"rp.cat.ws.siga.com";
		
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
			
		FtpPcajgAbstract ftpPcajgAbstract = null;
							
		try {
			
			ClsLogging.writeFileLog("Ejecutando la clase " + this.getClass(), 3);					
			String comienzoFicheroIEE_GEN = "IEE_GEN_" + getIdInstitucion() + "_";			
			String comienzoFicheroIR_GEN = "IR_GEN_" + getIdInstitucion() + "_";
			
			ftpPcajgAbstract = FtpPcajgFactory.getInstance(getUsrBean(), String.valueOf(getIdInstitucion()));
			escribeLogRemesa("Conectando con el servidor FTP");
			ftpPcajgAbstract.connect();	
			
			List<String> vectorFiles = ftpPcajgAbstract.ls();
			List<File> filesIEE = new ArrayList<File>();			
			List<File> filesIR = new ArrayList<File>();
			
			FileOutputStream fileOutputStream = null;
			
			for (String fileName : vectorFiles) {								
				if (fileName.startsWith(comienzoFicheroIEE_GEN) || fileName.startsWith(comienzoFicheroIR_GEN)){					
					ClsLogging.writeFileLog("Encontrado fichero " + fileName, 3);					
					
					if (fileName.startsWith(comienzoFicheroIEE_GEN)) {
						File fileIEE = getRespuestaFile(getIdInstitucion(), getIdRemesa(), fileName);					
						fileOutputStream = new FileOutputStream(fileIEE);
						filesIEE.add(fileIEE);
					} else if (fileName.startsWith(comienzoFicheroIR_GEN)) {
						File fileIR = getRespuestaFile(getIdInstitucion(), getIdRemesa(), fileName);
						fileOutputStream = new FileOutputStream(fileIR);
						filesIR.add(fileIR);
					}
					
					ftpPcajgAbstract.download(fileName, fileOutputStream);
					fileOutputStream.flush();
					fileOutputStream.close();
					//break; no hacemos break pq si esta la respuesta y las resoluciones tratamos primero la respuesta
				}
			}
			
			if (filesIEE.size() > 0 || filesIR.size() > 0) {

				for (File file : filesIEE) {
					procesaIEE(ftpPcajgAbstract,  file);//fichero de respuesta					
				}				
			
				for (File file : filesIR) {
					procesaIR(ftpPcajgAbstract, file);//fichero de resoluciones					
				}
			} else {
				escribeLogRemesa("No se ha encontrado el fichero en el servidor FTP");
			}
		} catch (JSchException e) {
			escribeLogRemesa("Se ha producido un error de conexión con el servidor FTP");
			ClsLogging.writeFileLogError("Error en el envío FTP", e, 3);		
		} finally {
			if (ftpPcajgAbstract != null) {
				ftpPcajgAbstract.disconnect();
			}
		}
		
			
		
	}
	
	/**
	 * Ejecuta las resoluciones de su mismo colegio, independientemente de la remesa que sea
	 * @param chan
	 * @param dirOUT
	 * @param file
	 */
	private void procesaIR(FtpPcajgAbstract ftpPcajgAbstract, File file) {
		
		UserTransaction tx = null;
		
		if (file != null) {					
			try {			
				
				ClsLogging.writeFileLog("Fichero copiado en el servidor. Ruta = " + file.getAbsolutePath(), 3);
							
				XmlOptions xmlOptions = new XmlOptions();
				Map<String, String> map = new HashMap<String, String>();
				map.put("", namespace);
				xmlOptions.setLoadSubstituteNamespaces(map);
				
				IntercambioDocument intercambioIRDoc = IntercambioDocument.Factory.parse(file, xmlOptions);
				if (validate(intercambioIRDoc).size() > 0) {
					throw new ClsExceptions("El xml " + file.getName() + " no es válido");
				}			
				
				InformacionIntercambio informacionIntercambio = intercambioIRDoc.getIntercambio().getInformacionIntercambio();
				
				String idInstitucion = informacionIntercambio.getIdentificacionIntercambio().getCodDestinoIntercambio();
				TipoGenerico tipoGenerico = informacionIntercambio.getTipoGenerico();
				if (tipoGenerico == null) {
					throw new ClsExceptions("El nodo " + TipoGenerico.class.getSimpleName() + " no puede ser null");
				}			
				
				Expediente[] expedientes = tipoGenerico.getExpedienteArray();
				
				int anyoExpediente = -1;
				String numExpediente = null;
				Calendar fechaEstado = null;
				String identificadorResolucion = null;
				String codTipoResolucion = null;
				String codMotivoResolucion = null;
				String intervaloIngresosRecursos = null;
				StringBuffer tipoPrestacion = null;
				
				tx = getUsrBean().getTransaction();
				tx.begin();
				
				for (Expediente expediente : expedientes) {					
					tipoPrestacion = new StringBuffer();
					
					DatosExpediente datosExpediente = expediente.getDatosExpediente();
					numExpediente = datosExpediente.getCodigoExpediente().getNumExpediente();
					anyoExpediente = datosExpediente.getCodigoExpediente().getAnyoExpediente();
					
					TramiteResolucion tramiteResolucion = expediente.getDatosTramitacionExpediente().getTramiteResolucion();
					fechaEstado = tramiteResolucion.getIdentificacionTramite().getFechaEstado();
					identificadorResolucion = tramiteResolucion.getIdentificadorResolucion();
					codTipoResolucion = tramiteResolucion.getCodTipoResolucion();
					codMotivoResolucion = tramiteResolucion.getCodMotivoResolucion();
					intervaloIngresosRecursos = tramiteResolucion.getIntervaloIngresosRecursos();
					
					for (PrestacionesResolucion prestacionesResolucion : tramiteResolucion.getPrestacionesResolucionArray()) {
						tipoPrestacion.append("\n" + prestacionesResolucion.getCodTipoPrestacion());
						tipoPrestacion.append(" " + prestacionesResolucion.getDescTipoPrestacion());
					}
					actualizaExpediente(idInstitucion, anyoExpediente, numExpediente, fechaEstado, identificadorResolucion, codTipoResolucion, codMotivoResolucion, intervaloIngresosRecursos, tipoPrestacion.toString());
				}
						
				
				CajgRemesaEstadosAdm cajgRemesaEstadosAdm = new CajgRemesaEstadosAdm(getUsrBean());				
				List<Integer> listaIdRemesas = cajgRemesaEstadosAdm.comprobarRemesaRecibida(Integer.parseInt(idInstitucion));
				
				if (listaIdRemesas != null) {
					for (Integer idRemesa : listaIdRemesas) {
						cajgRemesaEstadosAdm.nuevoEstadoRemesa(getUsrBean(), getIdInstitucion(), idRemesa, Integer.valueOf("3"));	
					}
				}
				
				ftpPcajgAbstract.moveToHIST(file.getName());
				
				tx.commit();
				
			} catch (Exception e) {
						
				
				try {
					if (tx != null) {
						tx.rollback();
					}
					escribeLogRemesa("Se ha producido un error al tratar la resolución");
				} catch (Exception e1) {
					ClsLogging.writeFileLogError("Se ha producido un error al tratar las resoluciones ", e1, 3);
				}
				ClsLogging.writeFileLogError("Se ha producido un error al tratar la resolución", e, 3);
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
	private void actualizaExpediente(String idInstitucion, int anyoExpediente, String numExpediente, Calendar fechaEstadoCalendar, String identificadorResolucion, String codTipoResolucion, String codMotivoResolucion, String intervaloIngresosRecursos, String tipoPrestacion) throws Exception {		
				
		ScsEJGAdm scsEJGAdm = new ScsEJGAdm(getUsrBean());
		CajgEJGRemesaAdm cajgEJGRemesaAdm = new CajgEJGRemesaAdm(getUsrBean());
		CajgEJGRemesaBean cajgEJGRemesaBean = new CajgEJGRemesaBean();
		String[] claves = new String[]{CajgEJGRemesaBean.C_IDINSTITUCION, CajgEJGRemesaBean.C_ANIO, CajgEJGRemesaBean.C_NUMERO, CajgEJGRemesaBean.C_IDTIPOEJG};
		String[] campos = new String[]{CajgEJGRemesaBean.C_RECIBIDA};
		
		Vector vectorEJGs = scsEJGAdm.select(" WHERE " + ScsEJGBean.C_IDINSTITUCION + " = " + idInstitucion +
											" AND " + ScsEJGBean.C_ANIO   + " = " + anyoExpediente +
											" AND TO_NUMBER(" +  ScsEJGBean.C_NUMEJG + ") = " + Integer.parseInt(numExpediente.trim()));
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String fechaEstado = sdf.format(fechaEstadoCalendar.getTime());
		
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
				
				//El estado lo pone el trigger como resuelto
				String observaciones = intervaloIngresosRecursos;
				observaciones = observaciones==null?"":("Intervalo ingresos recursos: " + observaciones);
				
				if (tipoPrestacion != null && !tipoPrestacion.trim().equals("")){
					observaciones += "\nPrestaciones:";
					observaciones += tipoPrestacion;
				}
				
				String update = "UPDATE " + ScsEJGBean.T_NOMBRETABLA + " SET " +
						ScsEJGBean.C_IDTIPORATIFICACIONEJG + " = (SELECT PS.IDTIPORESOLUCION" +
										" FROM PCAJG_TIPO_RESOLUCION P, PCAJG_TIPO_RESOLUCION_SCSTIPOR PS" +
										" WHERE P.IDINSTITUCION = PS.IDINSTITUCION" +
										" AND P.IDENTIFICADOR = PS.IDENTIFICADOR" +
										" AND P.IDINSTITUCION = " + idInstitucion +
										" AND P.CODIGO = '" + codTipoResolucion + "')" +  //tipoResolucion
						", " + ScsEJGBean.C_IDFUNDAMENTOJURIDICO + " = (SELECT T.IDFUNDAMENTO" +
								" FROM SCS_TIPOFUNDAMENTOS T" +
								" WHERE T.IDINSTITUCION = " + idInstitucion +
								" AND T.CODIGO = '" + codMotivoResolucion + "')" + //motivoResolucion
						", " + ScsEJGBean.C_FECHARESOLUCIONCAJG + " = TO_DATE('" + fechaEstado + "', 'YYYY-MM-DD')" +
						", " + ScsEJGBean.C_REFAUTO + " = '" + (identificadorResolucion==null?"":identificadorResolucion) + "'" +
						", " + ScsEJGBean.C_RATIFICACIONDICTAMEN + " = '" + observaciones + "'" +
						" WHERE " + ScsEJGBean.C_IDINSTITUCION + " = " + idInstitucion +
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
	private void procesaIEE(FtpPcajgAbstract ftpPcajgAbstract, File file) {
		UserTransaction tx = null;
		
		try {			
			
			if (file != null) {	
				
				ClsLogging.writeFileLog("Fichero copiado en el servidor. Ruta = " + file.getAbsolutePath(), 3);
				XmlOptions xmlOptions = new XmlOptions();
				Map<String, String> map = new HashMap<String, String>();
				map.put("", namespace);
				xmlOptions.setLoadSubstituteNamespaces(map);
				IntercambioDocument intercambioRespuestaDoc = null;
				try {
					intercambioRespuestaDoc = IntercambioDocument.Factory.parse(file, xmlOptions);
				} catch (XmlException e) {
					escribeLogRemesa("El xml " + file.getName() + " no es un fichero de \"Intercambio\" válido.");
					throw e;
				}	
				if (validate(intercambioRespuestaDoc).size() > 0) {
					throw new ClsExceptions("El xml " + file.getName() + " no es válido");
				}			
				
				Intercambio intercambioRespuesta = intercambioRespuestaDoc.getIntercambio();
				InformacionIntercambio informacionIntercambio = intercambioRespuesta.getInformacionIntercambio();
				IntercambioErroneo intercambioErroneo = informacionIntercambio.getIntercambioErroneo();
				
				DatosError[] datosErrors = intercambioErroneo.getDatosErrorArray();
				TipoIdentificacionIntercambio identificacionIntercambio = intercambioErroneo.getIdentificacionIntercambio();
				String idInstitucion = identificacionIntercambio.getCodOrigenIntercambio();
				int idRemesa = (int)identificacionIntercambio.getIdentificadorIntercambio();
				
				if (!String.valueOf(getIdInstitucion()).equals(idInstitucion)) {
					escribeLogRemesa("La institucion del fichero es nula o distinta a la del usuario de SIGA");
					throw new ClsExceptions("La institucion del fichero es distinta a la del usuario de SIGA");
				}
				
				if (getIdRemesa() != idRemesa/10) {
					file.delete();
					//si es distinta remesa ya lo tratará su remesa
					return;
//					escribeLogRemesa("La remesa del fichero es distinta a la del usuario de SIGA");
//					throw new ClsExceptions("La remesa del fichero es distinta a la del usuario de SIGA");
				}
				
				escribeLogRemesa("Fichero de respuesta " + file.getName() + " encontrado en el servidor FTP. Analizando respuesta.");
				
				CajgEJGRemesaAdm cajgEJGRemesaAdm = new CajgEJGRemesaAdm(getUsrBean());
				ScsEJGAdm scsEJGAdm = new ScsEJGAdm(getUsrBean());
				CajgRespuestaEJGRemesaAdm cajgRespuestaEJGRemesaAdm = new CajgRespuestaEJGRemesaAdm(getUsrBean());
				
				tx = getUsrBean().getTransaction();
				tx.begin();
				
				//cajgRespuestaEJGRemesaAdm.eliminaAnterioresErrores(getIdInstitucion(), getIdRemesa());
				
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
								
							String where = " WHERE " + ScsEJGBean.C_IDINSTITUCION + " = " + idInstitucion +
								" AND " + ScsEJGBean.C_ANIO + " = " + String.valueOf(anioExp) +
								" AND TO_NUMBER(" +  ScsEJGBean.C_NUMEJG + ") = " + Integer.parseInt(numExp.trim());
							
							Vector vectorEJGs = scsEJGAdm.select(where);
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
									hashEjgRem.put(CajgEJGRemesaBean.C_IDREMESA, getIdRemesa());
									
									Vector vectorRemesa = cajgEJGRemesaAdm.select(hashEjgRem);
									if (vectorRemesa.size() == 0) {
										escribeLogRemesa("No se ha encontrado el EJG año/número = " + anioExp + "/" + numExp + " en la remesa");										
									} else if (vectorRemesa.size() > 1) {
										escribeLogRemesa("Se ha encontrado más de un EJG año/número = " + anioExp + "/" + numExp + " en la remesa");
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
				
				ClsLogging.writeFileLog("Moviendo fichero a la carpeta de histórico", 3);
				ftpPcajgAbstract.moveToHIST(file.getName());
				
				tx.commit();
			} else {
				escribeLogRemesa("No se ha encontrado el fichero de respuesta");
				ClsLogging.writeFileLog("No se ha encontrado respuesta para institucion/remesa = " + getIdInstitucion() + "/" + getIdRemesa(), 3);
			}

		} catch (Exception e) {
			if (file != null) {
				file.delete();
			}
			try {
				if (tx != null) {
					tx.rollback();
				}
				escribeLogRemesa("Se ha producido un error al tratar la respuesta");				
			} catch (Exception e1) {
				ClsLogging.writeFileLogError("Se ha producido un error al tratar la remesa " + getIdRemesa(), e1, 3);
			}
			ClsLogging.writeFileLogError("Se ha producido un error al tratar la respuesta en la remesa " + getIdRemesa(), e, 3);			
		} finally {
			
		}
	}
	

}
