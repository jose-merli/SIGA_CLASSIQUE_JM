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
			Vector Vsufijos = sufijoAdm.consultaBusqueda(user.getLocation(),miForm.getSufijo(), miForm.getConcepto());

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
			
			String sufijo = (String)miForm.getDatosTablaOcultos(0).get(1);
			
			FacSufijoAdm sufijoAdm = new FacSufijoAdm (this.getUserBean(request));
			Hashtable claves = new Hashtable ();
			UtilidadesHash.set (claves, FacSufijoBean.C_IDINSTITUCION, user.getLocation());
			UtilidadesHash.set (claves, FacSufijoBean.C_SUFIJO, sufijo);
						
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
			//CHECKBOX:
			String checkBox = ClsConstants.DB_FALSE;
			if (miForm.getVarios()!=null && miForm.getVarios().equals(ClsConstants.DB_TRUE)){
				// Comprobamos que para la institucion en la que nos encontramos no existe ya un sufijo distinto del seleccionado con el check de conceptos varios activado
				
				where=" WHERE "+FacSufijoBean.C_IDINSTITUCION+"="+(String)user.getLocation()+
				      " AND "+FacSufijoBean.C_VARIOS+"='"+ClsConstants.DB_TRUE+"'"+
					  " AND "+FacSufijoBean.C_SUFIJO+"<>'"+miForm.getSufijo()+"'";
					  
				Vector existeCheckVarios=sufijoAdm.select(where);
				if (existeCheckVarios.size()>0){
					throw new SIGAException("facturacion.sufijos.literal.checkVarios");
				}else{
				  checkBox = ClsConstants.DB_TRUE;
				}
				
			}	
			sufijoBean.setVarios(checkBox);
			
			
			tx.begin();
			sufijoAdm.update(sufijoBean);
			tx.commit();
		}
		catch (SIGAException e) {
			throw e;
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
			//CHECKBOX:
			String checkBox = ClsConstants.DB_FALSE;
			if (miForm.getVarios()!=null && miForm.getVarios().equals(ClsConstants.DB_TRUE)){
				// Comprobamos que para la institucion en la que nos encontramos no existe ya un sufijo creado con el check de conceptos varios activado
				
				Hashtable datosConcepto=new Hashtable();
				datosConcepto.put(FacSufijoBean.C_IDINSTITUCION,(String)user.getLocation());
				datosConcepto.put(FacSufijoBean.C_VARIOS,ClsConstants.DB_TRUE);
				Vector existeCheckVarios=sufijoAdm.select(datosConcepto);
				if (existeCheckVarios.size()>0){
					throw new SIGAException("facturacion.sufijos.literal.checkVarios");
				}else{
				  checkBox = ClsConstants.DB_TRUE;
				}
				
			}
			
			//Se comprueba si el sufijo es numérico y de tres caracteres
			try { 
				
				Integer.parseInt(miForm.getSufijo()); 
				
				if(miForm.getSufijo().length()!=3)
					throw new SIGAException("facturacion.mensajes.sufijoNuevo");
	
			} catch (NumberFormatException exc) {

				throw new SIGAException("facturacion.mensajes.sufijoNuevo");
			} 

			
			sufijoBean.setVarios(checkBox);
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
			
			String sufijo 	= (String)miForm.getDatosTablaOcultos(0).get(1);
			
			FacSufijoAdm sufijoAdm = new FacSufijoAdm (this.getUserBean(request));
			Hashtable claves = new Hashtable ();
			UtilidadesHash.set (claves, FacSufijoBean.C_IDINSTITUCION, user.getLocation());
			UtilidadesHash.set (claves, FacSufijoBean.C_SUFIJO, sufijo);
			
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