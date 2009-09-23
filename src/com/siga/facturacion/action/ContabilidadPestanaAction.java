/*
 * VERSIONES:
 */
package com.siga.facturacion.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.FacSerieFacturacionAdm;
import com.siga.beans.FacSerieFacturacionBean;
import com.siga.facturacion.form.ContabilidadPestanaForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 * Clase action para confirmar la facturacion.<br/>
 * Gestiona abrir y confirmar Facturas
 * @version david.sanchezp: cambios para pedir y tener en cuenta la fecha de cargo.
 */
public class ContabilidadPestanaAction extends MasterAction{
	
	/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error

	protected ActionForward executeInternal (ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String mapDestino = "exception";
		MasterForm miForm = null;

		try {
			miForm = (MasterForm) formulario;
			if (miForm == null) {
					return mapping.findForward(mapDestino);
				}
				
				String accion = miForm.getModo();

				// La primera vez que se carga el formulario 
				if (accion == null || accion.equalsIgnoreCase("") || accion.equalsIgnoreCase("abrir")){
					mapDestino = abrir(mapping, miForm, request, response);	
				}
				else {
					return super.executeInternal(mapping, formulario, request, response);
				}

			// Redireccionamos el flujo a la JSP correspondiente
			if (mapDestino == null) 				
			{ 				
			    if (miForm.getModal().equalsIgnoreCase("TRUE"))
			    {
			        request.setAttribute("exceptionTarget", "parent.modal");
			    }			    
			    throw new ClsExceptions("El ActionMapping no puede ser nulo","","0","GEN00","15");
			}			
		}
		catch (SIGAException es) { 
		    throw es; 
		}
		catch (Exception e) { 
		    throw new SIGAException("messages.general.error",e,new String[] {"modulo.facturacion"}); 
		} 
		return mapping.findForward(mapDestino);   	
	}
	
	
*/	
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException 
	{
		String idInstitucion = "" + this.getIDInstitucion(request);
		String idSerieFacturacion=(String)request.getSession().getAttribute("idSerieFacturacion");
		
		if (idSerieFacturacion == null) {
			return "serieFacturacionNoExiste";
		}
		
		Hashtable h = new Hashtable ();
		UtilidadesHash.set(h, FacSerieFacturacionBean.C_IDINSTITUCION, idInstitucion);
		UtilidadesHash.set(h, FacSerieFacturacionBean.C_IDSERIEFACTURACION, idSerieFacturacion);
		FacSerieFacturacionAdm adm = new FacSerieFacturacionAdm (this.getUserBean(request));
		Vector v = adm.selectByPK(h);
		if (v != null && v.size() == 1) {
		     request.setAttribute("serieFacturacion", (FacSerieFacturacionBean)v.get(0));
		}

		return super.abrir(mapping, formulario, request, response);
	}
	
	
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException 
	{
		try {
			String idInstitucion = "" + this.getIDInstitucion(request);
			String idSerieFacturacion=(String)request.getSession().getAttribute("idSerieFacturacion");
			
			Hashtable h = new Hashtable ();
			UtilidadesHash.set(h, FacSerieFacturacionBean.C_IDINSTITUCION, idInstitucion);
			UtilidadesHash.set(h, FacSerieFacturacionBean.C_IDSERIEFACTURACION, idSerieFacturacion);
			FacSerieFacturacionAdm adm = new FacSerieFacturacionAdm (this.getUserBean(request));
			Vector v = adm.selectByPK(h);
			if (v == null || v.size() != 1) {
				return this.error("messages.upload.error", new ClsExceptions("messages.upload.error"), request);
			}
			
			ContabilidadPestanaForm miform = (ContabilidadPestanaForm) formulario; 
			FacSerieFacturacionBean bean = (FacSerieFacturacionBean)v.get(0);
			bean.setConfigDeudor(miform.getCuentaClienteConfiguracion());
			bean.setConfigIngresos(miform.getCuentaIngresosConfiguracion());
			bean.setCuentaClientes(miform.getCuentaClienteGenerica());
			bean.setCuentaIngresos(miform.getCuentaIngresosGenerica());
			if (!adm.updateDirect(bean)) {
				return this.error("messages.upload.error", new ClsExceptions("messages.upload.error"), request);
			}
		}
		catch (Exception e) {
			return this.error("messages.upload.error", new ClsExceptions("messages.upload.error"), request);
		}
    	return exito("messages.updated.success", request);					
	}
}