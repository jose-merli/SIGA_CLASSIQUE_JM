package com.siga.gratuita.action;

import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.autogen.model.FacBancoinstitucion;
import org.redabogacia.sigaservices.app.autogen.model.ScsEjgPrestacionRechazada;
import org.redabogacia.sigaservices.app.services.fac.CuentasBancariasService;
import org.redabogacia.sigaservices.app.services.scs.ScsEjgPrestacionRechazadaService;
import org.redabogacia.sigaservices.app.services.scs.ScsPrestacionService;
import org.redabogacia.sigaservices.app.vo.CuentaBancariaVo;
import org.redabogacia.sigaservices.app.vo.scs.PrestacionRechazadaEjgVo;
import org.redabogacia.sigaservices.app.vo.scs.PrestacionVo;
import org.redabogacia.sigaservices.app.vo.services.VoService;

import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.beans.ScsEJGAdm;
import com.siga.facturacion.form.CuentasBancariasForm;
import com.siga.facturacion.service.CuentaBancariaVoService;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefinirDictamenEJGForm;
import com.siga.gratuita.form.PrestacionRechazadaEjgForm;
import com.siga.gratuita.service.PrestacionRechazadaEjgVoService;

import es.satec.businessManager.BusinessManager;

/**
 * 
 * @author jorgeta 
 * @date   27/06/2013
 *
 * La imaginaci�n es m�s importante que el conocimiento
 *
 */
public class PrestacionesRechazadasEJGAction extends MasterAction {
	
	protected ActionForward executeInternal(ActionMapping mapping,
		      ActionForm formulario,
		      HttpServletRequest request, 
		      HttpServletResponse response) throws SIGAException {
		
		MasterForm miForm = (MasterForm) formulario;
		if (miForm == null)
			try {
				return mapping.findForward(this.abrir(mapping, miForm, request, response));
			}catch(Exception e){
				return mapping.findForward("exception");
			}
		else return super.executeInternal(mapping, formulario, request, response); 
	}
	

	/**
	 * Rellena un hash con los valores recogidos del formulario y los modifica en la base de datos.
	 * 
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la informaci�n. De tipo MasterForm.
	 * @param request Informaci�n de sesi�n. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicar� la siguiente acci�n a llevar a cabo. 
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
	
		PrestacionRechazadaEjgForm prestacionRechazadaForm = (PrestacionRechazadaEjgForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		String forward="exception";
		try {
			BusinessManager bm = getBusinessManager();
			ScsEjgPrestacionRechazadaService prestacionService = (ScsEjgPrestacionRechazadaService)bm.getService(ScsEjgPrestacionRechazadaService.class);
			VoService<PrestacionRechazadaEjgForm, PrestacionRechazadaEjgVo, ScsEjgPrestacionRechazada> voService = new PrestacionRechazadaEjgVoService();
			PrestacionRechazadaEjgVo prestacionRechazadaVo =  voService.getForm2Vo(prestacionRechazadaForm);
			prestacionRechazadaVo.setUsumodificacion(new Integer(usrBean.getUserName()));
			prestacionService.actualizarEjgPrestacionesRechazadas(prestacionRechazadaVo);
			forward = exito("messages.updated.success",request);
			
			
		}catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}
		
		return forward;
	}

	
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		UsrBean usrBean = this.getUserBean(request);
		PrestacionRechazadaEjgForm prestacionesEJGForm = (PrestacionRechazadaEjgForm)formulario;
		if(prestacionesEJGForm.getEjgAnio()!=null && !prestacionesEJGForm.getEjgAnio().equals("")){
			prestacionesEJGForm.setEjgAnio(request.getParameter("ANIO").toString());
			prestacionesEJGForm.setEjgIdInstitucion(usrBean.getLocation().toString());
			prestacionesEJGForm.setEjgIdTipo(request.getParameter("IDTIPOEJG").toString());
			prestacionesEJGForm.setEjgNumero(request.getParameter("NUMERO").toString());
			prestacionesEJGForm.setSolicitante(request.getParameter("solicitante").toString());
			prestacionesEJGForm.setEjgNumEjg(request.getParameter("ejgNumEjg").toString());
		}
		String forward = "inicio";
		try {
			BusinessManager bm = getBusinessManager();
			ScsPrestacionService prestacionService = (ScsPrestacionService)bm.getService(ScsPrestacionService.class);
			PrestacionVo prestacionVo = new PrestacionVo();
			prestacionVo.setIdinstitucion(Short.valueOf(usrBean.getLocation().toString()));
			prestacionVo.setIdioma(usrBean.getLanguage());
			request.setAttribute("prestaciones", prestacionService.getList(prestacionVo));
			
			ScsEjgPrestacionRechazadaService prestacionRechazadaService = (ScsEjgPrestacionRechazadaService)bm.getService(ScsEjgPrestacionRechazadaService.class);
			VoService<PrestacionRechazadaEjgForm, PrestacionRechazadaEjgVo, ScsEjgPrestacionRechazada> voService = new PrestacionRechazadaEjgVoService();
			List<PrestacionRechazadaEjgVo> prestacionRechazadaVos =prestacionRechazadaService.getPrestacionesRechazadas(voService.getForm2Vo(prestacionesEJGForm));
			List<PrestacionRechazadaEjgForm> prestacionesRechazadas = voService.getVo2FormList(prestacionRechazadaVos);
			prestacionesEJGForm.setModo("modificar");
			request.setAttribute("prestacionesRechazadas", prestacionesRechazadas);
			
			
		} catch (Exception e){
			
			throwExcp("messages.general.errorExcepcion", e, null); 
			
		}
		return forward;
	}
	
	
}