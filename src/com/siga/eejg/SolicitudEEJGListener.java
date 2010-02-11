/**
 * 
 */
package com.siga.eejg;

import java.net.ConnectException;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.management.InstanceNotFoundException;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.servlet.ServletContextEvent;

import org.apache.axis.AxisFault;
import org.redabogacia.www.pjgpra.wspjgpra.RespuestaSolicitudPeticionInfoAAPP.Informacion;
import org.redabogacia.www.pjgpra.wspjgpra.RespuestaSolicitudPeticionInfoAAPP.Respuesta;
import org.redabogacia.www.pjgpra.wspjgpra.RespuestaSolicitudPeticionInfoAAPP.RespuestaSolicitudPeticionInfoAAPP;
import org.w3c.dom.Document;

import weblogic.management.timer.Timer;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ReadProperties;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.AxisObjectSerializerDeserializer;
import com.siga.Utilidades.SIGAReferences;
import com.siga.beans.eejg.ScsEejgPeticionesAdm;
import com.siga.beans.eejg.ScsEejgPeticionesBean;
import com.siga.beans.eejg.ScsEejgXmlAdm;
import com.siga.beans.eejg.ScsEejgXmlBean;
import com.siga.servlets.SIGAContextListenerAdapter;
import com.sun.org.apache.xerces.internal.dom.DocumentImpl;

/**
 * @author angelcpe
 *
 */
public class SolicitudEEJGListener extends SIGAContextListenerAdapter implements NotificationListener {

	private Timer timer = null;
	private Integer idNotificacion;
	private String nombreProceso = "SolicitudEEJGListener";
	private long intervalo = -1;
		
	/**
	 * 
	 */
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		intervalo = Long.parseLong(rp.returnProperty("eejg.timerEEJG"));
		
		timer = new Timer();
        timer.addNotificationListener(this, null, nombreProceso);        
        idNotificacion = timer.addNotification(nombreProceso, nombreProceso, this, new Date(), intervalo);
        timer.start();
    }
	
	/**
	 * 
	 */
	public void contextDestroyed(ServletContextEvent event) {

		if (timer != null) {
			if (timer.isActive()) {
				timer.stop();
			}
			try {
				timer.removeNotification(idNotificacion);
			} catch (InstanceNotFoundException e) {
				ClsLogging.writeFileLogError("Error en SolicitudEEJGListener.contextDestroyed", e, 3);
			}
		}
	}

	/**
	 * 
	 */
	public void handleNotification(Notification notification, Object handback) {
				
		try {
			UsrBean usrBean = new UsrBean();
			usrBean.setUserName(String.valueOf(ClsConstants.USUMODIFICACION_AUTOMATICO));   
			
			trataPeticionesIniciadas(usrBean);
			trataSolicitudesPendientes(usrBean);			
		
		} catch (Exception e) {
			ClsLogging.writeFileLogError("Error en SolicitudEEJGListener.handleNotification", e, 3);
		}
	}



	/**
	 * 
	 * @param usrBean
	 * @throws Exception 
	 */
	private void trataPeticionesIniciadas(UsrBean usrBean) throws Exception {
		
		ReadProperties rp = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		int NUM_ERROR_CONEXION = Integer.parseInt(rp.returnProperty("eejg.numeroErroresConexion"));
		int NUMERO_REINTENTOS_SOLICITUD = Integer.parseInt(rp.returnProperty("eejg.numeroReintentosSolicitud"));
		
		ScsEejgPeticionesAdm scsPeticionesAdm = new ScsEejgPeticionesAdm(usrBean);
		List<ScsEejgPeticionesBean> listaPeticiones = scsPeticionesAdm.getPeticionesIniciadas();

		int numeroErrores = 0;
		if (listaPeticiones != null) {
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
		
		if (listaPeticiones != null) {
			
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

	/**
	 * 
	 * @param scsEejgXmlAdm
	 * @param scsEejgPeticionesBean
	 * @throws ClsExceptions
	 */
	/*private void trataErrorConexion(ScsEejgXmlAdm scsEejgXmlAdm, ScsEejgPeticionesBean scsEejgPeticionesBean) throws ClsExceptions {
		Hashtable hash = new Hashtable();
		hash.put(ScsEejgXmlBean.C_IDXML, scsEejgXmlAdm.getNuevoIdXml());
		hash.put(ScsEejgXmlBean.C_IDPETICION, scsEejgPeticionesBean.getIdPeticion());
		hash.put(ScsEejgXmlBean.C_ESTADO, ScsEejgPeticionesBean.EEJG_ESTADO_ERROR);
		hash.put(ScsEejgXmlBean.C_XML, generaXMLError());
		hash.put(ScsEejgXmlBean.C_ENVIORESPUESTA, ScsEejgXmlBean.RESPUESTA);						
		
		scsEejgXmlAdm.insert(hash);	
		
	}*/

	/**
	 * 
	 */
	private String generaXMLError() {
		String xml = null;
//		String tipoError = "ES001";
//		String descripcionError = "Error de conexión con el servidor de webservice";
//		String resultadoComunicacion = "FALSE";
//		String idPeticionInfoAAPP = "-1";
//		Long idOperacionSolicitud = new Long(0);
//		
//		Respuesta respuesta = new Respuesta(idOperacionSolicitud, idPeticionInfoAAPP, resultadoComunicacion, tipoError , descripcionError);
//		String id = "ID_RES";
//		Informacion informacion = new Informacion(respuesta, id);
//		RespuestaSolicitudPeticionInfoAAPP respuestaSolicitudPeticionInfoAAPP = new RespuestaSolicitudPeticionInfoAAPP(informacion, null);
//		try {
//			xml = AxisObjectSerializerDeserializer.serializeAxisObject(respuestaSolicitudPeticionInfoAAPP, false, false);
//		} catch (Exception e) {
//			ClsLogging.writeFileLogError("Error al crear el xml", e, 3);
//		}
		return xml;
	}
}
