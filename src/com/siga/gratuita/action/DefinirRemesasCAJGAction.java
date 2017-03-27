//Clase: DefinirExpedientesSOJAction 
//Autor: julio.vicente@atosorigin.com
//Ultima modificación: 14/02/2005

package com.siga.gratuita.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.AppConstants.ESTADOS_EJG;
import org.redabogacia.sigaservices.app.AppConstants.GEN_RECURSOS;
import org.redabogacia.sigaservices.app.AppConstants.MODULO;
import org.redabogacia.sigaservices.app.AppConstants.OPERACION;
import org.redabogacia.sigaservices.app.AppConstants.PARAMETRO;
import org.redabogacia.sigaservices.app.autogen.model.CajgRemesa;
import org.redabogacia.sigaservices.app.autogen.model.EcomCola;
import org.redabogacia.sigaservices.app.exceptions.BusinessException;
import org.redabogacia.sigaservices.app.helper.SIGAServicesHelper;
import org.redabogacia.sigaservices.app.services.caj.CajgRemesaService;
import org.redabogacia.sigaservices.app.services.caj.PCAJGInsertaColaService;
import org.redabogacia.sigaservices.app.services.ecom.EcomColaService;
import org.redabogacia.sigaservices.app.services.ecom.EcomColaService.RESPUESTA_ENVIO_REMESA;
import org.redabogacia.sigaservices.app.services.gen.GenParametrosService;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;
import org.redabogacia.sigaservices.app.vo.scs.CajgEjgRemesaVo;
import org.redabogacia.sigaservices.app.vo.scs.CajgRemesaVo;

import weblogic.management.timer.Timer;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ClsMngBBDD;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.GestorContadores;
import com.siga.Utilidades.Paginador;
import com.siga.Utilidades.PaginadorBind;
import com.siga.Utilidades.PaginadorCaseSensitive;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
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
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.ScsEJGAdm;
import com.siga.beans.ScsEJGBean;
import com.siga.beans.ScsEstadoEJGAdm;
import com.siga.beans.ScsEstadoEJGBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefinicionRemesas_CAJG_Form;
import com.siga.gratuita.form.DefinirEJGForm;
import com.siga.informes.MasterWords;
import com.siga.ws.CajgConfiguracion;
import com.siga.ws.SIGAWSClientAbstract;
import com.siga.ws.SIGAWSListener;

import es.satec.businessManager.BusinessManager;




/**
* Maneja las acciones que se pueden realizar sobre la tabla SCS_EJG
*/
public class DefinirRemesasCAJGAction extends MasterAction {
	final String[] clavesBusqueda={ScsEJGBean.C_IDINSTITUCION,ScsEJGBean.C_IDTIPOEJG,ScsEJGBean.C_ANIO
			,ScsEJGBean.C_NUMERO,"IDEJGREMESA","NUMEROINTERCAMBIO","PERMITIRSOLINFECONOMICO"};
	private static final Logger log = Logger.getLogger(DefinirRemesasCAJGAction.class);
	
