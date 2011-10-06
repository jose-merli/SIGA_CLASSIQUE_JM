/*
 * @author RGG
 *
 */
package com.siga.expedientes.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.ExpCampoConfAdm;
import com.siga.beans.ExpCampoTipoExpedienteAdm;
import com.siga.beans.ExpCampoTipoExpedienteBean;
import com.siga.beans.ExpCamposValorAdm;
import com.siga.beans.ExpExpedienteAdm;
import com.siga.beans.ExpExpedienteBean;
import com.siga.beans.ExpPestanaConfAdm;
import com.siga.expedientes.form.PestanaConfigurableForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 * 
 * Clase action para el mantenimiento de campos de expediente.<br/>
 * Gestiona la edicion, borrado y creación de los campos. 
 *
 */
public class PestanaConfigurableAction extends MasterAction {
    
    protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions,SIGAException
	{	
        
        // Eliminamos el backup de la sesión
        HttpSession ses=request.getSession();
        UsrBean userBean=(UsrBean)ses.getAttribute("USRBEAN");
        
        PestanaConfigurableForm form = (PestanaConfigurableForm)formulario;
        ExpPestanaConfAdm admPest = new ExpPestanaConfAdm(this.getUserBean(request));
        form.setNombrePestana(admPest.getPestana(new Integer(form.getIdPestanaConf()).intValue(),userBean.getLocation(),form.getIdTipoExpediente(),form.getIdCampo()).getNombre());        String accion = (String)request.getParameter("accion"); // nuevo, consulta, edicion
        form.setAccion(accion);
        if (accion.equals("edicion")) {
            form.setModo("modificar");
        }

        //Datos generales para todas las pestanhas
		String idInstitucion = request.getParameter("idInstitucion");
		String idInstitucion_TipoExpediente = request.getParameter("idInstitucion_TipoExpediente");
		String idTipoExpediente = request.getParameter("idTipoExpediente");
		String numExpediente = request.getParameter("numeroExpediente");
		String anioExpediente = request.getParameter("anioExpediente");
		
	   if(numExpediente==null || numExpediente.equals(""))
	           numExpediente = (String) request.getSession().getAttribute("numeroExpedienteSession");
	   
	   if(anioExpediente==null || anioExpediente.equals(""))
	           anioExpediente = (String) request.getSession().getAttribute("anioExpedienteSession");
		
		
		if (form.getIdPestanaConf().equals("1")) {
		    form.setIdCampo(ClsConstants.IDCAMPO_PARAPESTANACONF1);
		} else {
		    form.setIdCampo(ClsConstants.IDCAMPO_PARAPESTANACONF2);
		}

		form.setIdInstitucion(idInstitucion_TipoExpediente);
		form.setIdTipoExpediente(idTipoExpediente);
		form.setNumeroExpediente(numExpediente);
		form.setAnioExpediente(anioExpediente);
		
	    ExpCampoConfAdm admCampo = new ExpCampoConfAdm(this.getUserBean(request));
	    Vector campos = admCampo.getCamposValoresPlantilla(userBean.getLocation(),form.getIdTipoExpediente(),form.getNumeroExpediente(), form.getAnioExpediente(),form.getIdPestanaConf());
	    request.setAttribute("datos",campos);

	    //Recogemos el expediente
        Hashtable hash = new Hashtable();		
		hash.put(ExpExpedienteBean.C_IDINSTITUCION,idInstitucion);
		hash.put(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE,idInstitucion_TipoExpediente);
		hash.put(ExpExpedienteBean.C_IDTIPOEXPEDIENTE,idTipoExpediente);
		hash.put(ExpExpedienteBean.C_NUMEROEXPEDIENTE,numExpediente);
		hash.put(ExpExpedienteBean.C_ANIOEXPEDIENTE,anioExpediente);
		ExpExpedienteAdm expAdm = new ExpExpedienteAdm(this.getUserBean(request));
        Vector vExp = expAdm.selectByPK(hash);

        //Recogemos el denunciado
        CenPersonaAdm personaAdm = new CenPersonaAdm(this.getUserBean(request));
        Hashtable hashIdPers = new Hashtable();		
		hashIdPers.put(CenPersonaBean.C_IDPERSONA,((ExpExpedienteBean)vExp.elementAt(0)).getIdPersona());
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
	    
        return "inicio";
	}
    
    
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#modificar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
	    
	    // Recuperamos los datos anteriores de la sesión
	    ExpCamposValorAdm adm = new ExpCamposValorAdm(this.getUserBean(request));
            
	    PestanaConfigurableForm form = (PestanaConfigurableForm)formulario;
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        
        
        //Iniciamos la transacción
        UserTransaction tx = userBean.getTransaction();
	    try {
	        tx.begin();     

	        // RGG busco todos los campos
	        boolean salir = false;
	        int contador = 1;
	        while (!salir) {
	            String idCampoConf = request.getParameter("oculto"+contador+"_1");
	            if (idCampoConf!=null) {
	                //Procesamos el campo
	                String valor = request.getParameter("oculto"+contador+"_2");
		            // lo guardamos
	                adm.guardarValor(form, idCampoConf, valor);
	            } else {
	                // salimos
	                salir = true;
	            }
	            contador++;
	        }

	        tx.commit();
	               
	    } catch (Exception e) {        
	        throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,tx);
	    }      
        
 	    return this.exitoRefresco("messages.updated.success", request);
	}
}

