package com.siga.servlets;

import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.management.InstanceNotFoundException;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.servlet.ServletContextEvent;

import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import weblogic.management.timer.Timer;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsLogging;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.beans.AdmContadorAdm;
import com.siga.beans.AdmContadorBean;
import com.siga.beans.CenInstitucionBean;

public final class SIGASvlProcesoAutomaticoContadores extends SIGAContextListenerAdapter implements NotificationListener 
{
    private Timer timer;
    private Integer idNotificacion;
    private long lIntervalo = 1;
    private String lHoraEjecucion = "00:00";
    private String sNombreProceso = "ProcesoAutomaticoContadores";
    private boolean reinicio = true;

    public void contextInitialized(ServletContextEvent event)
    {
    	super.contextInitialized(event);
        //ClsLogging.writeFileLogWithoutSession("", 3);
    	//ClsLogging.writeFileLogWithoutSession("", 3);
        ClsLogging.writeFileLogWithoutSession("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>", 3);
        ClsLogging.writeFileLogWithoutSession(" Arrancando Notificaciones JMX.", 3);

	    ReadProperties properties= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//        ReadProperties properties=new ReadProperties("SIGA.properties");
        String sIntervaloAux = properties.returnProperty("administracion.reconfiguracionContadores.tiempo.ciclo");
        String sIntervalo = sIntervaloAux;
        
        String sHoraEjecucionAux = properties.returnProperty("administracion.reconfiguracionContadores.horaEjecucion");
        String sHoraEjecucion = sHoraEjecucionAux;
        
        
        if (sIntervalo==null || sIntervalo.trim().equals(""))
        {
            sIntervalo="0";
        }

        if (sIntervalo.equals("0"))
        {
	        ClsLogging.writeFileLogWithoutSession("    - Notificación \"" + sNombreProceso + "\" no arrancada.", 3);
	        ClsLogging.writeFileLogWithoutSession("    - Intervalo de ejecución: Erróneo (" + sIntervaloAux + ").", 3);
        }

        else
        {
	        lIntervalo = Long.parseLong(sIntervalo)*60*1000;
	        lHoraEjecucion = sHoraEjecucion;
	        
	        timer = new Timer();
	        timer.addNotificationListener(this, null, sNombreProceso);

	        Date timerTriggerAt = new Date((new Date()).getTime() + 60000L);
	        idNotificacion = timer.addNotification(sNombreProceso, sNombreProceso, this, timerTriggerAt, lIntervalo);

	        timer.start();

	        ClsLogging.writeFileLogWithoutSession("    - Notificación \"" + sNombreProceso + "\" arrancada.", 3);
	        ClsLogging.writeFileLogWithoutSession("    - Intervalo de ejecución: " + sIntervalo + " minuto(s).", 3);
        }

        ClsLogging.writeFileLogWithoutSession(" Notificaciones JMX arrancadas.", 3);
        ClsLogging.writeFileLogWithoutSession("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>", 3);
        //ClsLogging.writeFileLogWithoutSession("", 3);
        //ClsLogging.writeFileLogWithoutSession("", 3);
    }

    public void contextDestroyed(ServletContextEvent event)
    {
    	//ClsLogging.writeFileLogWithoutSession("", 3);
    	//ClsLogging.writeFileLogWithoutSession("", 3);
        ClsLogging.writeFileLogWithoutSession("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>", 3);
        ClsLogging.writeFileLogWithoutSession(" Destruyendo notificaciones JMX.", 3);

        try
		{
			if (timer!=null) {
				if (timer.isActive())
					timer.stop();
				timer.removeNotification(idNotificacion);
			}

            ClsLogging.writeFileLogWithoutSession("    - Notificación \"" + sNombreProceso + "\" parada.", 3);
            ClsLogging.writeFileLogWithoutSession(" Notificaciones JMX destruídas.", 3);
            ClsLogging.writeFileLogWithoutSession("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>", 3);
            //ClsLogging.writeFileLogWithoutSession("", 3);
            //ClsLogging.writeFileLogWithoutSession("", 3);
		}

		catch (InstanceNotFoundException e)
		{
            e.printStackTrace();
        }
    }

