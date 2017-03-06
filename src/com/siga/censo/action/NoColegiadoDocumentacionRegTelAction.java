package com.siga.censo.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.exceptions.SIGAServiceException;
import org.redabogacia.sigaservices.app.helper.DocuShareHelper;

import com.atos.utils.ClsConstants;
import com.atos.utils.UsrBean;
import com.siga.beans.CenNoColegiadoAdm;
import com.siga.beans.CenNoColegiadoBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.censo.form.DatosColegialesForm;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.action.DocumentacionRegTelAction;

/**
 * Action de la documentación de censo
 */
public class NoColegiadoDocumentacionRegTelAction extends DocumentacionRegTelAction {

	/**
	 * 	
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String salto = null;
		String nombre = null;
		String idNoColegiado = "";
		DatosColegialesForm miForm = (DatosColegialesForm) formulario;
		UsrBean userBean = (UsrBean) request.getSession().getAttribute("USRBEAN");
		
		try {
			request.getSession().removeAttribute("DATAPAGINADOR");

			CenNoColegiadoAdm noColegiadoAdm = new CenNoColegiadoAdm (userBean);
			CenPersonaAdm personaAdm = new CenPersonaAdm(userBean);				
			CenNoColegiadoBean noColegiadoBean = noColegiadoAdm.existeNoColegiadoInstitucion(new Long(miForm.getIdPersona()), getIDInstitucion(request));

			if (noColegiadoBean == null) {				
				throw new SIGAException("messages.censo.docushare.colegiadoNoEncontrado");
			}
			CenPersonaBean personaBean = personaAdm.getPersonaPorId(miForm.getIdPersona());
			idNoColegiado = noColegiadoAdm.getIdentificadorColegiado(personaBean);
			nombre = personaAdm.obtenerNombreApellidos(String.valueOf(miForm.getIdPersona()));
			request.setAttribute("NOMBRE", nombre);
			request.setAttribute("ID_NOCOLEGIADO", idNoColegiado);
			request.setAttribute("IDPERSONA", miForm.getIdPersona());
			
			short idInstitucion = Short.parseShort(miForm.getIdInstitucion());
			DocuShareHelper docuShareHelper = new DocuShareHelper(idInstitucion);

			if (noColegiadoBean.getIdentificadorDS() == null || noColegiadoBean.getIdentificadorDS().trim().equals("")) {
				String idDS = null;
				if (personaBean.getIdTipoIdentificacion() == ClsConstants.TIPO_IDENTIFICACION_NIF) {
					idDS = docuShareHelper.buscaCollectionNoColegiado("NIF " + personaBean.getNIFCIF());
				} else if (personaBean.getIdTipoIdentificacion() == ClsConstants.TIPO_IDENTIFICACION_CIF) {
					idDS = docuShareHelper.buscaCollectionNoColegiado("CIF " + personaBean.getNIFCIF());
				} else if (personaBean.getIdTipoIdentificacion() == ClsConstants.TIPO_IDENTIFICACION_TRESIDENTE) {
					idDS = docuShareHelper.buscaCollectionNoColegiado("NIE " + personaBean.getNIFCIF());
				}				
				if (idDS != null) {
					noColegiadoBean.setIdentificadorDS(idDS);
					noColegiadoAdm.updateDirect(noColegiadoBean);
				}
			}
		
			request.setAttribute("IDENTIFICADORDS", noColegiadoBean.getIdentificadorDS());
			request.getSession().removeAttribute("MIGAS_DS");			
			request.setAttribute("ACTION","/CEN_NoColegiado_DocumentacionRegTel.do?noReset=true");
			salto = "inicioDS";

		} catch (SIGAServiceException e) {
			throw new SIGAException(e.getMessage(), e);
			
		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null); 
		} 
		
		return salto;
	}
	


	@Override
	protected String createCollection(MasterForm formulario, HttpServletRequest request) throws Exception {
		UsrBean userBean = this.getUserBean(request);
		CenNoColegiadoAdm noColegiadoAdm = new CenNoColegiadoAdm(userBean);
		CenPersonaAdm personaAdm = new CenPersonaAdm(userBean);
		DatosColegialesForm miForm = (DatosColegialesForm) formulario;
		String idDS = null;

		CenNoColegiadoBean noColegiadoBean = noColegiadoAdm.existeNoColegiadoInstitucion(new Long(miForm.getIdPersona()), getIDInstitucion(request));
		if (noColegiadoBean == null) {
			throw new SIGAException("messages.censo.docushare.colegiadoNoEncontrado");
		}
		CenPersonaBean personaBean = personaAdm.getPersonaPorId(miForm.getIdPersona());

		if (personaBean != null) {
			
			String title = "";
			if (personaBean.getIdTipoIdentificacion() == ClsConstants.TIPO_IDENTIFICACION_NIF) {
				title = "NIF " + personaBean.getNIFCIF();
			} else if (personaBean.getIdTipoIdentificacion() == ClsConstants.TIPO_IDENTIFICACION_CIF) {
				title = "CIF " + personaBean.getNIFCIF();
			} else if (personaBean.getIdTipoIdentificacion() == ClsConstants.TIPO_IDENTIFICACION_TRESIDENTE) {
				title = "NIE " + personaBean.getNIFCIF();
			}				
			
			String description=personaBean.getNombreCompleto();
			/** Se crea la coleccion **/
			short idInstitucion = getIDInstitucion(request).shortValue();
			DocuShareHelper docuShareHelper = new DocuShareHelper(idInstitucion);
			idDS = docuShareHelper.createCollectionNoColegiado(title,description);
			noColegiadoBean.setIdentificadorDS(idDS);

			/** Se actualiza el registro en la tabla de No Colegiados **/
			noColegiadoAdm.updateDirect(noColegiadoBean);
		}

		return idDS;
	}
}
