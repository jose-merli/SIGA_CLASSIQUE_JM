package com.siga.censo.ws.action;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.autogen.model.EcomCenDatos;
import org.redabogacia.sigaservices.app.autogen.model.EcomCenDatosExample;
import org.redabogacia.sigaservices.app.autogen.model.EcomCenDatosExample.Criteria;
import org.redabogacia.sigaservices.app.autogen.model.EcomCenWs;
import org.redabogacia.sigaservices.app.services.cen.CenWSService;

import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesString;
import com.siga.Utilidades.paginadores.PaginadorVector;
import com.siga.beans.CenInstitucionAdm;
import com.siga.censo.service.CensoService;
import com.siga.censo.ws.form.EdicionRemesaForm;
import com.siga.comun.vos.InstitucionVO;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

import es.satec.businessManager.BusinessManager;

public class EdicionRemesasAction extends MasterAction {
	
	public static final String DATAPAGINADOR = "DATAPAGINADOR_LISTADO_COLEGIADOS";
	
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
	
	protected String buscarPor (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		
		try {
			EdicionRemesaForm edicionRemesaForm = (EdicionRemesaForm) formulario;			
			HashMap databackup = new HashMap();

			if (request.getSession().getAttribute(DATAPAGINADOR) != null) {
				databackup = (HashMap) request.getSession().getAttribute(DATAPAGINADOR);
				PaginadorVector<EcomCenDatos> paginador = (PaginadorVector<EcomCenDatos>) databackup.get("paginador");
				List<EcomCenDatos> datos = new ArrayList<EcomCenDatos>();

				// Si no es la primera llamada, obtengo la página del request y
				// la busco con el paginador
				String pagina = request.getParameter("paginaListaCol");

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
				List<EcomCenDatos> datos = getDatos(edicionRemesaForm);																	
				
				PaginadorVector<EcomCenDatos> paginador = new PaginadorVector(datos);
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


	private List<EcomCenDatos> getDatos(EdicionRemesaForm form) throws ParseException {
		CenWSService cenWSService = (CenWSService) BusinessManager.getInstance().getService(CenWSService.class);
		EcomCenDatosExample ecomCenDatosExample = new EcomCenDatosExample();
		Criteria datosCriteria = ecomCenDatosExample.createCriteria();
		
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
		
		return cenWSService.getEcomCenDatosList(Long.valueOf(form.getIdcensows()), ecomCenDatosExample);

	}
	
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		try {
			EdicionRemesaForm edicionRemesaForm = (EdicionRemesaForm) formulario;
			
			HttpSession session = request.getSession();
			if (request.getParameter("volver") == null) {
				session.removeAttribute(DATAPAGINADOR);
				edicionRemesaForm.reset();
			}
			
			
			// Recuperamos los datos del registro que hemos seleccionado
			Vector ocultos = formulario.getDatosTablaOcultos(0);
			
			Hashtable miHash = new Hashtable();
			
			Long idcensows = null;
			
			if (ocultos != null && ocultos.size() > 0) {
				idcensows = Long.valueOf(ocultos.get(0).toString());
			} else {
				throw new IllegalArgumentException("No se ha recibido el identificador para editar la remesa");
			}
					
			CenWSService cenWSService = (CenWSService) BusinessManager.getInstance().getService(CenWSService.class);
			EcomCenWs ecomCenWs = cenWSService.getEcomCenWsByPk(idcensows);
			
			CenInstitucionAdm institucionAdm = new CenInstitucionAdm(getUserBean(request));
			edicionRemesaForm.setIdcensows(ecomCenWs.getIdcensows().toString());
			edicionRemesaForm.setNombreColegio(institucionAdm.getNombreInstitucion(ecomCenWs.getIdinstitucion().toString()));
			
			edicionRemesaForm.setNumeroPeticion(ecomCenWs.getNumeropeticion());
			edicionRemesaForm.setFechapeticion(GstDate.getFormatedDateShort(ecomCenWs.getFechapeticion()));
			if (ecomCenWs.getCoderror() != null && !ecomCenWs.getCoderror().trim().equals("")) {
				edicionRemesaForm.setCoderror(ecomCenWs.getCoderror());
				edicionRemesaForm.setDescerror(ecomCenWs.getDescerror());
			} else {
				edicionRemesaForm.setDescerror(UtilidadesString.getMensajeIdioma(getUserBean(request), "censo.ws.literal.sinIncidencia"));
			}
			
			// Entramos al formulario en modo 'modificación'
			session.setAttribute("accion", "editar");
			
		} catch (Exception e) {
			throwExcp("messages.general.error", e, null);
		}
		return "editar";
	}
	


	private boolean isNotnull(String value) {		
		return value != null && !value.trim().equals("");
	}


	private String getCampoLike(String value) {		
		return AppConstants.TANTO_POR_CIENTO + value + AppConstants.TANTO_POR_CIENTO;
	}
			
	
}
