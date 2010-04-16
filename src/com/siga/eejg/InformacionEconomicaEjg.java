package com.siga.eejg;

import java.net.ConnectException;
import java.util.List;

import org.apache.axis.AxisFault;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.eejg.ScsEejgPeticionesAdm;
import com.siga.beans.eejg.ScsEejgPeticionesBean;

public class InformacionEconomicaEjg {
	
private static Boolean alguienEjecutando=Boolean.FALSE;
	public boolean isAlguienEjecutando(){
		synchronized(InformacionEconomicaEjg.alguienEjecutando){
			if (!InformacionEconomicaEjg.alguienEjecutando){
				InformacionEconomicaEjg.alguienEjecutando=Boolean.TRUE;
				return false;
			} else {
				return true;
			}
		}
	}

	public void setNadieEjecutando(){
		synchronized(InformacionEconomicaEjg.alguienEjecutando){
			InformacionEconomicaEjg.alguienEjecutando=Boolean.FALSE;
		}
	}
	public void tratarSolicitudesEejg()throws Exception{
		if (isAlguienEjecutando()){
			ClsLogging.writeFileLogError("gratuita.eejg.message.isAlguienEjecutando",new SchedulerException("gratuita.eejg.message.isAlguienEjecutando"), 3);
			//setNadieEjecutando();
			return;
		}
		
		try {
			UsrBean usrBean = new UsrBean();
			usrBean.setUserName(String.valueOf(ClsConstants.USUMODIFICACION_AUTOMATICO));
			trataPeticionesIniciadas(usrBean);
			trataSolicitudesPendientes(usrBean);
			
		} catch(Exception e){
			throw e;
		}
		finally {
			setNadieEjecutando();
		}
	}

	private void trataPeticionesIniciadas(UsrBean usrBean) throws Exception {
		
		ScsEejgPeticionesAdm scsPeticionesAdm = new ScsEejgPeticionesAdm(usrBean);
		List<ScsEejgPeticionesBean> listaPeticiones = scsPeticionesAdm.getPeticionesIniciadas();
		
		int numeroErrores = 0;
		if (listaPeticiones != null && listaPeticiones.size()>0 ) {
						
			GenParametrosAdm admParametros = new GenParametrosAdm(usrBean);
			
			int NUM_ERROR_CONEXION = Integer.parseInt(admParametros.getValor(ScsEejgPeticionesBean.INSTITUCION_PARAMETROS_EEJG, "SCS", "EEJG_NUMERO_ERRORES_CONEXION", ""));
			int NUMERO_REINTENTOS_SOLICITUD = Integer.parseInt(admParametros.getValor(ScsEejgPeticionesBean.INSTITUCION_PARAMETROS_EEJG, "SCS", "EEJG_NUMERO_REINTENTOS_SOLICITUD", ""));
			
			//Iniciamos cambiando a estado pendiente
			scsPeticionesAdm.updatePeticionesIniciadas();
			SolicitudesEEJG solicitudesEEJG = new SolicitudesEEJG();
			String idPeticionInfoAAPP = null;
			for (ScsEejgPeticionesBean scsEejgPeticionesBean : listaPeticiones) {
				try {
					idPeticionInfoAAPP = null;
					if (numeroErrores < NUM_ERROR_CONEXION) {
						idPeticionInfoAAPP = solicitudesEEJG.solicitudPeticionInfoAAPP(scsEejgPeticionesBean);	
					}					
					scsEejgPeticionesBean.setIdSolicitud(idPeticionInfoAAPP);
					
					if (idPeticionInfoAAPP != null) {
						scsEejgPeticionesBean.setEstado(ScsEejgPeticionesBean.EEJG_ESTADO_ESPERA);
						scsEejgPeticionesBean.setFechaSolicitud("SYSDATE");
					} 
					
				} catch (AxisFault e) {
					numeroErrores++;
					if (e.getCause() instanceof ConnectException) {												
						ClsLogging.writeFileLogError("No hay conexión con el WS EEJG", e, 3);
					} else {
						ClsLogging.writeFileLogError("Error con el servidor", e, 3);
					}
				}catch (Throwable e) {					
					numeroErrores++;
					ClsLogging.writeFileLogError("Error No esperado", new Exception(e), 3);
					//e.printStackTrace();
					
				} finally {
					scsEejgPeticionesBean.setNumeroIntentosSolicitud(scsEejgPeticionesBean.getNumeroIntentosSolicitud() + 1);
					if (idPeticionInfoAAPP == null && scsEejgPeticionesBean.getNumeroIntentosSolicitud() >= NUMERO_REINTENTOS_SOLICITUD) {
						scsEejgPeticionesBean.setEstado(ScsEejgPeticionesBean.EEJG_ESTADO_ERROR_SOLICITUD);
					}
					
					scsPeticionesAdm.updateDirect(scsEejgPeticionesBean);					
				}
			}
		}

	}

