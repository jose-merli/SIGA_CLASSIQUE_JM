package com.siga.gratuita.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.UsrBean;
import com.siga.beans.ScsTelefonosPersonaJGAdm;
import com.siga.beans.ScsTelefonosPersonaJGBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefinirTelefonosJGForm;

/* 
 * Clase para crear nuevos telefonosJG 
 * @author david.sachezp
 * @version 06/02/2006
 */
public class DefinirTelefonosJGAction extends MasterAction {
	
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {        
		return "nuevoTelefono";
	}

	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
		UserTransaction tx=null;		
		Hashtable miHash = new Hashtable();		
		DefinirTelefonosJGForm miForm = (DefinirTelefonosJGForm)formulario;
		Vector resultado = new Vector();
		String forward = "";
		
		try {
			ScsTelefonosPersonaJGAdm admTelefonosJG =  new ScsTelefonosPersonaJGAdm(this.getUserBean(request));					
			tx=usr.getTransaction();
										
			miHash.put(ScsTelefonosPersonaJGBean.C_IDINSTITUCION, miForm.getIdInstitucion());
			miHash.put(ScsTelefonosPersonaJGBean.C_IDPERSONA, miForm.getIdPersona());
			miHash.put(ScsTelefonosPersonaJGBean.C_NOMBRETELEFONO, miForm.getNombreTelefonoJG());
			miHash.put(ScsTelefonosPersonaJGBean.C_NUMEROTELEFONO, miForm.getNumeroTelefonoJG());
			miHash.put(ScsTelefonosPersonaJGBean.C_FECHAMODIFICACION, "sysdate");
			miHash.put(ScsTelefonosPersonaJGBean.C_USUMODIFICACION, usr.getUserName());
			
			//Calcula el nuevo idTelefono:
			admTelefonosJG.prepararInsert(miHash);
			
			tx.begin();			
			if (admTelefonosJG.insert(miHash)) {				
				tx.commit();
				forward = exitoModal("messages.inserted.success",request);
	        } else
	        	forward = exito("messages.inserted.error",request);
		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		} 		
        return forward;
	}

	protected String borrar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		UsrBean usr = null;
		UserTransaction tx = null;
				
		Vector ocultos = formulario.getDatosTablaOcultos(0);			
		ScsTelefonosPersonaJGAdm admBean =  new ScsTelefonosPersonaJGAdm(this.getUserBean(request));
		String forward = "";
		Hashtable miHash = new Hashtable();
		
		try {				
			usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			tx=usr.getTransaction();
			
			miHash.put(ScsTelefonosPersonaJGBean.C_IDINSTITUCION,(ocultos.get(0)));			
			miHash.put(ScsTelefonosPersonaJGBean.C_IDPERSONA,(ocultos.get(1)));
			miHash.put(ScsTelefonosPersonaJGBean.C_IDTELEFONO,(ocultos.get(2)));

			tx.begin();
			if (admBean.delete(miHash)) {
				tx.commit();			
				forward = exitoRefresco("messages.deleted.success",request);
		    } else 
		    	forward = exito("messages.deleted.success",request);			
		} catch (Exception e) {
			   throwExcp("messages.deleted.error",e,tx);
		} 
		return forward;
	}
	
	/* Este metodo se llama la primera vez que se carga el listado de telefonos. Realiza la busqueda de los telefonos de un idpersona por primera vez.<br>
	 * Los datos de accion e idpersona se pasan por primera vez en la url.
	 * @see com.siga.general.MasterAction#abrir(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		DefinirTelefonosJGForm miForm = (DefinirTelefonosJGForm)formulario;
		ScsTelefonosPersonaJGAdm admTelefonosJG = new ScsTelefonosPersonaJGAdm(this.getUserBean(request));
		
		Vector vTelefonosJG = new Vector();
		String idPersona = request.getParameter("idPersona");
		if (idPersona!=null && idPersona.equals("null")){
			idPersona=null;
		}
		String idInstitucion = request.getParameter("idInstitucion");
		String accion = request.getParameter("accion");
		
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		
		String sql = " SELECT * FROM " + ScsTelefonosPersonaJGBean.T_NOMBRETABLA + 
					 " WHERE " + ScsTelefonosPersonaJGBean.C_IDINSTITUCION + " = " + idInstitucion+
					 " AND " + ScsTelefonosPersonaJGBean.C_IDPERSONA + " = " + idPersona;
		try {
			if (idPersona!=null && !idPersona.equals("") )
				vTelefonosJG = admTelefonosJG.selectGenerico(sql);
			else
				vTelefonosJG = null;	
		} catch(Exception e) {
			vTelefonosJG = null;
		}
		miForm.setIdPersona(idPersona);
		miForm.setIdInstitucion(idInstitucion);
		miForm.setAccion(accion);
		
		request.setAttribute("VTELEFONOS",vTelefonosJG);
		return "listadoTelefonos";
	}
	
	/* Refresca la lista de telefonosJG. Los datos de idPersona y accion se pasan por formulario.
	 * @see com.siga.general.MasterAction#buscarPor(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		DefinirTelefonosJGForm miForm = (DefinirTelefonosJGForm)formulario;
		ScsTelefonosPersonaJGAdm admTelefonosJG = new ScsTelefonosPersonaJGAdm(this.getUserBean(request));
		
		Vector vTelefonosJG = new Vector();
		String idPersona = miForm.getIdPersona();
		String idInstitucion = miForm.getIdInstitucion();
		String accion = miForm.getAccion();
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		
		String sql = " SELECT * FROM " + ScsTelefonosPersonaJGBean.T_NOMBRETABLA + 
					 " WHERE " + ScsTelefonosPersonaJGBean.C_IDINSTITUCION + " = " + idInstitucion+
					 " AND " + ScsTelefonosPersonaJGBean.C_IDPERSONA + " = " + idPersona;
		try {
			vTelefonosJG = admTelefonosJG.selectGenerico(sql);
		} catch(Exception e) {
			vTelefonosJG = null;
		}
		miForm.setIdPersona(idPersona);
		miForm.setIdInstitucion(idInstitucion);
		miForm.setAccion(accion);
		
		request.setAttribute("VTELEFONOS",vTelefonosJG);
		return "listadoTelefonos";
	}
	
}