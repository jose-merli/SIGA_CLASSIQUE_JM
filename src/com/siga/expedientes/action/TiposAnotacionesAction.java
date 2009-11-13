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
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.beans.CenClienteBean;
import com.siga.beans.ExpEstadosAdm;
import com.siga.beans.ExpEstadosBean;
import com.siga.beans.ExpFasesAdm;
import com.siga.beans.ExpFasesBean;
import com.siga.beans.ExpRolesBean;
import com.siga.beans.ExpTipoExpedienteAdm;
import com.siga.beans.ExpTipoExpedienteBean;
import com.siga.beans.ExpTiposAnotacionesAdm;
import com.siga.beans.ExpTiposAnotacionesBean;
import com.siga.beans.GenRecursosCatalogosAdm;
import com.siga.beans.GenRecursosCatalogosBean;
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
        
		//Aplicar acceso
        if(request.getParameter("acceso").equalsIgnoreCase("Ver")) {
            userBean.setAccessType(SIGAPTConstants.ACCESS_READ);
        }
        
        TiposAnotacionesForm form = (TiposAnotacionesForm)formulario;
        ExpTiposAnotacionesAdm anotacionesAdm = new ExpTiposAnotacionesAdm (this.getUserBean(request));
               
        String where = " WHERE ";
        
