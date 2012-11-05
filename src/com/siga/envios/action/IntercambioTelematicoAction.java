package com.siga.envios.action;

import java.io.File;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.beans.EnvEnviosAdm;
import com.siga.beans.EnvEnviosBean;
import com.siga.beans.EnvImagenPlantillaAdm;
import com.siga.envios.form.ImagenPlantillaForm;
import com.siga.envios.form.IntercambioTelematicoForm;
import com.siga.envios.service.IntercambiosService;
import com.siga.envios.service.IntercambiosServiceDispatcher;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
/**
 * 
 * @author jorgeta
 *
 */
public class IntercambioTelematicoAction extends MasterAction
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
	            } else if (accion.equalsIgnoreCase("descargarLog"))
	            {
	                mapDestino = descargarLog(mapping, miForm, request, response);
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
		try 
		{
			ImagenPlantillaForm form = (ImagenPlantillaForm)formulario;
	        Vector vOcultos = form.getDatosTablaOcultos(0);
			String idImagen = (String)vOcultos.elementAt(3);
			form.setIdImagen(idImagen);

			EnvImagenPlantillaAdm admImagenPlantilla = new EnvImagenPlantillaAdm(this.getUserBean(request));
			File fImagen = admImagenPlantilla.getImagen(form.getImagenPlantillaBean());

			if(fImagen==null || !fImagen.exists()){
				throw new SIGAException("error.messages.fileNotFound"); 
			}
			String nombreFichero = fImagen.getName();
			request.setAttribute("nombreFichero", nombreFichero);
			request.setAttribute("rutaFichero", fImagen.getPath());
			request.setAttribute("borrarFichero", "false");			
			request.setAttribute("generacionOK","OK");
		} 
		catch (SIGAException e) 
		{ 
		  	throw e; 
		}
		catch (Exception e) 
		{ 
			throwExcp("messages.general.error", new String[] {"modulo.envios"}, e, null); 
		}
		
		
		return "descarga";
	}
	
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{
		IntercambioTelematicoForm form = (IntercambioTelematicoForm)formulario;
		UsrBean userBean =this.getUserBean(request);
		String idInstitucion = userBean.getLocation();
        String idEnvio = (String)request.getParameter("idEnvio");
//        String idIntercambio = (String)request.getParameter("idIntercambio");
        Hashtable htPk = new Hashtable();
		htPk.put(EnvEnviosBean.C_IDINSTITUCION,idInstitucion);
		htPk.put(EnvEnviosBean.C_IDENVIO,idEnvio);
		//obtengo el envio
		EnvEnviosAdm enviosAdm = new EnvEnviosAdm(userBean);
		EnvEnviosBean envBean = (EnvEnviosBean)enviosAdm.selectByPKForUpdate(htPk).elementAt(0);
		IntercambiosService intercambiosService = (IntercambiosService) IntercambiosServiceDispatcher.getService(getBusinessManager(),envBean.getIdTipoIntercambioTelematico().toString());
        IntercambioTelematicoForm intercambioTelematicoForm = intercambiosService.getIntercambioTelematico(idEnvio,idInstitucion,userBean.getLanguage());
        request.setAttribute("IntercambioTelematicoForm", intercambioTelematicoForm);
       

		return "abrir";
	}
	private String descargarLog(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
		{
		String forward = "descargaFichero";
		try {
			IntercambioTelematicoForm form = (IntercambioTelematicoForm)formulario;
			UsrBean userBean =this.getUserBean(request);
			Hashtable htPk = new Hashtable();
			htPk.put(EnvEnviosBean.C_IDINSTITUCION,form.getIdInstitucion());
			htPk.put(EnvEnviosBean.C_IDENVIO,form.getIdEnvio());
			//obtengo el envio
			EnvEnviosAdm enviosAdm = new EnvEnviosAdm(userBean);
			EnvEnviosBean envBean = (EnvEnviosBean)enviosAdm.selectByPKForUpdate(htPk).elementAt(0);
			
				IntercambiosService intercambiosService = (IntercambiosService) IntercambiosServiceDispatcher.getService(getBusinessManager(),envBean.getIdTipoIntercambioTelematico().toString());	
			File fichero = intercambiosService.getFicheroLog(form.getIdEnvio(),form.getIdInstitucion());
			request.setAttribute("borrarFichero", "true");

			request.setAttribute("nombreFichero", fichero.getName());
			request.setAttribute("rutaFichero", fichero.getPath());
			

		} catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.envios"},e,null);
		}
		return forward;
	}

	
	
	

}