package com.siga.gratuita.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.ScsDeclaracionAdm;
import com.siga.beans.ScsDeclaracionBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.MantenimientoDeclaracionForm;

/**
 * @author david.sanchez
 * @since 23/01/2006
 *
 */
public class MantenimientoDeclaracionAction extends MasterAction {
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscarPor(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		try {
			MantenimientoDeclaracionForm miForm = (MantenimientoDeclaracionForm) formulario;
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");	
			
			ScsDeclaracionAdm declaracionAdm = new ScsDeclaracionAdm (this.getUserBean(request));
			
			String descripcion = miForm.getDescripcion();
			String where = " WHERE "+ComodinBusquedas.prepararSentenciaCompleta(descripcion.trim(),ScsDeclaracionBean.C_DESCRIPCION);
			
			Vector vDeclaraciones = declaracionAdm.select(where);

			request.setAttribute("vDeclaraciones", vDeclaraciones);
		}
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, null);
		} 
		return "buscarPor";
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#editar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String obtenerDatos(String modo, MasterForm formulario, HttpServletRequest request) throws SIGAException {
		
		try {
			MantenimientoDeclaracionForm miForm = (MantenimientoDeclaracionForm) formulario;
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
			
			String	idDeclaracion = (String)miForm.getDatosTablaOcultos(0).get(0);
			
			ScsDeclaracionAdm declaracionAdm = new ScsDeclaracionAdm (this.getUserBean(request));

			String where = " WHERE "+ScsDeclaracionBean.C_IDDECLARACION+"="+idDeclaracion;
			
			Vector vDeclaraciones = declaracionAdm.select(where);
			if (vDeclaraciones.size() == 1) {
				Hashtable hashDeclaracion = (Hashtable) vDeclaraciones.get(0);
				
				miForm.setIdDeclaracion((String)hashDeclaracion.get(ScsDeclaracionBean.C_IDDECLARACION));
				miForm.setDescripcion((String)hashDeclaracion.get(ScsDeclaracionBean.C_DESCRIPCION));

				request.getSession().setAttribute("DATABACKUP", hashDeclaracion);
			}
			request.setAttribute("modo", modo);			
		}
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, null); 
		} 
		return "editar";
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#editar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String modo = "editar";
		return this.obtenerDatos(modo,formulario,request);
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#editar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String modo = "ver";
		return this.obtenerDatos(modo,formulario,request);
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#modificar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		UserTransaction tx = null;
		
		try {
			tx = this.getUserBean(request).getTransaction();
			MantenimientoDeclaracionForm miForm = (MantenimientoDeclaracionForm) formulario;
			ScsDeclaracionAdm declaracionAdm = new ScsDeclaracionAdm (this.getUserBean(request));

			Hashtable hashDeclaracionOriginal = (Hashtable)request.getSession().getAttribute("DATABACKUP");
			Hashtable hashDeclaracionModificado = new Hashtable(); 

			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
			
			hashDeclaracionModificado.put(ScsDeclaracionBean.C_DESCRIPCION,miForm.getDescripcion());
			hashDeclaracionModificado.put(ScsDeclaracionBean.C_IDDECLARACION,miForm.getIdDeclaracion());

			tx.begin();
			declaracionAdm.update(hashDeclaracionModificado, hashDeclaracionOriginal);
			tx.commit();
		}
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		} 
		return exitoModal("messages.updated.success", request);
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#nuevo(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String modo = "nuevo";
		try {
			request.setAttribute("modo", modo);			
		}
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, null); 
		} 
		return "editar";
	}
	
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#insertar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario,	HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UserTransaction tx = null;
		
		try {
			tx = this.getUserBean(request).getTransaction();
			MantenimientoDeclaracionForm miForm = (MantenimientoDeclaracionForm) formulario;
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
			
			ScsDeclaracionBean beanDeclaracion = new ScsDeclaracionBean();
			ScsDeclaracionAdm declaracionAdm = new ScsDeclaracionAdm (this.getUserBean(request));
			
			beanDeclaracion.setDescripcion(miForm.getDescripcion());
			beanDeclaracion.setIdDeclaracion(declaracionAdm.getNuevoIdDeclaracion());
			
			tx.begin();
			declaracionAdm.insert(beanDeclaracion);
			tx.commit();
		}
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		} 
		return exitoModal("messages.inserted.success", request);
	}
	
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#borrar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UserTransaction tx = null;
		
		try {
			MantenimientoDeclaracionForm miForm = (MantenimientoDeclaracionForm) formulario;
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
			
			tx = this.getUserBean(request).getTransaction();
						
			Integer idDeclaracion = new Integer ((String)miForm.getDatosTablaOcultos(0).get(0));
			
			ScsDeclaracionAdm declaracionAdm = new ScsDeclaracionAdm(this.getUserBean(request));
			Hashtable claves = new Hashtable ();
			UtilidadesHash.set (claves, ScsDeclaracionBean.C_IDDECLARACION, idDeclaracion);			
			
			tx.begin();
			declaracionAdm.delete(claves);
			tx.commit();
		}
		catch (Exception e) { 
			throwExcp("messages.pys.mantenimientoProductos.errorBorrado", new SIGAException("messages.pys.mantenimientoProductos.errorBorrado"), tx); 
		}
		return exitoRefresco("messages.deleted.success", request);
	}
	
}