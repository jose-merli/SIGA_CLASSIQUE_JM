package com.siga.censo.ws.action;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.AppConstants.MODULO;
import org.redabogacia.sigaservices.app.AppConstants.PARAMETRO;
import org.redabogacia.sigaservices.app.autogen.model.EcomCenDatosExample;
import org.redabogacia.sigaservices.app.autogen.model.EcomCenWsEnvioExample;
import org.redabogacia.sigaservices.app.autogen.model.GenParametros;
import org.redabogacia.sigaservices.app.exceptions.ValidationException;
import org.redabogacia.sigaservices.app.model.EcomCenWsEnvioExtended;
import org.redabogacia.sigaservices.app.services.cen.CenInstitucionService;
import org.redabogacia.sigaservices.app.services.cen.CenWSService;
import org.redabogacia.sigaservices.app.services.gen.GenParametrosService;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.GstDate;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.paginadores.PaginadorVector;
import com.siga.censo.ws.form.BusquedaRemesasForm;
import com.siga.censo.ws.form.ConfiguracionPerfilColegioForm;
import com.siga.censo.ws.form.EdicionRemesaForm;
import com.siga.censo.ws.util.CombosCenWS;
import com.siga.comun.vos.InstitucionVO;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

import es.satec.businessManager.BusinessManager;

public class BusquedaRemesasAction extends MasterAction {	
	