	private static final String VALUE_TRUE = "1";

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
				
				
			}else if (accion.equalsIgnoreCase("buscarPorEJGInit")) {
				mapDestino = buscarPorEJGInit(mapping, miForm, request, response);
				
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
			} else if (accion.equalsIgnoreCase("validarRemesa")) {
				mapDestino = validarRemesa(mapping, miForm, request, response);
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
			} else if (accion.equalsIgnoreCase("generaXML")) {
				mapDestino = generaXML(mapping, miForm, request, response);			
			} else if (accion.equalsIgnoreCase("comunicarInfEconomico")) {
				mapDestino = comunicarInfEconomico(mapping, miForm, request, response);		
			
			} else if (accion.equalsIgnoreCase("marcarRespuestaIncorrectaManual")) {
				mapDestino = marcarRespuestaIncorrectaManual(mapping, miForm, request, response);		
			
			}else if (accion.equalsIgnoreCase("borrarOviedoTemporal")) {
				mapDestino = borrarOviedoTemporal(mapping, miForm, request, response);		
			
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
		UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
		CajgRemesaAdm cajgRemesaAdm = new CajgRemesaAdm(usr);
		try {
			
			boolean isDatosEconomicos = false;
			GenParametrosService genParametrosService = (GenParametrosService) BusinessManager.getInstance().getService(GenParametrosService.class);
			//Niramos si esta configurada la url de envio de informe economico
			if(usr.getLocation().equals("2003")){
				String urlEnvioInformeEconomico = genParametrosService.getValorParametroWithNull((short)2003,PARAMETRO.INFORMEECONOMICO_WS_URL,MODULO.ECOM);
				isDatosEconomicos = urlEnvioInformeEconomico!=null && !urlEnvioInformeEconomico.equalsIgnoreCase(""); 
				request.setAttribute("ISDATOSECONOMICOS", Boolean.valueOf(isDatosEconomicos) );
			}

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
				
				actualizarPagina( datos,isDatosEconomicos,usr);
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
						

				// Rellena un vector de Hastable con la claves primarias de la
				// tabla
				// scs_ejg para llevar el control de los check

				PaginadorCaseSensitive paginador = cajgRemesaAdm.getPaginadorRemesas(miForm);
				int totalRegistros = paginador.getNumeroTotalRegistros();

				if (totalRegistros == 0) {
					paginador = null;
				}

				databackup.put("paginador", paginador);
				if (paginador != null) {
					datos = paginador.obtenerPagina(1);
					databackup.put("datos", datos);
					actualizarPagina(datos,isDatosEconomicos, usr);
					request.getSession().setAttribute("DATAPAGINADOR", databackup);
					request.getSession().setAttribute("HORABUSQUEDA", UtilidadesBDAdm.getFechaCompletaBD("es"));
				}

				// resultado = admBean.selectGenerico(consulta);
				// request.getSession().setAttribute("resultado",v);

			}
			// En "DATOSBUSQUEDA" almacenamos el identificador del letrado
			miHash.put("BUSQUEDAREALIZADA", "1");
			request.setAttribute("ISDATOSECONOMICOS", Boolean.valueOf(isDatosEconomicos) );
			request.getSession().setAttribute("DATOSBUSQUEDA", miHash);

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
			boolean isDatosEconomicos = false;
			GenParametrosService genParametrosService = (GenParametrosService) BusinessManager.getInstance().getService(GenParametrosService.class);
			//Niramos si esta configurada la url de envio de informe economico
			if(this.getUserBean(request).getLocation().equals("2003")){
				String urlEnvioInformeEconomico = genParametrosService.getValorParametroWithNull((short)2003,PARAMETRO.INFORMEECONOMICO_WS_URL,MODULO.ECOM);
				isDatosEconomicos = urlEnvioInformeEconomico!=null && !urlEnvioInformeEconomico.equalsIgnoreCase(""); 
				
			}
			request.setAttribute("ISDATOSECONOMICOS", Boolean.valueOf(isDatosEconomicos) );
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
			String idRemesa = UtilidadesHash.getString(ocultos, "IDREMESA");
			Integer idInstitucion = this.getIDInstitucion(request);

			CajgRemesaAdm remesaAdm = new CajgRemesaAdm(this.getUserBean(request));

			miHash.put(CajgRemesaBean.C_IDINSTITUCION, idInstitucion);
			miHash.put(CajgRemesaBean.C_IDREMESA, idRemesa);
			Vector v = remesaAdm.selectByPK(miHash);
			
			if (v != null && v.size() == 1) {
				CajgRemesaBean cajgRemesaBean = (CajgRemesaBean)v.get(0);
				
				cajgRemesaBean.setDescripcion(miForm.getDescripcion());
				
				FormFile formFile = miForm.getFile();
				if (formFile != null && formFile.getFileSize() > 0) {					 
					
					EcomColaService ecomColaService = (EcomColaService) BusinessManager.getInstance().getService(EcomColaService.class);
					EcomCola ecomCola = new EcomCola();
					ecomCola.setIdinstitucion(Short.valueOf(idInstitucion.toString()));
					ecomCola.setIdoperacion(AppConstants.OPERACION.XUNTA_FICHERO_RESPUESTA.getId());
					ecomColaService.insert(ecomCola);
					cajgRemesaBean.setIdecomcola(ecomCola.getIdecomcola());
					log.debug("Se ha insertado en la cola correctamente para el colegio " + idInstitucion);
					
					File parentFile = SIGAServicesHelper.getDirectorioRespuesta(Short.valueOf(idInstitucion.toString()), Long.valueOf(idRemesa), ecomCola.getIdecomcola());					
					File file = new File(parentFile, formFile.getFileName());
					log.debug("La ruta donde se va a guardar el fichero es: " + parentFile.getAbsolutePath());
					OutputStream os = new FileOutputStream(file);
					InputStream is = formFile.getInputStream();
					byte[] bytes = new byte[is.available()];
					is.read(bytes);					
					os.write(bytes);
					os.flush();
					os.close();
					is.close();
					log.debug("El fichero se ha copiado a la ruta correctamente. Ruta del fichero: " + parentFile.getAbsolutePath());
				}
				
				tx = usr.getTransaction();
				tx.begin();
				remesaAdm.updateDirect(cajgRemesaBean);
				tx.commit();

				session.setAttribute("accion", "editar");
				request.setAttribute("IDREMESA", miHash.get(CajgRemesaBean.C_IDREMESA));
				request.setAttribute("INSTITUCION", this.getIDInstitucion(request).toString());

			} else {				
				throw new Exception("No se ha encontrado la remesa " + idRemesa + " de la institución " + idInstitucion);
			}
			
			
		} catch (Exception e) {
			log.error("Error al modificar la remesa", e);
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

			String idEjgRemesa = (String)ocultos.get(4);
			miHash.put(CajgEJGRemesaBean.C_IDEJGREMESA, idEjgRemesa);
			
			tx = usr.getTransaction();
			tx.begin();
			cajgRespuestaEJGRemesaAdm.deleteDirect(miHash, new String[]{CajgRespuestaEJGRemesaBean.C_IDEJGREMESA});
			admBean.delete(miHash);			

			Hashtable hashEstado = new Hashtable();
			hashEstado.put(ScsEJGBean.C_IDINSTITUCION, getIDInstitucion(request));
			hashEstado.put(ScsEJGBean.C_ANIO,ocultos.get(2));
			hashEstado.put(ScsEJGBean.C_NUMERO, ocultos.get(3));
			hashEstado.put(ScsEJGBean.C_IDTIPOEJG, ocultos.get(0));

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

	
	protected String borrarOviedoTemporal(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");

		DefinicionRemesas_CAJG_Form miForm = (DefinicionRemesas_CAJG_Form) formulario;
		
		try {

			BusinessManager bm = getBusinessManager();
			String idRemesa =  miForm.getIdRemesa();
			String idInstitucion = this.getIDInstitucion(request).toString();
			CajgRemesaService cajgRemesaService = (CajgRemesaService)bm.getService(CajgRemesaService.class);
			CajgRemesa cajgRemesa = new CajgRemesa();
			cajgRemesa.setIdinstitucion(Short.valueOf(idInstitucion));
			cajgRemesa.setIdremesa(Long.valueOf(idRemesa));
			cajgRemesaService.borrarOviedoTemporal(cajgRemesa);
		} catch (Exception e) {
			throw new SIGAException("No se puede borrar la remesa"+e.toString(),e);
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
				CajgRespuestaEJGRemesaAdm respuestaEJGRemesaAdm = new CajgRespuestaEJGRemesaAdm(this.getUserBean(request));
				respuestaEJGRemesaAdm.eliminaAnterioresErrores(Integer.parseInt(idInstitucion), Integer.parseInt(miForm.getIdRemesa()));				
				admRemesa.delete(hashRemesa);

				eliminaFicheroTXTGenerado(idInstitucion, miForm.getIdRemesa(),idInstitucion.equals("2003"),usr);

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

		if (idanterior.get(ScsEstadoEJGBean.C_IDESTADOEJG) != null) {
			
			String estadoEJG = idanterior.get(ScsEstadoEJGBean.C_IDESTADOEJG).toString(); 
			
			if (estadoEJG.equals(String.valueOf(ESTADOS_EJG.GENERADO_EN_REMESA.getCodigo()))) {
				Hashtable hashEstado = new Hashtable();
				hashEstado.put(ScsEJGBean.C_IDINSTITUCION, hashtable.get(ScsEJGBean.C_IDINSTITUCION));
				hashEstado.put(ScsEJGBean.C_ANIO, hashtable.get(ScsEJGBean.C_ANIO));
				hashEstado.put(ScsEJGBean.C_NUMERO, hashtable.get(ScsEJGBean.C_NUMERO));
				hashEstado.put(ScsEJGBean.C_IDTIPOEJG, hashtable.get(ScsEJGBean.C_IDTIPOEJG));
				hashEstado.put(ScsEstadoEJGBean.C_IDESTADOPOREJG, idanterior.get(ScsEstadoEJGBean.C_IDESTADOPOREJG));
				beanEstado.delete(hashEstado);			
			}
		}

	}

	/**
	 * 
	 * @param request
	 * @param idRemesa
	 * @return
	 */
	public static boolean eliminaFicheroTXTGenerado(String idInstitucion, String idRemesa, boolean isBorrarHistorico,UsrBean usrBean) {
		//como esta regenrando el fichero a enviar, eliminamos tambien  los registro en las tablas 'foto enviada'		
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
		
		if(numFicheros>0 && isBorrarHistorico){
			CajgEJGRemesaAdm cajEjgRemesaAdm = new CajgEJGRemesaAdm(usrBean);
			cajEjgRemesaAdm.borraHistoricoRemesa(new Long(idRemesa), Short.valueOf(idInstitucion));
		}

		return numFicheros>0;
	}
	
	public static File getFicheroXML(String idInstitucion, String idRemesa) throws SIGAException {
		return new File(SIGAWSClientAbstract.getRutaFicheroZIP(Integer.parseInt(idInstitucion), Integer.parseInt(idRemesa)));
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
		
		
		DefinicionRemesas_CAJG_Form miForm = (DefinicionRemesas_CAJG_Form) formulario;
		
		String volver = request.getParameter("volver");
		if (volver != null && volver.equalsIgnoreCase("SI")) {
			request.setAttribute("VOLVER", "1");			
		} else {
			request.setAttribute("VOLVER", "0");
			miForm.setAnioEJG("");
			miForm.setCodigoEJG("");
			request.getSession().removeAttribute("DATOSBUSQUEDA");
		}
		request.getSession().removeAttribute("DATABACKUP");
		request.getSession().removeAttribute("DATOSFORMULARIO");		
		request.getSession().removeAttribute("DATABACKUP");
		request.getSession().removeAttribute("accion");
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
			aux2.put("SELECCIONADO", VALUE_TRUE);
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
					h.put("SELECCIONADO", VALUE_TRUE);
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
						" SELECT EJG.IDINSTITUCION, EJG.IDTIPOEJG, EJG.ANIO, EJG.NUMERO, '" + ESTADOS_EJG.GENERADO_EN_REMESA.getCodigo() + "'" +
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
					if (seleccionado.equals(VALUE_TRUE)) {
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

	protected String buscarPorEJG(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws ClsExceptions,SIGAException  {
		
		ScsEJGAdm admBean =new ScsEJGAdm(this.getUserBean(request));
		Hashtable miHash = new Hashtable();
		String consulta = "";

		try {
			String longitudNumEjg = (String) request.getSession().getAttribute(PARAMETRO.LONGITUD_CODEJG.toString());
			DefinicionRemesas_CAJG_Form miForm = (DefinicionRemesas_CAJG_Form) formulario;			
			miHash = miForm.getDatos();
			String idremesa = miForm.getIdRemesa();
			String idinstitucion = this.getIDInstitucion(request).toString();
			request.setAttribute("idremesa", idremesa);
			boolean isDatosEconomicos = false;
			GenParametrosService genParametrosService = (GenParametrosService) BusinessManager.getInstance().getService(GenParametrosService.class);
			//Niramos si esta configurada la url de envio de informe economico
			if(idinstitucion.equals("2003")){
				String urlEnvioInformeEconomico = genParametrosService.getValorParametroWithNull((short)2003,PARAMETRO.INFORMEECONOMICO_WS_URL,MODULO.ECOM);
				isDatosEconomicos = urlEnvioInformeEconomico!=null && !urlEnvioInformeEconomico.equalsIgnoreCase(""); 
				
			}
			request.setAttribute("ISDATOSECONOMICOS", Boolean.valueOf(isDatosEconomicos) );
			
			

			//Si es seleccionar todos esta variable no vandra nula y ademas nos traera el numero de pagina 
			//donde nos han marcado el seleccionar todos(asi evitamos meter otra variable)
			boolean isSeleccionarTodos = miForm.getSeleccionarTodos()!=null 
				&& !miForm.getSeleccionarTodos().equals("");
			//si no es seleccionar todos los cambios van a afectar a los datos que se han mostrado en 
			//la jsp por lo que parseamos los datos dento dela variable Registro seleccionados. Cuando hay modificacion
			//habra que actualizar estos datos
			if(!isSeleccionarTodos){
				ArrayList clavesRegSeleccinados = (ArrayList) miForm.getRegistrosSeleccionados();
				String seleccionados = request.getParameter("Seleccion");
				
				
				if (seleccionados != null ) {
					ArrayList alRegistros = actualizarSelecionados(this.clavesBusqueda,seleccionados, clavesRegSeleccinados);
					if (alRegistros != null) {
						clavesRegSeleccinados = alRegistros;
						miForm.setRegistrosSeleccionados(clavesRegSeleccinados);
					}
				}
			}
			
			Hashtable busquedaRealizada =  (Hashtable)request.getSession().getAttribute("DATOSFORMULARIO");
			
			String idIncidenciasEnvioAnterior = busquedaRealizada!=null && busquedaRealizada.get("idIncidenciasEnvio")!=null?(String)busquedaRealizada.get("idIncidenciasEnvio"):"";
			HashMap databackup = null;
			if (miForm.getIdIncidenciasEnvio()!=null && idIncidenciasEnvioAnterior.equalsIgnoreCase(miForm.getIdIncidenciasEnvio()))
				databackup =  (HashMap) miForm.getDatosPaginador();
			
			
			if (databackup!=null && databackup.get("paginador")!=null&&!isSeleccionarTodos){
				com.siga.Utilidades.paginadores.PaginadorBind paginador = (com.siga.Utilidades.paginadores.PaginadorBind)databackup.get("paginador");
				Vector datos=new Vector();

				//Si no es la primera llamada, obtengo la página del request y la busco con el paginador
				String pagina = (String)request.getParameter("pagina");



				if (paginador!=null){	
					if (pagina!=null){
						datos = paginador.obtenerPagina(Integer.parseInt(pagina));
					}else{// cuando hemos editado un registro de la busqueda y volvemos a la paginacion
						datos = paginador.obtenerPagina((paginador.getPaginaActual()));
					}
				}	
				// jbd //
//				actualizarPagina(request, admBean, datos);
				databackup.put("paginador",paginador);
				databackup.put("datos",datos);




			}else{	

				databackup=new HashMap();

				//obtengo datos de la consulta 			
				com.siga.Utilidades.paginadores.PaginadorBind resultado = null;
				resultado = admBean.getPaginadorEJGRemesas(miHash, miForm,miForm.getIdInstitucion(),longitudNumEjg,isDatosEconomicos);
//				resultado=desigAdm.getBusquedaDesigna((String)usr.getLocation(),miHash);
				Vector datos = null;



				databackup.put("paginador",resultado);
				
				if (resultado!=null && resultado.getNumeroTotalRegistros()>0){ 
					
					
					if(isSeleccionarTodos){
						//Si hay que seleccionar todos hacemos la query completa.
						ArrayList clavesRegSeleccinados = new ArrayList((Collection)admBean.selectGenericoNLSBind(resultado.getQueryInicio(), resultado.getCodigosInicio()));
						aniadeClavesBusqueda(this.clavesBusqueda,clavesRegSeleccinados);
						miForm.setRegistrosSeleccionados(clavesRegSeleccinados);
						int pagina;
						try{
							pagina = Integer.parseInt(miForm.getSeleccionarTodos());
						}catch (Exception e) {
							// Con esto evitamos un error cuando se recupera una pagina y hemos "perdido" la pagina actual
							// cargamos la primera y no evitamos mostrar un error
							pagina = 1;
						}
						datos = resultado.obtenerPagina(pagina);
						miForm.setSeleccionarTodos("");
						
					}else{
//					
						miForm.setRegistrosSeleccionados(new ArrayList());
						datos = resultado.obtenerPagina(1);
					}
					// jbd //
//					actualizarPagina(request, admBean, datos);
					databackup.put("datos",datos);
						
					
					
				}else{
					resultado = null;
					miForm.setRegistrosSeleccionados(new ArrayList());
				} 
				miForm.setDatosPaginador(databackup);
				

			}			
		
			// En "DATOSFORMULARIO" almacenamos el identificador del letrado		
			miHash.put("BUSQUEDAREALIZADA", "1");
			request.getSession().setAttribute("DATOSFORMULARIO", miHash);
			
			


		}catch (SIGAException e1) {
			// Excepcion procedente de obtenerPagina cuando se han borrado datos
			return exitoRefresco("error.messages.obtenerPagina",request);
		}catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}

		return "listarEJG";
	}
	
	protected String buscarPorEJGInit(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {

		ScsEJGAdm admBean = new ScsEJGAdm(this.getUserBean(request));
		Hashtable miHash = new Hashtable();
		String consulta = "";

		try {
			String longitudNumEjg = (String) request.getSession().getAttribute(PARAMETRO.LONGITUD_CODEJG.toString());
			DefinicionRemesas_CAJG_Form miForm = (DefinicionRemesas_CAJG_Form) formulario;
			miHash = miForm.getDatos();
			String idremesa = miForm.getIdRemesa();
			String idinstitucion = this.getIDInstitucion(request).toString();
			request.setAttribute("idremesa", idremesa);

			boolean isDatosEconomicos = false;
			GenParametrosService genParametrosService = (GenParametrosService) BusinessManager.getInstance().getService(GenParametrosService.class);
			//Niramos si esta configurada la url de envio de informe economico
			if(this.getUserBean(request).getLocation().equals("2003")){
				String urlEnvioInformeEconomico = genParametrosService.getValorParametroWithNull((short)2003,PARAMETRO.INFORMEECONOMICO_WS_URL,MODULO.ECOM);
				isDatosEconomicos = urlEnvioInformeEconomico!=null && !urlEnvioInformeEconomico.equalsIgnoreCase(""); 
				
			}
			request.setAttribute("ISDATOSECONOMICOS", Boolean.valueOf(isDatosEconomicos) );
			
			com.siga.Utilidades.paginadores.PaginadorBind resultado = admBean.getPaginadorEJGRemesas(miHash, miForm, miForm.getIdInstitucion(), longitudNumEjg,isDatosEconomicos);
			HashMap databackup = new HashMap();
			databackup.put("paginador", resultado);
			if (resultado != null && resultado.getNumeroTotalRegistros() > 0) {
				miForm.setRegistrosSeleccionados(new ArrayList());
				Vector datos = resultado.obtenerPagina(1);
				databackup.put("datos", datos);
			} else {
				resultado = null;
				miForm.setRegistrosSeleccionados(new ArrayList());
			}
			miForm.setDatosPaginador(databackup);
			miHash.put("BUSQUEDAREALIZADA", "1");
			request.getSession().setAttribute("DATOSFORMULARIO", miHash);
			

		} catch (SIGAException e1) {
			// Excepcion procedente de obtenerPagina cuando se han borrado datos
			return exitoRefresco("error.messages.obtenerPagina", request);
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita" }, e, null);
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
			String longitudNumEjg = (String) request.getSession().getAttribute(PARAMETRO.LONGITUD_CODEJG.toString());
			DefinicionRemesas_CAJG_Form miForm = (DefinicionRemesas_CAJG_Form) formulario;
			admBean = new ScsEJGAdm(this.getUserBean(request));
			miHash = miForm.getDatos();
			
			//BNS TAG SELECT // Añadida por JBD
			if (miHash.get("GUARDIATURNO_IDTURNO") != null && !"".equals(miHash.get("GUARDIATURNO_IDTURNO").toString()) && miHash.get("GUARDIATURNO_IDTURNO").toString().startsWith("{")){
				try {
					miHash.put("GUARDIATURNO_IDTURNO", UtilidadesString.createHashMap(miHash.get("GUARDIATURNO_IDTURNO").toString()).get("idturno"));
				} catch (IOException e) {
					throw new SIGAException(e);
				}
			}

			HashMap databackup = new HashMap();
			Vector claves = new Vector();
			String seleccionados = request.getParameter("Seleccion");
			
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			String idInstitucion= user.getLocation();
			

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
				Hashtable htConsultaBind  = admBean.getBindBusquedaMantenimientoEJG(miHash,  miForm, ScsEJGAdm.TipoVentana.BUSQUEDA_ANIADIR_REMESA, idInstitucion,longitudNumEjg);
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

		if (cajgRemesaEstadosAdm.nuevoEstadoRemesa(usr, getIDInstitucion(request), Integer.valueOf(miForm.getIdRemesa()), ClsConstants.ESTADO_REMESA_ENVIADA)) {
			//si es alcala metemos el estabdo recibida respuesta 
			if(usr.getLocation().equals("2003"))
				cajgRemesaEstadosAdm.nuevoEstadoRemesa(usr, getIDInstitucion(request), Integer.valueOf(miForm.getIdRemesa()), ClsConstants.ESTADO_REMESA_RECIBIDA);
			cajgEJGRemesaAdm.nuevoEstadoEJGRemitidoComision(usr, getIDInstitucion(request).toString(), miForm.getIdRemesa(), ESTADOS_EJG.REMITIDO_COMISION);
		}

		tx.commit();

		request.getSession().removeAttribute("DATAPAGINADOR");

		request.setAttribute("descargarFichero", Boolean.TRUE);

		return exitoRefresco(null, request);
	}

	private String descargar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions,
			SIGAException {
			
		DefinicionRemesas_CAJG_Form miForm = (DefinicionRemesas_CAJG_Form) formulario;
		File file = null;
		int tipoCajg = CajgConfiguracion.getTipoCAJG(getIDInstitucion(request));
		if (tipoCajg == CajgConfiguracion.TIPO_CAJG_PCAJG_GENERAL
				|| tipoCajg == CajgConfiguracion.TIPO_CAJG_XML_SANTIAGO) {
			file = getFicheroXML(getIDInstitucion(request).toString(), miForm.getIdRemesa());
		} else {
			file = getFichero(getIDInstitucion(request).toString(), miForm.getIdRemesa());
		}
		return descargaFichero(formulario, request, file);
	}
	
	private String descargaFichero(MasterForm formulario, HttpServletRequest request, File file) throws SIGAException {
		
		try {

			if (file == null || !file.exists()) {
				throw new SIGAException("messages.general.error.ficheroNoExiste");
			}
			request.setAttribute("nombreFichero", file.getName());
			request.setAttribute("rutaFichero", file.getAbsolutePath());
			request.setAttribute("accion", "");
			

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.gratuita" }, e, null);
		}

		return "descargaFichero";
	}
	public File generaFicherosTXT (String idInstitucion, String idRemesa, String nombreFicheroPorDefecto, StringBuffer mensaje,boolean isSimulacion, String rutaFicheroZIP) throws Exception {
		return generaFicherosTXT(idInstitucion, idRemesa, nombreFicheroPorDefecto, mensaje, rutaFicheroZIP,isSimulacion, null);
		
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
	public File generaFicherosTXT (String idInstitucion, String idRemesa, String nombreFicheroPorDefecto, StringBuffer mensaje, String rutaFicheroZIP,boolean isSimulacion, UsrBean usrBean) throws Exception {
		
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
					" WHERE " + CajgProcedimientoRemesaBean.C_IDINSTITUCION + " = " + idInstitucion +
					" ORDER BY " + CajgProcedimientoRemesaBean.C_IDPROCREMESA;
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
//				    	System.out.println("fila_consulta"+fila_consulta);
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
						
						if(idInstitucion.equals("2003")&&funcion.equalsIgnoreCase("f_comunicaciones_ejg_2003_CAB")){
							List<File> files = generaFicheros(rc2, nombreFichero, sRutaJava, campos, "txt", delimitador, saltoLinea, subCabecera, (usaTablaEJGremesa?numeroEJGs:null), mensaje,idRemesa,isSimulacion,usrBean); 
							ficheros.addAll(files);
						}else if(!isSimulacion){
							File file = generaFichero(rc2, nombreFichero, sRutaJava, campos, "txt", imprimirCabecera, delimitador, saltoLinea, subCabecera, (usaTablaEJGremesa?numeroEJGs:null), mensaje);
							ficheros.add(file);
						}
						
					}					
				} catch (Exception e) {
					ClsLogging.writeFileLogError("Error al generar el fichero o al insertar el historico de envio", e, 3);
					throw new SIGAException("messages.cajg.error.descargaFichero", e);
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
					if(isSimulacion)
						mensaje.append("No hay ningun expediente nuevo o actualización válida pendiente de enviar.");
					else
						mensaje.append("cajg.error.noFicheroGenerado");
				}
				//throw new SIGAException("messages.cajg.error.nodatos");
			}
			

		} catch (SIGAException e) {
			throw e;
		} catch (ClsExceptions e) {
			ClsLogging.writeFileLogError("Error en generaFicheroTXT", e, 3);
			throwExcp("messages.cajg.error.nodatos", new String[] { "modulo.gratuita" }, e, null);
		} catch (Exception e) {
			ClsLogging.writeFileLogError("Error en generaFicheroTXT", e, 3);
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
			eliminaFicheroTXTGenerado(idInstitucion, form.getIdRemesa(),idInstitucion.equals("2003"), usr);//por si se estan regenerando...
			CajgRespuestaEJGRemesaAdm respuestaEJGRemesaAdm = new CajgRespuestaEJGRemesaAdm(this.getUserBean(request));
			respuestaEJGRemesaAdm.eliminaAnterioresErrores(Integer.parseInt(idInstitucion), Integer.parseInt(form.getIdRemesa()));
			
			File fileZIP = generaFicherosTXT(idInstitucion, form.getIdRemesa(), nombreFicheroPorDefecto, mensaje, null,false,usr);
				
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
	
	protected String validaRemesaTxt(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions, SIGAException {
		request.getSession().removeAttribute("DATAPAGINADOR");

			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			String idInstitucion = this.getIDInstitucion(request).toString();	
			DefinicionRemesas_CAJG_Form form = (DefinicionRemesas_CAJG_Form) formulario;		
			String nombreFicheroPorDefecto = form.getPrefijo() + form.getNumero() + form.getSufijo() + "_" + idInstitucion;
			
			StringBuffer mensaje = new StringBuffer();
//			String mensajeCorrecto = UtilidadesString.getMensajeIdioma(usr, "cajg.mensaje.ficheroCorrecto");
//			mensaje.append(mensajeCorrecto);
			
			try {		
				eliminaFicheroTXTGenerado(idInstitucion, form.getIdRemesa(),idInstitucion.equals("2003"), usr);//por si se estan regenerando...
				CajgRespuestaEJGRemesaAdm respuestaEJGRemesaAdm = new CajgRespuestaEJGRemesaAdm(this.getUserBean(request));
				respuestaEJGRemesaAdm.eliminaAnterioresErrores(Integer.parseInt(idInstitucion), Integer.parseInt(form.getIdRemesa()));
				
				File fileZIP = generaFicherosTXT(idInstitucion, form.getIdRemesa(), nombreFicheroPorDefecto, mensaje, null,true,usr);
					
//				if (fileZIP == null) {
//					mensaje = new StringBuffer("cajg.mensaje.ficheroNoGenerado");
//				}
				
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
		log.debug("Ejecutando en background para el colegio " + getIDInstitucion(request));
		DefinicionRemesas_CAJG_Form form = (DefinicionRemesas_CAJG_Form) formulario;
		Integer idInstitucion = getIDInstitucion(request);
		UsrBean usrBean = getUserBean(request);
					
		String idRemesa = form.getIdRemesa();		

		SIGAWSClientAbstract sigaWSClient = CajgConfiguracion.getSIGAWSClientAbstract(usrBean, idInstitucion, indexClass);
				
		if (sigaWSClient == null) {
			throw new ClsExceptions("El colegio no tiene implementado el WebService");
		}
		sigaWSClient.setIdInstitucion(idInstitucion);
		sigaWSClient.setUsrBean(usrBean);
		sigaWSClient.setIdRemesa(Integer.parseInt(idRemesa));
		sigaWSClient.setSimular(VALUE_TRUE.equals(form.getSimular()));
		
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
		log.debug("Iniciando el nuevo hilo para ejecución en background para el colegio " + getIDInstitucion(request));
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
	private String validarRemesa(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("Ejecutando validarRemesa para el colegio " + getIDInstitucion(request));
		return validaEnviaWS(mapping, (DefinicionRemesas_CAJG_Form) formulario, request, response);
	}
	
	private String validaEnviaWS(ActionMapping mapping, DefinicionRemesas_CAJG_Form formulario, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer idInstitucion = getIDInstitucion(request);
		int tipoCAJG = CajgConfiguracion.getTipoCAJG(idInstitucion);
		
		String idRemesa = formulario.getIdRemesa();
		String mensaje = null;
		
		boolean simular = VALUE_TRUE.equals(((DefinicionRemesas_CAJG_Form) formulario).getSimular());
		
		if (CajgConfiguracion.TIPO_CAJG_WEBSERVICE_PAISVASCO == tipoCAJG) {
			PCAJGInsertaColaService pcajgInsertaColaService = (PCAJGInsertaColaService) getBusinessManager().getService(PCAJGInsertaColaService.class);
			RESPUESTA_ENVIO_REMESA respuesta = null;
			if (simular) {			
				respuesta = pcajgInsertaColaService.validaExpedientesPaisVasco(Short.valueOf(idInstitucion.toString()), Long.valueOf(idRemesa)
					, UtilidadesString.getMensajeIdioma(getUserBean(request), GEN_RECURSOS.scs_mensaje_validando.getValor()));
			} else {
				respuesta = pcajgInsertaColaService.enviaExpedientesPaisVasco(Short.valueOf(idInstitucion.toString()), Long.valueOf(idRemesa)
						, UtilidadesString.getMensajeIdioma(getUserBean(request), GEN_RECURSOS.scs_mensaje_validando.getValor()));
			}
			mensaje = getMensajeRespuesta(respuesta, request, simular);			
		} else if (CajgConfiguracion.TIPO_CAJG_WEBSERVICE_GENERALITAT_VALENCIANA == tipoCAJG) {
			PCAJGInsertaColaService pcajgInsertaColaService = (PCAJGInsertaColaService) getBusinessManager().getService(PCAJGInsertaColaService.class);
			RESPUESTA_ENVIO_REMESA respuesta = null;
			if (simular) {
				respuesta = pcajgInsertaColaService.validaExpedientesGeneralitatValenciana(Short.valueOf(idInstitucion.toString()), Long.valueOf(idRemesa)
						, UtilidadesString.getMensajeIdioma(getUserBean(request), GEN_RECURSOS.scs_mensaje_validando.getValor()));	
			} else {
				respuesta = pcajgInsertaColaService.enviaExpedientesGeneralitatValenciana(Short.valueOf(idInstitucion.toString()), Long.valueOf(idRemesa)
						, UtilidadesString.getMensajeIdioma(getUserBean(request), GEN_RECURSOS.scs_mensaje_validando.getValor()));
			}
			
			mensaje = getMensajeRespuesta(respuesta, request, simular);		
		} else if (CajgConfiguracion.TIPO_CAJG_WEBSERVICE_EJIS_ANDALUCIA == tipoCAJG) {
			PCAJGInsertaColaService pcajgInsertaColaService = (PCAJGInsertaColaService) getBusinessManager().getService(PCAJGInsertaColaService.class);
			RESPUESTA_ENVIO_REMESA respuesta = null;
			if (simular) {
				respuesta = pcajgInsertaColaService.validaExpedientesEJIS(Short.valueOf(idInstitucion.toString()), Long.valueOf(idRemesa)
						, UtilidadesString.getMensajeIdioma(getUserBean(request), GEN_RECURSOS.scs_mensaje_validando.getValor()));	
			} else {
				respuesta = pcajgInsertaColaService.enviaExpedientesEJIS(Short.valueOf(idInstitucion.toString()), Long.valueOf(idRemesa)
						, UtilidadesString.getMensajeIdioma(getUserBean(request), GEN_RECURSOS.scs_mensaje_validando.getValor()));
			}
			
			mensaje = getMensajeRespuesta(respuesta, request, simular);
		}else if (CajgConfiguracion.TIPO_CAJG_TXT_ALCALA == tipoCAJG) {
			mensaje = validaRemesaTxt(mapping, formulario, request, response);
			
//			PCAJGInsertaColaService pcajgInsertaColaService = (PCAJGInsertaColaService) getBusinessManager().getService(PCAJGInsertaColaService.class);
//			RESPUESTA_ENVIO_REMESA respuesta = null;
//			if (simular) {
//				respuesta = pcajgInsertaColaService.validaExpedientesEJIS(Short.valueOf(idInstitucion.toString()), Long.valueOf(idRemesa)
//						, UtilidadesString.getMensajeIdioma(getUserBean(request), GEN_RECURSOS.scs_mensaje_validando.getValor()));	
//			} else {
//				respuesta = pcajgInsertaColaService.enviaExpedientesEJIS(Short.valueOf(idInstitucion.toString()), Long.valueOf(idRemesa)
//						, UtilidadesString.getMensajeIdioma(getUserBean(request), GEN_RECURSOS.scs_mensaje_validando.getValor()));
//			}
			
//			mensaje = getMensajeRespuesta(respuesta, request, simular);
		} else {
			ejecutaBackground(formulario, request, 0);	
			if (simular) {
				mensaje = exitoRefresco("messages.cajg.validarRemesa", request);
			} else {
				mensaje = exitoRefresco("messages.cajg.enviandoWS", request);
			}
		}
		
		return mensaje;
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
		log.debug("Ejecutando envio por WS para el colegio " + getIDInstitucion(request));
		return validaEnviaWS(mapping, (DefinicionRemesas_CAJG_Form) formulario, request, response);
	}
	
	
	private String getMensajeRespuesta(RESPUESTA_ENVIO_REMESA respuesta, HttpServletRequest request, boolean simular) throws SIGAException {
		String mensaje = null;
		if (RESPUESTA_ENVIO_REMESA.OK.equals(respuesta)) {
			if (simular) {
				mensaje = exitoRefresco("messages.cajg.validarRemesa", request);
			} else {
				mensaje = exitoRefresco("messages.cajg.enviandoWS", request);	
			}			
		} else if (RESPUESTA_ENVIO_REMESA.ERROR.equals(respuesta)) {
			throw new SIGAException("messages.general.error");
		} else if (RESPUESTA_ENVIO_REMESA.ENVIO_EN_EJECUCION.equals(respuesta)) {
			mensaje = exitoRefresco("messages.cajg.remesa.enviando", request);
		}
		return mensaje;
	}

	private String generaXML(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws Exception {		
		ejecutaBackground(formulario, request, 0);		
		return exitoRefresco("messages.cajg.generandoXML", request);
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
	 * @param idRemesa 
	 * @return
	 * @throws IOException
	 */
	private List<File> generaFicheros(RowsContainer rowsContainer, String nombreFichero, String rutaFichero, String[] cabeceras, String extension,  String delimitador, String saltoLinea, String subCabecera, String numeroEJGs, StringBuffer mensaje, String idRemesa,boolean isSimulacion, UsrBean usrBean) throws IOException, BusinessException {
		List<File> files = new ArrayList<File>();
		

		
		boolean isActualizacion = false;
		String arraySubCabeceras = new String();
		boolean inicioLinea = true;
		int numeroDatos = 0;
		
		//Iremos acumulando los datos de las tablas de historico 
		Map<String, Vector<List<String>>> mapTablasHistorico = null;
		Vector<List<String>> vectorPares = null;
		List<String> campos = null;
		List<String> values = null;
		CajgEJGRemesaAdm cajgRemesaAdm = new CajgEJGRemesaAdm(usrBean); 
		ScsEJGAdm ejgAdm = new ScsEJGAdm(usrBean);
		


		// Vamos a crearnos la lista de expedientes incluidos en la remesa. Ademas nos guardaremos la informacion que se envia en las tablas creadas para tal efecto
		List<Map<String, Vector<List<String>>>> expedientesList = new ArrayList<Map<String, Vector<List<String>>>>();
		Map<String, Map<String, String>> newIntercambiosRemesaMap = new TreeMap<String, Map<String, String>>();
		//En esta lista vamos a ir acumulando los expedientes que ya han sido incluidos en una remesa y disponemos historico
		//En el caso que no haya modificacionesde dichio expediente no lo meteremos en el fichero de actualizacion
		List<String> expedientesActualizar = new ArrayList<String>();
		//Aqui vamosd a ir acumulando las lineas del fichero de expedientes
		Map<String, String> expInluidosFichero = new HashMap<String, String>();

		for (int i = 0; i < rowsContainer.size(); i++) {
			String linea = "";
			String cabecera = "";
			inicioLinea = true;
			numeroDatos++;

			Row row = (Row) rowsContainer.get(i);

			boolean mismoExpediente = true;
			String numeroIntercambio = row.getString("CAB2_NUMERO_INTERCAMBIO");
			String aniooEJG = row.getString("EXP2_ANIO_EXPEDIENTE");
			String numEJG = row.getString("EXP1_NUM_EXPEDIENTE");

			//Comprobamos si es actualizacion. Mira si hay registro en el hostorico 
			isActualizacion = cajgRemesaAdm.isEJGHistorico(aniooEJG, numEJG);
			//si es actualizacion vamos acumulando lo expedientes de los que hay que informar en el fichero de actualizaciones.
			//Luego se mirara si ha tenido alguna modificacion con respecto a la ultima vez que se envio
			if (isActualizacion) {
//				expedientesActualizar.add(aniooEJG + "/" + numEJG);
				if(!expedientesActualizar.contains(numeroIntercambio))
					expedientesActualizar.add(numeroIntercambio);
				newIntercambiosRemesaMap.put(numeroIntercambio, row.getRow());
			}
			for (int k = 0; k < cabeceras.length; k++) {
				String cabeceraPK = cabeceras[k];
				String valor = row.getString(cabeceraPK);

				if (cabeceraPK.startsWith("TIPO_REGISTRO_CAB")) {
					mapTablasHistorico = new TreeMap<String, Vector<List<String>>>();
					expedientesList.add(mapTablasHistorico);

				}
				if (cabeceraPK.startsWith("TIPO_REGISTRO_")) {
					String tipoRegistro = cabeceraPK.split("TIPO_REGISTRO_")[1];
					if (!mapTablasHistorico.containsKey(tipoRegistro)) {
						vectorPares = new Vector<List<String>>();
						campos = new ArrayList<String>();
						values = new ArrayList<String>();
						if(!isSimulacion && tipoRegistro.equals("CAB")){
							ScsEJGBean ejgBean =  ejgAdm.getEjg(aniooEJG,numEJG,usrBean.getLocation());
							campos.add("CAB_EJG_IDINSTITUCION");
							values.add(""+ejgBean.getIdInstitucion());
							campos.add("CAB_EJG_IDTIPO");
							values.add(""+ejgBean.getIdTipoEJG());
							campos.add("CAB_EJG_ANIO");
							values.add(""+ejgBean.getAnio());
							campos.add("CAB_EJG_NUMERO");
							values.add(""+ejgBean.getNumero());
						}
						vectorPares.add(0, campos);
						vectorPares.add(1, values);
						
						
					} else {
						vectorPares = mapTablasHistorico.get(tipoRegistro);
					}
					mapTablasHistorico.put(tipoRegistro, vectorPares);
				} else {
					String tipoRegistro = cabeceraPK.substring(0, 3);
					// Miramos que la tabla contenga la cabecera, si no, sera uno de los campos de separacion o cambio de linea
					if (mapTablasHistorico.get(tipoRegistro) != null && valor != null && !valor.equals("")) {
						vectorPares = mapTablasHistorico.get(tipoRegistro);
						campos = vectorPares.get(0);
						values = vectorPares.get(1);
						campos.add(cabeceraPK);
						values.add(valor);
					}

				}

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

			if (!isActualizacion) {
				expInluidosFichero.put(aniooEJG + "/" + numEJG, linea);
			}

		}
		File directorio = new File(rutaFichero);
		directorio.mkdirs();

		File archivo = new File(rutaFichero + ClsConstants.FILE_SEP + nombreFichero + "." + extension);

		int j = 1;
		while (archivo.exists()) {
			archivo = new File(rutaFichero + ClsConstants.FILE_SEP + nombreFichero + " (" + j + ")." + extension);
			j++;
		}
		
		OutputStream out = null;
		if(expInluidosFichero.size()>0)
			out = new FileOutputStream(archivo);
		int findTheLast = 0;
		Iterator<String> iteraExpIncluidosFichero = expInluidosFichero.keySet().iterator();
		while (iteraExpIncluidosFichero.hasNext()) {
			findTheLast ++;
			String keyExpIncluidoFichero = (String) iteraExpIncluidosFichero.next();
			out.write(expInluidosFichero.get(keyExpIncluidoFichero).getBytes());
			if(findTheLast!=expInluidosFichero.keySet().size())
				out.write("\r\n".getBytes());
		}
		

		Map<String, List<String>> expInluidosFicheroActualizacion = cajgRemesaAdm.getLineasExpedientesFicheroActualizacion(newIntercambiosRemesaMap);
		Set<String> expActualizacionesSet = expInluidosFicheroActualizacion.keySet();
		Iterator<String> iteraExpIncluidosFicheroAct = expActualizacionesSet.iterator();
		List<String> lineasFicheroActualizacion = new ArrayList<String>();
		while (iteraExpIncluidosFicheroAct.hasNext()) {
			String keyExpIncluidoFichero = (String) iteraExpIncluidosFicheroAct.next();
			// Borramos los que hayan tenido modificaciones
			expedientesActualizar.remove(keyExpIncluidoFichero);
			List<String> lineasExp = expInluidosFicheroActualizacion.get(keyExpIncluidoFichero);
			lineasFicheroActualizacion.addAll(lineasExp);

		}
		if (lineasFicheroActualizacion != null && lineasFicheroActualizacion.size() > 0) {
			
			boolean isPrimeralinea = false;
			if(out==null){
				isPrimeralinea = true;
				out = new FileOutputStream(archivo);
			}
			for (String linea : lineasFicheroActualizacion) {
				String newLine = isPrimeralinea?linea:"\r\n"+linea;
				isPrimeralinea = false;
				out.write(newLine.getBytes());
			}
			
			
			
			
		}
		int numExpedientesProcesados = 0;
		if(out!=null){
			out.flush();
			out.close();
			files.add(archivo);
			if(isSimulacion){
				numExpedientesProcesados+=expInluidosFichero.size();
				if(expInluidosFichero.size()>0){
					mensaje.append(expInluidosFichero.keySet().size() );
					mensaje.append(" expedientes nuevos válidos.");
				}
				numExpedientesProcesados+=expActualizacionesSet.size();
				if (lineasFicheroActualizacion.size() > 0){
					if(mensaje.length()>0)
						mensaje.append("\n");
					mensaje.append(expActualizacionesSet.size());
					mensaje.append(" actualizaciones de expedientes validas.");
				}
				
			}else{
				
				mensaje.append("- " + nombreFichero + "." + extension + " generado:");
				if(expInluidosFichero.size()>0){
					mensaje.append("\n"); 
					mensaje.append(expInluidosFichero.keySet().size() );
					mensaje.append(" expedientes nuevos.");
				}
				if (lineasFicheroActualizacion.size() > 0){
					mensaje.append("\n");
					mensaje.append(expActualizacionesSet.size());
					mensaje.append(" actualizaciones de expedientes.");
				}
				
				
				
			}
			
		}
		
		if(!isSimulacion && expedientesList!=null && expedientesList.size()>0)
			cajgRemesaAdm.insertaHistoricoRemesa(expedientesList);
		if (expedientesActualizar.size() > 0){
			cajgRemesaAdm.insertaErroresHistoricoRemesa(usrBean.getLocation(),idRemesa,expedientesActualizar);
			numExpedientesProcesados+=expedientesActualizar.size();
			if(isSimulacion){
				if(mensaje.length()>0)
					mensaje.append("\n");
				mensaje.append( expedientesActualizar.size() );
				mensaje.append(" expedientes incluidos que ya estaban enviados, no han tenido modificaciones y no se incluiran en fichero.");
			}
			else{
				mensaje.append("\n");
				mensaje.append(expedientesActualizar.size() );
				mensaje.append(" expedientes incluidos que ya estaban enviados, no han tenido modificaciones y no se incluyen en fichero.");
			}
			
		}
		if(isSimulacion){
			if(Integer.parseInt(numeroEJGs)!=numExpedientesProcesados){
				if(mensaje.length()>0)
					mensaje.append("\n");
				mensaje.append((Integer.parseInt(numeroEJGs) - numExpedientesProcesados) );
				mensaje.append(" expedientes con incidencias que no se incluiran en el fichero");
				
			}
		}

		return files;
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
		request.setAttribute("accion", "");
		request.setAttribute("nombreFichero", file.getName());
		request.setAttribute("rutaFichero", file.getAbsolutePath());

		return "descargaFichero";
	}
	
	protected String marcarRespuestaIncorrectaManual(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
		DefinicionRemesas_CAJG_Form miForm = (DefinicionRemesas_CAJG_Form) formulario;
		try {
			CajgRemesaVo cajgRemesaVo = new CajgRemesaVo();
			cajgRemesaVo.setIdInstitucion(Short.valueOf(usr.getLocation()));
			cajgRemesaVo.setIdRemesa(Long.valueOf(miForm.getIdRemesa()));
			cajgRemesaVo.setUsuModificacion(Integer.valueOf(usr.getUserName()));
			String datosSolicInformeEconomico = (String) miForm.getDatosSolicInformeEconomico();
			String [] datosSolicInformeEconomicoStrings = datosSolicInformeEconomico.split("%%%");
			CajgEjgRemesaVo ejgVo = null;
			List<CajgEjgRemesaVo> ejgVos = new ArrayList<CajgEjgRemesaVo>();
			String respuestaErronea = "";
			for (int i = 0; i < datosSolicInformeEconomicoStrings.length; i++) {
				ejgVo = new CajgEjgRemesaVo();
				String datosSolicInformeEconomicoString = datosSolicInformeEconomicoStrings[i];
				if(datosSolicInformeEconomicoString!=null && !datosSolicInformeEconomicoString.equals("")){
				String[] clavesEjgSolicInformeEconomicoStrings =  datosSolicInformeEconomicoString.split("##");
					String idInstitucion = clavesEjgSolicInformeEconomicoStrings[0];
					String idTipoEjg = clavesEjgSolicInformeEconomicoStrings[1];
					String anio = clavesEjgSolicInformeEconomicoStrings[2];
					String numero = clavesEjgSolicInformeEconomicoStrings[3];
					ejgVo.setIdinstitucion(Short.valueOf(idInstitucion));
					ejgVo.setAnio(Short.valueOf(anio));
					ejgVo.setIdtipoejg(Short.valueOf(idTipoEjg));
					ejgVo.setNumero(Long.valueOf(numero));
					ejgVo.setIdejgremesa(Long.valueOf(clavesEjgSolicInformeEconomicoStrings[4]));
					ejgVo.setNumerointercambio( Integer.valueOf(clavesEjgSolicInformeEconomicoStrings[5]));
					respuestaErronea = clavesEjgSolicInformeEconomicoStrings[6];
					ejgVos.add(ejgVo);
				}
			}
			
			cajgRemesaVo.setEjgRemesaVo(ejgVos);
			BusinessManager bm = getBusinessManager();
			CajgRemesaService cajgRemesaService = (CajgRemesaService)bm.getService(CajgRemesaService.class);
			cajgRemesaService.erroresManual(cajgRemesaVo,respuestaErronea);
		} catch (Exception e) {
			throw new SIGAException("No se puede borrar la remesa"+e.toString(),e);
		}


		return exitoRefresco("messages.updated.success", request);
	}
	
	protected String comunicarInfEconomico(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
		DefinicionRemesas_CAJG_Form miForm = (DefinicionRemesas_CAJG_Form) formulario;
		try {
			CajgRemesaVo cajgRemesaVo = new CajgRemesaVo();
			cajgRemesaVo.setIdInstitucion(Short.valueOf(usr.getLocation()));
			cajgRemesaVo.setIdRemesa(Long.valueOf(miForm.getIdRemesa()));
			cajgRemesaVo.setUsuModificacion(Integer.valueOf(usr.getUserName()));
			String datosSolicInformeEconomico = (String) miForm.getDatosSolicInformeEconomico();
			String [] datosSolicInformeEconomicoStrings = datosSolicInformeEconomico.split("%%%");
			CajgEjgRemesaVo ejgVo = null;
			List<CajgEjgRemesaVo> ejgVos = new ArrayList<CajgEjgRemesaVo>();
			for (int i = 0; i < datosSolicInformeEconomicoStrings.length; i++) {
				ejgVo = new CajgEjgRemesaVo();
				String datosSolicInformeEconomicoString = datosSolicInformeEconomicoStrings[i];
				String[] clavesEjgSolicInformeEconomicoStrings =  datosSolicInformeEconomicoString.split("##");
				String idInstitucion = clavesEjgSolicInformeEconomicoStrings[0];
				String idTipoEjg = clavesEjgSolicInformeEconomicoStrings[1];
				String anio = clavesEjgSolicInformeEconomicoStrings[2];
				String numero = clavesEjgSolicInformeEconomicoStrings[3];
				String idEjgRemesa = clavesEjgSolicInformeEconomicoStrings[4];
				
				ejgVo.setIdejgremesa(Long.valueOf(idEjgRemesa));
				ejgVo.setIdinstitucion(Short.valueOf(idInstitucion));
				ejgVo.setAnio(Short.valueOf(anio));
				ejgVo.setIdtipoejg(Short.valueOf(idTipoEjg));
				ejgVo.setNumero(Long.valueOf(numero));
				ejgVos.add(ejgVo);
			}
			
			cajgRemesaVo.setEjgRemesaVo(ejgVos);
			BusinessManager bm = getBusinessManager();
			CajgRemesaService cajgRemesaService = (CajgRemesaService)bm.getService(CajgRemesaService.class);
			cajgRemesaService.comunicaInformeEconomicoEjg(cajgRemesaVo,OPERACION.ENVIO_INFORME_ECONOMICO);
		} catch (Exception e) {
			throw new SIGAException("No se puede borrar la remesa"+e.toString(),e);
		}


		return exitoRefresco("messages.updated.success", request);
	}
	private Vector actualizarPagina(Vector datos,boolean isDatosEconomicos,UsrBean usrBean) throws ClsExceptions, SIGAException{
		CajgRemesaAdm scsEJGAdm = new CajgRemesaAdm(usrBean);
		
		
		try{
			for (int i = 0; i < datos.size(); i++) {
				
				Row fila = (Row)datos.get(i);
				Hashtable registro = (Hashtable) fila.getRow();
				Hashtable datosRemesa  = scsEJGAdm.getDatosRemesa((String) registro.get("IDREMESA"),(String) registro.get("IDINSTITUCION"),isDatosEconomicos);
				registro.putAll(datosRemesa);
			}
		} catch (Exception e) {
			throwExcp("messages.general.error",e,null);
		}
		return datos;
	}
	

}