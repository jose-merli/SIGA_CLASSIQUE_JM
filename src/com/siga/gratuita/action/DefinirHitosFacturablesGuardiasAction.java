package com.siga.gratuita.action;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.ScsGuardiasTurnoAdm;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.beans.ScsHitoFacturableGuardiaAdm;
import com.siga.beans.ScsHitoFacturableGuardiaBean;
import com.siga.beans.ScsTipoActuacionBean;
import com.siga.beans.ScsTipoAsistenciaColegioAdm;
import com.siga.beans.ScsTipoAsistenciaColegioBean;
import com.siga.beans.ScsTurnoAdm;
import com.siga.beans.ScsTurnoBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefinirHitosFacturablesGuardiasForm;

/**
 * @author ruben.fernandez
 * @version david.sanchezp - 30-06-2005: controlar SIGA Exception y refrescar
 *          con metodo exito
 * @version adrian.ayala - 15-07-2008: Limpieza general
 */
public class DefinirHitosFacturablesGuardiasAction extends MasterAction {
	protected ActionForward executeInternal(ActionMapping mapping,
			ActionForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {
		MasterForm miform = (MasterForm) formulario;
		String accion = (miform == null ? "abrir" : miform.getModo());
		String result = "exception";

		try {
			if (accion != null && accion.equals("consultar"))
				result = consultar(mapping, miform, (String) request
						.getParameter("tipoConsulta"), request, response);
			else
				return super.executeInternal(mapping, formulario, request,
						response);

			if (result == null)
				throw new ClsExceptions("El ActionMapping no puede ser nulo");

			return mapping.findForward(result);
		} catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error", e,
					new String[] { "modulo.gratuita" });
		}
	} // executeInternal ()

	/**
	 * Funcion que atiende la accion abrir. Por defecto se abre el forward
	 * 'inicio'
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
	protected String consultar(ActionMapping mapping, MasterForm formulario,
			String tipo, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {
		try {
			UsrBean usr = (UsrBean) request.getSession()
					.getAttribute("USRBEAN");
			ScsTipoAsistenciaColegioAdm tipoAsist = new ScsTipoAsistenciaColegioAdm(
					this.getUserBean(request));
			String consulta = "";

			if (tipo.equals("asistencia")) {
				consulta = "select "
						+ UtilidadesMultidioma.getCampoMultidiomaSimple("h."
								+ ScsTipoAsistenciaColegioBean.C_DESCRIPCION,
								usr.getLanguage()) + " TIPOASISTENCIA, "
						+ "       decode("
						+ (String) request.getParameter("importeMax") + ", "
						+ "              0, " + "              h."
						+ ScsTipoAsistenciaColegioBean.C_IMPORTE + ", "
						+ "              h."
						+ ScsTipoAsistenciaColegioBean.C_IMPORTEMAXIMO
						+ ") IMPORTE" + "  from "
						+ ScsTipoAsistenciaColegioBean.T_NOMBRETABLA + " h "
						+ " where h."
						+ ScsTipoAsistenciaColegioBean.C_IDINSTITUCION + " = "
						+ usr.getLocation();
				tipo = "asistencia";
			} else {
				consulta = "select "
						+ UtilidadesMultidioma.getCampoMultidiomaSimple("a."
								+ ScsTipoActuacionBean.C_DESCRIPCION, this
								.getUserBean(request).getLanguage())
						+ " TIPOACTUACION, "
						+ "       decode("
						+ (String) request.getParameter("importeMax")
						+ ", "
						+ "              0, "
						+ "              a."
						+ ScsTipoActuacionBean.C_IMPORTE
						+ ", "
						+ "              a."
						+ ScsTipoActuacionBean.C_IMPORTEMAXIMO
						+ ") IMPORTE, "
						+ "       "
						+ UtilidadesMultidioma.getCampoMultidiomaSimple("b."
								+ ScsTipoAsistenciaColegioBean.C_DESCRIPCION,
								this.getUserBean(request).getLanguage())
						+ " TIPOASISTENCIA "
						+ "  from "
						+ ScsTipoActuacionBean.T_NOMBRETABLA
						+ " a, "
						+ "       "
						+ ScsTipoAsistenciaColegioBean.T_NOMBRETABLA
						+ " b "
						+ " where a."
						+ ScsTipoActuacionBean.C_IDINSTITUCION
						+ " = "
						+ usr.getLocation()
						+ "   and a."
						+ ScsTipoActuacionBean.C_IDINSTITUCION
						+ " = "
						+ "       b."
						+ ScsTipoAsistenciaColegioBean.C_IDINSTITUCION
						+ "   and a."
						+ ScsTipoActuacionBean.C_IDTIPOASISTENCIA
						+ " = "
						+ "       b."
						+ ScsTipoAsistenciaColegioBean.C_IDTIPOASISTENCIACOLEGIO;
				tipo = "actuacion";
			}
			Vector vResult = (Vector) tipoAsist.ejecutaSelect(consulta);
			request.setAttribute("resultado", vResult);
			request.setAttribute("tipo", tipo);
		} catch (Exception e) {
			throw new SIGAException("messages.general.error", e,
					new String[] { "modulo.gratuita" });
		}
		return "consultar";
	} // consultar ()

	/**
	 * Funcion que atiende la accion abrir. Genera el formulario a partir de los
	 * registros de BD para rellenar JSP
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
		// Controles generales
		UsrBean usr = null;
		ScsHitoFacturableGuardiaAdm hFact = null;

		// Variables
		String c_idhito = ScsHitoFacturableGuardiaBean.C_IDHITO;

		try {
			// Controles generales
			usr = this.getUserBean(request);
			hFact = new ScsHitoFacturableGuardiaAdm(null);

			// recuperando la guardia que está seleccionada desde la sesion
			ScsGuardiasTurnoBean guardia = (ScsGuardiasTurnoBean) request
					.getSession().getAttribute("DATABACKUPPESTANA");
			Hashtable hash1 = (Hashtable) guardia.getOriginalHash();
			request.getSession().setAttribute("ORIGINALHASH", hash1);

			String consulta = "select "
					+ UtilidadesMultidioma.getCampoMultidioma("h.descripcion",
							this.getUserBean(request).getLanguage()) + ", "
					+ "       hg.preciohito, "
					+ "       hg.diasaplicables, " + "       hg.agrupar, "
					+ "       hg.idinstitucion IDINSTITUCION, "
					+ "       hg.idturno IDTURNO, "
					+ "       hg.idguardia IDGUARDIA, "
					+ "       hg.idhito IDHITO, "
					+ "       hg.pagoofacturacion PAGOFACTURACION"
					+ "  from scs_hitofacturable h, "
					+ "       scs_hitofacturableguardia hg"
					+ " where h.idhito = hg.idhito"
					+ "   and hg.idinstitucion = " + usr.getLocation()
					+ "   and hg.idturno =" + (String) hash1.get("IDTURNO")
					+ "   and hg.idguardia =" + (String) hash1.get("IDGUARDIA")
					+ "   and hg.pagoofacturacion = 'F'";

			Vector vHitos = (Vector) hFact.ejecutaSelect(consulta);

			// Si no hay una configuracion de guardias se muestra un mensaje
			if (vHitos.size() == 0) {
				request.setAttribute("EXISTENHITOS", "0");
				// y se obliga a tener un hito
				Hashtable hash = new Hashtable();
				hash.put(c_idhito, "2");
				hash.put(ScsHitoFacturableGuardiaBean.C_PRECIOHITO, "0");
				hash.put(ScsHitoFacturableGuardiaBean.C_DIASAPLICABLES,
						"LMXJVSD");
				hash.put(ScsHitoFacturableGuardiaBean.C_AGRUPAR, "0");
				vHitos.add(hash);
			} else
				request.setAttribute("EXISTENHITOS", "1");

			// obteniendo los hitos del vector a un hash y mas
			Hashtable hashResul = new Hashtable();
			Hashtable hashElem;
			String listaPagaGuardias = "", listaNoPagaGuardias = "";
			boolean agruparPagaGuardias = false, agruparNoPagaGuardias = false;
			boolean hayHito45 = false, hayHito46 = false;
			for (int cont = 0; cont < vHitos.size(); cont++) {
				// obteniendo hash de cada hito
				hashElem = (Hashtable) vHitos.get(cont);

				// obteniendo valores especiales: DiasAplicables y Agrupar
				if (hashElem.get(c_idhito).equals("2")
						|| hashElem.get(c_idhito).equals("4")) {
					listaPagaGuardias = (String) hashElem
							.get(ScsHitoFacturableGuardiaBean.C_DIASAPLICABLES);
					agruparPagaGuardias = ((String) hashElem
							.get(ScsHitoFacturableGuardiaBean.C_AGRUPAR))
							.equals("1");
				}
				if (hashElem.get(c_idhito).equals("5")
						|| hashElem.get(c_idhito).equals("7")) {
					listaNoPagaGuardias = (String) hashElem
							.get(ScsHitoFacturableGuardiaBean.C_DIASAPLICABLES);
					agruparNoPagaGuardias = ((String) hashElem
							.get(ScsHitoFacturableGuardiaBean.C_AGRUPAR))
							.equals("1");
				}

				// Obteniendo Maximo para Duplicar Asistencias y Actuaciones
				hayHito45 = hayHito45 || (hashElem.get(c_idhito).equals("45"));
				hayHito46 = hayHito46 || (hashElem.get(c_idhito).equals("46"));

				hashResul.put(hashElem.get(c_idhito), hashElem
						.get(ScsHitoFacturableGuardiaBean.C_PRECIOHITO));
			}

			// definiendo el formulario
			DefinirHitosFacturablesGuardiasForm miForm = (DefinirHitosFacturablesGuardiasForm) formulario;

			miForm.setRadioA("0");
			miForm.setCheckB1(false);
			miForm.setCheckB2(false);
			if (hashResul.containsKey("2")) {
				miForm.setRadioA("0");
				miForm.setCheckB1(true);
			}
			if (hashResul.containsKey("4")) {
				miForm.setRadioA("1");
				miForm.setCheckB1(true);
			}
			if (hashResul.containsKey("5")) {
				miForm.setRadioA("0");
				miForm.setCheckB2(true);
			}
			if (hashResul.containsKey("7")) {
				miForm.setRadioA("1");
				miForm.setCheckB2(true);
			}

			this.rellenaSeleccionDiasFormulario(miForm, listaPagaGuardias,
					listaNoPagaGuardias);
			miForm.setChPagaGuardiaPorDia(!agruparPagaGuardias);
			miForm.setChNoPagaGuardiaPorDia(!agruparNoPagaGuardias);

			miForm.setChAsist(hashResul.containsKey("3"));
			miForm.setChMinAsist(hashResul.containsKey("10"));
			miForm.setChActuacion(hashResul.containsKey("8"));
			miForm.setChMinActuacion(hashResul.containsKey("19"));
			miForm.setCheckC(hashResul.containsKey("9"));
			miForm.setChActFG(hashResul.containsKey("6"));
			miForm.setChGuardias((hashResul.containsKey("20") || hashResul
					.containsKey("22")));
			miForm.setChNoGuardias((hashResul.containsKey("20") || hashResul
					.containsKey("25")));

			// obteniendo valores de parametros generales para los hitos de
			// maximo para duplicar de asistencias (45) y actuciones (46)
			String numAsist = "", numAct = "";
			GenParametrosAdm paramAdm = new GenParametrosAdm(this
					.getUserBean(request));
			try {
				numAsist = paramAdm.getValor(usr.getLocation(), "FCS",
						"NUM_ASISTENCIAS", "");
				numAct = paramAdm.getValor(usr.getLocation(), "FCS",
						"NUM_ACTUACIONES", "");
			} catch (Exception e) {
			}
			if (!hayHito45)
				hashResul.put("45", numAsist);
			if (!hayHito46)
				hashResul.put("46", numAct);

			// introduciendo los hitos en el formulario que se mostrara en JSP
			miForm.setHitoPrecioHt(hashResul);

			// obteniendo y pasando por sesion los nombres de turno y guardia
			Hashtable hashTurno = new Hashtable();
			hashTurno.put(ScsTurnoBean.C_IDINSTITUCION, usr.getLocation());
			hashTurno
					.put(ScsTurnoBean.C_IDTURNO, (String) hash1.get("IDTURNO"));
			ScsTurnoBean beanTurno = (ScsTurnoBean) (new ScsTurnoAdm(usr)
					.select(hashTurno)).get(0);
			request.setAttribute("NOMBRETURNO", beanTurno.getNombre());
			Hashtable hashGuardia = new Hashtable();
			hashGuardia.put(ScsGuardiasTurnoBean.C_IDINSTITUCION, usr
					.getLocation());
			hashGuardia.put(ScsGuardiasTurnoBean.C_IDTURNO, (String) hash1
					.get("IDTURNO"));
			hashGuardia.put(ScsGuardiasTurnoBean.C_IDGUARDIA, (String) hash1
					.get("IDGUARDIA"));
			ScsGuardiasTurnoBean beanGuardia = (ScsGuardiasTurnoBean) (new ScsGuardiasTurnoAdm(
					usr).select(hashGuardia)).get(0);
			request.setAttribute("NOMBREGUARDIA", beanGuardia.getNombre());

			request.getSession().removeAttribute("pagos");
		} catch (Exception e) {
			throw new SIGAException("messages.general.error", e,
					new String[] { "modulo.gratuita" });
		}

		return "abrir";
	} // abrir ()

	protected String abrirAvanzada(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) {
		return mapSinDesarrollar;
	} // abrirAvanzada ()

	protected String buscar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response) {
		return mapSinDesarrollar;
	} // buscar ()

	/**
	 * Funcion que atiende la accion editar
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
		// Controles generales
		UsrBean usr = null;
		ScsHitoFacturableGuardiaAdm hitoAdm = null;

		try {
			// Controles generales
			usr = this.getUserBean(request);

			// recuperando la guardia que está seleccionada desde la sesion
			ScsGuardiasTurnoBean guardia = (ScsGuardiasTurnoBean) request
					.getSession().getAttribute("DATABACKUPPESTANA");

			Vector vOcultos = (Vector) formulario.getDatosTablaOcultos(0);
			Hashtable hashGuardia = (Hashtable) guardia.getOriginalHash();
			DefinirHitosFacturablesGuardiasForm miForm = (DefinirHitosFacturablesGuardiasForm) formulario;
			Hashtable hash = new Hashtable();

			hash.put("IDINSTITUCION", (String) usr.getLocation());
			hash.put("IDTURNO", (String) hashGuardia.get("IDTURNO"));
			hash.put("IDGUARDIA", (String) hashGuardia.get("IDGUARDIA"));
			hash.put("IDHITO", (String) vOcultos.get(0)); // el IDHITO
			hash.put("PAGOOFACTURACION", "F");
			hitoAdm = new ScsHitoFacturableGuardiaAdm(usr);
			Vector vElegido = hitoAdm.select(hash);
			ScsHitoFacturableGuardiaBean hito = (ScsHitoFacturableGuardiaBean) vElegido
					.get(0);
			request.getSession().setAttribute("HITO", hito);
			request.setAttribute("nombreHito", ((Vector) miForm
					.getDatosTablaVisibles(0)).get(0));
			request.setAttribute("idHito", (String) vOcultos.get(0));
			request.setAttribute("modo", "EDITAR");
		} catch (Exception e) {
			throw new SIGAException("messages.general.error", e,
					new String[] { "modulo.gratuita" });
		}

		return "editar";
	} // editar ()

	/**
	 * Funcion que atiende la accion ver
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
		String forward = this.editar(mapping, formulario, request, response);
		request.setAttribute("modo", "VER");
		return forward;
	} // ver ()

	/**
	 * Funcion que atiende la accion nuevo
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
		return "nuevo";
	} // nuevo ()

	/**
	 * Funcion que atiende la accion insertar
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
	protected String insertar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
		// Controles generales
		UsrBean usr = null;
		UserTransaction tx = null;
		ScsHitoFacturableGuardiaAdm hitoAdm = null;

		try {
			// Controles generales
			usr = this.getUserBean(request);
			hitoAdm = new ScsHitoFacturableGuardiaAdm(usr);

			// recuperando la guardia que está seleccionada desde la sesion
			ScsGuardiasTurnoBean guardia = (ScsGuardiasTurnoBean) request
					.getSession().getAttribute("DATABACKUPPESTANA");

			// insertando
			tx = usr.getTransaction();
			tx.begin();

			Hashtable hash = (Hashtable) formulario.getDatos();
			hash.put("USUMODIFICACION", usr.getUserName());
			hash.put("FECHAMODIFICACION", "SYSDATE");
			hash.put("IDINSTITUCION", usr.getLocation());
			hash.put("IDTURNO", guardia.getIdTurno().toString());
			hash.put("IDGUARDIA", guardia.getIdGuardia().toString());
			hash.put("PAGOOFACTURACION", "F"); // cogerlo de un variable de
												// session, pasada desde las
												// pestanhas

			hitoAdm.insert(hash);
			tx.commit();
		} catch (Exception e) {
			throwExcp("messages.general.error",
					new String[] { "modulo.gratuita" }, e, tx);
		}

		return exitoModal("messages.inserted.success", request);
	} // insertar ()

	/**
	 * Funcion que atiende la accion modificar. Obtiene los datos del formulario
	 * para introducirlos en BD
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
	protected String modificar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
		DefinirHitosFacturablesGuardiasForm miForm = (DefinirHitosFacturablesGuardiasForm) formulario;
		UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
		Hashtable originalHash = (Hashtable) request.getSession().getAttribute(
				"ORIGINALHASH");
		ScsHitoFacturableGuardiaAdm hitoAdm = new ScsHitoFacturableGuardiaAdm(
				this.getUserBean(request));
		Hashtable hMap = (Hashtable) miForm.getHitoPrecioHt();
		Hashtable hash = (Hashtable) miForm.getDatos();
		UserTransaction tx = null;

		try {
			borrarHitoPrecio(mapping, miForm, request, response);

			// saliendo si no se pudo crear el mapa de hitos
			if (hMap == null)
				return exito("messages.deleted.error", request);

			// obteniendo las selecciones de dias
			String listaPagaGuardia = this
					.obtieneSeleccionDiasPagaGuardiaFormulario(miForm);
			String listaNoPagaGuardia = this
					.obtieneSeleccionDiasNoPagaGuardiaFormulario(miForm);

			Enumeration e = hMap.keys();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				hash.clear();
				hash.put("PAGOOFACTURACION", "F");
				hash.put("IDINSTITUCION", usr.getLocation());
				hash.put("IDGUARDIA", originalHash.get("IDGUARDIA"));
				hash.put("IDTURNO", originalHash.get("IDTURNO"));
				hash.put("IDHITO", key);
				hash.put("PRECIOHITO",
						(((String) hMap.get(key)).equals("")) ? "0" : hMap.get(
								key).toString().replace(',', '.'));

				if (key.equals("1") || key.equals("2") || key.equals("4")
						|| key.equals("45") || key.equals("46")) {
					hash.put("DIASAPLICABLES", listaPagaGuardia);
					hash.put("AGRUPAR", miForm.getChPagaGuardiaPorDia() ? "0"
							: "1");
					// tener en cuenta que en BD se guarda AGRUPAR,
					// sin embargo por pantalla se selecciona POR DIA, asi que
					// es lo contrario
				}
				if (key.equals("5") || key.equals("3") || key.equals("10")
						|| key.equals("7") || key.equals("8")
						|| key.equals("19")) {
					hash.put("DIASAPLICABLES", listaNoPagaGuardia);
					hash.put("AGRUPAR", miForm.getChNoPagaGuardiaPorDia() ? "0"
							: "1");
					// tener en cuenta que en BD se guarda AGRUPAR,
					// sin embargo por pantalla se selecciona POR DIA, asi que
					// es lo contrario
				}

				hash.put("FECHAMODIFICACION", "SYSDATE");
				hash.put("USUMODIFICACION", usr.getUserName());
				tx = usr.getTransaction();

				tx.begin();
				hitoAdm.insert(hash);
				tx.commit();
			} // while

			// anhadiendo los ocultos que hay que enviar cuando
			// el check de aplicar tipos de Guardia y de Fuera de Guardia
			// estan activados
			// Si se ha actividado el check de aplicar tipos para No Paga
			// Guardia
			if (miForm.getChGuardias()) {
				if (miForm.getCheckB2() && (miForm.getRadioA().equals("0"))) {
					hash.clear();
					hash.put("PAGOOFACTURACION", "F");
					hash.put("IDINSTITUCION", usr.getLocation());
					hash.put("IDGUARDIA", originalHash.get("IDGUARDIA"));
					hash.put("IDTURNO", originalHash.get("IDTURNO"));
					hash.put("IDHITO", "20"); // importe por asistencia
					hash.put("PRECIOHITO", "0");
					hash.put("DIASAPLICABLES", listaNoPagaGuardia);
					hash.put("AGRUPAR", miForm.getChNoPagaGuardiaPorDia() ? "0"
							: "1");
					hash.put("FECHAMODIFICACION", "SYSDATE");
					hash.put("USUMODIFICACION", usr.getUserName());
					tx = usr.getTransaction();

					tx.begin();
					hitoAdm.insert(hash);
					tx.commit();

					if (miForm.getChAsist()) {
						hash.clear();
						hash.put("PAGOOFACTURACION", "F");
						hash.put("IDINSTITUCION", usr.getLocation());
						hash.put("IDGUARDIA", originalHash.get("IDGUARDIA"));
						hash.put("IDTURNO", originalHash.get("IDTURNO"));
						hash.put("IDHITO", "21"); // max. pago por asistencias
						hash.put("PRECIOHITO", "0");
						hash.put("DIASAPLICABLES", listaNoPagaGuardia);
						hash.put("AGRUPAR",
								miForm.getChNoPagaGuardiaPorDia() ? "0" : "1");
						hash.put("FECHAMODIFICACION", "SYSDATE");
						hash.put("USUMODIFICACION", usr.getUserName());
						tx = usr.getTransaction();

						tx.begin();
						hitoAdm.insert(hash);
						tx.commit();
					}
				}
				// Si NO se ha actividado el check de aplicar tipos para No Paga
				// Guardia
				else if (miForm.getCheckB2()
						&& (miForm.getRadioA().equals("1"))) {
					hash.clear();
					hash.put("PAGOOFACTURACION", "F");
					hash.put("IDINSTITUCION", usr.getLocation());
					hash.put("IDGUARDIA", originalHash.get("IDGUARDIA"));
					hash.put("IDTURNO", originalHash.get("IDTURNO"));
					hash.put("IDHITO", "22"); // importe por actuacion
					hash.put("PRECIOHITO", "0");
					hash.put("DIASAPLICABLES", listaNoPagaGuardia);
					hash.put("AGRUPAR", miForm.getChNoPagaGuardiaPorDia() ? "0"
							: "1");
					hash.put("FECHAMODIFICACION", "SYSDATE");
					hash.put("USUMODIFICACION", usr.getUserName());
					tx = usr.getTransaction();

					tx.begin();
					hitoAdm.insert(hash);
					tx.commit();

					if (miForm.getChActuacion()) {
						hash.clear();
						hash.put("PAGOOFACTURACION", "F");
						hash.put("IDINSTITUCION", usr.getLocation());
						hash.put("IDGUARDIA", originalHash.get("IDGUARDIA"));
						hash.put("IDTURNO", originalHash.get("IDTURNO"));
						hash.put("IDHITO", "23"); // max. pago por actuacion
						hash.put("PRECIOHITO", "0");
						hash.put("DIASAPLICABLES", listaNoPagaGuardia);
						hash.put("AGRUPAR",
								miForm.getChNoPagaGuardiaPorDia() ? "0" : "1");
						hash.put("FECHAMODIFICACION", "SYSDATE");
						hash.put("USUMODIFICACION", usr.getUserName());
						tx = usr.getTransaction();

						tx.begin();
						hitoAdm.insert(hash);
						tx.commit();
					}
				}
			}

			// Si se ha actividado el check de aplicar tipos para Fuera de
			// Guardia
			if (miForm.getChNoGuardias()) {
				hash.clear();
				hash.put("PAGOOFACTURACION", "F");
				hash.put("IDINSTITUCION", usr.getLocation());
				hash.put("IDGUARDIA", originalHash.get("IDGUARDIA"));
				hash.put("IDTURNO", originalHash.get("IDTURNO"));
				hash.put("IDHITO", "25"); // importe por actuacion Fuera de
											// Guardia
				hash.put("PRECIOHITO", "0");
				hash.put("FECHAMODIFICACION", "SYSDATE");
				hash.put("USUMODIFICACION", usr.getUserName());
				tx = usr.getTransaction();

				tx.begin();
				hitoAdm.insert(hash);
				tx.commit();

				if (miForm.getChActFG()) {
					hash.clear();
					hash.put("PAGOOFACTURACION", "F");
					hash.put("IDINSTITUCION", usr.getLocation());
					hash.put("IDGUARDIA", originalHash.get("IDGUARDIA"));
					hash.put("IDTURNO", originalHash.get("IDTURNO"));
					hash.put("IDHITO", "24"); // importe max. actuacion Fuera de
												// Guardia
					hash.put("PRECIOHITO", "0");
					hash.put("FECHAMODIFICACION", "SYSDATE");
					hash.put("USUMODIFICACION", usr.getUserName());
					tx = usr.getTransaction();

					tx.begin();
					hitoAdm.insert(hash);
					tx.commit();
				}
			}
		} catch (Exception e) {
			throwExcp("messages.general.error",
					new String[] { "modulo.gratuita" }, e, tx);
		}

		return exitoRefresco("messages.updated.success", request);
	} // modificar ()

	/**
	 * Funcion que atiende la accion borrar
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
	protected String borrar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
		UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
		ScsHitoFacturableGuardiaAdm hitoAdm = new ScsHitoFacturableGuardiaAdm(
				this.getUserBean(request));
		// recuperando la guardia que está seleccionada desde la sesion
		ScsGuardiasTurnoBean guardia = (ScsGuardiasTurnoBean) request
				.getSession().getAttribute("DATABACKUPPESTANA");
		Hashtable hash = new Hashtable();

		try {
			hash.put("IDINSTITUCION", usr.getLocation());
			hash.put("IDTURNO", guardia.getIdTurno().toString());
			hash.put("IDGUARDIA", guardia.getIdGuardia().toString());
			hash.put("IDHITO", ((Vector) formulario.getDatosTablaOcultos(0))
					.get(0));
			hash.put("PAGOOFACTURACION", "F");

			try {
				hitoAdm.delete(hash);
			} catch (Exception e) {
				return exito("messages.deleted.error", request);
			}
		} catch (Exception e) {
			throwExcp("messages.deleted.error", e, null);
		}

		return exitoRefresco("messages.deleted.success", request);
	} // borrar ()

	/**
	 * Funcion que atiende la accion borrar
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
	protected void borrarHitoPrecio(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {
		UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
		// recuperando la guardia que esta seleccionada desde la sesion
		ScsGuardiasTurnoBean guardia = (ScsGuardiasTurnoBean) request
				.getSession().getAttribute("DATABACKUPPESTANA");
		Hashtable hash = new Hashtable();

		try {
			hash.put("IDINSTITUCION", usr.getLocation());
			hash.put("IDTURNO", guardia.getIdTurno().toString());
			hash.put("IDGUARDIA", guardia.getIdGuardia().toString());
			hash.put("PAGOOFACTURACION", "F");

			try {
				Row row = new Row();
				row.load(hash);

				String[] claves = {
						ScsHitoFacturableGuardiaBean.C_IDINSTITUCION,
						ScsHitoFacturableGuardiaBean.C_IDTURNO,
						ScsHitoFacturableGuardiaBean.C_IDGUARDIA,
						ScsHitoFacturableGuardiaBean.C_PAGOFACTURACION };

				row.delete(ScsHitoFacturableGuardiaBean.T_NOMBRETABLA, claves);
			} catch (Exception e) {
				throw new ClsExceptions(e,
						"Excepcion en ScsHitoFacturableGuardiaBean."
								+ "borrarHitoPrecio() al borrar un registro");
			}
		} catch (Exception e) {
			throwExcp("messages.deleted.error", e, null);
		}
	} // borrarHitoPrecio ()

	protected String buscarPor(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions {
		return null;
	} // buscarPor ()

	/**
	 * Rellena la seleccion de dias en el formulario que se muestra en pantalla
	 * 
	 * @param miForm
	 * @param listaPagaGuardia
	 * @param listaNoPagaGuardia
	 */
	private void rellenaSeleccionDiasFormulario(
			DefinirHitosFacturablesGuardiasForm miForm,
			String listaPagaGuardia, String listaNoPagaGuardia) {
		miForm.setChPagaGuardiaLunes((listaPagaGuardia.indexOf('L') > -1));
		miForm.setChPagaGuardiaMartes((listaPagaGuardia.indexOf('M') > -1));
		miForm.setChPagaGuardiaMiercoles((listaPagaGuardia.indexOf('X') > -1));
		miForm.setChPagaGuardiaJueves((listaPagaGuardia.indexOf('J') > -1));
		miForm.setChPagaGuardiaViernes((listaPagaGuardia.indexOf('V') > -1));
		miForm.setChPagaGuardiaSabado((listaPagaGuardia.indexOf('S') > -1));
		miForm.setChPagaGuardiaDomingo((listaPagaGuardia.indexOf('D') > -1));

		miForm.setChNoPagaGuardiaLunes((listaNoPagaGuardia.indexOf('L') > -1));
		miForm.setChNoPagaGuardiaMartes((listaNoPagaGuardia.indexOf('M') > -1));
		miForm
				.setChNoPagaGuardiaMiercoles((listaNoPagaGuardia.indexOf('X') > -1));
		miForm.setChNoPagaGuardiaJueves((listaNoPagaGuardia.indexOf('J') > -1));
		miForm
				.setChNoPagaGuardiaViernes((listaNoPagaGuardia.indexOf('V') > -1));
		miForm.setChNoPagaGuardiaSabado((listaNoPagaGuardia.indexOf('S') > -1));
		miForm
				.setChNoPagaGuardiaDomingo((listaNoPagaGuardia.indexOf('D') > -1));
	} // rellenaSeleccionDiasFormulario ()

	/**
	 * Obtiene la seleccion de dias que paga guardia desde el formulario que se
	 * muestra en pantalla
	 * 
	 * @param miForm
	 * @param listaPagaGuardia
	 * @param listaNoPagaGuardia
	 */
	private String obtieneSeleccionDiasPagaGuardiaFormulario(
			DefinirHitosFacturablesGuardiasForm miForm) {
		String lista = "";
		lista += miForm.getChPagaGuardiaLunes() ? "L" : "";
		lista += miForm.getChPagaGuardiaMartes() ? "M" : "";
		lista += miForm.getChPagaGuardiaMiercoles() ? "X" : "";
		lista += miForm.getChPagaGuardiaJueves() ? "J" : "";
		lista += miForm.getChPagaGuardiaViernes() ? "V" : "";
		lista += miForm.getChPagaGuardiaSabado() ? "S" : "";
		lista += miForm.getChPagaGuardiaDomingo() ? "D" : "";
		return lista;
	}

	/**
	 * Obtiene la seleccion de dias que no paga guardia desde el formulario que
	 * se muestra en pantalla
	 * 
	 * @param miForm
	 * @param listaPagaGuardia
	 * @param listaNoPagaGuardia
	 */
	private String obtieneSeleccionDiasNoPagaGuardiaFormulario(
			DefinirHitosFacturablesGuardiasForm miForm) {
		String lista = "";
		lista += miForm.getChNoPagaGuardiaLunes() ? "L" : "";
		lista += miForm.getChNoPagaGuardiaMartes() ? "M" : "";
		lista += miForm.getChNoPagaGuardiaMiercoles() ? "X" : "";
		lista += miForm.getChNoPagaGuardiaJueves() ? "J" : "";
		lista += miForm.getChNoPagaGuardiaViernes() ? "V" : "";
		lista += miForm.getChNoPagaGuardiaSabado() ? "S" : "";
		lista += miForm.getChNoPagaGuardiaDomingo() ? "D" : "";
		return lista;
	} // obtieneSeleccionDiasNoPagaGuardiaFormulario ()

}