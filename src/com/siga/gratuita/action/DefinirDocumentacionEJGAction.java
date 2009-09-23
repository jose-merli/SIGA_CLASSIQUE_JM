//Clase: DefinirDocumentacionEJGAction 
//Autor: julio.vicente@atosorigin.com
//Ultima modificación: 14/02/2005

package com.siga.gratuita.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.*;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.ScsDocumentacionEJGAdm;
import com.siga.beans.ScsDocumentacionEJGBean;
import com.siga.beans.ScsDocumentoEJGAdm;
import com.siga.beans.ScsDocumentoEJGBean;
import com.siga.beans.ScsEJGAdm;
import com.siga.beans.ScsEJGBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefinirDocumentacionEJGForm;

/**
 * Maneja las acciones que se pueden realizar sobre la tabla
 * SCS_DOCUMENTACIONEJG
 */
public class DefinirDocumentacionEJGAction extends MasterAction {

	/** Not implemented * */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {

		return null;
	}

	/**
	 * Rellena un hash con los valores recogidos del formulario, almacenando
	 * esta hash en la sesión con el nombre "elegido"
	 * 
	 * @param mapping
	 *            Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario
	 *            del que se recoge la información. De tipo MasterForm.
	 * @param request
	 *            Información de sesión. De tipo HttpServletRequest
	 * @param response
	 *            De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo.
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {

		try {
			Vector ocultos = formulario.getDatosTablaOcultos(0);
			DefinirDocumentacionEJGForm miForm = (DefinirDocumentacionEJGForm) formulario;

			UsrBean usr = (UsrBean) request.getSession()
					.getAttribute("USRBEAN");

			ScsDocumentacionEJGAdm admBean = new ScsDocumentacionEJGAdm(this
					.getUserBean(request));
			// Entramos al formulario en modo 'modificación'
			request.setAttribute("accionModo", "editar");

			Hashtable miHash = new Hashtable();
			miHash.put(ScsDocumentacionEJGBean.C_IDTIPOEJG, miForm
					.getIdTipoEJG());
			miHash.put(ScsDocumentacionEJGBean.C_IDINSTITUCION, usr
					.getLocation());
			miHash.put(ScsDocumentacionEJGBean.C_ANIO, miForm.getAnio());
			miHash.put(ScsDocumentacionEJGBean.C_NUMERO, miForm.getNumero());
			miHash.put(ScsDocumentacionEJGBean.C_IDDOCUMENTACION, ocultos
					.get(0));
			miHash.put(ScsDocumentacionEJGBean.C_IDDOCUMENTO, ocultos.get(1));
			miHash.put(ScsDocumentacionEJGBean.C_IDTIPODOCUMENTO, ocultos
					.get(2));
			miHash.put(ScsDocumentacionEJGBean.C_PRESENTADOR, ocultos.get(3));
			// Volvemos a obtener de base de datos la información, para que se
			// la más actúal que hay en la base de datos
			Vector resultado = admBean.selectPorClave(miHash);
			ScsDocumentacionEJGBean ejg = (ScsDocumentacionEJGBean) resultado
					.get(0);

			request.getSession().setAttribute("DATABACKUP",
					admBean.beanToHashTable(ejg));
		} catch (Exception e) {
			throwExcp("messages.general.error", e, null);
		}
		return "editar";

	}

	/**
	 * No implementado
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {

		try {
			Vector ocultos = formulario.getDatosTablaOcultos(0);
			DefinirDocumentacionEJGForm miForm = (DefinirDocumentacionEJGForm) formulario;

			UsrBean usr = (UsrBean) request.getSession()
					.getAttribute("USRBEAN");

			ScsDocumentacionEJGAdm admBean = new ScsDocumentacionEJGAdm(this
					.getUserBean(request));
			// Entramos al formulario en modo 'modificación'
			request.setAttribute("accionModo", "ver");

			Hashtable miHash = new Hashtable();
			miHash.put(ScsDocumentacionEJGBean.C_IDTIPOEJG, miForm
					.getIdTipoEJG());
			miHash.put(ScsDocumentacionEJGBean.C_IDINSTITUCION, usr
					.getLocation());
			miHash.put(ScsDocumentacionEJGBean.C_ANIO, miForm.getAnio());
			miHash.put(ScsDocumentacionEJGBean.C_NUMERO, miForm.getNumero());
			miHash.put(ScsDocumentacionEJGBean.C_IDDOCUMENTACION, ocultos
					.get(0));
			miHash.put(ScsDocumentacionEJGBean.C_IDDOCUMENTO, ocultos.get(1));
			miHash.put(ScsDocumentacionEJGBean.C_IDTIPODOCUMENTO, ocultos
					.get(2));
			miHash.put(ScsDocumentacionEJGBean.C_PRESENTADOR, ocultos.get(3));
			// Volvemos a obtener de base de datos la información, para que se
			// la más actúal que hay en la base de datos
			Vector resultado = admBean.selectPorClave(miHash);
			ScsDocumentacionEJGBean ejg = (ScsDocumentacionEJGBean) resultado
					.get(0);

			request.getSession().setAttribute("DATABACKUP",
					admBean.beanToHashTable(ejg));
		} catch (Exception e) {
			throwExcp("messages.general.error", e, null);
		}
		return "ver";
	}

	/**
	 * Rellena el string que indica la acción a llevar a cabo con "
	 *  . .Fiesta" para que redirija a la pantalla de inserción.
	 * 
	 * @param mapping
	 *            Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario
	 *            del que se recoge la información. De tipo MasterForm.
	 * @param request
	 *            Información de sesión. De tipo HttpServletRequest
	 * @param response
	 *            De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo.
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {

		DefinirDocumentacionEJGForm miForm = (DefinirDocumentacionEJGForm) formulario;
		Hashtable miHash = new Hashtable();
		try {
			miHash.put("ANIO", formulario.getDatos().get("ANIO"));
			miHash.put("NUMERO", formulario.getDatos().get("NUMERO"));
			miHash.put("IDTIPOEJG", formulario.getDatos().get("IDTIPOEJG"));
			miHash
					.put("IDINSTITUCION", this.getUserBean(request)
							.getLocation());

			ScsEJGAdm admEjg = new ScsEJGAdm(this.getUserBean(request));
			Vector v = admEjg.selectByPK(miHash);
			if (v != null && v.size() > 0) {
				ScsEJGBean b = (ScsEJGBean) v.get(0);
				miHash.put("FECHALIMITE", b.getFechaLimitePresentacion());
			}
			request.getSession().setAttribute("EJG", miHash);

		} catch (Exception e) {
			throwExcp("messages.general.error",
					new String[] { "modulo.gratuita" }, e, null);
		}

		return "insertarDocumentacionEJG";
	}

	/**
	 * Rellena un hash con los valores recogidos del formulario y los inserta en
	 * la base de datos.
	 * 
	 * @param mapping
	 *            Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario
	 *            del que se recoge la información. De tipo MasterForm.
	 * @param request
	 *            Información de sesión. De tipo HttpServletRequest
	 * @param response
	 *            De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo.
	 */
	protected synchronized String insertar(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {

		UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
		UserTransaction tx = null;

		DefinirDocumentacionEJGForm miForm = (DefinirDocumentacionEJGForm) formulario;

		boolean faltaObligatorio = miForm.getPresentador() == null || miForm.getPresentador().trim().equals("");
		faltaObligatorio = faltaObligatorio || miForm.getIdTipoDocumento() == null || miForm.getIdTipoDocumento().trim().equals("");
		faltaObligatorio = faltaObligatorio || miForm.getIdDocumento() == null || miForm.getIdDocumento().trim().equals("");
		
		if (faltaObligatorio) {
			throw new SIGAException("messages.ejg.documentacion.camposObligatorios");
		}

		
		Hashtable miHash = new Hashtable();
		
		String documento = "", tipoDocumento = "";

		try {
			miHash = miForm.getDatos();
			tx = usr.getTransaction();
			tx.begin();

			documento = (String) miHash.get("IDDOCUMENTO");
			tipoDocumento = (String) miHash.get("IDTIPODOCUMENTO");
			miHash.put("IDTIPODOCUMENTO", tipoDocumento.split(",")[0]);
			UtilidadesHash.set(miHash, ScsDocumentacionEJGBean.C_FECHALIMITE, GstDate.getApplicationFormatDate("",
					miHash.get(ScsDocumentacionEJGBean.C_FECHALIMITE).toString()));
			
			UtilidadesHash.set(miHash, ScsDocumentacionEJGBean.C_FECHAENTREGA, GstDate.getApplicationFormatDate("",
					miHash.get(ScsDocumentacionEJGBean.C_FECHAENTREGA).toString()));
			
			insertaDocumentacion(request, documento, miHash, tipoDocumento.split(",")[0]);
			
			tx.commit();
		} catch (Exception e) {
			throwExcp("messages.general.error",
					new String[] { "modulo.gratuita" }, e, tx);
		}
		return exitoModal("messages.inserted.success", request);
	}

	private void insertaDocumentacion(HttpServletRequest request, String documento, Hashtable miHash,
			String tipoDocumento) throws ClsExceptions, SIGAException {
		ScsDocumentacionEJGAdm admBean = new ScsDocumentacionEJGAdm(this.getUserBean(request));
		if (documento.equals("0")) {
			ScsDocumentoEJGAdm admDoc = new ScsDocumentoEJGAdm(this.getUserBean(request));
			Hashtable hashDocs = new Hashtable();
			// TODOS. Obtengo todos los documentos del tipo y los inserto.
			hashDocs.put("IDINSTITUCION", getUserBean(request).getLocation());
			hashDocs.put("IDTIPODOCUMENTOEJG", tipoDocumento);
			Vector vDocs = admDoc.select(hashDocs);

			ScsDocumentoEJGBean bDocs = null;
			if (vDocs == null || vDocs.size() == 0) {
				throw new SIGAException("gratuita.ejg.documentacion.noexiste");
			}
			for (int g = 0; vDocs != null && g < vDocs.size(); g++) {
				bDocs = (ScsDocumentoEJGBean) vDocs.get(g);
				miHash.put("IDDOCUMENTO", bDocs.getIdDocumentoEJG());
				admBean.prepararInsert(miHash);
				admBean.insert(miHash);
			}

		} else {
			miHash.put("IDDOCUMENTO", documento.split(",")[0]);
			admBean.prepararInsert(miHash);
			admBean.insert(miHash);
		}
	}

	/**
	 * Rellena un hash con los valores recogidos del formulario y los modifica
	 * en la base de datos.
	 * 
	 * @param mapping
	 *            Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario
	 *            del que se recoge la información. De tipo MasterForm.
	 * @param request
	 *            Información de sesión. De tipo HttpServletRequest
	 * @param response
	 *            De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo.
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {

		UserTransaction tx = null;
		try {
			DefinirDocumentacionEJGForm miForm = (DefinirDocumentacionEJGForm) formulario;
			ScsDocumentacionEJGAdm admBean = new ScsDocumentacionEJGAdm(this.getUserBean(request));
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			Hashtable nuevos = miForm.getDatos();
			Hashtable clave = new Hashtable();

			String documento = "", tipoDocumento = "";

			clave.put(ScsDocumentacionEJGBean.C_IDINSTITUCION, usr.getLocation());
			clave.put(ScsDocumentacionEJGBean.C_ANIO, miForm.getAnio());
			clave.put(ScsDocumentacionEJGBean.C_NUMERO, miForm.getNumero());
			clave.put(ScsDocumentacionEJGBean.C_IDTIPOEJG, miForm.getIdTipoEJG());
			clave.put(ScsDocumentacionEJGBean.C_IDDOCUMENTACION, miForm.getIdDocumentacion());

			clave.put(ScsDocumentacionEJGBean.C_IDDOCUMENTO, miForm.getIdDocumentoAnterior());
			clave.put(ScsDocumentacionEJGBean.C_IDTIPODOCUMENTO, miForm.getIdTipoDocumentoAnterior());
			clave.put(ScsDocumentacionEJGBean.C_PRESENTADOR, miForm.getPresentadorAnterior());

			// Volvemos a obtener de base de datos la información, para que se
			// la más actúal que hay en la base de datos
			// Vector resultado = admBean.selectPorClave(clave);
			// ScsDocumentacionEJGBean documentacion =
			// (ScsDocumentacionEJGBean)resultado.get(0);
			tx = usr.getTransaction();
			tx.begin();

			// UtilidadesHash.set(nuevos, "FECHALIMITE", GstDate
			// .getApplicationFormatDate("", nuevos.get("FECHALIMITE")
			// .toString()));
			documento = (String) nuevos.get("IDDOCUMENTO");
			tipoDocumento = (String) nuevos.get("IDTIPODOCUMENTO");
			nuevos.put("IDDOCUMENTO", documento.split(",")[0]);
			nuevos.put("IDTIPODOCUMENTO", tipoDocumento.split(",")[0]);

			UtilidadesHash.set(nuevos, ScsDocumentacionEJGBean.C_FECHALIMITE, GstDate.getApplicationFormatDate("",
					nuevos.get(ScsDocumentacionEJGBean.C_FECHALIMITE).toString()));
			
			UtilidadesHash.set(nuevos, ScsDocumentacionEJGBean.C_FECHAENTREGA, GstDate.getApplicationFormatDate("",
					nuevos.get(ScsDocumentacionEJGBean.C_FECHAENTREGA).toString()));

			insertaDocumentacion(request, documento, nuevos, tipoDocumento.split(",")[0]);

			admBean.delete(clave);

			tx.commit();

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita" }, e, tx);
		}

		return exitoModal("messages.updated.success", request);
	}

	/**
	 * Rellena un hash con los valores recogidos del formulario y los borra de
	 * la base de datos.
	 * 
	 * @param mapping
	 *            Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario
	 *            del que se recoge la información. De tipo MasterForm.
	 * @param request
	 *            Información de sesión. De tipo HttpServletRequest
	 * @param response
	 *            De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo.
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {

		UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
		UserTransaction tx = null;

		Vector ocultos = formulario.getDatosTablaOcultos(0);
		ScsDocumentacionEJGAdm admBean = new ScsDocumentacionEJGAdm(this
				.getUserBean(request));
		DefinirDocumentacionEJGForm miForm = (DefinirDocumentacionEJGForm) formulario;

		Hashtable miHash = new Hashtable();

		try {
			miHash.put(ScsDocumentacionEJGBean.C_IDDOCUMENTACION, (ocultos
					.get(0)));
			miHash.put(ScsDocumentacionEJGBean.C_IDTIPOEJG, miForm
					.getIdTipoEJG());
			miHash.put(ScsDocumentacionEJGBean.C_ANIO, miForm.getAnio());
			miHash.put(ScsDocumentacionEJGBean.C_NUMERO, miForm.getNumero());
			miHash.put(ScsDocumentacionEJGBean.C_IDINSTITUCION, usr
					.getLocation());
			miHash.put(ScsDocumentacionEJGBean.C_IDDOCUMENTO, ocultos.get(1));
			miHash.put(ScsDocumentacionEJGBean.C_IDTIPODOCUMENTO, ocultos
					.get(2));
			miHash.put(ScsDocumentacionEJGBean.C_PRESENTADOR, ocultos.get(3));

			tx = usr.getTransaction();
			tx.begin();
			admBean.delete(miHash);
			tx.commit();

		} catch (Exception e) {
			throwExcp("messages.deleted.error", e, tx);
		}

		return exitoRefresco("messages.deleted.success", request);
	}

	/**
	 * No implementado
	 */
	protected String buscar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
		return null;
	}

