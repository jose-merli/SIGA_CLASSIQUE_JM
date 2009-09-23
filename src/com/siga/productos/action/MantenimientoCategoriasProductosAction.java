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
import com.siga.beans.*;
import com.siga.general.*;
import com.siga.productos.form.MantenimientoCategoriasProductosForm;

/**
 * @author daniel.campos
 *
 */
public class MantenimientoCategoriasProductosAction extends MasterAction {
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscarPor(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		try {
			MantenimientoCategoriasProductosForm miForm = (MantenimientoCategoriasProductosForm) formulario;
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");	
			
			PysProductosAdm productosAdm = new PysProductosAdm (this.getUserBean(request));
			String where = "";
			if( (miForm.getBuscarIdTipoProducto() != null) && (miForm.getBuscarIdTipoProducto().intValue() != 0)) { 
				where = " Where " + PysProductosBean.C_IDTIPOPRODUCTO + " = " + miForm.getBuscarIdTipoProducto()+
					    " AND "+PysProductosBean.C_IDINSTITUCION+" = "+user.getLocation();				
			} else {
				where = " Where "+PysProductosBean.C_IDINSTITUCION+" = "+user.getLocation();				
			}
			Vector productos = productosAdm.select(where);

			request.setAttribute("productos", productos);
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
			MantenimientoCategoriasProductosForm miForm = (MantenimientoCategoriasProductosForm) formulario;
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
			
			Long 	idProducto 		= new Long 	  ((String)miForm.getDatosTablaOcultos(0).get(0));
			Integer idTipoProducto 	= new Integer ((String)miForm.getDatosTablaOcultos(0).get(1));
			
			PysProductosAdm productosAdm = new PysProductosAdm (this.getUserBean(request));
			Hashtable claves = new Hashtable ();
			UtilidadesHash.set (claves, PysProductosBean.C_IDPRODUCTO, idProducto);
			UtilidadesHash.set (claves, PysProductosBean.C_IDTIPOPRODUCTO, idTipoProducto);
			UtilidadesHash.set (claves, PysProductosBean.C_IDINSTITUCION, user.getLocation());
			
			Vector productos = productosAdm.select(claves);
			if (productos.size() == 1) {
				PysProductosBean producto = (PysProductosBean) productos.get(0);
				request.getSession().setAttribute("DATABACKUP", producto);
			}
			request.setAttribute("modo", modo);			
		}
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.productos"}, e, null); 
		} 
		return modo;
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#modificar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {

		UserTransaction t = this.getUserBean(request).getTransaction();
		
		try {
			MantenimientoCategoriasProductosForm miForm = (MantenimientoCategoriasProductosForm) formulario;

			PysProductosBean producto = (PysProductosBean) request.getSession().getAttribute("DATABACKUP");
			producto.setDescripcion(miForm.getDescripcionProducto());
			
			PysProductosAdm productosAdm = new PysProductosAdm (this.getUserBean(request));
			t.begin();
			if(productosAdm.update(producto)) {
				t.commit();
			}
			else {
				throwExcp("messages.updated.error",new String[] {"modulo.productos"},new ClsExceptions("messages.updated.error"),t);
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
	protected String insertar(ActionMapping mapping, MasterForm formulario,	HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		UserTransaction t = this.getUserBean(request).getTransaction();
		
		try {
			MantenimientoCategoriasProductosForm miForm = (MantenimientoCategoriasProductosForm) formulario;
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
			
			PysProductosBean producto = new PysProductosBean();
			producto.setDescripcion(miForm.getDescripcionProducto());
			producto.setIdTipoProducto(miForm.getIdTipoProducto());
			producto.setIdInstitucion(new Integer(user.getLocation()));
			
			PysProductosAdm productosAdm = new PysProductosAdm (this.getUserBean(request));
			t.begin();
			if(productosAdm.insert(producto)) {
				t.commit();
			}
			else {
				throwExcp("messages.general.error",new String[] {"modulo.productos"},new ClsExceptions(productosAdm.getError()),t); 
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
			MantenimientoCategoriasProductosForm miForm = (MantenimientoCategoriasProductosForm) formulario;
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
			
			Long 	idProducto 		= new Long 	  ((String)miForm.getDatosTablaOcultos(0).get(0));
			Integer idTipoProducto 	= new Integer ((String)miForm.getDatosTablaOcultos(0).get(1));
			
			PysProductosAdm productosAdm = new PysProductosAdm (this.getUserBean(request));
			Hashtable claves = new Hashtable ();
			UtilidadesHash.set (claves, PysProductosBean.C_IDPRODUCTO, idProducto);
			UtilidadesHash.set (claves, PysProductosBean.C_IDTIPOPRODUCTO, idTipoProducto);
			UtilidadesHash.set (claves, PysProductosBean.C_IDINSTITUCION, user.getLocation());
			
			t.begin();
			if(productosAdm.delete(claves)) {
				t.commit();
			}
			else {
				throwExcp("messages.deleted.error",new String[] {"modulo.productos"},new ClsExceptions("messages.deleted.error"), t);
			}
		}
		catch (Exception e) { 
			throwExcp("messages.pys.mantenimientoProductos.errorBorrado", new SIGAException("messages.pys.mantenimientoProductos.errorBorrado"), t); 
		}
		return exitoRefresco("messages.deleted.success", request);
	}
	
	void miF() {
		int i = 0;
		i = 1;
		i = i * 10;
	}
}