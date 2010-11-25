package com.siga.gratuita.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import java.util.Hashtable;
import java.util.Vector;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.*;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.beans.ScsEstadoEJGAdm;
import com.siga.beans.ScsEstadoEJGBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefinirEstadosEJGForm;


/**
 * Maneja las acciones que se pueden realizar sobre la tabla SCS_ESTADOSEJG
 * 
 * @since 14-02-2005
 * @version adrianag - 10-09-2008:
*/
public class DefinirEstadosEJGAction extends MasterAction
{
	//////////////////// METODOS GENERICOS DE ACTION ////////////////////
	protected ActionForward executeInternal (ActionMapping mapping,
											 ActionForm formulario,
											 HttpServletRequest request,
											 HttpServletResponse response)
			throws SIGAException
	{
		MasterForm miForm = (MasterForm) formulario;
		if (miForm == null)
			try {
				return mapping.findForward (this.abrir (mapping, miForm, request, response));
			} catch (Exception e) {
				return mapping.findForward ("exception");
			}
		else
				return super.executeInternal (mapping, formulario, request, response); 
	} //executeInternal ()
	
	
	//////////////////// METODOS DE ACCIONES ////////////////////
	/** No implementado */
	protected String buscarPor (ActionMapping mapping,
								MasterForm formulario,
								HttpServletRequest request,
								HttpServletResponse response)
			throws SIGAException
	{		
		return null;
	} //buscarPor ()
	
	/** No implementado */
	protected String editar (ActionMapping mapping,
							 MasterForm formulario,
							 HttpServletRequest request,
							 HttpServletResponse response)
			throws SIGAException
	{   Vector v=new Vector();
	   try{
		Hashtable miHash = new Hashtable();
		Vector ocultos = formulario.getDatosTablaOcultos (0);			
		DefinirEstadosEJGForm miForm = (DefinirEstadosEJGForm) formulario; 
		miHash.put (ScsEstadoEJGBean.C_IDESTADOPOREJG, ocultos.get(0));
		miHash.put (ScsEstadoEJGBean.C_IDTIPOEJG, miForm.getIdTipoEJG ());
		miHash.put (ScsEstadoEJGBean.C_ANIO, miForm.getAnio ());
		miHash.put (ScsEstadoEJGBean.C_NUMERO, miForm.getNumero ());
		miHash.put (ScsEstadoEJGBean.C_IDINSTITUCION, this.getUserBean(request).getLocation ());
		ScsEstadoEJGAdm  estadoEJGAdm=new ScsEstadoEJGAdm(this.getUserBean(request));
		v=estadoEJGAdm.selectByPK(miHash);
		request.getSession ().setAttribute ("EJG", miHash);
		
		request.setAttribute ("resultado", v);
		request.setAttribute("modo","editar");
	   }catch (Exception e){
	   	throwExcp ("messages.general.error", e, null);
	   	
	   }
		return "editar";
	} //editar ()
	
	/** No implementado */
	protected String ver (ActionMapping mapping,
						  MasterForm formulario,
						  HttpServletRequest request,
						  HttpServletResponse response)
			throws SIGAException
	{		
		Vector v=new Vector();
	   try{
		Hashtable miHash = new Hashtable();
		Vector ocultos = formulario.getDatosTablaOcultos (0);			
		DefinirEstadosEJGForm miForm = (DefinirEstadosEJGForm) formulario; 
		miHash.put (ScsEstadoEJGBean.C_IDESTADOPOREJG, ocultos.get(0));
		miHash.put (ScsEstadoEJGBean.C_IDTIPOEJG, miForm.getIdTipoEJG ());
		miHash.put (ScsEstadoEJGBean.C_ANIO, miForm.getAnio ());
		miHash.put (ScsEstadoEJGBean.C_NUMERO, miForm.getNumero ());
		miHash.put (ScsEstadoEJGBean.C_IDINSTITUCION, this.getUserBean(request).getLocation ());
		ScsEstadoEJGAdm  estadoEJGAdm=new ScsEstadoEJGAdm(this.getUserBean(request));
		v=estadoEJGAdm.selectByPK(miHash);
		request.getSession ().setAttribute ("EJG", miHash);
		
		request.setAttribute ("resultado", v);
		request.setAttribute("modo","consulta");
	   }catch (Exception e){
	   	throwExcp ("messages.general.error", e, null);
	   	
	   }
		return "ver";
	} //ver ()
	
	/**
	 * Devuelve los datos necesarios para la insercion de un nuevo estado
	 */
	protected String nuevo (ActionMapping mapping,
							MasterForm formulario,
							HttpServletRequest request,
							HttpServletResponse response)
			throws SIGAException
	{
		Hashtable miHash = new Hashtable();
		
		miHash.put ("ANIO", formulario.getDatos ().get ("ANIO"));
		miHash.put ("NUMERO", formulario.getDatos ().get ("NUMERO"));
		miHash.put ("IDTIPOEJG", formulario.getDatos ().get ("IDTIPOEJG"));
		
		request.getSession ().setAttribute ("EJG", miHash);

		request.setAttribute("modo","nuevo");
		return "insertar";
	} //nuevo ()
	
