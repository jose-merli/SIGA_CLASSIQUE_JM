/*
 * Created on Mar 28, 2005
 * @author jmgrau
 *
 */
package com.siga.envios.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.EnvDestinatariosAdm;
import com.siga.beans.EnvDestinatariosBean;
import com.siga.beans.EnvEnviosAdm;
import com.siga.beans.EnvEnviosBean;
import com.siga.beans.EnvTipoEnviosAdm;
import com.siga.beans.EnvTipoEnviosBean;
import com.siga.envios.form.RemitentesForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gui.processTree.SIGAPTConstants;

/**
 * 
 * Clase action para el mantenimiento de destinatarios manuales del envio.<br/>
 * Gestiona la edicion y borrado de destinatarios, as� como la direcci�n de envio. 
 *
 */
public class DestinatarioManualAction extends MasterAction {
    
    
	/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	
	public ActionForward executeInternal (ActionMapping mapping,
							      ActionForm formulario,
							      HttpServletRequest request, 
							      HttpServletResponse response) throws SIGAException {

		String mapDestino = "exception";
		MasterForm miForm = null;

		try { 
			do {
				miForm = (MasterForm) formulario;
				if (miForm != null) {
					String accion = miForm.getModo();

					if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
						mapDestino = abrir(mapping, miForm, request, response);
						break;				
					
					} else if (accion.equalsIgnoreCase("buscarDirecciones")){
						mapDestino = buscarDirecciones(mapping, miForm, request, response);
					
					} else if (accion.equalsIgnoreCase("enviarDireccion")){
						mapDestino = enviarDireccion(mapping, miForm, request, response);
					
					} else {
						return super.executeInternal(mapping,formulario,request,response);
					}
				}
			} while (false);

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null)	{ 
			    throw new ClsExceptions("El ActionMapping no puede ser nulo");
			}
			return mapping.findForward(mapDestino);
		} catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.envios"});
		}
	}

    protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{	                  
		//Aplicar acceso
        if(request.getParameter("acceso").equalsIgnoreCase("Ver")) {
            HttpSession ses=request.getSession();
        	UsrBean user=(UsrBean)ses.getAttribute("USRBEAN");
		    user.setAccessType(SIGAPTConstants.ACCESS_READ);
        }
        
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        
        String idInstitucion = userBean.getLocation();
        String idEnvio = (String)request.getParameter("idEnvio");
        
        //Recuperamos los datos del envio
        Hashtable htPk = new Hashtable();
        htPk.put(EnvDestinatariosBean.C_IDINSTITUCION,idInstitucion);
        htPk.put(EnvDestinatariosBean.C_IDENVIO,idEnvio);        
        
        //Recupero el bean del envio para mostrar el nombre y el tipo
        EnvEnviosAdm envioAdm = new EnvEnviosAdm (this.getUserBean(request));
        EnvTipoEnviosAdm tipoAdm = new EnvTipoEnviosAdm (this.getUserBean(request));
        Vector envio, tipo, datos;
        try {
            envio = envioAdm.selectByPK(htPk);        
	        EnvEnviosBean envioBean = (EnvEnviosBean)envio.firstElement();
	        request.setAttribute("nombreEnv", envioBean.getDescripcion());
	        
	        Hashtable htTipo = new Hashtable();
	        htTipo.put(EnvTipoEnviosBean.C_IDTIPOENVIOS,envioBean.getIdTipoEnvios());
	        tipo = tipoAdm.selectByPK(htTipo);
	        EnvTipoEnviosBean tipoBean = (EnvTipoEnviosBean)tipo.firstElement();	        
	        request.setAttribute("tipo", tipoBean.getNombre());
	        request.setAttribute("idTipoEnvio", tipoBean.getIdTipoEnvios());
	        
	        //recupero los destinatarios manuales y las paso a la jsp
	        datos = envioAdm.getDestinatariosManuales(idInstitucion,idEnvio);
	        request.setAttribute("datos", datos); 
	        
        } catch (Exception e) {
            throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
        }
		return "inicio";
	}


	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#insertar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
	    
	    EnvDestinatariosAdm destinatariosAdm = new EnvDestinatariosAdm(this.getUserBean(request));
	    RemitentesForm form = (RemitentesForm) formulario;
	    
	    try {
	        //Rellenamos el nuevo Bean		    
		    EnvDestinatariosBean destinatario = new EnvDestinatariosBean();
		    
		    String idInstitucion = form.getIdInstitucion();
		    String idPersona = form.getIdPersona();
		    String idEnvio = form.getIdEnvio();		    
		    
		    destinatario.setIdInstitucion(Integer.valueOf(idInstitucion));
		    destinatario.setIdEnvio(Integer.valueOf(idEnvio));
		    destinatario.setIdPersona(Long.valueOf(idPersona));
		    destinatario.setDomicilio(form.getDomicilio());
		    destinatario.setCodigoPostal(form.getCodigoPostal());
		    destinatario.setNombre(form.getNombre());
		    destinatario.setApellidos1(form.getApellidos1());
		    destinatario.setApellidos2(form.getApellidos2());
		    destinatario.setMovil(form.getMovil());
		    
		    
		    String idPoblacion="", idProvincia="", idPais="";
		    try{
		        Integer.valueOf(form.getIdPoblacion());
		        destinatario.setIdPoblacion(form.getIdPoblacion());			    
		    } catch (NumberFormatException e){
		        destinatario.setIdPoblacion("");			    
		    }
		    try{
		        Integer.valueOf(form.getIdProvincia());
		        destinatario.setIdProvincia(form.getIdProvincia());
		    } catch (NumberFormatException e){
		        destinatario.setIdProvincia("");
		    }
		    try{
		        Integer.valueOf(form.getIdPais());
		        destinatario.setIdPais(form.getIdPais());
		        if (destinatario.getIdPais().equals("")) destinatario.setIdPais(ClsConstants.ID_PAIS_ESPANA); 
		    } catch (NumberFormatException e){}

			destinatario.setPoblacionExtranjera(form.getPoblacionExt());

			destinatario.setFax1(form.getFax1());
			destinatario.setFax2(form.getFax2());
			destinatario.setCorreoElectronico(form.getCorreoElectronico());
			
			//Ahora procedemos a insertarlo si no ha sido insertado previamente
		    if (!destinatariosAdm.existeDestinatario(idEnvio,idInstitucion,idPersona)){
		        destinatariosAdm.insert(destinatario);
		        request.setAttribute("descOperation","messages.inserted.success");	            
		    } else {
		        throw new SIGAException("messages.envios.error.existeelemento");
		    }			
		    
		  } catch (Exception e) {
		      throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
		  }
		  return exitoModal("messages.inserted.success",request);
	}
	
	
	
	/** 
	 * @see com.siga.general.MasterAction#editar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
	        	   
	    return mostrarRegistro(mapping,formulario,request,response,true);
	}

	/**
	 * @see com.siga.general.MasterAction#ver(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
	    	    
	    return mostrarRegistro(mapping,formulario,request,response,false);	
	}
	
	/**
	 * @see com.siga.general.MasterAction#modificar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
	    
	    RemitentesForm form = (RemitentesForm)formulario;
	    EnvDestinatariosAdm destAdm = new EnvDestinatariosAdm (this.getUserBean(request));
	    
	    Hashtable htPk = new Hashtable();
	    htPk.put(EnvDestinatariosBean.C_IDINSTITUCION,form.getIdInstitucion());
	    htPk.put(EnvDestinatariosBean.C_IDENVIO,form.getIdEnvio());
	    htPk.put(EnvDestinatariosBean.C_IDPERSONA,form.getIdPersona());
	    
	    //Recupero el bean de la lista
	    EnvDestinatariosBean destBean = null;
        try {
            destBean = (EnvDestinatariosBean)destAdm.selectByPKForUpdate(htPk).firstElement();
        } catch (Exception e) {
            throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
        }
        // Modificamos los valores que vienen del formulario
        // Recordar que el bean guarda en su interior los datos antiguos
        destBean.setDomicilio(form.getDomicilio());
        destBean.setCodigoPostal(form.getCodigoPostal());
        
        String idPoblacion="", idProvincia="", idPais="";
	    try{
	        Integer.valueOf(form.getIdPoblacion());
	        destBean.setIdPoblacion(form.getIdPoblacion());			    
	    } catch (NumberFormatException e){
	        destBean.setIdPoblacion("");			    
		}
	    try{
	        Integer.valueOf(form.getIdProvincia());
	        destBean.setIdProvincia(form.getIdProvincia());
	    } catch (NumberFormatException e){
	        destBean.setIdProvincia("");
    	}
	    try{
	        Integer.valueOf(form.getIdPais());
	        destBean.setIdPais(form.getIdPais());
	        if (destBean.getIdPais().equals("")) destBean.setIdPais(ClsConstants.ID_PAIS_ESPANA); 
	    } catch (NumberFormatException e){}

	    destBean.setPoblacionExtranjera(form.getPoblacionExt());

	    destBean.setCorreoElectronico(form.getCorreoElectronico());
	    destBean.setMovil(form.getMovil());
	    
        destBean.setFax1(form.getFax1());
        destBean.setFax2(form.getFax2());        
	    
	    destBean.setNombre(form.getNombre());
	    destBean.setApellidos1(form.getApellidos1());
	    destBean.setApellidos2(form.getApellidos2());
	    
        try{
            destAdm.update(destBean);            
	    } catch (Exception exc) {
	        throwExcp("messages.general.error",new String[] {"modulo.expediente"},exc,null);
	    }
	    return exitoModal("messages.updated.success",request);
	}
	
	/** 
	 * @see com.siga.envios.BusqListaCorreosAction#mostrarRegistro(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	
	protected String mostrarRegistro(ActionMapping mapping, 
	        						 MasterForm formulario, 
	        						 HttpServletRequest request, 
	        						 HttpServletResponse response,
	        						 boolean bEditable)
			throws SIGAException{
	    
	    RemitentesForm form = (RemitentesForm)formulario;
	    Vector vOcultos = form.getDatosTablaOcultos(0);
	    
	    String idPersona = (String)vOcultos.elementAt(0);
	    String idEnvio = (String)vOcultos.elementAt(1);
	    String idTipoEnvio = (String)vOcultos.elementAt(2);
        
	    UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));   
	    String idInstitucion = userBean.getLocation();
	    
		EnvDestinatariosAdm destAdm = new EnvDestinatariosAdm(this.getUserBean(request));
		Vector datos = new Vector();
		Row datosDest = new Row();
		try {
            datos = destAdm.getDatosDestinatario(idEnvio,idInstitucion,idPersona);
            datosDest = (Row)datos.firstElement();    		
		} catch (Exception e) {
            this.throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
        }        
        
        //seteamos los valores del formulario
		form.setIdInstitucion(idInstitucion);
		form.setIdEnvio(idEnvio);
		form.setIdTipoEnvio(idTipoEnvio);
		form.setIdPersona(idPersona);
        form.setNombre(datosDest.getString(CenPersonaBean.C_NOMBRE));
        form.setApellidos1(datosDest.getString(CenPersonaBean.C_APELLIDOS1));
        form.setApellidos2(datosDest.getString(CenPersonaBean.C_APELLIDOS2));
        form.setNumColegiado(datosDest.getString(CenColegiadoBean.C_NCOLEGIADO));
        form.setNifcif(datosDest.getString(CenPersonaBean.C_NIFCIF));
        
        form.setDomicilio(datosDest.getString(EnvDestinatariosBean.C_DOMICILIO));
        form.setPoblacion(datosDest.getString("POBLACION"));
        form.setPoblacionExt(datosDest.getString("POBLACIONEXTRANJERA"));
        form.setProvincia(datosDest.getString("PROVINCIA"));
        form.setPais(datosDest.getString("PAIS"));
        form.setCodigoPostal(datosDest.getString(EnvDestinatariosBean.C_CODIGOPOSTAL));
        form.setFax1(datosDest.getString(EnvDestinatariosBean.C_FAX1));
        form.setFax2(datosDest.getString(EnvDestinatariosBean.C_FAX2));
        form.setCorreoElectronico(datosDest.getString(EnvDestinatariosBean.C_CORREOELECTRONICO));
        form.setMovil(datosDest.getString(EnvDestinatariosBean.C_MOVIL));
	    
               
        String editable = bEditable?"true":"false";
        
        request.setAttribute("editable",editable);
        request.setAttribute("accion", "editar");		
	    
		return "editar";
	}
	
	
	/**
	 * @see com.siga.general.MasterAction#buscar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
    protected String buscar(ActionMapping mapping, 
            				MasterForm formulario, 
            				HttpServletRequest request, 
            				HttpServletResponse response) throws SIGAException
	{
        /*RemitentesForm form = (RemitentesForm)formulario;
        HttpSession ses=request.getSession();
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        
        String idInstitucion = form.getIdInstitucion();
        String idPersona = form.getIdPersona();
        String idTipoEnvio = form.getIdTipoEnvio();
        
        EnvEnviosAdm enviosAdm = new EnvEnviosAdm (this.getUserName(request));
         
        Vector direcciones = null;
        try {
            direcciones = enviosAdm.getDirecciones(idInstitucion,idPersona,idTipoEnvio);
        } catch (Exception e) {
            this.throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
        }
        
        request.setAttribute("direcciones", direcciones);*/
        return "buscarDirecciones";
        
	}
    
    
	/**
	 * @see buscarDirecciones(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
    protected String buscarDirecciones(ActionMapping mapping, 
            							MasterForm formulario, 
            							HttpServletRequest request, 
            							HttpServletResponse response) throws SIGAException
	{
        RemitentesForm form = (RemitentesForm)formulario;
        HttpSession ses=request.getSession();
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        
        String idInstitucion = form.getIdInstitucion();
        String idPersona = form.getIdPersona();
        String idTipoEnvio = form.getIdTipoEnvio();
        
        EnvEnviosAdm enviosAdm = new EnvEnviosAdm (this.getUserBean(request));
         
        Vector direcciones = null;
        try {
            direcciones = enviosAdm.getDirecciones(idInstitucion,idPersona,idTipoEnvio);
        } catch (Exception e) {
            this.throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
        }
             
        request.setAttribute("direcciones", direcciones);
        return "mostrarDirecciones";        
	}
	
	/** 
	 * @see com.siga.general.MasterAction#nuevo(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {	            
	    
		try {
			RemitentesForm f = (RemitentesForm)formulario;
			EnvEnviosAdm adm = new EnvEnviosAdm (this.getUserBean(request));
			Vector v = adm.getDirecciones(f.getIdInstitucion(), f.getIdPersona(), f.getIdTipoEnvio());
			if (v != null && v.size() == 1)
				request.setAttribute("unicaDireccion", (Hashtable)v.get(0));
		}
		catch (Exception e) {}
		
	    request.setAttribute("accion", "nuevo");		
	    request.setAttribute("editable","true");        
        return "editar";
	}
	
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#borrar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
		
		RemitentesForm form = (RemitentesForm)formulario;
	    
	    form.setModal("false");
	    
	    UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
	    EnvDestinatariosAdm destinatarioAdm = new EnvDestinatariosAdm (this.getUserBean(request));
	    
	    Vector vOcultos = form.getDatosTablaOcultos(0);	    					
	    
	    Hashtable hash = new Hashtable();
	    	    
	    hash.put(EnvDestinatariosBean.C_IDINSTITUCION, userBean.getLocation());
	    hash.put(EnvDestinatariosBean.C_IDPERSONA,(String)vOcultos.elementAt(0) );
	    hash.put(EnvDestinatariosBean.C_IDENVIO, (String)vOcultos.elementAt(1));	
	    
	    try {
	        destinatarioAdm.delete(hash);	        
	    } catch (Exception e) {
	        this.throwExcp("messages.elementoenuso.error",e,null);
        }
		
		return exitoRefresco("messages.deleted.success",request);
	}

	/**
	 * Metodo que implementa el modo enviarDireccion
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String enviarDireccion (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException 
	{
		try {
			
			RemitentesForm form = (RemitentesForm)formulario;

			// OBTENGO VALORES DEL FORM
			// solamente el 0 porque es el unico que he pulsado
			Vector fila = form.getDatosTablaOcultos(0);

			// obtener datos de Direccion
			String direccion = (String)fila.get(0);
			String poblacion = (String)fila.get(1);
			String provincia = (String)fila.get(2);
			String pais = (String)fila.get(3);
			String cp = (String)fila.get(4);
			String fax1 = (String)fila.get(5);
			String fax2 = (String)fila.get(6);
			String correoElectronico = (String)fila.get(7);
			String idPoblacion = (String)fila.get(8);
			String idProvincia = (String)fila.get(9);
			String idPais = (String)fila.get(10);
			String poblacionExtranjera = (String)fila.get(11);
			String movil = (String)fila.get(13);
			
			
			Hashtable ht = new Hashtable();
			ht.put(EnvDestinatariosBean.C_DOMICILIO,UtilidadesString.andToComa(direccion));
			ht.put("POBLACION",poblacion);
			ht.put("PROVINCIA",provincia);
			ht.put("PAIS",pais);
			ht.put(EnvDestinatariosBean.C_CODIGOPOSTAL,cp);
			ht.put(EnvDestinatariosBean.C_FAX1,fax1);
			ht.put(EnvDestinatariosBean.C_FAX2,fax2);
			ht.put(EnvDestinatariosBean.C_CORREOELECTRONICO,correoElectronico);
			ht.put(EnvDestinatariosBean.C_IDPOBLACION,idPoblacion);
			ht.put(EnvDestinatariosBean.C_POBLACIONEXTRANJERA,poblacionExtranjera);
			ht.put(EnvDestinatariosBean.C_IDPROVINCIA,idProvincia);
			ht.put(EnvDestinatariosBean.C_IDPAIS,idPais);
			ht.put(EnvDestinatariosBean.C_MOVIL,movil);
			
			request.setAttribute("direccion", ht);		

			request.setAttribute("accion", "editar");		

	     } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
	   	 }
		 return "seleccion";
	}
	
}
