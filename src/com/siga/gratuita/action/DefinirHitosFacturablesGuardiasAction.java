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
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.FcsHistoricoHitoFactAdm;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.ScsGuardiasTurnoAdm;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.beans.ScsHitoFacturableAdm;
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
	protected ActionForward executeInternal(ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		MasterForm miform = (MasterForm) formulario;
		String accion = (miform == null ? "abrir" : miform.getModo());
		String result = "exception";

		try {
			if (accion != null && accion.equals("consultar"))
				result = consultar(mapping, miform, (String) request.getParameter("tipoConsulta"), request, response);
			else
				return super.executeInternal(mapping, formulario, request, response);

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
	protected String consultar(ActionMapping mapping, MasterForm formulario, String tipo, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			ScsTipoAsistenciaColegioAdm tipoAsist = new ScsTipoAsistenciaColegioAdm(this.getUserBean(request));
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
			throw new SIGAException("messages.general.error", e, new String[] { "modulo.gratuita" });
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
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		// Variables
		String c_idhito = ScsHitoFacturableGuardiaBean.C_IDHITO;

		try {
			// Controles generales
			UsrBean usr = this.getUserBean(request);
			
			// definiendo el formulario
			DefinirHitosFacturablesGuardiasForm miForm = (DefinirHitosFacturablesGuardiasForm) formulario;

			// recuperando la guardia que está seleccionada desde la sesion
			ScsGuardiasTurnoBean guardia = (ScsGuardiasTurnoBean) request.getSession().getAttribute("DATABACKUPPESTANA");
			Hashtable<String,Object> hash1 = guardia.getOriginalHash();
			request.getSession().setAttribute("ORIGINALHASH", hash1);
			
			String sInstitucion = usr.getLocation();						
			String sIdTurno = UtilidadesHash.getString(hash1, "IDTURNO");
			String sIdGuardia = UtilidadesHash.getString(hash1, "IDGUARDIA");
			FcsHistoricoHitoFactAdm admFcsHistoricoHitoFact = new FcsHistoricoHitoFactAdm(usr);
			Vector<Hashtable<String,Object>> vFcsHistoricoHitoFact = admFcsHistoricoHitoFact.obtenerFacturacionesSJCSGuardia(sInstitucion, sIdTurno, sIdGuardia);
			request.setAttribute("vFcsHistoricoHitoFact", vFcsHistoricoHitoFact);
			
			Vector<Hashtable<String,Object>> vHitos;
			String sBuscarFacturacionSJCS =  miForm.getBuscarFacturacionSJCS();
			ScsHitoFacturableAdm admHitoFacturable = new ScsHitoFacturableAdm(usr);
			if (sBuscarFacturacionSJCS==null || sBuscarFacturacionSJCS.equals("")) {
				vHitos = admHitoFacturable.obtenerHitosActual(sInstitucion, sIdTurno, sIdGuardia);
			} else {
				vHitos = admHitoFacturable.obtenerHitosHistorico(sInstitucion, sIdTurno, sIdGuardia, sBuscarFacturacionSJCS);
			}

			// Si no hay una configuracion de guardias se muestra un mensaje
			if (vHitos.size() == 0) {
				request.setAttribute("EXISTENHITOS", "0");
				// y se obliga a tener un hito
				Hashtable<String,Object> hash = new Hashtable<String,Object>();
				hash.put(c_idhito, "1"); // 1:GAs
				hash.put(ScsHitoFacturableGuardiaBean.C_PRECIOHITO, "0");
				hash.put(ScsHitoFacturableGuardiaBean.C_DIASAPLICABLES, "LMXJVSD");
				hash.put(ScsHitoFacturableGuardiaBean.C_AGRUPAR, "0");
				vHitos.add(hash);
			} else
				request.setAttribute("EXISTENHITOS", "1");

			// obteniendo los hitos del vector a un hash y mas
			Hashtable<String,Object> hashResul = new Hashtable<String,Object>();
			Hashtable<String,Object> hashElem;
			String listaPagaGuardias = "", listaNoPagaGuardias = "";
			boolean agruparPagaGuardias = false, agruparNoPagaGuardias = false;
			boolean hayHito45 = false, hayHito46 = false;
			for (int cont = 0; cont < vHitos.size(); cont++) {
				// obteniendo hash de cada hito
				hashElem = (Hashtable<String,Object>) vHitos.get(cont);

				// 44=GAc; 1=GAs
				if (hashElem.get(c_idhito).equals("1") || hashElem.get(c_idhito).equals("44")) {
					listaPagaGuardias = (String) hashElem.get(ScsHitoFacturableGuardiaBean.C_DIASAPLICABLES);
					agruparPagaGuardias = ((String) hashElem.get(ScsHitoFacturableGuardiaBean.C_AGRUPAR)).equals("1");
				}
				
				// 5=As; 7=Ac
				if (hashElem.get(c_idhito).equals("5") || hashElem.get(c_idhito).equals("7")) {
					listaNoPagaGuardias = (String) hashElem.get(ScsHitoFacturableGuardiaBean.C_DIASAPLICABLES);
					agruparNoPagaGuardias = ((String) hashElem.get(ScsHitoFacturableGuardiaBean.C_AGRUPAR)).equals("1");
				}

				// Obteniendo Maximo para Duplicar Asistencias y Actuaciones
				hayHito45 = hayHito45 || (hashElem.get(c_idhito).equals("45")); // 45:NDAs
				hayHito46 = hayHito46 || (hashElem.get(c_idhito).equals("46")); // 46:NDAc

				hashResul.put(hashElem.get(c_idhito).toString(), hashElem.get(ScsHitoFacturableGuardiaBean.C_PRECIOHITO));
			}
			
			// IDHITO=61(FacConfig) - Obtener hitos de fact.= 0:Siempre actual; 1:Del histórico siempre que sea posible
			// IDHITO=62(FacConfigFG) - Obtener hitos de fact. FG= 0:Siempre actual; 1:De actuacion moderna; 2:De actuacion antigua
			CenInstitucionAdm admInstituciones = new CenInstitucionAdm(usr);
			Integer iConsejo = admInstituciones.getInstitucionPadre(Integer.valueOf(sInstitucion));
			if (iConsejo!=null && iConsejo.equals(3001)) { // 3001 = Catalan
				miForm.setRadioConfig("1"); //  IDHITO=61(FacConfig) => 1:Configuracion Antigua
				miForm.setRadioConfigFg("1"); //  IDHITO=62(FacConfigFG) => 1:Configuracion Facturacion Moderna
			} else {
				miForm.setRadioConfigFg("0"); // IDHITO=61(FacConfig) => 0:Configuracion Actual
				miForm.setRadioConfig("0"); // IDHITO=62(FacConfigFG) => 0:Configuracion Actual
			}
			if (hashResul.containsKey("61") && hashResul.get("61")!=null && !UtilidadesHash.getString(hashResul,"61").equals("")) {
				miForm.setRadioConfig(UtilidadesHash.getString(hashResul,"61"));
			}
			if (hashResul.containsKey("62") && hashResul.get("62")!=null && !UtilidadesHash.getString(hashResul,"62").equals("")) {
				miForm.setRadioConfigFg(UtilidadesHash.getString(hashResul,"62"));
			}

			miForm.setRadioPG("0"); // Activo Asistencias en el radio de paga guardia (0:As; 1:Ac) => GAs			
			miForm.setCheckB1(false); // Desmarco check de Paga Guardia						
			if (hashResul.containsKey("1")) { // 1:GAs 
				miForm.setCheckB1(true); // Marco el check de Paga Guardia
			}			
			if (hashResul.containsKey("44")) { // 44:GAc
				miForm.setRadioPG("1"); // Activo Actuaciones en el radio de no paga guardia (0:As; 1:Ac) => GAc
				miForm.setCheckB1(true); // Marco el check de Paga Guardia
			}
			
			miForm.setRadioNPG("0"); // Activo Asistencias en el radio de no paga guardia (0:As; 1:Ac) => As
			miForm.setCheckB2(false); // Desmarco check de No Paga Guardia
			if (hashResul.containsKey("5")) { // 5:As
				miForm.setCheckB2(true); // Marco check de No Paga Guardia
			}			
			if (hashResul.containsKey("7")) { // 7:Ac
				miForm.setRadioNPG("1"); // Activo Actuaciones en el radio de no paga guardia (0:As; 1:Ac) => Ac
				miForm.setCheckB2(true); // Marco check de No Paga Guardia
			}

			this.rellenaSeleccionDiasFormulario(miForm, listaPagaGuardias, listaNoPagaGuardias);
			miForm.setChPagaGuardiaPorDia(!agruparPagaGuardias);
			miForm.setChNoPagaGuardiaPorDia(!agruparNoPagaGuardias);

			// ChAsist = Máximo Asistencias No paga Guardia
			miForm.setChAsist(hashResul.containsKey("3")); // 3:AsMax
			
			// ChMinAsist = Minimo Asistencias No Paga Guardia
			miForm.setChMinAsist(hashResul.containsKey("10")); // 10:AsMin
			
			// ChActuacion = Máximo Actuaciones No Paga Guardia
			miForm.setChActuacion(hashResul.containsKey("8")); // 8:AcMax
			
			// ChMinActuacion = Minimo Actuaciones No Paga Guardia
			miForm.setChMinActuacion(hashResul.containsKey("19")); // 19:AcMin
			
			// checkC = Check Fuera Guardia
			miForm.setCheckC(hashResul.containsKey("9")); // 9:AcFG
			
			// ChActFG = Máximo Actuaciones Fuera Guardia 
			miForm.setChActFG(hashResul.containsKey("6")); // 6:AcFGMax
			
			// ChGuardias = Aplica Tipos No Paga Guardias
			miForm.setChGuardias(hashResul.containsKey("20") || hashResul.containsKey("22")); // 20:AsTp; 22:AcTp
			
			// ChNoGuardias = Aplica Tipos Fuera Guardias
			miForm.setChNoGuardias(hashResul.containsKey("25")); // 25:AcFGTp

			// obteniendo valores de parametros generales para los hitos de
			// maximo para duplicar de asistencias (45) y actuciones (46)
			String numAsist = "", numAct = "";
			GenParametrosAdm paramAdm = new GenParametrosAdm(this.getUserBean(request));
			try {
				numAsist = paramAdm.getValor(sInstitucion, "FCS", "NUM_ASISTENCIAS", "");
				numAct = paramAdm.getValor(sInstitucion, "FCS", "NUM_ACTUACIONES", "");
			} catch (Exception e) {
			}
			
			 // 45:NDAs
			if (!hayHito45)
				hashResul.put("45", numAsist);
			
			 // 46:NDAc
			if (!hayHito46)
				hashResul.put("46", numAct);

			// introduciendo los hitos en el formulario que se mostrara en JSP
			miForm.setHitoPrecioHt(hashResul);

			// obteniendo y pasando por sesion los nombres de turno y guardia
			Hashtable<String,Object> hashTurno = new Hashtable<String,Object>();
			hashTurno.put(ScsTurnoBean.C_IDINSTITUCION, sInstitucion);
			hashTurno.put(ScsTurnoBean.C_IDTURNO, sIdTurno);
			ScsTurnoBean beanTurno = (ScsTurnoBean) (new ScsTurnoAdm(usr).select(hashTurno)).get(0);			
			request.setAttribute("NOMBRETURNO", beanTurno.getNombre());
			
			Hashtable<String,Object> hashGuardia = new Hashtable<String,Object>();
			hashGuardia.put(ScsGuardiasTurnoBean.C_IDINSTITUCION, sInstitucion);
			hashGuardia.put(ScsGuardiasTurnoBean.C_IDTURNO, sIdTurno);
			hashGuardia.put(ScsGuardiasTurnoBean.C_IDGUARDIA, sIdGuardia);
			ScsGuardiasTurnoBean beanGuardia = (ScsGuardiasTurnoBean) (new ScsGuardiasTurnoAdm(usr).select(hashGuardia)).get(0);
			request.setAttribute("NOMBREGUARDIA", beanGuardia.getNombre());

			request.getSession().removeAttribute("pagos");
						
			/* Inicio facturacion controlada cantabria (2016) */
			String checkControlado = "5";
			String importeControlado = "0";
			String minimoControlado = "0";			
			if (sInstitucion.equals("2016")) { // 2016 = Cantabria
				
				if (vHitos.size() == 5 && 
						hashResul.containsKey("5") && // 5:As 
						hashResul.containsKey("12") && // 12:SOJ
						hashResul.containsKey("13") && // 13:EJG
						hashResul.containsKey("61") && // 61:FacConfig
						hashResul.containsKey("62") && // 62:FacConfigFG
						hashResul.get("5")!=null && !UtilidadesHash.getString(hashResul,"5").equals("0")) { // Compruebo que tiene importe de asistencia
					checkControlado = "1";
					importeControlado = UtilidadesHash.getString(hashResul,"5");
					
				} else if (vHitos.size() == 6 && 
						hashResul.containsKey("5") && // 5:As  
						hashResul.containsKey("10") && // 10:AsMin
						hashResul.containsKey("12") && // 12:SOJ
						hashResul.containsKey("13") && // 13:EJG
						hashResul.containsKey("61") && // 61:FacConfig
						hashResul.containsKey("62") && // 62:FacConfigFG
						!agruparNoPagaGuardias && // AGRUPARNOPAGAGUARDIA=0 => Aplica Minimo en UG (no tiene máximos)
						hashResul.get("5")!=null && !UtilidadesHash.getString(hashResul,"5").equals("0") && // Compruebo que tiene importe de asistencia 
						hashResul.get("10")!=null && !UtilidadesHash.getString(hashResul,"10").equals("0")) { // Compruebo que tiene importe mínimo de asistencia 
					checkControlado = "2";
					importeControlado = UtilidadesHash.getString(hashResul,"5");
					minimoControlado = UtilidadesHash.getString(hashResul,"10");
					
				} else if (vHitos.size() == 7 && 
						hashResul.containsKey("5") && // 5:As 
						hashResul.containsKey("10") && // 10:AsMin 
						hashResul.containsKey("12") && // 12:SOJ 
						hashResul.containsKey("13") && // 13:EJG 
						hashResul.containsKey("20") && // 20:AsTp
						hashResul.containsKey("61") && // 61:FacConfig
						hashResul.containsKey("62") && // 62:FacConfigFG
						!agruparNoPagaGuardias && // AGRUPARNOPAGAGUARDIA=0 => Aplica Minimo en UG (no tiene máximos)
						hashResul.get("5")!=null && !UtilidadesHash.getString(hashResul,"5").equals("0") && // Compruebo que tiene importe de asistencia
						hashResul.get("10")!=null && !UtilidadesHash.getString(hashResul,"10").equals("0")) { // Compruebo que tiene importe mínimo de asistencia 
					checkControlado = "3";
					importeControlado = UtilidadesHash.getString(hashResul,"5");
					minimoControlado = UtilidadesHash.getString(hashResul,"10");
					
				} else if ((vHitos.size() == 7 || (vHitos.size() == 8 && hashResul.containsKey("53"))) && // 53:GAsMin   
						hashResul.containsKey("1") && // 1:GAs 
						hashResul.containsKey("2") && // 2:GDAs 
						hashResul.containsKey("12") && // 12:SOJ 
						hashResul.containsKey("13") && // 13:EJG 
						hashResul.containsKey("45") && // 45:NDAs
						hashResul.containsKey("61") && // 61:FacConfig
						hashResul.containsKey("62") && // 62:FacConfigFG
						hashResul.get("1")!=null && !UtilidadesHash.getString(hashResul,"1").equals("0") && // Compruebo que tiene importe de guardia por asistencias 
						(
							hashResul.get("1")==null || // No tenga valor 
							UtilidadesHash.getString(hashResul,"2").equals("") || // No tenga dato 
							UtilidadesHash.getString(hashResul,"2").equals("0") || // Tenga importe doblado a cero
							UtilidadesHash.getString(hashResul,"2").equals(UtilidadesHash.getString(hashResul,"1")) // Tenga el mismo importe doblado que el de la guardia por asistencias
						)) {
					checkControlado = "4";
					importeControlado = UtilidadesHash.getString(hashResul,"1");
				
				// Este apartado es para que salgan las opciones de controlado al crear una guardia 
				} else if (request.getAttribute("EXISTENHITOS").toString() == "0") {
					checkControlado = "4";				
				}				
			}	
			request.setAttribute("checkControlado", checkControlado);
			request.setAttribute("importeControlado", importeControlado);
			request.setAttribute("minimoControlado", minimoControlado);
			/* Fin facturacion controlada cantabria */
			
			// Obtiene el paquete con el que realiza la facturacion SJCS
			GenParametrosAdm admParam = new GenParametrosAdm(usr);
			String parametroPaqueteFacturacionSJCS = admParam.getValor("" + sInstitucion, "FCS", "FACTURACION_HITOS", "N");
			request.setAttribute("parametroPaqueteFacturacionSJCS", parametroPaqueteFacturacionSJCS);
			request.setAttribute("iConsejo", iConsejo);			

		} catch (Exception e) {
			throw new SIGAException("messages.general.error", e, new String[] { "modulo.gratuita" });
		}

		return "abrir";
	} // abrir ()

	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) {
		return mapSinDesarrollar;
	} // abrirAvanzada ()

	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) {
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
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		// Controles generales
		UsrBean usr = null;
		ScsHitoFacturableGuardiaAdm hitoAdm = null;

		try {
			// Controles generales
			usr = this.getUserBean(request);

			// recuperando la guardia que está seleccionada desde la sesion
			ScsGuardiasTurnoBean guardia = (ScsGuardiasTurnoBean) request.getSession().getAttribute("DATABACKUPPESTANA");

			Vector<String> vOcultos = formulario.getDatosTablaOcultos(0);
			Hashtable<String,Object> hashGuardia = guardia.getOriginalHash();
			DefinirHitosFacturablesGuardiasForm miForm = (DefinirHitosFacturablesGuardiasForm) formulario;
			Hashtable<String,Object> hash = new Hashtable<String,Object>();

			hash.put("IDINSTITUCION", (String) usr.getLocation());
			hash.put("IDTURNO", (String) hashGuardia.get("IDTURNO"));
			hash.put("IDGUARDIA", (String) hashGuardia.get("IDGUARDIA"));
			hash.put("IDHITO", (String) vOcultos.get(0)); // el IDHITO
			hash.put("PAGOOFACTURACION", "F");
			hitoAdm = new ScsHitoFacturableGuardiaAdm(usr);
			Vector<ScsHitoFacturableGuardiaBean> vElegido = hitoAdm.select(hash);
			ScsHitoFacturableGuardiaBean hito = (ScsHitoFacturableGuardiaBean) vElegido.get(0);
			request.getSession().setAttribute("HITO", hito);
			request.setAttribute("nombreHito", ((Vector) miForm.getDatosTablaVisibles(0)).get(0));
			request.setAttribute("idHito", (String) vOcultos.get(0));
			request.setAttribute("modo", "EDITAR");
		} catch (Exception e) {
			throw new SIGAException("messages.general.error", e, new String[] { "modulo.gratuita" });
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
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
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
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
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
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		// Controles generales
		UsrBean usr = null;
		UserTransaction tx = null;
		ScsHitoFacturableGuardiaAdm hitoAdm = null;

		try {
			// Controles generales
			usr = this.getUserBean(request);
			hitoAdm = new ScsHitoFacturableGuardiaAdm(usr);

			// recuperando la guardia que está seleccionada desde la sesion
			ScsGuardiasTurnoBean guardia = (ScsGuardiasTurnoBean) request .getSession().getAttribute("DATABACKUPPESTANA");

			// insertando
			tx = usr.getTransaction();
			tx.begin();

			Hashtable<String,Object> hash = formulario.getDatos();
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
			throwExcp("messages.general.error", new String[] { "modulo.gratuita" }, e, tx);
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
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		DefinirHitosFacturablesGuardiasForm miForm = (DefinirHitosFacturablesGuardiasForm) formulario;
		UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
		Hashtable<String,Object> originalHash = (Hashtable<String,Object>) request.getSession().getAttribute("ORIGINALHASH");
		ScsHitoFacturableGuardiaAdm hitoAdm = new ScsHitoFacturableGuardiaAdm(this.getUserBean(request));
		Hashtable<String,Object> hMap = miForm.getHitoPrecioHt();
		Hashtable<String,Object> hash = miForm.getDatos();
		UserTransaction tx = null;

		try {
			tx = usr.getTransaction();
			tx.begin();
			
			borrarHitoPrecio(mapping, miForm, request, response);

			// saliendo si no se pudo crear el mapa de hitos
			if (hMap == null)
				return exito("messages.deleted.error", request);

			// obteniendo las selecciones de dias
			String listaPagaGuardia = this.obtieneSeleccionDiasPagaGuardiaFormulario(miForm);
			String listaNoPagaGuardia = this.obtieneSeleccionDiasNoPagaGuardiaFormulario(miForm);
			
			String sIdInstitucion = usr.getLocation();
			String sIdGuardia = UtilidadesHash.getString(originalHash, "IDGUARDIA");
			String sIdTurno = UtilidadesHash.getString(originalHash, "IDTURNO");
			String sUser = usr.getUserName();			

			Enumeration<String> e = hMap.keys();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				String sPrecio = (String) hMap.get(key);
				hash.clear();
				hash.put("PAGOOFACTURACION", "F");
				hash.put("IDINSTITUCION", sIdInstitucion);
				hash.put("IDGUARDIA", sIdGuardia);
				hash.put("IDTURNO", sIdTurno);
				hash.put("IDHITO", key);
				hash.put("PRECIOHITO", sPrecio.equals("") ? "0" : sPrecio.replace(',', '.'));

				// 1:GAs; 44:GAc
				if (key.equals("1") || key.equals("44")) {
					hash.put("DIASAPLICABLES", listaPagaGuardia);
					hash.put("AGRUPAR", miForm.getChPagaGuardiaPorDia() ? "0" : "1");
					// tener en cuenta que en BD se guarda AGRUPAR, sin embargo por pantalla se selecciona POR DIA, asi que es lo contrario
				}
				
				// 5:As; 7:Ac
				if (key.equals("5") || key.equals("7")) {
					hash.put("DIASAPLICABLES", listaNoPagaGuardia);
					hash.put("AGRUPAR", miForm.getChNoPagaGuardiaPorDia() ? "0" : "1");
					// tener en cuenta que en BD se guarda AGRUPAR, sin embargo por pantalla se selecciona POR DIA, asi que es lo contrario
				}

				hash.put("FECHAMODIFICACION", "SYSDATE");
				hash.put("USUMODIFICACION", sUser);
				hitoAdm.insert(hash);
			} // while
			
			// IDHITO=61(FacConfig) - Obtener hitos de fact.= 0:Siempre actual; 1:Del histórico siempre que sea posible
			String sRadioConfig = miForm.getRadioConfig();
			hash.clear();
			if (sRadioConfig!=null && sRadioConfig.equals("1")) { 
				hash.put("PRECIOHITO", "1"); // 1:Del histórico siempre que sea posible
			} else {
				hash.put("PRECIOHITO", "0"); // 0:Siempre actual
			}			
			hash.put("PAGOOFACTURACION", "F");
			hash.put("IDINSTITUCION", sIdInstitucion);
			hash.put("IDGUARDIA", sIdGuardia);
			hash.put("IDTURNO", sIdTurno);
			hash.put("IDHITO", "61"); // 61:FacConfig			
			hash.put("FECHAMODIFICACION", "SYSDATE");
			hash.put("USUMODIFICACION", sUser);
			hitoAdm.insert(hash);
			
			// IDHITO=62(FacConfigFG) - Obtener hitos de fact. FG= 0:Siempre actual; 1:De actuacion moderna; 2:De actuacion antigua
			String sRadioConfigFg = miForm.getRadioConfigFg();
			hash.clear();
			if (sRadioConfigFg!=null && sRadioConfigFg.equals("1")) { 
				hash.put("PRECIOHITO", "1"); // 1:De actuacion moderna
			} else if (sRadioConfigFg!=null && sRadioConfigFg.equals("2")) { 
				hash.put("PRECIOHITO", "2"); // 2:De actuacion antigua
			} else {
				hash.put("PRECIOHITO", "0"); // 0:Siempre actual
			}			
			hash.put("PAGOOFACTURACION", "F");
			hash.put("IDINSTITUCION", sIdInstitucion);
			hash.put("IDGUARDIA", sIdGuardia);
			hash.put("IDTURNO", sIdTurno);
			hash.put("IDHITO", "62"); // 62:FacConfigFG			
			hash.put("FECHAMODIFICACION", "SYSDATE");
			hash.put("USUMODIFICACION", sUser);
			hitoAdm.insert(hash);			

			// anhadiendo los ocultos que hay que enviar cuando
			// el check de aplicar tipos de Guardia y de Fuera de Guardia estan activados
			// Si se ha actividado el check de aplicar tipos para No Paga Guardia
			if (miForm.getChGuardias()) { // Aplica tipos No Paga Guardias
				if (miForm.getCheckB2() && miForm.getRadioNPG().equals("0")) { // CheckB2=NoPagaGuardia; RadioNPG[0]=As
					hash.clear();
					hash.put("PAGOOFACTURACION", "F");
					hash.put("IDINSTITUCION", sIdInstitucion);
					hash.put("IDGUARDIA", sIdGuardia);
					hash.put("IDTURNO", sIdTurno);
					hash.put("IDHITO", "20"); // 20:AsTp
					hash.put("PRECIOHITO", "0");
					hash.put("FECHAMODIFICACION", "SYSDATE");
					hash.put("USUMODIFICACION", sUser);
					hitoAdm.insert(hash);

					if (miForm.getChAsist()) { // Máximo Asistencias No paga Guardia
						hash.clear();
						hash.put("PAGOOFACTURACION", "F");
						hash.put("IDINSTITUCION", sIdInstitucion);
						hash.put("IDGUARDIA", sIdGuardia);
						hash.put("IDTURNO", sIdTurno);
						hash.put("IDHITO", "21"); // 21:AsTpMax
						hash.put("PRECIOHITO", "0");
						hash.put("FECHAMODIFICACION", "SYSDATE");
						hash.put("USUMODIFICACION", sUser);
						hitoAdm.insert(hash);
					}
				}
				
				// Si NO se ha actividado el check de aplicar tipos para No Paga Guardia
				else if (miForm.getCheckB2() && miForm.getRadioNPG().equals("1")) { // CheckB2=NoPagaGuardia; RadioNPG[1]=Ac
					hash.clear();
					hash.put("PAGOOFACTURACION", "F");
					hash.put("IDINSTITUCION", sIdInstitucion);
					hash.put("IDGUARDIA", sIdGuardia);
					hash.put("IDTURNO", sIdTurno);
					hash.put("IDHITO", "22"); // 22:AcTp
					hash.put("PRECIOHITO", "0");
					hash.put("FECHAMODIFICACION", "SYSDATE");
					hash.put("USUMODIFICACION", sUser);
					hitoAdm.insert(hash);

					if (miForm.getChActuacion()) { // Máximo Actuaciones No paga Guardia
						hash.clear();
						hash.put("PAGOOFACTURACION", "F");
						hash.put("IDINSTITUCION", sIdInstitucion);
						hash.put("IDGUARDIA", sIdGuardia);
						hash.put("IDTURNO", sIdTurno);
						hash.put("IDHITO", "23"); // 23:AcTpMax
						hash.put("PRECIOHITO", "0");
						hash.put("FECHAMODIFICACION", "SYSDATE");
						hash.put("USUMODIFICACION", sUser);
						hitoAdm.insert(hash);
					}
				}
			}

			// Si se ha actividado el check de aplicar tipos para Fuera de Guardia
			if (miForm.getChNoGuardias()) { // Aplica tipos Fuera Guardias
				hash.clear();
				hash.put("PAGOOFACTURACION", "F");
				hash.put("IDINSTITUCION", sIdInstitucion);
				hash.put("IDGUARDIA", sIdGuardia);
				hash.put("IDTURNO", sIdTurno);
				hash.put("IDHITO", "25"); // 25:FGTp
				hash.put("PRECIOHITO", "0");
				hash.put("FECHAMODIFICACION", "SYSDATE");
				hash.put("USUMODIFICACION", sUser);
				hitoAdm.insert(hash);

				if (miForm.getChActFG()) { // Máximo Actuaciones Fuera Guardia
					hash.clear();
					hash.put("PAGOOFACTURACION", "F");
					hash.put("IDINSTITUCION", sIdInstitucion);
					hash.put("IDGUARDIA", sIdGuardia);
					hash.put("IDTURNO", sIdTurno);
					hash.put("IDHITO", "24"); // 24:FGTpMax
					hash.put("PRECIOHITO", "0");
					hash.put("FECHAMODIFICACION", "SYSDATE");
					hash.put("USUMODIFICACION", sUser);
					hitoAdm.insert(hash);
				}
			}
			
			tx.commit();
			
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita" }, e, tx);
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
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
		ScsHitoFacturableGuardiaAdm hitoAdm = new ScsHitoFacturableGuardiaAdm(this.getUserBean(request));
		// recuperando la guardia que está seleccionada desde la sesion
		ScsGuardiasTurnoBean guardia = (ScsGuardiasTurnoBean) request.getSession().getAttribute("DATABACKUPPESTANA");
		Hashtable<String,Object> hash = new Hashtable<String,Object>();

		try {
			hash.put("IDINSTITUCION", usr.getLocation());
			hash.put("IDTURNO", guardia.getIdTurno().toString());
			hash.put("IDGUARDIA", guardia.getIdGuardia().toString());
			hash.put("IDHITO", ((Vector) formulario.getDatosTablaOcultos(0)).get(0));
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
	protected void borrarHitoPrecio(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
		// recuperando la guardia que esta seleccionada desde la sesion
		ScsGuardiasTurnoBean guardia = (ScsGuardiasTurnoBean) request.getSession().getAttribute("DATABACKUPPESTANA");
		Hashtable<String,Object> hash = new Hashtable<String,Object>();

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
						ScsHitoFacturableGuardiaBean.C_IDGUARDIA };

				row.delete(ScsHitoFacturableGuardiaBean.T_NOMBRETABLA, claves);
			} catch (Exception e) {
				throw new ClsExceptions(e, "Excepcion en ScsHitoFacturableGuardiaBean." + "borrarHitoPrecio() al borrar un registro");
			}
		} catch (Exception e) {
			throwExcp("messages.deleted.error", e, null);
		}
	} // borrarHitoPrecio ()

	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions {
		return null;
	} // buscarPor ()

	/**
	 * Rellena la seleccion de dias en el formulario que se muestra en pantalla
	 * 
	 * @param miForm
	 * @param listaPagaGuardia
	 * @param listaNoPagaGuardia
	 */
	private void rellenaSeleccionDiasFormulario(DefinirHitosFacturablesGuardiasForm miForm, String listaPagaGuardia, String listaNoPagaGuardia) {
		miForm.setChPagaGuardiaLunes((listaPagaGuardia.indexOf('L') > -1));
		miForm.setChPagaGuardiaMartes((listaPagaGuardia.indexOf('M') > -1));
		miForm.setChPagaGuardiaMiercoles((listaPagaGuardia.indexOf('X') > -1));
		miForm.setChPagaGuardiaJueves((listaPagaGuardia.indexOf('J') > -1));
		miForm.setChPagaGuardiaViernes((listaPagaGuardia.indexOf('V') > -1));
		miForm.setChPagaGuardiaSabado((listaPagaGuardia.indexOf('S') > -1));
		miForm.setChPagaGuardiaDomingo((listaPagaGuardia.indexOf('D') > -1));

		miForm.setChNoPagaGuardiaLunes((listaNoPagaGuardia.indexOf('L') > -1));
		miForm.setChNoPagaGuardiaMartes((listaNoPagaGuardia.indexOf('M') > -1));
		miForm.setChNoPagaGuardiaMiercoles((listaNoPagaGuardia.indexOf('X') > -1));
		miForm.setChNoPagaGuardiaJueves((listaNoPagaGuardia.indexOf('J') > -1));
		miForm.setChNoPagaGuardiaViernes((listaNoPagaGuardia.indexOf('V') > -1));
		miForm.setChNoPagaGuardiaSabado((listaNoPagaGuardia.indexOf('S') > -1));
		miForm.setChNoPagaGuardiaDomingo((listaNoPagaGuardia.indexOf('D') > -1));
	} // rellenaSeleccionDiasFormulario ()

	/**
	 * Obtiene la seleccion de dias que paga guardia desde el formulario que se
	 * muestra en pantalla
	 * 
	 * @param miForm
	 * @param listaPagaGuardia
	 * @param listaNoPagaGuardia
	 */
	private String obtieneSeleccionDiasPagaGuardiaFormulario(DefinirHitosFacturablesGuardiasForm miForm) {
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
	private String obtieneSeleccionDiasNoPagaGuardiaFormulario(DefinirHitosFacturablesGuardiasForm miForm) {
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