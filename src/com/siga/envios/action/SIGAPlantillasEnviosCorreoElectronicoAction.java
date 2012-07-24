package com.siga.envios.action;

import java.util.*;

import com.atos.utils.*;
import com.siga.beans.*;
import com.siga.general.*;
import javax.servlet.http.*;
import javax.transaction.UserTransaction;
import com.siga.Utilidades.UtilidadesString;
import com.siga.envios.form.*;

import org.apache.struts.action.*;

public class SIGAPlantillasEnviosCorreoElectronicoAction extends MasterAction
{
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{
	    SIGAPlantillasEnviosCorreoElectronicoForm form = (SIGAPlantillasEnviosCorreoElectronicoForm)formulario;
	    
		form.setIdInstitucion(request.getParameter("idInstitucion").toString());
		form.setIdTipoEnvios(request.getParameter("idTipoEnvio").toString());
		form.setIdPlantillaEnvios(request.getParameter("idPlantillaEnvios").toString());
		String nombrePlantilla = request.getParameter("plantilla").toString();
	    
		Hashtable<String,Object> ht = new Hashtable<String, Object>();
		ht.put(EnvPlantillasEnviosBean.C_IDINSTITUCION,form.getIdInstitucion());
		ht.put(EnvPlantillasEnviosBean.C_IDPLANTILLAENVIOS,form.getIdPlantillaEnvios());
		ht.put(EnvPlantillasEnviosBean.C_IDTIPOENVIOS,form.getIdTipoEnvios());
        
		EnvPlantillasEnviosAdm plantillasEnvioAdm = new EnvPlantillasEnviosAdm (this.getUserBean(request));
        EnvPlantillasEnviosBean plantillasEnvioBean = (EnvPlantillasEnviosBean) plantillasEnvioAdm.selectByPK(ht).firstElement();		        
        nombrePlantilla = plantillasEnvioBean.getNombre();	
        request.setAttribute("nombrePlantilla", nombrePlantilla);
	    
	    EnvCamposPlantillaAdm admProducto = new EnvCamposPlantillaAdm(this.getUserBean(request));	    
	    Vector vDatos = admProducto.obtenerCampos(form.getIdInstitucion(), form.getIdTipoEnvios(), form.getIdPlantillaEnvios(), "E");
	    
	    if (vDatos!=null && vDatos.size()==0)
	    {
	    	form.setAsunto("");
	    	form.setCuerpo("");
	        
	    }
	    
	    else
	    {
	    	Hashtable<String,Object> ht1 = new Hashtable<String, Object>();
		    ht1 = (Hashtable)vDatos.elementAt(0);
		    Hashtable<String,Object> ht2 = new Hashtable<String, Object>();
		    ht2 = (Hashtable)vDatos.elementAt(1);
		    
		    if (ht1.get(EnvCamposBean.C_IDCAMPO).equals(EnvCamposPlantillaAdm.K_IDCAMPO_ASUNTO))
		    {
		    	form.setAsunto((String)ht1.get(EnvCamposPlantillaBean.C_VALOR));
		    	form.setCuerpo((String)ht2.get(EnvCamposPlantillaBean.C_VALOR));
		      
		    }
		    else
		    {	form.setAsunto((String)ht2.get(EnvCamposPlantillaBean.C_VALOR));
	    		form.setCuerpo((String)ht1.get(EnvCamposPlantillaBean.C_VALOR));
		        
		    }
	    }
		return "abrir";
	}

	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{
	    try
	    {
		    SIGAPlantillasEnviosCorreoElectronicoForm form = (SIGAPlantillasEnviosCorreoElectronicoForm)formulario;
	        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
	        
	        EnvCamposPlantillaAdm admProducto = new EnvCamposPlantillaAdm(this.getUserBean(request));
		    
	        /* RGG Cambiando por bvean y updateDirect 
		    Hashtable htDatosOld = new Hashtable();
	
		    htDatosOld.put(EnvCamposPlantillaBean.C_IDINSTITUCION, form.getIdInstitucion());
		    htDatosOld.put(EnvCamposPlantillaBean.C_IDTIPOENVIOS, form.getIdTipoEnvio());
		    htDatosOld.put(EnvCamposPlantillaBean.C_IDPLANTILLAENVIOS, form.getIdPlantillaEnvios());
		    htDatosOld.put(EnvCamposPlantillaBean.C_IDCAMPO, EnvCamposPlantillaAdm.K_IDCAMPO_ASUNTO);
		    htDatosOld.put(EnvCamposPlantillaBean.C_TIPOCAMPO, EnvCamposAdm.K_TIPOCAMPO_E);
		    
		    Hashtable htDatosNew = (Hashtable)htDatosOld.clone();
		    
		    htDatosNew.put(EnvCamposPlantillaBean.C_VALOR, form.getAsunto());
		    */
	        EnvCamposPlantillaBean beanCampos= new EnvCamposPlantillaBean();

	        beanCampos.setIdInstitucion(new Integer(form.getIdInstitucion()));
	        beanCampos.setIdTipoEnvios(new Integer(form.getIdTipoEnvio()));
	        beanCampos.setIdPlantillaEnvios(new Integer(form.getIdPlantillaEnvios()));
	        beanCampos.setIdCampo(new Integer(EnvCamposPlantillaAdm.K_IDCAMPO_ASUNTO));
	        beanCampos.setTipoCampo(EnvCamposAdm.K_TIPOCAMPO_E);
	        beanCampos.setValor(form.getAsunto());
	        
		    UserTransaction tx = userBean.getTransaction();
		    tx.begin();
		    
		    admProducto.delete(beanCampos);
		    
	        if (admProducto.insert(beanCampos))
	        {
	            beanCampos.setIdCampo(new Integer(EnvCamposPlantillaAdm.K_IDCAMPO_CUERPO));
	            String valor= UtilidadesString.reemplazarTextoEntreMarca(form.getCuerpo(),"%%");      	
	            beanCampos.setValor(valor);
	            
	           // beanCampos.setValor(form.getCuerpo());
	    	
		        /*
		        htDatosOld.put(EnvCamposPlantillaBean.C_IDCAMPO, EnvCamposPlantillaAdm.K_IDCAMPO_CUERPO);
		        htDatosNew.put(EnvCamposPlantillaBean.C_IDCAMPO, EnvCamposPlantillaAdm.K_IDCAMPO_CUERPO);
		        htDatosNew.put(EnvCamposPlantillaBean.C_VALOR, form.getCuerpo());
		        */
	        
/*ESTE*/		admProducto.delete(beanCampos);
			        if (admProducto.insert(beanCampos))
				    {
				        tx.commit();
				        request.setAttribute("mensaje","messages.updated.success");
				    }
				    else
				    {
				        tx.rollback();
				        request.setAttribute("mensaje","messages.updated.error");
				    }
		    }
		    else
		    {
		        tx.rollback();
		        request.setAttribute("mensaje","messages.updated.error");
		    }

		    return "exito";
	    }
	    
	    catch(ClsExceptions e)
	    {
	        throw e;
	    }
	    
	    catch(Exception e)
	    {
	        throw new ClsExceptions(e, "Error en update");
	    }
	}
}