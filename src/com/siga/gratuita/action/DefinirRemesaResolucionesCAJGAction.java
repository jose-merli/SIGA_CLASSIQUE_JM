package com.siga.gratuita.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
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
import org.apache.struts.upload.FormFile;
import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.autogen.model.EcomCola;
import org.redabogacia.sigaservices.app.services.ecom.EcomColaService;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import weblogic.management.timer.Timer;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.GestorContadores;
import com.siga.Utilidades.Paginador;
import com.siga.Utilidades.PaginadorCaseSensitive;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CajgRemesaResolucionAdm;
import com.siga.beans.CajgRemesaResolucionBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefinicionRemesaResolucionesCAJGForm;
import com.siga.gratuita.form.DefinirEJGForm;
import com.siga.ws.CajgConfiguracion;
import com.siga.ws.SIGAWUploadDocResRemListener;
import com.siga.ws.SigaWSUploadDocResRem;
import com.siga.ws.i2055.DesignacionProcuradorAsigna;
import com.siga.ws.i2055.ResolucionesAsigna;

import es.satec.businessManager.BusinessManager;



public class DefinirRemesaResolucionesCAJGAction extends MasterAction {

	private final boolean ELIMINA_DATOS_TABLA_TEMPORAL = true;
	
		
	
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
			
			} else if (accion.equalsIgnoreCase("descargar")) {
				mapDestino = descargar(mapping, miForm, request, response, false);			
			} else if (accion.equalsIgnoreCase("descargarLog")) {
				mapDestino = descargar(mapping, miForm, request, response, true);
			} else if (accion.equalsIgnoreCase("obtenerResoluciones")) {
				mapDestino = obtenerResoluciones(mapping, miForm, request, response);
			} else if (accion.equalsIgnoreCase("obtenerDesignaProcurador")) {
				mapDestino = obtenerDesignaProcurador(mapping, miForm, request, response);				
			}else if (accion.equalsIgnoreCase("editarRemesa")) {
				mapDestino = editarRemesa(mapping, miForm, request, response);				
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
	
	
	
	private String obtenerDesignaProcurador(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		request.getSession().removeAttribute("DATAPAGINADOR");
	
		try {
			DesignacionProcuradorAsigna designacionProcuradorAsigna = new DesignacionProcuradorAsigna();
			designacionProcuradorAsigna.obtenerDesignaciones(getIDInstitucion(request).shortValue());
			String mensaje = "message.remesaDesignaProcurador.asigna.esperando";
			request.setAttribute("mensajeUsuario", UtilidadesString.getMensajeIdioma(getUserBean(request), mensaje));
			
		} catch (Exception e) {
			throwExcp("messages.general.error", e, null);
		}
				
		return buscarPor(mapping, formulario, request, response);
	}
	private String editarRemesa(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		request.getSession().removeAttribute("DATAPAGINADOR");
	
		try {
			DesignacionProcuradorAsigna designacionProcuradorAsigna = new DesignacionProcuradorAsigna();
			designacionProcuradorAsigna.obtenerDesignaciones(getIDInstitucion(request).shortValue());
			String mensaje = "message.remesaDesignaProcurador.asigna.esperando";
			request.setAttribute("mensajeUsuario", UtilidadesString.getMensajeIdioma(getUserBean(request), mensaje));
			
		} catch (Exception e) {
			throwExcp("messages.general.error", e, null);
		}
				
		return buscarPor(mapping, formulario, request, response);
	}
	
	
	private String obtenerResoluciones(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		request.getSession().removeAttribute("DATAPAGINADOR");
	
		try {
						
			int tipoCAJG = CajgConfiguracion.getTipoCAJG(getIDInstitucion(request));
			
			String mensaje = UtilidadesString.getMensajeIdioma(getUserBean(request), "message.remesaResolucion.consultaLanzada");
			
			if (tipoCAJG == CajgConfiguracion.TIPO_CAJG_WEBSERVICE_PAMPLONA) {
				ResolucionesAsigna resolucionesAsigna = new ResolucionesAsigna();
				resolucionesAsigna.obtenerResoluciones(getIDInstitucion(request).toString());
			} else if (tipoCAJG == CajgConfiguracion.TIPO_CAJG_XML_SANTIAGO) {
				EcomColaService ecomColaService = (EcomColaService) BusinessManager.getInstance().getService(EcomColaService.class);				
				EcomCola ecomCola = new EcomCola();
				ecomCola.setIdinstitucion(Short.valueOf(getIDInstitucion(request).toString()));
				ecomCola.setIdoperacion(AppConstants.OPERACION.XUNTA_RESOLUCIONES.getId());
				ecomColaService.insertaColaConsultaResoluciones(ecomCola);				
			} else if (tipoCAJG == CajgConfiguracion.TIPO_CAJG_WEBSERVICE_PAISVASCO) {
				EcomColaService ecomColaService = (EcomColaService) BusinessManager.getInstance().getService(EcomColaService.class);				
				EcomCola ecomCola = new EcomCola();
				ecomCola.setIdinstitucion(Short.valueOf(getIDInstitucion(request).toString()));
				ecomCola.setIdoperacion(AppConstants.OPERACION.GV_RESOLUCIONES.getId());
				ecomColaService.insertaColaConsultaResoluciones(ecomCola);				
			} else if (tipoCAJG == CajgConfiguracion.TIPO_CAJG_CATALANES) {
				EcomColaService ecomColaService = (EcomColaService) BusinessManager.getInstance().getService(EcomColaService.class);				
				EcomCola ecomCola = new EcomCola();
				ecomCola.setIdinstitucion(Short.valueOf(getIDInstitucion(request).toString()));
				ecomCola.setIdoperacion(AppConstants.OPERACION.CAT_RESOLUCIONES_PDF.getId());
				ecomColaService.insertaColaConsultaResoluciones(ecomCola);
			}
			
			
			request.setAttribute("mensajeUsuario", mensaje);
			
		} catch (Exception e) {
			throwExcp("messages.general.error", e, null);
		}
				
		return buscarPor(mapping, formulario, request, response);
	}



	/**
	 * Rellena un hash con los valores recogidos del formulario y realiza la
	 * consulta a partir de esos datos. Almacena un vector con los resultados en
	 * la sesi�n con el nombre "resultado"
	 * 
	 * @param mapping
	 *            Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario
	 *            del que se recoge la informaci�n. De tipo MasterForm.
	 * @param request
	 *            Informaci�n de sesi�n. De tipo HttpServletRequest
	 * @param response
	 *            De tipo HttpServletResponse
	 * 
	 * @return String que indicar� la siguiente acci�n a llevar a cabo.
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		Hashtable miHash = new Hashtable();
		String consulta = "";

		try {

			DefinicionRemesaResolucionesCAJGForm miForm = (DefinicionRemesaResolucionesCAJGForm) formulario;
			
			miHash = miForm.getDatos();

			HashMap databackup = new HashMap();

			if (request.getSession().getAttribute("DATAPAGINADOR") != null) {
				databackup = (HashMap) request.getSession().getAttribute("DATAPAGINADOR");
				Paginador paginador = (Paginador) databackup.get("paginador");
				Vector datos = new Vector();

				// Si no es la primera llamada, obtengo la p�gina del request y
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
				consulta = "select R." + CajgRemesaResolucionBean.C_IDREMESARESOLUCION +
								"," + "R." + CajgRemesaResolucionBean.C_PREFIJO +
								"," + "R." + CajgRemesaResolucionBean.C_NUMERO +
								"," + "R." + CajgRemesaResolucionBean.C_SUFIJO +
								"," + "R." + CajgRemesaResolucionBean.C_NOMBREFICHERO +
								"," + "R." + CajgRemesaResolucionBean.C_OBSERVACIONES +
								"," + "R." + CajgRemesaResolucionBean.C_FECHACARGA +
								"," + "R." + CajgRemesaResolucionBean.C_FECHARESOLUCION +
								"," + "R." + CajgRemesaResolucionBean.C_LOGGENERADO +
								"," + "R." + CajgRemesaResolucionBean.C_IDTIPOREMESA +
								"," + "R.IDREMESA "+
								" FROM " + CajgRemesaResolucionBean.T_NOMBRETABLA + " R" +
								" WHERE R." + CajgRemesaResolucionBean.C_IDINSTITUCION + " = " + this.getIDInstitucion(request) +
								" AND R." + CajgRemesaResolucionBean.C_IDTIPOREMESA + " = " + miForm.getIdTipoRemesa();						
						
				
						
				if ((String) miHash.get(CajgRemesaResolucionBean.C_PREFIJO) != null && (!((String) miHash.get(CajgRemesaResolucionBean.C_PREFIJO)).equals(""))) {					
					consulta += " AND " + ComodinBusquedas.prepararSentenciaNLS(((String) miHash.get(CajgRemesaResolucionBean.C_PREFIJO)).trim(), "r." + CajgRemesaResolucionBean.C_PREFIJO);
				}
				if ((String) miHash.get(CajgRemesaResolucionBean.C_NUMERO) != null && (!((String) miHash.get(CajgRemesaResolucionBean.C_NUMERO)).equals(""))) {					
					consulta += " AND " + ComodinBusquedas.prepararSentenciaNLS(((String) miHash.get(CajgRemesaResolucionBean.C_NUMERO)).trim(), "r." + CajgRemesaResolucionBean.C_NUMERO);
				}
				if ((String) miHash.get(CajgRemesaResolucionBean.C_SUFIJO) != null && (!((String) miHash.get(CajgRemesaResolucionBean.C_SUFIJO)).equals(""))) {
					consulta += " AND " + ComodinBusquedas.prepararSentenciaNLS(((String) miHash.get(CajgRemesaResolucionBean.C_SUFIJO)).trim(), "r." + CajgRemesaResolucionBean.C_SUFIJO);					
				}
				if ((String) miHash.get(CajgRemesaResolucionBean.C_OBSERVACIONES) != null && (!((String) miHash.get(CajgRemesaResolucionBean.C_OBSERVACIONES)).equals(""))) {
					// consulta +=" and r.descripcion like
					// '%"+(String)miHash.get("DESCRIPCION")+"'";
					consulta += " AND " + ComodinBusquedas.prepararSentenciaNLS(((String) miHash.get(CajgRemesaResolucionBean.C_OBSERVACIONES)).trim(), "r." + CajgRemesaResolucionBean.C_OBSERVACIONES);
				}
				
				if ((String) miHash.get(CajgRemesaResolucionBean.C_NOMBREFICHERO) != null && (!((String) miHash.get(CajgRemesaResolucionBean.C_NOMBREFICHERO)).equals(""))) {					
					consulta += " AND " + ComodinBusquedas.prepararSentenciaNLS(((String) miHash.get(CajgRemesaResolucionBean.C_NOMBREFICHERO)).trim(), "r." + CajgRemesaResolucionBean.C_NOMBREFICHERO);
				}
				
				if ((String) miHash.get(CajgRemesaResolucionBean.C_FECHARESOLUCION) != null && (!((String) miHash.get(CajgRemesaResolucionBean.C_FECHARESOLUCION)).equals(""))) {
					consulta += " and R." + CajgRemesaResolucionBean.C_FECHARESOLUCION + " >= '" + (String) miHash.get(CajgRemesaResolucionBean.C_FECHARESOLUCION) + "'";
				}
				
				if ((String) miHash.get(CajgRemesaResolucionBean.C_FECHARESOLUCIONHASTA) != null && (!((String) miHash.get(CajgRemesaResolucionBean.C_FECHARESOLUCIONHASTA)).equals(""))) {
					consulta += " and R." + CajgRemesaResolucionBean.C_FECHARESOLUCION + " <= '" + (String) miHash.get(CajgRemesaResolucionBean.C_FECHARESOLUCIONHASTA) + "'";
				}
				
				if ((String) miHash.get(CajgRemesaResolucionBean.C_FECHACARGA) != null && (!((String) miHash.get(CajgRemesaResolucionBean.C_FECHACARGA)).equals(""))) {
					consulta += " and R." + CajgRemesaResolucionBean.C_FECHACARGA + " >= '" + (String) miHash.get(CajgRemesaResolucionBean.C_FECHACARGA) + "'";
				}
				
				if ((String) miHash.get(CajgRemesaResolucionBean.c_FECHACARGAHASTA) != null && (!((String) miHash.get(CajgRemesaResolucionBean.c_FECHACARGAHASTA)).equals(""))) {
					consulta += " and R." + CajgRemesaResolucionBean.C_FECHACARGA + " <= '" + (String) miHash.get(CajgRemesaResolucionBean.c_FECHACARGAHASTA) + "'";
				}
				
				if (((String) miHash.get("ANIO") != null && !((String) miHash.get("ANIO")).equals(""))
						|| ((String) miHash.get("NUMEJG") != null && !((String) miHash.get("NUMEJG")).equals(""))) {
					consulta += " AND (R.IDINSTITUCION, R.IDREMESARESOLUCION) IN (SELECT ER.IDINSTITUCION, ER.IDREMESARESOLUCION " +
							" FROM CAJG_EJGRESOLUCION ER" +
							" WHERE ER.IDINSTITUCION = R.IDINSTITUCION";
					if ((String) miHash.get("ANIO") != null && !((String) miHash.get("ANIO")).equals("")) {
							consulta += " AND ER.ANIO = " +  miHash.get("ANIO");
					}
					
					if ((String) miHash.get("NUMEJG") != null && !((String) miHash.get("NUMEJG")).equals("")) {
						consulta += " AND ER.NUMEJG = " +  miHash.get("NUMEJG");
					}
					
					consulta += ")";
						
				}
				
				consulta += " ORDER BY R." + CajgRemesaResolucionBean.C_IDREMESARESOLUCION + " DESC";


				PaginadorCaseSensitive paginador = new PaginadorCaseSensitive(consulta);			
				
				
				int totalRegistros = paginador.getNumeroTotalRegistros();

				if (totalRegistros == 0) {
					paginador = null;
				}

				databackup.put("paginador", paginador);
				if (paginador != null) {
					int pagina = 1;
					String paginaSeleccionada = request.getParameter("paginaSeleccionada");
					if (paginaSeleccionada != null && !paginaSeleccionada.trim().equals("")) {
						pagina = Integer.parseInt(paginaSeleccionada.trim());
					}
					datos = paginador.obtenerPagina(pagina);
					databackup.put("datos", datos);
					request.getSession().setAttribute("DATAPAGINADOR", databackup);					
				}

			}

			request.getSession().setAttribute("DATOSFORMULARIO", miHash);
			
			
		} catch (Exception e) {
			throwExcp("messages.general.error", e, null);
		}
		return "listarResoluciones";
	}

	/**
	 * Rellena un hash con los valores recogidos del formulario, almacenando
	 * esta hash en la sesi�n con el nombre "elegido"
	 * 
	 * @param mapping
	 *            Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario
	 *            del que se recoge la informaci�n. De tipo MasterForm.
	 * @param request
	 *            Informaci�n de sesi�n. De tipo HttpServletRequest
	 * @param response
	 *            De tipo HttpServletResponse
	 * 
	 * @return String que indicar� la siguiente acci�n a llevar a cabo.
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		try {
			DefinicionRemesaResolucionesCAJGForm miForm = (DefinicionRemesaResolucionesCAJGForm) formulario;

			// Recuperamos los datos del registro que hemos seleccionado
			Vector ocultos = miForm.getDatosTablaOcultos(0);
			Vector visibles = miForm.getDatosTablaVisibles(0);
			Hashtable miHash = new Hashtable();

			if ((ocultos != null && visibles != null)) {
				miHash.put(CajgRemesaResolucionBean.C_IDREMESARESOLUCION, ocultos.get(0));
				miHash.put(CajgRemesaResolucionBean.C_IDINSTITUCION, this.getIDInstitucion(request).toString());
				
			} else {
				
				miHash.put(CajgRemesaResolucionBean.C_IDREMESARESOLUCION, miForm.getIdRemesaResolucion());
				miHash.put(CajgRemesaResolucionBean.C_IDINSTITUCION, this.getIDInstitucion(request).toString());
				miHash.put(CajgRemesaResolucionBean.C_IDTIPOREMESA, miForm.getIdTipoRemesa());
			}
			CajgRemesaResolucionAdm remesaAdm = new CajgRemesaResolucionAdm(this.getUserBean(request));
			CajgRemesaResolucionBean b = new CajgRemesaResolucionBean();
			Vector a = remesaAdm.selectByPK(miHash);
			b = (CajgRemesaResolucionBean) a.get(0);
			Hashtable h = b.getOriginalHash();
			
			request.setAttribute("modo", "editar");

			request.setAttribute("REMESARESOLUCION", h);
		} catch (Exception e) {
			throwExcp("messages.general.error", e, null);
		}
		return "editar";
	}

	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		try {
			DefinicionRemesaResolucionesCAJGForm miForm = (DefinicionRemesaResolucionesCAJGForm) formulario;

			// Recuperamos los datos del registro que hemos seleccionado
			Vector ocultos = miForm.getDatosTablaOcultos(0);
			Vector visibles = miForm.getDatosTablaVisibles(0);
			Hashtable miHash = new Hashtable();
			miHash.put(CajgRemesaResolucionBean.C_IDINSTITUCION, this.getIDInstitucion(request).toString());
			
			if ((ocultos != null && visibles != null)) {
				miHash.put(CajgRemesaResolucionBean.C_IDREMESARESOLUCION, ocultos.get(0));
			} else {				
				miHash.put(CajgRemesaResolucionBean.C_IDREMESARESOLUCION, miForm.getIdRemesaResolucion());
			}
			
			CajgRemesaResolucionAdm RemesaAdm = new CajgRemesaResolucionAdm(this.getUserBean(request));
			CajgRemesaResolucionBean b = new CajgRemesaResolucionBean();
			Vector a = RemesaAdm.selectByPK(miHash);
			b = (CajgRemesaResolucionBean) a.get(0);
			Hashtable h = b.getOriginalHash();
			
			request.setAttribute("modo", "consultar");

			request.setAttribute("REMESARESOLUCION", h);
		} catch (Exception e) {
			throwExcp("messages.general.error", e, null);
		}
		return "editar";
	}

	/**
	 * Rellena el string que indica la acci�n a llevar a cabo con "
	 *  . .Fiesta" para que redirija a la pantalla de inserci�n.
	 * 
	 * @param mapping
	 *            Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario
	 *            del que se recoge la informaci�n. De tipo MasterForm.
	 * @param request
	 *            Informaci�n de sesi�n. De tipo HttpServletRequest
	 * @param response
	 *            De tipo HttpServletResponse
	 * 
	 * @return String que indicar� la siguiente acci�n a llevar a cabo.
	 * @throws ClsExceptions 
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException, ClsExceptions {
		// si el usuario logado es letrado consultar en BBDD el nColegiado para
		// mostrar en la jsp
		UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");

		DefinicionRemesaResolucionesCAJGForm miForm = (DefinicionRemesaResolucionesCAJGForm) formulario;

		Integer idinstitucion = new Integer(usr.getLocation());
		GestorContadores gcRemesa = new GestorContadores(this.getUserBean(request));

		Hashtable contadorTablaHashRemesa = gcRemesa.getContador(idinstitucion,	getIdContador(usr, getIDInstitucion(request).toString(), miForm.getIdTipoRemesa()));

		String siguiente = gcRemesa.getNuevoContador(contadorTablaHashRemesa);

		String prefijo = UtilidadesHash.getString(contadorTablaHashRemesa,"PREFIJO");
		String sufijo = UtilidadesHash.getString(contadorTablaHashRemesa,"SUFIJO");
		String modocontador = contadorTablaHashRemesa.get("MODO").toString();

		miForm.setNumero(siguiente);
		miForm.setPrefijo(prefijo);
		miForm.setSufijo(sufijo);

		request.setAttribute("modoContador", modocontador);			

		
		return "insertarResolucion";
	}

	/**
	 * 
	 * @param usrBean
	 * @param idInstitucion
	 * @param idTipoRemesa
	 * @return
	 * @throws ClsExceptions
	 */
	private String getIdContador(UsrBean usrBean, String idInstitucion, String idTipoRemesa) throws ClsExceptions {
		CajgRemesaResolucionAdm cajgRemesaResolucionAdm = new CajgRemesaResolucionAdm(usrBean);
		String idContador = cajgRemesaResolucionAdm.getIdContador(idInstitucion, idTipoRemesa);
		if (idContador == null || idContador.trim().equals("")) {
			throw new ClsExceptions("No se ha encontrado el contador para el tipo remesa " + idTipoRemesa);
		}
		return idContador;
	}

	/**
	 * Rellena un hash con los valores recogidos del formulario y los inserta en
	 * la base de datos.
	 * 
	 * @param mapping
	 *            Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario
	 *            del que se recoge la informaci�n. De tipo MasterForm.
	 * @param request
	 *            Informaci�n de sesi�n. De tipo HttpServletRequest
	 * @param response
	 *            De tipo HttpServletResponse
	 * 
	 * @return String que indicar� la siguiente acci�n a llevar a cabo.
	 */
	protected synchronized String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {

		UserTransaction tx = null;
		String mensaje = "";

		try {
			
			
			CajgRemesaResolucionBean cajgRemesaResolucionBean = new CajgRemesaResolucionBean();
			HttpSession session = request.getSession();
			
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			DefinicionRemesaResolucionesCAJGForm miForm = (DefinicionRemesaResolucionesCAJGForm) formulario;
			String idInstitucion = miForm.getIdInstitucion();
			
			
			
			CajgRemesaResolucionAdm resolucionAdm = new CajgRemesaResolucionAdm(this.getUserBean(request));

			FormFile formFile = miForm.getFile();
			if (formFile.getFileSize() == 0){
			
				throw new SIGAException("message.cajg.ficheroValido"); 
			}
			//Si es remesa de resultado tiene que ser un txt
			String nombreFile = formFile.getFileName();
			String extension = nombreFile.substring(nombreFile.lastIndexOf(".")+1,nombreFile.length());
			
			/*if (miForm.getIdTipoRemesa().equals("3") && !extension.equals("txt")){
				String mensajeString = UtilidadesString.getMensajeIdioma(usr,"messages.ficheros.tipoFicheroErroneo");
				
				throw new SIGAException(mensajeString+ " txt"); 
			}
			*/
			
			//Se comprueba el n�mero de l�neas del fichero
			InputStream fis =formFile.getInputStream();
			InputStreamReader isr = new InputStreamReader(fis, "ISO-8859-15"); 
			BufferedReader br = new BufferedReader(isr);

			int numLineas = 0;

			String linea="";
			while(br.ready()) {  
                linea = br.readLine();  
                if(linea!=null){
                	numLineas++;
				}
            } 
			
			br.close();
			isr.close();
			fis.close();
			
			GenParametrosAdm admParametros = new GenParametrosAdm(usr);
			int maxNumLineas=Integer.parseInt(admParametros.getValor("0","SCS", "MAX_NUM_LINEAS_FICH","10000"));
			
			SigaWSUploadDocResRem obj = new SigaWSUploadDocResRem();
				
			obj.setFechaResolucion(miForm.getFechaResolucion());
			obj.setFechaCarga(miForm.getFechaCarga());
			obj.setIdTipoRemesa(Integer.parseInt(miForm.getIdTipoRemesa()));
			obj.setFile(miForm.getFile()); 
			obj.setUsrBean(usr);
			obj.setIdInstitucion(Integer.parseInt(idInstitucion));
			obj.setPrefijo(miForm.getPrefijo());
			obj.setSufijo(miForm.getSufijo());
			obj.setNumero(miForm.getNumero());
			
			String idRemesaResolucion = resolucionAdm.seleccionarMaximo(String.valueOf(idInstitucion));
			obj.setIdRemesaResolucion(idRemesaResolucion);

			if(numLineas>maxNumLineas){

				//Se ejecuta en background
				obj.setTipoEjecucion("background");
				SIGAWUploadDocResRemListener sigaWSListener = new SIGAWUploadDocResRemListener();
				Timer timer = new Timer();
				timer.addNotificationListener(sigaWSListener, null, timer);
				Integer idNotificacion = timer.addNotification("WSType", "WSMessage", obj, new Date(), 0);
				sigaWSListener.setIdNotificacion(idNotificacion);		
				timer.start();

				mensaje = (UtilidadesString.getMensajeIdioma(usr, "messages.fichero.supera.nummax.lineas"));

			}else{
				
				//Se ejecuta en online
				obj.setTipoEjecucion("online");
				obj.execute();
				
				if((obj.getMensajeOut()==null)||(obj.getMensajeOut().isEmpty()))
					mensaje = UtilidadesString.getMensajeIdioma(usr,"messages.inserted.success");
				else
					mensaje =obj.getMensajeOut();

			}
			
			session.setAttribute("accion", "editar");
			request.setAttribute(CajgRemesaResolucionBean.C_IDREMESARESOLUCION, idRemesaResolucion);
			request.setAttribute(CajgRemesaResolucionBean.C_IDINSTITUCION, this.getIDInstitucion(request).toString());
				
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita" }, e, tx);
		}
		String resultadoFinal[] = new String[1];
		resultadoFinal[0] =mensaje;
		request.setAttribute("parametrosArray",resultadoFinal);
		request.setAttribute("modal", "");
		return "exitoParametros";
	}


	/**
	 * 
	 * @param parentFile
	 */
	private static void deleteFiles(File parentFile) {
		if (parentFile != null) {
			File[] files = parentFile.listFiles();
			if (files != null) {
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory()) {
						deleteFiles(files[i]);
					}
					files[i].delete();
				}		
			}
		}
	}

	public Charset detectCodepage(InputStream in) throws IOException {
    	int len = 1;
        byte[] bom = new byte[len]; // Get the byte-order mark, if there is one
        in.read(bom, 0, len);
        // Unicode formats => read BOM
        System.out.println(bom[0]==Byte.valueOf("-1"));
        byte b = (byte)0xEF;
        in.reset();
        if (bom[0] == (byte)0x00 && bom[1] == (byte)0x00 && bom[2] == (byte)0xFE
                && bom[2] == (byte)0xFF) // utf-32BE
            return Charset.forName("UTF-32BE");
        if (bom[0] == (byte)0xFF && bom[1] == (byte)0xFE && bom[2] == (byte)0x00
                && bom[2] == (byte)0x00) // utf-32BE
            return Charset.forName("UTF-32LE");
        if (bom[0] == (byte)0xEF && bom[1] == (byte)0xBB && bom[2] == (byte)0xBF) // utf-8
            return Charset.forName("UTF-8");
        if (bom[0] == (byte)0xff && bom[1] == (byte)0xfe) // ucs-2le, ucs-4le, and ucs-16le
            return Charset.forName("UTF-16LE");
        if (bom[0] == (byte)0xfe && bom[1] == (byte)0xff) // utf-16 and ucs-2
            return Charset.forName("UTF-16BE");
        if (bom[0] == (byte)0 && bom[1] == (byte)0 && bom[2] == (byte)0xfe && bom[3] == (byte)0xff) // ucs-4
            return Charset.forName("UCS-4");
        return null;
    }

	/**
	 * Rellena un hash con los valores recogidos del formulario y los modifica
	 * en la base de datos.
	 * 
	 * @param mapping
	 *            Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario
	 *            del que se recoge la informaci�n. De tipo MasterForm.
	 * @param request
	 *            Informaci�n de sesi�n. De tipo HttpServletRequest
	 * @param response
	 *            De tipo HttpServletResponse
	 * 
	 * @return String que indicar� la siguiente acci�n a llevar a cabo.
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		UserTransaction tx = null;
		try {

			Hashtable miHash = new Hashtable();
			
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			DefinicionRemesaResolucionesCAJGForm miForm = (DefinicionRemesaResolucionesCAJGForm) formulario;			

			CajgRemesaResolucionAdm remesaAdm = new CajgRemesaResolucionAdm(this.getUserBean(request));

			miHash.put(CajgRemesaResolucionBean.C_IDINSTITUCION, this.getIDInstitucion(request));
			miHash.put(CajgRemesaResolucionBean.C_IDREMESARESOLUCION, miForm.getIdRemesaResolucion());
			
			miHash.put(CajgRemesaResolucionBean.C_PREFIJO, miForm.getPrefijo());
			miHash.put(CajgRemesaResolucionBean.C_NUMERO, miForm.getNumero());
			miHash.put(CajgRemesaResolucionBean.C_SUFIJO, miForm.getSufijo());			
			
			miHash.put(CajgRemesaResolucionBean.C_NOMBREFICHERO, miForm.getNombreFichero());
			miHash.put(CajgRemesaResolucionBean.C_OBSERVACIONES, miForm.getObservaciones());			
			miHash.put(CajgRemesaResolucionBean.C_IDTIPOREMESA, miForm.getIdTipoRemesa());
			miHash.put(CajgRemesaResolucionBean.C_FECHACARGA, GstDate.getApplicationFormatDate("", miForm.getFechaCarga()));
			miHash.put(CajgRemesaResolucionBean.C_FECHARESOLUCION, GstDate.getApplicationFormatDate("", miForm.getFechaResolucion()));
			miHash.put(CajgRemesaResolucionBean.C_LOGGENERADO, miForm.getLogGenerado());
			
			tx = usr.getTransaction();
			tx.begin();
			remesaAdm.updateDirect(miHash, null, null);
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
	 *            del que se recoge la informaci�n. De tipo MasterForm.
	 * @param request
	 *            Informaci�n de sesi�n. De tipo HttpServletRequest
	 * @param response
	 *            De tipo HttpServletResponse
	 * 
	 * @return String que indicar� la siguiente acci�n a llevar a cabo.
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return exitoRefresco("messages.deleted.success", request);
	}

		
	public static File getFichero(String idInstitucion, String idRemesaResolucion, boolean log) {
		File file = null;
	    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//		ReadProperties rp = new ReadProperties("SIGA.properties");
		String rutaAlmacen = rp.returnProperty("cajg.directorioFisicoCAJG") + rp.returnProperty("cajg.directorioCAJGJava");
			
		rutaAlmacen += File.separator + idInstitucion + File.separator + "remesaResoluciones";		
		rutaAlmacen += File.separator + idRemesaResolucion;
		
		if (log) {
			rutaAlmacen += File.separator + "log";
		}
		
		File dir = new File(rutaAlmacen);
		if (dir.exists()) {
			if (dir.listFiles() != null && dir.listFiles().length > 0) {
				int numFicheros = 0;
				for (int i = 0; i < dir.listFiles().length ; i++) {
					if (dir.listFiles()[i].isFile()){
						file = dir.listFiles()[i];
						numFicheros++;
					}
				}
									
				if (numFicheros > 1) {
//					throw new SIGAException("Existe m�s de un fichero zip");
				}
			}
		}
		
		return file;
	}

	/**
	 * No implementado
	 */
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		return buscarPor(mapping, formulario, request, response);		
	}

	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		try {
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
		
			int tipoCAJG = CajgConfiguracion.getTipoCAJG(getIDInstitucion(request));
			request.setAttribute("pcajgActivo", tipoCAJG);
		}catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}					
		
		  
		return "inicio";
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


	private String descargar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response, boolean log) throws ClsExceptions,
			SIGAException {

		
		DefinicionRemesaResolucionesCAJGForm miForm = (DefinicionRemesaResolucionesCAJGForm) formulario;			
		File file = getFichero(getIDInstitucion(request).toString(), miForm.getIdRemesaResolucion(), log);

		if (file == null) {								
			throw new SIGAException("messages.general.error.ficheroNoExiste");
		}				
		
		request.setAttribute("nombreFichero", file.getName());
		request.setAttribute("rutaFichero", file.getAbsolutePath());

		return "descargaFichero";
	}
	
}