	protected String abrir(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {

		/*
		 * "DATABACKUP" se usa habitualmente así que en primero lugar borramos
		 * esta variable
		 */
		request.getSession().removeAttribute("DATABACKUP");

		ScsDocumentacionEJGAdm admBean = new ScsDocumentacionEJGAdm(this
				.getUserBean(request));
		Vector v = new Vector();
		Hashtable miHash = new Hashtable();

		miHash.put("ANIO", request.getParameter("ANIO").toString());
		miHash.put("NUMERO", request.getParameter("NUMERO").toString());
		miHash.put("IDTIPOEJG", request.getParameter("IDTIPOEJG").toString());
		miHash.put("IDINSTITUCION", request.getParameter("IDINSTITUCION")
				.toString());

		try {
			v = admBean.buscar(miHash);
			request.setAttribute("resultado", v);
		} catch (Exception e) {
			throwExcp("messages.general.error", e, null);
		}
		return "inicio";

	}

	/**
	 * No implementadofeditar
	 * 
	 */
	protected String abrirAvanzada(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {

		ScsDocumentacionEJGAdm admBean = new ScsDocumentacionEJGAdm(this
				.getUserBean(request));
		DefinirDocumentacionEJGForm miForm = (DefinirDocumentacionEJGForm) formulario;
		UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
		Vector v = new Vector();
		Hashtable miHash = new Hashtable();

		miHash.put("ANIO", miForm.getAnio());
		miHash.put("NUMERO", miForm.getNumero());
		miHash.put("IDTIPOEJG", miForm.getIdTipoEJG());
		miHash.put("IDINSTITUCION", usr.getLocation());

		request.setAttribute("DATOSEJG", miHash);

		try {
			v = admBean.buscar(miHash);
			request.setAttribute("resultado", v);
			request.setAttribute("accion", formulario.getModo());
		} catch (Exception e) {
			throwExcp("messages.general.error", e, null);
		}
		return "inicio";
	}
}