package com.siga.certificados.action;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONException;
import org.json.JSONObject;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.beans.CerIncompatibilidadesAdm;
import com.siga.beans.CerIncompatibilidadesBean;
import com.siga.beans.CerPlantillasBean;
import com.siga.beans.PysProductosInstitucionAdm;
import com.siga.beans.PysProductosInstitucionBean;
import com.siga.certificados.form.SIGAMantenimientoCertificadosIncompatibilidadesForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

public class SIGAMantenimientoCertificadosIncompatibilidadesAction extends MasterAction
{
	public ActionForward executeInternal(ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{
		String mapDestino = "exception";
		MasterForm miForm = null;

		try {
			miForm = (MasterForm) formulario;

			if (miForm != null) {
				String accion = miForm.getModo();

				if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")) {
					mapDestino = abrir(mapping, miForm, request, response);
				} else if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("getAjaxBusqueda")) {
					buscar(mapping, miForm, request, response);
					return null;
				} else if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("crearIncompatibilidad")) {
					grabar(mapping, miForm, request, response, true);
					return null;
				} else if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("modificarIncompatibilidad")) {
					grabar(mapping, miForm, request, response, false);
					return null;
				} else if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("borrarIncompatibilidad")) {
					borrarIncompatibilidad(mapping, miForm, request, response);
					return null;
				} else {
					return super.executeInternal(mapping, formulario, request, response);
				}
			}

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) {
				throw new ClsExceptions("El ActionMapping no puede ser nulo", "", "0", "GEN00", "15");
			}

			return mapping.findForward(mapDestino);
		} catch (Exception e) {
			throw new SIGAException("messages.general.error", e, new String[] { "modulo.certificados" });
		}
	}

	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{
		// Controles y Variables generales
		UsrBean userBean = this.getUserBean(request);
		SIGAMantenimientoCertificadosIncompatibilidadesForm form = (SIGAMantenimientoCertificadosIncompatibilidadesForm) formulario;
		CerIncompatibilidadesAdm admIncompatibilidades = new CerIncompatibilidadesAdm(userBean);
		PysProductosInstitucionAdm productoAdm = new PysProductosInstitucionAdm(userBean);
		String institucion = userBean.getLocation();

		// consultando los tipos de certificados existentes
		StringBuilder where = new StringBuilder();
		where.append(" WHERE ");
		where.append(PysProductosInstitucionBean.C_TIPOCERTIFICADO);
		where.append(" IN ('");
		where.append(PysProductosInstitucionBean.PI_COMUNICACION_CODIGO);
		where.append("','");
		where.append(PysProductosInstitucionBean.PI_DILIGENCIA_CODIGO);
		where.append("','");
		where.append(PysProductosInstitucionBean.PI_CERTIFICADO_CODIGO);
		where.append("') AND ");
		where.append(PysProductosInstitucionBean.C_IDINSTITUCION);
		where.append(" = ");
		where.append(institucion);
		Vector tiposCertificados = productoAdm.select(where.toString());
		request.setAttribute("tiposCertificados", (List<PysProductosInstitucionBean>) tiposCertificados);

		// obteniendo los valores desde el formulario
		String idInstitucion = form.getIdInstitucion();
		String idTipoProducto = form.getIdTipoProducto();
		String idProducto = form.getIdProducto();
		String idProductoInstitucion = form.getIdProductoInstitucion();

		String sCertificado = form.getCertificado();
		String sEditable = form.getEditable();
		String descCertificado = form.getDescripcionCertificado();
		
		// obteniendo las incompatibilidades existentes
		Vector vDatos = admIncompatibilidades.obtenerListaIncompatibilidades(idInstitucion, idTipoProducto, idProducto, idProductoInstitucion);

		request.setAttribute("idInstitucion", idInstitucion);
		request.setAttribute("idTipoProducto", idTipoProducto);
		request.setAttribute("idProducto", idProducto);
		request.setAttribute("idProductoInstitucion", idProductoInstitucion);

		request.setAttribute("certificado", sCertificado);
		request.setAttribute("editable", sEditable);

		request.setAttribute("descripcionCertificado", descCertificado);

		request.setAttribute("datos", vDatos);

		return "abrir";
	}

	/**
	 * Devuelve el listado de tipos de certificados existentes (llamado por AJAX)
	 */
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{
		return "exito";
	}

	protected String borrarIncompatibilidad(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException, JSONException, IOException
	{
		// Controles y variables generales
		UsrBean user = this.getUserBean(request);
		SIGAMantenimientoCertificadosIncompatibilidadesForm form = (SIGAMantenimientoCertificadosIncompatibilidadesForm) formulario;
		CerIncompatibilidadesAdm admIncompatibilidades = new CerIncompatibilidadesAdm(user);

		// obteniendo la incompatibilidad a borrar
		CerIncompatibilidadesBean incompatibilidadBean = new CerIncompatibilidadesBean();
		incompatibilidadBean.setIdInstitucion(Integer.valueOf((String) request.getParameter("idInstitucion")));
		incompatibilidadBean.setIdTipoProducto(Integer.valueOf((String) request.getParameter("idTipoProducto")));
		incompatibilidadBean.setIdProducto(Integer.valueOf((String) request.getParameter("idProducto")));
		incompatibilidadBean.setIdProductoInstitucion(Integer.valueOf((String) request.getParameter("idProductoInstitucion")));
		incompatibilidadBean.setIdTipoProd_Incompatible(Integer.valueOf((String) request.getParameter("idTipoProd_Incompatible")));
		incompatibilidadBean.setIdProd_Incompatible(Integer.valueOf((String) request.getParameter("idProd_Incompatible")));
		incompatibilidadBean.setIdProdInst_Incompatible(Integer.valueOf((String) request.getParameter("idProdInst_Incompatible")));

		// borrando
		JSONObject json = new JSONObject();
		if (admIncompatibilidades.delete(incompatibilidadBean)) {
			request.setAttribute("mensaje", "messages.deleted.success");
			json.put("msg", "Incompatibilidad borrada correctamente");
		} else {
			request.setAttribute("mensaje", "error.messages.deleted");
			json.put("msg", "No se ha podido borrar la incompatibilidad");
		}

		// json.
		response.setContentType("text/x-json;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Content-Type", "application/json");
		response.setHeader("X-JSON", json.toString());
		response.getWriter().write(json.toString());
		return "exito";
	}

	protected String mostrarRegistro(ActionMapping mapping,
			MasterForm formulario,
			HttpServletRequest request,
			HttpServletResponse response,
			boolean bEditable,
			boolean bNuevo) throws ClsExceptions
	{
		SIGAMantenimientoCertificadosIncompatibilidadesForm form = (SIGAMantenimientoCertificadosIncompatibilidadesForm) formulario;

		String sIdInstitucion = form.getIdInstitucion();
		String sIdTipoProducto = form.getIdTipoProducto();
		String sIdProducto = form.getIdProducto();
		String sIdProductoInstitucion = form.getIdProductoInstitucion();

		String sIdTipoProd_Incompatible = "";
		String sIdProd_Incompatible = "";
		String sIdProdInst_Incompatible = "";
		String sDescripcion = "";
		String sMotivo = "";

		if (!bNuevo) {
			Vector vOcultos = form.getDatosTablaOcultos(0);

			sIdTipoProd_Incompatible = (String) vOcultos.elementAt(0);
			sIdProd_Incompatible = (String) vOcultos.elementAt(1);
			sIdProdInst_Incompatible = (String) vOcultos.elementAt(2);
			sDescripcion = (String) vOcultos.elementAt(3);
			sMotivo = (String) vOcultos.elementAt(4);
		}

		Vector datos = new Vector();
		Hashtable htDatos = new Hashtable();

		htDatos.put("idInstitucion", sIdInstitucion);
		htDatos.put("idTipoProducto", sIdTipoProducto);
		htDatos.put("idProducto", sIdProducto);
		htDatos.put("idProductoInstitucion", sIdProductoInstitucion);
		htDatos.put("idTipoProd_Incompatible", sIdTipoProd_Incompatible);
		htDatos.put("idProd_Incompatible", sIdProd_Incompatible);
		htDatos.put("idProdInst_Incompatible", sIdProdInst_Incompatible);
		htDatos.put("descripcion", sDescripcion);
		htDatos.put("motivo", sMotivo);

		datos.add(htDatos);

		request.setAttribute("datos", datos);
		request.setAttribute("editable", bEditable ? "1" : "0");
		request.setAttribute("nuevo", bNuevo ? "1" : "0");

		if (bEditable) {
			Hashtable hashBackUp = new Hashtable();

			hashBackUp.put(CerPlantillasBean.C_IDINSTITUCION, sIdInstitucion);
			hashBackUp.put(CerPlantillasBean.C_IDTIPOPRODUCTO, sIdTipoProducto);
			hashBackUp.put(CerPlantillasBean.C_IDPRODUCTO, sIdProducto);
			hashBackUp.put(CerPlantillasBean.C_IDPRODUCTOINSTITUCION, sIdProductoInstitucion);

			request.getSession().setAttribute("DATABACKUP", hashBackUp);
		}

		return "mostrar";
	}

	protected String grabar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response, boolean nuevo) throws ClsExceptions, SIGAException, JSONException, IOException
	{
		// Controles y variables generales
		UsrBean user = this.getUserBean(request);
		SIGAMantenimientoCertificadosIncompatibilidadesForm form = (SIGAMantenimientoCertificadosIncompatibilidadesForm) formulario;
		CerIncompatibilidadesAdm admIncompatibilidades = new CerIncompatibilidadesAdm(user);

		// obteniendo la incompatibilidad a insertar
		String idInstitucion = (String) request.getParameter("idInstitucion");
		String idTipoProducto = (String) request.getParameter("idTipoProducto");
		String idProducto = (String) request.getParameter("idProducto");
		String idProductoInstitucion = (String) request.getParameter("idProductoInstitucion");
		String idTipoProd_Incompatible = (String) request.getParameter("idTipoProd_Incompatible");
		String idProd_Incompatible = (String) request.getParameter("idProd_Incompatible");
		String idProdInst_Incompatible = (String) request.getParameter("idProdInst_Incompatible");
		String motivo = (String) request.getParameter("motivo");

		JSONObject json = new JSONObject();
		// insertando
		if (nuevo) {
			CerIncompatibilidadesBean incompatibilidadBean = new CerIncompatibilidadesBean();
			incompatibilidadBean.setIdInstitucion(Integer.valueOf(idInstitucion));
			incompatibilidadBean.setIdTipoProducto(Integer.valueOf(idTipoProducto));
			incompatibilidadBean.setIdProducto(Integer.valueOf(idProducto));
			incompatibilidadBean.setIdProductoInstitucion(Integer.valueOf(idProductoInstitucion));
			incompatibilidadBean.setIdTipoProd_Incompatible(Integer.valueOf(idTipoProd_Incompatible));
			incompatibilidadBean.setIdProd_Incompatible(Integer.valueOf(idProd_Incompatible));
			incompatibilidadBean.setIdProdInst_Incompatible(Integer.valueOf(idProdInst_Incompatible));

			incompatibilidadBean.setMotivo(motivo);

			if (admIncompatibilidades.insert(incompatibilidadBean)) {
				request.setAttribute("mensaje", "messages.deleted.success");
				json.put("msg", "Incompatibilidad insertada correctamente");
			} else {
				request.setAttribute("mensaje", "error.messages.deleted");
				json.put("msg", "No se ha podido insertar la incompatibilidad");
			}
		} else {
			Hashtable<String, String> incompatibilidadHash = new Hashtable<String, String>();
			incompatibilidadHash.put(CerIncompatibilidadesBean.C_IDINSTITUCION, idInstitucion);
			incompatibilidadHash.put(CerIncompatibilidadesBean.C_IDTIPOPRODUCTO, idTipoProducto);
			incompatibilidadHash.put(CerIncompatibilidadesBean.C_IDPRODUCTO, idProducto);
			incompatibilidadHash.put(CerIncompatibilidadesBean.C_IDPRODUCTOINSTITUCION, idProductoInstitucion);
			incompatibilidadHash.put(CerIncompatibilidadesBean.C_IDTIPOPROD_INCOMPATIBLE, idTipoProd_Incompatible);
			incompatibilidadHash.put(CerIncompatibilidadesBean.C_IDPROD_INCOMPATIBLE, idProd_Incompatible);
			incompatibilidadHash.put(CerIncompatibilidadesBean.C_IDPRODINST_INCOMPATIBLE, idProdInst_Incompatible);
			CerIncompatibilidadesBean incompatibilidadBean = (CerIncompatibilidadesBean) admIncompatibilidades.select(incompatibilidadHash).firstElement();

			incompatibilidadBean.setMotivo(motivo);

			if (admIncompatibilidades.update(incompatibilidadBean)) {
				request.setAttribute("mensaje", "messages.deleted.success");
				json.put("msg", "Incompatibilidad actualizada correctamente");
			} else {
				request.setAttribute("mensaje", "error.messages.deleted");
				json.put("msg", "No se ha podido actualizar la incompatibilidad");
			}
		}

		// json.
		response.setContentType("text/x-json;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Content-Type", "application/json");
		response.setHeader("X-JSON", json.toString());
		response.getWriter().write(json.toString());
		return "exito";
	}

}