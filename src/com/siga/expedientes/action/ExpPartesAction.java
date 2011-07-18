/*
 * Created on Jan 18, 2005
 * @author emilio.grau
 *
 */
package com.siga.expedientes.action;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenDireccionesBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.ExpCampoTipoExpedienteAdm;
import com.siga.beans.ExpCampoTipoExpedienteBean;
import com.siga.beans.ExpExpedienteAdm;
import com.siga.beans.ExpExpedienteBean;
import com.siga.beans.ExpPartesAdm;
import com.siga.beans.ExpPartesBean;
import com.siga.beans.ExpRolesBean;
import com.siga.expedientes.form.ExpPartesForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 * Action Partes de un expediente
 */
public class ExpPartesAction extends MasterAction {

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrir(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
    protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{	
    	try{
        
	        ExpPartesForm form = (ExpPartesForm)formulario;
	        ExpPartesAdm partesAdm = new ExpPartesAdm (this.getUserBean(request));
	               
	        String where = " WHERE ";
	                
			//Datos generales para todas las pestanhas
			String idInstitucion = request.getParameter("idInstitucion");
			form.setIdInstitucion(idInstitucion);
			String idInstitucion_TipoExpediente = request.getParameter("idInstitucion_TipoExpediente");
			String idTipoExpediente = request.getParameter("idTipoExpediente");
			String numExpediente = request.getParameter("numeroExpediente");
			String anioExpediente = request.getParameter("anioExpediente");
					
			where += "E."+ExpExpedienteBean.C_IDINSTITUCION + " = '" + idInstitucion + "' AND ";
			where += "E."+ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE + " = '" + idInstitucion_TipoExpediente + "' AND ";
			where += "E."+ExpExpedienteBean.C_IDTIPOEXPEDIENTE + " = '" + idTipoExpediente + "' AND ";
			where += "E."+ExpExpedienteBean.C_NUMEROEXPEDIENTE + " = '" + numExpediente + "' AND ";
			where += "E."+ExpExpedienteBean.C_ANIOEXPEDIENTE + " = '" + anioExpediente + "'";
			
		    //join de las tablas PARTE, ROLPARTE, EXPEDIENTE, PERSONA
			where += " AND E."+ExpExpedienteBean.C_IDINSTITUCION+" = PR."+ExpPartesBean.C_IDINSTITUCION;
	        where += " AND E."+ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE+" = PR."+ExpPartesBean.C_IDINSTITUCION_TIPOEXPEDIENTE;
	        where += " AND E."+ExpExpedienteBean.C_IDTIPOEXPEDIENTE+" = PR."+ExpPartesBean.C_IDTIPOEXPEDIENTE;
	        where += " AND E."+ExpExpedienteBean.C_NUMEROEXPEDIENTE+" = PR."+ExpPartesBean.C_NUMEROEXPEDIENTE;
	        where += " AND E."+ExpExpedienteBean.C_ANIOEXPEDIENTE+" = PR."+ExpPartesBean.C_ANIOEXPEDIENTE;
	        
	        where += " AND PR."+ExpPartesBean.C_IDINSTITUCION+" = RP."+ExpRolesBean.C_IDINSTITUCION;
	        where += " AND PR."+ExpPartesBean.C_IDTIPOEXPEDIENTE+" = RP."+ExpRolesBean.C_IDTIPOEXPEDIENTE;
	        where += " AND PR."+ExpPartesBean.C_IDROL+" = RP."+ExpRolesBean.C_IDROL;
	        
	        where += " AND PR."+ExpPartesBean.C_IDPERSONA+" = P."+CenPersonaBean.C_IDPERSONA;            
	        			
			Vector datos = partesAdm.selectPartesRolPersonaExp(where);
	
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
    	}
    	catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null); 
		}
        
