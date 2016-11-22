package com.siga.censo.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.exceptions.SIGAServiceException;
import org.redabogacia.sigaservices.app.helper.DocuShareHelper;

import com.atos.utils.UsrBean;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.censo.form.DatosColegialesForm;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.action.DocumentacionRegTelAction;

/**
 * Action de la documentaci�n de censo
 */
public class CensoDocumentacionRegTelAction extends DocumentacionRegTelAction {

	/**
	 * 	
	 */
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
			request.setAttribute("IDPERSONA", miForm.getIdPersona());
			
			short idInstitucion = Short.parseShort(miForm.getIdInstitucion());

			DocuShareHelper docuShareHelper = new DocuShareHelper(idInstitucion);

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
			
			request.setAttribute("IDENTIFICADORDS", colegiadoBean.getIdentificadorDS());
			request.getSession().removeAttribute("MIGAS_DS");
			request.setAttribute("ACTION","/CEN_Censo_DocumentacionRegTel.do?noReset=true");
			
			salto = "inicioDS";

		} catch (SIGAServiceException e) {
			throw new SIGAException(e.getMessage(), e);

		} catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null); 
		} 
		return salto;
	}
	


	@Override
	protected String createCollection(MasterForm formulario, HttpServletRequest request) throws Exception {
		CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm (this.getUserBean(request));
		DatosColegialesForm miForm = (DatosColegialesForm) formulario;
		
		CenColegiadoBean colegiadoBean = colegiadoAdm.getDatosColegiales(new Long(miForm.getIdPersona()), getIDInstitucion(request));
		
		String title = null;
		if (colegiadoBean.getComunitario()!=null && colegiadoBean.getComunitario().equals("1")){
			title = colegiadoBean.getNComunitario();
		} else {
			title = colegiadoBean.getNColegiado();
		}
		short idInstitucion = getIDInstitucion(request).shortValue();
		DocuShareHelper docuShareHelper = new DocuShareHelper(idInstitucion);
		String idDS = docuShareHelper.createCollectionCenso(title);
		colegiadoBean.setIdentificadorDS(idDS);		
		
		colegiadoAdm.updateDirect(colegiadoBean);
		
		return idDS;
	}
}
