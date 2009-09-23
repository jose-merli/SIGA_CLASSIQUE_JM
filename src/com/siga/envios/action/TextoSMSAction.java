/*
 * VERSIONES:
 * jose.barrientos 		4-6-2009		Creacion	
 *
 */

/**
 * Clase encargada del manejo de plantillas de sms  
 */

package com.siga.envios.action;

import java.util.*;

import com.atos.utils.*;
import com.siga.administracion.SIGAConstants;
import com.siga.beans.*;
import com.siga.general.*;

import javax.servlet.http.*;
import javax.transaction.UserTransaction;

import com.siga.envios.form.*;

import org.apache.struts.action.*;

public class TextoSMSAction extends MasterAction
{

	public ActionForward executeInternal (ActionMapping mapping,
			ActionForm formulario,
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException {

		String mapDestino = "exception";
		MasterForm miForm = null;

		try { 

			do {
				UsrBean usrbean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
				//Si venimos de Productos y Servicios para la seleccion de la direccion del envio, damos acceso total:
				if (usrbean.isLetrado())
					usrbean.setAccessType(SIGAConstants.ACCESS_READ);

				miForm = (MasterForm) formulario;
				if (miForm != null) {
					String accion = miForm.getModo();

					if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
						mapDestino = abrir(mapping, miForm, request, response);
						break;				
					} else {
						return super.executeInternal(mapping,formulario,request,response);
					}
				}
			} while (false);

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null)	{ 
				throw new ClsExceptions("El ActionMapping no puede ser nulo");
			}
			return mapping.findForward(mapDestino);
		} catch (SIGAException es) {
			throw es;
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.envios"});
		}
	}
	
	/**
	 * 
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{
	    TextoSMSForm form = (TextoSMSForm)formulario;
	    EnvCamposPlantillaAdm admPlantillas = new EnvCamposPlantillaAdm(this.getUserBean(request));

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
	    
	    Vector vDatos = admPlantillas.obtenerCampos(idInstitucion, idTipoEnvio, idPlantillaEnvios, "S");
	    
	    if (vDatos!=null && vDatos.size()==0)
	    {
	        request.setAttribute("cuerpo","");
	    }
	    
	    else
	    {
		    Hashtable ht1 = (Hashtable)vDatos.elementAt(0);
		    
		    if (ht1.get(EnvCamposBean.C_IDCAMPO).equals(EnvCamposPlantillaAdm.K_IDCAMPO_SMS))
		    {
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
	    
		return "inicio";
	}
	
	
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions
	{
	    try
	    {
	    	TextoSMSForm form = (TextoSMSForm)formulario;
	        UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));
	        EnvCamposPlantillaAdm admProducto = new EnvCamposPlantillaAdm(this.getUserBean(request));
		    EnvCamposPlantillaBean beanCampos= new EnvCamposPlantillaBean();

	        beanCampos.setIdInstitucion(new Integer(form.getIdInstitucion()));
	        beanCampos.setIdTipoEnvios(new Integer(form.getIdTipoEnvio()));
	        beanCampos.setIdPlantillaEnvios(new Integer(form.getIdPlantillaEnvios()));
	        beanCampos.setIdCampo(new Integer(EnvCamposPlantillaAdm.K_IDCAMPO_SMS));
	        beanCampos.setTipoCampo(EnvCamposAdm.K_TIPOCAMPO_S);
	        
		    UserTransaction tx = userBean.getTransaction();
		    tx.begin();
		    
		    admProducto.delete(beanCampos);
		    
	        if (admProducto.insert(beanCampos))
	        {
	            beanCampos.setIdCampo(new Integer(EnvCamposPlantillaAdm.K_IDCAMPO_SMS));
	            beanCampos.setValor(form.getCuerpo());
	        
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