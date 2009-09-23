/*
 * Created on 14-feb-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.productos.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.PysServiciosAdm;
import com.siga.beans.PysServiciosBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.productos.form.MantenimientoCategoriasServiciosForm;

/**
 * @author daniel.campos
 *
 */
public class MantenimientoCategoriasServiciosAction extends MasterAction
{
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscarPor(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		try {
			MantenimientoCategoriasServiciosForm miForm = (MantenimientoCategoriasServiciosForm) formulario;
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
			
			PysServiciosAdm serviciosAdm = new PysServiciosAdm (this.getUserBean(request));
			String where = "";
			if( (miForm.getBuscarIdTipoServicio() != null) && (miForm.getBuscarIdTipoServicio().intValue() != 0)) { 
				where = " Where " + PysServiciosBean.C_IDTIPOSERVICIOS + " = " + miForm.getBuscarIdTipoServicio()+
					    " AND "+PysServiciosBean.C_IDINSTITUCION+" = "+user.getLocation();
			} else {
				where = " Where "+PysServiciosBean.C_IDINSTITUCION+" = "+user.getLocation();
			}
			Vector servicios = serviciosAdm.select(where);

			request.setAttribute("servicios", servicios);
		}
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.productos"}, e, null);
		} 
		return "buscarPor";
	}
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#editar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		String modo = "editar";
		try {
			MantenimientoCategoriasServiciosForm miForm = (MantenimientoCategoriasServiciosForm) formulario;
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
			
			Long 	idServicio 		= new Long 	  ((String)miForm.getDatosTablaOcultos(0).get(0));
			Integer idTipoServicio 	= new Integer ((String)miForm.getDatosTablaOcultos(0).get(1));
			
			PysServiciosAdm serviciosAdm = new PysServiciosAdm (this.getUserBean(request));
			Hashtable claves = new Hashtable ();
			UtilidadesHash.set (claves, PysServiciosBean.C_IDSERVICIO, idServicio);
			UtilidadesHash.set (claves, PysServiciosBean.C_IDTIPOSERVICIOS, idTipoServicio);
			UtilidadesHash.set (claves, PysServiciosBean.C_IDINSTITUCION, user.getLocation());
			
			Vector servicios = serviciosAdm.select(claves);
			if (servicios.size() == 1) {
				PysServiciosBean servicio = (PysServiciosBean) servicios.get(0);
				request.getSession().setAttribute("DATABACKUP", servicio);
			}
			request.setAttribute("modo", modo);			
		}
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.productos"}, e, null); 
		} 
		return modo;
	}
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#modificar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {

		UserTransaction t = this.getUserBean(request).getTransaction();
		
		try {
			MantenimientoCategoriasServiciosForm miForm = (MantenimientoCategoriasServiciosForm) formulario;

			PysServiciosBean servicio = (PysServiciosBean) request.getSession().getAttribute("DATABACKUP");
			servicio.setDescripcion(miForm.getDescripcionServicio());
			
			PysServiciosAdm serviciosAdm = new PysServiciosAdm (this.getUserBean(request));
			t.begin();
			if(serviciosAdm.update(servicio)) {
				t.commit();
			}
			else {
				throwExcp("messages.updated.error", new String[] {"modulo.productos"}, new ClsExceptions("messages.updated.error"), t); 
			}
		}
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, t); 
		} 
		return exitoModal("messages.updated.success", request);
	}
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#nuevo(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		String modo = "nuevo";
		try {
			request.setAttribute("modo", modo);			
		}
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.productos"}, e, null); 
		} 
		return modo;
	}
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#insertar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		UserTransaction t = this.getUserBean(request).getTransaction();
		
		try {
			MantenimientoCategoriasServiciosForm miForm = (MantenimientoCategoriasServiciosForm) formulario;
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
			
			PysServiciosBean servicio = new PysServiciosBean();
			servicio.setDescripcion(miForm.getDescripcionServicio());
			servicio.setIdTipoServicios(miForm.getIdTipoServicio());
			servicio.setIdInstitucion(new Integer(user.getLocation()));
			
			PysServiciosAdm serviciosAdm = new PysServiciosAdm (this.getUserBean(request));
			t.begin();
			if(serviciosAdm.insert(servicio)) {
				t.commit();
			}
			else {
				throwExcp("messages.general.error", new String[] {"modulo.productos"}, new ClsExceptions("messages.inserted.error"), t); 
			}
		}
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.productos"}, e, t); 
		} 
		return exitoModal("messages.inserted.success", request);
	}
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#borrar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		UserTransaction t = this.getUserBean(request).getTransaction();
		
		try {
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");	
			MantenimientoCategoriasServiciosForm miForm = (MantenimientoCategoriasServiciosForm) formulario;
			
			Long 	idServicio 		= new Long 	  ((String)miForm.getDatosTablaOcultos(0).get(0));
			Integer idTipoServicio 	= new Integer ((String)miForm.getDatosTablaOcultos(0).get(1));
			
			PysServiciosAdm serviciosAdm = new PysServiciosAdm (this.getUserBean(request));
			Hashtable claves = new Hashtable ();
			UtilidadesHash.set (claves, PysServiciosBean.C_IDSERVICIO, idServicio);
			UtilidadesHash.set (claves, PysServiciosBean.C_IDTIPOSERVICIOS, idTipoServicio);
			UtilidadesHash.set (claves, PysServiciosBean.C_IDINSTITUCION, user.getLocation());
			
			t.begin();
			if(serviciosAdm.delete(claves)) {
				t.commit();
			}
			else {
				throwExcp("messages.deleted.error",new String[] {"modulo.productos"},new ClsExceptions("messages.deleted.error"),t);
			}
		}
		catch (Exception e) { 
			throwExcp("messages.pys.mantenimientoServicios.errorBorrado", new SIGAException("messages.pys.mantenimientoServicios.errorBorrado"), t); 
		}
		return exitoRefresco("messages.deleted.success", request);
	}
}
