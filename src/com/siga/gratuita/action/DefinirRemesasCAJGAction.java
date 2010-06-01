//Clase: DefinirExpedientesSOJAction 
//Autor: julio.vicente@atosorigin.com
//Ultima modificación: 14/02/2005

package com.siga.gratuita.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import weblogic.management.timer.Timer;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.GstDate;
import com.atos.utils.ReadProperties;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.GestorContadores;
import com.siga.Utilidades.Paginador;
import com.siga.Utilidades.PaginadorBind;
import com.siga.Utilidades.PaginadorCaseSensitive;
import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CajgEJGRemesaAdm;
import com.siga.beans.CajgEJGRemesaBean;
import com.siga.beans.CajgProcedimientoRemesaBean;
import com.siga.beans.CajgRemesaAdm;
import com.siga.beans.CajgRemesaBean;
import com.siga.beans.CajgRemesaEstadosAdm;
import com.siga.beans.CajgRemesaEstadosBean;
import com.siga.beans.CajgRespuestaEJGRemesaAdm;
import com.siga.beans.CajgRespuestaEJGRemesaBean;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.GenClientesTemporalBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.ScsEJGAdm;
import com.siga.beans.ScsEJGBean;
import com.siga.beans.ScsEstadoEJGAdm;
import com.siga.beans.ScsEstadoEJGBean;
import com.siga.beans.ScsGuardiasColegiadoAdm;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.beans.ScsTipoEJGBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefinicionRemesas_CAJG_Form;
import com.siga.gratuita.form.DefinirEJGForm;
import com.siga.informes.MasterWords;
import com.siga.ws.CajgConfiguracion;
import com.siga.ws.SIGAWSClientAbstract;
import com.siga.ws.SIGAWSListener;




/**
* Maneja las acciones que se pueden realizar sobre la tabla SCS_EJG
*/
public class DefinirRemesasCAJGAction extends MasterAction {

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
	 * @exception SIGAExceptions
	 *                En cualquier caso de error
	 */

