/**
 * 
 */
package com.siga.ws.cat;

import java.io.File;
import java.io.FileInputStream;
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
import org.redabogacia.sigaservices.app.helper.SIGAServicesHelper;
import org.redabogacia.sigaservices.app.helper.ftp.FtpPcajgAbstract;
import org.redabogacia.sigaservices.app.helper.ftp.FtpPcajgFactory;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.FileHelper;
import com.atos.utils.GstDate;
import com.jcraft.jsch.JSchException;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CajgEJGRemesaAdm;
import com.siga.beans.CajgEJGRemesaBean;
import com.siga.beans.CajgRemesaAdm;
import com.siga.beans.CajgRemesaBean;
import com.siga.beans.CajgRemesaEstadosAdm;
import com.siga.beans.CajgRespuestaEJGRemesaAdm;
import com.siga.beans.CajgRespuestaEJGRemesaBean;
import com.siga.beans.ScsEJGAdm;
import com.siga.beans.ScsEJGBean;
import com.siga.general.SIGAException;
import com.siga.ws.PCAJGConstantes;
import com.siga.ws.SIGAWSClientAbstract;
import com.siga.ws.pcajg.cat.xsd.IntercambioDocument;
import com.siga.ws.pcajg.cat.xsd.IntercambioDocument.Intercambio;
import com.siga.ws.pcajg.cat.xsd.IntercambioDocument.Intercambio.InformacionIntercambio;
import com.siga.ws.pcajg.cat.xsd.IntercambioDocument.Intercambio.InformacionIntercambio.IntercambioErroneo;
import com.siga.ws.pcajg.cat.xsd.IntercambioDocument.Intercambio.InformacionIntercambio.IntercambioErroneo.DatosError;
import com.siga.ws.pcajg.cat.xsd.IntercambioDocument.Intercambio.InformacionIntercambio.IntercambioErroneo.DatosError.ErrorContenido;
import com.siga.ws.pcajg.cat.xsd.IntercambioDocument.Intercambio.InformacionIntercambio.IntercambioErroneo.DatosError.ErrorContenido.CodigoExpedienteError;
import com.siga.ws.pcajg.cat.xsd.IntercambioDocument.Intercambio.InformacionIntercambio.IntercambioErroneo.DatosError.ErrorContenido.DetalleError;
import com.siga.ws.pcajg.cat.xsd.IntercambioDocument.Intercambio.InformacionIntercambio.IntercambioErroneo.DatosError.ErrorGeneral;
import com.siga.ws.pcajg.cat.xsd.IntercambioDocument.Intercambio.InformacionIntercambio.TipoGenerico;
import com.siga.ws.pcajg.cat.xsd.IntercambioDocument.Intercambio.InformacionIntercambio.TipoGenerico.Expediente;
import com.siga.ws.pcajg.cat.xsd.IntercambioDocument.Intercambio.InformacionIntercambio.TipoGenerico.Expediente.DatosExpediente;
import com.siga.ws.pcajg.cat.xsd.IntercambioDocument.Intercambio.InformacionIntercambio.TipoGenerico.Expediente.DatosTramitacionExpediente.TramiteResolucion;
import com.siga.ws.pcajg.cat.xsd.IntercambioDocument.Intercambio.InformacionIntercambio.TipoGenerico.Expediente.DatosTramitacionExpediente.TramiteResolucion.PrestacionesResolucion;
import com.siga.ws.pcajg.cat.xsd.MillorFortunaType;
import com.siga.ws.pcajg.cat.xsd.MillorFortunaType.DadesResolucio.Resolucio;
import com.siga.ws.pcajg.cat.xsd.TipoIdentificacionIntercambio;




/**
 * @author angelcpe
 * 
 */
public class PCAJGxmlResponse extends SIGAWSClientAbstract implements PCAJGConstantes {

