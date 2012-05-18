/*
 * VERSIONES:
 * 
 * miguel.villegas - 10-03-2005 - Creacion
 *	
 */

/**
 * Clase action para el mantenimiento de los datos generales de los abonos.<br/>
 * Gestiona la edicion, consulta y mantenimiento de los datos generales  
 */

package com.siga.facturacion.action;

import javax.servlet.http.*;
import javax.transaction.*;

import org.apache.struts.action.*;
import com.atos.utils.*;
import com.siga.beans.*;
import com.siga.general.*;
import com.siga.facturacion.form.AbonosDatosGeneralesForm;
import java.util.*;


public class AbonosDatosGeneralesAction extends MasterAction {

	/** 
	 *  Funcion que atiende la accion abrir.
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result="abrir";
		Vector seleccionados= new Vector();
		Hashtable datosAbono= new Hashtable();

		try{
			// Obtengo el UserBean y los diferentes parametros recibidos
			UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");			
			String accion = (String)request.getParameter("accion");
			String idAbono = (String)request.getParameter("idAbono");
			String idInstitucionAbono = (String)request.getParameter("idInstitucion");
			String volver = null;
			if (request.getParameter("botonVolver")!=null )
				volver = (String)request.getParameter("botonVolver");
			else
				volver = "NO";
			
			String idInstitucion=user.getLocation();


			// Obtengo manejadores para accesos a las BBDDs (cuidado con ls identificadores de usuario)
			FacAbonoAdm abonoAdm = new FacAbonoAdm(this.getUserBean(request));
			FacFacturaAdm facturaAdm = new FacFacturaAdm(this.getUserBean(request));

			seleccionados=abonoAdm.getAbono(idInstitucionAbono,idAbono);
			datosAbono=((Row)seleccionados.firstElement()).getRow();
			
			
			// Paso de parametros empleando request
			request.setAttribute("IDABONO", idAbono);
			request.setAttribute("IDINSTITUCION", idInstitucion);
			request.setAttribute("ACCION", accion);
			request.setAttribute("container", datosAbono);
			request.getSession().setAttribute("DATABACKUP", datosAbono);
			request.setAttribute("volver", volver);		
			String informeUnico = ClsConstants.DB_TRUE;
			
			AdmInformeAdm adm = new AdmInformeAdm(this.getUserBean(request));
			// mostramos la ventana con la pregunta
			
			Vector informeBeans=adm.obtenerInformesTipo(idInstitucion,"ABONO",null, null);
			if(informeBeans!=null && informeBeans.size()>1){
				informeUnico = ClsConstants.DB_FALSE;
				
			}
				
				
			
			request.setAttribute("informeUnico", informeUnico);
			
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,null); 
		}				
		return result;
	}
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrir(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result="abrirAvanzada";

		return result;
		
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		return "buscar";
	}

	/** 
	 *  Funcion que implementa la accion editar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String editar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result="editar";
			
		return (result);
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#ver(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String result="ver";

		return (result);
	}

	/** 
	 *  Funcion que implementa la accion nuevo
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String result="nuevo";
			
		return result;
	}

	/** 
	 *  Funcion que implementa la accion insertar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result="insertar";

		return (result);
	}

	/** 
	 *  Funcion que implementa la accion modificar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result="error";
		UserTransaction tx = null;
		Hashtable hash = new Hashtable();
		Hashtable hashOriginal = new Hashtable();
		
		try {		 				

			// Obtengo usuario y creo manejadores para acceder a las BBDD
			UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			FacAbonoAdm admin=new FacAbonoAdm(this.getUserBean(request));			
 			
			// Obtengo los datos del formulario
			AbonosDatosGeneralesForm miForm = (AbonosDatosGeneralesForm)formulario;

			// Cargo una hastable con los valores originales del registro sobre el que se realizará la modificacion						
			hashOriginal=(Hashtable)request.getSession().getAttribute("DATABACKUP");
			
			// Cargo la tabla hash con los valores del formulario para insertar en la BBDD
			hash=(Hashtable)hashOriginal.clone();
			hash.put(FacAbonoBean.C_MOTIVOS, miForm.getMotivos());									
			hash.put(FacAbonoBean.C_OBSERVACIONES, miForm.getObservaciones());									
			
			// Comienzo control de transacciones
			tx = usr.getTransaction();
			tx.begin();	
			
			if (admin.update(hash,hashOriginal)){
				//vuelvo a dejar en sesion la copia ahora original
				request.getSession().setAttribute("DATABACKUP", hash);
				result=exitoRefresco("messages.updated.success",request);
				tx.commit();
			}	
		} 
		catch (Exception e) { 
			throwExcp("messages.general.error",new String[] {"modulo.facturacion"},e,tx); 
		}
		return (result);		
	}


	/** 
	 *  Funcion que implementa la accion borrar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		String result="borrar";

		return (result);
	}
					
	/** 
	 *  Funcion que implementa la accion buscarPor
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String result="listar";
		
		return (result);
				
	}

}
