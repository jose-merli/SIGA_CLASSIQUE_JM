package com.siga.servlets;

import java.io.IOException;
import java.util.Date;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.CenInstitucionBean;
import com.siga.facturacion.Facturacion;

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
		Vector vInstituciones;
		Date momentoInicio, momentoActual;
		long minutosTranscurridos, minutosQueFaltanAntesDeSiguienteAutomaticoFacturacion, minutosEntreCadaProcesoAutomaticoFacturacion;
		boolean alMenosUnafacturacionProgramadaEncontrada;

		try {
			// obteniendo el parametro que guarda el tiempo entre cada proceso automatico de facturacion
			ReadProperties properties = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
			String sIntervalo = properties.returnProperty("facturacion.programacionAutomatica.tiempo.ciclo");
			if (sIntervalo == null || sIntervalo.trim().equals("")) {
				sIntervalo = "0";
			}
			minutosEntreCadaProcesoAutomaticoFacturacion = Long.parseLong(sIntervalo) * 60 * 1000;

			usr = new UsrBean();
			CenInstitucionAdm admInstitucion = new CenInstitucionAdm(usr); // Esta controlado que no necesita usrbean
			vInstituciones = admInstitucion.obtenerInstitucionesAlta();
			if (vInstituciones == null) {
				vInstituciones = new Vector();
			}

			// guardando el momento de inicio
			momentoInicio = new Date();

			// Para cada colegio
			for (int i = 0; i < vInstituciones.size(); i++) {

				try {
					beanInstitucion = (CenInstitucionBean) vInstituciones.elementAt(i);
					idinstitucion = beanInstitucion.getIdInstitucion().toString();
					usr = UsrBean.UsrBeanAutomatico(idinstitucion);
					fac = new Facturacion(usr);
					
					// Para cada proceso comprobamos que no termina el plazo de tiempo de esta ejecucion.
					// Si el plazo va a llegar pronto o ha llegado, terminamos el proceso por completo pq un nuevo proceso se encargara

					ClsLogging.writeFileLogWithoutSession(" ---------- INICIO GENERACION DE FACTURAS. INSTITUCION: " + idinstitucion, 3);
					do {
						alMenosUnafacturacionProgramadaEncontrada = fac.procesarFacturas(idinstitucion, usr);
						momentoActual = new Date();
						minutosTranscurridos = (momentoActual.getTime() - momentoInicio.getTime()) / (1000 * 60);
						minutosQueFaltanAntesDeSiguienteAutomaticoFacturacion = minutosEntreCadaProcesoAutomaticoFacturacion - minutosTranscurridos;
					} while (alMenosUnafacturacionProgramadaEncontrada && 
							minutosTranscurridos < minutosEntreCadaProcesoAutomaticoFacturacion && 
							minutosQueFaltanAntesDeSiguienteAutomaticoFacturacion > 5); 
					ClsLogging.writeFileLogWithoutSession(" ---------- OK GENERACION DE FACTURAS. INSTITUCION: " + idinstitucion, 3);

					ClsLogging.writeFileLogWithoutSession(" ---------- INICIO CONFIRMACION DE FACTURAS. INSTITUCION: " + idinstitucion, 3);
					do {
						alMenosUnafacturacionProgramadaEncontrada = fac.confirmarProgramacionesFacturasInstitucion(request, idinstitucion, usr);
						momentoActual = new Date();
						minutosTranscurridos = (momentoActual.getTime() - momentoInicio.getTime()) / (1000 * 60);
						minutosQueFaltanAntesDeSiguienteAutomaticoFacturacion = minutosEntreCadaProcesoAutomaticoFacturacion - minutosTranscurridos;
					} while (alMenosUnafacturacionProgramadaEncontrada && 
							minutosTranscurridos < minutosEntreCadaProcesoAutomaticoFacturacion && 
							minutosQueFaltanAntesDeSiguienteAutomaticoFacturacion > 5); 
					ClsLogging.writeFileLogWithoutSession(" ---------- OK CONFIRMACION DE FACTURAS. INSTITUCION: " + idinstitucion, 3);

					if (minutosTranscurridos < minutosEntreCadaProcesoAutomaticoFacturacion && 
							minutosQueFaltanAntesDeSiguienteAutomaticoFacturacion > 5) { 
						ClsLogging.writeFileLogWithoutSession(" ---------- INICIO REEENVIO DE FACTURAS. INSTITUCION: " + idinstitucion, 3);
						// Este proceso no deberia ejecutarse ya que se ejecuta en el mismo momento en que lo pide el usuario (proceso individual)
						fac.generarPDFsYenviarFacturasProgramacion(request, "" + idinstitucion);
						ClsLogging.writeFileLogWithoutSession(" ---------- OK REEENVIO DE FACTURAS. INSTITUCION: " + idinstitucion, 3);
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