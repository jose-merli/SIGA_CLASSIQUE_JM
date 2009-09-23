//VERSIONES:
//ruben.fernandez Creacion: 21/03/2005 
//

package com.siga.facturacionSJCS.action;


import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.beans.*;
import com.siga.facturacionSJCS.form.MantenimientoTramosLECForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;



public class MantenimientoTramosLECAction extends MasterAction {

	/**
	 * Metodo que implementa el modo abrir
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrir (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException
	{
		return "inicio";
	}


	/**
	 * Metodo que implementa el modo editar 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
	
		FcsTramosLecBean tramoBean = new FcsTramosLecBean();
		try{
			//recogemos el formulario
			MantenimientoTramosLECForm miform = (MantenimientoTramosLECForm)formulario;
			
			//recogemos el campo oculto de fila seleccionada, que será el idTramosLec
			Vector ocultos = (Vector)miform.getDatosTablaOcultos(0);
			String idTramo = (String)ocultos.get(0);
			
			//recogemos los campos visibles
			Vector visibles = (Vector)miform.getDatosTablaVisibles(0); 
			
			//consultamos en bd el registro a editar
			Hashtable tramoOld = new Hashtable();
			FcsTramosLecAdm tramoAdm = new FcsTramosLecAdm(this.getUserBean(request));
			String condicion = " where " + FcsTramosLecBean.C_IDTRAMOLEC + "=" + idTramo + " ";
			tramoBean = (FcsTramosLecBean)((Vector)tramoAdm.select(condicion)).get(0);
			
		}catch(Exception e){
			throwExcp("error.messages.notedited",e,null);
		}
		request.setAttribute("resultado",tramoBean.getOriginalHash());
		//pasamos el databackup para futuras actualizaciones
		request.getSession().setAttribute("DATABACKUP",tramoBean.getOriginalHash());
		return "mantenimiento";
	}

	/**
	 * Metodo que implementa el modo insertar 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
	
		//recogemos el usrBean
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		
		//recogemos el formulario
		MantenimientoTramosLECForm miform = (MantenimientoTramosLECForm)formulario;
		
		//construimos el hash a insertar
		Hashtable tramoNuevo = (Hashtable)miform.getDatos();
		
		//variable para saber si el insert ha ido bien o no
		boolean ok = false;
		
		//administrador de la tabla
		FcsTramosLecAdm tramoAdm = new FcsTramosLecAdm(this.getUserBean(request));
		
		//comprobamos que no se solapen tramos
		boolean solapado = tramoAdm.solapadoCon((String)miform.getMinimo(), (String)miform.getMaximo(), "-1"); 
		if (solapado) throwExcp("messages.factSJCS.error.solapamientoRango", new SIGAException("messages.factSJCS.error.solapamientoRango") , null);
		
		try {
			//preparamos la hashtable
			tramoNuevo = (Hashtable)tramoAdm.prepararInsert(tramoNuevo);
		}catch(Exception e){
			//si no se ha podido calcular el nuevo identificador para ese tramo,
			//lanzamos la excepcion, porque de todas formas fallaría al insertar
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
		}
		
		try{
			//insertamos
			ok = tramoAdm.insert(tramoNuevo);
		}
		catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
		}
		//devolvemos el mapping dependiendo de si todo ha ido bien o no
		if (ok) return exitoModal("messages.inserted.success",request);
		else return exitoModalSinRefresco("messages.inserted.error",request);
	}

		
	/**
	 * Metodo que implementa el modo modificar 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
	
		Hashtable tramoOld = new Hashtable();
		FcsTramosLecBean tramoBean = new FcsTramosLecBean();
		FcsTramosLecAdm tramoAdm = null;
		//variable para saber si el modificado ha ido bien o no
		boolean ok = false;
		
		try{
			//recogemos el formulario
			MantenimientoTramosLECForm miform = (MantenimientoTramosLECForm)formulario;
			
			//recogemos el idTramosLec
			String idTramo = (String)miform.getIdTramo();
			
			//recogemos de session el registro a modificar
			tramoAdm = new FcsTramosLecAdm(this.getUserBean(request));
			
			tramoOld = (Hashtable)request.getSession().getAttribute("DATABACKUP"); 
			//tramoOld = (Hashtable)tramoBean.getOriginalHash().clone();
		
			//comprobamos que no se solapen tramos
			boolean solapado = tramoAdm.solapadoCon((String)miform.getMinimo(), (String)miform.getMaximo(), idTramo); 
			if (solapado) throwExcp("messages.factSJCS.error.solapamientoRango", new SIGAException("messages.factSJCS.error.solapamientoRango") , null);
			
			
			//construimos el nuevo tramo
			Hashtable tramoNew = (Hashtable)tramoOld.clone();
			tramoNew.put(FcsTramosLecBean.C_MINIMOSMI, (String)miform.getMinimo());
			tramoNew.put(FcsTramosLecBean.C_MAXIMOSMI, (String)miform.getMaximo());
			tramoNew.put(FcsTramosLecBean.C_RETENCION, (String)miform.getRetencion());
		
		//modificamos en bd
			ok = tramoAdm.update(tramoNew, tramoOld);
		}catch(Exception e){
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
		}
		if (ok) return exitoModal("messages.updated.success",request);
		else throw new SIGAException (tramoAdm.getError());
	}


	/**
	 * Metodo que implementa el modo ver 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
	
		return mapSinDesarrollar;
	}



	/**
	 * Metodo que implementa el modo nuevo 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		request.getSession().setAttribute("modo","nuevo");
		return "mantenimiento";
	}

	/**
	 * Metodo que implementa el modo buscarPor 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		//	Recogemos de sesion el UsrBean
		UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
		MantenimientoTramosLECForm miForm = (MantenimientoTramosLECForm)formulario;
		
		try {
			//hacemos la consulta de los tramosLec
			FcsTramosLecAdm tramosAdm = new FcsTramosLecAdm (this.getUserBean(request));
			
			String condicion = " SELECT * FROM " + FcsTramosLecBean.T_NOMBRETABLA;
			
			// Ahora se anhade el criterio de búsqueda
			if ((miForm.getRetencion()!= null) && (!miForm.getRetencion().toString().equals(""))) condicion += " WHERE " + FcsTramosLecBean.C_RETENCION + " = " + miForm.getRetencion();
			
			// Ahora se anhade el criterio de ordenacion
			condicion += " ORDER BY " + FcsTramosLecBean.C_RETENCION;
			
			Vector resultado = (Vector)tramosAdm.selectGenerico(condicion);
			
			//pasamos los resultados por la request
			request.setAttribute("resultado",resultado);
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
		}
		//mapping normal
		return "listarLEC";
	}

	
	/** 
	 *  Funcion que implementa la accion borrar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		//recogemos el formulario
		MantenimientoTramosLECForm miform = (MantenimientoTramosLECForm)formulario;
		
		//recogemos la fila seleccionada para borrar
		Vector visibles = (Vector)miform.getDatosTablaVisibles(0);
		Vector ocultos = (Vector)miform.getDatosTablaOcultos(0);
		
		//construimos la hash del registro a borrar
		Hashtable aBorrar = new Hashtable();
		aBorrar.put(FcsTramosLecBean.C_IDTRAMOLEC,(String)ocultos.get(0));
		
		//variable para saber si el borrado ha ido bien
		boolean ok = false;
		
		try
		{
			//borramos
			FcsTramosLecAdm tramosAdm = new FcsTramosLecAdm (this.getUserBean(request));
			ok = tramosAdm.delete(aBorrar);
		}
		catch(Exception e){
		    return exitoRefresco("messages.deleted.error",request);
//			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
		}
		//si ha ido bien refrescamos y devolvemos un mensage de exito
		if (ok) return exitoRefresco("messages.deleted.success",request);
		//si ha fallado habría saltado el thowExcp, pero en todo caso , sino ha saltado el error porque no ha encontrado el idTramo
		//devolvemos mensage de que no se ha borrado
		else return exitoRefresco("messages.deleted.error",request);
		
	}

}
