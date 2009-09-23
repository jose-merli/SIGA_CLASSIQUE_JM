package com.siga.gratuita.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.ScsTurnoAdm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 * @author ruben.fernandez
 * @since 26/01/2005
 */
public class ProximasDesignasAction extends MasterAction {

	protected ActionForward executeInternal(ActionMapping mapping,
		      ActionForm formulario,
		      HttpServletRequest request, 
		      HttpServletResponse response) throws SIGAException {
		
		MasterForm miform = (MasterForm)formulario;
		try{
			if((miform==null)||(miform.getModo()==null))
				return mapping.findForward(this.abrir(mapping, miform, request, response));
			else return super.executeInternal(mapping, formulario, request, response);
		}catch(SIGAException e){
			
		}
		catch(Exception e){
			
		}
		return mapping.findForward("exception");
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
	protected String abrir (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException 
	{
		String numero = "";
		String nombre = "";
		CenColegiadoBean datosColegiales;		
		
		try {
			HttpSession ses = request.getSession();
			String idPersona = (String) ses.getAttribute("idPersonaTurno");
			Vector ocultos = (Vector)ses.getAttribute("ocultos");
			Hashtable turno = (Hashtable)request.getSession().getAttribute("turnoElegido");
			UsrBean usr = (UsrBean)ses.getAttribute("USRBEAN");
			Vector resultado = new Vector();
			ScsTurnoAdm designaAdm = new ScsTurnoAdm(this.getUserBean(request));
			String consulta =	" select  turno.idturno idturno, turno.idinstitucion institucion, persona.nombre nombrecolegiado, persona.apellidos1 ap1, persona.apellidos2 ap2,turno.nombre turno, turno.abreviatura abreviatura, area.nombre area, materia.nombre materia, zona.nombre zona, subzona.nombre subzona, inscripcion.fechavalidacion fechavalidacion, inscripcion.fechabaja fechabaja, persona.idpersona idpersona"+
								" from scs_turno turno, scs_materia materia, scs_area area, scs_subzona subzona, scs_zona zona, scs_inscripcionturno inscripcion, cen_persona persona"+
								" WHERE area.idinstitucion = turno.idinstitucion"+
								" AND area.idarea = turno.idarea"+
								" and persona.idpersona = inscripcion.idpersona"+
								" AND materia.idinstitucion = turno.idinstitucion"+
								" AND materia.idarea = turno.idarea"+
								" AND materia.idmateria = turno.idmateria"+
								" AND subzona.idinstitucion (+)= turno.idinstitucion"+
								" AND subzona.idzona (+)= turno.idzona"+
								" AND subzona.idsubzona (+)= turno.idsubzona"+
								" AND zona.idinstitucion (+)= turno.idinstitucion"+
								" AND zona.idzona (+)= turno.idzona"+
								" AND turno.idinstitucion ="+usr.getLocation()+
								" AND inscripcion.idinstitucion = turno.idinstitucion"+
								" AND inscripcion.idturno		= turno.idturno"+	
								" AND inscripcion.idpersona = "+idPersona+
								" and inscripcion.fechavalidacion is not null"+
								" and inscripcion.fechabaja is null";
			
			//Si vengo del menu de censo miro los datos colegiales para mostrar por pantalla:
			if (request.getSession().getAttribute("entrada")!=null && request.getSession().getAttribute("entrada").equals("2")) {
				try {
					// Preparo para obtener la informacion del colegiado:
					Long idPers = new Long(request.getParameter("idPersonaPestanha"));
					Integer idInstPers = new Integer(request.getParameter("idInstitucionPestanha"));
					CenPersonaAdm personaAdm = new CenPersonaAdm(this.getUserBean(request));
					CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm(this.getUserBean(request));
		
					// Obtengo la informacion del colegiado:
					nombre = personaAdm.obtenerNombreApellidos(String.valueOf(idPers));
					datosColegiales = colegiadoAdm.getDatosColegiales(idPers,idInstPers);
					numero = colegiadoAdm.getIdentificadorColegiado(datosColegiales);
				} catch (Exception e1){
					nombre = (String)request.getAttribute("NOMBRECOLEGPESTAÑA");
					numero = (String)request.getAttribute("NUMEROCOLEGPESTAÑA");
				}
			}
			// Almaceno la informacion del colegiado (almaceno "" si no tengo la informacion):
			request.setAttribute("NOMBRECOLEGPESTAÑA", nombre);
			request.setAttribute("NUMEROCOLEGPESTAÑA", numero);	
			
			resultado = (Vector)designaAdm.selectTabla(consulta);
			request.getSession().setAttribute("DATABACKUP",resultado);
		}catch(Exception e){
			throwExcp("messages.select.error",e,null);
		}
		return "listado";
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
		
		HttpSession ses = request.getSession();
		String idPersona = (String) ses.getAttribute("idPersonaTurno");
		UsrBean usr = (UsrBean)ses.getAttribute("USRBEAN");
		ScsTurnoAdm designaAdm = new ScsTurnoAdm(this.getUserBean(request));
		Vector resultado = new Vector();
		String consulta =	" select  turno.idturno idturno, turno.idinstitucion institucion, persona.nombre nombrecolegiado, persona.apellidos1 ap1, persona.apellidos2 ap2, turno.nombre turno, turno.abreviatura abreviatura, area.nombre area, materia.nombre materia, zona.nombre zona, subzona.nombre subzona, inscripcion.fechavalidacion fechavalidacion, inscripcion.fechabaja fechabaja, persona.idpersona idpersona"+
							" from scs_turno turno, scs_materia materia, scs_area area, scs_subzona subzona, scs_zona zona, scs_inscripcionturno inscripcion, cen_persona persona"+
							" WHERE area.idinstitucion = turno.idinstitucion"+
							" AND area.idarea = turno.idarea"+
							" AND materia.idinstitucion = turno.idinstitucion"+
							" AND materia.idarea = turno.idarea"+
							" AND materia.idmateria = turno.idmateria"+
							" AND subzona.idinstitucion (+)= turno.idinstitucion"+
							" AND subzona.idzona (+)= turno.idzona"+
							" AND subzona.idsubzona (+)= turno.idsubzona"+
							" AND zona.idinstitucion (+)= turno.idinstitucion"+
							" AND zona.idzona (+)= turno.idzona"+
							" AND turno.idinstitucion ="+usr.getLocation()+
							" and persona.idpersona = inscripcion.idpersona"+
							" AND inscripcion.idinstitucion = turno.idinstitucion"+
							" AND inscripcion.idturno		= turno.idturno"+	
							" AND inscripcion.idpersona = "+idPersona+
							" and inscripcion.fechavalidacion is not null"+
							" and inscripcion.fechabaja is null";
		
		try{
			resultado = (Vector)designaAdm.selectTabla(consulta);
			request.setAttribute("resultado",resultado);
		}catch(Exception e){
			
		}
		return "listado";


	}

	/** 
	 *  Funcion que atiende la accion abrirConParametros
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String abrirConParametros (ActionMapping mapping, 		
										  	 MasterForm formulario, 
											 HttpServletRequest request, 
											 HttpServletResponse response) throws ClsExceptions{
		return mapSinDesarrollar;

	}

	/** 
	 * Funcion que atiende la accion buscar
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
									  HttpServletResponse response) throws ClsExceptions{
		return mapSinDesarrollar;

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
	protected String editar (ActionMapping mapping, 		
									  MasterForm formulario,
									  HttpServletRequest request, 
									  HttpServletResponse response) throws ClsExceptions{
		return mapSinDesarrollar;

	}
									
	/** 
	 *  Funcion que atiende la accion ver
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String ver (ActionMapping mapping,
								   MasterForm formulario,
								   HttpServletRequest request, 
								   HttpServletResponse response) throws ClsExceptions{
		return mapSinDesarrollar;
	}

	/** 
	 *  Funcion que atiende la accion nuevo
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String nuevo (ActionMapping mapping,
									 MasterForm formulario,
									 HttpServletRequest request, 
									 HttpServletResponse response) throws ClsExceptions{
		return mapSinDesarrollar;

	}

	/** 
	 *  Funcion que atiende la accion insertar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String insertar (ActionMapping mapping,
										MasterForm formulario,
										HttpServletRequest request, 
										HttpServletResponse response) throws ClsExceptions{
		return mapSinDesarrollar;

	}

	/** 
	 *  Funcion que atiende la accion modificar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String modificar (ActionMapping mapping,
										 MasterForm formulario,
										 HttpServletRequest request, 
										 HttpServletResponse response) throws ClsExceptions{
		return mapSinDesarrollar;

	}

	/** 
	 *  Funcion que atiende la accion borrar
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String borrar (ActionMapping mapping,
									  MasterForm formulario,
									  HttpServletRequest request, 
									  HttpServletResponse response) throws ClsExceptions{
		return mapSinDesarrollar;

	}

	/** 
	 *  Funcion que atiende la accion buscarPor
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String buscarPor (ActionMapping mapping,
										 MasterForm formulario,
										 HttpServletRequest request, 
										 HttpServletResponse response) throws ClsExceptions {
		return mapSinDesarrollar;
	}


}