	private String namespace = IntercambioDocument.type.getProperties()[0].getName().getNamespaceURI();//"rp.cat.ws.siga.com";
	private static String DESCRIPCION_SIN_ERRORES = "No hi han errors";	
		
		
	private File getResolucionIRFile(int idInstitucion, String fileName) {		
		ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String rutaAlmacen = rp.returnProperty("cajg.directorioFisicoCAJG") + rp.returnProperty("cajg.directorioCAJGJava");			
		rutaAlmacen += File.separator + idInstitucion;				
		File file = new File(rutaAlmacen + File.separator + "resolucionIR");
		FileHelper.mkdirs(rutaAlmacen + File.separator + "resolucionIR");
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
			
			ftpPcajgAbstract = FtpPcajgFactory.getInstance((short)getIdInstitucion());
			escribeLogRemesa("Conectando con el servidor FTP");
			ftpPcajgAbstract.connect();	
			
			List<String> vectorFiles = ftpPcajgAbstract.ls();
			List<File> filesIEE = new ArrayList<File>();			
			List<File> filesIR = new ArrayList<File>();
			
			FileOutputStream fileOutputStream = null;
			
			for (String fileName : vectorFiles) {								
				if (fileName.startsWith(comienzoFicheroIEE_GEN) || fileName.startsWith(comienzoFicheroIR_GEN)){					
					ClsLogging.writeFileLog("Encontrado fichero " + fileName, 3);					
					File file = null;
					
					if (fileName.startsWith(comienzoFicheroIEE_GEN)) {
						file = getRespuestaFile(getIdInstitucion(), String.valueOf(getIdRemesa()), fileName);
						filesIEE.add(file);
					} else if (fileName.startsWith(comienzoFicheroIR_GEN)) {
						file = getResolucionIRFile(getIdInstitucion(), fileName);						
						filesIR.add(file);
					}
					
					fileOutputStream = new FileOutputStream(file);
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
			escribeLogRemesa("Se ha producido un error de conexi�n con el servidor FTP");
			ClsLogging.writeFileLogError("Error en el env�o FTP. Revisar la configuraci�n FTP del colegio " + getIdInstitucion(), e, 3);		
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
				
				IntercambioDocument intercambioIRDoc = null;				
				try {					
					intercambioIRDoc = IntercambioDocument.Factory.parse(file, xmlOptions);
				} catch (XmlException e) {
					escribeLogRemesa("El xml " + file.getName() + " no es un fichero de \"Intercambio\" v�lido.");
					throw e;
				}
				
				if (SIGAServicesHelper.validate(intercambioIRDoc).size() > 0) {					
					throw new ClsExceptions("El xml " + file.getName() + " no es v�lido");
				}
				
				IntercambioDocument intercambioRespuesta = IntercambioDocument.Factory.newInstance();				
				InformacionIntercambio informacionIntercambioRespuesta = intercambioRespuesta.addNewIntercambio().addNewInformacionIntercambio();
				IntercambioErroneo intercambioErroneo = informacionIntercambioRespuesta.addNewIntercambioErroneo();
				DatosError datosError = intercambioErroneo.addNewDatosError();
								
				InformacionIntercambio informacionIntercambio = intercambioIRDoc.getIntercambio().getInformacionIntercambio();
				TipoIdentificacionIntercambio idInterOrigen = informacionIntercambio.getIdentificacionIntercambio();
				
				TipoIdentificacionIntercambio idInterDestino = intercambioErroneo.addNewIdentificacionIntercambio();
				idInterDestino.setTipoIntercambio(idInterOrigen.getTipoIntercambio());
				idInterDestino.setCodOrigenIntercambio(idInterOrigen.getCodOrigenIntercambio());
				idInterDestino.setDescOrigenIntercambio(idInterOrigen.getDescOrigenIntercambio());
				idInterDestino.setCodDestinoIntercambio(idInterOrigen.getCodDestinoIntercambio());
				idInterDestino.setDescDestinoIntercambio(idInterOrigen.getDescDestinoIntercambio());
				idInterDestino.setIdentificadorIntercambio(idInterOrigen.getIdentificadorIntercambio());
				idInterDestino.setFechaIntercambio(idInterOrigen.getFechaIntercambio());
				idInterDestino.setNumeroDetallesIntercambio(idInterOrigen.getNumeroDetallesIntercambio());
				idInterDestino.setVersion(idInterOrigen.getVersion());
				
								
				String idInstitucion = idInterOrigen.getCodDestinoIntercambio();
				TipoGenerico tipoGenerico = informacionIntercambio.getTipoGenerico();
				if (tipoGenerico == null) {
					throw new ClsExceptions("El nodo " + TipoGenerico.class.getSimpleName() + " no puede ser null");
				}			
				
				Expediente[] expedientes = tipoGenerico.getExpedienteArray();
				MillorFortunaType[] millorFortunaArray = tipoGenerico.getMillorFortunaArray();
				
				int anyoExpediente = -1;
				String numExpediente = null;
				Calendar fechaEstado = null;
				String identificadorResolucion = null;
				String codTipoResolucion = null;
				String codMotivoResolucion = null;
				String intervaloIngresosRecursos = null;
				StringBuffer tipoPrestacion = null;
				
				tx = getUsrBean().getTransactionPesada();
				tx.begin();
				
				if (expedientes != null && expedientes.length > 0) {
					for (Expediente expediente : expedientes) {					
						tipoPrestacion = new StringBuffer();
						
						DatosExpediente datosExpediente = expediente.getDatosExpediente();
						
						if (datosExpediente.getCodigoExpedienteServicio() != null) {
							String descError = "El expediente no se puede relacionar en el sistema. Se ha recibido el siguiente c�digo expediente servicio (a�o/n�mero)" +
									" = " + datosExpediente.getCodigoExpedienteServicio().getAnyoExpedienteServicio() + "/" + datosExpediente.getCodigoExpedienteServicio().getNumExpedienteServicio();
							escribeLogRemesa(descError);
							rellenaErrorContenido(datosError, datosExpediente.getCodigoExpedienteServicio().getOrigenExpedienteServicio(), 
									datosExpediente.getCodigoExpedienteServicio().getAnyoExpedienteServicio(), 
									datosExpediente.getCodigoExpedienteServicio().getNumExpedienteServicio(), 
									"CodigoExpedienteServicio", descError);
						} else {
							
							numExpediente = datosExpediente.getCodigoExpediente().getNumExpediente();
							anyoExpediente = datosExpediente.getCodigoExpediente().getAnyoExpediente();
							
							if (expediente.getDatosTramitacionExpediente() == null || expediente.getDatosTramitacionExpediente().getTramiteResolucion() == null) {
								String descError = "No se ha recibido resoluci�n para el expediente (a�o/n�mero)" +
										" = " + anyoExpediente + "/" + numExpediente;
								escribeLogRemesa(descError);
								rellenaErrorContenido(datosError, datosExpediente.getCodigoExpediente().getColegioExpediente(), anyoExpediente, numExpediente, "TramiteResolucion", descError);
								
							} else {
								
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
								actualizaExpediente(idInstitucion, datosExpediente.getCodigoExpediente().getColegioExpediente(), anyoExpediente, numExpediente, fechaEstado, identificadorResolucion, codTipoResolucion, codMotivoResolucion, intervaloIngresosRecursos, tipoPrestacion.toString(), datosError);
							}
						}
					}
				}
				
				if (millorFortunaArray != null && millorFortunaArray.length > 0) {
					for (MillorFortunaType millorFortuna : millorFortunaArray) {					
						tipoPrestacion = new StringBuffer();
						
						
						if (millorFortuna.getDadesMillorFortuna().getCodiExpedientServei() != null) {
							String descError = "El expediente no se puede relacionar en el sistema. Se ha recibido el siguiente c�digo expediente servicio (a�o/n�mero)" +
									" = " + millorFortuna.getDadesMillorFortuna().getCodiExpedientServei().getAnyoExpedienteServicio() + "/" + millorFortuna.getDadesMillorFortuna().getCodiExpedientServei().getNumExpedienteServicio();
							escribeLogRemesa(descError);
							rellenaErrorContenido(datosError, millorFortuna.getDadesMillorFortuna().getCodiExpedientServei().getOrigenExpedienteServicio(), 
									millorFortuna.getDadesMillorFortuna().getCodiExpedientServei().getAnyoExpedienteServicio(), 
									millorFortuna.getDadesMillorFortuna().getCodiExpedientServei().getNumExpedienteServicio(), 
									"CodigoExpedienteServicio", descError);
						} else {
							
							numExpediente = millorFortuna.getDadesMillorFortuna().getCodiExpedient().getNumExpediente();
							anyoExpediente = millorFortuna.getDadesMillorFortuna().getCodiExpedient().getAnyoExpediente();
							
							Resolucio resolucio = millorFortuna.getDadesResolucio().getResolucio();
							fechaEstado = resolucio.getDataResolucio();
							identificadorResolucion = resolucio.getNumResolucio();
							codTipoResolucion = "MF";
							codMotivoResolucion = resolucio.getMotiuResolucio();
							intervaloIngresosRecursos = null;
							
							actualizaExpediente(idInstitucion, millorFortuna.getDadesMillorFortuna().getCodiExpedient().getColegioExpediente(), anyoExpediente, numExpediente, fechaEstado, identificadorResolucion, codTipoResolucion, codMotivoResolucion, intervaloIngresosRecursos, tipoPrestacion.toString(), datosError);
						}
					}
				}
				
				if (datosError.getErrorContenidoArray() == null || datosError.getErrorContenidoArray().length == 0) {
					//si no hay errores de contenido rellenamos el error general con OK
					datosError.addNewErrorGeneral().setDescErrorGen("No hay errores");
				}
				
				int numeroDetalles = datosError.getErrorContenidoArray()!=null?datosError.getErrorContenidoArray().length:0;
				
				rellenaIdentificacionIntercambio(informacionIntercambioRespuesta, informacionIntercambio, numeroDetalles);
				//salvar y subir al ftp la respuesta				
				String nombreFichero = getNombreFichero(informacionIntercambioRespuesta.getIdentificacionIntercambio());
				File fileRespuestaIR = new File(file.getParent(), nombreFichero);
				
				guardaFicheroFormatoCatalan(intercambioRespuesta, fileRespuestaIR);
				
				CajgRemesaEstadosAdm cajgRemesaEstadosAdm = new CajgRemesaEstadosAdm(getUsrBean());				
				List<Integer> listaIdRemesas = cajgRemesaEstadosAdm.comprobarRemesaRecibida(Integer.parseInt(idInstitucion));
				
				if (listaIdRemesas != null) {
					for (Integer idRemesa : listaIdRemesas) {
						cajgRemesaEstadosAdm.nuevoEstadoRemesa(getUsrBean(), getIdInstitucion(), idRemesa, ClsConstants.ESTADO_REMESA_RECIBIDA);	
					}
				}
				//subimos la respuesta al ftp
				FileInputStream fis = new FileInputStream(fileRespuestaIR);
				escribeLogRemesa("Subiendo XML generado al servidor FTP");
				ftpPcajgAbstract.upload(fileRespuestaIR.getName(), fis);				
				fis.close();				
				
				ftpPcajgAbstract.moveToHIST(file.getName());				
				tx.commit();
				
			} catch (Exception e) {
						
				
				try {
					if (tx != null) {
						tx.rollback();
					}
					escribeLogRemesa("Se ha producido un error al tratar la resoluci�n");
				} catch (Exception e1) {
					ClsLogging.writeFileLogError("Se ha producido un error al tratar las resoluciones ", e1, 3);
				}
				ClsLogging.writeFileLogError("Se ha producido un error al tratar la resoluci�n", e, 3);
			}
		}
	}
	
	
	private void rellenaIdentificacionIntercambio(InformacionIntercambio informacionIntercambioRespuesta, InformacionIntercambio informacionIntercambioIR, int numeroDetalles) {
		TipoIdentificacionIntercambio idIntercambio = informacionIntercambioRespuesta.addNewIdentificacionIntercambio();
		idIntercambio.setTipoIntercambio(PCAJGGeneraXML.INTERCAMBIO_ERRORES_EXPEDIENTE);
		idIntercambio.setCodOrigenIntercambio(String.valueOf(getIdInstitucion()));
		idIntercambio.setCodDestinoIntercambio(informacionIntercambioIR.getIdentificacionIntercambio().getCodOrigenIntercambio());		
		idIntercambio.setIdentificadorIntercambio(informacionIntercambioIR.getIdentificacionIntercambio().getIdentificadorIntercambio());
		idIntercambio.setFechaIntercambio(SIGAServicesHelper.clearCalendar(Calendar.getInstance()));		
		idIntercambio.setNumeroDetallesIntercambio(numeroDetalles);
		idIntercambio.setVersion(informacionIntercambioIR.getIdentificacionIntercambio().getVersion());
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
	 * @param  
	 * @throws Exception
	 */
	private void actualizaExpediente(String idInstitucion, String colegioExpediente, int anyoExpediente, String numExpediente, Calendar fechaEstadoCalendar, String identificadorResolucion
			, String codTipoResolucion, String codMotivoResolucion, String intervaloIngresosRecursos, String tipoPrestacion, DatosError datosError) throws Exception {		
		
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
				String descError = "No se encuentra el expediente en el sistema con el a�o/n�mero = " + anyoExpediente + "/" + numExpediente;
				escribeLogRemesa(descError);
				rellenaErrorContenido(datosError, colegioExpediente, anyoExpediente, numExpediente, "NumExpediente", descError);				
			} else if (vectorEJGs.size() > 1) {
				//se ha encontrado mas de un EJG
				String descError = "Se ha encontrado m�s de un expediente con el mismo a�o/n�mero = " + anyoExpediente + "/" + numExpediente;
				escribeLogRemesa(descError);				
				rellenaErrorContenido(datosError, colegioExpediente, anyoExpediente, numExpediente, "NumExpediente", descError);				
			} else {
				//EJG encontrado
				ScsEJGBean scsEJGBean = (ScsEJGBean) vectorEJGs.get(0);				
				
				String sqlCodTipoResolucion = "SELECT PS.IDTIPORESOLUCION" +
										" FROM PCAJG_TIPO_RESOLUCION P, PCAJG_TIPO_RESOLUCION_SCSTIPOR PS" +
										" WHERE P.IDINSTITUCION = PS.IDINSTITUCION" +
										" AND P.IDENTIFICADOR = PS.IDENTIFICADOR" +
										" AND P.IDINSTITUCION = " + idInstitucion +
										" AND P.CODIGO = '" + codTipoResolucion + "'";
								
				
				Vector vector = cajgEJGRemesaAdm.selectGenerico(sqlCodTipoResolucion);
				if (vector.size() == 0) {
					String descError = "No se ha encontrado el c�digo " + codTipoResolucion;									
					rellenaErrorContenido(datosError, colegioExpediente, anyoExpediente, numExpediente, "CodTipoResolucion", descError);
					return;
				} else if (vector.size() > 1) {
					String descError = "Se ha encontrado m�s de un elemento con el c�digo " + codTipoResolucion;								
					rellenaErrorContenido(datosError, colegioExpediente, anyoExpediente, numExpediente, "CodTipoResolucion", descError);
					return;
				}
				
				String sqlCodMotivoResolucion = "SELECT T.IDFUNDAMENTO" +
								" FROM SCS_TIPOFUNDAMENTOS T" +
								" WHERE T.IDINSTITUCION = " + idInstitucion +
								" AND T.CODIGO = '" + codMotivoResolucion + "'";
				
				vector = cajgEJGRemesaAdm.selectGenerico(sqlCodMotivoResolucion);
				if (vector.size() == 0) {
					String descError = "No se ha encontrado el c�digo " + codMotivoResolucion;									
					rellenaErrorContenido(datosError, colegioExpediente, anyoExpediente, numExpediente, "CodMotivoResolucion", descError);
					return;
				} else if (vector.size() > 1) {
					String descError = "Se ha encontrado m�s de un elemento con el c�digo " + codMotivoResolucion;									
					rellenaErrorContenido(datosError, colegioExpediente, anyoExpediente, numExpediente, "CodMotivoResolucion", descError);
					return;
				}
				
				StringBuffer observaciones = new StringBuffer();
				
				aniadeTexto(observaciones, scsEJGBean.getRatificacionDictamen());
				aniadeTexto(observaciones, recuperaDatosEJG(scsEJGBean));
				
				//El estado lo pone el trigger como resuelto
				aniadeTexto(observaciones, (intervaloIngresosRecursos!=null?("Intervalo ingresos recursos: " + intervaloIngresosRecursos):""));
				
				if (tipoPrestacion != null && !tipoPrestacion.trim().equals("")){
					aniadeTexto(observaciones, "Prestaciones: " + tipoPrestacion);
				}
				
				String sObservaciones = observaciones.toString();
				sObservaciones = UtilidadesString.escapaCaracteres(sObservaciones);				
				
				String update = "UPDATE " + ScsEJGBean.T_NOMBRETABLA + " SET " +
						ScsEJGBean.C_IDTIPORATIFICACIONEJG + " = (" + sqlCodTipoResolucion + ")" +//tipoResolucion
						", " + ScsEJGBean.C_IDFUNDAMENTOJURIDICO + " = (" + sqlCodMotivoResolucion + ")" + //motivoResolucion
						", " + ScsEJGBean.C_FECHARESOLUCIONCAJG + " = TO_DATE('" + fechaEstado + "', 'YYYY-MM-DD')" +
						", " + ScsEJGBean.C_REFAUTO + " = '" + (identificadorResolucion==null?"":identificadorResolucion) + "'" +
						", " + ScsEJGBean.C_RATIFICACIONDICTAMEN + " = '" + sObservaciones + "'" +
						", " + ScsEJGBean.C_FECHAMODIFICACION + " = SYSDATE" +
						// jbd // A peticion de los catalanes ya no se actualiza el usumodificacion
						// ", " + ScsEJGBean.C_USUMODIFICACION + " = " + getUsrBean().getUserName() +
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
	
	private String recuperaDatosEJG(ScsEJGBean scsEJGBean) throws ClsExceptions, SIGAException {
		String observaciones = "";
		if (scsEJGBean != null && scsEJGBean.getIdTipoRatificacionEJG() != null) {
			String sql = "select f_siga_getrecurso(tr.descripcion, 2) TIPORESOLUCION from scs_tiporesolucion tr where tr.idtiporesolucion = " + scsEJGBean.getIdTipoRatificacionEJG();
			CajgEJGRemesaAdm cajgEJGRemesaAdm = new CajgEJGRemesaAdm(getUsrBean());
			Vector vector = cajgEJGRemesaAdm.selectGenerico(sql);
			if (vector != null && vector.size() == 1) {
				Hashtable hashtable = (Hashtable) vector.get(0);
				if (hashtable.get("TIPORESOLUCION") != null) {
					observaciones = "Resoluci�n anterior: " + hashtable.get("TIPORESOLUCION");
					 
					if (scsEJGBean.getIdFundamentoJuridico() != null) {
						sql = "SELECT f_siga_getrecurso(T.descripcion, 2) TIPOFUNDAMENTOS" +
							" FROM SCS_TIPOFUNDAMENTOS T" +
							" WHERE T.IDINSTITUCION = " + scsEJGBean.getIdInstitucion() +
							" AND T.IDFUNDAMENTO = " + scsEJGBean.getIdFundamentoJuridico();
						vector = cajgEJGRemesaAdm.selectGenerico(sql);
						if (vector != null && vector.size() == 1) {
							hashtable = (Hashtable) vector.get(0);
							if (hashtable.get("TIPOFUNDAMENTOS") != null) {
								observaciones += " (" + hashtable.get("TIPOFUNDAMENTOS") + ")";
							}
						}
						
						if (scsEJGBean.getFechaResolucionCAJG() != null && !scsEJGBean.getFechaResolucionCAJG().trim().equals("")) {
							try {
								observaciones += " (" + (GstDate.getFormatedDateShort("", scsEJGBean.getFechaResolucionCAJG())) + ")";
							} catch (ClsExceptions e) {
								//si falla por el formata lo fecha no la insertamos en el campo observaciones
								ClsLogging.writeFileLogError("Se ha producido un error al tratar la fecha de resoluci�n " + scsEJGBean.getFechaResolucionCAJG(), e, 3);
							}
							
						}
						if (scsEJGBean.getRefAuto() != null && !scsEJGBean.getRefAuto().trim().equals("")) {
							observaciones += ". RefAuto: " + scsEJGBean.getRefAuto();
						}
					}
				}
			}
		}
		
		return observaciones;
	}


	private void aniadeTexto(StringBuffer observaciones, String string) {
		if (string != null && !string.trim().equals("")) {
			if (observaciones.length() > 0) {
				observaciones.append("\n");
			}
			observaciones.append(string);
		}
	}


	


	private void rellenaErrorContenido(DatosError datosError, String colegioExpediente, int anyoExpediente, String numExpediente, String campoError, String descError) {
		ErrorContenido errorContenido = datosError.addNewErrorContenido();
		CodigoExpedienteError codExpError = errorContenido.addNewCodigoExpedienteError();
		codExpError.setColegioExpediente(colegioExpediente);
		codExpError.setNumExpediente(numExpediente);
		codExpError.setAnyoExpediente(anyoExpediente);		
		
		DetalleError detalleError = errorContenido.addNewDetalleError();
		detalleError.setCampoError(campoError);
		detalleError.setDescError(descError);
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
					escribeLogRemesa("El xml " + file.getName() + " no es un fichero de \"Intercambio\" v�lido.");
					throw e;
				}	
				if (SIGAServicesHelper.validate(intercambioRespuestaDoc).size() > 0) {
					throw new ClsExceptions("El xml " + file.getName() + " no es v�lido");
				}			
				
				Intercambio intercambioRespuesta = intercambioRespuestaDoc.getIntercambio();
				InformacionIntercambio informacionIntercambio = intercambioRespuesta.getInformacionIntercambio();
				IntercambioErroneo intercambioErroneo = informacionIntercambio.getIntercambioErroneo();
				
				DatosError[] datosErrors = intercambioErroneo.getDatosErrorArray();
				TipoIdentificacionIntercambio identificacionIntercambio = intercambioErroneo.getIdentificacionIntercambio();
				String idInstitucion = identificacionIntercambio.getCodOrigenIntercambio();
				int idIntercambio = (int)identificacionIntercambio.getIdentificadorIntercambio();
				idIntercambio = idIntercambio / 10;
				
				//
				CajgRemesaAdm cajgRemesaAdm = new CajgRemesaAdm(getUsrBean());
				Hashtable hash = new Hashtable();
				hash.put(CajgRemesaBean.C_IDINSTITUCION, getIdInstitucion());
				hash.put(CajgRemesaBean.C_IDINTERCAMBIO, idIntercambio);
				Vector v = cajgRemesaAdm.select(hash);
				
				if (v == null || v.size() != 1) {
					throw new ClsExceptions("No se ha encontrado la remesa con idinstitucion = " + getIdInstitucion() + " e idintercambio = " + idIntercambio);
				}
				CajgRemesaBean cajgRemesaBean = (CajgRemesaBean) v.get(0);
								
				setIdRemesa(cajgRemesaBean.getIdRemesa());
				//cerramos el log para a partir de ahora escribir en el log correspondiente
				closeLogFile();
				
				if (!String.valueOf(getIdInstitucion()).equals(idInstitucion)) {
					escribeLogRemesa("La institucion del fichero es nula o distinta a la del usuario de SIGA");
					throw new ClsExceptions("La institucion del fichero es distinta a la del usuario de SIGA");
				}
				
				file.renameTo(getRespuestaFile(getIdInstitucion(), String.valueOf(getIdRemesa()), file.getName()));
				ClsLogging.writeFileLog("Fichero movido a la ruta: " + file.getAbsolutePath(), 3);
				
				escribeLogRemesa("Fichero de respuesta " + file.getName() + " encontrado en el servidor FTP. Analizando respuesta.");
				
				CajgEJGRemesaAdm cajgEJGRemesaAdm = new CajgEJGRemesaAdm(getUsrBean());
				ScsEJGAdm scsEJGAdm = new ScsEJGAdm(getUsrBean());
				CajgRespuestaEJGRemesaAdm cajgRespuestaEJGRemesaAdm = new CajgRespuestaEJGRemesaAdm(getUsrBean());
				
				tx = getUsrBean().getTransactionPesada();
				tx.begin();
				
				//cajgRespuestaEJGRemesaAdm.eliminaAnterioresErrores(getIdInstitucion(), getIdRemesa());
				
				for (DatosError datosError : datosErrors) {
					
					ErrorGeneral errorGeneral = datosError.getErrorGeneral();
					
					if (errorGeneral != null) {
						escribeLogRemesa(errorGeneral.getDescErrorGen().toString());
						if (!DESCRIPCION_SIN_ERRORES.equals(errorGeneral.getDescErrorGen())) {
						
							Hashtable<String, Object> hashEjgRem = new Hashtable<String, Object>();
							
							hashEjgRem.put(CajgEJGRemesaBean.C_IDINSTITUCIONREMESA, idInstitucion);
							hashEjgRem.put(CajgEJGRemesaBean.C_IDREMESA, getIdRemesa());
							
							Vector<CajgEJGRemesaBean> vectorRemesa = cajgEJGRemesaAdm.select(hashEjgRem);
							
							for (CajgEJGRemesaBean cajgEJGRemesaBean : vectorRemesa) {							
								CajgRespuestaEJGRemesaBean cajgRespuestaEJGRemesaBean = new CajgRespuestaEJGRemesaBean();											
								cajgRespuestaEJGRemesaBean.setIdEjgRemesa(cajgEJGRemesaBean.getIdEjgRemesa());
								cajgRespuestaEJGRemesaBean.setCodigo("-1");
								cajgRespuestaEJGRemesaBean.setDescripcion(errorGeneral.getDescErrorGen().toString());
								cajgRespuestaEJGRemesaBean.setFecha("SYSDATE");
								cajgRespuestaEJGRemesaBean.setIdTipoRespuesta(CajgRespuestaEJGRemesaBean.TIPO_RESPUESTA_COMISION);
								
								cajgRespuestaEJGRemesaAdm.insert(cajgRespuestaEJGRemesaBean);
							}
						}
						
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
									escribeLogRemesa("No se ha encontrado el EJG a�o/n�mero = " + anioExp + "/" + numExp);
								} else if (vectorEJGs.size() > 1) {
									//se ha encontrado mas de un EJG
									escribeLogRemesa("Se ha encontrado m�s de un EJG con el a�o/n�mero = " + anioExp + "/" + numExp);
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
										escribeLogRemesa("No se ha encontrado el EJG a�o/n�mero = " + anioExp + "/" + numExp + " en la remesa");										
									} else if (vectorRemesa.size() > 1) {
										escribeLogRemesa("Se ha encontrado m�s de un EJG a�o/n�mero = " + anioExp + "/" + numExp + " en la remesa");
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
//												descripcion.append("<b>M�s informaci�n:</b> " + detallError.getAmpliacionError());
//											}
											CajgRespuestaEJGRemesaBean cajgRespuestaEJGRemesaBean = new CajgRespuestaEJGRemesaBean();											
											cajgRespuestaEJGRemesaBean.setIdEjgRemesa(cajgEJGRemesaBean.getIdEjgRemesa());
											cajgRespuestaEJGRemesaBean.setCodigo("-1");
											cajgRespuestaEJGRemesaBean.setDescripcion(descripcion.toString());
											cajgRespuestaEJGRemesaBean.setFecha("SYSDATE");
											cajgRespuestaEJGRemesaBean.setIdTipoRespuesta(CajgRespuestaEJGRemesaBean.TIPO_RESPUESTA_COMISION);
											
											cajgRespuestaEJGRemesaAdm.insert(cajgRespuestaEJGRemesaBean);
										}
									}
								}
							}
						}
					}
					
				}
				
				ClsLogging.writeFileLog("Moviendo fichero a la carpeta de hist�rico", 3);
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
			ClsLogging.writeFileLogError("Se ha producido un error al tratar la respuesta del colegio " + getIdInstitucion(), e, 3);			
		} finally {
			
		}
	}
	

}
