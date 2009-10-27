/*
 * Created on Feb 3, 2005
 * @author juan.grau
 *
 */
package com.siga.expedientes.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.Paginador;
import com.siga.beans.ExpAlertaAdm;
import com.siga.beans.ExpAlertaBean;
import com.siga.beans.ExpCampoTipoExpedienteAdm;
import com.siga.beans.ExpExpedienteAdm;
import com.siga.beans.ExpExpedienteBean;
import com.siga.expedientes.ExpPermisosTiposExpedientes;
import com.siga.expedientes.form.BusquedaAlertaForm;
import com.siga.expedientes.form.BusquedaExpedientesForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 * Action para la búsqueda de expedientes (simple y avanzada)
 */
public class BusquedaAlertaAction extends MasterAction {
	
	public ActionForward executeInternal (ActionMapping mapping,
							      ActionForm formulario,
							      HttpServletRequest request, 
							      HttpServletResponse response) throws SIGAException {

		String mapDestino = "exception";
		MasterForm miForm = null;
		try { 
			do {
				miForm = (MasterForm) formulario;
				if (miForm == null) {
					break;
				}
				String accion = miForm.getModo();

		  		// La primera vez que se carga el formulario 
				// Abrir
				if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
					mapDestino = abrir(mapping, miForm, request, response);
					break;
				}else if (accion.equalsIgnoreCase("buscarIni")){
					borrarPaginador(request, paginador);
					BusquedaAlertaForm formExp = (BusquedaAlertaForm)miForm;
					formExp.reset(new String[]{"datosPaginador"});
					//formExp.reset(mapping,request);
					mapDestino = buscar(mapping, miForm, request, response);
					break;
				}else if (accion.equalsIgnoreCase("buscar")){
					mapDestino = buscar(mapping, miForm, request, response);
					break;
				} else {
					return super.executeInternal(mapping,formulario,request,response);
				}
			} while (false);
			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) { 
			    throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
			}
			return mapping.findForward(mapDestino);
		} catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.censo"});
		}
	}	
	
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions {
		return("inicio");
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
    protected String buscarInit(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions {
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
    
    /**
     * Busca usando el paginador 
     */
    protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{
		String forward = "resultado";
		try{
			BusquedaAlertaForm miFormulario = (BusquedaAlertaForm)formulario; 
			UsrBean user = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
	        
	        String idInstitucion = user.getLocation();
	        ExpAlertaAdm alertaAdm = new ExpAlertaAdm (this.getUserBean(request));
	                
			miFormulario.setIdInstitucion(idInstitucion);
			
			HashMap databackup = (HashMap) miFormulario.getDatosPaginador();
			if (databackup!=null && databackup.get("paginador")!=null){ 
				Paginador paginador = (Paginador)databackup.get("paginador");
				Vector datos=new Vector();
	
	
				//Si no es la primera llamada, obtengo la página del request y la busco con el paginador
				String pagina = (String)request.getParameter("pagina");
	
				if (paginador!=null){	 
					if (pagina!=null){
						datos = paginador.obtenerPagina(Integer.parseInt(pagina));
					}else{// cuando hemos editado un registro de la busqueda y volvemos a la paginacion
						datos = paginador.obtenerPagina((paginador.getPaginaActual()));
					}
				}	
	
				databackup.put("paginador",paginador);
				databackup.put("datos",datos);
	
	
	
	
			}else{
				
				databackup=new HashMap();
	
				//obtengo datos de la consulta 			
				Paginador resultado = null;
				Vector datos = null;
				
				resultado = alertaAdm.getPaginadorAlertas(miFormulario,user);
				// Paso de parametros empleando la sesion
				databackup.put("paginador",resultado);
				
				if (resultado!=null){ 
					datos = resultado.obtenerPagina(1);
					databackup.put("datos",datos);
				} 
				miFormulario.setDatosPaginador(databackup);
			}
			//para saber en que tipo de busqueda estoy
			request.getSession().setAttribute("volverAuditoriaExpedientes","Al");
			
			//obtenemos los permisos a aplicar
			ExpPermisosTiposExpedientes perm=new ExpPermisosTiposExpedientes(user);
			request.setAttribute("permisos",perm);
			
		}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}
		
		return forward;
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
