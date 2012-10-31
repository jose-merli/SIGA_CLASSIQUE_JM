package com.siga.gratuita.action;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.DocuShareHelper;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.paginadores.PaginadorVector;
import com.siga.censo.form.DatosRegTelForm;
import com.siga.censo.vos.DocuShareObjectVO;
import com.siga.censo.vos.DocuShareObjectVO.DocuShareTipo;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

public class DocumentacionRegTelAction extends MasterAction {

	
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
				}
				
				
				databackup = new HashMap();				
				DocuShareHelper docuShareHelper = new DocuShareHelper(this.getUserBean(request));
				List<DocuShareObjectVO> datos = docuShareHelper.getContenidoCollection(miForm.getIdentificadorDs());											
				
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
			request.getSession().setAttribute("accion","ver");
				
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
			DocuShareHelper docuShareHelper = new DocuShareHelper(this.getUserBean(request));
		
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
