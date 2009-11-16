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

import org.apache.struts.action.ActionMapping;

import com.atos.utils.UsrBean;
import com.atos.utils.Validaciones;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.ExpFasesAdm;
import com.siga.beans.ExpFasesBean;
import com.siga.beans.ExpTipoExpedienteAdm;
import com.siga.beans.ExpTipoExpedienteBean;
import com.siga.expedientes.form.FasesForm;
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
public class FasesAction extends MasterAction {

    protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{	                  
		//Aplicar acceso
        if(request.getParameter("acceso").equalsIgnoreCase("Ver")) {
            HttpSession ses=request.getSession();
        	UsrBean user=(UsrBean)ses.getAttribute("USRBEAN");
		    user.setAccessType(SIGAPTConstants.ACCESS_READ);
        }
        
        FasesForm form = (FasesForm)formulario;
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        ExpFasesAdm fasesAdm = new ExpFasesAdm (this.getUserBean(request));
        
        String institucion = userBean.getLocation();
        //Recuperamos el expediente a mostrar
    
        // Si es null idtipoexpediente ->excepcion
        
        String where = " WHERE ";
        
        where += ExpFasesBean.C_IDINSTITUCION + " = " + institucion+" AND "+ExpFasesBean.C_IDTIPOEXPEDIENTE + " = " + form.getIdTipoExpediente();
        
        //Recupero el bean de Tipo de expediente para mostrar el nombre
        ExpTipoExpedienteAdm tipoExpedienteAdm = new ExpTipoExpedienteAdm (this.getUserBean(request));
        Vector tipoExp,datos;
        try {
            tipoExp = tipoExpedienteAdm.select(where);
        
	        ExpTipoExpedienteBean beantipoexp=(ExpTipoExpedienteBean)tipoExp.elementAt(0);
	        request.setAttribute("nombreExp", beantipoexp.getNombre());
	        
	        //recupero las fases y las paso a la jsp
	        datos = fasesAdm.select(where);
	        request.setAttribute("datos", datos); 
        } catch (Exception e) {
            throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null);
        }
		return "abrir";
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

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#nuevo(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response) { 
	    
	    UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        
        ExpFasesBean nuevafase = new ExpFasesBean();
        nuevafase.setDescripcion("");
        nuevafase.setNombre("");        
        nuevafase.setIdTipoExpediente(Integer.valueOf(((FasesForm)formulario).getIdTipoExpediente()));
        
        Vector datos=new Vector();
        datos.add(nuevafase);
        request.setAttribute("datos", datos);
        request.setAttribute("editable", "1");

		return "mostrar";
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#insertar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
		
	    UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
	    ExpFasesAdm fasesadm = new ExpFasesAdm(this.getUserBean(request));
	    FasesForm form = (FasesForm) formulario;
	    
	    try {
//	      	Rellenamos el nuevo Bean		    
		    ExpFasesBean fase = new ExpFasesBean();	    
		    
		    fase.setIdInstitucion(Integer.valueOf(userBean.getLocation()));
		    fase.setIdTipoExpediente(Integer.valueOf(form.getIdTipoExpediente()));
		    fase.setIdFase(fasesadm.getNewIdFase(form.getIdTipoExpediente(),userBean));
		    if (form.getDiasAntelacion()==null || form.getDiasAntelacion().equals(""))
		    	fase.setDiasAntelacion(new Integer(0));
		    else
		    	fase.setDiasAntelacion(Integer.valueOf(form.getDiasAntelacion()));
		    if (form.getDiasVencimiento()==null || form.getDiasVencimiento().equals(""))
		    	fase.setDiasVencimiento(new Integer(1));
		    else
		    	fase.setDiasVencimiento(Integer.valueOf(form.getDiasVencimiento()));
		    fase.setNombre(form.getNombre());
		    
			//Ahora procedemos a insertarlo
		    fasesadm.insert(fase);
	        request.setAttribute("descOperation","messages.inserted.success");
	        
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
	    
	    FasesForm form = (FasesForm)formulario;
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        ExpFasesAdm fasesAdm = new ExpFasesAdm (this.getUserBean(request));
        
        Hashtable hashOld = (Hashtable)request.getSession().getAttribute("DATABACKUP");
        
        Hashtable hashNew = (Hashtable)hashOld.clone();	    
	    
        hashNew.put(ExpFasesBean.C_NOMBRE, form.getNombre());
	    String diasAntelacion = form.getDiasAntelacion();
	    if (Validaciones.validaNoInformado(diasAntelacion)){
	    	diasAntelacion = "0";
	    }
	    hashNew.put(ExpFasesBean.C_DIASANTELACION, diasAntelacion);
	    
	    String diasVencimiento = form.getDiasVencimiento();
	    if (Validaciones.validaNoInformado(diasAntelacion)){
	    	diasVencimiento = "0";
	    }
	    hashNew.put(ExpFasesBean.C_DIASVENCIMIENTO, diasVencimiento);
	    
	    request.removeAttribute("DATABACKUP");
	    
	    try {
        fasesAdm.update(hashNew, hashOld);
        request.setAttribute("descOperation","messages.updated.success");
	    } catch (Exception e) {
            throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null);
        }
	    
