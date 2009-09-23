/*
 * Created on Mar 29, 2005
 * @author jmgrau
 *
 */
package com.siga.envios.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.UsrBean;
import com.siga.beans.EnvDestinatariosBean;
import com.siga.beans.EnvEnviosAdm;
import com.siga.beans.EnvEnviosBean;
import com.siga.beans.EnvListaCorreosEnviosAdm;
import com.siga.beans.EnvListaCorreosEnviosBean;
import com.siga.beans.EnvTipoEnviosAdm;
import com.siga.beans.EnvTipoEnviosBean;
import com.siga.envios.form.ListasDestinatariasForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gui.processTree.SIGAPTConstants;

/**
 * 
 * Clase action para el mantenimiento de destinatarios manuales del envio.<br/>
 * Gestiona la edicion y borrado de destinatarios, así como la dirección de envio. 
 *
 */
public class ListasDestinatariasAction extends MasterAction {

    protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{	                  
		//Aplicar acceso
        if(request.getParameter("acceso").equalsIgnoreCase("Ver")) {
            HttpSession ses=request.getSession();
        	UsrBean user=(UsrBean)ses.getAttribute("USRBEAN");
		    user.setAccessType(SIGAPTConstants.ACCESS_READ);
        }
        
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
        Vector envio, tipo, datos;
        try {
            envio = envioAdm.selectByPK(htPk);        
	        EnvEnviosBean envioBean = (EnvEnviosBean)envio.firstElement();
	        request.setAttribute("nombreEnv", envioBean.getDescripcion());
	        
	        Hashtable htTipo = new Hashtable();
	        htTipo.put(EnvTipoEnviosBean.C_IDTIPOENVIOS,envioBean.getIdTipoEnvios());
	        tipo = tipoAdm.selectByPK(htTipo);
	        EnvTipoEnviosBean tipoBean = (EnvTipoEnviosBean)tipo.firstElement();	        
	        request.setAttribute("tipo", tipoBean.getNombre());
	        
	        //recupero las fases y las paso a la jsp
	        datos = envioAdm.getListasDestinatarias(idInstitucion,idEnvio);
	        request.setAttribute("datos", datos); 
	        
        } catch (Exception e) {
            throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
        }
		return "inicio";
	}


	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#nuevo(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {	    
	    	
		  return "nuevo";
	}
    
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#insertar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
	    
	    UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
	    EnvListaCorreosEnviosAdm listasAdm = new EnvListaCorreosEnviosAdm(this.getUserBean(request));
	    ListasDestinatariasForm form = (ListasDestinatariasForm) formulario;
	    
	    try {
//	      	Rellenamos el nuevo Bean		    
		    EnvListaCorreosEnviosBean listaBean = new EnvListaCorreosEnviosBean();	    
		    
		    String idInstitucion = userBean.getLocation();
		    String idListaCorreo = form.getIdListaCorreo();
		    String idEnvio = form.getIdEnvio();
		    
		    listaBean.setIdInstitucion(Integer.valueOf(idInstitucion));
		    listaBean.setIdEnvio(Integer.valueOf(idEnvio));
		    listaBean.setIdListaCorreo(Integer.valueOf(idListaCorreo));
		    
		    //Ahora procedemos a insertarlo
		    if (!listasAdm.existeLista(idEnvio,idInstitucion,idListaCorreo)){
		        listasAdm.insert(listaBean);
		        request.setAttribute("descOperation","messages.inserted.success");	            
		    } else {
		        throw new SIGAException("messages.envios.error.existeelemento");
		    }
		  } catch (Exception e) {
		      throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
		  }
		  return exitoRefresco("messages.inserted.success",request);
	}


	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#borrar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
		
	    ListasDestinatariasForm form = (ListasDestinatariasForm) formulario;
	    
	    form.setModal("false");
	    
	    UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
	    EnvListaCorreosEnviosAdm listasAdm = new EnvListaCorreosEnviosAdm(this.getUserBean(request));
	    
	    Vector vOcultos = form.getDatosTablaOcultos(0);	    					
	    
	    Hashtable hash = new Hashtable();
	    	    
	    hash.put(EnvListaCorreosEnviosBean.C_IDINSTITUCION, userBean.getLocation());
	    hash.put(EnvListaCorreosEnviosBean.C_IDLISTACORREO,(String)vOcultos.elementAt(0) );
	    hash.put(EnvListaCorreosEnviosBean.C_IDENVIO, (String)vOcultos.elementAt(1));	
	    
	    try {
	        listasAdm.delete(hash);	        
	    } catch (Exception e) {
	        this.throwExcp("messages.elementoenuso.error",e,null);
        }
		
		return exitoRefresco("messages.deleted.success",request);
	}

}
