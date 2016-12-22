// VERSIONES:
// raul.ggonzalez 14-12-2004 Creacion
// miguel.villegas 11-01-2005 Incorpora "borrar"
// juan.grau 18-04-2005 Incorpora 'buscarPersona' y 'enviarPersona'
// aalg. 2012. Se modifica todo el funcionamiento de duplicados. Se hace que aparezca todo en una misma pantalla
// se pagina (se crea buscarPor)
// la exportación ya no es de lo que se muestra en pantalla sino que hay que hacer la selección de todos los que están en el paginador
// se añaden ver y editar para acceder a los datos del no colegiado o el letrado 
/**
 * @version 30/01/2006 (david.sanchezp): nuevo valor de pestanha.
 */
package com.siga.censo.action;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONObject;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.Utilidades.PaginadorBind;
import com.siga.administracion.SIGAConstants;
import com.siga.beans.AdmLenguajesAdm;
import com.siga.beans.AdmLenguajesBean;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenClienteBean;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenDatosColegialesEstadoAdm;
import com.siga.beans.CenDatosColegialesEstadoBean;
import com.siga.beans.CenDireccionTipoDireccionBean;
import com.siga.beans.CenDireccionesAdm;
import com.siga.beans.CenDireccionesBean;
import com.siga.beans.CenEstadoCivilAdm;
import com.siga.beans.CenEstadoCivilBean;
import com.siga.beans.CenHistoricoAdm;
import com.siga.beans.CenHistoricoBean;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.CenInstitucionBean;
import com.siga.beans.CenNoColegiadoAdm;
import com.siga.beans.CenNoColegiadoBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.CenSancionAdm;
import com.siga.beans.CenTipoDireccionBean;
import com.siga.beans.CenTratamientoAdm;
import com.siga.beans.CenTratamientoBean;
import com.siga.beans.CerSolicitudCertificadosAdm;
import com.siga.beans.DuplicadosHelper;
import com.siga.beans.FcsPagoColegiadoAdm;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.ScsCabeceraGuardiasAdm;
import com.siga.censo.form.MantenimientoDuplicadosForm;
import com.siga.general.EjecucionPLs;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.informes.MasterReport;

/**
 * Clase action del caso de uso BUSCAR CLIENTE
 * @author AtosOrigin 14-12-2004
 */
public class MantenimientoDuplicadosAction extends MasterAction {
	//Atencion!!Tenr en cuenta que el orden de estas claves es el mismo oden que se va a
	//seguir al obtener los adtos en la jsp. Ver metodos actualizarSelecionados y aniadeClaveBusqueda(2)
	//de la super clase(MasterAction)
	final String[] clavesBusqueda={CenClienteBean.C_IDINSTITUCION,CenClienteBean.C_IDPERSONA};

	private static final Object mantenimientoDuplicados = new Object(); // semaforo
	private static class ControlFusionador {
		/**
		 * Conjunto de personas que se estan fusionando en este momento. Se van metiendo y sacando segun se termina la fusion
		 */
		private static HashSet<String> listaPersonasFusionando = new HashSet<String>();
		
		/**
		 * Obtiene un control basado en dos personas a fusionar
		 * @param idpersona1
		 * @param idpersona2
		 * @return
		 */
		public static ControlFusionador getControlFusionador(String idpersona1, String idpersona2) {
			synchronized (listaPersonasFusionando) {
				if (listaPersonasFusionando.contains(idpersona1) || listaPersonasFusionando.contains(idpersona2)) {
					return null;
				} else {
					return new ControlFusionador(idpersona1, idpersona2);
				}
			}
		}
		
		private String personaFusionando1, personaFusionando2;
		
		/**
		 * Constructor privado: solo se puede obtener un control llamando a getControlFusionador(), que busca si las personas ya estan en una fusion
		 * @param idpersona1
		 * @param idpersona2
		 */
		private ControlFusionador (String idpersona1, String idpersona2) {
			listaPersonasFusionando.add(idpersona1);
			listaPersonasFusionando.add(idpersona2);
			personaFusionando1 = idpersona1;
			personaFusionando2 = idpersona2;
		}
		
		/**
		 * Da por terminada la fusion controlada por este objeto
		 */
		public void removeControlFusionador() {
			synchronized (listaPersonasFusionando) {
				listaPersonasFusionando.remove(this.personaFusionando1);
				listaPersonasFusionando.remove(this.personaFusionando2);
			}
		}
	}

