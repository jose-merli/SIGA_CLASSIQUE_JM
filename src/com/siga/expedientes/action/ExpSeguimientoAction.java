/*
 * Created on Jan 18, 2005
 * @author emilio.grau
 *
 */
package com.siga.expedientes.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.AdmInformeBean;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.CenInstitucionBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.EnvDestProgramInformesAdm;
import com.siga.beans.EnvDestProgramInformesBean;
import com.siga.beans.EnvEnvioProgramadoAdm;
import com.siga.beans.EnvEnvioProgramadoBean;
import com.siga.beans.EnvInformesGenericosAdm;
import com.siga.beans.EnvInformesGenericosBean;
import com.siga.beans.EnvProgramInformesAdm;
import com.siga.beans.EnvProgramInformesBean;
import com.siga.beans.EnvValorCampoClaveAdm;
import com.siga.beans.EnvValorCampoClaveBean;
import com.siga.beans.ExpAlertaAdm;
import com.siga.beans.ExpAnotacionAdm;
import com.siga.beans.ExpAnotacionBean;
import com.siga.beans.ExpCampoTipoExpedienteAdm;
import com.siga.beans.ExpCampoTipoExpedienteBean;
import com.siga.beans.ExpDenunciadoAdm;
import com.siga.beans.ExpDenunciadoBean;
import com.siga.beans.ExpDestinatariosAvisosAdm;
import com.siga.beans.ExpDestinatariosAvisosBean;
import com.siga.beans.ExpEstadosAdm;
import com.siga.beans.ExpEstadosBean;
import com.siga.beans.ExpExpedienteAdm;
import com.siga.beans.ExpExpedienteBean;
import com.siga.beans.ExpFasesAdm;
import com.siga.beans.ExpFasesBean;
import com.siga.beans.ExpTipoExpedienteAdm;
import com.siga.beans.ExpTipoExpedienteBean;
import com.siga.beans.ExpTiposAnotacionesAdm;
import com.siga.beans.ExpTiposAnotacionesBean;
import com.siga.envios.EnvioInformesGenericos;
import com.siga.expedientes.form.ExpSeguimientoForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.servlets.SIGASvlProcesoAutomaticoRapido;

/**
 * Action de las anotaciones de un expediente
 */
