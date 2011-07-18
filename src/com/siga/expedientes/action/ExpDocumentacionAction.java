/*
 * Created on Jan 18, 2005
 * @author emilio.grau
 *
 */
package com.siga.expedientes.action;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.DocuShareHelper;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.ExpCampoTipoExpedienteAdm;
import com.siga.beans.ExpCampoTipoExpedienteBean;
import com.siga.beans.ExpDocumentosAdm;
import com.siga.beans.ExpDocumentosBean;
import com.siga.beans.ExpEstadosAdm;
import com.siga.beans.ExpEstadosBean;
import com.siga.beans.ExpExpedienteAdm;
import com.siga.beans.ExpExpedienteBean;
import com.siga.beans.ExpFasesAdm;
import com.siga.beans.ExpFasesBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.expedientes.form.ExpDocumentacionForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 * Action de la documentación de un expediente
 */
public class ExpDocumentacionAction extends MasterAction {
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrir(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
    protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
    	String salto=null;
    	
        try{
	        ExpDocumentacionForm form = (ExpDocumentacionForm)formulario;
	        
			//Datos generales para todas las pestanhas
			String idInstitucion = request.getParameter("idInstitucion");
			String idInstitucion_TipoExpediente = request.getParameter("idInstitucion_TipoExpediente");
			String idTipoExpediente = request.getParameter("idTipoExpediente");
			String numExpediente = request.getParameter("numeroExpediente");
			String anioExpediente = request.getParameter("anioExpediente");							
	               
			
	        //Recuperamos el nombre del denunciado
	        Hashtable hash = new Hashtable();		
			hash.put(ExpExpedienteBean.C_IDINSTITUCION,idInstitucion);
			hash.put(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE,idInstitucion_TipoExpediente);
			hash.put(ExpExpedienteBean.C_IDTIPOEXPEDIENTE,idTipoExpediente);
			hash.put(ExpExpedienteBean.C_NUMEROEXPEDIENTE,numExpediente);
			hash.put(ExpExpedienteBean.C_ANIOEXPEDIENTE,anioExpediente);
	
	        ExpExpedienteAdm expAdm = new ExpExpedienteAdm(this.getUserBean(request));
	        Vector vExp = expAdm.selectByPK(hash);        
	        CenPersonaAdm personaAdm = new CenPersonaAdm(this.getUserBean(request));
	        Hashtable hashIdPers = new Hashtable();	
	        ExpExpedienteBean expExpedienteBean = (ExpExpedienteBean)vExp.elementAt(0);
	        
			hashIdPers.put(CenPersonaBean.C_IDPERSONA,(expExpedienteBean.getIdPersona()));
	        Vector vPersona = personaAdm.selectByPK(hashIdPers);
	        CenPersonaBean personaBean = (CenPersonaBean) vPersona.elementAt(0);
	        String nombrePersona = personaBean.getNombre() + " " + personaBean.getApellido1() + " " + personaBean.getApellido2();
	        	        
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
	        			 .append(": ").append((String)request.getParameter("numeroExpediente")).append(" / ").append((String)request.getParameter("anioExpediente"));

	        form.setTituloVentana(tituloVentana.toString());
	        
	        GenParametrosAdm parametrosAdm = new GenParametrosAdm(this.getUserBean(request));
            String valor = parametrosAdm.getValor(this.getUserBean(request).getLocation(), ClsConstants.MODULO_GENERAL, "REGTEL", "0");

            
	  	      
		        ExpDocumentosAdm docAdm = new ExpDocumentosAdm (this.getUserBean(request));
	            
		        String where = " WHERE ";
	        	//NOMBRES COLUMNAS PARA LA JOIN
				//Tabla EXP_ESTADOS
				String E_IDINSTITUCION="E."+ExpEstadosBean.C_IDINSTITUCION;
				String E_IDTIPOEXPEDIENTE="E."+ExpEstadosBean.C_IDTIPOEXPEDIENTE;
				String E_IDFASE="E."+ExpEstadosBean.C_IDFASE;
				String E_IDESTADO="E."+ExpEstadosBean.C_IDESTADO;
				
				//Tabla EXP_FASES
				String F_IDINSTITUCION="F."+ExpFasesBean.C_IDINSTITUCION;
				String F_IDTIPOEXPEDIENTE="F."+ExpFasesBean.C_IDTIPOEXPEDIENTE;
				String F_IDFASE="F."+ExpFasesBean.C_IDFASE;
				
				
				where += "D."+ExpDocumentosBean.C_IDINSTITUCION + " = '" + idInstitucion + "' AND ";
				where += "D."+ExpDocumentosBean.C_IDINSTITUCION_TIPOEXPEDIENTE + " = '" + idInstitucion_TipoExpediente + "' AND ";
				where += "D."+ExpDocumentosBean.C_IDTIPOEXPEDIENTE + " = '" + idTipoExpediente + "' AND ";
				where += "D."+ExpDocumentosBean.C_NUMEROEXPEDIENTE + " = '" + numExpediente + "' AND ";
				where += "D."+ExpDocumentosBean.C_ANIOEXPEDIENTE + " = '" + anioExpediente + "' ";
				
			    //join de las tablas DOCUMENTO D, ESTADOS E, FASES F
		        where += "AND D."+ExpDocumentosBean.C_IDINSTITUCION_TIPOEXPEDIENTE+" = "+E_IDINSTITUCION;
		        where += " AND D."+ExpDocumentosBean.C_IDTIPOEXPEDIENTE+" = "+E_IDTIPOEXPEDIENTE;
		        where += " AND D."+ExpDocumentosBean.C_IDFASE+" = "+E_IDFASE;
		        where += " AND D."+ExpDocumentosBean.C_IDESTADO+" = "+E_IDESTADO;
		        
		        where += " AND "+E_IDINSTITUCION+" = "+F_IDINSTITUCION;
		        where += " AND "+E_IDTIPOEXPEDIENTE+" = "+F_IDTIPOEXPEDIENTE;
		        where += " AND "+E_IDFASE+" = "+F_IDFASE;
		        			
				Vector datos = docAdm.selectDocFaseEstado(where);
		
		        request.setAttribute("datos", datos); 
		        
	        	
	        	salto="inicio";
	     
        }catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null); 
        }
        
        return salto;
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
			throws SIGAException{
	    
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
		    ExpDocumentacionForm form = (ExpDocumentacionForm)formulario;
	        ExpDocumentosAdm docAdm = new ExpDocumentosAdm (this.getUserBean(request));
			HttpSession ses = request.getSession();
			
	        Vector vOcultos = form.getDatosTablaOcultos(0);		
	
			String idInstitucion = (String)vOcultos.elementAt(0);
			String idInstitucion_TipoExpediente = (String)vOcultos.elementAt(1);
			String idTipoExpediente = (String)vOcultos.elementAt(2);
			String numExpediente = (String)vOcultos.elementAt(3);
			String anioExpediente = (String)vOcultos.elementAt(4);
	        String idDocumento = (String)vOcultos.elementAt(5);
	        
	        // Se ha quitado el uso de utilidadesString.mostrarDatoJSP porque con algunos acentos (catalán) no se veia bien
//	        String fase = UtilidadesString.mostrarDatoJSP((String)vOcultos.elementAt(6));
//	        String estado = UtilidadesString.mostrarDatoJSP((String)vOcultos.elementAt(7));
	        String fase   = (String)vOcultos.elementAt(6);  
	        String estado = (String)vOcultos.elementAt(7);
	        
			Hashtable hash = new Hashtable();
			hash.put(ExpDocumentosBean.C_IDINSTITUCION,idInstitucion);
			hash.put(ExpDocumentosBean.C_IDINSTITUCION_TIPOEXPEDIENTE,idInstitucion_TipoExpediente);
			hash.put(ExpDocumentosBean.C_IDTIPOEXPEDIENTE,idTipoExpediente);
			hash.put(ExpDocumentosBean.C_NUMEROEXPEDIENTE,numExpediente);
			hash.put(ExpDocumentosBean.C_ANIOEXPEDIENTE,anioExpediente);		
			hash.put(ExpDocumentosBean.C_IDDOCUMENTO,idDocumento);
			
	        Vector datos = docAdm.select(hash);
	        ExpDocumentosBean bean = (ExpDocumentosBean)datos.elementAt(0);
	        
	        //hacemos los set del formulario
	        form.setFase(fase);
	        form.setEstado(estado);
	        form.setDescripcion(bean.getDescripcion());
	        form.setRuta(bean.getRuta());
	        form.setRegentrada(bean.getRegEntrada());
	        form.setRegsalida(bean.getRegSalida());
	                     
	        
	        if (bEditable){
	        	HashMap datosExpediente = (HashMap)ses.getAttribute("DATABACKUP");
	        	datosExpediente.put("datosParticulares",bean);
	        	ses.setAttribute("DATABACKUP",datosExpediente);
	        	request.setAttribute("accion","edicion");
	        }else{
	        	request.setAttribute("accion","consulta");
	        }
	        
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
		    ExpDocumentosBean bean=(ExpDocumentosBean)datosExpediente.get("datosParticulares");	    
		    
		    ExpDocumentacionForm form = (ExpDocumentacionForm)formulario;
	        ExpDocumentosAdm docAdm = new ExpDocumentosAdm (this.getUserBean(request));
	        
	        // Actualizamos los datos del expediente
	        bean.setDescripcion(form.getDescripcion());
	        bean.setRuta(form.getRuta());
	        bean.setRegEntrada(form.getRegentrada());
	        bean.setRegSalida(form.getRegsalida());
	        
	        /*if (docAdm.update(bean)){
	        	return exitoModal("messages.updated.success",request);
	        }else{
	        	return exito("messages.updated.error",request);
	        }*/
	        docAdm.update(bean);
	        
		}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null); 
		}
		return exitoModal("messages.updated.success",request);
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#borrar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
	
		try{
			ExpDocumentacionForm form = (ExpDocumentacionForm)formulario;
		    	    
		    ExpDocumentosAdm docAdm = new ExpDocumentosAdm (this.getUserBean(request));
		    
		    Vector vOcultos = form.getDatosTablaOcultos(0);
		    
		    Hashtable hash = new Hashtable();
		    	    
		    hash.put(ExpDocumentosBean.C_IDINSTITUCION, (String)vOcultos.elementAt(0));
		    hash.put(ExpDocumentosBean.C_IDINSTITUCION_TIPOEXPEDIENTE, (String)vOcultos.elementAt(1));
		    hash.put(ExpDocumentosBean.C_IDTIPOEXPEDIENTE, (String)vOcultos.elementAt(2));	    
		    hash.put(ExpDocumentosBean.C_NUMEROEXPEDIENTE, (String)vOcultos.elementAt(3));
		    hash.put(ExpDocumentosBean.C_ANIOEXPEDIENTE, (String)vOcultos.elementAt(4));
		    hash.put(ExpDocumentosBean.C_IDDOCUMENTO, (String)vOcultos.elementAt(5));
		    
		    /*if (docAdm.delete(hash)){
	        	return exitoRefresco("messages.deleted.success",request);
	        }else{
	        	return exito("messages.deleted.error",request);
	        }*/
		    docAdm.delete(hash);
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
		    ExpDocumentacionForm form = (ExpDocumentacionForm)formulario;
	
		    
		    //Recuperamos la fase y el estado del expediente en cuestión	
		    HashMap datosExpediente = (HashMap)request.getSession().getAttribute("DATABACKUP");
		    Hashtable hash = (Hashtable)datosExpediente.get("datosGenerales");
		    
		    ExpExpedienteAdm expAdm=new ExpExpedienteAdm (this.getUserBean(request));
		    	  
			Vector datosExp =expAdm.select(hash);
			ExpExpedienteBean expBean = (ExpExpedienteBean)datosExp.elementAt(0);
		    
		    
		    //Recuperamos el nombre de la fase y el estado
			//FASE
		    ExpFasesAdm faseAdm = new ExpFasesAdm (this.getUserBean(request));		
			Hashtable hashFase = new Hashtable();
			
			hashFase.put(ExpFasesBean.C_IDINSTITUCION,hash.get(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE));
			hashFase.put(ExpFasesBean.C_IDTIPOEXPEDIENTE,hash.get(ExpExpedienteBean.C_IDTIPOEXPEDIENTE));
			UtilidadesHash.set(hashFase,ExpFasesBean.C_IDFASE,expBean.getIdFase());
			Vector datosFase = faseAdm.select(hashFase);
			if (datosFase.size()!=0) {
			ExpFasesBean faseBean = (ExpFasesBean)datosFase.elementAt(0);
			form.setIdFase(faseBean.getIdFase().toString());
			form.setFase(faseBean.getNombre());
			}		
			//ESTADO
			ExpEstadosAdm estadoAdm = new ExpEstadosAdm (this.getUserBean(request));		
			Hashtable hashEstado = new Hashtable();
			
			hashEstado.put(ExpEstadosBean.C_IDINSTITUCION,hash.get(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE));
			hashEstado.put(ExpEstadosBean.C_IDTIPOEXPEDIENTE,hash.get(ExpExpedienteBean.C_IDTIPOEXPEDIENTE));
			UtilidadesHash.set(hashEstado,ExpEstadosBean.C_IDFASE,expBean.getIdFase());
			UtilidadesHash.set(hashEstado,ExpEstadosBean.C_IDESTADO,expBean.getIdEstado());
			Vector datosEstado = estadoAdm.select(hashEstado);
			if (datosEstado.size()!=0) {
			ExpEstadosBean estadoBean = (ExpEstadosBean)datosEstado.elementAt(0);
			form.setIdEstado(estadoBean.getIdEstado().toString());
			form.setEstado(estadoBean.getNombre());
			}
			request.setAttribute("accion","nuevo");
			//fin selects
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
		
	    try{
		    ExpDocumentosAdm docAdm = new ExpDocumentosAdm(this.getUserBean(request));
		    ExpDocumentacionForm form = (ExpDocumentacionForm)formulario;
		    
		    HashMap datosExpediente = (HashMap)request.getSession().getAttribute("DATABACKUP");
		    Hashtable hash = (Hashtable)datosExpediente.get("datosGenerales");
		    
		    //Rellenamos el nuevo Bean	    
		    ExpDocumentosBean docBean = new ExpDocumentosBean();	    
		    
		    docBean.setIdInstitucion(Integer.valueOf((String)hash.get(ExpDocumentosBean.C_IDINSTITUCION)));
		    docBean.setIdInstitucion_TipoExpediente(Integer.valueOf((String)hash.get(ExpDocumentosBean.C_IDINSTITUCION_TIPOEXPEDIENTE)));
		    docBean.setIdTipoExpediente(Integer.valueOf((String)hash.get(ExpDocumentosBean.C_IDTIPOEXPEDIENTE)));
		    docBean.setNumeroExpediente(Integer.valueOf((String)hash.get(ExpDocumentosBean.C_NUMEROEXPEDIENTE)));
		    docBean.setAnioExpediente(Integer.valueOf((String)hash.get(ExpDocumentosBean.C_ANIOEXPEDIENTE)));
		    docBean.setIdDocumento(docAdm.getNewIdDocumento(hash));
		    docBean.setDescripcion(form.getDescripcion());
		    docBean.setRuta(form.getRuta().equals("")?null:form.getRuta());
		    docBean.setRegEntrada(form.getRegentrada().equals("")?null:form.getRegentrada());
		    docBean.setRegSalida(form.getRegsalida().equals("")?null:form.getRegsalida());
		    docBean.setIdEstado(form.getIdEstado().equals("")?null:Integer.valueOf(form.getIdEstado()));
		    docBean.setIdFase(form.getIdFase().equals("")?null:Integer.valueOf(form.getIdFase()));
		    
		    //Ahora procedemos a insertarlo
		    /*if (docAdm.insert(docBean))
	        {
	            return exitoModal("messages.inserted.success",request);
	        }else{
	        	return exito("messages.inserted.error",request);
	        }*/
		    docAdm.insert(docBean);
	    }catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null); 
		}
	    return exitoModal("messages.inserted.success",request);
	    
	}
	
	/**
	 * Obtiene la url del DocuShare para el identificador de la colección pasada por parémtro
	 * @param usrBean
	 * @param identificadorDS
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	private String getURLdocumentacionDS(UsrBean usrBean, String identificadorDS) throws ClsExceptions, SIGAException {
		if (identificadorDS == null || identificadorDS.trim().equals("")) {
			//El expediente no tiene Documentación asociada
			throw new SIGAException("expedientes.docushare.error.sinIdentificador");
		}
		DocuShareHelper docuShareHelper = new DocuShareHelper(usrBean);
		return docuShareHelper.getURLCollection(identificadorDS);
	}
}
