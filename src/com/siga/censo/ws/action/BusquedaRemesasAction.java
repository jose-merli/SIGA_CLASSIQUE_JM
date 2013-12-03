package com.siga.censo.ws.action;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.autogen.model.EcomCenDatosExample;
import org.redabogacia.sigaservices.app.autogen.model.EcomCenWsEnvioExample;
import org.redabogacia.sigaservices.app.model.EcomCenWsEnvioExtended;
import org.redabogacia.sigaservices.app.services.cen.CenWSService;

import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.paginadores.PaginadorVector;
import com.siga.beans.CenInstitucionAdm;
import com.siga.censo.service.CensoService;
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
	
	protected String abrir (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException {
		
		BusquedaRemesasForm busquedaRemesasForm = (BusquedaRemesasForm) formulario;
		busquedaRemesasForm.reset();
		request.getSession().removeAttribute(DATAPAGINADOR);
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
			
			CenInstitucionAdm institucionAdm = new CenInstitucionAdm(getUserBean(request));
			String nombreInstitucionAcceso=institucionAdm.getNombreInstitucion(getUserBean(request).getLocation());
			busquedaRemesasForm.setNombreColegio(nombreInstitucionAcceso);
			busquedaRemesasForm.setIdColegio(getUserBean(request).getLocation());

			busquedaRemesasForm.setInstituciones(getColegiosDependientes(getUserBean(request).getLocation()));
			busquedaRemesasForm.setTiposIdentificacion(CombosCenWS.getTiposIdentificacion(getUserBean(request)));
			
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
		if (institucionEsGeneral(idInstitucion)){
			CensoService service = (CensoService) getBusinessManager().getService(CensoService.class);
			instituciones = service.getColegiosNoConsejo(idInstitucion);
		}
		//Si la institucion no conectada es un Consejo, se recuperan sus colegios dependientes
		else if (institucionEsConsejo(idInstitucion)){
			CensoService service = (CensoService) getBusinessManager().getService(CensoService.class);
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
				databackup.put("datos", datos);

			} else {									
				List<EdicionRemesaForm> datos = getDatos(form);																	
				
				PaginadorVector<EdicionRemesaForm> paginador = new PaginadorVector(datos);
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


	private List<EdicionRemesaForm> getDatos(BusquedaRemesasForm form) throws ParseException, ClsExceptions {
		
		List<EdicionRemesaForm> lista = null;
		CenWSService cenWSService = (CenWSService) BusinessManager.getInstance().getService(CenWSService.class);
		EcomCenWsEnvioExample ecomCenWsEnvioExample = new EcomCenWsEnvioExample();
		EcomCenWsEnvioExample.Criteria ecomWSCriteria = ecomCenWsEnvioExample.createCriteria();
		
		
		//colegio
		if (isNotnull(form.getIdColegio())) {
			ecomWSCriteria.andIdinstitucionEqualTo(Short.valueOf(form.getIdColegio()));
		}		
		//numero de peticion
		if (isNotnull(form.getNumeroPeticion())) {
			ecomWSCriteria.andNumeropeticionLike(getCampoLike(form.getNumeroPeticion()));
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
		
		//número de colegiado
		if (isNotnull(form.getNumeroColegiado())) {
			datosCriteria.andNcolegiadoUpperLike(getCampoLike(form.getNumeroColegiado()));
		}
		//nombre
		if (isNotnull(form.getNombre())) {
			datosCriteria.andNombreUpperLike(getCampoLike(form.getNombre()));
		}
		//apellido 1
		if (isNotnull(form.getPrimerApellido())) {
			datosCriteria.andApellido1UpperLike(getCampoLike(form.getPrimerApellido()));
		}
		//apellido 2
		if (isNotnull(form.getSegundoApellido())) {
			datosCriteria.andApellido2UpperLike(getCampoLike(form.getSegundoApellido()));
		}
		//tipo identificación
		if (isNotnull(form.getIdTipoIdentificacion())) {
			datosCriteria.andIdcensotipoidentificacionEqualTo(Short.valueOf(form.getIdTipoIdentificacion()));
		}
		//identificación
		if (isNotnull(form.getIdentificacion())) {
			datosCriteria.andNumdocumentoUpperLike(getCampoLike(form.getIdentificacion()));
		}
		ecomCenWsEnvioExample.orderByNumeropeticionDESC();
				
		List<EcomCenWsEnvioExtended> listaEcomCenWsEnvio = cenWSService.getEcomCenWsEnvioList(ecomCenWsEnvioExample, ecomCenDatosExample, form.isConIncidencia(), form.isConError());
		if (listaEcomCenWsEnvio != null) {
			lista = new ArrayList<EdicionRemesaForm>();
			for (EcomCenWsEnvioExtended ecomCenWsEnvio : listaEcomCenWsEnvio) {
				EdicionRemesaForm edicionRemesaForm = new EdicionRemesaForm();
				edicionRemesaForm.setIdcensowsenvio(ecomCenWsEnvio.getIdcenwsenvio());
				edicionRemesaForm.setIdinstitucion(ecomCenWsEnvio.getIdinstitucion());
				edicionRemesaForm.setNumeroPeticion(ecomCenWsEnvio.getNumeropeticion());
				edicionRemesaForm.setFechapeticion(GstDate.getFormatedDateShort(ecomCenWsEnvio.getFechacreacion()));
				edicionRemesaForm.setListaErrores(cenWSService.getListaErrores(ecomCenWsEnvio.getIdcenwsenvio()));
				edicionRemesaForm.setIncidencias(ecomCenWsEnvio.getIncidencias());
				edicionRemesaForm.setConerrores(ecomCenWsEnvio.getConerrores());
				edicionRemesaForm.setIdEstadoenvio(ecomCenWsEnvio.getIdestadoenvio());
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