        /*request.setAttribute("modal","1");
	    return "refresh";*/
	    return exitoModal("messages.updated.success",request);
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#borrar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
		
		FasesForm form = (FasesForm)formulario;
		try {
	    form.setModal("false");
	    
	    UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
	    ExpFasesAdm fasesAdm = new ExpFasesAdm (this.getUserBean(request));
	    
	    Vector vOcultos = form.getDatosTablaOcultos(0);	    					
	    
	    Hashtable hash = new Hashtable();
	    	    
	    hash.put(ExpFasesBean.C_IDINSTITUCION, userBean.getLocation());
	    hash.put(ExpFasesBean.C_IDFASE, (String)vOcultos.elementAt(0));	
	    hash.put(ExpFasesBean.C_IDTIPOEXPEDIENTE,(String)vOcultos.elementAt(1) );
	    
	    if (!fasesAdm.delete(hash)){
        	throw new SIGAException("messages.elementoenuso.error");
        	//throw new ClsExceptions("mensaje: "+estadosAdm.getError());
        }
	       
	        request.setAttribute("descOperation","messages.deleted.success");
		} catch (Exception exc){
			throwExcp("messages.general.error",new String[] {"modulo.expedientes"},exc,null);
	    	//throwExcp("messages.elementoenuso.error",exc,null);

	    }

	    request.setAttribute("urlAction", "/SIGA/EXP_TiposExpedientes_Fases.do");
        
	    return "refresh";
	}

	protected String mostrarRegistro(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response, boolean bEditable) throws SIGAException
	{
        FasesForm form = (FasesForm)formulario;
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        ExpFasesAdm fasesAdm = new ExpFasesAdm (this.getUserBean(request));
        
		
		Vector vOcultos = form.getDatosTablaOcultos(0);		

        String idfase = (String)vOcultos.elementAt(0);
        String idtipoexpediente = (String)vOcultos.elementAt(1);
        String institucion = (String)userBean.getLocation();

        String where = " WHERE ";
        
        where += ExpFasesBean.C_IDFASE + " = '" + idfase + "' AND ";
        where += ExpFasesBean.C_IDINSTITUCION + " = '" + institucion + "' AND ";
        where += ExpFasesBean.C_IDTIPOEXPEDIENTE + " = '" + idtipoexpediente + "'";
       
        Vector datos = null;
        try {
            datos = fasesAdm.select(where);
        } catch (Exception e) {
            throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null);
        }
        request.setAttribute("datos", datos);
        request.setAttribute("editable", bEditable ? "1" : "0");
        
        if (bEditable)
        {
            Hashtable hashBackUp = new Hashtable();
            
            hashBackUp.put(ExpFasesBean.C_IDFASE, idfase);
            hashBackUp.put(ExpFasesBean.C_IDINSTITUCION, institucion);
            hashBackUp.put(ExpFasesBean.C_IDTIPOEXPEDIENTE, idtipoexpediente);            
            
            request.getSession().setAttribute("DATABACKUP", hashBackUp);
        }

		return "mostrar";        
	}
}
