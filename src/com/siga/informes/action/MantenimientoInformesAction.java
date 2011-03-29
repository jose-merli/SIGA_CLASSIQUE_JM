package com.siga.informes.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.UsrBean;
import com.siga.beans.AdmTipoFiltroInformeBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.informes.InformeFichaFacturacion;
import com.siga.informes.InformeFichaPago;
import com.siga.informes.InformePersonalizable;
import com.siga.informes.form.MantenimientoInformesForm;

/**
 * Clase para el Mantenimiento de los Informes generados en formato PDF.
 * Nota: estos informes son llamados directamente desde las JSPs, 
 * ya que se trata de pantallas que no tienen Action propio 
 * y solo se dedican a generar el informe
 * 
 * @author david.sanchezp
 */
public class MantenimientoInformesAction extends MasterAction {

	/**
	 * Método que atiende a las peticiones. Segun el valor del parametro modo
	 * del formulario ejecuta distintas acciones
	 */
	public ActionForward executeInternal(ActionMapping mapping,
			ActionForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {

		String mapDestino = "exception";
		MasterForm miForm = null;

		try {
			miForm = (MasterForm) formulario;
			String modo = miForm.getModo();

			if (modo != null) {
				// GENERAR INFORMES
				if (modo.equalsIgnoreCase("generarFichaPago")) {
					InformeFichaPago inf = new InformeFichaPago();
					mapDestino = inf.generarFichaPago(mapping, formulario,
							request, response);
					return mapping.findForward(mapDestino);
				} else if (modo.equalsIgnoreCase("generarFichaFacturacion")) {
					InformeFichaFacturacion inf = new InformeFichaFacturacion();
					mapDestino = inf.generarFichaFacturacion(mapping,
							formulario, request, response);
					return mapping.findForward(mapDestino);
				} else if (modo.equalsIgnoreCase("generarInformeFacturacion")) {
					InformeFichaFacturacion inf = new InformeFichaFacturacion();
					mapDestino = inf.generarInformeFacturacion(mapping,
							formulario, request, response);
					return mapping.findForward(mapDestino);
				} else if (modo.equalsIgnoreCase("generarCertificadoPago")) {
					ArrayList<HashMap<String, String>> filtrosInforme = 
							obtenerDatosFormCertificadoPago(formulario, request);
					InformePersonalizable inf = new InformePersonalizable();
					mapDestino = inf.generarInformes(formulario, request, 
							InformePersonalizable.I_CERTIFICADOPAGO, filtrosInforme);
					return mapping.findForward(mapDestino);/**/
				} else if (miForm.getModo().equalsIgnoreCase(
						"generarInformeIRPF")) {
					return mapping.findForward(this.generarInformeIRPF(mapping,
							miForm, request, response));

				} else if (modo.equalsIgnoreCase("descargar")) {
					mapDestino = descargar(mapping, miForm, request, response);
					return mapping.findForward(mapDestino);
				} else {
					return super.executeInternal(mapping, formulario, request,
							response);
				}
			} else
				return super.executeInternal(mapping, formulario, request,
						response);
		} catch (Exception e) {
			throw new SIGAException("messages.general.error", e,
					new String[] { "modulo.facturacionSJCS" });
		}
	}

	/**
	 * Abre la JSP de inicio
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
		try {
			request.setAttribute("anyoIRPF", String.valueOf(Calendar
					.getInstance().get(Calendar.YEAR) - 1));

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.sjcs" },
					e, null);
		}

		return "inicio";
	}

	/**
	 * Este metodo reenvia la llamada a los Informes Genericos, 
	 *   ya que el Informe IRPF se genera en formato DOC
	 */
	protected String generarInformeIRPF(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {
		String salida = "generaInformeIRPF";
		return salida;
	}

	/**
	 * Obtiene los filtros del formulario para generar el Certificado de Pago
	 */
	private ArrayList<HashMap<String, String>> obtenerDatosFormCertificadoPago(
			ActionForm formulario, HttpServletRequest request) throws SIGAException
	{
		// Controles y Variables
		UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
		ArrayList<HashMap<String, String>> filtros;
		HashMap<String, String> filtro;
		String idinstitucion = null;
		String idpago = null;
		String idioma = null;
		
		// obteniendo valores del formulario
		try {
			idinstitucion = usr.getLocation();
			idpago = request.getParameter("idPago");
			idioma = usr.getLanguage();
		} catch(Exception e) {
			throwExcp("messages.general.error",
					new String[] { "modulo.facturacionSJCS" }, e, null);
		}

		// generando lista de filtros
		filtros = new ArrayList<HashMap<String, String>>();
		
		filtro = new HashMap<String, String>();
		filtro.put(AdmTipoFiltroInformeBean.C_NOMBRECAMPO, "IDINSTITUCION");
		filtro.put("VALOR", idinstitucion);
		filtros.add(filtro);
		filtro = new HashMap<String, String>();
		filtro.put(AdmTipoFiltroInformeBean.C_NOMBRECAMPO, "IDPAGO");
		filtro.put("VALOR", idpago);
		filtros.add(filtro);
		filtro = new HashMap<String, String>();
		filtro.put(AdmTipoFiltroInformeBean.C_NOMBRECAMPO, "IDIOMA");
		filtro.put("VALOR", idioma);
		filtros.add(filtro);

		return filtros;
	}

	/**
	 * Metodo que permite la descarga de un fichero
	 */
	protected String descargar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {

		File fichero = null;
		String rutaFichero = null;
		MantenimientoInformesForm miform = null;

		try {
			// Obtenemos el formulario y sus datos:
			miform = (MantenimientoInformesForm) formulario;
			rutaFichero = miform.getRutaFichero();
			fichero = new File(rutaFichero);
			if (fichero == null || !fichero.exists()) {
				throw new SIGAException(
						"messages.general.error.ficheroNoExiste");
			}
			request.setAttribute("nombreFichero", fichero.getName());
			request.setAttribute("rutaFichero", fichero.getPath());
			request.setAttribute("borrarFichero", "true");

		} catch (Exception e) {
			throwExcp("messages.general.error",
					new String[] { "modulo.facturacionSJCS" }, e, null);
		}
		return "descargaFichero";
	}

}