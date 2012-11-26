package com.siga.gratuita.action;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.helper.DocuShareHelper;
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

public abstract class DocumentacionRegTelAction extends MasterAction {

	
	protected abstract String createCollection(MasterForm formulario, HttpServletRequest request) throws Exception;
	
	/**
	 * 
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {	
		
		try {
			DatosRegTelForm miForm = (DatosRegTelForm) formulario;
			HashMap databackup = new HashMap();

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
			
				
		} catch (Exception e) {
			throwExcp("messages.general.error", e, null);
		}
		return "listadoCollection";		
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
