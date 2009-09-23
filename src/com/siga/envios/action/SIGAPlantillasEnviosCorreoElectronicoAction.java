package com.siga.envios.action;

import java.util.*;

import com.atos.utils.*;
import com.siga.beans.*;
import com.siga.general.*;
import javax.servlet.http.*;
import javax.transaction.UserTransaction;

import com.siga.envios.form.*;
import org.apache.struts.action.*;

public class SIGAPlantillasEnviosCorreoElectronicoAction extends MasterAction
{
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{
	    SIGAPlantillasEnviosCorreoElectronicoForm form = (SIGAPlantillasEnviosCorreoElectronicoForm)formulario;
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
	    
	    Vector vDatos = admProducto.obtenerCampos(idInstitucion, idTipoEnvio, idPlantillaEnvios, "E");
	    
	    if (vDatos!=null && vDatos.size()==0)
	    {
	        request.setAttribute("asunto","");
	        request.setAttribute("cuerpo","");
	    }
	    
	    else
	    {
		    Hashtable ht1 = (Hashtable)vDatos.elementAt(0);
		    Hashtable ht2 = (Hashtable)vDatos.elementAt(1);
		    
		    if (ht1.get(EnvCamposBean.C_IDCAMPO).equals(EnvCamposPlantillaAdm.K_IDCAMPO_ASUNTO))
		    {
		        request.setAttribute("asunto", ht1.get(EnvCamposPlantillaBean.C_VALOR));
		        request.setAttribute("cuerpo", ht2.get(EnvCamposPlantillaBean.C_VALOR));
		    }
		    else
		    {
		        request.setAttribute("asunto", ht2.get(EnvCamposPlantillaBean.C_VALOR));
		        request.setAttribute("cuerpo", ht1.get(EnvCamposPlantillaBean.C_VALOR));
		    }
	    }
	    
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
	            beanCampos.setValor(form.getCuerpo());
	    	
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