	public static final String DATAPAGINADOR = "DATAPAGINADOR_BUSQUEDA_REMESAS";
	
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
				return super.executeInternal(mapping,formulario,request,response);				
			}

			// Redireccionamos el flujo a la JSP correspondiente
			return mapping.findForward(mapDestino);
		} catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.censo"});
		}
	}
	
	protected String abrir (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException {
		ClsLogging.writeFileLog("BusquedaRemesasAction.abrir() - INICIO", 3);
		BusquedaRemesasForm busquedaRemesasForm = (BusquedaRemesasForm) formulario;
		busquedaRemesasForm.reset();
		request.getSession().removeAttribute(DATAPAGINADOR);
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -2);
				
		busquedaRemesasForm.setFechaPeticionDesde(AppConstants.DATE_FORMAT.format(cal.getTime()));
		ClsLogging.writeFileLog("BusquedaRemesasAction.abrir() - FIN", 3);
		return abrirAvanzada(mapping, formulario, request, response);
		
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
	protected String abrirAvanzada (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException
	{
		try {
			ClsLogging.writeFileLog("BusquedaRemesasAction.abrirAvanzada() - INICIO", 3);
			BusquedaRemesasForm busquedaRemesasForm = (BusquedaRemesasForm) formulario;
			
			request.getSession().removeAttribute(DATAPAGINADOR);
			
			CombosCenWS combosCenWS = new CombosCenWS();
			
			List<InstitucionVO> listInstitucionVOs = getColegiosDependientes(getUserBean(request).getLocation());
			busquedaRemesasForm.setInstituciones(listInstitucionVOs);
			busquedaRemesasForm.setTiposIdentificacion(combosCenWS.getTiposIdentificacion(getUserBean(request)));
			
			ClsLogging.writeFileLog("BusquedaRemesasAction.abrirAvanzada() - Obtengo el listado de instituciones", 3);
			List<InstitucionVO> listInstitucionWsVos = getColegiosWS();
			//CENSO-297@DTT.JAMARTIN@10/06/2022@INICIO
			List<InstitucionVO> listInstitucionVos = getColegios();
			//CENSO-297@DTT.JAMARTIN@10/06/2022@FIN
			
			if (listInstitucionVOs != null) {
				Map<String, String> mapa = new HashMap<String, String>();
				for (InstitucionVO institucionVO : listInstitucionVOs) {
					mapa.put(institucionVO.getId(), institucionVO.getNombre());
				}
				busquedaRemesasForm.setMapaInstituciones(mapa);
			}
			
			//CENSO-297@DTT.JAMARTIN@10/06/2022@INICIO
			if(listInstitucionWsVos!=null){
				ClsLogging.writeFileLog("BusquedaRemesasAction.abrirAvanzada() - Asignamos las instituciones al combobox de colegios con carga automática y manual", 3);
				busquedaRemesasForm.setInstitucionesWS(listInstitucionWsVos);
			}
			
			if (listInstitucionVos != null) {
				ClsLogging.writeFileLog("BusquedaRemesasAction.abrirAvanzada() - Asignamos las instituciones al combobox de colegios con carga manual", 3);
				busquedaRemesasForm.setInstituciones(listInstitucionVos);
			}
			//CENSO-297@DTT.JAMARTIN@10/06/2022@FIN
			
			String buscar = request.getParameter("buscar");
			request.setAttribute("buscar",buscar);
		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		
		ClsLogging.writeFileLog("BusquedaRemesasAction.abrirAvanzada() - FIN", 3);
		return "inicio";
	}
	
	
	/**
	 * Recupera los colegios que tienen carga de censo por Web Services <code>idInstitucion</code>
	 * @param idInstitucion Institucion para la cual se buscan sus colegios asociados
	 * @return Una lista con los colegios que tienen carga de web services, o <code>null</code> si la institucion
	 * no tiene colegios dependientes, es decir, si es un Colegio y no un Consejo.
	 * @throws SIGAException 
	 */
	private List<InstitucionVO> getColegiosWS() throws SIGAException{
		ClsLogging.writeFileLog("BusquedaRemesasAction.getColegiosWS() - INICIO", 3);
		List<InstitucionVO> institucionesWS = null;
		CenInstitucionService service = (CenInstitucionService) getBusinessManager().getService(CenInstitucionService.class);
		institucionesWS =	service.getColegiosWS();
		ClsLogging.writeFileLog("BusquedaRemesasAction.getColegiosWS() - FIN", 3);
		return institucionesWS;
	}
	
	//CENSO-297@DTT.JAMARTIN@10/06/2022@INICIO
	/**
	 * Recupera los colegios que tienen carga de censo manual <code>idInstitucion</code>
	 * @param idInstitucion Institucion para la cual se buscan sus colegios asociados
	 * @return Una lista con los colegios que tienen carga de web services, o <code>null</code> si la institucion
	 * no tiene colegios dependientes, es decir, si es un Colegio y no un Consejo.
	 * @throws SIGAException 
	 */
	private List<InstitucionVO> getColegios() throws SIGAException{
		ClsLogging.writeFileLog("BusquedaRemesasAction.getColegios() - INICIO", 3);
		List<InstitucionVO> institucionesWS = null;
		CenInstitucionService service = (CenInstitucionService) getBusinessManager().getService(CenInstitucionService.class);
		institucionesWS = service.getColegios();
		ClsLogging.writeFileLog("BusquedaRemesasAction.getColegios() - FIN", 3);
		return institucionesWS;
	}
	//CENSO-297@DTT.JAMARTIN@10/06/2022@FIN
	
	
	
	/**
	 * Recupera los colegios asociados a la institucion <code>idInstitucion</code>
	 * @param idInstitucion Institucion para la cual se buscan sus colegios asociados
	 * @return Una lista con los colegios dependientes de la institucion, o <code>null</code> si la institucion
	 * no tiene colegios dependientes, es decir, si es un Colegio y no un Consejo.
	 * @throws SIGAException 
	 */
	private List<InstitucionVO> getColegiosDependientes(String idInstitucion) throws SIGAException{
		ClsLogging.writeFileLog("BusquedaRemesasAction.getColegiosDependientes() - INICIO", 3);
		List<InstitucionVO> instituciones = null;
		//Si la institucion conectada es General se recuperan todos los colegios (no los consejos)
		CenInstitucionService service = (CenInstitucionService) getBusinessManager().getService(CenInstitucionService.class);
		if (institucionEsGeneral(idInstitucion)){
			ClsLogging.writeFileLog("BusquedaRemesasAction.getColegiosDependientes() - La institucion es general, recuperamos todos los colegios", 3);
			instituciones = service.getColegiosNoConsejo(idInstitucion);
		}
		//Si la institucion no conectada es un Consejo, se recuperan sus colegios dependientes
		else if (institucionEsConsejo(idInstitucion)){
			ClsLogging.writeFileLog("BusquedaRemesasAction.getColegiosDependientes() - La institucion no es general, recuperamos los colegios dependientes", 3);
			instituciones = service.getColegiosDeConsejo(idInstitucion);
		}
		ClsLogging.writeFileLog("BusquedaRemesasAction.getColegiosDependientes() - INICIO", 3);
		return instituciones;
	}
	

	protected String buscar (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException{
		ClsLogging.writeFileLog("BusquedaRemesasAction.buscar() - INICIO", 3);
		request.getSession().removeAttribute(DATAPAGINADOR);
		ClsLogging.writeFileLog("BusquedaRemesasAction.buscar() - FIN", 3);
		return buscarPor(mapping, formulario, request, response);
		
	}
	
	protected String buscarPor (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		
		try {
			ClsLogging.writeFileLog("BusquedaRemesasAction.buscarPor() - INICIO", 3);
			BusquedaRemesasForm form = (BusquedaRemesasForm) formulario;			
			HashMap databackup = new HashMap();
			CenWSService cenWSService = (CenWSService) BusinessManager.getInstance().getService(CenWSService.class);

			if (request.getSession().getAttribute(DATAPAGINADOR) != null) {
				databackup = (HashMap) request.getSession().getAttribute(DATAPAGINADOR);
				PaginadorVector<EdicionRemesaForm> paginador = (PaginadorVector<EdicionRemesaForm>) databackup.get("paginador");
				List<EdicionRemesaForm> datos = new ArrayList<EdicionRemesaForm>();

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
				calculaIncidencias(cenWSService, datos);
				databackup.put("datos", datos);

			} else {	
				
				List<EdicionRemesaForm> datos = getDatos(cenWSService, form);																	
				
				PaginadorVector<EdicionRemesaForm> paginador = new PaginadorVector(datos);
				int totalRegistros = paginador.getNumeroTotalRegistros();

				if (totalRegistros == 0) {
					paginador = null;
				}

				databackup.put("paginador", paginador);
				if (paginador != null) {
					datos = paginador.obtenerPagina(1);
					calculaIncidencias(cenWSService, datos);
					databackup.put("datos", datos);
					request.getSession().setAttribute(DATAPAGINADOR, databackup);
					request.getSession().setAttribute("HORABUSQUEDA", UtilidadesBDAdm.getFechaCompletaBD("es"));
				}
			}				
			
		} catch (Exception e) {
			ClsLogging.writeFileLogError("ERROR - BusquedaRemesasAction.buscarPor(): " + e.getMessage() , e, 3);
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		ClsLogging.writeFileLog("BusquedaRemesasAction.buscarPor() - FIN", 3);
		return "resultado";
	}


	
	private void calculaTotalRegistros(CenWSService cenWSService,	List<EdicionRemesaForm> datos) throws SIGAException {
		if (datos != null) {
			for (EdicionRemesaForm edicionRemesaForm : datos) {
				cenWSService.selectCountColegiadosEnvio(edicionRemesaForm.getIdcensowsenvio());
			}
		}
	}
	
	private void calculaIncidencias(CenWSService cenWSService,	List<EdicionRemesaForm> datos) throws SIGAException {

		if (datos != null) {
			List<Long> listaIdcensowsenvio = new ArrayList<Long>();
			for (EdicionRemesaForm edicionRemesaForm : datos) {
				//si tiene errores no tiene incidencias
				if (edicionRemesaForm.getConerrores() == null || edicionRemesaForm.getConerrores() < 1) {
					listaIdcensowsenvio.add(edicionRemesaForm.getIdcensowsenvio());
				}
			}
			if (listaIdcensowsenvio != null && listaIdcensowsenvio.size() > 0) {
				Map<Long, EcomCenWsEnvioExtended> mapa = cenWSService.selectCountIncidenciasEnvio(listaIdcensowsenvio);
				for (EdicionRemesaForm edicionRemesaForm : datos) {
					EcomCenWsEnvioExtended ecomCenWsEnvioExtended = mapa.get(edicionRemesaForm.getIdcensowsenvio());
					if (ecomCenWsEnvioExtended != null) {
						edicionRemesaForm.setIncidencias(ecomCenWsEnvioExtended.getIncidencias());
					}
				}
				
			}
		}
	}
	
	
	/**
	 * 
	 * @param idinstitucion
	 * @return
	 * @throws ValidationException
	 */
	private String getUmbral(Short idinstitucion) throws ValidationException {
		String modulo = MODULO.CEN.toString();
		String parametro  = PARAMETRO.UMBRAL_ERROR.toString();
		
		GenParametrosService genParametrosService = (GenParametrosService) BusinessManager.getInstance().getService(GenParametrosService.class);
		
		GenParametros genParametros = new GenParametros();
		genParametros.setIdinstitucion(idinstitucion);
		genParametros.setModulo(modulo);
		genParametros.setParametro(parametro);
		
		genParametros = genParametrosService.getGenParametroInstitucionORvalor0(genParametros);
		
		if (genParametros == null) {
			String error = String.format("No se ha encontrado el parámetro '%s', con módulo '%s' para el colegio %s", parametro, modulo, idinstitucion);
			throw new ValidationException(error);
		}
		String valor = genParametros.getValor();
		return valor;
	}
	
	
	
	

	private List<EdicionRemesaForm> getDatos(CenWSService cenWSService, BusquedaRemesasForm form) throws ParseException, ClsExceptions {
		
		ClsLogging.writeFileLog("BusquedaRemesasAction.getDatos() - INICIO", 3);
		List<EdicionRemesaForm> lista = null;
		
		EcomCenWsEnvioExample ecomCenWsEnvioExample = new EcomCenWsEnvioExample();
		EcomCenWsEnvioExample.Criteria ecomWSCriteria = ecomCenWsEnvioExample.createCriteria();
		
		
		//colegio
		if (isNotnull(form.getIdColegio())) {
			ClsLogging.writeFileLog("BusquedaRemesasAction.getDatos() - 1", 3);
			ecomWSCriteria.andIdinstitucionEqualTo(Short.valueOf(form.getIdColegio()));
		}		
		//numero de peticion
		if (isNotnull(form.getNumeroPeticion())) {
			ClsLogging.writeFileLog("BusquedaRemesasAction.getDatos() - 2", 3);
			ecomWSCriteria.andNumeropeticionLike(getCampoLike(form.getNumeroPeticion().trim()));
		}
		//fecha peticion desde
		if (isNotnull(form.getFechaPeticionDesde())) {
			ClsLogging.writeFileLog("BusquedaRemesasAction.getDatos() - 3", 3);
			ecomWSCriteria.andFechacreacionGreaterThanOrEqualTo(AppConstants.DATE_FORMAT.parse(form.getFechaPeticionDesde()));
		}			
		//fecha peticion hasta
		if (isNotnull(form.getFechaPeticionHasta())) {
			ClsLogging.writeFileLog("BusquedaRemesasAction.getDatos() - 4", 3);
			ecomWSCriteria.andFechacreacionLessThanOrEqualTo(AppConstants.DATE_FORMAT.parse(form.getFechaPeticionHasta()));
		}
		
		//busqueda referente a los datos del colegiado
		EcomCenDatosExample ecomCenDatosExample = new EcomCenDatosExample();
		EcomCenDatosExample.Criteria datosCriteria = ecomCenDatosExample.createCriteria();	
		
		boolean busquedaDatos = false;
		
		//número de colegiado
		if (isNotnull(form.getNumeroColegiado())) {
			ClsLogging.writeFileLog("BusquedaRemesasAction.getDatos() - 5", 3);
			datosCriteria.andNcolegiadoUpperLike(getCampoLike(form.getNumeroColegiado().trim()));
			busquedaDatos = true;
		}
		//nombre
		if (isNotnull(form.getNombre())) {
			ClsLogging.writeFileLog("BusquedaRemesasAction.getDatos() - 6", 3);
			datosCriteria.andNombreUpperLike(getCampoLike(form.getNombre().trim()));
			busquedaDatos = true;
		}
		//apellido 1
		if (isNotnull(form.getPrimerApellido())) {
			ClsLogging.writeFileLog("BusquedaRemesasAction.getDatos() - 7", 3);
			datosCriteria.andApellido1UpperLike(getCampoLike(form.getPrimerApellido().trim()));
			busquedaDatos = true;
		}
		//apellido 2
		if (isNotnull(form.getSegundoApellido())) {
			ClsLogging.writeFileLog("BusquedaRemesasAction.getDatos() - 8", 3);
			datosCriteria.andApellido2UpperLike(getCampoLike(form.getSegundoApellido().trim()));
			busquedaDatos = true;
		}
		//tipo identificación
		if (isNotnull(form.getIdTipoIdentificacion())) {
			ClsLogging.writeFileLog("BusquedaRemesasAction.getDatos() - 9", 3);
			datosCriteria.andIdcensotipoidentificacionEqualTo(Short.valueOf(form.getIdTipoIdentificacion()));
			busquedaDatos = true;
		}
		//identificación
		if (isNotnull(form.getIdentificacion())) {
			ClsLogging.writeFileLog("BusquedaRemesasAction.getDatos() - 10", 3);
			datosCriteria.andNumdocumentoUpperLike(getCampoLike(form.getIdentificacion().trim()));
			busquedaDatos = true;
		}
		if (!busquedaDatos) {
			ClsLogging.writeFileLog("BusquedaRemesasAction.getDatos() - 11", 3);
			//así conseguimos que sea más rápida la query al no cruzar con las tablas para usar este criterio
			ecomCenDatosExample = null;
		}
		
		ecomCenWsEnvioExample.orderByIdcenwsenvioDESC();
				
		List<EcomCenWsEnvioExtended> listaEcomCenWsEnvio = cenWSService.getEcomCenWsEnvioList(ecomCenWsEnvioExample, ecomCenDatosExample, form.isConIncidencia(), form.isConError());
		ClsLogging.writeFileLog("BusquedaRemesasAction.getDatos() - listaEcomCenWsEnvio tiene un tamaño de " + listaEcomCenWsEnvio.size(), 3); 
		if (listaEcomCenWsEnvio != null) {
			lista = new ArrayList<EdicionRemesaForm>();
			for (EcomCenWsEnvioExtended ecomCenWsEnvio : listaEcomCenWsEnvio) {
				EdicionRemesaForm edicionRemesaForm = new EdicionRemesaForm();
				edicionRemesaForm.setIdcensowsenvio(ecomCenWsEnvio.getIdcenwsenvio());
				edicionRemesaForm.setIdinstitucion(ecomCenWsEnvio.getIdinstitucion());
				edicionRemesaForm.setNumeroPeticion(ecomCenWsEnvio.getNumeropeticion());
				//edicionRemesaForm.setFechapeticion(GstDate.getFormatedDateShort(ecomCenWsEnvio.getFechacreacion()));
				edicionRemesaForm.setFechapeticion(GstDate.getFormatedDateLong("ES", ecomCenWsEnvio.getFechacreacion()));
//				edicionRemesaForm.setIncidencias(ecomCenWsEnvio.getIncidencias());
				edicionRemesaForm.setConerrores(ecomCenWsEnvio.getConerrores());
				edicionRemesaForm.setIdEstadoenvio(ecomCenWsEnvio.getIdestadoenvio());
				edicionRemesaForm.setTipoEnvio(ecomCenWsEnvio.getIdtipoenvio());
				lista.add(edicionRemesaForm);
			}
		}
		ClsLogging.writeFileLog("BusquedaRemesasAction.getDatos() - FIN", 3);
		return lista;
	}


	private boolean isNotnull(String value) {		
		return value != null && !value.trim().equals("");
	}


	private String getCampoLike(String value) {		
		return AppConstants.TANTO_POR_CIENTO + value + AppConstants.TANTO_POR_CIENTO;
	}
			
	
}
