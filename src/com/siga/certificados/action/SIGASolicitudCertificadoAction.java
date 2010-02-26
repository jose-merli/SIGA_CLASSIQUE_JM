package com.siga.certificados.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenClienteBean;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.CerSolicitudCertificadosAdm;
import com.siga.beans.CerSolicitudCertificadosBean;
import com.siga.certificados.form.SIGASolicitudCertificadoForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

public class SIGASolicitudCertificadoAction extends MasterAction{
	
	public ActionForward executeInternal (ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		String mapDestino = "exception";
		MasterForm miForm = null;
	
		try 
		{ 
	        miForm = (MasterForm) formulario;
	        if (miForm != null) 
	        {
	            String accion = miForm.getModo();
	            if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir"))
	            {
	                mapDestino = abrir(mapping, miForm, request, response);
	            } 
	            else if (accion.equalsIgnoreCase("confirmar"))
	            {
	                mapDestino = confirmar(mapping, miForm, request, response);
	            } 
	            else if (accion.equalsIgnoreCase("insertar"))
	            {
	                mapDestino = insertar(mapping, miForm, request, response);
	            }    
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
		    throw new SIGAException("messages.general.error",e,new String[] {"modulo.certificados"});
		} 
	
	}

    protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{
    	
    	// MAV 26/8/2005 Resolucion incidencia relacionada con no aparicion de datos si se trata de un letrado
    	try{
			UsrBean user = this.getUserBean(request);
			String idInstitucion=user.getLocation();
	
			String idPersona = "";
			String nombre = "";
			String apellido1 = "";
			String apellido2 = "";
			String nif = "";
	
			// Si el usuario es letrado buscamos sus datos sino no mostramos información poruq debe seleccionar un letrado
			if (user.isLetrado()){					
				idPersona = new Long(user.getIdPersona()).toString();
				CenPersonaAdm personaAdm = new CenPersonaAdm(this.getUserBean(request));
				CenPersonaBean personaBean = personaAdm.getIdentificador(new Long(idPersona));
	
				// Existen datos de la persona
				if(personaBean != null){				
					nombre = personaBean.getNombre();
					apellido1 = personaBean.getApellido1();
					apellido2 = personaBean.getApellido2();
					nif 	= personaBean.getNIFCIF();
				}
			}
			request.setAttribute("idPersona", idPersona);
			request.setAttribute("nombre", nombre);
			request.setAttribute("apellido1", apellido1);
			request.setAttribute("apellido2", apellido2);
			request.setAttribute("nif", nif);			
				
	}
	catch (Exception e) { 
		throwExcp("messages.general.error",new String[] {"modulo.certificados"},e, null); 
	}
    	return "abrir";
	}

	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) 
		throws ClsExceptions,SIGAException{
	    UserTransaction tx = null;
	    try {

	    	
	    	
	        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
	        tx=userBean.getTransaction();
		    CerSolicitudCertificadosAdm solicitudAdm = new CerSolicitudCertificadosAdm(this.getUserBean(request));
		    SIGASolicitudCertificadoForm form = (SIGASolicitudCertificadoForm) formulario;

		    tx.begin();
            solicitudAdm.insertarSolicitudCertificado(form.getIdPersona(),
            								          form.getIdInstitucionOrigen(),
            								          form.getIdInstitucionDestino(),
            								          form.getDescripcion(),
            								          form.getFechaSolicitud(),
            								          form.getMetodoSolicitud(),
            								          userBean);
            tx.commit();
	    }
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.certificados"},e, tx); 
		}
	    
	    return exito("messages.inserted.success",request);
	}
	
	protected String confirmar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) 
	throws ClsExceptions,SIGAException{
    UserTransaction tx = null;
    try {

    	SIGASolicitudCertificadoForm form = (SIGASolicitudCertificadoForm) formulario;
	    HttpSession ses=request.getSession();
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        

	    
	    if (form.getIdInstitucionOrigen()==null || form.getIdInstitucionOrigen().trim().equals("")) {
	    	return exito("certificados.solicitudes.literal.faltaOrigen",request);
	    }
	    if (form.getIdInstitucionDestino()==null || form.getIdInstitucionDestino().trim().equals("")) {
	    	return exito("certificados.solicitudes.literal.faltaDestino",request);
	    }
	    CenClienteBean beanCli = null;
	    CenClienteAdm admCliente = new CenClienteAdm(userBean);
	    beanCli = admCliente.existeCliente (new Long(Long.parseLong(form.getIdPersona())),
	    									new Integer(Integer.parseInt(form.getIdInstitucionOrigen())));
	    if (beanCli == null) {
	    	return "preguntaNoColegiado";
	    }
	    
    }
	catch (Exception e) { 
		throwExcp("messages.general.error",new String[] {"modulo.certificados"},e, tx); 
	}
    
	return insertar(mapping,formulario,request,response);
}


}