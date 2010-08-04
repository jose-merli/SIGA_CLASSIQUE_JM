package com.siga.gratuita.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.beans.ScsInscripcionGuardiaAdm;
import com.siga.beans.ScsInscripcionGuardiaBean;
import com.siga.beans.ScsInscripcionTurnoAdm;
import com.siga.beans.ScsInscripcionTurnoBean;
import com.siga.beans.ScsOrdenacionColasAdm;
import com.siga.beans.ScsOrdenacionColasBean;
import com.siga.beans.ScsSaltosCompensacionesAdm;
import com.siga.beans.ScsSaltosCompensacionesBean;
import com.siga.beans.ScsTurnoAdm;
import com.siga.beans.ScsTurnoBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.InscripcionTurno;
import com.siga.gratuita.form.BajaTurnosForm;
import com.siga.gratuita.form.DefinirTurnosForm;


/**
 * @author carlos.vidal
 */

public class BajaTurnosAction extends MasterAction {

	private CenColegiadoBean miForm;

	/**
	 * Esta clase se encarga de validar los turnos de los letrados
	 * El resutado lo manda como variable del request a la jsp que se encargará de mostrar una tabla.
	 * 
	 */
	
	
	
 	public ActionForward executeInternal (ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException 
	{
		String mapDestino = "exception";
		try {
			MasterForm miForm = null;
			miForm = (MasterForm) formulario;
			if (miForm != null) {
				
				String destino = "";
				String accion = miForm.getModo();  
				if (accion != null && accion.equalsIgnoreCase("bajaEnTodosLosTurnos")) {
					destino =  solicitarBajaEnTodosLosTurnos (formulario, request);
				}else if (accion != null && accion.equalsIgnoreCase("abrirModalBaja")) {
					destino =  abrirModalBaja (miForm, request);
				}
				else {
					return super.executeInternal(mapping, formulario,request,response);
				}
				
				return mapping.findForward(destino);
			}
			else{
				return mapping.findForward(this.abrirAvanzada(mapping,miForm,request,response));
			}
		} 
		catch(Exception e)
		{
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
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String abrir (ActionMapping mapping, 		
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException 
	{
		return this.buscarPor(mapping,formulario,request,response);
	}
		
	
	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#buscarPor(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	/** 
	 *  Funcion que atiende a la peticion 'buscarPor'. Busca los turnos para los letrado. 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
	protected String buscarPor(ActionMapping mapping, 
			MasterForm formulario, 
			HttpServletRequest request, 
			HttpServletResponse response) throws SIGAException {
		String forward = "listadoBaja";
		try
		{
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			ScsInscripcionTurnoAdm turnos = new ScsInscripcionTurnoAdm(this.getUserBean(request));
			//Preparamos la select a ejecutar.
			String sql = 
				"SELECT "+
					"inscri."+ScsInscripcionTurnoBean.C_IDINSTITUCION+" IDINSTITUCION,"+
					"inscri."+ScsInscripcionTurnoBean.C_IDPERSONA+" IDPERSONA,"+
					"inscri."+ScsInscripcionTurnoBean.C_IDTURNO+" IDTURNO,"+
					"inscri."+ScsInscripcionTurnoBean.C_FECHASOLICITUD+" FECHASOLICITUD,"+
					"colegi."+CenColegiadoBean.C_NCOLEGIADO+" NCOLEGIADO,"+
					"person."+CenPersonaBean.C_NOMBRE+" NOMBRE_PERSONA,"+
					"person."+CenPersonaBean.C_APELLIDOS1+" APELLIDO1_PERSONA,"+
					"person."+CenPersonaBean.C_APELLIDOS2+" APELLIDO2_PERSONA,"+
					"turnos."+ScsTurnoBean.C_NOMBRE+" NOMBRE_TURNO "+
				"FROM "+
					ScsInscripcionTurnoBean.T_NOMBRETABLA 	+" inscri, "+
					CenColegiadoBean.T_NOMBRETABLA 			+" colegi, "+
					CenPersonaBean.T_NOMBRETABLA 			+" person, "+
					ScsTurnoBean.T_NOMBRETABLA 				+" turnos  ";
			
			String where = "WHERE ";
			//Si es letrado filtramos por idpersona:
			if (usr.getLetrado()) 
				where += "colegi.idpersona="+String.valueOf(usr.getIdPersona())+" AND ";
			
			where +="colegi.idinstitucion 	= inscri.idinstitucion and "+
					"colegi.idpersona 		= inscri.idpersona and "	+
					"person.idpersona		= inscri.idpersona and "	+
					"turnos.idinstitucion 	= inscri.idinstitucion and "+
					"turnos.idturno 		= inscri.idturno and "		+
					"inscri.idinstitucion 	= "+usr.getLocation()+" and " +
					"(inscri.fechasolicitudbaja is not nulL) and "+				
					"inscri.fechabaja 		is null "				+
	//				"order by turnos.nombre, inscri.fechasolicitud";
					"order by inscri.fechasolicitud desc";
			sql += where;
			
			Vector vTurno = turnos.selectTabla(sql);
			request.setAttribute("resultado",vTurno);
			request.setAttribute("localizacion","gratuita.listarTurnosLetrado.literal.localizacionB");
			request.setAttribute("titulo","gratuita.listarTurnosLetrado.literal.tituloB");
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#ver(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	/** 
	 *  Funcion que atiende a la peticion 'ver'. Muestra el detalle del turno seleccionado. 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */		
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
 		String forward = "";
		UsrBean usr;

		try {
			usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			ScsTurnoAdm turno = new ScsTurnoAdm(this.getUserBean(request));
			BajaTurnosForm miForm = (BajaTurnosForm) formulario;			
			String paso	= miForm.getPaso();			
			// Dependiendo de si se solicita la consulta del turno o de las guardias, se ejecuta
			// una opcion u otra.
			
			// RGG 27-02-2006 Hacemos una nhapa porque se está utilizando el mismo Action para dos cosas
			String solBaja = request.getParameter("solBaja");
			if (solBaja!=null && !solBaja.equals("")) request.setAttribute("solBaja",solBaja);
			
			
			if(paso.equals("turno") || paso.equals("turnoS"))
			{
				// OBTENEMOS LOS VALORES PARA EL WHERE DE LA CONSULTA.
				// Dependiendo de si es alta o es validacion se obtinenen
				// los datos necesarios. 
				// Alta: idturno
				// Validacion: IDINSTITUCION,IDPERSONA,IDTURNO
				Integer IDTURNO			= miForm.getIdTurno();
				Integer IDINSTITUCION	= miForm.getIdInstitucion();			
				Integer IDPERSONA		= miForm.getIdPersona();
				String  FECHASOLICITUD	= miForm.getFechaSolicitud();
				
				if (this.comprobarGuardiasDesignasPendientes(
						miForm.getConfirmacion(), request, usr, 
						Long.valueOf(String.valueOf(IDPERSONA)), IDINSTITUCION, IDTURNO)) {
					return "confirmacion";
				}

				// Consultamos los datos para el turno seleccionado
				String where=	" WHERE SCS_PARTIDAPRESUPUESTARIA.idinstitucion (+)= SCS_TURNO.idinstitucion"+            
				   				" AND SCS_PARTIDAPRESUPUESTARIA.idpartidapresupuestaria (+)= SCS_TURNO.idpartidapresupuestaria"+  
								" AND SCS_GRUPOFACTURACION.idinstitucion = SCS_TURNO.idinstitucion"+
								" AND SCS_GRUPOFACTURACION.idgrupofacturacion = SCS_TURNO.idgrupofacturacion"+
								" AND SCS_MATERIA.idinstitucion = SCS_TURNO.idinstitucion"+
								" AND SCS_MATERIA.idarea = SCS_TURNO.idarea"+
								" AND SCS_MATERIA.idmateria = SCS_TURNO.idmateria"+
								" AND SCS_AREA.idinstitucion = SCS_MATERIA.idinstitucion"+
								" AND SCS_AREA.idarea = SCS_MATERIA.idarea"+
								" AND SCS_SUBZONA.idinstitucion (+)= SCS_TURNO.idinstitucion"+
								" AND SCS_SUBZONA.idzona (+)= SCS_TURNO.idzona"+
								" AND SCS_SUBZONA.idsubzona (+)= SCS_TURNO.idsubzona"+
								" AND SCS_ZONA.idinstitucion (+)= SCS_TURNO.idinstitucion"+
								" AND SCS_ZONA.idzona (+)= SCS_TURNO.idzona"+
								" AND CEN_PARTIDOJUDICIAL.idpartido (+)= SCS_SUBZONA.idpartido"+
								" and scs_ordenacioncolas.idordenacioncolas = scs_turno.idordenacioncolas "+
								" AND SCS_INSCRIPCIONTURNO.idturno = SCS_TURNO.idturno"+
								" AND SCS_INSCRIPCIONTURNO.idinstitucion = SCS_TURNO.idinstitucion"+ //jbd INC_01 18-12-2008
								" AND SCS_INSCRIPCIONTURNO.idturno ="+IDTURNO+
								" AND SCS_INSCRIPCIONTURNO.idinstitucion = "+IDINSTITUCION+
								" AND SCS_INSCRIPCIONTURNO.fechasolicitud = TO_DATE('"+FECHASOLICITUD+"','YYYY/MM/DD hh24:mi:ss')"+
								" AND SCS_INSCRIPCIONTURNO.idpersona = "+IDPERSONA+" ";
				// El 2 = letrado.
				Vector vTurno = turno.selectTabla(where,"2");
				request.setAttribute("idinstitucion",IDINSTITUCION);
				//request.setAttribute("action","JGR_BajaTurnos.do");
				request.setAttribute("modo","ver");
				if(paso.equals("turnoS")) 
				{
					request.setAttribute("paso","guardiaS");
					request.setAttribute("titulo","gratuita.consultaTurno.literal.solititle1");
				}
				else
				{
					request.setAttribute("paso","guardia");
					request.setAttribute("titulo","gratuita.consultaTurno.literal.bajatitle1");
				}
				request.setAttribute("idturno",IDTURNO);
				request.setAttribute("idpersona",IDPERSONA);
				request.setAttribute("resultado",vTurno);
				
				// RGG 
				ScsTurnoAdm admTurno = new ScsTurnoAdm(this.getUserBean(request));
				Hashtable aux = new Hashtable();
				aux.put(ScsTurnoBean.C_IDINSTITUCION,usr.getLocation());
				aux.put(ScsTurnoBean.C_IDTURNO,IDTURNO);
				Vector v = admTurno.selectByPK(aux);
				ScsTurnoBean beanTurno = null;
				if (v!=null) {
					beanTurno = (ScsTurnoBean) v.get(0);
				}
				// RGG cambio para ordenacion
				ScsOrdenacionColasBean orden = new ScsOrdenacionColasBean();
				CenPersonaBean personaBean = new CenPersonaBean();
				ScsOrdenacionColasAdm ordenacion = new ScsOrdenacionColasAdm(this.getUserBean(request));
				String condicion =" where "+ScsOrdenacionColasBean.C_IDORDENACIONCOLAS+"="+beanTurno.getIdOrdenacionColas().toString()+" ";
				Vector vOrdenacion = ordenacion.select(condicion);
				orden = (ScsOrdenacionColasBean) vOrdenacion.get(0);
				
				DefinirTurnosForm form = new DefinirTurnosForm();
				form.setAlfabeticoApellidos(orden.getAlfabeticoApellidos().toString());
				form.setAntiguedad(orden.getNumeroColegiado().toString());
				form.setAntiguedadEnCola(orden.getAntiguedadCola().toString());
				form.setEdad(orden.getFechaNacimiento().toString());
				request.setAttribute("DefinirTurnosFormOrdenacion",form);
				
				forward = "verTurno";
			}
			else if(paso.equals("guardia") || paso.equals("guardiaS"))
			{
				Integer IDPERSONA		= miForm.getIdPersona();			
				Integer IDINSTITUCION	= miForm.getIdInstitucion();			
				Integer IDTURNO			= miForm.getIdTurno();			
				String FECHASOLICITUD	= miForm.getFechaSolicitud();			
				String OBSERVACIONESSOLICITUD	= miForm.getObservacionesSolicitud();			
				String FECHASOLICITUDBAJA	= miForm.getFechaSolicitudBaja();			
				String OBSERVACIONESBAJA	= miForm.getObservacionesBaja();			
				String FECHAVALIDACION	= miForm.getFechaValidacion();			
				String OBSERVACIONESVALIDACION	= miForm.getObservacionesValidacion();								
				
				String sql=
				"Select a.IDGUARDIA IDGUARDIA,a.NOMBRE NOMBRE,a.NUMEROLETRADOSGUARDIA NUMEROLETRADOSGUARDIA,"+
				"a.NUMEROSUSTITUTOSGUARDIA NUMEROSUSTITUTOSGUARDIA,a.SELECCIONLABORABLES, a.SELECCIONFESTIVOS, a.TIPODIASGUARDIA TIPODIASGUARDIA,"+
				"a.DIASGUARDIA DIASGUARDIA,a.DIASPAGADOS DIASPAGADOS,"+
				"a.DIASSEPARACIONGUARDIAS DIASSEPARACIONGUARDIAS from "+ScsGuardiasTurnoBean.T_NOMBRETABLA+" a,"+
				ScsInscripcionGuardiaBean.T_NOMBRETABLA+" b"+
				" where b.IDINSTITUCION = " + IDINSTITUCION+
				" and b.IDTURNO = " + IDTURNO+
				" and b.IDPERSONA = " + IDPERSONA+
				" and a.idinstitucion = b.idinstitucion "+
				" and a.idturno = b.idturno "+
				" and a.idguardia = b.idguardia" +
				" and b.fechabaja is null";
				
				Vector vTurno = turno.ejecutaSelect(sql);
				request.setAttribute("resultado",vTurno);
				if(paso.equals("guardiaS"))
					request.setAttribute("titulo","gratuita.altaTurnos_2.literal.solititle2");
				else
					request.setAttribute("titulo","gratuita.altaTurnos_2.literal.bajatitle2");
				request.setAttribute("botones","X,S");
				//request.setAttribute("action","/JGR_BajaTurnos");
				request.setAttribute("modo","modificar");
				request.setAttribute("paso",paso);
				request.setAttribute("IDPERSONA",IDPERSONA);
				request.setAttribute("idturno",IDTURNO);
				request.setAttribute("IDINSTITUCION",IDINSTITUCION);
				request.setAttribute("FECHASOLICITUD",FECHASOLICITUD);
				request.setAttribute("OBSERVACIONESSOLICITUD",OBSERVACIONESSOLICITUD);
				request.setAttribute("FECHAVALIDACION",FECHAVALIDACION);
				request.setAttribute("OBSERVACIONESVALIDACION",OBSERVACIONESVALIDACION);
				request.setAttribute("FECHASOLICITUDBAJA",FECHASOLICITUDBAJA);
				request.setAttribute("OBSERVACIONESBAJA",OBSERVACIONESBAJA);
				forward = "verGuardia";
			}
			
			// RGG continuación de la nhapa
			if (solBaja==null || solBaja.equals("")) {
				request.setAttribute("action","/JGR_BajaTurnos");
			} else {
				request.setAttribute("action","/JGR_SolicitarBajaTurno");
			}
			
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#nuevo(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	/** 
	 *  Funcion que atiende a la peticion 'nuevo'. Existen dos posibilidades dependiendo del valor del paso.
	 *  Si el paso es = 1, consulta las guardias para el turno seleccionado, por el contrario, si el paso != 1
	 *  nos muestra una pantalla donde se solicita fecha de solicitud y observaciones a la solicitud. 
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  SIGAException  En cualquier caso de error
	 */
		
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "nuevo";
		try
		{
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			BajaTurnosForm form = (BajaTurnosForm) formulario;
			Integer idTurno 	= (Integer)form.getIdTurno();
			request.setAttribute("idturno"			,(Integer)form.getIdTurno());
			request.setAttribute("IDPERSONA"		,(Integer)form.getIdPersona());
			request.setAttribute("IDINSTITUCION"	,(Integer)form.getIdInstitucion());
			request.setAttribute("FECHASOLICITUD"	,(String)form.getFechaSolicitud());
			request.setAttribute("OBSERVACIONESSOLICITUD"	,(String)form.getObservacionesSolicitud());
			request.setAttribute("FECHASOLICITUDBAJA"		,(String)form.getFechaSolicitudBaja());
			request.setAttribute("OBSERVACIONESBAJA"		,(String)form.getObservacionesBaja());
			request.setAttribute("FECHAVALIDACION"			,(String)form.getFechaValidacion());
			request.setAttribute("OBSERVACIONESVALIDACION"	,(String)form.getObservacionesValidacion());
			request.setAttribute("botones","C,F");
			if(form.getPaso().equals("guardiaS"))
				request.setAttribute("titulo","gratuita.altaTurnos.literal.solititle3");
			else
				request.setAttribute("titulo","gratuita.altaTurnos.literal.solititle3");
			
			// RGG 27-02-2006 Hacemos una nhapa porque se está utilizando el mismo Action para dos cosas
			String solBaja = request.getParameter("solBaja");
			if (solBaja==null || solBaja.equals("")) {
				request.setAttribute("action","/JGR_BajaTurnos");
			} else {
				request.setAttribute("solBaja",solBaja);
				request.setAttribute("action","/JGR_SolicitarBajaTurno");
			}
			
			request.setAttribute("paso",form.getPaso());
			request.setAttribute("accion",form.getModo());
			
			// RGG 
			ScsTurnoAdm admTurno = new ScsTurnoAdm(this.getUserBean(request));
			Hashtable aux = new Hashtable();
			aux.put(ScsTurnoBean.C_IDINSTITUCION,form.getIdInstitucion());
			aux.put(ScsTurnoBean.C_IDTURNO,form.getIdTurno());
			Vector v = admTurno.selectByPK(aux);
			ScsTurnoBean beanTurno = null;
			if (v!=null) {
				beanTurno = (ScsTurnoBean) v.get(0);
			}
			request.setAttribute("VALIDARINSCRIPCIONES"	,beanTurno.getValidarInscripciones());
			
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}
	
	/**
	 * Ordena la baja en un turno de un colegiado
	 */
	protected String modificar (ActionMapping mapping,
								MasterForm formulario,
								HttpServletRequest request,
								HttpServletResponse response)
		throws SIGAException
	{
		UserTransaction tx = null;
		String forward = "";
		
		try {
			//Controles generales
			BajaTurnosForm miForm = (BajaTurnosForm) formulario;			
			UsrBean usr = this.getUserBean(request);
			
			//Variables generales
			Long idPersona = Long.valueOf(String.valueOf(miForm.getIdPersona()));
			Integer idInstitucion = miForm.getIdInstitucion();
			Integer idTurno = miForm.getIdTurno();
			
			//obteniendo parametros de baja
			String fechaBaja = GstDate.getApplicationFormatDate(
					usr.getLanguage(), miForm.getFechaBaja());
			String fechaSolicitudBaja = GstDate.getApplicationFormatDate(
				usr.getLanguage(), miForm.getFechaSolicitudBaja());
			String observaciones = miForm.getObservacionesBaja();
			String fechaSolicitud = miForm.getFechaSolicitud();
			
			//iniciando transaccion
			tx = usr.getTransaction();
	        tx.begin();
	        
	        //dando de baja el turno
			InscripcionTurno inscripcion = InscripcionTurno.getInscripcionTurno(
					idInstitucion, idTurno, idPersona, fechaSolicitud, usr, true);
			inscripcion.solicitarBaja(fechaSolicitudBaja, observaciones, usr);
			if (fechaBaja != null && !fechaBaja.equals(""))
				inscripcion.validarBaja(fechaBaja, observaciones, usr);
			
			//finalizando transaccion
			tx.commit();
			
			
			//Lo siguiente hay que integrarlo en la baja de la guardia
			//Y supongo que tambien en la baja del turno
			//
			//  Aqui se da fecha de cumplimentacion a las compensaciones y saltos para que no se tengan en cuenta a la hora de asignar
			/*
			ScsSaltosCompensacionesAdm admSaltosYCompensaciones = new ScsSaltosCompensacionesAdm(this.getUserBean(request));
			Hashtable registro = new Hashtable();
			UtilidadesHash.set(registro,ScsSaltosCompensacionesBean.C_IDINSTITUCION, miForm.getIdInstitucion());
			UtilidadesHash.set(registro,ScsSaltosCompensacionesBean.C_IDTURNO, miForm.getIdTurno());
			UtilidadesHash.set(registro,ScsSaltosCompensacionesBean.C_IDPERSONA, miForm.getIdPersona());
			UtilidadesHash.set(registro,ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO, (String)miForm.getFechaBaja());
			UtilidadesHash.set(registro,ScsSaltosCompensacionesBean.C_MOTIVOS, 
					UtilidadesString.getMensajeIdioma(this.getUserBean(request), "gratuita.modalMantenimiento_SaltosYCompensaciones.literal.letradoBaja"));
			admSaltosYCompensaciones.updateCompensacionesSaltos(registro);
			*/
			
			forward = "exito";
	        request.setAttribute("modal", "1");
		}
		catch (Exception e) 
		{
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx); 
		} 
		
		return forward;
		
	} //modificar()
	
	/**
	 * Ordena la baja en todos los turnos de un colegiado
	 */
	protected String solicitarBajaEnTodosLosTurnos (ActionForm formulario,
													HttpServletRequest request)
		throws SIGAException 
	{
		UserTransaction tx = null;
		
		try {
			//Controles generales
			UsrBean usr = this.getUserBean(request);
			Integer idInstitucion = this.getIDInstitucion(request);
			String idInstitucionStr = String.valueOf(idInstitucion);
			String idPersonaStr = (String) request.getSession().getAttribute("idPersonaTurno");
			Long idPersona = Long.valueOf(idPersonaStr);
			
			//obteniendo parametros de baja
			String fechaBaja          = "SYSDATE";
			String fechaSolicitudBaja = "SYSDATE";
			String observaciones      = UtilidadesString.getMensajeIdioma(
					usr, "censo.sjcs.turnos.bajaEnTodosLosTurnos.observacion.literal");
			
			//obteniendo los turnos
			Vector vTurno = new ScsTurnoAdm(usr).getTurnosLetrado(idInstitucionStr, idPersonaStr);
			if (vTurno == null) 
				return "exito";
			
			//comprobando si hay alguna guardia pendiente, si hay alguna pendiente avisamos
			for (int i = 0; i < vTurno.size(); i++) {
				Hashtable turnoHash = (Hashtable) vTurno.get(i);
				Integer idTurno = UtilidadesHash.getInteger(turnoHash, "IDTURNO");
				if (this.comprobarGuardiasDesignasPendientes(
						((BajaTurnosForm) formulario).getConfirmacion(), 
						request, usr, idPersona, idInstitucion, idTurno)) {
					return "confirmacion";
				}
			}
			
			//iniciando transaccion
			tx = usr.getTransaction();
	        tx.begin();                 
	        
	        //dando de baja todos los turnos
	        InscripcionTurno inscripcion;
			for (int i = 0; i < vTurno.size(); i++) {
				Hashtable turnoHash = (Hashtable)vTurno.get(i);

				inscripcion = InscripcionTurno.getInscripcionTurno(
						idInstitucion, 
						Integer.valueOf((String) turnoHash.get("IDTURNO")), 
						idPersona, 
						(String) turnoHash.get("FECHASOLICITUD"), 
						usr, true);
				inscripcion.solicitarBaja(fechaSolicitudBaja, observaciones, usr);
				if (!UtilidadesString.stringToBoolean((String)turnoHash.get("VALIDARINSCRIPCIONES")))
					inscripcion.validarBaja(fechaBaja, observaciones, usr);
			} //for
			
			//finalizando transaccion
			tx.commit();
		}
		catch (Exception e) 
		{
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx);
		}
		
		return this.exitoRefresco("messages.updated.success", request);
		
	} //solicitarBajaEnTodosLosTurnos()
	
	private boolean comprobarGuardiasDesignasPendientes (String confirmacion,
														 HttpServletRequest request,
														 UsrBean usr,
														 Long idPersona,
														 Integer idInstitucion,
														 Integer idTurno)
		throws SIGAException 
	{
		if (confirmacion == null || confirmacion.equals("") || confirmacion.equals("0")) {
			// Devuelve el nivel de error:
			//	  0: NO hay nada pendiente
			//	  1: Error, tiene guardias pendientes
			//	  2: Error, tiene designas pendientes
			//	  3: Excepcion
			CenClienteAdm admCliente = new CenClienteAdm(usr);
			switch (admCliente.tieneTrabajosSJCSPendientes(
					idPersona, idInstitucion, idTurno))
			{
				case 1: request.setAttribute("mensajeConfirmacion", 
								UtilidadesString.getMensajeIdioma(usr, 
										"error.message.bajaTurno.guardiasPendientes"));
						return true;
				case 2:	request.setAttribute("mensajeConfirmacion", 
								UtilidadesString.getMensajeIdioma(usr, 
										"error.message.bajaTurno.designasPendientes"));
						return true;
				case 3: throw new SIGAException(admCliente.getError());
				default: return true;
			}
		}
		
		return false; // No hay nada pendiente
	} //comprobarGuardiasDesignasPendientes()
	
	
	
	protected String abrirModalBaja (
			MasterForm formulario, 
			HttpServletRequest request) throws SIGAException 
	{
		return "abrirModalBaja";
	}
}