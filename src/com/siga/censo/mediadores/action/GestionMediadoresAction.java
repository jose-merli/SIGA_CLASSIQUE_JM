package com.siga.censo.mediadores.action;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.autogen.model.EcomCenDatosExample;
import org.redabogacia.sigaservices.app.autogen.model.EcomCenWsEnvioExample;
import org.redabogacia.sigaservices.app.model.EcomCenWsEnvioExtended;
import org.redabogacia.sigaservices.app.services.cen.CenWSService;

import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.paginadores.PaginadorVector;
import com.siga.beans.CenInstitucionAdm;
import com.siga.censo.mediadores.form.MediadoresImportForm;
import com.siga.censo.service.CensoService;
import com.siga.censo.ws.form.EdicionRemesaForm;
import com.siga.censo.ws.util.CombosCenWS;
import com.siga.comun.vos.InstitucionVO;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

import es.satec.businessManager.BusinessManager;

public class GestionMediadoresAction extends MasterAction {	
	
	public static final String DATAPAGINADOR = "DATAPAGINADOR_MEDIADORES_IMPORT";
	
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
			return super.executeInternal(mapping,formulario,request,response);			
		} catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.censo"});
		}
	}
	
	protected String abrir (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException {
		
		try {
			
			MediadoresImportForm mediadoresImportForm = (MediadoresImportForm) formulario;
			mediadoresImportForm.reset();
						
			request.getSession().removeAttribute(DATAPAGINADOR);
			
		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		return "inicio";
		
	}

	
}
