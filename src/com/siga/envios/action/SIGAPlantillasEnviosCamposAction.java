package com.siga.envios.action;

import java.util.*;

import com.atos.utils.*;
import com.siga.beans.*;
import com.siga.general.*;
import javax.servlet.http.*;
import com.siga.envios.form.*;
import org.apache.struts.action.*;

public class SIGAPlantillasEnviosCamposAction extends MasterAction
{
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{
	    SIGAPlantillasEnviosCamposForm form = (SIGAPlantillasEnviosCamposForm)formulario;
	    EnvCamposPlantillaAdm admProducto = new EnvCamposPlantillaAdm(this.getUserBean(request));

	    String idInstitucion = form.getIdInstitucion();
	    String idTipoEnvio = form.getIdTipoEnvio();
	    String idPlantillaEnvios = form.getIdPlantillaEnvios();
	    
	    String sEditable = form.getEditable();
	    
	    //Obtenemos el nombre de plantilla por si se ha modificado
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
	    
	    Vector vDatos = admProducto.obtenerCampos(idInstitucion, idTipoEnvio, idPlantillaEnvios, "");
	    
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
	
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{
	    SIGAPlantillasEnviosCamposForm form = (SIGAPlantillasEnviosCamposForm)formulario;
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        
        EnvCamposPlantillaAdm admProducto = new EnvCamposPlantillaAdm(this.getUserBean(request));
	    
	    Hashtable htDatos = new Hashtable();

	    htDatos.put(EnvCamposPlantillaBean.C_IDINSTITUCION, form.getIdInstitucion());
	    htDatos.put(EnvCamposPlantillaBean.C_IDTIPOENVIOS, form.getIdTipoEnvio());
	    htDatos.put(EnvCamposPlantillaBean.C_IDPLANTILLAENVIOS, form.getIdPlantillaEnvios());
	    htDatos.put(EnvCamposPlantillaBean.C_IDCAMPO, form.getIdCampo());
	    htDatos.put(EnvCamposPlantillaBean.C_IDFORMATO, form.getIdFormato());
	    htDatos.put(EnvCamposPlantillaBean.C_TIPOCAMPO, form.getTipoCampo());
	    htDatos.put(EnvCamposPlantillaBean.C_VALOR, form.getValor());

	    if (admProducto.insert(htDatos))
	    {
	        request.setAttribute("mensaje","messages.updated.success");
	    }
	    
	    else
	    {
	        request.setAttribute("mensaje","messages.updated.error");
	    }
        
        request.setAttribute("modal","1");
	    
	    return "exito";
	}

	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions 
	{
	    SIGAPlantillasEnviosCamposForm form = (SIGAPlantillasEnviosCamposForm)formulario;
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        EnvCamposPlantillaAdm admProducto = new EnvCamposPlantillaAdm (this.getUserBean(request));
        
        Hashtable hashOld = (Hashtable)request.getSession().getAttribute("DATABACKUP");
        
        Hashtable hashNew = (Hashtable)hashOld.clone();
	    
	    hashNew.put(EnvCamposPlantillaBean.C_IDFORMATO, form.getIdFormato());
	    hashNew.put(EnvCamposPlantillaBean.C_VALOR, form.getValor());
	    
        if (admProducto.update(hashNew, hashOld))
        {
            request.setAttribute("mensaje","messages.updated.success");
         
            request.removeAttribute("DATABACKUP");
        }
        
        else
        {
            request.setAttribute("mensaje","messages.updated.error");
        }

        request.setAttribute("modal","1");
	    
        return "exito";
	}

	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{
	    SIGAPlantillasEnviosCamposForm form = (SIGAPlantillasEnviosCamposForm)formulario;
        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));

        EnvCamposPlantillaAdm admProducto = new EnvCamposPlantillaAdm(this.getUserBean(request));
        
        Vector vOcultos = form.getDatosTablaOcultos(0);
        
        String sIdInstitucion = form.getIdInstitucion();
        String sIdTipoEnvio = form.getIdTipoEnvio();
        String sIdPlantillaEnvios = form.getIdPlantillaEnvios();
        String idCampo = ((String)vOcultos.elementAt(0)).trim();
        String tipoCampo = ((String)vOcultos.elementAt(2)).trim();

	    Hashtable hash = new Hashtable();
	    
	    hash.put(EnvCamposPlantillaBean.C_IDINSTITUCION, sIdInstitucion);
	    hash.put(EnvCamposPlantillaBean.C_IDTIPOENVIOS, sIdTipoEnvio);
	    hash.put(EnvCamposPlantillaBean.C_IDPLANTILLAENVIOS, sIdPlantillaEnvios);
	    hash.put(EnvCamposPlantillaBean.C_IDCAMPO, idCampo);
	    hash.put(EnvCamposPlantillaBean.C_TIPOCAMPO, tipoCampo);
	    
