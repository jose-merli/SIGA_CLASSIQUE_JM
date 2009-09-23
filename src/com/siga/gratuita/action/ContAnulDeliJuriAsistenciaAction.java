package com.siga.gratuita.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMapping;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.beans.ScsAsistenciasAdm;
import com.siga.beans.ScsAsistenciasBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.AsistenciasForm;

/**
 * @author carlos.vidal
 * @since 3/2/2005
 */

public class ContAnulDeliJuriAsistenciaAction extends MasterAction {
	
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
		UsrBean usr 			= (UsrBean)request.getSession().getAttribute("USRBEAN");
		AsistenciasForm miForm 	= (AsistenciasForm)formulario;
		String path = mapping.getPath();
		
		String sEsFichaColegial = (String)request.getParameter("esFichaColegial");

		// Obtenemos el turno seleccionado para la consulta
		String anio = request.getParameter("ANIO");
		String numero = request.getParameter("NUMERO");
		String select = "SELECT "+
		" A.ANIO ANIO, A.NUMERO NUMERO, A.CONTRARIOS CONTRARIOS, A.MOTIVOSANULACION, A.FECHAANULACION FECHAANULACION,"+
		" A.DELITOSIMPUTADOS, A.DATOSDEFENSAJURIDICA FROM"+
		" SCS_ASISTENCIA A where IDINSTITUCION = "+usr.getLocation()+" AND "+
		" ANIO = "+anio+" AND NUMERO = "+numero;
		ScsAsistenciasAdm asistencias = new ScsAsistenciasAdm(this.getUserBean(request));
		try{
			Vector resultado = null;
			resultado=(Vector)asistencias.ejecutaSelect(select);
			if(path.equals("/JGR_ContrariosAsistencia") || path.equals("/JGR_ContrariosAsistenciaLetrado"))
			{
				if (path.equals("/JGR_ContrariosAsistencia"))
					request.setAttribute("ACTION","/JGR_ContrariosAsistencia.do");
				else
					request.setAttribute("ACTION","/JGR_ContrariosAsistenciaLetrado.do");
				
				request.setAttribute("TITULO","gratuita.contAnulDeliJuriAsistencia.literal.tituloCont");
				request.setAttribute("TITULOTEXTAREA","gratuita.contAnulDeliJuriAsistencia.literal.contrarios");
				request.setAttribute("NOMBRETEXTAREA","contrarios");
				request.setAttribute("DATO","CONTRARIOS");
			}
			else if(path.equals("/JGR_AnulacionAsistencia"))
			{
				request.setAttribute("ACTION","/JGR_AnulacionAsistencia.do");
				request.setAttribute("TITULO","gratuita.contAnulDeliJuriAsistencia.literal.tituloAnul");
				request.setAttribute("TITULOTEXTAREA","gratuita.contAnulDeliJuriAsistencia.literal.manulacion");
				request.setAttribute("NOMBRETEXTAREA","motivosAnulacion");
				request.setAttribute("DATO","MOTIVOSANULACION");
			}
			else if(path.equals("/JGR_DelitosAsistencia") || path.equals("/JGR_DelitosAsistenciaLetrado"))
			{
				if (path.equals("/JGR_DelitosAsistencia"))
					request.setAttribute("ACTION","/JGR_DelitosAsistencia.do");
				else
					request.setAttribute("ACTION","/JGR_DelitosAsistenciaLetrado.do");
					
				request.setAttribute("TITULO","gratuita.contAnulDeliJuriAsistencia.literal.tituloDeli");
				request.setAttribute("TITULOTEXTAREA","gratuita.contAnulDeliJuriAsistencia.literal.dimputados");
				request.setAttribute("NOMBRETEXTAREA","delitosImputados");
				request.setAttribute("DATO","DELITOSIMPUTADOS");
			}
			else if(path.equals("/JGR_DefensaJuridicaAsistencia") || path.equals("/JGR_DefensaJuridicaAsistenciaLetrado"))
			{
				if (path.equals("/JGR_DefensaJuridicaAsistencia"))
					request.setAttribute("ACTION","/JGR_DefensaJuridicaAsistencia.do");
				else
					request.setAttribute("ACTION","/JGR_DefensaJuridicaAsistenciaLetrado.do");
					
				request.setAttribute("TITULO","gratuita.contAnulDeliJuriAsistencia.literal.tituloJuri");
				request.setAttribute("TITULOTEXTAREA","gratuita.contAnulDeliJuriAsistencia.literal.djuridica");
				request.setAttribute("NOMBRETEXTAREA","datosDefensaJuridica");
				request.setAttribute("DATO","DATOSDEFENSAJURIDICA");
			}
			request.setAttribute("resultado",resultado);
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		}
		
		request.setAttribute("esFichaColegial",sEsFichaColegial);
		