		return "inicio";
	}
    
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#editar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
	    
	    return mostrarRegistro(mapping, formulario, request, response, true, false);
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#ver(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
	    
	    return mostrarRegistro(mapping, formulario, request, response, false, false);
	}
	

	/** 
	 * Funcion que muestra el formulario en modo consulta o edicion
	 * @param  mapping
	 * @param  formulario
	 * @param  request
	 * @param  response
	 * @param  bEditable
	 * @param  bNuevo 
	 * @exception  ClsExceptions  En cualquier caso de error
	 * @return String para el forward
	 */
	protected String mostrarRegistro(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response, boolean bEditable, boolean bNuevo) throws SIGAException
	{
		try{
		    ExpPartesForm form = (ExpPartesForm)formulario;
	        ExpPartesAdm partesAdm = new ExpPartesAdm (this.getUserBean(request));
	        
	        CenColegiadoAdm colAdm = new CenColegiadoAdm (this.getUserBean(request));
			HttpSession ses = request.getSession();
			HashMap datosExpediente = (HashMap)ses.getAttribute("DATABACKUP");
			
			if (!bNuevo){
				Vector vOcultos = form.getDatosTablaOcultos(0);		
		
				String idInstitucion = (String)vOcultos.elementAt(0);
				String idInstitucion_TipoExpediente = (String)vOcultos.elementAt(1);
				String idTipoExpediente = (String)vOcultos.elementAt(2);
				String numExpediente = (String)vOcultos.elementAt(3);
				String anioExpediente = (String)vOcultos.elementAt(4);
		        String idParte = (String)vOcultos.elementAt(5);
		        String idPersona = (String)vOcultos.elementAt(6);
		        String idRol = (String)vOcultos.elementAt(7);
		        String nombreRol = (String)vOcultos.elementAt(8);
		        String idDireccion = (String)vOcultos.elementAt(9);
		        request.setAttribute("idTipoExpediente",idTipoExpediente);	        
		        
		        form.setIdDireccion(idDireccion);
		        form.setIdPersona(idPersona);
		        form.setIdRol(idRol);
		        form.setRolSel(nombreRol);
		        form.setIdInstitucion(idInstitucion);
		        
		        
		        //obtenemos datos del colegiado
		        Hashtable hashCol = new Hashtable();
		        hashCol.put(CenColegiadoBean.C_IDINSTITUCION,idInstitucion);        
		        hashCol.put(CenColegiadoBean.C_IDPERSONA,idPersona);
		        Vector datosCol = colAdm.select(hashCol);
		        
		        if(datosCol!=null && datosCol.size()>0){
		        	CenColegiadoBean cBean = (CenColegiadoBean)datosCol.elementAt(0);
		        	form.setNumColegiado(cBean.getNColegiado());
		        }
		        else 
		        	form.setNumColegiado("");
		        CenPersonaAdm personaAdm = new CenPersonaAdm (this.getUserBean(request));
		        CenPersonaBean persona = personaAdm.getIdentificador(new Long(idPersona));
		        
		        form.setNombre(persona.getNombre());
		        form.setNif(persona.getNIFCIF());                
		        form.setPrimerApellido(persona.getApellido1());
		        form.setSegundoApellido(persona.getApellido2());
		        form.setIdPersona(idPersona);
		        
		        CenClienteAdm clienteAdm = new CenClienteAdm (this.getUserBean(request));
		        if(idDireccion!=null && !idDireccion.trim().equals("")){
			        Hashtable direccion = clienteAdm.getDirecciones(new Long(idPersona),new Integer(idInstitucion), new Long(idDireccion));
			        form.setDireccion(UtilidadesHash.getString(direccion, CenDireccionesBean.C_DOMICILIO));
			        form.setPoblacion(UtilidadesHash.getString(direccion, "POBLACION"));
			        form.setPoblacionExt(UtilidadesHash.getString(direccion, CenDireccionesBean.C_POBLACIONEXTRANJERA));
			        form.setProvincia(UtilidadesHash.getString(direccion, "PROVINCIA"));
			        form.setPais(UtilidadesHash.getString(direccion, "PAIS"));
			        form.setCpostal(UtilidadesHash.getString(direccion, CenDireccionesBean.C_CODIGOPOSTAL));
			        
			        String telefono = UtilidadesHash.getString(direccion, CenDireccionesBean.C_TELEFONO1);
			        if (telefono == null || telefono.equals("")) telefono = UtilidadesHash.getString(direccion, CenDireccionesBean.C_MOVIL);
			        form.setIdDireccion(idDireccion);
			        form.setTelefono(telefono);
		        }else{
		        	
			        form.setDireccion("");
			        form.setPoblacion("");
			        form.setPoblacionExt("");
			        form.setProvincia("");
			        form.setPais("");
			        form.setCpostal("");
			        form.setIdDireccion("");
			        form.setTelefono("");
		        	
		        }
		        
		        
		        if (bEditable){ //pensando en el update
		        	request.setAttribute("idRol",idRol);	
		        	
			        Hashtable hash = new Hashtable();		
		    		hash.put(ExpPartesBean.C_IDINSTITUCION,idInstitucion);
		    		hash.put(ExpPartesBean.C_IDINSTITUCION_TIPOEXPEDIENTE,idInstitucion_TipoExpediente);
		    		hash.put(ExpPartesBean.C_IDTIPOEXPEDIENTE,idTipoExpediente);
		    		hash.put(ExpPartesBean.C_NUMEROEXPEDIENTE,numExpediente);
		    		hash.put(ExpPartesBean.C_ANIOEXPEDIENTE,anioExpediente);
		    		hash.put(ExpPartesBean.C_IDPARTE,idParte);    		
		            Vector datos = partesAdm.select(hash);
		            ExpPartesBean bean = (ExpPartesBean)datos.elementAt(0);
		            datosExpediente.put("datosParticulares",bean);
		        	ses.setAttribute("DATABACKUP",datosExpediente);
		        }
			}else{
				
				
				 Hashtable hash = (Hashtable)datosExpediente.get("datosGenerales");
				 form.setIdInstitucion(hash.get(ExpExpedienteBean.C_IDINSTITUCION).toString());
				 request.setAttribute("idTipoExpediente",hash.get(ExpExpedienteBean.C_IDTIPOEXPEDIENTE));
				
				
			}
	        
	        if (bEditable){
	        	if (bNuevo){
	        		request.setAttribute("accion","nuevo");
	        	}else{
	        		request.setAttribute("accion","edicion");
	        	}
	        }else{
	        	request.setAttribute("accion","consulta");
	        }
		
		}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null); 
		}

		return "mostrar";
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#modificar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
	    
		try{
		    // Recuperamos los datos anteriores de la sesión
		    HttpSession ses=request.getSession();
		    HashMap datosExpediente = (HashMap)ses.getAttribute("DATABACKUP");
		    ExpPartesBean bean=(ExpPartesBean)datosExpediente.get("datosParticulares");	    
		    
		    ExpPartesForm form = (ExpPartesForm)formulario;
	        ExpPartesAdm parAdm = new ExpPartesAdm (this.getUserBean(request));
	        
	        // Actualizamos los datos del expediente
	        bean.setIdPersona(Integer.valueOf(form.getIdPersona()));
	        bean.setIdDireccion(form.getIdDireccion());
	        bean.setIdRol(Integer.valueOf(form.getIdRol()));        
	        
	        /*if (parAdm.update(bean)){
	            return exitoModal("messages.updated.success",request);
	        }else{
	        	return exito("messages.updated.error",request);
	        }*/
	        parAdm.update(bean);
		}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null); 
		}
		return exitoModal("messages.updated.success",request);

	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#borrar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
	
		try{
			ExpPartesForm form = (ExpPartesForm)formulario;
		    	    
		    ExpPartesAdm parAdm = new ExpPartesAdm (this.getUserBean(request));
		    
		    Vector vOcultos = form.getDatosTablaOcultos(0);
		    
		    Hashtable hash = new Hashtable();
		    	    
		    hash.put(ExpPartesBean.C_IDINSTITUCION, (String)vOcultos.elementAt(0));
		    hash.put(ExpPartesBean.C_IDINSTITUCION_TIPOEXPEDIENTE, (String)vOcultos.elementAt(1));
		    hash.put(ExpPartesBean.C_IDTIPOEXPEDIENTE, (String)vOcultos.elementAt(2));	    
		    hash.put(ExpPartesBean.C_NUMEROEXPEDIENTE, (String)vOcultos.elementAt(3));
		    hash.put(ExpPartesBean.C_ANIOEXPEDIENTE, (String)vOcultos.elementAt(4));
		    hash.put(ExpPartesBean.C_IDPARTE, (String)vOcultos.elementAt(5));
		    
		    
		    /*if (parAdm.delete(hash)){
	        	return exitoRefresco("messages.deleted.success",request);
	        }else{
	        	return exito("messages.deleted.error",request);
	        }*/
		    parAdm.delete(hash);
		}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null); 
		}
		return exitoRefresco("messages.deleted.success",request);
	
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#nuevo(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException { 	
        
		return mostrarRegistro(mapping, formulario, request, response, true, true);
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#insertar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws SIGAException {
		
	    try{
		    ExpPartesAdm parAdm = new ExpPartesAdm(this.getUserBean(request));
		    ExpPartesForm form = (ExpPartesForm)formulario;
		    HashMap datosExpediente = (HashMap)request.getSession().getAttribute("DATABACKUP");
		    Hashtable hash = (Hashtable)datosExpediente.get("datosGenerales");
		    
		    //Rellenamos el nuevo Bean	    
		    ExpPartesBean parBean = new ExpPartesBean();	    
		    
		    parBean.setIdInstitucion(Integer.valueOf((String)hash.get(ExpPartesBean.C_IDINSTITUCION)));
		    parBean.setIdInstitucion_TipoExpediente(Integer.valueOf((String)hash.get(ExpPartesBean.C_IDINSTITUCION_TIPOEXPEDIENTE)));
		    parBean.setIdTipoExpediente(Integer.valueOf((String)hash.get(ExpPartesBean.C_IDTIPOEXPEDIENTE)));
		    parBean.setNumeroExpediente(Integer.valueOf((String)hash.get(ExpPartesBean.C_NUMEROEXPEDIENTE)));
		    parBean.setAnioExpediente(Integer.valueOf((String)hash.get(ExpPartesBean.C_ANIOEXPEDIENTE)));
		    parBean.setIdParte(parAdm.getNewIdParte(hash));
		    parBean.setIdPersona(Integer.valueOf(form.getIdPersona()));
		    parBean.setIdRol(Integer.valueOf(form.getIdRol()));
		    parBean.setIdDireccion(form.getIdDireccion());
		    
		    //Ahora procedemos a insertarlo
		    /*if (parAdm.insert(parBean))
	        {
	            return exitoModal("messages.inserted.success",request);
	        }else{
	        	return exito("messages.inserted.error",request);
	        }*/
		    parAdm.insert(parBean);
	    }catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null); 
		}
	    return exitoModal("messages.inserted.success",request);
	    
	}

}
