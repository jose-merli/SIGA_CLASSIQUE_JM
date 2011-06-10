/*
 * Created on Mar 21, 2005
 * @author emilio.grau
 * 
 */
package com.siga.gratuita.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.beans.FcsFacturacionJGAdm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.CartaFacturacionCensoForm;

/**
 * Action para la consulta de morosos.
 */
public class CartaFacturacionCensoAction extends MasterAction {
	final String[] clavesBusqueda={"IDFACTURACION","IDPERSONA",};
	/**
	 * Funcion que atiende a las peticiones. Segun el valor del parametro modo
	 * del formulario ejecuta distintas acciones
	 * 
	 * @param mapping -
	 *            Mapeo de los struts
	 * @param formulario -
	 *            Action Form asociado a este Action
	 * @param request -
	 *            objeto llamada HTTP
	 * @param response -
	 *            objeto respuesta HTTP
	 * @return String Destino del action
	 * @exception SIGAException
	 *                En cualquier caso de error
	 */
	protected ActionForward executeInternal(ActionMapping mapping,
			ActionForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {

		String mapDestino = "exception";
		MasterForm miForm = null;

		try {
			miForm = (MasterForm) formulario;
			if (miForm == null) {
				return mapping.findForward(mapDestino);
			}

			String accion = miForm.getModo();

			// La primera vez que se carga el formulario Abrir
			if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")) {
				mapDestino = abrir(mapping, miForm, request, response);
			
			} else {
				return super.executeInternal(mapping, formulario, request, response);
			}

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) {
				// mapDestino = "exception";
				if (miForm.getModal().equalsIgnoreCase("TRUE")) {
					request.setAttribute("exceptionTarget", "parent.modal");
				}

				// throw new SIGAException("El ActionMapping no puede ser
				// nulo");
				throw new ClsExceptions("El ActionMapping no puede ser nulo","", "0", "GEN00", "15");
			}

		} catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error", e,
					new String[] { "modulo.facturacionSJCS" }); // o el recurso
																// del modulo
																// que sea
		}
		return mapping.findForward(mapDestino);
	}

	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions, SIGAException {

		String destino = "inicio";
		try {
			// obtener institucion
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			String idInstitucion = user.getLocation();
			String facturaciones = "";
			String idPersona = request.getSession().getAttribute("IDPERSONA").toString();

			// casting del formulario
			CartaFacturacionCensoForm miFormulario = (CartaFacturacionCensoForm) formulario;
			miFormulario.setIdInstitucion(user.getLocation());

			HashMap databackup = new HashMap();

			Vector datos = null;
			FcsFacturacionJGAdm factAdm = new FcsFacturacionJGAdm(user);
			datos = factAdm.getVectorDetalleFacturacionPorColegiado(idInstitucion, idPersona);

			if (datos != null) {
				databackup.put("datos", datos);
			} else {
				miFormulario.setRegistrosSeleccionados(new ArrayList());
			}
			miFormulario.setDatosPaginador(databackup);

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.facturacion" }, e, null);
		}
		return destino;
	}

}
