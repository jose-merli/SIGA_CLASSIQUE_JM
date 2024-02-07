/*
 * Created on Feb 3, 2005
 * @author juan.grau
 *
 */
package com.siga.envios.action;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.AppConstants.TipoIntercambioEnum;
import org.redabogacia.sigaservices.app.autogen.model.CerEnvios;
import org.redabogacia.sigaservices.app.autogen.model.EnvEnvios;
import org.redabogacia.sigaservices.app.helper.ExcelHelper;
import org.redabogacia.sigaservices.app.helper.SIGAServicesHelper;
import org.redabogacia.sigaservices.app.services.cer.CerSolicitudCertificadosEnviosService;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.FileHelper;
import com.atos.utils.GstDate;
import com.atos.utils.LogFileWriter;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.Paginador;
import com.siga.Utilidades.UtilidadesFicheros;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.administracion.SIGAConstants;
import com.siga.administracion.service.InformesService;
import com.siga.beans.AdmInformeAdm;
import com.siga.beans.AdmInformeBean;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenClienteBean;
import com.siga.beans.CenDireccionesAdm;
import com.siga.beans.CenDireccionesBean;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.CenInstitucionBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.CenSolicitudIncorporacionAdm;
import com.siga.beans.CenSolicitudIncorporacionBean;
import com.siga.beans.CerSolicitudCertificadosAdm;
import com.siga.beans.CerSolicitudCertificadosBean;
import com.siga.beans.EnvDestinatariosBean;
import com.siga.beans.EnvEnviosAdm;
import com.siga.beans.EnvEnviosBean;
import com.siga.beans.EnvEstadoEnvioAdm;
import com.siga.beans.EnvEstatEnvioAdm;
import com.siga.beans.EnvPlantillasEnviosBean;
import com.siga.beans.EnvTipoEnviosAdm;
import com.siga.beans.EnvTipoEnviosBean;
import com.siga.beans.FacFacturaAdm;
import com.siga.beans.FacFacturaBean;
import com.siga.beans.FacSerieFacturacionAdm;
import com.siga.beans.FacSerieFacturacionBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.PysCompraBean;
import com.siga.beans.PysProductosInstitucionAdm;
import com.siga.beans.PysProductosInstitucionBean;
import com.siga.beans.ScsDefendidosDesignaAdm;
import com.siga.beans.ScsDesignaAdm;
import com.siga.beans.ScsGuardiasTurnoAdm;
import com.siga.beans.ScsInclusionGuardiasEnListasAdm;
import com.siga.beans.ScsInclusionGuardiasEnListasBean;
import com.siga.envios.Documento;
import com.siga.envios.Envio;
import com.siga.envios.EnvioInformesGenericos;
import com.siga.envios.form.DefinirEnviosForm;
import com.siga.envios.service.SalidaEnviosService;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.informes.InformeAbono;
import com.siga.informes.InformeFactura;
import com.siga.informes.MasterReport;
import com.siga.servlets.SIGASvlProcesoAutomaticoRapido;

import es.satec.businessManager.BusinessManager;


/**
 * Action para la búsqueda de expedientes (simple y avanzada)
 * aalg: INC_09409_SIGA. Añadidas trazas
 */
public class DefinirEnviosAction extends MasterAction {


