package com.siga.censo.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.DocuShareHelper;
import com.atos.utils.UsrBean;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.censo.form.DatosColegialesForm;
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
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String salto = null;
		String nombre = null;
		String numero = "";
		DatosColegialesForm miForm = (DatosColegialesForm) formulario;
		UsrBean userBean = (UsrBean) request.getSession().getAttribute("USRBEAN");
		
		try {
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
			miForm.setUrlDocumentacionDS(getURLdocumentacionDS(docuShareHelper, colegiadoBean.getIdentificadorDS()));

			salto = "inicioDS";

		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null); 
		} 
		return salto;
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
