/*
 * Created on Dec 28, 2004
 * @author jmgrau
 *
 */
package com.siga.expedientes.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.beans.CenClienteBean;
import com.siga.beans.ExpEstadosAdm;
import com.siga.beans.ExpEstadosBean;
import com.siga.beans.ExpFasesAdm;
import com.siga.beans.ExpFasesBean;
import com.siga.beans.ExpTipoExpedienteAdm;
import com.siga.beans.ExpTipoExpedienteBean;
import com.siga.beans.ExpTiposAnotacionesAdm;
import com.siga.beans.ExpTiposAnotacionesBean;
import com.siga.expedientes.form.TiposAnotacionesForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gui.processTree.SIGAPTConstants;

/**
 * 
 * Clase action para el mantenimiento de fases de expediente.<br/>
 * Gestiona la edicion, borrado y creación de las fases. 
 *
 */
public class TiposAnotacionesAction extends MasterAction {
    
    protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{	
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        String institucion = userBean.getLocation();
        
		//Aplicar acceso
        if(request.getParameter("acceso").equalsIgnoreCase("Ver")) {
            userBean.setAccessType(SIGAPTConstants.ACCESS_READ);
        }
        
        TiposAnotacionesForm form = (TiposAnotacionesForm)formulario;
        ExpTiposAnotacionesAdm anotacionesAdm = new ExpTiposAnotacionesAdm (this.getUserBean(request));
               
        String where = " WHERE ";
        
//      campos de búsqueda
        where += "TA." + ExpTiposAnotacionesBean.C_IDINSTITUCION + " = " + institucion + " AND TA." + ExpTiposAnotacionesBean.C_IDTIPOEXPEDIENTE + " = " + form.getIdTipoExpediente();
        
//      join de las tablas TIPOANOTACION TA, FASES F
        where += " AND TA."+ExpTiposAnotacionesBean.C_IDFASE+" = "+ "F." + ExpFasesBean.C_IDFASE + "(+)";
        where += " AND TA."+ExpTiposAnotacionesBean.C_IDINSTITUCION + " = " + "F." + ExpFasesBean.C_IDINSTITUCION + "(+)";
        where += " AND TA."+ExpTiposAnotacionesBean.C_IDTIPOEXPEDIENTE + " = " + "F." + ExpFasesBean.C_IDTIPOEXPEDIENTE + "(+)";      
        
//		join de las tablas TIPOANOTACION TA, ESTADOS E
        where += " AND TA."+ExpTiposAnotacionesBean.C_IDESTADO+" = " + "E.IDESTADO(+)";
        where += " AND TA."+ExpTiposAnotacionesBean.C_IDINSTITUCION+" = " + "E.IDINSTITUCION" + "(+)";
        where += " AND TA."+ExpTiposAnotacionesBean.C_IDFASE+" = " + "E.IDFASE(+)";
        where += " AND TA."+ExpTiposAnotacionesBean.C_IDTIPOEXPEDIENTE+" = " + "E.IDTIPOEXPEDIENTE" + "(+)";        
    
        Vector datos = null;
        try {
            datos = anotacionesAdm.selectBusqAnot(where);
        } catch (Exception e){
            throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null);
        }
        request.setAttribute("datos", datos);                 
        
//      ******************************        
        String where1 = " WHERE ";        
        where1 += ExpFasesBean.C_IDINSTITUCION + " = " + institucion+" AND "+ExpFasesBean.C_IDTIPOEXPEDIENTE + " = " + form.getIdTipoExpediente();
        
//      Recupero el bean de Tipo de expediente para mostrar el nombre
        ExpTipoExpedienteAdm tipoExpedienteAdm = new ExpTipoExpedienteAdm (this.getUserBean(request));
        Vector tipoExp = null;
        try{
            tipoExp = tipoExpedienteAdm.select(where1);
        } catch (Exception e){
            throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null);
        }

        ExpTipoExpedienteBean beantipoexp=(ExpTipoExpedienteBean)tipoExp.elementAt(0);
        request.setAttribute("nombreExp", beantipoexp.getNombre());
