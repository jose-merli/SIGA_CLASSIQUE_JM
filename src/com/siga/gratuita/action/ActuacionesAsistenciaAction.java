package com.siga.gratuita.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.AjaxCollectionXmlBuilder;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.ScsAsistenciasAdm;
import com.siga.beans.ScsAsistenciasBean;
import com.siga.comun.vos.ValueKeyVO;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.ActuacionAsistenciaForm;
import com.siga.gratuita.form.AsistenciaForm;
import com.siga.gratuita.service.AsistenciasService;
import com.siga.ws.CajgConfiguracion;

import es.satec.businessManager.BusinessManager;

/**
 * @author carlos.vidal
 * @since 3/2/2005
 * @version 06/04/2006 (david.sanchezp): modificaciones para incluir los combos de tipo de actuacion y coste fijo.
 * @version 26/09/2011 (jtacosta): Unifico Actaciones de censo con las de sjcs. 
 * 
 */

public class ActuacionesAsistenciaAction extends MasterAction {
	
	
	protected ActionForward executeInternal (ActionMapping mapping,
			ActionForm formulario,
			HttpServletRequest request, 
			HttpServletResponse response)throws SIGAException {

		String mapDestino = "exception";
		ActuacionAsistenciaForm miForm = null;

		try {
			miForm = (ActuacionAsistenciaForm) formulario;
			if (miForm == null) {
				return mapping.findForward(mapDestino);
			}

			String accion = miForm.getModo();

			if (accion.equalsIgnoreCase("consultarAsistencia")){
				mapDestino = consultarAsistencia(mapping, miForm, request, response);
			}else if ( accion.equalsIgnoreCase("getAjaxTipoCosteFijoActuacion")){
				getAjaxTipoCosteFijoActuacion (mapping, miForm, request, response);
				return null;
			}else if ( accion.equalsIgnoreCase("nuevoDesdeVolanteExpress")){
				mapDestino = nuevo (mapping, miForm, request, response);
				
			}
			else {
				return super.executeInternal(mapping,
						formulario,
						request, 
						response);
			}

//			Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) 
			{ 
//				mapDestino = "exception";
				if (miForm.getModal().equalsIgnoreCase("TRUE"))
				{
					request.setAttribute("exceptionTarget", "parent.modal");
				}

//				throw new ClsExceptions("El ActionMapping no puede ser nulo");
				throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
			}

		}
		catch (SIGAException es) { 
			throw es; 
		} 
		catch (Exception e) { 
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.facturacion"}); // o el recurso del modulo que sea 
		} 
		return mapping.findForward(mapDestino);
	}	
	
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException
	{
		UsrBean usrBean = this.getUserBean(request);	
		ActuacionAsistenciaForm actuacionAsistenciaForm 	= (ActuacionAsistenciaForm)formulario;
		  
		
		try {

			
			//Datos de la pestanha:
			String modoPestanha = request.getParameter("MODO");
			String anio 	= null;
			String numero 	= null;
			//Modo de la pestanha: (inicialmente en la URL de la pestanha, luego en el formulario)
			if (modoPestanha!=null){
				anio 	= request.getParameter("ANIO");
				numero 	= request.getParameter("NUMERO");
				actuacionAsistenciaForm.setModoPestanha(modoPestanha);
				
			}else{
				anio 	= actuacionAsistenciaForm.getAnio();
				numero 	= actuacionAsistenciaForm.getNumero();
				
				
			}
			
			String esFichaColegial = request.getParameter("esFichaColegial");
			if (esFichaColegial!=null && !esFichaColegial.equals("null")) { 
				if(Boolean.parseBoolean(esFichaColegial))
					actuacionAsistenciaForm.setFichaColegial("1");
				else
					actuacionAsistenciaForm.setFichaColegial("0");
			}else{
				actuacionAsistenciaForm.setFichaColegial("0");
				
			}			
			
			AsistenciaForm asistenciaForm = new AsistenciaForm();
			asistenciaForm.setIdInstitucion(usrBean.getLocation());
			asistenciaForm.setAnio(anio);
			asistenciaForm.setNumero(numero);
						
			
			BusinessManager bm = getBusinessManager();
			AsistenciasService asistenciasService = (AsistenciasService)bm.getService(AsistenciasService.class);
			asistenciaForm = asistenciasService.getDatosAsistencia(asistenciaForm, usrBean);
			asistenciaForm.setFichaColegial(actuacionAsistenciaForm.getFichaColegial());
			asistenciaForm.setModoPestanha(actuacionAsistenciaForm.getModoPestanha());
			request.setAttribute("asistencia",asistenciaForm);
			List <ActuacionAsistenciaForm>  actuacionesAsistenciaList = asistenciasService.getActuacionesAsistencia(asistenciaForm, usrBean);
			if(actuacionAsistenciaForm.getModoPestanha()!=null){
				for (ActuacionAsistenciaForm actuacionAsistenciaForm2 : actuacionesAsistenciaList) {
					actuacionAsistenciaForm2.setModoPestanha(actuacionAsistenciaForm.getModoPestanha());
					
				}
				
			}
			request.setAttribute("actuacionesAsistenciaList",actuacionesAsistenciaList);
			actuacionAsistenciaForm.setTiposActuacion(asistenciasService.getTiposActuacion(asistenciaForm, usrBean));
			actuacionAsistenciaForm.setTipoCosteFijoActuaciones(new ArrayList<ValueKeyVO>());
			actuacionAsistenciaForm.setPrisiones(asistenciasService.getPrisiones(asistenciaForm, usrBean));
			
		
			request.setAttribute("error", "");
		}
		catch (Exception e) 
		{
			request.setAttribute("actuacionesAsistenciaList", new ArrayList<ActuacionAsistenciaForm>());
			request.setAttribute("error", UtilidadesString.getMensajeIdioma(usrBean,"messages.general.errorExcepcion"));
			throwExcp("error.messages.editar",e,null); 
		} 
		return "listadoActuacionesAsistencia";
	}
	
