package com.siga.facturacion.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.FacSufijoAdm;
import com.siga.beans.FacSufijoBean;
import com.siga.facturacion.form.SufijosForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 * Action del mantenimiento del caso de uso FAC_SUFIJO.
 * @author david.sanchezp
 * @since 26-10-2005
 */
public class SufijosAction extends MasterAction {
	
	/**
	 * Busca por sufijo y concepto (y por institucion).
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return String
	 * @throws SIGAException
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			SufijosForm	miForm = (SufijosForm) formulario;
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");	
			
			FacSufijoAdm sufijoAdm = new FacSufijoAdm (this.getUserBean(request));
			FacSufijoBean sufijoBean = new FacSufijoBean ();
			Vector Vsufijos;
			
			//Pasamos el campo idSufijo porque es clave y por si estamos buscando un sufijo con espacios para que lo encuentre
			sufijoBean.setIdInstitucion(Integer.parseInt(user.getLocation()));

			if(miForm.getIdSufijo()!=null)
			{	
				sufijoBean.setIdSufijo(miForm.getIdSufijo());
				sufijoBean.setSufijo(miForm.getSufijo());
				sufijoBean.setConcepto(miForm.getConcepto());
				
				Vsufijos = sufijoAdm.consultaBusqueda(sufijoBean);
			}else{
				sufijoBean.setSufijo(miForm.getSufijo());
				sufijoBean.setConcepto(miForm.getConcepto());
				Vsufijos = sufijoAdm.consultaBusqueda(sufijoBean);
			}
			request.setAttribute("Vsufijos", Vsufijos);
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.productos"}, e, null);
		} 
		return "resultado";
	}

	/**
	 * Obtiene los datos a mostrar en la pantalla modal de edicion o consulta.
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return String
	 * @throws SIGAException
	 */
	private String obtenerDatos(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		try {
			SufijosForm miForm = (SufijosForm) formulario;
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
			String idSufijo = (String)miForm.getDatosTablaOcultos(0).get(2).toString();
			
			FacSufijoAdm sufijoAdm = new FacSufijoAdm (this.getUserBean(request));
			Hashtable claves = new Hashtable ();
			UtilidadesHash.set (claves, FacSufijoBean.C_IDINSTITUCION, user.getLocation());
			UtilidadesHash.set (claves, FacSufijoBean.C_IDSUFIJO, idSufijo);
						
			Vector Vsufijos = sufijoAdm.select(claves);
			if (Vsufijos.size() == 1) {
				FacSufijoBean sufijoBean = (FacSufijoBean) Vsufijos.get(0);
				request.getSession().setAttribute("DATABACKUP", sufijoBean);
				request.setAttribute("SUFIJO", sufijoBean);
			}
			
		}
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.productos"}, e, null); 
		} 
		return "editar";
	}

	
	/**
	 * Actualiza el modo a editar y consulta los datos a visualizar.
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return String
	 * @throws SIGAException
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		try {
			request.setAttribute("modo", "editar");			
		}
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.productos"}, e, null); 
		} 
		return this.obtenerDatos(mapping,formulario,request,response);
	}

	/**
	 * Actualiza el modo a ver y consulta los datos a visualizar.
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return String
	 * @throws SIGAException
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			request.setAttribute("modo", "ver");			
		}
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.productos"}, e, null); 
		} 
		return this.obtenerDatos(mapping,formulario,request,response);
	}	
	
	/**
	 * Modifica el registro.
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return String
	 * @throws SIGAException
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UserTransaction tx = null;
		
		try {
			String where="";
			tx = this.getUserBean(request).getTransaction();
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
			SufijosForm miForm = (SufijosForm) formulario;
			FacSufijoAdm sufijoAdm = new FacSufijoAdm (this.getUserBean(request));

			FacSufijoBean sufijoBean = (FacSufijoBean) request.getSession().getAttribute("DATABACKUP");
			sufijoBean.setConcepto(miForm.getConcepto());

			tx.begin();
			sufijoAdm.update(sufijoBean);
			tx.commit();
		}
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.productos"}, e, tx); 
		} 
		return exitoModal("messages.updated.success", request);
	}

	/**
	 * Navegacion a la ventana para crear un nuevo registro.
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return String
	 * @throws SIGAException
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {
			request.setAttribute("modo", "nuevo");			
		}
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.productos"}, e, null); 
		} 
		return "editar";
	}
	
	
	/**
	 * Inserta un nuevo registro.
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return String
	 * @throws SIGAException
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario,	HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UserTransaction tx = null;
		
		try {
			tx = this.getUserBean(request).getTransaction();
			SufijosForm miForm = (SufijosForm) formulario;
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
			
			FacSufijoBean sufijoBean = new FacSufijoBean();
			FacSufijoAdm sufijoAdm = new FacSufijoAdm (this.getUserBean(request));
			sufijoBean.setSufijo(miForm.getSufijo());
			sufijoBean.setConcepto(miForm.getConcepto());
			sufijoBean.setIdInstitucion(new Integer(user.getLocation()));
			
			//Se crea el nuevo idSufijo, si la institución no tiene ninguno será idSufijo=1
			sufijoBean.setIdSufijo(sufijoAdm.idSufijoMaxInstitucion(sufijoBean.getIdInstitucion()));

			//Se comprueba si el sufijo es numérico y de tres caracteres
			try { 
				
				//Se permiten 3 espacios o números
				if((miForm.getSufijo()).trim().length()>0)
					Integer.parseInt(miForm.getSufijo()); 
				
				if(miForm.getSufijo().length()!=3)
					throw new SIGAException("facturacion.mensajes.sufijoNuevo");
	
			} catch (NumberFormatException exc) {

				throw new SIGAException("facturacion.mensajes.sufijoNuevo");
			} 

			tx.begin();
			sufijoAdm.insert(sufijoBean);
			tx.commit();
		}
		catch (Exception e) { 
			throwExcp("messages.general.error", new String[] {"modulo.productos"}, e, tx); 
		} 
		return exitoModal("messages.inserted.success", request);
	}
	
	
	/**
	 * Borra un registro.
	 * @param mapping
	 * @param formulario
	 * @param request
	 * @param response
	 * @return String
	 * @throws SIGAException
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		UserTransaction tx = null;
		
		try {
			tx = this.getUserBean(request).getTransaction();
			SufijosForm miForm = (SufijosForm) formulario;

			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
			
			String idSufijo = (String)miForm.getDatosTablaOcultos(0).get(2).toString();
			FacSufijoAdm sufijoAdm = new FacSufijoAdm (this.getUserBean(request));

			Hashtable claves = new Hashtable ();
			UtilidadesHash.set (claves, FacSufijoBean.C_IDINSTITUCION, user.getLocation());
			UtilidadesHash.set (claves, FacSufijoBean.C_IDSUFIJO, idSufijo);
			tx.begin();
			sufijoAdm.delete(claves);
			tx.commit();
			
		}
		catch (Exception e) { 
			throwExcp("messages.pys.mantenimientoProductos.errorBorrado", new SIGAException("messages.pys.mantenimientoProductos.errorBorrado"), tx); 
		}
		return exitoRefresco("messages.deleted.success", request);
	}

}