/*
 * VERSIONES:
 *	
 */

package com.siga.informes.action;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.AjaxCollectionXmlBuilder;
import com.siga.beans.AdmInformeAdm;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.CenInstitucionBean;
import com.siga.beans.FcsFacturacionJGAdm;
import com.siga.beans.FcsFacturacionJGBean;
import com.siga.beans.FcsPagosJGAdm;
import com.siga.beans.FcsPagosJGBean;
import com.siga.beans.ScsGrupoFacturacionAdm;
import com.siga.beans.ScsGrupoFacturacionBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.informes.InformeFacturasEmitidas;



public class InformesFacturacionMultipleAction extends MasterAction 
{
	/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAExceptions  En cualquier caso de error
	 */
	
	protected ActionForward executeInternal (ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response)throws SIGAException 
	{
		String mapDestino = "exception";
		MasterForm miForm = null;

		try {
			miForm = (MasterForm) formulario;
			if (miForm == null) {
				return mapping.findForward(mapDestino);
			}
				
			String accion = miForm.getModo();
				
			// La primera vez que se carga el formulario 
			// Abrir
			if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
				
				String idTipoInforme = mapping.getParameter();
				if(idTipoInforme!=null &&!idTipoInforme.equals("")){
					String informeUnico = ClsConstants.DB_TRUE;
					AdmInformeAdm adm = new AdmInformeAdm(this.getUserBean(request));
					Vector informeBeans=adm.obtenerInformesTipo(this.getUserBean(request).getLocation(),idTipoInforme,null, null);
					if(informeBeans!=null && informeBeans.size()>1){
						informeUnico = ClsConstants.DB_FALSE;
						
					}
	
					request.setAttribute("informeUnico", informeUnico);
				}
				mapDestino = abrir(mapping, miForm, request, response);
				
			} else if (accion.equalsIgnoreCase("generarInforme")){
				mapDestino = generarInforme(mapping, miForm, request, response);
				
			} else if (accion.equalsIgnoreCase("ajaxObtenerInstituciones")){
				ajaxObtenerInstituciones(mapping, miForm, request, response);
				return null;				
				
			} else if (accion.equalsIgnoreCase("ajaxObtenerTurnos")){
				ajaxObtenerTurnos(mapping, miForm, request, response);
				return null;
				
			} else if (accion.equalsIgnoreCase("ajaxObtenerFacturas")){
				ajaxObtenerFacturas(mapping, miForm, request, response);
				return null;		
				
			} else if (accion.equalsIgnoreCase("ajaxObtenerPagos")){
				ajaxObtenerPagos(mapping, miForm, request, response);
				return null;					
				
			} else {
				return super.executeInternal(mapping, formulario, request, response);
			}

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) 
			{ 
				//mapDestino = "exception";
			    if (miForm.getModal().equalsIgnoreCase("TRUE"))
			    {
			        request.setAttribute("exceptionTarget", "parent.modal");
			    }
			    
			    //throw new ClsExceptions("El ActionMapping no puede ser nulo");
			    throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
			}
		}
		catch (SIGAException es) { 
			throw es; 
		} 
		catch (Exception e) { 
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.censo"}); // o el recurso del modulo que sea 
		} 
		return mapping.findForward(mapDestino);
	}

	/**
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	protected String generarInformeOld(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
	    String salida = "";
		try {
			File fichero = null;

			{	// FOP
				InformeFacturasEmitidas informe = new InformeFacturasEmitidas (this.getUserBean(request));
				fichero = informe.generarListadoFacturasEmitidasOld(request, formulario.getDatos());
			}
			
			if(fichero!= null){
				request.setAttribute("nombreFichero", fichero.getName());
				request.setAttribute("rutaFichero", fichero.getAbsolutePath());			
				request.setAttribute("borrarFichero", "false");			
				request.setAttribute("generacionOK","OK");
				salida= "descarga";
			}
			else{
				return exitoModalSinRefresco("facturacion.informes.facturasEmitidas.generarInforme.error", request);
			}
		}
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}				
		return salida;
	}
	protected String generarInforme(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
	    String salida = "";
		try {
			File fichero = null;

			{	// FOP
				InformeFacturasEmitidas informe = new InformeFacturasEmitidas (this.getUserBean(request));
				fichero = informe.generarListadoFacturasEmitidasPdfFromXmlToFoXsl(request, formulario.getDatos());
				
			}
			
			if(fichero!= null){
				request.setAttribute("nombreFichero", fichero.getName());
				request.setAttribute("rutaFichero", fichero.getAbsolutePath());			
				request.setAttribute("borrarFichero", "false");			
				request.setAttribute("generacionOK","OK");
				salida = "descargaFichero";
			}
			else{
				throw new SIGAException("facturacion.informes.facturasEmitidas.generarInforme.error");
			}
			if(fichero!= null){
				request.setAttribute("nombreFichero", fichero.getName());
				request.setAttribute("rutaFichero", fichero.getAbsolutePath());			
				request.setAttribute("borrarFichero", "false");			
				request.setAttribute("generacionOK","OK");
				salida= "descarga";
			}
			else{
				return exitoModalSinRefresco("facturacion.informes.facturasEmitidas.generarInforme.error", request);
			}
		}
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}				
		return salida;
	}
	
	/**
	 * 
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @throws ClsExceptions
	 * @throws SIGAException
	 * @throws Exception
	 */
	protected void ajaxObtenerInstituciones (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception {
		UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
		List<CenInstitucionBean> aInstituciones = null;
		
		//Recogemos el parametro enviado por ajax
		String idInstitucion = request.getParameter("idInstitucion");	
		int intInstitucion = Integer.parseInt(idInstitucion);
		
		//Compruebo si estan indicados los datos minimos
		if (intInstitucion == ClsConstants.INSTITUCION_CONSEJOGENERAL || intInstitucion >= ClsConstants.INSTITUCION_CONSEJO) { 		
			CenInstitucionAdm admInstituciones = new CenInstitucionAdm(user);
			aInstituciones = admInstituciones.getInstitucionesConsejo(idInstitucion);		
		} else {
			aInstituciones = new ArrayList<CenInstitucionBean>();
		}
	    respuestaAjax(new AjaxCollectionXmlBuilder<CenInstitucionBean>(), aInstituciones, response);		
	}	
	
	/**
	 * 
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @throws ClsExceptions
	 * @throws SIGAException
	 * @throws Exception
	 */
	protected void ajaxObtenerTurnos (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception {
		UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
		List<ScsGrupoFacturacionBean> alTurnos = null;
		
		//Recogemos el parametro enviado por ajax
		String idInstitucion = request.getParameter("idInstitucion");		
		
		//Compruebo si estan indicados los datos minimos
		if (idInstitucion==null || idInstitucion.equalsIgnoreCase("-1")) {
			alTurnos = new ArrayList<ScsGrupoFacturacionBean>();
			
		} else {
			//Sacamos los turnos
			ScsGrupoFacturacionAdm admTurnos = new ScsGrupoFacturacionAdm(user);
			alTurnos = admTurnos.getTurnosInformes(idInstitucion);
		}
	    respuestaAjax(new AjaxCollectionXmlBuilder<ScsGrupoFacturacionBean>(), alTurnos, response);
	}	
	
	/**
	 * 
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @throws ClsExceptions
	 * @throws SIGAException
	 * @throws Exception
	 */
	protected void ajaxObtenerFacturas (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception {		
		List<FcsFacturacionJGBean> aFacturas = null;
		
		//Recogemos los parametros enviados por ajax
		String idInstitucion = request.getParameter("idInstitucion");		
		String idGrupo = request.getParameter("idGrupo");
		
		//Obtengo datos del usuario
		UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
		String strInstitucion = user.getLocation();			
		int intInstitucion = Integer.parseInt(strInstitucion);
		
		//Compruebo si estan indicados los datos minimos
		if (idInstitucion==null || idGrupo == null || idInstitucion.equalsIgnoreCase("-1") || idGrupo.equalsIgnoreCase("")) {
			aFacturas = new ArrayList<FcsFacturacionJGBean>();
			
		} else {

			FcsFacturacionJGAdm admFacturaciones = new FcsFacturacionJGAdm(user);
			
			//Compruebo si el usuario entra desde un consejo
			//Solo muestra facturas cerradas
			if (intInstitucion == ClsConstants.INSTITUCION_CONSEJOGENERAL || intInstitucion >= ClsConstants.INSTITUCION_CONSEJO) 
				aFacturas = admFacturaciones.getFacturacionesInformes(idInstitucion, idGrupo, "30");
			
			else 
				aFacturas = admFacturaciones.getFacturacionesInformes(idInstitucion, idGrupo, "20,30");
		}
	    respuestaAjax(new AjaxCollectionXmlBuilder<FcsFacturacionJGBean>(), aFacturas, response);
	}
	
	/**
	 * 
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @throws ClsExceptions
	 * @throws SIGAException
	 * @throws Exception
	 */
	protected void ajaxObtenerPagos (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception {		
		List<FcsPagosJGBean> aPagos = null;
		
		//Recogemos los parametros enviados por ajax
		String idInstitucion = request.getParameter("idInstitucion");		
		String idGrupo = request.getParameter("idGrupo");
		
		//Obtengo datos del usuario
		UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
		
		//Compruebo si estan indicados los datos minimos
		if (idInstitucion==null || idGrupo == null || idInstitucion.equalsIgnoreCase("-1") || idGrupo.equalsIgnoreCase("")) {
			aPagos = new ArrayList<FcsPagosJGBean>();
			
		} else {
			FcsPagosJGAdm admPagos = new FcsPagosJGAdm(user);
			aPagos = admPagos.getPagosInformes(idInstitucion, idGrupo, ClsConstants.ESTADO_PAGO_EJECUTADO);
		}
	    respuestaAjax(new AjaxCollectionXmlBuilder<FcsPagosJGBean>(), aPagos, response);
	}		
}