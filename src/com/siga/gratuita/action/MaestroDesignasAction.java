package com.siga.gratuita.action;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.AjaxCollectionXmlBuilder;
import com.siga.Utilidades.UtilidadesFecha;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.Utilidades.UtilidadesString;
import com.siga.Utilidades.paginadores.PaginadorBind;
import com.siga.beans.ScsActuacionDesignaAdm;
import com.siga.beans.ScsAsistenciasAdm;
import com.siga.beans.ScsAsistenciasBean;
import com.siga.beans.ScsDefendidosDesignaAdm;
import com.siga.beans.ScsDesignaAdm;
import com.siga.beans.ScsDesignaBean;
import com.siga.beans.ScsDesignasLetradoAdm;
import com.siga.beans.ScsDesignasLetradoBean;
import com.siga.beans.ScsEJGAdm;
import com.siga.beans.ScsEJGBean;
import com.siga.beans.ScsGuardiasTurnoAdm;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.beans.ScsJuzgadoAdm;
import com.siga.beans.ScsJuzgadoBean;
import com.siga.beans.ScsJuzgadoProcedimientoAdm;
import com.siga.beans.ScsProcedimientosAdm;
import com.siga.beans.ScsProcedimientosBean;
import com.siga.beans.ScsProcuradorAdm;
import com.siga.beans.ScsProcuradorBean;
import com.siga.beans.ScsTipoDesignaColegioAdm;
import com.siga.beans.ScsTipoDesignaColegioBean;
import com.siga.beans.ScsTurnoAdm;
import com.siga.beans.ScsTurnoBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.BuscarDesignasForm;
import com.siga.gratuita.form.BusquedaDesignasForm;
import com.siga.gratuita.form.DefinirEJGForm;
import com.siga.gratuita.form.DefinirMantenimientoEJGForm;
import com.siga.gratuita.form.DesignaForm;
import com.siga.gratuita.form.MaestroDesignasForm;
import com.siga.gratuita.form.VolantesExpressForm;
import com.siga.ws.CajgConfiguracion;


/**
 * @author ruben.fernandez
 * @since 9/2/2005
 * @version 26/01/2006 (david.sanchezp): arreglos varios y nuevos combos.
 */

public class MaestroDesignasAction extends MasterAction {
	
