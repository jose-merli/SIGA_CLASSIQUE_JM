/*
 * Created on Dec 28, 2004
 * @author jmgrau
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.expedientes.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.siga.beans.ExpEstadosAdm;
import com.siga.beans.ExpEstadosBean;
import com.siga.beans.ExpPerfilBean;
import com.siga.beans.ExpPermisosTiposExpedientesAdm;
import com.siga.beans.ExpPermisosTiposExpedientesBean;
import com.siga.beans.ExpPlazoEstadoClasificacionAdm;
import com.siga.beans.ExpTipoExpedienteAdm;
import com.siga.beans.ExpTipoExpedienteBean;
import com.siga.expedientes.form.EstadosForm;
import com.siga.expedientes.form.PermisosTiposExpedientesForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gui.processTree.SIGAPTConstants;
/**
 * 
 * Clase action para el mantenimiento de campos de expediente.<br/>
 * Gestiona la edicion, borrado y creación de los campos. 
 *
 */
public class PermisosTiposExpedientesAction extends MasterAction {
    
    public static final String nACCESS_FULL = "3";
    public static final String nACCESS_READ = "2";
    public static final String nACCESS_DENY = "1";
    public static final String nACCESS_NONE = "0";    
    
    protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{	                  
        // Eliminamos el backup de la sesión
        HttpSession ses=request.getSession();
        ses.removeAttribute("DATABACKUP");
        //Aplicar acceso
        if(request.getParameter("acceso").equalsIgnoreCase("Ver")) {
            UsrBean user=(UsrBean)ses.getAttribute("USRBEAN");
		    user.setAccessType(SIGAPTConstants.ACCESS_READ);
        }

        PermisosTiposExpedientesForm form = (PermisosTiposExpedientesForm)formulario;
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        ExpPermisosTiposExpedientesAdm PermisosTiposExpedientesAdm = new ExpPermisosTiposExpedientesAdm (this.getUserBean(request));
        
        //Seteamos las propiedades iniciales comunes de los perfiles en el formulario
        String idInstitucion = userBean.getLocation();
        String idInstitucionTipoExpediente = request.getParameter("idInstitucionTipoExpediente");
        String idTipoExpediente = request.getParameter("idTipoExpediente");
        form.setIdInstitucion(idInstitucion);
        form.setIdInstitucionTipoExpediente(idInstitucionTipoExpediente);
        form.setIdTipoExpediente(idTipoExpediente);
        
        
        //Recuperamos los perfiles a mostrar
        String institucion = userBean.getLocation();       
        String where1 = " WHERE ";        
        where1 += "P." + ExpPermisosTiposExpedientesBean.C_IDINSTITUCION + " = " + institucion;        
        Vector perfilesExp = PermisosTiposExpedientesAdm.selectPerfiles(idInstitucionTipoExpediente,idTipoExpediente,idInstitucion);
        
        //Seteo la hash de perfiles en el formulario
        form.setPerfiles(perfilesExp);
        
        //Metemos en el backup los resultados de los perfiles obtenidos
        ses.setAttribute("DATABACKUP",perfilesExp);        
                
        //Recupero el bean de Tipo de expediente para mostrar el nombre
        String where2 = " WHERE ";        
        where2 += ExpPermisosTiposExpedientesBean.C_IDINSTITUCION + " = " + idInstitucionTipoExpediente + " AND " + ExpPermisosTiposExpedientesBean.C_IDTIPOEXPEDIENTE + " = " + form.getIdTipoExpediente();
        ExpTipoExpedienteAdm tipoExpedienteAdm = new ExpTipoExpedienteAdm (this.getUserBean(request));
        Vector tipoExp = tipoExpedienteAdm.select(where2);
        ExpTipoExpedienteBean beantipoexp=(ExpTipoExpedienteBean)tipoExp.elementAt(0);
        request.setAttribute("nombreExp", beantipoexp.getNombre());   
        return "abrir";
	}
    
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#editar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions {
	    
	    return null;
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#ver(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions {
	    
	    return null;
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#nuevo(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions {     

		return null;
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#insertar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions {

	    return null;
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#modificar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
	    
	    // Recuperamos los datos anteriores de la sesión
	    HttpSession ses=request.getSession();
	    Vector backup=(Vector)ses.getAttribute("DATABACKUP");
	        
	    PermisosTiposExpedientesForm form = (PermisosTiposExpedientesForm)formulario;
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        ExpPermisosTiposExpedientesAdm permisosAdm = new ExpPermisosTiposExpedientesAdm (this.getUserBean(request));
        

        UserTransaction tx = null;