	/**
	 * Rellena un hash con los valores recogidos del formulario
	 * y los inserta en la base de datos
	 */
	protected String insertar (ActionMapping mapping,
							   MasterForm formulario,
							   HttpServletRequest request,
							   HttpServletResponse response)
			throws SIGAException
	{
		//Controles generales
		UsrBean usr = (UsrBean) request.getSession ().getAttribute ("USRBEAN");
		ScsEstadoEJGAdm admBean = new ScsEstadoEJGAdm (usr);
		UserTransaction tx = null;
		
		DefinirEstadosEJGForm miForm = (DefinirEstadosEJGForm) formulario;		
		
		try {
			Hashtable miHash = new Hashtable ();
			miHash = miForm.getDatos ();
			admBean.prepararInsert (miHash);
			miHash.put(ScsEstadoEJGBean.C_AUTOMATICO,ClsConstants.DB_FALSE);
			if(usr.isComision())
				miHash.put(ScsEstadoEJGBean.C_PROPIETARIOCOMISION,ClsConstants.DB_TRUE);
			tx = usr.getTransaction ();
			tx.begin ();
			admBean.insert (miHash);
			tx.commit ();
		} catch (Exception e) {
			throwExcp ("messages.general.error",
					new String[] {"modulo.gratuita"}, e, tx);
		}
		
		return exitoModal ("messages.inserted.success", request);
	} //insertar ()
	
	/**
	 * No implementado
	 */
	protected String modificar (ActionMapping mapping,
								MasterForm formulario,
								HttpServletRequest request,
								HttpServletResponse response)
			throws SIGAException
	{ 
		
		UsrBean usr = (UsrBean) request.getSession ().getAttribute ("USRBEAN");
		ScsEstadoEJGAdm admBean = new ScsEstadoEJGAdm (usr);
		UserTransaction tx = null;
		
					
		DefinirEstadosEJGForm miForm = (DefinirEstadosEJGForm) formulario;		
		
		try {
			Hashtable miHash = new Hashtable ();
			miHash = miForm.getDatos ();
			admBean.prepararUpdate (miHash);
			if(usr.isComision())
				miHash.put(ScsEstadoEJGBean.C_PROPIETARIOCOMISION,ClsConstants.DB_TRUE);
			tx = usr.getTransaction ();
			tx.begin ();
			admBean.updateDirect(miHash,null,null);
			tx.commit ();
		} catch (Exception e) {
			throwExcp ("messages.general.error",
					new String[] {"modulo.gratuita"}, e, tx);
		}
		
		return exitoModal ("messages.updated.success", request);
	} //modificar ()
	
	/**
	 * Rellena un hash con los valores recogidos del formulario
	 * y los borra de la base de datos
	 */
	protected String borrar (ActionMapping mapping,
							 MasterForm formulario,
							 HttpServletRequest request,
							 HttpServletResponse response)
			throws SIGAException
	{
		//Controles generales
		UsrBean usr = (UsrBean) request.getSession ().getAttribute ("USRBEAN");
		ScsEstadoEJGAdm admBean =  new ScsEstadoEJGAdm (usr);
		UserTransaction tx = null;
		
		Vector ocultos = formulario.getDatosTablaOcultos (0);			
		DefinirEstadosEJGForm miForm = (DefinirEstadosEJGForm) formulario; 
		
		Hashtable miHash = new Hashtable ();
		
		try {				
			miHash.put (ScsEstadoEJGBean.C_IDESTADOPOREJG, ocultos.get(0));
			miHash.put (ScsEstadoEJGBean.C_IDTIPOEJG, miForm.getIdTipoEJG ());
			miHash.put (ScsEstadoEJGBean.C_ANIO, miForm.getAnio ());
			miHash.put (ScsEstadoEJGBean.C_NUMERO, miForm.getNumero ());
			miHash.put (ScsEstadoEJGBean.C_IDINSTITUCION, usr.getLocation ());
			
			tx = usr.getTransaction ();
			tx.begin ();
			admBean.delete (miHash);		    
			tx.commit ();
		} catch (Exception e) {
			   throwExcp("messages.deleted.error",e,tx);
		}
		
		return exitoRefresco ("messages.deleted.success", request);
	} //borrar ()

	/** 
	 * No implementado
	 */	
	protected String buscar (ActionMapping mapping,
							 MasterForm formulario,
							 HttpServletRequest request,
							 HttpServletResponse response)
			throws SIGAException
	{
		return null;		
	} //buscar ()
	
