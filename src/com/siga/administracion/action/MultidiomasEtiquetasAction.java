package com.siga.administracion.action;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.util.PropertyReader;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ClsMngProperties;
import com.siga.Utilidades.UtilidadesString;
import com.siga.administracion.form.MultidiomasForm;
import com.siga.beans.GenRecursosAdm;
import com.siga.beans.GenRecursosBean;
import com.siga.beans.MasterBeanAdministrador;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

public class MultidiomasEtiquetasAction extends MasterAction {
	protected ActionForward executeInternal(ActionMapping mapping,
			ActionForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {
		String mapDestino = "exception";
		MasterForm miForm = null;
		request.setAttribute("CATALOGOS_MAESTROS", "false");
		try {
			miForm = (MasterForm) formulario;
			if (miForm != null) {
				String accion = miForm.getModo();
				if (accion != null) {
					// abrirAvanzadaConParametros
					if (accion.equalsIgnoreCase("generarFicheroRecursos")) {
						mapDestino = generarFicheroRecursos(mapping, miForm,
								request, response);
						return mapping.findForward(mapDestino);
					}
				}
				return super.executeInternal(mapping, formulario, request,
						response);
			}

			// Redireccionamos el flujo a la JSP correspondiente
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

			// Etiquetas
			GenRecursosAdm adm = new GenRecursosAdm(this.getUserBean(request));
			v = adm.selectPorDescripcion(miForm.getDescripcion(),
					miForm.getIdIdiomaATraducir(), miForm.getIdIdioma());

			request.setAttribute("CATALOGOS_MAESTROS", "false");
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
			adm = new GenRecursosAdm(this.getUserBean(request));

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

				GenRecursosBean b = new GenRecursosBean();
				b.setDescripcion(valor);
				b.setIdRecurso(idRecurso);
				b.setIdLenguaje(idLenguaje);
				String[] campos = { GenRecursosBean.C_DESCRIPCION,
						GenRecursosBean.C_IDLENGUAJE,
						GenRecursosBean.C_IDRECURSO };
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

	protected String generarFicheroRecursos(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws ClsExceptions, SIGAException {
		ClsMngProperties.initProperties();
		PropertyReader.cleanProperties();
		ClsLogging.reset();
		UtilidadesString.recargarFicherosIdiomasEnCaliente();
		return this.exito("messages.updated.success", request);
	}
}