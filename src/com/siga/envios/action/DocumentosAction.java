package com.siga.envios.action;

import java.io.*;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.beans.*;
import com.siga.envios.form.DocumentosForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gui.processTree.SIGAPTConstants;

public class DocumentosAction extends MasterAction
{
    public ActionForward executeInternal (ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
    	String mapDestino = "exception";
    	MasterForm miForm = null;

    	try 
    	{ 
	        miForm = (MasterForm) formulario;

	        if (miForm != null) 
	        {
	            String accion = miForm.getModo();

	            if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir"))
	            {
	                mapDestino = abrir(mapping, miForm, request, response);
	            } 

	            else if (accion.equalsIgnoreCase("download"))
	            {
	                mapDestino = download(mapping, miForm, request, response);
	            } 

	            else 
	            {
	                return super.executeInternal(mapping,formulario,request,response);
	            }
	        }

    	    // Redireccionamos el flujo a la JSP correspondiente
    	    if (mapDestino == null)	
    	    { 
    	        throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
    	    }

    	    return mapping.findForward(mapDestino);  

    	}     	
    	catch (SIGAException e) 
    	{ 
    	    throw e;
    	}
    	catch (Exception e) 
    	{ 
    	    throw new SIGAException("messages.general.error",e,new String[] {"modulo.envios"});
    	} 
	}

