package com.siga.gratuita.action;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.ScsInscripcionTurnoAdm;
import com.siga.beans.ScsInscripcionTurnoBean;
import com.siga.beans.ScsTurnoAdm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.InscripcionTurno;
import com.siga.gratuita.form.InscripcionTGForm;
import com.siga.gratuita.util.calendarioSJCS.LetradoInscripcion;

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
		String estado = "";
		CenColegiadoBean datosColegiales;		
		
		try {
			HttpSession ses = request.getSession();
			String idPersona = (String) ses.getAttribute("idPersonaTurno");
			UsrBean usr = (UsrBean)ses.getAttribute("USRBEAN");
		
			
			//Si vengo del menu de censo miro los datos colegiales para mostrar por pantalla:
			if (request.getSession().getAttribute("entrada")!=null && request.getSession().getAttribute("entrada").equals("2")) {
				try {
					// Preparo para obtener la informacion del colegiado:
					Long idPers = new Long(request.getParameter("idPersonaPestanha"));
					Integer idInstPers = new Integer(request.getParameter("idInstitucionPestanha"));
					CenPersonaAdm personaAdm = new CenPersonaAdm(this.getUserBean(request));
					CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm(this.getUserBean(request));
					CenClienteAdm clienteAdm = new CenClienteAdm(this.getUserBean(request));
		
					// Obtengo la informacion del colegiado:
					nombre = personaAdm.obtenerNombreApellidos(String.valueOf(idPers));
					datosColegiales = colegiadoAdm.getDatosColegiales(idPers,idInstPers);
					numero = colegiadoAdm.getIdentificadorColegiado(datosColegiales);
					estado = clienteAdm.getEstadoColegial(String.valueOf(idPers), String.valueOf(idInstPers));
				} catch (Exception e1){
					nombre = (String)request.getAttribute("NOMBRECOLEGPESTAÑA");
					numero = (String)request.getAttribute("NUMEROCOLEGPESTAÑA");
					estado="";
				}
			}
			// Almaceno la informacion del colegiado (almaceno "" si no tengo la informacion):
			request.setAttribute("NOMBRECOLEGPESTAÑA", nombre);
			request.setAttribute("NUMEROCOLEGPESTAÑA", numero);	
			request.setAttribute("ESTADOCOLEGIAL", estado);
			ScsTurnoAdm turnoAdm = new ScsTurnoAdm(this.getUserBean(request));
			ScsInscripcionTurnoAdm admInsTurno = new ScsInscripcionTurnoAdm(usr);
			InscripcionTGForm inscripcionTurnoForm = new InscripcionTGForm();
			inscripcionTurnoForm.setIdInstitucion(usr.getLocation());
			inscripcionTurnoForm.setIdPersona(idPersona);
			inscripcionTurnoForm.setFechaActiva("sysdate");
			// jbd // inc7765 // Seteamos el tipo para evitar que de error al recuperar las inscripciones
			inscripcionTurnoForm.setTipo("A");
			
			
			List<ScsInscripcionTurnoBean> inscripcionTurnoList= admInsTurno.getInscripcionesTurno(inscripcionTurnoForm);
			if(inscripcionTurnoList!=null ){
				for(ScsInscripcionTurnoBean insTurnoBean:inscripcionTurnoList){
					
					insTurnoBean.setTurno(turnoAdm.getTurnoInscripcion(insTurnoBean.getIdInstitucion(), insTurnoBean.getIdTurno()));
					
					List<LetradoInscripcion> colaTurnoList = InscripcionTurno.getColaTurno(insTurnoBean.getIdInstitucion(), insTurnoBean.getIdTurno(), "sysdate", false, usr);
					boolean foundIt = false;
					int i=0;
					while (!foundIt && i<colaTurnoList.size()) {
						
						LetradoInscripcion letradoTurno = colaTurnoList.get(i);
						
						
						if(letradoTurno.getIdPersona().toString().equals(idPersona)){
							foundIt = true;
							insTurnoBean.getTurno().setIdOrdenacionColas(i+1);
							
						}
						i++;
					}
										
				}
			}else{
				inscripcionTurnoList = new ArrayList<ScsInscripcionTurnoBean>();
				
			}
			
			request.setAttribute("resultado",inscripcionTurnoList);

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
	 * @throws SIGAException 
	 */
	protected String abrirAvanzada (ActionMapping mapping, 		
										  	 MasterForm formulario, 
											 HttpServletRequest request, 
											 HttpServletResponse response) throws ClsExceptions, SIGAException{
		
		HttpSession ses = request.getSession();
		try {
		String idPersona = (String) ses.getAttribute("idPersonaTurno");
		UsrBean usr = (UsrBean)ses.getAttribute("USRBEAN");
		ScsTurnoAdm turnoAdm = new ScsTurnoAdm(this.getUserBean(request));
		ScsInscripcionTurnoAdm admInsTurno = new ScsInscripcionTurnoAdm(usr);
		InscripcionTGForm inscripcionTurnoForm = new InscripcionTGForm();
		inscripcionTurnoForm.setIdInstitucion(usr.getLocation());
		inscripcionTurnoForm.setIdPersona(idPersona);
		//Estado confirmada
		inscripcionTurnoForm.setEstado("C");
		// de alta
		inscripcionTurnoForm.setTipo("A");
		//activas a feccha
		inscripcionTurnoForm.setFechaActiva("sysdate");
		
		List<ScsInscripcionTurnoBean> inscripcionTurnoList= admInsTurno.getInscripcionesTurno(inscripcionTurnoForm);
		
		for(ScsInscripcionTurnoBean insTurnoBean:inscripcionTurnoList){
			insTurnoBean.setTurno(turnoAdm.getTurnoInscripcion(insTurnoBean.getIdInstitucion(), insTurnoBean.getIdTurno()));
			List<LetradoInscripcion> colaTurnoList = InscripcionTurno.getColaTurno(insTurnoBean.getIdInstitucion(), insTurnoBean.getIdTurno(), "sysdate", false, usr);
			boolean foundIt = false;
			int i=0;
			while (!foundIt && i<colaTurnoList.size()) {
				
				LetradoInscripcion letradoTurno = colaTurnoList.get(i);
				
				
				if(letradoTurno.getIdPersona().toString().equals(idPersona)){
					foundIt = true;
					
					insTurnoBean.getTurno().setIdOrdenacionColas(i+1);
					
				}
				i++;
			}
		}
		
		request.setAttribute("resultado",inscripcionTurnoList);
		}catch(Exception e){
			throwExcp("messages.select.error",e,null);
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
