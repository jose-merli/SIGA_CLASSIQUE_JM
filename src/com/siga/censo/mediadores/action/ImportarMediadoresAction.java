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

public class ImportarMediadoresAction extends MasterAction {	
	
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
			if (miForm != null) {
				return super.executeInternal(mapping,formulario,request,response);				
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
	
	protected String abrir (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException {
		
		try {
			MediadoresImportForm mediadoresImportForm = (MediadoresImportForm) formulario;
						
			request.getSession().removeAttribute(DATAPAGINADOR);
			
			CenInstitucionAdm institucionAdm = new CenInstitucionAdm(getUserBean(request));
			String nombreInstitucionAcceso=institucionAdm.getNombreInstitucion(getUserBean(request).getLocation());
			mediadoresImportForm.setNombreColegio(nombreInstitucionAcceso);
			mediadoresImportForm.setIdColegio(getUserBean(request).getLocation());

			mediadoresImportForm.setInstituciones(getColegiosDependientes(getUserBean(request).getLocation()));
			
		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.censo"},e,null);
		}
		return "inicio";
		
	}

	
	/**
	 * Recupera los colegios asociados a la institucion <code>idInstitucion</code>
	 * @param idInstitucion Institucion para la cual se buscan sus colegios asociados
	 * @return Una lista con los colegios dependientes de la institucion, o <code>null</code> si la institucion
	 * no tiene colegios dependientes, es decir, si es un Colegio y no un Consejo.
	 * @throws SIGAException 
	 */
	private List<InstitucionVO> getColegiosDependientes(String idInstitucion) throws SIGAException{
		List<InstitucionVO> instituciones = null;
		//Si la institucion conectada es General se recuperan todos los colegios (no los consejos)
		if (institucionEsGeneral(idInstitucion)){
			CensoService service = (CensoService) getBusinessManager().getService(CensoService.class);
			instituciones = service.getColegiosNoConsejo(idInstitucion);
		}
		//Si la institucion no conectada es un Consejo, se recuperan sus colegios dependientes
		else if (institucionEsConsejo(idInstitucion)){
			CensoService service = (CensoService) getBusinessManager().getService(CensoService.class);
			instituciones = service.getColegiosDeConsejo(idInstitucion);
		}
		return instituciones;
	}
	
			
	
}