	/**
	 * 
	 * @param usrBean
	 * @throws Exception 
	 */
	private void trataSolicitudesPendientes(UsrBean usrBean) throws Exception {
		GenParametrosAdm admParametros = new GenParametrosAdm(usrBean);		
		double horas = Double.parseDouble(admParametros.getValor(ScsEejgPeticionesBean.INSTITUCION_PARAMETROS_EEJG, "SCS", "EEJG_HORAS_PETICION_CONSULTA", ""));		
		
		ScsEejgPeticionesAdm scsPeticionesAdm = new ScsEejgPeticionesAdm(usrBean);		
		List<ScsEejgPeticionesBean> listaPeticiones = scsPeticionesAdm.getSolicitudesPendientes(horas);
		int numeroErrores = 0;
		
		if (listaPeticiones != null && listaPeticiones.size()>0 ) {
			
			int NUM_ERROR_CONEXION = Integer.parseInt(admParametros.getValor(ScsEejgPeticionesBean.INSTITUCION_PARAMETROS_EEJG, "SCS", "EEJG_NUMERO_ERRORES_CONEXION", ""));
			int NUMERO_REINTENTOS_CONSULTA = Integer.parseInt(admParametros.getValor(ScsEejgPeticionesBean.INSTITUCION_PARAMETROS_EEJG, "SCS", "EEJG_NUMERO_REINTENTOS_CONSULTA", ""));
			int NUMERO_REINTENTOS_PENDIENTEINFO = Integer.parseInt(admParametros.getValor(ScsEejgPeticionesBean.INSTITUCION_PARAMETROS_EEJG, "SCS", "EEJG_NUMERO_REINTENTOS_PENDIENTE_INFO", ""));
			
			//Iniciamos cambiando a estado pendiente
			scsPeticionesAdm.updatePeticionesIniciadas();
			SolicitudesEEJG solicitudesEEJG = new SolicitudesEEJG();		
			int idXML = -1;
			for (ScsEejgPeticionesBean scsEejgPeticionesBean : listaPeticiones) {
				idXML = -1;
				try {					
					if (numeroErrores < NUM_ERROR_CONEXION) {
						idXML = solicitudesEEJG.consultaInfoAAPP(scsEejgPeticionesBean);
					}	
				} catch (AxisFault e) {								
					numeroErrores++;
					if (e.getCause() instanceof ConnectException) {												
						ClsLogging.writeFileLogError("No hay conexión con el WS EEJG", e, 3);					
					} else {
						ClsLogging.writeFileLogError("Error con el servidor", e, 3);
					}
				} catch (Throwable e) {
					numeroErrores++;
					ClsLogging.writeFileLogError("Error No esperado", new Exception(e), 3);
				} finally {
					//si ya tenía respuesta incrementamos el contador de pendienteInfo y si no el de Intentos consulta
					if (scsEejgPeticionesBean.getIdXml() != null) {
						scsEejgPeticionesBean.setNumeroIntentosPendienteInfo(scsEejgPeticionesBean.getNumeroIntentosPendienteInfo() + 1);
					} else {
						scsEejgPeticionesBean.setNumeroIntentosConsulta(scsEejgPeticionesBean.getNumeroIntentosConsulta() + 1);
					}
					if (idXML > -1) {	
						//el estado lo pone el método consultaInfoAAPP
						scsEejgPeticionesBean.setIdXml(idXML);
						scsEejgPeticionesBean.setFechaConsulta("SYSDATE");
					} else {
						if (scsEejgPeticionesBean.getNumeroIntentosConsulta() >= NUMERO_REINTENTOS_CONSULTA ||
								scsEejgPeticionesBean.getNumeroIntentosPendienteInfo() >= NUMERO_REINTENTOS_PENDIENTEINFO) {					
							scsEejgPeticionesBean.setEstado(ScsEejgPeticionesBean.EEJG_ESTADO_ERROR_CONSULTA_INFO);
						}
					}
					scsPeticionesAdm.updateDirect(scsEejgPeticionesBean);
				}
			}
			
		}
	}
	
}