    public void handleNotification(Notification notif, Object handback)
    {
		String sProximaEjecucion = "Próxima ejecución dentro de " + (lIntervalo/60/1000) + " minuto(s).";

        ClsLogging.writeFileLogWithoutSession(" - INVOCANDO...  >>>  Ejecutando Notificación: \"" + sNombreProceso + "\".", 3);
	
		
		try
		{
			java.text.SimpleDateFormat sdfNew = new java.text.SimpleDateFormat("HH:mm");
			Date dateParam= sdfNew.parse(lHoraEjecucion);	
			
			String horaHoy = UtilidadesBDAdm.getHoraBD();
			Date dateNow = sdfNew.parse(horaHoy);
			if (dateNow.before(dateParam) && reinicio == false){
				reinicio = true;
			}
			
			if (dateNow.after(dateParam) && reinicio == true ) {
			    ClsLogging.writeFileLogWithoutSession(" - OK.  >>>  Es la hora de ejecución: \"" + sNombreProceso + "\" " + sProximaEjecucion, 3);
			    //BNS INC_06950_SIGA
			    reinicio = false;
			    UsrBean usr = UsrBean.UsrBeanAutomatico(String.valueOf(AppConstants.IDINSTITUCION_2000));
			    AdmContadorAdm admContadorAdm = new AdmContadorAdm(usr);
			    StringBuilder query = new StringBuilder();
			    query.append(" WHERE ("+AdmContadorBean.C_FECHARECONFIGURACION+" IS NOT NULL AND "+AdmContadorBean.C_FECHARECONFIGURACION+" < SYSDATE)");
			    query.append(" AND "+AdmContadorBean.C_IDINSTITUCION+" IN ( ");
			    query.append(" SELECT DISTINCT "+CenInstitucionBean.C_IDINSTITUCION+" FROM "+CenInstitucionBean.T_NOMBRETABLA);
			    query.append(" WHERE "+CenInstitucionBean.C_FECHAENPRODUCCION+" IS NOT NULL)");
			    Vector<AdmContadorBean> vContadoresAreconfigurar = admContadorAdm.select(query.toString());
			    if (vContadoresAreconfigurar != null){
			    	Iterator<AdmContadorBean> iteraContadoresAreconfigurar = vContadoresAreconfigurar.iterator();
			    	while(iteraContadoresAreconfigurar.hasNext()){
			    		AdmContadorBean contadorAreconfigurar = iteraContadoresAreconfigurar.next();			    		
			    		try{
				    		// Cambiamos los valores del contador por los valores de la reconfiguración
				    		contadorAreconfigurar.setPrefijo(contadorAreconfigurar.getReconfiguracionPrefijo()); 
				    		contadorAreconfigurar.setContador(Long.valueOf(contadorAreconfigurar.getReconfiguracionContador()));
				    		contadorAreconfigurar.setSufijo(contadorAreconfigurar.getReconfiguracionSufijo());
				    		
				    		// Actualizamos la fecha de reconfiguración para el siguiente año
				    		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
				    		Date fechaReconfiguracion = sdf.parse(contadorAreconfigurar.getFechaReconfiguracion());
				    		Calendar cal = Calendar.getInstance();
				            cal.setTime(fechaReconfiguracion);
				            cal.add(Calendar.YEAR, 1);
				            fechaReconfiguracion = cal.getTime();
				    		contadorAreconfigurar.setFechaReconfiguracion(sdf.format(fechaReconfiguracion));
				    		//Actualizamos el prefijo y el sufijo sumando uno a la última cifra encontrada (si la hay)
				    		String sPrefijo = 	contadorAreconfigurar.getReconfiguracionPrefijo();
				    		String sSufijo 	= 	contadorAreconfigurar.getReconfiguracionSufijo();
				    		sPrefijo = calcularSiguienteXfijo(sPrefijo);
				    		sSufijo = calcularSiguienteXfijo(sSufijo);
				    		contadorAreconfigurar.setReconfiguracionPrefijo(sPrefijo);
				    		contadorAreconfigurar.setReconfiguracionSufijo(sSufijo);
				    		
				    		if (admContadorAdm.update(contadorAreconfigurar))
				    			ClsLogging.writeFileLogWithoutSession(" - OK.    >>>  CONTADOR RECONFIGURADO: "+"idInstitucion="+contadorAreconfigurar.getIdinstitucion()+ " idContador="+contadorAreconfigurar.getIdContador(), 3);
				    		else{
				    			ClsLogging.writeFileLogWithoutSession(" - ERROR. >>>  ERROR AL ACTUALIZAR EL CONTADOR idInstitucion="+contadorAreconfigurar.getIdinstitucion()+ " idContador="+contadorAreconfigurar.getIdContador(), 3);
				    			reinicio = true;
				    		}
				    		
			    		} catch (Exception e) {
			    			reinicio = true;
			    			ClsLogging.writeFileLogWithoutSession(" - ERROR. >>>  ERROR AL RECONFIGURAR CONTADOR: "+e.getMessage(), 3);
			    		    e.printStackTrace();
			    		}
			    	}
			    }
				/*
			    Object[] paramIn = new Object[0];
				ClsMngBBDD.callPLProcedure("{call PROC_RECONFIGURACIONCONTADOR (?,?)}", 2, paramIn);
				*/
			}
		    ClsLogging.writeFileLogWithoutSession(" - OK.  >>>  Ejecutando Notificación: \"" + sNombreProceso + "\" ejecutada OK. " + sProximaEjecucion, 3);
		} catch(Exception e) {
			reinicio = true;
		    ClsLogging.writeFileLogWithoutSession(" - Notificación \"" + sNombreProceso + "\" ejecutada ERROR. " + sProximaEjecucion, 3);
		    e.printStackTrace();
		}
    }
    
    private String calcularSiguienteXfijo(String xFijo){
    	StringBuilder sbXfijo = new StringBuilder(xFijo);
		
		int i = sbXfijo.length() - 1;
		Integer startDigitIndex = 0;
		Integer endDigitIndex = 0;
		String sNumero = "";
		boolean bOk = false;
		while (i >= 0 && !bOk){
			if (Character.isDigit(sbXfijo.charAt(i))){
				if ("".equals(sNumero)){
					endDigitIndex = i;
				}
				sNumero = String.valueOf(sbXfijo.charAt(i)) + sNumero;
			} else if (!"".equals(sNumero)){
				startDigitIndex = i+1;
				bOk = true;
			}
			i--;
		}
		String sPrefijo = "";
		sNumero = ((Integer)(Integer.valueOf(sNumero) + 1)).toString();
		String sSufijo = "";
		if (startDigitIndex > 0){
			sPrefijo = sbXfijo.substring(0, startDigitIndex);
		}
		if (endDigitIndex < sbXfijo.length() - 1){
			sSufijo = sbXfijo.substring(endDigitIndex + 1);
		}
		return sPrefijo + sNumero + sSufijo;
    }
    
}