	protected String download(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{
	    File fDocumento = null;
	    try 
		{
			DocumentosForm form = (DocumentosForm)formulario;
			EnvDocumentosAdm docAdm = new EnvDocumentosAdm(this.getUserBean(request));
			
			Vector vOcultos = form.getDatosTablaOcultos(0);

			String idInstitucion = (String)vOcultos.elementAt(0);
			String idEnvio = (String)vOcultos.elementAt(1);
			String idDocumento = (String)vOcultos.elementAt(2);			
			String nombreOriginal = (String)vOcultos.elementAt(3);			
			
			fDocumento = docAdm.getFile(idInstitucion,idEnvio,idDocumento);
			if(fDocumento==null || !fDocumento.exists()){
					throw new SIGAException("messages.general.error.ficheroNoExiste"); 
			}
			request.setAttribute("rutaFichero", fDocumento.getPath());
			request.setAttribute("nombreFichero", nombreOriginal);
			
		} 		
		catch (Exception e) { 
            throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
		}
				
		return "descargaFichero";
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
        request.setAttribute("idEnvio",idEnvio);
        
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
	        //request.setAttribute("idTipoEnvio", tipoBean.getIdTipoEnvios());
	        
	        //recupero los documentos y las paso a la jsp
	        EnvDocumentosAdm documentosAdm = new EnvDocumentosAdm(this.getUserBean(request));
	        String where = " WHERE " + EnvDocumentosBean.C_IDINSTITUCION + " = " + idInstitucion +
	        			   " AND " + EnvDocumentosBean.C_IDENVIO + " = " + idEnvio;
	        datos = documentosAdm.select(where);
	        request.setAttribute("datos", datos); 
	        
        } catch (Exception e) {
            throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
        }
        
		return "inicio";	    
	}
	
	

	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions 
	{
	    DocumentosForm form = (DocumentosForm)formulario;
	    
	    Vector vOcultos = form.getDatosTablaOcultos(0);
	    Vector vVisibles = form.getDatosTablaVisibles(0);
	    
	    String idInstitucion = (String)vOcultos.elementAt(0);
	    String idEnvio = (String)vOcultos.elementAt(1);
	    String idDocumento = (String)vOcultos.elementAt(2);
	    String descripcion = (String)vVisibles.elementAt(0);
       	
	    form.setModo("modificar");
	    form.setIdInstitucion(idInstitucion);
        form.setIdEnvio(idEnvio);
        form.setIdDocumento(idDocumento);
        form.setDescripcion(descripcion);
        
	    return "mostrar";
	}

	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions 
	{
	    DocumentosForm form = (DocumentosForm)formulario;
	    UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        
        String idInstitucion = userBean.getLocation();
        String idEnvio = form.getIdEnvio();
	    
	    EnvDocumentosAdm documentosAdm = new EnvDocumentosAdm(this.getUserBean(request));
	    String idDocumento = String.valueOf(documentosAdm.getNewIdDocumento(idInstitucion,idEnvio));
	    
	    form.setModo("insertar");
	    form.setIdInstitucion(idInstitucion);
        form.setIdEnvio(idEnvio);
        form.setIdDocumento(idDocumento);        
	    form.setDescripcion("");
        
	    return "mostrar";
	}
	
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException
	{
	    try {
	    	boolean grabarOK = grabar(mapping, formulario, request, response);
	    	if (!grabarOK)
	    		return exito("messages.upload.error",request);
	    		
	        DocumentosForm form = (DocumentosForm)formulario;
	        EnvDocumentosBean docBean = new EnvDocumentosBean();
	        docBean.setIdInstitucion(Integer.valueOf(form.getIdInstitucion()));
	        docBean.setIdEnvio(Integer.valueOf(form.getIdEnvio()));
	        docBean.setIdDocumento(Integer.valueOf(form.getIdDocumento()));
	        docBean.setPathDocumento(form.getTheFile().getFileName());
	        docBean.setDescripcion(form.getDescripcion()); 
	        EnvDocumentosAdm docAdm = new EnvDocumentosAdm(this.getUserBean(request));
	        docAdm.insert(docBean);
	    }
	    catch (SIGAException e){
            throw e;
	    }
	    catch (Exception e){
            throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
        }    
	    return exitoModal("messages.inserted.success",request);
	}

	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException 
	{
	    DocumentosForm form = (DocumentosForm)formulario;
        String idInstitucion = form.getIdInstitucion();
        String idEnvio = form.getIdEnvio();
        String idDocumento = form.getIdDocumento();
        
	    try{    
	        Hashtable htPk = new Hashtable();
	        htPk.put(EnvDocumentosBean.C_IDINSTITUCION,idInstitucion);
	        htPk.put(EnvDocumentosBean.C_IDENVIO,idEnvio);
	        htPk.put(EnvDocumentosBean.C_IDDOCUMENTO,idDocumento);
	        
	        EnvDocumentosAdm docAdm = new EnvDocumentosAdm(this.getUserBean(request));
	        EnvDocumentosBean docBean = (EnvDocumentosBean)docAdm.selectByPKForUpdate(htPk).firstElement();
	        boolean grabarOK = false;
	        if (form.getTheFile().getFileSize()>0){
	            docBean.setPathDocumento(form.getTheFile().getFileName());
	            grabarOK = grabar(mapping, formulario, request, response);
	        }	        
	        docBean.setDescripcion(form.getDescripcion()); 
	        
	        if (grabarOK)
	        	docAdm.update(docBean);
	        else 
	        	return exito("messages.updated.error", request);					
	    } catch (Exception exc) {
	        throwExcp("messages.general.error",new String[] {"modulo.envios"},exc,null);
	    }
	    return exitoModal("messages.updated.success",request);
	}

	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) 
		throws ClsExceptions, SIGAException
	{
	    DocumentosForm form = (DocumentosForm)formulario;	        
        Vector vOcultos = form.getDatosTablaOcultos(0);	    
	    String idInstitucion = (String)vOcultos.elementAt(0);
	    String idEnvio = (String)vOcultos.elementAt(1);
	    String idDocumento = (String)vOcultos.elementAt(2);	    
	           
	    EnvDocumentosAdm documentosAdm = new EnvDocumentosAdm(this.getUserBean(request));
	    EnvDocumentosBean docBean = null;
	    try {		    
	        Hashtable hash = new Hashtable();		    
			hash.put(EnvDocumentosBean.C_IDINSTITUCION, idInstitucion);
			hash.put(EnvDocumentosBean.C_IDENVIO, idEnvio);	
			hash.put(EnvDocumentosBean.C_IDDOCUMENTO,idDocumento);
			//Por si hay que restaurar los datos al fallar el borrado del fichero
			docBean = (EnvDocumentosBean)documentosAdm.select(hash).firstElement();
			documentosAdm.delete(hash);	        
		} catch (Exception e) {
		    throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
		}
		
        File fDocumento = documentosAdm.getFile(idInstitucion, idEnvio, idDocumento);
        if (fDocumento==null || !fDocumento.exists()){
            return exitoRefresco("messages.deleted.success",request);
        } else {
	        if (fDocumento.delete()){
	            return exitoRefresco("messages.deleted.success",request);
	        } else {
	            documentosAdm.insert(docBean);
	            throw new SIGAException("messages.general.error");
	        }
        }
	}

	protected String mostrarRegistro(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response, boolean bNuevo) throws ClsExceptions
	{
	    DocumentosForm form = (DocumentosForm)formulario;
	    
	    Vector vOcultos = form.getDatosTablaOcultos(0);
	    Vector vVisibles = form.getDatosTablaVisibles(0);
	    
	    String idInstitucion = (String)vOcultos.elementAt(0);
	    String idEnvio = (String)vOcultos.elementAt(1);
	    String idDocumento = (String)vOcultos.elementAt(2);
	    String descripcion = (String)vVisibles.elementAt(0);
       	
        form.setIdInstitucion(idInstitucion);
        form.setIdEnvio(idEnvio);
        
        if (bNuevo){
            form.setModo("insertar");
            form.setDescripcion("");
        } else {
            form.setModo("modificar");            
	        form.setIdDocumento(idDocumento);
	        form.setDescripcion(descripcion);
	    }
	    
		return "mostrar";
	}
	
	protected boolean grabar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException
	{
	    boolean resultado=false;
	    try {
		    DocumentosForm form = (DocumentosForm)formulario;
		    		
		    FormFile theFile = form.getTheFile();

		    String sNombreFichero="";
			
			String idInstitucion = form.getIdInstitucion();
			String idEnvio = form.getIdEnvio();
			
			//Recuperamos los datos del envio
	        Hashtable htPk = new Hashtable();
	        htPk.put(EnvDocumentosBean.C_IDINSTITUCION,idInstitucion);
	        htPk.put(EnvDocumentosBean.C_IDENVIO,idEnvio);        
	        
	        EnvEnviosAdm envioAdm = new EnvEnviosAdm(this.getUserBean(request));
			EnvEnviosBean envioBean = (EnvEnviosBean) envioAdm.selectByPK(htPk).firstElement();
			
			//Comprobamos que en los correos de tipo ordinario, s�lo se adjunten pdf's
		    if(theFile!=null && theFile.getFileSize()>=0) 
			if ((envioBean.getIdTipoEnvios().equals(new Integer(EnvEnviosAdm.TIPO_CORREO_ORDINARIO)) || 
				 envioBean.getIdTipoEnvios().equals(new Integer(EnvEnviosAdm.TIPO_FAX))) && 
				!(theFile.getContentType().equalsIgnoreCase("application/pdf")||
				  theFile.getContentType().equalsIgnoreCase("application/msword"))) {
			    
				throw new SIGAException("messages.envios.error.formatoPDFIncorrecto");
			}
			
	        EnvDocumentosAdm docAdm = new EnvDocumentosAdm(this.getUserBean(request));
		    if(theFile.getFileSize()>0) 
		    {
		    	InputStream stream =null;
		    
		    	String sNombreDocumento = form.getIdInstitucion() + "_" + form.getIdEnvio()+ "_" + form.getIdDocumento();
		    	
		    	String sDirectorio = envioAdm.getPathEnvio(idInstitucion,idEnvio);
		    	File fDirectorio = new File(sDirectorio);
		    	fDirectorio.mkdirs();
		    	
		    	OutputStream bos = null;
		    	try 
		    	{			
		    		stream = theFile.getInputStream();
		    		sNombreFichero=sDirectorio + File.separator + sNombreDocumento;
		    		
		    		bos = new FileOutputStream(sNombreFichero);
		    		int bytesRead = 0;
		    		final int BUFFER = 8192;//8KB
		    		byte[] buffer = new byte[BUFFER];
		    		
		    		/*
		            Calendar cal = Calendar.getInstance();
		            Date hora = cal.getTime();
		            DateFormat df = DateFormat.getTimeInstance();
		            String horaActual = df.format(hora);
		            System.out.println("--> HORA="+horaActual);
		            */		    		
		    		
		    		while ((bytesRead = stream.read(buffer, 0, BUFFER)) != -1) {
		    			bos.write(buffer, 0, bytesRead);
		    		}
		            
		    		stream.close();
		    		bos.close();
		    		resultado=true;
		    	} 
		    	catch (IOException e){
		    		if (bos   != null) bos.close();
		    		if (stream!= null) stream.close();
		    		resultado=false;
		    	} 
		    	catch (Exception e){
			        resultado=false;		    	    
		    	}
		    } 
		    else {
		        resultado=false;
		    }	        
	    } 
	    catch(SIGAException e){
	        throw e;
	    
	    } 
	    catch(Exception e){
			throwExcp("messages.upload.error", new String[] {"modulo.envios"}, e, null); 
	    }	    
	    
	    return resultado;
	}	
	
}