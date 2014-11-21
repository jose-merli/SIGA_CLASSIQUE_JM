package com.siga.servlets;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.CenInstitucionBean;
import com.siga.facturacion.Facturacion;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>SchlumbergerSema: </p>
 * @SchlumbergerSema
 * @version 1.0
 */

public class SIGASvlProcesoFacturacion extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private String sNombreProceso = "ProcesoAutomaticoFacturacion";
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doPost(request,response);
    }
    
    /**
     * Notas Jorge PT 118:
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //ClsLogging.writeFileLogWithoutSession("", 3);
    	//ClsLogging.writeFileLogWithoutSession("", 3);
    	//ClsLogging.writeFileLogWithoutSession("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>", 3);
    	//ClsLogging.writeFileLogWithoutSession(" Arrancando Notificaciones JMX (LLAMADA DIRECTA).", 3);


    	//ClsLogging.writeFileLogWithoutSession(" ", 3);
        //ClsLogging.writeFileLogWithoutSession(" - INVOCANDO... >>>  Ejecutando Notificación: \"" + sNombreProceso + "\".", 3);

   		try {
   			CenInstitucionAdm admInstitucion = new CenInstitucionAdm(new UsrBean()); // Este usrbean esta controlado que no se necesita el valor

   			Vector vInstituciones = admInstitucion.obtenerInstitucionesAlta();
   			
   			Facturacion fac = null;
   			if (vInstituciones!=null) {
   				
   				CenInstitucionBean beanInstitucion = null;
   				for (int i=0; i<vInstituciones.size(); i++) {
   					
   					try {
   						beanInstitucion = (CenInstitucionBean)vInstituciones.elementAt(i);
   	   					UsrBean usr = UsrBean.UsrBeanAutomatico(beanInstitucion.getIdInstitucion().toString());
   	   					
   	   					ClsLogging.writeFileLogWithoutSession(" ---------- GENERACION DE FACTURAS. INSTITUCION: "+beanInstitucion.getIdInstitucion(), 3);
   	   					
   						Date ini = new Date();
   						
   	   					Facturacion.procesarFacturas(beanInstitucion.getIdInstitucion().toString(), usr);
   						
   						Date fin = new Date();
   						// Control de transacciones largas
   						if ((fin.getTime()-ini.getTime())>3000) {
   						    Date dat = Calendar.getInstance().getTime();
   						    SimpleDateFormat sdfLong = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); 
   						    String fecha = sdfLong.format(dat);
   						    ClsLogging.writeFileLog(fecha + ",==> SIGA: Control de tiempo de transacciones (>3 seg.),Proceso facturacion,"+beanInstitucion.getIdInstitucion().toString()+",automatico,"+new Long((fin.getTime()-ini.getTime())).toString(),10);
   						}
   	   					
   	   					ClsLogging.writeFileLogWithoutSession(" ---------- INICIO CONFIRMACION DE FACTURAS (PDF Y ENVIOS) ", 3);

   	   					fac = new Facturacion(usr);
   	   					fac.confirmarProgramacionesFacturasInstitucion(request,beanInstitucion.getIdInstitucion().toString(),usr);
   	   					
   	   					ClsLogging.writeFileLogWithoutSession(" ---------- INICIO REEENVIO DE FACTURAS ", 3);
   	   					//No deberia hacerse por que ya existe un proceso indivdual de confirmacion que realiza la generacion de pdf y los envios no se hacen en background
   	   					fac.generarPDFsYenviarFacturasProgramacion(request,""+beanInstitucion.getIdInstitucion());
   	   					
   	   					ClsLogging.writeFileLogWithoutSession(" ---------- OK GENERACION Y CONFIRMACION DE FACTURAS. INSTITUCION: "+beanInstitucion.getIdInstitucion(), 3);
   	   					
					} catch (Exception e) {
						if(beanInstitucion != null && beanInstitucion.getIdInstitucion()!=null)
							ClsLogging.writeFileLogWithoutSession(" ---------- ERROR GENERACION DE FACTURAS. INSTITUCION: "+beanInstitucion.getIdInstitucion(), 3);
						else
							ClsLogging.writeFileLogWithoutSession(" ---------- ERROR GENERACION DE FACTURAS.", 3);
					}

   				}
   			}
   	        
   			ClsLogging.writeFileLogWithoutSession(" OK proceso facturación automática ", 3);
   	        response.setContentType("text/html");
   	        java.io.PrintWriter out = response.getWriter();
   	        out.println("OK proceso facturación automática");
   	        out.close();

   		} catch(Exception e) {
   	        response.setContentType("text/html");
   	        java.io.PrintWriter out = response.getWriter();
   	        out.println("ERROR en proceso facturación automática");
   	        out.close();
   			
   			ClsLogging.writeFileLogWithoutSession(" - Notificación \"" + sNombreProceso + "\" ejecutada ERROR. : " + e.toString() , 3);
   		    e.printStackTrace();
   		} finally {
   	        // reiniciar el timer para que se vuelva a ejecutar el proceso automatico de facturacion pasado un tiempo
   	        new SIGASvlProcesoAutomaticoFacturacion().iniciarTimer();
   		}
    }
}