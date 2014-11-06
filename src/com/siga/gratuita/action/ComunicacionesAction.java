package com.siga.gratuita.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.AppConstants.TipoIntercambioEnum;
import org.redabogacia.sigaservices.app.autogen.model.EnvEntradaEnvios;
import org.redabogacia.sigaservices.app.autogen.model.EnvEnvios;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.TransformBeanToForm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.administracion.SIGAConstants;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.ScsDesignaBean;
import com.siga.envios.EnvioInformesGenericos;
import com.siga.envios.form.DefinirEnviosForm;
import com.siga.envios.form.EntradaEnviosForm;
import com.siga.envios.service.EntradaEnviosService;
import com.siga.envios.service.SalidaEnviosService;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.ComunicacionesForm;

import es.satec.businessManager.BusinessManager;

/**
 * 
 * @author jorgeta 
 * @date   01/08/2013
 *
 * La imaginaci�n es m�s importante que el conocimiento
 *
 */
public class ComunicacionesAction extends MasterAction {
	
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
	

	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		if(mapping.getParameter()!=null && mapping.getParameter().equals("EJG")){
			return abrirEJG(mapping, formulario, request, response);
		}else {
			return abrirDesignacion(mapping, formulario, request, response);
		}
		
	}
	
	protected String abrirEJG(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		
		UsrBean usrBean = this.getUserBean(request);
		
		
//		comunicacionesSalida
		ComunicacionesForm comunicacionesForm = (ComunicacionesForm)formulario;
		String accion = (String)request.getSession().getAttribute("accion");
		comunicacionesForm.setModo(accion);
		
		if(request.getParameter("ANIO")!=null && !request.getParameter("ANIO").toString().equals("")){
			comunicacionesForm.setEjgAnio(request.getParameter("ANIO").toString());
			comunicacionesForm.setEjgIdInstitucion(request.getParameter("IDINSTITUCION").toString());
			comunicacionesForm.setEjgIdTipo(request.getParameter("IDTIPOEJG").toString());
			comunicacionesForm.setEjgNumero(request.getParameter("NUMERO").toString());
			comunicacionesForm.setSolicitante(request.getParameter("solicitante").toString());
			comunicacionesForm.setEjgNumEjg(request.getParameter("ejgNumEjg").toString());
			comunicacionesForm.setAnio(comunicacionesForm.getEjgAnio());
			comunicacionesForm.setCodigoDesignaNumEJG(comunicacionesForm.getEjgNumEjg());
		}
		
		String forward = "inicio";
		List<EntradaEnviosForm> comunicacionesEntrada = new ArrayList<EntradaEnviosForm>();
		List<DefinirEnviosForm> comunicacionesSalida = new ArrayList<DefinirEnviosForm>();

		try {
			BusinessManager businessManager =  BusinessManager.getInstance();
			
			EntradaEnviosService entradaEnviosService = (EntradaEnviosService) businessManager.getService(EntradaEnviosService.class);
			UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
			comunicacionesForm.setComisionAJG(userBean.isComision()?"1":"0");
			List<EnvEntradaEnvios> entradaEnvios = entradaEnviosService.getComunicacionesEJG(Integer.parseInt(comunicacionesForm.getEjgIdInstitucion()),
					Short.parseShort(comunicacionesForm.getEjgAnio()),Short.parseShort(comunicacionesForm.getEjgIdTipo()),Integer.parseInt(comunicacionesForm.getEjgNumero())
					,Short.parseShort(comunicacionesForm.getComisionAJG()));
			
			for(EnvEntradaEnvios entrada: entradaEnvios){
				EntradaEnviosForm entradaEnvioFormulario = TransformBeanToForm.getEntradaEnviosForm(entrada);
				if(entradaEnvioFormulario.getIdTipoIntercambioTelematico()!=null && entradaEnvioFormulario.getIdTipoIntercambioTelematico().equals(TipoIntercambioEnum.SGP_ICA_ENV_SOL_ABG_PRO.getCodigo())){
					entradaEnviosService.updateFormularioDatosSolDesignaProvisional(entradaEnvioFormulario);
				} else if(entradaEnvioFormulario.getIdTipoIntercambioTelematico()!=null && entradaEnvioFormulario.getIdTipoIntercambioTelematico().equals(TipoIntercambioEnum.SGP_ICA_RESP_SOL_SUSP_PROC.getCodigo())){
					entradaEnviosService.updateFormularioDatosRespuestaSuspensionProcedimiento(entradaEnvioFormulario);
				}
				entradaEnvioFormulario.setModo(comunicacionesForm.getModo());
				
				comunicacionesEntrada.add(entradaEnvioFormulario);
			}
			
			SalidaEnviosService salidaEnviosService = (SalidaEnviosService) businessManager.getService(SalidaEnviosService.class);
			List<EnvEnvios> salidaEnvios = salidaEnviosService.getComunicacionesEJG(Integer.parseInt(comunicacionesForm.getEjgIdInstitucion()),
					Short.parseShort(comunicacionesForm.getEjgAnio()),Short.parseShort(comunicacionesForm.getEjgIdTipo()),Integer.parseInt(comunicacionesForm.getEjgNumero()),
					Short.parseShort(comunicacionesForm.getComisionAJG()));
			
			for(EnvEnvios salida: salidaEnvios){
				DefinirEnviosForm salidaEnviosForm = TransformBeanToForm.getSalidaEnviosForm(salida);
				salidaEnviosForm.setEstado(UtilidadesMultidioma.getDatoMaestroIdioma(salidaEnviosForm.getEstado(), userBean));
				salidaEnviosForm.setTipoEnvio(UtilidadesMultidioma.getDatoMaestroIdioma(salidaEnviosForm.getTipoEnvio(), userBean));
				
				salidaEnviosForm.setModo(comunicacionesForm.getModo());
				comunicacionesSalida.add(salidaEnviosForm);
			}
			GenParametrosAdm paramAdm = new GenParametrosAdm (userBean);
			String prefijoExpedienteCajg = paramAdm.getValor (comunicacionesForm.getEjgIdInstitucion(), ClsConstants.MODULO_SJCS, ClsConstants.GEN_PARAM_PREFIJO_EXPEDIENTES_CAJG, " ");
			request.setAttribute("PREFIJOEXPEDIENTECAJG",prefijoExpedienteCajg);
			//Miramos si tienem permiso de envio sya que si no tiene no le vamos a mostrar los iconos de envios
			request.setAttribute("PERMISOENVIOS",ClsConstants.DB_TRUE);
			String accessEnvio = testAccess(request.getContextPath()+"/ENV_DefinirEnvios.do",null,request);
			if (!accessEnvio.equals(SIGAConstants.ACCESS_READ) && !accessEnvio.equals(SIGAConstants.ACCESS_FULL)) {
				//Miramos si tienen acceso a las comunicaciones de designaiones o ejgs, en tal caso se les permite enviar
				request.setAttribute("PERMISOENVIOS",ClsConstants.DB_FALSE);

			}
			//hacemos lo siguiente para setear el permiso de esta accion
			testAccess(request.getContextPath()+mapping.getPath()+".do",null,request);
			

		} catch (Exception e) {
			this.throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
		}

		request.setAttribute("comunicacionesEntrada", comunicacionesEntrada);     
		request.setAttribute("comunicacionesSalida", comunicacionesSalida);
		
		return forward;
	}
	
	protected String abrirDesignacion(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		
		UsrBean usrBean = this.getUserBean(request);
		
		
//		comunicacionesSalida
		ComunicacionesForm comunicacionesForm = (ComunicacionesForm)formulario;
		String accion = (String)request.getSession().getAttribute("Modo");
		comunicacionesForm.setModo(accion);
		/*	UtilidadesHash.set(resultado,ScsDesignaBean.C_ANIO, 				(String)request.getParameter("ANIO"));
				UtilidadesHash.set(resultado,ScsDesignaBean.C_NUMERO, 				(String)request.getParameter("NUMERO"));
				UtilidadesHash.set(resultado,ScsDesignaBean.C_IDINSTITUCION,		(String)usr.getLocation());
				UtilidadesHash.set(resultado,ScsDesignaBean.C_IDTURNO,				(String)request.getParameter("IDTURNO"));*/
		if(request.getParameter("ANIO")!=null && !request.getParameter("ANIO").toString().equals("")){
			comunicacionesForm.setDesignaAnio(request.getParameter("ANIO").toString());
			comunicacionesForm.setDesignaIdInstitucion(request.getParameter("IDINSTITUCION").toString());
			comunicacionesForm.setDesignaIdTurno(request.getParameter("IDTURNO").toString());
			comunicacionesForm.setDesignaNumero(request.getParameter("NUMERO").toString());
			comunicacionesForm.setAnio(comunicacionesForm.getDesignaAnio());
			comunicacionesForm.setCodigoDesignaNumEJG(request.getParameter("designaCodigo").toString());
			comunicacionesForm.setSolicitante(request.getParameter("solicitante").toString());
		}
		
		String forward = "inicio";
		List<EntradaEnviosForm> comunicacionesEntrada = new ArrayList<EntradaEnviosForm>();
		List<DefinirEnviosForm> comunicacionesSalida = new ArrayList<DefinirEnviosForm>();

		try {
			BusinessManager businessManager =  BusinessManager.getInstance();
			
			EntradaEnviosService entradaEnviosService = (EntradaEnviosService) businessManager.getService(EntradaEnviosService.class);
			UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
			comunicacionesForm.setComisionAJG(userBean.isComision()?"1":"0");
			List<EnvEntradaEnvios> entradaEnvios = entradaEnviosService.getComunicacionesDesigna(Integer.parseInt(comunicacionesForm.getDesignaIdInstitucion()),
					Short.parseShort(comunicacionesForm.getDesignaAnio()),Short.parseShort(comunicacionesForm.getDesignaIdTurno()),Integer.parseInt(comunicacionesForm.getDesignaNumero())
					,Short.parseShort(comunicacionesForm.getComisionAJG()));
			
			for(EnvEntradaEnvios entrada: entradaEnvios){
				EntradaEnviosForm entradaEnvioFormulario = TransformBeanToForm.getEntradaEnviosForm(entrada);
				if(entradaEnvioFormulario.getIdTipoIntercambioTelematico()!=null && entradaEnvioFormulario.getIdTipoIntercambioTelematico().equals(TipoIntercambioEnum.SGP_ICA_ENV_SOL_ABG_PRO.getCodigo())){
					entradaEnviosService.updateFormularioDatosSolDesignaProvisional(entradaEnvioFormulario);
				} else if(entradaEnvioFormulario.getIdTipoIntercambioTelematico()!=null && entradaEnvioFormulario.getIdTipoIntercambioTelematico().equals(TipoIntercambioEnum.SGP_ICA_RESP_SOL_SUSP_PROC.getCodigo())){
					entradaEnviosService.updateFormularioDatosRespuestaSuspensionProcedimiento(entradaEnvioFormulario);
				}
				entradaEnvioFormulario.setModo(comunicacionesForm.getModo());
				
				comunicacionesEntrada.add(entradaEnvioFormulario);
			}
			
			SalidaEnviosService salidaEnviosService = (SalidaEnviosService) businessManager.getService(SalidaEnviosService.class);
			List<EnvEnvios> salidaEnvios = salidaEnviosService.getComunicacionesDesigna(Integer.parseInt(comunicacionesForm.getDesignaIdInstitucion()),
					Short.parseShort(comunicacionesForm.getDesignaAnio()),Short.parseShort(comunicacionesForm.getDesignaIdTurno()),Integer.parseInt(comunicacionesForm.getDesignaNumero())
					,Short.parseShort(comunicacionesForm.getComisionAJG()));
			
			for(EnvEnvios salida: salidaEnvios){
				DefinirEnviosForm salidaEnviosForm = TransformBeanToForm.getSalidaEnviosForm(salida);
				salidaEnviosForm.setEstado(UtilidadesMultidioma.getDatoMaestroIdioma(salidaEnviosForm.getEstado(), userBean));
				salidaEnviosForm.setTipoEnvio(UtilidadesMultidioma.getDatoMaestroIdioma(salidaEnviosForm.getTipoEnvio(), userBean));
				
				salidaEnviosForm.setModo(comunicacionesForm.getModo());
				comunicacionesSalida.add(salidaEnviosForm);
			}
			
			//Miramos si tienem permiso de envio sya que si no tiene no le vamos a mostrar los iconos de envios
			request.setAttribute("PERMISOENVIOS",ClsConstants.DB_TRUE);
			String accessEnvio = testAccess(request.getContextPath()+"/ENV_DefinirEnvios.do",null,request);
			if (!accessEnvio.equals(SIGAConstants.ACCESS_READ) && !accessEnvio.equals(SIGAConstants.ACCESS_FULL)) {
				//Miramos si tienen acceso a las comunicaciones de designaiones o ejgs, en tal caso se les permite enviar
				request.setAttribute("PERMISOENVIOS",ClsConstants.DB_FALSE);

			}
			//hacemos lo siguiente para setear el permiso de esta accion
			testAccess(request.getContextPath()+mapping.getPath()+".do",null,request);			
			

		} catch (Exception e) {
			this.throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
		}

		request.setAttribute("comunicacionesEntrada", comunicacionesEntrada);     
		request.setAttribute("comunicacionesSalida", comunicacionesSalida);
		
		return forward;
	}
	
	
}