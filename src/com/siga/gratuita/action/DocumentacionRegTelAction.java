package com.siga.gratuita.action;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.exceptions.SIGAServiceException;
import org.redabogacia.sigaservices.app.helper.DocuShareHelper;
import org.redabogacia.sigaservices.app.services.scs.EjgService;
import org.redabogacia.sigaservices.app.vo.DocuShareObjectVO;
import org.redabogacia.sigaservices.app.vo.DocuShareObjectVO.DocuShareTipo;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.paginadores.PaginadorVector;
import com.siga.beans.GenParametrosAdm;
import com.siga.censo.form.DatosRegTelForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefinirEJGForm;
import com.siga.gratuita.form.DefinirUnidadFamiliarEJGForm;

import es.satec.businessManager.BusinessManager;

public abstract class DocumentacionRegTelAction extends MasterAction {

	protected ActionForward executeInternal(ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String mapDestino = "exception";
		MasterForm miForm = null;
		try {

			do {
				miForm = (MasterForm) formulario;
				if (miForm != null) {
					String accion = miForm.getModo();
					String modo = request.getParameter("modo");
					if (modo != null)
						accion = modo;
					if (accion != null && accion.equalsIgnoreCase("enviaDocumentoCAJG")) {
						mapDestino = enviaDocumentoCAJG(mapping, miForm, request, response);
					}else {
						return super.executeInternal(mapping, formulario, request, response);
					}

				}
			} while (false);
			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) {
				throw new ClsExceptions("El ActionMapping no puede ser nulo");
			}
			return mapping.findForward(mapDestino);
		} catch (SIGAException es) {

			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error", e, new String[] { "modulo.gratuita" });
		}
	}
	
	
	protected abstract String createCollection(MasterForm formulario, HttpServletRequest request) throws Exception;
	
	/**
	 * 
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {	
		
		try {
			DatosRegTelForm miForm = (DatosRegTelForm) formulario;
			
			boolean isColegioConfiguradoEnvioCAJG = miForm.isColegioConfiguradoEnvioCAJG();
			HashMap databackup = new HashMap();
			request.setAttribute("isColegioConfiguradoEnvioCAJG",isColegioConfiguradoEnvioCAJG);


			if (request.getSession().getAttribute("DATAPAGINADOR") != null) {
				databackup = (HashMap) request.getSession().getAttribute("DATAPAGINADOR");
				PaginadorVector paginador = (PaginadorVector) databackup.get("paginador");
				List<DocuShareObjectVO> datos = new ArrayList<DocuShareObjectVO>();

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
				
				GenParametrosAdm parametrosAdm = new GenParametrosAdm(this.getUserBean(request));
				String regtel = parametrosAdm.getValor(this.getUserBean(request).getLocation(), ClsConstants.MODULO_GENERAL, "REGTEL", "0");
		        
				boolean parametroRegtel = false;
	        	if (regtel!=null && regtel.equals("1")){
	        		parametroRegtel = true;
	        	}
				
				String accion = (String)request.getSession().getAttribute("accion");
				boolean accionVer = false;
				if (accion != null && accion.trim().equals("ver")) {
					accionVer = true;
				}
				
				ClsLogging.writeFileLog("Valor de parametroRegtel = " + parametroRegtel, 3);
				ClsLogging.writeFileLog("Valor de creaCollection = " + miForm.isCreaCollection(), 3);
				ClsLogging.writeFileLog("Valor de accionVer = " + accionVer, 3);
				ClsLogging.writeFileLog("Valor de idComision = " + getUserBean(request).isComision(), 3);
				ClsLogging.writeFileLog("Valor de identificadorDs() = " + miForm.getIdentificadorDs(), 3);
				
				
				if (parametroRegtel && miForm.isCreaCollection() && !accionVer && !getUserBean(request).isComision()) {
					String idDS = createCollection(formulario, request);
					miForm.setIdentificadorDs(idDS);	
					request.getSession().removeAttribute("MIGAS_DS");
				} 

				List<DocuShareObjectVO> migas = (List<DocuShareObjectVO>) request.getSession().getAttribute("MIGAS_DS");
				
				if (migas != null) {
					if (miForm.getPosicionDs() != null && !miForm.getPosicionDs().trim().equals("")) {
						int posicion = Integer.parseInt(miForm.getPosicionDs());
						while(posicion < migas.size()-1) {
							migas.remove(migas.size()-1);
						}
					} else {
						DocuShareObjectVO dsObjectVO = new DocuShareObjectVO(DocuShareTipo.COLLECTION);
						dsObjectVO.setId(miForm.getIdentificadorDs());
						dsObjectVO.setTitle(miForm.getTitleDs());
						migas.add(dsObjectVO);
					}
				} else {
					migas = new ArrayList<DocuShareObjectVO>();
					DocuShareObjectVO dsObjectVO = new DocuShareObjectVO(DocuShareTipo.COLLECTION);
					dsObjectVO.setId(miForm.getIdentificadorDs());
					//dsObjectVO.setTitle("HOME");
					migas.add(dsObjectVO);
					request.getSession().setAttribute("MIGAS_DS", migas);
					
					
		        	
					boolean preguntaCreaCollection = false;
					if (parametroRegtel && !accionVer && (miForm.getIdentificadorDs() == null || miForm.getIdentificadorDs().trim().equals("")) && !getUserBean(request).isComision()) {
						preguntaCreaCollection = true;
					}
					ClsLogging.writeFileLog("Valor de preguntaCreaCollection = " + preguntaCreaCollection, 3);
					request.setAttribute("CREACOLLECTION", Boolean.valueOf(preguntaCreaCollection));
				}			
				
				
				databackup = new HashMap();		
				
				short idInstitucion = getIDInstitucion(request).shortValue();				
				DocuShareHelper docuShareHelper = new DocuShareHelper(idInstitucion);
				
				List<DocuShareObjectVO> datos = null;
				
				if (miForm.getIdentificadorDs() != null && !miForm.getIdentificadorDs().trim().equals("")) {
					datos = docuShareHelper.getContenidoCollection(miForm.getIdentificadorDs());	
				}															
				
				PaginadorVector<DocuShareObjectVO> paginador = new PaginadorVector(datos);
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

			
			}
			
			
		} catch (SIGAServiceException e) {
			throw new SIGAException(e.getMessage(), e);
			
		} catch (Exception e) {
			throwExcp("messages.general.error", e, null);
		}
		return "listadoCollection";		
	}
	protected String enviaDocumentoCAJG(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		DefinirEJGForm miForm = (DefinirEJGForm) formulario;
		
		try {
			Vector vCampos = miForm.getDatosTablaOcultos(0);
			String idInstitucion = miForm.getIdInstitucion();
			String idTipoEJG = miForm.getIdTipoEJG();
			String anio = miForm.getAnio();
			String numero = miForm.getNumero();
			
			
			List<HashMap<String, String>> listCola = new ArrayList<HashMap<String, String>>();
			HashMap map = new HashMap<String, String>();
			map.put(AppConstants.PARAM_ECOMCOLA_IDINSTITUCION,	idInstitucion);
			map.put(AppConstants.PARAM_ECOMCOLA_ANIO, anio);
			map.put(AppConstants.PARAM_ECOMCOLA_IDTIPOEJG, idTipoEJG);
			map.put(AppConstants.PARAM_ECOMCOLA_NUMERO, numero);
			map.put(AppConstants.PARAM_ECOMCOLA_IDDOCUSHARE,	miForm.getIdentificadorDs());
			map.put(AppConstants.PARAM_ECOMCOLA_ULTIMODOCUMENTO,	AppConstants.DB_TRUE);

			listCola.add(map);
			EjgService ejgService =  (EjgService) BusinessManager.getInstance().getService(EjgService.class);			
			
			
			if(idInstitucion.equalsIgnoreCase("2055")) {
				ejgService.encolaEnvioDocumentacion(listCola, new Short(idInstitucion), AppConstants.OPERACION.ASIGNA_ENVIO_DOCUMENTO);
			}else if(idInstitucion.equalsIgnoreCase("2032")) {
				ejgService.encolaEnvioDocumentacion(listCola, new Short(idInstitucion), AppConstants.OPERACION.GV_ENVIO_DOCUMENTO);
			}else
				ejgService.encolaEnvioDocumentacion(listCola, new Short(idInstitucion), AppConstants.OPERACION.PERICLES_ENVIA_COMUNICACION);
		} catch (Exception e) {
			throwExcp("messages.general.error",e,null);
		}
		return exitoRefresco("messages.inserted.success",request); 
		
	}

	/**
	 * 
	 */
	protected String ver (ActionMapping mapping, MasterForm formulario, HttpServletRequest request,	HttpServletResponse response) throws ClsExceptions, SIGAException{		
		request.getSession().removeAttribute("DATAPAGINADOR");		
		DatosRegTelForm miForm = (DatosRegTelForm) formulario;		
		buscarPor(mapping, formulario, request, response);		
		return "listadoCollection";
	}
	
	/**
	 * Descarga un fichero desde docushare
	 */
	protected String download (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		
		DatosRegTelForm miForm = (DatosRegTelForm) formulario;
		try {
			short idInstitucion = getIDInstitucion(request).shortValue();
			DocuShareHelper docuShareHelper = new DocuShareHelper(idInstitucion);
		
			File file = docuShareHelper.getDocument(miForm.getIdentificadorDs());
			request.setAttribute("nombreFichero", file.getName());
			request.setAttribute("rutaFichero", file.getAbsolutePath());
			request.setAttribute("borrarFichero", "true");	
		
		} catch (Exception e) {
			throwExcp("messages.general.error", e, null);
		}
		
		return "descargaFichero";
	}
	
}
