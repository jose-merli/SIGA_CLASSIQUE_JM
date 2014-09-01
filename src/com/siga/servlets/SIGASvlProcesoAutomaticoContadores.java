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
import com.siga.Utilidades.UtilidadesString;
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
			/* TODO El siguiente metodo controla la hora de reconfiguracion de contadores. Utiliza un sistema complejo y eficaz pero debil porque 
			 * funciona siempre que 00:00 + lIntervalo < lHoraEjecucion
			 * Por ejemplo,     con 00:00 + 30         < 01:30
			 * 
			 * Creo que deberiamos cambiarlo y hacerlo mas sencillo: no usar el parametro lHoraEjecucion. 
			 * En vez de eso ejecutar siempre la consulta existente de contadores pendientes de reconfigurar
			 * Ademas, de esta forma, se facilitaria la reconfiguracion de contadores ya que siempre se ejecutaria cuando pongamos la fecha de hoy a un contador
			 */
			java.text.SimpleDateFormat sdfNew = new java.text.SimpleDateFormat("HH:mm");
			Date dateParam= sdfNew.parse(lHoraEjecucion);	
			
			String horaHoy = UtilidadesBDAdm.getHoraBD();
			Date dateNow = sdfNew.parse(horaHoy);
			// Se comprueba si la hora actual (cuando se esta ejecutando) es anterior a la fijada por parametro
			// Es decir, si el parametro = 01:30 y la hora actual = 08:00 entonces no se hace nada 
			// Solo cuando la hora actual llega a 00:00 o poco despues entonces se activa la bandera de reinicio
			if (dateNow.before(dateParam) && reinicio == false)
			{
				reinicio = true;
				// En la siguiente iteracion se comprueba y solo se ejecuta todo el resto del proceso pasada la hora fijada por parametro
				// Es decir, si el parametro = 01:30 y la hora actual = 01:00, entonces no se hace nada
				// Solo se empieza a ejecutar pasada la hora del parametro, por ejemplo, si la hora actual = 02:00
			} else if (dateNow.after(dateParam) && reinicio == true ) 
			{
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
				    		
				    		//Actualizamos el prefijo y el sufijo sumando uno a la última cifra encontrada (si la hay)
				    		String sPrefijo = 	contadorAreconfigurar.getReconfiguracionPrefijo();
				    		String sSufijo 	= 	contadorAreconfigurar.getReconfiguracionSufijo();
				    		sPrefijo = calcularSiguienteXfijo(sPrefijo,fechaReconfiguracion);
				    		sSufijo = calcularSiguienteXfijo(sSufijo,fechaReconfiguracion);
				    		contadorAreconfigurar.setReconfiguracionPrefijo(sPrefijo);
				    		contadorAreconfigurar.setReconfiguracionSufijo(sSufijo);
				    		
				    		
				    		Calendar cal = Calendar.getInstance();
				            cal.setTime(fechaReconfiguracion);
				            cal.add(Calendar.YEAR, 1);
				            fechaReconfiguracion = cal.getTime();
				    		contadorAreconfigurar.setFechaReconfiguracion(sdf.format(fechaReconfiguracion));
				    		
				    		
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
    
    private static String calcularSiguienteXfijo(String xFijo,Date fechaReconfiguracion){
    	Calendar cal = Calendar.getInstance();
        cal.setTime(fechaReconfiguracion);
        int year = cal.get(Calendar.YEAR)-2000;
        //Buscamos si esta el año en cuantro digitos
        xFijo = UtilidadesString .replaceAllIgnoreCase(xFijo, ""+year, ""+(year+1));
    	return xFijo;
    	
    }
    
}