//      campos de búsqueda
        where += "TA." + ExpTiposAnotacionesBean.C_IDINSTITUCION + " = " + userBean.getLocation() + " AND TA." + ExpTiposAnotacionesBean.C_IDTIPOEXPEDIENTE + " = " + form.getIdTipoExpediente();
        
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
        where1 += ExpFasesBean.C_IDINSTITUCION + " = " + userBean.getLocation()+" AND "+ExpFasesBean.C_IDTIPOEXPEDIENTE + " = " + form.getIdTipoExpediente();
        
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
	    TiposAnotacionesForm form = (TiposAnotacionesForm)formulario;
        
        ExpTiposAnotacionesBean nuevaAnotacion = new ExpTiposAnotacionesBean();
        nuevaAnotacion.setNombre("");
        nuevaAnotacion.setMensaje("");
        nuevaAnotacion.setIdInstitucion(Integer.valueOf(userBean.getLocation()));
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
		
	    UserTransaction tx = null;
		UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));        
	    tx=userBean.getTransaction();
	    ExpTiposAnotacionesAdm anotacionesAdm = new ExpTiposAnotacionesAdm(this.getUserBean(request));
	    TiposAnotacionesForm form = (TiposAnotacionesForm) formulario;
	    
	    //Rellenamos el nuevo Bean
	    
	    ExpTiposAnotacionesBean anotacion = new ExpTiposAnotacionesBean();	    
	    
	    try{
	       
	    	tx.begin();
	    	
		    anotacion.setIdInstitucion(Integer.valueOf(form.getIdInstitucion()));
		    anotacion.setIdTipoExpediente(Integer.valueOf(form.getIdTipoExpediente()));
		    anotacion.setIdTipoAnotacion(anotacionesAdm.getNewIdTipoAnotacion(form.getIdTipoExpediente(),userBean));
		    anotacion.setMensaje(form.getMensaje());	    
		    anotacion.setIdEstado(form.getIdInst_idExp_idFase_idEstado().equals("")?null:Integer.valueOf(form.getIdEstado()));
		    anotacion.setIdFase(form.getIdInst_idExp_idFase().equals("")?null:Integer.valueOf(form.getIdFase()));
		    String idRecurso = GenRecursosCatalogosAdm.getNombreIdRecurso(ExpTiposAnotacionesBean.T_NOMBRETABLA, ExpTiposAnotacionesBean.C_NOMBRE, new Integer(form.getIdInstitucion()), form.getIdTipoExpediente()+"_"+anotacion.getIdTipoAnotacion());
		    anotacion.setNombre((idRecurso!=null)?""+idRecurso:form.getNombre());

		    //Ahora procedemos a insertarlo
		    if (anotacionesAdm.insert(anotacion))
	        {
	            request.setAttribute("descOperation","messages.inserted.success");
	        } else {
	        	throw new ClsExceptions("Error al insertar tipo de anotación. "+anotacionesAdm.getError());
	        }
		    
		    ///////////////////////////////////////////
        	// Multiidioma: Insertamos los recursos en gen_recursos_catalogos
    		if (idRecurso != null) {
    			String idRecursoAlias = GenRecursosCatalogosAdm.getNombreIdRecursoAlias(ExpTiposAnotacionesBean.T_NOMBRETABLA, ExpTiposAnotacionesBean.C_NOMBRE, new Integer(form.getIdInstitucion()), form.getIdTipoExpediente()+"_"+anotacion.getIdTipoAnotacion());
	        	GenRecursosCatalogosAdm admRecCatalogos = new GenRecursosCatalogosAdm (this.getUserBean(request));
	        	GenRecursosCatalogosBean recCatalogoBean = new GenRecursosCatalogosBean ();
	        	recCatalogoBean.setCampoTabla(ExpTiposAnotacionesBean.C_NOMBRE);
	        	recCatalogoBean.setDescripcion(form.getNombre());
	        	recCatalogoBean.setIdInstitucion(this.getIDInstitucion(request));
	        	recCatalogoBean.setIdRecurso(idRecurso);
	        	recCatalogoBean.setIdRecursoAlias(idRecursoAlias);
	        	recCatalogoBean.setNombreTabla(ExpTiposAnotacionesBean.T_NOMBRETABLA);
	        	if(!admRecCatalogos.insert(recCatalogoBean, userBean.getLanguageInstitucion())) { 
	        		throw new ClsExceptions ("Error al insertar en recursos catalogos "+admRecCatalogos.getError());
	        	}
        	}
        	///////////////////////////////////////////
		    
		    
		    tx.commit();
		    
	    } catch (Exception e) {
            throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,tx);
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
	    
	    UserTransaction tx = null;
	    UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));        
	    TiposAnotacionesForm form = (TiposAnotacionesForm)formulario;	    
	    ExpTiposAnotacionesAdm anotacionesAdm = new ExpTiposAnotacionesAdm (this.getUserBean(request));
	    
	    String pepe = request.getParameter("mensaje");
	    System.out.println("********************* VALOR RECIBIDO="+pepe);
	    	
	    try {
	        Vector vOcultos = form.getDatosTablaOcultos(0);
	        Hashtable hashOld = (Hashtable)request.getSession().getAttribute("DATABACKUP");
	        
	        Hashtable hashNew = (Hashtable)hashOld.clone();
		    
	        hashNew.put(ExpTiposAnotacionesBean.C_NOMBRE, form.getNombre());
		    hashNew.put(ExpTiposAnotacionesBean.C_IDTIPOANOTACION, hashOld.get(ExpTiposAnotacionesBean.C_IDTIPOANOTACION));
		    hashNew.put(ExpTiposAnotacionesBean.C_IDFASE, form.getIdFase());
		    hashNew.put(ExpTiposAnotacionesBean.C_IDESTADO, form.getIdEstado());
		    hashNew.put(ExpTiposAnotacionesBean.C_MENSAJE, form.getMensaje());	
	
		    Hashtable codigos = new Hashtable();
	        codigos.put(new Integer(1),form.getIdInstitucion());
	        codigos.put(new Integer(2),form.getIdTipoExpediente());
	        codigos.put(new Integer(3),form.getIdTipoAnotacion());
	        String sSQL = "SELECT NOMBRE FROM EXP_TIPOANOTACION WHERE IDINSTITUCION=:1 AND IDTIPOEXPEDIENTE=:2 AND IDTIPOANOTACION=:3";
		    
		    request.removeAttribute("DATABACKUP");
		    
		    RowsContainer rc = new RowsContainer();
		    
	    	tx = userBean.getTransaction();
	    	tx.begin();
	    	if (rc.findForUpdateBind(sSQL,codigos)) {
	
		        Row row = (Row)rc.get(0);
		        row.setCompareData(row.getRow());
		        
		        String idRecurso = GenRecursosCatalogosAdm.getNombreIdRecurso(ExpTiposAnotacionesBean.T_NOMBRETABLA, ExpTiposAnotacionesBean.C_NOMBRE, new Integer(form.getIdInstitucion()), form.getIdTipoExpediente()+"_"+form.getIdTipoAnotacion());
				if (idRecurso == null) {
					hashNew.put(ExpTipoExpedienteBean.C_NOMBRE, form.getNombre());
				} else {
					hashNew.put(ExpTipoExpedienteBean.C_NOMBRE, idRecurso);
				}
	
				if (!anotacionesAdm.updateDirect(hashNew, anotacionesAdm.getClavesBean(), anotacionesAdm.getCamposBean())) {
		        	throw new ClsExceptions("Error al actualizar el tipo anotación. "+anotacionesAdm.getError());
		        }
		        
	        	///////////////////////////////////////////
	        	// Multiidioma: Actualizamos los recursos en gen_recursos_catalogos
				// Long idRecurso = GenRecursosCatalogosAdm.getNombreIdRecurso(sNombreTabla, sNombreCampoDescripcion, (sLocal.equalsIgnoreCase("S")?this.getIDInstitucion(request):null), sCodigo);
				if (idRecurso != null) {
	    			String idRecursoAlias = GenRecursosCatalogosAdm.getNombreIdRecursoAlias(ExpTiposAnotacionesBean.T_NOMBRETABLA, ExpTiposAnotacionesBean.C_NOMBRE, new Integer(form.getIdInstitucion()), form.getIdTipoExpediente()+"_"+form.getIdTipoAnotacion());
	    			GenRecursosCatalogosAdm admRecCatalogos = new GenRecursosCatalogosAdm(this.getUserBean(request));
		        	GenRecursosCatalogosBean recCatalogoBean = new GenRecursosCatalogosBean ();
		        	recCatalogoBean.setDescripcion(form.getNombre());
		        	recCatalogoBean.setIdRecurso(idRecurso);
		        	recCatalogoBean.setIdRecursoAlias(idRecursoAlias);
		        	if(!admRecCatalogos.update(recCatalogoBean, this.getUserBean(request))) { 
		        		throw new ClsExceptions ("Error al insertar en recursos catalogos "+admRecCatalogos.getError());
		        	}
				}
	        	///////////////////////////////////////////
		        
	    	}
		    else {
		    	throw new ClsExceptions("No se encuentra el elemento a modificar");	    	
		    }
	
	    	tx.commit();
		    
	        
	    } catch (Exception e) {
            throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,tx);
        }
	    
        return exitoModal("messages.updated.success",request);
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#borrar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
		
		UserTransaction tx = null;
		TiposAnotacionesForm form = (TiposAnotacionesForm)formulario;
		
		try{
		    form.setModal("false");
		    
		    UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
		    tx = userBean.getTransaction();
		    
		    tx.begin();
		    
		    ExpTiposAnotacionesAdm anotacionesAdm = new ExpTiposAnotacionesAdm (this.getUserBean(request));
		    
		    Vector vOcultos = form.getDatosTablaOcultos(0);
		    Hashtable hash = new Hashtable();	    	    
		    hash.put(ExpTiposAnotacionesBean.C_IDINSTITUCION, userBean.getLocation());
		    hash.put(ExpTiposAnotacionesBean.C_IDTIPOANOTACION, (String)vOcultos.elementAt(0));	
		    hash.put(ExpTiposAnotacionesBean.C_IDTIPOEXPEDIENTE,form.getIdTipoExpediente());
	    
	    	if (!anotacionesAdm.delete(hash)){
	        	throw new ClsExceptions("Error al eliminar tipo de anotación. "+anotacionesAdm.getError());
	        }
	        
	    	///////////////////////////////////////////
        	// Multiidioma: Borramos los recursos en gen_recursos_catalogos
			String idRecurso = GenRecursosCatalogosAdm.getNombreIdRecurso(ExpTiposAnotacionesBean.T_NOMBRETABLA, ExpTiposAnotacionesBean.C_NOMBRE, new Integer(userBean.getLocation()), form.getIdTipoExpediente()+"_"+(String)hash.get(ExpTiposAnotacionesBean.C_IDTIPOANOTACION));
			if (idRecurso != null) {
    			GenRecursosCatalogosAdm admRecCatalogos = new GenRecursosCatalogosAdm (this.getUserBean(request));
	        	GenRecursosCatalogosBean recCatalogoBean = new GenRecursosCatalogosBean ();
	        	recCatalogoBean.setIdRecurso(idRecurso);
	        	if(!admRecCatalogos.delete(recCatalogoBean)) { 
	        		throw new ClsExceptions ("Error al eliminar recursos catalogos. "+admRecCatalogos.getError());
	        	}
			}
        	///////////////////////////////////////////
				    	
	        request.setAttribute("descOperation","messages.deleted.success");
	        tx.commit();
	        
		} catch (Exception exc){
			throwExcp("messages.general.error",new String[] {"modulo.expedientes"},exc,tx);
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

		Vector vVisibles = form.getDatosTablaVisibles(0);
		Vector vOcultos = form.getDatosTablaOcultos(0);		

        String idAnotacion = (String)vOcultos.elementAt(0);
        String descEstado="";
        
        String where = " WHERE ";
        
        where += ExpTiposAnotacionesBean.C_IDTIPOANOTACION + " = '" + idAnotacion + "' AND ";
        where += ExpTiposAnotacionesBean.C_IDINSTITUCION + " = '" + userBean.getLocation() + "' AND ";
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
        hashBackUp.put(ExpTiposAnotacionesBean.C_IDINSTITUCION, userBean.getLocation());
        hashBackUp.put(ExpTiposAnotacionesBean.C_IDTIPOEXPEDIENTE, form.getIdTipoExpediente());
        
        request.getSession().setAttribute("DATABACKUP", hashBackUp);
    
		return "mostrar";
	}


}