	    if (admProducto.delete(hash))
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
	    SIGAPlantillasEnviosCamposForm form = (SIGAPlantillasEnviosCamposForm)formulario;
	    
	    String idCampo="";
		String descCampo="";
		String idFormato="";
	    String descFormato="";
	    String tipoCampo="";
	    String capturarDatos="";
	    String valor="";

		String sIdInstitucion=form.getIdInstitucion();
		String sIdTipoEnvio=form.getIdTipoEnvio();
		String sIdPlantillaEnvios=form.getIdPlantillaEnvios();
		
		if (!bNuevo)
		{
		    Vector vVisibles = form.getDatosTablaVisibles(0);
			Vector vOcultos = form.getDatosTablaOcultos(0);		

			descCampo = ((String)vVisibles.elementAt(0)).trim();
		    descFormato = ((String)vVisibles.elementAt(1)).trim();
			
	        idCampo = ((String)vOcultos.elementAt(0)).trim();
	        idFormato = ((String)vOcultos.elementAt(1)).trim();
	        
	        tipoCampo = ((String)vOcultos.elementAt(2)).trim();
	        capturarDatos = ((String)vOcultos.elementAt(3)).trim();
	        
	        if (vOcultos.size()==5)
	        {
	            valor = ((String)vOcultos.elementAt(4)).trim();
	        }
		}

	    Vector datos = new Vector();
	    Hashtable htDatos = new Hashtable();
	    
	    htDatos.put("idCampo", idCampo);
	    htDatos.put("descCampo", descCampo);
	    htDatos.put("idFormato", idFormato);
	    htDatos.put("descFormato", descFormato);

	    htDatos.put("tipoCampo", tipoCampo);
	    htDatos.put("capturarDatos", capturarDatos);
	    htDatos.put("valor", valor);

	    htDatos.put("idInstitucion", sIdInstitucion);
	    htDatos.put("idTipoEnvio", sIdTipoEnvio);
	    htDatos.put("idPlantillaEnvios", sIdPlantillaEnvios);
	    
	    datos.add(htDatos);
	    
        request.setAttribute("datos", datos);
        request.setAttribute("editable", bEditable ? "1" : "0");
        request.setAttribute("nuevo", bNuevo ? "1" : "0");
        
        if (bEditable)
        {
            Hashtable hashBackUp = new Hashtable();
            
            hashBackUp.put(EnvCamposPlantillaBean.C_IDCAMPO, idCampo);
            hashBackUp.put(EnvCamposPlantillaBean.C_IDFORMATO, idFormato);
            hashBackUp.put(EnvCamposPlantillaBean.C_IDINSTITUCION, sIdInstitucion);
            hashBackUp.put(EnvCamposPlantillaBean.C_IDTIPOENVIOS, sIdTipoEnvio);
            hashBackUp.put(EnvCamposPlantillaBean.C_IDPLANTILLAENVIOS, sIdPlantillaEnvios);
            hashBackUp.put(EnvCamposPlantillaBean.C_TIPOCAMPO, tipoCampo);
            hashBackUp.put(EnvCamposPlantillaBean.C_VALOR, valor);
            
            request.getSession().setAttribute("DATABACKUP", hashBackUp);
        }
	    
		return "mostrar";
	}
	
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions 
	{
	    SIGAPlantillasEnviosCamposForm form = (SIGAPlantillasEnviosCamposForm)formulario;
	    UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
        
	    //Obtenemos el bean de plantilla para editar el nombre
	    EnvPlantillasEnviosAdm plantAdm = new EnvPlantillasEnviosAdm(this.getUserBean(request));
	    Hashtable htPk = new Hashtable();
	    htPk.put(EnvCamposPlantillaBean.C_IDINSTITUCION,form.getIdInstitucion());
	    htPk.put(EnvCamposPlantillaBean.C_IDTIPOENVIOS,form.getIdTipoEnvio());
	    htPk.put(EnvCamposPlantillaBean.C_IDPLANTILLAENVIOS,form.getIdPlantillaEnvios());
	    Vector vPlant = plantAdm.selectByPKForUpdate(htPk);	    
	    
	    EnvPlantillasEnviosBean plantBean = (EnvPlantillasEnviosBean)vPlant.firstElement();
	    plantBean.setNombre(form.getPlantilla());
	    plantAdm.update(plantBean);
	    
	    return exitoRefresco("messages.updated.success",request);
	    
	}
}