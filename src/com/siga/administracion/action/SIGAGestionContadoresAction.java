package com.siga.administracion.action;

import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.administracion.form.SIGAGestionContadoresForm;
import com.siga.beans.AdmContadorAdm;
import com.siga.beans.AdmContadorBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

public class SIGAGestionContadoresAction extends MasterAction
{

	protected String buscar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions, SIGAException {
		SIGAGestionContadoresForm form = (SIGAGestionContadoresForm) formulario;

		AdmContadorAdm admContador = new AdmContadorAdm(this
				.getUserBean(request));

		Vector v = admContador.getContadores(this.getIDInstitucion(request),
				form.getIdModulo(), form.getCodigo(), form.getNombreContador(),
				form.getDescripcionContador());

		request.setAttribute("resultados", v);

		return "resultado";
	}

	protected String modificar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions, SIGAException {
		UserTransaction tx = this.getUserBean(request).getTransaction();
		UsrBean userBean = ((UsrBean) request.getSession().getAttribute(
				("USRBEAN")));
		AdmContadorBean beanOld = new AdmContadorBean();

		try {
			SIGAGestionContadoresForm form = (SIGAGestionContadoresForm) formulario;

			tx.begin();

			AdmContadorAdm adm = new AdmContadorAdm(this.getUserBean(request));
			/*    * Recuperamos los datos antiguos del contador seleccionado* */
			Hashtable datosContadorHash = new Hashtable();
			datosContadorHash.put(AdmContadorBean.C_IDCONTADOR, form
					.getCodigo());
			datosContadorHash.put(AdmContadorBean.C_IDINSTITUCION, userBean
					.getLocation());
			Vector vDatosContador = adm.selectByPK(datosContadorHash);
			if (vDatosContador.size() > 0) {
				beanOld = (AdmContadorBean) vDatosContador.elementAt(0);
			}
			/******************************************************************/

			/*    **** Datos actuales del contador modificado************** */
			AdmContadorBean bean = new AdmContadorBean();
			bean.setIdContador(form.getCodigo());
			bean.setIdinstitucion(new Integer(userBean.getLocation()));
			bean.setDescripcion(form.getDescripcionContador());
			if (form.getFechaReconfiguracion() != null
					&& !form.getFechaReconfiguracion().equals("")) {
				bean.setFechaReconfiguracion(GstDate.getApplicationFormatDate(
						"EN", form.getFechaReconfiguracion()));
			} else {
				bean.setFechaReconfiguracion("");
			}
			bean.setContador(new Long(form.getContador()));
			bean.setPrefijo(form.getPrefijo());
			bean.setSufijo(form.getSufijo());
			bean.setLongitudContador(new Integer(form.getLongitud()));
			bean.setNombre(form.getNombreContador());

			bean
					.setReconfiguracionContador((form
							.getReconfiguracionContador() == null || form
							.getReconfiguracionContador().equals("")) ? "0"
							: form.getReconfiguracionContador());
			bean.setReconfiguracionPrefijo(form.getReconfiguracionPrefijo());
			bean.setReconfiguracionSufijo(form.getReconfiguracionSufijo());
			bean.setModoContador(new Integer(form.getModoContador()));
			boolean bModificable = UtilidadesString.stringToBoolean(form
					.getModificable());
			if (bModificable) {
				bean.setModificableContador(ClsConstants.DB_TRUE);
			} else {
				bean.setModificableContador(ClsConstants.DB_FALSE);
			}

			if (!adm.update(bean, beanOld)) {
				throw new ClsExceptions("messages.updated.error");
			}

			tx.commit();
		} catch (Exception e) {
			throwExcp("messages.updated.error", e, tx);
		}
		request.setAttribute("modal", "1");
		return this.exitoRefresco("messages.updated.success", request);
	}

	protected String editar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions {
		mostrarRegistro(mapping, formulario, request, response, true);
		request.setAttribute("modo", "editar");
		return "mostrar";
	}

	protected String ver(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions {
		mostrarRegistro(mapping, formulario, request, response, true);
		request.setAttribute("modo", "ver");
		return "ver";
	}

	protected void mostrarRegistro(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response, boolean bEditable)
			throws ClsExceptions {
		SIGAGestionContadoresForm form = (SIGAGestionContadoresForm) formulario;
		UsrBean userBean = ((UsrBean) request.getSession().getAttribute(
				("USRBEAN")));

		Vector vOcultos = form.getDatosTablaOcultos(0);

		String idCodigo = (String) vOcultos.elementAt(0);

		AdmContadorAdm contadorAdm = new AdmContadorAdm(this
				.getUserBean(request));
		Hashtable datosContadorHash = new Hashtable();
		datosContadorHash.put(AdmContadorBean.C_IDCONTADOR, idCodigo);
		datosContadorHash.put(AdmContadorBean.C_IDINSTITUCION, userBean
				.getLocation());
		Vector vDatosContador = contadorAdm.selectByPK(datosContadorHash);
		AdmContadorBean bean = null;
		if (vDatosContador.size() > 0)
			bean = (AdmContadorBean) vDatosContador.elementAt(0);

		Vector datos = new Vector();
		datos.add(bean);

		request.setAttribute("datos", datos);
	}

	protected String borrar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions, SIGAException {
		return this.exitoRefresco("messages.deleted.success", request);
	}
}
