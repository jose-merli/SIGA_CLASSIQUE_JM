package com.siga.administracion.action;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.siga.administracion.form.MultidiomasForm;
import com.siga.beans.GenRecursosCatalogosAdm;
import com.siga.beans.GenRecursosCatalogosBean;
import com.siga.beans.MasterBeanAdministrador;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

public class MultidiomasCatalogosAction extends MasterAction {
	protected ActionForward executeInternal(ActionMapping mapping,
			ActionForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {
		String mapDestino = "exception";
		MasterForm miForm = null;

		request.setAttribute("CATALOGOS_MAESTROS", "true");
		try {
			miForm = (MasterForm) formulario;
			if (miForm != null) {				
				return super.executeInternal(mapping, formulario, request,
						response);
			} else
				return mapping.findForward(mapDestino);
		} catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error", e,
					new String[] { "modulo.administracion" });
		}
	}

	protected String abrir(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions, SIGAException {		
		return super.abrir(mapping, formulario, request, response);
	}

	protected String buscar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions, SIGAException {
		try {
			Vector v = null;

			MultidiomasForm miForm = (MultidiomasForm) formulario;

			// Catalogos Maestros
			GenRecursosCatalogosAdm adm = new GenRecursosCatalogosAdm(
					this.getUserBean(request));
			v = adm.selectPorDescripcion(miForm.getDescripcion(),
					miForm.getIdIdiomaATraducir(), miForm.getIdIdioma());

			request.setAttribute("CATALOGOS_MAESTROS", "" + miForm.esCatalogo());
			request.setAttribute("resultados", v);
			return "resultado";
		} catch (ClsExceptions e) {
			return this.error(e.getMsg(), e, request);
		}
	}

	protected String modificar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions, SIGAException {
		UserTransaction tx = this.getUserBean(request).getTransaction();

		try {
			MultidiomasForm form = (MultidiomasForm) formulario;
			String a = form.getDatosModificados();
			if (a.length() < 0) {
				throw new ClsExceptions("messages.updated.error");
			}

			MasterBeanAdministrador adm = null;
			adm = new GenRecursosCatalogosAdm(this.getUserBean(request));

			tx.begin();

			// Esctructura form.getDatosModificados -->
			// idLenguaje1#;#idRecurso1#=#valor1#;;# .....
			String[] elementos = a.split("#;;#");

			for (int i = 0; i < elementos.length; i++) {
				String[] aux = elementos[i].split("#;#");
				String idLenguaje = aux[0];
				String[] key_valor = aux[1].split("#=#");
				String idRecurso = key_valor[0];
				String valor = key_valor[1];

				GenRecursosCatalogosBean b = new GenRecursosCatalogosBean();
				b.setDescripcion(valor);
				b.setIdRecurso(new String(idRecurso));
				b.setIdLenguaje(idLenguaje);
				String[] campos = { GenRecursosCatalogosBean.C_DESCRIPCION,
						GenRecursosCatalogosBean.C_IDLENGUAJE,
						GenRecursosCatalogosBean.C_IDRECURSO };
				if (!adm.updateDirect(b, null, campos)) {
					throw new ClsExceptions("messages.updated.error");
				}
			}
			tx.commit();
		} catch (Exception e) {
			throwExcp("messages.updated.error", e, tx);
		}
		return this.exitoRefresco("messages.updated.success", request);
	}

}