package com.siga.gratuita.schedulers;

import java.net.ConnectException;
import java.util.List;

import org.apache.axis.AxisFault;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsLogging;
import com.atos.utils.ReadProperties;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.SIGAReferences;
import com.siga.beans.eejg.ScsEejgPeticionesAdm;
import com.siga.beans.eejg.ScsEejgPeticionesBean;
import com.siga.eejg.SolicitudesEEJG;
import com.siga.general.SIGAException;

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
		
		ReadProperties rp = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		int NUM_ERROR_CONEXION = Integer.parseInt(rp.returnProperty("eejg.numeroErroresConexion"));
		int NUMERO_REINTENTOS_SOLICITUD = Integer.parseInt(rp.returnProperty("eejg.numeroReintentosSolicitud"));
		ScsEejgPeticionesAdm scsPeticionesAdm = new ScsEejgPeticionesAdm(usrBean);
		List<ScsEejgPeticionesBean> listaPeticiones = scsPeticionesAdm.getPeticionesIniciadas();
		
		int numeroErrores = 0;
		if (listaPeticiones != null && listaPeticiones.size()>0 ) {
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
					ClsLogging.writeFileLogError("Error No esperado",new SIGAException("messages.general.error"), 3);
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
		ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		int horas = Integer.parseInt(rp.returnProperty("eejg.horasPeticionConsulta"));
		int NUM_ERROR_CONEXION = Integer.parseInt(rp.returnProperty("eejg.numeroErroresConexion"));
		int NUMERO_REINTENTOS_CONSULTA = Integer.parseInt(rp.returnProperty("eejg.numeroReintentosConsulta"));
		
		ScsEejgPeticionesAdm scsPeticionesAdm = new ScsEejgPeticionesAdm(usrBean);
		
		List<ScsEejgPeticionesBean> listaPeticiones = scsPeticionesAdm.getSolicitudesPendientes(horas);
		int numeroErrores = 0;
		
		if (listaPeticiones != null && listaPeticiones.size()>0 ) {
			//Iniciamos cambiando a estado pendiente
			scsPeticionesAdm.updatePeticionesIniciadas();
			SolicitudesEEJG solicitudesEEJG = new SolicitudesEEJG();		
			int idXML = -1;
			for (ScsEejgPeticionesBean scsEejgPeticionesBean : listaPeticiones) {
				idXML = -1;
				try {					
					if (numeroErrores < NUM_ERROR_CONEXION) {
						idXML = solicitudesEEJG.consultaInfoAAPP(scsEejgPeticionesBean); 
						if (idXML > -1) {		
							scsEejgPeticionesBean.setIdXml(idXML);
							scsEejgPeticionesBean.setEstado(ScsEejgPeticionesBean.EEJG_ESTADO_FINALIZADO);
							scsEejgPeticionesBean.setFechaConsulta("SYSDATE");
						}
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
					ClsLogging.writeFileLogError("Error No esperado",new SIGAException("messages.general.error"), 3);
					//e.printStackTrace();
					
				} finally {
					scsEejgPeticionesBean.setNumeroIntentosConsulta(scsEejgPeticionesBean.getNumeroIntentosConsulta() + 1);
					if (idXML == -1 && scsEejgPeticionesBean.getNumeroIntentosConsulta() >= NUMERO_REINTENTOS_CONSULTA) {
						scsEejgPeticionesBean.setEstado(ScsEejgPeticionesBean.EEJG_ESTADO_ERROR_CONSULTA_INFO);
					}
					scsPeticionesAdm.updateDirect(scsEejgPeticionesBean);
				}
			}
			
		}
	}
	

}