public class ExpSeguimientoAction extends MasterAction {
	
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException, ClsExceptions {

		try {
			String idInstitucion = request.getParameter("idInstitucion");
			String idInstitucion_TipoExpediente = request.getParameter("idInstitucion_TipoExpediente");
			String idTipoExpediente = request.getParameter("idTipoExpediente");
			String numExpediente = request.getParameter("numeroExpediente");
			String anioExpediente = request.getParameter("anioExpediente");
			
			if(numExpediente==null || numExpediente.equals(""))
	           numExpediente = (String) request.getSession().getAttribute("numeroExpedienteSession");
	   
			if(anioExpediente==null || anioExpediente.equals(""))
	           anioExpediente = (String) request.getSession().getAttribute("anioExpedienteSession");			
			
			String editable = (String)request.getParameter("editable");
			boolean isEditable = editable.equals("1")? true : false;
			
			ExpSeguimientoForm form = (ExpSeguimientoForm) formulario;
			// Limpiamos los filtros de búsqueda introducidos anteriormente
			form.setFechaDesde("");
			form.setFechaHasta("");
			form.setIdInstitucion(idInstitucion);
			form.setIdInstitucionTipoExpediente(idInstitucion_TipoExpediente);
			form.setIdTipoExpediente(idTipoExpediente);
			form.setNumeroExpediente(numExpediente);
			form.setAnioExpediente(anioExpediente);
			form.setEditable(new Boolean(isEditable));
			
			
			//Recuperamos el nombre del denunciado        
			ExpDenunciadoAdm denunciadoAdm = new ExpDenunciadoAdm (this.getUserBean(request));
	        CenPersonaBean denunciadoPpal = denunciadoAdm.getPersonaDenunciadoById(Integer.valueOf(idInstitucion), Integer.valueOf(idInstitucion_TipoExpediente), Integer.valueOf(idTipoExpediente), numExpediente,Integer.valueOf(anioExpediente), ExpDenunciadoBean.ID_DENUNCIADO_PRINCIPAL);
	        String nombrePersona = denunciadoPpal.getNombreCompleto();
	        request.setAttribute("denunciado", nombrePersona);
	
	        String denunciado="";
			//Chapuza para identificar si se llama denunciante o impugnante
			StringBuffer sbWhere= new StringBuffer("where ");
			sbWhere.append(ExpCampoTipoExpedienteBean.C_IDINSTITUCION).append("=").append(idInstitucion)
				 .append(" and ").append(ExpCampoTipoExpedienteBean.C_IDTIPOEXPEDIENTE).append("=").append(idTipoExpediente)
				 .append(" and ").append(ExpCampoTipoExpedienteBean.C_IDCAMPO).append("=").append(ClsConstants.IDCAMPO_TIPOEXPEDIENTE_DENUNCIANTE)
				 .append(" and ").append(ExpCampoTipoExpedienteBean.C_NOMBRE).append(" like ").append("'%impugnante%'");
			ExpCampoTipoExpedienteAdm campoTipoExpedienteAdm = new ExpCampoTipoExpedienteAdm (this.getUserBean(request));
			Vector resultado=campoTipoExpedienteAdm.select(sbWhere.toString());
	        if (resultado!=null && resultado.size()>0) {
	        	denunciado=UtilidadesString.getMensajeIdioma(this.getLenguaje(request), "expedientes.auditoria.literal.impugnado");
	    	} else {
	        	denunciado=UtilidadesString.getMensajeIdioma(this.getLenguaje(request), "expedientes.auditoria.literal.denunciado");
	        }

	        StringBuffer tituloVentana = new StringBuffer();
	        tituloVentana.append(denunciado).append(": ").append(nombrePersona).append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
	        			 .append(UtilidadesString.getMensajeIdioma(this.getLenguaje(request), "expedientes.auditoria.literal.tipo"))
	        			 .append(": ").append((String)request.getParameter("nombreTipoExpediente")).append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
	        			 .append(UtilidadesString.getMensajeIdioma(this.getLenguaje(request), "expedientes.auditoria.literal.nexpediente"))
	        			 .append(": ").append((String)request.getParameter("anioExpediente")).append(" / ").append((String)request.getParameter("numeroExpediente"));

        	form.setTituloVentana(tituloVentana.toString());
        	
        	
        	
		} catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null); 
		}
		
		return("inicio");
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrir(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
    protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{	
        try{
	        //ExpSeguimientoForm form = (ExpSeguimientoForm)formulario;
	        ExpAnotacionAdm anotAdm = new ExpAnotacionAdm (this.getUserBean(request));   
	        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
	        
			//Datos generales para todas las pestanhas
			
			//String editable = request.getParameter("editable");
			//boolean edit = editable.equals("1")?true:false;
			
			//Recuperamos el nombre del denunciado
	        ExpSeguimientoForm form = (ExpSeguimientoForm) formulario;
	        
	        
	         
	    	
	        
	        
			Vector datos = anotAdm.getAnotacionesExpediente(form,userBean);
	
	        request.setAttribute("datos", datos); 
	        
        }catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null); 
		}
        
        return "resultado";
	}
    
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#editar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
	    
	    return mostrarRegistro(mapping, formulario, request, response, true);
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#ver(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
	    
	    return mostrarRegistro(mapping, formulario, request, response, false);
	}
	

	/** 
	 * Funcion que muestra el formulario en modo consulta o edicion
	 * @param  mapping
	 * @param  formulario
	 * @param  request
	 * @param  response
	 * @param  bEditable
	 * @exception  ClsExceptions  En cualquier caso de error
	 * @return String para el forward
	 */
	protected String mostrarRegistro(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response, boolean bEditable) throws SIGAException
	{
		try{
		    ExpSeguimientoForm form = (ExpSeguimientoForm)formulario;
	        ExpAnotacionAdm anotAdm = new ExpAnotacionAdm (this.getUserBean(request));
			HttpSession ses = request.getSession();
			
	        Vector vOcultos = form.getDatosTablaOcultos(0);		
	
			String idInstitucion = (String)vOcultos.elementAt(0);
			String idInstitucion_TipoExpediente = (String)vOcultos.elementAt(1);
			String idTipoExpediente = (String)vOcultos.elementAt(2);
	        String numeroExpediente = (String)vOcultos.elementAt(3);
	        String anioExpediente= (String)vOcultos.elementAt(4);
	        String idAnotacion = (String)vOcultos.elementAt(5);
	        String fase = UtilidadesString.mostrarDatoJSP((String)vOcultos.elementAt(6));
	        String estado = UtilidadesString.mostrarDatoJSP((String)vOcultos.elementAt(7));
	        String usuario = (String)vOcultos.elementAt(8);
	        String tipoAnotacion =UtilidadesString.mostrarDatoJSP((String)vOcultos.elementAt(9));
	        String idFase =(String)vOcultos.elementAt(10); 
	        String idEstado =(String)vOcultos.elementAt(11); 
	        
			Hashtable hash = new Hashtable();
			hash.put(ExpAnotacionBean.C_IDINSTITUCION,idInstitucion);
			hash.put(ExpAnotacionBean.C_IDINSTITUCION_TIPOEXPEDIENTE,idInstitucion_TipoExpediente);
			hash.put(ExpAnotacionBean.C_IDTIPOEXPEDIENTE,idTipoExpediente);
			hash.put(ExpAnotacionBean.C_NUMEROEXPEDIENTE,numeroExpediente);
			hash.put(ExpAnotacionBean.C_ANIOEXPEDIENTE,anioExpediente);
			hash.put(ExpAnotacionBean.C_IDANOTACION,idAnotacion);
			
	        Vector datos = anotAdm.select(hash);
	        ExpAnotacionBean bean = (ExpAnotacionBean)datos.elementAt(0);
	        
	        //hacemos los set del formulario
	        form.setFecha(GstDate.getFormatedDateShort("",bean.getFechaAnotacion()));
	        form.setTipoAnotacion(tipoAnotacion);
	        form.setUsuario(usuario);
	        form.setFase(fase);
	        form.setEstado(estado);
	        form.setDescripcion(bean.getDescripcion());
	        form.setRegentrada(bean.getRegEntrada());
	        form.setRegsalida(bean.getRegSalida());
			form.setFechaInicioEstado(GstDate.getFormatedDateShort("",bean.getFechaInicioEstado()));
			form.setFechaFinEstado(GstDate.getFormatedDateShort("",bean.getFechaFinEstado()));

	        //form.setIdTipoAnotacion(String.valueOf(bean.getIdTipoAnotacion()));
	                     
	        if (bEditable){
	        	request.setAttribute("accion","edicion");
	        	request.setAttribute("idInstitucion_TipoExpediente",idInstitucion_TipoExpediente);
				request.setAttribute("idTipoExpediente",idTipoExpediente);
				request.setAttribute("idFase",idFase);
				request.setAttribute("idEstado",idEstado);
				request.setAttribute("idTipoAnotacion",String.valueOf(bean.getIdTipoAnotacion()));
	        	
	        	HashMap datosExpediente = (HashMap)ses.getAttribute("DATABACKUP");
	        	datosExpediente.put("datosParticulares",bean);
	        	ses.setAttribute("DATABACKUP",datosExpediente);
	        }else{
	        	request.setAttribute("idInstitucion_TipoExpediente",idInstitucion_TipoExpediente);
				request.setAttribute("idTipoExpediente",idTipoExpediente);
				request.setAttribute("idFase",idFase);
				request.setAttribute("idEstado",idEstado);
				request.setAttribute("idTipoAnotacion",String.valueOf(bean.getIdTipoAnotacion()));
	        	request.setAttribute("accion","consulta");
	        }
	       // enviar(bean,idInstitucion,idTipoExpediente,idInstitucion_TipoExpediente,((UsrBean)request.getSession().getAttribute(("USRBEAN"))));
	        request.setAttribute("automatico", bean.getAutomatico());
	        
		}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null); 
		}
        
		return "mostrar";
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#modificar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
	    
		try{
		    // Recuperamos los datos anteriores de la sesión
		    HttpSession ses=request.getSession();
		    HashMap datosExpediente = (HashMap)ses.getAttribute("DATABACKUP");
		    ExpAnotacionBean bean=(ExpAnotacionBean)datosExpediente.get("datosParticulares");	    
		    
		    ExpSeguimientoForm form = (ExpSeguimientoForm)formulario;
	        ExpAnotacionAdm anotAdm = new ExpAnotacionAdm (this.getUserBean(request));
	        
	        // Actualizamos los datos del expediente
	        bean.setFechaAnotacion(form.getFecha().equals("")?"sysdate":GstDate.getApplicationFormatDate("",form.getFecha()));
	        bean.setDescripcion(form.getDescripcion());
	        bean.setRegEntrada(form.getRegentrada());
	        bean.setRegSalida(form.getRegsalida());
	        bean.setFechaInicioEstado(GstDate.getApplicationFormatDate("",form.getFechaInicioEstado()));
	        bean.setIdTipoAnotacion(new Integer(form.getIdTipoAnotacion()));
	        
	        /*if (anotAdm.update(bean)){
	        	return exitoModal("messages.updated.success",request);
	        }else{
	        	return exito("messages.updated.error",request);
	        }*/
	        anotAdm.update(bean);
	        
		}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null); 
		}
		return exitoModal("messages.updated.success",request);
	}
	
	protected String borrar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
	
		try{
			ExpSeguimientoForm form = (ExpSeguimientoForm)formulario;
		    	    
		    ExpAnotacionAdm anotAdm = new ExpAnotacionAdm (this.getUserBean(request));
		    
		    Vector vOcultos = form.getDatosTablaOcultos(0);
		    
		    Hashtable hash = new Hashtable();
		    	    
		    hash.put(ExpAnotacionBean.C_IDINSTITUCION, (String)vOcultos.elementAt(0));
		    hash.put(ExpAnotacionBean.C_IDINSTITUCION_TIPOEXPEDIENTE, (String)vOcultos.elementAt(1));
		    hash.put(ExpAnotacionBean.C_IDTIPOEXPEDIENTE, (String)vOcultos.elementAt(2));	    
		    hash.put(ExpAnotacionBean.C_NUMEROEXPEDIENTE, (String)vOcultos.elementAt(3));    
		    hash.put(ExpAnotacionBean.C_ANIOEXPEDIENTE, (String)vOcultos.elementAt(4));
		    hash.put(ExpAnotacionBean.C_IDANOTACION, (String)vOcultos.elementAt(5));
		    
		    /*if (anotAdm.delete(hash)){
	        	return exitoRefresco("messages.deleted.success",request);
	        }else{
	        	return exito("messages.deleted.error",request);
	        }*/
		    anotAdm.delete(hash);
		}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null); 
		}
		return exitoRefresco("messages.deleted.success",request);
	
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#nuevo(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException { 	
        
		try{
		    ExpSeguimientoForm form = (ExpSeguimientoForm)formulario;
		    form.setDescripcion("");
		  //metemos al formulario la fecha de hoy por defecto
			GstDate gstDate = new GstDate();
			String fecha = gstDate.parseDateToString(new Date(),"dd/MM/yyyy", this.getLocale(request)); 
			form.setFecha(fecha);
			
		    
		    form.setTipoAnotacion("");
		    form.setRegentrada("");
		    form.setRegsalida("");
//		    UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));   
		    
		    //Recuperamos datos del expediente en cuestión	
		    HashMap datosExpediente = (HashMap)request.getSession().getAttribute("DATABACKUP");
		    Hashtable hash = (Hashtable)datosExpediente.get("datosGenerales");
		    
		    ExpExpedienteAdm expAdm=new ExpExpedienteAdm (this.getUserBean(request));
		    	  
			Vector datosExp =expAdm.select(hash);
			ExpExpedienteBean expBean = (ExpExpedienteBean)datosExp.elementAt(0);
			
			//Recuperamos el nombre de la fase y el estado		
		    if (expBean.getIdFase()!=null){
			    //FASE
			    ExpFasesAdm faseAdm = new ExpFasesAdm (this.getUserBean(request));		
				Hashtable hashFase = new Hashtable();
				
				hashFase.put(ExpFasesBean.C_IDINSTITUCION,hash.get(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE));
				hashFase.put(ExpFasesBean.C_IDTIPOEXPEDIENTE,hash.get(ExpExpedienteBean.C_IDTIPOEXPEDIENTE));
				hashFase.put(ExpFasesBean.C_IDFASE,expBean.getIdFase());
				Vector datosFase = faseAdm.select(hashFase);
				ExpFasesBean faseBean = (ExpFasesBean)datosFase.elementAt(0);
				
				form.setIdFase(faseBean.getIdFase().toString());
				form.setFase(faseBean.getNombre());
		    }else{
		    	form.setIdFase("");
				form.setFase("");
		    }
		    
			if 	(expBean.getIdEstado()!=null && expBean.getIdFase()!=null){	
				//ESTADO
				ExpEstadosAdm estadoAdm = new ExpEstadosAdm (this.getUserBean(request));		
				Hashtable hashEstado = new Hashtable();
				
				hashEstado.put(ExpEstadosBean.C_IDINSTITUCION,hash.get(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE));
				hashEstado.put(ExpEstadosBean.C_IDTIPOEXPEDIENTE,hash.get(ExpExpedienteBean.C_IDTIPOEXPEDIENTE));
				hashEstado.put(ExpEstadosBean.C_IDFASE,expBean.getIdFase());
				hashEstado.put(ExpEstadosBean.C_IDESTADO,expBean.getIdEstado());
				Vector datosEstado = estadoAdm.select(hashEstado);
				ExpEstadosBean estadoBean = (ExpEstadosBean)datosEstado.elementAt(0);
				
				form.setIdEstado(estadoBean.getIdEstado().toString());
				form.setEstado(estadoBean.getNombre());		
			}else{
				form.setIdEstado("");
				form.setEstado("");		
			}
			//fin selects
			
			form.setFechaInicioEstado(GstDate.getFormatedDateShort("",expBean.getFechaInicialEstado()));
			form.setFechaFinEstado(GstDate.getFormatedDateShort("",expBean.getFechaFinalEstado()));

			
			//Comprobamos que existen tipos de anotaciones definidos para este tipo de expediente
			ExpTiposAnotacionesAdm tAnotAdm = new ExpTiposAnotacionesAdm (this.getUserBean(request));
			
			String a_idInstitucion = String.valueOf(expBean.getIdInstitucion_tipoExpediente());
			String a_idTipoExpediente = String.valueOf(expBean.getIdTipoExpediente());
			String a_idfase = (expBean.getIdFase())!=null ?String.valueOf(expBean.getIdFase()):null;
			String a_idestado = (expBean.getIdEstado())!=null ?String.valueOf(expBean.getIdEstado()):null;
			
			Vector datosTipoAnot = tAnotAdm.selectTiposAnotaciones(a_idInstitucion,a_idTipoExpediente,a_idfase,a_idestado);
			
			if (datosTipoAnot.isEmpty()){
				return "errorAnotacion";
			}
			
			//usuario
			//form.setUsuario(userBean.getUserDescription());
			//form.setIdUsuario(userBean.getUserName());	
			form.setUsuario((this.getUserBean(request)).getUserDescription());
			form.setIdUsuario(this.getUserName(request).toString());
			
			request.setAttribute("accion","nuevo");
			request.setAttribute("idInstitucion_TipoExpediente",hash.get(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE));
			request.setAttribute("idTipoExpediente",hash.get(ExpExpedienteBean.C_IDTIPOEXPEDIENTE));
			request.setAttribute("idFase",expBean.getIdFase()!=null ? expBean.getIdFase().toString():null);
			request.setAttribute("idEstado",expBean.getIdEstado()!=null ? expBean.getIdEstado().toString():null);
			
		}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null); 
		}
			
		return "mostrar";
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#insertar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
		
		UserTransaction tx =null;
		
		try{
			UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));   
		    
		    ExpAnotacionAdm anotAdm = new ExpAnotacionAdm(this.getUserBean(request));
		    ExpSeguimientoForm form = (ExpSeguimientoForm)formulario;
		    
		    HashMap datosExpediente = (HashMap)request.getSession().getAttribute("DATABACKUP");
		    Hashtable hash = (Hashtable)datosExpediente.get("datosGenerales");
		    
		    //Rellenamos el nuevo Bean	    
		    ExpAnotacionBean anotBean = new ExpAnotacionBean();	    
		    
		    anotBean.setIdInstitucion(Integer.valueOf((String)hash.get(ExpExpedienteBean.C_IDINSTITUCION)));
		    anotBean.setIdInstitucion_TipoExpediente(Integer.valueOf((String)hash.get(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE)));
		    anotBean.setIdTipoExpediente(Integer.valueOf((String)hash.get(ExpExpedienteBean.C_IDTIPOEXPEDIENTE)));
		    anotBean.setIdTipoAnotacion(Integer.valueOf(form.getIdTipoAnotacion()));	    
		    anotBean.setFechaAnotacion(form.getFecha().equals("")?"sysdate":GstDate.getApplicationFormatDate("",form.getFecha()));
		    anotBean.setDescripcion(form.getDescripcion().equals("")?null:form.getDescripcion());
		    anotBean.setRegEntrada(form.getRegentrada().equals("")?null:form.getRegentrada());
		    anotBean.setRegSalida(form.getRegsalida().equals("")?null:form.getRegsalida());
		    anotBean.setIdEstado(form.getIdEstado().equals("")?null:Integer.valueOf(form.getIdEstado()));
		    anotBean.setIdFase(form.getIdFase().equals("")?null:Integer.valueOf(form.getIdFase()));
		    anotBean.setNumeroExpediente(Integer.valueOf((String)hash.get(ExpExpedienteBean.C_NUMEROEXPEDIENTE)));
		    anotBean.setAnioExpediente(Integer.valueOf((String)hash.get(ExpExpedienteBean.C_ANIOEXPEDIENTE)));
		    anotBean.setIdUsuario(this.getUserName(request));
		    anotBean.setIdInstitucion_Usuario(this.getIDInstitucion(request));
		    anotBean.setAutomatico("N");
		    anotBean.setFechaInicioEstado(GstDate.getApplicationFormatDate("",form.getFechaInicioEstado()));
		    anotBean.setFechaFinEstado(GstDate.getApplicationFormatDate("",form.getFechaFinEstado()));
		    
		    
		    //Nuevo idAnotacion
		    Hashtable datosAnot = new Hashtable();
		    datosAnot.put(ExpAnotacionBean.C_IDINSTITUCION,(String)hash.get(ExpExpedienteBean.C_IDINSTITUCION));
		    datosAnot.put(ExpAnotacionBean.C_IDINSTITUCION_TIPOEXPEDIENTE,(String)hash.get(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE));
		    datosAnot.put(ExpAnotacionBean.C_IDTIPOEXPEDIENTE,(String)hash.get(ExpExpedienteBean.C_IDTIPOEXPEDIENTE));
		    datosAnot.put(ExpAnotacionBean.C_NUMEROEXPEDIENTE,(String)hash.get(ExpExpedienteBean.C_NUMEROEXPEDIENTE));
		    datosAnot.put(ExpAnotacionBean.C_ANIOEXPEDIENTE,(String)hash.get(ExpExpedienteBean.C_ANIOEXPEDIENTE));
		    
		    anotBean.setIdAnotacion(anotAdm.getNewIdAnotacion(datosAnot));
		    
		    //Iniciamos la transacción
		    tx = userBean.getTransactionPesada();
		    tx.begin();  
		    
		    //Ahora procedemos a insertarlo
		    if (anotAdm.insert(anotBean)){	    	
		    	ExpExpedienteAdm expAdm = new ExpExpedienteAdm (this.getUserBean(request));
		    	ExpExpedienteBean expBean = (ExpExpedienteBean) expAdm.select(hash).elementAt(0);
		    	ExpAlertaAdm alertaAdm = new ExpAlertaAdm (this.getUserBean(request));
		    	
		    	//Si el expediente no pertenece a esta institucion, insertamos una alerta
		    	if (!expBean.getIdInstitucion().toString().equals(userBean.getLocation())){
		    		CenInstitucionAdm instAdm = new CenInstitucionAdm (this.getUserBean(request));
		    		Hashtable hInst = new Hashtable();
		    		hInst.put(CenInstitucionBean.C_IDINSTITUCION,userBean.getLocation());
		    		CenInstitucionBean instBean = (CenInstitucionBean) instAdm.select(hInst).elementAt(0);
		    		try{
		    			alertaAdm.insertarAlerta(expBean,instBean.getAbreviatura()+" ha anhadido una anotación nueva");
		    		}catch(Exception e){
		    			throw new ClsExceptions(e,"Error al insertar una alerta"); 
		    		}
		    	}
		    	
		    	//Si la anotación lleva un mensaje asociado (ExpTipoAnotacion.mensaje), insertamos una alerta con este mensaje	   
		    	ExpTiposAnotacionesAdm taAdm = new ExpTiposAnotacionesAdm(this.getUserBean(request));
		    	Hashtable hTA = new Hashtable();
		    	hTA.put(ExpTiposAnotacionesBean.C_IDINSTITUCION,hash.get(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE));
		    	hTA.put(ExpTiposAnotacionesBean.C_IDTIPOEXPEDIENTE,hash.get(ExpExpedienteBean.C_IDTIPOEXPEDIENTE));
		    	hTA.put(ExpTiposAnotacionesBean.C_IDTIPOANOTACION,form.getIdTipoAnotacion());
		    	ExpTiposAnotacionesBean taBean = (ExpTiposAnotacionesBean) taAdm.select(hTA).elementAt(0);
		    	
		    	if (taBean.getMensaje()!=null && !taBean.getMensaje().equals("")){
		    		try{
		    			alertaAdm.insertarAlerta(expBean,taBean.getMensaje());
		    		}catch(Exception e){
		    			throw new ClsExceptions(e,"Error al insertar una alerta"); 
		    		}
		    	}
		    	tx.commit();
		    	
		    }else{
		    	throw new ClsExceptions(anotAdm.getError());
		    }
		}catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,tx); 
		}
		return exitoModal("messages.inserted.success",request);
	}
	

}
