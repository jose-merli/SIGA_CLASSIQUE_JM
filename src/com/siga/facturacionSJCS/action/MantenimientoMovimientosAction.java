//VERSIONES:
//ruben.fernandez Creacion: 21/03/2005 
//

package com.siga.facturacionSJCS.action;


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
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.FcsMovimientosVariosAdm;
import com.siga.beans.FcsMovimientosVariosBean;
import com.siga.beans.FcsPagosJGBean;
import com.siga.facturacionSJCS.form.MantenimientoMovimientosForm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;



public class MantenimientoMovimientosAction extends MasterAction {

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
		try {
			// Si vengo desde la ficha colegial
			if (mapping.getParameter() != null && mapping.getParameter().toUpperCase().contains(ClsConstants.PARAM_ESFICHACOLEGIAL.toUpperCase())) {
				return this.buscarPor(mapping, formulario, request,response);
			}

			//Recogemos de sesion el UsrBean
			UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
		}
		//mapping normal
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
	
		String destino = "error";
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		MantenimientoMovimientosForm miform = (MantenimientoMovimientosForm)formulario;
		Vector ocultos = (Vector)miform.getDatosTablaOcultos(0);
		FcsMovimientosVariosAdm movimAdm = new FcsMovimientosVariosAdm (this.getUserBean(request));
		try {
			
			String consulta =	" select m.idmovimiento idmovimiento, m.idinstitucion idinstitucion, m.descripcion descripcion, m.motivo motivo,"+
								" m.cantidad cantidad, (p.nombre||' '||p.apellidos1||' '||p.apellidos2) nombre, p.nifcif nif, c.ncolegiado ncolegiado, m.idpersona idpersona, m.idpagosjg idpago, "+
								//Numero de Colegiado o Comunitario segun proceda:
								" (CASE c."+CenColegiadoBean.C_COMUNITARIO+" WHEN '"+ClsConstants.DB_FALSE+"' THEN c."+CenColegiadoBean.C_NCOLEGIADO+" ELSE c."+CenColegiadoBean.C_NCOMUNITARIO+" END ) AS NUMERO "+
								" from "+ FcsMovimientosVariosBean.T_NOMBRETABLA +" m,"+ CenPersonaBean.T_NOMBRETABLA +" p, "+ CenColegiadoBean.T_NOMBRETABLA +" c"+
								" where p."+ CenPersonaBean.C_IDPERSONA + " = m."+ FcsMovimientosVariosBean.C_IDPERSONA +
								" and c."+ CenColegiadoBean.C_IDINSTITUCION + " (+) = m."+ FcsMovimientosVariosBean.C_IDINSTITUCION +
								" and c."+ CenColegiadoBean.C_IDPERSONA + " (+) = m."+ FcsMovimientosVariosBean.C_IDPERSONA +
								" and m."+ FcsMovimientosVariosBean.C_IDINSTITUCION +" = "+usr.getLocation()+ 
								" and m."+ FcsMovimientosVariosBean.C_IDMOVIMIENTO +" = "+(String)ocultos.get(0)+ " ";
			
			Hashtable resultado = new Hashtable(); 
			Vector v = (Vector) movimAdm.selectGenerico(consulta);
			if (v!=null && v.size()>0)  {
				resultado = (Hashtable)v.get(0);
			}		
			request.getSession().setAttribute("resultado",resultado);
			request.getSession().setAttribute("modo","edicion");
			destino = "nuevo";
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
		}
		
		return destino;
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
	
		String destino = "error";
		boolean ok = true;
		
		//recogemos el formulario
		MantenimientoMovimientosForm miform = (MantenimientoMovimientosForm)formulario;
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		UserTransaction tx = (UserTransaction)usr.getTransaction();
		
