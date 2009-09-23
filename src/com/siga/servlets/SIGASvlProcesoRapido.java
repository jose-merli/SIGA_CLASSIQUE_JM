package com.siga.servlets;

import java.io.IOException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.CenInstitucionBean;
import com.siga.beans.FacRegistroFichContaAdm;
import com.siga.beans.FcsFacturacionJGAdm;
import com.siga.facturacion.Facturacion;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>SchlumbergerSema: </p>
 * @SchlumbergerSema
 * @version 1.0
 */

public class SIGASvlProcesoRapido extends HttpServlet {

     private String sNombreProceso = "ProcesoAutomaticoRapido";

    
// version de una sola llamada
    
    public void init() throws ServletException {

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doPost(request,response);
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //ClsLogging.writeFileLogWithoutSession("", 3);
    	//ClsLogging.writeFileLogWithoutSession("", 3);
    	//ClsLogging.writeFileLogWithoutSession("<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>", 3);
    	//ClsLogging.writeFileLogWithoutSession(" Arrancando Notificaciones JMX (LLAMADA DIRECTA).", 3);


    	//ClsLogging.writeFileLogWithoutSession(" ", 3);
        //ClsLogging.writeFileLogWithoutSession(" - INVOCANDO... >>>  Ejecutando Notificaci�n: \"" + sNombreProceso + "\".", 3);

   		try
   		{
   			CenInstitucionAdm admInstitucion = new CenInstitucionAdm(new UsrBean()); // Este usrbean esta controlado que no se necesita el valor
               
   			

   			Vector vInstituciones = admInstitucion.obtenerInstitucionesAlta();
   			
   			Facturacion fac = null;
   			if (vInstituciones!=null)
   			{
   				CenInstitucionBean beanInstitucion = null;
   				for (int i=0; i<vInstituciones.size(); i++)
   				{
					beanInstitucion = (CenInstitucionBean)vInstituciones.elementAt(i);
   					UsrBean usr			= UsrBean.UsrBeanAutomatico(beanInstitucion.getIdInstitucion().toString());
   					
   					// JASU: PROCESO DE GENERACION DE PDFS DE FACTURAS
   					try {
   	   					ClsLogging.writeFileLogWithoutSession(" ---------- INICIO GENERACION PDF DE FACTURAS SOLO", 10);
   	   					fac = new Facturacion(usr);
   	   					fac.generarZipFacturasSolo(request,""+beanInstitucion.getIdInstitucion(),usr);
   	   					ClsLogging.writeFileLogWithoutSession(" ---------- OK GENERACION PDF DE FACTURAS SOLO. INSTITUCION: "+beanInstitucion.getIdInstitucion(), 10);
					} catch (Exception e) {
						if(beanInstitucion != null && beanInstitucion.getIdInstitucion()!=null)
							ClsLogging.writeFileLogError(" ---------- ERROR GENERACION PDF DE FACTURAS. INSTITUCION: "+beanInstitucion.getIdInstitucion(),e, 3);
						else
							ClsLogging.writeFileLogError(" ---------- ERROR GENERACION PDF DE FACTURAS.",e, 3);
					}

					// RGG 01/06/2009: PROCESO DE EXPORTACION DE CONTABILIDAD
   					try {
   			   			FacRegistroFichContaAdm admContabilidad = new FacRegistroFichContaAdm(usr);
   	   					ClsLogging.writeFileLogWithoutSession(" ---------- INICIO DE EXPORTACION A CONTABILIDAD", 10);
   	   					admContabilidad.contabilidadesProgramadas(request,""+beanInstitucion.getIdInstitucion());
   	   					ClsLogging.writeFileLogWithoutSession(" ---------- OK EXPORTACION A CONTABILIDAD. INSTITUCION: "+beanInstitucion.getIdInstitucion(), 10);
					} catch (Exception e) {
						if(beanInstitucion != null && beanInstitucion.getIdInstitucion()!=null)
							ClsLogging.writeFileLogError(" ---------- ERROR EXPORTACION A CONTABILIDAD. INSTITUCION: "+beanInstitucion.getIdInstitucion(),e, 3);
						else
							ClsLogging.writeFileLogError(" ---------- ERROR EXPORTACION A CONTABILIDAD.",e, 3);
					}

					// RGG 15/07/2009: PROCESO DE EJECUCION DE FACTURACION SJCS
   					try {
   			   			FcsFacturacionJGAdm facAdm = new FcsFacturacionJGAdm(usr);
   	   					ClsLogging.writeFileLogWithoutSession(" ---------- INICIO DE EJECUCION FACTURACION SJCS", 10);
   	   					facAdm.facturacionesSJCSProgramadas(""+beanInstitucion.getIdInstitucion(),usr);
   	   					ClsLogging.writeFileLogWithoutSession(" ---------- OK EJECUCION FACTURACION SJCS. INSTITUCION: "+beanInstitucion.getIdInstitucion(), 10);
					} catch (Exception e) {
						if(beanInstitucion != null && beanInstitucion.getIdInstitucion()!=null)
							ClsLogging.writeFileLogError(" ---------- ERROR EJECUCION FACTURACION SJCSD. INSTITUCION: "+beanInstitucion.getIdInstitucion(),e, 3);
						else
							ClsLogging.writeFileLogError(" ---------- ERROR EJECUCION FACTURACION SJCS.",e, 3);
					}

   				}
   			}
   	        
   			ClsLogging.writeFileLogWithoutSession(" OK proceso autom�tico r�pido ", 3);
   	        response.setContentType("text/html");
   	        java.io.PrintWriter out = response.getWriter();
   	        out.println("OK proceso autom�tico r�pido");
   	        out.close();

   		}

   		catch(Exception e)
   		{
   	        response.setContentType("text/html");
   	        java.io.PrintWriter out = response.getWriter();
   	        out.println("ERROR en proceso autom�tico r�pido");
   	        out.close();

   			
   			ClsLogging.writeFileLogWithoutSession(" - Notificaci�n \"" + sNombreProceso + "\" ejecutada ERROR. : " + e.toString() , 3);
   		    e.printStackTrace();
   		}

    }

}