        try {
	    	tx = userBean.getTransaction();
	    	
		    Vector perfilesMod = form.getPerfiles();
		    Hashtable pk = new Hashtable();
		    pk.put(ExpPermisosTiposExpedientesBean.C_IDINSTITUCION, form.getIdInstitucion());
		    pk.put(ExpPermisosTiposExpedientesBean.C_IDINSTITUCIONTIPOEXPEDIENTE, form.getIdInstitucionTipoExpediente());
		    pk.put(ExpPermisosTiposExpedientesBean.C_IDTIPOEXPEDIENTE, form.getIdTipoExpediente());

	        //Iniciamos la transacción
		    tx.begin();            
		    for (int i=0; i<perfilesMod.size(); i++){
		        ExpPerfilBean perfilNew = (ExpPerfilBean)perfilesMod.elementAt(i);
		        if(perfilNew==null) continue;
		        pk.put(ExpPermisosTiposExpedientesBean.C_IDPERFIL,perfilNew.getIdPerfil());
		        ExpPerfilBean perfilOld = (ExpPerfilBean)backup.elementAt(i);
		        if (perfilNew.getDerechoAcceso().equals(nACCESS_NONE)){
		            if (!perfilOld.getDerechoAcceso().equals(nACCESS_NONE)){
		                //Existía la entrada en la tabla de perfiles y tenemos que eliminarla
		                permisosAdm.delete(pk);
		            }	            
		        } else if (perfilOld.getDerechoAcceso().equals(nACCESS_NONE)){
		            //NO existía la entrada en la tabla de perfiles y pasamos a crearla
		            //con el derecho correspondiente
		            pk.put(ExpPermisosTiposExpedientesBean.C_DERECHOACCESO,perfilNew.getDerechoAcceso());
		            permisosAdm.insert(pk);	            
		        } else {
		            //Existía la entrada en la tabla y modificamos el derecho de acceso
		            ExpPermisosTiposExpedientesBean modBean = (ExpPermisosTiposExpedientesBean)permisosAdm.selectByPKForUpdate(pk).firstElement();
		            modBean.setDerechoAcceso(Integer.valueOf(perfilNew.getDerechoAcceso()));
		            permisosAdm.update(modBean);	            
		        }
		    }
		    tx.commit();
		    
	        ses.setAttribute("DATABACKUP",perfilesMod);
	        ClsLogging.writeFileLog("Transacción de Permisos realizada.",10);
	    } catch (Exception e) {
	        // RGG 26-8-2004 throwExcp("messages.updated.error",e,tx);
			 throwExcp("messages.general.error",new String[] {"modulo.censo"},e,tx);
	    }        
	   	return exitoRefresco("messages.updated.success",request);
	}     
    
	protected String mostrarRegistro(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response, boolean bEditable) throws ClsExceptions
	{
	    EstadosForm form = (EstadosForm)formulario;
        ExpEstadosAdm estadosAdm = new ExpEstadosAdm (this.getUserBean(request));
        ExpPlazoEstadoClasificacionAdm plazoAdm = new ExpPlazoEstadoClasificacionAdm (this.getUserBean(request));
        
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));        
	    String institucion = userBean.getLocation();

		Vector vOcultos = form.getDatosTablaOcultos(0);		
		
        String idEstado = (String)vOcultos.elementAt(0);
        String idFase = (String)vOcultos.elementAt(1);
        
        // Recupero los nombres de fase y estado para modo consulta
        Vector vVisibles = form.getDatosTablaVisibles(0);
        String fase = (String)vVisibles.elementAt(0);
        String estado = (String)vVisibles.elementAt(1);        
        String estadoSiguiente = (String)vVisibles.elementAt(2);  
		
        
        String where = " WHERE ";        
        where += ExpEstadosBean.C_IDFASE + " = '" + idFase + "' AND ";
        where += ExpEstadosBean.C_IDINSTITUCION + " = '" + institucion + "' AND ";
        where += ExpEstadosBean.C_IDTIPOEXPEDIENTE + " = '" + form.getIdTipoExpediente() + "' AND ";                
        where += ExpEstadosBean.C_IDESTADO + " = '" + idEstado + "'";
        
        Vector datos = estadosAdm.select(where);
        ExpEstadosBean estadosBean = (ExpEstadosBean) datos.elementAt(0);
        //seteo los booleanos del formulario
        form.setAutomatico(estadosBean.getAutomatico().equals("S"));
        form.setEjecucionSancion(estadosBean.getEjecucionSancion().equals("S"));
       
        String where1 = "P." + ExpEstadosBean.C_IDFASE + " = '" + idFase + "' AND ";
	    where1 += "P." + ExpEstadosBean.C_IDESTADO + " = '" + idEstado + "'";        
        
        String where2 = "C." + ExpEstadosBean.C_IDINSTITUCION + " = '" + institucion + "' AND ";
        where2 += "C." + ExpEstadosBean.C_IDTIPOEXPEDIENTE + " = '" + form.getIdTipoExpediente() + "'";                
             
        Vector vPlazos = plazoAdm.selectClasifPlazo(where1, where2);
        
        request.setAttribute("datos", datos);
        request.setAttribute("vPlazos", vPlazos);
        request.setAttribute("fase",fase);
        request.setAttribute("estado",estado);
        request.setAttribute("estadoSiguiente",estadoSiguiente);        
        request.setAttribute("editable", bEditable ? "1" : "0");
        
        if (bEditable)
        {
            Hashtable hashBackUp = new Hashtable();
            
            hashBackUp.put("estado",estadosBean);
            hashBackUp.put("vPlazos",vPlazos);
            
            request.getSession().setAttribute("DATABACKUP", hashBackUp);
        }

		return "mostrar";
	}
}
