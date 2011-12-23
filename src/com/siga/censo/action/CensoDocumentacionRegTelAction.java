package com.siga.censo.action;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.DocuShareHelper;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.paginadores.PaginadorVector;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.censo.form.DatosColegialesForm;
import com.siga.censo.vos.DocuShareObjectVO;
import com.siga.censo.vos.DocuShareObjectVO.DocuShareTipo;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 * Action de la documentación de censo
 */
public class CensoDocumentacionRegTelAction extends MasterAction {

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrir(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
//	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
//		String salto = null;
//		String nombre = null;
//		String numero = "";
//		DatosColegialesForm miForm = (DatosColegialesForm) formulario;
//		UsrBean userBean = (UsrBean) request.getSession().getAttribute("USRBEAN");
//		
//		try {
//			CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm (this.getUserBean(request));
//			CenPersonaAdm personaAdm = new CenPersonaAdm(userBean);				
//			CenColegiadoBean colegiadoBean = colegiadoAdm.getDatosColegiales(new Long(miForm.getIdPersona()), new Integer(miForm.getIdInstitucion()));
//
//			if (colegiadoBean == null) {				
//				throw new SIGAException("messages.censo.docushare.colegiadoNoEncontrado");
//			}
//			
//			numero = colegiadoAdm.getIdentificadorColegiado(colegiadoBean);
//			nombre = personaAdm.obtenerNombreApellidos(String.valueOf(miForm.getIdPersona()));
//			request.setAttribute("NOMBRE", nombre);
//			request.setAttribute("NUMERO", numero);
//
//			DocuShareHelper docuShareHelper = new DocuShareHelper(userBean);
//
//			if (colegiadoBean.getIdentificadorDS() == null || colegiadoBean.getIdentificadorDS().trim().equals("")) {
//				String idDS = null;
//				if (colegiadoBean.getComunitario()!=null && colegiadoBean.getComunitario().equals("1")){
//					idDS = docuShareHelper.buscaCollectionCenso(colegiadoBean.getNComunitario());
//				} else {
//					idDS = docuShareHelper.buscaCollectionCenso(colegiadoBean.getNColegiado());
//				}
//				if (idDS != null) {
//					colegiadoBean.setIdentificadorDS(idDS);
//					colegiadoAdm.updateDirect(colegiadoBean);
//				}
//			}
//
//			if (colegiadoBean.getIdentificadorDS() == null || colegiadoBean.getIdentificadorDS().trim().equals("")) {
//				throw new SIGAException("messages.censo.docushare.faltaColeccion");				
//			}
//			miForm.setUrlDocumentacionDS(getURLdocumentacionDS(docuShareHelper, colegiadoBean.getIdentificadorDS()));
//
//			salto = "inicioDS";
//
//		} catch (Exception e) {
//			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null); 
//		} 
//		return salto;
//	}
	
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String salto = null;
		String nombre = null;
		String numero = "";
		DatosColegialesForm miForm = (DatosColegialesForm) formulario;
		UsrBean userBean = (UsrBean) request.getSession().getAttribute("USRBEAN");
		
		try {
			
			request.getSession().removeAttribute("DATAPAGINADOR");

			CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm (this.getUserBean(request));
			CenPersonaAdm personaAdm = new CenPersonaAdm(userBean);				
			CenColegiadoBean colegiadoBean = colegiadoAdm.getDatosColegiales(new Long(miForm.getIdPersona()), new Integer(miForm.getIdInstitucion()));

			if (colegiadoBean == null) {				
				throw new SIGAException("messages.censo.docushare.colegiadoNoEncontrado");
			}
			
			numero = colegiadoAdm.getIdentificadorColegiado(colegiadoBean);
			nombre = personaAdm.obtenerNombreApellidos(String.valueOf(miForm.getIdPersona()));
			request.setAttribute("NOMBRE", nombre);
			request.setAttribute("NUMERO", numero);

			DocuShareHelper docuShareHelper = new DocuShareHelper(userBean);

			if (colegiadoBean.getIdentificadorDS() == null || colegiadoBean.getIdentificadorDS().trim().equals("")) {
				String idDS = null;
				if (colegiadoBean.getComunitario()!=null && colegiadoBean.getComunitario().equals("1")){
					idDS = docuShareHelper.buscaCollectionCenso(colegiadoBean.getNComunitario());
				} else {
					idDS = docuShareHelper.buscaCollectionCenso(colegiadoBean.getNColegiado());
				}
				if (idDS != null) {
					colegiadoBean.setIdentificadorDS(idDS);
					colegiadoAdm.updateDirect(colegiadoBean);
				}
			}

			if (colegiadoBean.getIdentificadorDS() == null || colegiadoBean.getIdentificadorDS().trim().equals("")) {
				throw new SIGAException("messages.censo.docushare.faltaColeccion");				
			}
		
			
			request.setAttribute("IDENTIFICADORDS", colegiadoBean.getIdentificadorDS());
			
			request.getSession().removeAttribute("MIGAS_DS");			
			
			request.getSession().setAttribute("accion","ver");

			salto = "inicioDS";

		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null); 
		} 
		return salto;
	}
	
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		Hashtable miHash = new Hashtable();
		
		try {

			DatosColegialesForm miForm = (DatosColegialesForm) formulario;
			
			miHash = miForm.getDatos();

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
					if (miForm.getPosicionDs() != null) {
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
	
	protected String ver (ActionMapping mapping,
			MasterForm formulario,
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException{
		
		request.getSession().removeAttribute("DATAPAGINADOR");
		
		DatosColegialesForm miForm = (DatosColegialesForm) formulario;
		
		if (miForm.getIdentificadorDs() == null) {
			Vector ocultos = miForm.getDatosTablaOcultos(0);
			miForm.setIdentificadorDs((String) ocultos.get(0));
			miForm.setTitleDs((String) ocultos.get(1));
		}
		
		buscarPor(mapping, formulario, request, response);
		
		return "listadoCollection";
	}
	
	protected String download (ActionMapping mapping,
			MasterForm formulario,
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException {
		
		DatosColegialesForm miForm = (DatosColegialesForm) formulario;
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



	/**
	 * Obtiene la url del DocuShare para el identificador de la colección pasada por parametro
	 * @param docuShareHelper
	 * @param identificadorDS
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	private String getURLdocumentacionDS(DocuShareHelper docuShareHelper, String identificadorDS) throws ClsExceptions, SIGAException {
		if (identificadorDS == null || identificadorDS.trim().equals("")) {
			//El colegiado no tiene Documentación asociada
			throw new SIGAException("messages.censo.docushare.sinIdentificador");
		}

		return docuShareHelper.getURLCollection(identificadorDS);
	}
}
