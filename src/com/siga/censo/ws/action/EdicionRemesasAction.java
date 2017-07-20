package com.siga.censo.ws.action;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.AppConstants.ECOM_CEN_MAESESTADOENVIO;
import org.redabogacia.sigaservices.app.AppConstants.MODULO;
import org.redabogacia.sigaservices.app.AppConstants.PARAMETRO;
import org.redabogacia.sigaservices.app.autogen.model.EcomCenColegiado;
import org.redabogacia.sigaservices.app.autogen.model.EcomCenDatos;
import org.redabogacia.sigaservices.app.autogen.model.EcomCenDatosExample;
import org.redabogacia.sigaservices.app.autogen.model.EcomCenDatosExample.Criteria;
import org.redabogacia.sigaservices.app.autogen.model.EcomCenWsEnvio;
import org.redabogacia.sigaservices.app.autogen.model.GenParametros;
import org.redabogacia.sigaservices.app.services.cen.CenWSService;
import org.redabogacia.sigaservices.app.services.cen.EcomCenWsEnvioService;
import org.redabogacia.sigaservices.app.services.ecom.EcomColaService;
import org.redabogacia.sigaservices.app.services.gen.GenParametrosService;
import org.redabogacia.sigaservices.app.vo.EcomCenColegiadoVO;

import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.paginadores.PaginadorVector;
import com.siga.beans.CenInstitucionAdm;
import com.siga.censo.ws.form.EdicionColegiadoForm;
import com.siga.censo.ws.form.EdicionRemesaForm;
import com.siga.censo.ws.util.CombosCenWS;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

import es.satec.businessManager.BusinessManager;

public class EdicionRemesasAction extends MasterAction {
	
	public static final String DATAPAGINADOR = "DATAPAGINADOR_LISTADO_COLEGIADOS";
	
	private static final Logger log = Logger.getLogger(EdicionRemesasAction.class);
	
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

