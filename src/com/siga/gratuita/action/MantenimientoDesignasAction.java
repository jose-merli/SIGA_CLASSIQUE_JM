package com.siga.gratuita.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.beans.ScsDesignaAdm;
import com.siga.beans.ScsDesignaBean;
import com.siga.beans.ScsTurnoAdm;
import com.siga.beans.ScsTurnoBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.BusquedaDesignasForm;

/**
 * @author ruben.fernandez
 * @since 20/01/2005
 * Modificado por david.sanchezp  30-06-2005 controlar SIGA Exception.
 */
public class MantenimientoDesignasAction extends MasterAction {

	
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
							HttpServletResponse response) 
							throws SIGAException {

		return "inicio";
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
											 HttpServletResponse response) throws SIGAException{
		try {
			HttpSession ses = request.getSession();
			BusquedaDesignasForm miform = (BusquedaDesignasForm)formulario;
			UsrBean usr = (UsrBean)ses.getAttribute("USRBEAN");
			Vector ocultos = (Vector)ses.getAttribute("ocultos");
			ScsDesignaAdm designaAdm = new ScsDesignaAdm(this.getUserBean(request));
			Vector resultado = new Vector();
			String consulta =" select col.ncolegiado ncolegiado, per.nombre nombrecolegiado, per.apellidos1 ap1,per.apellidos2 ap2, des.anio anio,"+
							" des.numero numerodes,des.fechaentrada fechaentrada,des.estado estado,des.resumenasunto resumen"+
							" from  scs_designa des, scs_designasletrado deslet, cen_colegiado col, cen_persona per"+
							" where des.idinstitucion = deslet.idinstitucion"+
							" and des.idturno       = deslet.idturno"+
							" and des.anio          = deslet.anio"+
							" and des.numero        = deslet.numero"+
							" and col.idpersona     = deslet.idpersona"+
							" and col.idinstitucion = deslet.idinstitucion"+
							" and per.idpersona     = col.idpersona"+
							" and des.idinstitucion  = "+usr.getLocation(); 
							if (ocultos!=null) {
								consulta += " and des.idturno 		= "+(String)ocultos.get(0);
							}
			int orden=Integer.parseInt(miform.getOrden());
			switch(orden){
				 case 0:	consulta += " order by ncolegiado";
				 			break;
				 case 1:	consulta += " order by nombre, ap1, ap2";
				 			break;
				 case 2:	consulta += " order by anio desc, numerodes desc";
		 					break;
				 case 3:	consulta += " order by fechaentrada desc";
				 			break;
				 default: 	break;
			}
			request.setAttribute("orden",String.valueOf(orden));
			
			resultado = (Vector)designaAdm.ejecutaSelect(consulta);
			request.setAttribute("resultado",resultado);
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
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
											 HttpServletResponse response) {
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
									  HttpServletResponse response) {
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
									  HttpServletResponse response) throws SIGAException{
		try { 
			HttpSession ses = (HttpSession)request.getSession();
			UsrBean usr = (UsrBean)ses.getAttribute("USRBEAN");
			BusquedaDesignasForm miform = (BusquedaDesignasForm)formulario;
			Vector visibles = (Vector)miform.getDatosTablaVisibles(0);
			Vector ocultos = (Vector)miform.getDatosTablaOcultos(0);
			String consultaDesigna =" select "+ScsDesignaBean.C_ANIO+", "+ScsDesignaBean.C_IDINSTITUCION+", "+ScsDesignaBean.C_IDTURNO+", "+ScsDesignaBean.C_NUMERO+
			                        " from "+ScsDesignaBean.T_NOMBRETABLA+
									" where " 	+ ScsDesignaBean.C_IDINSTITUCION+ " = " + (String)usr.getLocation()	+
									" and "   	+ ScsDesignaBean.C_IDTURNO 		+ " = " + (String)ocultos.get(0) 	+	//el idturno
									" and " 	+ ScsDesignaBean.C_ANIO			+ " = " + (String)visibles.get(2)	+	//el anho
									" and "		+ ScsDesignaBean.C_NUMERO		+ " = " + (String)visibles.get(3)+" ";
			
			ScsDesignaAdm designaAdm = new ScsDesignaAdm (this.getUserBean(request));
			Vector result = designaAdm.selectGenerico(consultaDesigna);
			Hashtable elegido = (Hashtable)result.elementAt(0);
			
			ses.setAttribute("Modo","editar");		// para saber en que modo se mete desde la tabla
			request.setAttribute("idDesigna",elegido);
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
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
								   HttpServletResponse response) throws SIGAException{
		String forward = this.editar(mapping,formulario,request,response);
		request.getSession().setAttribute("Modo","ver");
		return forward;
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
										HttpServletResponse response) {
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
										 HttpServletResponse response) {
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
									  HttpServletResponse response) {
		return mapSinDesarrollar;

	}

	/** 
	 *  Funcion que atiende la accion buscarPor
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @throws SIGAException 
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String buscarPor (ActionMapping mapping,
										 MasterForm formulario,
										 HttpServletRequest request, 
										 HttpServletResponse response) throws SIGAException {
		try {		
			HttpSession ses = request.getSession();
			UsrBean usr = (UsrBean)ses.getAttribute("USRBEAN");
			Vector ocultos = (Vector)ses.getAttribute("ocultos");
			
			//IDTURNO:
			String idTurno="";
			//Venimos de editar o consultar un turno:
			if (ocultos!=null)
				idTurno=(String)ocultos.get(0);
			//Venimos de insertar un nuevo turno:
			else {
				Hashtable hashTurno = (Hashtable)request.getSession().getAttribute("turnoElegido");
				idTurno = (String)hashTurno.get("IDTURNO");
			}
			
			ScsDesignaAdm designaAdm = new ScsDesignaAdm(this.getUserBean(request));
			Vector resultado = new Vector();
			String consulta =" select F_SIGA_CALCULONCOLEGIADO(col.idinstitucion,col.idpersona) ncolegiado, per.nombre nombrecolegiado, per.apellidos1 ap1,per.apellidos2 ap2, des.anio anio,deslet.idturno idturno,"+
							" des.numero numerodes,des.fechaentrada fechaentrada,des.estado estado,des.resumenasunto resumen, des.codigo codigo"+
							" from  scs_designa des, scs_designasletrado deslet, cen_colegiado col, cen_persona per"+
							" where des.idinstitucion = deslet.idinstitucion"+
							" and des.idturno       = deslet.idturno"+
							" and des.anio          = deslet.anio"+
							" and des.numero        = deslet.numero"+
							" and col.idpersona     = deslet.idpersona"+
							" and col.idinstitucion = deslet.idinstitucion"+
							" and per.idpersona     = col.idpersona"+
							" and des.idinstitucion  = "+usr.getLocation()+ 
							" and des.idturno 		= "+idTurno;
			
			//filtra por año si el campo viene informado
			BusquedaDesignasForm form = (BusquedaDesignasForm) formulario;
			String anio = form.getAnio();
			if (anio != null && !"".equals(anio)){
				consulta += " and des.anio = " + anio;
				request.setAttribute("anio",String.valueOf(anio));				
			}
			
			String ord = request.getParameter("orden"); 
			if (ord==null) {
				consulta += " order by fechaentrada desc ";
			} else {
				int orden=Integer.parseInt(ord);
				switch(orden){
					 case 0:	consulta += " order by ncolegiado";
					 			break;
					 case 1:	consulta += " order by nombre, ap1, ap2";
					 			break;
					 case 2:	consulta += " order by anio desc, numerodes desc";
			 					break;
					 case 3:	consulta += " order by fechaentrada desc";
					 			break;
					 default: 	break;
				}
				request.setAttribute("orden",String.valueOf(orden));
			}
							
	
			ScsTurnoAdm turnoAdm = new ScsTurnoAdm(this.getUserBean(request));
			Hashtable tabla = new Hashtable();
			tabla.put(ScsTurnoBean.C_IDTURNO,idTurno);
			tabla.put(ScsTurnoBean.C_IDINSTITUCION,usr.getLocation());
			Vector v = turnoAdm.selectByPK(tabla);
			if (v!=null && v.size()>0) {
				ScsTurnoBean turnoBean = (ScsTurnoBean) v.get(0);
				request.setAttribute("datosTurno",turnoBean);
			}
			
			resultado = (Vector)designaAdm.ejecutaSelect(consulta);
			request.setAttribute("resultado",resultado);
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		}
		return "listado";
	}


}
