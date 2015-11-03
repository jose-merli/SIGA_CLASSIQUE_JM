package com.siga.censo.ws.action;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

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
import com.atos.utils.GstDate;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.paginadores.PaginadorVector;
import com.siga.beans.CenInstitucionAdm;
import com.siga.censo.ws.form.BusquedaRemesasForm;
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
		
		BusquedaRemesasForm busquedaRemesasForm = (BusquedaRemesasForm) formulario;
		busquedaRemesasForm.reset();
		request.getSession().removeAttribute(DATAPAGINADOR);
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -2);
				
		busquedaRemesasForm.setFechaPeticionDesde(AppConstants.DATE_FORMAT.format(cal.getTime()));
		
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
			BusquedaRemesasForm busquedaRemesasForm = (BusquedaRemesasForm) formulario;
			
			request.getSession().removeAttribute(DATAPAGINADOR);
			
			CenInstitucionAdm institucionAdm = new CenInstitucionAdm(getUserBean(request));
			String nombreInstitucionAcceso=institucionAdm.getNombreInstitucion(getUserBean(request).getLocation());
			busquedaRemesasForm.setNombreColegio(nombreInstitucionAcceso);
			busquedaRemesasForm.setIdColegio(getUserBean(request).getLocation());
			
			CombosCenWS combosCenWS = new CombosCenWS();

			busquedaRemesasForm.setInstituciones(getColegiosDependientes(getUserBean(request).getLocation()));
			busquedaRemesasForm.setTiposIdentificacion(combosCenWS.getTiposIdentificacion(getUserBean(request)));
			
			String buscar = request.getParameter("buscar");
			request.setAttribute("buscar",buscar);
		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		return "inicio";
	}
	
	/**
	 * Recupera los colegios asociados a la institucion <code>idInstitucion</code>
	 * @param idInstitucion Institucion para la cual se buscan sus colegios asociados
	 * @return Una lista con los colegios dependientes de la institucion, o <code>null</code> si la institucion
	 * no tiene colegios dependientes, es decir, si es un Colegio y no un Consejo.
	 * @throws SIGAException 
	 */
	private List<InstitucionVO> getColegiosDependientes(String idInstitucion) throws SIGAException{
		List<InstitucionVO> instituciones = null;
		//Si la institucion conectada es General se recuperan todos los colegios (no los consejos)
		CenInstitucionService service = (CenInstitucionService) getBusinessManager().getService(CenInstitucionService.class);
		if (institucionEsGeneral(idInstitucion)){
			instituciones = service.getColegiosNoConsejo(idInstitucion);
		}
		//Si la institucion no conectada es un Consejo, se recuperan sus colegios dependientes
		else if (institucionEsConsejo(idInstitucion)){
			instituciones = service.getColegiosDeConsejo(idInstitucion);
		}
		return instituciones;
	}
	
		
	protected String buscar (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException{
		
		request.getSession().removeAttribute(DATAPAGINADOR);
		return buscarPor(mapping, formulario, request, response);
		
	}
	
	protected String buscarPor (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		
		try {
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
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		return "resultado";
	}


	private void calculaIncidencias(CenWSService cenWSService,	List<EdicionRemesaForm> datos) throws SIGAException {
		double porcentaCalculado=0;
		int totalincidencias =0;
		int totalRegistros=0;
		if (datos != null) {
			for (EdicionRemesaForm edicionRemesaForm : datos) {
				//si tiene errores no tiene incidencias
				if (edicionRemesaForm.getConerrores() == null || edicionRemesaForm.getConerrores() < 1) {
					//totalincidencias=cenWSService.selectCountIncidenciasEnvio(edicionRemesaForm.getIdcensowsenvio());
					//totalRegistros =cenWSService.selectCountColegiadosEnvio(edicionRemesaForm.getIdcensowsenvio());
					edicionRemesaForm.setIncidencias(cenWSService.selectCountIncidenciasEnvio(edicionRemesaForm.getIdcensowsenvio()));
					/*edicionRemesaForm.setCountTotalColegiados(cenWSService.selectCountColegiadosEnvio(edicionRemesaForm.getIdcensowsenvio()));
					if(totalRegistros>0){
						porcentaCalculado = ((totalincidencias*100)/totalRegistros);
					}else{
						porcentaCalculado=0;
					}
					
					int porcentajeRedondeo = (int) Math.round(porcentaCalculado);
					edicionRemesaForm.setPorcentajeCalculado(porcentajeRedondeo);
					edicionRemesaForm.setUmbral(getUmbral(edicionRemesaForm.getIdinstitucion()));*/
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
		
		List<EdicionRemesaForm> lista = null;
		
		EcomCenWsEnvioExample ecomCenWsEnvioExample = new EcomCenWsEnvioExample();
		EcomCenWsEnvioExample.Criteria ecomWSCriteria = ecomCenWsEnvioExample.createCriteria();
		
		
		//colegio
		if (isNotnull(form.getIdColegio())) {
			ecomWSCriteria.andIdinstitucionEqualTo(Short.valueOf(form.getIdColegio()));
		}		
		//numero de peticion
		if (isNotnull(form.getNumeroPeticion())) {
			ecomWSCriteria.andNumeropeticionLike(getCampoLike(form.getNumeroPeticion().trim()));
		}
		//fecha peticion desde
		if (isNotnull(form.getFechaPeticionDesde())) {
			ecomWSCriteria.andFechacreacionGreaterThanOrEqualTo(AppConstants.DATE_FORMAT.parse(form.getFechaPeticionDesde()));
		}			
		//fecha peticion hasta
		if (isNotnull(form.getFechaPeticionHasta())) {
			ecomWSCriteria.andFechacreacionLessThanOrEqualTo(AppConstants.DATE_FORMAT.parse(form.getFechaPeticionHasta()));
		}
		
		//busqueda referente a los datos del colegiado
		EcomCenDatosExample ecomCenDatosExample = new EcomCenDatosExample();
		EcomCenDatosExample.Criteria datosCriteria = ecomCenDatosExample.createCriteria();	
		
		boolean busquedaDatos = false;
		
		//número de colegiado
		if (isNotnull(form.getNumeroColegiado())) {
			datosCriteria.andNcolegiadoUpperLike(getCampoLike(form.getNumeroColegiado().trim()));
			busquedaDatos = true;
		}
		//nombre
		if (isNotnull(form.getNombre())) {
			datosCriteria.andNombreUpperLike(getCampoLike(form.getNombre().trim()));
			busquedaDatos = true;
		}
		//apellido 1
		if (isNotnull(form.getPrimerApellido())) {
			datosCriteria.andApellido1UpperLike(getCampoLike(form.getPrimerApellido().trim()));
			busquedaDatos = true;
		}
		//apellido 2
		if (isNotnull(form.getSegundoApellido())) {
			datosCriteria.andApellido2UpperLike(getCampoLike(form.getSegundoApellido().trim()));
			busquedaDatos = true;
		}
		//tipo identificación
		if (isNotnull(form.getIdTipoIdentificacion())) {
			datosCriteria.andIdcensotipoidentificacionEqualTo(Short.valueOf(form.getIdTipoIdentificacion()));
			busquedaDatos = true;
		}
		//identificación
		if (isNotnull(form.getIdentificacion())) {
			datosCriteria.andNumdocumentoUpperLike(getCampoLike(form.getIdentificacion().trim()));
			busquedaDatos = true;
		}
		if (!busquedaDatos) {
			//así conseguimos que sea más rápida la query al no cruzar con las tablas para usar este criterio
			ecomCenDatosExample = null;
		}
		
		ecomCenWsEnvioExample.orderByIdcenwsenvioDESC();
				
		List<EcomCenWsEnvioExtended> listaEcomCenWsEnvio = cenWSService.getEcomCenWsEnvioList(ecomCenWsEnvioExample, ecomCenDatosExample, form.isConIncidencia(), form.isConError());
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
		
		return lista;
	}


	private boolean isNotnull(String value) {		
		return value != null && !value.trim().equals("");
	}


	private String getCampoLike(String value) {		
		return AppConstants.TANTO_POR_CIENTO + value + AppConstants.TANTO_POR_CIENTO;
	}
			
	
}
