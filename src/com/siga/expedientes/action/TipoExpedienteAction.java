/*
 * Created on Dec 22, 2004
 * @author emilio.grau
 *
 */
package com.siga.expedientes.action;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.ExpCampoTipoExpedienteAdm;
import com.siga.beans.ExpCampoTipoExpedienteBean;
import com.siga.beans.ExpCamposExpedientesAdm;
import com.siga.beans.ExpCamposExpedientesBean;
import com.siga.beans.ExpPestanaConfAdm;
import com.siga.beans.ExpPestanaConfBean;
import com.siga.beans.ExpRolesAdm;
import com.siga.beans.ExpRolesBean;
import com.siga.beans.ExpTipoExpedienteAdm;
import com.siga.beans.ExpTipoExpedienteBean;
import com.siga.beans.ExpTiposAnotacionesAdm;
import com.siga.beans.ExpTiposAnotacionesBean;
import com.siga.beans.GenRecursosCatalogosAdm;
import com.siga.beans.GenRecursosCatalogosBean;
import com.siga.expedientes.form.TipoExpedienteForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 * 
 * Clase action para el mantenimiento de tipos de expediente.<br/>
 * Gestiona la edicion, borrado, consulta y mantenimiento de los expedientes. 
 *
 */

public class TipoExpedienteAction extends MasterAction {

	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{	                  
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        ExpTipoExpedienteAdm tipoExpAdm = new ExpTipoExpedienteAdm (this.getUserBean(request));
        
        String institucion = userBean.getLocation();
        
        // Mostramos todos los expedientes propios de la institución y aquellos
        // de otras insituciones que sean generales.
        String where = " WHERE ";
        
        where += ExpTipoExpedienteBean.C_IDINSTITUCION + " = " + institucion +
        		 " OR " + ExpTipoExpedienteBean.C_ESGENERAL + " = 'S'";
        
        Vector datos = tipoExpAdm.select(where);
        
        request.setAttribute("datos", datos); 
       
        
		return "abrir";
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#editar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions {
	    	        
	    TipoExpedienteForm form = (TipoExpedienteForm)formulario;
	    String idtipoexpediente= null;
	    String idInstitucionTipoExpediente = null;
	    if(form.getIdTipoExpediente()!=null &&!form.getIdTipoExpediente().equals("") &&
	    		form.getIdInstitucion()!=null &&!form.getIdInstitucion().equals("")){
	    	idtipoexpediente=form.getIdTipoExpediente();
		    idInstitucionTipoExpediente = form.getIdInstitucion();
	    }else{
		    Vector vOcultos = form.getDatosTablaOcultos(0);
		    // Recuperamos el tipo de expediente
		    
		    idtipoexpediente=(String)vOcultos.elementAt(0);
		    idInstitucionTipoExpediente = (String)vOcultos.elementAt(1);
	    }
	    
	    UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
	    ExpTipoExpedienteAdm tipoExpedienteAdm = new ExpTipoExpedienteAdm(userBean);
    	Hashtable tipoExpedienteHashtable = new Hashtable();
    	tipoExpedienteHashtable.put(ExpTipoExpedienteBean.C_IDTIPOEXPEDIENTE,idtipoexpediente);
    	tipoExpedienteHashtable.put(ExpTipoExpedienteBean.C_IDINSTITUCION,idInstitucionTipoExpediente);
    	Vector tipoExpVector= tipoExpedienteAdm.selectByPK(tipoExpedienteHashtable);
    	ExpTipoExpedienteBean tipoExpedienteBean = (ExpTipoExpedienteBean) tipoExpVector.get(0);
    	if(tipoExpedienteBean.getEnviarAvisos()==null || tipoExpedienteBean.getEnviarAvisos().toString().equals(ClsConstants.DB_FALSE)){
	    	String[] pestanasOcultas=new String [1];
	 		pestanasOcultas[0]=ClsConstants.IDPROCESO_DESTINATARIOSEXPEDIENTE;
	 		request.setAttribute("pestanasOcultas",pestanasOcultas);
    	} 
	    
	    
	    if(form.getRefresh()==null) {	        
	    request.setAttribute("urlAction",request.getRequestURI());
	    return "refresh2";
	    }
	    
	    form.setModal("false");
	   
	    
	    // Controlamos el tipo de acceso 
	    String acceso="Editar";
	    if(form.getModo().equalsIgnoreCase("Ver")) acceso="Ver";	    
	    
	    //Anhadimos parametros para las pestanhas
	    Hashtable htParametros=new Hashtable();
	    htParametros.put("idTipoExpediente",idtipoexpediente);
	    htParametros.put("idInstitucionTipoExpediente",idInstitucionTipoExpediente);
	    htParametros.put("acceso",acceso);
	    request.setAttribute("tipoexpediente", htParametros);
	    
	    request.setAttribute("urlAction", "/SIGA/EXP_MantenerTiposExpedientes.do");
        
	    if (userBean.getLocation().equals(idInstitucionTipoExpediente)){
	    	
	        return "editar";
	    } else {	    
	        return "permisos";
	    }
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#ver(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions {
	
	    return editar(mapping,formulario,request,response);
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#nuevo(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions {
	    UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        
        ExpTipoExpedienteBean nuevoTipo = new ExpTipoExpedienteBean();
        nuevoTipo.setNombre("");        
        nuevoTipo.setEsGeneral("N");
        nuevoTipo.setIdInstitucion(Integer.valueOf(userBean.getLocation()));
        Vector datos=new Vector();
        datos.add(nuevoTipo);
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
	    ExpTipoExpedienteAdm tipoExpedienteAdm = new ExpTipoExpedienteAdm(this.getUserBean(request));
	    ExpCamposExpedientesAdm camposExpAdm = new ExpCamposExpedientesAdm(this.getUserBean(request));
	    ExpCampoTipoExpedienteAdm campoTipoExpAdm = new ExpCampoTipoExpedienteAdm(this.getUserBean(request));
	    ExpPestanaConfAdm pestanaConfAdm = new ExpPestanaConfAdm(this.getUserBean(request));
	    
	    TipoExpedienteForm form = (TipoExpedienteForm) formulario;
	    UserTransaction tx = userBean.getTransaction();	    
	    
	    //Rellenamos el nuevo Bean de tipo de expediente
	    
	    ExpTipoExpedienteBean tipo = new ExpTipoExpedienteBean();	    
	    Integer id = null;
	    try {
		    id=tipoExpedienteAdm.getNewIdTipoExpediente(userBean);	    
		    tipo.setIdInstitucion(Integer.valueOf(userBean.getLocation()));
		    tipo.setIdTipoExpediente(id);
		    tipo.setNombre(form.getNombre());
		    tipo.setEsGeneral(form.getEsGeneral());
		    tipo.setRelacionEjg(0);
		    
		    Vector camposExp = camposExpAdm.select();
		    Iterator camposIt = camposExp.iterator();		    
		    
	        tx.begin();
	        tipoExpedienteAdm.insert(tipo);
		    while (camposIt.hasNext()){
		        ExpCamposExpedientesBean camposExpBean = (ExpCamposExpedientesBean)camposIt.next();
		        ExpCampoTipoExpedienteBean campoTipoExpBean = new ExpCampoTipoExpedienteBean();
		        campoTipoExpBean.setIdCampo(camposExpBean.getIdCampo());
		        if (camposExpBean.getIdCampo().intValue()==14 || camposExpBean.getIdCampo().intValue()==15) {
		            // solo para los campos configurables de pestaña
		            campoTipoExpBean.setVisible(ExpCampoTipoExpedienteBean.si);
		        } else {
		            campoTipoExpBean.setVisible(ExpCampoTipoExpedienteBean.no);
		        }
		        campoTipoExpBean.setIdInstitucion(Integer.valueOf(userBean.getLocation()));
		        campoTipoExpBean.setIdTipoExpediente(id);
		        campoTipoExpAdm.insert(campoTipoExpBean);		        
		    }
		     
		    // RGG: meto las pestañas configurables sin seleccionar
		    // Pestaña 1
		    ExpPestanaConfBean pestanaBean = new ExpPestanaConfBean();
		    pestanaBean.setIdInstitucion(Integer.valueOf(userBean.getLocation()));
		    pestanaBean.setIdTipoExpediente(id); 
		    pestanaBean.setIdCampo(new Integer(ClsConstants.IDCAMPO_PARAPESTANACONF1));
		    pestanaBean.setIdPestanaConf(new Integer(1));
		    pestanaBean.setSeleccionado(new Integer(0));
		    pestanaBean.setNombre("");
		    if (!pestanaConfAdm.insert(pestanaBean)) {
		        throw new ClsExceptions("Error al insertar pestaña configurable: "+pestanaConfAdm.getError());
		    }
		    // Pestaña 2
		    pestanaBean = new ExpPestanaConfBean();
		    pestanaBean.setIdInstitucion(Integer.valueOf(userBean.getLocation()));
		    pestanaBean.setIdTipoExpediente(id);
		    pestanaBean.setIdCampo(new Integer(ClsConstants.IDCAMPO_PARAPESTANACONF2));
		    pestanaBean.setIdPestanaConf(new Integer(2));
		    pestanaBean.setSeleccionado(new Integer(0));
		    pestanaBean.setNombre("");
		    if (!pestanaConfAdm.insert(pestanaBean)) {
		        throw new ClsExceptions("Error al insertar pestaña configurable: "+pestanaConfAdm.getError());
		    }

		    
	        ExpTiposAnotacionesAdm admAnotacion = new ExpTiposAnotacionesAdm(userBean);

	        // anotación base de comunicaciones
	        ExpTiposAnotacionesBean beanAnotacion = new ExpTiposAnotacionesBean();
	        beanAnotacion.setIdInstitucion(tipo.getIdInstitucion());
	        beanAnotacion.setIdTipoExpediente(tipo.getIdTipoExpediente());
	        beanAnotacion.setIdTipoAnotacion(ExpTiposAnotacionesAdm.codigoTipoComunicacion);
	        ///
	        String idRecurso = GenRecursosCatalogosAdm.getNombreIdRecurso(ExpTiposAnotacionesBean.T_NOMBRETABLA, ExpTiposAnotacionesBean.C_NOMBRE, new Integer(tipo.getIdInstitucion()), tipo.getIdTipoExpediente()+"_"+ExpTiposAnotacionesAdm.codigoTipoComunicacion);
	        beanAnotacion.setNombre((idRecurso!=null)?""+idRecurso:UtilidadesString.getMensajeIdioma(userBean,"expedientes.tipoAnotacion.comunicacion.nombre"));

	        beanAnotacion.setMensaje(UtilidadesString.getMensajeIdioma(userBean,"expedientes.tipoAnotacion.comunicacion.mensaje"));	    
		    //Ahora procedemos a insertarlo
		    if (!admAnotacion.insert(beanAnotacion)) {
		        throw new ClsExceptions("Error al insertar anotación base (comunicacion). "+admAnotacion.getError());
		    }
		    ///////////////////////////////////////////
        	// Multiidioma: Insertamos los recursos en gen_recursos_catalogos
    		if (idRecurso != null) {
    			String idRecursoAlias = GenRecursosCatalogosAdm.getNombreIdRecursoAlias(ExpTiposAnotacionesBean.T_NOMBRETABLA, ExpTiposAnotacionesBean.C_NOMBRE, new Integer(tipo.getIdInstitucion()), tipo.getIdTipoExpediente()+"_"+ExpTiposAnotacionesAdm.codigoTipoComunicacion);
	        	GenRecursosCatalogosAdm admRecCatalogos = new GenRecursosCatalogosAdm (this.getUserBean(request));
	        	GenRecursosCatalogosBean recCatalogoBean = new GenRecursosCatalogosBean ();
	        	recCatalogoBean.setCampoTabla(ExpRolesBean.C_NOMBRE);
	        	recCatalogoBean.setDescripcion(UtilidadesString.getMensajeIdioma(userBean,"expedientes.tipoAnotacion.comunicacion.nombre"));
	        	recCatalogoBean.setIdInstitucion(this.getIDInstitucion(request));
	        	recCatalogoBean.setIdRecurso(idRecurso);
	        	recCatalogoBean.setIdRecursoAlias(idRecursoAlias);
	        	recCatalogoBean.setNombreTabla(ExpRolesBean.T_NOMBRETABLA);
	        	if(!admRecCatalogos.insert(recCatalogoBean, userBean.getLanguageInstitucion())) { 
	        		throw new ClsExceptions ("Error al insertar en recursos catalogos "+admRecCatalogos.getError());
	        	}
        	}
        	///////////////////////////////////////////

		    
	        // anotación base de anotaciones automaticas
	        beanAnotacion = new ExpTiposAnotacionesBean();
	        beanAnotacion.setIdInstitucion(tipo.getIdInstitucion());
	        beanAnotacion.setIdTipoExpediente(tipo.getIdTipoExpediente());
	        beanAnotacion.setIdTipoAnotacion(ExpTiposAnotacionesAdm.codigoTipoAutomatico);
	        ///
	        idRecurso = GenRecursosCatalogosAdm.getNombreIdRecurso(ExpTiposAnotacionesBean.T_NOMBRETABLA, ExpTiposAnotacionesBean.C_NOMBRE, new Integer(tipo.getIdInstitucion()), tipo.getIdTipoExpediente()+"_"+ExpTiposAnotacionesAdm.codigoTipoAutomatico);
	        beanAnotacion.setNombre((idRecurso!=null)?""+idRecurso:UtilidadesString.getMensajeIdioma(userBean,"expedientes.tipoAnotacion.automatica.nombre"));

	        beanAnotacion.setMensaje(UtilidadesString.getMensajeIdioma(userBean,"expedientes.tipoAnotacion.automatica.mensaje"));	    
		    //Ahora procedemos a insertarlo
		    if (!admAnotacion.insert(beanAnotacion)) {
		        throw new ClsExceptions("Error al insertar anotación base (automatica). "+admAnotacion.getError());
		    }
		    ///////////////////////////////////////////
        	// Multiidioma: Insertamos los recursos en gen_recursos_catalogos
    		if (idRecurso != null) {
    			String idRecursoAlias = GenRecursosCatalogosAdm.getNombreIdRecursoAlias(ExpTiposAnotacionesBean.T_NOMBRETABLA, ExpTiposAnotacionesBean.C_NOMBRE, new Integer(tipo.getIdInstitucion()), tipo.getIdTipoExpediente()+"_"+ExpTiposAnotacionesAdm.codigoTipoAutomatico);
	        	GenRecursosCatalogosAdm admRecCatalogos = new GenRecursosCatalogosAdm (this.getUserBean(request));
	        	GenRecursosCatalogosBean recCatalogoBean = new GenRecursosCatalogosBean ();
	        	recCatalogoBean.setCampoTabla(ExpRolesBean.C_NOMBRE);
	        	recCatalogoBean.setDescripcion(UtilidadesString.getMensajeIdioma(userBean,"expedientes.tipoAnotacion.automatica.nombre"));
	        	recCatalogoBean.setIdInstitucion(this.getIDInstitucion(request));
	        	recCatalogoBean.setIdRecurso(idRecurso);
	        	recCatalogoBean.setIdRecursoAlias(idRecursoAlias);
	        	recCatalogoBean.setNombreTabla(ExpRolesBean.T_NOMBRETABLA);
	        	if(!admRecCatalogos.insert(recCatalogoBean, userBean.getLanguageInstitucion())) { 
	        		throw new ClsExceptions ("Error al insertar en recursos catalogos "+admRecCatalogos.getError());
	        	}
        	}
        	///////////////////////////////////////////
		    
		    
	        // anotación base de cambios de estado
	        beanAnotacion = new ExpTiposAnotacionesBean();
	        beanAnotacion.setIdInstitucion(tipo.getIdInstitucion());
	        beanAnotacion.setIdTipoExpediente(tipo.getIdTipoExpediente());
	        beanAnotacion.setIdTipoAnotacion(ExpTiposAnotacionesAdm.codigoTipoCambioEstado);
	        ///
	        idRecurso = GenRecursosCatalogosAdm.getNombreIdRecurso(ExpTiposAnotacionesBean.T_NOMBRETABLA, ExpTiposAnotacionesBean.C_NOMBRE, new Integer(tipo.getIdInstitucion()), tipo.getIdTipoExpediente()+"_"+ExpTiposAnotacionesAdm.codigoTipoCambioEstado);
	        beanAnotacion.setNombre((idRecurso!=null)?""+idRecurso:UtilidadesString.getMensajeIdioma(userBean,"expedientes.tipoAnotacion.cambioEstado.nombre"));

	        //beanAnotacion.setNombre(UtilidadesString.getMensajeIdioma(userBean,"expedientes.tipoAnotacion.cambioEstado.nombre") );
	        beanAnotacion.setMensaje(UtilidadesString.getMensajeIdioma(userBean,"expedientes.tipoAnotacion.cambioEstado.mensaje"));	    
		    //Ahora procedemos a insertarlo
		    if (!admAnotacion.insert(beanAnotacion)) {
		        throw new ClsExceptions("Error al insertar anotación base (cambio de estado). "+admAnotacion.getError());
		    }
		    ///////////////////////////////////////////
        	// Multiidioma: Insertamos los recursos en gen_recursos_catalogos
    		if (idRecurso != null) {
    			String idRecursoAlias = GenRecursosCatalogosAdm.getNombreIdRecursoAlias(ExpTiposAnotacionesBean.T_NOMBRETABLA, ExpTiposAnotacionesBean.C_NOMBRE, new Integer(tipo.getIdInstitucion()), tipo.getIdTipoExpediente()+"_"+ExpTiposAnotacionesAdm.codigoTipoCambioEstado);
	        	GenRecursosCatalogosAdm admRecCatalogos = new GenRecursosCatalogosAdm (this.getUserBean(request));
	        	GenRecursosCatalogosBean recCatalogoBean = new GenRecursosCatalogosBean ();
	        	recCatalogoBean.setCampoTabla(ExpRolesBean.C_NOMBRE);
	        	recCatalogoBean.setDescripcion(UtilidadesString.getMensajeIdioma(userBean,"expedientes.tipoAnotacion.cambioEstado.nombre"));
	        	recCatalogoBean.setIdInstitucion(this.getIDInstitucion(request));
	        	recCatalogoBean.setIdRecurso(idRecurso);
	        	recCatalogoBean.setIdRecursoAlias(idRecursoAlias);
	        	recCatalogoBean.setNombreTabla(ExpRolesBean.T_NOMBRETABLA);
	        	if(!admRecCatalogos.insert(recCatalogoBean, userBean.getLanguageInstitucion())) { 
	        		throw new ClsExceptions ("Error al insertar en recursos catalogos "+admRecCatalogos.getError());
	        	}
        	}
        	///////////////////////////////////////////
		    
	        tx.commit();
	        request.setAttribute("descOperation","messages.inserted.success");
	              
        } catch (Exception e) {
            throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,tx);
        }
        //"retorno"contiene el valor que devolverá la ventana modal, el este caso el id
        request.setAttribute("retorno",String.valueOf(id.intValue())+","+userBean.getLocation()+",% "+form.getNombre()+",");
	    request.setAttribute("modal","1");
	    return "refresh";
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#borrar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
	throws SIGAException {

		TipoExpedienteForm form = (TipoExpedienteForm)formulario;

		form.setModal("false");

		UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));

		Vector vOcultos = form.getDatosTablaOcultos(0);

		Hashtable hash = new Hashtable();
		String idTipoExpediente = (String)vOcultos.elementAt(0);
		hash.put(ExpTipoExpedienteBean.C_IDINSTITUCION, userBean.getLocation());
		hash.put(ExpTipoExpedienteBean.C_IDTIPOEXPEDIENTE, idTipoExpediente);	
		String[] claves = {ExpTipoExpedienteBean.C_IDINSTITUCION,ExpTipoExpedienteBean.C_IDTIPOEXPEDIENTE};

		UserTransaction tx = null;
		try{
			tx = userBean.getTransaction();
			tx.begin();

			// Elimina los tipos_anotacion asociados al expediente y sus recursos
			ExpTiposAnotacionesAdm anotacionesAdm = new ExpTiposAnotacionesAdm(this.getUserBean(request));
			Vector anotaciones = anotacionesAdm.select(hash);
			for (Iterator iter = anotaciones.iterator(); iter.hasNext();){
				ExpTiposAnotacionesBean anotacionBean = (ExpTiposAnotacionesBean)iter.next();
				String idRecurso = GenRecursosCatalogosAdm.getNombreIdRecurso(ExpTiposAnotacionesBean.T_NOMBRETABLA, ExpTiposAnotacionesBean.C_NOMBRE, new Integer(userBean.getLocation()), idTipoExpediente + "_" + anotacionBean.getIdTipoAnotacion());
				if (idRecurso != null) {
					GenRecursosCatalogosAdm admRecCatalogos = new GenRecursosCatalogosAdm (this.getUserBean(request));
					GenRecursosCatalogosBean recCatalogoBean = new GenRecursosCatalogosBean ();
					recCatalogoBean.setIdRecurso(idRecurso);
					if(!admRecCatalogos.delete(recCatalogoBean)) { 
						throw new ClsExceptions ("Error al eliminar recursos catalogos. "+admRecCatalogos.getError());
					}
				}
			}
			if (!anotacionesAdm.deleteDirect(hash, claves)){
				throw new ClsExceptions ("Error al eliminar tipos de anotacion. "+anotacionesAdm.getError());
			}


			// Elimina los roles asociados al expediente y sus recursos
			ExpRolesAdm rolesAdm = new ExpRolesAdm (this.getUserBean(request));
			Vector roles = rolesAdm.select(hash);
			for (Iterator iter = roles.iterator(); iter.hasNext();){
				ExpRolesBean rolBean = (ExpRolesBean)iter.next();
				String idRecurso = GenRecursosCatalogosAdm.getNombreIdRecurso(ExpRolesBean.T_NOMBRETABLA, ExpRolesBean.C_NOMBRE, new Integer(userBean.getLocation()),  idTipoExpediente+"_"+rolBean.getIdRol());
				if (idRecurso != null) {
					GenRecursosCatalogosAdm admRecCatalogos = new GenRecursosCatalogosAdm (this.getUserBean(request));
					GenRecursosCatalogosBean recCatalogoBean = new GenRecursosCatalogosBean ();
					recCatalogoBean.setIdRecurso(idRecurso);
					if(!admRecCatalogos.delete(recCatalogoBean)) { 
						throw new ClsExceptions ("Error al eliminar recursos catalogos. "+admRecCatalogos.getError());
					}
				}
			}
			if (!rolesAdm.deleteDirect(hash, claves)){
				throw new ClsExceptions ("Error al eliminar roles. "+rolesAdm.getError());
			}

			// Elimina el expediente
			ExpTipoExpedienteAdm tipoExpAdm = new ExpTipoExpedienteAdm (this.getUserBean(request));
			if (!tipoExpAdm.delete(hash)){
				request.setAttribute("descOperation","error.messages.deleted");
			}
			else
			{
				request.setAttribute("descOperation","messages.deleted.success");
			}

			tx.commit();
		} catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,tx);
		}
		request.setAttribute("urlAction", "/SIGA/EXP_MantenerTiposExpedientes.do");

		return "refresh2";
	}

}
