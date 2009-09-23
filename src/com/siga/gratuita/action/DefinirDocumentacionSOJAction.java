//Clase: DefinirDocumentacionEJGAction 
//Autor: julio.vicente@atosorigin.com
//Ultima modificación: 14/02/2005

package com.siga.gratuita.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.*;
import com.siga.beans.*;
import com.siga.general.*;
import com.siga.gratuita.form.DatosDocumentacionSOJForm;

/**
* Maneja las acciones que se pueden realizar sobre la tabla SCS_DOCUMENTACIONSOJ
*/
public class DefinirDocumentacionSOJAction extends MasterAction {	
	
	/** Not implemented **/
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {		
		
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
			Vector ocultos = formulario.getDatosTablaOcultos(0);
			DatosDocumentacionSOJForm miForm = (DatosDocumentacionSOJForm)formulario;
			
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
			
			ScsDocumentacionSOJAdm admBean =  new ScsDocumentacionSOJAdm(this.getUserBean(request));
			//Entramos al formulario en modo 'modificación'
			request.setAttribute("accionModo","editar");
			
			Hashtable miHash = new Hashtable();
			miHash.put(ScsDocumentacionSOJBean.C_IDTIPOSOJ,miForm.getIdTipoSOJ());
			miHash.put(ScsDocumentacionSOJBean.C_IDINSTITUCION,usr.getLocation());
			miHash.put(ScsDocumentacionSOJBean.C_ANIO,miForm.getAnio());
			miHash.put(ScsDocumentacionSOJBean.C_NUMERO,miForm.getNumero());
			miHash.put(ScsDocumentacionSOJBean.C_IDDOCUMENTACION,ocultos.get(0));
			
			// Volvemos a obtener de base de datos la información, para que se la más actúal que hay en la base de datos
			Vector resultado = admBean.selectPorClave(miHash);
			ScsDocumentacionSOJBean ejg = (ScsDocumentacionSOJBean)resultado.get(0);
			
			request.getSession().setAttribute("DATABACKUP",admBean.beanToHashTable(ejg));
		} catch (Exception e) {
			   throwExcp("messages.general.error",e,null);
		}		
		return "editar";
	
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
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {		
		
		try {
			Vector ocultos = formulario.getDatosTablaOcultos(0);
			DatosDocumentacionSOJForm miForm = (DatosDocumentacionSOJForm)formulario;
			
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
			
			ScsDocumentacionSOJAdm admBean =  new ScsDocumentacionSOJAdm(this.getUserBean(request));
			//Entramos al formulario en modo 'modificación'
			request.setAttribute("accionModo","ver");
			
			Hashtable miHash = new Hashtable();
			miHash.put(ScsDocumentacionSOJBean.C_IDTIPOSOJ,miForm.getIdTipoSOJ());
			miHash.put(ScsDocumentacionSOJBean.C_IDINSTITUCION,usr.getLocation());
			miHash.put(ScsDocumentacionSOJBean.C_ANIO,miForm.getAnio());
			miHash.put(ScsDocumentacionSOJBean.C_NUMERO,miForm.getNumero());
			miHash.put(ScsDocumentacionSOJBean.C_IDDOCUMENTACION,ocultos.get(0));
			// Volvemos a obtener de base de datos la información, para que se la más actúal que hay en la base de datos
			Vector resultado = admBean.selectPorClave(miHash);
			ScsDocumentacionSOJBean soj = (ScsDocumentacionSOJBean)resultado.get(0);
			
			request.getSession().setAttribute("DATABACKUP",admBean.beanToHashTable(soj));
		} catch (Exception e) {
			   throwExcp("messages.general.error",e,null);
		}		
		return "ver";
	}

