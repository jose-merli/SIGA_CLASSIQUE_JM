
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
import com.siga.administracion.SIGAConstants;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenDireccionesBean;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.EnvDestinatariosBean;
import com.siga.beans.EnvEnviosAdm;
import com.siga.beans.EnvEnviosBean;
import com.siga.beans.EnvPlantillaRemitentesAdm;
import com.siga.beans.EnvPlantillaRemitentesBean;
import com.siga.beans.EnvPlantillasEnviosAdm;
import com.siga.beans.EnvPlantillasEnviosBean;
import com.siga.beans.EnvTipoEnviosAdm;
import com.siga.beans.EnvTipoEnviosBean;
import com.siga.envios.form.RemitentesPlantillasForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 * 
 * Clase action para el mantenimiento de remitentes del envio.<br/>
 * Gestiona la edicion y borrado de remitentes, así como la dirección de envio. 
 *
 */
public class RemitentesPlantillasAction extends MasterAction {
    
    
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
				UsrBean usrbean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
				//Si venimos de Productos y Servicios para la seleccion de la direccion del envio, damos acceso total:
				if (usrbean.isLetrado())
					usrbean.setAccessType(SIGAConstants.ACCESS_READ);
				
				miForm = (MasterForm) formulario;
				if (miForm != null) {
					String accion = miForm.getModo();

					if (accion != null) {
						if (accion.equalsIgnoreCase("enviarDireccion")){
							mapDestino = enviarDireccion(mapping, miForm, request, response);
						} 
						else if (accion.equalsIgnoreCase("buscarDirecciones")){
							mapDestino = buscarDirecciones(mapping, miForm, request, response);
						} 
						else {
							return super.executeInternal(mapping,formulario,request,response);
						}
					}
					else {
						return super.executeInternal(mapping,formulario,request,response);
					}
				}
			} while (false);

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null)	{ 
			    throw new ClsExceptions("El ActionMapping no puede ser nulo");
			}
			return mapping.findForward(mapDestino);
		} 
		catch (SIGAException es) {
			throw es;
		} 
		catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.envios"});
		}
	}

    protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{	                  
        String idInstitucion     = "" + this.getIDInstitucion(request);
        String idTipoEnvios      = (String)request.getParameter("idTipoEnvio");
        String nombrePlantilla   = (String)request.getParameter("plantilla");
        String idPlantillaEnvios = (String)request.getParameter("idPlantillaEnvios");
        String editable = (String)request.getParameter("editable").toString();
        Hashtable<String,Object> htTipo = new Hashtable<String, Object>();
        
        try {	        
	        htTipo.put(EnvTipoEnviosBean.C_IDTIPOENVIOS,idTipoEnvios);
	        EnvTipoEnviosAdm tipoAdm = new EnvTipoEnviosAdm (this.getUserBean(request));
	        EnvTipoEnviosBean tipoBean = (EnvTipoEnviosBean)tipoAdm.selectByPK(htTipo).firstElement();	        
	        
	        
	        EnvPlantillasEnviosAdm plantillasEnvioAdm = new EnvPlantillasEnviosAdm (this.getUserBean(request));
	        htTipo.put(EnvPlantillasEnviosBean.C_IDINSTITUCION,idInstitucion);
	        htTipo.put(EnvPlantillasEnviosBean.C_IDPLANTILLAENVIOS,idPlantillaEnvios);
	        
	        EnvPlantillasEnviosBean plantillasEnvioBean = (EnvPlantillasEnviosBean) plantillasEnvioAdm.selectByPK(htTipo).firstElement();	        
	        nombrePlantilla = plantillasEnvioBean.getNombre();	        	        	       
	        
	        request.setAttribute("tipo", tipoBean.getNombre());
	        request.setAttribute("datos", plantillasEnvioAdm.getRemitentes(idInstitucion,idTipoEnvios,idPlantillaEnvios)); 
	        request.setAttribute("nombrePlantilla", nombrePlantilla);
	        request.setAttribute("acuseRecibo", plantillasEnvioBean.getAcuseRecibo());
	        request.setAttribute("idTipoEnvio", idTipoEnvios);
	        request.setAttribute("idPlantillaEnvios", idPlantillaEnvios);
	        request.setAttribute("editable", editable);
        } 
        catch (Exception e) {
            throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
        }
		return "inicio";
	}

	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
	    
	    try {
		    RemitentesPlantillasForm form = (RemitentesPlantillasForm) formulario;

		    String idInstitucion     = form.getIdInstitucion();
		    String idPersona         = form.getIdPersona();
		    String idTipoEnvios      = form.getIdTipoEnvios();
		    String idPlantillaEnvios = form.getIdPlantillaEnvios();
		    
	        // Rellenamos el nuevo Bean		    
		    EnvPlantillaRemitentesBean remitente = new EnvPlantillaRemitentesBean();
		    remitente.setIdInstitucion(Integer.valueOf(idInstitucion));
		    remitente.setIdTipoEnvios(Integer.valueOf(idTipoEnvios));
		    remitente.setIdPlantillaEnvios(Integer.valueOf(idPlantillaEnvios));
		    remitente.setIdPersona(Long.valueOf(idPersona));
		    remitente.setDomicilio(form.getDomicilio());
		    remitente.setCodigoPostal(form.getCodigoPostal());
		    
		    try{
		        Integer.valueOf(form.getIdPoblacion());
		        remitente.setIdPoblacion(form.getIdPoblacion());			    
		    } 
		    catch (NumberFormatException e){
		        remitente.setIdPoblacion("");			    
		    }
		    try{
		        Integer.valueOf(form.getIdProvincia());
		        remitente.setIdProvincia(form.getIdProvincia());
		    } 
		    catch (NumberFormatException e){
		        remitente.setIdProvincia("");			    
		    }
		    try{
		        Integer.valueOf(form.getIdPais());
		        remitente.setIdPais(form.getIdPais());
		        if (remitente.getIdPais().equals("")) remitente.setIdPais(ClsConstants.ID_PAIS_ESPANA); 
		    } 
		    catch (NumberFormatException e){}
		    
		    remitente.setPoblacionExtranjera(form.getPoblacionExt());
			remitente.setFax1(form.getFax1());
			remitente.setFax2(form.getFax2());
			remitente.setMovil(form.getMovil());
			remitente.setCorreoElectronico(form.getCorreoElectronico());
			remitente.setDescripcion(form.getDescripcion());
			
		    //Ahora procedemos a insertarlo
		    EnvPlantillaRemitentesAdm remitentesAdm = new EnvPlantillaRemitentesAdm(this.getUserBean(request));
		    if (!remitentesAdm.existeRemitente(idTipoEnvios, idPlantillaEnvios,idInstitucion,idPersona)){
		        remitentesAdm.insert(remitente);
		        request.setAttribute("descOperation","messages.inserted.success");	            
		    } 
		    else {
		        throw new SIGAException("messages.envios.error.existeelemento");
		    }
		} 
	    catch (Exception e) {
		      throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
		}
		return exitoModal("messages.inserted.success",request);
	}
	
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response)	throws SIGAException 
	{
	    return mostrarRegistro(mapping,formulario,request,response,true);
	}

	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
	    return mostrarRegistro(mapping,formulario,request,response,false);	
	}
	
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
	    RemitentesPlantillasForm form = (RemitentesPlantillasForm)formulario;
	    EnvPlantillaRemitentesAdm remAdm = new EnvPlantillaRemitentesAdm (this.getUserBean(request));
	    
	    Hashtable htPk = new Hashtable();
	    htPk.put(EnvPlantillaRemitentesBean.C_IDPERSONA,         form.getIdPersona());
	    htPk.put(EnvPlantillaRemitentesBean.C_IDINSTITUCION,     form.getIdInstitucion());
	    htPk.put(EnvPlantillaRemitentesBean.C_IDTIPOENVIOS,      form.getIdTipoEnvios());
	    htPk.put(EnvPlantillaRemitentesBean.C_IDPLANTILLAENVIOS, form.getIdPlantillaEnvios());
	    
	    //Recupero el bean del remitente
	    EnvPlantillaRemitentesBean remBean = null;
        try {
            remBean = (EnvPlantillaRemitentesBean)remAdm.selectByPKForUpdate(htPk).firstElement();
        } 
        catch (Exception e) {
            throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
        }
        // Modificamos los valores que vienen del formulario
        // Recordar que el bean guarda en su interior los datos antiguos
        remBean.setDomicilio(form.getDomicilio());
        remBean.setCodigoPostal(form.getCodigoPostal());
         
	    try {
	        Integer.valueOf(form.getIdPoblacion());
	        remBean.setIdPoblacion(form.getIdPoblacion());			    
	    } 
	    catch (NumberFormatException e){
	        remBean.setIdPoblacion("");			    
	    }
	    try {
	        Integer.valueOf(form.getIdProvincia());
	        remBean.setIdProvincia(form.getIdProvincia());
	    } 
	    catch (NumberFormatException e){
	        remBean.setIdProvincia("");
	    }
	    try {
	        Integer.valueOf(form.getIdPais());
	        remBean.setIdPais(form.getIdPais());
	        if (remBean.getIdPais().equals("")) remBean.setIdPais(ClsConstants.ID_PAIS_ESPANA); 
	    } 
	    catch (NumberFormatException e){}
	    
	    remBean.setPoblacionExtranjera(form.getPoblacionExt());
	    remBean.setCorreoElectronico(form.getCorreoElectronico());
	    remBean.setFax1(form.getFax1());
	    remBean.setFax2(form.getFax2());
	    remBean.setMovil(form.getMovil());  
	    remBean.setDescripcion(form.getDescripcion());        
	    
        try {
            remAdm.update(remBean);            
	    } 
        catch (Exception exc) {
	        throwExcp("messages.general.error",new String[] {"modulo.envios"},exc,null);
	    }
	    return exitoModal("messages.updated.success",request);
	}
	
	protected String mostrarRegistro(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response, boolean bEditable)	throws SIGAException
	{
	    RemitentesPlantillasForm form = (RemitentesPlantillasForm)formulario;
	    Vector vOcultos = form.getDatosTablaOcultos(0);
	    
	    String idPersona         = (String)vOcultos.elementAt(0);
	    String idPlantillaEnvios = (String)vOcultos.elementAt(1);
	    String idTipoEnvios      = (String)vOcultos.elementAt(2);
	    String idInstitucion     = "" + this.getIDInstitucion(request);
	    
		EnvPlantillaRemitentesAdm destAdm = new EnvPlantillaRemitentesAdm(this.getUserBean(request));
		Vector datos = new Vector();
		Row datosRem = new Row();
		try {
            datos = destAdm.getDatosRemitente(idTipoEnvios, idPlantillaEnvios, idInstitucion,idPersona);
            datosRem = (Row)datos.firstElement();    		
		} 
		catch (Exception e) {
            this.throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
        }        
        
        //seteamos los valores del formulario
		form.setIdInstitucion(idInstitucion);
		form.setIdTipoEnvios(idTipoEnvios);
		form.setIdPlantillaEnvios(idPlantillaEnvios);
		form.setIdPersona(idPersona);
        form.setNombre(datosRem.getString(CenPersonaBean.C_NOMBRE));
        form.setApellidos1(datosRem.getString(CenPersonaBean.C_APELLIDOS1));
        form.setApellidos2(datosRem.getString(CenPersonaBean.C_APELLIDOS2));
        form.setNumColegiado(datosRem.getString(CenColegiadoBean.C_NCOLEGIADO));
        form.setNifcif(datosRem.getString(CenPersonaBean.C_NIFCIF));
        form.setDescripcion(datosRem.getString(EnvPlantillaRemitentesBean.C_DESCRIPCION));
        form.setDomicilio(datosRem.getString(EnvDestinatariosBean.C_DOMICILIO));
        form.setPoblacion(datosRem.getString("POBLACION"));
        form.setPoblacionExt(datosRem.getString("POBLACIONEXTRANJERA"));
        form.setProvincia(datosRem.getString("PROVINCIA"));
        form.setPais(datosRem.getString("PAIS"));
        form.setCodigoPostal(datosRem.getString(EnvDestinatariosBean.C_CODIGOPOSTAL));
        form.setFax1(datosRem.getString(EnvDestinatariosBean.C_FAX1));
        form.setFax2(datosRem.getString(EnvDestinatariosBean.C_FAX2));
        form.setMovil(datosRem.getString(EnvDestinatariosBean.C_MOVIL));
        form.setCorreoElectronico(datosRem.getString(EnvDestinatariosBean.C_CORREOELECTRONICO));
                
        String editable = bEditable?"true":"false";
        request.setAttribute("editable",editable);
        request.setAttribute("accion", "editar");		
	    
		return "editar";
	}
	
	
	/**
	 * @see com.siga.general.MasterAction#buscar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
    protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{
        return "buscarDirecciones";
	}
	
	/** 
	 * @see com.siga.general.MasterAction#nuevo(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{	            
		try {
			RemitentesPlantillasForm f = (RemitentesPlantillasForm)formulario;
			EnvEnviosAdm adm = new EnvEnviosAdm (this.getUserBean(request));
			Vector v = adm.getDirecciones(f.getIdInstitucion(), f.getIdPersona(), f.getIdTipoEnvios());
			if (v != null && v.size() == 1)
				request.setAttribute("unicaDireccion", (Hashtable)v.get(0));
		}
		catch (Exception e) {}
		
	    request.setAttribute("accion", "nuevo");		
	    request.setAttribute("editable","true");        
        return "editar";
	}
	
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		RemitentesPlantillasForm form = (RemitentesPlantillasForm)formulario;
	    form.setModal("false");
	    
	    Vector vOcultos = form.getDatosTablaOcultos(0);	    					
	    String idPersona         = (String)vOcultos.elementAt(0);
	    String idPlantillaEnvios = (String)vOcultos.elementAt(1);
	    String idTipoEnvios      = (String)vOcultos.elementAt(2);
	    
	    Hashtable hash = new Hashtable();
	    hash.put(EnvPlantillaRemitentesBean.C_IDINSTITUCION,     this.getIDInstitucion(request));
	    hash.put(EnvPlantillaRemitentesBean.C_IDPERSONA,         idPersona);
	    hash.put(EnvPlantillaRemitentesBean.C_IDTIPOENVIOS,      idTipoEnvios);	
	    hash.put(EnvPlantillaRemitentesBean.C_IDPLANTILLAENVIOS, idPlantillaEnvios);	
	    
	    try {
		    EnvPlantillaRemitentesAdm destinatarioAdm = new EnvPlantillaRemitentesAdm (this.getUserBean(request));
	        destinatarioAdm.delete(hash);	        
	    } 
	    catch (Exception e) {
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
	protected String enviarDireccion (ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		try {
			RemitentesPlantillasForm form = (RemitentesPlantillasForm)formulario;
			
			String datos=(String)request.getParameter("datosSeleccionados");
			String datos1 []=datos.split(",");

			// OBTENGO VALORES DEL FORM
			// solamente el 0 porque es el unico que he pulsado
			Vector fila = form.getDatosTablaOcultos(0);

			// obtener datos de Direccion
			String direccion = (String)datos1[0];
			String poblacion =  (String)datos1[1];
			String provincia = (String)datos1[2];
			String pais = (String)datos1[3];
			String cp = (String)datos1[4];
			String fax1 = (String)datos1[5];
			String fax2 = (String)datos1[6];
			String correoElectronico = (String)datos1[7];
			String idPoblacion = (String)datos1[8];
			String idProvincia = (String)datos1[9];
			String idPais = (String)datos1[10];
			String idDireccion = (String)datos1[11];
			String telefono1 = (String)datos1[12];
			String movil = (String)datos1[13];
			String poblacionExtranjera ="";
			try {
				poblacionExtranjera = (String)datos1[14];
			} 
			catch (Exception e) {}
			
			HttpSession ses=request.getSession();
	        String idTipoEnvio = (String)ses.getAttribute("DATABACKUP_TIPOENVIO");
			
			Hashtable ht = new Hashtable();
			ht.put(EnvPlantillaRemitentesBean.C_DOMICILIO,UtilidadesString.andToComa(direccion));
			ht.put("POBLACION",poblacion);
			ht.put("PROVINCIA",provincia);
			ht.put("PAIS",pais);
			ht.put(EnvPlantillaRemitentesBean.C_CODIGOPOSTAL,cp);
			ht.put(EnvPlantillaRemitentesBean.C_FAX1,fax1);
			ht.put(EnvPlantillaRemitentesBean.C_FAX2,fax2);
			ht.put(EnvPlantillaRemitentesBean.C_CORREOELECTRONICO,correoElectronico);
			ht.put(EnvPlantillaRemitentesBean.C_IDPOBLACION,idPoblacion);
			ht.put(EnvPlantillaRemitentesBean.C_POBLACIONEXTRANJERA,poblacionExtranjera);
			ht.put(EnvPlantillaRemitentesBean.C_IDPROVINCIA,idProvincia);
			ht.put(EnvPlantillaRemitentesBean.C_IDPAIS,idPais);
			ht.put(CenDireccionesBean.C_IDDIRECCION,idDireccion);
			ht.put(CenDireccionesBean.C_TELEFONO1,telefono1);
			ht.put(CenDireccionesBean.C_MOVIL,movil);
			ht.put(EnvEnviosBean.C_IDTIPOENVIOS,idTipoEnvio);
			
			request.setAttribute("direccion", ht);		
			request.setAttribute("accion", "editar");		
	     } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
	   	 }
		 return "seleccion";
	}

	/**
	 * 
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return
	 * @throws SIGAException
	 */
    protected String buscarDirecciones(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
        RemitentesPlantillasForm form = (RemitentesPlantillasForm)formulario;
        HttpSession ses=request.getSession();
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        
        String idInstitucion = form.getIdInstitucion();
        String idPersona = form.getIdPersona();
        String idTipoEnvio = form.getIdTipoEnvios();
        ses.setAttribute("DATABACKUP_TIPOENVIO",idTipoEnvio);
        
        EnvEnviosAdm enviosAdm = new EnvEnviosAdm (this.getUserBean(request));
         
        Vector direcciones = null;
        try {
            direcciones = enviosAdm.getDirecciones(idInstitucion,idPersona,idTipoEnvio);
        } 
        catch (Exception e) {
            this.throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
        }
             
        request.setAttribute("direcciones", direcciones);
        return "mostrarDirecciones";        
	}
}