//      ******************************
          
		return "abrir";
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#editar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException, ClsExceptions {
	    
	    return mostrarRegistro(mapping, formulario, request, response, true);
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#ver(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException,ClsExceptions {	    
	    
	    return mostrarRegistro(mapping, formulario, request, response, false);
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#nuevo(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException { 	    
	    
	    UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));        
	    String institucion = userBean.getLocation();
	    TiposAnotacionesForm form = (TiposAnotacionesForm)formulario;
        
        ExpTiposAnotacionesBean nuevaAnotacion = new ExpTiposAnotacionesBean();
        nuevaAnotacion.setNombre("");
        nuevaAnotacion.setMensaje("");
        nuevaAnotacion.setIdInstitucion(Integer.valueOf(institucion));
        nuevaAnotacion.setIdTipoExpediente(Integer.valueOf(form.getIdTipoExpediente()));
                
        Vector datos = new Vector();
        datos.add(nuevaAnotacion);
        request.setAttribute("datos", datos);
        request.setAttribute("editable", "0");

		return "mostrar";
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#insertar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
		
	    UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));        
	    String institucion = userBean.getLocation();
        
	    ExpTiposAnotacionesAdm anotacionesAdm = new ExpTiposAnotacionesAdm(this.getUserBean(request));
	    TiposAnotacionesForm form = (TiposAnotacionesForm) formulario;
	    
	    //Rellenamos el nuevo Bean
	    
	    ExpTiposAnotacionesBean anotacion = new ExpTiposAnotacionesBean();	    
	    
	    try{
	       
		    anotacion.setIdInstitucion(Integer.valueOf(institucion));
		    anotacion.setIdTipoExpediente(Integer.valueOf(form.getIdTipoExpediente()));
		    anotacion.setIdTipoAnotacion(anotacionesAdm.getNewIdTipoAnotacion(form.getIdTipoExpediente(),userBean));
		    anotacion.setNombre(form.getNombre());
		    anotacion.setMensaje(form.getMensaje());	    
		    anotacion.setIdEstado(form.getIdInst_idExp_idFase_idEstado().equals("")?null:Integer.valueOf(form.getIdEstado()));
		    anotacion.setIdFase(form.getIdInst_idExp_idFase().equals("")?null:Integer.valueOf(form.getIdFase()));
		    
		    //Ahora procedemos a insertarlo
		    if (anotacionesAdm.insert(anotacion))
	        {
	            request.setAttribute("descOperation","messages.inserted.success");
	        }
	    } catch (Exception e) {
            throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null);
        }

	    request.setAttribute("modal","1");
	    return "refresh";
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#modificar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
	    
	    TiposAnotacionesForm form = (TiposAnotacionesForm)formulario;	    
	    ExpTiposAnotacionesAdm anotacionesAdm = new ExpTiposAnotacionesAdm (this.getUserBean(request));
	    
        Vector vOcultos = form.getDatosTablaOcultos(0);
        Hashtable hashOld = (Hashtable)request.getSession().getAttribute("DATABACKUP");
        
        Hashtable hashNew = (Hashtable)hashOld.clone();
	    
        hashNew.put(ExpTiposAnotacionesBean.C_NOMBRE, form.getNombre());
	    hashNew.put(ExpTiposAnotacionesBean.C_IDTIPOANOTACION, hashOld.get(ExpTiposAnotacionesBean.C_IDTIPOANOTACION));
	    hashNew.put(ExpTiposAnotacionesBean.C_IDFASE, form.getIdFase());
	    hashNew.put(ExpTiposAnotacionesBean.C_IDESTADO, form.getIdEstado());
	    hashNew.put(ExpTiposAnotacionesBean.C_MENSAJE, form.getMensaje());	
	    
	    request.removeAttribute("DATABACKUP");
	    boolean exito = false;
	    try{
	        exito = anotacionesAdm.update(hashNew, hashOld);		       
	    } catch (Exception e) {
            throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null);
        }
	    
        if (exito){
            return exitoModal("messages.updated.success",request);
        }        
        else {
            return exito("messages.updated.error",request);
        }	    
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#borrar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
		
		TiposAnotacionesForm form = (TiposAnotacionesForm)formulario;
		try{
	    form.setModal("false");
	    
	    UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));        
	    String institucion = userBean.getLocation();
	    
	    ExpTiposAnotacionesAdm anotacionesAdm = new ExpTiposAnotacionesAdm (this.getUserBean(request));
	    
	    Vector vOcultos = form.getDatosTablaOcultos(0);
	    Hashtable hash = new Hashtable();	    	    
	    hash.put(ExpTiposAnotacionesBean.C_IDINSTITUCION, userBean.getLocation());
	    hash.put(ExpTiposAnotacionesBean.C_IDTIPOANOTACION, (String)vOcultos.elementAt(0));	
	    hash.put(ExpTiposAnotacionesBean.C_IDTIPOEXPEDIENTE,form.getIdTipoExpediente());
        
	    
	    
	    	if (!anotacionesAdm.delete(hash)){
	        	throw new SIGAException("messages.elementoenuso.error");
	        	//throw new ClsExceptions("mensaje: "+estadosAdm.getError());
	        }
	        
	        request.setAttribute("descOperation","messages.deleted.success");
	    } catch (Exception exc){
			throwExcp("messages.general.error",new String[] {"modulo.expedientes"},exc,null);
	    	//throwExcp("messages.elementoenuso.error",exc,null);

	    }    
        
	    request.setAttribute("urlAction", "/SIGA/EXP_TiposExpedientes_TiposAnotaciones.do");
        
	    return "refresh";
	}

	protected String mostrarRegistro(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response, boolean bEditable) throws SIGAException, ClsExceptions
	{
	    TiposAnotacionesForm form = (TiposAnotacionesForm)formulario;
        ExpTiposAnotacionesAdm anotacionesAdm = new ExpTiposAnotacionesAdm (this.getUserBean(request));
        ExpEstadosAdm estadoAdm = new ExpEstadosAdm(this.getUserBean(request));
        ExpFasesAdm faseAdm = new ExpFasesAdm(this.getUserBean(request));
        
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));        
	    String institucion = userBean.getLocation();

		Vector vVisibles = form.getDatosTablaVisibles(0);
		Vector vOcultos = form.getDatosTablaOcultos(0);		

        String idAnotacion = (String)vOcultos.elementAt(0);
        String descEstado="";
        
        String where = " WHERE ";
        
        where += ExpTiposAnotacionesBean.C_IDTIPOANOTACION + " = '" + idAnotacion + "' AND ";
        where += ExpTiposAnotacionesBean.C_IDINSTITUCION + " = '" + institucion + "' AND ";
        where += ExpTiposAnotacionesBean.C_IDTIPOEXPEDIENTE + " = '" + form.getIdTipoExpediente() + "'";
        Hashtable datosEstado=new Hashtable();
        Hashtable datosFase=new Hashtable();
        Vector datos = null;
        try {
            datos = anotacionesAdm.select(where);
        } catch (Exception e) {
            throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null);
        }
        
        if (datos!=null && datos.size()>0){
        	if(((ExpTiposAnotacionesBean) datos.get (0)).getIdEstado()!=null && ((ExpTiposAnotacionesBean) datos.get (0)).getIdFase()!=null) {
        	datosEstado.put("IDESTADO", ((ExpTiposAnotacionesBean) datos.get (0)).getIdEstado());
        	datosEstado.put("IDINSTITUCION",((ExpTiposAnotacionesBean) datos.get (0)).getIdInstitucion());
        	datosEstado.put("IDFASE",((ExpTiposAnotacionesBean) datos.get (0)).getIdFase());
        	datosEstado.put("IDTIPOEXPEDIENTE", ((ExpTiposAnotacionesBean) datos.get (0)).getIdTipoExpediente());
        	Vector dEstado=estadoAdm.selectByPK(datosEstado);
 		    request.setAttribute("descEstado", ((ExpEstadosBean) dEstado.get (0)).getNombre());
        	}
        	
        	if (((ExpTiposAnotacionesBean) datos.get (0)).getIdFase()!=null) {
        	datosFase.put("IDINSTITUCION",((ExpTiposAnotacionesBean) datos.get (0)).getIdInstitucion());
        	datosFase.put("IDFASE",((ExpTiposAnotacionesBean) datos.get (0)).getIdFase());
        	datosFase.put("IDTIPOEXPEDIENTE", ((ExpTiposAnotacionesBean) datos.get (0)).getIdTipoExpediente());
           Vector dFase=faseAdm.selectByPK(datosFase);
		   request.setAttribute("descFase", ((ExpFasesBean) dFase.get (0)).getNombre());
        	}
					
        }
        
        request.setAttribute("datos", datos);
        request.setAttribute("editable", "1");
        
        Hashtable hashBackUp = new Hashtable();
        
        hashBackUp.put(ExpTiposAnotacionesBean.C_IDTIPOANOTACION, idAnotacion);
        hashBackUp.put(ExpTiposAnotacionesBean.C_IDINSTITUCION, institucion);
        hashBackUp.put(ExpTiposAnotacionesBean.C_IDTIPOEXPEDIENTE, form.getIdTipoExpediente());
        
        request.getSession().setAttribute("DATABACKUP", hashBackUp);
    
		return "mostrar";
	}


}
