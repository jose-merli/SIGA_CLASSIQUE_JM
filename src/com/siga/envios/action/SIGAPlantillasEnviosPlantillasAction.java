package com.siga.envios.action;

import java.io.*;
import java.util.*;

import com.atos.utils.*;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.*;
import com.siga.general.*;

import javax.servlet.http.*;
import javax.transaction.UserTransaction;

import com.siga.envios.form.*;

import org.apache.struts.action.*;
import org.apache.struts.upload.*;

public class SIGAPlantillasEnviosPlantillasAction extends MasterAction
{
    private String sContentTypeZIP="application/x-zip-compressed";
    
    
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
	            }  else if (accion.equalsIgnoreCase("descargar"))
	            {
	                mapDestino = descargar(mapping, miForm, request, response);
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
    	
    	catch (SIGAException es) 
    	{ 
    	    throw es; 
    	} 
    	
    	catch (Exception e) 
    	{ 
    	    throw new SIGAException("messages.general.error",e,new String[] {"modulo.certificados"});
    	} 
	}

	protected String download(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException
	{
	    File fPlantilla = null;
	    
		try 
		{
			SIGAPlantillasEnviosPlantillasForm form = (SIGAPlantillasEnviosPlantillasForm)formulario;

			Vector vOcultos = form.getDatosTablaOcultos(0);

			String idInstitucion = (String)vOcultos.elementAt(0);
			String idTipoEnvios = (String)vOcultos.elementAt(1);
			String idPlantillaEnvios = (String)vOcultos.elementAt(2);
			String idPlantilla = (String)vOcultos.elementAt(3);
			String idTipoArchivo = (String)vOcultos.elementAt(6);
			
			//String sNombrePlantilla = idTipoEnvios + "_" + idPlantillaEnvios + "_" + idPlantilla + ".zip";
	    	//String sRutaPlantilla = getPathPlantillasFromDB() + File.separator + form.getIdInstitucion();
			//String sNombreFichero=sRutaPlantilla + File.separator + sNombrePlantilla;
			
			EnvPlantillaGeneracionAdm admPlantilla = new EnvPlantillaGeneracionAdm(this.getUserBean(request));
			//fPlantilla = admPlantilla.descargarPlantilla(idInstitucion, idTipoProducto, idProducto, idProductoInstitucion, idPlantilla);
			fPlantilla = admPlantilla.descargarPlantilla(idInstitucion, idTipoEnvios, idPlantillaEnvios, idPlantilla,idTipoArchivo);
			
			if(fPlantilla==null || !fPlantilla.exists()){
				throw new SIGAException("messages.general.error.ficheroNoExiste"); 
			}
			String nombreFichero = fPlantilla.getName();
			if (fPlantilla.getName().indexOf(".zip")==-1){
			    nombreFichero = nombreFichero + "."+idTipoArchivo;
			}
			request.setAttribute("nombreFichero", nombreFichero);
			request.setAttribute("rutaFichero", fPlantilla.getPath());
			//request.setAttribute("generacionOK","OK");
		} 
		catch (SIGAException e) 
		{ 
		  	throw e; 
		}
		catch (Exception e) 
		{ 
		  	throwExcp("messages.certificados.error.descargarplantilla", null, null); 
		}
		
		
		return "descargaFichero";
	}
	
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{
	    SIGAPlantillasEnviosPlantillasForm form = (SIGAPlantillasEnviosPlantillasForm)formulario;
	    EnvPlantillaGeneracionAdm admPlantilla = new EnvPlantillaGeneracionAdm(this.getUserBean(request));

	    String idInstitucion = form.getIdInstitucion();
	    String idTipoEnvio = form.getIdTipoEnvio();
	    String idPlantillaEnvios = form.getIdPlantillaEnvios();
	    
	    String sEditable = form.getEditable();
	    
	    //	  Obtenemos el nombre de plantilla por si se ha modificado
	    EnvPlantillasEnviosAdm plantAdm = new EnvPlantillasEnviosAdm(this.getUserBean(request));
	    Hashtable htPk = new Hashtable();
	    htPk.put(EnvCamposPlantillaBean.C_IDINSTITUCION,idInstitucion);
	    htPk.put(EnvCamposPlantillaBean.C_IDTIPOENVIOS,idTipoEnvio);
	    htPk.put(EnvCamposPlantillaBean.C_IDPLANTILLAENVIOS,idPlantillaEnvios);
	    Vector vPlant = plantAdm.selectByPK(htPk);	    
	    EnvPlantillasEnviosBean plantBean = (EnvPlantillasEnviosBean)vPlant.firstElement();
	    String plantilla = plantBean.getNombre();
	    
	    String descPlantilla = form.getDescripcionPlantilla();
	    String idTipoEnvios = form.getIdTipoEnvios();
	    
	    Vector vDatos = admPlantilla.obtenerListaPlantillas(idInstitucion, idTipoEnvio, idPlantillaEnvios);
	    
	    request.setAttribute("idInstitucion", idInstitucion);
	    request.setAttribute("idTipoEnvio", idTipoEnvio);
	    request.setAttribute("idPlantillaEnvios", idPlantillaEnvios);
	    
	    request.setAttribute("editable", sEditable);
	    
	    request.setAttribute("plantilla", plantilla);
	    
	    request.setAttribute("descripcionPlantilla", descPlantilla);
	    request.setAttribute("idTipoEnvios", idTipoEnvios);
	    
	    request.setAttribute("datos", vDatos);
	    
		return "abrir";
	}

	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions 
	{
	    return mostrarRegistro(mapping, formulario, request, response, true, false);
	}

	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions 
	{
	    return mostrarRegistro(mapping, formulario, request, response, false, false);
	}

	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions 
	{
	    return mostrarRegistro(mapping, formulario, request, response, true, true);
	}
	
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException
	{
	    return grabar(mapping, formulario, request, response);
	}

	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException 
	{
	    return grabar(mapping, formulario, request, response);
	}

	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException
	{
	    SIGAPlantillasEnviosPlantillasForm form = (SIGAPlantillasEnviosPlantillasForm)formulario;
        
        EnvPlantillaGeneracionAdm admPlantilla = new EnvPlantillaGeneracionAdm (this.getUserBean(request));
        
        Vector vOcultos = form.getDatosTablaOcultos(0);
        
        String sIdInstitucion = form.getIdInstitucion();
        String sIdTipoEnvio = form.getIdTipoEnvio();
        String sIdPlantillaEnvios = form.getIdPlantillaEnvios();
        String sIdPlantilla = ((String)vOcultos.elementAt(3)).trim();
        
        if (admPlantilla.borrarPlantilla(sIdInstitucion, sIdTipoEnvio, sIdPlantillaEnvios, sIdPlantilla))
        {
            request.setAttribute("mensaje","messages.deleted.success");
        }
        
        else
        {
            request.setAttribute("mensaje","error.messages.deleted");
        }

        return "exito";
	}

	protected String mostrarRegistro(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response, boolean bEditable, boolean bNuevo) throws ClsExceptions
	{
	    SIGAPlantillasEnviosPlantillasForm form = (SIGAPlantillasEnviosPlantillasForm)formulario;
	    
	    String sIdInstitucion = form.getIdInstitucion();
	    String sIdTipoEnvios = form.getIdTipoEnvio();
	    String sIdPlantillaEnvios = form.getIdPlantillaEnvios();

		String sIdPlantilla="";
		String sDescripcion="";
		String sPorDefecto="";
		
		if (!bNuevo)
		{
			Vector vOcultos = form.getDatosTablaOcultos(0);
			
			sIdPlantilla=(String)vOcultos.elementAt(3);
			sDescripcion=(String)vOcultos.elementAt(4);
			sPorDefecto=(String)vOcultos.elementAt(5);
		}

	    Vector datos = new Vector();
	    Hashtable htDatos = new Hashtable();

	    htDatos.put("idInstitucion", sIdInstitucion);
	    htDatos.put("idTipoEnvios", sIdTipoEnvios);
	    htDatos.put("idPlantillaEnvios", sIdPlantillaEnvios);
	    htDatos.put("idPlantilla", sIdPlantilla);
	    htDatos.put("descripcion", sDescripcion);
	    htDatos.put("porDefecto", sPorDefecto);
	    
	    datos.add(htDatos);
	    
        request.setAttribute("datos", datos);
        request.setAttribute("editable", bEditable ? "1" : "0");
        request.setAttribute("nuevo", bNuevo ? "1" : "0");
        
        if (bEditable)
        {
            Hashtable hashBackUp = new Hashtable();
            
            hashBackUp.put(EnvPlantillaGeneracionBean.C_IDINSTITUCION, sIdInstitucion);
            hashBackUp.put(EnvPlantillaGeneracionBean.C_IDTIPOENVIOS, sIdTipoEnvios);
            hashBackUp.put(EnvPlantillaGeneracionBean.C_IDPLANTILLAENVIOS, sIdPlantillaEnvios);
            hashBackUp.put(EnvPlantillaGeneracionBean.C_IDPLANTILLA, sIdPlantilla);
            hashBackUp.put(EnvPlantillaGeneracionBean.C_DESCRIPCION, sDescripcion);
            hashBackUp.put(EnvPlantillaGeneracionBean.C_PORDEFECTO, sPorDefecto);
            
            request.getSession().setAttribute("DATABACKUP", hashBackUp);
        }
	    
		return "mostrar";
	}
	
	protected String grabar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException
	{
	    String sMensaje="messages.updated.error";
	    String forward = null;
	    UserTransaction tx = null;

	    try
	    {
		    SIGAPlantillasEnviosPlantillasForm form = (SIGAPlantillasEnviosPlantillasForm)formulario;
	        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));

			tx = userBean.getTransaction();			

			FormFile theFile = form.getTheFile();
//		    if(theFile==null || theFile.getFileSize()<1)
//		    	throw new SIGAException("messages.general.error.ficheroNoExiste");


			String sNombreFicheroTemporal="";
			File fPlantilla=null;
			String nombre =  theFile.getFileName();
			
			String 	extension = nombre.substring(nombre.lastIndexOf(".")+1).toLowerCase();
			
			
//			System.out.println("extension"+extension);
			//boolean bZIP=theFile.getContentType().equals(this.extZip);
	
		    if(theFile.getFileSize()>0) 
		    {
		    	InputStream stream =null;
		    	OutputStream bos = null;

		    	String sNombrePlantilla = form.getIdTipoEnvio() + "_" + form.getIdPlantillaEnvios();
		    	String sDirectorio = getPathPlantillasFromDB(userBean) + File.separator + form.getIdInstitucion();
		    	
		    	File fDirectorio = new File(sDirectorio);
		    	fDirectorio.mkdirs();
		    	
		    	try 
		    	{			
		    		stream = theFile.getInputStream();
		    		sNombreFicheroTemporal=sDirectorio + File.separator + "#" + sNombrePlantilla + "_" + (new Date()).getTime() + ".tmp";
		    		bos = new FileOutputStream(sNombreFicheroTemporal);
		    		int bytesRead = 0;
		    		byte[] buffer = new byte[8192];
	
		    		while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) 
		    		{
		    			bos.write(buffer, 0, bytesRead);
		    		}
		    		
		    		fPlantilla = new File(sNombreFicheroTemporal);
		    		fPlantilla.deleteOnExit();
		    	} 
		    	catch (Exception e) 
		    	{
		    	    request.setAttribute("mensaje","messages.updated.error");
		    	}
		    	finally {
		    		try {
			    		bos.close();
			    		stream.close();
		    		}
			    	catch (Exception e) {}
		    	}
		    }
	        
	        EnvPlantillaGeneracionAdm admPlantilla = new EnvPlantillaGeneracionAdm(this.getUserBean(request));
	
	        String descripcionPlantilla=form.getDescripcion();
	        String idInstitucion=form.getIdInstitucion();
	        String idTipoEnvios=form.getIdTipoEnvio();
	        String idPlantillaEnvios=form.getIdPlantillaEnvios();
	        String idPlantilla=form.getIdPlantilla();
	        boolean bPorDefecto = (form.getPorDefecto()!=null && form.getPorDefecto().equals("1")) ? true : false;

	        tx.begin();	        
	        if (admPlantilla.importarPlantilla(descripcionPlantilla, idInstitucion, idTipoEnvios, idPlantillaEnvios, idPlantilla, fPlantilla, bPorDefecto, extension))
	        {
	            sMensaje="messages.updated.success";
	            tx.commit();
	            forward = exitoModal(sMensaje,request);
	        } else{
	        	tx.rollback();
	        	sMensaje="messages.certificados.error.importarplantilla";
	        	forward = exitoModalSinRefresco(sMensaje,request);
	        }

	    }
	    
	    catch(Exception e)
	    {
			throwExcp("messages.general.error", new String[] {"modulo.envios"}, e, tx); 
	    }
	    
	    return forward;
	}
	
	private String getPathPlantillasFromDB(UsrBean usr) throws SIGAException
	{
	    String sPath="";
	    
	    try
	    {
	        String sWhere = " WHERE " + GenParametrosBean.C_PARAMETRO + "='PATH_PLANTILLAS' AND "+
	                                    GenParametrosBean.C_MODULO + "='CER' AND " + 
	                                    GenParametrosBean.C_IDINSTITUCION + "=0";
	        
	        GenParametrosAdm admParametros = new GenParametrosAdm(usr);
	        Vector vParametros = admParametros.select(sWhere);
	        
	        if (vParametros==null || vParametros.size()==0)
	        {
	            throwExcp("messages.certificados.error.importarplantilla", null, null);
	        }

	        GenParametrosBean beanParametros = (GenParametrosBean)vParametros.elementAt(0);
	        sPath = beanParametros.getValor();
	    }
	    
	    catch(Exception e)
	    {
	        throwExcp("messages.certificados.error.importarplantilla", e, null);
	    }
	    
	    return sPath;
	}
	protected String descargar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException
	{
	    UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
	    
	    SIGAPlantillasEnviosPlantillasForm form = (SIGAPlantillasEnviosPlantillasForm)formulario;

		Vector vOcultos = form.getDatosTablaOcultos(0);
        
	    String idInstitucion = (String)vOcultos.elementAt(0);
		String idTipoEnvios = (String)vOcultos.elementAt(1);
		String idPlantillaEnvios = (String)vOcultos.elementAt(2);
		String idPlantilla = (String)vOcultos.elementAt(3);
		String idTipoArchivo = (String)vOcultos.elementAt(6);
	    
//		if(idTipoArchivo.equals(EnvEnviosAdm.TIPOARCHIVO_DOC))
//			return download(mapping, formulario, request, response);
	    
	    EnvPlantillaGeneracionAdm admPlantilla = new EnvPlantillaGeneracionAdm(userBean);
        File fPlantilla = admPlantilla.obtenerPlantilla(idInstitucion, 
        		idTipoEnvios,    		idPlantillaEnvios, idPlantilla);
        Hashtable htPkPlantillaGeneracion = new Hashtable();
        htPkPlantillaGeneracion.put(EnvPlantillaGeneracionBean.C_IDINSTITUCION,idInstitucion);
        htPkPlantillaGeneracion.put(EnvPlantillaGeneracionBean.C_IDTIPOENVIOS,idTipoEnvios);
        htPkPlantillaGeneracion.put(EnvPlantillaGeneracionBean.C_IDPLANTILLAENVIOS,idPlantillaEnvios);
        htPkPlantillaGeneracion.put(EnvPlantillaGeneracionBean.C_IDPLANTILLA,idPlantilla);
        
//        Vector vPlant = admPlantilla.selectByPK(htPkPlantillaGeneracion);	    
//	    EnvPlantillaGeneracionBean plantBean = (EnvPlantillaGeneracionBean)vPlant.firstElement();
//	    String tipoArchivoPlantilla = plantBean.getTipoArchivo();
	               
        EnvEnviosAdm admEnvios = new EnvEnviosAdm(userBean);
        EnvEnviosBean envBean = new EnvEnviosBean();
        envBean.setIdInstitucion(new Integer(idInstitucion));
        envBean.setIdEnvio(new Integer(0));
        envBean.setFechaCreacion(UtilidadesString.formatoFecha(new Date(), "yyyy/MM/dd HH:mm:ss"));
        EnvDestinatariosBean beanDestinatario = new EnvDestinatariosBean();
        beanDestinatario.setIdPersona(new Long("0"));
    	String pathArchivoGenerado = admEnvios.generarDocumentoEnvioPDFDestinatario(envBean, beanDestinatario, fPlantilla,idTipoArchivo,new Hashtable());
		
		request.setAttribute("rutaFichero", pathArchivoGenerado);
		request.setAttribute("generacionOK","OK");
		return "descarga";
	}
}