	protected ActionForward executeInternal(ActionMapping mapping,
		      ActionForm formulario,
		      HttpServletRequest request, 
		      HttpServletResponse response) throws SIGAException {

		MasterForm miForm = null;
		miForm = (MasterForm) formulario;
		try{
			if((miForm == null)||(miForm.getModo()==null)||(miForm.getModo().equals(""))){
				return mapping.findForward(this.abrir(mapping, miForm, request, response));
			}else if ( miForm.getModo().equalsIgnoreCase("actualizarDesigna")){
				return mapping.findForward(actualizarDesigna(mapping, miForm, request, response));
				
			}else if ( miForm.getModo().equalsIgnoreCase("actualizaDesigna")){
				return mapping.findForward(actualizaDesigna(mapping, miForm, request, response));
				
			}else if ( miForm.getModo().equalsIgnoreCase("getAjaxModulos")){
				getAjaxModulos(mapping, miForm, request, response);
				return null;
				
			} else 
				return super.executeInternal(mapping, formulario, request, response);
		} catch (SIGAException e) {
			throw e;
		} catch(ClsExceptions e) {
			throw new SIGAException("ClsException no controlada -> " + e.getMessage() ,e);
		} catch (Exception e){
			throw new SIGAException("Exception no controlada -> " + e.getMessage(),e);
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
		HttpSession ses = request.getSession();
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");		
		String forward="inicio";
		String consultaTipoDesigna=null, consultaTurno=null, nombreTurno="", nombreTipoDesigna = "", nombreTurnoAsistencia="", nombreGuardiaAsistencia="", consultaTurnoAsistencia="", consultaGuardiaAsistencia="";
		ScsTipoDesignaColegioAdm tipodesigna = null;
		ScsTurnoAdm turno = null;
		ScsGuardiasTurnoAdm guardia = null;
		ScsDesignaAdm admDesigna = null; 
		Hashtable resultado = new Hashtable();
		ScsDesignaBean beanDesigna = null;
		String idJuzgado = "" ;
		MaestroDesignasForm miform = (MaestroDesignasForm)formulario;
		String idtipoencalidad="";
		try {
			turno = new ScsTurnoAdm(this.getUserBean(request));
			guardia = new ScsGuardiasTurnoAdm(this.getUserBean(request));
			tipodesigna = new ScsTipoDesignaColegioAdm(this.getUserBean(request));
			admDesigna = new ScsDesignaAdm(this.getUserBean(request));

			//Recogemos de la pestanha la designa insertada o la que se quiere consultar
			//y los metemos en un hashtable para el jsp		
			if(miform.getAnio()!=null && !miform.getAnio().equals("")&&
					miform.getIdTurno()!=null && !miform.getIdTurno().equals("")&&
					miform.getNumero()!=null && !miform.getNumero().equals("")){
				UtilidadesHash.set(resultado,ScsDesignaBean.C_ANIO, 				miform.getAnio());
				UtilidadesHash.set(resultado,ScsDesignaBean.C_NUMERO, 				miform.getNumero());
				UtilidadesHash.set(resultado,ScsDesignaBean.C_IDINSTITUCION,		(String)usr.getLocation());
				UtilidadesHash.set(resultado,ScsDesignaBean.C_IDTURNO,				miform.getIdTurno());
					
			}else{
			
				UtilidadesHash.set(resultado,ScsDesignaBean.C_ANIO, 				(String)request.getParameter("ANIO"));
				UtilidadesHash.set(resultado,ScsDesignaBean.C_NUMERO, 				(String)request.getParameter("NUMERO"));
				UtilidadesHash.set(resultado,ScsDesignaBean.C_IDINSTITUCION,		(String)usr.getLocation());
				UtilidadesHash.set(resultado,ScsDesignaBean.C_IDTURNO,				(String)request.getParameter("IDTURNO"));
			}
			
			
			// jbd 01/02/2010 Pasamos el valor del pcajg del colegio
			int valorPcajgActivo=CajgConfiguracion.getTipoCAJG(new Integer(usr.getLocation()));
			request.setAttribute("PCAJG_ACTIVO", new Integer(valorPcajgActivo));
			
			// Consulto la designa:					
			Vector vDesignas = admDesigna.select(resultado);
			beanDesigna = (ScsDesignaBean)vDesignas.get(0);
			request.setAttribute("beanDesigna",beanDesigna);
			if ((beanDesigna.getIdTurno()!=null)&&(!(beanDesigna.getIdTurno()).equals(""))){
				consultaTurno=" where idTurno = " + beanDesigna.getIdTurno() + " and idinstitucion="+usr.getLocation()+" ";
				nombreTurno = ((ScsTurnoBean)((Vector)turno.select(consultaTurno)).get(0)).getAbreviatura();
			}
				
			//Se recupera el campo calidad, si tiene para el interesado de la designa.
			//para que se pase el dato a la jsp,y para cuando le damos al botón volver
			//vuelva con los datos de la busqueda que se realizo.
			ScsDefendidosDesignaAdm defendidosAdm = new ScsDefendidosDesignaAdm(usr);
			idtipoencalidad= defendidosAdm.getidTipoEnCalidad(resultado);
			
			//recuperando el idJuzgado.
			if(beanDesigna.getIdJuzgado()!= null){
				 idJuzgado = beanDesigna.getIdJuzgado().toString();
			}
			
			if ((beanDesigna.getIdTipoDesignaColegio()!=null)&&(!(beanDesigna.getIdTipoDesignaColegio()).equals(""))){
				consultaTipoDesigna = " where "+ScsTipoDesignaColegioBean.C_IDTIPODESIGNACOLEGIADO +" = " + beanDesigna.getIdTipoDesignaColegio() + " and idinstitucion ="+ usr.getLocation()+" ";
				nombreTipoDesigna = ((ScsTipoDesignaColegioBean)((Vector)tipodesigna.select(consultaTipoDesigna)).get(0)).getDescripcion();
			}
		} catch(Exception e){
			if ((beanDesigna.getIdTipoDesignaColegio()!=null)&&(!(beanDesigna.getIdTipoDesignaColegio()).equals(""))){
				consultaTipoDesigna = " where "+ScsTipoDesignaColegioBean.C_IDTIPODESIGNACOLEGIADO +" = " + beanDesigna.getIdTipoDesignaColegio() + " and idinstitucion ="+ usr.getLocation()+" ";
				nombreTipoDesigna = ((ScsTipoDesignaColegioBean)((Vector)tipodesigna.select(consultaTipoDesigna)).get(0)).getDescripcion();
			}
		}
		try {
			resultado = beanDesigna.getOriginalHash();
			UtilidadesHash.set(resultado,"TURNO", 								nombreTurno);
			UtilidadesHash.set(resultado,"TIPODESIGNA",							nombreTipoDesigna);
			
			if ((idtipoencalidad!=null)&&(idtipoencalidad.equals(""))){
				UtilidadesHash.set(resultado,"CALIDAD", idtipoencalidad);
			}else{
				UtilidadesHash.set(resultado,"CALIDAD", "");
			}
				
			ses.setAttribute("resultado",resultado);	
			
			ScsAsistenciasBean asistenciaBean = null;
			
			// DCG 
			// Obtenemos los datos de la asistencia asociada, si la tiene
			{
				String miWhere = " WHERE " + ScsAsistenciasBean.C_DESIGNA_ANIO + " = " + beanDesigna.getAnio() +
								   " AND " + ScsAsistenciasBean.C_DESIGNA_NUMERO + " = " + beanDesigna.getNumero() +
								   " AND " + ScsAsistenciasBean.C_DESIGNA_TURNO + " = " + beanDesigna.getIdTurno() +
								   " AND " + ScsAsistenciasBean.C_IDINSTITUCION + " = " + usr.getLocation();
				ScsAsistenciasAdm asistenciaAdm = new ScsAsistenciasAdm (this.getUserBean(request));
				Vector vA = asistenciaAdm.select(miWhere);
				if ((vA != null) && (vA.size() == 1)) {
					asistenciaBean = (ScsAsistenciasBean) vA.get(0);
					request.setAttribute("asistenciaBean", asistenciaBean);
				}
			}
			if (asistenciaBean!=null) {
				consultaTurnoAsistencia=" where idTurno = " + asistenciaBean.getIdTurno() + " and idinstitucion="+usr.getLocation()+" ";
				consultaGuardiaAsistencia=" where idTurno = " + asistenciaBean.getIdTurno() + " and idGuardia = " + asistenciaBean.getIdGuardia() + " and idinstitucion="+usr.getLocation()+" ";
	
				nombreTurnoAsistencia = ((ScsTurnoBean)((Vector)turno.select(consultaTurnoAsistencia)).get(0)).getNombre();
				nombreGuardiaAsistencia = ((ScsGuardiasTurnoBean)((Vector)guardia.select(consultaGuardiaAsistencia)).get(0)).getNombre();
				request.setAttribute("nombreTurnoAsistencia",nombreTurnoAsistencia);
				request.setAttribute("nombreGuardiaAsistencia",nombreGuardiaAsistencia);				
			}
			ses.setAttribute("ModoAction","editar");
		}		
		catch (Exception e2){
		    throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e2, null); 
		}
		return forward;
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
		try {
			HttpSession ses = (HttpSession)request.getSession();
			UsrBean usr = (UsrBean)ses.getAttribute("USRBEAN");
			MaestroDesignasForm miform = (MaestroDesignasForm)formulario;
			
		
			Vector visibles = (Vector)miform.getDatosTablaVisibles(0);
			Vector ocultos = (Vector)miform.getDatosTablaOcultos(0);
			String idturno ="", anio="",numero="";
			String estado= "";			
			String desdeEjg=(String)request.getParameter("desdeEjg");
			String desdeEJG=(String)request.getParameter("desdeEJG");
			if ((desdeEjg!=null && desdeEjg.equalsIgnoreCase("si"))||(desdeEJG!=null && desdeEJG.equalsIgnoreCase("si"))){
				ses.removeAttribute("DATAPAGINADOR");
			}
			boolean hayDatos = false;
			if (ocultos==null){
				hayDatos = true;
				idturno = (String) miform.getIdTurno();
				anio = (String)miform.getAnio();
				numero = (String)miform.getNumero();
				estado= miform.getEstado();
			}
			
		
			
			Hashtable elegido = new Hashtable();
			elegido.put(ScsDesignaBean.C_IDINSTITUCION, usr.getLocation());
			elegido.put(ScsDesignaBean.C_IDTURNO, (hayDatos?idturno:(String)ocultos.get(0)));
			elegido.put(ScsDesignaBean.C_ANIO, (hayDatos?anio:(String)ocultos.get(3)));
			elegido.put(ScsDesignaBean.C_NUMERO, (hayDatos?numero:(String)ocultos.get(2)));
			
			
			if (hayDatos==true){				 
				 estado = miform.getEstado();				 
			}else {
				estado= (String)visibles.get(5);
			}			

			String modoaction="";
			 if (estado!=null&&(estado.equalsIgnoreCase("FINALIZADO")|| estado.equalsIgnoreCase("F"))){				 
				  ses.setAttribute("ModoAction","editar");
				 ses.setAttribute("Modo","ver");
			}else{					
				ses.setAttribute("Modo","editar");	
			}		 
	  
			request.setAttribute("idDesigna",elegido);
		} 
		catch (Exception e2){
		    throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e2, null); 
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
	 * @exception  ClsExceptions,SIGAException   En cualquier caso de error
	 */
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions,SIGAException  {

	    String consultaDesigna = "";
		UsrBean usr = this.getUserBean(request);
		MaestroDesignasForm miform = (MaestroDesignasForm)formulario;
		String desdeEjg=(String)request.getParameter("desdeEjg");
		String desdeEJG=(String)request.getParameter("desdeEJG");
		if ((desdeEjg!=null && desdeEjg.equalsIgnoreCase("si"))||(desdeEJG!=null && desdeEJG.equalsIgnoreCase("si"))){
			request.getSession().removeAttribute("DATAPAGINADOR");
		}
		
		String idInstitucion = null;
		String idTurno = null;
		String anio = null;
		String numero = null;
		
		Vector ocultos = (Vector)miform.getDatosTablaOcultos(0);
		
		try {
			if ((miform.getDesdeEjg() != null) && (miform.getDesdeEjg().equalsIgnoreCase("si"))) {				
				idInstitucion = this.getIDInstitucion(request).toString();
				idTurno = miform.getIdTurno();
				anio = miform.getAnio();
				numero = ((ocultos == null)?miform.getNumero():(String)ocultos.get(2));			
			} else {
				idInstitucion = (String)usr.getLocation();
				idTurno = (String)ocultos.get(0);
				anio = (String)ocultos.get(3);
				numero = ((ocultos == null)?miform.getNumero():(String)ocultos.get(2));			
			}
			
			Hashtable elegido = new Hashtable();
			elegido.put(ScsDesignaBean.C_IDINSTITUCION, idInstitucion);
			elegido.put(ScsDesignaBean.C_IDTURNO, idTurno);
			elegido.put(ScsDesignaBean.C_ANIO, anio);
			elegido.put(ScsDesignaBean.C_NUMERO, numero);
	
			HttpSession ses = (HttpSession)request.getSession();
			ses.setAttribute("Modo","Ver");
			ses.setAttribute("ModoAction","Ver");
			request.setAttribute("idDesigna",elegido);
		} 
		catch (Exception e2){
		    throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e2, null); 
		}
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
		
		UserTransaction tx = null;
		UsrBean usr 	= (UsrBean)request.getSession().getAttribute("USRBEAN");
		tx=usr.getTransaction();
	    BuscarDesignasForm miform = (BuscarDesignasForm)formulario;
		Hashtable hash = (Hashtable)miform.getDatos();
		Hashtable nuevaDesigna = new Hashtable();
		ScsDesignaAdm designaAdm = new ScsDesignaAdm (this.getUserBean(request));
		
		try{
			tx.begin();
		    nuevaDesigna = designaAdm.prepararInsert(hash);
			
			Hashtable datosEntrada = miform.getDatos();
			String fechaJuicio = (String)datosEntrada.get("FechaJuicio");
			String horasJuicio = (String)datosEntrada.get("HorasJuicio");
			String minutosJuicio = (String)datosEntrada.get("MinutosJuicio");
			if (fechaJuicio!=null && !fechaJuicio.equals("")) {
				String aux = fechaJuicio.substring(0,fechaJuicio.length()-9) + " " + ((horasJuicio.trim().equals(""))?"00":horasJuicio)+":"+((minutosJuicio.trim().equals(""))?"00":minutosJuicio)+":00";
				nuevaDesigna.put(ScsDesignaBean.C_FECHAJUICIO,aux);		
			}
			
			hash.put(ScsDesignaBean.C_FECHAALTA,"SYSDATE");
			
			if (!designaAdm.insert(hash)) {
			    throw new  ClsExceptions("Error al insertar designación: "+designaAdm.getError());
			}
			
			tx.commit();
			
		} 
		catch (Exception e2){
		    throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e2, tx); 
		}
		return "mantenimiento";
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
		