	/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	
	public ActionForward executeInternal (ActionMapping mapping,
							      ActionForm formulario,
							      HttpServletRequest request, 
							      HttpServletResponse response) throws SIGAException {

		String mapDestino = "exception";
		MasterForm miForm = null;
		
		UsrBean usr = this.getUserBean(request);
		if (! usr.getLocation().equalsIgnoreCase(Integer.toString(ClsConstants.INSTITUCION_CGAE))) {
			throw (new SIGAException("Esta funcionalidad no está disponible. Consulte con el Administrador"));
		}

		try { 

			miForm = (MasterForm) formulario;
			if (miForm != null) {
				String accion = miForm.getModo();
				
				if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("inicio") || accion.equalsIgnoreCase("mantenimientoDuplicadosCertificados") ){
					miForm.reset(new String[]{"registrosSeleccionados","datosPaginador","seleccionarTodos"});
					MantenimientoDuplicadosForm formDupl = (MantenimientoDuplicadosForm)miForm;
					formDupl.reset(mapping,request);
					formDupl.setApellidos("");
					formDupl.setNifcif("");
					formDupl.setNombre("");
					formDupl.setNumeroColegiado("");
					formDupl.setIdInstitucion("");
					request.getSession().removeAttribute("DATAPAGINADOR");
					mapDestino = abrir(mapping, miForm, request, response);
				}else if(accion.equalsIgnoreCase("buscar")){
					miForm.reset(new String[]{"registrosSeleccionados","datosPaginador","seleccionarTodos"});
					((MantenimientoDuplicadosForm)miForm).setDatosPaginador(null);
					mapDestino = buscar(mapping, miForm, request, response);
				}else if(accion.equalsIgnoreCase("buscarPor")){
					mapDestino = buscarPor(mapping, miForm, request, response);
				}else if(accion.equalsIgnoreCase("gestionar")){
					mapDestino = gestionar(mapping, miForm, request, response);
				}else if(accion.equalsIgnoreCase("aceptar")){
					mapDestino = combinar(mapping, miForm, request, response);
				}else if(accion.equalsIgnoreCase("volver")){
					mapDestino = volver(mapping, miForm, request, response);
				}else if(accion.equalsIgnoreCase("export")){
					miForm.setModo("");
					mapDestino = export(mapping, miForm, request, response);
				} else if (accion.equalsIgnoreCase("getAjaxObtenerDuplicados")) {
					miForm.setModo("");
		            getAjaxObtenerDuplicados(request, response);	     
					return null;
				}else if(accion.equalsIgnoreCase("exportar")){
					mapDestino = exportar(mapping, miForm, request, response);
				}else {
					return super.executeInternal(mapping,formulario,request,response);
				}
			}
			 miForm.setModo("");

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null)	{ 
			    throw new ClsExceptions("El ActionMapping no puede ser nulo");
			}
			return mapping.findForward(mapDestino);
		} catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.censo"});
		}
	}

	
	/**
	 * Metodo que implementa el modo abrir
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrir (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException
	{
		try {
			// No hacemos nada
			//Devolvemos la lista de instituciones
			UsrBean usr = this.getUserBean(request);
			String parametros = usr.getLocation();
			CenInstitucionAdm admInstitucion = new CenInstitucionAdm(usr); 
			List<CenInstitucionBean> listadoInstituciones = admInstitucion.getNombreColegiosTodos(parametros);
			request.setAttribute("listadoInstituciones", listadoInstituciones);
			request.getSession().setAttribute("CenBusquedaClientesTipo","DUPLICADOS"); // Desde duplicados
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		return "inicio";
	}
	
	/**
	 * Metodo que implementa el modo cuando se vuelve de la pantalla de busqueda
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String volver (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException
	{
		try {
			// Ponemos el formulario de busqueda otra vez para recuperar los filtros
			MantenimientoDuplicadosForm miFormulario = (MantenimientoDuplicadosForm)request.getSession().getAttribute("duplicadosForm");
			request.getSession().setAttribute("mantenimientoDuplicadosForm", miFormulario);
			request.setAttribute("MantenimientoDuplicadosForm", miFormulario);
			formulario=miFormulario;
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		return "inicio";
	}
	
	
	/**
	 * Metodo que implementa el modo buscar para realizar la busqueda de duplicados
	 * 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{
		// Controles generales
		UsrBean user = this.getUserBean(request);
		MantenimientoDuplicadosForm miFormulario = (MantenimientoDuplicadosForm) formulario;
		DuplicadosHelper helper = new DuplicadosHelper();
		
		// Variables generales
		String destino = "";
		
		try {

			PaginadorBind resultado = new PaginadorBind(helper.getPersonasSimilares(miFormulario));
			request.setAttribute("resultado", resultado);

			HashMap databackup = new HashMap();
			databackup.put("paginador", resultado);
			databackup.put("datos", resultado.obtenerPagina(1));
			miFormulario.setDatosPaginador(databackup);

			miFormulario.setRegistrosSeleccionados(new ArrayList());

			// calculando si se ha de mostrar los campos de colegio en funcion de si se busca por colegiado o por
			// persona
			String institucion = miFormulario.getIdInstitucion();
			institucion = (institucion == null || institucion.trim().equalsIgnoreCase("")) ? "" : institucion;
			String nColegiado = miFormulario.getNumeroColegiado();
			nColegiado = (nColegiado == null || nColegiado.trim().equalsIgnoreCase("")) ? "" : nColegiado;
			if (institucion.equalsIgnoreCase("") && nColegiado.equalsIgnoreCase("")) {
				request.setAttribute("mostarNColegiado", "0");
			} else {
				request.setAttribute("mostarNColegiado", "1");
			}

			destino = "resultado";

		} catch (SIGAException e1) {
			// Excepcion procedente de obtenerPagina cuando se han borrado datos
			return exitoRefresco("error.messages.obtenerPagina", request);
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.censo" }, e, null);
		}
		return destino;
	}

	/**
	 * Metodo que implementa el modo buscar para realizar la busqueda de duplicados
	 * 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String destino = "";
		try {
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			String idInstitucion=user.getLocation();

			MantenimientoDuplicadosForm miFormulario = (MantenimientoDuplicadosForm)formulario;
			
			ArrayList clavesRegSeleccinados = (ArrayList) miFormulario.getRegistrosSeleccionados();
			String seleccionados = request.getParameter("Seleccion");
			
			
			if (seleccionados != null ) {
				ArrayList alRegistros = actualizarSelecionados(clavesBusqueda,seleccionados, clavesRegSeleccinados);
				if (alRegistros != null) {
					clavesRegSeleccinados = alRegistros;
					miFormulario.setRegistrosSeleccionados(clavesRegSeleccinados);
				}
			}
			
			request.getSession().setAttribute("duplicadosForm", miFormulario);

			DuplicadosHelper helper = new DuplicadosHelper();
			HashMap databackup = (HashMap) miFormulario.getDatosPaginador();
			if (databackup!=null && databackup.get("paginador")!=null){ 
				PaginadorBind paginador = (PaginadorBind)databackup.get("paginador");
				//Si no es la primera llamada, obtengo la página del request y la busco con el paginador
				
				Vector datos=new Vector();
				if (paginador!=null){
					String pagina = (String)request.getParameter("pagina");
					if (pagina!=null){
						datos = paginador.obtenerPagina(Integer.parseInt(pagina));
					}else{// cuando hemos editado un registro de la busqueda y volvemos a la paginacion
						datos = paginador.obtenerPagina((paginador.getPaginaActual()));
					}
				}	
				databackup.put("paginador",paginador);
				databackup.put("datos",datos);

			}
			
			formulario=miFormulario;
			// calculando si se ha de mostrar los campos de colegio en funcion de si se busca por colegiado o por
			// persona
			String institucion = miFormulario.getIdInstitucion();
			institucion = (institucion == null || institucion.trim().equalsIgnoreCase("")) ? "" : institucion;
			String nColegiado = miFormulario.getNumeroColegiado();
			nColegiado = (nColegiado == null || nColegiado.trim().equalsIgnoreCase("")) ? "" : nColegiado;
			if (institucion.equalsIgnoreCase("") && nColegiado.equalsIgnoreCase("")) {
				request.setAttribute("mostarNColegiado", "0");
			} else {
				request.setAttribute("mostarNColegiado", "1");
			}
			destino="resultado";

		}catch (SIGAException e1) {
			// Excepcion procedente de obtenerPagina cuando se han borrado datos
			return exitoRefresco("error.messages.obtenerPagina",request);
		}catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		return destino;
	}
	
	/**
	 * Metodo que implementa el modo buscar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String gestionar(ActionMapping mapping,
			MasterForm formulario,
			HttpServletRequest request,
			HttpServletResponse response) throws SIGAException
	{
		// Controles
		MantenimientoDuplicadosForm miFormulario = (MantenimientoDuplicadosForm) formulario;

		UsrBean usr = this.getUserBean(request);
		CenPersonaAdm admPersona = new CenPersonaAdm(usr);
		CenClienteAdm admCliente = new CenClienteAdm(usr);
		CenColegiadoAdm admColeg = new CenColegiadoAdm(usr);
		CenNoColegiadoAdm admNoColeg = new CenNoColegiadoAdm(usr);
		CenSancionAdm admSancion = new CenSancionAdm(usr);
		CerSolicitudCertificadosAdm admCertificados = new CerSolicitudCertificadosAdm(usr);
		CenInstitucionAdm admInstitucion = new CenInstitucionAdm(usr);
		CenEstadoCivilAdm estadoCivilAdm = new CenEstadoCivilAdm(usr);
		CenTratamientoAdm tratamientoAdm = new CenTratamientoAdm(usr);
		AdmLenguajesAdm lenguajeAdm = new AdmLenguajesAdm(usr);
		
		
		// Variables
		String idPersona;
		Hashtable todosLosDatos;
		
		// 1. datos personales
		ArrayList	<CenPersonaBean> datosPersonaDeAmbas = new ArrayList	<CenPersonaBean>(2);
					CenPersonaBean datosPersonaDeUna;
					
		// 2. datos del cliente CGAE
		ArrayList	<CenClienteBean> datosClienteCGAEDeAmbas = new ArrayList	<CenClienteBean>(2);
					CenClienteBean datosClienteCGAEDeUna;
					
		// 3. datos de colegiaciones y no colegiaciones
		ArrayList	<Hashtable	<String, Hashtable>> listaColegiacionesDeAmbas = new ArrayList<Hashtable<String,Hashtable>>();
					Hashtable	<String, Hashtable> listaColegiacionesDeUna;
								Hashtable datosColegiacionDeUna;
											Hashtable <String, String> datosColegioDeUna;
											CenColegiadoBean beanColegiado;
											Hashtable <String, String> hashCliente;
											Row estadoUltimo;
											Vector estadosColegio;
											Vector	<Hashtable<String, String>> listaDireccionesDeUna;
													Hashtable<String, String> direccion;

		ArrayList	<ArrayList	<Hashtable>> listaColegiacionesDiferentesDeAmbas = new ArrayList	<ArrayList	<Hashtable>>(2);
					ArrayList	<Hashtable> listaColegiacionesDiferentesDeUna;
		//Hashtable<String,	Hashtable	<String, ArrayList>> listaColegiacionesComunesDeAmbas = new Hashtable<String, Hashtable<String,ArrayList>>();
		ArrayList	<Hashtable> listaColegiacionesComunesDeAmbas = new ArrayList<Hashtable>();
					Hashtable colegiacionComunDeAmbas;
								ArrayList	<Hashtable <String, String>> datosColegioDeAmbas;
								ArrayList	<CenColegiadoBean> beanColegiadoDeAmbas;
								ArrayList	<Hashtable <String, String>> hashClienteDeAmbas;
								ArrayList	<Row> estadoUltimoDeAmbas;
								ArrayList	<Vector> estadosColegioDeAmbas;
								ArrayList	<Vector	<Hashtable<String, String>>> listaDireccionesDeAmbas;
		
		// 4. direcciones en CGAE
		ArrayList	<Vector	<Hashtable<String, String>>> listaDireccionesCGAEDeAmbas = new ArrayList	<Vector<Hashtable<String, String>>>(2);
					Vector	<Hashtable<String, String>> listaDireccionesCGAEDeUna;
		
		
		// A continuacion todo el proceso de lectura y devolucion de los datos de las personas seleccionadas
		try {
			// controlando el Volver
			String valorVolver = miFormulario.getVolver();
			miFormulario.setVolver("NO");
			// obteniendo las personas seleccionadas, dependiendo de si es un acceso inicial o desde el boton volver
			String seleccionados;
			if (valorVolver != null && "SI".equalsIgnoreCase(valorVolver)) {
				seleccionados = (String) request.getSession().getAttribute("registrosSeleccionados");
			} else {
				// Si es la primera vez, almacenamos el valor de los seleccionados para luego tenerlos cuando demos a
				// volver
				seleccionados = request.getParameter("registrosSeleccionados");
				request.getSession().removeAttribute("registrosSeleccionados");
				request.getSession().setAttribute("registrosSeleccionados", seleccionados);
			}
			// Si no hay seleccionados, no se puede hacer nada mas
			if (seleccionados == null || seleccionados.equalsIgnoreCase("")) {
				request.setAttribute("mensaje",UtilidadesString.getMensajeIdioma(usr, "No se ha seleccionado nada. Seleccione dos personas para fusionar."));
				return "exitoFusionar";
			}
			// Los seleccionados deben ser 2, separados por comas
			String[] personasSeleccionadas = UtilidadesString.split(seleccionados, ",");
			if (personasSeleccionadas.length != 2) {
				request.setAttribute("mensaje",UtilidadesString.getMensajeIdioma(usr, "Selección incorrecta. Seleccione dos personas para fusionar."));
				return "exitoFusionar";
			}
			
			// obteniendo los datos de cada persona
			for (int iPersona = 0; iPersona < personasSeleccionadas.length; iPersona++) {
				// Las claves de cada registro estan separadas por ||
				idPersona = UtilidadesString.split(personasSeleccionadas[iPersona], "||") [1];

				// Controlando si ya se estan fusionando estas personas
				//TODO hay que mostrar al usuario un recurso concreto
				ControlFusionador controlFusionador = ControlFusionador.getControlFusionador(idPersona, null);
				if (controlFusionador == null) {
					request.setAttribute("mensaje",UtilidadesString.getMensajeIdioma(usr, "Ya se ha solicitado la combinación de alguna de estas personas. Espere unos minutos hasta que termine."));
					return "exitoFusionar";
				}
				controlFusionador.removeControlFusionador(); // abrimos el semaforo: ya se cerrara mas tarde al comenzar la fusion
				
				// 1. obteniendo datos personales
				datosPersonaDeUna = admPersona.getPersonaPorId(idPersona);
				datosPersonaDeUna.setFechaMod(UtilidadesString.formatoFecha(datosPersonaDeUna.getFechaMod(), ClsConstants.DATE_FORMAT_JAVA, ClsConstants.DATE_FORMAT_SHORT_SPANISH));
				if (datosPersonaDeUna.getFechaNacimiento() != null && !datosPersonaDeUna.getFechaNacimiento().equalsIgnoreCase("")) {
					datosPersonaDeUna.setFechaNacimiento(UtilidadesString.formatoFecha(datosPersonaDeUna.getFechaNacimiento(), ClsConstants.DATE_FORMAT_JAVA, ClsConstants.DATE_FORMAT_SHORT_SPANISH));
				}
				if (datosPersonaDeUna.getSexo() != null && !datosPersonaDeUna.getSexo().equalsIgnoreCase("")) {
					datosPersonaDeUna.setSexoStr(UtilidadesString.getMensajeIdioma(usr, datosPersonaDeUna.getSexo().equalsIgnoreCase(ClsConstants.TIPO_SEXO_HOMBRE) ? "censo.sexo.hombre" : "censo.sexo.mujer"));
				}
				if (datosPersonaDeUna.getIdEstadoCivil() != null) {
					datosPersonaDeUna.setIdEstadoCivilStr(((CenEstadoCivilBean) estadoCivilAdm.select("where "+CenEstadoCivilBean.C_IDESTADO+"="+datosPersonaDeUna.getIdEstadoCivil()).get(0)).getDescripcion());
				}
				datosPersonaDeAmbas.add(datosPersonaDeUna);

				// 2. obteniendo datos del cliente CGAE
				datosClienteCGAEDeUna = admCliente.existeCliente(Long.valueOf(idPersona), ClsConstants.INSTITUCION_CGAE);
				if (datosClienteCGAEDeUna == null) {
					datosClienteCGAEDeUna = new CenClienteBean();
				} else {
					datosClienteCGAEDeUna.setIdTratamientoStr(((CenTratamientoBean) tratamientoAdm.select("where "+CenTratamientoBean.C_IDTRATAMIENTO+"="+datosClienteCGAEDeUna.getIdTratamiento()).get(0)).getDescripcion());
					datosClienteCGAEDeUna.setIdLenguajeStr(((AdmLenguajesBean) lenguajeAdm.select("where "+AdmLenguajesBean.C_IDLENGUAJE+"="+datosClienteCGAEDeUna.getIdLenguaje()).get(0)).getDescripcion());
					datosClienteCGAEDeUna.setSanciones(Integer.toString(admSancion.getSancionesLetrado(idPersona, String.valueOf(ClsConstants.INSTITUCION_CGAE)).size()));
					datosClienteCGAEDeUna.setCertificados(Integer.toString(admCertificados.getNumeroCertificados(String.valueOf(ClsConstants.INSTITUCION_CGAE), idPersona)));
				}
				datosClienteCGAEDeAmbas.add(datosClienteCGAEDeUna);
				
				// 3. obteniendo los datos de colegiaciones y no colegiaciones
				listaColegiacionesDeUna = new Hashtable<String, Hashtable>();
				Vector<Integer> vColegiaciones = admColeg.getColegiaciones(idPersona);
				vColegiaciones.addAll(admNoColeg.getColegiaciones(idPersona));
				for (Integer idInstitucionCol : vColegiaciones) {
					datosColegiacionDeUna = new Hashtable();
					
					// obteniendo datos generales
					datosColegioDeUna = new Hashtable();
					{
						datosColegioDeUna.put("institucionColegiacion", admInstitucion.getAbreviaturaInstitucion(idInstitucionCol.toString()));
						datosColegioDeUna.put("fechaProduccion", admInstitucion.getFechaEnProduccion(idInstitucionCol.toString()));
					}
					datosColegiacionDeUna.put("datosColegio", datosColegioDeUna);
					
					// obteniendo datos del colegio
					beanColegiado = admColeg.getDatosColegiales(Long.valueOf(idPersona), idInstitucionCol);
					if (beanColegiado != null) {
						if (beanColegiado.getFechaIncorporacion() != null && !beanColegiado.getFechaIncorporacion().equalsIgnoreCase("")) {
							beanColegiado.setFechaIncorporacion(UtilidadesString.formatoFecha(beanColegiado.getFechaIncorporacion(), ClsConstants.DATE_FORMAT_JAVA, ClsConstants.DATE_FORMAT_SHORT_SPANISH));
						}
						datosColegiacionDeUna.put("datosColegiacion", beanColegiado);
	
						// obteniendo historico de estados del colegiado
						estadosColegio = admColeg.getEstadosColegiales(Long.valueOf(idPersona), idInstitucionCol, "1");
						if (estadosColegio != null && estadosColegio.size() > 0) {
							estadoUltimo = (Row) estadosColegio.get(0);
							datosColegiacionDeUna.put("estadoColegiacion", estadoUltimo);
							datosColegiacionDeUna.put("historicoEstadosColegiacion", estadosColegio);
						}
					}
					
					// obteniendo otros datos del colegiado
					hashCliente = (Hashtable) admCliente.getDatosPersonales(Long.valueOf(idPersona), idInstitucionCol).get(0);
					datosColegiacionDeUna.put("datosCliente", hashCliente);

					// obteniendo direcciones del colegiado
					listaDireccionesDeUna = admCliente.getDirecciones(Long.valueOf(idPersona), idInstitucionCol, false);//incluirBajas = false;
					for (int j = 0; j < listaDireccionesDeUna.size(); j++) {
						direccion = (Hashtable<String, String>) listaDireccionesDeUna.get(j);
						direccion.put("FECHAMODIFICACION", UtilidadesString.formatoFecha(direccion.get("FECHAMODIFICACION").toString(),
								ClsConstants.DATE_FORMAT_JAVA, ClsConstants.DATE_FORMAT_SHORT_SPANISH));
						direccion.put("TIPOSDIRECCION", direccion.get("CEN_TIPODIRECCION.DESCRIPCION").toString());
					}
					datosColegiacionDeUna.put("direcciones", listaDireccionesDeUna);
					
					listaColegiacionesDeUna.put(idInstitucionCol.toString(), datosColegiacionDeUna);
				} // fin de recorrer todas las colegiaciones y no colegiaciones
				listaColegiacionesDeAmbas.add(listaColegiacionesDeUna);

				// 4. obteniendo direcciones en CGAE
				listaDireccionesCGAEDeUna = admCliente.getDirecciones(Long.valueOf(idPersona), ClsConstants.INSTITUCION_CGAE, false);//incluirBajas = false;
				for (int j = 0; j < listaDireccionesCGAEDeUna.size(); j++) {
					direccion = (Hashtable<String, String>) listaDireccionesCGAEDeUna.get(j);
					direccion.put("FECHAMODIFICACION", UtilidadesString.formatoFecha(direccion.get("FECHAMODIFICACION").toString(),
							ClsConstants.DATE_FORMAT_JAVA, ClsConstants.DATE_FORMAT_SHORT_SPANISH));
					direccion.put("TIPOSDIRECCION", direccion.get("CEN_TIPODIRECCION.DESCRIPCION").toString());
				}
				listaDireccionesCGAEDeAmbas.add(listaDireccionesCGAEDeUna);
			}
			
			// ahora hay que separar las colegiaciones comunes de las diferentes
			{
				listaColegiacionesDiferentesDeUna = new ArrayList<Hashtable>();
				final int primeraPersona = 0;
				final int segundaPersona = 1;
				// Para cada colegiacion de la primera persona
				for (String idInstitucionCol : listaColegiacionesDeAmbas.get(primeraPersona).keySet()) {
					if (listaColegiacionesDeAmbas.get(segundaPersona).containsKey(idInstitucionCol)) {
						// si la colegiacion de la primera esta en la segunda, ambas se meten en el listado de colegiaciones comunes
						colegiacionComunDeAmbas = new Hashtable<String, ArrayList>();
						
						datosColegioDeAmbas = new ArrayList<Hashtable<String,String>>(2);
						beanColegiadoDeAmbas = new ArrayList<CenColegiadoBean>(2);
						hashClienteDeAmbas = new ArrayList<Hashtable<String,String>>(2);
						estadoUltimoDeAmbas = new ArrayList<Row>(2);
						estadosColegioDeAmbas = new ArrayList<Vector>(2);
						listaDireccionesDeAmbas = new ArrayList<Vector<Hashtable<String,String>>>();
	
						for (int iPersona = 0; iPersona < personasSeleccionadas.length; iPersona++) {
							if (iPersona == primeraPersona) {
								datosColegiacionDeUna = listaColegiacionesDeAmbas.get(primeraPersona).get(idInstitucionCol); // aqui no se puede eliminar porque fallaria el bucle
							} else {
								// se obtiene y se elimina de la segunda persona para que asi solo queden las que no estan duplicadas
								datosColegiacionDeUna = listaColegiacionesDeAmbas.get(segundaPersona).remove(idInstitucionCol);
							}
							
							datosColegioDeAmbas.add		((Hashtable<String,String>) 		datosColegiacionDeUna.get("datosColegio"));
							beanColegiadoDeAmbas.add	((CenColegiadoBean) 				datosColegiacionDeUna.get("datosColegiacion"));
							hashClienteDeAmbas.add		((Hashtable <String, String>) 		datosColegiacionDeUna.get("datosCliente"));
							estadoUltimoDeAmbas.add		((Row) 								datosColegiacionDeUna.get("estadoColegiacion"));
							estadosColegioDeAmbas.add	((Vector) 							datosColegiacionDeUna.get("historicoEstadosColegiacion"));
							listaDireccionesDeAmbas.add	((Vector<Hashtable<String,String>>) datosColegiacionDeUna.get("direcciones"));
						}
						
						colegiacionComunDeAmbas.put("institucionColegiacion", 		datosColegioDeAmbas.get(0).get("institucionColegiacion"));
						colegiacionComunDeAmbas.put("fechaProduccion", 				datosColegioDeAmbas.get(0).get("fechaProduccion"));
						colegiacionComunDeAmbas.put("datosColegiacion", 			beanColegiadoDeAmbas);
						colegiacionComunDeAmbas.put("datosCliente", 				hashClienteDeAmbas);
						colegiacionComunDeAmbas.put("estadoColegiacion", 			estadoUltimoDeAmbas);
						colegiacionComunDeAmbas.put("historicoEstadosColegiacion", 	estadosColegioDeAmbas);
						colegiacionComunDeAmbas.put("direcciones", 					listaDireccionesDeAmbas);
						
						listaColegiacionesComunesDeAmbas.add(colegiacionComunDeAmbas);
					} else {
						// si no (colegiacion independiente), se mete en el listado de colegiaciones diferentes
						datosColegiacionDeUna = listaColegiacionesDeAmbas.get(primeraPersona).get(idInstitucionCol); // aqui no se puede eliminar porque fallaria el bucle
						listaColegiacionesDiferentesDeUna.add(datosColegiacionDeUna);
					}
				}
				listaColegiacionesDiferentesDeAmbas.add(listaColegiacionesDiferentesDeUna);
				
				listaColegiacionesDiferentesDeUna = new ArrayList<Hashtable>();
				for (String idInstitucionCol : listaColegiacionesDeAmbas.get(segundaPersona).keySet()) {
					// el resto de colegiaciones de la segunda se meten entonces en el listado de colegiaciones diferentes
					datosColegiacionDeUna = listaColegiacionesDeAmbas.get(segundaPersona).get(idInstitucionCol); // aqui no se puede eliminar porque fallaria el bucle
					listaColegiacionesDiferentesDeUna.add(datosColegiacionDeUna);
				}
				listaColegiacionesDiferentesDeAmbas.add(listaColegiacionesDiferentesDeUna);
			} // separando las colegiaciones comunes de las diferentes
			
			// guardando todos los datos
			todosLosDatos = new Hashtable();
			todosLosDatos.put("datosPersonales", datosPersonaDeAmbas);
			todosLosDatos.put("datosClienteCGAE", datosClienteCGAEDeAmbas);
			todosLosDatos.put("datosColegialesIguales", listaColegiacionesComunesDeAmbas);
			todosLosDatos.put("datosColegiales", listaColegiacionesDiferentesDeAmbas);
			todosLosDatos.put("datosDirecciones", listaDireccionesCGAEDeAmbas);
			
			request.setAttribute("datos", todosLosDatos);
		}catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.censo" }, e, null);
		}
		return "gestionar";
	} // gestionar()
	
	/**
	 * Combina a 2 personas en una unica teniendo en cuenta las preferencias del usuario
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 * @throws SystemException 
	 * @throws NotSupportedException 
	 */
	protected String combinar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{

		UsrBean user = this.getUserBean(request);
		MantenimientoDuplicadosForm miForm = (MantenimientoDuplicadosForm) formulario;
		CenDireccionesAdm admDireccion = new CenDireccionesAdm(user);
		CenColegiadoAdm admColeg = new CenColegiadoAdm(user);
		CenNoColegiadoAdm admNoColeg = new CenNoColegiadoAdm(user);
		CenDatosColegialesEstadoAdm admEstadoColegial = new CenDatosColegialesEstadoAdm(user);
		CenPersonaAdm admPersona = new CenPersonaAdm(user);
		CenClienteAdm admCliente = new CenClienteAdm(user);
		CenInstitucionAdm admInst = new CenInstitucionAdm(user);

		ControlFusionador controlFusionador = null;
		String idPersonaDestino = miForm.getIdPersonaDestino();
		String idPersonaOrigen = miForm.getIdPersonaOrigen();
		HashSet<String> conjuntoColegiosIguales;
		String idInstitucion, fechaEstado;
		boolean bDesdeCgae = true;

		UserTransaction tx = null;
		
		// Eliminando el modo del formulario permitimos que el usuario pueda volver a buscar sin que haya terminado la fusion
		miForm.setModo("inicio");

		try {
			// Control de fusion de colegiados en el mismo colegio
			Vector<Integer> listaColegiacionesPersonaOrigen = admColeg.getColegiaciones(idPersonaOrigen);
			listaColegiacionesPersonaOrigen.addAll(admNoColeg.getColegiaciones(idPersonaOrigen));
			String stInstitucion;
			String nombreInstitucion;
			int intInstitucion;
			for(int i=0;i<listaColegiacionesPersonaOrigen.size();i++){
				// para cada colegiacion de la persona origen
				stInstitucion = listaColegiacionesPersonaOrigen.get(i).toString();
				nombreInstitucion = admInst.getAbreviaturaInstitucion(stInstitucion);
				intInstitucion = Integer.parseInt(stInstitucion);
				// Si se quiere fusionar un colegiado en el mismo colegio, solo lo permitimos al personal de IT o bien si el colegio no esta en produccion
				if (admColeg.existeColegiado(Long.parseLong(idPersonaDestino), intInstitucion) != null && !tienePermisoFusionColegiosEnProduccion(mapping, request) && admInst.estaEnProduccion(stInstitucion)) {
					request.setAttribute("mensaje",UtilidadesString.getMensajeIdioma(user, "No está permitida la fusión por seguridad. El colegio "+nombreInstitucion+" usa SIGA y puede contener datos delicados. Por favor, pida ayuda al Administrador."));
					return "exitoFusionar";
				}
			}

			// semaforo para evitar que se pida la fusion de la misma persona varias veces
			controlFusionador = ControlFusionador.getControlFusionador(idPersonaOrigen, idPersonaDestino);
			if (controlFusionador == null) {
				request.setAttribute("mensaje",UtilidadesString.getMensajeIdioma(user, "Ya se ha solicitado la combinación de alguna de estas personas. Espere unos minutos hasta que termine."));
				return "exitoFusionar";
			}
			
			// comprobando que existan las dos personas a fusionar, por si acaso ya no existe alguna (por ejemplo, si se ha fusionado en otro hilo de ejecucion)
			if (admPersona.getPersonaPorId(idPersonaOrigen) == null || admPersona.getPersonaPorId(idPersonaDestino) == null) {
				request.setAttribute("mensaje",UtilidadesString.getMensajeIdioma(user, "Ya se ha solicitado la combinación de alguna de estas personas. Por favor, realice una nueva búsqueda."));
				return "exitoFusionar";
			}

			tx = user.getTransactionPesada();
			tx.begin();
			
			// Como no se puede pasar al PL el listado de estados elegidos, hay que borrar los que el usuario ha deseleccionado en la interfaz.
			// Pero primero, vamos a pasar los estados existentes, para que nos salten errores en este momento.
			// TODO hay que propagar el error a la interfaz
			conjuntoColegiosIguales = new HashSet<String>();
			
			String[] estados = miForm.getListaEstados().split(",");
			String[] estado;
			if (estados.length > 0) {
				Hashtable<String, String> estadoColegialOrigen, estadoColegialDestino;
				for (int i = 0; i < estados.length; i++) {
					estado = estados[i].split("&&");
					if (estado.length > 1) {
						// recuperando el registro original
						idInstitucion = estado[0];
						fechaEstado = estado[2];
						Hashtable<String, String> pkEstado = new Hashtable<String, String>();
						pkEstado.put(CenDatosColegialesEstadoBean.C_IDPERSONA, idPersonaOrigen);
						pkEstado.put(CenDatosColegialesEstadoBean.C_IDINSTITUCION, idInstitucion);
						pkEstado.put(CenDatosColegialesEstadoBean.C_FECHAESTADO, fechaEstado);
						estadoColegialOrigen = admEstadoColegial.beanToHashTable((CenDatosColegialesEstadoBean) admEstadoColegial.selectByPK(pkEstado).get(0));

						// moviendo a la persona destino
						estadoColegialDestino = (Hashtable<String, String>) estadoColegialOrigen.clone();
						estadoColegialDestino.put(CenDatosColegialesEstadoBean.C_IDPERSONA, idPersonaDestino);
						estadoColegialDestino.put(CenHistoricoBean.C_MOTIVO, "Estado movido a persona destino antes de fusión");
						// la comprobacion la hace el metodo de insertar
						admEstadoColegial.modificacionConHistorico(estadoColegialDestino, estadoColegialOrigen, bDesdeCgae);

						// guardamos el colegio para luego borrar el resto de estados
						conjuntoColegiosIguales.add(idInstitucion);
					}
				}
			}
			// anyadiendo tambien el listado de colegios iguales de los que se ha deseleccionado todos los estados
			estados = miForm.getListaEstadosNoSeleccionados().split(",");
			if (estados.length > 0) {
				for (int i = 0; i < estados.length; i++) {
					estado = estados[i].split("&&");
					if (estado.length > 1) {
						idInstitucion = estado[0];
						conjuntoColegiosIguales.add(idInstitucion);
					}
				}
			}
			// borrando los estados de los colegios iguales (ya se movieron los que fueron seleccionados en la interfaz)
			Vector<Hashtable<String, String>> estadosColegialesEnUnColegio;
			for (String colegio : conjuntoColegiosIguales) {
				estadosColegialesEnUnColegio = admEstadoColegial.getDatosColegialesPersonaInstitucion(colegio, idPersonaOrigen);
				for (Hashtable<String, String> estadoColegial : estadosColegialesEnUnColegio) {
					admEstadoColegial.borrarConHistorico(estadoColegial, bDesdeCgae);
				}
			}
			
			// comprobando datos que no es posible fusionar y hay que arreglar a mano
			ArrayList<String> listaIdPersonas = new ArrayList<String>();
			listaIdPersonas.add(idPersonaOrigen);
			listaIdPersonas.add(idPersonaDestino);
			Vector vRegistros;
			FcsPagoColegiadoAdm pagoColAdm = new FcsPagoColegiadoAdm(user);
			ScsCabeceraGuardiasAdm cabGuaAdm = new ScsCabeceraGuardiasAdm(user);
			for (String colegio : conjuntoColegiosIguales) {
				vRegistros = pagoColAdm.selectPagosColegiadoDeVariasPersonas(colegio, listaIdPersonas);
				if (vRegistros != null && vRegistros.size() > 0) {
					tx.rollback();
					request.setAttribute("mensaje",UtilidadesString.getMensajeIdioma(user, "Ambas personas tienen registros en el mismo Pago SJCS. Por favor, consulte al Administrador."));
					return "exitoFusionar";
				}
				
				vRegistros = cabGuaAdm.getCabeceraGuardiasDeVariasPersonas(colegio, listaIdPersonas);
				if (vRegistros != null && vRegistros.size() > 0) {
					tx.rollback();
					request.setAttribute("mensaje",UtilidadesString.getMensajeIdioma(user, "Ambas personas tienen guardia en el mismo día. Por favor, consulte al Administrador."));
					return "exitoFusionar";
				}
			}
			
			// Aunque el proceso de fusion (en PL) ya se encarga de combinar las direcciones, 
			// tenemos que comprobar las unicidades. Para ello, es mejor moverlas ahora y comprobar las unicidades
			conjuntoColegiosIguales = new HashSet<String>();
			
			String[] direcciones = miForm.getListaDirecciones().split(",");
			String[] direccion;
			if (direcciones.length > 0) {
				CenDireccionesBean beanDireccion = new CenDireccionesBean();

				String idInstitucionComun, idDireccionOrigen;
				Hashtable<String, String> hashDireccion;
				List<Integer> listaTipos;
				HttpServletRequest preguntarAlUsuario = null;
				for (int i = 0; i < direcciones.length; i++) {
					direccion = direcciones[i].split("&&");
					if (direccion.length > 1) {
						idInstitucionComun = direccion[0];
						idDireccionOrigen = direccion[2];
						beanDireccion.setIdInstitucion(Integer.valueOf(idInstitucionComun));
						beanDireccion.setIdPersona(Long.valueOf(idPersonaDestino));
						
						// recuperando el registro original que se va a copiar
						hashDireccion = admDireccion.selectDirecciones(Long.valueOf(idPersonaOrigen), Integer.valueOf(idInstitucionComun), Long.valueOf(idDireccionOrigen));
						
						// comprobando los tipos
						List<String> tiposOriginales = (List<String>) Arrays.asList(((String) hashDireccion.get(CenTipoDireccionBean.C_IDTIPODIRECCION)).split(","));
						List<String> tiposAinsertar = Direccion.revisarTiposEnDireccionesExistentes(idInstitucionComun, idPersonaDestino, tiposOriginales, user);
						CenDireccionTipoDireccionBean vBeanTipoDir [];
						if (tiposAinsertar.size() == 0) {
							hashDireccion.put(CenDireccionesBean.C_FECHABAJA, "sysdate");
							// y dejamos los tipos originales
							vBeanTipoDir = Direccion.establecerTipoDireccion(tiposOriginales.toArray(new String[0]));
						} else {
							vBeanTipoDir = Direccion.establecerTipoDireccion(tiposAinsertar.toArray(new String[0]));
						}
						
						// comprobando las preferencias
						String preferencias = hashDireccion.get(CenDireccionesBean.C_PREFERENTE);
						preferencias = Direccion.revisarPreferenciasEnDireccionesExistentes(idInstitucionComun, idPersonaDestino, preferencias, user);
						hashDireccion.put(CenDireccionesBean.C_PREFERENTE, preferencias);
						
						// insertando en la persona destino
						hashDireccion.put(CenDireccionesBean.C_IDPERSONA, idPersonaDestino);
						admDireccion.insertarConHistorico((CenDireccionesBean)admDireccion.hashTableToBean(hashDireccion), vBeanTipoDir, "Dirección movida a persona destino antes de fusión", null, user.getLanguage());
						
						// guardando la institucion en la que se estan tratando direcciones
						conjuntoColegiosIguales.add(idInstitucionComun);
					}
				}
			}
			// anyadiendo tambien el listado de colegios iguales de los que se ha deseleccionado todas las direcciones
			direcciones = miForm.getListaDireccionesNoSeleccionadas().split(",");
			if (direcciones.length > 0) {
				for (int i = 0; i < direcciones.length; i++) {
					direccion = direcciones[i].split("&&");
					if (direccion.length > 1) {
						idInstitucion = direccion[0];
						conjuntoColegiosIguales.add(idInstitucion);
					}
				}
			}
			// borrando todas direcciones de las instituciones (ya se movieron las que fueron seleccionadas en la interfaz)
			boolean incluirBajas = false, validarTipos = false;
			Vector<Hashtable<String, String>> direccionesInstitucion;
			for (String idInstitucionCol : conjuntoColegiosIguales) {
				direccionesInstitucion = admDireccion.selectDirecciones(Long.valueOf(idPersonaOrigen), Integer.valueOf(idInstitucionCol), incluirBajas);
				for (Hashtable<String, String> direccionEnCGAE : direccionesInstitucion) {
					admDireccion.deleteConHistorico(direccionEnCGAE, "Dirección movida a persona destino antes de fusión", validarTipos, null);
				}
			}
			
			EjecucionPLs.ejecutarPL_fusion(idPersonaOrigen, idPersonaDestino);
			tx.commit();
			
			CenPersonaBean beanP = admPersona.getPersonaPorId(idPersonaDestino);
			String msgSalida = "Fusión completada: se encuentran todos los datos de '"+beanP.getNombreCompleto()+"' en el registro con Num. ident. '"+beanP.getNIFCIF()+"'";
			request.setAttribute("mensaje", msgSalida);
			
		} catch (Exception e) {
			throwExcp("Error en la fusión de las personas. Consulte al administrador", new String[] { "modulo.censo" }, e, tx);
		} finally {
			// ABRIMOS EL SEMAFORO. SE DEBE EJECUTAR SIEMPRE
			if (controlFusionador != null) {
				controlFusionador.removeControlFusionador();
			}
		}
		
		return "exitoFusionar";
	}
	
	/**
	 * Abre la ventana de exportacion para que el proceso se haga en background
	 */
	protected String export(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		request.setAttribute("registrosSeleccionados", request.getParameter("registrosSeleccionados"));
		return "espera";
	}
	/**
	 * Exporta un listado de duplicados completo a Excel.
	 * Este listado se genera una vez al dia (esta comprobacion se controla simplemente con el nombre del fichero)
	 * 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	private String exportar(ActionMapping mapping,
			MasterForm formulario,
			HttpServletRequest request,
			HttpServletResponse response) throws ClsExceptions, SIGAException
	{
		// Controles y variables generales
		UsrBean user = this.getUserBean(request);
		ReadProperties rp = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		SimpleDateFormat sdfDateOnly = new SimpleDateFormat("dd-MM-yyyy");

		try {
			String rutaFichero = rp.returnProperty("informes.directorioFisicoSalidaInformesJava")
					+ ClsConstants.FILE_SEP + rp.returnProperty("censo.duplicados.directorio");
			String fechaActual = sdfDateOnly.format(Calendar.getInstance().getTime());
			String nombreFichero = rutaFichero + ClsConstants.FILE_SEP + "duplicados_" + fechaActual;
			File ruta = new File(rutaFichero);
			ruta.mkdirs();

			File fichero;
			synchronized (mantenimientoDuplicados) {
				// antes de generar, se comprueba si existe ya el fichero
				fichero = new File(nombreFichero + ".zip");
				if (fichero == null || !fichero.exists()) {
					fichero = generarExportacionXLS(nombreFichero, user);
				}
			}
			
			if (fichero == null || !fichero.exists()) {
				throw new SIGAException("messages.general.error.ficheroNoExiste");
			} else {
				request.setAttribute("nombreFichero", fichero.getName());
				request.setAttribute("rutaFichero", fichero.getPath());
			}

		} catch (SIGAException e) {
			throwExcp(e.getLiteral(), e, null);

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] { "modulo.facturacionSJCS" }, e, null);
		}

		return "descargaFichero";
	} // exportar()
	
	/**
	 * Genera un fichero de exportacion con todos los datos de personas duplicadas y lo devuelve para descarga
	 * 
	 * @param nombreFichero
	 * @param user
	 * @return
	 * @throws Exception
	 */
	private File generarExportacionXLS(String nombreFichero, UsrBean user) throws Exception {
		DuplicadosHelper helper = new DuplicadosHelper();
		Vector<Hashtable<String, String>> personas;
		File ficheroGenerado, ficheroSalida;
		ArrayList<ArrayList<String>> datosPersona;

		// controles de acceso a datos
		CenPersonaAdm admPersona = new CenPersonaAdm(user);

		ArrayList<File> listaFicheros = new ArrayList<File>();
		for (int similaritud = 0; similaritud < DuplicadosHelper.similaritudes.length; similaritud++) {
			personas = helper.getPersonasSimilares(similaritud);

			// generando cada fichero
			ficheroGenerado = new File(nombreFichero + "_" + DuplicadosHelper.similaritudes[similaritud] + ".xls");
			ficheroGenerado.createNewFile();
			BufferedWriter out = new BufferedWriter(new FileWriter(ficheroGenerado));

			// escribiendo las cabeceras
			datosPersona = helper.getDatosPersonaSimilar(null, user);
			for (Iterator<String> iterColumnas = datosPersona.get(0).iterator(); iterColumnas.hasNext();) {
				out.write(iterColumnas.next() + "\t");
			}
			out.newLine();

			// escribiendo los resultados
			for (Iterator<Hashtable<String, String>> iterPersonas = personas.iterator(); iterPersonas.hasNext();) {
				datosPersona = helper.getDatosPersonaSimilar(iterPersonas.next(), user);
				for (Iterator<ArrayList<String>> iterFilas = datosPersona.iterator(); iterFilas.hasNext();) {
					for (Iterator<String> iterColumnas = iterFilas.next().iterator(); iterColumnas.hasNext();) {
						out.write(iterColumnas.next() + "\t");
					}
	
					out.newLine();
				}
			}

			// cerrando el fichero
			out.close();
			listaFicheros.add(ficheroGenerado);
			ficheroGenerado.deleteOnExit();
		}

		if (listaFicheros.size() == 0) {
			throw new SIGAException("No se ha generado ningun informe");
		}
		ficheroSalida = MasterReport.doZip(listaFicheros, nombreFichero);
		
		return ficheroSalida;
	}
	
	/**
	 * Metodo que implementa el modo ver 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String destino = "";
		
		try{	
			MantenimientoDuplicadosForm miFormulario = (MantenimientoDuplicadosForm)formulario;
			miFormulario.setModo("");
			// OBTENGO VALORES DEL FORM
			// solamente el 0 porque es el unico que he pulsado
			Vector vOcultos = miFormulario.getDatosTablaOcultos(0);
			// obtener idpersona
			String idPersona = (String)vOcultos.get(0);
			// obtener idinstitucion
			String idInstitucion;
			if (vOcultos.size()>1)
				idInstitucion = (String)vOcultos.get(1);
			else // es un no colegiado que tiene que ser obligatoriamente de CGAE. 2000
				idInstitucion = Integer.toString(ClsConstants.INSTITUCION_CGAE);
			// Obtenemos el tipo para saber si el registro es de una Sociedad o Personal:
			String tipo = ClsConstants.COMBO_TIPO_PERSONAL;
			if (vOcultos.size()==3)
				tipo = (String)vOcultos.get(2);
			
			String modo = "ver";
			Integer tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO);
			CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserBean(request));
			
			String[] pestanasOcultas=new String [1];
			
			// jbd // 12124 // Un no colegiado tiene que tener la institucion CGAE
			idInstitucion = Integer.toString(ClsConstants.INSTITUCION_CGAE);
			
			String tipoCliente = clienteAdm.getTipoCliente(new Long(idPersona), new Integer(idInstitucion));
			
			if (tipoCliente.equals(ClsConstants.TIPO_CLIENTE_COLEGIADO) || tipoCliente.equals(ClsConstants.TIPO_CLIENTE_COLEGIADO_BAJA)) {
				tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_COLEGIADO);
			}else{
				
			  
				CenPersonaAdm admPer = new CenPersonaAdm(this.getUserBean(request));
				CenPersonaBean beanPer = admPer.getIdentificador(new Long(idPersona));
				
				Hashtable  claveh=new Hashtable();
				claveh.put(CenClienteBean.C_IDPERSONA,idPersona);
				claveh.put(CenClienteBean.C_IDINSTITUCION,idInstitucion);
				
				
				Vector resultadoObj = clienteAdm.selectByPK(claveh);
				CenClienteBean obj = (CenClienteBean)resultadoObj.get(0);
				if (obj.getLetrado().equals(ClsConstants.DB_TRUE)){
					tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_LETRADO);
				}else{

				// AHORA HAY QUE COMPROBARLO POR EL TIPO DE NO COLEGIADO
				// obtengo los datos de nocolegiado
				Hashtable hashNoCol = new Hashtable();
				hashNoCol.put(CenNoColegiadoBean.C_IDINSTITUCION,idInstitucion);
				hashNoCol.put(CenNoColegiadoBean.C_IDPERSONA,idPersona);
				CenNoColegiadoAdm nocolAdm = new CenNoColegiadoAdm(this.getUserBean(request));
				Vector v = nocolAdm.selectByPK(hashNoCol);
					if (v!=null && v.size()>0) {
						CenNoColegiadoBean nocolBean = (CenNoColegiadoBean) v.get(0);
						if (!nocolBean.getTipo().equals(ClsConstants.COMBO_TIPO_PERSONAL)) {
							// SOCIEDAD
							tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO_FISICO);
						} else {
							// PERSONAL
							tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO);
						}
					}			
			   }	
			 }
			
			if (tipoAcceso.intValue()==new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO).intValue()){
				  
				  pestanasOcultas[0]=ClsConstants.IDPROCESO_REGTEL_CENSO;
				  request.setAttribute("pestanasOcultas",pestanasOcultas);
			}else{
				GenParametrosAdm parametrosAdm = new GenParametrosAdm(this.getUserBean(request));
		 		String valor = parametrosAdm.getValor(this.getUserBean(request).getLocation(), ClsConstants.MODULO_GENERAL, "REGTEL", "0");
		 		if (valor!=null && valor.equals(ClsConstants.DB_FALSE)){
				  pestanasOcultas=new String [1];
				  pestanasOcultas[0]=ClsConstants.IDPROCESO_REGTEL_CENSO;
				  request.setAttribute("pestanasOcultas",pestanasOcultas);
		 		}
			}
			Hashtable datosCliente = new Hashtable();
			datosCliente.put("accion",modo);
			datosCliente.put("idPersona",idPersona);
			datosCliente.put("idInstitucion",idInstitucion);
			datosCliente.put("tipoAcceso",String.valueOf(tipoAcceso));
			// Para ver si debemos abrir el jsp comun de colegiados o el propio para no colegiados:
			datosCliente.put("tipo",tipo);
          
			request.setAttribute("datosCliente", datosCliente);		
			
			request.getSession ().setAttribute ("CenBusquedaClientesTipo", "DUPLICADOS");  
			
			destino="administracion";
		 } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
	 	 }
		 return destino;
    }
	
	/**
	 * Metodo que implementa el modo editar 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String elementoActivo="1";
		try {
			MantenimientoDuplicadosForm miform = (MantenimientoDuplicadosForm)formulario;
			miform.setModo("");
	
			// OBTENGO VALORES DEL FORM
			// solamente el 0 porque es el unico que he pulsado
			Vector vOcultos = miform.getDatosTablaOcultos(0);
			// obtener idpersona
			String idPersona = (String)vOcultos.get(0);
			// obtener idinstitucion
			String idInstitucion;
			if (vOcultos.size()>1)
				idInstitucion = (String)vOcultos.get(1);
			else // es un no colegiado que tiene que ser obligatoriamente de CGAE. 2000
				idInstitucion = Integer.toString(ClsConstants.INSTITUCION_CGAE);
			// Obtenemos el tipo para saber si el registro es de una Sociedad o Personal:
			String tipo = ClsConstants.COMBO_TIPO_PERSONAL;
			if (vOcultos.size()==3)
				tipo = (String)vOcultos.get(2);
			
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
			String[] pestanasOcultas=new String [1];
			Integer tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO);
			
			
			CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserBean(request));
			String verFichaLetrado = request.getParameter("verFichaLetrado");
			/*if (verFichaLetrado == null){
				verFichaLetrado = miform.getVerFichaLetrado();
			}*/
			
			// jbd // 12124 // Un no colegiado tiene que tener la institucion CGAE
			idInstitucion = Integer.toString(ClsConstants.INSTITUCION_CGAE);
			
			if (verFichaLetrado!=null && verFichaLetrado.equals("1")){
				tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_LETRADO);
				elementoActivo="3";
				
			}else{
			String tipoCliente = clienteAdm.getTipoCliente(new Long(idPersona), new Integer(idInstitucion));
			
			if (tipoCliente.equals(ClsConstants.TIPO_CLIENTE_COLEGIADO) || tipoCliente.equals(ClsConstants.TIPO_CLIENTE_COLEGIADO_BAJA)) {
				tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_COLEGIADO);
			} else {
				//tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO);
				CenPersonaAdm admPer = new CenPersonaAdm(this.getUserBean(request));
				CenPersonaBean beanPer = admPer.getIdentificador(new Long(idPersona));

				Hashtable  claveh=new Hashtable();
				claveh.put(CenClienteBean.C_IDPERSONA,idPersona);
				claveh.put(CenClienteBean.C_IDINSTITUCION,idInstitucion);
				
				
				Vector resultadoObj = clienteAdm.selectByPK(claveh);
				CenClienteBean obj = (CenClienteBean)resultadoObj.get(0);
				if (obj.getLetrado().equals(ClsConstants.DB_TRUE)){//si es letrado 
					tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_LETRADO);
				}else{//si no es letrado
					// AHORA HAY QUE COMPROBARLO POR EL TIPO DE NO COLEGIADO
					// obtengo los datos de nocolegiado
					Hashtable hashNoCol = new Hashtable();
					hashNoCol.put(CenNoColegiadoBean.C_IDINSTITUCION,idInstitucion);
					hashNoCol.put(CenNoColegiadoBean.C_IDPERSONA,idPersona);
					CenNoColegiadoAdm nocolAdm = new CenNoColegiadoAdm(this.getUserBean(request));
					Vector v = nocolAdm.selectByPK(hashNoCol);
					if (v!=null && v.size()>0) {
						CenNoColegiadoBean nocolBean = (CenNoColegiadoBean) v.get(0);
						if (!nocolBean.getTipo().equals(ClsConstants.COMBO_TIPO_PERSONAL)) {
							// SOCIEDAD
							if (nocolBean.getSociedadSJ().equals("1") || (nocolBean.getSociedadSP().equals("1")) ){
								tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO_FISICO);
							}else{
								tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO);
							}
						} else {
							// PERSONAL
							tipoAcceso = new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO);
						}
					}
			  }	
				
			}
			}
			
			
			if (tipoAcceso.intValue()==new Integer(ClsConstants.TIPO_ACCESO_PESTANAS_NOCOLEGIADO).intValue()){
				  
				  pestanasOcultas[0]=ClsConstants.IDPROCESO_REGTEL_CENSO;
				  request.setAttribute("pestanasOcultas",pestanasOcultas);
			}else{
				GenParametrosAdm parametrosAdm = new GenParametrosAdm(this.getUserBean(request));
		 		String valor = parametrosAdm.getValor(this.getUserBean(request).getLocation(), ClsConstants.MODULO_GENERAL, "REGTEL", "0");
		 		if (valor!=null && valor.equals(ClsConstants.DB_FALSE)){
				  pestanasOcultas=new String [1];
				  pestanasOcultas[0]=ClsConstants.IDPROCESO_REGTEL_CENSO;
				  request.setAttribute("pestanasOcultas",pestanasOcultas);
		 		}
			}
			String modo = "editar";
			Hashtable datosCliente = new Hashtable();
			datosCliente.put("accion",modo);
			datosCliente.put("idPersona",idPersona);
			datosCliente.put("idInstitucion",idInstitucion);
			request.setAttribute("elementoActivo",elementoActivo);    
			datosCliente.put("tipoAcceso",String.valueOf(tipoAcceso));
			// Para ver si debemos abrir el jsp comun de colegiados o el propio para no colegiados:
			datosCliente.put("tipo",tipo);
	
			request.setAttribute("datosCliente", datosCliente);		
			if(request.getParameter("volver") != null && "MD".equalsIgnoreCase(request.getParameter("volver"))){
				request.getSession ().setAttribute ("CenBusquedaClientesTipo", "MD");  
			}else{
				request.getSession ().setAttribute ("CenBusquedaClientesTipo", "DUPLICADOS");  
			}
			
			 
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}	
		return "administracion";
	}
	
	/**
	 * Metodo que implementa el modo abrirConParametros
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action 
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrirConParametros (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException 
	{
		try {

			// obtengo la visibilidad para el user
			//String visibilidad = obtenerVisibilidadUsuario(request);
			//request.setAttribute("CenInstitucionesVisibles",visibilidad);

			// miro a ver si tengo que ejecutar 
			//la busqueda una vez presentada la pagina
			String buscar = request.getParameter("buscar");
			request.setAttribute("buscar",buscar);
			UsrBean usr = this.getUserBean(request);
			String parametros = usr.getLocation();
			CenInstitucionAdm admInstitucion = new CenInstitucionAdm(usr); 
			List<CenInstitucionBean> listadoInstituciones = admInstitucion.getNombreColegiosTodos(parametros);
			request.setAttribute("listadoInstituciones", listadoInstituciones);
			
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		
		return "inicio";
	}
	
	/**
	 *Se Verifica que el usuario tiene permiso de Archivazión.
	 * @param form Formulario con los criterios
	 * @return se muestra un resultado con un numero si tiene permiso.
	 * @throws ClsExceptions
	 */
	public boolean tienePermisoFusionColegiosEnProduccion(ActionMapping mapping, HttpServletRequest request) throws ClsExceptions {
		boolean permiso = false;
		try {

			String accessEnvio = testAccess(request.getContextPath() + "/CEN_FusionColegiosEnProduccion.do", null, request);
			if (accessEnvio.equals(SIGAConstants.ACCESS_FULL)) {
				permiso = true;
			}
			// Hacemos lo siguiente para setear el permiso de esta accion
			testAccess(request.getContextPath() + mapping.getPath() + ".do", null, request);

		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al obtener el permiso para fusionar personas en colegios en produccion.");
		}
		return permiso;
	} // tienePermisoFusionColegiosEnProduccion()
	
	/**
	 * Obtiene los duplicados de una persona
	 * @param request
	 * @param response
	 */
	private void getAjaxObtenerDuplicados(HttpServletRequest request, HttpServletResponse response) throws Exception {	

		// Controles generales
		UsrBean user = this.getUserBean(request);
		DuplicadosHelper helper = new DuplicadosHelper();
		CenColegiadoAdm admColeg = new CenColegiadoAdm(user);
		MantenimientoDuplicadosForm miFormulario = new MantenimientoDuplicadosForm();
		
		// Variables generales
		StringBuilder tableHeader = new StringBuilder(), tableBody = new StringBuilder();
		Vector personasSimilares = new Vector();
		ArrayList<String> aOptionsListadoDocumentacion = null;
		String idPersonaActual = request.getParameter("idPersona");
		idPersonaActual = (idPersonaActual == null) ? "" : idPersonaActual;

		
		// buscando duplicados por NIF parecido
		miFormulario.setNifcif(request.getParameter("nidSolicitante"));
		personasSimilares.addAll(helper.getPersonasSimilares(miFormulario));
		miFormulario.setNifcif("");

		// buscando duplicados por Numero colegiado
		String numCol = request.getParameter("nColegiado");
		if (numCol != null && ! numCol.equalsIgnoreCase("")) {
			miFormulario.setIdInstitucion(request.getParameter("idInstitucion"));
			miFormulario.setNumeroColegiado(numCol);
			personasSimilares.addAll(helper.getPersonasSimilares(miFormulario));
		} else {
			Vector<CenColegiadoBean> listaColegiacionesPersonaOrigen = admColeg.getColegiacionesCompletas(idPersonaActual);
			for(CenColegiadoBean beanColegiado : listaColegiacionesPersonaOrigen){
				// para cada colegiacion de la persona
				miFormulario.setIdInstitucion(beanColegiado.getIdInstitucion().toString());
				miFormulario.setNumeroColegiado(beanColegiado.getNumCol().toString());
				personasSimilares.addAll(helper.getPersonasSimilares(miFormulario));
			}
		}
		miFormulario.setIdInstitucion("");
		miFormulario.setNumeroColegiado("");

		// buscando duplicados por Nombre y apellidos
		miFormulario.setNombre(request.getParameter("nombre"));
		miFormulario.setApellidos(request.getParameter("apellidos"));
		personasSimilares.addAll(helper.getPersonasSimilares(miFormulario));
		miFormulario.setNombre("");
		miFormulario.setApellidos("");
		
		if (personasSimilares != null && personasSimilares.size() > 1) {
			tableHeader.append("<tr>");
			tableHeader.append("  <td><strong>Nif</strong></td>");
			tableHeader.append("  <td><strong>Nombre</strong></td>");
			tableHeader.append("  <td><strong>Apellido1</strong></td>");
			tableHeader.append("  <td><strong>Apellido2</td><strong></td>");
			tableHeader.append("  <td><strong>Colegio</strong></td>");
			tableHeader.append("  <td><strong>Nº de colegiado</strong></td>");
			tableHeader.append("  <td>&nbsp;</td>");
			tableHeader.append("</tr>");

			Hashtable registro;
			HashSet<String> conjuntoPersonas = new HashSet<String>();
			for (int i = 0; i < personasSimilares.size(); i++) {
				registro = (Hashtable) personasSimilares.elementAt(i);
				// solo hay que mostrar personas diferentes a la actual
				if (! idPersonaActual.equalsIgnoreCase((String) registro.get("IDPERSONA"))) {
					conjuntoPersonas.add((String) registro.get("IDPERSONA"));
	
					tableBody.append("<tr>");
					tableBody.append("  <td>");
					tableBody.append((String) registro.get("NIFCIF"));
					tableBody.append("  </td>");
					tableBody.append("  <td>");
					tableBody.append((String) registro.get("NOMBRE"));
					tableBody.append("  </td>");
					tableBody.append("  <td>");
					tableBody.append((String) registro.get("APELLIDOS1"));
					tableBody.append("  </td>");
					tableBody.append("  <td>");
					tableBody.append((String) registro.get("APELLIDOS2"));
					tableBody.append("  </td>");
					tableBody.append("  <td>");
					tableBody.append((registro.get("ABREVIATURA") == null) ? "&nbsp;" : (String) registro.get("ABREVIATURA"));
					tableBody.append("  </td>");
					tableBody.append("  <td>");
					tableBody.append((registro.get("NCOLEGIADO") == null) ? "&nbsp;" : (String) registro.get("NCOLEGIADO"));
					tableBody.append("  </td>");
					tableBody.append("  <td>");
					tableBody.append("    <img id='iconoboton_informacionLetrado1' src='/SIGA/html/imagenes/binformacionLetrado_off.gif'");
					tableBody.append("         style='cursor:pointer;' alt='Información letrado' class='botonesIcoTabla' ");
					tableBody.append("         name='iconoFila' border='0' onClick=\"informacionLetrado(");
					tableBody.append((String) registro.get("IDPERSONA"));
					tableBody.append(", 2000);\">");
					tableBody.append("    <img id='iconoboton_informacionLetrado1' src='/SIGA/html/imagenes/bconsultar_on.gif'");
					tableBody.append("         style='cursor:pointer;' alt='Mantenimiento duplicados' class='botonesIcoTabla' ");
					tableBody.append("         name='iconoFila' border='0' onClick=\"mantenimientoDuplicados(");
					
					if (registro.get("NCOLEGIADO") != null) {
						tableBody.append("'', '" + (String) registro.get("NCOLEGIADO") + "', '" + (String) registro.get("IDINSTITUCION") + "', '', '', ''");
					}
					else if (registro.get("APELLIDOS") != null) {
						tableBody.append("'', '', '', '" + (String) registro.get("NOMBRE") + "','" + (String) registro.get("APELLIDOS1")
							+ "','" + (String) registro.get("APELLIDOS2") + "'");
					}
					else {
						tableBody.append("'" + (String) registro.get("NIFCIF") + "', '', '', '', '', ''");
					}
					
					tableBody.append(");\">");
					tableBody.append("  </td>");
					tableBody.append("</tr>");
				}
			}
			
			aOptionsListadoDocumentacion = new ArrayList<String>();
			if (tableBody != null && !"".equalsIgnoreCase(tableBody.toString())) {
				aOptionsListadoDocumentacion.add(tableHeader.toString() + tableBody.toString());
			}
		}

		JSONObject json = new JSONObject();
		UsrBean usr = this.getUserBean(request);

		// Devuelvo la lista de series de facturacion
		json.put("aOptionsListadoDocumentacion", aOptionsListadoDocumentacion);

		response.setContentType("text/x-json;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Content-Type", "application/json");
		response.setHeader("X-JSON", json.toString());
		response.getWriter().write(json.toString());
		
	} //getAjaxObtenerDuplicados()

}
