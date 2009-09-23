package com.siga.gratuita.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ComodinBusquedas;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.beans.*;
import com.siga.general.*;
import com.siga.gratuita.form.*;


//Clase: DefinirGrupoFacturacionAcion 
//Autor: julio.vicente@atosorigin.com
//Ultima modificación: 08/04/2005
/**
* Maneja las acciones que se pueden realizar sobre la tabla SCS_GRUPOFACTURACION
*/
public class DefinirGrupoFacturacionAction extends MasterAction {	
	
	/**
	 * 	Not implemented 
	 */
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {		
				
		return null;
	}

	/**
	 * Rellena un hash con los valores recogidos del formulario, almacenando esta hash en la sesión con el nombre "elegido"
	 *
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la información. De tipo MasterForm.
	 * @param request Información de sesión. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		try {
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
			Vector ocultos = formulario.getDatosTablaOcultos(0);		
			ScsGrupoFacturacionAdm admBean =  new ScsGrupoFacturacionAdm(this.getUserBean(request));		
			Hashtable miHash = new Hashtable();
			miHash.put(ScsGrupoFacturacionBean.C_IDGRUPOFACTURACION,(ocultos.get(0)));
			miHash.put(ScsGrupoFacturacionBean.C_IDINSTITUCION,usr.getLocation());
			// Volvemos a obtener de base de datos la información, para que se la más actúal que hay en la base de datos
			Vector resultado = admBean.selectPorClave(miHash);
			ScsGrupoFacturacionBean fiesta = (ScsGrupoFacturacionBean)resultado.get(0);		
			miHash.clear();		
			miHash = fiesta.getOriginalHash();
			request.setAttribute("accion","modificar");
			request.setAttribute("DATABACKUP",miHash);
			request.getSession().setAttribute("DATABACKUP",miHash);		
		}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.SJCS"},e,null);			
		}
			
		return "operar";
	
	}

	/** 
	 * No implementado
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {		
		return null;
	}

	/**
	 * Rellena el string que indica la acción a llevar a cabo 
	 * 
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la información. De tipo MasterForm.
	 * @param request Información de sesión. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
			
		request.setAttribute("accion","insertar");
		return "operar";
	}

	/**
	 * Rellena un hash con los valores recogidos del formulario y los inserta en la base de datos.
	 * 
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la información. De tipo MasterForm.
	 * @param request Información de sesión. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		UserTransaction tx=null;
		
		DefinirGrupoFacturacionForm miForm = (DefinirGrupoFacturacionForm) formulario;		
		ScsGrupoFacturacionAdm admBean =  new ScsGrupoFacturacionAdm(this.getUserBean(request));
		Hashtable miHash = new Hashtable();		
		
		try {					
			miHash = miForm.getDatos();
			miHash.put(ScsGrupoFacturacionBean.C_IDINSTITUCION,usr.getLocation());
			tx=usr.getTransaction();
			admBean.prepararInsert(miHash);
			tx.begin();
			try {
				admBean.insert(miHash);
			} catch (Exception e){
				tx.rollback();
				return exitoModalSinRefresco("messages.inserted.error",request);
			}
			tx.commit();
			miHash.remove(ScsGrupoFacturacionBean.C_FECHAMODIFICACION);
			request.getSession().setAttribute("DATABACKUP",miHash);
		}catch(Exception e){
			
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}
		return exitoModal("messages.inserted.success",request);		
	}

	/**
	 * Rellena un hash con los valores recogidos del formulario y los modifica en la base de datos.
	 * 
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la información. De tipo MasterForm.
	 * @param request Información de sesión. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
	
		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		UserTransaction tx=null;
				
		DefinirGrupoFacturacionForm miForm = (DefinirGrupoFacturacionForm) formulario;		
		ScsGrupoFacturacionAdm admBean =  new ScsGrupoFacturacionAdm(this.getUserBean(request));
		String forward = "";
		Hashtable hashBkp = new Hashtable();			
		
		try {				
			hashBkp = (Hashtable)request.getSession().getAttribute("DATABACKUP");						
			tx=usr.getTransaction();
			tx.begin();
			try {
				admBean.update(miForm.getDatos(),hashBkp);
			} catch (Exception e){
				return exitoModalSinRefresco("messages.updated.error",request);
			}
			request.getSession().removeAttribute("DATABACKUP");
			tx.commit();
			Hashtable miHash = miForm.getDatos();
			miHash.remove(ScsGrupoFacturacionBean.C_FECHAMODIFICACION);
			request.getSession().setAttribute("DATABACKUP",miHash);
		}catch(Exception e){
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}
		return exitoModal("messages.updated.success",request);		
	}

	/**
	 * Rellena un hash con los valores recogidos del formulario y los borra de la base de datos.
	 * 
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la información. De tipo MasterForm.
	 * @param request Información de sesión. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		UserTransaction tx=null;
		
		Vector visibles = formulario.getDatosTablaVisibles(0);
		Vector ocultos = formulario.getDatosTablaOcultos(0);			
		ScsGrupoFacturacionAdm admBean =  new ScsGrupoFacturacionAdm(this.getUserBean(request));
		String forward = "";
		Hashtable miHash = new Hashtable();
		
		try {				
			miHash.put(ScsGrupoFacturacionBean.C_IDGRUPOFACTURACION,(ocultos.get(0)));
			miHash.put(ScsGrupoFacturacionBean.C_IDINSTITUCION,usr.getLocation());										
			
			tx=usr.getTransaction();
			tx.begin();
			try { 
				admBean.delete(miHash);
			} catch (Exception e){
				return exito("messages.deleted.error",request);
			}
			tx.commit();
		}catch(Exception e){
			throwExcp("messages.gratuita.error.eliminarGruposFacturacion",new String[] {"modulo.SJCS"},e,tx);
		}
		return exitoRefresco("messages.deleted.success",request);		
	}

	/** 
	 * No implementado
	 */	
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		ScsGrupoFacturacionAdm admBean =  new ScsGrupoFacturacionAdm(this.getUserBean(request));
		DefinirGrupoFacturacionForm miForm = (DefinirGrupoFacturacionForm)formulario;
		Vector v = new Vector ();
		
		String consulta = "SELECT IDGRUPOFACTURACION, " + UtilidadesMultidioma.getCampoMultidioma("NOMBRE", this.getUserBean(request).getLanguage()) + ", IDINSTITUCION FROM " + ScsGrupoFacturacionBean.T_NOMBRETABLA + " WHERE " + ScsGrupoFacturacionBean.C_IDINSTITUCION + " = " + usr.getLocation();
		
		// Ahora se anhade el criterio de búsqueda
		if ((miForm.getNombre()!= null) && (!miForm.getNombre().toString().equals(""))) consulta += " and "+ComodinBusquedas.prepararSentenciaCompleta(miForm.getNombre().trim(),ScsGrupoFacturacionBean.C_NOMBRE  );
		
		// Y por último el criterio de ordenación
		consulta += " ORDER BY UPPER(" + ScsGrupoFacturacionBean.C_NOMBRE + ")";
		
		try {
			v = admBean.selectGenerico(consulta);
			request.setAttribute("resultado",v);			
		}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.SJCS"},e,null);			
		}		
		return "listarGrupos";		
	}

	/** 
	 * No implementado
	 */
	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		return "inicio";
	}
}