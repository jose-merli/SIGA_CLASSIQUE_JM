package com.siga.gratuita.action;
import java.util.ArrayList;
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

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.AjaxCollectionXmlBuilder;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.ScsAsistenciasAdm;
import com.siga.beans.ScsAsistenciasBean;
import com.siga.beans.ScsDesignaAdm;
import com.siga.beans.ScsDesignaBean;
import com.siga.beans.ScsDesignasLetradoAdm;
import com.siga.beans.ScsDesignasLetradoBean;
import com.siga.beans.ScsGuardiasTurnoAdm;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.beans.ScsJuzgadoAdm;
import com.siga.beans.ScsJuzgadoBean;
import com.siga.beans.ScsJuzgadoProcedimientoAdm;
import com.siga.beans.ScsProcedimientosAdm;
import com.siga.beans.ScsProcedimientosBean;
import com.siga.beans.ScsTipoDesignaColegioAdm;
import com.siga.beans.ScsTipoDesignaColegioBean;
import com.siga.beans.ScsTurnoAdm;
import com.siga.beans.ScsTurnoBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.BuscarDesignasForm;
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
			
			//recuperando el idJuzgado.
			 idJuzgado = beanDesigna.getIdJuzgado().toString();
						
			consultaTurno=" where idTurno = " + beanDesigna.getIdTurno() + " and idinstitucion="+usr.getLocation()+" ";
			consultaTipoDesigna = " where "+ScsTipoDesignaColegioBean.C_IDTIPODESIGNACOLEGIADO +" = " + request.getParameter("TIPODESIGNA") + " and idinstitucion ="+ usr.getLocation()+" ";

			nombreTurno = ((ScsTurnoBean)((Vector)turno.select(consultaTurno)).get(0)).getAbreviatura();
			nombreTipoDesigna = ((ScsTipoDesignaColegioBean)((Vector)tipodesigna.select(consultaTipoDesigna)).get(0)).getDescripcion();
		} catch(Exception e){ 
			consultaTipoDesigna = " where "+ScsTipoDesignaColegioBean.C_IDTIPODESIGNACOLEGIADO +" = " + beanDesigna.getIdTipoDesignaColegio() + " and idinstitucion ="+ usr.getLocation()+" ";
			if ((beanDesigna.getIdTipoDesignaColegio()!=null)&&(!(beanDesigna.getIdTipoDesignaColegio()).equals("")))
				nombreTipoDesigna = ((ScsTipoDesignaColegioBean)((Vector)tipodesigna.select(consultaTipoDesigna)).get(0)).getDescripcion();
		}
		try {
			resultado = beanDesigna.getOriginalHash();
			UtilidadesHash.set(resultado,"TURNO", 								nombreTurno);
			UtilidadesHash.set(resultado,"TIPODESIGNA",							nombreTipoDesigna);
			
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
		boolean ok=false;
		UserTransaction tx = null;
		String aux2 = "";
		String Fechaoficiojuzgado="";

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
						String fechaApertura = GstDate.getApplicationFormatDate("",(String)datosEntrada.get("FECHA"));
						if (fechaApertura!=null && !fechaApertura.equals("")) {
							//String aux = fechaApertura.substring(0,fechaApertura.length()-9);
							designaNueva.put(ScsDesignaBean.C_FECHAENTRADA,fechaApertura);		
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
		
		Hashtable filtro = new Hashtable();
		UtilidadesHash.set(filtro,ScsDesignaBean.C_ANIO, 				miform.getAnio());
		UtilidadesHash.set(filtro,ScsDesignaBean.C_NUMERO, 				miform.getNumero());
		UtilidadesHash.set(filtro,ScsDesignaBean.C_IDINSTITUCION,		(String)usr.getLocation());
		UtilidadesHash.set(filtro,ScsDesignaBean.C_IDTURNO,				miform.getIdTurno());
		
		
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
		
		
		List<ScsJuzgadoBean> alJuzgados = null;
		ScsJuzgadoAdm admJuzgados = new ScsJuzgadoAdm(usr);
		alJuzgados = admJuzgados.getJuzgados((String)usr.getLocation(),miform.getIdTurno(),usr);
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
			String camposDesigna[]={ScsDesignaBean.C_IDINSTITUCIONJUZGADO,ScsDesignaBean.C_IDJUZGADO,ScsDesignaBean.C_IDPROCEDIMIENTO};
			Hashtable<String, String> htDesigna = new Hashtable<String, String>();
			htDesigna.put(ScsDesignaBean.C_IDINSTITUCION,usr.getLocation());
			htDesigna.put(ScsDesignaBean.C_ANIO,miform.getAnio());
			htDesigna.put(ScsDesignaBean.C_IDTURNO,miform.getIdTurno());
			htDesigna.put(ScsDesignaBean.C_NUMERO,miform.getNumero());
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
	
}