	/**
	 * Rellena el string que indica la acción a llevar a cabo con "

.
.Fiesta" para que redirija a la pantalla de inserción. 
	 * 
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la información. De tipo MasterForm.
	 * @param request Información de sesión. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		DatosDocumentacionSOJForm miForm = (DatosDocumentacionSOJForm) formulario;		
		Hashtable miHash = new Hashtable();
		
		miHash.put("ANIO",formulario.getDatos().get("ANIO"));
		miHash.put("NUMERO",formulario.getDatos().get("NUMERO"));
		miHash.put("IDTIPOSOJ",formulario.getDatos().get("IDTIPOSOJ"));
		
		request.getSession().setAttribute("SOJ",miHash);
		
		return "insertar";
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
		
		DatosDocumentacionSOJForm miForm = (DatosDocumentacionSOJForm) formulario;		
		ScsDocumentacionSOJAdm admBean =  new ScsDocumentacionSOJAdm(this.getUserBean(request));
		String forward="";
		Hashtable miHash = new Hashtable();		
		
		try {					
			miHash = miForm.getDatos();			
			tx=usr.getTransaction();		
			tx.begin();
			admBean.prepararInsert(miHash);
			admBean.insert(miHash);	        
			tx.commit();			
		} catch (Exception e) {
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
	
		UserTransaction tx=null;
		try {			
			DatosDocumentacionSOJForm miForm = (DatosDocumentacionSOJForm) formulario;
			ScsDocumentacionSOJAdm admBean =  new ScsDocumentacionSOJAdm(this.getUserBean(request));
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
			Hashtable nuevos = miForm.getDatos();
			Hashtable clave = new Hashtable();
			
			clave.put("IDINSTITUCION", usr.getLocation());
			clave.put("ANIO", miForm.getAnio());
			clave.put("NUMERO", miForm.getNumero());
			clave.put("IDTIPOSOJ", miForm.getIdTipoSOJ());
			clave.put("IDDOCUMENTACION", miForm.getIdDocumentacion());
			
			// Volvemos a obtener de base de datos la información, para que se la más actúal que hay en la base de datos
			Vector resultado = admBean.selectPorClave(clave);
			ScsDocumentacionSOJBean documentacion = (ScsDocumentacionSOJBean)resultado.get(0);
			tx=usr.getTransaction();
			tx.begin();
			nuevos.put("FECHALIMITE",GstDate.getApplicationFormatDate("",nuevos.get("FECHALIMITE").toString()));
			if ((nuevos.containsKey("FECHAENTREGA")) && (!nuevos.get("FECHAENTREGA").toString().equals(""))) nuevos.put("FECHAENTREGA",GstDate.getApplicationFormatDate("",nuevos.get("FECHAENTREGA").toString()));
			admBean.update(nuevos,admBean.beanToHashTable(documentacion));		    
			tx.commit();
		
		} catch (Exception e) {
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
		
		Vector ocultos = formulario.getDatosTablaOcultos(0);			
		ScsDocumentacionSOJAdm admBean =  new ScsDocumentacionSOJAdm(this.getUserBean(request));
		DatosDocumentacionSOJForm miForm = (DatosDocumentacionSOJForm) formulario; 
		
		Hashtable miHash = new Hashtable();
		
		try {				
			miHash.put(ScsDocumentacionSOJBean.C_IDDOCUMENTACION,(ocultos.get(0)));
			miHash.put(ScsDocumentacionSOJBean.C_IDTIPOSOJ,miForm.getIdTipoSOJ());
			miHash.put(ScsDocumentacionSOJBean.C_ANIO,miForm.getAnio());
			miHash.put(ScsDocumentacionSOJBean.C_NUMERO,miForm.getNumero());
			miHash.put(ScsDocumentacionSOJBean.C_IDINSTITUCION,usr.getLocation());
			
			tx=usr.getTransaction();
			tx.begin();
			admBean.delete(miHash);		    
			tx.commit();
			
		} catch (Exception e) {
			   throwExcp("messages.deleted.error",e,tx);
		}
		
		return exitoRefresco("messages.deleted.success",request);
	}

	/** 
	 * No implementado
	 */	
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return null;		
	}

	
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
				
		/* "DATABACKUP" se usa habitualmente así que en primero lugar borramos esta variable*/		
		request.getSession().removeAttribute("DATABACKUP");		
		
		ScsDocumentacionSOJAdm admBean =  new ScsDocumentacionSOJAdm(this.getUserBean(request));
		Vector v = new Vector ();
		Hashtable miHash = new Hashtable();
		String consulta = "";
		request.getSession().setAttribute("accion",request.getSession().getAttribute("accion"));		
		
		try{
			miHash.put("ANIO",request.getParameter("ANIO").toString());		
			miHash.put("NUMERO",request.getParameter("NUMERO").toString());
			miHash.put("IDTIPOSOJ",request.getParameter("IDTIPOSOJ").toString());
			miHash.put("IDINSTITUCION",request.getParameter("IDINSTITUCION").toString());			
		}
		catch (Exception e){}
		consulta = "SELECT * FROM " + ScsDocumentacionSOJBean.T_NOMBRETABLA + " WHERE ANIO = " + miHash.get("ANIO") + " AND NUMERO = " + miHash.get("NUMERO") + " AND IDTIPOSOJ = " + miHash.get("IDTIPOSOJ") + " AND IDINSTITUCION = " + miHash.get("IDINSTITUCION");
		
		try {			
			v = admBean.selectGenerico(consulta);
			request.setAttribute("resultado",v);			
		} catch (Exception e) {
			   throwExcp("messages.general.error",e,null);
		}		
		return "inicio";
		
	}
	
	/** 
	 * No implementado
	 */
	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		ScsDocumentacionSOJAdm admBean =  new ScsDocumentacionSOJAdm(this.getUserBean(request));
		DatosDocumentacionSOJForm miForm = (DatosDocumentacionSOJForm) formulario;
		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");	
		Vector v = new Vector ();
		Hashtable miHash = new Hashtable();
		String consulta = "";

		miHash.put("ANIO",miForm.getAnio());
		miHash.put("NUMERO",miForm.getNumero());
		miHash.put("IDTIPOSOJ",miForm.getIdTipoSOJ());
		miHash.put("IDINSTITUCION",usr.getLocation());
		
		request.setAttribute("DATOSEJG",miHash);
		
		consulta = "SELECT * FROM " + ScsDocumentacionSOJBean.T_NOMBRETABLA + " WHERE ANIO = " + miHash.get("ANIO") + " AND NUMERO = " + miHash.get("NUMERO") + " AND IDTIPOSOJ = " + miHash.get("IDTIPOSOJ") + " AND IDINSTITUCION = " + miHash.get("IDINSTITUCION");
		
		try {			
			v = admBean.selectGenerico(consulta);			
			request.setAttribute("resultado",v);
		} catch (Exception e) {
			   throwExcp("messages.general.error",e,null);
		}		
		return "inicio";
	}
}