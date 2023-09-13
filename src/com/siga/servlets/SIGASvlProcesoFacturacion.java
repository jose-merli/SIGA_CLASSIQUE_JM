package com.siga.servlets;

import java.io.IOException;
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
import com.siga.facturacion.form.ConfirmarFacturacionForm;

/**
 * Recibe la llamada del proceso automatico de facturacion y reparte las tareas
 * 
 * @author adrianag Revisado el 21/11/2014
 */
public class SIGASvlProcesoFacturacion extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private String sNombreProceso = "ProcesoAutomaticoFacturacion";

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		UsrBean usr;
		Facturacion fac;
		CenInstitucionBean beanInstitucion = null;
		String idinstitucion = "";
		Vector<CenInstitucionBean> vInstituciones;
		Date momentoInicio, momentoActual;
		long minutosTranscurridos, minutosQueFaltanAntesDeSiguienteAutomaticoFacturacion, minutosEntreCadaProcesoAutomaticoFacturacion;
		long duracionMediaFacturacion = 5;
		boolean alMenosUnafacturacionProgramadaEncontrada;

		try {
			// obteniendo el parametro que guarda el tiempo entre cada proceso automatico de facturacion
			minutosEntreCadaProcesoAutomaticoFacturacion = SIGASvlProcesoAutomaticoFacturacion.getDuracionIntervaloEnminutos();
			duracionMediaFacturacion = SIGASvlProcesoAutomaticoFacturacion.getDuracionMediaFacturacionEnminutos();
			minutosQueFaltanAntesDeSiguienteAutomaticoFacturacion = minutosEntreCadaProcesoAutomaticoFacturacion;
			minutosTranscurridos = 0;

			usr = new UsrBean();
			fac = new Facturacion(usr);
			vInstituciones = fac.getInstitucionesConFacturasPendientes(usr,true);
			if (vInstituciones == null || vInstituciones.size()==0) {
				vInstituciones = new Vector<CenInstitucionBean>();
				ClsLogging.writeFileLogWithoutSession(" ---------- NO EXISTEN INSTITUCIONES CON GENERACION DE FACTURAS PENDIENTES.", 3);
			}

			// guardando el momento de inicio
			momentoInicio = new Date();

			// Para cada colegio
			for (int i = 0; minutosQueFaltanAntesDeSiguienteAutomaticoFacturacion > duracionMediaFacturacion && 
					i < vInstituciones.size(); i++) {

				try {
					beanInstitucion = (CenInstitucionBean) vInstituciones.elementAt(i);
					idinstitucion = beanInstitucion.getIdInstitucion().toString();
					// Para cada proceso comprobamos que no termina el plazo de tiempo de esta ejecucion.
					// Si el plazo va a llegar pronto o ha llegado, terminamos el proceso por completo pq un nuevo proceso se encargara
					usr = UsrBean.UsrBeanAutomatico(idinstitucion);
					fac = new Facturacion(usr);
					ClsLogging.writeFileLogWithoutSession(" ---------- INICIO GENERACION DE FACTURACION. INSTITUCION: " + idinstitucion, 3);
					do {
						alMenosUnafacturacionProgramadaEncontrada = fac.procesarFacturas(idinstitucion, usr);
						
						momentoActual = new Date();
						minutosTranscurridos = (momentoActual.getTime() - momentoInicio.getTime()) / (1000 * 60);
						minutosQueFaltanAntesDeSiguienteAutomaticoFacturacion = minutosEntreCadaProcesoAutomaticoFacturacion - minutosTranscurridos;
					} while (alMenosUnafacturacionProgramadaEncontrada && 
							minutosQueFaltanAntesDeSiguienteAutomaticoFacturacion > duracionMediaFacturacion); 
					ClsLogging.writeFileLogWithoutSession(" ---------- OK GENERACION DE FACTURAS. INSTITUCION: " + idinstitucion, 3);

					if (minutosQueFaltanAntesDeSiguienteAutomaticoFacturacion > duracionMediaFacturacion) {
						
						ClsLogging.writeFileLogWithoutSession(" ---------- INICIO CONFIRMACION DE FACTURACION. INSTITUCION: " + idinstitucion, 3);
						do {
							alMenosUnafacturacionProgramadaEncontrada = fac.confirmarProgramacionesFacturasInstitucion(request, idinstitucion, usr);
							
							momentoActual = new Date();
							minutosTranscurridos = (momentoActual.getTime() - momentoInicio.getTime()) / (1000 * 60);
							minutosQueFaltanAntesDeSiguienteAutomaticoFacturacion = minutosEntreCadaProcesoAutomaticoFacturacion - minutosTranscurridos;
						} while (alMenosUnafacturacionProgramadaEncontrada && 
								minutosQueFaltanAntesDeSiguienteAutomaticoFacturacion > duracionMediaFacturacion); 
						ClsLogging.writeFileLogWithoutSession(" ---------- OK CONFIRMACION DE FACTURACION. INSTITUCION: " + idinstitucion, 3);
					}

					if (minutosQueFaltanAntesDeSiguienteAutomaticoFacturacion > duracionMediaFacturacion) {
						ClsLogging.writeFileLogWithoutSession(" ---------- INICIO COMPROBACION TRASPASO DE FACTURAS. INSTITUCION: " + idinstitucion, 3);
						fac.comprobacionTraspasoFacturas(request, idinstitucion);
						ClsLogging.writeFileLogWithoutSession(" ---------- OK COMPROBACION TRASPASO DE FACTURAS. INSTITUCION: " + idinstitucion, 3);
						
					}
					
					if (minutosQueFaltanAntesDeSiguienteAutomaticoFacturacion > duracionMediaFacturacion) {
						
						ClsLogging.writeFileLogWithoutSession(" ---------- INICIO GENERACION DE FACTURAS(PDF). INSTITUCION: " + idinstitucion, 3);
						// Este proceso no deberia ejecutarse ya que se ejecuta en el mismo momento en que lo pide el usuario (proceso individual)
						//SOLO SE EJECUTARA ESTE PROCESO SI NO QUEDA NINGUNA GENERACION DE PROGRAMACION PENDIENTE
						Vector institucionesPendientesProgramacion = fac.getInstitucionesConFacturasPendientes(usr,false);
						if(institucionesPendientesProgramacion!=null && institucionesPendientesProgramacion.size()>0 ) {
							ClsLogging.writeFileLogWithoutSession(" ---------- SE POSPONE LA GENERACION DE FACTURAS(PDF) AL HABER GENERACION DE PROGRAMACIONES PENDIENTES. INSTITUCION: " + idinstitucion, 3);
						}else {
							fac.generarPDFsYenviarFacturasProgramacion(request, "" + idinstitucion);
							momentoActual = new Date();
							minutosTranscurridos = (momentoActual.getTime() - momentoInicio.getTime()) / (1000 * 60);
							minutosQueFaltanAntesDeSiguienteAutomaticoFacturacion = minutosEntreCadaProcesoAutomaticoFacturacion - minutosTranscurridos;
							ClsLogging.writeFileLogWithoutSession(" ---------- OK  INICIO GENERACION DE FACTURAS(PDF). INSTITUCION: " + idinstitucion, 3);
						}
					}
					
					if (minutosQueFaltanAntesDeSiguienteAutomaticoFacturacion > duracionMediaFacturacion) {
						
						ClsLogging.writeFileLogWithoutSession(" ---------- INICIO ENVIO DE FACTURAS. INSTITUCION: " + idinstitucion, 3);
						// Este proceso no deberia ejecutarse ya que se ejecuta en el mismo momento en que lo pide el usuario (proceso individual)
						Vector institucionesPendientesProgramacion = fac.getInstitucionesConFacturasPendientes(usr,false);
						if(institucionesPendientesProgramacion!=null && institucionesPendientesProgramacion.size()>0 ) {
							ClsLogging.writeFileLogWithoutSession(" ---------- SE POSPONE EL ENVIO DE FACTURAS AL HABER GENERACION DE PROGRAMACIONES PENDIENTES. INSTITUCION: " + idinstitucion, 3);
						}else {
							fac.generarEnviosFacturasPendientes(request, "" + idinstitucion);
							ClsLogging.writeFileLogWithoutSession(" ---------- OK ENVIO DE FACTURAS. INSTITUCION: " + idinstitucion, 3);
						}
					}
					
					
					
					

				} catch (Exception e) {
					ClsLogging.writeFileLogWithoutSession(" ---------- ERROR GENERACION DE FACTURAS. INSTITUCION: "	+ idinstitucion, 3);
				}

			}

			ClsLogging.writeFileLogWithoutSession(" OK proceso facturación automática ", 3);
			response.setContentType("text/html");
			java.io.PrintWriter out = response.getWriter();
			out.println("OK proceso facturación automática");
			out.close();

		} catch (Exception e) {
			try {
				response.setContentType("text/html");
				java.io.PrintWriter out = response.getWriter();
				out.println("ERROR en proceso facturación automática");
				out.close();
			} catch (Exception e2) {
				;
			} finally {
				ClsLogging.writeFileLogWithoutSession(" - Notificación \"" + sNombreProceso + "\" ejecutada ERROR. : " + e.toString(), 3);
				e.printStackTrace();
			}
		}
	}
}