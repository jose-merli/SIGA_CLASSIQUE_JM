package com.siga.envios.action;

import java.io.File;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.beans.EnvCamposPlantillaBean;
import com.siga.beans.EnvEnviosAdm;
import com.siga.beans.EnvEnviosBean;
import com.siga.beans.EnvImagenPlantillaAdm;
import com.siga.beans.EnvImagenPlantillaBean;
import com.siga.beans.EnvPlantillasEnviosAdm;
import com.siga.beans.EnvPlantillasEnviosBean;
import com.siga.envios.form.ImagenPlantillaForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
/**
 * 
 * @author jorgeta
 *
 */
public class ImagenPlantillaAction extends MasterAction
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
	            	if(mapping.getPath().equals("/ENV_ImagenesEnvio")){
	            		mapDestino = abrirImagenesEnvio(mapping, miForm, request, response);
	        			
	        		}else if(mapping.getPath().equals("/ENV_ImagenesPlantilla")){
	        			mapDestino = abrirImagenesPlantilla(mapping, miForm, request, response);	
	        			
	        		}
	                
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
			//String idImagen = (String)form.getDatosTablaOcultos(0).elementAt(3);
			//form.setIdImagen(idImagen);

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
	
	protected String abrirImagenesPlantilla(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{
		ImagenPlantillaForm form = (ImagenPlantillaForm)formulario;
		
		form.setIdInstitucion(request.getParameter("idInstitucion").toString());
		form.setIdTipoEnvios(request.getParameter("idTipoEnvio").toString());
		form.setIdPlantillaEnvios(request.getParameter("idPlantillaEnvios").toString());
		String editable = request.getParameter("editable").toString();
		
		// Obtenemos el nombre de plantilla por si se ha modificado
	    EnvPlantillasEnviosAdm plantAdm = new EnvPlantillasEnviosAdm(this.getUserBean(request));
	    Hashtable<String,Object> htPk = new Hashtable<String, Object>();
	    htPk.put(EnvCamposPlantillaBean.C_IDINSTITUCION,form.getIdInstitucion());
	    htPk.put(EnvCamposPlantillaBean.C_IDTIPOENVIOS,form.getIdTipoEnvios());
	    htPk.put(EnvCamposPlantillaBean.C_IDPLANTILLAENVIOS,form.getIdPlantillaEnvios());
	    EnvPlantillasEnviosBean pantillaEnvios = (EnvPlantillasEnviosBean)plantAdm.selectByPK(htPk).firstElement();   	    
	    form.setPlantillaEnvios(pantillaEnvios);
	    
		EnvImagenPlantillaAdm admImagenPlantilla = new EnvImagenPlantillaAdm(this.getUserBean(request));
	    List<ImagenPlantillaForm> lImagenes = admImagenPlantilla.getImagenes(form.getImagenPlantillaBean());
	    form.setImagenes(lImagenes);
	    form.setEditable(editable.equals(ClsConstants.DB_TRUE));
	    form.setVolverAccion("/SIGA/ENV_DefinirPlantillas.do?noreset=true");
	    form.setVolverModo("abrirConParametros");

	    return "abrir";
	}
	protected String abrirImagenesEnvio(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{
		ImagenPlantillaForm form = (ImagenPlantillaForm)formulario;
		UsrBean userBean =this.getUserBean(request);

        String idInstitucion = userBean.getLocation();
        String idEnvio = (String)request.getParameter("idEnvio");

        Hashtable<String,Object> htAux = new Hashtable<String, Object>();

        htAux.put(EnvEnviosBean.C_IDINSTITUCION, idInstitucion);
        htAux.put(EnvEnviosBean.C_IDENVIO, idEnvio);

        
        EnvEnviosAdm admEnvio = new EnvEnviosAdm(userBean);
        EnvEnviosBean envioBean = (EnvEnviosBean)admEnvio.selectByPK(htAux).elementAt(0);

//        String sDescripcionEnvio = envioBean.getDescripcion();
        String sIdPlantillaEnvio = ""+envioBean.getIdPlantillaEnvios();

        htAux.clear();
        htAux.put(EnvPlantillasEnviosBean.C_IDINSTITUCION, envioBean.getIdInstitucion());
        htAux.put(EnvPlantillasEnviosBean.C_IDTIPOENVIOS, envioBean.getIdTipoEnvios());
        htAux.put(EnvPlantillasEnviosBean.C_IDPLANTILLAENVIOS, envioBean.getIdPlantillaEnvios());
        

        EnvPlantillasEnviosAdm admPlantillaEnvio = new EnvPlantillasEnviosAdm(this.getUserBean(request));
        EnvPlantillasEnviosBean plantillaEnvio = (EnvPlantillasEnviosBean)admPlantillaEnvio.selectByPK(htAux).elementAt(0);

		
		form.setIdInstitucion(idInstitucion);
		form.setIdTipoEnvios(envioBean.getIdTipoEnvios().toString());
		form.setIdPlantillaEnvios(sIdPlantillaEnvio);

	    form.setPlantillaEnvios(plantillaEnvio);
		EnvImagenPlantillaAdm admImagenPlantilla = new EnvImagenPlantillaAdm(this.getUserBean(request));
	    List<ImagenPlantillaForm> lImagenes = admImagenPlantilla.getImagenes(form.getImagenPlantillaBean());
	    form.setImagenes(lImagenes);
	    form.setEditable(false);
	    form.setVolverAccion("/SIGA/ENV_DefinirEnvios.do?noReset=true&buscar=true");
	    form.setVolverModo("abrir");
	    
		return "abrir";
	}

	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions 
	{
		ImagenPlantillaForm form = (ImagenPlantillaForm)formulario;
		EnvImagenPlantillaAdm admImagenPlantilla = new EnvImagenPlantillaAdm(this.getUserBean(request));
		String idImagen = (String)form.getDatosTablaOcultos(0).elementAt(3);
	    
		Hashtable<String,Object> htPk = new Hashtable<String, Object>();
	    htPk.put(EnvImagenPlantillaBean.C_IDINSTITUCION,form.getIdInstitucion());
	    htPk.put(EnvImagenPlantillaBean.C_IDTIPOENVIOS,form.getIdTipoEnvios());
	    htPk.put(EnvImagenPlantillaBean.C_IDPLANTILLAENVIOS,form.getIdPlantillaEnvios());
	    htPk.put(EnvImagenPlantillaBean.C_IDIMAGEN,idImagen);
	    
	    EnvImagenPlantillaBean imagenBean =(EnvImagenPlantillaBean)admImagenPlantilla.selectByPK(htPk).get(0);	 
	    form.setIdImagen(idImagen);
	    form.setNombre(imagenBean.getNombre());
	    form.setTipoArchivo(imagenBean.getTipoArchivo());
	    form.setEmbebed(imagenBean.isEmbebed());
	    
	    return mostrarRegistro(mapping, form, request, response, true, false);
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
		  String forward = null;

		    try
		    {
		    	ImagenPlantillaForm form = (ImagenPlantillaForm)formulario;
		        UsrBean userBean = this.getUserBean(request);
		        EnvImagenPlantillaAdm imagenAdm = new EnvImagenPlantillaAdm(userBean);
		        imagenAdm.insertar(form.getImagenPlantillaBean());
	            forward = exitoModal("messages.updated.success",request);


		    }
		    
		    catch(Exception e)
		    {
				throwExcp("messages.general.error", new String[] {"modulo.envios"}, e, null); 
		    }
		    
		    return forward;
	}

	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException 
	{
		 String forward = null;

		    try
		    {
		    	ImagenPlantillaForm form = (ImagenPlantillaForm)formulario;
		        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
		        EnvImagenPlantillaAdm imagenAdm = new EnvImagenPlantillaAdm(userBean);
		        imagenAdm.modificarImagen(form.getImagenPlantillaBean());
	            forward = exitoModal("messages.updated.success",request);


		    }
		    catch(Exception e)
		    {
				throwExcp("messages.general.error", new String[] {"modulo.envios"}, e, null); 
		    }
		    
		    return forward;
	}

	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException
	{
		ImagenPlantillaForm form = (ImagenPlantillaForm)formulario;
		UsrBean userBean = this.getUserBean(request);
        EnvImagenPlantillaAdm imagenAdm = new EnvImagenPlantillaAdm(userBean);
		String idImagen = (String)form.getDatosTablaOcultos(0).elementAt(3);
		form.setIdImagen(idImagen);
        
        
        try {
        	imagenAdm.borrarImagen(form.getImagenPlantillaBean());
            request.setAttribute("mensaje","messages.deleted.success");
		} catch (Exception e) {
			request.setAttribute("mensaje","error.messages.deleted");
		}
        return "exito";
	}

	protected String mostrarRegistro(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response, boolean bEditable, boolean bNuevo) throws ClsExceptions
	{
		
		return "mostrar";
	}
	

}