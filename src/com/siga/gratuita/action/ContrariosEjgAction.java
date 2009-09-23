package com.siga.gratuita.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.transaction.UserTransaction;


import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;

import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;

import com.siga.beans.ScsContrariosEJGAdm;
import com.siga.beans.ScsContrariosEJGBean;

import com.siga.beans.ScsEJGAdm;

import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

import com.siga.gratuita.form.ContrariosEjgForm;


/**
 * @author ruben.fernandez
 * @since 9/2/2005
 * @version 07/02/2006 (david.sanchezp): modificacion para incluir el combo procurador y arreglos varios.
 */

public class ContrariosEjgAction extends MasterAction {
	
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		String anio=null, numero=null, modoPestanha=null, idTipoEJG=null;
		ContrariosEjgForm miForm = (ContrariosEjgForm)formulario;
		//Tomo los datos de la pestanha:
		numero = miForm.getNumero().toString();
		anio = miForm.getAnio().toString();
		idTipoEJG = miForm.getIdTipoEJG().toString();
		modoPestanha	= request.getParameter("MODO");
		
		// Mandamos estos datos en el request:
		request.setAttribute("anio", anio);
		request.setAttribute("numero", numero);
		request.setAttribute("idTipoEJG", idTipoEJG);
		request.setAttribute("modoPestanha", modoPestanha);
		
		return "inicio";
	}


	/**
	 * Busca los delitos de un EJG concreto. 
	 */
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		ContrariosEjgForm miForm = (ContrariosEjgForm)formulario;		
		String anio, numero, idInstitucion, idTipoEJG;
		
		try {			
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
			ScsContrariosEJGAdm admContrarosEJG = new ScsContrariosEJGAdm(this.getUserBean(request));
			ScsEJGAdm admBean =  new ScsEJGAdm(this.getUserBean(request));
			// Obtengo los datos seleccionados:
			
			idInstitucion =usr.getLocation();
			numero = miForm.getNumero().toString();
			anio = miForm.getAnio().toString();
			idTipoEJG = miForm.getIdTipoEJG().toString();	
			
			
			Vector vContrariosEJG = admContrarosEJG.getDatosContrariosEJG(idInstitucion,idTipoEJG,anio,numero);
				
			request.setAttribute("vContrariosEJG",vContrariosEJG);
			miForm.setIdTipoEJG(new Integer(idTipoEJG));
			miForm.setNumero(new Integer(numero));
			miForm.setAnio(new Integer(anio));
			Hashtable ejg = new Hashtable();
			UtilidadesHash.set(ejg,"NUMERO",numero);
			UtilidadesHash.set(ejg,"ANIO",anio);			
			UtilidadesHash.set(ejg,"IDTIPOEJG",idTipoEJG);
			request.getSession().setAttribute("DATABACKUP",ejg);
				
		} catch (Exception e){
			throwExcp("messages.general.error",e,null);
		}
		return "listado";
	}

	/**
	 * Mapeo a la ventana modal para crear un nuevo delito EJG.
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) {
		return "nuevo";
	}

	/**
	 * Inserta un nuevo delito a un EJG concreto.	 
	 *  
	 * @param ActionMapping mapping Mapeador de las acciones.
	 * @param MasterForm formulario: formulario del que se recoge la información.
	 * @param HttpServletRequest request: información de entrada de la pagina original.
	 * @param HttpServletResponse response: información de salida para la pagina destino. 
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 * @throws ClsExceptions
	 */
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		
		try {			
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
			//Por desarrollar
			
		} catch (Exception e){
			
		}		
		return exitoModal("messages.inserted.success",request);
	}

	/**
	 * 
	 * Borra un delito para 1 EJG y un idDelitoEJG.
	 * 
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		ContrariosEjgForm miForm = (ContrariosEjgForm)formulario;
				
		Integer anio, numero, idInstitucion, idTipoEJG, idPersona;
		UserTransaction tx = null;
		
		try {			
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
			tx = usr.getTransaction();
			
			ScsContrariosEJGAdm admContrariosEJG = new ScsContrariosEJGAdm(this.getUserBean(request));
			
			Vector vOcultos = miForm.getDatosTablaOcultos(0);
			
			// Obtengo los datos seleccionados:
			idInstitucion = new Integer(usr.getLocation());
			idPersona = new Integer((String)vOcultos.get(5));
			
			numero = new Integer((String)vOcultos.get(8));
			anio = new Integer((String)vOcultos.get(7));
			idTipoEJG = new Integer((String)vOcultos.get(9));
			
			ScsContrariosEJGBean beanContrariosEJG = new ScsContrariosEJGBean();
			beanContrariosEJG.setAnio(anio);
			beanContrariosEJG.setIdPersona(idPersona);
			beanContrariosEJG.setIdInstitucion(idInstitucion);
			beanContrariosEJG.setNumero(numero);
			beanContrariosEJG.setIdTipoEJG(idTipoEJG);
						
			tx.begin();
			admContrariosEJG.delete(beanContrariosEJG);
			tx.commit();
		} catch (Exception e){
			throwExcp("messages.deleted.error",e,tx);
		}		
		return exitoRefresco("messages.deleted.success",request);
	}
	
}