		return "editar";
	}
    
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#abrirAvanzada(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String abrirAvanzada(ActionMapping mapping,
			MasterForm formulario, HttpServletRequest request,
			HttpServletResponse response) throws ClsExceptions, SIGAException {
		
		String sEsFichaColegial = (String)request.getParameter("esFichaColegial");
		request.setAttribute("esFichaColegial",sEsFichaColegial);
		
		return null;
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
	 *  Funcion que atiende la accion editar
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
		
		String sEsFichaColegial = (String)request.getParameter("esFichaColegial");
		request.setAttribute("esFichaColegial",sEsFichaColegial);
		
        return null;
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
		String sEsFichaColegial = (String)request.getParameter("esFichaColegial");
		request.setAttribute("esFichaColegial",sEsFichaColegial);
		
		return null;
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
		String sEsFichaColegial = (String)request.getParameter("esFichaColegial");
		request.setAttribute("esFichaColegial",sEsFichaColegial);
		
		return "Ver";
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
		String sEsFichaColegial = (String)request.getParameter("esFichaColegial");
		request.setAttribute("esFichaColegial",sEsFichaColegial);
		
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
		
		String sEsFichaColegial = (String)request.getParameter("esFichaColegial");		
		request.setAttribute("esFichaColegial",sEsFichaColegial);
		
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
			throws SIGAException  {
		String forward = "";
		
		String sEsFichaColegial = (String)request.getParameter("esFichaColegial");
		
		try
		{
			Hashtable hash = new Hashtable();
			// Dependiendo de donde vengamos tenemos que modificar unos campos u otros.
			UsrBean usr 			= (UsrBean)request.getSession().getAttribute("USRBEAN");
			String path = mapping.getPath();
			ScsAsistenciasAdm scsAsistenciaAdm = new ScsAsistenciasAdm(usr);
			AsistenciasForm miForm 	= (AsistenciasForm)formulario;
			request.setAttribute("mensaje","messages.updated.success");
			forward = "exito";
			//Dependiendo de la pestanha elegida, la modificacion sera de unos campos u otros.
			// Campos a clave
			hash.put(ScsAsistenciasBean.C_IDINSTITUCION,usr.getLocation());
			hash.put(ScsAsistenciasBean.C_ANIO,miForm.getAnio());
			hash.put(ScsAsistenciasBean.C_NUMERO,miForm.getNumero());
			if(path.equals("/JGR_ContrariosAsistencia") || path.equals("/JGR_ContrariosAsistenciaLetrado"))
			{
				String[] campos = {ScsAsistenciasBean.C_CONTRARIOS};
				hash.put(ScsAsistenciasBean.C_CONTRARIOS,miForm.getContrarios());
				scsAsistenciaAdm.updateDirect(hash,null,campos);
			}
			else if(path.equals("/JGR_AnulacionAsistencia"))
			{
				String[] campos = {ScsAsistenciasBean.C_FECHAANULACION,ScsAsistenciasBean.C_MOTIVOSANULACION};
				hash.put(ScsAsistenciasBean.C_FECHAANULACION,GstDate.getApplicationFormatDate(usr.getLanguage(),miForm.getFechaAnulacion()));
				hash.put(ScsAsistenciasBean.C_MOTIVOSANULACION,miForm.getMotivosAnulacion());
				scsAsistenciaAdm.updateDirect(hash,null,campos);
			}
			else if(path.equals("/JGR_DelitosAsistencia") || path.equals("/JGR_DelitosAsistenciaLetrado"))
			{
				String[] campos = {ScsAsistenciasBean.C_DELITOSIMPUTADOS};
				hash.put(ScsAsistenciasBean.C_DELITOSIMPUTADOS,miForm.getDelitosImputados());
				scsAsistenciaAdm.updateDirect(hash,null,campos);
			}
			else if(path.equals("/JGR_DefensaJuridicaAsistencia") || path.equals("/JGR_DefensaJuridicaAsistenciaLetrado"))
			{
				String[] campos = {ScsAsistenciasBean.C_DATOSDEFENSAJURIDICA};
				hash.put(ScsAsistenciasBean.C_DATOSDEFENSAJURIDICA,miForm.getDatosDefensaJuridica());
				scsAsistenciaAdm.updateDirect(hash,null,campos);
			}
			else
			{
				request.setAttribute("mensaje","messages.inserted.error");
			}
		}
		catch (Exception e) 
		{
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, null); 
//			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		}
		
		request.setAttribute("esFichaColegial",sEsFichaColegial);
		
		return forward;
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
		
		String sEsFichaColegial = (String)request.getParameter("esFichaColegial");
		request.setAttribute("esFichaColegial",sEsFichaColegial);
		
		return "exito";
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscarPor(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions,SIGAException  {
        
		String sEsFichaColegial = (String)request.getParameter("esFichaColegial");
		request.setAttribute("esFichaColegial",sEsFichaColegial);
		
		return null;
	}

}