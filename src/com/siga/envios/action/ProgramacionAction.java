/*
 * Created on Apr 06, 2005
 * @author jmgrau
 *
 */
package com.siga.envios.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.EnvDestinatariosBean;
import com.siga.beans.EnvEnviosAdm;
import com.siga.beans.EnvEnviosBean;
import com.siga.beans.EnvEstadoEnvioAdm;
import com.siga.beans.EnvHistoricoEstadoEnvioAdm;
import com.siga.beans.EnvHistoricoEstadoEnvioBean;
import com.siga.beans.EnvTipoEnviosAdm;
import com.siga.beans.EnvTipoEnviosBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.envios.Envio;
import com.siga.envios.form.DefinirEnviosForm;
import com.siga.envios.form.ProgramacionForm;
import com.siga.envios.service.IntercambiosService;
import com.siga.envios.service.IntercambiosServiceDispatcher;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gui.processTree.SIGAPTConstants;


/**
 * 
 * Clase action para la programación del envio.<br/>
 * Gestiona la fecha y hora del envio, así como los parámetros de generación y etiquetas. 
 *
 */
public class ProgramacionAction extends MasterAction {
    
    public ActionForward executeInternal (ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
    	String mapDestino = "exception";
    	MasterForm miForm = null;

    	try 
    	{ 
	        miForm = (MasterForm) formulario;
	        
	        if (miForm != null){
	            String modo = miForm.getModo();

	            if (modo == null || modo.equalsIgnoreCase("") || modo.equalsIgnoreCase("abrir")){
	                mapDestino = abrir(mapping, miForm, request, response);
	            }else if (modo.equalsIgnoreCase("descargar")){
	                mapDestino = descargar(mapping, miForm, request, response);
	            }else if (modo.equalsIgnoreCase("generarEtiquetas")){
	                mapDestino = generarEtiquetas(mapping, miForm, request, response);
	            }else if (modo.equalsIgnoreCase("procesarEnvio")){
	                mapDestino = procesarEnvio(mapping, miForm, request, response);
	            }else if (modo.equalsIgnoreCase("descargarLogErrores")){
	            	mapDestino = descargarLogErrores(mapping, miForm, request, response);
	            }else{
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
    	    throw new SIGAException("messages.general.error",e,new String[] {"modulo.certificados"});
    	} 
	}

    protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{	                  
		//Aplicar acceso
        String editable;
        if(request.getParameter("acceso")!= null && request.getParameter("acceso").equalsIgnoreCase("Ver")) {
            HttpSession ses=request.getSession();
        	UsrBean user=(UsrBean)ses.getAttribute("USRBEAN");
		    user.setAccessType(SIGAPTConstants.ACCESS_READ);
		    editable="false";
        } else {
            editable="true";
        }
        request.setAttribute("editable",editable);
        ProgramacionForm form = (ProgramacionForm)formulario;
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        
        String idInstitucion = userBean.getLocation();
        String idEnvio = (String)request.getParameter("idEnvio");
        
        //Recuperamos los datos del envio
        Hashtable htPk = new Hashtable();
        htPk.put(EnvDestinatariosBean.C_IDINSTITUCION,idInstitucion);
        htPk.put(EnvDestinatariosBean.C_IDENVIO,idEnvio);        
        
        //Recupero el bean del envio para mostrar el nombre y el tipo
        EnvEnviosAdm envioAdm = new EnvEnviosAdm (this.getUserBean(request));
        EnvTipoEnviosAdm tipoAdm = new EnvTipoEnviosAdm (this.getUserBean(request));
        Vector envio, tipo;
        EnvEnviosBean envioBean = null;
        try {
            envio = envioAdm.selectByPK(htPk);   
           
         // Obtengo el pathFTP
    		
            
            
	        envioBean = (EnvEnviosBean)envio.firstElement();
	        if(envioBean.getIdTipoEnvios()!=null && envioBean.getIdTipoEnvios().intValue()==EnvEnviosAdm.TIPO_CORREO_ORDINARIO){
		        String pathFTP = "";
	    		GenParametrosAdm paramAdm = new GenParametrosAdm(this.getUserBean(request));
	    		try {
	    			pathFTP = paramAdm.getValor(userBean.getLocation(),"ENV","URL_FTP_DESCARGA_ENVIOS_ORDINARIOS","");
	    		} catch (Exception e) {
	    			//
	    		}
	    		
				idEnvio = envioBean.getIdEnvio().toString();
			    String fechaCreacion = envioBean.getFechaCreacion();
				SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
			    Calendar cal = Calendar.getInstance();
				Date d = sdf.parse(fechaCreacion);
		        cal.setTime(d);
			    String anio = String.valueOf(cal.get(Calendar.YEAR));
				String mes = String.valueOf(cal.get(Calendar.MONTH)+1);		
				String dia = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));		
				String directorioFicheros = "/" + anio + "/" + mes + "/" + dia + "/" + idEnvio + "/" ;						
	    		
				request.setAttribute("pathFTPDescarga",pathFTP+directorioFicheros);
				
	    		
	        }
        } catch (Exception e) {
            throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
        }    
	        request.setAttribute("nombreEnv", envioBean.getDescripcion());
	        request.setAttribute("idTipoEnvio", String.valueOf(envioBean.getIdTipoEnvios()));
	        
	        SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
	        Calendar cal = Calendar.getInstance();
			Date d;// = new Date();
			String horas="";
			String minutos="";
			String fecha="";
			form.setAutomatico("false");
			
			try
			{
				if (!envioBean.getFechaProgramada().equals(""))
				{
					//sdf.applyPattern(ClsConstants.DATE_FORMAT_SHORT_SPANISH);
	                d = sdf.parse(envioBean.getFechaProgramada());
	                cal.setTime(d);

	                form.setAutomatico("true");

	                sdf.applyPattern(ClsConstants.DATE_FORMAT_SHORT_SPANISH);
	                fecha = sdf.format(cal.getTime()).toString();
	                horas = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));            
	    	        minutos = String.valueOf(cal.get(Calendar.MINUTE));
		            horas = this.formateoHHMM(horas);
		            minutos = this.formateoHHMM(minutos);
				}
			}
			
			catch (Exception e)
			{
				throwExcp("messages.general.error", new String[] {"modulo.envios"}, e, null);
			}
			/*
            try {
                d = sdf.parse(envioBean.getFechaProgramada());
                cal.setTime(d);
                form.setAutomatico("true");
            } catch (ParseException e1) {                
                form.setAutomatico("false");
            }
            String horas = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));            
	        String minutos = String.valueOf(cal.get(Calendar.MINUTE));
	        try {
	            horas = this.formateoHHMM(horas);
	            minutos = this.formateoHHMM(minutos);
	        } catch (Exception eFormateo){
	            throwExcp("messages.general.error",new String[] {"modulo.envios"},eFormateo,null);
	        }
        	
	        if (envioBean.getFechaProgramada().equals("")) {
                horas = "00";
                minutos = "00";
        	}
        	
	        if (editable.equalsIgnoreCase("false")){
	            try {
	            	if (!envioBean.getFechaProgramada().equals("")) {
	                    horas = this.formateoHHMM(horas);
	                    minutos = this.formateoHHMM(minutos);
	                } else {
	                    horas = "00";
	                    minutos = "00";
	                }
                } catch (ClsExceptions e4) {
                    throwExcp("messages.general.error",new String[] {"modulo.envios"},e4,null);
                }
	        }
	        */
			
	        //sdf.applyPattern(ClsConstants.DATE_FORMAT_SHORT_SPANISH);

            try {
                form.setFechaProgramada(fecha);
    	        form.setHoras(horas);
    	        form.setMinutos(minutos);
    	        form.setImprimirEtiquetas(envioBean.getImprimirEtiquetas());
    	        form.setGenerarDocumento(envioBean.getGenerarDocumento());
    	        form.setIdImpresora(String.valueOf(envioBean.getIdImpresora()));
    	        request.setAttribute("idImpresora",String.valueOf(envioBean.getIdImpresora()));
    	        request.setAttribute("fecha",form.getFechaProgramada());
    	        request.setAttribute("horas",form.getHoras());
    	        request.setAttribute("minutos",form.getMinutos());
    	        if (envioBean.getIdEstado().equals(new Integer(EnvEstadoEnvioAdm.K_ESTADOENVIO_PENDIENTE_AUTOMATICO)))
    	            form.setAutomatico("true");    	        
            } catch (Exception e2) {
                throwExcp("messages.general.error",new String[] {"modulo.envios"},e2,null);
            }
            
	        Hashtable htTipo = new Hashtable();
	        htTipo.put(EnvTipoEnviosBean.C_IDTIPOENVIOS,envioBean.getIdTipoEnvios());
	        try {
                tipo = tipoAdm.selectByPK(htTipo);
                EnvTipoEnviosBean tipoBean = (EnvTipoEnviosBean)tipo.firstElement();	        
    	        request.setAttribute("tipo", tipoBean.getNombre());    	        
            } catch (ClsExceptions e3) {
                throwExcp("messages.general.error",new String[] {"modulo.envios"},e3,null);
            }	        
	        
	        // Por ultimo recuperamos los estados por los que ha pasado el envio
	        EnvHistoricoEstadoEnvioAdm historicoAdm = new EnvHistoricoEstadoEnvioAdm(userBean);
	        try {
				request.setAttribute("estados", historicoAdm.getHistoricoEnvio(envioBean));
			} catch (ClsExceptions e) {
				throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
			}
        
		return "inicio";
	}


	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#modificar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
	    
	    UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
	    String language = userBean.getLanguage();
	    String format = language.equalsIgnoreCase("EN")?ClsConstants.DATE_FORMAT_SHORT_ENGLISH:ClsConstants.DATE_FORMAT_SHORT_SPANISH;
	    GstDate gstDate = new GstDate();
	    ProgramacionForm form = (ProgramacionForm)formulario;
	    EnvEnviosAdm enviosAdm = new EnvEnviosAdm(this.getUserBean(request));
	    
	    
	    Hashtable htPk = new Hashtable();
	    htPk.put(EnvEnviosBean.C_IDENVIO,form.getIdEnvio());
	    htPk.put(EnvEnviosBean.C_IDINSTITUCION,userBean.getLocation());
	    Date dateSistemaFinal =new Date();
	    Date dateFinal =new Date();
	    
	    try{
	        EnvEnviosBean envioBean = (EnvEnviosBean)enviosAdm.selectByPKForUpdate(htPk).firstElement();
	        
	        boolean bAutomatico = form.getAutomatico()!=null && form.getAutomatico().equalsIgnoreCase("true");
	        
	        Calendar cal = Calendar.getInstance();
	        Date dateSistema = new Date();
	        
	        
	        if (form.getAutomatico()!=null && form.getAutomatico().equals("true")) {
		        Date date = gstDate.parseStringToDate(form.getFechaProgramada(),format,request.getLocale());
		        
		        cal.setTime(date);
		        cal.set(Calendar.HOUR,Integer.valueOf(form.getHoras()).intValue());
		        cal.set(Calendar.MINUTE,Integer.valueOf(form.getMinutos()).intValue());
		        SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
		        String fecha = sdf.format(cal.getTime());
		       
	        
	        	envioBean.setFechaProgramada(fecha);
	        	//Solo miramos si el dia de la programacion del envio es anterior a la fecha del sistema (sin tener en cuenta las horas y los minutos)
			        	SimpleDateFormat sdf1 = new SimpleDateFormat(ClsConstants.DATE_FORMAT_SHORT_SPANISH);
			        	String fechaSistema=sdf1.format(dateSistema);
			        	 dateSistemaFinal = sdf1.parse(fechaSistema);
			        	String fechaIntroducida=sdf1.format(date);
			        	dateFinal = sdf1.parse(fechaIntroducida);
			    //    	
	        	
	        	
	        } else {
	        	envioBean.setFechaProgramada("");
	        }
	        
	        //LMS 18/08/2006
	        //La lógica anterior no tenía en cuenta grabar un automático con fecha pasada.
	        /*if (envioBean.getIdEstado().equals(new Integer(EnvEstadoEnvioAdm.K_ESTADOENVIO_PENDIENTE_MANUAL)) && bAutomatico){
	            if (Calendar.getInstance().getTime().after(cal.getTime())){
	                throw new SIGAException("messages.general.error.fechaPosteriorActual");
	            }else{
	                envioBean.setIdEstado(new Integer(EnvEstadoEnvioAdm.K_ESTADOENVIO_PENDIENTE_AUTOMATICO));
	            }
	        } else if (envioBean.getIdEstado().equals(new Integer(EnvEstadoEnvioAdm.K_ESTADOENVIO_PENDIENTE_AUTOMATICO)) && !bAutomatico)
	            envioBean.setIdEstado(new Integer(EnvEstadoEnvioAdm.K_ESTADOENVIO_PENDIENTE_MANUAL));
	        */
	        if (bAutomatico)
		        {
		        /*if (Calendar.getInstance().getTime().after(cal.getTime()))
		        {
		        	throw new SIGAException("messages.general.error.fechaPosteriorActual");
		        }*/
	        	if (dateSistemaFinal.after(dateFinal)){
	        		throw new SIGAException("messages.general.error.fechaPosteriorActual");
	        	}
		        
		        else
		        {
		        	/*if (envioBean.getIdEstado().equals(new Integer(EnvEstadoEnvioAdm.K_ESTADOENVIO_PENDIENTE_MANUAL)))
		        	{
		        		envioBean.setIdEstado(new Integer(EnvEstadoEnvioAdm.K_ESTADOENVIO_PENDIENTE_AUTOMATICO));
		        	}
		        	
		        	else if (envioBean.getIdEstado().equals(new Integer(EnvEstadoEnvioAdm.K_ESTADOENVIO_PENDIENTE_AUTOMATICO)))
		        	{
		        		envioBean.setIdEstado(new Integer(EnvEstadoEnvioAdm.K_ESTADOENVIO_PENDIENTE_MANUAL));
		        	}*/
		        	envioBean.setIdEstado(new Integer(EnvEstadoEnvioAdm.K_ESTADOENVIO_PENDIENTE_AUTOMATICO));
		        }
		    }
	        
	        else
	        {
	        	envioBean.setFechaProgramada("");
	        	envioBean.setIdEstado(new Integer(EnvEstadoEnvioAdm.K_ESTADOENVIO_PENDIENTE_MANUAL));
	        }
	        
	        //envioBean.setGenerarDocumento(form.getGenerarDocumento());
	        envioBean.setImprimirEtiquetas(form.getImprimirEtiquetas());
	        if (form.getIdImpresora()!=null && !form.getIdImpresora().equals(""))
	            envioBean.setIdImpresora(Integer.valueOf(form.getIdImpresora()));
	        
	        enviosAdm.update(envioBean);
	    } catch (Exception e){
			throwExcp("messages.general.error", new String[] {"modulo.envios"}, e, null); 
	    }
	    return exitoRefresco("messages.updated.success",request);
	}
	
	//Metodo para descargar el fichero del envio:
	protected String descargar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException
	{
	    UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
	    
        ProgramacionForm form = (ProgramacionForm)formulario;
        
	    String idInstitucion = userBean.getLocation();
	    String idEnvio = form.getIdEnvio();
	    
	    String nombre = idInstitucion + "_" + idEnvio +"_etiquetas.pdf";
		request.setAttribute("nombreFichero", nombre);
		
		EnvEnviosAdm enviosAdm = new EnvEnviosAdm(this.getUserBean(request));
		String ruta = enviosAdm.getPathEtiquetas(idInstitucion,idEnvio);
		File fEtiquetas = new File(ruta);
		if(fEtiquetas==null || !fEtiquetas.exists()){
			throw new SIGAException("messages.general.error.ficheroNoExiste"); 
		}
		request.setAttribute("rutaFichero", ruta);
		request.setAttribute("generacionOK","OK");
		return "descarga";
	}
	protected String generarEtiquetas(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException
	{
	    UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
	    
        ProgramacionForm form = (ProgramacionForm)formulario;
        
	    String idInstitucion = userBean.getLocation();
	    String idEnvio = form.getIdEnvio();
		EnvEnviosAdm enviosAdm = new EnvEnviosAdm(this.getUserBean(request));
		// Obtengo las claves
		Hashtable htPk = new Hashtable();
		htPk.put(EnvEnviosBean.C_IDINSTITUCION,idInstitucion);
		htPk.put(EnvEnviosBean.C_IDENVIO,idEnvio);
		//obtengo el envio
		EnvEnviosBean envBean = (EnvEnviosBean)enviosAdm.selectByPK(htPk).firstElement();
		// OBTENCION DE DESTINATARIOS 
        /////////////////////////////////////
		EnvEnviosAdm envAdm = new EnvEnviosAdm(this.getUserBean(request));
		Vector vDestinatarios =  envAdm.getDestinatarios(envBean.getIdInstitucion().toString(), envBean.getIdEnvio().toString(), envBean.getIdTipoEnvios().toString());			
		if(vDestinatarios== null)
			throw new SIGAException("gratuita.envio.ordinario.noDestinatario"); 
		String ruta =  enviosAdm.generarEtiquetas(String.valueOf(envBean.getIdInstitucion()),String.valueOf(envBean.getIdEnvio()),enviosAdm.getPathDescargaEnviosOrdinarios(envBean),vDestinatarios);
		request.setAttribute("rutaFichero", ruta);
		request.setAttribute("generacionOK","OK");
		
		return "descarga";
	}
	
	//Formato de fecha para las horas y minutos con 2 digitos:
	private String formateoHHMM(String dato)throws ClsExceptions{
		String salida = null; 
		
		salida = UtilidadesString.formatea(dato,2,true);
		if (salida.length()==1)
			salida = "0"+salida;
		if (salida.equals("0"))
			salida = "00";
		return salida; 
	}
 
	
	protected String procesarEnvio(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) 
	throws SIGAException{
		UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
		UserTransaction tx = null;
			
		try {
			
		    EnvEnviosAdm envAdm = new EnvEnviosAdm(this.getUserBean(request));
			tx = userBean.getTransaction();
			
			// Obtengo las claves
			ProgramacionForm form = (ProgramacionForm)formulario;
			String idInstitucion = userBean.getLocation();	
			String idEnvio = form.getIdEnvio();
			
			Hashtable htPk = new Hashtable();
			htPk.put(EnvEnviosBean.C_IDINSTITUCION,idInstitucion);
			htPk.put(EnvEnviosBean.C_IDENVIO,idEnvio);

			//obtengo el envio
			EnvEnviosBean envBean = (EnvEnviosBean)envAdm.selectByPKForUpdate(htPk).firstElement();
			Envio envio = new Envio(envBean, userBean);
			if(envBean.getIdEstado().compareTo(EnvEstadoEnvioAdm.K_ESTADOENVIO_PROCESANDO)==0){
				throw new SIGAException("messages.envios.procesandoEnvio");
				
			}else{
				envBean.setIdEstado(EnvEstadoEnvioAdm.K_ESTADOENVIO_PROCESANDO);
                envAdm.updateDirect(envBean);
                
				
			}
			// lo proceso
			envio.procesarEnvio(tx);
			
	    }catch (Exception e){
	        this.throwExcp("messages.general.error",new String[] {"modulo.envios"},e,tx);
	    }
	    return exitoRefresco("messages.inserted.success",request);
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
			ProgramacionForm form = (ProgramacionForm)formulario;
			UsrBean user = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));   

			String idInstitucion = user.getLocation();  
			String idEnvio = form.getIdEnvio();
			
			EnvEnviosAdm envioAdm = new EnvEnviosAdm(this.getUserBean(request));
			File fichero = null;
			
			sFicheroLog = envioAdm.getPathEnvio(idInstitucion,idEnvio) + File.separator + "informeEnvio" + ".log.xls";
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

}