	protected void getAjaxTipoCosteFijoActuacion (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception
			{
		ActuacionAsistenciaForm actuacionAsistenciaForm 	= (ActuacionAsistenciaForm)formulario;
		UsrBean usrBean = this.getUserBean(request);
		//Recogemos el parametro enviado por ajax
		String idTipoActuacion = request.getParameter("idTipoActuacion");
		String idTipoAsistencia = request.getParameter("idTipoAsistencia");
		String idInstitucion = request.getParameter("idInstitucion");
		actuacionAsistenciaForm.setIdTipoAsistencia(idTipoAsistencia);
		actuacionAsistenciaForm.setIdTipoActuacion(idTipoActuacion);
		actuacionAsistenciaForm.setIdInstitucion(idInstitucion);
		
		//Sacamos las guardias si hay algo selccionado en el turno
		List<ValueKeyVO> tipoCosteList = null;
		if(idTipoActuacion!= null && !idTipoActuacion.equals("-1")&& !idTipoActuacion.equals("")){
			BusinessManager bm = getBusinessManager();
			AsistenciasService asistenciasService = (AsistenciasService)bm.getService(AsistenciasService.class);
			tipoCosteList = asistenciasService.getTipoCosteFijoActuaciones(actuacionAsistenciaForm,idTipoActuacion, usrBean);
		}
		
		respuestaAjax(new AjaxCollectionXmlBuilder<ValueKeyVO>(), tipoCosteList,response);
		
	}
    
	
	
	/** 
	 *  Funcion que atiende la accion editar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String editar(ActionMapping mapping, 
							MasterForm formulario,
							HttpServletRequest request, 
							HttpServletResponse response)throws ClsExceptions,SIGAException  {

		String forward="exception";
		try {
		
			UsrBean usrBean = this.getUserBean(request);
			ActuacionAsistenciaForm actuacionAsistenciaForm 	= (ActuacionAsistenciaForm)formulario;
			
			AsistenciaForm asistenciaForm = new AsistenciaForm();
			asistenciaForm.setIdInstitucion(actuacionAsistenciaForm.getIdInstitucion());
			asistenciaForm.setAnio(actuacionAsistenciaForm.getAnio());
			asistenciaForm.setNumero(actuacionAsistenciaForm.getNumero());
			
			
			BusinessManager bm = getBusinessManager();
			AsistenciasService asistenciasService = (AsistenciasService)bm.getService(AsistenciasService.class);
			asistenciaForm = asistenciasService.getDatosAsistencia(asistenciaForm, usrBean);
			asistenciaForm.setFichaColegial(actuacionAsistenciaForm.getFichaColegial());
			asistenciaForm.setModoPestanha(actuacionAsistenciaForm.getModoPestanha());
			request.setAttribute("asistencia",asistenciaForm);
			
			ActuacionAsistenciaForm actuacionAsistenciaFormEdicion = asistenciasService.getActuacionAsistencia(
					actuacionAsistenciaForm, usrBean);
			actuacionAsistenciaFormEdicion.setNumeroProcedimientoAsistencia(asistenciaForm.getNumeroDiligencia());
			actuacionAsistenciaFormEdicion.setNumeroDiligenciaAsistencia(asistenciaForm.getNumeroDiligencia());
			actuacionAsistenciaFormEdicion.setComisariaAsistencia(asistenciaForm.getComisaria());
			actuacionAsistenciaFormEdicion.setJuzgadoAsistencia(asistenciaForm.getJuzgado());
			
			String idPrision = actuacionAsistenciaFormEdicion.getIdPrision();
			String idInstitucionPrision = actuacionAsistenciaFormEdicion.getIdInstitucionPris();
			if(idPrision!=null &&!idPrision.equals("")){
				String codigoPrision = idInstitucionPrision+","+idPrision;
				actuacionAsistenciaFormEdicion.setIdPrision(codigoPrision);
			}
			
			String idComisaria = actuacionAsistenciaFormEdicion.getIdComisaria();
			if(idComisaria==null ||idComisaria.equals(""))
				idComisaria = asistenciaForm.getComisaria();
			
			if(idComisaria==null ||idComisaria.equals(""))
				actuacionAsistenciaForm.setComisarias(asistenciasService.getComisarias(asistenciaForm, usrBean));
			else
				actuacionAsistenciaForm.setComisarias(asistenciasService.getComisarias(asistenciaForm,idComisaria, usrBean));
			
			
			String idJuzgado = actuacionAsistenciaFormEdicion.getIdJuzgado();
			if(idJuzgado==null ||idJuzgado.equals(""))
				idJuzgado = asistenciaForm.getJuzgado();
			
			if(idJuzgado==null ||idJuzgado.equals(""))
				actuacionAsistenciaForm.setJuzgados(asistenciasService.getJuzgados(asistenciaForm, usrBean));
			else
				actuacionAsistenciaForm.setJuzgados(asistenciasService.getJuzgados(asistenciaForm,idJuzgado, usrBean));
			
		
			actuacionAsistenciaFormEdicion.setModo("modificar");
			request.setAttribute("ActuacionAsistenciaFormEdicion", actuacionAsistenciaFormEdicion);
			int valorPcajgActivo=CajgConfiguracion.getTipoCAJG(new Integer(usrBean.getLocation()));
			request.setAttribute("tipoPcajg", new Integer(valorPcajgActivo));
			request.setAttribute("botones", "R,Y,C");
			forward=  "edicion";
		} catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}
		return forward;
		
	}

	/** 
	 *  Funcion que atiende la accion ver
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions,SIGAException  {
		
		String forward="exception";
		try {
			
		
			UsrBean usrBean = this.getUserBean(request);
			ActuacionAsistenciaForm actuacionAsistenciaForm 	= (ActuacionAsistenciaForm)formulario;
			
			AsistenciaForm asistenciaForm = new AsistenciaForm();
			asistenciaForm.setIdInstitucion(actuacionAsistenciaForm.getIdInstitucion());
			asistenciaForm.setAnio(actuacionAsistenciaForm.getAnio());
			asistenciaForm.setNumero(actuacionAsistenciaForm.getNumero());
			asistenciaForm.setModoPestanha(actuacionAsistenciaForm.getModoPestanha());
			
			BusinessManager bm = getBusinessManager();
			AsistenciasService asistenciasService = (AsistenciasService)bm.getService(AsistenciasService.class);
			asistenciaForm = asistenciasService.getDatosAsistencia(asistenciaForm, usrBean);
			asistenciaForm.setFichaColegial(actuacionAsistenciaForm.getFichaColegial());
			request.setAttribute("asistencia",asistenciaForm);
			
			ActuacionAsistenciaForm actuacionAsistenciaFormEdicion = asistenciasService.getActuacionAsistencia(
					actuacionAsistenciaForm, usrBean);
			
			
			String idPrision = actuacionAsistenciaFormEdicion.getIdPrision();
			String idInstitucionPrision = actuacionAsistenciaFormEdicion.getIdInstitucionPris();
			if(idPrision!=null &&!idPrision.equals("")){
				String codigoPrision = idInstitucionPrision+","+idPrision;
				actuacionAsistenciaFormEdicion.setIdPrision(codigoPrision);
			}
			String idComisaria = actuacionAsistenciaFormEdicion.getIdComisaria();
			if(idComisaria==null ||idComisaria.equals(""))
				idComisaria = asistenciaForm.getComisaria();
			
			if(idComisaria==null ||idComisaria.equals(""))
				actuacionAsistenciaForm.setComisarias(asistenciasService.getComisarias(asistenciaForm, usrBean));
			else
				actuacionAsistenciaForm.setComisarias(asistenciasService.getComisarias(asistenciaForm,idComisaria, usrBean));
			
			
			String idJuzgado = actuacionAsistenciaFormEdicion.getIdJuzgado();
			if(idJuzgado==null ||idJuzgado.equals(""))
				idJuzgado = asistenciaForm.getJuzgado();
			
			if(idJuzgado==null ||idJuzgado.equals(""))
				actuacionAsistenciaForm.setJuzgados(asistenciasService.getJuzgados(asistenciaForm, usrBean));
			else
				actuacionAsistenciaForm.setJuzgados(asistenciasService.getJuzgados(asistenciaForm,idJuzgado, usrBean));
			actuacionAsistenciaForm.setModo("ver");
			request.setAttribute("ActuacionAsistenciaFormEdicion", actuacionAsistenciaFormEdicion);
			int valorPcajgActivo=CajgConfiguracion.getTipoCAJG(new Integer(usrBean.getLocation()));
			request.setAttribute("tipoPcajg", new Integer(valorPcajgActivo));			
			request.setAttribute("botones", "C");
			forward=  "edicion";
		} catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}
		return forward;
	}

	/** 
	 *  Funcion que atiende la accion nuevo
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario,
							HttpServletRequest request, HttpServletResponse response)
							throws ClsExceptions,SIGAException  {
		
		String forward="exception";
		try{
			UsrBean usrBean = this.getUserBean(request);
			ActuacionAsistenciaForm actuacionAsistenciaForm 	= (ActuacionAsistenciaForm)formulario;
			
			AsistenciaForm asistenciaForm = new AsistenciaForm();
			asistenciaForm.setIdInstitucion(actuacionAsistenciaForm.getIdInstitucion());
			asistenciaForm.setAnio(actuacionAsistenciaForm.getAnio());
			asistenciaForm.setNumero(actuacionAsistenciaForm.getNumero());
			asistenciaForm.setModoPestanha(actuacionAsistenciaForm.getModoPestanha());
			
			BusinessManager bm = getBusinessManager();
			AsistenciasService asistenciasService = (AsistenciasService)bm.getService(AsistenciasService.class);
			asistenciaForm = asistenciasService.getDatosAsistencia(asistenciaForm, usrBean);
			asistenciaForm.setFichaColegial(actuacionAsistenciaForm.getFichaColegial());
			request.setAttribute("asistencia",asistenciaForm);
			
			ActuacionAsistenciaForm actuacionAsistenciaFormEdicion = new ActuacionAsistenciaForm();
			
			actuacionAsistenciaFormEdicion.setIdActuacion(asistenciasService.getNuevaActuacionAsistencia(asistenciaForm, usrBean).toString());
			
			actuacionAsistenciaFormEdicion.setIdTipoAsistencia(asistenciaForm.getIdTipoAsistenciaColegio());
			actuacionAsistenciaFormEdicion.setFacturado("0");
			actuacionAsistenciaFormEdicion.setValidada("0");
			if(asistenciaForm.getJuzgado()!=null&&!asistenciaForm.getJuzgado().equals("")&&(asistenciaForm.getComisaria()==null||asistenciaForm.getComisaria().equals(""))){
				actuacionAsistenciaFormEdicion.setIdJuzgado(asistenciaForm.getJuzgado());
				actuacionAsistenciaFormEdicion.setIdInstitucionJuzg(asistenciaForm.getIdInstitucion());
				actuacionAsistenciaFormEdicion.setNumeroAsunto(asistenciaForm.getNumeroProcedimiento());
			}
			if(asistenciaForm.getComisaria()!=null&&!asistenciaForm.getComisaria().equals("")&&(asistenciaForm.getJuzgado()==null||asistenciaForm.getJuzgado().equals(""))){
				actuacionAsistenciaFormEdicion.setIdComisaria(asistenciaForm.getComisaria());
				actuacionAsistenciaFormEdicion.setIdInstitucionComis(asistenciaForm.getIdInstitucion());
				actuacionAsistenciaFormEdicion.setNumeroAsunto(asistenciaForm.getNumeroDiligencia());
			}
			//No hay campo en el formulario y no admite nulos. Por defecto 0
			actuacionAsistenciaFormEdicion.setAcuerdoExtrajudicial("0");
			actuacionAsistenciaFormEdicion.setIdInstitucion(asistenciaForm.getIdInstitucion());
			actuacionAsistenciaFormEdicion.setAnio(asistenciaForm.getAnio());
			actuacionAsistenciaFormEdicion.setNumero(asistenciaForm.getNumero());
			actuacionAsistenciaFormEdicion.setNumeroProcedimientoAsistencia(asistenciaForm.getNumeroProcedimiento());
			actuacionAsistenciaFormEdicion.setNumeroDiligenciaAsistencia(asistenciaForm.getNumeroDiligencia());
			actuacionAsistenciaFormEdicion.setComisariaAsistencia(asistenciaForm.getComisaria());
			actuacionAsistenciaFormEdicion.setJuzgadoAsistencia(asistenciaForm.getJuzgado());
			
			if(actuacionAsistenciaForm.getModo().equals("nuevoDesdeVolanteExpress")){
				actuacionAsistenciaForm.setTiposActuacion(asistenciasService.getTiposActuacion(asistenciaForm, usrBean));
				actuacionAsistenciaForm.setTipoCosteFijoActuaciones(new ArrayList<ValueKeyVO>());
				actuacionAsistenciaForm.setPrisiones(asistenciasService.getPrisiones(asistenciaForm, usrBean));
				
				
			}
			
			actuacionAsistenciaForm.setComisarias(asistenciasService.getComisarias(asistenciaForm, usrBean));
			actuacionAsistenciaForm.setJuzgados(asistenciasService.getJuzgados(asistenciaForm, usrBean));
			
			actuacionAsistenciaForm.setModo("abrir");
			actuacionAsistenciaFormEdicion.setModo("insertar");
			request.setAttribute("ActuacionAsistenciaFormEdicion", actuacionAsistenciaFormEdicion);
			int valorPcajgActivo=CajgConfiguracion.getTipoCAJG(new Integer(usrBean.getLocation()));
			request.setAttribute("tipoPcajg", new Integer(valorPcajgActivo));			
			request.setAttribute("botones", "R,Y,C"); 
			forward=  "edicion";
		} catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}
		return forward;
	}

	/** 
	 *  Funcion que atiende la accion insertar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected synchronized String insertar(	ActionMapping mapping, MasterForm formulario,
								HttpServletRequest request, HttpServletResponse response)
								throws ClsExceptions,SIGAException  {
		
		
		
		
		
		UsrBean usrBean = this.getUserBean(request);
		ActuacionAsistenciaForm actuacionAsistenciaFormEdicion 	= (ActuacionAsistenciaForm)formulario;

		
		String forward="exception";
		try {
			
			BusinessManager bm = getBusinessManager();
			AsistenciasService asistenciasService = (AsistenciasService)bm.getService(AsistenciasService.class);
			String codigoPrision = actuacionAsistenciaFormEdicion.getIdPrision();
			if(codigoPrision!=null && !codigoPrision.equals("")){
				String[] prision = codigoPrision.split(",");
				String idPrision = prision[1];
				String idInstitucionPrision = prision[0];
				actuacionAsistenciaFormEdicion.setIdPrision(idPrision);
				actuacionAsistenciaFormEdicion.setIdInstitucionPris(idInstitucionPrision);
			}
			//No hay campo en el formulario y no admite nulos. Por defecto 0
			actuacionAsistenciaFormEdicion.setAcuerdoExtrajudicial("0");
			
			asistenciasService.insertarActuacionAsistencia(actuacionAsistenciaFormEdicion, usrBean);
			
			actuacionAsistenciaFormEdicion.setModo("abrir");
			forward = exitoModal("messages.inserted.success",request);
		} catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}
		
 
		return forward;
		
		
		
	}

	
	protected String modificar(ActionMapping mapping, 
			MasterForm formulario,
			HttpServletRequest request, 
			HttpServletResponse response)throws ClsExceptions,SIGAException  {

		UsrBean usrBean = this.getUserBean(request);
		ActuacionAsistenciaForm actuacionAsistenciaFormEdicion 	= (ActuacionAsistenciaForm)formulario;

		
		String forward="exception";
		try {
			
			BusinessManager bm = getBusinessManager();
			AsistenciasService asistenciasService = (AsistenciasService)bm.getService(AsistenciasService.class);
			String codigoPrision = actuacionAsistenciaFormEdicion.getIdPrision();
			if(codigoPrision!=null && !codigoPrision.equals("")){
				String[] prision = codigoPrision.split(",");
				String idPrision = prision[1];
				String idInstitucionPrision = prision[0];
				actuacionAsistenciaFormEdicion.setIdPrision(idPrision);
				actuacionAsistenciaFormEdicion.setIdInstitucionPris(idInstitucionPrision);
			}
			//No hay campo en el formulario y no admite nulos. Por defecto 0
			actuacionAsistenciaFormEdicion.setAcuerdoExtrajudicial("0");
			
			asistenciasService.modificarActuacionAsistencia(actuacionAsistenciaFormEdicion, usrBean);
			actuacionAsistenciaFormEdicion.setModo("abrir");
			forward = exitoModal("messages.updated.success",request);
		} catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}
		return forward;
	}
	
	/** 
	 *  Funcion que atiende la accion borrar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario,
							HttpServletRequest request, HttpServletResponse response)
							throws ClsExceptions,SIGAException  {
		
		
		UsrBean usrBean = this.getUserBean(request);
		ActuacionAsistenciaForm actuacionAsistenciaFormEdicion 	= (ActuacionAsistenciaForm)formulario;

		
		String forward="exception";
		try {
			
			BusinessManager bm = getBusinessManager();
			AsistenciasService asistenciasService = (AsistenciasService)bm.getService(AsistenciasService.class);
			asistenciasService.borrarActuacionAsistencia(actuacionAsistenciaFormEdicion, usrBean);
			forward = exitoRefresco("messages.deleted.success",request);
		} catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}
		
		return forward;
	}
	protected String consultarAsistencia(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException
			{
		ActuacionAsistenciaForm miForm =(ActuacionAsistenciaForm)formulario;
		miForm.setModo("abrir");
		ScsAsistenciasAdm admAsistencias = new ScsAsistenciasAdm(this.getUserBean(request));
		ScsAsistenciasBean beanAsistencia = admAsistencias.getAsistenciaVolanteExpress(miForm.getAnio(),miForm.getNumero(),miForm.getIdInstitucion());
		request.setAttribute("asistencia", beanAsistencia);
		
		return "consultarAsistencia";
		
	}
	

}