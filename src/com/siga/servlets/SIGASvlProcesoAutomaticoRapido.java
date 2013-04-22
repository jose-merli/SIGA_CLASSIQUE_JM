package com.siga.servlets;


import java.net.URL;
import java.util.Date;
import java.util.Hashtable;

import javax.management.InstanceNotFoundException;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.servlet.ServletException;

import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import weblogic.management.timer.Timer;

import com.atos.utils.ClsLogging;
import com.siga.beans.MasterBean;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>SchlumbergerSema: </p>
 * @SchlumbergerSema
 * @version 1.0
 */

public class SIGASvlProcesoAutomaticoRapido extends SIGAServletAdapter implements NotificationListener {
	private static final long serialVersionUID = 1859932068515262676L;

	static private Timer timer;
    private Integer idNotificacion;
	private long lIntervalo = 1;
	static private String sNombreProceso = "ProcesoAutomaticoRapido";
    private String urlSiga = "";
    static final public String procesoRapido = "SIGASvlProcesoRapido";
    static final public String procesoGeneracionEnvio = "SIGASvlProcesoGeneracionEnvio";
    static final public String procesoIndividualConfirmacionFacturacion = "SIGASvlProcesoIndividualConfirmacionFacturacion";
	static final public String htNombreProceso = "proceso";
	static final public String htNombreDatosHashtable = "datosHashtable";
	
	
    
	//Global vars
	public void init() throws ServletException {
		try{
			super.init();
	
			ClsLogging.writeFileLogWithoutSession("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>", 3);
			ClsLogging.writeFileLogWithoutSession(" Arrancando Notificaciones JMX.", 3);
	
		    ReadProperties properties= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
			String sIntervaloAux = properties.returnProperty("facturacion.procesoRapido.tiempo.ciclo");
			String sIntervalo = sIntervaloAux;
	
		    urlSiga = properties.returnProperty("general.urlSIGA");
		    
			timer = new Timer();
			timer.addNotificationListener(this, null, sNombreProceso);
			if (sIntervalo==null || sIntervalo.trim().equals("")){
				sIntervalo="0";
			}
			
			if (sIntervalo.equals("0")){
				ClsLogging.writeFileLogWithoutSession("    - Notificación \"" +
						"" + sNombreProceso + "\" no arrancada. " +
						"(Este proceso no requiere ser arrancado: requiere llamada directa desde la interfaz)", 3);
			}
			
			else {
				//Al ser este un proceso automático "rápido" el intervalo se define como segundos
				//no como minutos, que es lo que se utiliza para el resto de procesos
				lIntervalo = Long.parseLong(sIntervalo)*1000;
				
				Date timerTriggerAt = new Date((new Date()).getTime() + 60000L);
				idNotificacion = timer.addNotification(sNombreProceso, sNombreProceso, this, timerTriggerAt, lIntervalo);
	
				timer.start();
				
				ClsLogging.writeFileLogWithoutSession("    - Notificación \"" + sNombreProceso + "\" arrancada.", 3);
				ClsLogging.writeFileLogWithoutSession("    - Intervalo de ejecución: " + sIntervalo + " segundo(s).", 3);
			}
	
			ClsLogging.writeFileLogWithoutSession(" Notificaciones JMX arrancadas.", 3);
			ClsLogging.writeFileLogWithoutSession("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>", 3);
		} catch(Exception e){
			ClsLogging.writeFileLogWithoutSession(" - Notificación \"" + sNombreProceso + "\" arrancado ERROR. ", 3);
			e.printStackTrace();
		} 
	}

	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#destroy()
	 */
	public void destroy() {
		//ClsLogging.writeFileLogWithoutSession("", 3);
		//ClsLogging.writeFileLogWithoutSession("", 3);
		ClsLogging.writeFileLogWithoutSession("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>", 3);
		ClsLogging.writeFileLogWithoutSession(" Destruyendo notificaciones JMX.", 3);

		try {
			if (timer!=null) {
				if (timer.isActive())
					timer.stop();
				if(idNotificacion!=null)
					timer.removeNotification(idNotificacion);
			}

			ClsLogging.writeFileLogWithoutSession("    - Notificación \"" + sNombreProceso + "\" parada.", 3);
			ClsLogging.writeFileLogWithoutSession(" Notificaciones JMX destruídas.", 3);
			ClsLogging.writeFileLogWithoutSession("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>", 3);
			//ClsLogging.writeFileLogWithoutSession("", 3);
			//ClsLogging.writeFileLogWithoutSession("", 3);
		} catch (InstanceNotFoundException e) {
			ClsLogging.writeFileLogWithoutSession(" - Notificación \"" + sNombreProceso + "\" parada Instancia no encontrada. ERROR. ", 3);
			e.printStackTrace();
		} catch(Exception e){
			ClsLogging.writeFileLogWithoutSession(" - Notificación \"" + sNombreProceso + "\" parada ERROR. ", 3);
			e.printStackTrace();
		}
	}

