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
import com.siga.facturacion.Facturacion;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>SchlumbergerSema: </p>
 * @SchlumbergerSema
 * @version 1.0
 */

public class SIGASvlProcesoRapidoFacturacion extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private String sNombreProceso = "ProcesoRapidoFacturacion";
    
    // version de una sola llamada
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doPost(request,response);
    }

    /**
     * Notas Jorge PT 118:
     */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			CenInstitucionAdm admInstitucion = new CenInstitucionAdm(new UsrBean()); // Este usrbean esta controlado que no se necesita el valor
			Vector vInstituciones = admInstitucion.obtenerInstitucionesAlta();
			Facturacion fac = null;
			if (vInstituciones != null) {
				CenInstitucionBean beanInstitucion = null;
				for (int i = 0; i < vInstituciones.size(); i++) {
					beanInstitucion = (CenInstitucionBean) vInstituciones.elementAt(i);
					UsrBean usr = UsrBean.UsrBeanAutomatico(beanInstitucion.getIdInstitucion().toString());

					// JASU: PROCESO DE GENERACION DE PDFS DE FACTURAS
					try {
						ClsLogging.writeFileLogWithoutSession(" ---------- INICIO GENERACION PDF DE FACTURAS SOLO", 10);
						fac = new Facturacion(usr);
						fac.generarZipFacturasSolo(request, "" + beanInstitucion.getIdInstitucion(), usr);
						ClsLogging.writeFileLogWithoutSession(" ---------- OK GENERACION PDF DE FACTURAS SOLO. INSTITUCION: " + beanInstitucion.getIdInstitucion(), 10);
						
					} catch (Exception e) {
						if (beanInstitucion != null && beanInstitucion.getIdInstitucion() != null)
							ClsLogging.writeFileLogError(" ---------- ERROR GENERACION PDF DE FACTURAS. INSTITUCION: " + beanInstitucion.getIdInstitucion(), e, 3);
						else
							ClsLogging.writeFileLogError(" ---------- ERROR GENERACION PDF DE FACTURAS.", e, 3);
					}
					
					// RGG 01/06/2009: PROCESO DE EXPORTACION DE CONTABILIDAD
					try {
						FacRegistroFichContaAdm admContabilidad = new FacRegistroFichContaAdm(usr);
						ClsLogging.writeFileLogWithoutSession(" ---------- INICIO DE EXPORTACION A CONTABILIDAD", 10);
						admContabilidad.contabilidadesProgramadas(request, "" + beanInstitucion.getIdInstitucion());
						ClsLogging.writeFileLogWithoutSession(" ---------- OK EXPORTACION A CONTABILIDAD. INSTITUCION: " + beanInstitucion.getIdInstitucion(), 10);
						
					} catch (Exception e) {
						if (beanInstitucion != null && beanInstitucion.getIdInstitucion() != null)
							ClsLogging.writeFileLogError(" ---------- ERROR EXPORTACION A CONTABILIDAD. INSTITUCION: " + beanInstitucion.getIdInstitucion(), e, 3);
						else
							ClsLogging.writeFileLogError(" ---------- ERROR EXPORTACION A CONTABILIDAD.", e, 3);
					}
				}
			}

			ClsLogging.writeFileLogWithoutSession(" OK proceso automático rápido ", 3);
			response.setContentType("text/html");
			java.io.PrintWriter out = response.getWriter();
			out.println("OK proceso automático rápido");
			out.close();

		} catch (Exception e) {
			response.setContentType("text/html");
			java.io.PrintWriter out = response.getWriter();
			out.println("ERROR en proceso automático rápido");
			out.close();

			ClsLogging.writeFileLogWithoutSession(" - Notificación \"" + sNombreProceso + "\" ejecutada ERROR. : " + e.toString(), 3);
			e.printStackTrace();
		}
	}
}