	public ActionForward executeInternal (ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		String mapDestino = "exception";
		MasterForm miForm = null;
		UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
		try 
		{ 
			miForm = (MasterForm) formulario;

			if (miForm != null) {
				String accion = miForm.getModo();

				if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
					ClsLogging.writeFileLog("DefinirEnviosAction:abrir. IdInstitucion:" + userBean.getLocation(), 10);
					mapDestino = abrir(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("getJQueryTiposEnvioPermitido")){
					ClsLogging.writeFileLog("DefinirEnviosAction:getJQueryTiposEnvioPermitido. IdInstitucion:" + userBean.getLocation(), 10);
					getJQueryTiposEnvioPermitido(mapping, miForm, request, response);
					return null;
				} 
				else if (accion.equalsIgnoreCase("getJQueryPlantillasEnvio")){
					ClsLogging.writeFileLog("DefinirEnviosAction:getJQueryPlantillasEnvio. IdInstitucion:" + userBean.getLocation(), 10);
					getJQueryPlantillasEnvio(mapping, miForm, request, response);
					return null;
				
				} else if (accion.equalsIgnoreCase("getJQueryTiposIntercambioTelematico"))	{
					ClsLogging.writeFileLog("DefinirEnviosAction:getJQueryTiposIntercambioTelematico. IdInstitucion:" + userBean.getLocation(), 10);
					getJQueryTiposIntercambioTelematico(mapping, miForm, request, response);
					return null;
				
				} else if (accion.equalsIgnoreCase("envioModal")){
					ClsLogging.writeFileLog("DefinirEnviosAction:envioModal. IdInstitucion:" + userBean.getLocation(), 10);
					mapDestino = envioModal(mapping, miForm, request, response);
				} 
				
				else if (accion.equalsIgnoreCase("envioModalCertificado")){
					ClsLogging.writeFileLog("DefinirEnviosAction:envioModalCertificado. IdInstitucion:" + userBean.getLocation(), 10);
					mapDestino = envioModalCertificado(mapping, miForm, request, response);
				} else if (accion.equalsIgnoreCase("insertarEnvioModalCertificado")){
					ClsLogging.writeFileLog("DefinirEnviosAction:insertarEnvioModalCertificado. IdInstitucion:" + userBean.getLocation(), 10);
					mapDestino = insertarEnvioModalCertificado(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("envioModalMasivoCertificado")){
					ClsLogging.writeFileLog("DefinirEnviosAction:insertarEnvioModalMasivoCertificado. IdInstitucion:" + userBean.getLocation(), 10);
					mapDestino = envioModalMasivoCertificado(mapping, miForm, request, response);
				}
				else if (accion.equalsIgnoreCase("insertarEnvioGenerico")){
					ClsLogging.writeFileLog("DefinirEnviosAction:insertarEnvioGenerico. IdInstitucion:" + userBean.getLocation(), 10);
					mapDestino = insertarEnvioGenerico(mapping, miForm, request, response);
				}
				else if (accion.equalsIgnoreCase("insertarEnvioModal")){
					ClsLogging.writeFileLog("DefinirEnviosAction:insertarEnvioModal. IdInstitucion:" + userBean.getLocation(), 10);
					mapDestino = insertarEnvioModal(mapping, miForm, request, response);
				}
				else if (accion.equalsIgnoreCase("procesarEnvio")){
					ClsLogging.writeFileLog("DefinirEnviosAction:procesarEnvio. IdInstitucion:" + userBean.getLocation(), 10);
					mapDestino = procesarEnvio(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("reenviar")){
					ClsLogging.writeFileLog("DefinirEnviosAction:procesarEnvio. IdInstitucion:" + userBean.getLocation(), 10);
					String idPaginador = getIdPaginador(super.paginador,getClass().getName());
					request.getSession().removeAttribute(idPaginador);
					mapDestino = reenviar(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("enviardenuevo")){
					ClsLogging.writeFileLog("DefinirEnviosAction:procesarEnvio. IdInstitucion:" + userBean.getLocation(), 10);
					mapDestino = enviardenuevo(mapping, miForm, request, response);
				}

				else if (accion.equalsIgnoreCase("descargarLogErrores")){
					ClsLogging.writeFileLog("DefinirEnviosAction:descargarLogErrores. IdInstitucion:" + userBean.getLocation(), 10);
					mapDestino = descargarLogErrores(mapping, miForm, request, response);
				}

				else if (accion.equalsIgnoreCase("descargarEstadistica")){
					ClsLogging.writeFileLog("DefinirEnviosAction:descargarEstadistica. IdInstitucion:" + userBean.getLocation(), 10);
					mapDestino = descargarEstadistica(mapping, miForm, request, response);
				
				} else if (accion.equalsIgnoreCase("verRelacionEnvio")){
					ClsLogging.writeFileLog("DefinirEnviosAction:verRelacionEnvio. IdInstitucion:" + userBean.getLocation(), 10);
					mapDestino = verRelacionEnvio(mapping, miForm, request, response);
				
				} else if (accion.equalsIgnoreCase("editarRelacionEnvio")){
					ClsLogging.writeFileLog("DefinirEnviosAction:editarRelacionEnvio. IdInstitucion:" + userBean.getLocation(), 10);
					mapDestino = editarRelacionEnvio(mapping, miForm, request, response);
				
				} else if (accion.equalsIgnoreCase("respuestaTelematica")){
					ClsLogging.writeFileLog("DefinirEnviosAction:respuestaTelematica. IdInstitucion:" + userBean.getLocation(), 10);
					mapDestino = respuestaTelematica(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("editarComunicacion")){
					ClsLogging.writeFileLog("editarComunicacion. IdInstitucion:" + userBean.getLocation(), 10);
					mapDestino = editarComunicacion(mapping, miForm, request, response,true);
				}else if (accion.equalsIgnoreCase("consultarComunicacion")){
					ClsLogging.writeFileLog("consultarComunicacion. IdInstitucion:" + userBean.getLocation(), 10);
					mapDestino = editarComunicacion(mapping, miForm, request, response,false);
				}else if (accion.equalsIgnoreCase("borrarComunicacion")){
					ClsLogging.writeFileLog("borrarComunicacion. IdInstitucion:" + userBean.getLocation(), 10);
					mapDestino = borrarComunicacion(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("descargarLogComunicacion")){
					ClsLogging.writeFileLog("descargarLogComunicacion. IdInstitucion:" + userBean.getLocation(), 10);
					mapDestino = descargarLogComunicacion(mapping, miForm, request, response);
				}else if (accion.equalsIgnoreCase("buscarInicio")){
					String idPaginador = getIdPaginador(super.paginador,getClass().getName());
					request.getSession().removeAttribute(idPaginador);
					mapDestino = buscar(mapping, miForm, request, response);
				}else if(accion.equalsIgnoreCase("descargarCertificadosYFacturas")){
					ClsLogging.writeFileLog("descargarCertificadosYFacturas. IdInstitucion:" + userBean.getLocation(), 10);
					mapDestino = descargarCertificadosYFacturas(mapping, miForm, request, response);
					
				}
					
				/*else if (accion.equalsIgnoreCase("procesarCorreoOrdinario"))
	            {
	                mapDestino = procesarCorreoOrdinario(mapping, miForm, request, response);
	            }*/
				else 
				{
					return super.executeInternal(mapping,formulario,request,response);
				}
			}

			if (mapDestino == null)	
			{ 
				throw new ClsExceptions("El ActionMapping no puede ser nulo", "", "0", "GEN00", "15");
			}

			return mapping.findForward(mapDestino);
		} 
		catch (SIGAException es) 
		{ 
			throw es; 
		} 
		catch (Exception e) 
		{ 
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.envios"});
		} 
	}

	protected String abrir(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws SIGAException {

		// Si el acceso es de lectura, quitamos el botón de 'Nuevo'

		try{

			//DefinirEnviosForm form = (DefinirEnviosForm)formulario;

			//if (request.getParameter("buscar")!=null){
			/*if (ses.getAttribute("buscarEnvios")!=null){
	    	    request.setAttribute("buscarEnvios","true");
	    	    ses.removeAttribute("buscarEnvios");
		    	if (ses.getAttribute("DATABACKUP")!=null){
			    	if (ses.getAttribute("DATABACKUP").getClass().getName().equals("com.siga.envios.form.DefinirEnviosForm")){
			    	    DefinirEnviosForm formBackup = (DefinirEnviosForm)request.getSession().getAttribute("DATABACKUP");
			    		form.setFechaDesde(formBackup.getFechaDesde());
		    			form.setFechaHasta(formBackup.getFechaHasta());
		    			form.setNombre(formBackup.getNombre());
		    			form.setIdEstado(formBackup.getIdEstado());
		    			form.setIdTipoEnvio(formBackup.getIdTipoEnvio());
		    		}
		    	}
	    	}*/

			// miro a ver si tengo que ejecutar 
			//la busqueda una vez presentada la pagina
			String buscar2 = request.getParameter("buscar");
			request.setAttribute("buscar",buscar2);
			request.setAttribute("reset",request.getParameter("reset")!=null?request.getParameter("reset"):"");

			/*
	    	if (ses.getAttribute("DATABACKUP")!=null){
	    	    try{
	    	        Hashtable htBackup = (Hashtable)ses.getAttribute("DATABACKUP");

	    	        //parámetro que fuerza la búsqueda
	    	        String buscar = (String)htBackup.get("buscarEnvios");
	    	        if (buscar!=null) request.setAttribute("buscarEnvios","true");

	    	        //rellenamos el formulario
	    	        DefinirEnviosForm formBackup = (DefinirEnviosForm)htBackup.get("enviosForm");
		    		if (formBackup!=null) {
		    	        form.setFechaDesde(formBackup.getFechaDesde());
		    			form.setFechaHasta(formBackup.getFechaHasta());
		    			form.setNombre(formBackup.getNombre());
		    			form.setIdEstado(formBackup.getIdEstado());
		    			form.setComboTipoEnvio(formBackup.getComboTipoEnvio());
		    		}		    	    
	    			// miro a ver si tengo que ejecutar 
	    			//la busqueda una vez presentada la pagina
	    			String buscar2 = request.getParameter("buscar");
	    			request.setAttribute("buscar",buscar2);

	    	    } catch (Exception e){
	    	        e.printStackTrace();
	    	    }
	    	}
			 */
			
		}catch (Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null); 
		}
		UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
		request.setAttribute("isComision",  userBean.isComision());
		ClsLogging.writeFileLog("DefinirEnviosAction:fin abrir. IdInstitucion:" + userBean.getLocation(), 10);
		return("inicio");
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#nuevo(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws SIGAException {	    
		UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
		DefinirEnviosForm form = (DefinirEnviosForm) formulario;
		form.setNombre("");	   

		//Para volver correctamente desde envios:
		request.getSession().removeAttribute("EnvEdicionEnvio");		
		GenParametrosAdm param = new GenParametrosAdm(userBean);
		try {
			request.setAttribute("smsHabilitado",param.getValor(userBean.getLocation(), "ENV", "HABILITAR_SMS_BUROSMS", "N"));
		} catch (Exception e) {
			// Si no se mete este parametro se controla en la jsp
		}
		ClsLogging.writeFileLog("DefinirEnviosAction:fin nuevo. IdInstitucion:" + userBean.getLocation(), 10);
		return "nuevo";
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#insertar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws SIGAException {

		UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
		UserTransaction tx = userBean.getTransaction();

		EnvEnviosAdm envioAdm = new EnvEnviosAdm(this.getUserBean(request));	    
		DefinirEnviosForm form = (DefinirEnviosForm) formulario;

		//Obtenemos los parámetros del formulario

		EnvEnviosBean envioBean = new EnvEnviosBean();	    
		Integer id = null;
		try {
			tx.begin();
			id=envioAdm.getNewIdEnvio(userBean);
			envioBean.setIdEnvio(id);
			envioBean.setIdInstitucion(Integer.valueOf(userBean.getLocation()));
			envioBean.setDescripcion(form.getNombre());
			envioBean.setIdTipoEnvios(Integer.valueOf(form.getIdTipoEnvio()));
			if(envioBean.getIdTipoEnvios().equals(EnvTipoEnviosAdm.K_CORREO_ELECTRONICO) || envioBean.getIdTipoEnvios().equals(EnvTipoEnviosAdm.K_DOCUMENTACIONLETRADO))
				envioBean.setAcuseRecibo(form.getAcuseRecibo());
			envioBean.setIdPlantillaEnvios(Integer.valueOf(form.getIdPlantillaEnvios()));
			if (!form.getIdPlantillaGeneracion().trim().equals("")){
				envioBean.setIdPlantilla(Integer.valueOf(form.getIdPlantillaGeneracion()));
			}
			envioBean.setIdEstado(new Integer(EnvEnviosAdm.ESTADO_INICIAL));
			envioBean.setFechaCreacion("SYSDATE");
			envioBean.setGenerarDocumento("N");//Valor por defecto
			envioBean.setImprimirEtiquetas("N");//Valor por defecto
			envioAdm.insert(envioBean);

			Integer idTipoEnvio = Integer.valueOf(form.getIdTipoEnvio());

			envioAdm.copiarCamposPlantilla(Integer.valueOf(userBean.getLocation()), 
					id, 
					idTipoEnvio,
					Integer.valueOf(form.getIdPlantillaEnvios()),null);


			request.setAttribute("descOperation","messages.inserted.success");
			tx.commit();

		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.envios"},e,tx);
		}
		//"retorno" contiene el valor que devolverá la ventana modal, utilizando "refresh" (genericLoadForm.jsp)
		//en retorno, simulamos la pulsación en editar de una fila
		//debemos montar un string con los campos ocultos separados por comas, 
		//un %, y los campos visibles separados por comas, todo entre comillas simples

		request.setAttribute("retorno",String.valueOf(id.intValue())+","+form.getIdTipoEnvio()+"%"+form.getNombre());
		request.setAttribute("modal","1");   
		ClsLogging.writeFileLog("DefinirEnviosAction:fin insertar. IdInstitucion:" + userBean.getLocation(), 10);
		return "refresh";

	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String buscar(ActionMapping mapping, MasterForm formulario, 
			HttpServletRequest request, HttpServletResponse response) 
	throws SIGAException
	{
		DefinirEnviosForm form = (DefinirEnviosForm)formulario;
		HttpSession ses=request.getSession();
		UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));

		String fechaDesde = form.getFechaDesde();
		String fechaHasta = form.getFechaHasta();
		//String sIdEstado = form.getIdEstado();
		String idEstado = form.getIdEstado();
		String nombre = form.getNombre();
		String idTipoEnvio = form.getIdTipoEnvio();        
		String idEnvio = form.getIdEnvioBuscar();        
		String idInstitucion = userBean.getLocation();
		String tipoFecha = form.getTipoFecha();
		boolean conArchivados = UtilidadesString.stringToBoolean(form.getConArchivados());

		EnvEnviosAdm enviosAdm = new EnvEnviosAdm (this.getUserBean(request));
		Vector datos = null;
		try {

			/***** PAGINACION*****/
			String idPaginador = getIdPaginador(super.paginador,getClass().getName());
			request.setAttribute(ClsConstants.PARAM_PAGINACION,idPaginador);
			HashMap databackup=getPaginador(request, idPaginador);
			if (databackup!=null){
//			if (request.getSession().getAttribute("DATAPAGINADOR")!=null){ 
				Paginador paginador = (Paginador)databackup.get("paginador");
				datos=new Vector();


				//Si no es la primera llamada, obtengo la página del request y la busco con el paginador
				String pagina = (String)request.getParameter("pagina");




				if (pagina!=null){
					datos = paginador.obtenerPagina(Integer.parseInt(pagina));
				}else{// cuando hemos editado un registro de la busqueda y volvemos a la paginacion
					datos = paginador.obtenerPagina((paginador.getPaginaActual()));
				}




				databackup.put("paginador",paginador);
				databackup.put("datos",datos);

			}else{	

				databackup=new HashMap();
				Paginador resultado = null;
				datos = null;

				resultado = enviosAdm.buscarEnvio(idEnvio,tipoFecha,fechaDesde,fechaHasta,idEstado,
						nombre,idTipoEnvio,idInstitucion,userBean.isComision(),conArchivados);
				databackup.put("paginador",resultado);
				if (resultado!=null){ 
					datos = resultado.obtenerPagina(1);
					databackup.put("datos",datos);
					setPaginador(request, idPaginador, databackup);
//					request.getSession().setAttribute("DATAPAGINADOR",databackup);
				}   



			}

		} catch (Exception e) {
			this.throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
		}

		// Obtengo el pathFTP
		String pathFTP = "";
		GenParametrosAdm paramAdm = new GenParametrosAdm(this.getUserBean(request));
		try {
			pathFTP = paramAdm.getValor(userBean.getLocation(),"ENV","URL_FTP_DESCARGA_ENVIOS_ORDINARIOS","");
		} catch (Exception e) {
			//
		}
		request.setAttribute("pathFTP",pathFTP);

		Hashtable htBackup = new Hashtable();
		htBackup.put("enviosForm",form);
		htBackup.put("buscarEnvios","true");        
		ses.setAttribute("DATABACKUP",htBackup);

		request.setAttribute("datos", datos);        
		ClsLogging.writeFileLog("DefinirEnviosAction:fin buscar. IdInstitucion:" + userBean.getLocation(), 10);
		return "resultado";
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#editar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws ClsExceptions {

		return mostrarRegistro(mapping,formulario,request,response,true);
	}
	
	
	protected String editarComunicacion(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response, boolean bEditable) throws ClsExceptions{

		HttpSession ses=request.getSession();
		UsrBean userBean = ((UsrBean)ses.getAttribute(("USRBEAN")));
		DefinirEnviosForm form = (DefinirEnviosForm)formulario;

		Hashtable htParametros=new Hashtable();
		htParametros.put("idEnvio",form.getIdEnvio());
		htParametros.put("idInstitucion",form.getIdInstitucion());
		htParametros.put("idTipoEnvio",form.getIdTipoEnvio());
		htParametros.put("acceso",bEditable?"editar":"ver");
		htParametros.put("idIntercambio",form.getIdIntercambio());
		htParametros.put("origen",form.getOrigen());
		htParametros.put("datosEnvios",form.getDatosEnvios());
		request.setAttribute("envio", htParametros);    
		request.setAttribute("nuevo", "false");	    
		request.setAttribute("buscarEnvios","false");
		ClsLogging.writeFileLog("DefinirEnviosAction:fin editarComunicacion . IdInstitucion:" + userBean.getLocation(), 10);
			
		return "editar";
	}
	

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#ver(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws ClsExceptions {

		return mostrarRegistro(mapping,formulario,request,response,false);		
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#borrar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws SIGAException {

		HttpSession ses=request.getSession();
		UsrBean userBean = ((UsrBean)ses.getAttribute(("USRBEAN")));

		DefinirEnviosForm form = (DefinirEnviosForm)formulario;
		Vector vOcultos = form.getDatosTablaOcultos(0);		

		String idInstitucion = userBean.getLocation();
		String idEnvio = (String)vOcultos.elementAt(0);
		try {
			BusinessManager businessManager =  BusinessManager.getInstance();
			SalidaEnviosService salidaEnviosService = (SalidaEnviosService) businessManager.getService(SalidaEnviosService.class);
			EnvEnvios envEnvios = new EnvEnvios();
			envEnvios.setIdenvio(new Long(idEnvio));
			envEnvios.setIdinstitucion(new Short(idInstitucion));
			envEnvios.setUsumodificacion(new Integer(userBean.getUserName()));

			//CR7 - Ya no se borran comunicaciones, solo se dan bajas logicas
			salidaEnviosService.darBajaLogicaEnvio(envEnvios);

		} catch (Exception e) {
			this.throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
		}

		ClsLogging.writeFileLog("DefinirEnviosAction:fin borrar. IdInstitucion:" + userBean.getLocation(), 10);
		return exitoRefresco("messages.deleted.success",request);
	}
	protected String borrarComunicacion(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws SIGAException {

		HttpSession ses=request.getSession();
		UsrBean userBean = ((UsrBean)ses.getAttribute(("USRBEAN")));

		DefinirEnviosForm form = (DefinirEnviosForm)formulario;
		String idInstitucion = form.getIdInstitucion();
		String idEnvio = form.getIdEnvio();
		
		try {
			BusinessManager businessManager =  BusinessManager.getInstance();
			SalidaEnviosService salidaEnviosService = (SalidaEnviosService) businessManager.getService(SalidaEnviosService.class);
			EnvEnvios envEnvios = new EnvEnvios();
			envEnvios.setIdenvio(new Long(idEnvio));
			envEnvios.setIdinstitucion(new Short(idInstitucion));
			envEnvios.setUsumodificacion(new Integer(userBean.getUserName()));

			//CR7 - Ya no se borran comunicaciones, solo se dan bajas logicas
			salidaEnviosService.darBajaLogicaEnvio(envEnvios);
			

		} catch (Exception e) {
			this.throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
		}
		ClsLogging.writeFileLog("DefinirEnviosAction:fin borrarComunicacion. IdInstitucion:" + idInstitucion, 10);
		return exitoRefresco("messages.deleted.success",request);
	}
	private String descargarLogComunicacion(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) 
			throws SIGAException{
		String forward = "descargaFichero";
		
		DefinirEnviosForm form = (DefinirEnviosForm)formulario;
		String idInstitucion = form.getIdInstitucion();
		String idEnvio = form.getIdEnvio();
		
		try {
			BusinessManager businessManager =  BusinessManager.getInstance();
			SalidaEnviosService salidaEnviosService = (SalidaEnviosService) businessManager.getService(SalidaEnviosService.class);
			File logComunicacion =  salidaEnviosService.getLogComunicacion(new Long(idEnvio), new Short(idInstitucion));
			if(logComunicacion==null || !logComunicacion.exists()){
				throw new SIGAException("messages.general.error.ficheroNoExiste"); 
			}
			String extension = logComunicacion.getName().substring(logComunicacion.getName().lastIndexOf("."));
			if(!extension.equals(".xls"))
				request.setAttribute("borrarFichero", "true");
			request.setAttribute("nombreFichero", logComunicacion.getName());
			request.setAttribute("rutaFichero", logComunicacion.getPath());

		} catch (Exception e) {
			this.throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
		}
		ClsLogging.writeFileLog("DefinirEnviosAction:fin borrarComunicacion. IdInstitucion:" + idInstitucion, 10);
		return forward;
	}


	protected String mostrarRegistro(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response, boolean bEditable) throws ClsExceptions{

		HttpSession ses=request.getSession();
		UsrBean userBean = ((UsrBean)ses.getAttribute(("USRBEAN")));
		DefinirEnviosForm form = (DefinirEnviosForm)formulario;

		Vector vOcultos = form.getDatosTablaOcultos(0);	
		if(vOcultos!=null){
			String idEnvio = (String)vOcultos.elementAt(0);
			String idTipoEnvio = (String)vOcultos.elementAt(1);
			String idIntercambio = null;
			if(vOcultos.size()>2)
				idIntercambio = (String)vOcultos.elementAt(2);
			
			//Anhadimos parametros para las pestanhas
			Hashtable htParametros=new Hashtable();
			htParametros.put("idEnvio",idEnvio);
			htParametros.put("idTipoEnvio",idTipoEnvio);
			htParametros.put("acceso",bEditable?"editar":"ver");
			if(idIntercambio!=null && Integer.parseInt(idTipoEnvio)==EnvTipoEnviosAdm.K_ENVIOTELEMATICO){
				htParametros.put("idIntercambio",idIntercambio);
			}else{
				htParametros.put("idIntercambio","");
			}
			
			request.setAttribute("envio", htParametros);    
	
	
			request.setAttribute("nuevo", "false");	    
			request.setAttribute("buscarEnvios","true");
	
			ClsLogging.writeFileLog("DefinirEnviosAction:fin mostrarRegistro. IdInstitucion:" + userBean.getLocation(), 10);
		}else{
			//Anhadimos parametros para las pestanhas
			Hashtable htParametros=new Hashtable();
			htParametros.put("idEnvio",form.getIdEnvio());
			htParametros.put("idInstitucion",form.getIdInstitucion()!=null?form.getIdInstitucion():userBean.getLocation());
			htParametros.put("idTipoEnvio",form.getIdTipoEnvio());
			htParametros.put("acceso",bEditable?"editar":"ver");
			htParametros.put("idIntercambio",form.getIdIntercambio()!=null?form.getIdIntercambio():"");
			request.setAttribute("envio", htParametros);    
			request.setAttribute("nuevo", "false");	    
			request.setAttribute("buscarEnvios","false");
			
			ClsLogging.writeFileLog("DefinirEnviosAction:fin mostrarRegistro de pestaña comunicaciones. IdInstitucion:" + userBean.getLocation(), 10);
			
		}
		return "editar";
	}
	
	/**
	 * Este metodo no convierte el vector de datos que viene de la jsp y los trasforma en
	 * un hashtable donde las clavs son las persona. Luego los envios a morosos se haran por persona.
	 * @return
	 */
	/*private Hashtable getFacturasPersonaAComunicar(DefinirEnviosForm form){
		Hashtable htFacturasPersona = new Hashtable();
		ArrayList alFacturas = null;
		for (int i = 0; i < form.getDatosTabla().size(); i++) {
			Vector vCampos = form.getDatosTablaOcultos(i);
			String idPersona = (String) vCampos.get(0);
			String idFactura = (String) vCampos.get(1);
			if(htFacturasPersona.containsKey(idPersona)){
				alFacturas = (ArrayList) htFacturasPersona.get(idPersona);

			}else{
				alFacturas = new ArrayList();

			}
			alFacturas.add(idFactura);
			htFacturasPersona.put(idPersona, alFacturas);


		}


		return htFacturasPersona;


	}*/
	protected String envioModal(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) 
	throws SIGAException{

		DefinirEnviosForm form = (DefinirEnviosForm)formulario;
		UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));   

		String idInstitucion = userBean.getLocation();	
		String idSolicitud = form.getIdSolicitud();
		String idFactura = form.getIdFactura();
		String idPersona = form.getIdPersona();
		String desc = form.getDescEnvio();
		String subModo = form.getSubModo();
		String datosEnvios = form.getDatosEnvios();

		String nombreyapellidos = "";
		//String idEnvio=null;
		String idTipoEnvio=form.getIdTipoEnvio();

		try {
			//SOLICITUD CERTIFICADO:
			// Si el envío procede de una solicitud de certificado, seteamos el tipo
			// de envío según el tipo que se eligió al solicitar el certificado.
			if (subModo!=null && subModo.equalsIgnoreCase("SolicitudCertificado")){
				CerSolicitudCertificadosAdm solCerAdm = new CerSolicitudCertificadosAdm(this.getUserBean(request));
				Hashtable htPk = new Hashtable();
				htPk.put(CerSolicitudCertificadosBean.C_IDINSTITUCION,idInstitucion);
				htPk.put(CerSolicitudCertificadosBean.C_IDSOLICITUD,idSolicitud);
				CerSolicitudCertificadosBean solCerBean = (CerSolicitudCertificadosBean) solCerAdm.selectByPK(htPk).firstElement();
				idTipoEnvio = String.valueOf(solCerBean.getIdTipoEnvios());
				//form.setComboTipoEnvio(idInstitucion+","+idTipoEnvio);
				ArrayList comboTipoEnvio = new ArrayList();
				comboTipoEnvio.add(idInstitucion+","+idTipoEnvio);
				request.setAttribute("comboTipoEnvio",comboTipoEnvio);

				PysProductosInstitucionAdm prodAdm = new PysProductosInstitucionAdm(this.getUserBean(request));
				Integer idTipoProducto = solCerBean.getPpn_IdTipoProducto();
				Long idProducto = solCerBean.getPpn_IdProducto();
				Long idProductoInstitucion = solCerBean.getPpn_IdProductoInstitucion();
				Hashtable htPk2 = new Hashtable();
				htPk2.put(PysProductosInstitucionBean.C_IDINSTITUCION,idInstitucion);
				htPk2.put(PysProductosInstitucionBean.C_IDTIPOPRODUCTO,idTipoProducto);
				htPk2.put(PysProductosInstitucionBean.C_IDPRODUCTO,idProducto);
				htPk2.put(PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION,idProductoInstitucion);
				PysProductosInstitucionBean prodBean = (PysProductosInstitucionBean)prodAdm.selectByPK(htPk2).firstElement();
				String tipoCert = prodBean.getTipoCertificado();
				if (tipoCert.equalsIgnoreCase(PysProductosInstitucionAdm.TIPO_CERTIFICADO_COMUNICACION)||
						tipoCert.equalsIgnoreCase(PysProductosInstitucionAdm.TIPO_CERTIFICADO_DILIGENCIA)){
					request.setAttribute("ComDil","true");
				}
				//SOLICITUD INCORPORACION:
			} else if (subModo!=null && subModo.equalsIgnoreCase("solicitudIncorporacion")){
				CenSolicitudIncorporacionAdm admSolic = new CenSolicitudIncorporacionAdm(this.getUserBean(request));
				Hashtable hashSolic = new Hashtable();
				hashSolic.put(CenSolicitudIncorporacionBean.C_IDSOLICITUD,idSolicitud);
				Vector aux = admSolic.selectByPK(hashSolic);
				CenSolicitudIncorporacionBean beanSolic = null;
				if (aux!=null && aux.size()>0) {
					beanSolic = (CenSolicitudIncorporacionBean) aux.get(0);
					nombreyapellidos = beanSolic.getNombre()+" "+beanSolic.getApellido1()+" "+beanSolic.getApellido2(); 
				}
			} else if (subModo!=null && subModo.equalsIgnoreCase("envioFactura")){
				FacFacturaAdm admFac = new FacFacturaAdm(this.getUserBean(request));
				Hashtable hashFac = new Hashtable();
				hashFac.put(FacFacturaBean.C_IDINSTITUCION,userBean.getLocation());
				hashFac.put(FacFacturaBean.C_IDFACTURA,idFactura);
				Vector aux = admFac.selectByPK(hashFac);
				FacFacturaBean beanFac = null;
				if (aux!=null && aux.size()>0) {
					beanFac = (FacFacturaBean) aux.get(0);
					desc = UtilidadesString.getMensajeIdioma(userBean.getLanguage(),"envios.literal.facturaNumero") + " " + beanFac.getNumeroFactura();
					idPersona=beanFac.getIdPersona().toString();
				}
			}
			//obtener idEnvio
			//EnvEnviosAdm envAdm = new EnvEnviosAdm(this.getUserBean(request));
			//idEnvio = String.valueOf(envAdm.getNewIdEnvio(idInstitucion));


			//obtener nombreyapellidos
			CenPersonaAdm persAdm = new CenPersonaAdm(this.getUserBean(request));		
			if ( (idPersona!=null && !idPersona.equals("")) && 
					(subModo!=null && !subModo.equalsIgnoreCase("solicitudIncorporacion")
					) )
				nombreyapellidos = persAdm.obtenerNombreApellidos(idPersona);

			/*// RGG como no entiendo la condicion de arriba, anhado la que necesita
			//JTA Yo tampoco! no obstante añado el or con envio morosos
			if ( idPersona!=null && !idPersona.equals(""))
					nombreyapellidos = persAdm.obtenerNombreApellidos(idPersona);*/


			//Nombre:
			// Antes: String nombre = idEnvio + "-" + desc + "-" + nombreyapellidos;
			// Ahora INC 4403:
			String nombre = ""  + desc + " " + nombreyapellidos;
			//////////////////////////////

			if (nombre.length()>100){
				nombre = nombre.substring(0,99);
			}
			form.setNombre(nombre);

			//Fecha Programada:
			SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);

			if (form.getModo().equalsIgnoreCase("envioModal"))
			{
				sdf.applyPattern(ClsConstants.DATE_FORMAT_SHORT_SPANISH);
			}

			else
			{
				sdf.applyPattern(ClsConstants.DATE_FORMAT_LONG_SPANISH);
			}

			Calendar cal = Calendar.getInstance();   
			String fecha;
			fecha = sdf.format(cal.getTime()).toString();
			form.setFechaProgramada(fecha);


			//request.setAttribute(EnvEnviosBean.C_IDENVIO,idEnvio);
			request.setAttribute(CerSolicitudCertificadosBean.C_IDPERSONA_DES,idPersona);
			request.setAttribute(CerSolicitudCertificadosBean.C_IDSOLICITUD,idSolicitud);
			request.setAttribute(FacFacturaBean.C_IDFACTURA,idFactura);
			request.setAttribute("subModo",subModo);
			request.setAttribute("datosEnvios",datosEnvios);
			GenParametrosAdm param = new GenParametrosAdm(userBean);
			request.setAttribute("smsHabilitado",param.getValor(idInstitucion, "ENV", "HABILITAR_SMS_BUROSMS", "N"));
			
		} catch (Exception ex) {
			throwExcp("messages.general.error",new String[] {"modulo.envios"},ex,null);
		}
		
		ClsLogging.writeFileLog("DefinirEnviosAction:fin envioModal. IdInstitucion:" + userBean.getLocation(), 10);
		return "envioModal";
	}
	protected String envioModalCertificado(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) 
	throws SIGAException{

		DefinirEnviosForm form = (DefinirEnviosForm)formulario;
		UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN"))); 

		try {
			//SOLICITUD CERTIFICADO:

			//Fecha Programada:
			SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
			sdf.applyPattern(ClsConstants.DATE_FORMAT_SHORT_SPANISH);

			Calendar cal = Calendar.getInstance();   
			String fecha;
			fecha = sdf.format(cal.getTime()).toString();
			form.setFechaProgramada(fecha);
			GenParametrosAdm param = new GenParametrosAdm(userBean);
			request.setAttribute("smsHabilitado",param.getValor(userBean.getLocation(), "ENV", "HABILITAR_SMS_BUROSMS", "N"));

		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
		}
		ClsLogging.writeFileLog("DefinirEnviosAction:fin envioModalCertificado. IdInstitucion:" + userBean.getLocation(), 10);
		return "envioModalCertificado";
	}

	protected String insertarEnvioGenerico (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException 
			{

		UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));   
		UserTransaction tx = userBean.getTransactionPesada();
		DefinirEnviosForm form = (DefinirEnviosForm)formulario;
		boolean isEnvioBatch = false;
		
		try {
//			String fechaProg = form.getFechaProgramada();
//			if (fechaProg != null && fechaProg.length() == 10)
//				fechaProg += " " + new Date().getHours() + ":" + new Date().getMinutes() + ":" + new Date().getSeconds();
			Date fechaProgr = SIGAServicesHelper.getFecha(form.getFechaProgramada()+form.getHoras()+form.getMinutos(),"dd/MM/yyyyHHmm");
			if(fechaProgr.compareTo(new Date())<0) {
				throw new SIGAException("messages.general.error.fechaPosteriorActual");
			} 
			
			
				tx.begin();
				EnvioInformesGenericos envioInformesGenericos = new EnvioInformesGenericos();
				if (form.getIdTipoInforme().equalsIgnoreCase(EnvioInformesGenericos.comunicacionesDesigna)){
					envioInformesGenericos.gestionarComunicacionDesignas(form,  request.getLocale(), userBean);
					isEnvioBatch = envioInformesGenericos.isEnvioBatch();
				}else if (form.getIdTipoInforme().equalsIgnoreCase(EnvioInformesGenericos.comunicacionesEjg)){
					envioInformesGenericos.gestionarComunicacionEjg(form,  request.getLocale(), userBean);
					isEnvioBatch = envioInformesGenericos.isEnvioBatch();
				}else if (form.getIdTipoInforme().equalsIgnoreCase(EnvioInformesGenericos.comunicacionesCAJG)){
					envioInformesGenericos.gestionarComunicacionCAJG(form,  request.getLocale(), userBean);
					isEnvioBatch = envioInformesGenericos.isEnvioBatch();
				}else if (form.getIdTipoInforme().equalsIgnoreCase(EnvioInformesGenericos.comunicacionesMorosos)){
					envioInformesGenericos.gestionarComunicacionMorosos(form,  request.getLocale(), userBean);
					isEnvioBatch = envioInformesGenericos.isEnvioBatch();
				}else if (form.getIdTipoInforme().equalsIgnoreCase(EnvioInformesGenericos.comunicacionesCenso)){
					envioInformesGenericos.gestionarComunicacionCenso(form,  request.getLocale(), userBean);
					form.setEditarEnvio("true");
					isEnvioBatch = envioInformesGenericos.isEnvioBatch();
				}else if (form.getIdTipoInforme().equalsIgnoreCase(EnvioInformesGenericos.comunicacionesFacturacionesColegiados)){
//					form.setClavesIteracion("idPersona");
					envioInformesGenericos.gestionarComunicacionFacturacionColegiados(form,  request.getLocale(), userBean);
					isEnvioBatch = envioInformesGenericos.isEnvioBatch();
				}else if (form.getIdTipoInforme().equalsIgnoreCase(EnvioInformesGenericos.comunicacionesPagoColegiados)){
					envioInformesGenericos.gestionarComunicacionPagoColegiados(form,  request.getLocale(), userBean);
					isEnvioBatch = envioInformesGenericos.isEnvioBatch();
				}else if (form.getIdTipoInforme().equalsIgnoreCase(EnvioInformesGenericos.comunicacionesFacturaRectificativa)){
					envioInformesGenericos.gestionarComunicacionFacturasRectificativas(form,  request.getLocale(), userBean);
					isEnvioBatch = envioInformesGenericos.isEnvioBatch();
				}else if (form.getIdTipoInforme().equalsIgnoreCase(EnvioInformesGenericos.comunicacionesJustificacion)){
					envioInformesGenericos.gestionarComunicacionJustificaciones(form,  request.getLocale(), userBean);
					isEnvioBatch = envioInformesGenericos.isEnvioBatch();
				}else if (form.getIdTipoInforme().equalsIgnoreCase(EnvioInformesGenericos.comunicacionesOrdenDomicializacion)){
					envioInformesGenericos.gestionarComunicacionOrdenDomiciliacion(form,  request.getLocale(), userBean);
					isEnvioBatch = envioInformesGenericos.isEnvioBatch();
				}else if (form.getIdTipoInforme().equalsIgnoreCase(EnvioInformesGenericos.comunicacionesAnexoOrdenDomiciliacion)){
					envioInformesGenericos.gestionarComunicacionAnexosOrdenDomiciliacion(form,  request.getLocale(),userBean);
					isEnvioBatch = envioInformesGenericos.isEnvioBatch();
				}else if (form.getIdTipoInforme().equalsIgnoreCase(EnvioInformesGenericos.comunicacionesExpedientes)){
					envioInformesGenericos.gestionarComunicacionExpedientes(form,  request.getLocale(), userBean);
					isEnvioBatch = envioInformesGenericos.isEnvioBatch();
				}else if (form.getIdTipoInforme().equalsIgnoreCase(EnvioInformesGenericos.comunicacionesListadoGuardias)){
					envioInformesGenericos.gestionarComunicacionListadoGuardias(form,  request.getLocale(), userBean);
					isEnvioBatch = envioInformesGenericos.isEnvioBatch();
				}else if (form.getIdTipoInforme().equalsIgnoreCase(EnvioInformesGenericos.comunicacionesIRPF)){
					envioInformesGenericos.gestionarComunicacionIRPF(form,  request.getLocale(), userBean);
					isEnvioBatch = envioInformesGenericos.isEnvioBatch();
				}else if (form.getIdTipoInforme().equalsIgnoreCase(EnvioInformesGenericos.comunicacionesAcreditacionDeOficio)){
					envioInformesGenericos.gestionarComunicacionAcreditacionOficio(form,  request.getLocale(), userBean);
					form.setEditarEnvio("true");
					isEnvioBatch = envioInformesGenericos.isEnvioBatch();
				}

				tx.commit();
				if(isEnvioBatch){
					SIGASvlProcesoAutomaticoRapido.NotificarAhora(SIGASvlProcesoAutomaticoRapido.procesoGeneracionEnvio);
					
					//Comunicaciones designaciones a juzgados sin codigo EJIS
					if(form.getIdTipoInforme().equalsIgnoreCase(EnvioInformesGenericos.comunicacionesDesigna) &&
						form.getIsCodigoEjis() != null && form.getIsCodigoEjis().equals("0")){
						//throw new ClsExceptions("Existe un registro en el cual no hay codigo EJIS");
						String[] datosEnvio = form.getDatosEnvios().split(",");
						request.setAttribute("mensajeDescarga", UtilidadesString.getMensajeIdioma(userBean.getLanguage(),"definirenvios.message.noadmitenviotelematico"));
						request.setAttribute("idsInforme",datosEnvio[2]+","+datosEnvio[3]);
						request.setAttribute("datosInforme",form.getDatosInforme());
						ClsLogging.writeFileLog("DefinirEnviosAction:fin insertarEnvioGenerico.descargarEnvioTelematico", 10);
						return "descargarEnvioTelematico";	
					
					}else{
						ClsLogging.writeFileLog("DefinirEnviosAction:fin insertarEnvioGenerico.Exito. IdInstitucion:" + userBean.getLocation(), 10);
						if(form.getMostrarPanel()==null)
							return exitoModal("messages.inserted.success",request);
						else {
							return exitoRefresco("messages.inserted.success",request);
							
						}
					}
				
				}else{
					
					
					if(form.getIdEnvio()!=null&&!form.getIdEnvio().equals("")){
						
						String accessEnvio = testAccess(request.getContextPath()+"/ENV_DefinirEnvios.do",null,request);
						if (!accessEnvio.equals(SIGAConstants.ACCESS_READ) && !accessEnvio.equals(SIGAConstants.ACCESS_FULL)) {
							testAccess(request.getContextPath()+mapping.getPath()+".do",null,request);
							ClsLogging.writeFileLog("Acceso denegado al modulo de envios, cerramos la seleccion de plantillas",request,3);
							if(form.getMostrarPanel()==null)
								return exitoModal("messages.inserted.success",request);
							else {
								return exitoRefresco("messages.inserted.success",request);
								
							}
						}else{
							testAccess(request.getContextPath()+mapping.getPath()+".do",null,request);
							Hashtable htEnvio = new Hashtable();
							htEnvio.put(EnvEnviosBean.C_IDTIPOENVIOS,form.getIdTipoEnvio());
							htEnvio.put(EnvEnviosBean.C_IDENVIO,form.getIdEnvio());
							htEnvio.put(EnvEnviosBean.C_DESCRIPCION,form.getNombre());
							request.getSession().removeAttribute("EnvEdicionEnvio");
							request.setAttribute("envio",htEnvio);
							ClsLogging.writeFileLog("DefinirEnviosAction:fin insertarEnvioGenerico.seleccionEnvio. IdInstitucion:" + userBean.getLocation(), 10);
							return "seleccionEnvio";
						}
						//hacemos lo siguiente para setear el permiso de esta accion
						
						
						
					}else{
						ClsLogging.writeFileLog("DefinirEnviosAction:fin insertarEnvioGenerico.errorNoDireccion. IdInstitucion:" + userBean.getLocation(), 10);
						if(form.getMostrarPanel()==null)
							return exitoModal("messages.envio.errorNoDireccion",request);
						else {
							return exitoRefresco("messages.inserted.success",request);
							
						}
						
					}
				}
		}
		catch (ClsExceptions e){
			this.throwExcp(e.getMessage(),new String[] {"modulo.envios"}, e, tx);
		}
		catch (Exception e){
			this.throwExcp("messages.general.error",new String[] {"modulo.envios"},e,tx);
		}
		ClsLogging.writeFileLog("DefinirEnviosAction:fin insertarEnvioGenerico.exception. IdInstitucion:" + userBean.getLocation(), 10);
		return "exception";
		
	} 

	
	protected String insertarEnvioModal (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException 
			{

		UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));   
		UserTransaction tx = userBean.getTransactionPesada();
		DefinirEnviosForm form = (DefinirEnviosForm)formulario;
		boolean tieneDireccion = true;
		String subModo = form.getSubModo();
		try {

			// obtener idInstitucion
			String idInstitucion = userBean.getLocation();
			// obtener nombre
			String nombreEnvio = form.getNombre();
			// obtener tipoEnvio
			String idTipoEnvio = form.getIdTipoEnvio();
			boolean isEnvioSMS = Integer.parseInt(idTipoEnvio)==EnvTipoEnviosAdm.K_BUROSMS || Integer.parseInt(idTipoEnvio)==EnvTipoEnviosAdm.K_SMS;
			String acuseRecibo = form.getAcuseRecibo();
			// obtener plantilla
			
			if(form.getIdPlantillaEnvios().equals("predefinida"))
				form.setIdPlantillaEnvios("");
			String idPlantilla = form.getIdPlantillaEnvios();
			//obtener plantilla de generacion
			String idPlantillaGeneracion = form.getIdPlantillaGeneracion();

			// obtener fechaProgramada
			String fechaProgramada = null;
			String fechaProg = form.getFechaProgramada();
			//fechaProg = GstDate.anyadeHora(fechaProg);
			if (fechaProg != null && fechaProg.length() == 10)
				fechaProg += " " + new Date().getHours() + ":" + new Date().getMinutes() + ":" + new Date().getSeconds();

			String language = userBean.getLanguage();
			String format = language.equalsIgnoreCase("EN")?ClsConstants.DATE_FORMAT_LONG_ENGLISH:ClsConstants.DATE_FORMAT_LONG_SPANISH;		    
			GstDate gstDate = new GstDate();
			if (fechaProg != null && !fechaProg.equals("")) {
				Date date = gstDate.parseStringToDate(fechaProg,format,request.getLocale());
				//A peticion de Luis pedro retraso 15 minutos el envio
				date.setTime(date.getTime()+900000);
				//date.
				SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
				fechaProgramada = sdf.format(date);
			} else
				fechaProgramada = null;

			// obtener idSolicitud
			String idSolicitud = form.getIdSolicitud();
			// obtener idFactura
			String idFactura = form.getIdFactura();
			//obtener idPersona
			String idPersona = form.getIdPersona();
			if (idPersona!=null && idPersona.trim().equals("")) {
				idPersona = null;
			}


			Hashtable hash = new Hashtable();
			CenSolicitudIncorporacionBean bean = null;
			UtilidadesHash.set(hash, CenSolicitudIncorporacionBean.C_IDSOLICITUD, idSolicitud);
			CenSolicitudIncorporacionAdm solicitudAdm = new CenSolicitudIncorporacionAdm (userBean);
			Vector datosSelect = solicitudAdm.selectByPK(hash);
			if ((datosSelect != null) && (datosSelect.size() > 0)) {
				bean = (CenSolicitudIncorporacionBean) datosSelect.get(0);
			}
			
			tx.begin();
			
			//mhg - INC_10323_SIGA
			if(idPersona==null){
				CenClienteAdm adminCli = new CenClienteAdm(userBean);
				CenClienteBean beanCli = new CenClienteBean ();
				beanCli = adminCli.insertarNoColegiadoParaEnvio(bean, null, request);
				idPersona = beanCli.getIdPersona().toString(); 
				
			}
			
			
			String idEnvio = form.getIdEnvio();
				if (idEnvio.equalsIgnoreCase("")){
					EnvEnviosAdm envAdm = new EnvEnviosAdm(this.getUserBean(request));
					idEnvio = String.valueOf(envAdm.getNewIdEnvio(idInstitucion));
				}
			
			
			


			//***** Creamos bean de envio *****
			EnvEnviosBean enviosBean = new EnvEnviosBean();
			GenParametrosAdm paramAdm = new GenParametrosAdm(this.getUserBean(request));

			enviosBean.setIdInstitucion(Integer.valueOf(idInstitucion));
			enviosBean.setAcuseRecibo(acuseRecibo);
			
			
			if(idEnvio!=null)
				enviosBean.setIdEnvio(Integer.valueOf(idEnvio));
			enviosBean.setDescripcion(nombreEnvio);
			enviosBean.setIdTipoEnvios(Integer.valueOf(idTipoEnvio));
			//mhg - Se saca fuera del if el setIdPlantillaEnvios ya que es un campo obligatorio en bbdd. Ese if es para controlar el idPlantillaGeneracion.
			enviosBean.setIdPlantillaEnvios(Integer.valueOf(idPlantilla));
			if(!isEnvioSMS){
				if (idPlantillaGeneracion!=null && !idPlantillaGeneracion.equals("")) {
					enviosBean.setIdPlantilla(Integer.valueOf(idPlantillaGeneracion));
				} else {
					enviosBean.setIdPlantilla(null);
				}
			}
			enviosBean.setFechaProgramada(fechaProgramada);
			enviosBean.setFechaCreacion("SYSDATE");
			enviosBean.setGenerarDocumento(paramAdm.getValor(idInstitucion,"ENV","GENERAR_DOCUMENTO_ENVIO","C"));
			enviosBean.setImprimirEtiquetas(paramAdm.getValor(idInstitucion,"ENV","IMPRIMIR_ETIQUETAS_ENVIO","1"));
			if (fechaProgramada==null || fechaProgramada.equals(""))
				enviosBean.setIdEstado(new Integer(EnvEnviosAdm.ESTADO_INICIAL));
			else{
				if(enviosBean.getIdTipoEnvios()!=null &&!enviosBean.getIdTipoEnvios().equals(EnvTipoEnviosAdm.K_CORREO_ELECTRONICO)&&!enviosBean.getIdTipoEnvios().equals(EnvTipoEnviosAdm.K_DOCUMENTACIONLETRADO))			
					enviosBean.setIdEstado(new Integer(EnvEnviosAdm.ESTADO_INICIAL));
				else
					enviosBean.setIdEstado(new Integer(EnvEnviosAdm.ESTADO_PENDIENTE_AUTOMATICO));
			}
			


			//El vector vDocs se rellenará en función del subModo.
			Vector vDocs = null;


			
			if (subModo!=null && subModo.equalsIgnoreCase("SolicitudCertificado")){
				CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(userBean);
				PysProductosInstitucionAdm admProd = new PysProductosInstitucionAdm(userBean);
				boolean existe = true;
				//System.out.println("paraconsejo="+request.getParameter("paraConsejo"));
				// control de direcciones
				
				Hashtable<String, Object> solicitudCertificadosPkHashtable = new Hashtable<String, Object>();
				solicitudCertificadosPkHashtable.put(CerSolicitudCertificadosBean.C_IDINSTITUCION, idInstitucion);
				solicitudCertificadosPkHashtable.put(CerSolicitudCertificadosBean.C_IDSOLICITUD, idSolicitud);

				Vector solicitudCertificadoPKVector = admSolicitud.selectByPK(solicitudCertificadosPkHashtable);
				if (solicitudCertificadoPKVector == null || solicitudCertificadoPKVector.size() < 1)
					throw new SIGAException("No se ha encontrado la solicitud");
				CerSolicitudCertificadosBean solicitudCertificadoBean = (CerSolicitudCertificadosBean) solicitudCertificadoPKVector.get(0);

				Hashtable<String, Object> productoInstitucionPKHashtable = new Hashtable<String, Object>();
				productoInstitucionPKHashtable.put(PysProductosInstitucionBean.C_IDINSTITUCION, solicitudCertificadoBean.getIdInstitucion());
				productoInstitucionPKHashtable.put(PysProductosInstitucionBean.C_IDTIPOPRODUCTO, solicitudCertificadoBean.getPpn_IdTipoProducto());
				productoInstitucionPKHashtable.put(PysProductosInstitucionBean.C_IDPRODUCTO, solicitudCertificadoBean.getPpn_IdProducto());
				productoInstitucionPKHashtable.put(PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION, solicitudCertificadoBean.getPpn_IdProductoInstitucion());

				Vector productoInstitucionPKVector = admProd.selectByPK(productoInstitucionPKHashtable);
				if (productoInstitucionPKVector == null || productoInstitucionPKVector.size() < 1){
					throw new SIGAException("No se ha encontrado el producto");
				}
				PysProductosInstitucionBean beanProd = (PysProductosInstitucionBean) productoInstitucionPKVector.get(0);

				if (beanProd.getTipoCertificado().equalsIgnoreCase("D"))  {
					// DILIGENCIA (CGAE)
					// en caso de uno se pregunta, si no se pone directamente la institucion de cgae
					if (request.getParameter("paraConsejo")==null) {
						// Proceso normal
						// Cuando es un consejo o cgae comprobamos si existe la direccion en el colegio origen. 
						// Si no existe enviamos un mensaje de confirmacion
						boolean esDeConsejo=admSolicitud.esConsejo(idInstitucion);
						if (esDeConsejo) {
							existe = admSolicitud.existePersonaCertificado(idInstitucion, idSolicitud);
						}
						if (!existe) {

							// Si no existe lo tenemos que buscar en CGAE en lugar de en Origen.
							// Y si no existe tammbien en CGAE (Esta pregunta solamente avisa)
							tx.rollback();

							request.setAttribute("PREG_idTipoEnvio",form.getIdTipoEnvio());
							request.setAttribute("PREG_idSolicitud",form.getIdSolicitud());
							request.setAttribute("PREG_idFactura",form.getIdFactura());
							request.setAttribute("PREG_idPersona",form.getIdPersona());
							request.setAttribute("PREG_idEnvio",form.getIdEnvio());
							request.setAttribute("PREG_idTipoEnvio",form.getIdTipoEnvio());
							request.setAttribute("PREG_idTipoEnvio",form.getIdTipoEnvio());
							request.setAttribute("PREG_nombreEnvio",nombreEnvio);
							request.setAttribute("PREG_idPlantilla",idPlantilla);
							request.setAttribute("PREG_idPlantillaGeneracion",idPlantillaGeneracion);
							request.setAttribute("PREG_fechaProgramada",form.getFechaProgramada());
							request.setAttribute("PREG_existe",new Boolean(existe).toString());
							request.setAttribute("PREG_subModo",subModo);

							return "preguntaDireccionEnvio";
						}
					} else {
						//existe = new Boolean(request.getParameter("PREG_existe")).booleanValue();
						// RGG Nos da igual la respuesta, se coge siempre la de CGAE
						existe = false;
					}
				} else {
					// si es certificado o comunicacion no se pregunta.
					// existe = admCer.existePersonaCertificado(idInstitucion, idSolicitud);
					// RGG me da igual que exista o no, se coge siempre la de CGAE
					existe = false;
				}

					
				
				String rutaFichero = admSolicitud.getRutaCertificadoFichero(solicitudCertificadoBean,admSolicitud.getRutaCertificadoDirectorioBD(solicitudCertificadoBean.getIdInstitucion()));
				Documento certificadoDoc = new Documento(rutaFichero,admSolicitud.getNombreFicheroSalida(solicitudCertificadoBean));
				if(vDocs==null)
					vDocs = new Vector();
				vDocs.add(certificadoDoc);
//				vDocs = tratarSolicitudCertificado(idInstitucion,idSolicitud,this.getUserBean(request),subModo);
				Envio envio = new Envio(enviosBean,userBean);
				tieneDireccion = envio.generarEnvioCertificado(idPersona,vDocs,idSolicitud,enviosBean,existe);	

				// Enviado. Ahora ponemos la fecha de envio
				solicitudCertificadoBean.setFechaEnvio("SYSDATE");
				if (!admSolicitud.updateDirect(solicitudCertificadoBean)) {
					throw new ClsExceptions("No se ha podido actualizar la fecha de envío. Error: "+admSolicitud.getError());
				}


			} else if (subModo!=null && subModo.equalsIgnoreCase("envioFactura")){
				vDocs = this.tratarFactura(request, idInstitucion,idFactura,this.getUserBean(request),subModo);
				//Generamos el envío del certificado:
				//Para idinstitucion=CGAE(2000) se realiza el commit aunque no tenga direccion:
				Envio envio = new Envio(enviosBean,userBean);
				tieneDireccion = envio.generarEnvioFactura(idPersona,vDocs,idFactura);
				
				Documento doc = (Documento) vDocs.get(0); 
				File fichero = doc.getDocumento();
				File directorio = fichero.getParentFile();
				fichero.delete(); // Se borra el pdf firmado
				if (directorio!=null && directorio.isDirectory() && directorio.list().length==0) {
	    			directorio.delete(); // borra el directorio de las firmas
				}	
				
				
			} else if (subModo!=null && subModo.equalsIgnoreCase("solicitudIncorporacion")){
				EnvDestinatariosBean dest = tratarSolicitudIncorporacion(idSolicitud,this.getUserBean(request), new Integer(idInstitucion),idTipoEnvio);

				// RGG 30-09-2005 Compruebo si es cliente
				//CenClienteAdm cliAdm = new CenClienteAdm(this.getUserName(request));
				//if (cliAdm.existeCliente(dest.getIdPersona(),dest.getIdInstitucion())==null) {
				//	throw new SIGAException ("messages.envios.error.noCliente"); 
				//}

				dest.setIdEnvio(Integer.valueOf(idEnvio));
				//Generamos el envío de la solicitud de incorporacion:
				Envio envio = new Envio(enviosBean,userBean);
				envio.generarEnvio(idPersona, EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA, vDocs,null,null);
				
				//mhg Se comenta la linea anterior porque en el método generarEnvio ya se esta llamando.
				//envio.addDestinatarioIndividualDocAdjuntos(dest,null,true);
				
				// RGG envio.addDestinatarioIndividual(dest,null,true);
			}else if (subModo!=null && subModo.equalsIgnoreCase("abono")){
				
				vDocs = new Vector(); 
				// Obtiene del campo idInforme los ids separados por ## y devuelve sus beans
				InformeAbono informeAbono = new InformeAbono(userBean);
				AdmInformeAdm adm = new AdmInformeAdm(userBean);
				// mostramos la ventana con la pregunta
				Vector plantillas=adm.obtenerInformesTipo(idInstitucion,"ABONO",null, null);
				
				// MODELO DE TIPO FOP: LLAMADA A MASTER REPORT
				for (int i=0;i<plantillas.size();i++) {
					AdmInformeBean b = (AdmInformeBean) plantillas.get(i); 
					String idAbono = idSolicitud;
					
					File fileAbono = informeAbono.generarAbono(request,userBean.getLanguage(),userBean.getLocation(),idAbono,b.getIdPlantilla()); 
					Documento doc = new Documento(fileAbono.getPath(),fileAbono.getName().substring(0,fileAbono.getName().indexOf(".")));
					vDocs.add(doc);
					
				}
				
				
				//vDocs = tratarFactura(request, idInstitucion,idFactura,this.getUserBean(request),subModo);
				//Generamos el envío del certificado:
				//Para idinstitucion=CGAE(2000) se realiza el commit aunque no tenga direccion:
				Envio envio = new Envio(enviosBean,userBean);
				tieneDireccion = envio.generarEnvioFactura(idPersona,vDocs,idFactura);	
			} 
			else {
				//Generamos el envío
				Envio envio = new Envio(enviosBean,userBean);
				envio.generarEnvio(idPersona, EnvDestinatariosBean.TIPODESTINATARIO_CENPERSONA,null,null,null);
			}

			if (!tieneDireccion) {
				throw new SIGAException("messages.envio.errorNoDireccion");
			}

			if(idEnvio!=null&&!idEnvio.equals("")){
				Hashtable htEnvio = new Hashtable();
				htEnvio.put(EnvEnviosBean.C_IDTIPOENVIOS,idTipoEnvio);
				htEnvio.put(EnvEnviosBean.C_IDENVIO,idEnvio);
				htEnvio.put(EnvEnviosBean.C_DESCRIPCION,nombreEnvio);			
				request.setAttribute("envio",htEnvio);
			}

			tx.commit();
			
			
		}
		catch (ClsExceptions e){
			this.throwExcp(e.getMessage(),new String[] {"modulo.envios"}, e, tx);
		}
		catch (Exception e){
			this.throwExcp("messages.general.error",new String[] {"modulo.envios"},e,tx);
		}

