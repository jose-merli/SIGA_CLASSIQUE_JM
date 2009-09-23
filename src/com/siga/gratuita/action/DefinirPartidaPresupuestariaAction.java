package com.siga.gratuita.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.UsrBean;
import com.siga.beans.ScsPartidaPresupuestariaAdm;
import com.siga.beans.ScsPartidaPresupuestariaBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefinirPartidaPresupuestariaForm;

//Clase: DefinirPartidaPresupuestariaAction 
//Autor: julio.vicente@atosorigin.com
//Ultima modificación: 21/12/2004
/**
 * Maneja las acciones que se pueden realizar sobre la tabla SCS_PARTIDASPRESUPUESTARIAS
 */
public class DefinirPartidaPresupuestariaAction extends MasterAction {	
		
	/** 
	 * No implementado
	 */
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
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
			Vector ocultos = formulario.getDatosTablaOcultos(0);
			ScsPartidaPresupuestariaAdm admBean =  new ScsPartidaPresupuestariaAdm(this.getUserBean(request));
			request.getSession().setAttribute("accion",formulario.getModo());
			Hashtable miHash = new Hashtable();
			miHash.put(ScsPartidaPresupuestariaBean.C_IDPARTIDAPRESUPUESTARIA,(ocultos.get(0)));
			miHash.put(ScsPartidaPresupuestariaBean.C_IDINSTITUCION,usr.getLocation());
			// Volvemos a obtener de base de datos la información, para que se la más actúal que hay en la base de datos
			Vector resultado = admBean.selectPorClave(miHash);
			ScsPartidaPresupuestariaBean retencion = (ScsPartidaPresupuestariaBean)resultado.get(0);
			Hashtable hashRetencion = retencion.getOriginalHash();
			
