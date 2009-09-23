/*
 * Created on Feb 3, 2005
 * @author juan.grau
 *
 */
package com.siga.expedientes.action;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.beans.ExpAlertaAdm;
import com.siga.beans.ExpAlertaBean;
import com.siga.beans.ExpCampoTipoExpedienteAdm;
import com.siga.beans.ExpExpedienteBean;
import com.siga.expedientes.ExpPermisosTiposExpedientes;
import com.siga.expedientes.form.BusquedaAlertaForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;

/**
 * Action para la búsqueda de expedientes (simple y avanzada)
 */
public class BusquedaAlertaAction extends MasterAction {
	
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions {
		return("inicio");
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
    protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions {
        BusquedaAlertaForm form = (BusquedaAlertaForm)formulario;
//        HttpSession ses=request.getSession();
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        
        String idInstitucion = userBean.getLocation();
        ExpAlertaAdm alertaAdm = new ExpAlertaAdm (this.getUserBean(request));
                
        //NOMBRES COLUMNAS PARA LA JOIN

        //Tabla EXP_ALERTA
		
		form.setIdInstitucion(idInstitucion);
        
        Vector datos = alertaAdm.getAlertas(form, userBean);
        
        request.setAttribute("datos", datos);
        
        //para saber en que tipo de busqueda estoy
		request.getSession().setAttribute("volverAuditoriaExpedientes","Al");
		
//		obtenemos los permisos a aplicar
		
		ExpPermisosTiposExpedientes perm=new ExpPermisosTiposExpedientes(userBean);
		request.setAttribute("permisos",perm);
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
			throws ClsExceptions {
	
	    BusquedaAlertaForm form = (BusquedaAlertaForm)formulario;
	    
		ExpAlertaAdm alertaAdm = new ExpAlertaAdm (this.getUserBean(request));
		
		Vector vOcultos = form.getDatosTablaOcultos(0);
		
		Hashtable hash = new Hashtable();
			    
		hash.put(ExpAlertaBean.C_IDINSTITUCION, (String)vOcultos.elementAt(0));
		hash.put(ExpAlertaBean.C_IDINSTITUCIONTIPOEXPEDIENTE, (String)vOcultos.elementAt(1));
		hash.put(ExpAlertaBean.C_IDTIPOEXPEDIENTE, (String)vOcultos.elementAt(2));	    
		hash.put(ExpAlertaBean.C_NUMEROEXPEDIENTE, (String)vOcultos.elementAt(3));
		hash.put(ExpAlertaBean.C_ANIOEXPEDIENTE, (String)vOcultos.elementAt(4));
		hash.put(ExpAlertaBean.C_IDALERTA, (String)vOcultos.elementAt(6));
		
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
		
		return "exito";	
	}

	
	protected String mostrarRegistro(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response, boolean bEditable) throws ClsExceptions{
        
	    BusquedaAlertaForm form = (BusquedaAlertaForm)formulario;
		UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));   
 
//		Vector vVisibles = form.getDatosTablaVisibles(0);
		Vector vOcultos = form.getDatosTablaOcultos(0);		

        String idInstitucion = (String)vOcultos.elementAt(0);
        String idInstitucion_TipoExpediente = (String)vOcultos.elementAt(1);
        String idTipoExpediente = (String)vOcultos.elementAt(2);
        String numExpediente = (String)vOcultos.elementAt(3);
        String anioExpediente = (String)vOcultos.elementAt(4);
//        String denunciado = (String)vOcultos.elementAt(5);
        String nombreTipoExpediente = (String)vOcultos.elementAt(6);        
        
        // Si se intenta editar un expediente de otra institucion,
        //sólo se permitirá modificar las anotaciones (pestanha de seguimiento)
        String soloSeguimiento = "false";
        if (bEditable){
        	soloSeguimiento = (!userBean.getLocation().equals(idInstitucion))?"true":"false";	        	
        }
	    //Anhadimos parametros para las pestanhas
	    Hashtable htParametros=new Hashtable();
	    htParametros.put("idInstitucion",idInstitucion);
	    htParametros.put("idInstitucion_TipoExpediente",idInstitucion_TipoExpediente);
	    htParametros.put("idTipoExpediente",idTipoExpediente);
	    htParametros.put("numeroExpediente",numExpediente);
	    htParametros.put("anioExpediente",anioExpediente);
	    //htParametros.put("denunciado",denunciado);
	    htParametros.put("nombreTipoExpediente",nombreTipoExpediente);
	    htParametros.put("editable", bEditable ? "1" : "0");
	    htParametros.put("accion", bEditable ? "edicion" : "consulta");
	    htParametros.put("soloSeguimiento",soloSeguimiento);
	    
	    request.setAttribute("expediente", htParametros);
	    request.setAttribute("nuevo", "false");

		//Recuperamos las pestanhas ocultas para no mostrarlas
		ExpCampoTipoExpedienteAdm campoAdm = new ExpCampoTipoExpedienteAdm(this.getUserBean(request));
		String[] pestanasOcultas = campoAdm.obtenerPestanasOcultas(idInstitucion_TipoExpediente,idTipoExpediente);
		
		request.setAttribute("pestanasOcultas",pestanasOcultas);
		request.setAttribute("idTipoExpediente",idTipoExpediente);
		request.setAttribute("idInstitucionTipoExpediente",idInstitucion_TipoExpediente);
		

	    //Metemos los datos no editables del expediente en Backup.
	    //Los datos particulares se anhadirán a la HashMap en cada caso.
	    HashMap datosExpediente = new HashMap();
	    Hashtable datosGenerales = new Hashtable();
	    datosGenerales.put(ExpExpedienteBean.C_IDINSTITUCION,idInstitucion);
	    datosGenerales.put(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE,idInstitucion_TipoExpediente);
	    datosGenerales.put(ExpExpedienteBean.C_IDTIPOEXPEDIENTE,idTipoExpediente);
	    datosGenerales.put(ExpExpedienteBean.C_NUMEROEXPEDIENTE,numExpediente);
	    datosGenerales.put(ExpExpedienteBean.C_ANIOEXPEDIENTE,anioExpediente);
		datosExpediente.put("datosGenerales",datosGenerales);
		request.getSession().setAttribute("DATABACKUP",datosExpediente);
	    
		return "editar";
	}
}