		HttpSession ses = request.getSession();
		UsrBean usr = (UsrBean)ses.getAttribute("USRBEAN");
		MaestroDesignasForm miform = (MaestroDesignasForm)formulario;
		Hashtable datosEntrada = null;
		ScsDesignaAdm designaAdm = new ScsDesignaAdm (this.getUserBean(request));
		ScsDesignasLetradoAdm admDesignasLetrado = new ScsDesignasLetradoAdm(this.getUserBean(request));
		ScsActuacionDesignaAdm admActuacionesDesignas = new ScsActuacionDesignaAdm(this.getUserBean(request));
		boolean ok=false;
		UserTransaction tx = null;
		String aux2 = "";
		String Fechaoficiojuzgado="";
		boolean actualizarFechaLetrado = false;
		Hashtable hsDesignaLetrado = new Hashtable();
		Hashtable hsDesignaLetradoNew = new Hashtable();

		try {
			tx = usr.getTransaction();
			datosEntrada = (Hashtable)miform.getDatos();
			
		    if ((!ValidacionExisteDesigna(miform,request))|| (ValidacionExisteDesigna(miform,request)&& request.getParameter("modificarDesigna")!=null && request.getParameter("modificarDesigna").equals("1"))){
		    	
		    
						String consultaDesigna = " where " +ScsDesignaBean.C_ANIO+"="+(String)datosEntrada.get("ANIO")+ 
												 " and "+ScsDesignaBean.C_NUMERO+"="+(String)datosEntrada.get("NUMERO")+
												 // La fecha pasa a ser modificable, si la sacamos del form puede ser erronea
												 // " and "+GstDate.dateBetween0and24h(ScsDesignaBean.C_FECHAENTRADA,(String)datosEntrada.get("FECHA"))+
												 " and "+ScsDesignaBean.C_IDTURNO+"="+(String)datosEntrada.get("IDTURNO")+" "+
												 " and "+ScsDesignaBean.C_IDINSTITUCION+"="+usr.getLocation()+" "; 
						
						ScsDesignaBean designaAntigua = (ScsDesignaBean)((Vector)designaAdm.select(consultaDesigna)).get(0);
						
						Hashtable designaNueva = (Hashtable)(designaAntigua.getOriginalHash().clone());
						String lengua = (String)usr.getLanguage();
						try{
							aux2 = (String)GstDate.getApplicationFormatDate(lengua,(String)datosEntrada.get("FECHACIERRE"));
							if (aux2==null)
								aux2 = "";
						}catch(Exception e){aux2="";}
						designaNueva.put(ScsDesignaBean.C_FECHAFIN,aux2);					
						String tipo=(String)datosEntrada.get("TIPO");
						if (tipo!=null){
						 designaNueva.put(ScsDesignaBean.C_IDTIPODESIGNACOLEGIO,(String)datosEntrada.get("TIPO"));	
						}
						
						String estado = (String)datosEntrada.get("ESTADO");//estado seleccionado
						String estadoOriginal = miform.getEstadoOriginal();//estado original
						boolean anular = false;
						boolean desAnular=false;
						//Control del estado:
						if (estadoOriginal!=null && !estadoOriginal.equalsIgnoreCase("A") && !estadoOriginal.equalsIgnoreCase(estado)) {
							designaNueva.put(ScsDesignaBean.C_ESTADO, estado);
							designaNueva.put(ScsDesignaBean.C_FECHAESTADO, "SYSDATE");
							
						    if (estado!=null && estado.equalsIgnoreCase("A") && estadoOriginal!=null && !estadoOriginal.equalsIgnoreCase(estado)) {
						    	designaNueva.put(ScsDesignaBean.C_FECHAESTADO,"SYSDATE");
						    	designaNueva.put(ScsDesignaBean.C_FECHA_ANULACION,"SYSDATE");
							    anular = true;
						    }
						}else{
							if (estadoOriginal!=null && estadoOriginal.equalsIgnoreCase("A") && !estadoOriginal.equalsIgnoreCase(estado)) {
								designaNueva.put(ScsDesignaBean.C_ESTADO, estado);
								designaNueva.put(ScsDesignaBean.C_FECHA_ANULACION,"");
								designaNueva.put(ScsDesignaBean.C_FECHAESTADO, "SYSDATE");
								desAnular = true;
							}
						}
						
						// JBD INC-5682-SIGA Actualizamos la fecha de entrada 
						// si el usuario la ha modificado mete la nueva, si no, vuelve a meter la original
						// jbd // Agregamos un control para que la fecha sea correcta y ademas tenemos que cambiar 
						       // la fecha de designacion del primer letrado
						String fechaApertura = GstDate.getApplicationFormatDate("",(String)datosEntrada.get("FECHA"));
						if (fechaApertura!=null && !fechaApertura.equals("")) {
							//String aux = fechaApertura.substring(0,fechaApertura.length()-9);
							designaNueva.put(ScsDesignaBean.C_FECHAENTRADA,fechaApertura);
							if(!fechaApertura.equalsIgnoreCase(designaAntigua.getFechaEntrada())){
								Date dtFechaApertura = UtilidadesFecha.getDate(fechaApertura,ClsConstants.DATE_FORMAT_JAVA);
								// Recogemos las fechas de renuncia y actuacion que marcan el limite de la fecha apertura
								// Si es posterior a esas fechas no dejamos que se cambie la fecha
								hsDesignaLetrado = admDesignasLetrado.getPrimerLetrado(designaAntigua);
								String stFechaPrimeraRenuncia = (String)hsDesignaLetrado.get(ScsDesignasLetradoBean.C_FECHARENUNCIA);
								Date dtFechaPrimeraRenuncia=null;
								if(stFechaPrimeraRenuncia!=null && !stFechaPrimeraRenuncia.equalsIgnoreCase("")){							
									dtFechaPrimeraRenuncia = UtilidadesFecha.getDate(stFechaPrimeraRenuncia,ClsConstants.DATE_FORMAT_JAVA);
								}
								String stFechaPrimeraActuacion = admActuacionesDesignas.getFechaPrimeraActuacion(designaAntigua);
								Date dtFechaPrimeraActuacion = null;
								if(stFechaPrimeraActuacion!=null && !stFechaPrimeraActuacion.equalsIgnoreCase("")){
									dtFechaPrimeraActuacion = UtilidadesFecha.getDate(stFechaPrimeraActuacion,ClsConstants.DATE_FORMAT_JAVA);
								}
								// Ambas fechas pueden ser null
								String stMotivo="";
								Date dtFechaCorte=null;
								if(dtFechaPrimeraActuacion==null && dtFechaPrimeraRenuncia==null){
									// Si no hay fecha de renuncia ni fecha de actuacion actualizamos la fecha de designa del letrado 
									actualizarFechaLetrado = true;
									hsDesignaLetradoNew.put(ScsDesignasLetradoBean.C_FECHADESIGNA, fechaApertura);
								}else{
									// Si una es nula cogemos la otra
									if(dtFechaPrimeraActuacion==null){
										dtFechaCorte = dtFechaPrimeraRenuncia;
										stMotivo="messages.designa.fechaDesigna.anterior.renuncia";
									}else if(dtFechaPrimeraRenuncia==null){
										dtFechaCorte = dtFechaPrimeraActuacion;
										stMotivo="messages.designa.fechaDesigna.anterior.actuacion";
									}else{
										// Ninguna es nula
										if(dtFechaPrimeraActuacion.before(dtFechaPrimeraRenuncia)){
											dtFechaCorte = dtFechaPrimeraActuacion;
											stMotivo="messages.designa.fechaDesigna.anterior.actuacion";
										}else{
											dtFechaCorte = dtFechaPrimeraRenuncia;
											stMotivo="messages.designa.fechaDesigna.anterior.renuncia";
										}
									}
									if(dtFechaCorte.before(dtFechaApertura)){
										String mensaje = UtilidadesString.getMensajeIdioma(this.getUserBean(request),"messages.designa.fechaDesigna.anterior");
										mensaje += UtilidadesString.getMensajeIdioma(this.getUserBean(request),stMotivo);
										mensaje += " (" +UtilidadesString.formatoFecha(dtFechaCorte, ClsConstants.DATE_FORMAT_SHORT_SPANISH)+")";
										return exitoRefresco(mensaje, request);
									}else{
										actualizarFechaLetrado = true;
										hsDesignaLetradoNew.put(ScsDesignasLetradoBean.C_FECHADESIGNA, fechaApertura);
									}
								}
								
							}
						}
						
						String fechaJuicio = GstDate.getApplicationFormatDate("",(String)datosEntrada.get("FechaJuicio"));
						String horasJuicio = (String)datosEntrada.get("HorasJuicio");
						String minutosJuicio = (String)datosEntrada.get("MinutosJuicio");
						if (fechaJuicio!=null && !fechaJuicio.equals("")) {
							String aux = fechaJuicio.substring(0,fechaJuicio.length()-9) + " " + ((horasJuicio.trim().equals(""))?"00":horasJuicio)+":"+((minutosJuicio.trim().equals(""))?"00":minutosJuicio)+":00";
							designaNueva.put(ScsDesignaBean.C_FECHAJUICIO,aux);		
						} else { 
							designaNueva.put(ScsDesignaBean.C_FECHAJUICIO,"");		
						}
						
						designaNueva.put(ScsDesignaBean.C_RESUMENASUNTO,(String)datosEntrada.get("ASUNTO"));
						designaNueva.put(ScsDesignaBean.C_OBSERVACIONES,(String)datosEntrada.get("OBSERVACIONES"));
						designaNueva.put(ScsDesignaBean.C_DELITOS,(String)datosEntrada.get("DELITOS"));
			
						// Obtengo el idProcurador y la idInstitucion del procurador:
						Integer idProcurador, idInstitucionProcurador;
						idProcurador = null;
						idInstitucionProcurador = null;			
						String procurador = (String)datosEntrada.get(ScsDesignaBean.C_PROCURADOR);
						if (procurador!=null && !procurador.equals("")){
							idProcurador = new Integer(procurador.substring(0,procurador.indexOf(",")));
							idInstitucionProcurador = new Integer(procurador.substring(procurador.indexOf(",")+1));
							designaNueva.put(ScsDesignaBean.C_IDPROCURADOR, idProcurador);
							designaNueva.put(ScsDesignaBean.C_IDINSTITUCIONPROCURADOR, idInstitucionProcurador);
						} else {
							designaNueva.put(ScsDesignaBean.C_IDPROCURADOR, "");
							designaNueva.put(ScsDesignaBean.C_IDINSTITUCIONPROCURADOR, "");
						}			
			
						// Obtengo el idJuzgado y la idInstitucion del Juzgado:
						Integer idJuzgado, idInstitucionJuzgado;
						idJuzgado = null;
						idInstitucionJuzgado = null;			
						String juzgado = (String)datosEntrada.get("JUZGADO");
						if (juzgado!=null && !juzgado.equals("")){
							idJuzgado = new Integer(juzgado.substring(0,juzgado.indexOf(",")));
							idInstitucionJuzgado = new Integer(juzgado.substring(juzgado.indexOf(",")+1));
							designaNueva.put(ScsDesignaBean.C_IDJUZGADO, idJuzgado);
							designaNueva.put(ScsDesignaBean.C_IDINSTITUCIONJUZGADO, idInstitucionJuzgado);
						} else {
							designaNueva.put(ScsDesignaBean.C_IDJUZGADO, "");
							designaNueva.put(ScsDesignaBean.C_IDINSTITUCIONJUZGADO, "");
						}			
						
						if (miform.getNumeroProcedimiento() != null) {
						    UtilidadesHash.set(designaNueva, ScsDesignaBean.C_NUMPROCEDIMIENTO, miform.getNumeroProcedimiento());
						}else{
							 UtilidadesHash.set(designaNueva, ScsDesignaBean.C_NUMPROCEDIMIENTO, "");
						}	
						
						String procedimientoSel=(String)datosEntrada.get("IDPROCEDIMIENTO");
						if (procedimientoSel!=null){
							if(procedimientoSel.equals("")&& designaAntigua.getEstado().equals("F")){
								if (!designaAntigua.getProcedimiento().equals("")){
									designaNueva.put(ScsDesignaBean.C_IDPROCEDIMIENTO, designaAntigua.getProcedimiento());
								}else{
									designaNueva.put(ScsDesignaBean.C_IDPROCEDIMIENTO, "");
								}
							}else{
								String procedimiento[] = procedimientoSel.split(",");
								designaNueva.put(ScsDesignaBean.C_IDPROCEDIMIENTO, procedimiento[0]);
							}							
						}
						// JBD 16/2/2009 INC-5739-SIGA
						// Obtenemos el idPretension						
						String pretensionSel=(String)datosEntrada.get("IDPRETENSION");
						if (pretensionSel!=null){
							if(pretensionSel.equals("")&& designaAntigua.getEstado().equals("F")){							
								if (designaAntigua.getIdPretension()!=null){
									if (!designaAntigua.getIdPretension().equals("")){
										designaNueva.put(ScsDesignaBean.C_IDPRETENSION, designaAntigua.getIdPretension());
									}else{
										designaNueva.put(ScsDesignaBean.C_IDPRETENSION, "");
									}
								}						
							}else{
								String pretenciaon[] = pretensionSel.split(",");
								designaNueva.put(ScsDesignaBean.C_IDPRETENSION, pretenciaon[0]);
							}							
						}
						
						// jbd 8/3/2010 inc-6876						
						String fechaoficiojuzgado = (String)GstDate.getApplicationFormatDate(lengua,(String)datosEntrada.get("FECHAOFICIOJUZGADO"));
						if (fechaoficiojuzgado!=null){
							if(fechaoficiojuzgado.equals("")&& designaAntigua.getEstado().equals("F")){
									if (!designaAntigua.getFechaOficioJuzgado().equals("")){
										designaNueva.put(ScsDesignaBean.C_FECHAOFICIOJUZGADO,designaAntigua.getFechaOficioJuzgado());
									}else{
										designaNueva.put(ScsDesignaBean.C_FECHAOFICIOJUZGADO, "");
									}
							}else{
								designaNueva.put(ScsDesignaBean.C_FECHAOFICIOJUZGADO,fechaoficiojuzgado);
							}							
						}else if (!designaAntigua.getEstado().equals("F")){
							designaNueva.put(ScsDesignaBean.C_FECHAOFICIOJUZGADO, "");
						}		
						
						String fechaRecepcionColegio = (String)GstDate.getApplicationFormatDate(lengua,(String)datosEntrada.get("FECHARECEPCIONCOLEGIO"));
						if (fechaRecepcionColegio!=null){
							if(fechaRecepcionColegio.equals("")&& designaAntigua.getEstado().equals("F")){
									if (!designaAntigua.getFechaRecepcionColegio().equals("")){
										designaNueva.put(ScsDesignaBean.C_FECHARECEPCIONCOLEGIO,designaAntigua.getFechaRecepcionColegio());
									}else{
										designaNueva.put(ScsDesignaBean.C_FECHARECEPCIONCOLEGIO, "");
									}
							}else{
								designaNueva.put(ScsDesignaBean.C_FECHARECEPCIONCOLEGIO,fechaRecepcionColegio);
							}
							
						}else if (!designaAntigua.getEstado().equals("F")){
							designaNueva.put(ScsDesignaBean.C_FECHARECEPCIONCOLEGIO, "");
						}		
						
						tx.begin();
						designaAdm.update(designaNueva,designaAntigua.getOriginalHash());	
						if(actualizarFechaLetrado){
							admDesignasLetrado.updateFechaDesigna(hsDesignaLetrado, fechaApertura);
						}
						if (anular)
							designaAdm.anularDesigna(mapping, formulario, request, response);
						if (desAnular)
							designaAdm.desAnularDesigna(mapping, formulario, request, response);						
						tx.commit();
						
						
		    }else{
		    	 return "PreguntaDesignaDuplicada";
		    	
		    }
		}catch(Exception e){
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
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
			
		HttpSession ses = request.getSession();
		UsrBean usr = (UsrBean)ses.getAttribute("USRBEAN");
		MaestroDesignasForm miform = (MaestroDesignasForm)formulario;		
		Vector ocultos = (Vector)miform.getDatosTablaOcultos(0);
		Vector visibles = (Vector)miform.getDatosTablaVisibles(0);
		Hashtable hash = new Hashtable();
		//recoger la designa actual, los datos que hacen falta
		hash.put(ScsDesignaBean.C_ANIO,(String)ocultos.get(3));
		hash.put(ScsDesignaBean.C_NUMERO,(String)ocultos.get(2));
		hash.put(ScsDesignaBean.C_IDINSTITUCION,usr.getLocation());
		hash.put(ScsDesignaBean.C_IDTURNO,(String)ocultos.get(0));
		hash.put(ScsDesignasLetradoBean.C_IDPERSONA,(String)ocultos.get(1));
		boolean ok = false, ok2 = false;
		UserTransaction tx=null;
		String sqlDel=" delete "+ScsDesignasLetradoBean.T_NOMBRETABLA+" where "+ScsDesignasLetradoBean.C_ANIO+"="+(String)ocultos.get(3)+
        " and "+ScsDesignasLetradoBean.C_NUMERO+"="+(String)ocultos.get(2)+
        " and "+ScsDesignasLetradoBean.C_IDINSTITUCION+"="+usr.getLocation()+
        " and "+ScsDesignasLetradoBean.C_IDTURNO+"="+(String)ocultos.get(0);
       
		
		//recoger la designa actual, los datos que hacen falta, para la llamada de la funcion donde introduce los 
		// datos en la tabla SCS_SALTOSCOMPENSACIONES
		String anio=(String)ocultos.get(3);
		String numero=(String)ocultos.get(2);
		String idinstitucion=usr.getLocation();
		String idTurno=(String)ocultos.get(0);
		String codigo= (String)visibles.get(3);		
	
		try{
			ScsDesignaAdm designaAdm = new ScsDesignaAdm (this.getUserBean(request));
			ScsDesignasLetradoAdm desletAdm = new ScsDesignasLetradoAdm(this.getUserBean(request));
			tx=usr.getTransaction();
			tx.begin();						  
			// Comprobamos que se quiera compensar o no por el borrado de la designacion
			String compensar = request.getParameter("compensar");
			if (compensar.equalsIgnoreCase("1")){//En caso de que SI se quiera compensar al letrado
				designaAdm.compensacionDesigna(request,anio,codigo, numero, idTurno, idinstitucion);						
			}								
			if (!desletAdm.deleteSQL(sqlDel)){
			    throw new ClsExceptions("Error al borrar designas letrado: "+desletAdm.getError());
			}
			if (!designaAdm.delete(hash)) {
			    throw new ClsExceptions("Error al borrar designas: "+designaAdm.getError());
			}			
			tx.commit();			
		}catch(Exception e){
			throwExcp("gratuita.listadoDesignas.message.error1",new String[] {"modulo.gratuita"},e,tx);
		}
		return exitoRefresco("messages.deleted.success",request);
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscarPor(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String buscarPor(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions,SIGAException  {
		return null;
	}
	
	
	protected boolean ValidacionExisteDesigna(MaestroDesignasForm formulario,HttpServletRequest request) throws ClsExceptions,SIGAException {
		try{
			String idJuzgadoObtenido=formulario.getJuzgado();
			if ((idJuzgadoObtenido!=null && !idJuzgadoObtenido.equals("")) && (formulario.getNumeroProcedimiento()!=null && !formulario.getNumeroProcedimiento().equals(""))){
				String cadena[]=idJuzgadoObtenido.split(",");
				String idJuzgado=cadena[0];
				String idinstitucionJuzgado=cadena[1];
		        
			         
				String consultaDesigna = " where " +ScsDesignaBean.C_IDJUZGADO+"="+idJuzgado+
				 " and "+ScsDesignaBean.C_IDINSTITUCIONJUZGADO+"="+idinstitucionJuzgado+
				 " and upper("+ScsDesignaBean.C_NUMPROCEDIMIENTO+")=upper('"+(String)formulario.getNumeroProcedimiento()+"')"+
				 " and ("+ScsDesignaBean.C_NUMERO+","+ScsDesignaBean.C_IDINSTITUCION+","+ScsDesignaBean.C_IDTURNO+","+ScsDesignaBean.C_ANIO+") not in ("+
				 "     select "+ScsDesignaBean.C_NUMERO+","+ScsDesignaBean.C_IDINSTITUCION+","+ScsDesignaBean.C_IDTURNO+","+ScsDesignaBean.C_ANIO+
				 "      from  "+ ScsDesignaBean.T_NOMBRETABLA+
				 "     where  "+ScsDesignaBean.C_IDINSTITUCIONJUZGADO+"="+idinstitucionJuzgado+
				 "        and "+ScsDesignaBean.C_IDJUZGADO+"="+idJuzgado+
				 "        and upper("+ScsDesignaBean.C_NUMPROCEDIMIENTO+")=upper('"+(String)formulario.getNumeroProcedimiento()+"')"+
				 "        and "+ScsDesignaBean.C_NUMERO+"="+formulario.getNumero()+
				 "        and "+ScsDesignaBean.C_IDINSTITUCION+"="+this.getUserBean(request).getLocation()+
				 "        and "+ScsDesignaBean.C_IDTURNO+"="+formulario.getIdTurno()+
				 "        and "+ScsDesignaBean.C_ANIO+"="+formulario.getAnio()+")"+
				 "  and "+ScsDesignaBean.C_IDINSTITUCION+"="+this.getUserBean(request).getLocation();
				 
			  ScsDesignaAdm designaAdm = new ScsDesignaAdm (this.getUserBean(request));	 
	          Vector existeDesigna=designaAdm.select(consultaDesigna);
	          if (existeDesigna!=null && existeDesigna.size()>0){
	          	return true;
	          }
			}
		}
		catch (Exception e){
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, null);
		}
		return false;
	}
	protected String actualizarDesigna(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException
	{
		HttpSession ses = request.getSession();
		UsrBean usr = this.getUserBean(request);
		String forward="actualizarDesigna";
		MaestroDesignasForm miform = (MaestroDesignasForm)formulario;
		ScsDesignaAdm admDesigna  = new ScsDesignaAdm(usr);
		ScsTurnoAdm admTurno = new ScsTurnoAdm(usr);
		/*String[] nombres =null;
		///Saco los EJG
		HashMap databackup=getPaginador(request, paginadorPenstania);
		List<DesignaForm> designaFormList = new ArrayList<DesignaForm>();
		DefinirEJGForm expedientes= new DefinirEJGForm();
		if (databackup!=null){ 
			designaFormList = (List<DesignaForm>)databackup.get("datos");
			for (int i = 0; i < designaFormList.size(); i++) {
				DesignaForm designaRow = designaFormList.get(i);
				if(designaRow.getNumero().equalsIgnoreCase(miform.getNumero()) && 
						designaRow.getAnio().equalsIgnoreCase(miform.getAnio())){
					List<DefinirEJGForm> ejgList  = (List<DefinirEJGForm>) designaRow.getExpedientes();
					for (int j = 0; j < ejgList.size(); j++) {
						expedientes=ejgList.get(j);
						Hashtable expe = expedientes.getDatos();
						String numEjg= (String) expe.get("NOMBRE");
						
					}
				}
			}
			
		}
		//databackup.set("datos",designaFormList);
		 */
		Hashtable filtro = new Hashtable();

		UtilidadesHash.set(filtro,ScsDesignaBean.C_ANIO, 				miform.getAnio());
		UtilidadesHash.set(filtro,ScsDesignaBean.C_NUMERO, 				miform.getNumero());
		UtilidadesHash.set(filtro,ScsDesignaBean.C_IDINSTITUCION,		(String)usr.getLocation());
		UtilidadesHash.set(filtro,ScsDesignaBean.C_IDTURNO,				miform.getIdTurno());
		///Calculo de EJGs
		Vector datos = admDesigna.getDatosEJG((String)usr.getLocation(), miform.getNumero(), miform.getIdTurno(), miform.getAnio());
		// Recorrer los defendidos
		List<ScsEJGBean> ejgList = new ArrayList<ScsEJGBean>();
		for (int i = 0; i < datos.size(); i++) {
				ScsEJGBean ejg = new ScsEJGBean();
				String anio = (String) ((Hashtable) datos.get(i)).get("ANIO_EJG");
				String numero = (String) ((Hashtable) datos.get(i)).get("NUMERO_EJG");
				String tipo =(String) ((Hashtable) datos.get(i)).get("TIPO_EJG");
				ejg = abrir(request, anio, numero, tipo);
				ejgList.add(ejg);
		}
		///Calculo de EJGs
		miform.setEjgs(ejgList);
	
		Vector vDesignas = admDesigna.select(filtro);
		
		Hashtable letradoHashtable = admDesigna.obtenerLetradoDesigna((String)usr.getLocation(), miform.getIdTurno(), miform.getAnio(), miform.getNumero());
		miform.setLetrado(UtilidadesHash.getString(letradoHashtable, "NCOLEGIADO")+" "+UtilidadesHash.getString(letradoHashtable, "NOMBRE"));
		Hashtable pkTurnoHashtable = new Hashtable();
		pkTurnoHashtable.put(ScsTurnoBean.C_IDTURNO, miform.getIdTurno());
		pkTurnoHashtable.put(ScsTurnoBean.C_IDINSTITUCION, (String)usr.getLocation());
		Vector turnoVector = admTurno.selectByPK(pkTurnoHashtable);
		ScsTurnoBean turnoBean = (ScsTurnoBean) turnoVector.get(0) ;
		miform.setTurno(turnoBean.getDescripcion());
		ScsDesignaBean beanDesigna = (ScsDesignaBean)vDesignas.get(0);
		miform.setNumeroProcedimiento(beanDesigna.getNumProcedimiento());
		
		List<ScsJuzgadoBean> alJuzgados = null;
		ScsJuzgadoAdm admJuzgados = new ScsJuzgadoAdm(usr);
		String idJuzgado = "";
		if(miform.getJuzgado()!=null)
			idJuzgado = miform.getJuzgado();
		
		alJuzgados = admJuzgados.getJuzgados((String)usr.getLocation(),miform.getIdTurno(),usr,true, false, idJuzgado);
		if(alJuzgados==null){
			alJuzgados = new ArrayList<ScsJuzgadoBean>();
		
		}
		miform.setJuzgados(alJuzgados);
		miform.setModulos(new ArrayList<ScsProcedimientosBean>());
		
		miform.setFormulario(beanDesigna);

		return "actualizarDesigna";
	}
	protected String actualizaDesigna(ActionMapping mapping, MasterForm formulario,
			HttpServletRequest request, HttpServletResponse response)
			throws ClsExceptions,SIGAException  {
		
		HttpSession ses = request.getSession();
		UsrBean usr = (UsrBean)ses.getAttribute("USRBEAN");
		MaestroDesignasForm miform = (MaestroDesignasForm)formulario;
		
		try {
			ScsDesignaAdm designaAdm = new ScsDesignaAdm (this.getUserBean(request));
			String clavesDesigna[] = { ScsDesignaBean.C_ANIO, ScsDesignaBean.C_NUMERO,
					ScsDesignaBean.C_IDINSTITUCION, ScsDesignaBean.C_IDTURNO };
			String camposDesigna[]={ScsDesignaBean.C_IDINSTITUCIONJUZGADO,ScsDesignaBean.C_IDJUZGADO,ScsDesignaBean.C_IDPROCEDIMIENTO,ScsDesignaBean.C_NUMPROCEDIMIENTO};
			Hashtable<String, String> htDesigna = new Hashtable<String, String>();
			htDesigna.put(ScsDesignaBean.C_IDINSTITUCION,usr.getLocation());
			htDesigna.put(ScsDesignaBean.C_ANIO,miform.getAnio());
			htDesigna.put(ScsDesignaBean.C_IDTURNO,miform.getIdTurno());
			htDesigna.put(ScsDesignaBean.C_NUMERO,miform.getNumero());
			htDesigna.put(ScsDesignaBean.C_NUMPROCEDIMIENTO,miform.getNumeroProcedimiento());
			if(miform.getIdProcedimiento()!=null && !miform.getIdProcedimiento().equals("")&& !miform.getIdProcedimiento().equals("-1"))
				htDesigna.put(ScsDesignaBean.C_IDPROCEDIMIENTO,miform.getIdProcedimiento());
			if(miform.getIdJuzgado()!=null && !miform.getIdJuzgado().equals("")&& !miform.getIdJuzgado().equals("-1")){
				htDesigna.put(ScsDesignaBean.C_IDJUZGADO, miform.getIdJuzgado());
				htDesigna.put(ScsDesignaBean.C_IDINSTITUCIONJUZGADO, usr.getLocation());
			}
			designaAdm.updateDirect(htDesigna, clavesDesigna, camposDesigna);

		}catch(Exception e){
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, null); 
		}
		return exitoModal("messages.updated.success",request);
	}
	
	protected void getAjaxModulos (ActionMapping mapping, 		
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws ClsExceptions, SIGAException ,Exception
			{
		MaestroDesignasForm miForm = (MaestroDesignasForm) formulario;
		UsrBean usr = this.getUserBean(request);
		//Recogemos el parametro enviado por ajax
		String idJuzgado = request.getParameter("idJuzgado");
		miForm.setIdJuzgado(idJuzgado);
		
		//Sacamos las guardias si hay algo selccionado en el turno
		List<ScsProcedimientosBean> modulosList = null;
		if(idJuzgado!= null && !idJuzgado.equals("-1")&& !idJuzgado.equals("")){
			ScsJuzgadoProcedimientoAdm admModulos = new ScsJuzgadoProcedimientoAdm(usr);
			modulosList = admModulos.getModulos(new Integer(idJuzgado),new Integer(usr.getLocation()),true);
		}
		if(modulosList==null){
			modulosList = new ArrayList<ScsProcedimientosBean>();
			
		}
		miForm.setModulos(modulosList);
		respuestaAjax(new AjaxCollectionXmlBuilder<ScsProcedimientosBean>(), modulosList,response);
		
	}
	protected ScsEJGBean abrir(HttpServletRequest request, String anio, String numero, String idtipoEjg) throws SIGAException {
		
		
		Hashtable ejg = null;
		
		ScsEJGBean ejgBean=null;
		try {
			ejgBean = new ScsEJGBean();
			Hashtable miHash = new Hashtable();
			UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
			String nombreTurnoAsistencia="", nombreGuardiaAsistencia="", consultaTurnoAsistencia="", consultaGuardiaAsistencia="";
			ScsEJGAdm admBean =  new ScsEJGAdm(this.getUserBean(request));
			
			int valorPcajgActivo=CajgConfiguracion.getTipoCAJG(new Integer(usr.getLocation()));
			request.setAttribute("PCAJG_ACTIVO", new Integer(valorPcajgActivo));
			// Recuperamos los datos de la clave del EJG. Pueden venir de la request si accedemos por primera vez a esa pestanha
				miHash.put(ScsEJGBean.C_IDINSTITUCION,usr.getLocation());
				miHash.put(ScsEJGBean.C_NUMERO,numero);
				miHash.put(ScsEJGBean.C_ANIO,anio);
				miHash.put(ScsEJGBean.C_IDTIPOEJG,idtipoEjg);
			
			
			// Ahora realizamos la consulta. Primero cogemos los campos que queremos recuperar 
			String consulta = "select ejg.ANIO, ejg.NUMEJG,designa.ESTADO,ejg.IDTIPOEJG AS IDTIPOEJG,ejg.NUMERO_CAJG AS NUMERO_CAJG, ejg.NUMERO, turno.ABREVIATURA AS NOMBRETURNO, guardia.NOMBRE AS NOMBREGUARDIA, guardia.IDGUARDIA AS IDGUARDIA, " + UtilidadesMultidioma.getCampoMultidiomaSimple("tipoejg.DESCRIPCION",this.getUserBean(request).getLanguage()) + " AS TIPOEJG, ejg.IDTIPOEJGCOLEGIO AS IDTIPOEJGCOLEGIO," +
							  "decode(ejg.ORIGENAPERTURA,'M','Manual','S','SOJ','A','ASISTENCIA','DESIGNA'), ejg.IDPRETENSION as IDPRETENSION, ejg.IDPRETENSIONINSTITUCION as IDPRETENSIONINSTITUCION, ejg.idtipodictamenejg as IDTIPODICTAMENEJG, " + 
							  "ejg.FECHAAPERTURA AS FECHAAPERTURA, personajg.NIF AS NIFASISTIDO, personajg.NOMBRE AS NOMBREASISTIDO, personajg.APELLIDO1 AS APELLIDO1ASISTIDO, personajg.APELLIDO2 AS APELLIDO2ASISTIDO, " +
							  " (Select Decode(Ejg.Idtipoencalidad, Null,'', f_Siga_Getrecurso(Tipcal.Descripcion,"+ this.getUserBean(request).getLanguage() + ")) "+
                              "  From Scs_Tipoencalidad Tipcal Where Tipcal.Idtipoencalidad = Ejg.Idtipoencalidad "+
                              "  And Tipcal.Idinstitucion = Ejg.Calidadidinstitucion) as calidad, Ejg.Idtipoencalidad, Ejg.Calidadidinstitucion, "+							  
                              "colegiado.NCOLEGIADO AS NCOLEGIADO, PERSONA.IDPERSONA AS IDPERSONA, persona.NOMBRE AS NOMBRELETRADO, " +
							  "persona.APELLIDOS1 AS APELLIDO1LETRADO, persona.APELLIDOS2 AS APELLIDO2LETRADO, soj.ANIO AS ANIOSOJ, soj.NUMERO AS NUMEROSOJ, soj.NUMSOJ AS CODIGOSOJ, " + UtilidadesMultidioma.getCampoMultidiomaSimple("tiposoj.DESCRIPCION",this.getUserBean(request).getLanguage()) + " AS TIPOSOJ, tiposoj.IDTIPOSOJ AS IDTIPOSOJ, " +
							  "soj.FECHAAPERTURA AS FECHAAPERTURASOJ, asistencia.ANIO AS ANIOASISTENCIA, asistencia.NUMERO AS ASISTENCIANUMERO, asistencia.FECHAHORA AS ASISTENCIAFECHA, " +
							  " ejgd.aniodesigna AS DESIGNA_ANIO,ejgd.idturno AS DESIGNA_IDTURNO,ejgd.numerodesigna AS DESIGNA_NUMERO," +
							  "(SELECT ABREVIATURA FROM scs_turno t WHERE t.idturno = ejgd.IDTURNO and t.IDINSTITUCION = ejg.IDINSTITUCION) DESIGNA_TURNO_NOMBRE, " +
							  "designa.FECHAENTRADA AS FECHAENTRADADESIGNA, " + UtilidadesMultidioma.getCampoMultidiomaSimple("tipoejgcolegio.DESCRIPCION",this.getUserBean(request).getLanguage()) + " AS TIPOEJGCOLEGIO," +
							  "ejg.FECHAPRESENTACION, ejg.FECHALIMITEPRESENTACION, ejg.PROCURADORNECESARIO, ejg.PROCURADOR, ejg.OBSERVACIONES, ejg.DELITOS"+
							  ",ejg."+ScsEJGBean.C_IDPROCURADOR+
							  ",ejg."+ScsEJGBean.C_IDINSTITUCIONPROCURADOR+
							  ",ejg."+ScsEJGBean.C_NUMERO_CAJG+
							  ",ejg."+ScsEJGBean.C_ANIO_CAJG +
							  ",ejg."+ScsEJGBean.C_NUMERODILIGENCIA + " " + ScsEJGBean.C_NUMERODILIGENCIA +
							  ",ejg."+ScsEJGBean.C_NUMEROPROCEDIMIENTO + " " + ScsEJGBean.C_NUMEROPROCEDIMIENTO +
							  ",ejg."+ScsEJGBean.C_JUZGADO + " " + ScsEJGBean.C_JUZGADO +
							  ",ejg."+ScsEJGBean.C_JUZGADOIDINSTITUCION + " " + ScsEJGBean.C_JUZGADOIDINSTITUCION +
							  ",ejg."+ScsEJGBean.C_COMISARIA + " " + ScsEJGBean.C_COMISARIA +
							  ",ejg."+ScsEJGBean.C_COMISARIAIDINSTITUCION + " " + ScsEJGBean.C_COMISARIAIDINSTITUCION +
							  ",ejg."+ScsEJGBean.C_GUARDIATURNO_IDTURNO + " IDTURNO " +
							  ",ejg."+ScsEJGBean.C_SUFIJO + " SUFIJO " + 
							  ",ejg."+ScsEJGBean.C_IDORIGENCAJG + " IDORIGENCAJG " + 
							  ",designa.codigo codigo";
			// Ahora las tablas de donde se sacan los campos
			consulta += " from scs_ejg ejg, scs_personajg personajg, cen_colegiado colegiado, scs_turno turno, scs_guardiasturno guardia, " +
					   "scs_soj soj, scs_designa designa, scs_tipoejg tipoejg, scs_tiposoj tiposoj, scs_asistencia asistencia, scs_tipoejgcolegio tipoejgcolegio, " +
					   "cen_persona persona,scs_ejgdesigna ejgd";
			// Y por último efectuamos la join
			consulta += " where ejg.idinstitucion             = turno.idinstitucion(+) and " +
					      " ejg.guardiaturno_idturno      = turno.idturno(+) and " +     
					      " ejg.IDTIPOEJG                 = tipoejg.IDTIPOEJG and "+
						  "ejg.idinstitucion             = guardia.idinstitucion(+) and " +
						  "ejg.guardiaturno_idturno      = guardia.idturno(+) and " +
						  "ejg.guardiaturno_idguardia    = guardia.idguardia(+) and " +   
						  "personajg.idinstitucion    (+)= ejg.idinstitucion and " +       
						  "personajg.idpersona        (+)= ejg.idpersonajg and " +         
						  "ejg.idinstitucion             = colegiado.idinstitucion(+) and " +    
						  "ejg.idpersona                 = colegiado.idpersona(+) and " +        
						  "soj.idinstitucion (+)= ejg.idinstitucion and " +
						  "soj.ejgidtipoejg	 (+)= ejg.idtipoejg and " +
						  "soj.ejganio (+)         = ejg.anio and " +
						  "soj.ejgnumero(+)        = ejg.numero and " +
						  "asistencia.idinstitucion (+)= ejg.idinstitucion and " +
						  "asistencia.ejganio (+)= ejg.anio and " +
						  "asistencia.ejgnumero (+)= ejg.numero " +
						 
						  "and designa.idinstitucion(+) = ejgd.idinstitucion " +
						  "and designa.anio(+) = ejgd.aniodesigna " +
						  "and designa.numero(+) = ejgd.numerodesigna " +
						  "and designa.idturno(+) = ejgd.idturno " +
						  
						  "and ejg.idinstitucion=ejgd.idinstitucion(+) " +
						  "and ejg.anio=ejgd.anioejg(+) " +
						  "and ejg.numero=ejgd.numeroejg(+) " +
						  "and ejg.idtipoejg=ejgd.idtipoejg(+) and " +

						  "tipoejgcolegio.idinstitucion (+)= ejg.idinstitucion and " +
						  "tipoejgcolegio.idtipoejgcolegio (+)= ejg.idtipoejgcolegio and "+
						  "tiposoj.idtiposoj (+)= soj.idtiposoj and "+
						  "ejg.idpersona = persona.idpersona(+) and ";
			
			
			// Y por último filtramos por la clave principal de ejg
			consulta += " ejg.idtipoejg = " + miHash.get("IDTIPOEJG") + " and ejg.idinstitucion = " + miHash.get("IDINSTITUCION") + " and ejg.anio = " + miHash.get("ANIO") + " and ejg.numero = " + miHash.get("NUMERO");
			
			// jbd inc-6803 Ordenamos para quedarnos solo con la mas moderna
			consulta += " order by designa.fechaentrada desc";
			
			
			// Volvemos a obtener de base de datos la información, para que se la más actúal que hay en la base de datos			
			Vector resultado = admBean.selectGenerico(consulta);
			String fechaapertura="", tipoejg="",dictamen="",fechapresen="", fechalimite="",idorigen="", ano="",num="", sufijo="",estadoEjg="",observaciones="";
			String lenguaje = this.getUserBean(request).getLanguage();
			String idpreten= "";
			String idcomi= "";
			String idjuzgado= "";
			String idcomiInsti= "";
			String idjuzgadoInsti= "";
			String idPretenInsti= "";
			
			try{
				ejg = (Hashtable)resultado.get(0);
			}catch (Exception e) {
				throwExcp("error.general.yanoexiste",e,null);
			}
			String estado = "", nombre="",apellido1="",apellido2="",nombreSolicita="",observa = "",calidad="",nproc="",numDili="";
			if (!resultado.isEmpty()) {apellido1 =(String) ((Hashtable)resultado.get(0)).get("APELLIDO1ASISTIDO");}		
			if (!resultado.isEmpty()) {apellido2 = (String)((Hashtable)resultado.get(0)).get("APELLIDO2ASISTIDO");}		
			if (!resultado.isEmpty()) {nombre = (String) ((Hashtable)resultado.get(0)).get("NOMBREASISTIDO");}	
			if (!resultado.isEmpty()) {observa = (String) ((Hashtable)resultado.get(0)).get("TIPOEJGCOLEGIO");}
			if (!resultado.isEmpty()) {fechaapertura = (String)((Hashtable)resultado.get(0)).get("FECHAAPERTURA");}				
			if (!resultado.isEmpty()) {tipoejg = (String)((Hashtable)resultado.get(0)).get("TIPOEJG");}	
			if (!resultado.isEmpty()) {dictamen = (String)((Hashtable)resultado.get(0)).get("IDTIPODICTAMENEJG");}
			if (!resultado.isEmpty()) {fechapresen = (String)((Hashtable)resultado.get(0)).get("FECHAPRESENTACION");}	
			if (!resultado.isEmpty()) {fechalimite = (String)((Hashtable)resultado.get(0)).get("FECHALIMITEPRESENTACION");}
			if (!resultado.isEmpty()) {ano = (String)((Hashtable)resultado.get(0)).get("ANIOCAJG");}
			if (!resultado.isEmpty()) {num = (String)((Hashtable)resultado.get(0)).get("NUMERO_CAJG");}	
			if (!resultado.isEmpty()) {idorigen = (String)((Hashtable)resultado.get(0)).get("IDORIGENCAJG");}
			if (!resultado.isEmpty()) {sufijo = (String)((Hashtable)resultado.get(0)).get("SUFIJO");}	
			if (!resultado.isEmpty()) {calidad = (String)((Hashtable)resultado.get(0)).get("CALIDAD");}			
			if (!resultado.isEmpty()) {nproc = (String)((Hashtable)resultado.get(0)).get("NUMEROPROCEDIMIENTO");}
			if (!resultado.isEmpty()) {numDili = (String)((Hashtable)resultado.get(0)).get("NUMERODILIGENCIA");}
			if (!resultado.isEmpty()) {observaciones = (String)((Hashtable)resultado.get(0)).get("OBSERVACIONES");}
			if (!resultado.isEmpty()) {idpreten = (String)((Hashtable)resultado.get(0)).get("IDPRETENSION");}			
			if (!resultado.isEmpty()) {idjuzgado = (String)((Hashtable)resultado.get(0)).get("JUZGADO");}
			if (!resultado.isEmpty()) {idcomi = (String)((Hashtable)resultado.get(0)).get("COMISARIA");}
			if (!resultado.isEmpty()) {idjuzgadoInsti = (String)((Hashtable)resultado.get(0)).get("JUZGADOIDINSTITUCION");}
			if (!resultado.isEmpty()) {idcomiInsti = (String)((Hashtable)resultado.get(0)).get("COMISARIAIDINSTITUCION");}						
			if (!resultado.isEmpty()) {idPretenInsti = (String)((Hashtable)resultado.get(0)).get("IDPRETENSIONINSTITUCION");}
			
			String pretension = "", comisaria = "",origen ="", juzgado="";
			if(idpreten!=null && !idpreten.trim().equals("")){
			
				String comboPretensiones="SELECT IDPRETENSION AS ID, f_siga_getrecurso (DESCRIPCION,"+lenguaje+") AS DESCRIPCION "+ 
				"FROM SCS_PRETENSION WHERE IDINSTITUCION = "+idPretenInsti+" AND IDPRETENSION = "+idpreten;
				resultado.clear();
				resultado = admBean.selectGenerico(comboPretensiones);
				
				//Puede que esta consulta no devuelva valores, por tanto hay que controlarlo
				if (!resultado.isEmpty()) { pretension = (String)((Hashtable)resultado.get(0)).get("DESCRIPCION");}
			}
			if(idcomi!=null && !idcomi.trim().equals("")){
				String comboComisariasTurno="SELECT C.IDCOMISARIA || ',' || C.IDINSTITUCION AS ID, c.NOMBRE || ' (' || po.nombre || ')' AS DESCRIPCION " +
						" FROM SCS_COMISARIA c, cen_poblaciones po where c.idpoblacion = po.idpoblacion and c.IDINSTITUCION = "+idcomiInsti+" AND " +
						" c.idcomisaria = "+idcomi;
				resultado.clear();
				resultado = admBean.selectGenerico(comboComisariasTurno);
				
				//Puede que esta consulta no devuelva valores, por tanto hay que controlarlo
				if (!resultado.isEmpty()) { comisaria = (String)((Hashtable)resultado.get(0)).get("DESCRIPCION");}
			}
			if(idorigen!=null && !idorigen.trim().equals("")){
				String origenCAJG="select F_SIGA_GETRECURSO(DESCRIPCION, "+lenguaje+") as DESCRIPCION from scs_origencajg where idorigencajg="+idorigen;
				resultado.clear();
				resultado = admBean.selectGenerico(origenCAJG);
				
				
				//Puede que esta consulta no devuelva valores, por tanto hay que controlarlo
				if (!resultado.isEmpty()) { origen = (String)((Hashtable)resultado.get(0)).get("DESCRIPCION");}
			}
			if(idjuzgado!=null && !idjuzgado.trim().equals("")){
				String comboJuzgados="SELECT IDJUZGADO || ',' || IDINSTITUCION AS ID,decode(j.fechabaja, null ,j.NOMBRE || ' (' || p.nombre || ')'," +
						"j.NOMBRE || ' (' || p.nombre || ') (BAJA)') AS DESCRIPCION FROM SCS_JUZGADO j, cen_poblaciones p WHERE " +
						"j.idpoblacion = p.idpoblacion(+) AND (IDINSTITUCION = "+idjuzgadoInsti+" OR IDINSTITUCION = 2000) and IDJUZGADO=" +idjuzgado;
						
				resultado.clear();
				resultado = admBean.selectGenerico(comboJuzgados);
				
				
				//Puede que esta consulta no devuelva valores, por tanto hay que controlarlo
				if (!resultado.isEmpty()) { juzgado = (String)((Hashtable)resultado.get(0)).get("DESCRIPCION");}
				
			}
			
			consulta = "SELECT F_SIGA_GETRECURSO(F_SIGA_GET_ULTIMOESTADOEJG(" +
					miHash.get("IDINSTITUCION") +
					", " + miHash.get("IDTIPOEJG") +
					", " + miHash.get("ANIO") +
					", " + miHash.get("NUMERO") + "), " + this.getUserBean(request).getLanguage() + ") as DESCRIPCION FROM DUAL";
			
			resultado.clear();
			resultado = admBean.selectGenerico(consulta);
			
			//Puede que esta consulta no devuelva valores, por tanto hay que controlarlo
			if (!resultado.isEmpty()) {estado = (String)((Hashtable)resultado.get(0)).get("DESCRIPCION");}		
			
			
			
			if(apellido1!=null && !apellido1.trim().equals(""))
				nombreSolicita=apellido1;
			if(apellido2!=null)
				nombreSolicita+=" "+apellido2;
			if(nombre!=null  && !nombre.trim().equals(""))
				nombreSolicita+=", "+nombre;
			
			
			// Obtenemos el procurador seleccionado:
			// Obtengo el idProcurador y la idInstitucion del procurador:
			String idProcurador = (String)ejg.get(ScsEJGBean.C_IDPROCURADOR);
			String idInstitucionProcurador = (String)ejg.get(ScsEJGBean.C_IDINSTITUCIONPROCURADOR);
			
			try {
				ScsProcuradorAdm procuradorAdm = new ScsProcuradorAdm(this.getUserBean(request));
				Hashtable h = new Hashtable();
				UtilidadesHash.set(h, ScsProcuradorBean.C_IDPROCURADOR, idProcurador);
				UtilidadesHash.set(h, ScsProcuradorBean.C_IDINSTITUCION, idInstitucionProcurador);
				ScsProcuradorBean b = (ScsProcuradorBean)(procuradorAdm.select(h)).get(0);
				}
			catch (Exception e) {}
			
			String procurador = idProcurador+","+idInstitucionProcurador;
			/*int i = new Integer( (String) miHash.get("NUMERO")).intValue();
			if(sufijo!=null && !sufijo.trim().equals("")){
				ejg.put(ScsEJGBean.C_SUFIJO,sufijo);
				i= i+new Integer(sufijo).intValue();
				ejg.put(ScsEJGBean.C_NUMERO,""+i);
			}	*/
			if(ano!=null)
				ejg.put(ScsEJGBean.C_ANIO_CAJG,ano);
			if(num!=null)
				ejg.put(ScsEJGBean.C_NUMERO_CAJG,num);
			if(origen!=null)
				ejg.put(ScsEJGBean.C_ORIGENAPERTURA,origen);
			if(fechapresen!=null  && !fechapresen.trim().equals(""))
				ejg.put(ScsEJGBean.C_FECHAPRESENTACION,fechapresen.substring(0, 10));
			if(fechalimite!=null&& !fechalimite.trim().equals(""))
				ejg.put(ScsEJGBean.C_FECHALIMITEPRESENTACION,fechalimite.substring(0, 10));

			if(dictamen!=null)
				ejg.put(ScsEJGBean.C_DICTAMEN,dictamen);
			
			if(fechaapertura!=null && !fechaapertura.trim().equals(""))
				ejg.put(ScsEJGBean.C_FECHAAPERTURA,fechaapertura.substring(0, 10));
			if(nombreSolicita!=null)
				ejg.put(ScsEJGBean.C_TIPOLETRADO,nombreSolicita);

			if(procurador!=null)	
				ejg.put(ScsEJGBean.C_PROCURADOR,procurador);
			if(calidad!=null)
				ejg.put(ScsEJGBean.C_CALIDAD,calidad);
			if(nproc!=null)
				ejg.put(ScsEJGBean.C_NUMEROPROCEDIMIENTO,nproc);
			if(numDili!=null)
				ejg.put(ScsEJGBean.C_NUMERODILIGENCIA,numDili);
			
			if(observaciones!=null)
				ejg.put(ScsEJGBean.C_OBSERVACIONES,observaciones);
			ejgBean =(ScsEJGBean) admBean.hashTableToBean(ejg);
			if(tipoejg!=null)
				ejgBean.setDeTipoEjg(tipoejg);
			if(estadoEjg!=null)
				ejgBean.setEstadoEjg(estadoEjg);
			if(observa!=null)
				ejgBean.setTipoEjgCol(observa);
			if(origen!=null)
				ejgBean.setDescripcionOrigen(origen);
			if(comisaria!=null)
				ejgBean.setDescripcionComisaria(comisaria);
			if(juzgado!=null)
				ejgBean.setDescripcionJuzgado(juzgado);
			if(pretension!=null)
				ejgBean.setDescripcionPretension(pretension);
			

		} catch (Exception e) {
			   throwExcp("messages.general.error",e,null);
		}			
		return ejgBean;	
	}

	
}