		try { 
			
			miForm = (MasterForm) formulario;
			if (miForm != null) {
				String accion = miForm.getModo();
				

				if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){						
					request.getSession().removeAttribute(DATAPAGINADOR);
					mapDestino = abrir(mapping, miForm, request, response);
				} else if (accion.equalsIgnoreCase("buscarInit")){					
					request.getSession().removeAttribute(DATAPAGINADOR);						
					mapDestino = buscarPor(mapping, miForm, request, response);					
				} else if (accion.equalsIgnoreCase("erroresCarga")){							
					mapDestino = erroresCarga(mapping, miForm, request, response);
				} else if (accion.equalsIgnoreCase("generaExcel")){							
					mapDestino = generaExcel(mapping, miForm, request, response);					
				} else if (accion.equalsIgnoreCase("actualizarCenso")){							
					mapDestino = actualizarCenso(mapping, miForm, request, response);
				} else if (accion.equalsIgnoreCase("actualizarCensoProgramado")){							
					mapDestino = actualizarCensoProgramado(mapping, miForm, request, response);
				} else {
					return super.executeInternal(mapping,formulario,request,response);
				}
			}

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

	
	protected String buscarPor (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		
		try {
			EdicionRemesaForm edicionRemesaForm = (EdicionRemesaForm) formulario;			
			HashMap databackup = new HashMap();

			if (request.getSession().getAttribute(DATAPAGINADOR) != null) {
				databackup = (HashMap) request.getSession().getAttribute(DATAPAGINADOR);
				PaginadorVector<EdicionColegiadoForm> paginador = (PaginadorVector<EdicionColegiadoForm>) databackup.get("paginador");
				List<EdicionColegiadoForm> datos = new ArrayList<EdicionColegiadoForm>();

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
				List<EdicionColegiadoForm> datos = getDatos(edicionRemesaForm);																	
				
				PaginadorVector<EdicionColegiadoForm> paginador = new PaginadorVector(datos);
				int totalRegistros = paginador.getNumeroTotalRegistros();

				if (totalRegistros == 0) {
					paginador = null;
				}

				databackup.put("paginador", paginador);
				if (paginador != null) {
					datos = paginador.obtenerPagina(1);
					databackup.put("datos", datos);
					request.getSession().setAttribute(DATAPAGINADOR, databackup);
					request.getSession().setAttribute("HORABUSQUEDA", UtilidadesBDAdm.getFechaCompletaBD("es"));
				}
			}				
			
		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		return "resultado";
	}


	private List<EdicionColegiadoForm> getDatos(EdicionRemesaForm form) throws ParseException {
		List<EdicionColegiadoForm> datos = new ArrayList<EdicionColegiadoForm>();
		CenWSService cenWSService = (CenWSService) BusinessManager.getInstance().getService(CenWSService.class);
		
		EcomCenDatosExample ecomCenDatosExample = new EcomCenDatosExample();
		Criteria datosCriteria = ecomCenDatosExample.createCriteria();
		
		rellenaFiltroDatos(datosCriteria, form);
		
		String idincidencia = null;
		if (isNotnull(form.getIdincidencia())) {
			idincidencia = form.getIdincidencia();
		}
		
		ecomCenDatosExample.orderByApellido1();
		ecomCenDatosExample.orderByApellido2();
		ecomCenDatosExample.orderByNombre();
		
		List<EcomCenColegiadoVO> ecomColegiadoVOs = cenWSService.getEcomCenDatosList(form.getIdcensowsenvio(), ecomCenDatosExample, idincidencia, form.isModificado());
		
		if (ecomColegiadoVOs != null) {
			for (EcomCenColegiadoVO ecomCenColegiadoVO : ecomColegiadoVOs) {
				EcomCenDatos ecomCenDato = ecomCenColegiadoVO.getEcomCenDatos();
				EcomCenColegiado ecomCenColegiado = ecomCenColegiadoVO.getEcomCenColegiado();
				
				EdicionColegiadoForm edicionColegiadoForm = new EdicionColegiadoForm();
				edicionColegiadoForm.setIdcensodatos(ecomCenDato.getIdcensodatos());
				edicionColegiadoForm.setNcolegiado(ecomCenDato.getNcolegiado());
				edicionColegiadoForm.setNombre(ecomCenDato.getNombre());
				edicionColegiadoForm.setApellido1(ecomCenDato.getApellido1());
				edicionColegiadoForm.setApellido2(ecomCenDato.getApellido2());
								
				short idestadocolegiado = ecomCenDato.getIdestadocolegiado();
				edicionColegiadoForm.setIdestadocolegiado(idestadocolegiado);
				
				if (idestadocolegiado==AppConstants.ECOM_CEN_MAESESTADOCOLEGIAL.ALTA_COLEGIADO.getCodigo()
						|| idestadocolegiado==AppConstants.ECOM_CEN_MAESESTADOCOLEGIAL.ALTA_PERSONA_COLEGIADO.getCodigo()
						|| idestadocolegiado==AppConstants.ECOM_CEN_MAESESTADOCOLEGIAL.ACTUALIZACION_COLEGIADO.getCodigo()
						|| idestadocolegiado==AppConstants.ECOM_CEN_MAESESTADOCOLEGIAL.ACTUALIZACION_COLEGIADO_MENOS_NUMERO_DOCUMENTO.getCodigo()
						|| idestadocolegiado==AppConstants.ECOM_CEN_MAESESTADOCOLEGIAL.ARCHIVADO.getCodigo()) {
					
					edicionColegiadoForm.setIdpersona(ecomCenColegiado.getIdpersona());
					edicionColegiadoForm.setIdinstitucion(ecomCenColegiado.getIdinstitucion());
				}				
			
				datos.add(edicionColegiadoForm);
			}
		}
		
		return datos;

	}
	
	private void rellenaFiltroDatos(Criteria datosCriteria,	EdicionRemesaForm form) {
		
		//numero de colegiado
		if (isNotnull(form.getNumeroColegiado())) {			
			datosCriteria.andNcolegiadoUpperLike(getCampoLike(form.getNumeroColegiado().trim()));
		}
		//nombre
		if (isNotnull(form.getNombre())) {
			datosCriteria.andNombreUpperLike(getCampoLike(form.getNombre().trim()));
		}
		//apellido 1
		if (isNotnull(form.getPrimerApellido())) {
			datosCriteria.andApellido1UpperLike(getCampoLike(form.getPrimerApellido().trim()));
		}
		//apellido 2
		if (isNotnull(form.getSegundoApellido())) {
			datosCriteria.andApellido2UpperLike(getCampoLike(form.getSegundoApellido().trim()));
		}
		//tipo identificación
		if (isNotnull(form.getIdTipoIdentificacion())) {
			datosCriteria.andIdcensotipoidentificacionEqualTo(Short.valueOf(form.getIdTipoIdentificacion().trim()));
		}
		//identificación
		if (isNotnull(form.getIdentificacion())) {
			datosCriteria.andNumdocumentoUpperLike(getCampoLike(form.getIdentificacion().trim()));
		}
		//estado colegiado
		if (isNotnull(form.getIdestadocolegiado())) {
			datosCriteria.andIdestadocolegiadoEqualTo(Short.valueOf(form.getIdestadocolegiado()));
		}
		
	}


	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return verEditar("ver", formulario, request);		
	}
	
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return verEditar("editar", formulario, request);		
	}
	
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return eliminaRemesa(formulario, request);
	}
	
	
	protected String actualizarCenso(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		actualizaCenso(formulario, null);		
//		return exitoRefresco("messages.actualizar.censo", request);
		request.setAttribute("mensaje", "messages.actualizar.censo");
		return verEditar("ver", formulario, request);
	}
	
	protected String actualizarCensoProgramado(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			EdicionRemesaForm edicionRemesaForm = (EdicionRemesaForm) formulario;
			
			GenParametros genParametros = new GenParametros();
			genParametros.setModulo(MODULO.CEN.name());
			genParametros.setParametro(PARAMETRO.EXCEL_HORA_ACTUALIZA_CENSO.name());
			genParametros.setIdinstitucion(edicionRemesaForm.getIdinstitucion());
			
			GenParametrosService genParametrosService = (GenParametrosService) getBusinessManager().getService(GenParametrosService.class);
			genParametros = genParametrosService.getGenParametroInstitucionORvalor0(genParametros);
			
			String value = genParametros.getValor();
			log.debug("La hora recibido por parámetro es: " + value);
			
			String[] horaMin = value.split(":");
			String hora = horaMin[0];
			String min = horaMin[1];
			
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hora));
			cal.set(Calendar.MINUTE, Integer.parseInt(min));
			
			if (Calendar.getInstance().getTimeInMillis() > cal.getTimeInMillis()) {
				cal.add(Calendar.DAY_OF_MONTH, 1);
			}
		
			actualizaCenso(formulario, cal.getTime());
		} catch (Exception e) {
			throwExcp("messages.general.error", e, null);
		}
				
