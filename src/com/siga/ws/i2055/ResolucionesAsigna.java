package com.siga.ws.i2055;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import javax.transaction.UserTransaction;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.AxisObjectSerializerDeserializer;
import com.siga.Utilidades.GestorContadores;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CajgRemesaResolucionAdm;
import com.siga.beans.CajgRemesaResolucionBean;
import com.siga.beans.EcomColaAdm;
import com.siga.beans.EcomColaBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.ScsEJGAdm;
import com.siga.beans.ScsEJGBean;
import com.siga.gratuita.action.DefinirRemesaResolucionesCAJGAction;

public class ResolucionesAsigna {
	
	
	public int obtenerResoluciones(UsrBean usrBean, String idInstitucion) throws Exception {
		int numeroResoluciones = 0;
		GenParametrosAdm admParametros = new GenParametrosAdm(usrBean);
		String urlWS = admParametros.getValor(idInstitucion.toString(), "SCS", "PCAJG_WS_URL", "");
		IntegracionSigaAsignaLocator locator = new IntegracionSigaAsignaLocator(SIGAWSClient.createClientConfig(usrBean, idInstitucion, "Envío y recepción webservice obtención de resoluciones del colegio " + idInstitucion));
		ServiceSoap_BindingStub stub = new ServiceSoap_BindingStub(new java.net.URL(urlWS), locator);
		
		Calendar fechaHasta = Calendar.getInstance();
		
		Calendar fechaInicio = obtenerFechaInicio(usrBean, idInstitucion, 1);
		
		ResultadoConsultaResolucionesExpediente[] resultados = stub.consultaResolucionesExpediente(fechaInicio, fechaHasta);
		if (resultados != null && resultados.length > 0) {
			CajgRemesaResolucionAdm cajgRemesaResolucionAdm = new CajgRemesaResolucionAdm(usrBean);			
			String idRemesaResolucion = cajgRemesaResolucionAdm.seleccionarMaximo(idInstitucion);			
			File parentFile = DefinirRemesaResolucionesCAJGAction.getRutaAlmacenFichero(idInstitucion, idRemesaResolucion);
			String nombreFichero = "resoluciones";
			
			File resolFile = new File(parentFile, nombreFichero + ".xml");
			Writer fw = new FileWriter(resolFile);
			BufferedWriter bw = new BufferedWriter(fw);
			
			File logFile = DefinirRemesaResolucionesCAJGAction.getLogFile(resolFile, nombreFichero);
			Writer logFw = new FileWriter(logFile);
			BufferedWriter logBw = new BufferedWriter(logFw);
			
			try {
				
				UserTransaction tx = usrBean.getTransaction();
				tx.begin();
			
				for (int i = 0; i < resultados.length;i++) {
					ResultadoConsultaResolucionesExpediente resol = resultados[i];
					numeroResoluciones++;
					
					bw.write(AxisObjectSerializerDeserializer.serializeAxisObject(resol, false, true));
					bw.newLine();
					bw.flush();
					
					String identificadorExpedienteSIGA = resol.getIdentificadorExpedienteSIGA();
					if (identificadorExpedienteSIGA == null || identificadorExpedienteSIGA.length() != 18) {
						logBw.write("El campo identificadorExpedienteSIGA no sigue el patrón establecido. Valor = '" + identificadorExpedienteSIGA + "'.");
						logBw.newLine();
						logBw.flush();
						continue;
					}
					//buscar el EJG
					ScsEJGAdm scsEJGAdm = new ScsEJGAdm(usrBean);
					Vector v = scsEJGAdm.select("WHERE " + ScsEJGBean.C_IDINSTITUCION + " || " + ScsEJGBean.C_ANIO + " || LPAD(" + ScsEJGBean.C_NUMEJG + ", 10, '0') = '" + identificadorExpedienteSIGA + "'");
					
					if (v != null && v.size() == 1) {//si lo encuentro
						ScsEJGBean scsEJGBean = (ScsEJGBean)v.get(0);
						String anio = scsEJGBean.getAnio().toString();
						String numEjg = scsEJGBean.getNumEJG();
						
						if (resol.getCalificacionConsultaResoluciones() == null) {
							logBw.write(anio + "/" + numEjg + ": No se ha encontrado el nodo con la resolución en el fichero.");
							logBw.newLine();
							logBw.flush();
							continue;
						}
						ECalificaciones eCalificaciones = resol.getCalificacionConsultaResoluciones().getCalificacion();

						String sqlCodTipoResolucion = "SELECT PS.IDTIPORESOLUCION" +
							" FROM PCAJG_TIPO_RESOLUCION P, PCAJG_TIPO_RESOLUCION_SCSTIPOR PS" +
							" WHERE P.IDINSTITUCION = PS.IDINSTITUCION" +
							" AND P.IDENTIFICADOR = PS.IDENTIFICADOR" +
							" AND P.IDINSTITUCION = " + idInstitucion +
							" AND P.CODIGO = '" + eCalificaciones + "'";
				

						Vector vector = cajgRemesaResolucionAdm.selectGenerico(sqlCodTipoResolucion);
						if (vector.size() == 0) {							
							logBw.write(anio + "/" + numEjg + ": No se ha encontrado el código del tipo de resolución \"" + eCalificaciones + "\"");
							logBw.newLine();
							logBw.flush();
							continue;
						} else if (vector.size() > 1) {
							logBw.write(anio + "/" + numEjg + ": Se ha encontrado más de un código del tipo de resolución \"" + eCalificaciones + "\"");
							logBw.newLine();
							logBw.flush();
							continue;							
						}
						
						Hashtable hash = (Hashtable) vector.get(0);
						scsEJGBean.setIdTipoRatificacionEJG(Integer.valueOf(hash.get("IDTIPORESOLUCION").toString()));
						scsEJGBean.setIdFundamentoJuridico(null);
						
						ArrayOfMotivosRechazo motRechazo = resol.getCalificacionConsultaResoluciones().getMotivoRechazo();
						if (motRechazo != null && motRechazo.getCodigo() != null) {
							String sqlCodMotivoResolucion = "SELECT T.IDFUNDAMENTO" +
										" FROM SCS_TIPOFUNDAMENTOS T" +
										" WHERE T.IDINSTITUCION = " + idInstitucion +
										" AND T.CODIGO = '" + motRechazo.getCodigo() + "'";
						
							vector = cajgRemesaResolucionAdm.selectGenerico(sqlCodMotivoResolucion);
							
							if (vector.size() == 0) {							
								logBw.write(anio + "/" + numEjg + ": No se ha encontrado el código del motivo de la resolución \"" + eCalificaciones + "\"");
								logBw.newLine();
								logBw.flush();
								continue;
							} else if (vector.size() > 1) {
								logBw.write(anio + "/" + numEjg + ": Se ha encontrado más de un código del motivo de la resolución \"" + eCalificaciones + "\"");
								logBw.newLine();
								logBw.flush();
								continue;							
							}
							hash = (Hashtable) vector.get(0);
							scsEJGBean.setIdFundamentoJuridico(Integer.valueOf(hash.get("IDFUNDAMENTO").toString()));
						}
						
						
						String ratificacionDictamen = "";
						if (resol.getCalificacionConsultaResoluciones().getObservaciones() != null) {
							ratificacionDictamen += resol.getCalificacionConsultaResoluciones().getObservaciones().getValue() + "\n";
						}
						
						if (ECalificaciones.SOLOABOG.equals(eCalificaciones)) {
							ratificacionDictamen += "Solamente abogado.\n";													
						}
						
						
						
						ArrayOfMotivosRechazo arrayOfMotivosRechazo = resol.getCalificacionConsultaResoluciones().getMotivoRechazo();
						if (arrayOfMotivosRechazo != null) {
							String[] datos1 = new String[]{(arrayOfMotivosRechazo.getParam1()==null?"":arrayOfMotivosRechazo.getParam1())};
							String[] datos3 = new String[]{(arrayOfMotivosRechazo.getParam1()==null?"":arrayOfMotivosRechazo.getParam1())
									, (arrayOfMotivosRechazo.getParam2()==null?"":arrayOfMotivosRechazo.getParam2())
									, (arrayOfMotivosRechazo.getParam3()==null?"":arrayOfMotivosRechazo.getParam3())};
							
							EMotivosDeNegacion eMotivosDeNegacion = arrayOfMotivosRechazo.getCodigo();
							if (EMotivosDeNegacion.REPROD.equals(eMotivosDeNegacion)) {
								String mensaje = "messages.gratuita.asigna.REPROD";
								ratificacionDictamen += UtilidadesString.getMensaje(mensaje, datos1, usrBean.getLanguage()) + "\n";
							} else if (EMotivosDeNegacion.YARECON.equals(eMotivosDeNegacion)) {
								String mensaje = "messages.gratuita.asigna.YARECON";
								ratificacionDictamen += UtilidadesString.getMensaje(mensaje, datos3, usrBean.getLanguage()) + "\n";
							} else if (EMotivosDeNegacion.ACUMEXP.equals(eMotivosDeNegacion)) {
								String mensaje = "messages.gratuita.asigna.ACUMEXP";
								ratificacionDictamen += UtilidadesString.getMensaje(mensaje, datos1, usrBean.getLanguage()) + "\n";
							} else if (EMotivosDeNegacion.OTROS.equals(eMotivosDeNegacion)) {								
								ratificacionDictamen += (arrayOfMotivosRechazo.getDescripcion()==null?"":arrayOfMotivosRechazo.getDescripcion()) + "\n";
							}
						}
						
						scsEJGBean.setRatificacionDictamen(ratificacionDictamen);
						
						Calendar cal = resol.getCalificacionConsultaResoluciones().getFecha();						
						scsEJGBean.setFechaResolucionCAJG(GstDate.convertirFechaHora(cal.getTime()));	
						
						EcomColaBean ecomColaBean = new EcomColaBean();		
						ecomColaBean.setIdInstitucion(Integer.valueOf(idInstitucion));
						ecomColaBean.setIdOperacion(EcomColaBean.OPERACION.ASIGNA_CONSULTA_NUMERO.getId());		
						
						EcomColaAdm ecomColaAdm = new EcomColaAdm(usrBean);
						if (!ecomColaAdm.insert(ecomColaBean)) {
							throw new ClsExceptions("No se ha podido insertar en la cola de comunicaciones.");
						}
							
						scsEJGBean.setIdEcomCola(ecomColaBean.getIdEcomCola());
						
						if (scsEJGAdm.update(scsEJGBean)) {
							logBw.write(anio + "/" + numEjg + ": El expediente se ha actualizado correctamente.");
							logBw.newLine();
							logBw.flush();	
						} else {
							logBw.write(anio + "/" + numEjg + ": El expediente no se ha actualizado correctamente.");
							logBw.newLine();
							logBw.flush();
						}
						
						
					} else {
						logBw.write("No se ha encontrado el EJG en siga con valor de identificadorExpedienteSIGA igual a \"" + identificadorExpedienteSIGA + "\".");
						logBw.newLine();
						logBw.flush();
					}
					
				}
				
				insertaCajgRemesaResolucion(usrBean, idInstitucion, idRemesaResolucion, fechaHasta.getTime(), resolFile.getName());				
				tx.commit();
				
			} finally {
				logBw.close();
				logFw.close();
				bw.close();
				fw.close();
			}
		} else {
			ClsLogging.writeFileLog("No se ha encontrado ninguna resolución en Asigna para el colegio " + idInstitucion, 3);
		}
		return numeroResoluciones;
	}

