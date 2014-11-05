/*
 * Created on Jan 18, 2005
 * @author emilio.grau
 *
 */
package com.siga.gratuita.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.exceptions.SIGAServiceException;
import org.redabogacia.sigaservices.app.helper.DocuShareHelper;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.beans.ScsEJGAdm;
import com.siga.beans.ScsEJGBean;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefinirEJGForm;

/**
 * Action de la documentaci�n de un expediente
 */
public class EJGDocumentacionRegTelAction extends DocumentacionRegTelAction {

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrir(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String salto = null;

		DefinirEJGForm form = (DefinirEJGForm) formulario;
		
		request.getSession().removeAttribute("DATAPAGINADOR");

		request.getSession().removeAttribute("DATABACKUP");

		Vector v = new Vector();
		Hashtable miHash = new Hashtable();
		UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");

		miHash.put("ANIO", request.getParameter("ANIO").toString());
		miHash.put("NUMERO", request.getParameter("NUMERO").toString());
		miHash.put("IDTIPOEJG", request.getParameter("IDTIPOEJG").toString());
		miHash.put("IDINSTITUCION", request.getParameter("IDINSTITUCION").toString());

		ScsEJGAdm admEJG = new ScsEJGAdm(this.getUserBean(request));

		try {

			v = admEJG.selectPorClave(miHash);
			if (v == null || v.size() != 1) {				
				throw new SIGAException("expedientes.docushare.error.EJGNoEncontrado");
			}
			ScsEJGBean scsEJGBean = (ScsEJGBean) v.get(0);
			
			short idInstitucion = scsEJGBean.getIdInstitucion().shortValue();

			DocuShareHelper docuShareHelper = new DocuShareHelper(idInstitucion);

			if (scsEJGBean.getIdentificadorDS() == null || scsEJGBean.getIdentificadorDS().trim().equals("")) {
				if (scsEJGBean.getAnio() != null) {
					String title = DocuShareHelper.getTitleEJG(scsEJGBean.getAnio().toString(), scsEJGBean.getNumEJG());
					String idDS = docuShareHelper.buscaCollectionEJG(title);
										
					if (idDS != null) {
						scsEJGBean.setIdentificadorDS(idDS);
						admEJG.updateDirect(scsEJGBean);
					}
				}
			}
//			if (scsEJGBean.getIdentificadorDS() == null || scsEJGBean.getIdentificadorDS().trim().equals("")) {
//				throw new SIGAException("expedientes.docushare.error.faltaColeccion");				
//			}
			
			request.getSession().setAttribute("DATABACKUP", admEJG.beanToHashTable((ScsEJGBean) v.get(0)));
			
			request.setAttribute("IDENTIFICADORDS", scsEJGBean.getIdentificadorDS());
			
			
						
			request.getSession().removeAttribute("MIGAS_DS");						
			

			salto = "inicioDS";

		} catch (Exception e) {
			throwExcp("messages.general.error", e, null);

		} 

		return salto;
	}


	@Override
	protected String createCollection(MasterForm formulario, HttpServletRequest request) throws SIGAServiceException, ClsExceptions {
		Hashtable hashtable = (Hashtable)request.getSession().getAttribute("DATABACKUP");
		
		String title = DocuShareHelper.getTitleEJG(hashtable.get(ScsEJGBean.C_ANIO).toString(), hashtable.get(ScsEJGBean.C_NUMEJG).toString());
		short idInstitucion = Short.valueOf((String)hashtable.get(ScsEJGBean.C_IDINSTITUCION));
		DocuShareHelper docuShareHelper = new DocuShareHelper(idInstitucion);
		String idDS = docuShareHelper.createCollectionEJG(title);
		hashtable.put(ScsEJGBean.C_IDENTIFICADORDS, idDS);
		
		ScsEJGAdm admEJG = new ScsEJGAdm(this.getUserBean(request));
		admEJG.updateDirect(admEJG.hashTableToBean(hashtable));
		
		return idDS;
	}
}