//		return exitoRefresco("messages.actualizar.censoProgramado", request);
		request.setAttribute("mensaje", "messages.actualizar.censoProgramado");
		return verEditar("ver", formulario, request);
	}
	
	
	private void actualizaCenso(MasterForm formulario, Date fechaEjecucion) throws SIGAException {
		try {
			
			EdicionRemesaForm edicionRemesaForm = (EdicionRemesaForm) formulario;
			
			if (AppConstants.ECOM_CEN_MAESESTADOENVIO.PENDIENTE.getCodigo() == edicionRemesaForm.getIdEstadoenvio() 
					|| AppConstants.ECOM_CEN_MAESESTADOENVIO.PROCESADO.getCodigo() == edicionRemesaForm.getIdEstadoenvio()) {
			
				//creamos el mapa de parámetros
				Map<String, String> mapa = new HashMap<String, String>();
				mapa.put(EcomCenWsEnvio.C_IDCENWSENVIO, edicionRemesaForm.getIdcensowsenvio().toString());
				mapa.put(AppConstants.ENVIO_MAIL, Boolean.TRUE.toString());
				
				EcomColaService ecomColaService = (EcomColaService) getBusinessManager().getService(EcomColaService.class);	
				ecomColaService.insertaColaProcesarEnvioCensoProgramado(mapa, edicionRemesaForm.getIdcensowsenvio(), fechaEjecucion);
				
				edicionRemesaForm.setIdEstadoenvio(AppConstants.ECOM_CEN_MAESESTADOENVIO.PROCESANDO.getCodigo());
				edicionRemesaForm.setAccion("ver");
			}
		} catch (Exception e) {
			throwExcp("messages.general.error", e, null);
		}
		
	}


	protected String generaExcel(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		try {
			Vector datos = new Vector();
			EdicionRemesaForm edicionRemesaForm = (EdicionRemesaForm) formulario;
			
			String[] cabeceras = new String[]{"COLEGIO"
			       	, "FECHA_PETICION"
	           		, "PUBLICAR_COLEGIADO"
			       	, "N_COLEGIADO"
			       	, "NOMBRE"
			       	, "APELLIDO_1"
			       	, "APELLIDO_2"
	           		, "FECHA_NACIMIENTO"
	           		, "TIPO_IDENTIFICACION"
			       	, "NUM_DOCUMENTO"
	           		, "PUBLICAR_TELEFONO"
	           		, "TELEFONO"
	           		, "PUBLICAR_MOVIL"
	           		, "TELEFONO_MOVIL"
	           		, "PUBLICAR_FAX"
	           		, "FAX"				
	           		, "PUBLICAR_EMAIL"
	           		, "EMAIL"
	           		, "SITUACION"
			       	, "FECHA_SITUACION"
	           		, "RESIDENTE"
	           		, "ESTADO"       
			       	, "INCIDENCIA"
			       	, "DETALLE_INCIDENCIA"
	           		, "FECHA_INCORPORACION"};
	        
	        CenWSService cenWSService = (CenWSService) BusinessManager.getInstance().getService(CenWSService.class);
			
			EcomCenDatosExample ecomCenDatosExample = new EcomCenDatosExample();
			Criteria datosCriteria = ecomCenDatosExample.createCriteria();
			
			rellenaFiltroDatos(datosCriteria, edicionRemesaForm);
			
			String idincidencia = null;
			if (isNotnull(edicionRemesaForm.getIdincidencia())) {
				idincidencia = edicionRemesaForm.getIdincidencia();
			}
			
			ecomCenDatosExample.orderByApellido1();
			ecomCenDatosExample.orderByApellido2();
			ecomCenDatosExample.orderByNombre();
			
			List<Hashtable<String, Object>> listaResultados = cenWSService.getDatosExcel(edicionRemesaForm.getIdcensowsenvio(), ecomCenDatosExample, idincidencia, edicionRemesaForm.isModificado());
	    	
	    	datos.addAll(listaResultados);
	    	
	    	String nombreFichero = request.getParameter("nombreFichero");
	    
			request.setAttribute("campos",cabeceras);
			request.setAttribute("datos",datos);
			request.setAttribute("cabeceras",cabeceras);
			request.setAttribute("descripcion", nombreFichero);
			
		} catch (Exception e) {
			throwExcp("messages.general.error", e, null);
		}
		
		return "generaExcel";		
	}
	
	
	protected String erroresCarga(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			CenWSService cenWSService = (CenWSService) BusinessManager.getInstance().getService(CenWSService.class);
			EdicionRemesaForm edicionRemesaForm = (EdicionRemesaForm) formulario;
			Long idcensowsenvio = edicionRemesaForm.getIdcensowsenvio();
			
			List<String> listaErrores = cenWSService.getListaErrores(idcensowsenvio);
			edicionRemesaForm.setListaErrores(listaErrores);
			
			
			
		} catch (Exception e) {
			throwExcp("messages.general.error", e, null);
		}
		return "erroresCarga";		
	}
	
	


	private String verEditar(String accion, MasterForm formulario, HttpServletRequest request) throws SIGAException {
		try {
			
			EdicionRemesaForm edicionRemesaForm = (EdicionRemesaForm) formulario;
			HttpSession session = request.getSession();
			
			if (request.getParameter("volver") == null) {				
				edicionRemesaForm.reset();				
			}
			
			//debemos siempre borrar la paginación al editar o dar a volver 
			//pq al volver puede que cambie el colegiado y no debe estar en memoria el antiguo
			session.removeAttribute(DATAPAGINADOR);
			
			
			// Recuperamos los datos del registro que hemos seleccionado
			Vector ocultos = edicionRemesaForm.getDatosTablaOcultos(0);
			
			Hashtable miHash = new Hashtable();
			
			Long idcensowsenvio = null;
			
			if (ocultos != null && ocultos.size() > 0) {
				idcensowsenvio = Long.valueOf(ocultos.get(0).toString());
			} else {
				throw new IllegalArgumentException("No se ha recibido el identificador para editar la remesa");
			}
					
			CenWSService cenWSService = (CenWSService) BusinessManager.getInstance().getService(CenWSService.class);
			EcomCenWsEnvio ecomCenWsEnvio = cenWSService.getEcomCenWsEnvioByPk(idcensowsenvio);
			
			CenInstitucionAdm institucionAdm = new CenInstitucionAdm(getUserBean(request));
			edicionRemesaForm.setIdcensowsenvio(ecomCenWsEnvio.getIdcenwsenvio());
			edicionRemesaForm.setNombreColegio(institucionAdm.getNombreInstitucion(ecomCenWsEnvio.getIdinstitucion().toString()));
			edicionRemesaForm.setIdinstitucion(ecomCenWsEnvio.getIdinstitucion());
			
			edicionRemesaForm.setNumeroPeticion(ecomCenWsEnvio.getNumeropeticion());
			edicionRemesaForm.setFechapeticion(GstDate.getFormatedDateShort(ecomCenWsEnvio.getFechacreacion()));
			
			edicionRemesaForm.setConerrores(ecomCenWsEnvio.getConerrores());
			edicionRemesaForm.setListaErrores(cenWSService.getListaErrores(idcensowsenvio));
			
			CombosCenWS combosCenWS = new CombosCenWS();
			 			
			edicionRemesaForm.setTiposIdentificacion(combosCenWS.getTiposIdentificacion(getUserBean(request)));
			edicionRemesaForm.setEstadosColegiado(combosCenWS.getEstadosColegiado(getUserBean(request)));
			edicionRemesaForm.setIncidenciasColegiado(combosCenWS.getIncidencaisColegiado(getUserBean(request)));
			
			edicionRemesaForm.setIdEstadoenvio(ecomCenWsEnvio.getIdestadoenvio());
			
			if (ECOM_CEN_MAESESTADOENVIO.PROCESANDO.getCodigo() == ecomCenWsEnvio.getIdestadoenvio()) {
				accion = "ver";
			}
			
			request.getSession().setAttribute("CenBusquedaClientesTipo", "CARGA_CENSO_WS");
			
			edicionRemesaForm.setAccion(accion);
						
		} catch (Exception e) {
			throwExcp("messages.general.error", e, null);
		}
		return accion;
		
	}


	private String eliminaRemesa(MasterForm formulario, HttpServletRequest request){
	
		EdicionRemesaForm edicionRemesaForm = (EdicionRemesaForm) formulario;
		
		// Recuperamos los datos del registro que hemos seleccionado
		Vector ocultos = edicionRemesaForm.getDatosTablaOcultos(0);
		
		Long idcensowsenvio = null;
		
		if (ocultos != null && ocultos.size() > 0) {
			idcensowsenvio = Long.valueOf(ocultos.get(0).toString());
		} else {
			throw new IllegalArgumentException("No se ha recibido el identificador para editar la remesa");
		}
		
		EcomCenWsEnvioService ecomCenWsEnvioService = (EcomCenWsEnvioService) BusinessManager.getInstance().getService(EcomCenWsEnvioService.class);		
		ecomCenWsEnvioService.eliminarRemesa(idcensowsenvio);
		
		return exitoRefresco("messages.processed.success.deleteRemesa", request);
		
	}
	
	

	private boolean isNotnull(String value) {		
		return value != null && !value.trim().equals("");
	}
	
	private boolean isNotnull(Short value) {		
		return value != null;
	}


	private String getCampoLike(String value) {		
		return AppConstants.TANTO_POR_CIENTO + value + AppConstants.TANTO_POR_CIENTO;
	}
			
	
}
