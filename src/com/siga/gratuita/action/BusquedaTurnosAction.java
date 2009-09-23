package com.siga.gratuita.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMapping;
import com.atos.utils.ClsExceptions;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;


/**
 * @author ruben.fernandez
 */

public class BusquedaTurnosAction extends MasterAction {

	protected String editar(ActionMapping mapping, MasterForm formulario,HttpServletRequest request,HttpServletResponse response) throws ClsExceptions {
		return null;
	}
	protected String ver(	ActionMapping mapping, MasterForm formulario,HttpServletRequest request,HttpServletResponse response) throws ClsExceptions {
		return null;
	}
	
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions {
		return null;
	}
	
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions {
			return null;
	}
	
	protected String modificar(	ActionMapping mapping,MasterForm formulario,HttpServletRequest request,HttpServletResponse response) throws ClsExceptions {
		return null;
	}
	
	protected String borrar(ActionMapping mapping,MasterForm formulario,HttpServletRequest request,HttpServletResponse response) throws ClsExceptions {
		return null;
	}
	
	protected String buscarPor(	ActionMapping mapping,MasterForm formulario,HttpServletRequest request,HttpServletResponse response) throws ClsExceptions {
		return null;	
	}

	/** 
	 *  Funcion que atiende la accion abrir. Por defecto se abre el forward 'inicio'
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions {
		
		String recargar = (String)request.getAttribute("recargar");
		if ((recargar!=null)&&(recargar.equalsIgnoreCase("si"))){
			return this.abrirAvanzada(mapping, formulario, request, response);
		}else{
			request.getSession().removeAttribute("DATABACKUP");
			request.getSession().removeAttribute("DATOSFORMULARIO");
			request.getSession().removeAttribute("BUSQUEDAREALIZADA");
			request.getSession().removeAttribute("accionTurno");
			request.getSession().removeAttribute("pestanas");
			return "inicio";
		}
	}

	/** 
	 *  Funcion que atiende la accion abrirAvanzada
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrirAvanzada (ActionMapping mapping, 		
		  	 MasterForm formulario, 
			 HttpServletRequest request, 
			 HttpServletResponse response) throws ClsExceptions{
			
		//borrar variables de sesión
		request.getSession().removeAttribute("DATABACKUP");
		request.getSession().removeAttribute("accionTurno");
		request.getSession().removeAttribute("pestanas");
		return "inicio";
	}
}