	private void insertaCajgRemesaResolucion(UsrBean usrBean, String idInstitucion, String idRemesaResolucion, Date fechaCarga, String nombreFichero) throws Exception {
		
		CajgRemesaResolucionAdm cajgRemesaResolucionAdm = new CajgRemesaResolucionAdm(usrBean);
		CajgRemesaResolucionBean cajgRemesaResolucionBean = new CajgRemesaResolucionBean();
		
		String idContador = cajgRemesaResolucionAdm.getIdContador(idInstitucion, "1");
		
		GestorContadores gcRemesa = new GestorContadores(usrBean);
		Hashtable contadorTablaHashRemesa = gcRemesa.getContador(Integer.valueOf(idInstitucion), idContador);
		String siguiente = gcRemesa.getNuevoContador(contadorTablaHashRemesa);
		
		cajgRemesaResolucionBean.setPrefijo(contadorTablaHashRemesa.get(CajgRemesaResolucionBean.C_PREFIJO).toString());
		cajgRemesaResolucionBean.setSufijo(contadorTablaHashRemesa.get(CajgRemesaResolucionBean.C_SUFIJO).toString());
		cajgRemesaResolucionBean.setNumero(siguiente);
		cajgRemesaResolucionBean.setFechaCarga(GstDate.convertirFechaHora(fechaCarga));
		cajgRemesaResolucionBean.setFechaResolucion(GstDate.convertirFechaHora(fechaCarga));
		
		cajgRemesaResolucionBean.setIdInstitucion(Integer.valueOf(idInstitucion));
		
		cajgRemesaResolucionBean.setIdRemesaResolucion(Integer.valueOf(idRemesaResolucion));			
		cajgRemesaResolucionBean.setIdTipoRemesa(Integer.valueOf(1));
		
		cajgRemesaResolucionBean.setNombreFichero(nombreFichero);
    	cajgRemesaResolucionBean.setLogGenerado("1");
		
		cajgRemesaResolucionAdm.insert(cajgRemesaResolucionBean);
		gcRemesa.setContador(contadorTablaHashRemesa, siguiente);
		
	}

	private Calendar obtenerFechaInicio(UsrBean usrBean, String idInstitucion, int idTipoRemesa) throws ClsExceptions {
		CajgRemesaResolucionAdm cajgRemesaResolucionAdm = new CajgRemesaResolucionAdm(usrBean);
		Calendar cal = cajgRemesaResolucionAdm.getMaximaFechaCarga(idInstitucion, idTipoRemesa);
		if (cal == null) {
			cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, 2000);
		}
		return cal;
	}
}
