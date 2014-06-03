package com.siga.envios.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesFicheros;
import com.siga.beans.EnvDestinatariosBean;
import com.siga.beans.EnvDocumentosAdm;
import com.siga.beans.EnvDocumentosBean;
import com.siga.beans.EnvEnviosAdm;
import com.siga.beans.EnvEnviosBean;
import com.siga.beans.EnvTipoEnviosAdm;
import com.siga.beans.EnvTipoEnviosBean;
import com.siga.certificados.Plantilla;
import com.siga.envios.form.DocumentosForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gui.processTree.SIGAPTConstants;

public class DocumentosAction extends MasterAction
{
	private static final Integer FILENAME_MAX_LENGTH = 260;
	
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
	            else if (accion.equalsIgnoreCase("descargarEnv"))
	            {
	                mapDestino = descargarEnv(mapping, miForm, request, response);
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
    		UsrBean usrBean = this.getUserBean(request);
    		DocumentosForm form = (DocumentosForm)formulario;
    		String idInstitucion = this.getUserBean(request).getLocation();
            String idEnvio = (String)request.getParameter("idEnvio");
    		Hashtable htPk = new Hashtable();
    		htPk.put(EnvDestinatariosBean.C_IDINSTITUCION,idInstitucion);
    		htPk.put(EnvDestinatariosBean.C_IDENVIO,idEnvio);        

    		//Recupero el bean del envio para mostrar el nombre y el tipo
    		EnvEnviosAdm envioAdm = new EnvEnviosAdm (usrBean);
    		EnvTipoEnviosAdm tipoAdm = new EnvTipoEnviosAdm (usrBean);
    		Vector envio = envioAdm.selectByPK(htPk);        
    		EnvEnviosBean envioBean = (EnvEnviosBean)envio.get(0);


    		/*if(envioBean.getIdTipoEnvios().intValue()==EnvEnviosAdm.TIPO_TELEMATICO ){
    			BusinessManager bm = getBusinessManager();
    			AtosEnviosService enviosService = (AtosEnviosService)bm.getService(AtosEnviosService.class);
    			File pdfIntercambio =  enviosService.getPdfIntercambio(idEnvio, idInstitucion, usrBean);
    			request.setAttribute("rutaFichero", pdfIntercambio.getPath());
				request.setAttribute("nombreFichero", pdfIntercambio.getName());
				request.setAttribute("borrarFichero", "true");

    		}else{
*/

    			EnvDocumentosAdm docAdm = new EnvDocumentosAdm(usrBean);	


    			Vector vOcultos = form.getDatosTablaOcultos(0);
    			String idDocumento = (String)vOcultos.elementAt(2);			
    			String nombreOriginal = (String)vOcultos.elementAt(3);			
    			String pathDocumentosAdjuntos = "";		
    			try {
    				pathDocumentosAdjuntos = envioAdm.getPathEnvio(idInstitucion,idEnvio);
    			} catch (Exception e) {
    				new ClsExceptions (e, "Error al recuperar el envio");
    			}
    			String rutaAlm = pathDocumentosAdjuntos + ClsConstants.FILE_SEP;				

    			fDocumento = docAdm.getFile(idInstitucion,idEnvio,idDocumento);
    			File fdocumento1 = new File(rutaAlm+nombreOriginal);
    			if(fDocumento==null || !fDocumento.exists()){
    				if(!fdocumento1.exists()){
    					throw new SIGAException("messages.general.error.ficheroNoExiste");
    				}else{
    					request.setAttribute("rutaFichero", fdocumento1.getPath());
    					request.setAttribute("nombreFichero", nombreOriginal);
    				}
    			}else{
    				request.setAttribute("rutaFichero", fDocumento.getPath());
    				request.setAttribute("nombreFichero", nombreOriginal);
    			}
//    		}
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
        	
	        /*if(tipoBean.getIdTipoEnvios().intValue()==EnvEnviosAdm.TIPO_TELEMATICO ){
				if(envioBean.getIdEstado().toString().equals(EnvEstadoEnvioAdm.K_ESTADOENVIO_PROCESADO )){
//					BusinessManager bm = getBusinessManager();
//					AtosEnviosService enviosService = (AtosEnviosService)bm.getService(AtosEnviosService.class);
					
					EnvDocumentosBean  documentosBean = new EnvDocumentosBean();
					documentosBean.setIdInstitucion(new Integer(idInstitucion));
					documentosBean.setIdEnvio(new Integer(idEnvio));
					documentosBean.setIdDocumento(new Integer(idEnvio));
					documentosBean.setPathDocumento("");
					documentosBean.setDescripcion("Archivo pdf de Intercambio");
					datos = new Vector();
					datos.add(documentosBean);
					request.setAttribute("datos", datos);
				}
			}else{*/
		        //request.setAttribute("idTipoEnvio", tipoBean.getIdTipoEnvios());
		        
		        //recupero los documentos y las paso a la jsp
		        EnvDocumentosAdm documentosAdm = new EnvDocumentosAdm(this.getUserBean(request));
		        String where = " WHERE " + EnvDocumentosBean.C_IDINSTITUCION + " = " + idInstitucion +
		        			   " AND " + EnvDocumentosBean.C_IDENVIO + " = " + idEnvio;
		        datos = documentosAdm.select(where);
		        request.setAttribute("datos", datos);
//			}
	        
	        
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
	    	DocumentosForm form = (DocumentosForm)formulario;
	    	String sPathDocumento = form.getTheFile().getFileName();
	    	if (sPathDocumento == null || sPathDocumento.length() > FILENAME_MAX_LENGTH){
	    		return this.exito("messages.upload.nameError",request);
	    	}
	    	boolean grabarOK = grabar(mapping, formulario, request, response);
	    	if (!grabarOK)
	    		return exito("messages.upload.error",request);
	    			        
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
	    	String sPathDocumento = form.getTheFile().getFileName();
	    	if (sPathDocumento == null || sPathDocumento.length() > FILENAME_MAX_LENGTH){
	    		return this.exito("messages.upload.nameError",request);
	    	}
	        Hashtable htPk = new Hashtable();
	        htPk.put(EnvDocumentosBean.C_IDINSTITUCION,idInstitucion);
	        htPk.put(EnvDocumentosBean.C_IDENVIO,idEnvio);
	        htPk.put(EnvDocumentosBean.C_IDDOCUMENTO,idDocumento);
	        
	        EnvDocumentosAdm docAdm = new EnvDocumentosAdm(this.getUserBean(request));
	        EnvDocumentosBean docBean = (EnvDocumentosBean)docAdm.selectByPKForUpdate(htPk).firstElement();
	        boolean grabarOK = false;
	    	
	        if (form.getTheFile()!=null && form.getTheFile().getFileSize()>0){       	
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
			
			//Comprobamos que en los correos de tipo ordinario, sólo se adjunten pdf's
		    if(theFile!=null && theFile.getFileSize()>=0) 
			if ((envioBean.getIdTipoEnvios().equals(new Integer(EnvEnviosAdm.TIPO_CORREO_ORDINARIO)) || 
				 envioBean.getIdTipoEnvios().equals(new Integer(EnvEnviosAdm.TIPO_FAX))) && 
				!(theFile.getContentType().equalsIgnoreCase("application/pdf")||
				  theFile.getContentType().equalsIgnoreCase("application/msword"))) {
			    
				throw new SIGAException("messages.envios.error.formatoPDFIncorrecto");
			}
		    
	    	//CR7-INC_10354_SIGA. Comprobamos que el tamaño del archivo no supere cierto tamaño (en principio 5MB)
        	ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
			String maxsize = rp.returnProperty(AppConstants.GEN_PROPERTIES.ficheros_maxsize_MB.getValor());
			int maxSizebytes = Integer.parseInt(maxsize) * 1000 * 1024;
			if(theFile!=null && form.getTheFile().getFileSize() > maxSizebytes){				
				throw new SIGAException("messages.general.file.maxsize",new String[] { (maxsize) });
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
	
	
	/**
	 * Metodo que permite la generación de un zip de los informes 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String descargarEnv(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{
	     File fDocumento = null;
	    try 
		{
	    	UsrBean usr = this.getUserBean(request);
			DocumentosForm form = (DocumentosForm)formulario;
			EnvDocumentosAdm docAdm = new EnvDocumentosAdm(this.getUserBean(request));			 
            EnvDestinatariosBean destBean = null;
            Vector informesRes = new Vector(); 
            File ficheroSalida = null;
            String idInstitucion = (String) usr.getLocation();
			String idEnvio = (String) form.getIdEnvio();			
			Hashtable ht = new Hashtable();
			Hashtable clonDatoscomunes = new Hashtable();
			Hashtable datoscomunes=new Hashtable();
			ht.put(EnvDocumentosBean.C_IDINSTITUCION,idInstitucion);
			ht.put(EnvDocumentosBean.C_IDENVIO,idEnvio);
			Vector vDocs = docAdm.select(ht);	
			EnvEnviosAdm envioAdm = new EnvEnviosAdm(usr);	
			String pathDocumentosAdjuntos = "";
			try {
			   pathDocumentosAdjuntos = envioAdm.getPathEnvio(idInstitucion,idEnvio);
		  	} catch (Exception e) {
		   		new ClsExceptions (e, "Error al recuperar el envio");
		   	}
										
			if (vDocs!=null){				
				if (vDocs.size()==1){
					try{	/*Se verifica que exista el fichero y si existe se saca por pantalla ya que solo es un archivo.*/
						for (int i=0;i<vDocs.size();i++){			
						 EnvDocumentosBean docBean = (EnvDocumentosBean)vDocs.elementAt(i);
						 String idDocumento = (String)docBean.getIdDocumento().toString();							
						 String Descripcion= (String) docBean.getDescripcion();	
						 String Pathdocumento= (String) docBean.getPathDocumento();
						 
						 String nombrePlantilla=idInstitucion+"_"+idEnvio+"_"+idDocumento;
						 String rutaAlm = pathDocumentosAdjuntos + ClsConstants.FILE_SEP;					
						 String direccionPlantilla=rutaAlm+nombrePlantilla;				
						 String direccionPlantilla1=rutaAlm+Descripcion;	
						 File archivo = new 	File(direccionPlantilla);						
						 File f2 = new File(direccionPlantilla1);
						 fDocumento = docAdm.getFile(idInstitucion,idEnvio,idDocumento);
						 File fdocumento1 = new File(rutaAlm+Pathdocumento);
						 if(fDocumento==null || !fDocumento.exists()){
						  if(!fdocumento1.exists()){
								throw new SIGAException("messages.general.error.ficheroNoExiste");
						  }else{
							 request.setAttribute("rutaFichero", fdocumento1.getPath());
							 request.setAttribute("nombreFichero", Descripcion);
						  }
						}else{
						  request.setAttribute("rutaFichero", fDocumento.getPath());
						  request.setAttribute("nombreFichero", Descripcion);
						}
								
						}
					 } catch (Exception e) { 
					       throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
					}
				}else{/** se recorre los datos ya que hay mas de un fichero y se van sacando una a uno para guardarlos en un vector
				 		   y posteriormente se leera dicho vector para guardarlo en un zip.
				 	  **/		
						for (int i=0;i<vDocs.size();i++){			
						EnvDocumentosBean docBean = (EnvDocumentosBean)vDocs.elementAt(i);		
					 	String Descripcion= (String)docBean.getDescripcion();
						String idDocumento = (String) docBean.getIdDocumento().toString();
						String Pathdocumento= (String) docBean.getPathDocumento();
						String nombrePlantilla=idInstitucion+"_"+idEnvio+"_"+idDocumento;
						String rutaAlm = pathDocumentosAdjuntos + ClsConstants.FILE_SEP;					
						String direccionPlantilla=rutaAlm+nombrePlantilla;		
						String direccionPlantilla1=rutaAlm+ Pathdocumento;	
						File archivo = new 	File(rutaAlm+nombrePlantilla);						
						File f2 = new File(direccionPlantilla1);
					    fDocumento = docAdm.getFile(idInstitucion,idEnvio,idDocumento);					
					    if(fDocumento==null || !fDocumento.exists()){					    	
					    	  if(!f2.exists()){
					 			throw new SIGAException("messages.general.error.ficheroNoExiste");
					    	  }
						}									   
						//Renombramos el archivo con el nombre que tiene el f2
						UtilidadesFicheros.copyFile(archivo, f2);
						
						/**Se guarda en el vector de informeRes los datos de los diferentes archivos**/
						informesRes.add(f2);
					}
					}//FIN DEL ELSE
				
						/**Aqui recorremos un vector y guardamos los datos del fichero para posteriormente realizar un zip.
					   		**/	
					if (informesRes.size()!=0) {					
					  try{							    	
							ArrayList ficherosPDF= new ArrayList();
							for (int i=0;i<informesRes.size();i++){								
								 File f = (File) informesRes.get(i);							 
								 ficherosPDF.add(f);	
							}
							
							String nombreFicheroZIP= idInstitucion+"_"+ idEnvio +"_" +UtilidadesBDAdm.getFechaCompletaBD("").replaceAll("/","").replaceAll(":","").replaceAll(" ","");
							String rutaServidorDescargasZip = pathDocumentosAdjuntos;
							rutaServidorDescargasZip += ClsConstants.FILE_SEP+idInstitucion+ClsConstants.FILE_SEP+"temp"+ File.separator;
							File ruta = new File(rutaServidorDescargasZip);
							ruta.mkdirs();
							Plantilla.doZip(rutaServidorDescargasZip,nombreFicheroZIP,ficherosPDF, false);				
							ficheroSalida = new File(rutaServidorDescargasZip + nombreFicheroZIP + ".zip");				
							
							//Borramos los ficheros clonados
							for (int i=0;i<informesRes.size();i++){								
								File f = (File) informesRes.get(i);							 
								f.delete();
							}
							
							request.setAttribute("nombreFichero", ficheroSalida.getName());
							request.setAttribute("rutaFichero", ficheroSalida.getPath());
							request.setAttribute("borrarFichero", "true");
							
						  } catch (Exception e) { 
								throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
						     }  
						}
					}
		} 		
		catch (Exception e) { 
            throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
		}
				
		return "descargaFichero";  
	}
	
}