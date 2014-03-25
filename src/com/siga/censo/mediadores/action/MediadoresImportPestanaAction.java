package com.siga.censo.mediadores.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.siga.general.MasterAction;
import com.siga.general.SIGAException;

public class MediadoresImportPestanaAction  extends MasterAction {

	/**
	 * Funcion que atiende a las peticiones. Segun el valor del parametro modo
	 * del formulario ejecuta distintas acciones
	 * 
	 * @param mapping
	 *            - Mapeo de los struts
	 * @param formulario
	 *            - Action Form asociado a este Action
	 * @param request
	 *            - objeto llamada HTTP
	 * @param response
	 *            - objeto respuesta HTTP
	 * @return String Destino del action
	 * @exception ClsExceptions
	 *                En cualquier caso de error
	 */

	public ActionForward executeInternal(ActionMapping mapping,
			ActionForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {


		try {

			return mapping.findForward("inicio");

		} catch (Exception e) {
			throw new SIGAException("messages.general.error", e, new String[] { "modulo.censo" });
		}
	}

}
