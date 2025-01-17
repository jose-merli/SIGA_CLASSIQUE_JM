package com.siga.eejg;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.List;

import org.apache.axis.AxisFault;
import org.redabogacia.sigaservices.app.AppConstants.EEJG_ESTADO;
import org.redabogacia.sigaservices.app.AppConstants.MODULO;
import org.redabogacia.sigaservices.app.AppConstants.PARAMETRO;
import org.redabogacia.sigaservices.app.exceptions.BusinessException;
import org.redabogacia.sigaservices.app.services.gen.GenParametrosService;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.eejg.ScsEejgPeticionesAdm;
import com.siga.beans.eejg.ScsEejgPeticionesBean;
import com.siga.ws.CajgConfiguracion;

import es.satec.businessManager.BusinessManager;

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
				boolean isValidadoCorrectamente = true;
				try {
					idPeticionInfoAAPP = null;
					if (numeroErrores < NUM_ERROR_CONEXION) {
						idPeticionInfoAAPP = solicitudesEEJG.solicitudPeticionInfoAAPP(scsEejgPeticionesBean);
						numeroErrores = 0;
					}					
					scsEejgPeticionesBean.setIdSolicitud(idPeticionInfoAAPP);
					
					if (idPeticionInfoAAPP != null) {
						scsEejgPeticionesBean.setEstado((int)EEJG_ESTADO.ESPERA.getId());
						scsEejgPeticionesBean.setFechaSolicitud("SYSDATE");
						scsEejgPeticionesBean.setMsgError("");
					} 
					
				} catch (BusinessException e) {					
					ClsLogging.writeFileLogError("Errores controlados en validaciones de campos ampliados",e,3);
					scsEejgPeticionesBean.setMsgError(e.getMessage());
					isValidadoCorrectamente = false;
					
				} catch (AxisFault e) {					
					if (e.getCause() instanceof ConnectException) {
						numeroErrores++;
						ClsLogging.writeFileLogError("No hay conexi�n con el WS EEJG", e, 3);
					} else {
						ClsLogging.writeFileLogError("Error con el servidor", e, 3);
					}
				}catch (Throwable e) {
					ClsLogging.writeFileLogError("Error No esperado", new Exception(e), 3);
					
				} finally {
					scsEejgPeticionesBean.setNumeroIntentosSolicitud(scsEejgPeticionesBean.getNumeroIntentosSolicitud() + 1);
					if(!isValidadoCorrectamente){
						scsEejgPeticionesBean.setEstado((int)EEJG_ESTADO.ERROR_SOLICITUD.getId());
						
					}
					else if (idPeticionInfoAAPP == null && scsEejgPeticionesBean.getNumeroIntentosSolicitud() >= NUMERO_REINTENTOS_SOLICITUD) {
						scsEejgPeticionesBean.setEstado((int)EEJG_ESTADO.ERROR_SOLICITUD.getId());
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
		GenParametrosService genParametrosService = (GenParametrosService) BusinessManager.getInstance().getService(GenParametrosService.class);
		double horas = Double.parseDouble(admParametros.getValor(ScsEejgPeticionesBean.INSTITUCION_PARAMETROS_EEJG, "SCS", "EEJG_HORAS_PETICION_CONSULTA", ""));		
		
		ScsEejgPeticionesAdm scsPeticionesAdm = new ScsEejgPeticionesAdm(usrBean);		
		List<ScsEejgPeticionesBean> listaPeticiones = scsPeticionesAdm.getSolicitudesPendientes(horas);
		int numeroErrores = 0;
		HashMap<Integer, Boolean> intercambioEconomicoActivadoHash = new HashMap<Integer, Boolean>();
		if (listaPeticiones != null && listaPeticiones.size()>0) {
			
			int NUM_ERROR_CONEXION = Integer.parseInt(admParametros.getValor(ScsEejgPeticionesBean.INSTITUCION_PARAMETROS_EEJG, "SCS", "EEJG_NUMERO_ERRORES_CONEXION", ""));
			int NUMERO_REINTENTOS_CONSULTA = Integer.parseInt(admParametros.getValor(ScsEejgPeticionesBean.INSTITUCION_PARAMETROS_EEJG, "SCS", "EEJG_NUMERO_REINTENTOS_CONSULTA", ""));
			int NUMERO_REINTENTOS_PENDIENTEINFO = Integer.parseInt(admParametros.getValor(ScsEejgPeticionesBean.INSTITUCION_PARAMETROS_EEJG, "SCS", "EEJG_NUMERO_REINTENTOS_PENDIENTE_INFO", ""));
			
			//Iniciamos cambiando a estado pendiente
			scsPeticionesAdm.updatePeticionesIniciadas();
			
			
			SolicitudesEEJG solicitudesEEJG = new SolicitudesEEJG();		
			SolicitudesEEJGInformacionCompleta solicitudesEEJGInformacionCompleta = new SolicitudesEEJGInformacionCompleta();
			int idXML = -1;
			for (ScsEejgPeticionesBean scsEejgPeticionesBean : listaPeticiones) {
				
				boolean isActivadoIntercambioEconomico = false;
				if(!intercambioEconomicoActivadoHash.containsKey(scsEejgPeticionesBean.getIdInstitucion())){
					String urlEnvioInformeEconomico = genParametrosService.getValorParametroWithNull(scsEejgPeticionesBean.getIdInstitucion().shortValue(),PARAMETRO.INFORMEECONOMICO_WS_URL,MODULO.ECOM);
					isActivadoIntercambioEconomico = urlEnvioInformeEconomico!=null && !urlEnvioInformeEconomico.equalsIgnoreCase("");
					
				}else{
					isActivadoIntercambioEconomico = intercambioEconomicoActivadoHash.get(scsEejgPeticionesBean.getIdInstitucion());
					
				}
				intercambioEconomicoActivadoHash.put(scsEejgPeticionesBean.getIdInstitucion(), isActivadoIntercambioEconomico);
				scsEejgPeticionesBean.setActivadoIntercambioEconomico(isActivadoIntercambioEconomico);
				
				
				
				idXML = -1;
				try {					
					if (numeroErrores < NUM_ERROR_CONEXION) {
						if(isActivadoIntercambioEconomico){
							idXML = solicitudesEEJGInformacionCompleta.consultaInformacionCompletaAAPP(scsEejgPeticionesBean);
						}else{
							idXML = solicitudesEEJG.consultaInfoAAPP(scsEejgPeticionesBean);
						}
						numeroErrores = 0;
					}	
				} catch (AxisFault e) {
					if (e.getCause() instanceof ConnectException) {
						numeroErrores++;
						ClsLogging.writeFileLogError("No hay conexi�n con el WS EEJG", e, 3);					
					} else {
						ClsLogging.writeFileLogError("Error con el servidor", e, 3);
					}
				} catch (Throwable e) {					
					ClsLogging.writeFileLogError("Error No esperado", new Exception(e), 3);
				} finally {
					//si ya ten�a respuesta incrementamos el contador de pendienteInfo y si no el de Intentos consulta
					if (scsEejgPeticionesBean.getIdXml() != null) {
						scsEejgPeticionesBean.setNumeroIntentosPendienteInfo(scsEejgPeticionesBean.getNumeroIntentosPendienteInfo() + 1);
					} else {
						scsEejgPeticionesBean.setNumeroIntentosConsulta(scsEejgPeticionesBean.getNumeroIntentosConsulta() + 1);
					}
					if (idXML > -1) {	
						//el estado lo pone el m�todo consultaInfoAAPP
						scsEejgPeticionesBean.setIdXml(idXML);						
						if (CajgConfiguracion.getTipoCAJG(scsEejgPeticionesBean.getIdInstitucion()) == CajgConfiguracion.TIPO_CAJG_WEBSERVICE_PAMPLONA) {
//							enviaPDF(scsEejgPeticionesBean, usrBean);
						}
						scsEejgPeticionesBean.setFechaConsulta("SYSDATE");
					} else {
						
						if (scsEejgPeticionesBean.getNumeroIntentosConsulta() >= NUMERO_REINTENTOS_CONSULTA ||
								scsEejgPeticionesBean.getNumeroIntentosPendienteInfo() >= NUMERO_REINTENTOS_PENDIENTEINFO) {					
							scsEejgPeticionesBean.setEstado((int)EEJG_ESTADO.ERROR_CONSULTA_INFO.getId());
						}
					}
					scsPeticionesAdm.updateDirect(scsEejgPeticionesBean);
				}
			}
		}
		
	}

}