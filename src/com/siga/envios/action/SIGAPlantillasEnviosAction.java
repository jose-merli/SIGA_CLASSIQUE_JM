package com.siga.envios.action;

import java.util.*;
import com.atos.utils.*;
import com.siga.beans.*;
import com.siga.general.*;
import javax.servlet.http.*;
import javax.transaction.UserTransaction;

import com.siga.envios.form.*;
import org.apache.struts.action.*;

public class SIGAPlantillasEnviosAction extends MasterAction
{
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{
		return "abrir";
	}

	protected String abrirConParametros(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{
	    SIGAPlantillasEnviosForm form = (SIGAPlantillasEnviosForm)formulario;
	    
	    request.setAttribute("descripcionPlantilla", form.getDescripcionPlantilla());
	    request.setAttribute("idTipoEnvios", form.getIdTipoEnvios());
	    
		return "abrir";
	}

	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{
	    SIGAPlantillasEnviosForm form = (SIGAPlantillasEnviosForm)formulario;
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        EnvPlantillasEnviosAdm admPlantilla = new EnvPlantillasEnviosAdm(this.getUserBean(request));
        
        String idInstitucion = userBean.getLocation();
        String descripcionPlantilla = form.getDescripcionPlantilla();
        String idTipoEnvios = form.getIdTipoEnvios();

        Vector vDatos = admPlantilla.buscarPlantillas(idInstitucion, idTipoEnvios, descripcionPlantilla);

        request.setAttribute("datos", vDatos);
        
        request.setAttribute("descripcionPlantilla", form.getDescripcionPlantilla());
        request.setAttribute("idTipoEnvios", form.getIdTipoEnvios());
        
        return "buscar";
	}

	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions 
	{
	    return mostrarRegistro(mapping, formulario, request, response, true);
	}

	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions 
	{
	    return mostrarRegistro(mapping, formulario, request, response, false);
	}

	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions 
	{
	    return "mostrar";
	}
	
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions 
	{
	    SIGAPlantillasEnviosForm form = (SIGAPlantillasEnviosForm)formulario;
	    EnvPlantillasEnviosAdm admPlantilla = new EnvPlantillasEnviosAdm(this.getUserBean(request));
	    
	    Vector vOcultos = form.getDatosTablaOcultos(0);
	    
	    String idInstitucion = ((String)vOcultos.elementAt(0)).trim();
	    String idTipoEnvio = ((String)vOcultos.elementAt(1)).trim();
	    String idPlantillaEnvios = ((String)vOcultos.elementAt(2)).trim();
	    
	    if (admPlantilla.borrarPlantilla(idInstitucion, idPlantillaEnvios, idTipoEnvio))
	    {
	        request.setAttribute("mensaje","messages.deleted.success");
	    }
	    
	    else
	    {
	        request.setAttribute("mensaje","error.messages.deleted");
	    }

	    return "exito";
	}

	protected String mostrarRegistro(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response, boolean bEditable) throws ClsExceptions
	{
	    SIGAPlantillasEnviosForm form = (SIGAPlantillasEnviosForm)formulario;
	    
	    Vector vOcultos = form.getDatosTablaOcultos(0);
	    Vector vVisibles = form.getDatosTablaVisibles(0);

        String idInstitucion = (String)vOcultos.elementAt(0);
        String idTipoEnvios = (String)vOcultos.elementAt(1);
        String idPlantillaEnvios = (String)vOcultos.elementAt(2);
        
        String sPlantilla = (String)vVisibles.elementAt(0);

	    Hashtable htDatos = new Hashtable();
	    
	    htDatos.put("idInstitucion", idInstitucion);
	    htDatos.put("idTipoEnvio", idTipoEnvios);
	    htDatos.put("idPlantillaEnvios", idPlantillaEnvios);
	    htDatos.put("editable", bEditable ? "1" : "0");
	    htDatos.put("plantilla", sPlantilla);
	    htDatos.put("descripcionPlantilla", form.getDescripcionPlantilla());
//	    htDatos.put("idTipoEnvios", form.getIdTipoEnvios());
	    
	    request.setAttribute("htDatos", htDatos);
	    
		return "abrirPestanas";
	}
	
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException
	{
	    try
	    {
		    SIGAPlantillasEnviosForm form = (SIGAPlantillasEnviosForm)formulario;
		    UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
		    EnvPlantillasEnviosAdm admPlantilla = new EnvPlantillasEnviosAdm(this.getUserBean(request));
		    
		    String idInstitucion = userBean.getLocation();
		    String idTipoEnvio = form.getIdTipoEnvio();
		    String descPlantilla = form.getDescripcionPlantilla();

		    UserTransaction tx = userBean.getTransaction();
		    tx.begin();
	
		    if (admPlantilla.insertarPlantilla(idInstitucion, descPlantilla, idTipoEnvio))
		    {
		        tx.commit();
		        
		        Hashtable hashAux = new Hashtable();
		        
		        hashAux.put(EnvPlantillasEnviosBean.C_IDINSTITUCION, idInstitucion);
		        hashAux.put(EnvPlantillasEnviosBean.C_IDTIPOENVIOS, idTipoEnvio);

		        String idPlantilla=""+(Integer.parseInt((new EnvPlantillasEnviosAdm(this.getUserBean(request))).getNuevoID(hashAux))-1);
		        
		        request.setAttribute("retorno", idInstitucion + "," + idTipoEnvio + "," + idPlantilla + "%" + descPlantilla + ",;");
		        
		        request.setAttribute("descOperation","messages.updated.success");
		    }
		    
		    else
		    {
		        tx.rollback();
		        
		        request.setAttribute("descOperation","messages.updated.error");
		    }
	
		    request.setAttribute("modal", "1");
		    return "refresh";
	    }
	    
	    catch(ClsExceptions e)
	    {
	        throw e;
	    }
	    
	    catch(Exception e)
	    {
	        throw new SIGAException("Error en insert");
	    }
	}
}