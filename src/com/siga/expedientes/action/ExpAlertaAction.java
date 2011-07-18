/*
 * Created on Jan 18, 2005
 * @author emilio.grau
 *
 */
package com.siga.expedientes.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.ExpAlertaAdm;
import com.siga.beans.ExpAlertaBean;
import com.siga.beans.ExpCampoTipoExpedienteAdm;
import com.siga.beans.ExpCampoTipoExpedienteBean;
import com.siga.beans.ExpEstadosBean;
import com.siga.beans.ExpExpedienteAdm;
import com.siga.beans.ExpExpedienteBean;
import com.siga.beans.ExpFasesBean;
import com.siga.expedientes.form.ExpAlertaForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 * Action de las alertas de un expediente
 */
public class ExpAlertaAction extends MasterAction {
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrir(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
    protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{	
    	try{
	        ExpAlertaForm form = (ExpAlertaForm)formulario;
	        ExpAlertaAdm alertaAdm = new ExpAlertaAdm (this.getUserBean(request));
	        
	        String where = " WHERE ";
	        
			//Datos generales para todas las pestanhas
			String idInstitucion = request.getParameter("idInstitucion");
			String idInstitucion_TipoExpediente = request.getParameter("idInstitucion_TipoExpediente");
			String idTipoExpediente = request.getParameter("idTipoExpediente");
			String numExpediente = request.getParameter("numeroExpediente");
			String anioExpediente = request.getParameter("anioExpediente");
					
	        //NOMBRES COLUMNAS PARA LA JOIN
			
			//Tabla EXP_ALERTA
			
			String A_IDINSTITUCION="A." + ExpAlertaBean.C_IDINSTITUCION;
			String A_IDINSTITUCIONTIPOEXPEDIENTE="A." + ExpAlertaBean.C_IDINSTITUCIONTIPOEXPEDIENTE;
			String A_IDTIPOEXPEDIENTE="A." + ExpAlertaBean.C_IDTIPOEXPEDIENTE;
			String A_IDFASE="A." + ExpAlertaBean.C_IDFASE;
			String A_IDESTADO="A." + ExpAlertaBean.C_IDESTADO;
			String A_NUMEROEXPEDIENTE="A." + ExpAlertaBean.C_NUMEROEXPEDIENTE;
			String A_ANIOEXPEDIENTE="A." + ExpAlertaBean.C_ANIOEXPEDIENTE;
			String A_BORRADO="A." + ExpAlertaBean.C_BORRADO;
			String A_FECHAALERTA="A." + ExpAlertaBean.C_FECHAALERTA;
					
			//Tabla EXP_ESTADOS
			String E_IDINSTITUCION="E." + ExpEstadosBean.C_IDINSTITUCION;
			String E_IDTIPOEXPEDIENTE="E." + ExpEstadosBean.C_IDTIPOEXPEDIENTE;
			String E_IDFASE="E." + ExpEstadosBean.C_IDFASE;
			String E_IDESTADO="E." + ExpEstadosBean.C_IDESTADO;
			
			//Tabla EXP_FASES
			String F_IDINSTITUCION="F." + ExpFasesBean.C_IDINSTITUCION;
			String F_IDTIPOEXPEDIENTE="F." + ExpFasesBean.C_IDTIPOEXPEDIENTE;
			String F_IDFASE="F." + ExpFasesBean.C_IDFASE;
			
			
			where += A_IDINSTITUCION + " = '" + idInstitucion + "' AND ";
			where += A_IDINSTITUCIONTIPOEXPEDIENTE + " = '" + idInstitucion_TipoExpediente + "' AND ";
			where += A_IDTIPOEXPEDIENTE + " = '" + idTipoExpediente + "' AND ";
			where += A_NUMEROEXPEDIENTE + " = '" + numExpediente + "' AND ";
			where += A_ANIOEXPEDIENTE + " = '" + anioExpediente + "' AND ";
			where += A_BORRADO + " = 'N' ";
			
		    //join de las tablas ALERTA A, ESTADOS E, FASES F
	        where += " AND "+A_IDINSTITUCIONTIPOEXPEDIENTE+" = "+E_IDINSTITUCION;
	        where += " AND "+A_IDTIPOEXPEDIENTE+" = "+E_IDTIPOEXPEDIENTE;
	        where += " AND "+A_IDFASE+" = "+E_IDFASE;
	        where += " AND "+A_IDESTADO+" = "+E_IDESTADO;
	        
	        where += " AND "+A_IDINSTITUCIONTIPOEXPEDIENTE+" = "+F_IDINSTITUCION;
	        where += " AND "+A_IDTIPOEXPEDIENTE+" = "+F_IDTIPOEXPEDIENTE;
	        where += " AND "+A_IDFASE+" = "+F_IDFASE;
	        where += " ORDER BY "+ A_FECHAALERTA;
	        			
			Vector datos = alertaAdm.selectAlertas(where);
	
	        request.setAttribute("datos", datos);
	        
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
			hashIdPers.put(CenPersonaBean.C_IDPERSONA,((ExpExpedienteBean)vExp.elementAt(0)).getIdPersona());
	        Vector vPersona = personaAdm.selectByPK(hashIdPers);
	        CenPersonaBean personaBean = (CenPersonaBean) vPersona.elementAt(0);
	        String nombrePersona = personaBean.getNombre() + " " + personaBean.getApellido1() + " " + personaBean.getApellido2();
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
	        			 .append(": ").append((String)request.getParameter("numeroExpediente")).append(" / ").append((String)request.getParameter("anioExpediente"));

	        form.setTituloVentana(tituloVentana.toString());
	        
    	}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null); 
		}
    	
	    return "inicio";
	}
    
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#editar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions {
	    
	    return "editar";
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#ver(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions {
	    
	    return "consultar";
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#borrar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
		
		try{
	
			ExpAlertaForm form = (ExpAlertaForm)formulario;
		    	    
		    ExpAlertaAdm alertaAdm = new ExpAlertaAdm (this.getUserBean(request));
		    
		    Vector vOcultos = form.getDatosTablaOcultos(0);
		    
		    Hashtable hash = new Hashtable();
		    	    
		    hash.put(ExpAlertaBean.C_IDINSTITUCION, (String)vOcultos.elementAt(0));
		    hash.put(ExpAlertaBean.C_IDINSTITUCIONTIPOEXPEDIENTE, (String)vOcultos.elementAt(1));
		    hash.put(ExpAlertaBean.C_IDTIPOEXPEDIENTE, (String)vOcultos.elementAt(2));	    
		    hash.put(ExpAlertaBean.C_NUMEROEXPEDIENTE, (String)vOcultos.elementAt(3));
		    hash.put(ExpAlertaBean.C_ANIOEXPEDIENTE, (String)vOcultos.elementAt(4));
		    hash.put(ExpAlertaBean.C_IDALERTA, (String)vOcultos.elementAt(5));
		    
		    ExpAlertaBean alertaBean = (ExpAlertaBean)((Vector)alertaAdm.selectByPK(hash)).elementAt(0);
		    alertaBean.setBorrado("S");
		        
		    if (alertaAdm.update(alertaBean))
		    {
		        request.setAttribute("descOperation","messages.deleted.success");
		        request.setAttribute("mensaje","messages.deleted.success");
		    }else
		    {
		        request.setAttribute("descOperation","error.messages.deleted");
		    }
		
		}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null); 
		}
	    
	    return "exito";	
	}
	
}