		if (form.getEditarEnvio()!=null && form.getEditarEnvio().equalsIgnoreCase("true")){
			// JTA Borramos el paginador Paginador ya que es el mismo que el de envios 
			// Asi evitamos ClassCastException
			if(subModo!=null && (subModo.equalsIgnoreCase("certificadoIRPF")||subModo.equalsIgnoreCase("pagoColegiados")))
				borrarPaginador(request,this.paginador);
			ClsLogging.writeFileLog("DefinirEnviosAction:fin insertarEnvioModal.seleccionEnvio. IdInstitucion:" + userBean.getLocation(), 10);
			return "seleccionEnvio";
		} else {
			ClsLogging.writeFileLog("DefinirEnviosAction:fin insertarEnvioModal.exito. IdInstitucion:" + userBean.getLocation(), 10);
			return exitoModal("messages.inserted.success",request);
		}
	} 
	/**
	 * Metodo que implementa el modo enviarPersona
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String insertarEnvioModalCertificado(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		UsrBean userBean = ((UsrBean) request.getSession().getAttribute(("USRBEAN")));
		UserTransaction tx = userBean.getTransactionPesada();
		DefinirEnviosForm form = (DefinirEnviosForm) formulario;
		String mensaje = "";
		try {

			// obtener tipoEnvio
			String idTipoEnvio = form.getIdTipoEnvio();
			// obtener plantilla
			String idPlantilla = form.getIdPlantillaEnvios();
			// obtener plantilla de generacion
			String idPlantillaGeneracion = form.getIdPlantillaGeneracion();
			String acuseRecibo = form.getAcuseRecibo();
			// Obtenemos el coegio destino

			// obtener fechaProgramada
			String fechaProgramada = null;
			String fechaProg = form.getFechaProgramada();
			// fechaProg = GstDate.anyadeHora(fechaProg);
			if (fechaProg != null && fechaProg.length() == 10)
				fechaProg += " " + new Date().getHours() + ":" + new Date().getMinutes() + ":" + new Date().getSeconds();

			String language = userBean.getLanguage();
			String format = language.equalsIgnoreCase("1") ? ClsConstants.DATE_FORMAT_LONG_ENGLISH : ClsConstants.DATE_FORMAT_LONG_SPANISH;
			GstDate gstDate = new GstDate();
			if (fechaProg != null && !fechaProg.equals("")) {
				Date date = gstDate.parseStringToDate(fechaProg, format, request.getLocale());
				SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
				fechaProgramada = sdf.format(date);
			} else {
				fechaProgramada = null;
			}

			// Obtenemos el certificado
			CerSolicitudCertificadosAdm admCer = new CerSolicitudCertificadosAdm(userBean);

			CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(userBean);
			PysProductosInstitucionAdm admProd = new PysProductosInstitucionAdm(userBean);
			CenInstitucionAdm admInst = new CenInstitucionAdm(userBean);
			EnvEnviosAdm enviosAdm =  new EnvEnviosAdm(userBean);

			HashMap<Long, List<CerSolicitudCertificadosBean>> hashCertificadosPorDestinatario = new HashMap<Long, List<CerSolicitudCertificadosBean>>();
			List<CerSolicitudCertificadosBean> cerSolicitudCertificadosBeans = null;
			StringBuilder errores = new StringBuilder("");
			String[] lineas = form.getIdsParaEnviar().split(";");
			for (int i = 0; i < lineas.length; i++) {
				StringBuilder datosSolicitudError = null;
				try {
					String[] campos = lineas[i].split("%%");
					if (campos.length > 1) {
						String idInstitucion = campos[6];

						String idSolicitud = campos[1];

						Hashtable<String, Object> solicitudCertificadosPkHashtable = new Hashtable<String, Object>();
						solicitudCertificadosPkHashtable.put(CerSolicitudCertificadosBean.C_IDINSTITUCION, idInstitucion);
						solicitudCertificadosPkHashtable.put(CerSolicitudCertificadosBean.C_IDSOLICITUD, idSolicitud);

						Vector solicitudCertificadoPKVector = admSolicitud.selectByPK(solicitudCertificadosPkHashtable);
						if (solicitudCertificadoPKVector == null || solicitudCertificadoPKVector.size() < 1)
							throw new SIGAException("No se ha encontrado la solicitud");
						CerSolicitudCertificadosBean solicitudCertificadoBean = (CerSolicitudCertificadosBean) solicitudCertificadoPKVector.get(0);

						Hashtable<String, Object> productoInstitucionPKHashtable = new Hashtable<String, Object>();
						productoInstitucionPKHashtable.put(PysProductosInstitucionBean.C_IDINSTITUCION, solicitudCertificadoBean.getIdInstitucion());
						productoInstitucionPKHashtable.put(PysProductosInstitucionBean.C_IDTIPOPRODUCTO, solicitudCertificadoBean.getPpn_IdTipoProducto());
						productoInstitucionPKHashtable.put(PysProductosInstitucionBean.C_IDPRODUCTO, solicitudCertificadoBean.getPpn_IdProducto());
						productoInstitucionPKHashtable.put(PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION, solicitudCertificadoBean.getPpn_IdProductoInstitucion());

						Vector productoInstitucionPKVector = admProd.selectByPK(productoInstitucionPKHashtable);
						if (productoInstitucionPKVector == null || productoInstitucionPKVector.size() < 1){
							datosSolicitudError = new StringBuilder();
							datosSolicitudError.append("[Institucion:");
							datosSolicitudError.append(idInstitucion);
							datosSolicitudError.append(", solicitud:");
							datosSolicitudError.append(idSolicitud);
							datosSolicitudError.append("],");
							errores.append(datosSolicitudError);
							throw new SIGAException("No se ha encontrado el producto");
						}
						PysProductosInstitucionBean beanProd = (PysProductosInstitucionBean) productoInstitucionPKVector.get(0);
						// Seteamos al descripcion del producto para el nombre
						// del envio
						solicitudCertificadoBean.setProductoDescripcion(beanProd.getDescripcion());
						// decidimos sobre cual es la institucion para enviar
						Integer idInstitucionDestinatario = null;
						
						if (solicitudCertificadoBean.getIdInstitucionOrigen() == null) {
							// Es certificado y no han definido colegio
							// presentador
							datosSolicitudError = new StringBuilder();
							datosSolicitudError.append("[Institucion:");
							datosSolicitudError.append(idInstitucion);
							datosSolicitudError.append(", solicitud:");
							datosSolicitudError.append(idSolicitud);
							datosSolicitudError.append("],");
							errores.append(datosSolicitudError);
							throw new SIGAException("Es un certificado y no se ha definido colegio presentador (No se envía).");
						} else {
							// destinatario el colegio origen / PRESENTADOR
							idInstitucionDestinatario = solicitudCertificadoBean.getIdInstitucionOrigen();
						}
						

						// obtenemos el nombre del colegio
						Hashtable<String, Object> institucionPKHashtable = new Hashtable<String, Object>();
						institucionPKHashtable.put(CenInstitucionBean.C_IDINSTITUCION, idInstitucionDestinatario);

						// Obtenemos la persona de la isntitucion
						Vector institucionPKVector = admInst.selectByPK(institucionPKHashtable);
						if (institucionPKVector == null || institucionPKVector.size() < 1){
							datosSolicitudError = new StringBuilder();
							datosSolicitudError.append("[Institucion:");
							datosSolicitudError.append(idInstitucion);
							datosSolicitudError.append(", solicitud:");
							datosSolicitudError.append(idSolicitud);
							datosSolicitudError.append("],");
							errores.append(datosSolicitudError);
							throw new SIGAException("No se ha encontrado la institucion destinataria");
						}

						CenInstitucionBean beanInst = (CenInstitucionBean) institucionPKVector.get(0);
						// Seteamos al abreviatura de la institucino para el
						// nombre del envio
						solicitudCertificadoBean.setInstitucionAbreviatura(beanInst.getAbreviatura());

						String rutaFichero = admSolicitud.getRutaCertificadoFichero(solicitudCertificadoBean,admSolicitud.getRutaCertificadoDirectorioBD(solicitudCertificadoBean.getIdInstitucion()));
						Documento certificadoDoc = new Documento(rutaFichero,admSolicitud.getNombreFicheroSalida(solicitudCertificadoBean));
						
						 
						solicitudCertificadoBean.setCertificado(certificadoDoc);

						if (certificadoDoc.getDocumento() == null || !certificadoDoc.getDocumento().exists()) {
							datosSolicitudError = new StringBuilder();
							datosSolicitudError.append("[Institucion:");
							datosSolicitudError.append(idInstitucion);
							datosSolicitudError.append(", solicitud:");
							datosSolicitudError.append(idSolicitud);
							datosSolicitudError.append("],");
							errores.append(datosSolicitudError);
							throw new SIGAException("messages.general.error.ficheroNoExiste");
						}

						if (hashCertificadosPorDestinatario.containsKey(beanInst.getIdPersona())) {
							cerSolicitudCertificadosBeans = hashCertificadosPorDestinatario.get(beanInst.getIdPersona());
						} else {
							cerSolicitudCertificadosBeans = new ArrayList<CerSolicitudCertificadosBean>();
						}
						cerSolicitudCertificadosBeans.add(solicitudCertificadoBean);
						hashCertificadosPorDestinatario.put(beanInst.getIdPersona(), cerSolicitudCertificadosBeans);
					}

				} catch (SIGAException e) {
					ClsLogging.writeFileLog("Error descarga Certificado" + datosSolicitudError.toString() + " Error: " + e.getLiteral(userBean.getLanguage()), 3);
				}
			}
			
			EnvEnviosBean enviosBean = null;

			Iterator<Long> iteratorDestinatarios = hashCertificadosPorDestinatario.keySet().iterator();
			while (iteratorDestinatarios.hasNext()) {
				StringBuilder datosSolicitudError = null;
				try {
					Long keyPersonaDestinataria = (Long) iteratorDestinatarios.next();

				List<CerSolicitudCertificadosBean> solicitudCertificadosBeans = (List<CerSolicitudCertificadosBean>) hashCertificadosPorDestinatario.get(keyPersonaDestinataria);
				Vector<Documento> certificadosVector = new Vector<Documento>();
				for (int i = 0; i < solicitudCertificadosBeans.size(); i++) {
					CerSolicitudCertificadosBean cerSolicitudCertificadosBean = solicitudCertificadosBeans.get(i);
					if(i==0){
						enviosBean = new EnvEnviosBean();
						enviosBean.setAcuseRecibo(acuseRecibo);
						enviosBean.setIdInstitucion(cerSolicitudCertificadosBean.getIdInstitucion());
						enviosBean.setGenerarDocumento("C");
						enviosBean.setImprimirEtiquetas("1");
						enviosBean.setIdEnvio(enviosAdm.getNewIdEnvio(userBean.getLocation()));
						String nombreEnvio = "Certificados " + cerSolicitudCertificadosBean.getInstitucionAbreviatura();
						// trunco la descripción
						if (nombreEnvio.length() > 200)
							nombreEnvio = nombreEnvio.substring(0, 99);
	
						enviosBean.setDescripcion(nombreEnvio);
						enviosBean.setIdTipoEnvios(Integer.valueOf(idTipoEnvio));
						enviosBean.setIdPlantillaEnvios(Integer.valueOf(idPlantilla));
						if (idPlantillaGeneracion != null && !idPlantillaGeneracion.equals("")) {
							enviosBean.setIdPlantilla(Integer.valueOf(idPlantillaGeneracion));
						} else {
							enviosBean.setIdPlantilla(null);
						}
						enviosBean.setFechaProgramada(fechaProgramada);
						enviosBean.setFechaCreacion("SYSDATE");
						if (fechaProgramada == null || fechaProgramada.equals(""))
							enviosBean.setIdEstado(new Integer(EnvEnviosAdm.ESTADO_INICIAL));
						else {
							if (enviosBean.getIdTipoEnvios() != null && !enviosBean.getIdTipoEnvios().equals(EnvTipoEnviosAdm.K_CORREO_ELECTRONICO)&& !enviosBean.getIdTipoEnvios().equals(EnvTipoEnviosAdm.K_DOCUMENTACIONLETRADO))
								enviosBean.setIdEstado(new Integer(EnvEnviosAdm.ESTADO_INICIAL));
							else
								enviosBean.setIdEstado(new Integer(EnvEnviosAdm.ESTADO_PENDIENTE_AUTOMATICO));
						}

						
	
					}
					certificadosVector.add(cerSolicitudCertificadosBean.getCertificado());
					
				}
				Envio envio = new Envio(enviosBean, userBean);
				boolean isEnvioGenerado = envio.generarEnvioCertificado(keyPersonaDestinataria.toString(),	certificadosVector, enviosBean);
				if (!isEnvioGenerado) {
					datosSolicitudError = new StringBuilder();
					datosSolicitudError.append(enviosBean.getDescripcion());
					errores.append(datosSolicitudError);
					throw new SIGAException("El envio no se ha generado , probablemente porque el destinatario no tiene correctamente la direccion de envio" + enviosBean.getDescripcion());
				}else{
					solicitudCertificadosBeans = (List<CerSolicitudCertificadosBean>) hashCertificadosPorDestinatario.get(keyPersonaDestinataria);
					for (CerSolicitudCertificadosBean cerSolicitudCertificadosBean  : solicitudCertificadosBeans) {
						cerSolicitudCertificadosBean.setFechaEnvio("SYSDATE");
						if (!admCer.updateDirect(cerSolicitudCertificadosBean)) {
							throw new ClsExceptions("No se ha podido actualizar la fecha de envío. envio: " + cerSolicitudCertificadosBean.getIdSolicitud() + " Error: " + admCer.getError());
						}		
					}
					
				}
				
				// Enviado. Ahora ponemos la fecha de envio
				
				} catch (SIGAException e) {
					ClsLogging.writeFileLog("Error envio Certificado" + datosSolicitudError.toString() + " Error: " + e.getLiteral(userBean.getLanguage()), 3);
				}
				

			}
			if (errores.toString().equals("")) {
				mensaje = "messages.inserted.success";
			} else {
				mensaje = UtilidadesString.getMensaje("messages.envios.envioCertificadosMasivo.success", new String[] {errores.toString() }, userBean.getLanguage());
			}

		} catch (Exception e) {
			this.throwExcp("messages.general.error", new String[] { "modulo.envios" }, e, tx);
		}
		ClsLogging.writeFileLog("DefinirEnviosAction:fin insertarEnvioModalCertificado. IdInstitucion:" + userBean.getLocation(), 10);
		return exitoModal(mensaje, request);
	}

	private EnvDestinatariosBean tratarSolicitudIncorporacion(String idSolicitud, UsrBean userBean, Integer idInstitucion, String idTipoEnvio)
	throws ClsExceptions, SIGAException {

		CenPersonaAdm admPer = new CenPersonaAdm(userBean);
		CenClienteAdm admCli = new CenClienteAdm(userBean);

		// obtenemos la solicitud
		CenSolicitudIncorporacionAdm admSolic = new CenSolicitudIncorporacionAdm(userBean);
		Hashtable hashSolic = new Hashtable();
		hashSolic.put(CenSolicitudIncorporacionBean.C_IDSOLICITUD,idSolicitud);
		Vector aux = admSolic.selectByPK(hashSolic);
		CenSolicitudIncorporacionBean beanSolic = null;
		if (aux!=null && aux.size()>0) {
			beanSolic = (CenSolicitudIncorporacionBean) aux.get(0);
		}

		CenPersonaBean beanPer = null;
		Long idPersona = null;

		try {
			beanPer = admPer.getPersonaSolicitud(beanSolic.getNumeroIdentificador(), beanSolic.getNombre(), beanSolic.getApellido1(), beanSolic.getApellido2());
			if (beanPer==null) {
				throw new SIGAException("NO PERSONA");
			}
			idPersona = beanPer.getIdPersona();
			Hashtable hashCli = new Hashtable();
			hashCli.put(CenClienteBean.C_IDPERSONA,beanPer.getIdPersona());
			hashCli.put(CenClienteBean.C_IDINSTITUCION,idInstitucion);
			Vector aux2 = admCli.selectByPK(hashCli);
			if (aux2==null || aux2.size()==0) {
				// no existe el cliente
				idPersona = null;
			}
		} catch (SIGAException e) {
			// no existe la persona
		}

		EnvDestinatariosBean salida = new EnvDestinatariosBean();

		if (beanPer!=null) {
			salida.setIdPersona(beanPer.getIdPersona());
			salida.setApellidos1(beanSolic.getApellido1());
			salida.setApellidos2(beanSolic.getApellido2());
			salida.setNombre(beanSolic.getNombre());
			salida.setNifcif(beanSolic.getNumeroIdentificador());
			salida.setIdInstitucion(beanSolic.getIdInstitucion());

			// la persona existe , entonces cogemos su direccion preferente
			EnvEnviosAdm enviosAdm = new EnvEnviosAdm(userBean);
			Vector direcciones = null;
			try {
				direcciones = enviosAdm.getDirecciones(idInstitucion.toString(),idPersona.toString(),idTipoEnvio);
				if (direcciones!=null && direcciones.size()>0) {
					Hashtable hashDir = (Hashtable) direcciones.get(0); 
					if (hashDir!=null) {
						salida.setCodigoPostal((String)hashDir.get(CenDireccionesBean.C_CODIGOPOSTAL));
						salida.setCorreoElectronico((String)hashDir.get(CenDireccionesBean.C_CORREOELECTRONICO));
						salida.setDomicilio((String)hashDir.get(CenDireccionesBean.C_DOMICILIO));
						salida.setFax1((String)hashDir.get(CenDireccionesBean.C_FAX1));
						salida.setFax2((String)hashDir.get(CenDireccionesBean.C_FAX2));
						salida.setIdPais((String)hashDir.get(CenDireccionesBean.C_IDPAIS));
						if (salida.getIdPais().equals("")) salida.setIdPais(ClsConstants.ID_PAIS_ESPANA); 
						salida.setIdPoblacion((String)hashDir.get(CenDireccionesBean.C_IDPOBLACION));
						salida.setPoblacionExtranjera((String)hashDir.get(CenDireccionesBean.C_POBLACIONEXTRANJERA));
						salida.setIdProvincia((String)hashDir.get(CenDireccionesBean.C_IDPROVINCIA));
						salida.setMovil((String)hashDir.get(CenDireccionesBean.C_MOVIL));
					} else {

					}
				} else {

				}
			} 
			catch (Exception e) { }
		} 
		else {
			salida.setIdPersona(CenPersonaAdm.K_PERSONA_GENERICA);
			salida.setApellidos1(beanSolic.getApellido1());
			salida.setApellidos2(beanSolic.getApellido2());
			salida.setNombre(beanSolic.getNombre());
			salida.setNifcif(beanSolic.getNumeroIdentificador());
			salida.setCodigoPostal(beanSolic.getCodigoPostal());
			salida.setCorreoElectronico(beanSolic.getCorreoElectronico());
			salida.setDomicilio(beanSolic.getDomicilio());
			salida.setFax1(beanSolic.getFax1());
			salida.setFax2(beanSolic.getFax2());
			salida.setIdPais(beanSolic.getIdPais());
			if (salida.getIdPais().equals("")) 
				salida.setIdPais(ClsConstants.ID_PAIS_ESPANA);

			salida.setIdPoblacion(beanSolic.getIdPoblacion());
			salida.setMovil(beanSolic.getMovil());
			salida.setPoblacionExtranjera(beanSolic.getPoblacionExtranjera());
			salida.setIdProvincia(beanSolic.getIdProvincia());
			salida.setIdInstitucion(beanSolic.getIdInstitucion());
		}
		ClsLogging.writeFileLog("DefinirEnviosAction:fin tratarSolicitudIncorporacion. IdInstitucion:" + userBean.getLocation(), 10);
		return salida;
	}

//	private Vector tratarSolicitudCertificado(String idInstitucion,String idSolicitud, UsrBean userBean, String desc) throws ClsExceptions 
//	{
//		//obtenemos la ruta al documento a partir del bean de solicitud
//		Hashtable htPk = new Hashtable();
//		htPk.put(CerSolicitudCertificadosBean.C_IDINSTITUCION,idInstitucion);
//		htPk.put(CerSolicitudCertificadosBean.C_IDSOLICITUD,idSolicitud);
//		CerSolicitudCertificadosAdm solAdm = new CerSolicitudCertificadosAdm(userBean);
//		CerSolicitudCertificadosBean solBean = null;
//		solBean = (CerSolicitudCertificadosBean)solAdm.selectByPK(htPk).firstElement();        
//		String rutaDir = solAdm.getRutaCertificadoDirectorioBD(solBean.getIdInstitucion());
//		String rutaFichero = solAdm.getRutaCertificadoFichero(solBean,rutaDir);
//
//		//Generamos el vector de documentos
//		Documento doc = new Documento(rutaFichero,desc);
//		Vector vDocs = new Vector();
//		vDocs.add(doc);
//		ClsLogging.writeFileLog("DefinirEnviosAction:fin tratarSolicitudCertificado. IdInstitucion:" + userBean.getLocation(), 10);
//		return vDocs;
//	}

	private Vector tratarFactura(HttpServletRequest request, String idInstitucion,String idFactura, UsrBean userBean, String desc) throws ClsExceptions, SIGAException {

		//obtenemos la ruta al documento a partir del bean de solicitud
		Hashtable<String,Object> hFacFactura = new Hashtable<String,Object>();
		hFacFactura.put(FacFacturaBean.C_IDINSTITUCION, idInstitucion);
		hFacFactura.put(FacFacturaBean.C_IDFACTURA, idFactura);

		InformeFactura infFactura = new InformeFactura(userBean);
		File fichero = infFactura.generarPdfFacturaFirmada(request, hFacFactura);

		//Generamos el vector de documentos 
		Documento doc = new Documento(fichero, desc);
		Vector vDocs = new Vector();
		vDocs.add(doc);		

		ClsLogging.writeFileLog("DefinirEnviosAction:fin tratarFactura. IdInstitucion:" + userBean.getLocation(), 10);
		return vDocs;
	}

	protected String procesarEnvio(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) 
	throws SIGAException{
		UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
		UserTransaction tx = null;
		EnvEnviosAdm envAdm = new EnvEnviosAdm(this.getUserBean(request));
		Integer idEstadoEnvioFinal = null;
		try {

			
//			tx = userBean.getTransaction();

			// Obtengo las claves
			DefinirEnviosForm form = (DefinirEnviosForm)formulario;
			String idInstitucion = userBean.getLocation();	
			String idEnvio = form.getIdEnvio();
			String idTipoEnvio = form.getIdTipoEnvio();
			
			
			EnvEnviosAdm envioAdm = new EnvEnviosAdm(this.getUserBean(request));
			Hashtable htPk = new Hashtable();
			htPk.put(EnvEnviosBean.C_IDINSTITUCION,idInstitucion);
			htPk.put(EnvEnviosBean.C_IDENVIO,idEnvio);
			//obtengo el envio
			EnvEnviosBean envBean = (EnvEnviosBean)envAdm.selectByPKForUpdate(htPk).elementAt(0);
			String idEstadoEnvio = envBean.getIdEstado().toString();
			
			if(envBean.getIdEstado().compareTo(EnvEstadoEnvioAdm.K_ESTADOENVIO_PROCESANDO)==0)
				throw new SIGAException("messages.envios.procesandoEnvio");
			
	
				
				
				if(envBean.getIdEstado().compareTo(EnvEstadoEnvioAdm.K_ESTADOENVIO_PROCESANDO)==0){
					throw new SIGAException("messages.envios.procesandoEnvio");
					
				}else{
					envBean.setIdEstado(EnvEstadoEnvioAdm.K_ESTADOENVIO_PROCESANDO);
	                envAdm.updateDirect(envBean);
	                
					
				}
				
				Envio envio = new Envio(envBean, userBean);
				// lo proceso
				envio.procesarEnvio();
				idEstadoEnvioFinal = envBean.getIdEstado();
			

		}catch (Exception e){
			this.throwExcp("messages.general.error",new String[] {"modulo.envios"},e,tx);
		}
		ClsLogging.writeFileLog("DefinirEnviosAction:fin procesarEnvio. IdInstitucion:" + userBean.getLocation(), 10);
		if(idEstadoEnvioFinal!=null && idEstadoEnvioFinal.compareTo(Integer.valueOf(EnvEstadoEnvioAdm.K_ESTADOENVIO_PROCESADOCONERRORES))==0)
			return exitoRefresco("messages.envio.successErrores",request);
		else
			return exitoRefresco("messages.envio.success",request);
	}

	protected String enviardenuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UsrBean userBean = ((UsrBean) request.getSession().getAttribute(("USRBEAN")));
		EnvEnviosAdm envAdm = new EnvEnviosAdm(this.getUserBean(request));
		Integer idEstadoEnvioFinal = null;
		try {

			DefinirEnviosForm form = (DefinirEnviosForm) formulario;
			String idInstitucion = userBean.getLocation();
			String idEnvio = form.getIdEnvio();
			Integer newIdEnvio = envAdm.enviarDeNuevo(Integer.valueOf(idEnvio),Short.valueOf(idInstitucion)) ;

		} catch (Exception e) {
			this.throwExcp("messages.general.error", new String[] { "modulo.envios" }, e, null);
		}
		
		return exitoRefresco("Se ha copiado correctamente el envio. Si necesita realizar algun cambio edite el envio y modifíquelo. Cuando haya terminado pulse el botón enviar para procesar el envio.",request);
		
	}

	protected String reenviar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UsrBean userBean = ((UsrBean) request.getSession().getAttribute(("USRBEAN")));
		EnvEnviosAdm envAdm = new EnvEnviosAdm(this.getUserBean(request));
		Integer idEstadoEnvioFinal = null;
		try {

			DefinirEnviosForm form = (DefinirEnviosForm) formulario;
			String idInstitucion = userBean.getLocation();
			String idEnvio = form.getIdEnvio();
			Integer newIdEnvio =  envAdm.reenviar(Integer.valueOf(idEnvio),Short.valueOf(idInstitucion)) ;
			
			Hashtable htParametros=new Hashtable();
			htParametros.put("idEnvio",newIdEnvio);
			htParametros.put("idInstitucion",form.getIdInstitucion()!=null?form.getIdInstitucion():userBean.getLocation());
			htParametros.put("idTipoEnvio",form.getTipoEnvio());
			htParametros.put("acceso","editar");
			htParametros.put("idIntercambio","");
			htParametros.put("elementoActivo","6");
			request.setAttribute("envio", htParametros);    
			request.setAttribute("buscarEnvios","true");
			request.setAttribute("nuevo", "false");
			

		} catch (Exception e) {
			this.throwExcp("messages.general.error", new String[] { "modulo.envios" }, e, null);
		}
		return "destinatariosIndividuales";
//		return exitoRefresco("Se ha copiado correctamente el envio excepto los destinatarios. Edite el envio y agregue los destinatarios. Cuando haya terminado pulse el botón enviar para procesar el envio.",request);
	}
	
	/**
	 * Descarga el fichero de Log.
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
	private String descargarLogErrores(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) 
	throws SIGAException{
		String forward = "descargaFichero";
		String sFicheroLog=null, sIdInstitucion=null, sIdEnvio=null;

		try {
			DefinirEnviosForm form = (DefinirEnviosForm)formulario;
			UsrBean user = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));   

			Vector vOcultos = form.getDatosTablaOcultos(0);		
			sIdInstitucion = user.getLocation();  
			sIdEnvio = (String)vOcultos.elementAt(0);
			String idTipoEnvio = (String)vOcultos.elementAt(1);
			String idEstadoEnvio = (String)vOcultos.elementAt(3);

			EnvEnviosAdm envioAdm = new EnvEnviosAdm(this.getUserBean(request));
			File fichero = null;
		
			sFicheroLog = envioAdm.getPathEnvio(sIdInstitucion,sIdEnvio) + File.separator + "informeEnvio" + ".log.xls";
			fichero = new File(sFicheroLog);
			if(fichero==null || !fichero.exists()){
				throw new SIGAException("messages.general.error.ficheroNoExiste"); 
			}
			
			
			
			
			
			
			request.setAttribute("nombreFichero", fichero.getName());
			request.setAttribute("rutaFichero", fichero.getPath());
			ClsLogging.writeFileLog("DefinirEnviosAction:fin descargarLogErrores. IdInstitucion:" + user.getLocation(), 10);

		} catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
		}
		return forward;
	}


	/**
	 * 
	 * @param form
	 * @return idPersona si es una unica, si no devolvera nul
	 * @throws ClsExceptions
	 */
	private String getIdPersonaUnica(DefinirEnviosForm form) throws ClsExceptions{
		Hashtable htPersonas = new Hashtable();
		MasterReport masterReport = new  MasterReport(); 
		Vector vCampos = masterReport.obtenerDatosFormulario(form);

		String idPersona  = null;
		for (int i = 0; i < vCampos.size(); i++) {
			Hashtable ht = (Hashtable) vCampos.get(i); 
			idPersona = (String) ht.get("idPersona");

			if(!htPersonas.containsKey(idPersona)&&i!=0){
				idPersona = null;
				break;

			}else{
				htPersonas.put(idPersona, idPersona);

			}



		}


		return idPersona;


	}
	private String getIdColegiadoUnico(DefinirEnviosForm form) throws ClsExceptions{
		Hashtable htPersonas = new Hashtable();
		MasterReport masterReport = new  MasterReport(); 
		Vector vCampos = masterReport.obtenerDatosFormulario(form);

		String idPersona  = null;
		String idInstitucion  = null;
		for (int i = 0; i < vCampos.size(); i++) {
			Hashtable ht = (Hashtable) vCampos.get(i); 
			idPersona = (String) ht.get("idPersona");
			idInstitucion = (String) ht.get("idInstitucion");
			String key = idPersona+"||"+idInstitucion;
			if(!htPersonas.containsKey(key)&&i!=0){
				idPersona = null;
				break;

			}else{
				htPersonas.put(key, idPersona);

			}

		}
		return idPersona;
	}
	protected String calcularPersona(Vector datos, DefinirEnviosForm form) throws SIGAException  
	{
		
		String todos="";
		String idPersona=null;

		 //Map map = new HashMap();
		
		    String datosInforme = "";
		    int j;
		    String resto = form.getDatosEnvios();
		    if(datos.size()==0)
		    	throw new SIGAException("messages.general.error.noExistenDatos"); 
			for (j=0; j<datos.size(); j++){		    						
				Hashtable letradoOut=(Hashtable)datos.get(j);	
				Long letrado =new Long((String)letradoOut.get("IDPERSONA")).longValue();	
		    	datosInforme="idPersona==" +letrado+"##"+resto;
		    	todos+= datosInforme;
		    	idPersona="" +letrado;

			}				   
			
			/*List guardiasList = null;
			for (int j=0; j<datos.size(); j++){		    						
				   Hashtable lineaGuardia=(Hashtable)datos.get(j);			
				   Long letrado =new Long((String)lineaGuardia.get("IDPERSONA")).longValue();	
				   if(map.containsKey(letrado)){
					   guardiasList = (List) map.get(letrado);
				   }else{
					   guardiasList = new ArrayList();
				   }
				   guardiasList.add(lineaGuardia);
				   map.put(letrado, guardiasList);
			}
			Iterator it = map.keySet().iterator();
		    List guardiasOutList = null;
		    String datosInforme = "";
		    String resto = form.getDatosEnvios();	
		    while (it.hasNext()) {
	    		i++;
		    	long letradoOut = (Long) it.next();
		    	datosInforme="idPersona==" +letradoOut+"##"+resto;
		    	todos+= datosInforme;
		    	idPersona="" +letradoOut;
		    }
			*/
			form.setDatosEnvios(todos);
			if(datos.size()!=1)
				idPersona=null;
			
		
		
        return  idPersona;
	}

	private String getIdColegiados(DefinirEnviosForm form, HttpServletRequest request) throws ClsExceptions, SIGAException{
		Hashtable htPersonas = new Hashtable();
		Vector datos = new Vector();
		Vector guardias = new Vector();
		String fechaInicio = null;
		String fechaFin = null;
		String idlista = null;
		String idPersona  = null;
		String idInstitucion  = null;
	
				MasterReport masterReport = new  MasterReport(); 
				Vector vCampos = masterReport.obtenerDatosFormulario(form);
				if ( vCampos.size()>0) {
					Hashtable ht = (Hashtable) vCampos.get(0); 
					idInstitucion = (String) ht.get("idInstitucion");
					fechaInicio = (String) ht.get("fechaIni");
					fechaFin = (String) ht.get("fechaFin");
					idlista = (String) ht.get("idLista");
				}
				ScsInclusionGuardiasEnListasAdm admIGL =new ScsInclusionGuardiasEnListasAdm(this.getUserBean(request));
				Hashtable paramBusqueda=new Hashtable();
				paramBusqueda.put(ScsInclusionGuardiasEnListasBean.C_IDINSTITUCION,idInstitucion);
				paramBusqueda.put(ScsInclusionGuardiasEnListasBean.C_IDLISTA,idlista);
				Vector listasIncluidas=admIGL.select(paramBusqueda);
				Enumeration listaResultados=listasIncluidas.elements();
				
				while(listaResultados.hasMoreElements()){
					ScsInclusionGuardiasEnListasBean fila=(ScsInclusionGuardiasEnListasBean)listaResultados.nextElement();
					// Recopilacion datos
					/* RGG 08/10/2007 cambio para que obtenga todas las guardias del tiron
					 * Vector datosParciales= admGT.getDatosListaGuardias(fila.getIdInstitucion().toString(),fila.getIdTurno().toString(),fila.getIdGuardia().toString(),fechaInicio,fechaFin);
					int i=0;
					while (i<datosParciales.size()){
						datos.add(datosParciales.elementAt(i));
						i++;
					}
					*/
					Vector guardia = new Vector();
					guardia.add(fila.getIdTurno().toString());
					guardia.add(fila.getIdGuardia().toString());
					
					guardias.add(guardia);
					
				}
				
				ScsGuardiasTurnoAdm admGT = new ScsGuardiasTurnoAdm(this.getUserBean(request));
				
				datos = admGT.getDatosPersonasGuardias(idInstitucion,idlista,guardias,fechaInicio,fechaFin);
				idPersona = calcularPersona(datos,form);
				
	
	
		return idPersona;
	}

	private String getIdColegiadoUnicoDesignas(Vector vCampos,UsrBean userBean)throws ClsExceptions,SIGAException{

		
		ScsDesignaAdm desigAdm = new ScsDesignaAdm(userBean);
		String idPersona  = null;
		String idTurno  = null;
		String idInstitucion  = null;
		String anio  = null;
		String numero  = null;
		List alPersonas = new ArrayList();
		for (int i = 0; i < vCampos.size(); i++) {
			Hashtable ht = (Hashtable) vCampos.get(i); 
			idInstitucion = (String) ht.get("idInstitucion");
			anio = (String)ht.get("anio");
			idTurno = (String) ht.get("idTurno");
			numero = (String)ht.get("numero");
			idPersona =  desigAdm.getIDLetradoDesig(idInstitucion,idTurno,anio,numero);
			ht.put("idPersona", idPersona);

			String key = idPersona+"||"+idInstitucion;
			if(!alPersonas.contains(key)&&i!=0){
				idPersona = null;
				break;

			}else{
				alPersonas.add(key);

			}
		}
		return idPersona;

	}
	
	
	private int getNumSolicitantesDesignas(Vector vCampos,UsrBean userBean)throws ClsExceptions,SIGAException{
		ScsDesignaAdm desigAdm = new ScsDesignaAdm(userBean);
		ScsDefendidosDesignaAdm defendidosAdm = new ScsDefendidosDesignaAdm(userBean);
		
		String idPersonaJG = null;
		String idTurno  = null;
		String idInstitucion  = null;
		String anio  = null;
		String numero  = null;
		boolean isSolicitanteUnicoDesignas = true;
		int numSolicitantes = 0;
		for (int i = 0; i < vCampos.size(); i++) {
			Hashtable ht = (Hashtable) vCampos.get(i); 
			idInstitucion = (String) ht.get("idInstitucion");
			anio = (String)ht.get("anio");
			idTurno = (String) ht.get("idTurno");
			numero = (String)ht.get("numero");
			Vector vDefendidos = defendidosAdm.getDefendidosDesigna(new Integer(idInstitucion), new Integer(anio),new Integer(numero), new Integer(idTurno),null);
			if(vDefendidos!=null)
				numSolicitantes += vDefendidos.size();
			if(numSolicitantes>1){
				break;
			}


		}	
		
		
		
		return numSolicitantes;

	}


	/** 
	 * Descarga el resultado de la consulta en un fichero
	 * @param  mapping
	 * @param  formulario
	 * @param  request
	 * @param  response
	 * @exception  SIGAException  En cualquier caso de error
	 * @return String para el forward
	 */
	protected String descargarEstadistica(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws SIGAException {


		try {
			EnvEstatEnvioAdm adm = new EnvEstatEnvioAdm(this.getUserBean(request));
			Vector v = adm.getDatosInforme((DefinirEnviosForm)formulario);
			String sql = "";
			Hashtable codigos = new Hashtable();
			if (v!=null && v.size()==2) {
				sql=(String)v.get(0);
				codigos=(Hashtable)v.get(1);
			}
			String nombreFichero = "stat_envios"; 
			String[] cabeceras = {"IDPERSONA","APELLIDOS1","APELLIDOS2","NOMBRE","NUM_ELECTRONICO","NUM_ORDINARIO","NUM_FAX","NUM_SMS","NUM_BUROSMS"};


			RowsContainer rc = new RowsContainer();
			rc.queryBind(sql, codigos);
			request.setAttribute("datos",rc.getAll());
			request.setAttribute("descripcion",nombreFichero);
			request.setAttribute("cabeceras",cabeceras);


		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null); 
		}
		ClsLogging.writeFileLog("DefinirEnviosAction:fin descargarEstadistica", 10);
		return "export";
	}
	protected void getJQueryPlantillasEnvio (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, JSONException, IOException
			{
		UsrBean usr = this.getUserBean(request);
		String idTipoEnvio = request.getParameter("idTipoEnvio");
		String idPlantillaEnvioDefecto = request.getParameter("idPlantillaEnvioDefecto");
		JSONObject json = new JSONObject();
		BusinessManager bm = getBusinessManager();
		InformesService informeService = (InformesService)bm.getService(InformesService.class);
		List<EnvPlantillasEnviosBean> plantillasEnviosBeans = informeService.getPlantillasEnvio(idTipoEnvio,usr.getLocation(),usr,idPlantillaEnvioDefecto);
		JSONArray plantillasEnviosJsonArray = new JSONArray();
		for (int i=0;i<plantillasEnviosBeans.size();i++) {
			plantillasEnviosJsonArray.put(plantillasEnviosBeans.get(i).getJSONObject());
		}

		json.put("plantillasEnvio", plantillasEnviosJsonArray);
		
		// json.
		 response.setContentType("text/x-json;charset=UTF-8");
		 response.setHeader("Cache-Control", "no-cache");
		 response.setHeader("Content-Type", "application/json");
	     response.setHeader("X-JSON", json.toString());
		 response.getWriter().write(json.toString()); 
		
	}
	
	protected void getJQueryTiposIntercambioTelematico (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, JSONException, IOException
			{
		UsrBean usr = this.getUserBean(request);
		String idTipoEnvio = request.getParameter("idTipoEnvio");
		JSONObject json = new JSONObject();
		JSONArray tiposIntercambioJsonArray = new JSONArray();
		
		if(idTipoEnvio.equals(ClsConstants.TIPO_IDTIPOENVIO_TELEMATICO)){
			for (TipoIntercambioEnum tipoIntercambioEnum: TipoIntercambioEnum.values()) {
				JSONObject obj = new JSONObject();
				obj.put("idTipoIntercambioTelematico", tipoIntercambioEnum.getCodigo());
	            obj.put("nombre", tipoIntercambioEnum.getDescripcion().split(":")[1]);
				tiposIntercambioJsonArray.put(obj);
			}
		}
		
		json.put("tiposIntercambio", tiposIntercambioJsonArray);
		
		response.setContentType("text/x-json;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Content-Type", "application/json");
	    response.setHeader("X-JSON", json.toString());
		response.getWriter().write(json.toString()); 
		
	}
	
	protected void getJQueryTiposEnvioPermitido (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, JSONException, IOException
			{
		UsrBean usr = this.getUserBean(request);
		String idPlantilla = request.getParameter("idPlantilla");
		String idInstitucion = request.getParameter("idInstitucion");
		JSONObject json = new JSONObject();
		BusinessManager bm = getBusinessManager();
		InformesService informeService = (InformesService)bm.getService(InformesService.class);
		AdmInformeBean informeBean = new AdmInformeBean();
		informeBean.setIdPlantilla(idPlantilla);
		informeBean.setIdInstitucion(new Integer(idInstitucion));
		List<EnvTipoEnviosBean> tipoEnviosBeans = informeService.getTiposEnvioPermitidos(informeBean,usr);
		JSONArray tiposEnvioJsonArray = new JSONArray();
		for (int i=0;i<tipoEnviosBeans.size();i++) {
			tiposEnvioJsonArray.put(tipoEnviosBeans.get(i).getJSONObject());
		}

		
		json.put("tiposEnvioPermitidos", tiposEnvioJsonArray);
		// json.
		 response.setContentType("text/x-json;charset=UTF-8");
		 response.setHeader("Cache-Control", "no-cache");
		 response.setHeader("Content-Type", "application/json");
	     response.setHeader("X-JSON", json.toString());
		 response.getWriter().write(json.toString()); 
		
	}

	protected String editarRelacionEnvio(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws ClsExceptions {

		return relacionarEnvioDesdeEntrada(mapping,formulario,request,response,true);		
	}
	
	
		protected String verRelacionEnvio(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws ClsExceptions {

		return relacionarEnvioDesdeEntrada(mapping,formulario,request,response,false);		
	}
	
	
	protected String relacionarEnvioDesdeEntrada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response, boolean editable) throws ClsExceptions{

		DefinirEnviosForm form = (DefinirEnviosForm)formulario;
		String idEnvio = form.getIdEnvio();
		
		//Anhadimos parametros para las pestanhas
		Hashtable htParametros=new Hashtable();
		htParametros.put("idEnvio",idEnvio);
		htParametros.put("idTipoEnvio",""+EnvTipoEnviosAdm.K_ENVIOTELEMATICO);
		htParametros.put("acceso",editable?"editar":"ver");
		htParametros.put("idIntercambio","");

		
		request.setAttribute("envio", htParametros);    


		request.setAttribute("nuevo", "false");	    
		request.setAttribute("buscarEnvios","true");

		return "editar";
	}
	
	protected String respuestaTelematica(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException{

		DefinirEnviosForm form = (DefinirEnviosForm)formulario;
		UsrBean usr = this.getUserBean(request);
		String datosEnvios = "";
		AdmInformeAdm informeAdm = new AdmInformeAdm(usr);
		SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_SHORT_SPANISH);
		
		try {
			Hashtable result = informeAdm.getInformeTelematico(usr.getLocation(), form.getIdTipoEnvio(),AppConstants.TipoIntercambioEnum.ICA_SGP_COM_DES_PROV_ABG_PRO.getCodigo()); 
			datosEnvios = form.getIdTipoEnvio()+","+(String)result.get("IDPLANTILLAENVIODEF")+","+(String)result.get("IDPLANTILLA")+","+usr.getLocation()+",0##";	
			form.setDatosEnvios(datosEnvios);
			form.setFechaProgramada(sdf.format(Calendar.getInstance().getTime()));
			form.setNombre(TipoIntercambioEnum.ICA_SGP_ENV_SOL_SUSP_PROC.getDescripcion().split(":")[1]);
			form.setIdTipoInforme((String)result.get("IDTIPOINFORME"));
			EnvioInformesGenericos envioInformesGenericos = new EnvioInformesGenericos();
			envioInformesGenericos.gestionarComunicacionDesignas(form,  request.getLocale(), usr);
			boolean isEnvioBatch = envioInformesGenericos.isEnvioBatch();
			if(isEnvioBatch){
				SIGASvlProcesoAutomaticoRapido.NotificarAhora(SIGASvlProcesoAutomaticoRapido.procesoGeneracionEnvio);
			}
			
			request.setAttribute("idEnvioRelacionado", form.getIdEnvio());
			
		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
		}
		
		return "finalizar";
	}	

	protected String descargarCertificadosYFacturas(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		UsrBean userBean = ((UsrBean) request.getSession().getAttribute(("USRBEAN")));
		UserTransaction tx = userBean.getTransactionLigera();
		DefinirEnviosForm form = (DefinirEnviosForm) formulario;

		ReadProperties rp = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);

		try {

			// Obtenemos el certificado
			CerSolicitudCertificadosAdm admCer = new CerSolicitudCertificadosAdm(userBean);
			CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(userBean);
			FacSerieFacturacionAdm admSerieFacturacion = new FacSerieFacturacionAdm(userBean);
			StringBuilder pathDirectorioTemporal = new StringBuilder(admSolicitud.getRutaCertificadoDirectorioBD(2000));
			pathDirectorioTemporal.append(ClsConstants.FILE_SEP);
			pathDirectorioTemporal.append("tmp");
			FileHelper.mkdirs(pathDirectorioTemporal.toString());
			List<Documento> documentosList = new ArrayList<Documento>();
			List<CerSolicitudCertificadosBean> cerSolicitudCertificadosBeans = new ArrayList<CerSolicitudCertificadosBean>();
			Vector<Hashtable<String, Object>> datosVectorError = new Vector<Hashtable<String,Object>>();
			Vector<Hashtable<String, Object>> datosVectorErrorFacturas = new Vector<Hashtable<String,Object>>();
			
			if(form.getCheckCertificados().equalsIgnoreCase("0") && form.getCheckFacturas().equalsIgnoreCase("0")){
				throw new SIGAException("messages.general.error.seleccion.documentos");
			}else{
				if(form.getCheckCertificados().equalsIgnoreCase("1")){

						String[] lineas = form.getIdsParaEnviar().split(";");
						for (int i = 0; i < lineas.length; i++) {
							StringBuilder datosSolicitudError = null;
							Hashtable<String, Object> datosHashtable = null;
							try {
								String[] campos = lineas[i].split("\\|\\|");
								String nombreSolicitud = "";
								if (campos.length > 1) {
									String idInstitucion = campos[6];
			
									String idSolicitud = campos[1];
			
									Hashtable<String, Object> htSolicitud = new Hashtable<String, Object>();
									htSolicitud.put(CerSolicitudCertificadosBean.C_IDINSTITUCION, idInstitucion);
									htSolicitud.put(CerSolicitudCertificadosBean.C_IDSOLICITUD, idSolicitud);
			
									Vector vDatos = admSolicitud.selectByPK(htSolicitud);
			
									CerSolicitudCertificadosBean beanSolicitud = (CerSolicitudCertificadosBean) vDatos.elementAt(0);
			
									ArrayList<File> file=admSolicitud.recuperarVariosCertificado(beanSolicitud);
									
									//Si file es mayor que uno quiere decir que esa solicitud tiene plantillas adjuntas
									
									if(file.size()>1){
										//Recorremos las plantillas
										//Recorro los certificados hasta obtener el .zip
										Iterator<File> iteratorFicheros = file.iterator();
										File fichero = null;
										while (iteratorFicheros.hasNext() ) {
											fichero = (File) iteratorFicheros.next();
											
											if (fichero == null || !fichero.exists()) {
												datosHashtable =  new Hashtable<String, Object>();
												datosHashtable.put("Nº Solicitud",idSolicitud);
												
												datosVectorError.add(datosHashtable);
												
												datosSolicitudError = new StringBuilder();
												datosSolicitudError.append("[Institucion:");
												datosSolicitudError.append(idInstitucion);
												datosSolicitudError.append(", solicitud:");
												datosSolicitudError.append(idSolicitud);
												datosSolicitudError.append("],");
												throw new SIGAException("messages.general.error.ficheroNoExiste");
												
											}
												Documento certificado = new Documento(fichero,admSolicitud.getNombreVariosFicheroSalida(beanSolicitud,fichero.getName()));
												documentosList.add(certificado);
												beanSolicitud.setCertificado(certificado);
												cerSolicitudCertificadosBeans.add(beanSolicitud);
										    
										}
									}else{
										
										if (file.isEmpty() || file.get(0) == null || !file.get(0).exists()) {
											datosHashtable =  new Hashtable<String, Object>();
											datosHashtable.put("Nº Solicitud",idSolicitud);
											
											datosVectorError.add(datosHashtable);
											datosSolicitudError = new StringBuilder();
											datosSolicitudError.append("[Institucion:");
											datosSolicitudError.append(idInstitucion);
											datosSolicitudError.append(", solicitud:");
											datosSolicitudError.append(idSolicitud);
											datosSolicitudError.append("],");
											throw new SIGAException("messages.general.error.ficheroNoExiste");
										}
											
											Documento certificado = new Documento(file.get(0),admSolicitud.getNombreVariosFicheroSalida(beanSolicitud,file.get(0).getName()));
											documentosList.add(certificado);
											beanSolicitud.setCertificado(certificado);
											cerSolicitudCertificadosBeans.add(beanSolicitud);
										
										
									}
									beanSolicitud.setFechaDescarga("SYSDATE");
									
									
			
									
								}
			
							} catch (SIGAException e) {
								ClsLogging.writeFileLog("Error descarga Certificado" + datosSolicitudError.toString() + " Error: " + e.getLiteral(userBean.getLanguage()), 3);
							}
						}
						
						Boolean exito = admSolicitud.actualizarFechaDescargaCertificados(cerSolicitudCertificadosBeans);
						if(exito == Boolean.FALSE){
							throw new ClsExceptions("Error al actualizar la fecha de descarga: " + admSolicitud.getError());
						}
						String aviso = null;
				}
				if(form.getCheckFacturas().equalsIgnoreCase("1")){
					
					    FacFacturaAdm admFactura = new FacFacturaAdm(userBean);
					    InformeFactura informe = new InformeFactura(userBean);	
					    CenPersonaAdm personaAdm = new CenPersonaAdm(userBean);
					    Vector<Hashtable<String,Object>> vFacturas = new Vector<Hashtable<String,Object>>();
						String[] lineas = form.getIdsParaEnviar().split(";");
						for (int i = 0; i < lineas.length; i++) {
							Hashtable<String, Object> datosHashtable = null;
							String idSolicitud="";
							try{
								StringBuilder datosSolicitudError = null;
								
								
								
									String[] campos = lineas[i].split("\\|\\|");
									String nombreSolicitud = "";
									if (campos.length > 1) {
										String idInstitucion = campos[6];
										
										idSolicitud = campos[1];
										
										// Obtiene las facturas de una solicitud de certificado
							        	vFacturas = admFactura.obtenerFacturasFacturacionRapida(idInstitucion, null, idSolicitud);	
							        	
							        	  if (vFacturas==null || vFacturas.size()==0) {
							        		  datosHashtable =  new Hashtable<String, Object>();
											  datosHashtable.put("Nº Solicitud",idSolicitud);
											  datosVectorErrorFacturas.add(datosHashtable);
							        	  }else{
						        			// Obtengo los datos de la factura
							        		Hashtable<String,Object> hFactura = vFacturas.get(0);
							        		
							        		// Obtengo la peticion del certificado
							        		String idPeticion = UtilidadesHash.getString(hFactura, PysCompraBean.C_IDPETICION);
							        		  // GENERAR FICHERO: Siempre elimina el zip con los pdfs firmads o el pdf firmado 	    
							      			File fichero = informe.generarInformeFacturacionRapida(request, idInstitucion, idPeticion, vFacturas);
							      			
							      		// DESCARGAR FICHERO
							    			String nombreColegiado ="";
							    			if(vFacturas != null &&  vFacturas.size()>0){
							    			Hashtable<String,Object> obj = vFacturas.get(0);
							    			String idPersona = (String)obj.get("IDPERSONA");
							    		    nombreColegiado ="";
							    			if(idPersona != null && !"".equalsIgnoreCase(idPersona)){
							    				 nombreColegiado = personaAdm.obtenerNombreApellidos(idPersona);
							    				if(nombreColegiado != null && !"".equalsIgnoreCase(nombreColegiado)){
							    					nombreColegiado = UtilidadesString.eliminarAcentosYCaracteresEspeciales(nombreColegiado)+"-";	
							    				}else{
							    					nombreColegiado="";
							    				}
							    			}
							    			}
							      			
							    			
							    			
							    			String where = " WHERE " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDSERIEFACTURACION + " = " + vFacturas.get(0).get("IDSERIEFACTURACION") +
													" AND " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDINSTITUCION +" = " + vFacturas.get(0).get("IDINSTITUCION");
														
											Vector<FacSerieFacturacionBean> vSeriesFacturacion = admSerieFacturacion.select(where);
																	
											Documento factura = null;
											String[] nombreFicherosarrays;
											if (vSeriesFacturacion!=null && vSeriesFacturacion.size()>0) {
												FacSerieFacturacionBean beanSerieFacturacion = vSeriesFacturacion.get(0);
												
												switch (beanSerieFacturacion.getIdNombreDescargaPDF()) {
												case 1:
													nombreFicherosarrays = fichero.getName().split("-",2);
													factura = new Documento(fichero,nombreFicherosarrays[1]);
													break;
												case 2:
													//Quitamos la extensión del fichero y añadimos el nombre más la extensión
													String[] separacionExtensionDelFichero = fichero.getName().split(Pattern.quote("."));
													String[] separacionNombreColegiado = nombreColegiado.split("-");
													nombreFicherosarrays = separacionExtensionDelFichero[0].split("-",2);
													factura = new Documento(fichero,nombreFicherosarrays[1] + "-"+separacionNombreColegiado[0]+"."+separacionExtensionDelFichero[1]);
													
													break;
												case 3:
													nombreFicherosarrays = fichero.getName().split("-",2);
													factura = new Documento(fichero,nombreColegiado+ nombreFicherosarrays[1]);
													
													break;
							
												default:
													nombreFicherosarrays = fichero.getName().split("-",2);
													factura = new Documento(fichero,nombreColegiado+ nombreFicherosarrays[1]);
													break;
												}
											}else{
												nombreFicherosarrays = fichero.getName().split("-",2);
												factura = new Documento(fichero,nombreFicherosarrays[1]);
											}
							    			
							    			
							    			
							      			//Comprobamos que la factura no esté añadida ya. Caso en que dos certificados pertenezca a la misma facturación con que salga una vez sería suficiente.
							      			Iterator<Documento> iteratorFicheros = documentosList.iterator();
											Boolean encontrado = Boolean.FALSE;
											Documento documento = null;
											while (iteratorFicheros.hasNext() && !encontrado) {
												documento = (Documento) iteratorFicheros.next();
												if(factura.getDescripcion().equalsIgnoreCase(documento.getDescripcion())){
													encontrado=Boolean.TRUE;
												}
											}
							      			if(!encontrado)
							      				documentosList.add(factura);
							         }
									
								}
							} catch (SIGAException e) {
								datosHashtable =  new Hashtable<String, Object>();
								datosHashtable.put("Nº Solicitud",idSolicitud);
								
								datosVectorErrorFacturas.add(datosHashtable);
								ClsLogging.writeFileLog("Error descarga Factura Error: " + e.getLiteral(userBean.getLanguage()), 3);
								
							}	
					}
				}
			}
			
			if (documentosList.size() > 0) {
				
				StringBuilder pathZip = new StringBuilder(pathDirectorioTemporal);
				File directorio = new File(pathZip.toString());
				if (!directorio.exists())
					directorio.mkdir();
				pathZip.append(File.separatorChar);
				pathZip.append("Certificados_");
				pathZip.append(GstDate.parseDateToString(new Date(),"yyyyMMdd_hhmmss",new Locale("ES")));
				pathZip.append(".zip");
				
				if (datosVectorError.size()>0){
					File erroresFile = ExcelHelper.createExcelFile(new ArrayList<String>(datosVectorError.get(0).keySet()) , datosVectorError, "LogErrorCertificadosNoDescargados");
					Documento erroresDoc = new Documento(erroresFile,"LogErrorCertificadosNoDescargados.xls");
					documentosList.add(erroresDoc);
					request.setAttribute("aviso","certificados.descarga.incompleta");
					
					
				}
				
				if (datosVectorErrorFacturas.size()>0){
					File erroresFile = ExcelHelper.createExcelFile(new ArrayList<String>(datosVectorErrorFacturas.get(0).keySet()) , datosVectorErrorFacturas, "LogErrorFacturasNoDescargados");
					Documento erroresDoc = new Documento(erroresFile,"LogErrorFacturasNoDescargados.xls");
					documentosList.add(erroresDoc);
					request.setAttribute("aviso","certificados.descarga.incompleta");
					
					
				}
				
				File filezip = UtilidadesFicheros.doZip(pathZip.toString(), documentosList);
				filezip.deleteOnExit();
				
				request.setAttribute("nombreFichero", filezip.getName());
				request.setAttribute("rutaFichero", filezip.getPath());
				request.setAttribute("borrarFichero", "true");
				request.setAttribute("generacionOK", "OK");

			}else if(datosVectorError.size()>0 || datosVectorErrorFacturas.size()>0){
				
				StringBuilder pathZip = new StringBuilder(pathDirectorioTemporal);
				File directorio = new File(pathZip.toString());
				if (!directorio.exists())
					directorio.mkdir();
				pathZip.append(File.separatorChar);
				pathZip.append("Certificados_");
				pathZip.append(GstDate.parseDateToString(new Date(),"yyyyMMdd_hhmmss",new Locale("ES")));
				pathZip.append(".zip");
				if(datosVectorError.size()>0){
					File erroresFile = ExcelHelper.createExcelFile(new ArrayList<String>(datosVectorError.get(0).keySet()) , datosVectorError, "LogErrorCertificadosNoDescargados");
					Documento erroresDoc = new Documento(erroresFile,"LogErrorCertificadosNoDescargados.xls");
					documentosList.add(erroresDoc);
				}
				if(datosVectorErrorFacturas.size()>0){
					File erroresFileFacturas = ExcelHelper.createExcelFile(new ArrayList<String>(datosVectorErrorFacturas.get(0).keySet()) , datosVectorErrorFacturas, "LogErrorFacturasNoDescargados");
					Documento erroresDocFacturas = new Documento(erroresFileFacturas,"LogErrorFacturasNoDescargados.xls");
					documentosList.add(erroresDocFacturas);
				}
				
				File filezip = UtilidadesFicheros.doZip(pathZip.toString(), documentosList);
				filezip.deleteOnExit();
				request.setAttribute("nombreFichero", filezip.getName());
				request.setAttribute("rutaFichero", filezip.getPath());
				request.setAttribute("borrarFichero", "true");
				request.setAttribute("generacionOK", "OK");
				request.setAttribute("aviso","certificados.descarga.incompleta");
			}else{
				request.setAttribute("aviso", "messages.informes.ningunInformeGenerado");
				ClsLogging.writeFileLog("No hay ficheros que descargar ", 3);
			}
			
			return "descarga";

		} catch (Exception e) {
			this.throwExcp("messages.general.error", new String[] { "modulo.envios" }, e, tx);
		}
		return "exception";
	}
	
	
	/**
	 * Metodo que implementa el modo enviarPersona
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String envioModalMasivoCertificado(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		UsrBean userBean = ((UsrBean) request.getSession().getAttribute(("USRBEAN")));
		DefinirEnviosForm form = (DefinirEnviosForm) formulario;
		String mensaje = "";
		// Obtenemos el certificado
		CerSolicitudCertificadosAdm admCer = new CerSolicitudCertificadosAdm(userBean);

		CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(userBean);
		PysProductosInstitucionAdm admProd = new PysProductosInstitucionAdm(userBean);
		CenInstitucionAdm admInst = new CenInstitucionAdm(userBean);
		EnvEnviosAdm enviosAdm =  new EnvEnviosAdm(userBean);
		
		//Cuando es más de 20 se ejecuta el proceso automático cada min
		CerSolicitudCertificadosEnviosService cerSolicitudCertificadosEnviosService = (CerSolicitudCertificadosEnviosService) BusinessManager.getInstance().getService(CerSolicitudCertificadosEnviosService.class);
		CerEnvios cerEnvios = new CerEnvios();
		
		try {

			// obtener tipoEnvio
			String idTipoEnvio = form.getIdTipoEnvio();
			// obtener plantilla
			String idPlantilla = form.getIdPlantillaEnvios();
			// obtener plantilla de generacion
			String idPlantillaGeneracion = form.getIdPlantillaGeneracion();
			String acuseRecibo = form.getAcuseRecibo();
			String fechaProg = form.getFechaProgramada();
			String destinatario = form.getRadioDestinatario();
			String comunicacionAsunto = form.getComunicacionAsunto();
			String[] lineas = form.getIdsParaEnviar().split(";");
			
			
			if(form.getCheckCertificados().equalsIgnoreCase("0") && form.getCheckFacturas().equalsIgnoreCase("0")){
				throw new SIGAException("messages.general.error.seleccion.documentos");
				
			}else{
				
				//Menor que 21 debido a la posición primera que va vacia.		
				if(lineas != null && lineas.length <=21){
					insertarEnvioModalMasivoCertificado(idTipoEnvio, idPlantilla, idPlantillaGeneracion,
							 acuseRecibo, fechaProg, lineas, form.getCheckCertificados(), form.getCheckFacturas(), userBean,request, destinatario,comunicacionAsunto,"NO");
				}else{
					//Obtenemos el identificador que indicará qué solicitudes pertenecen a la misma solicitud.
					 Long idEnvio = enviosAdm.getSecuenciaNextVal(EnvEnviosBean.SEQ_ENV_ENVIOS_MASIVOS);
					 Short activo = 1;
					 Short desactivado = 0;
					 //Insertamos de uno.
					 for (int i = 0; i < lineas.length; i++) {
							StringBuilder datosSolicitudError = null;
								String linea = lineas[i];
						
								String[] campos = linea.split("\\|\\|");
								if (campos.length > 1) {
									String idInstitucion = campos[6];
									String idSolicitud = campos[1];
									
									cerEnvios.setIdinstitucion(Short.valueOf(idInstitucion));
									cerEnvios.setIdsolicitud(Long.valueOf(idSolicitud));
									cerEnvios.setAsunto(comunicacionAsunto);
									if(fechaProg != null && !"".equalsIgnoreCase(fechaProg)){
										SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd/mm/yyyy");
										Date fecha = null;
										fecha = formatoDelTexto.parse(fechaProg);
										cerEnvios.setFechaprogramada(fecha);
									}
									cerEnvios.setDestinatario(Short.valueOf(destinatario));
									cerEnvios.setTipoenvio(Short.valueOf(idTipoEnvio));
									cerEnvios.setTipoplantilla(Short.valueOf(idPlantilla));
									if(idPlantillaGeneracion != null && !"".equalsIgnoreCase(idPlantillaGeneracion))
										cerEnvios.setPlantilla(Short.valueOf(idPlantillaGeneracion));
									
									if (form.getCheckCertificados().equalsIgnoreCase("1"))
										cerEnvios.setCertificados(activo);
									else
										cerEnvios.setCertificados(desactivado);
									
									if(form.getCheckFacturas().equalsIgnoreCase("1"))
										cerEnvios.setFacturas(activo);
									else
										cerEnvios.setFacturas(desactivado);
									cerEnvios.setAcuselectura(Short.valueOf(acuseRecibo));
									cerEnvios.setIdpeticiondeenvio(idEnvio);
									cerEnvios.setUsucreacion(new Integer(userBean.getUserName()));
									cerEnvios.setFechacreacion(new Date());
									cerEnvios.setUsumodificacion(new Integer(userBean.getUserName()));
									cerEnvios.setFechamodificacion(new Date());
								
									cerSolicitudCertificadosEnviosService.insert(cerEnvios);
								}
				  }
			   }
			}
					
		} catch (Exception e) {
			this.throwExcp("messages.general.error", new String[] { "modulo.envios" }, e, null);
		}
		ClsLogging.writeFileLog("DefinirEnviosAction:fin insertarEnvioModalCertificado. IdInstitucion:" + userBean.getLocation(), 10);
		mensaje = "Envio masivo procesado correctamente";
		return exitoModal(mensaje, request);
	}
	
	
	
	/**
	 * Metodo que implementa el modo enviarPersona
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public static String insertarEnvioModalMasivoCertificado(String idTipoEnvio,String idPlantilla,String idPlantillaGeneracion,
			String acuseRecibo,String fechaProg, String[] lineas,String checkCertificados, String checkFacturas,UsrBean userBean,
			HttpServletRequest request, String destinatario, String comunicacionAsunto, String origenDemonio) throws SIGAException {

		UserTransaction tx = userBean.getTransactionPesada();
		LogFileWriter log = null;

		String mensaje = "";
		try {
		
			// obtener fechaProgramada
			String fechaProgramada = null;
			// fechaProg = GstDate.anyadeHora(fechaProg);
			if (fechaProg != null && fechaProg.length() == 10)
				fechaProg += " " + new Date().getHours() + ":" + new Date().getMinutes() + ":" + new Date().getSeconds();

			String language = userBean.getLanguage();
			String format = language.equalsIgnoreCase("1") ? ClsConstants.DATE_FORMAT_LONG_ENGLISH : ClsConstants.DATE_FORMAT_LONG_SPANISH;
			SimpleDateFormat formatoDelTexto = new SimpleDateFormat(format);
			GstDate gstDate = new GstDate();
			if (fechaProg != null && !fechaProg.equals("")) {
				Date date = formatoDelTexto.parse(fechaProg);
				SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
				fechaProgramada = sdf.format(date);
			} else {
				fechaProgramada = null;
			}

			// Obtenemos el certificado
			CerSolicitudCertificadosAdm admCer = new CerSolicitudCertificadosAdm(userBean);

			CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(userBean);
			PysProductosInstitucionAdm admProd = new PysProductosInstitucionAdm(userBean);
			CenInstitucionAdm admInst = new CenInstitucionAdm(userBean);
			CenPersonaAdm admPersona = new CenPersonaAdm(userBean);
			EnvEnviosAdm enviosAdm =  new EnvEnviosAdm(userBean);
			FacSerieFacturacionAdm admSerieFacturacion = new FacSerieFacturacionAdm(userBean);

			HashMap<Long, List<CerSolicitudCertificadosBean>> hashCertificadosPorDestinatario = new HashMap<Long, List<CerSolicitudCertificadosBean>>();
			List<CerSolicitudCertificadosBean> cerSolicitudCertificadosBeans = new ArrayList<CerSolicitudCertificadosBean>();
			StringBuilder errores = new StringBuilder("");
			for (int i = 0; i < lineas.length; i++) {
				StringBuilder datosSolicitudError = null;
				try {
					
					String[] campos = lineas[i].split("\\|\\|");
					String idInstitucion = null;
					String idSolicitud = null;
					
					if (campos.length > 1) {
						if(origenDemonio.equalsIgnoreCase("NO")){
							idInstitucion = campos[6];
	
							idSolicitud = campos[1];
						}else{
							idInstitucion = campos[0];
							
							idSolicitud = campos[1];
						}

						Hashtable<String, Object> solicitudCertificadosPkHashtable = new Hashtable<String, Object>();
						solicitudCertificadosPkHashtable.put(CerSolicitudCertificadosBean.C_IDINSTITUCION, idInstitucion);
						solicitudCertificadosPkHashtable.put(CerSolicitudCertificadosBean.C_IDSOLICITUD, idSolicitud);

						Vector solicitudCertificadoPKVector = admSolicitud.selectByPK(solicitudCertificadosPkHashtable);
						if (solicitudCertificadoPKVector == null || solicitudCertificadoPKVector.size() < 1)
							throw new SIGAException("No se ha encontrado la solicitud");
						CerSolicitudCertificadosBean solicitudCertificadoBean = (CerSolicitudCertificadosBean) solicitudCertificadoPKVector.get(0);

						//Creamos el log de error
				        log = LogFileWriter.getLogFileWriter(admSolicitud.obtenerRutaLogError(solicitudCertificadoBean),idSolicitud+"-LogError");
						log.clear();
						
						Hashtable<String, Object> productoInstitucionPKHashtable = new Hashtable<String, Object>();
						productoInstitucionPKHashtable.put(PysProductosInstitucionBean.C_IDINSTITUCION, solicitudCertificadoBean.getIdInstitucion());
						productoInstitucionPKHashtable.put(PysProductosInstitucionBean.C_IDTIPOPRODUCTO, solicitudCertificadoBean.getPpn_IdTipoProducto());
						productoInstitucionPKHashtable.put(PysProductosInstitucionBean.C_IDPRODUCTO, solicitudCertificadoBean.getPpn_IdProducto());
						productoInstitucionPKHashtable.put(PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION, solicitudCertificadoBean.getPpn_IdProductoInstitucion());

						Vector productoInstitucionPKVector = admProd.selectByPK(productoInstitucionPKHashtable);
						if (productoInstitucionPKVector == null || productoInstitucionPKVector.size() < 1){
							datosSolicitudError = new StringBuilder();
							datosSolicitudError.append("[Institucion:");
							datosSolicitudError.append(idInstitucion);
							datosSolicitudError.append(", solicitud:");
							datosSolicitudError.append(idSolicitud);
							datosSolicitudError.append("],");
							errores.append(datosSolicitudError);
							
							log.addLog(new String[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), "Error: No se ha encontrado el producto"});
					    	  /** Escribiendo fichero de log **/
							if (log != null)
								log.flush();
							
							throw new SIGAException("No se ha encontrado el producto");
						}
						PysProductosInstitucionBean beanProd = (PysProductosInstitucionBean) productoInstitucionPKVector.get(0);
						// Seteamos al descripcion del producto para el nombre
						// del envio
						solicitudCertificadoBean.setProductoDescripcion(beanProd.getDescripcion());
						Long idPersonaDestinataria;
						if(destinatario.equals("0")){
								// decidimos sobre cual es la institucion para enviar
								Integer idInstitucionDestinatario = null;
						
								if (solicitudCertificadoBean.getIdInstitucionOrigen() == null) {
									// Es certificado y no han definido colegio
									// presentador
									datosSolicitudError = new StringBuilder();
									datosSolicitudError.append("[Institucion:");
									datosSolicitudError.append(idInstitucion);
									datosSolicitudError.append(", solicitud:");
									datosSolicitudError.append(idSolicitud);
									datosSolicitudError.append("],");
									errores.append(datosSolicitudError);
									
									log.addLog(new String[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), "Error: Es un certificado y no se ha definido colegio presentador (No se envía)."});
							    	  /** Escribiendo fichero de log **/
									if (log != null)
										log.flush();
									
									throw new SIGAException("Es un certificado y no se ha definido colegio presentador (No se envía).");
								} else {
									// destinatario el colegio origen / PRESENTADOR
									idInstitucionDestinatario = solicitudCertificadoBean.getIdInstitucionOrigen();
								}
								
								
								// obtenemos el nombre del colegio
								Hashtable<String, Object> institucionPKHashtable = new Hashtable<String, Object>();
								institucionPKHashtable.put(CenInstitucionBean.C_IDINSTITUCION, idInstitucionDestinatario);
								
								
								//TODO: Si es solicitante debo de obtener la persona destinataria del certificado y guardarlo en un CenPersonaBean
								
								// Obtenemos la persona de la isntitucion
								Vector institucionPKVector = admInst.selectByPK(institucionPKHashtable);
								if (institucionPKVector == null || institucionPKVector.size() < 1){
									datosSolicitudError = new StringBuilder();
									datosSolicitudError.append("[Institucion:");
									datosSolicitudError.append(idInstitucion);
									datosSolicitudError.append(", solicitud:");
									datosSolicitudError.append(idSolicitud);
									datosSolicitudError.append("],");
									errores.append(datosSolicitudError);
									log.addLog(new String[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), "Error: No se ha encontrado la institucion destinataria"});
							    	  /** Escribiendo fichero de log **/
									if (log != null)
										log.flush();
									
									throw new SIGAException("No se ha encontrado la institucion destinataria");
								}
		
								CenInstitucionBean beanInst = (CenInstitucionBean) institucionPKVector.get(0);
								// Seteamos al abreviatura de la institucino para el
								// nombre del envio
								solicitudCertificadoBean.setInstitucionAbreviatura(beanInst.getAbreviatura());
								
								//Obtenemos el idPersona
								idPersonaDestinataria = beanInst.getIdPersona().longValue();
						}else{
							//Si se ha seleccionado solicitante, se cogerá la persona destinataria del certificado.
							idPersonaDestinataria = solicitudCertificadoBean.getIdPersona_Des();
							solicitudCertificadoBean.setInstitucionAbreviatura(admPersona.obtenerNombreApellidos(String.valueOf(idPersonaDestinataria)));
							
						}
						//Si se ha seleccionado el check de certificados 
						if(checkCertificados.equalsIgnoreCase("1")){
							try{
								ArrayList<File> file=admSolicitud.recuperarVariosCertificado(solicitudCertificadoBean);
								
									if(file.size() >1){
										//Recorremos las plantillas
										//Recorro los certificados hasta obtener el .zip
										Iterator<File> iteratorFicheros = file.iterator();
										File fichero = null;
										CerSolicitudCertificadosBean solicitudCertificadoBeanAux = null;
										while (iteratorFicheros.hasNext() ) {
											fichero = (File) iteratorFicheros.next();
											
											String rutaFichero =fichero.getPath();
											Documento certificadoDoc = new Documento(rutaFichero,admSolicitud.getNombreVariosFicheroSalida(solicitudCertificadoBean,fichero.getName()));
											
											solicitudCertificadoBeanAux = (CerSolicitudCertificadosBean) BeanUtils.cloneBean(solicitudCertificadoBean);
											solicitudCertificadoBeanAux.setCertificado(certificadoDoc);
			
											if (certificadoDoc.getDocumento() == null || !certificadoDoc.getDocumento().exists()) {
												datosSolicitudError = new StringBuilder();
												datosSolicitudError.append("[Institucion:");
												datosSolicitudError.append(idInstitucion);
												datosSolicitudError.append(", solicitud:");
												datosSolicitudError.append(idSolicitud);
												datosSolicitudError.append("],");
												errores.append(datosSolicitudError);
												log.addLog(new String[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), "No existe fichero de Certificado generado"});
										    	  /** Escribiendo fichero de log **/
												if (log != null)
													log.flush();
												
												throw new SIGAException("No existe fichero de Certificado generado");
											}
			
											if (hashCertificadosPorDestinatario.containsKey(idPersonaDestinataria)) {
												cerSolicitudCertificadosBeans = hashCertificadosPorDestinatario.get(idPersonaDestinataria);
											} else {
												cerSolicitudCertificadosBeans = new ArrayList<CerSolicitudCertificadosBean>();
											}
											cerSolicitudCertificadosBeans.add(solicitudCertificadoBeanAux);
											solicitudCertificadoBeanAux = new CerSolicitudCertificadosBean();
											hashCertificadosPorDestinatario.put(idPersonaDestinataria,cerSolicitudCertificadosBeans);
										}
										
									}else{
										//Este if esta puesto para el caso de certificados que aún no se han aprobado ni generado
										if(solicitudCertificadoBean.getContadorCer() != null && !"".equalsIgnoreCase(solicitudCertificadoBean.getContadorCer())){
											
											String rutaFichero = admSolicitud.getRutaCertificadoFichero(solicitudCertificadoBean,admSolicitud.getRutaCertificadoDirectorioBD(solicitudCertificadoBean.getIdInstitucion()));
											Documento certificadoDoc = new Documento(rutaFichero,admSolicitud.getNombreFicheroSalida(solicitudCertificadoBean));
											
											 
											solicitudCertificadoBean.setCertificado(certificadoDoc);
				
											if (certificadoDoc.getDocumento() == null || !certificadoDoc.getDocumento().exists()) {
												datosSolicitudError = new StringBuilder();
												datosSolicitudError.append("[Institucion:");
												datosSolicitudError.append(idInstitucion);
												datosSolicitudError.append(", solicitud:");
												datosSolicitudError.append(idSolicitud);
												datosSolicitudError.append("],");
												errores.append(datosSolicitudError);
												log.addLog(new String[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), "No existe fichero de Certificado generado"});
										    	  /** Escribiendo fichero de log **/
												if (log != null)
													log.flush();
												throw new SIGAException("No existe fichero de Certificado generado");
											}
				
											if (hashCertificadosPorDestinatario.containsKey(idPersonaDestinataria)) {
												cerSolicitudCertificadosBeans = hashCertificadosPorDestinatario.get(idPersonaDestinataria);
											} else {
												cerSolicitudCertificadosBeans = new ArrayList<CerSolicitudCertificadosBean>();
											}
											cerSolicitudCertificadosBeans.add(solicitudCertificadoBean);
											
											hashCertificadosPorDestinatario.put(idPersonaDestinataria,cerSolicitudCertificadosBeans);
										
										}else{
											datosSolicitudError = new StringBuilder();
											datosSolicitudError.append("[Institucion:");
											datosSolicitudError.append(idInstitucion);
											datosSolicitudError.append(", solicitud:");
											datosSolicitudError.append(idSolicitud);
											datosSolicitudError.append("],");
											errores.append(datosSolicitudError);
											log.addLog(new String[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), "No existe fichero de Certificado generado"});
									    	  /** Escribiendo fichero de log **/
											if (log != null)
												log.flush();
											throw new SIGAException("No existe fichero de Certificado generado");
										}
									}
								} catch (SIGAException e) {
									ClsLogging.writeFileLog(e.getMessage(),3);
								}									
							}
						//Si se ha seleccionado el check de facturas 
						if(checkFacturas.equalsIgnoreCase("1")){
							FacFacturaAdm admFactura = new FacFacturaAdm(userBean);
						    InformeFactura informe = new InformeFactura(userBean);	
						    CenPersonaAdm personaAdm = new CenPersonaAdm(userBean);
						    Vector<Hashtable<String,Object>> vFacturas = new Vector<Hashtable<String,Object>>();
						    CerSolicitudCertificadosBean solicitudCertificadoBeanAux = new CerSolicitudCertificadosBean();
						    
						 // Obtiene las facturas de una solicitud de certificado
				        	vFacturas = admFactura.obtenerFacturasFacturacionRapida(idInstitucion, null, idSolicitud);	
							
				        	
				        	  if (vFacturas==null || vFacturas.size()==0) {
				        		
								datosSolicitudError = new StringBuilder();
								datosSolicitudError.append("[Institucion:");
								datosSolicitudError.append(idInstitucion);
								datosSolicitudError.append(", solicitud:");
								datosSolicitudError.append(idSolicitud);
								datosSolicitudError.append("],");
								errores.append(datosSolicitudError);
								log.addLog(new String[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), "No se ha podido generar el fichero de Factura"});
						    	  /** Escribiendo fichero de log **/
								if (log != null)
									log.flush();
								throw new SIGAException("No se ha podido generar el fichero de Factura");
						
				        	  }else{
				        		  try{
				        			// Obtengo los datos de la factura
					        		Hashtable<String,Object> hFactura = vFacturas.get(0);
					        		
					        		// Obtengo la peticion del certificado
					        		String idPeticion = UtilidadesHash.getString(hFactura, PysCompraBean.C_IDPETICION);
					        		  // GENERAR FICHERO: Siempre elimina el zip con los pdfs firmads o el pdf firmado 	    
					      			File fichero = informe.generarInformeFacturacionRapida(request, idInstitucion, idPeticion, vFacturas);
					      			
					      		// DESCARGAR FICHERO
					    			String nombreColegiado ="";
					    			if(vFacturas != null &&  vFacturas.size()>0){
					    			Hashtable<String,Object> obj = vFacturas.get(0);
					    			String idPersona = (String)obj.get("IDPERSONA");
					    		    nombreColegiado ="";
					    			if(idPersona != null && !"".equalsIgnoreCase(idPersona)){
					    				 nombreColegiado = personaAdm.obtenerNombreApellidos(idPersona);
					    				if(nombreColegiado != null && !"".equalsIgnoreCase(nombreColegiado)){
					    					nombreColegiado = UtilidadesString.eliminarAcentosYCaracteresEspeciales(nombreColegiado)+"-";	
					    				}else{
					    					nombreColegiado="";
					    				}
					    			}
					    			}
					      			
					    			
					    			String where = " WHERE " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDSERIEFACTURACION + " = " + vFacturas.get(0).get("IDSERIEFACTURACION") +
											" AND " + FacSerieFacturacionBean.T_NOMBRETABLA + "." + FacSerieFacturacionBean.C_IDINSTITUCION +" = " + vFacturas.get(0).get("IDINSTITUCION");
												
									Vector<FacSerieFacturacionBean> vSeriesFacturacion = admSerieFacturacion.select(where);
															
									Documento certificado = null;
									String[] nombreFicherosarrays;
									if (vSeriesFacturacion!=null && vSeriesFacturacion.size()>0) {
										FacSerieFacturacionBean beanSerieFacturacion = vSeriesFacturacion.get(0);
										
										switch (beanSerieFacturacion.getIdNombreDescargaPDF()) {
										case 1:
											nombreFicherosarrays = fichero.getName().split("-",2);
											certificado = new Documento(fichero,nombreFicherosarrays[1]);
											break;
										case 2:
											//Quitamos la extensión y añadimos el nombre más la extensión
											String[] separacionExtensionDelFichero = fichero.getName().split(Pattern.quote("."));
											String[] separacionNombreColegiado = nombreColegiado.split("-");
											nombreFicherosarrays = separacionExtensionDelFichero[0].split("-",2);
											certificado = new Documento(fichero,nombreFicherosarrays[1] + "-"+separacionNombreColegiado[0]+"."+separacionExtensionDelFichero[1]);
											break;
										case 3:
											nombreFicherosarrays = fichero.getName().split("-",2);
											certificado = new Documento(fichero,nombreColegiado+ nombreFicherosarrays[1]);
											break;
					
										default:
											nombreFicherosarrays = fichero.getName().split("-",2);
											certificado = new Documento(fichero,nombreColegiado+ nombreFicherosarrays[1]);
											break;
										}
									}else{
										nombreFicherosarrays = fichero.getName().split("-",2);
										certificado = new Documento(fichero,nombreColegiado+ nombreFicherosarrays[1]);
									}
									
					      			solicitudCertificadoBeanAux = (CerSolicitudCertificadosBean) BeanUtils.cloneBean(solicitudCertificadoBean);
									solicitudCertificadoBeanAux.setCertificado(certificado);
									
					      			
									if (certificado.getDocumento() == null || !certificado.getDocumento().exists()) {
										datosSolicitudError = new StringBuilder();
										datosSolicitudError.append("[Institucion:");
										datosSolicitudError.append(idInstitucion);
										datosSolicitudError.append(", solicitud:");
										datosSolicitudError.append(idSolicitud);
										datosSolicitudError.append("],");
										errores.append(datosSolicitudError);
										log.addLog(new String[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), "No se ha podido generar el fichero de Factura"});
								    	  /** Escribiendo fichero de log **/
										if (log != null)
											log.flush();
										throw new SIGAException("No se ha podido generar el fichero de Factura");
									}
		
									if (hashCertificadosPorDestinatario.containsKey(idPersonaDestinataria)) {
										cerSolicitudCertificadosBeans = hashCertificadosPorDestinatario.get(idPersonaDestinataria);
									} else {
										cerSolicitudCertificadosBeans = new ArrayList<CerSolicitudCertificadosBean>();
									}
									 cerSolicitudCertificadosBeans.add(solicitudCertificadoBeanAux);
					        	
									 hashCertificadosPorDestinatario.put(idPersonaDestinataria,cerSolicitudCertificadosBeans);
									
				        	  }catch (SIGAException e) {
				        			log.addLog(new String[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), "No se ha podido generar el fichero de Factura, posiblemente sea un fallo al obtener el fichero firmado"});
							    	  /** Escribiendo fichero de log **/
									if (log != null)
										log.flush();
									throw new SIGAException("No se ha podido generar el fichero de Factura");
							}
						}
				    }
				}

				} catch (SIGAException e) {
					ClsLogging.writeFileLog(e.getMessage(),3);
				}
			}
			
			EnvEnviosBean enviosBean = null;

			Iterator<Long> iteratorDestinatarios = hashCertificadosPorDestinatario.keySet().iterator();
			while (iteratorDestinatarios.hasNext()) {
				StringBuilder datosSolicitudError = null;
				try {
				Long keyPersonaDestinataria = (Long) iteratorDestinatarios.next();

				List<CerSolicitudCertificadosBean> solicitudCertificadosBeans = (List<CerSolicitudCertificadosBean>) hashCertificadosPorDestinatario.get(keyPersonaDestinataria);
				Vector<Documento> certificadosVector = new Vector<Documento>();
				for (int i = 0; i < solicitudCertificadosBeans.size(); i++) {
					CerSolicitudCertificadosBean cerSolicitudCertificadosBean = solicitudCertificadosBeans.get(i);
					if(i==0){
						enviosBean = new EnvEnviosBean();
						enviosBean.setAcuseRecibo(acuseRecibo);
						enviosBean.setIdInstitucion(cerSolicitudCertificadosBean.getIdInstitucion());
						enviosBean.setGenerarDocumento("C");
						enviosBean.setImprimirEtiquetas("1");
						enviosBean.setIdEnvio(enviosAdm.getNewIdEnvio(userBean.getLocation()));
						String nombreEnvio = comunicacionAsunto+" "+ cerSolicitudCertificadosBean.getInstitucionAbreviatura();
						// trunco la descripción
						if (nombreEnvio.length() > 200)
							nombreEnvio = nombreEnvio.substring(0, 99);
	
						enviosBean.setDescripcion(nombreEnvio);
						enviosBean.setIdTipoEnvios(Integer.valueOf(idTipoEnvio));
						enviosBean.setIdPlantillaEnvios(Integer.valueOf(idPlantilla));
						if (idPlantillaGeneracion != null && !idPlantillaGeneracion.equals("")) {
							enviosBean.setIdPlantilla(Integer.valueOf(idPlantillaGeneracion));
						} else {
							enviosBean.setIdPlantilla(null);
						}
						enviosBean.setFechaProgramada(fechaProgramada);
						enviosBean.setFechaCreacion("SYSDATE");
						if (fechaProgramada == null || fechaProgramada.equals(""))
							enviosBean.setIdEstado(new Integer(EnvEnviosAdm.ESTADO_INICIAL));
						else {
							if (enviosBean.getIdTipoEnvios() != null && !enviosBean.getIdTipoEnvios().equals(EnvTipoEnviosAdm.K_CORREO_ELECTRONICO)&& !enviosBean.getIdTipoEnvios().equals(EnvTipoEnviosAdm.K_DOCUMENTACIONLETRADO))
								enviosBean.setIdEstado(new Integer(EnvEnviosAdm.ESTADO_INICIAL));
							else
								enviosBean.setIdEstado(new Integer(EnvEnviosAdm.ESTADO_PENDIENTE_AUTOMATICO));
						}

						
	
					}
					certificadosVector.add(cerSolicitudCertificadosBean.getCertificado());
					
				}
				Envio envio = new Envio(enviosBean, userBean);
				CenDireccionesAdm dirAdm;
				CenDireccionesBean dirBean;
				// obteniendo la direccion
				dirAdm = new CenDireccionesAdm(userBean);
				dirBean = dirAdm.obtenerDireccionPorTipo(keyPersonaDestinataria.toString(), String.valueOf(enviosBean.getIdInstitucion()), enviosBean.getIdTipoEnvios().toString()); 
				if(dirBean != null){
					envio.generarEnvioCertificado(keyPersonaDestinataria.toString(),	certificadosVector, enviosBean);
						
					solicitudCertificadosBeans = (List<CerSolicitudCertificadosBean>) hashCertificadosPorDestinatario.get(keyPersonaDestinataria);
					for (CerSolicitudCertificadosBean cerSolicitudCertificadosBean  : solicitudCertificadosBeans) {
						cerSolicitudCertificadosBean.setFechaEnvio("SYSDATE");
						if (!admCer.updateDirect(cerSolicitudCertificadosBean)) {
							throw new ClsExceptions("No se ha podido actualizar la fecha de envío. envio: " + cerSolicitudCertificadosBean.getIdSolicitud() + " Error: " + admCer.getError());
						}		
					}
				}else{
					//Como no hay dirección habrá que poner log de error a todos los certificados seleccionados que tienen la misma dirección (dirección que no existe).
					//Es necesario realizar el for ya que si no se realiza sólo se pondrá el log de error en una solicitud.
					for (int i = 0; i < solicitudCertificadosBeans.size(); i++) {
						//Creamos el log de error
				        log = LogFileWriter.getLogFileWriter(admSolicitud.obtenerRutaLogError(solicitudCertificadosBeans.get(i)),solicitudCertificadosBeans.get(i).getIdSolicitud()+"-LogError");
						log.clear();
						log.addLog(new String[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), "La dirección del destinatario no es correcta"});
				    	  /** Escribiendo fichero de log **/
						if (log != null)
							log.flush();
					}
				}
					
					
				
				// Enviado. Ahora ponemos la fecha de envio
				
				} catch (SIGAException e) {
					ClsLogging.writeFileLog("Error envio Certificado", 3);
				}
				

			}
			if (errores.toString().equals("")) {
				mensaje = "messages.inserted.success";
			} else {
				mensaje = UtilidadesString.getMensaje("messages.envios.envioCertificadosMasivo.success", new String[] {errores.toString() }, userBean.getLanguage());
			}

		} catch (Exception e) {
			ClsLogging.writeFileLog("messages.general.error", 10);
		}
		ClsLogging.writeFileLog("DefinirEnviosAction:fin insertarEnvioModalCertificado. IdInstitucion:" + userBean.getLocation(), 10);
		return "exito";
	}
	
	
}