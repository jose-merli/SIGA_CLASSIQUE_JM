package com.siga.gratuita.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.*;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.*;
import com.siga.general.*;
import com.siga.gratuita.form.DefensaJuridicaDesignasForm;


/**
 * @author ruben.fernandez
 * @since 9/2/2005
 */

public class DefensaJuridicaDesignasAction extends MasterAction {
	
	protected ActionForward executeInternal(ActionMapping mapping,
		      ActionForm formulario,
		      HttpServletRequest request, 
		      HttpServletResponse response) throws SIGAException {

		MasterForm miForm = null;
		miForm = (MasterForm) formulario;
		try{
			if((miForm == null)||(miForm.getModo()==null)||(miForm.getModo().equals(""))){
				return mapping.findForward(this.abrir(mapping, miForm, request, response));
			}else return super.executeInternal(mapping, formulario, request, response);
		}
		catch (SIGAException es) { 
			throw es; 
		} 
		catch (Exception e) { 
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"}); 
		} 
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
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException
	{
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN"); 
		try {
			//Recogemos de la pestanha la designa insertada o la que se quiere consultar
			//y los usamos para la consulta y además hacemos una hashtable y lo guardamos en session
			Hashtable designaActual = new Hashtable();
			UtilidadesHash.set(designaActual,ScsDesignaBean.C_ANIO, 				(String)request.getParameter("ANIO"));
			UtilidadesHash.set(designaActual,ScsDesignaBean.C_NUMERO, 				(String)request.getParameter("NUMERO"));
			UtilidadesHash.set(designaActual,ScsDesignaBean.C_IDINSTITUCION,		(String)usr.getLocation());
			UtilidadesHash.set(designaActual,ScsDesignaBean.C_IDTURNO,				(String)request.getParameter("IDTURNO"));			
			
			
			ScsContrariosDesignaAdm contrariosAdm = new ScsContrariosDesignaAdm(this.getUserBean(request));
			String consultaContrarios = " select des.idinstitucion idinstitucion, des.idturno idturno, des.anio anio, des.numero numero, des.defensajuridica defensajuridica"+
										" from scs_designa des"+ 
										" where des.idinstitucion = "+ (String)usr.getLocation()+
										" and des.idturno = "+(String)request.getParameter("IDTURNO")+
										" and des.anio = "+(String)request.getParameter("ANIO")+
										" and des.numero = "+(String)request.getParameter("NUMERO")+" ";
			
			Vector resultado = (Vector)contrariosAdm.ejecutaSelect(consultaContrarios);
			request.setAttribute("resultado",resultado);
			request.setAttribute("designaActual",designaActual);
			request.setAttribute("modo",(String)request.getParameter("modo"));
		}catch(Exception e){
			 throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
		return "inicio";
	}
    
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrirAvanzada(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String abrirAvanzada(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws ClsExceptions, SIGAException {
		
		return "nuevoRecarga";
	}
	
	/**Funcion que transforma los datos de entrada para poder hacer la insercion a BBDD
	 * 
	 * @param formulario con los datos recogidos en el formulario de entrada
	 * @return formulario con los datos que se necesitan meter en BBDD
	 */
	protected Hashtable prepararHash (Hashtable datos){
		return datos;
	}
	
	/** 
	 *  Funcion que atiende la accion buscar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String buscar (ActionMapping mapping, 		
			  MasterForm formulario, 
			  HttpServletRequest request, 
			  HttpServletResponse response) throws ClsExceptions, SIGAException{
    
		        return "listado";
	}

	/** 
	 *  Funcion que atiende la accion editar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String editar(ActionMapping mapping, 
							MasterForm formulario,
							HttpServletRequest request, 
							HttpServletResponse response)throws ClsExceptions,SIGAException  {
		
		return "edicion";
	}

	/** 
	 *  Funcion que atiende la accion ver
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions,SIGAException  {
	    
		return "edicion";
	}

	/** 
	 *  Funcion que atiende la accion nuevo
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String nuevo(ActionMapping mapping, MasterForm formulario,
							HttpServletRequest request, HttpServletResponse response)
							throws ClsExceptions,SIGAException  {
		
		return "nuevo";
	}

	/** 
	 *  Funcion que atiende la accion insertar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String insertar(	ActionMapping mapping, MasterForm formulario,
								HttpServletRequest request, HttpServletResponse response)
								throws ClsExceptions,SIGAException  {
		
		return null;
	}

	/** 
	 *  Funcion que atiende la accion modificar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions,SIGAException  {

		UserTransaction tx=null;		
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		DefensaJuridicaDesignasForm miform = (DefensaJuridicaDesignasForm)formulario;
		ScsDesignaAdm designaAdm = new ScsDesignaAdm (this.getUserBean(request));
		String consultaDesigna = " where anio = "+ miform.getAnio() +
								" and idturno = "+miform.getIdTurno()+
								" and numero = "+miform.getNumero()+
								" and idinstitucion = "+usr.getLocation()+" ";
		ScsDesignaBean designaAntigua = (ScsDesignaBean)((Vector)designaAdm.select(consultaDesigna)).get(0);
		boolean ok=false;
		tx=usr.getTransaction();
		
		try{
			tx.begin();
			Hashtable designaModificada = new Hashtable();
			designaModificada = (Hashtable)(designaAntigua.getOriginalHash()).clone();
			designaModificada.put(ScsDesignaBean.C_DEFENSAJURIDICA,miform.getDefensaJuridica());
			ok=designaAdm.update(designaModificada,designaAntigua.getOriginalHash());
			tx.commit();
		}catch(Exception e){
			 throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,tx);
		}
		return exitoRefresco("messages.updated.success",request);
	}

	/** 
	 *  Funcion que atiende la accion borrar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String borrar(ActionMapping mapping, MasterForm formulario,
							HttpServletRequest request, HttpServletResponse response)
							throws ClsExceptions,SIGAException  {
			
		return "exito";
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscarPor(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions,SIGAException  {
		return null;
	}

}