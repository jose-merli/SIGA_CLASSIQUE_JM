// VERSIONES:
// jose.barrientos 29/01/2009 
//
package com.siga.gratuita.action;

import java.util.Hashtable;

import javax.servlet.http.*;
import javax.transaction.*;

import org.apache.struts.action.*;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.*;
import com.siga.general.*;
import com.siga.gratuita.form.MantenimientoDestinatariosRetencionesForm;
import com.atos.utils.ClsExceptions;

import java.util.Vector;

/**
 * Clase action form del caso de uso MANTENIMIENTO DESTINATARIOS RETENCIONES
 * 
 * @author jose.barrientos 29/01/2009
 */
public class MantenimientoDestinatariosRetencionesAction extends MasterAction {

	public ActionForward executeInternal(ActionMapping mapping,
			ActionForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {

		String mapDestino = "exception";
		MasterForm miForm = null;

		try {
			do {
				miForm = (MasterForm) formulario;
				if (miForm != null) {
					String accion = miForm.getModo();
					if (accion == null || accion.equalsIgnoreCase("")
							|| accion.equalsIgnoreCase("abrir")) {
						mapDestino = abrir(mapping, miForm, request, response);
						break;
					} else if (accion.equalsIgnoreCase("buscar")) {
						mapDestino = buscar(mapping, miForm, request, response);
						break;
					} else if (accion.equalsIgnoreCase("nuevo")) {
						mapDestino = nuevo(mapping, miForm, request, response);
						break;
					} else if (accion.equalsIgnoreCase("ver")) {
						mapDestino = editar(mapping, miForm, request, response);
						break;
					} else if (accion.equalsIgnoreCase("editar")) {
						mapDestino = editar(mapping, miForm, request, response);
						break;
					} else if (accion.equalsIgnoreCase("borrar")) {
						mapDestino = borrar(mapping, miForm, request, response);
						break;
					} else if (accion.equalsIgnoreCase("buscarCuenta")) {
						mapDestino = buscarCuenta(mapping, miForm, request, response);
						break; 
					}else {
						return super.executeInternal(mapping, formulario,
								request, response);
					}
				}
			} while (false);

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) {
				throw new ClsExceptions("El ActionMapping no puede ser nulo");
			}
			return mapping.findForward(mapDestino);
		} catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error", e,
					new String[] { "modulo.facturacionSJCS" });
		}
	}

	/**
	 * Metodo que implementa el modo abrir. Con este metodo entramos a la
	 * primera pantalla del caso de uso.
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
	protected String abrir(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {

		return "abrir";
	}

	/**
	 * Metodo que implementa el modo editar. Pasa los parametros necesarios a la
	 * pagina de las pestanhas: <br>
	 * idInstitucion, idPagosJG y la accion.
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
	protected String editar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
		try{
			MantenimientoDestinatariosRetencionesForm form = (MantenimientoDestinatariosRetencionesForm)formulario;
			FcsDestinatariosRetencionesAdm adm = new FcsDestinatariosRetencionesAdm(this.getUserBean(request));
			// Recuperamos los datos ocultos de listadoDestinatariosRetenciones.jsp 
			Vector vOcultos = form.getDatosTablaOcultos(0);
			String idInstitucion = vOcultos.elementAt(0).toString();
			String idDestinatario = vOcultos.elementAt(1).toString();
			// Buscamos el elemento en la base de datos
			Hashtable destinatario = adm.getDestinatario(idInstitucion, idDestinatario);
			// Ponemos sus valores en el formulario
			form.setCodigoExt(UtilidadesHash.getString(destinatario,FcsDestinatariosRetencionesBean.C_CODIGOEXT));
			form.setIdDestinatario(UtilidadesHash.getString(destinatario,FcsDestinatariosRetencionesBean.C_IDDESTINATARIO));
			form.setIdInstitucion(UtilidadesHash.getString(destinatario,FcsDestinatariosRetencionesBean.C_IDINSTITUCION));
			form.setOrden(UtilidadesHash.getString(destinatario,FcsDestinatariosRetencionesBean.C_ORDEN));
			form.setNombre(UtilidadesHash.getString(destinatario,FcsDestinatariosRetencionesBean.C_NOMBRE));
			form.setCuentaContable(UtilidadesHash.getString(destinatario,FcsDestinatariosRetencionesBean.C_CUENTACONTABLE));
			
			request.setAttribute("destinatario", destinatario);
			request.getSession().setAttribute("DATABACKUP", destinatario);
		}catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
	   	 }
		return "editar";
	}

	/**
	 * Metodo que implementa el modo ver. Pasa los parametros necesarios a la
	 * pagina de las pestanhas: <br>
	 * idInstitucion, idPagosJG y la accion.
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
	protected String ver(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {

		return "editar";
	}

	/**
	 * Metodo que implementa el modo insertar.
	 * 
	 * @param mapping - Mapeo de los struts
	 * @param formulario - Action Form asociado a este Action
	 * @param request - objeto llamada HTTP
	 * @param response - objeto respuesta HTTP
	 * @return String Destino del action
	 * @exception ClsExceptions  En cualquier caso de error
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
		UserTransaction t = null;
		try {
			t = this.getUserBean(request).getTransaction();
			MantenimientoDestinatariosRetencionesForm form = (MantenimientoDestinatariosRetencionesForm) formulario;
			FcsDestinatariosRetencionesBean bean = new FcsDestinatariosRetencionesBean();
			FcsDestinatariosRetencionesAdm adm = new FcsDestinatariosRetencionesAdm(this.getUserBean(request));
			Integer idInstitucion = this.getIDInstitucion(request);
			// Recuperamos los datos del formulario y los añadimos al bean
			bean.setCuentaContable(form.getCuentaContable());
			bean.setIdInstitucion(idInstitucion);
			bean.setNombre(form.getNombre());
			bean.setOrden(form.getOrden());
			// El idDestinatario los tendremos que calcular al ser un nuevo destinatario
			bean.setIdDestinatario(adm.getNuevoId(idInstitucion.toString()));
			t.begin();
			if (!adm.insert(bean)) {
				t.rollback();
				throw new SIGAException (adm.getError());
			}
			t.commit();
		} 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
	   	 }

		return this.exitoModal("messages.inserted.success", request);
	}

	/**
	 * Metodo que implementa el modo nuevo. Pasa los parametros necesarios a la
	 * pagina de las pestanhas: <br>
	 * idInstitucion, idPagosJG y la accion.
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
	protected String nuevo(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {

		return "editar";
	}

	/**
	 * Método que implementa el modo buscar. Realiza la busqueda de los
	 * destinatarios por su nombre
	 * 
	 * @param mapping - Mapeo de los struts
	 * @param formulario - Action Form asociado a este Action
	 * @param request - objeto llamada HTTP
	 * @param response - objeto respuesta HTTP
	 * @return String Destino del action
	 * @exception ClsExceptions En cualquier caso de error
	 */
	protected String buscar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {

		try {

			FcsDestinatariosRetencionesAdm adm = new FcsDestinatariosRetencionesAdm(this.getUserBean(request));
			MantenimientoDestinatariosRetencionesForm miForm = (MantenimientoDestinatariosRetencionesForm) formulario;
			String idInstitucion = this.getIDInstitucion(request).toString();
			// recuperamos del formulario el nombre del destinatario buscado
			String nombre = miForm.getNombreBuscado();
			// Llamamos al adm y guardamos losresultados en la request
			Vector vDestinatarios = adm.buscarDestinatario(idInstitucion,nombre);
			request.setAttribute("resultado", vDestinatarios);
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.facturacionSJCS" }, e, null);
		}
		return "resultados";
	}

	/**
	 * Método que implementa la accion borrar
	 * 
	 * @param mapping - Mapeo de los struts
	 * @param formulario - Action Form asociado a este Action
	 * @param request - objeto llamada HTTP
	 * @param response - objeto respuesta HTTP
	 * @return String Destino del action
	 * @exception ClsExceptions  En cualquier caso de error
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {

		UserTransaction t = null;
		try {
			t = this.getUserBean(request).getTransaction();
			MantenimientoDestinatariosRetencionesForm form = (MantenimientoDestinatariosRetencionesForm) formulario;
			FcsDestinatariosRetencionesBean bean = new FcsDestinatariosRetencionesBean();
			FcsDestinatariosRetencionesAdm adm = new FcsDestinatariosRetencionesAdm(this.getUserBean(request));
			// Recuperamos de los datos ocultos del form el idDestinatario
			Vector vOcultos = form.getDatosTablaOcultos(0);
			String idDestinatario = vOcultos.elementAt(1).toString();
			bean.setIdDestinatario(new Integer(idDestinatario));
			// La institucion la copiamos de la request
			bean.setIdInstitucion(this.getIDInstitucion(request));
			t.begin();
			if (!adm.delete(bean)) {
				t.rollback();
				throw new ClsExceptions (adm.getError());
			}
			t.commit();

	     } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
	   	 }

		 return this.exitoRefresco("messages.deleted.success", request);
	}

	/**
	 * Implementa la modificacion de un destinatario
	 * 
	 * @param mapping - Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la información. De tipo MasterForm.
	 * @param request - Información de sesión. De tipo HttpServletRequest
	 * @param response - De tipo HttpServletResponse
	 * @return String que indicará la siguiente acción a llevar a cabo.
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
		UserTransaction t = null;
		try {
			// Copiamos los datos existentes del destinatario
			Hashtable hashOriginal = (Hashtable)request.getSession().getAttribute("DATABACKUP");
			t = this.getUserBean(request).getTransaction();
			MantenimientoDestinatariosRetencionesForm form = (MantenimientoDestinatariosRetencionesForm) formulario;
			FcsDestinatariosRetencionesBean bean = new FcsDestinatariosRetencionesBean();
			FcsDestinatariosRetencionesAdm adm = new FcsDestinatariosRetencionesAdm(this.getUserBean(request));
			// En el bean copiamos el backup y rellenamos el resto de campos
			bean.setOriginalHash(hashOriginal);
			Integer idInstitucion = this.getIDInstitucion(request);
			bean.setIdInstitucion(idInstitucion);
			// Sacamos todo del formulario menos el idDestinatario que no se puede modificar
			bean.setIdDestinatario(UtilidadesHash.getInteger(hashOriginal, FcsDestinatariosRetencionesBean.C_IDDESTINATARIO));
			bean.setCuentaContable(form.getCuentaContable());
			bean.setNombre(form.getNombre());
			String orden = form.getOrden();
			// Si el orden es null lo dejamos en ""
			if (!(orden == null)&&!(orden.equalsIgnoreCase(""))){
				bean.setOrden(form.getOrden());
			}else{
				bean.setOrden("");
			}
			t.begin();
			if (!adm.update(bean)) {
				t.rollback();
				throw new SIGAException (adm.getError());
			}
			t.commit();
		} 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
	   	 }

		return this.exitoModal("messages.inserted.success", request);
	}

	protected String buscarCuenta (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions 
	{
		try {
			MantenimientoDestinatariosRetencionesForm miform = (MantenimientoDestinatariosRetencionesForm)formulario;
			FcsDestinatariosRetencionesAdm destinatarioAdm= new FcsDestinatariosRetencionesAdm(this.getUserBean(request));
			String idDestinatario = miform.getIdDestinatario();
			String where = " where iddestinatario =" +idDestinatario+
					       " and idinstitucion="+this.getUserBean(request).getLocation();
			Vector resultadoCuenta = destinatarioAdm.select(where);
			if (resultadoCuenta!=null && resultadoCuenta.size()>0) {
				FcsDestinatariosRetencionesBean destinatarioBean = (FcsDestinatariosRetencionesBean) resultadoCuenta.get(0);
			}
			request.setAttribute("resultadoCuenta",resultadoCuenta);
			request.setAttribute("nombreObjetoDestino",miform.getNombreObjetoDestino());
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return "buscarCuenta";
	}
}
