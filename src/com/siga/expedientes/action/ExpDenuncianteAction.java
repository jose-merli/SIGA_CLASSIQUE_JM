/*
 * Created on Jan 18, 2005
 * @author emilio.grau
 *
 */
package com.siga.expedientes.action;

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
import com.siga.beans.ExpDenuncianteAdm;
import com.siga.beans.ExpDenuncianteBean;
import com.siga.beans.ExpExpedienteAdm;
import com.siga.beans.ExpExpedienteBean;
import com.siga.expedientes.form.ExpDenuncianteForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 * Action Denunciantes de un expediente
 */
public class ExpDenuncianteAction extends MasterAction {

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrir(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
    protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{	
        try{
	        ExpDenuncianteForm form = (ExpDenuncianteForm)formulario;
	        ExpDenuncianteAdm denuncianteAdm = new ExpDenuncianteAdm (this.getUserBean(request));
	               
	        //Datos generales para todas las pestanhas
			String idInstitucion = request.getParameter("idInstitucion");
			String idInstitucion_TipoExpediente = request.getParameter("idInstitucion_TipoExpediente");
			String idTipoExpediente = request.getParameter("idTipoExpediente");
			String numExpediente = request.getParameter("numeroExpediente");
			String anioExpediente = request.getParameter("anioExpediente");
			
			Hashtable hash = new Hashtable();		
			hash.put(ExpExpedienteBean.C_IDINSTITUCION,idInstitucion);
			hash.put(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE,idInstitucion_TipoExpediente);
			hash.put(ExpExpedienteBean.C_IDTIPOEXPEDIENTE,idTipoExpediente);
			hash.put(ExpExpedienteBean.C_NUMEROEXPEDIENTE,numExpediente);
			hash.put(ExpExpedienteBean.C_ANIOEXPEDIENTE,anioExpediente);
	        Vector datos = denuncianteAdm.select(hash);

	        Vector datosPersonas = new Vector();
	        CenPersonaAdm personaAdm = new CenPersonaAdm (this.getUserBean(request));
            CenClienteAdm clienteAdm = new CenClienteAdm (this.getUserBean(request));
	        for (int i = 0; i < datos.size(); i++) {
	            ExpDenuncianteBean denuncinante = (ExpDenuncianteBean)datos.get(i);
	            datosPersonas.add(personaAdm.getIdentificador(denuncinante.getIdPersona()));
	            
	            String telefono = " ";
	            Hashtable h = clienteAdm.getDirecciones(denuncinante.getIdPersona(),denuncinante.getIdInstitucion(), denuncinante.getIdDireccion());
	            if (h != null){
		            telefono = (String)h.get(CenDireccionesBean.C_TELEFONO1);
		            if (telefono == null || telefono.equals("")) telefono = (String)h.get(CenDireccionesBean.C_MOVIL);
		            if (telefono == null) telefono = new String(" ");
	            }
	            datosPersonas.add(telefono);
	        }
	        
	        request.setAttribute("datos", datos);
	        request.setAttribute("datosPersonas", datosPersonas);
	           
	        //Recuperamos el nombre del denunciado
	        ExpExpedienteAdm expAdm = new ExpExpedienteAdm(this.getUserBean(request));
	        Vector vExp = expAdm.selectByPK(hash);        
	        Hashtable hashIdPers = new Hashtable();		
			hashIdPers.put(CenPersonaBean.C_IDPERSONA,((ExpExpedienteBean)vExp.elementAt(0)).getIdPersona());
	        Vector vPersona = personaAdm.selectByPK(hashIdPers);
	        CenPersonaBean personaBean = (CenPersonaBean) vPersona.elementAt(0);
	        String nombrePersona = personaBean.getNombre() + " " + personaBean.getApellido1() + " " + personaBean.getApellido2();
	        request.setAttribute("denunciado", nombrePersona);
	        
	        String denunciado="";
			//Chapuza para identificar si se llama denunciante o impugnante
			StringBuffer where= new StringBuffer("where ");
			where.append(ExpCampoTipoExpedienteBean.C_IDINSTITUCION).append("=").append(idInstitucion)
				 .append(" and ").append(ExpCampoTipoExpedienteBean.C_IDTIPOEXPEDIENTE).append("=").append(idTipoExpediente)
				 .append(" and ").append(ExpCampoTipoExpedienteBean.C_IDCAMPO).append("=").append(ClsConstants.IDCAMPO_TIPOEXPEDIENTE_DENUNCIANTE)
				 .append(" and ").append(ExpCampoTipoExpedienteBean.C_NOMBRE).append(" like ").append("'%impugnante%'");
			ExpCampoTipoExpedienteAdm campoTipoExpedienteAdm = new ExpCampoTipoExpedienteAdm (this.getUserBean(request));
			Vector resultado=campoTipoExpedienteAdm.select(where.toString());
	        if (resultado!=null && resultado.size()>0) {
	        	request.setAttribute("tituloPagina", UtilidadesString.getMensajeIdioma(this.getUserBean(request), "expedientes.auditoria.impugnantes.cabecera"));
	        	denunciado=UtilidadesString.getMensajeIdioma(this.getLenguaje(request), "expedientes.auditoria.literal.impugnado");
        	} else {
	        	request.setAttribute("tituloPagina", UtilidadesString.getMensajeIdioma(this.getUserBean(request), "expedientes.auditoria.denunciantes.cabecera"));
	        	denunciado=UtilidadesString.getMensajeIdioma(this.getLenguaje(request), "expedientes.auditoria.literal.denunciado");
	        }

	        String tituloVentana = denunciado + 
			  ": " + nombrePersona + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
			  UtilidadesString.getMensajeIdioma(this.getLenguaje(request), "expedientes.auditoria.literal.tipo") +
			  ": " + (String)request.getParameter("nombreTipoExpediente") + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
			  UtilidadesString.getMensajeIdioma(this.getLenguaje(request), "expedientes.auditoria.literal.nexpediente") +
			  ": " + (String)request.getParameter("numeroExpediente") + " / " + (String)request.getParameter("anioExpediente");

	        form.setTituloVentana(tituloVentana);

	        //request.getSession().removeAttribute("DATABACKUP_BEAN");
			//request.getSession().removeAttribute("DATABACKUP");
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
	

	/** 
	 * Funcion que muestra el formulario en modo consulta o edicion
	 * @param  mapping
	 * @param  formulario
	 * @param  request
	 * @param  response
	 * @param  bEditable
	 * @exception  ClsExceptions  En cualquier caso de error
	 * @return String para el forward
	 */
	protected String mostrarRegistro(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response, boolean bEditable) throws SIGAException
	{
		try{
		    ExpDenuncianteForm form = (ExpDenuncianteForm)formulario;
	        ExpDenuncianteAdm denuncianteAdm = new ExpDenuncianteAdm (this.getUserBean(request));
			HttpSession ses = request.getSession();
			
	        Vector vOcultos = form.getDatosTablaOcultos(0);		
	
			String idInstitucion = (String)vOcultos.elementAt(0);
			String idInstitucion_TipoExpediente = (String)vOcultos.elementAt(1);
			String idTipoExpediente = (String)vOcultos.elementAt(2);
			String numExpediente = (String)vOcultos.elementAt(3);
			String anioExpediente = (String)vOcultos.elementAt(4);
	        String idDenunciante = (String)vOcultos.elementAt(5);
	        
	      //obtenemos datos del colegiado
	        Hashtable hashCol = new Hashtable();
	        CenColegiadoAdm colAdm = new CenColegiadoAdm (this.getUserBean(request));
	        hashCol.put(CenColegiadoBean.C_IDINSTITUCION,idInstitucion);        
	        hashCol.put(CenColegiadoBean.C_IDPERSONA,idDenunciante);
	        Vector datosCol = colAdm.select(hashCol);
	        if(datosCol!=null && datosCol.size()>0){
	        	CenColegiadoBean cBean = (CenColegiadoBean)datosCol.elementAt(0);
	        	form.setNumColegiado(cBean.getNColegiado());
	        }
	        else 
	        	form.setNumColegiado("");
	        
	        
	        
			Hashtable hash = new Hashtable();		
			hash.put(ExpDenuncianteBean.C_IDINSTITUCION,idInstitucion);
			hash.put(ExpDenuncianteBean.C_IDINSTITUCION_TIPOEXPEDIENTE,idInstitucion_TipoExpediente);
			hash.put(ExpDenuncianteBean.C_IDTIPOEXPEDIENTE,idTipoExpediente);
			hash.put(ExpDenuncianteBean.C_NUMEROEXPEDIENTE,numExpediente);
			hash.put(ExpDenuncianteBean.C_ANIOEXPEDIENTE,anioExpediente);
			hash.put(ExpDenuncianteBean.C_IDDENUNCIANTE,idDenunciante);
			
	        Vector datos = denuncianteAdm.select(hash);
	        ExpDenuncianteBean bean = (ExpDenuncianteBean)datos.elementAt(0);
	        
	        CenPersonaAdm personaAdm = new CenPersonaAdm (this.getUserBean(request));
            CenClienteAdm clienteAdm = new CenClienteAdm (this.getUserBean(request));

	        CenPersonaBean persona = personaAdm.getIdentificador(bean.getIdPersona());
	        form.setIdPersona(bean.getIdPersona().toString());
	        form.setNombre(persona.getNombre());
	        form.setNif(persona.getNIFCIF());                
	        form.setPrimerApellido(persona.getApellido1());
	        form.setSegundoApellido(persona.getApellido2());
	        if(bean.getIdDireccion()!=null ){
		        Hashtable direccion = clienteAdm.getDirecciones(bean.getIdPersona(),bean.getIdInstitucion(), bean.getIdDireccion());
		        // hacemos los set del formulario       
		        
		        form.setDireccion(UtilidadesHash.getString(direccion, CenDireccionesBean.C_DOMICILIO));
		        form.setPoblacion(UtilidadesHash.getString(direccion, "POBLACION"));
		        form.setPoblacionExt(UtilidadesHash.getString(direccion, CenDireccionesBean.C_POBLACIONEXTRANJERA));
		        form.setProvincia(UtilidadesHash.getString(direccion, "PROVINCIA"));
		        form.setPais(UtilidadesHash.getString(direccion, "PAIS"));
		        form.setCpostal(UtilidadesHash.getString(direccion, CenDireccionesBean.C_CODIGOPOSTAL));
		        
		        String telefono = UtilidadesHash.getString(direccion, CenDireccionesBean.C_TELEFONO1);
		        if (telefono == null || telefono.equals("")) telefono = UtilidadesHash.getString(direccion, CenDireccionesBean.C_MOVIL);
		 
		        form.setTelefono(telefono);
		        form.setIdDireccion(bean.getIdDireccion().toString());
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
	        
        	ses.setAttribute("DATABACKUP_BEAN",bean); // hay que etiquetarlo como DATABACKUP_BEAN pq sino se borra con las ventanas modales que se abren (buscar cliente, buscar direccion)

        	if (bEditable){
	        	request.setAttribute("accion","edicion");
	        }
	        else{
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
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		try{
		    // Recuperamos los datos anteriores de la sesión
		    HttpSession ses=request.getSession();
//		    HashMap datosExpediente = (HashMap)ses.getAttribute("DATABACKUP");
//		    ExpDenuncianteBean bean=(ExpDenuncianteBean)datosExpediente.get("datosParticulares");	
		    
		    ExpDenuncianteBean bean = (ExpDenuncianteBean)ses.getAttribute("DATABACKUP_BEAN");
		    ses.removeAttribute("DATABACKUP_BEAN");
		    
		    ExpDenuncianteForm form = (ExpDenuncianteForm)formulario;
	        ExpDenuncianteAdm denAdm = new ExpDenuncianteAdm (this.getUserBean(request));

	        // Actualizamos la direccion
	        if (form.getIdDireccion() != null && !form.getIdDireccion().equals(""))
	            bean.setIdDireccion(Long.valueOf(form.getIdDireccion()));

	        denAdm.update(bean);
	        
		}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null);
		}
		return exitoModal("messages.updated.success",request);
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#borrar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		try{
			ExpDenuncianteForm form = (ExpDenuncianteForm)formulario;
		    ExpDenuncianteAdm expAdm = new ExpDenuncianteAdm (this.getUserBean(request));
		    
		    Vector vOcultos = form.getDatosTablaOcultos(0);
		    Hashtable hash = new Hashtable();
		    hash.put(ExpDenuncianteBean.C_IDINSTITUCION, (String)vOcultos.elementAt(0));
		    hash.put(ExpDenuncianteBean.C_IDINSTITUCION_TIPOEXPEDIENTE, (String)vOcultos.elementAt(1));
		    hash.put(ExpDenuncianteBean.C_IDTIPOEXPEDIENTE, (String)vOcultos.elementAt(2));	    
		    hash.put(ExpDenuncianteBean.C_NUMEROEXPEDIENTE, (String)vOcultos.elementAt(3));
		    hash.put(ExpDenuncianteBean.C_ANIOEXPEDIENTE, (String)vOcultos.elementAt(4));
		    hash.put(ExpDenuncianteBean.C_IDDENUNCIANTE, (String)vOcultos.elementAt(5));    
		    expAdm.delete(hash);
		}
		catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null); 
		}
		return exitoRefresco("messages.deleted.success",request);
	
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#nuevo(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{ 	
        ExpDenuncianteForm form = (ExpDenuncianteForm)formulario;
        request.setAttribute("anioExpediente",form.getAnioExpediente());
        request.setAttribute("idInstitucion",form.getIdInstitucion());
        request.setAttribute("idInstitucion_TipoExpediente",form.getIdInstitucion_TipoExpediente());
        request.setAttribute("numExpediente",form.getNumExpediente());
        request.setAttribute("idTipoExpediente",form.getIdTipoExpediente());
        request.setAttribute("accion","nuevo");
		return "mostrar";
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#insertar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario,	HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
	    try {
		    ExpDenuncianteForm form = (ExpDenuncianteForm)formulario;
		    
		    // Rellenamos el nuevo Bean	    
		    ExpDenuncianteBean denBean = new ExpDenuncianteBean();	    
		    ExpDenuncianteAdm  denAdm  = new ExpDenuncianteAdm(this.getUserBean(request));
		    denBean.setIdInstitucion(Integer.valueOf(form.getIdInstitucion()));
		    denBean.setIdInstitucion_TipoExpediente(Integer.valueOf(form.getIdInstitucion_TipoExpediente()));
		    denBean.setIdTipoExpediente(Integer.valueOf(form.getIdTipoExpediente()));
		    denBean.setNumeroExpediente(Integer.valueOf(form.getNumExpediente()));
		    denBean.setAnioExpediente(Integer.valueOf(form.getAnioExpediente()));
		    denBean.setIdDenunciante(denAdm.getNewIdDenunciante(denBean));
		    denBean.setIdPersona(Long.valueOf(form.getIdPersona()));
		    
		    // El campo direccion no es obligatorio
		    if(!(form.getIdDireccion() == null || form.getIdDireccion().equals(""))){
		        denBean.setIdDireccion(Long.valueOf(form.getIdDireccion()));    
		    }
		    		    
		    if(!denAdm.insert(denBean)) {
			    return this.exitoModalSinRefresco("messages.inserted.error",request);
		    }
	    }
	    catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.expediente"},e,null); 
		}
	    return exitoModal("messages.inserted.success",request);
	}
}
