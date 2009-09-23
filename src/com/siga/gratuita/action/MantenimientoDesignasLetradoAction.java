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
import com.siga.beans.ScsDesignaAdm;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.BusquedaDesignasForm;

/**
 * @author ruben.fernandez
 * @since 20/01/2005
 */
public class MantenimientoDesignasLetradoAction extends MasterAction {

	protected ActionForward executeInternal(ActionMapping mapping,
		      ActionForm formulario,
		      HttpServletRequest request, 
		      HttpServletResponse response) throws SIGAException {
		
		MasterForm miform = (MasterForm)formulario;
		try{
			if(miform==null)return mapping.findForward(this.abrir(mapping, miform, request, response));
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
		BusquedaDesignasForm miForm = (BusquedaDesignasForm)formulario; 
		String numero = "";
		String nombre = "";
		CenColegiadoBean datosColegiales;		

		try {
			HttpSession ses = request.getSession();
			String idPersona = (String)request.getSession().getAttribute("idPersonaTurno");
			UsrBean usr = (UsrBean)ses.getAttribute("USRBEAN");
			Vector ocultos = (Vector)ses.getAttribute("ocultos");
			ScsDesignaAdm designaAdm = new ScsDesignaAdm(this.getUserBean(request));
			Vector resultado = new Vector();
			String consulta =" select tur.abreviatura abreviatura, tur.nombre nombre, col.ncolegiado ncolegiado, per.nombre nombrecolegiado, per.apellidos1 ap1,per.apellidos2 ap2, des.anio anio,"+
							" des.numero numerodes,des.fechaentrada fechaentrada,des.estado estado,des.resumenasunto resumen," +
							" deslet.idinstitucion idinstitucion, deslet.idturno idturno, deslet.idpersona idpersona,"+
							/**pdm inc-xxx1**/
							" F_SIGA_ACTDESIG_NOVALIDAR(deslet.idinstitucion,deslet.idturno,deslet.anio,deslet.numero) actnovalidadas,"+
							" des.codigo codigo"+
							/**/
							" from  scs_designa des, scs_designasletrado deslet, cen_colegiado col, cen_persona per, scs_turno tur"+
							" where des.idinstitucion = deslet.idinstitucion"+
							" and des.idturno       = deslet.idturno"+
							" and des.anio          = deslet.anio"+
							" and des.numero        = deslet.numero"+
							" and col.idpersona     = deslet.idpersona"+
							" and col.idinstitucion = deslet.idinstitucion"+
							" and per.idpersona     = col.idpersona"+
							" and des.idinstitucion = tur.idinstitucion"+
							" and des.idturno 		= tur.idturno"+
							" and des.idinstitucion = "+usr.getLocation()+ 
							" and deslet.idpersona	= "+idPersona+
							" order by fechaentrada desc";

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
					nombre = miForm.getNombreColegiadoPestanha();
					numero = miForm.getNumeroColegiadoPestanha();
				}
			}
			// Almaceno la informacion del colegiado (almaceno "" si no tengo la informacion):
			request.setAttribute("NOMBRECOLEGPESTAÑA", nombre);
			request.setAttribute("NUMEROCOLEGPESTAÑA", numero);
			
			resultado = (Vector)designaAdm.ejecutaSelect(consulta);
			request.setAttribute("resultado",resultado);
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
		BusquedaDesignasForm miForm = (BusquedaDesignasForm)formulario; 
		String numero = "";
		String nombre = "";

		try {
			HttpSession ses = request.getSession();
			String idPersona = (String)request.getSession().getAttribute("idPersonaTurno");
			BusquedaDesignasForm miform = (BusquedaDesignasForm)formulario;
			UsrBean usr = (UsrBean)ses.getAttribute("USRBEAN");
			Vector ocultos = (Vector)ses.getAttribute("ocultos");
			ScsDesignaAdm designaAdm = new ScsDesignaAdm(this.getUserBean(request));
			Vector resultado = new Vector();
/*
			String consulta =" select tur.abreviatura abreviatura, tur.nombre nombre, col.ncolegiado ncolegiado, per.nombre nombrecolegiado, per.apellidos1 ap1,per.apellidos2 ap2, des.anio anio,"+
							" des.numero numerodes,des.fechaentrada fechaentrada,des.estado estado,des.resumenasunto resumen"+
							" from  scs_designa des, scs_designasletrado deslet, cen_colegiado col, cen_persona per, scs_turno tur"+
							" where des.idinstitucion = deslet.idinstitucion"+
							" and des.idturno       = deslet.idturno"+
							" and des.anio          = deslet.anio"+
							" and des.numero        = deslet.numero"+
							" and col.idpersona     = deslet.idpersona"+
							" and col.idinstitucion = deslet.idinstitucion"+
							" and per.idpersona     = col.idpersona"+
							" and des.idinstitucion = tur.idinstitucion"+
							" and des.idturno 		= tur.idturno"+
							" and des.idinstitucion = "+usr.getLocation()+ 
							" and deslet.idpersona	= "+idPersona;
*/
			String consulta =" select tur.abreviatura abreviatura, tur.nombre nombre, col.ncolegiado ncolegiado, per.nombre nombrecolegiado, per.apellidos1 ap1,per.apellidos2 ap2, des.anio anio,"+
			" des.numero numerodes,des.fechaentrada fechaentrada,des.estado estado,des.resumenasunto resumen," +
			" deslet.idinstitucion idinstitucion, deslet.idturno idturno, deslet.idpersona idpersona,"+
			/**pdm inc-xxx1**/
			" F_SIGA_ACTDESIG_NOVALIDAR(deslet.idinstitucion,deslet.idturno,deslet.anio,deslet.numero) actnovalidadas,"+
			" des.codigo codigo"+
			/**/
			" from  scs_designa des, scs_designasletrado deslet, cen_colegiado col, cen_persona per, scs_turno tur"+
			" where des.idinstitucion = deslet.idinstitucion"+
			" and des.idturno       = deslet.idturno"+
			" and des.anio          = deslet.anio"+
			" and des.numero        = deslet.numero"+
			" and col.idpersona     = deslet.idpersona"+
			" and col.idinstitucion = deslet.idinstitucion"+
			" and per.idpersona     = col.idpersona"+
			" and des.idinstitucion = tur.idinstitucion"+
			" and des.idturno 		= tur.idturno"+
			" and des.idinstitucion = "+usr.getLocation()+ 
			" and deslet.idpersona	= "+idPersona;
			
			int orden=Integer.parseInt(miform.getOrden());
			switch(orden){
				 case 0:	consulta += " order by abreviatura";
				 			break;
				 case 1:	consulta += " order by nombre";
				 			break;
				 case 2:	consulta += " order by anio desc, numerodes desc";
		 					break;
				 case 3:	consulta += " order by fechaentrada desc";
				 			break;
				 default: 	break;
			}
			request.setAttribute("orden",String.valueOf(orden));
			
			//Si vengo del menu de censo miro los datos colegiales para mostrar por pantalla:
			if (request.getSession().getAttribute("entrada")!=null && request.getSession().getAttribute("entrada").equals("2")) {
				nombre = miForm.getNombreColegiadoPestanha();
				numero = miForm.getNumeroColegiadoPestanha();
			}
			// Almaceno la informacion del colegiado (almaceno "" si no tengo la informacion):
			request.setAttribute("NOMBRECOLEGPESTAÑA", nombre);
			request.setAttribute("NUMEROCOLEGPESTAÑA", numero);
			
			resultado = (Vector)designaAdm.ejecutaSelect(consulta);
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
		try {
			BusquedaDesignasForm miform = (BusquedaDesignasForm)formulario;
	
			// OBTENGO VALORES DEL FORM
			// solamente el 0 porque es el unico que he pulsado
			Vector vOcultos = miform.getDatosTablaOcultos(0);
            // obtener idinstitucion
			String idInstitucion = (String)vOcultos.get(0);
            // obtener idTurno
			String idTurno = (String)vOcultos.get(1);
            // obtener numero
			String numero = (String)vOcultos.get(2);	
            // obtener anio
			String anio = (String)vOcultos.get(3);
			// obtener idpersona
			String idPersona = (String)vOcultos.get(4);
			
			
			String modo = "editar";
			Hashtable idDesigna = new Hashtable();
			
			idDesigna.put("accion",modo);
			idDesigna.put("IDPERSONA",idPersona);
			idDesigna.put("IDINSTITUCION",idInstitucion);
			idDesigna.put("IDTURNO",idTurno);
			idDesigna.put("NUMERO",numero);
			idDesigna.put("ANIO",anio);
			idDesigna.put("deDonde","ficha");
		
			
			
	
			request.setAttribute("idDesigna", idDesigna);		
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al editar");
		}	
		

		
		return "edicion";
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
