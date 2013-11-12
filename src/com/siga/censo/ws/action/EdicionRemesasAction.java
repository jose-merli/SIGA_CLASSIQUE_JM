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
import org.redabogacia.sigaservices.app.autogen.model.EcomCenColegiado;
import org.redabogacia.sigaservices.app.autogen.model.EcomCenDatos;
import org.redabogacia.sigaservices.app.autogen.model.EcomCenDatosExample;
import org.redabogacia.sigaservices.app.autogen.model.EcomCenDatosExample.Criteria;
import org.redabogacia.sigaservices.app.autogen.model.EcomCenWsEnvio;
import org.redabogacia.sigaservices.app.services.cen.CenWSService;
import org.redabogacia.sigaservices.app.services.cen.ws.EcomCenColegiadoService;

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
		EcomCenColegiadoService ecomCenColegiadoService = (EcomCenColegiadoService) BusinessManager.getInstance().getService(EcomCenColegiadoService.class);
		
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
		
		ecomCenDatosExample.orderByApellido1();
		ecomCenDatosExample.orderByApellido2();
		ecomCenDatosExample.orderByNombre();
		
		List<EcomCenDatos> ecomCenDatos = cenWSService.getEcomCenDatosList(form.getIdcensowsenvio(), ecomCenDatosExample);
		
		if (ecomCenDatos != null) {
			for (EcomCenDatos ecomCenDato : ecomCenDatos) {
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
					EcomCenColegiado ecomCenColegiado = ecomCenColegiadoService.getEcomCenColegiado(ecomCenDato.getIdcensodatos());
					edicionColegiadoForm.setIdpersona(ecomCenColegiado.getIdpersona());
					edicionColegiadoForm.setIdinstitucion(ecomCenColegiado.getIdinstitucion());
				}				
			
				datos.add(edicionColegiadoForm);
			}
		}
		
		return datos;

	}
	
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return verEditar("ver", formulario, request);		
	}
	
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return verEditar("editar", formulario, request);		
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
			edicionRemesaForm.setAccion(accion);
						
			HttpSession session = request.getSession();
			if (request.getParameter("volver") == null) {
				session.removeAttribute(DATAPAGINADOR);
				edicionRemesaForm.reset();
			}
			
			
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
			
			edicionRemesaForm.setNumeroPeticion(ecomCenWsEnvio.getNumeropeticion());
			edicionRemesaForm.setFechapeticion(GstDate.getFormatedDateShort(ecomCenWsEnvio.getFechacreacion()));
			
			edicionRemesaForm.setConerrores(ecomCenWsEnvio.getConerrores());
			edicionRemesaForm.setListaErrores(cenWSService.getListaErrores(idcensowsenvio));
			 			
			edicionRemesaForm.setTiposIdentificacion(CombosCenWS.getTiposIdentificacion(getUserBean(request)));
						
		} catch (Exception e) {
			throwExcp("messages.general.error", e, null);
		}
		return accion;
		
	}


	
	

	private boolean isNotnull(String value) {		
		return value != null && !value.trim().equals("");
	}


	private String getCampoLike(String value) {		
		return AppConstants.TANTO_POR_CIENTO + value + AppConstants.TANTO_POR_CIENTO;
	}
			
	
}