	protected ActionForward executeInternal(ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {

		String mapDestino = "exception";
		MasterForm miForm = null;

		try {
			miForm = (MasterForm) formulario;
			if (miForm == null) {
				return mapping.findForward(mapDestino);
			}

			String accion = miForm.getModo();

			// La primera vez que se carga el formulario
			// Abrir
			if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")) {
				mapDestino = abrir(mapping, miForm, request, response);
			
			} else if (accion.equalsIgnoreCase("aniadirARemesa")) {
				mapDestino = aniadirARemesa(mapping, miForm, request, response);
			} else if (accion.equalsIgnoreCase("buscarPorEJG")) {
				mapDestino = buscarPorEJG(mapping, miForm, request, response);
			} else if (accion.equalsIgnoreCase("AniadirExpedientes")) {
				mapDestino = aniadirExpedientes(mapping, miForm, request, response);
			} else if (accion.equalsIgnoreCase("BuscarListos")) {
				mapDestino = buscarListos(mapping, miForm, request, response);
			} else if (accion.equalsIgnoreCase("descargar")) {
				mapDestino = descargar(mapping, miForm, request, response);
			} else if (accion.equalsIgnoreCase("generarFichero")) {
				mapDestino = generarFichero(mapping, miForm, request, response);			
			} else if (accion.equalsIgnoreCase("envioFTP")) {
				mapDestino = envioFTP(mapping, miForm, request, response);
			} else if (accion.equalsIgnoreCase("envioWS")) {
				mapDestino = envioWS(mapping, miForm, request, response);
			} else if (accion.equalsIgnoreCase("respuestaFTP")) {
				mapDestino = respuestaFTP(mapping, miForm, request, response);			
				
			} else if (accion.equalsIgnoreCase("marcarEnviada")) {
				mapDestino = marcarEnviada(mapping, miForm, request, response);
			} else if (accion.equalsIgnoreCase("borrarRemesa")) {
				mapDestino = borrarRemesa(mapping, miForm, request, response);
			} else if (accion.equalsIgnoreCase("buscarListosInicio")) {
				request.getSession().removeAttribute("DATAPAGINADOR");
				mapDestino = buscarListos(mapping, miForm, request, response);
			} else if (accion.equalsIgnoreCase("erroresResultadoCAJG")) {
				mapDestino = erroresResultadoCAJG(mapping, miForm, request, response);
			} else if (accion.equalsIgnoreCase("descargarLog")) {
				mapDestino = descargarLog(mapping, miForm, request, response);				
			} else {
				return super.executeInternal(mapping, formulario, request, response);
			}

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) {
				// mapDestino = "exception";
				if (miForm.getModal().equalsIgnoreCase("TRUE")) {
					request.setAttribute("exceptionTarget", "parent.modal");
				}

				// throw new ClsExceptions("El ActionMapping no puede ser
				// nulo");
				throw new ClsExceptions("El ActionMapping no puede ser nulo", "", "0", "GEN00", "15");
			}

		} catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error", e, new String[] { "modulo.gratuita" });
		}
		return mapping.findForward(mapDestino);
	}

	/**
	 * 
	 * @param mapping
	 * @param miForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */

	private String respuestaFTP(ActionMapping mapping, MasterForm miForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ejecutaBackground(miForm, request, 1);
		return exito("messages.cajg.recuperandoRespuestaFTP", request);
	}



	/**
	 * Rellena un hash con los valores recogidos del formulario y realiza la
	 * consulta a partir de esos datos. Almacena un vector con los resultados en
	 * la sesión con el nombre "resultado"
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
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		Hashtable miHash = new Hashtable();
		String consulta = "";

		try {

			DefinicionRemesas_CAJG_Form miForm = (DefinicionRemesas_CAJG_Form) formulario;
			
			miHash = miForm.getDatos();

			HashMap databackup = new HashMap();

			if (request.getSession().getAttribute("DATAPAGINADOR") != null) {
				databackup = (HashMap) request.getSession().getAttribute("DATAPAGINADOR");
				Paginador paginador = (Paginador) databackup.get("paginador");
				Vector datos = new Vector();

				// Si no es la primera llamada, obtengo la página del request y
				// la busco con el paginador
				String pagina = request.getParameter("pagina");

				if (paginador != null) {
					if (pagina != null) {
						datos = paginador.obtenerPagina(Integer.parseInt(pagina));
					} else {// cuando hemos editado un registro de la busqueda y
						// volvemos a la paginacion
						datos = paginador.obtenerPagina((paginador.getPaginaActual()));
					}
				}

				databackup.put("paginador", paginador);
				databackup.put("datos", datos);

			} else {

				databackup = new HashMap();

				// obtengo datos de la consulta
				
				Vector datos = null;

				/*
				 * Construimos la primera parte de la consulta, donde escogemos
				 * los campos a recuperar y las tablas necesarias
				 */
				consulta = "select r.idremesa AS IDREMESA," + "r.prefijo AS PREFIJO," + "r.numero AS NUMERO," + "r.sufijo AS SUFIJO,"
						+ "r.descripcion AS DESCRIPCION_REMESA," + "" +
						"(select max(idestado)" + " from cajg_remesaestados" + " where idinstitucion = e.idinstitucion"
						+ " and idremesa = e.idremesa)"//se añade el select max() aqui en lugar de e.idestado pq si no no funciona al poner alter session set NLS_COMP=LINGUISTIC NLS_SORT=GENERIC_BASELETTER
						+ " AS IDESTADO"
						+ "," + "f_siga_getrecurso(t.descripcion, " + this.getLenguaje(request)
						+ ") AS ESTADO," + "f_siga_get_fechaEstadoRemesa(e.idinstitucion, e.idremesa, 1) AS FECHAGENERACION,"
						+ "f_siga_get_fechaEstadoRemesa(e.idinstitucion, e.idremesa, 2) AS FECHAENVIO,"
						+ "f_siga_get_fechaEstadoRemesa(e.idinstitucion, e.idremesa, 3) AS FECHARECEPCION"
						+ " from cajg_remesa r, cajg_remesaestados e, cajg_tipoestadoremesa t" + " where r.idinstitucion = " + this.getIDInstitucion(request)
						+ "" + " and r.idinstitucion = e.idinstitucion" + " and r.idremesa = e.idremesa" + " and e.idestado = t.idestado"
						+ " and e.idestado = (select max(idestado)" + " from cajg_remesaestados" + " where idinstitucion = e.idinstitucion"
						+ " and idremesa = e.idremesa) ";
				/*
				 * " and e.fechamodificacion = (select max(fechamodificacion)"+ "
				 * from cajg_remesaestados"+ " where idinstitucion =
				 * e.idinstitucion"+ " and idremesa = e.idremesa) ";
				 */

				if ((String) miHash.get("PREFIJO") != null && (!((String) miHash.get("PREFIJO")).equals(""))) {
					consulta += " and r.prefijo='" + (String) miHash.get("PREFIJO") + "'";
				}
				if ((String) miHash.get("NUMERO") != null && (!((String) miHash.get("NUMERO")).equals(""))) {
					consulta += " and r.numero='" + (String) miHash.get("NUMERO") + "'";
				}
				if ((String) miHash.get("SUFIJO") != null && (!((String) miHash.get("SUFIJO")).equals(""))) {
					consulta += " and r.sufijo='" + (String) miHash.get("SUFIJO") + "'";
				}
				if ((String) miHash.get("DESCRIPCION") != null && (!((String) miHash.get("DESCRIPCION")).equals(""))) {
					// consulta +=" and r.descripcion like
					// '%"+(String)miHash.get("DESCRIPCION")+"'";
					consulta += " AND " + ComodinBusquedas.prepararSentenciaNLS(((String) miHash.get("DESCRIPCION")).trim(), "r.descripcion");
				}
				if ((String) miHash.get("IDESTADO") != null && (!((String) miHash.get("IDESTADO")).equals(""))) {
					consulta += " and e.idestado=" + (String) miHash.get("IDESTADO") + "";
				}
				if ((String) miHash.get("FECHAGENERACION") != null && (!((String) miHash.get("FECHAGENERACION")).equals(""))) {
					consulta += " and f_siga_get_fechaEstadoRemesa(e.idinstitucion, e.idremesa, 1)='" + (String) miHash.get("FECHAGENERACION") + "'";

				}
				if ((String) miHash.get("FECHAENVIO") != null && (!((String) miHash.get("FECHAENVIO")).equals(""))) {
					consulta += " and f_siga_get_fechaEstadoRemesa(e.idinstitucion, e.idremesa, 2)='" + (String) miHash.get("FECHAENVIO") + "'";
				}
				if ((String) miHash.get("FECHARECEPCION") != null && (!((String) miHash.get("FECHARECEPCION")).equals(""))) {

					consulta += " and f_siga_get_fechaEstadoRemesa(e.idinstitucion, e.idremesa, 3)='" + (String) miHash.get("FECHARECEPCION") + "'";
				}
				consulta += " order by r.prefijo,r.numero,r.sufijo";

				// Rellena un vector de Hastable con la claves primarias de la
				// tabla
				// scs_ejg para llevar el control de los check

				PaginadorCaseSensitive paginador = new PaginadorCaseSensitive(consulta);
				int totalRegistros = paginador.getNumeroTotalRegistros();

				if (totalRegistros == 0) {
					paginador = null;
				}

				databackup.put("paginador", paginador);
				if (paginador != null) {
					datos = paginador.obtenerPagina(1);
					databackup.put("datos", datos);
					request.getSession().setAttribute("DATAPAGINADOR", databackup);
					request.getSession().setAttribute("HORABUSQUEDA", UtilidadesBDAdm.getFechaCompletaBD("es"));
				}

				// resultado = admBean.selectGenerico(consulta);
				// request.getSession().setAttribute("resultado",v);

			}
			// En "DATOSFORMULARIO" almacenamos el identificador del letrado
			miHash.put("BUSQUEDAREALIZADA", "1");
			request.getSession().setAttribute("DATOSFORMULARIO", miHash);

		} catch (Exception e) {
			throwExcp("messages.general.error", e, null);
		}
		return "listarRemesas";
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
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		try {
			HttpSession session = request.getSession();
			DefinicionRemesas_CAJG_Form miForm = (DefinicionRemesas_CAJG_Form) formulario;

			// Recuperamos los datos del registro que hemos seleccionado
			Vector ocultos = miForm.getDatosTablaOcultos(0);
			Vector visibles = miForm.getDatosTablaVisibles(0);
			Hashtable miHash = new Hashtable();

			if ((ocultos != null && visibles != null)) {
				miHash.put(CajgRemesaBean.C_IDREMESA, ocultos.get(0));
				miHash.put(CajgRemesaBean.C_IDINSTITUCION, ocultos.get(1));
				session.removeAttribute("DATAPAGINADOR");

			} else {
				session.removeAttribute("DATAPAGINADOR");
				miHash.put(CajgRemesaBean.C_IDREMESA, miForm.getIdRemesa());
				miHash.put(CajgRemesaBean.C_IDINSTITUCION, this.getIDInstitucion(request).toString());

			}
			CajgRemesaAdm remesaAdm = new CajgRemesaAdm(this.getUserBean(request));
			CajgRemesaBean b = new CajgRemesaBean();
			Vector a = remesaAdm.selectByPK(miHash);
			b = (CajgRemesaBean) a.get(0);
			Hashtable h = b.getOriginalHash();

			// Entramos al formulario en modo 'modificación'
			session.setAttribute("accion", "editar");

			request.setAttribute("REMESA", h);
		} catch (Exception e) {
			throwExcp("messages.general.error", e, null);
		}
		return "editar";
	}
	
	protected String erroresResultadoCAJG(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		try {
			HttpSession session = request.getSession();
			DefinicionRemesas_CAJG_Form miForm = (DefinicionRemesas_CAJG_Form) formulario;
			
			String idEjgRemesa = miForm.getIdEjgRemesa();
			Hashtable hash = new Hashtable();
			hash.put(CajgRespuestaEJGRemesaBean.C_IDEJGREMESA, idEjgRemesa);
			CajgRespuestaEJGRemesaAdm cajgRespuestaEJGRemesaAdm = new CajgRespuestaEJGRemesaAdm(getUserBean(request));
			Vector vector = cajgRespuestaEJGRemesaAdm.select(hash);
			request.setAttribute("DATOS_RESPUESTA_CAJG", vector);

		} catch (Exception e) {
			throwExcp("messages.general.error", e, null);
		}
		return "erroresResultadoCAJG";
	}

	/**
	 * 
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		try {
			HttpSession session = request.getSession();
			DefinicionRemesas_CAJG_Form miForm = (DefinicionRemesas_CAJG_Form) formulario;

			// Recuperamos los datos del registro que hemos seleccionado
			Vector ocultos = miForm.getDatosTablaOcultos(0);
			Vector visibles = miForm.getDatosTablaVisibles(0);
			Hashtable miHash = new Hashtable();

			if ((ocultos != null && visibles != null)) {
				miHash.put(CajgRemesaBean.C_IDREMESA, ocultos.get(0));
				miHash.put(CajgRemesaBean.C_IDINSTITUCION, ocultos.get(1));
				session.removeAttribute("DATAPAGINADOR");

			} else {
				session.removeAttribute("DATAPAGINADOR");
				miHash.put(CajgRemesaBean.C_IDREMESA, miForm.getIdRemesa());
				miHash.put(CajgRemesaBean.C_IDINSTITUCION, this.getIDInstitucion(request).toString());

			}
			CajgRemesaAdm RemesaAdm = new CajgRemesaAdm(this.getUserBean(request));
			CajgRemesaBean b = new CajgRemesaBean();
			Vector a = RemesaAdm.selectByPK(miHash);
			b = (CajgRemesaBean) a.get(0);
			Hashtable h = b.getOriginalHash();

			// Entramos al formulario en modo 'modificación'
			session.setAttribute("accion", "consultar");

			request.setAttribute("REMESA", h);
		} catch (Exception e) {
			throwExcp("messages.general.error", e, null);
		}
		return "editar";
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
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		// si el usuario logado es letrado consultar en BBDD el nColegiado para
		// mostrar en la jsp
		UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");

		DefinicionRemesas_CAJG_Form miForm = (DefinicionRemesas_CAJG_Form) formulario;

		try {
			Integer idinstitucion = new Integer(usr.getLocation());
			GestorContadores gcRemesa = new GestorContadores(this.getUserBean(request));
			Hashtable contadorTablaHashRemesa = gcRemesa.getContador(idinstitucion, "REMESA");
			
			String siguiente = gcRemesa.getNuevoContador(contadorTablaHashRemesa);
  		

			String longitud = contadorTablaHashRemesa.get("LONGITUDCONTADOR").toString();
			String prefijo = UtilidadesHash.getString(contadorTablaHashRemesa, "PREFIJO");
			String sufijo = UtilidadesHash.getString(contadorTablaHashRemesa, "SUFIJO");
			String modocontador = contadorTablaHashRemesa.get("MODO").toString();

			miForm.setNumero(siguiente);
			miForm.setPrefijo(prefijo);
			miForm.setSufijo(sufijo);
			miForm.setLongitudContador(longitud);
			miForm.setModoContador(modocontador);

			request.setAttribute("modoContador", modocontador);

		} catch (SIGAException e) {
			throw e;
		} catch (Exception e) {
			miForm.setNumero("");
			miForm.setPrefijo("");
			miForm.setSufijo("");
			miForm.setLongitudContador("");
			miForm.setModoContador("");
			request.setAttribute("modoContador", "");
		}
		return "insertarRemesa";
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
	protected synchronized String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {

		UserTransaction tx = null;

		try {

			Hashtable miHash = new Hashtable();
			HttpSession session = request.getSession();

			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			DefinicionRemesas_CAJG_Form miForm = (DefinicionRemesas_CAJG_Form) formulario;

			// }
			CajgRemesaAdm remesaAdm = new CajgRemesaAdm(this.getUserBean(request));
			CajgRemesaEstadosAdm remesaEstadosAdm = new CajgRemesaEstadosAdm(this.getUserBean(request));

			tx = usr.getTransaction();
			tx.begin();
			GestorContadores gcRemesa = new GestorContadores(this.getUserBean(request));
			
			Hashtable contadorTablaHashRemesa = gcRemesa.getContador(this.getIDInstitucion(request), "REMESA");
			String siguiente = gcRemesa.getNuevoContador(contadorTablaHashRemesa);			
						
			miForm.setNumero(siguiente);
			miHash.put(CajgRemesaBean.C_PREFIJO, contadorTablaHashRemesa.get("PREFIJO"));
			miHash.put(CajgRemesaBean.C_SUFIJO, contadorTablaHashRemesa.get("SUFIJO"));
			miHash.put(CajgRemesaBean.C_NUMERO, siguiente);

			gcRemesa.setContador(contadorTablaHashRemesa, siguiente);

			miHash.put(CajgRemesaBean.C_IDINSTITUCION, this.getIDInstitucion(request));
			miHash.put(CajgRemesaBean.C_DESCRIPCION, miForm.getDescripcion());
			miHash.put(CajgRemesaEstadosBean.C_FECHAREMESA, GstDate.getApplicationFormatDate("", miForm.getFechaGeneracion()));
			miHash.put(CajgRemesaEstadosBean.C_IDESTADO, "0");
			miHash.put(CajgRemesaBean.C_IDREMESA, remesaAdm.SeleccionarMaximo(this.getIDInstitucion(request).toString()));

			remesaAdm.insert(miHash);
			remesaEstadosAdm.insert(miHash);

			tx.commit();

			session.setAttribute("accion", "editar");
			request.setAttribute("IDREMESA", miHash.get(CajgRemesaBean.C_IDREMESA));
			request.setAttribute("INSTITUCION", this.getIDInstitucion(request).toString());

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita" }, e, tx);
		}

		request.setAttribute("mensaje", "messages.inserted.success");
		request.setAttribute("modal", "");

		return "exitoRemesa";
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
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		UserTransaction tx = null;
		try {

			Hashtable miHash = new Hashtable();
			HttpSession session = request.getSession();

			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			DefinicionRemesas_CAJG_Form miForm = (DefinicionRemesas_CAJG_Form) formulario;
			Hashtable ocultos = miForm.getDatos();

			CajgRemesaAdm remesaAdm = new CajgRemesaAdm(this.getUserBean(request));

			miHash.put(CajgRemesaBean.C_IDINSTITUCION, this.getIDInstitucion(request));
			miHash.put(CajgRemesaBean.C_IDREMESA, UtilidadesHash.getString(ocultos, "IDREMESA"));
			miHash.put(CajgRemesaBean.C_PREFIJO, UtilidadesHash.getString(ocultos, "PREFIJO"));
			miHash.put(CajgRemesaBean.C_NUMERO, UtilidadesHash.getString(ocultos, "NUMERO"));
			miHash.put(CajgRemesaBean.C_SUFIJO, UtilidadesHash.getString(ocultos, "SUFIJO"));
			miHash.put(CajgRemesaBean.C_DESCRIPCION, miForm.getDescripcion());
			tx = usr.getTransaction();
			tx.begin();
			remesaAdm.updateDirect(miHash, null, null);
			tx.commit();

			session.setAttribute("accion", "editar");
			request.setAttribute("IDREMESA", miHash.get(CajgRemesaBean.C_IDREMESA));
			request.setAttribute("INSTITUCION", this.getIDInstitucion(request).toString());

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita" }, e, tx);
		}
		return exitoRefresco("messages.updated.success", request);
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
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
		UserTransaction tx = null;
		
		CajgEJGRemesaAdm admBean = new CajgEJGRemesaAdm(this.getUserBean(request));
		CajgRespuestaEJGRemesaAdm cajgRespuestaEJGRemesaAdm = new CajgRespuestaEJGRemesaAdm(getUserBean(request));
		Vector ocultos = formulario.getDatosTablaOcultos(0);
		
		Hashtable miHash = new Hashtable();
		
		try {

			String idEjgRemesa = (String)ocultos.get(0);
			miHash.put(CajgEJGRemesaBean.C_IDEJGREMESA, idEjgRemesa);
			
			tx = usr.getTransaction();
			tx.begin();
			cajgRespuestaEJGRemesaAdm.deleteDirect(miHash, new String[]{CajgRespuestaEJGRemesaBean.C_IDEJGREMESA});
			admBean.delete(miHash);			

			Hashtable hashEstado = new Hashtable();
			hashEstado.put(ScsEJGBean.C_IDINSTITUCION, getIDInstitucion(request));
			hashEstado.put(ScsEJGBean.C_ANIO,ocultos.get(1));
			hashEstado.put(ScsEJGBean.C_NUMERO, ocultos.get(2));
			hashEstado.put(ScsEJGBean.C_IDTIPOEJG, ocultos.get(3));

			gestionaEstadoEJG(request, hashEstado);

			// beanEstado.prepararInsert (hashEstado);
			// beanEstado.insert (hashEstado);
			tx.commit();
			request.getSession().removeAttribute("DATAPAGINADOR");

		} catch (Exception e) {
			throwExcp("messages.general.error", e, tx);
		}

		request.setAttribute("hiddenFrame", "1");

		return exitoRefresco("messages.deleted.success", request);
	}

	protected String borrarRemesa(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
		UserTransaction tx = null;

		DefinicionRemesas_CAJG_Form miForm = (DefinicionRemesas_CAJG_Form) formulario;
		
		try {
			CajgRemesaEstadosAdm admBean = new CajgRemesaEstadosAdm(this.getUserBean(request));
			int ultimo_estado = admBean.UltimoEstadoRemesa(this.getIDInstitucion(request).toString(), miForm.getIdRemesa());
			if (ultimo_estado == 0 || ultimo_estado == 1) {// se puede borrar
				Vector datos = null;
				CajgEJGRemesaAdm admEJGRemesa = new CajgEJGRemesaAdm(this.getUserBean(request));
				datos = admEJGRemesa.busquedaEJGRemesa(this.getIDInstitucion(request).toString(), miForm.getIdRemesa());
				// pasamos todos los EJG al estado anterior que tenían antes de
				// estar en la remesa
				tx = usr.getTransaction();
				tx.begin();
				for (int i = 0; i < datos.size(); i++) {
					Hashtable ejg = (Hashtable) datos.get(i);
					gestionaEstadoEJG(request, ejg);
				}
				String idInstitucion = getIDInstitucion(request).toString();
				Hashtable hashRemesa = new Hashtable();
				hashRemesa.put("IDINSTITUCION", idInstitucion);
				hashRemesa.put("IDREMESA", miForm.getIdRemesa());
				CajgRemesaAdm admRemesa = new CajgRemesaAdm(this.getUserBean(request));
				admRemesa.delete(hashRemesa);

				eliminaFicheroTXTGenerado(idInstitucion, miForm.getIdRemesa());

				tx.commit();
			} else {// tiene estado enviado o recibido y no se puede enviar
				throw new SIGAException("No se puede borrar la remesa");
			}

		} catch (Exception e) {
			throwExcp("messages.general.error", e, tx);
		}

		request.setAttribute("hiddenFrame", "1");

		return exitoRefresco("messages.deleted.success", request);
	}

	private void gestionaEstadoEJG(HttpServletRequest request, Hashtable hashtable) throws ClsExceptions {

		ScsEstadoEJGAdm beanEstado = new ScsEstadoEJGAdm(this.getUserBean(request));
		Hashtable idanterior = beanEstado.getEstadoAnterior(hashtable);

		if (idanterior.get(ScsEstadoEJGBean.C_IDESTADOEJG) != null && idanterior.get(ScsEstadoEJGBean.C_IDESTADOEJG).equals(ClsConstants.GENERADO_EN_REMESA)) {
			Hashtable hashEstado = new Hashtable();
			hashEstado.put(ScsEJGBean.C_IDINSTITUCION, hashtable.get(ScsEJGBean.C_IDINSTITUCION));
			hashEstado.put(ScsEJGBean.C_ANIO, hashtable.get(ScsEJGBean.C_ANIO));
			hashEstado.put(ScsEJGBean.C_NUMERO, hashtable.get(ScsEJGBean.C_NUMERO));
			hashEstado.put(ScsEJGBean.C_IDTIPOEJG, hashtable.get(ScsEJGBean.C_IDTIPOEJG));
			hashEstado.put(ScsEstadoEJGBean.C_IDESTADOPOREJG, idanterior.get(ScsEstadoEJGBean.C_IDESTADOPOREJG));
			beanEstado.delete(hashEstado);			
		}

	}

	/**
	 * 
	 * @param request
	 * @param idRemesa
	 * @return
	 */
	public static boolean eliminaFicheroTXTGenerado(String idInstitucion, String idRemesa) {
				
		int numFicheros = 0;
		File file = null;
	    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String rutaAlmacen = rp.returnProperty("cajg.directorioFisicoCAJG") + rp.returnProperty("cajg.directorioCAJGJava");
			
		rutaAlmacen += File.separator + idInstitucion;
		rutaAlmacen += File.separator + idRemesa;
		
		File dir = new File(rutaAlmacen);
		if (dir.exists()) {
			if (dir.listFiles() != null && dir.listFiles().length > 0) {
				
				for (int i = 0; i < dir.listFiles().length ; i++) {
					file = dir.listFiles()[i];
					if (file.isFile() && file.getName().endsWith(".zip")){
						if (file.delete()) {
							numFicheros++;
						}
					}
				}
			}
		}

		return numFicheros>0;
	}
	
	/**
	 * 
	 * @param idInstitucion
	 * @param idRemesa
	 * @return
	 * @throws SIGAException
	 */
	public static File getFichero(String idInstitucion, String idRemesa) throws SIGAException {
		File file = null;
	    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String rutaAlmacen = rp.returnProperty("cajg.directorioFisicoCAJG") + rp.returnProperty("cajg.directorioCAJGJava");
			
		rutaAlmacen += File.separator + idInstitucion;
		rutaAlmacen += File.separator + idRemesa;
		
		File dir = new File(rutaAlmacen);
		if (dir.exists()) {
			if (dir.listFiles() != null && dir.listFiles().length > 0) {
				int numFicheros = 0;
				for (int i = 0; i < dir.listFiles().length ; i++) {
					if (dir.listFiles()[i].isFile() && dir.listFiles()[i].getName().endsWith(".zip")){
						file = dir.listFiles()[i];
						numFicheros++;
					}
				}
									
				if (numFicheros > 1) {
					SIGAException e = new SIGAException("cajg.error.masDe1zip");
					ClsLogging.writeFileLogError("Existe mas de un fichero zip para la remesa " + idRemesa + " del colegio " + idInstitucion, e, 3);
//					throw e;
				}
			}
		}
		
		return file;
	}

	/**
	 * No implementado
	 */
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		/*
		 * Borramos de la sesion las variables que pueden haberse utilizado,
		 * para así evitar posibles problemas con datos erróneos contenidos ahí
		 */
		request.getSession().removeAttribute("DATABACKUP");
		request.getSession().removeAttribute("accion");
		request.getSession().removeAttribute("resultadoTelefonos");
		return "inicio";
	}

	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String volver = request.getParameter("volver");
		if (volver != null && volver.equalsIgnoreCase("SI")) {
			request.setAttribute("VOLVER", "1");
		} else {
			request.setAttribute("VOLVER", "0");
		}
		request.getSession().removeAttribute("DATABACKUP");
		request.getSession().removeAttribute("DATOSFORMULARIO");
		request.getSession().removeAttribute("DATABACKUP");
		request.getSession().removeAttribute("accion");
		return "inicio";
	}

	/**
	 * No implementado
	 */
	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		ScsGuardiasColegiadoAdm admGuardiasColegiado = new ScsGuardiasColegiadoAdm(this.getUserBean(request));
		ScsGuardiasColegiadoAdm admTemporal = new ScsGuardiasColegiadoAdm(this.getUserBean(request));

		Hashtable hashTemporal = new Hashtable();
		Vector letrados = new Vector();

		String contador = "", idinstitucion = "", idturno = "", idguardia = "", consulta = "";

		// Recuperamos los valores necesarios para llamar al PL
		idinstitucion = formulario.getDatos().get("IDINSTITUCION").toString();
		idturno = formulario.getDatos().get("GUARDIATURNO_IDTURNO").toString();
		idguardia = formulario.getDatos().get("GUARDIATURNO_IDGUARDIA").toString();

		// Ejecutamos el PL para obtener los letrados a los que asignarles un
		// EJG
		contador = admGuardiasColegiado.ejecutarPLGuardias(idinstitucion, idturno, idguardia);
		// Si NO tengo letrados aviso del error
		if (Integer.parseInt(contador) == 0) {
			// Lanzo excepcion con error a tratar:
			// throw new SIGAException("Error en
			// DefinirCalendarioGuardiaAction.insertarCalendarioAutomaticamente()");
			// //Si tengo letrados borro y genero el calendario
		} else {
			try {
				// Buscamos los datos necesarios para los letrados obtenidos a
				// traves del contador.
				// Los insertamos en un Vector de Hash
				consulta = " SELECT " + GenClientesTemporalBean.C_IDPERSONA + " FROM " + GenClientesTemporalBean.T_NOMBRETABLA + " WHERE "
						+ GenClientesTemporalBean.C_IDINSTITUCION + " = " + idinstitucion + " AND " + GenClientesTemporalBean.C_CONTADOR + " = " + contador;
				letrados = admTemporal.selectGenerico(consulta);
				// Borramos con el contador de la tabla temporal
				hashTemporal.clear();
				hashTemporal.put(GenClientesTemporalBean.C_IDINSTITUCION, idinstitucion);
				hashTemporal.put(GenClientesTemporalBean.C_CONTADOR, contador);
				admTemporal.delete(hashTemporal);

				// Almacenamos el IDPERSONA del letrado en la sesión para
				// recuperarlo posteriormente
				request.getSession().setAttribute("datosEntrada", ((Hashtable) letrados.get(0)).get("IDPERSONA").toString());
			} catch (Exception e) {
				throwExcp("messages.general.error", e, null);
			}
		}
		return "busquedaAutomatica";
	}

	/**
	 * 
	 */
	protected String download(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions,
			SIGAException {
		DefinirEJGForm miForm = (DefinirEJGForm) formulario;
		request.setAttribute("nombreFichero", miForm.getFicheroDownload());
		request.setAttribute("rutaFichero", miForm.getRutaFicheroDownload());
		request.setAttribute("borrarFichero", miForm.getBorrarFicheroDownload());
		return "descargaFichero";
	}


	protected Vector sacarClavesEJG(Vector v) {
		Hashtable aux = new Hashtable();

		Vector claves = new Vector();
		for (int k = 0; k < v.size(); k++) {
			aux = null;
			Hashtable aux2 = new Hashtable();
			aux = (Hashtable) v.get(k);
			aux2.put(ScsEJGBean.C_IDINSTITUCION, aux.get(ScsEJGBean.C_IDINSTITUCION));
			aux2.put(ScsEJGBean.C_ANIO, aux.get(ScsEJGBean.C_ANIO));
			aux2.put(ScsEJGBean.C_NUMERO, aux.get(ScsEJGBean.C_NUMERO));
			aux2.put(ScsEJGBean.C_IDTIPOEJG, aux.get(ScsEJGBean.C_IDTIPOEJG));
			aux2.put(ScsEJGBean.C_FECHAMODIFICACION, aux.get(ScsEJGBean.C_FECHAMODIFICACION));
			aux2.put("SELECCIONADO", "1");
			claves.addElement(aux2);
		}
		return claves;
	}

	protected Vector actualizarSelecionados(String seleccionados, Vector claves, HttpServletRequest request) {
		int contador = 1;
		if (seleccionados.equals("")) {
			contador = 0;
		}
		String sTextoBuscado = ",";
		String seleccionadosAux = seleccionados;
		while (seleccionadosAux.indexOf(sTextoBuscado) > -1) {
			seleccionadosAux = seleccionadosAux.substring(seleccionadosAux.indexOf(sTextoBuscado) + sTextoBuscado.length(), seleccionadosAux.length());
			contador++;
		}
		String[] v_seleccionados = new String[contador];
		v_seleccionados = seleccionados.split(",");

		Vector v_seleccionadosSesion = new Vector();
		for (int z = 0; z < claves.size(); z++) {
			Hashtable h = new Hashtable();
			h = (Hashtable) claves.get(z);
			String anio = (String) h.get(ScsEJGBean.C_ANIO);
			String numero = (String) h.get(ScsEJGBean.C_NUMERO);
			String idtipoejg = (String) h.get(ScsEJGBean.C_IDTIPOEJG);
			
			boolean encontrado = false;
			int j = 0;
			while (!encontrado && j < contador) {
				String[] v_aux = v_seleccionados[j].split("[|]{2}");			
				String anio_aux = v_aux[0];
				String numero_aux = v_aux[1];
				String aux_idtipoejg = v_aux[2];

				if (anio.equals(anio_aux) && numero.equals(numero_aux) && idtipoejg.equals(aux_idtipoejg)) {
					encontrado = true;
					h.put("SELECCIONADO", "1");
				}
				j++;
			}
			if (!encontrado) {
				h.put("SELECCIONADO", "0");
			}
			v_seleccionadosSesion.add(h);

		}
		return v_seleccionadosSesion;
	}

	protected synchronized String aniadirARemesa(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
		DefinicionRemesas_CAJG_Form miForm = (DefinicionRemesas_CAJG_Form) formulario;
		String seleccionados = miForm.getSelDefinitivo();
		Vector claves = (Vector) request.getSession().getAttribute("EJG_SELECCIONADOS");
		Vector v_seleccionadosSesion = new Vector();
		if (seleccionados != null && !seleccionados.equals("")) {
			v_seleccionadosSesion = actualizarSelecionados(seleccionados, claves, request);
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			ScsEstadoEJGAdm estadoEJGAdm = new ScsEstadoEJGAdm(usr);
			CajgEJGRemesaAdm cajgEJGRemesaAdm = new CajgEJGRemesaAdm(usr);
			UserTransaction tx = null;
			try {
				tx = usr.getTransactionPesada();
				tx.begin();

				CajgRemesaAdm cajgRemesaAdm = new CajgRemesaAdm(usr);
				Hashtable hashRemesa = new Hashtable();
				hashRemesa.put("IDINSTITUCION", getIDInstitucion(request));
				hashRemesa.put("IDREMESA", miForm.getIdRemesa());
				Vector vectorRemesa = cajgRemesaAdm.select(hashRemesa);
				if (vectorRemesa == null || vectorRemesa.size() != 1) {
					throw new SIGAException("Remesa no encontrada");
				}
				CajgRemesaBean cajgRemesaBean = (CajgRemesaBean) vectorRemesa.get(0);
				String prefijo = cajgRemesaBean.getPrefijo();
				String sufijo = cajgRemesaBean.getSufijo();
				String numRemesa = (prefijo != null && !prefijo.trim().equals("")) ? (prefijo) : "";
				numRemesa += cajgRemesaBean.getNumero();
				numRemesa += (sufijo != null && !sufijo.trim().equals("")) ? (sufijo) : "";
				
				int numeroIntercambio = cajgEJGRemesaAdm.getNextNumeroIntercambio(getIDInstitucion(request));
				numeroIntercambio--;
				int nextIdEjgRemesa = cajgEJGRemesaAdm.getNextVal();
				nextIdEjgRemesa--;				
				
				String sqlInsertEJGRemesa = getInsertCajgRemesa(request, miForm.getIdRemesa(), numeroIntercambio, nextIdEjgRemesa);
				
				String sqlMaxIdEstadoPorEJG = "SELECT NVL(MAX(IDESTADOPOREJG), 0) + 1" +
						" FROM SCS_ESTADOEJG E" +
						" WHERE E.IDINSTITUCION = EJG.IDINSTITUCION" +
						" AND E.ANIO = EJG.ANIO" +
						" AND E.NUMERO = EJG.NUMERO" +
						" AND E.IDTIPOEJG = EJG.IDTIPOEJG";
				
				String sqlInsertEstadoEJG = new String("insert into scs_estadoejg (idinstitucion, idtipoejg, anio, numero, idestadoejg" +
						", fechainicio, fechamodificacion, usumodificacion, observaciones, idestadoporejg, automatico)" +
						" SELECT EJG.IDINSTITUCION, EJG.IDTIPOEJG, EJG.ANIO, EJG.NUMERO, '" + ClsConstants.GENERADO_EN_REMESA + "'" +
						", TRUNC(SYSDATE), SYSDATE, " + getUserBean(request).getUserName() + ", '" + numRemesa + "', (" + sqlMaxIdEstadoPorEJG + "), 1" +
						" FROM CAJG_EJGREMESA EJG" +
						" WHERE EJG.IDINSTITUCION = " + getIDInstitucion(request) +
						" AND EJG.IDREMESA = " + miForm.getIdRemesa() +
						" AND (1 = 0");

				StringBuffer sbsqlInsertEJGRemesa = new StringBuffer();
				StringBuffer sbsqlInsertEstadoEJG = new StringBuffer();
				int cuenta = 0;
				int numeroMaximoExpedientesInsert = 500;
				for (int i = 0; i < v_seleccionadosSesion.size(); i++) {
					Hashtable miHashaux = new Hashtable();
					miHashaux = (Hashtable) v_seleccionadosSesion.get(i);
					String seleccionado = (String) miHashaux.get("SELECCIONADO");
					if (seleccionado.equals("1")) {
						cuenta++;
						String sql = " OR (EJG.ANIO = " + miHashaux.get(ScsEJGBean.C_ANIO) +
								" AND EJG.NUMERO = " + miHashaux.get(ScsEJGBean.C_NUMERO) +
								" AND EJG.IDTIPOEJG = " + miHashaux.get(ScsEJGBean.C_IDTIPOEJG) + ")";
						
						sbsqlInsertEJGRemesa.append(sql);
						sbsqlInsertEstadoEJG.append(sql);
						
						if ((cuenta % numeroMaximoExpedientesInsert) == 0) {							
							cajgEJGRemesaAdm.insertSQL(sqlInsertEJGRemesa + sbsqlInsertEJGRemesa.toString() + ")");
							estadoEJGAdm.insertSQL(sqlInsertEstadoEJG + sbsqlInsertEstadoEJG.toString() + ")");
							sbsqlInsertEJGRemesa = new StringBuffer();
							sbsqlInsertEstadoEJG = new StringBuffer();	
							numeroIntercambio += numeroMaximoExpedientesInsert;
							nextIdEjgRemesa += numeroMaximoExpedientesInsert;
							sqlInsertEJGRemesa = getInsertCajgRemesa(request, miForm.getIdRemesa(), numeroIntercambio, nextIdEjgRemesa);
						}
					}
				}
				
				cajgEJGRemesaAdm.insertSQL(sqlInsertEJGRemesa + sbsqlInsertEJGRemesa.toString() + ")");
				estadoEJGAdm.insertSQL(sqlInsertEstadoEJG + sbsqlInsertEstadoEJG.toString() + ")");
				tx.commit();
				exitoRefresco("messages.inserted.success", request);
				Hashtable miHash = new Hashtable();
				miHash.put("IDREMESA", miForm.getIdRemesa());
				miHash.put("IDINSTITUCION", this.getIDInstitucion(request).toString());
				CajgRemesaAdm admRemesa = new CajgRemesaAdm(usr);
				Vector aux = admRemesa.select(miHash);
				CajgRemesaBean b = (CajgRemesaBean) aux.get(0);

				UtilidadesHash.set(miHash, "SUFIJO", b.getSufijo());
				UtilidadesHash.set(miHash, "NUMERO", String.valueOf(b.getNumero()));
				UtilidadesHash.set(miHash, "PREFIJO", b.getPrefijo());
				UtilidadesHash.set(miHash, "DESCRIPCION", b.getDescripcion());

				request.getSession().removeAttribute("DATAPAGINADOR");

				request.setAttribute("REMESA", miHash);

				return "editar";
			} catch (SIGAException e) {
				throw e;
			} catch (Exception e) {
				throwExcp("messages.general.error", new String[] { "modulo.gratuita" }, e, tx);
			}

		}
		return exito("messages.cajg.error.listos", request);
	}

	private String getInsertCajgRemesa(HttpServletRequest request, String idRemesa, int numeroIntercambio, int nextIdEjgRemesa) {
		return ("insert into cajg_ejgremesa" +
				" (idinstitucion, anio, numero, idtipoejg, idinstitucionremesa, idremesa, fechamodificacion" +
				" , usumodificacion, numerointercambio, idejgremesa)" +
				" SELECT EJG.IDINSTITUCION, EJG.ANIO, EJG.NUMERO, EJG.IDTIPOEJG, " + getIDInstitucion(request) +
						", " + idRemesa + ", SYSDATE, " + getUserBean(request).getUserName() +
					    ", " + numeroIntercambio + " + ROWNUM, " + nextIdEjgRemesa + " + ROWNUM" +
				" FROM SCS_EJG EJG" +
				" WHERE EJG.IDINSTITUCION = " + getIDInstitucion(request) + 
				" AND (1 = 0");
	}

	protected String consultaMaximaFechaModificacionEJG(Hashtable ht, ScsEstadoEJGAdm admBean) throws SIGAException {
		String fecha = "";
		int idinstitucion = Integer.parseInt((String) ht.get("IDINSTITUCION"));
		int idtipoejg = Integer.parseInt((String) ht.get("IDTIPOEJG"));
		int anio = Integer.parseInt((String) ht.get("ANIO"));
		int numero = Integer.parseInt((String) ht.get("NUMERO"));

		String sql = "SELECT MAX(FECHA) AS FECHAMAXIMA " + "FROM (select max(fechamodificacion) as FECHA " + "from scs_estadoejg " + "where idinstitucion = "
				+ idinstitucion + " " + "and idtipoejg = " + idtipoejg + " " + "and anio = " + anio + " " + "and numero =  " + numero + " " + "union "
				+ "select fechamodificacion as FECHA " + "from scs_ejg " + "where idinstitucion = " + idinstitucion + " " + "and idtipoejg = " + idtipoejg
				+ " " + "and anio = " + anio + " " + "and numero = " + numero + " " + "union " + "select max(fechamodificacion) as FECHA "
				+ "from scs_unidadfamiliarejg " + "where idinstitucion = " + idinstitucion + " " + "and idtipoejg = " + idtipoejg + " " + "and anio = " + anio
				+ " " + "and numero = " + numero + " " + "union " + "select max(fechamodificacion) as FECHA " + "from scs_documentacionejg "
				+ "where idinstitucion = " + idinstitucion + " " + "and idtipoejg = " + idtipoejg + " " + "and anio = " + anio + " " + "and numero = " + numero
				+ " " + "union " + "select max(fechamodificacion) as FECHA " + "from scs_contrariosejg " + "where idinstitucion = " + idinstitucion + " "
				+ "and idtipoejg = " + idtipoejg + " " + "and anio = " + anio + " " + "and numero = " + numero + " " + "union "
				+ "select max(fechamodificacion) as FECHA " + "from scs_delitosejg " + "where idinstitucion = " + idinstitucion + " " + "and idtipoejg = "
				+ idtipoejg + " " + "and anio = " + anio + " " + "and numero = " + numero + ")";
		try {

			RowsContainer rc = new RowsContainer();

			if (rc.find(sql)) {
				Row r = (Row) rc.get(0);
				fecha = r.getString("FECHAMAXIMA");
			}

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita" }, e, null);
		}

		return fecha;
	}

	protected String buscarPorEJG(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		Hashtable miHash = new Hashtable();
		String consulta = "";

		try {

			DefinicionRemesas_CAJG_Form miForm = (DefinicionRemesas_CAJG_Form) formulario;			
			miHash = miForm.getDatos();
			String idremesa = miForm.getIdRemesa();
			String idinstitucion = this.getIDInstitucion(request).toString();
			request.setAttribute("idremesa", idremesa);
			HashMap databackup = new HashMap();
			String idIncidenciasEnvio =  miForm.getIdIncidenciasEnvio();
			if (idIncidenciasEnvio != null && !idIncidenciasEnvio.equals("0")) {
				request.getSession().removeAttribute("DATAPAGINADOR");
			}

			if (request.getSession().getAttribute("DATAPAGINADOR") != null) {
				databackup = (HashMap) request.getSession().getAttribute("DATAPAGINADOR");
				Paginador paginador = (Paginador) databackup.get("paginador");
				Vector datos = new Vector();

				// Si no es la primera llamada, obtengo la página del request y
				// la busco con el paginador
				String pagina = request.getParameter("pagina");

				if (paginador != null) {
					if (pagina != null) {
						datos = paginador.obtenerPagina(Integer.parseInt(pagina));
					} else {// cuando hemos editado un registro de la busqueda y
							// volvemos a la paginacion
						datos = paginador.obtenerPagina((paginador.getPaginaActual()));
					}
				}

				databackup.put("paginador", paginador);
				databackup.put("datos", datos);

			} else {

				databackup = new HashMap();

				// obtengo datos de la consulta
				
				Vector datos = null;
				
				String cuentaErrores = "(SELECT COUNT(1) FROM CAJG_RESPUESTA_EJGREMESA ER WHERE ER.IDEJGREMESA = ejgremesa." +  CajgEJGRemesaBean.C_IDEJGREMESA + ")"; 

				String filtrado = "";
				if (idIncidenciasEnvio != null) {
					if (idIncidenciasEnvio.equals("1")) {//con errores
						filtrado = " and " + cuentaErrores + " > 0";	
					} else if (idIncidenciasEnvio.equals("2")) {//sin errores
						filtrado = " and " + cuentaErrores + " = 0";
					}
					 
				}
				
				/*
				 * Construimos la primera parte de la consulta, donde escogemos
				 * los campos a recuperar y las tablas necesarias
				 */
				consulta = "select ejgremesa.idejgremesa, ejg."
						+ ScsEJGBean.C_ANIO
						+ ", ejg."
						+ ScsEJGBean.C_IDINSTITUCION
						+ ", ejg."
						+ ScsEJGBean.C_IDTIPOEJG
						+ ", ejg."
						+ ScsEJGBean.C_IDFACTURACION
						+ ", ejg. "
						+ ScsEJGBean.C_FECHARATIFICACION
						+ ",  "
						+ UtilidadesMultidioma.getCampoMultidiomaSimple("tipoejg." + ScsTipoEJGBean.C_DESCRIPCION, this.getUserBean(request).getLanguage())
						+ " as TIPOEJG, ejg."
						+ ScsEJGBean.C_NUMEJG
						+ ", ejg."
						+ ScsEJGBean.C_FECHAAPERTURA
						+ ", f_siga_getnombreturno( ejg."
						+ ScsEJGBean.C_IDINSTITUCION
						+ ", ejg."
						+ ScsEJGBean.C_GUARDIATURNO_IDTURNO
						+ ") as TURNO, ejg."
						+ ScsEJGBean.C_GUARDIATURNO_IDTURNO
						+ ", guardia."
						+ ScsGuardiasTurnoBean.C_NOMBRE
						+ " as GUARDIA, f_siga_getunidadejg(ejg.idinstitucion,ejg.anio,ejg.numero,ejg.idtipoejg) AS NOMBRE, '' AS APELLIDO1,'' AS APELLIDO2,"
//						+ " (select "
//						+ UtilidadesMultidioma.getCampoMultidioma("maestroes." + ScsMaestroEstadosEJGBean.C_DESCRIPCION, this.getUserBean(request)
//								.getLanguage()) + " from " + ScsEstadoEJGBean.T_NOMBRETABLA + " estadoejg," + ScsMaestroEstadosEJGBean.T_NOMBRETABLA
//						+ " maestroes WHERE estadoejg." + ScsEstadoEJGBean.C_IDINSTITUCION + " = ejg." + ScsEJGBean.C_IDINSTITUCION + " and estadoejg."
//						+ ScsEstadoEJGBean.C_IDTIPOEJG + " = ejg." + ScsEJGBean.C_IDTIPOEJG + " and estadoejg." + ScsEstadoEJGBean.C_ANIO + " = ejg."
//						+ ScsEJGBean.C_ANIO + " and estadoejg." + ScsEstadoEJGBean.C_NUMERO + " = ejg." + ScsEJGBean.C_NUMERO + " and maestroes."
//						+ ScsMaestroEstadosEJGBean.C_IDESTADOEJG + " = estadoejg." + ScsEstadoEJGBean.C_IDESTADOEJG + " and (estadoejg.idestadoporejg) "
//						+ " = (SELECT max (ultimoestado.idestadoporejg) from " + ScsEstadoEJGBean.T_NOMBRETABLA + " ultimoestado where ultimoestado."
//						+ ScsEstadoEJGBean.C_IDINSTITUCION + " = estadoejg." + ScsEstadoEJGBean.C_IDINSTITUCION + " and ultimoestado."
//						+ ScsEstadoEJGBean.C_IDTIPOEJG + " = estadoejg." + ScsEstadoEJGBean.C_IDTIPOEJG + " and ultimoestado." + ScsEstadoEJGBean.C_ANIO
//						+ " = estadoejg." + ScsEstadoEJGBean.C_ANIO + " and ultimoestado." + ScsEstadoEJGBean.C_NUMERO + " = estadoejg."
//						+ ScsEstadoEJGBean.C_NUMERO + ") and rownum=1) as estado" +
						+ " F_SIGA_GETRECURSO(f_siga_get_ultimoestadoejg(ejg.idinstitucion,ejg.idtipoejg, ejg.anio, ejg.numero), " + this.getUserBean(request).getLanguage() + ") as estado"								
						+ ", ejg." + ScsEJGBean.C_NUMERO
						+ ", "  + cuentaErrores + " AS ERRORES"						
						+ " from " + ScsEJGBean.T_NOMBRETABLA + " ejg,"
						+ ScsGuardiasTurnoBean.T_NOMBRETABLA + " guardia,"
						+ ScsTipoEJGBean.T_NOMBRETABLA + " tipoejg,"
						+ CenColegiadoBean.T_NOMBRETABLA+ " colegiado, "
						+ CajgEJGRemesaBean.T_NOMBRETABLA + " ejgremesa";
						

				/* realizamos la join con de las tablas que necesitamos */
				consulta += " where ejg." + ScsEJGBean.C_IDTIPOEJG + " = tipoejg." + ScsTipoEJGBean.C_IDTIPOEJG + " and " + " ejg."
						+ ScsEJGBean.C_IDINSTITUCION + " = guardia." + ScsGuardiasTurnoBean.C_IDINSTITUCION + "(+) and " + " ejg."
						+ ScsEJGBean.C_GUARDIATURNO_IDTURNO + " = guardia." + ScsGuardiasTurnoBean.C_IDTURNO + "(+) and " + " ejg."
						+ ScsEJGBean.C_GUARDIATURNO_IDGUARDIA + " = guardia." + ScsGuardiasTurnoBean.C_IDGUARDIA + "(+) and " + " ejg."
						+ ScsEJGBean.C_IDINSTITUCION + " = colegiado." + CenColegiadoBean.C_IDINSTITUCION + "(+) and " + " ejg." + ScsEJGBean.C_IDPERSONA
						+ " = colegiado." + CenColegiadoBean.C_IDPERSONA + "(+)" + " and ejg.idinstitucion=ejgremesa.idinstitucion"
						+ " and ejg.anio=ejgremesa.anio" + " and ejg.numero=ejgremesa.numero" + " and ejg.idtipoejg=ejgremesa.idtipoejg"
						+ " and ejgremesa.idremesa=" + idremesa + " and ejgremesa.idinstitucion=" + idinstitucion + filtrado;

				// Y ahora concatenamos los criterios de búsqueda

				consulta += " ORDER BY " + ScsEJGBean.C_ANIO + ", to_number(" + ScsEJGBean.C_NUMEJG + ") desc";

				// v = admBean.selectGenerico(consulta);
				Paginador paginador = new Paginador(consulta);
				int totalRegistros = paginador.getNumeroTotalRegistros();

				if (totalRegistros == 0) {
					paginador = null;
				}

				databackup.put("paginador", paginador);
				if (paginador != null) {
					datos = paginador.obtenerPagina(1);
					databackup.put("datos", datos);
					request.getSession().setAttribute("DATAPAGINADOR", databackup);
				}

				// resultado = admBean.selectGenerico(consulta);
				// request.getSession().setAttribute("resultado",v);
			}
			// En "DATOSFORMULARIO" almacenamos el identificador del letrado
			miHash.put("BUSQUEDAREALIZADA", "1");
			request.getSession().setAttribute("DATOSFORMULARIO", miHash);

		} catch (Exception e) {
			throwExcp("messages.general.error", e, null);
		}

		return "listarEJG";
	}

	protected String aniadirExpedientes(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
		DefinicionRemesas_CAJG_Form miForm = (DefinicionRemesas_CAJG_Form) formulario;
		HttpSession session = request.getSession();
		session.removeAttribute("BUSQUEDAREALIZADA");
		session.removeAttribute("DATOSFORMULARIO");
		session.removeAttribute("DATAPAGINADOR");
		request.setAttribute("idremesa", miForm.getIdRemesa());
		return "aniadirBusqueda";
	}

	protected String buscarListos(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		ScsEJGAdm admBean;
		Vector v = new Vector();
		Hashtable miHash = new Hashtable();

		try {

			DefinicionRemesas_CAJG_Form miForm = (DefinicionRemesas_CAJG_Form) formulario;
			admBean = new ScsEJGAdm(this.getUserBean(request));
			miHash = miForm.getDatos();

			HashMap databackup = new HashMap();
			Vector claves = new Vector();
			String seleccionados = request.getParameter("Seleccion");

			claves = (Vector) request.getSession().getAttribute("EJG_SELECCIONADOS");
			Vector v_seleccionadosSesion = new Vector();
			if (seleccionados != null) {
				v_seleccionadosSesion = actualizarSelecionados(seleccionados, claves, request);
				if (v_seleccionadosSesion != null) {
					request.getSession().setAttribute("EJG_SELECCIONADOS", v_seleccionadosSesion);
				}
			}

			if (request.getSession().getAttribute("DATAPAGINADOR") != null) {
				databackup = (HashMap) request.getSession().getAttribute("DATAPAGINADOR");
				PaginadorBind paginador = (PaginadorBind)databackup.get("paginador");
				Vector datos = new Vector();

				// Si no es la primera llamada, obtengo la página del request y
				// la busco con el paginador
				String pagina = request.getParameter("pagina");

				if (paginador != null) {
					if (pagina != null) {
						datos = paginador.obtenerPagina(Integer.parseInt(pagina));
					} else {// cuando hemos editado un registro de la busqueda y
							// volvemos a la paginacion
						datos = paginador.obtenerPagina((paginador.getPaginaActual()));
					}
				}

				databackup.put("paginador", paginador);
				databackup.put("datos", datos);

			} else {

				databackup = new HashMap();

				// obtengo datos de la consulta
				
				Vector datos = null;
				Hashtable htConsultaBind  = admBean.getBindBusquedaMantenimientoEJG(miHash,  miForm, ScsEJGAdm.TipoVentana.BUSQUEDA_ANIADIR_REMESA);
				v = admBean.getBusquedaMantenimientoEJG(htConsultaBind);

				// Rellena un vector de Hastable con la claves primarias de la
				// tabla scs_ejg para llevar el control de los check
				claves = sacarClavesEJG(v);
				PaginadorBind paginador = admBean.getPaginadorBusquedaMantenimientoEJG(htConsultaBind);
				int totalRegistros = paginador.getNumeroTotalRegistros();

				if (totalRegistros == 0) {
					paginador = null;
				}

				databackup.put("paginador", paginador);
				if (paginador != null) {
					datos = paginador.obtenerPagina(1);
					databackup.put("datos", datos);
					request.getSession().setAttribute("DATAPAGINADOR", databackup);
					request.getSession().setAttribute("HORABUSQUEDA", UtilidadesBDAdm.getFechaCompletaBD("es"));
				}

				// resultado = admBean.selectGenerico(consulta);
				// request.getSession().setAttribute("resultado",v);
				if (claves != null) {
					request.getSession().setAttribute("EJG_SELECCIONADOS", claves);
				}
			}
			// En "DATOSFORMULARIO" almacenamos el identificador del letrado
			miHash.put("BUSQUEDAREALIZADA", "1");
			request.getSession().setAttribute("DATOSFORMULARIO", miHash);

			request.setAttribute("idremesa", miForm.getIdRemesa());
		} catch (Exception e) {
			throwExcp("messages.general.error", e, null);
		}
		return "listarEJGListos";
	}

	private String marcarEnviada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions,
			Exception {

		HttpSession ses = request.getSession();
		UsrBean usr = (UsrBean) ses.getAttribute("USRBEAN");
		DefinicionRemesas_CAJG_Form miForm = (DefinicionRemesas_CAJG_Form) formulario;

		UserTransaction tx = usr.getTransaction();
		tx.begin();
		
		CajgRemesaEstadosAdm cajgRemesaEstadosAdm = new CajgRemesaEstadosAdm(usr);
		CajgEJGRemesaAdm cajgEJGRemesaAdm = new CajgEJGRemesaAdm(usr);

		if (cajgRemesaEstadosAdm.nuevoEstadoRemesa(usr, getIDInstitucion(request), Integer.valueOf(miForm.getIdRemesa()), Integer.valueOf("2"))) {
			cajgEJGRemesaAdm.nuevoEstadoEJGRemitidoComision(usr, getIDInstitucion(request).toString(), miForm.getIdRemesa(), ClsConstants.REMITIDO_COMISION);
		}

		tx.commit();

		request.getSession().removeAttribute("DATAPAGINADOR");

		request.setAttribute("descargarFichero", Boolean.TRUE);

		return exitoRefresco(null, request);
	}

	private String descargar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions,
			SIGAException {
			
		DefinicionRemesas_CAJG_Form miForm = (DefinicionRemesas_CAJG_Form) formulario;
		File file = getFichero(getIDInstitucion(request).toString(), miForm.getIdRemesa());
		return descargaFichero(formulario, request, file);
	}
	
	private String descargaFichero(MasterForm formulario, HttpServletRequest request, File file) throws SIGAException {
		
		try {

			if (file == null) {
				throw new SIGAException("messages.general.error.ficheroNoExiste");
			}
			request.setAttribute("nombreFichero", file.getName());
			request.setAttribute("rutaFichero", file.getAbsolutePath());

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita" }, e, null);
		}

		return "descargaFichero";
	}
	

	/**
	 * 
	 * @param idInstitucion
	 * @param idRemesa
	 * @param nombreFicheroPorDefecto
	 * @param mensaje
	 * @param rutaFicheroZIP
	 * @return
	 * @throws Exception
	 */
	public File generaFicherosTXT (String idInstitucion, String idRemesa, String nombreFicheroPorDefecto, StringBuffer mensaje, String rutaFicheroZIP) throws Exception {
		
		String keyPath = "cajg.directorioFisicoCAJG";
		String keyPath2 = "cajg.directorioCAJGJava";
				
	    ReadProperties p= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String pathFichero = p.returnProperty(keyPath) + p.returnProperty(keyPath2);
		
		File fileZIP = null;
		
		ArrayList<File> ficheros = new ArrayList<File>();		
		
		//quitamos los posibles simbolos que pueda tener el prefijo y sufijo
		nombreFicheroPorDefecto = reemplazaCaracteres(nombreFicheroPorDefecto);
						
		String sRutaJava = pathFichero + ClsConstants.FILE_SEP + idInstitucion  + ClsConstants.FILE_SEP + idRemesa;
		
		try {
			String[] campos = null;
			Row fila = null;
			String funcion, cabecera, delimitador, saltoLinea, subCabecera = null;
			String fila_consulta;
			
			
			Boolean imprimirCabecera = Boolean.FALSE;
			
			String sql1 = "SELECT COUNT(1) NUMEROEJGS" +
				" FROM " + CajgEJGRemesaBean.T_NOMBRETABLA +
				" WHERE " + CajgEJGRemesaBean.C_IDINSTITUCION + " = " + idInstitucion +
				" AND " + CajgEJGRemesaBean.C_IDREMESA + " = " + idRemesa;
	
			RowsContainer rc1 = new RowsContainer();
			String numeroEJGs = null;
			
			if (rc1.find(sql1)) {
				Row row = (Row) rc1.get(0);
				numeroEJGs = row.getString("NUMEROEJGS");
			}

			String sql = "SELECT *" +
					" FROM " + CajgProcedimientoRemesaBean.T_NOMBRETABLA + 
					" WHERE " + CajgProcedimientoRemesaBean.C_IDINSTITUCION + " = " + idInstitucion;
			RowsContainer rc = new RowsContainer();
			if (!rc.find(sql)) {
				ClsLogging.writeFileLog("No hay ningún registro en " + CajgProcedimientoRemesaBean.T_NOMBRETABLA + " para la institucion " + idInstitucion, 3);				
				throw new SIGAException("messages.cajg.funcionNoDefinida");
			}
			
			

			for (int j = 0; j < rc.size(); j++) {				
				fila = (Row) rc.get(j);
				funcion = fila.getString(CajgProcedimientoRemesaBean.C_CONSULTA);
				cabecera = (String) fila.getValue(CajgProcedimientoRemesaBean.C_CABECERA);
				delimitador = fila.getString(CajgProcedimientoRemesaBean.C_DELIMITADOR);
				saltoLinea = fila.getString(CajgProcedimientoRemesaBean.C_SALTOLINEA);
				subCabecera = fila.getString(CajgProcedimientoRemesaBean.C_SUBCABECERA);
				String nombreFichero = fila.getString(CajgProcedimientoRemesaBean.C_NOMBREFICHERO);
				if (nombreFichero == null || nombreFichero.trim().equals("")) {
					nombreFichero = nombreFicheroPorDefecto;
				} else {
					nombreFichero = trataNombreFichero(nombreFichero);
				}
				
				if (cabecera != null && cabecera.trim().equals("1")) {
					imprimirCabecera = Boolean.TRUE;
				}
				
				try {// Si la funcion no existe en la base de datos
					boolean usaTablaEJGremesa = false;
					RowsContainer rc2 = new RowsContainer();					
					
					Connection con = null;
					try {
						con=ClsMngBBDD.getReadConnection();
						CallableStatement cs=con.prepareCall("{? = call " + funcion + " (?,?)}");
						cs.registerOutParameter(1,Types.CLOB);
						
						cs.setString(2, idInstitucion);
						cs.setString(3, idRemesa);					    	
				    	cs.execute();
				    	fila_consulta = cs.getString(1);
					} catch (SQLException e) {					
						ClsLogging.writeFileLogError("Error al ejecutar la función o procedimiento " + funcion, e, 3);
						throw e;
				    } finally {
				    	if (con != null) {
				    		ClsMngBBDD.closeConnection(con);
				    	}
					}			    
					
					if (rc2.find(fila_consulta)) {
						usaTablaEJGremesa = fila_consulta.toUpperCase().indexOf(CajgEJGRemesaBean.T_NOMBRETABLA) > -1;
						campos = rc2.getFieldNames();
						
					} else {
						ClsLogging.writeFileLog("La query de la funcion " + funcion + " no ha devuelto datos", 3);
					}
					
					
					if (rc2 != null && rc2.size() > 0) {						
						// creación fichero						
						File file = generaFichero(rc2, nombreFichero, sRutaJava, campos, "txt", imprimirCabecera, delimitador, saltoLinea, subCabecera, (usaTablaEJGremesa?numeroEJGs:null), mensaje); 
						ficheros.add(file);
					}					
				} catch (Exception e) {
					throw new SIGAException("messages.cajg.error.descargaFichero");
				}			
				
			}

			if (ficheros != null && !ficheros.isEmpty()) {	
				
				if (ficheros.size() > 0) {	// si tiene mas de un fichero hacemos un zip	
					String pathdocumento = sRutaJava + ClsConstants.FILE_SEP + nombreFicheroPorDefecto;
					if (rutaFicheroZIP != null) {
						pathdocumento = rutaFicheroZIP + ClsConstants.FILE_SEP + nombreFicheroPorDefecto;
						File file = new File(rutaFicheroZIP);
						file.mkdirs();
					}					
									
					fileZIP = MasterWords.doZip(ficheros, pathdocumento);					
				}
				
			} else {
				// no devuelve ningun resultado la consulta
				if (numeroEJGs != null && Integer.parseInt(numeroEJGs) > 0) {
					mensaje.delete(0, mensaje.length());
					mensaje.append("cajg.error.noFicheroGenerado");
				}
				//throw new SIGAException("messages.cajg.error.nodatos");
			}
			

		} catch (SIGAException e) {
			throw e;
		} catch (ClsExceptions e) {
			throwExcp("messages.cajg.error.nodatos", new String[] { "modulo.gratuita" }, e, null);
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita" }, e, null);
		}
		
		return fileZIP;
	}

	/**
	 * 
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	protected String generarFichero(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions, SIGAException {
		
		request.getSession().removeAttribute("DATAPAGINADOR");

		UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
		String idInstitucion = this.getIDInstitucion(request).toString();	
		DefinicionRemesas_CAJG_Form form = (DefinicionRemesas_CAJG_Form) formulario;		
		String nombreFicheroPorDefecto = form.getPrefijo() + form.getNumero() + form.getSufijo() + "_" + idInstitucion;
		
		StringBuffer mensaje = new StringBuffer();
		String mensajeCorrecto = UtilidadesString.getMensajeIdioma(usr, "cajg.mensaje.ficheroCorrecto");
		mensaje.append(mensajeCorrecto);
		
		try {		
			eliminaFicheroTXTGenerado(idInstitucion, form.getIdRemesa());//por si se estan regenerando...
			File fileZIP = generaFicherosTXT(idInstitucion, form.getIdRemesa(), nombreFicheroPorDefecto, mensaje, null);
				
			if (fileZIP != null) {
				UserTransaction tx = usr.getTransaction();
				tx.begin();
				// Marcar como generada
				CajgRemesaEstadosAdm cajgRemesaEstadosAdm = new CajgRemesaEstadosAdm(usr);				
				cajgRemesaEstadosAdm.nuevoEstadoRemesa(usr, getIDInstitucion(request), Integer.valueOf(form.getIdRemesa()), Integer.valueOf("1"));
				tx.commit();
			} else {
				mensaje = new StringBuffer("cajg.mensaje.ficheroNoGenerado");				
			}
			
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita" }, e, null);
		}

		return exitoRefresco(mensaje.toString(), request);
	}

	/**
	 * 
	 * @param nombreFichero
	 * @return
	 */
	private String trataNombreFichero(String nombreFichero) {
		String nombre = nombreFichero;
		try {
			if (nombre != null) {
				int iniDate = -1;				
				while ((iniDate = nombre.indexOf("$DATE")) > -1) { //para plantilla $DATE(ddMMyyyy)$
					int finDate = nombreFichero.indexOf("$", iniDate+1) + 1;
					String sub = nombre.substring(iniDate, finDate);										
					String formato  = sub.substring(sub.indexOf("(")+1, sub.indexOf(")"));
					SimpleDateFormat sdf = new SimpleDateFormat(formato);
					String fechaSt = sdf.format(new Date());
					nombre = nombre.substring(0, iniDate) + fechaSt + nombre.substring(finDate);
				}
			}
		} catch (Exception e) {
			//si ocurre cualquier excepcion devolvemos el dato inicial y para que se revise
			nombre = nombreFichero;
		}
		return nombre;
	}


	/**
	 * 
	 * @param formulario
	 * @param request
	 * @throws Exception
	 */
	private void ejecutaBackground(MasterForm formulario, HttpServletRequest request, int indexClass) throws ClsExceptions {
		DefinicionRemesas_CAJG_Form form = (DefinicionRemesas_CAJG_Form) formulario;
		Integer idInstitucion = getIDInstitucion(request);
		UsrBean usrBean = getUserBean(request);
					
		String idRemesa = form.getIdRemesa();		

		SIGAWSClientAbstract sigaWSClient = CajgConfiguracion.getSIGAWSClientAbstract(idInstitucion, indexClass);
				
		if (sigaWSClient == null) {
			throw new ClsExceptions("El colegio no tiene implementado el WebService");
		}
		sigaWSClient.setIdInstitucion(idInstitucion);
		sigaWSClient.setUsrBean(usrBean);
		sigaWSClient.setIdRemesa(Integer.parseInt(idRemesa));
		
		GenParametrosAdm admParametros = new GenParametrosAdm(usrBean);		
		String urlWS = admParametros.getValor(idInstitucion.toString(), "SCS", "PCAJG_WS_URL", "");
		String generaTXT = admParametros.getValor(idInstitucion.toString(), "SCS", "PCAJG_GENERA_TXT_ANTIGUO", "");
		String firmarXML = admParametros.getValor(idInstitucion.toString(), "SCS", "PCAJG_FIRMAR_XML", "");
		
		sigaWSClient.setUrlWS(urlWS);
		sigaWSClient.setGeneraTXT(generaTXT.trim().equals("1"));
		sigaWSClient.setFirmarXML(firmarXML.trim().equals("1"));
		
				
		SIGAWSListener sigaWSListener = new SIGAWSListener();
		Timer timer = new Timer();
		timer.addNotificationListener(sigaWSListener, null, timer);
		Integer idNotificacion = timer.addNotification("WSType", "WSMessage", sigaWSClient, new Date(), 0);
		sigaWSListener.setIdNotificacion(idNotificacion);		
		
		timer.start();
		
	} 
	
	/**
	 * 
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	private String envioWS(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ejecutaBackground(formulario, request, 0);		
		return exitoRefresco("messages.cajg.enviandoWS", request);
	}
	
	
	/**
	 * 
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	private String envioFTP(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws Exception {				
		ejecutaBackground(formulario, request, 0);		
		return exitoRefresco("messages.cajg.envioFTP.correcto", request);
	}


	/**
	 * Funcion que reemplaza los caracteres extraños no permitidos como nombre de un fichero
	 * 
	 * @param nombre
	 * @return
	 */
	private String reemplazaCaracteres(String nombre) {
		if (nombre != null) {
			nombre = nombre.replaceAll("/", "_");
			nombre = nombre.replaceAll("\\\\", "_");
			nombre = nombre.replaceAll(":", "_");
			nombre = nombre.replaceAll("\\*", "_");
			nombre = nombre.replaceAll("\\?", "_");
			nombre = nombre.replaceAll("\"", "_");
			nombre = nombre.replaceAll("<", "_");
			nombre = nombre.replaceAll(">", "_");
			nombre = nombre.replaceAll("\\|", "_");
		}
		return nombre;
	}

	/**
	 * 
	 * @param datos
	 * @param nombreFichero
	 * @param rutaFichero
	 * @param cabeceras
	 * @param extension
	 * @param imprimirCabecera
	 * @param delimitador
	 * @param saltoLinea
	 * @param subCabecera
	 * @param mensaje 
	 * @param numeroEJGs 
	 * @return
	 * @throws IOException
	 */
	private File generaFichero(RowsContainer rowsContainer, String nombreFichero, String rutaFichero, String[] cabeceras, String extension, Boolean imprimirCabecera, String delimitador, String saltoLinea, String subCabecera, String numeroEJGs, StringBuffer mensaje)
			throws IOException {
		
		if (imprimirCabecera == null) {
			imprimirCabecera = Boolean.FALSE;
		}

		File directorio = new File(rutaFichero);
		directorio.mkdirs();

		File archivo = new File(rutaFichero + ClsConstants.FILE_SEP + nombreFichero + "." + extension);
		
		int i = 2;
		while (archivo.exists()) {
			archivo = new File(rutaFichero + ClsConstants.FILE_SEP + nombreFichero + " ("+i+")." + extension);
			i++;
		}

		OutputStream out = new FileOutputStream(archivo);
		String valor;
		String arraySubCabeceras = new String();
		boolean inicioLinea = true;
		int numeroDatos = 0;

		for (i = 0; i < rowsContainer.size(); i++) {
			String linea = "";
			String cabecera = "";
			inicioLinea = true;
			numeroDatos++;

			Row row = (Row) rowsContainer.get(i);
			if (imprimirCabecera.booleanValue()) {
				for (int j = 0; j < cabeceras.length; j++) {					
					if (j == 0) {
						cabecera += cabeceras[j];						
					} else {
						cabecera += delimitador + cabeceras[j];
					}
				}
				cabecera = cabecera.toLowerCase() + "\r\n";
				out.write(cabecera.getBytes());
				imprimirCabecera = Boolean.FALSE;
			}

			boolean mismoExpediente = true;
			
			for (int k = 0; k < cabeceras.length; k++) {
				valor = row.getString(cabeceras[k]);
				if (valor != null) {
					valor = valor.replaceAll("\\r\\n", " "); // sustituimos los posibles saltos de linea por un espacio
				}
				if (subCabecera != null && !subCabecera.trim().equals("") && subCabecera.trim().equals(valor)) {					
					if (arraySubCabeceras.indexOf(linea) == -1) {
						arraySubCabeceras = arraySubCabeceras + linea;
						linea = linea + "\r\n";
					} else {
						linea = "";
						if (mismoExpediente) {
							numeroDatos--;
							mismoExpediente = false;
						}
					}	
					inicioLinea = true;
				} else if (saltoLinea != null && !saltoLinea.trim().equals("") && saltoLinea.trim().equals(valor)) {
					linea = linea + "\r\n";
					inicioLinea = true;
				} else {
					if (inicioLinea) {
						linea += valor;
						inicioLinea = false;
					} else {
						linea += delimitador + valor;
					}
				}
			}

			if (i+1 < rowsContainer.size()) {
				linea = linea + "\r\n";
			}
			out.write(linea.getBytes());

		}
		out.flush();
		out.close();
		
		if (numeroEJGs != null) {
//			if (!numeroEJGs.trim().equals(String.valueOf(numeroDatos))) {				
			mensaje.append("\n - " + nombreFichero + "." + extension + ": " + numeroDatos + " registros.");				
//			}
		}
		

		return archivo;
	}

	/**
	 * 
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @param log
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	
	private String descargarLog(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {

		DefinicionRemesas_CAJG_Form miForm = (DefinicionRemesas_CAJG_Form) formulario;
		File file = SIGAWSClientAbstract.getErrorFile(getIDInstitucion(request), Integer.parseInt(miForm.getIdRemesa()));

		if (file == null) {
			throw new SIGAException("messages.general.error.ficheroNoExiste");
		}

		request.setAttribute("nombreFichero", file.getName());
		request.setAttribute("rutaFichero", file.getAbsolutePath());

		return "descargaFichero";
	}

}