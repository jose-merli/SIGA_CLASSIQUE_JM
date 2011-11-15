package com.siga.administracion.action;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.administracion.form.InformeForm;
import com.siga.administracion.service.InformesService;
import com.siga.beans.AdmConsultaInformeBean;
import com.siga.beans.AdmInformeBean;
import com.siga.beans.AdmTipoInformeBean;
import com.siga.beans.CenInstitucionBean;
import com.siga.beans.FileInforme;
import com.siga.beans.GenParametrosAdm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

import es.satec.businessManager.BusinessManager;


/**
 * @author jtacosta
 */

public class GestionInformesAction extends MasterAction {
	protected ActionForward executeInternal(ActionMapping mapping,ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		String mapDestino = "exception";
		MasterForm miForm = null;
		try { 
			
			
			do {
				miForm = (MasterForm) formulario;
				if (miForm != null) {
					String accion = miForm.getModo();
					String modo = request.getParameter("modo");
					if(modo!=null)
						accion = modo;
					if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
						mapDestino = inicio (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("buscar")){
						mapDestino = getAjaxBusqueda (mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("nuevo")){
						mapDestino = nuevo(mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("nuevoInformeConsulta")){
						mapDestino = nuevoInformeConsulta(mapping, miForm, request, response);
					}
					
					else if ( accion.equalsIgnoreCase("borrarConsultaInforme")){
						mapDestino = borrarConsultaInforme(mapping, miForm, request, response);
					}
					else if ( accion.equalsIgnoreCase("insertar")){
						mapDestino = insertar(mapping, miForm, request, response);
					}
					else if ( accion.equalsIgnoreCase("duplicar")){
						mapDestino = duplicar(mapping, miForm, request, response);
					}
//					else if ( accion.equalsIgnoreCase("comprobarBorrar")){
//						mapDestino = comprobarBorrar(mapping, miForm, request, response);
//					}
					else if ( accion.equalsIgnoreCase("borrar")){
						mapDestino = borrar(mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("consultar")){
						mapDestino = consultar(mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("consultarInformeConsulta")){
						mapDestino = consultarInformeConsulta(mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("editar")){
						mapDestino = editar(mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("editarInformeConsulta")){
						mapDestino = editarInformeConsulta(mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("modificar")){
						mapDestino = modificar(mapping, miForm, request, response);
					}else if ( accion.equalsIgnoreCase("refrescar")){
						mapDestino = refrescar(mapping, miForm, request, response);
					}else if (accion.equalsIgnoreCase("download"))  {
		                mapDestino = download(mapping, miForm, request, response);
		                
		            }else if (accion.equalsIgnoreCase("comprobarBorrarInformeFile"))  {
		                mapDestino = comprobarBorrarInformeFile(mapping, miForm, request, response);
		            }else if (accion.equalsIgnoreCase("borrarInformeFile"))  {
		                mapDestino = borrarInformeFile(mapping, miForm, request, response);
		            } else if (accion.equalsIgnoreCase("upload"))  {
		                mapDestino = upload(mapping, miForm, request, response);
		            } else if (accion.equalsIgnoreCase("listadoArchivosInforme"))  {
		                mapDestino = listadoArchivosInforme(mapping, miForm, request, response);
		            }else if (accion.equalsIgnoreCase("nuevoListadoArchivosInforme"))  {
		                mapDestino = nuevoListadoArchivosInforme(mapping, miForm, request, response);
		            }    					
					
					else {
						return super.executeInternal(mapping,formulario,request,response);
					}
				}
			} while (false);
			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null)	{ 
				throw new ClsExceptions("El ActionMapping no puede ser nulo");
			}
			return mapping.findForward(mapDestino);
		} 
		catch (SIGAException es) {

			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		}
	}

	protected String inicio (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		InformeForm informeForm = (InformeForm) formulario;
		informeForm.clear();
		informeForm.setMsgAviso("");
		informeForm.setMsgError("");
		UsrBean usrBean = this.getUserBean(request);
		informeForm.setUsrBean(usrBean);
		return inicioGestion(mapping, formulario, request, response);
		
	}
	protected String refrescar (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException{
		
		return "exito";
	}
	
	protected String inicioGestion (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
			
		InformeForm informeForm = (InformeForm) formulario;

		UsrBean usrBean = this.getUserBean(request);
		informeForm.setIdInstitucion(usrBean.getLocation());
		BusinessManager bm = getBusinessManager();
		InformesService informeService = (InformesService)bm.getService(InformesService.class);
		List<AdmTipoInformeBean> tiposInformeList = informeService.getTiposInforme(usrBean);
		informeForm.setTiposInforme(tiposInformeList);
		List<CenInstitucionBean> institucionesList = informeService.getInstitucionesInformes(new Integer(usrBean.getLocation()),usrBean);
		informeForm.setInstituciones(institucionesList);
		informeForm.setLenguajes(informeService.getLenguajes(usrBean));
		
			GenParametrosAdm param = new GenParametrosAdm(usrBean);
			boolean isEnvioSmsConfigurado = UtilidadesString.stringToBoolean(param.getValor(usrBean.getLocation(), "ENV", "HABILITAR_SMS_BUROSMS", "N"));
			String comboTipoEnvio = "cmbTipoEnviosInst";
			if(isEnvioSmsConfigurado){
//				Descomentar esto cuando se quiera abrir las plantillas al resto de tipos de envios(11/11/11) y borrar comboTipoEnvio = "cmbTipoEnviosSoloSms"(tambien del combo.properties)
//				comboTipoEnvio = "cmbTipoEnviosInstSms";
				comboTipoEnvio = "cmbTipoEnviosSoloSms";
				
			}
			request.setAttribute("comboTipoEnvio", comboTipoEnvio);
			
			request.setAttribute("parametrosComboEnvios", new String[]{usrBean.getLocation()});
			request.setAttribute("idTipoEnvioSeleccionado", new ArrayList());
			
			request.setAttribute("idPlantillaEnvioSeleccionado",new ArrayList());
			
			request.setAttribute("idPlantillaGeneracionSeleccionado",new ArrayList());
			
			
		
		
		return "inicio";
	}
	
	protected String getAjaxBusqueda (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions,Exception 
			{
		InformeForm informeForm = (InformeForm) formulario;
		informeForm.setMsgAviso("");
		informeForm.setMsgError("");
		UsrBean usrBean = this.getUserBean(request);
		String forward = "listadoInformes";
		 
		try {
			BusinessManager bm = getBusinessManager();
			InformesService informeService = (InformesService)bm.getService(InformesService.class);
			List<InformeForm> listadoInformes = informeService.getInformes(informeForm,usrBean.getLocation(),usrBean);
			request.setAttribute("listadoInformes", listadoInformes);
			
			
			
		} catch (Exception e){
			// informeForm.setInformes(new ArrayList<InformeForm>());
			String error = UtilidadesString.getMensajeIdioma(usrBean,"messages.general.errorExcepcion");
			informeForm.setMsgError(error);
			
		}
		return forward;
	}
	
	protected String nuevoInformeConsulta (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		InformeForm informeForm = (InformeForm) formulario;
		// informeForm.clear();
		informeForm.setModo("insertar");
		UsrBean usrBean = this.getUserBean(request);
		informeForm.setIdInstitucion(usrBean.getLocation());
		BusinessManager bm = getBusinessManager();
		InformesService informeService = (InformesService)bm.getService(InformesService.class);
		List<AdmTipoInformeBean> tiposInformeList = informeService.getTiposInforme(usrBean);
		informeForm.setTiposInforme(tiposInformeList);
		List<CenInstitucionBean> institucionesList = informeService.getInstitucionesInformes(new Integer(usrBean.getLocation()),usrBean);
		informeForm.setInstituciones(institucionesList);
		informeForm.setLenguajes(informeService.getLenguajes(usrBean));
		
		
		
			GenParametrosAdm param = new GenParametrosAdm(usrBean);
			boolean isEnvioSmsConfigurado = UtilidadesString.stringToBoolean(param.getValor(usrBean.getLocation(), "ENV", "HABILITAR_SMS_BUROSMS", "N"));
			String comboTipoEnvio = "cmbTipoEnviosInst";
			if(isEnvioSmsConfigurado){
//				Descomentar esto cuando se quiera abrir las plantillas al resto de tipos de envios(11/11/11) y borrar comboTipoEnvio = "cmbTipoEnviosSoloSms"(tambien del combo.properties)
//				comboTipoEnvio = "cmbTipoEnviosInstSms";
				comboTipoEnvio = "cmbTipoEnviosSoloSms";
				
			}
			request.setAttribute("comboTipoEnvio", comboTipoEnvio);
			request.setAttribute("parametrosComboEnvios", new String[]{usrBean.getLocation()});
			request.setAttribute("idTipoEnvioSeleccionado", new ArrayList());
			
			request.setAttribute("idPlantillaEnvioSeleccionado",new ArrayList());
			
			request.setAttribute("idPlantillaGeneracionSeleccionado",new ArrayList());
		
		
		InformeForm informeFormEdicion = new InformeForm();
		informeFormEdicion.setUsrBean(this.getUserBean(request));
		informeFormEdicion.setIdInstitucion(informeFormEdicion.getUsrBean().getLocation());
		informeFormEdicion.setLenguajes(informeForm.getLenguajes());
		informeFormEdicion.setAlias(informeForm.getAlias());
		informeFormEdicion.setIdConsulta(informeForm.getIdConsulta());
		informeFormEdicion.setIdInstitucionConsulta(informeForm.getIdInstitucionConsulta());
		
		for(AdmTipoInformeBean tipoInformeConsulta:tiposInformeList){
			if(tipoInformeConsulta.getIdTipoInforme().equals(AdmTipoInformeBean.TIPOINFORME_CONSULTAS)){
				informeFormEdicion.setClaseTipoInforme(tipoInformeConsulta.getClase());
				informeFormEdicion.setIdTipoInforme(tipoInformeConsulta.getIdTipoInforme());
				informeFormEdicion.setDirectorio(tipoInformeConsulta.getDirectorio());
				break;
				
			}
			
		}
		
		
		request.setAttribute("InformeFormEdicion", informeFormEdicion);
		return "edicion";
	}
	protected String nuevo (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		InformeForm informeForm = (InformeForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		// informeForm.clear();
		informeForm.setModo("insertar");
		InformeForm informeFormEdicion = new InformeForm();
		informeFormEdicion.setUsrBean(this.getUserBean(request));
		informeFormEdicion.setIdInstitucion(informeFormEdicion.getUsrBean().getLocation());
		informeFormEdicion.setLenguajes(informeForm.getLenguajes());
		
		
			GenParametrosAdm param = new GenParametrosAdm(usrBean);
			boolean isEnvioSmsConfigurado = UtilidadesString.stringToBoolean(param.getValor(usrBean.getLocation(), "ENV", "HABILITAR_SMS_BUROSMS", "N"));
			String comboTipoEnvio = "cmbTipoEnviosInst";
			if(isEnvioSmsConfigurado){
//				Descomentar esto cuando se quiera abrir las plantillas al resto de tipos de envios(11/11/11) y borrar comboTipoEnvio = "cmbTipoEnviosSoloSms"(tambien del combo.properties)
//				comboTipoEnvio = "cmbTipoEnviosInstSms";
				comboTipoEnvio = "cmbTipoEnviosSoloSms";
				
			}
			request.setAttribute("comboTipoEnvio", comboTipoEnvio);
			request.setAttribute("parametrosComboEnvios", new String[]{usrBean.getLocation()});
			request.setAttribute("idTipoEnvioSeleccionado", new ArrayList());
			
			request.setAttribute("idPlantillaEnvioSeleccionado",new ArrayList());
			
			request.setAttribute("idPlantillaGeneracionSeleccionado",new ArrayList());
		
		
		request.setAttribute("InformeFormEdicion", informeFormEdicion);
		
		return "edicion";
	}
	protected String editar(ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		
		InformeForm informeForm = (InformeForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		try {
			//Si no estuviera todo en el formulario accederiamos a ello de la manera que comento
			BusinessManager bm = getBusinessManager();
			InformesService informeService = (InformesService)bm.getService(InformesService.class);
			AdmInformeBean informeBean =  informeService.getInforme(informeForm, usrBean);
			InformeForm informeFormEdicion = informeBean.getInforme();
			informeFormEdicion.setLenguajes(informeForm.getLenguajes());
			informeFormEdicion.setClaseTipoInforme(informeForm.getClaseTipoInforme());
			
			request.setAttribute("InformeFormEdicion", informeFormEdicion);
			
				GenParametrosAdm param = new GenParametrosAdm(usrBean);
				boolean isEnvioSmsConfigurado = UtilidadesString.stringToBoolean(param.getValor(usrBean.getLocation(), "ENV", "HABILITAR_SMS_BUROSMS", "N"));
				String comboTipoEnvio = "cmbTipoEnviosInst";
				if(isEnvioSmsConfigurado){
//					Descomentar esto cuando se quiera abrir las plantillas al resto de tipos de envios(11/11/11) y borrar comboTipoEnvio = "cmbTipoEnviosSoloSms"(tambien del combo.properties)
//					comboTipoEnvio = "cmbTipoEnviosInstSms";
					comboTipoEnvio = "cmbTipoEnviosSoloSms";
					
				}
				request.setAttribute("comboTipoEnvio", comboTipoEnvio);
				request.setAttribute("parametrosComboEnvios", new String[]{usrBean.getLocation()});
				List alIdsTipoEnvio = new ArrayList();
				if(informeFormEdicion.getIdTipoEnvio()!=null && !informeFormEdicion.getIdTipoEnvio().equals("")){
					
					alIdsTipoEnvio.add(informeFormEdicion.getIdInstitucion()+","+informeFormEdicion.getIdTipoEnvio());
				}
				request.setAttribute("idTipoEnvioSeleccionado",alIdsTipoEnvio);
				List alIdsPlantillaEnvio = new ArrayList();
				if(informeFormEdicion.getIdPlantillaEnvio()!=null && !informeFormEdicion.getIdPlantillaEnvio().equals("")){
					
					alIdsPlantillaEnvio.add(informeFormEdicion.getIdPlantillaEnvio()+","+informeFormEdicion.getIdInstitucion()+","+informeFormEdicion.getIdTipoEnvio());
				}
				request.setAttribute("idPlantillaEnvioSeleccionado",alIdsPlantillaEnvio);
				List alIdsPlantillaGeneracion = new ArrayList();
				if(informeFormEdicion.getIdPlantillaGeneracion()!=null && !informeFormEdicion.getIdPlantillaGeneracion().equals("")){
					
					alIdsPlantillaGeneracion.add(informeFormEdicion.getIdPlantillaGeneracion());
				}
				request.setAttribute("idPlantillaGeneracionSeleccionado",alIdsPlantillaEnvio);
				
				
				
				
				
			
			informeForm.setModo("modificar");
		}catch (Exception e){
			throwExcp("messages.general.errorExcepcion", e, null); 			
		}
		
		return "edicion";
	}
	protected String editarInformeConsulta(ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		
		InformeForm informeForm = (InformeForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		try {
			//Si no estuviera todo en el formulario accederiamos a ello de la manera que comento
			BusinessManager bm = getBusinessManager();
			InformesService informeService = (InformesService)bm.getService(InformesService.class);
			
			informeForm.setIdInstitucion(usrBean.getLocation());
			List<AdmTipoInformeBean> tiposInformeList = informeService.getTiposInforme(usrBean);
			informeForm.setTiposInforme(tiposInformeList);
			List<CenInstitucionBean> institucionesList = informeService.getInstitucionesInformes(new Integer(usrBean.getLocation()),usrBean);
			informeForm.setInstituciones(institucionesList);
			informeForm.setLenguajes(informeService.getLenguajes(usrBean));
			
			
			
			AdmInformeBean informeBean =  informeService.getInforme(informeForm, usrBean);
			InformeForm informeFormEdicion = informeBean.getInforme();
			informeFormEdicion.setLenguajes(informeForm.getLenguajes());
			informeFormEdicion.setClaseTipoInforme(informeForm.getClaseTipoInforme());
			
			request.setAttribute("InformeFormEdicion", informeFormEdicion);
			
				GenParametrosAdm param = new GenParametrosAdm(usrBean);
				boolean isEnvioSmsConfigurado = UtilidadesString.stringToBoolean(param.getValor(usrBean.getLocation(), "ENV", "HABILITAR_SMS_BUROSMS", "N"));
				String comboTipoEnvio = "cmbTipoEnviosInst";
				if(isEnvioSmsConfigurado){
//					Descomentar esto cuando se quiera abrir las plantillas al resto de tipos de envios(11/11/11) y borrar comboTipoEnvio = "cmbTipoEnviosSoloSms"(tambien del combo.properties)
//					comboTipoEnvio = "cmbTipoEnviosInstSms";
					comboTipoEnvio = "cmbTipoEnviosSoloSms";
					
				}
				request.setAttribute("comboTipoEnvio", comboTipoEnvio);
				request.setAttribute("parametrosComboEnvios", new String[]{usrBean.getLocation()});
				List alIdsTipoEnvio = new ArrayList();
				if(informeFormEdicion.getIdTipoEnvio()!=null && !informeFormEdicion.getIdTipoEnvio().equals(""))
					alIdsTipoEnvio.add(informeFormEdicion.getIdInstitucion()+","+informeFormEdicion.getIdTipoEnvio());
				request.setAttribute("idTipoEnvioSeleccionado",alIdsTipoEnvio);
				List alIdsPlantillaEnvio = new ArrayList();
				if(informeFormEdicion.getIdPlantillaEnvio()!=null && !informeFormEdicion.getIdPlantillaEnvio().equals("")){
					
					alIdsPlantillaEnvio.add(informeFormEdicion.getIdPlantillaEnvio()+","+informeFormEdicion.getIdInstitucion()+","+informeFormEdicion.getIdTipoEnvio());
				}
				request.setAttribute("idPlantillaEnvioSeleccionado",alIdsPlantillaEnvio);
				List alIdsPlantillaGeneracion = new ArrayList();
				if(informeFormEdicion.getIdPlantillaGeneracion()!=null && !informeFormEdicion.getIdPlantillaGeneracion().equals("")){
					
					alIdsPlantillaGeneracion.add(informeFormEdicion.getIdPlantillaGeneracion());
				}
				request.setAttribute("idPlantillaGeneracionSeleccionado",alIdsPlantillaEnvio);
			
			
			informeForm.setModo("modificar");
		}catch (Exception e){
			throwExcp("messages.general.errorExcepcion", e, null); 			
		}
		
		return "edicion";
	}
	protected String consultar(ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		
		InformeForm informeForm = (InformeForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		try {
			BusinessManager bm = getBusinessManager();
			InformesService informeService = (InformesService)bm.getService(InformesService.class);
			AdmInformeBean informeBean =  informeService.getInforme(informeForm, usrBean);
			InformeForm informeFormEdicion = informeBean.getInforme();
			informeFormEdicion.setLenguajes(informeForm.getLenguajes());
			informeFormEdicion.setClaseTipoInforme(informeForm.getClaseTipoInforme());
			
//			request.setAttribute("comboTipoEnvio", "cmbTipoEnviosInstSms");
			request.setAttribute("comboTipoEnvio", "cmbTipoEnviosSoloSms");
			request.setAttribute("parametrosComboEnvios", new String[]{informeForm.getIdInstitucion()});
			
			List alIdsTipoEnvio = new ArrayList();
			if(informeFormEdicion.getIdTipoEnvio()!=null && !informeFormEdicion.getIdTipoEnvio().equals(""))
				alIdsTipoEnvio.add(informeFormEdicion.getIdInstitucion()+","+informeFormEdicion.getIdTipoEnvio());
			request.setAttribute("idTipoEnvioSeleccionado",alIdsTipoEnvio);
			List alIdsPlantillaEnvio = new ArrayList();
			if(informeFormEdicion.getIdPlantillaEnvio()!=null && !informeFormEdicion.getIdPlantillaEnvio().equals("")){
				
				alIdsPlantillaEnvio.add(informeFormEdicion.getIdPlantillaEnvio()+","+informeFormEdicion.getIdInstitucion()+","+informeFormEdicion.getIdTipoEnvio());
			}
			request.setAttribute("idPlantillaEnvioSeleccionado",alIdsPlantillaEnvio);
			List alIdsPlantillaGeneracion = new ArrayList();
			if(informeFormEdicion.getIdPlantillaGeneracion()!=null && !informeFormEdicion.getIdPlantillaGeneracion().equals("")){
				
				alIdsPlantillaGeneracion.add(informeFormEdicion.getIdPlantillaGeneracion());
			}
			request.setAttribute("idPlantillaGeneracionSeleccionado",alIdsPlantillaEnvio);
			
			
			request.setAttribute("InformeFormEdicion", informeFormEdicion);
			informeForm.setModo("consultar");
		}catch (Exception e){
			throwExcp("messages.general.errorExcepcion", e, null); 			
		}
		return "edicion";
	}
	protected String consultarInformeConsulta(ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		
		InformeForm informeForm = (InformeForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		try {
			informeForm.setIdInstitucion(usrBean.getLocation());
			BusinessManager bm = getBusinessManager();
			InformesService informeService = (InformesService)bm.getService(InformesService.class);
			List<AdmTipoInformeBean> tiposInformeList = informeService.getTiposInforme(usrBean);
			informeForm.setTiposInforme(tiposInformeList);
			List<CenInstitucionBean> institucionesList = informeService.getInstitucionesInformes(new Integer(usrBean.getLocation()),usrBean);
			informeForm.setInstituciones(institucionesList);
			informeForm.setLenguajes(informeService.getLenguajes(usrBean));

		
			
			
			

			AdmInformeBean informeBean =  informeService.getInforme(informeForm, usrBean);
			InformeForm informeFormEdicion = informeBean.getInforme();
			informeFormEdicion.setLenguajes(informeForm.getLenguajes());
			informeFormEdicion.setClaseTipoInforme(informeForm.getClaseTipoInforme());
//			request.setAttribute("comboTipoEnvio", "cmbTipoEnviosInstSms");
			request.setAttribute("comboTipoEnvio", "cmbTipoEnviosSoloSms");
			request.setAttribute("parametrosComboEnvios", new String[]{informeForm.getIdInstitucion()});
			
			List alIdsTipoEnvio = new ArrayList();
			if(informeFormEdicion.getIdTipoEnvio()!=null && !informeFormEdicion.getIdTipoEnvio().equals(""))
				alIdsTipoEnvio.add(informeFormEdicion.getIdInstitucion()+","+informeFormEdicion.getIdTipoEnvio());
			request.setAttribute("idTipoEnvioSeleccionado",alIdsTipoEnvio);
			List alIdsPlantillaEnvio = new ArrayList();
			if(informeFormEdicion.getIdPlantillaEnvio()!=null && !informeFormEdicion.getIdPlantillaEnvio().equals("")){
				
				alIdsPlantillaEnvio.add(informeFormEdicion.getIdPlantillaEnvio()+","+informeFormEdicion.getIdInstitucion()+","+informeFormEdicion.getIdTipoEnvio());
			}
			request.setAttribute("idPlantillaEnvioSeleccionado",alIdsPlantillaEnvio);
			List alIdsPlantillaGeneracion = new ArrayList();
			if(informeFormEdicion.getIdPlantillaGeneracion()!=null && !informeFormEdicion.getIdPlantillaGeneracion().equals("")){
				
				alIdsPlantillaGeneracion.add(informeFormEdicion.getIdPlantillaGeneracion());
			}
			request.setAttribute("idPlantillaGeneracionSeleccionado",alIdsPlantillaEnvio);
			
			request.setAttribute("InformeFormEdicion", informeFormEdicion);
			informeForm.setModo("consultar");
		}catch (Exception e){
			throwExcp("messages.general.errorExcepcion", e, null); 			
		}
		return "edicion";
	}
	protected String duplicar(ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{

		InformeForm informeForm = (InformeForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		informeForm.setIdInstitucion(usrBean.getLocation());
		String forward="exception";
		try {
			
			BusinessManager bm = getBusinessManager();
			InformesService informeService = (InformesService)bm.getService(InformesService.class);
			AdmInformeBean informeBean =  informeService.getInforme(informeForm, usrBean);
			informeForm.setAlias(informeBean.getAlias());
			informeForm.setASolicitantes(informeBean.getASolicitantes());
			informeForm.setDescripcion(informeBean.getDescripcion());
			informeForm.setDestinatarios(informeBean.getDestinatarios());
			informeForm.setDirectorio(informeBean.getDirectorio());
			informeForm.setNombreFisico(informeBean.getNombreFisico());
			informeForm.setNombreSalida(informeBean.getNombreSalida());
			informeForm.setOrden(informeBean.getOrden());
			informeForm.setVisible(informeBean.getVisible());
			informeForm.setPreseleccionado(informeBean.getPreseleccionado());
			informeForm.setIdTipoInforme(informeBean.getIdTipoInforme());		

			boolean isNombreFisicoUnico = informeService.isNombreFisicoUnico(informeForm,true,this.getUserBean(request));			
			informeService.insertaInforme(informeForm, usrBean);
			
			if(isNombreFisicoUnico)
				forward = exitoRefresco("messages.duplicated.success",request);
			else{
				forward = exitoRefresco("administracion.informes.mensaje.aviso.insertar.nombreFisicoRepetido",request);				
			}
			
		}catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}
		return forward;
	}
	
	protected String insertar(ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{

		InformeForm informeForm = (InformeForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		String forward="exception";
		try {
			
			BusinessManager bm = getBusinessManager();
			InformesService informeService = (InformesService)bm.getService(InformesService.class);
			boolean isNombreFisicoUnico = informeService.isNombreFisicoUnico(informeForm,true,this.getUserBean(request));
			
			informeService.insertaInforme(informeForm, usrBean);
			
			
			if(isNombreFisicoUnico)
				forward = exitoModal("messages.updated.success",request);
			else{
				forward = exitoModal("administracion.informes.mensaje.aviso.insertar.nombreFisicoRepetido",request);
				
			}
			
		}catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}
		return forward;
	}
//	protected String comprobarInsertar(ActionMapping mapping, 		
//			MasterForm formulario, 
//			HttpServletRequest request, 
//			HttpServletResponse response) throws ClsExceptions, SIGAException 
//			{
//
//		InformeForm informeForm = (InformeForm) formulario;
//		UsrBean usrBean = this.getUserBean(request);
//		String forward="exception";
//		try {
//			
//			BusinessManager bm = getBusinessManager();
//			InformesService informeService = (InformesService)bm.getService(InformesService.class);
//			boolean isNombreFisicoUnico = informeService.isNombreFisicoUnico(informeForm,this.getUserBean(request));
//			if(isNombreFisicoUnico)
//				return insertar(mapping, formulario, request, response);
//			else{
//				informeForm.setModo("comprobarInsertar");
//				request.setAttribute("InformeFormEdicion", informeForm);
//				forward = errorModal("censo.busquedaClientesAvanzada.colegiados.localizacion", new ClsExceptions("censo.busquedaClientesAvanzada.colegiados.localizacion"), request);	
//				
//			}
//			
//			
//		} catch (Exception e) {
//			throwExcp("messages.general.errorExcepcion", e, null); 
//		}
//		return forward;
//	}
	
	
	protected String borrarConsultaInforme(ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{

		InformeForm informeForm = (InformeForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		String forward = "exception";
		try {
			
			BusinessManager bm = getBusinessManager();
			InformesService informeService = (InformesService)bm.getService(InformesService.class);
			AdmInformeBean informeBean =  informeService.getInforme(informeForm, usrBean);
			InformeForm informeFormEdicion = informeBean.getInforme();
			informeFormEdicion.setClaseTipoInforme(informeForm.getClaseTipoInforme());
			AdmConsultaInformeBean consultaInformeBean = new AdmConsultaInformeBean();
			consultaInformeBean.setIdInstitucion_consulta(new Integer(informeForm.getIdInstitucionConsulta()));
			consultaInformeBean.setIdInstitucion(new Integer(informeForm.getIdInstitucion()));
			consultaInformeBean.setIdPlantilla(informeForm.getIdPlantilla());
			consultaInformeBean.setIdConsulta(new Integer(informeForm.getIdConsulta()));
			
			informeService.borrarConsultaInforme(consultaInformeBean,informeFormEdicion, usrBean);
			forward = exitoRefresco("messages.updated.success",request);
			
		} catch (ClsExceptions e) {
			
			String error = UtilidadesString.getMensajeIdioma(usrBean,e.getMsg());
			informeForm.setMsgError(error);
		}catch (Exception e){
			String error = UtilidadesString.getMensajeIdioma(usrBean,"messages.general.errorExcepcion");
			informeForm.setMsgError(error);			
		}
		
		return forward;
	}
	protected String borrar(ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{

		InformeForm informeForm = (InformeForm) formulario;
		UsrBean usrBean = this.getUserBean(request);
		String forward = "exception";
		try {
			
			BusinessManager bm = getBusinessManager();
			InformesService informeService = (InformesService)bm.getService(InformesService.class);
			AdmInformeBean informeBean =  informeService.getInforme(informeForm, usrBean);
			InformeForm informeFormEdicion = informeBean.getInforme();
			informeFormEdicion.setClaseTipoInforme(informeForm.getClaseTipoInforme());
			if(informeBean.getIdTipoInforme().equals(AdmTipoInformeBean.TIPOINFORME_CONSULTAS)){
				AdmConsultaInformeBean consultaInformeBean = new AdmConsultaInformeBean();
				consultaInformeBean.setIdInstitucion(new Integer(informeForm.getIdInstitucion()));
				consultaInformeBean.setIdPlantilla(informeForm.getIdPlantilla());
				
				informeService.borrarConsultaInforme(consultaInformeBean,informeFormEdicion, usrBean);
			}else{
				informeService.borrarInforme(informeFormEdicion, usrBean);
			}
			
			forward = exitoRefresco("messages.updated.success",request);
			
		} catch (ClsExceptions e) {
			
			String error = UtilidadesString.getMensajeIdioma(usrBean,e.getMsg());
			informeForm.setMsgError(error);
		}catch (Exception e){
			String error = UtilidadesString.getMensajeIdioma(usrBean,"messages.general.errorExcepcion");
			informeForm.setMsgError(error);			
		}
		
		return forward;
	}
//	protected String comprobarBorrar(ActionMapping mapping, 		
//			MasterForm formulario, 
//			HttpServletRequest request, 
//			HttpServletResponse response) throws ClsExceptions, SIGAException 
//			{
//
//		InformeForm informeForm = (InformeForm) formulario;
//		try {
//			
//			BusinessManager bm = getBusinessManager();
//			InformesService informeService = (InformesService)bm.getService(InformesService.class);
//			AdmInformeBean informeBean =  informeService.getInforme(informeForm, this.getUserBean(request));
//			boolean isNombreFisicoComun = informeService.isNombreFisicoComun(informeBean.getInforme(),this.getUserBean(request));
//			if(!isNombreFisicoComun)
//				return borrar(mapping, formulario, request, response);
//			else{
//				request.setAttribute("msjConfimacion", UtilidadesString.getMensajeIdioma(this.getUserBean(request), "censo.busquedaClientesAvanzada.colegiados.localizacion") );
//			  	informeForm.setModo("borrar");
//			  	return "confirmar";
//			} 
//				 
//		}catch (Exception e) {
//			throwExcp("messages.general.errorExcepcion", e, null); 
//		}
//		return "exito";
//	}
	
	
	protected String modificar(ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{

		InformeForm informeForm = (InformeForm) formulario;
//		if(informeForm.getIdPlantilla()==null ||informeForm.getIdPlantilla().equals("") )
//			return insertar(mapping, formulario, request, response);
		
		UsrBean usrBean = this.getUserBean(request);
		String forward="exception";
		try {
			
			BusinessManager bm = getBusinessManager();
			InformesService informeService = (InformesService)bm.getService(InformesService.class);
			informeService.modificarInforme(informeForm, usrBean);
			
			forward = exitoRefresco("messages.updated.success",request);
		} catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}
		return forward;
	}
	
	protected String download(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{
		try 
		{
			InformeForm informeForm = (InformeForm) formulario;
			FileInforme fileInforme = informeForm.getDirectorioFile().getFiles().get(informeForm.getFilaInformeSeleccionada());
			
			File informeFile = fileInforme.getFile();

			if(informeFile==null || !informeFile.exists()){
				throw new SIGAException("error.messages.fileNotFound"); 
			}
			String nombreFichero = informeFile.getName();
			request.setAttribute("nombreFichero", nombreFichero);
			request.setAttribute("rutaFichero", informeFile.getPath());
			request.setAttribute("borrarFichero", "false");			
			request.setAttribute("generacionOK","OK");
		} 
		catch (SIGAException e) 
		{ 
		  	throw e; 
		}
		catch (Exception e) 
		{ 
			throwExcp("messages.general.error", new String[] {"modulo.envios"}, e, null); 
		}
		
		
		return "descarga";
	}
	protected String borrarInformeFile(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{
		

		InformeForm informeForm = (InformeForm) formulario;
		try {
			
			BusinessManager bm = getBusinessManager();
			InformesService informeService = (InformesService)bm.getService(InformesService.class);
			informeService.borrarInformeFile(informeForm);
			request.setAttribute("mensaje","messages.deleted.success");
			
		}catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}
		return "exito";
	}
	
	protected String comprobarBorrarInformeFile(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{
		

		InformeForm informeForm = (InformeForm) formulario;
		try {
			
			BusinessManager bm = getBusinessManager();
			InformesService informeService = (InformesService)bm.getService(InformesService.class);
			boolean isNombreFisicoUnico = informeService.isNombreFisicoUnico(informeForm,false,this.getUserBean(request));
			if(isNombreFisicoUnico)
				return borrarInformeFile(mapping, formulario, request, response);
			else{
				request.setAttribute("msjConfimacion", UtilidadesString.getMensajeIdioma(this.getUserBean(request),"administracion.informes.mensaje.confirmar.eliminar.fichero.compartido") );
			  	informeForm.setModo("borrarInformeFile");
			  	return "confirmar";	
			}
		}catch (Exception e) {
			throwExcp("messages.general.errorExcepcion", e, null); 
		}
		return "exito";
	}
	
	protected String upload(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
   	{
		InformeForm informeForm = (InformeForm) formulario;
   		
   			try {
   				BusinessManager bm = getBusinessManager();
   				InformesService informeService = (InformesService)bm.getService(InformesService.class);
   				informeService.uploadFile(informeForm);

			} catch (SIGAException e) {
				throw e;
			}
   		
			request.setAttribute("mensaje","messages.updated.success");
			return "exito";
   	}
	protected String listadoArchivosInforme(ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		
		InformeForm informeForm = (InformeForm) formulario;
		try {
			//InformeForm informeEdicion =  informeForm.getInformes().get(informeForm.getFilaSeleccionada());
			BusinessManager bm = getBusinessManager();
			InformesService informeService = (InformesService)bm.getService(InformesService.class);
			String idIntitucionPropietario = informeForm.getIdInstitucion();
			
			
			if(informeForm.getUsrBean()==null){
				UsrBean usrBean = this.getUserBean(request);
				informeForm.setUsrBean(usrBean);
				
			}
			String  idInstitucionUsuario = informeForm.getUsrBean().getLocation();
			
			
			//solo se tendra pemiso de borrado cuando sea de su propia intitucion o
			boolean isPermitidoBorrar = (informeForm.getModoInterno()!=null && !informeForm.getModoInterno().equals("consultar")) && ((idIntitucionPropietario.equals("0")&&idInstitucionUsuario.equals("2000"))
					|| !idIntitucionPropietario.equals("0")&&idIntitucionPropietario.equals(idInstitucionUsuario));
			
				
			FileInforme directorioFile = informeService.getFileDirectorio(informeForm,isPermitidoBorrar);
			informeForm.setDirectorioFile(directorioFile);
			request.setAttribute("InformeFormEdicion", informeForm);

		}catch (Exception e){
			throwExcp("messages.general.errorExcepcion", e, null); 			
		}
		
		return "listadoArchivosInforme";
	}
	protected String nuevoListadoArchivosInforme(ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException 
			{
		
		InformeForm informeForm = (InformeForm) formulario;
		try {
			request.setAttribute("InformeFormEdicion", informeForm);
			informeForm.clear();
		}catch (Exception e){
			throwExcp("messages.general.errorExcepcion", e, null); 			
		}
		
		return "listadoArchivosInforme";
	}
	
	
}