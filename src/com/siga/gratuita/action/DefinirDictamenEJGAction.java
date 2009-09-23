//Clase: DefinirRatificacionEJGAction 
//Autor: julio.vicente@atosorigin.com
//Ultima modificación: 17/02/2005

package com.siga.gratuita.action;

import javax.servlet.http.*;
import javax.transaction.UserTransaction;

import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.*;
import com.atos.utils.*;

import org.apache.struts.action.*;

import java.util.*;

import com.siga.beans.*;

/**
* Maneja las acciones que se pueden realizar sobre la tabla SCS_SOJ
*/
public class DefinirDictamenEJGAction extends MasterAction {
	
	protected ActionForward executeInternal(ActionMapping mapping,
		      ActionForm formulario,
		      HttpServletRequest request, 
		      HttpServletResponse response) throws SIGAException {
		
		MasterForm miForm = (MasterForm) formulario;
		if (miForm == null)
			try {
				return mapping.findForward(this.abrir(mapping, miForm, request, response));
			}catch(Exception e){
				return mapping.findForward("exception");
			}
		else return super.executeInternal(mapping, formulario, request, response); 
	}
	
	/**
	 * No implementado 
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {		
		
		return null;
	}

	/**
	 * No implementado 
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
				
		return null;
	
	}

	/** 
	 * No implementado
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {		

		return null;
	}

	/**
	 * No implementado 
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		return null;
	}

	/**
	 * No implementado 
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		return null;
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
	
		Vector v = new Vector ();
		Hashtable nuevos = new Hashtable();		
		UserTransaction tx=null;
		
		try {
			
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
			DefinirDictamenEJGForm miForm = (DefinirDictamenEJGForm)formulario;		
			nuevos = miForm.getDatos();			
			ScsEJGAdm admEJG = new ScsEJGAdm(this.getUserBean(request));
		
			if (!nuevos.get("FECHADICTAMEN").equals("")) {
				// Ponemos la fecha en el formato correcto
				nuevos.put("FECHADICTAMEN", GstDate.getApplicationFormatDate("",nuevos.get("FECHADICTAMEN").toString()));
			} else {
				// Ponemos la fecha en el formato correcto
				nuevos.put("FECHADICTAMEN", nuevos.get("FECHADICTAMEN").toString());
			}
			// Actualizamos en la base de datos
			tx=usr.getTransaction();
			tx.begin();
			admEJG.update(nuevos,(Hashtable)request.getSession().getAttribute("DATABACKUP"));
			tx.commit();
			// En DATABACKUP almacenamos los datos más recientes por si se vuelve a actualizar seguidamente
			nuevos.put("FECHAMODIFICACION", "sysdate");
			request.getSession().setAttribute("DATABACKUP",nuevos);			
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}
		
		return exitoRefresco("messages.updated.success",request);
	}

	/**
	 * No implementado 
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
  
		return null;
	}

	/** 
	 * No implementado
	 */	
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		return null;		
	}

	
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		/* "DATABACKUP" se usa habitualmente así que en primer lugar borramos esta variable */		
		request.getSession().removeAttribute("DATABACKUP");
		
		
		Vector v = new Vector ();
		Hashtable miHash = new Hashtable();		
		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");		
		
		miHash.put("ANIO",request.getParameter("ANIO").toString());
		miHash.put("NUMERO",request.getParameter("NUMERO").toString());
		miHash.put("IDTIPOEJG",request.getParameter("IDTIPOEJG").toString());
		miHash.put("IDINSTITUCION",usr.getLocation().toString());	
		
		ScsEJGAdm admEJG = new ScsEJGAdm(this.getUserBean(request));
		
		try {			
			v = admEJG.selectPorClave(miHash);
			request.getSession().setAttribute("DATABACKUP",admEJG.beanToHashTable((ScsEJGBean)v.get(0)));			
		} catch (Exception e) {
			   throwExcp("messages.general.error",e,null);
		}
		
		return "inicio";		
	}
	
	/** 
	 * No implementado
	 */
	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		return null;
	}
}