	public void handleNotification(Notification notif, Object handback){
		Object userData = (Object)notif.getUserData();
		
		//Como actualmente se necesita un unico String solo esta implementado esto.
		//cuando se llegue a un acuerdo sobre que parametro incluir habra que eliminar 
		//la pregunta sobre que clase es
		if (userData instanceof String) {
			String proceso = (String) userData;
			ClsLogging.writeFileLogWithoutSession(" - INVOCANDO...  >>>  Ejecutando Notificación: \"" + sNombreProceso + "\".", 3);

			try {
			    // invocamos al servlet
				URL url = new URL(urlSiga+proceso+".svrl");
				Object ret = url.getContent();
				
				ClsLogging.writeFileLogWithoutSession(" - OK.  >>>  Ejecutando Notificación: \"" + sNombreProceso + "\".", 3);
			}
			catch(Exception e){
				ClsLogging.writeFileLogWithoutSession(" - Notificación \"" + sNombreProceso + "\" ejecutada ERROR. ", 3);
				e.printStackTrace();
			}
			
		}else if (userData instanceof Hashtable) {
			Hashtable hashUserData = (Hashtable) userData;
            String proceso = (String)hashUserData.get(SIGASvlProcesoAutomaticoRapido.htNombreProceso);
            Hashtable<String, String> datosHashtable= (Hashtable<String, String>) hashUserData.get(SIGASvlProcesoAutomaticoRapido.htNombreDatosHashtable);
            try {
                // invocamos al servlet
                StringBuffer urlAndParameters = new StringBuffer();
                urlAndParameters.append(urlSiga);
                urlAndParameters.append(proceso);
                urlAndParameters.append(".svrl");
                boolean isFirst = true;
                if(datosHashtable!=null && datosHashtable.size()>0){
                    for(String parameterName:datosHashtable.keySet()){
                        if(isFirst){
                            urlAndParameters.append("?");
                            isFirst = false;
                        }
                        else{
                            urlAndParameters.append("&");
                        }
                        urlAndParameters.append(parameterName);
                        urlAndParameters.append("=");
                        urlAndParameters.append(datosHashtable.get(parameterName));
                       
                    }
                }
               
               
                URL url = new URL(urlAndParameters.toString());
                Object ret = url.getContent();
			ClsLogging.writeFileLogWithoutSession(" - Notificación \"" + sNombreProceso + "\" con hashtable como parametro no implementada. ", 3);
            }catch(Exception e){
				ClsLogging.writeFileLogWithoutSession(" - Notificación \"" + sNombreProceso + "\" ejecutada ERROR. ", 3);
				e.printStackTrace();
			}
		}else if (userData instanceof MasterBean) {
			MasterBean masterBean = (MasterBean) userData;
			ClsLogging.writeFileLogWithoutSession(" - Notificación \"" + sNombreProceso + "\" con MasterBean como parametro no implementada. ", 3);
			
			
		}else{
			ClsLogging.writeFileLogWithoutSession(" - Notificación \"" + sNombreProceso + "\" con "+userData.getClass().getName()+" parametro no implementada. ", 3);
		}

		
	} 

	/**
	 * Crea una notificación. Se podria Sobreescribir este metodo cuantas veces quisiramos
	 * 
	 * 
	 */
	static public void NotificarAhora(String proceso){
		try{
			if (timer != null){
				if (!timer.isActive()){
					timer.start();
				}
				Date timerTriggerAt = new Date((new Date()).getTime() + 1000L);
				//El parametro que se pasa como parametro en tercer lugar(proceso),puede ser cualquier objeto
				//Este parametro se establece en el atributo UserData del Objeto javax.management.Notification.
				//Actualmente este atributo es un String con el nombre del proceso. En caso de que se necesitara se podrian
				//pasar objetos tales como maps o beans que guarden mas informacion
				timer.addNotification(sNombreProceso, sNombreProceso, proceso, timerTriggerAt);
			}
		} catch(Exception e){
			ClsLogging.writeFileLogWithoutSession(" - Notificación \"" + sNombreProceso + "\" notificando ERROR. ", 3);
			e.printStackTrace();
		}
	}
	static public void NotificarAhora(Hashtable htDatos){
		try{
			if (timer != null){
				if (!timer.isActive()){
					timer.start();
				}
				Date timerTriggerAt = new Date((new Date()).getTime() + 1000L);
				
				timer.addNotification(sNombreProceso, sNombreProceso, htDatos, timerTriggerAt);
			}
		} catch(Exception e){
			ClsLogging.writeFileLogWithoutSession(" - Notificación \"" + sNombreProceso + "\" notificando ERROR. ", 3);
			e.printStackTrace();
		}
	}
	static public void NotificarAhora(MasterBean bean){
		try{
			if (timer != null){
				if (!timer.isActive()){
					timer.start();
				}
				
				Date timerTriggerAt = new Date((new Date()).getTime() + 1000L);
				
				timer.addNotification(sNombreProceso, sNombreProceso, bean, timerTriggerAt);
			}
		} catch(Exception e){
			ClsLogging.writeFileLogWithoutSession(" - Notificación \"" + sNombreProceso + "\" notificando ERROR. ", 3);
			e.printStackTrace();
		}
	}
	
}