		//preparamos el nuevo registro
		Hashtable movimientoNew = new Hashtable();
		UtilidadesHash.set( movimientoNew, FcsMovimientosVariosBean.C_IDINSTITUCION, (String)usr.getLocation());
		//UtilidadesHash.set( movimientoNew, FcsMovimientosVariosBean.C_IDMOVIMIENTO, );
		UtilidadesHash.set( movimientoNew, FcsMovimientosVariosBean.C_IDPAGOSJG, (String)miform.getIdPagoJg());
		UtilidadesHash.set( movimientoNew, FcsMovimientosVariosBean.C_IDPERSONA, (String)miform.getIdPersona());
		UtilidadesHash.set( movimientoNew, FcsMovimientosVariosBean.C_DESCRIPCION, (String)miform.getDescripcion());
		UtilidadesHash.set( movimientoNew, FcsMovimientosVariosBean.C_MOTIVO, (String)miform.getMotivo());
		UtilidadesHash.set( movimientoNew, FcsMovimientosVariosBean.C_FECHAALTA, "sysdate");
		UtilidadesHash.set( movimientoNew, FcsMovimientosVariosBean.C_CANTIDAD, (String)miform.getCantidad());
		UtilidadesHash.set( movimientoNew, FcsMovimientosVariosBean.C_FECHAMODIFICACION, "sysdate");
		UtilidadesHash.set( movimientoNew, FcsMovimientosVariosBean.C_USUMODIFICACION, (String)usr.getUserName());
		UtilidadesHash.set( movimientoNew, FcsMovimientosVariosBean.C_CONTABILIZADO, "0");
		try {
			//consultamos el valor que falta, el idMovimiento
			FcsMovimientosVariosAdm movimientosAdm = new FcsMovimientosVariosAdm (this.getUserBean(request));
			movimientoNew = movimientosAdm.prepararInsert(movimientoNew);
		
			tx.begin();
			
			//insertar el nuevo registro
			ok = movimientosAdm.insert(movimientoNew);
	
			if (ok) {
				tx.commit();
			} else {
				tx.rollback();
			}
			
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,tx);
		}
		if (ok) return exitoModal("messages.inserted.success",request);
		else return exitoModal("messages.inserted.error",request);
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
	
		//variables
		String destino = "error";
		boolean ok = false;
		FcsMovimientosVariosAdm movimAdm = new FcsMovimientosVariosAdm ( this.getUserBean(request));
		
		//recoger formulario
		MantenimientoMovimientosForm miform = (MantenimientoMovimientosForm)formulario;

		//recoger el UsrBean
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		UserTransaction tx = (UserTransaction)usr.getTransaction();
		try {
			//recogemos el movimiento que est� en BBDD
			String where = " where " + FcsMovimientosVariosBean.C_IDINSTITUCION + "=" + usr.getLocation() +
							" and " + FcsMovimientosVariosBean.C_IDMOVIMIENTO + "=" +  (String)miform.getIdMovimiento() + " ";
			FcsMovimientosVariosBean movimBd = (FcsMovimientosVariosBean)((Vector)movimAdm.select (where)).get(0);
			
			//lo pasamos a HAshtable
			Hashtable movimOld = (Hashtable)movimBd.getOriginalHash().clone();
			
			//preparamos el nuevo HAshtable
			Hashtable movimNew = (Hashtable)movimOld.clone();
			UtilidadesHash.set( movimNew, FcsMovimientosVariosBean.C_CANTIDAD, (String)miform.getCantidad());
			UtilidadesHash.set( movimNew, FcsMovimientosVariosBean.C_DESCRIPCION, (String)miform.getDescripcion());
			UtilidadesHash.set( movimNew, FcsMovimientosVariosBean.C_MOTIVO, (String)miform.getMotivo());
			UtilidadesHash.set( movimNew, FcsMovimientosVariosBean.C_IDPERSONA, (String)miform.getIdPersona());
			UtilidadesHash.set( movimNew, FcsMovimientosVariosBean.C_USUMODIFICACION, (String)usr.getUserName());
			UtilidadesHash.set( movimNew, FcsMovimientosVariosBean.C_IDPAGOSJG, (String)miform.getIdPagoJg());
			UtilidadesHash.set( movimNew, FcsMovimientosVariosBean.C_FECHAMODIFICACION, "sysdate");

			//modificamos
			tx.begin();
			
			//insertar el nuevo registro
			ok = movimAdm.update(movimNew, movimOld);
	
			if (ok) {
				tx.commit();
			} else {
				tx.rollback();
			}
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,tx);
		}
		if (ok) return exitoModal("messages.updated.success", request);
		else return exitoModalSinRefresco("messages.updated.error", request);

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
	
		String destino = "error";
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		MantenimientoMovimientosForm miform = (MantenimientoMovimientosForm)formulario;
		Vector ocultos = (Vector)miform.getDatosTablaOcultos(0);
		FcsMovimientosVariosAdm movimAdm = new FcsMovimientosVariosAdm (this.getUserBean(request));
		String idMovim = (String)ocultos.get(0);
		
		try {
			
			String consulta =	" select m.idmovimiento idmovimiento, m.idinstitucion idinstitucion, m.descripcion descripcion, m.motivo motivo,"+
								" m.cantidad cantidad, (p.nombre||' '||p.apellidos1||' '||p.apellidos2) nombre, p.nifcif nif, c.ncolegiado ncolegiado, m.idpersona idpersona, pag.nombre pago, m.idpagosjg idpago, "+
								//Numero de Colegiado o Comunitario segun proceda:
								" (CASE c."+CenColegiadoBean.C_COMUNITARIO+" WHEN '"+ClsConstants.DB_FALSE+"' THEN c."+CenColegiadoBean.C_NCOLEGIADO+" ELSE c."+CenColegiadoBean.C_NCOMUNITARIO+" END ) AS NUMERO "+
								" from "+ FcsMovimientosVariosBean.T_NOMBRETABLA +" m,"+ CenPersonaBean.T_NOMBRETABLA +" p, "+ CenColegiadoBean.T_NOMBRETABLA +" c,"+ FcsPagosJGBean.T_NOMBRETABLA + " pag" +
								" where p."+ CenPersonaBean.C_IDPERSONA + " = m."+ FcsMovimientosVariosBean.C_IDPERSONA +
								" and c."+ CenColegiadoBean.C_IDINSTITUCION + " (+) = m."+ FcsMovimientosVariosBean.C_IDINSTITUCION +
								" and c."+ CenColegiadoBean.C_IDPERSONA + " (+) = m."+ FcsMovimientosVariosBean.C_IDPERSONA +
								" and pag." + FcsPagosJGBean.C_IDINSTITUCION + "(+)= m."+ FcsMovimientosVariosBean.C_IDINSTITUCION +
								" and pag." + FcsPagosJGBean.C_IDPAGOSJG + "(+)= m."+ FcsMovimientosVariosBean.C_IDPAGOSJG +
								" and m."+ FcsMovimientosVariosBean.C_IDINSTITUCION + " = " + usr.getLocation() + 
								" and m."+ FcsMovimientosVariosBean.C_IDMOVIMIENTO + " = " + idMovim + " ";
			
			Hashtable resultado = (Hashtable)((Vector)movimAdm.selectGenerico(consulta)).get(0);
			request.getSession().setAttribute("resultado",resultado);
			request.getSession().setAttribute("modo","consulta");
			destino = "nuevo";
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
		}
		
		return destino;
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
		return "nuevo";
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

		String destino = "error";
		try {
		 	// obtener institucion
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
	
			// recoger el fomulario
			MantenimientoMovimientosForm miform = (MantenimientoMovimientosForm)formulario;
			Hashtable datos = (Hashtable)miform.getDatos();
			//falta la institucion
			datos.put("IDINSTITUCION",(String)usr.getLocation());

			// Si vengo desde la ficha colegial
			if (mapping.getParameter() != null && mapping.getParameter().toUpperCase().contains(ClsConstants.PARAM_ESFICHACOLEGIAL.toUpperCase())) {
				String idPersona = (String)request.getParameter("idPersonaPestanha");
				UtilidadesHash.set(datos, "IDPERSONA", idPersona);
			}
			//Si no se viene de ficha colegial y se ha pulsado el bot�n buscar, no se tiene 
			//en cuenta el IDPERSONA 
			else if ( "si".equals(request.getParameter("botonBuscarPulsado")) ){
				UtilidadesHash.set(datos, "IDPERSONA", "");
			}

			//consulta a BBDD
			FcsMovimientosVariosAdm movimAdm = new FcsMovimientosVariosAdm (this.getUserBean(request));


			// ---------------------
			// Antes: 
			// String consulta = (String)movimAdm.consultaBusqueda(datos);
			// Vector resultado = (Vector)movimAdm.selectGenerico(consulta);
			// Ahora:
			Vector resultado = movimAdm.consultaBusqueda(datos);
			// ---------------------
				
			
			//pasar el par�metro por request
			request.setAttribute("resultado",resultado);
			
			//el mapping correcto
			destino = "resultado";
			
			//para que cuando vuelva a la p�gina de busqueda sepa que tiene que hacer la busqueda de nuevo
			miform.setBuscar("si");
			request.getSession().setAttribute("MantenimientoMovimientosForm",miform);
			
	     } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,null);
	   	 }
		 return destino;
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
		
		//variables
		String result="error";
		boolean ok = false;
		FcsMovimientosVariosAdm movimAdm = new FcsMovimientosVariosAdm (this.getUserBean(request));
		//recogemos el usrbean
		UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
		//recogemos el formulario
		MantenimientoMovimientosForm miform = (MantenimientoMovimientosForm)formulario;
		//recogemos los campos ocultos
		Vector ocultos = (Vector)miform.getDatosTablaOcultos(0); 
		UserTransaction tx = (UserTransaction)usr.getTransaction();
		try{
			//preparamos la hashtable a borrar
			Hashtable aBorrar = new Hashtable();
			UtilidadesHash.set( aBorrar, FcsMovimientosVariosBean.C_IDINSTITUCION, usr.getLocation());
			UtilidadesHash.set( aBorrar, FcsMovimientosVariosBean.C_IDMOVIMIENTO, (String)ocultos.get(0));
			//borramos
			tx.begin();
			
			//insertar el nuevo registro
			ok = movimAdm.delete( aBorrar);
	
			if (ok) {
				tx.commit();
			} else {
				tx.rollback();
			}			
			
		 } 	
		 catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.facturacionSJCS"},e,tx);
	   	 }
		 if (ok)return exitoRefresco("messages.deleted.success", request);
		 else return exitoRefresco("messages.deleted.error",request);
	}

}