	/**
	 * Devuelve la lista de Estados EJG
	 */
	protected String abrir (ActionMapping mapping,
							MasterForm formulario,
							HttpServletRequest request,
							HttpServletResponse response)
			throws SIGAException
	{
		//Controles generales
		UsrBean usr = (UsrBean) request.getSession ().getAttribute ("USRBEAN");
		ScsEstadoEJGAdm admBean =  new ScsEstadoEJGAdm (usr);		
		
		//borrando atributos de sesion por si acaso		
		request.getSession().removeAttribute("DATABACKUP");
		
		Vector v = new Vector ();
		Hashtable miHash = new Hashtable ();
		DefinirEstadosEJGForm miForm = (DefinirEstadosEJGForm) formulario;
		String accion = request.getSession ().getAttribute ("accion").toString ();
		
		//rellenando el hash de consulta
		UtilidadesHash.set(miHash,"ANIO",request.getParameter("ANIO").toString());
		UtilidadesHash.set(miHash,"NUMERO",request.getParameter("NUMERO").toString());
		UtilidadesHash.set(miHash,"IDTIPOEJG",request.getParameter("IDTIPOEJG").toString());
		UtilidadesHash.set(miHash,"IDINSTITUCION",request.getParameter("IDINSTITUCION").toString());
		
		
		String consulta = 
			"SELECT estado.*, " + 
			"       "+UtilidadesMultidioma.getCampoMultidioma 
							("estadoejg.DESCRIPCION", usr.getLanguage ())+" " +
			"  FROM SCS_ESTADOEJG estado, SCS_MAESTROESTADOSEJG estadoejg " +
			" WHERE estado.IDESTADOEJG = estadoejg.IDESTADOEJG " +
			"   AND estado.IDINSTITUCION = "+UtilidadesHash.getString(miHash,"IDINSTITUCION")+" " +
			"   AND estado.IDTIPOEJG = "+UtilidadesHash.getString(miHash,"IDTIPOEJG")+" " +
			"   AND estado.ANIO = "+UtilidadesHash.getString(miHash,"ANIO")+" " +
			"   AND estado.NUMERO = "+UtilidadesHash.getString(miHash,"NUMERO")+" " +
			" ORDER BY ESTADO.FECHAINICIO asc, ESTADO.IDESTADOPOREJG asc";
		
		try {
			v = admBean.selectGenerico (consulta);
			request.setAttribute ("resultado", v);
			request.getSession ().setAttribute ("accion", accion);
		} catch (Exception e) {
			   throwExcp ("messages.general.error", e, null);
		}
		
		return "inicio";
	} //abrir ()
	
	/** 
	 * Devuelve la lista de Estados EJG
	 */
	protected String abrirAvanzada (ActionMapping mapping,
									MasterForm formulario,
									HttpServletRequest request,
									HttpServletResponse response)
			throws SIGAException
	{
		//Controles generales
		UsrBean usr = (UsrBean) request.getSession ().getAttribute ("USRBEAN");
		ScsEstadoEJGAdm admBean =  new ScsEstadoEJGAdm (usr);		
		
		Vector v = new Vector ();
		Hashtable miHash = new Hashtable ();
		DefinirEstadosEJGForm miForm = (DefinirEstadosEJGForm) formulario;

		miHash.put ("ANIO", miForm.getAnio ());
		miHash.put ("NUMERO", miForm.getNumero ());
		miHash.put ("IDTIPOEJG", miForm.getIdTipoEJG ());
		miHash.put ("IDINSTITUCION", usr.getLocation ());
		
		request.setAttribute ("DATOSEJG", miHash);
		
		String consulta =
			"SELECT estado.*, " +
			"       "+UtilidadesMultidioma.getCampoMultidioma ("estadoejg.DESCRIPCION", usr.getLanguage ())+" " +
			"  FROM SCS_ESTADOEJG estado, SCS_MAESTROESTADOSEJG estadoejg " +
			" WHERE estado.IDESTADOEJG = estadoejg.IDESTADOEJG " +
			"   AND estado.IDINSTITUCION = "+miHash.get ("IDINSTITUCION")+" " +
			"   AND estado.IDTIPOEJG = "+miHash.get ("IDTIPOEJG")+" " +
			"   AND estado.ANIO = "+miHash.get ("ANIO")+" " +
			"   AND estado.NUMERO = "+miHash.get ("NUMERO")+" " +
			" ORDER BY ESTADO.FECHAINICIO asc, ESTADO.IDESTADOPOREJG asc";
		
		try {			
			v = admBean.selectGenerico (consulta);			
			request.setAttribute ("resultado",v);	
			request.setAttribute ("accion", formulario.getModo ());		
		} catch (Exception e) {
			   throwExcp("messages.general.error",e,null);
		}
		
		return "inicio";
	} //abrirAvanzada ()
}