			request.getSession().setAttribute("elegido",hashRetencion);
			request.getSession().setAttribute("DATABACKUP",hashRetencion);		
		}catch(Exception e){
			throwExcp("messages.general.error",e,null);
		}
		return "modificacionOk";
	
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
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
			Vector ocultos = formulario.getDatosTablaOcultos(0);			
			ScsPartidaPresupuestariaAdm admBean =  new ScsPartidaPresupuestariaAdm(this.getUserBean(request));
			request.getSession().setAttribute("accion",formulario.getModo());			
			Hashtable miHash = new Hashtable();
			miHash.put(ScsPartidaPresupuestariaBean.C_IDPARTIDAPRESUPUESTARIA,(ocultos.get(0)));			
			miHash.put(ScsPartidaPresupuestariaBean.C_IDINSTITUCION,usr.getLocation());
			// Volvemos a obtener de base de datos la información, para que se la más actúal que hay en la base de datos
			Vector resultado = admBean.selectPorClave(miHash);
			ScsPartidaPresupuestariaBean retencion = (ScsPartidaPresupuestariaBean)resultado.get(0);			
			request.getSession().setAttribute("elegido",admBean.beanToHashTable(retencion));
			request.getSession().setAttribute("DATABACKUP",admBean.beanToHashTable(retencion));		
		}catch(Exception e){
			throwExcp("messages.general.error",e,null);
		}
		return "modificacionOk";
	}

	/**
	 * Rellena el string que indica la acción a llevar a cabo con "insertarPartida" para que redirija a la pantalla de inserción. 
	 * 
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la información. De tipo MasterForm.
	 * @param request Información de sesión. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		return "insertarPartida";
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
		
		DefinirPartidaPresupuestariaForm miForm = (DefinirPartidaPresupuestariaForm) formulario;		
		ScsPartidaPresupuestariaAdm admBean =  new ScsPartidaPresupuestariaAdm(this.getUserBean(request));
		Hashtable miHash = new Hashtable();
		String forward="";
		
		Double dImportePartida;
		
		try
		{
			dImportePartida = new Double(miForm.getImportePartida());
		}
		
		catch(Exception e)
		{
			throw new SIGAException("gratuita.partidaPresupuestaria.valor.error");
		}
		
		if (dImportePartida.intValue()<0)
		{
			throw new SIGAException("gratuita.partidaPresupuestaria.valor.error");
		}

		try {
			miHash = admBean.prepararInsert(miForm.getDatos());
			tx=usr.getTransaction();
			tx.begin();
			if (admBean.insert(miHash))
	        {
				tx.commit();
				forward = "exitoModal";	            
	        }	        
	        else forward ="exito";            
	        
		}catch(Exception e){
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}
		if (forward.equalsIgnoreCase("exitoModal")) return exitoModal("messages.inserted.success",request);
		else return exito("messages.inserted.error",request);
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
		String forward = "";
		DefinirPartidaPresupuestariaForm miForm = (DefinirPartidaPresupuestariaForm) formulario;		
		ScsPartidaPresupuestariaAdm admBean =  new ScsPartidaPresupuestariaAdm(this.getUserBean(request));
		
		try{					
			Hashtable hashBkp = new Hashtable(), hashNew = new Hashtable();
			hashBkp = (Hashtable)request.getSession().getAttribute("DATABACKUP");
			hashNew = ((ScsPartidaPresupuestariaBean)((Vector)admBean.selectPorClave(miForm.getDatos())).get(0)).getOriginalHash();
			hashNew.put(ScsPartidaPresupuestariaBean.C_NOMBREPARTIDA,miForm.getNombrePartida());
			
			Double dImportePartida;
			
			try
			{
				dImportePartida = new Double(miForm.getImportePartida());
			}
			
			catch(Exception e)
			{
				throw new SIGAException("gratuita.partidaPresupuestaria.valor.error");
			}
			//hashNew.put(ScsPartidaPresupuestariaBean.C_IMPORTEPARTIDA,miForm.getImportePartida());
			hashNew.put(ScsPartidaPresupuestariaBean.C_IMPORTEPARTIDA,""+dImportePartida);
			hashNew.put(ScsPartidaPresupuestariaBean.C_DESCRIPCION,miForm.getDescripcion());
			tx=usr.getTransaction();
			tx.begin();
			if (admBean.update(hashNew,hashBkp)) 
			{
				tx.commit();
				forward ="exitoModal";				
			}			
			else forward="exito";
		}catch(Exception e){
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		}
		if (forward.equalsIgnoreCase("exitoModal")) return exitoModal("messages.updated.success",request);
		else return exito("messages.updated.error",request);		
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
		ScsPartidaPresupuestariaAdm admBean =  new ScsPartidaPresupuestariaAdm(this.getUserBean(request));
		String forward = "";
		Hashtable miHash = new Hashtable();
		
		try {				
			miHash.put(ScsPartidaPresupuestariaBean.C_IDPARTIDAPRESUPUESTARIA,(ocultos.get(0)));				
			miHash.put(ScsPartidaPresupuestariaBean.C_FECHAMODIFICACION,(ocultos.get(1)));
			miHash.put(ScsPartidaPresupuestariaBean.C_USUMODIFICACION,(ocultos.get(2)));
			miHash.put(ScsPartidaPresupuestariaBean.C_IDINSTITUCION,(ocultos.get(3)));
			miHash.put(ScsPartidaPresupuestariaBean.C_NOMBREPARTIDA,(visibles.get(0)));
			miHash.put(ScsPartidaPresupuestariaBean.C_DESCRIPCION,(visibles.get(1)));			

			tx=usr.getTransaction();
			tx.begin();
			if (admBean.delete(miHash))
		    {
				tx.commit();
				forward ="exitoRefresco";        
		    }		    
		    else forward="exito";		    
		}catch(Exception e){
			throwExcp("messages.deleted.error",e,tx);
		}
		if (forward.equalsIgnoreCase("exitoRefresco")) return exitoRefresco("messages.deleted.success",request);
		else return exito("messages.deleted.error",request);
	}

	/**
	 * Rellena un hash con los valores recogidos del formulario y realiza la consulta a partir de esos datos. Almacena un vector con los resultados
	 * en la sesión con el nombre "resultado"
	 * 
	 * @param mapping Mapeador de las acciones. De tipo ActionMapping.
	 * @param formulario del que se recoge la información. De tipo MasterForm.
	 * @param request Información de sesión. De tipo HttpServletRequest
	 * @param response De tipo HttpServletResponse
	 * 
	 * @return String que indicará la siguiente acción a llevar a cabo. 
	 */	
	protected String buscar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		
		DefinirPartidaPresupuestariaForm miForm = (DefinirPartidaPresupuestariaForm) formulario;		
		ScsPartidaPresupuestariaAdm admBean =  new ScsPartidaPresupuestariaAdm(this.getUserBean(request));
		Vector v = new Vector ();		

		try {						
			v = admBean.select(miForm.getDatos());				
			request.getSession().setAttribute("resultado",v);		 
		}catch(Exception e){
			throwExcp("messages.general.error",e,null);
		}		
		return "listarPartidas";		
	}

	/** 
	 * No implementado
	 */	
	protected String abrirAvanzada(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		// TODO Auto-generated method stub
		return null;
	}
}