package com.siga.gratuita.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.*;
import java.util.*;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.*;

/**
 * @author ruben.fernandez
 */

public class DefinirTurnosAction extends MasterAction {

	protected String editar(ActionMapping mapping, 
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws SIGAException {

		
		String forward="";
		Vector campos = new Vector();
		Vector ocultos = new Vector();
		
		try {
		
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			DefinirTurnosForm form = (DefinirTurnosForm) formulario;
			campos = (Vector)form.getDatosTablaVisibles(0);
			ocultos = (Vector)form.getDatosTablaOcultos(0);
			String idTurno ="";
			if (ocultos==null) {
				idTurno=(String)request.getSession().getAttribute("IDTURNOSESION");
			} else {
				idTurno = (String)ocultos.get(0);
				if (idTurno==null) {
					idTurno=(String)request.getSession().getAttribute("IDTURNOSESION");
				}
			}
			
			//Datos del Turno:
			ScsTurnoAdm turnoAdm = new ScsTurnoAdm(this.getUserBean(request));
			Hashtable miTurno = turnoAdm.getDatosTurno(usr.getLocation(), idTurno);
			//request.setAttribute("turnoElegido", miTurno);
			request.getSession().setAttribute("turnoElegido", miTurno);
			
			request.getSession().setAttribute("IDTURNOSESION",idTurno);
		
			request.getSession().setAttribute("accionTurno","Editar");
			
			Hashtable datosPestanas = new Hashtable();
			datosPestanas.put("accion","Ver");
			datosPestanas.put("turno",idTurno);
			datosPestanas.put("institucion",usr.getLocation());
			request.setAttribute("datosTurnoPestana",datosPestanas);

		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
		
		return "editar";
	}
	
	protected String ver(	ActionMapping mapping, 
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws ClsExceptions, SIGAException {
		//los campos del vector campos son: 
		//abreviatura , nombre , nombreArea , nombreMateria, nombreZona, nombreSubzona, nombrePartidoJudicial, nombrePartidaPresupuestaria, nombreGrupoFacturacion 
		//los campos del vector ocultos son: 
		//IDTURNO, GUARDIAS, VALIDARJUSTIFICACIONES, VALIDARINSCRIPCIONES, DESIGNADIRECTA, REPARTOPORPUNTOS, DESCRIPCION,
		//REQUISITOS,IDORDENACIONCOLAS,IDPERSONAULTIMO,IDAREA,IDMATERIA,IDZONA,IDSUBZONA,IDPARTIDAPRESUPUESTARIA,IDGRUPOFACTURACION,
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		Vector campos = new Vector(); 
		Vector ocultos = new Vector();
		String forward="";
		
		try {
			
			String entrada= (String) request.getSession().getAttribute("entrada");
			DefinirTurnosForm form = (DefinirTurnosForm) formulario;
			campos = (Vector)form.getDatosTablaVisibles(0);
			ocultos = (Vector)form.getDatosTablaOcultos(0);
			String idTurno ="";
			if (ocultos==null) {
				idTurno=(String)request.getSession().getAttribute("IDTURNOSESION");
			} else {
				idTurno = (String)ocultos.get(0);
				if (idTurno==null) {
					idTurno=(String)request.getSession().getAttribute("IDTURNOSESION");
				}
			}
			request.getSession().setAttribute("IDTURNOSESION",idTurno);
/*
			String idTurno ="";
			try{
				idTurno = (String)ocultos.get(0);
			}catch(Exception e){
				idTurno = (String)request.getAttribute("idTurno");
			}
*/			

			String consulta =	" select turno.nombre nombre, turno.abreviatura abreviatura, turno.idarea idarea, turno.idmateria idmateria, turno.idzona idzona, "+
								" turno.idpartidapresupuestaria idpartidapresupuestaria, turno.idgrupofacturacion idgrupofacturacion, turno.guardias guardias, turno.descripcion descripcion,"+
								" turno.requisitos requisitos, turno.idordenacioncolas idordenacioncolas, turno.idpersona_ultimo idpersona_ultimo, turno.idsubzona idsubzona, area.nombre area,"+
								" materia.nombre materia, zona.nombre zona, subzona.nombre subzona, partida.nombrepartida partidapresupuestaria, turno.idordenacioncolas idordenacioncolas, turno.idturno idturno, turno.validarjustificaciones validarjustificaciones, turno.validarinscripciones validarinscripciones,"+
								" turno.designadirecta designadirecta, turno.repartoporpuntos repartoporpuntos, subzona.idpartido idpartidojudicial, " +
								// RGG partido.nombre partidojudicial"+
								"  PKG_SIGA_SJCS.FUN_SJ_PARTIDOSJUDICIALES(turno.idinstitucion, turno.idsubzona, turno.idzona) partidojudicial " +
								" from scs_turno turno, scs_area area, scs_materia materia, scs_zona zona, scs_subzona subzona, scs_partidapresupuestaria partida, cen_partidojudicial partido"+
								" where area.idinstitucion = turno.idinstitucion"+
								" and area.idarea = turno.idarea"+
								" and partida.idinstitucion = turno.idinstitucion"+
								" and partida.idpartidapresupuestaria = turno.idpartidapresupuestaria"+
								" and materia.idinstitucion = turno.idinstitucion"+
								" and materia.idmateria = turno.idmateria"+
								" and materia.idarea = turno.idarea"+
								" and zona.idinstitucion = turno.idinstitucion"+
								" and zona.idzona = turno.idzona"+
								" and subzona.idinstitucion (+)= turno.idinstitucion"+
								" and subzona.idzona (+)= turno.idzona"+
								" and subzona.idsubzona (+)= turno.idsubzona"+
								" and partido.idpartido (+)= subzona.idpartido"+
								" and turno.idinstitucion ="+usr.getLocation()+
								" AND turno.idturno ="+idTurno;
	
			ScsTurnoAdm turno = new ScsTurnoAdm(this.getUserBean(request));
			Hashtable miTurno = (Hashtable)((Vector)turno.ejecutaSelect(consulta)).get(0);
			request.getSession().setAttribute("turnoElegido",miTurno);
			request.getSession().removeAttribute("accionTurno");
			request.getSession().setAttribute("accionTurno","Ver");
			request.getSession().setAttribute("campos",campos);
			request.getSession().setAttribute("ocultos",ocultos);
	
			Hashtable datosPestanas = new Hashtable();
			datosPestanas.put("accion","Ver");
			datosPestanas.put("turno",idTurno);
			datosPestanas.put("institucion",usr.getLocation());
			request.setAttribute("datosTurnoPestana",datosPestanas);
			
			// RGG cambio para ordenacion
			ScsOrdenacionColasBean orden = new ScsOrdenacionColasBean();
			CenPersonaBean personaBean = new CenPersonaBean();
			ScsOrdenacionColasAdm ordenacion = new ScsOrdenacionColasAdm(this.getUserBean(request));
			String condicion =" where "+ScsOrdenacionColasBean.C_IDORDENACIONCOLAS+"="+(String)miTurno.get("IDORDENACIONCOLAS")+" ";
			Vector vOrdenacion = ordenacion.select(condicion);
			orden = (ScsOrdenacionColasBean) vOrdenacion.get(0);
			form.setAlfabeticoApellidos(orden.getAlfabeticoApellidos().toString());
			form.setAntiguedad(orden.getNumeroColegiado().toString());
			form.setAntiguedadEnCola(orden.getAntiguedadCola().toString());
			form.setEdad(orden.getFechaNacimiento().toString());
			
			
			if (entrada.equalsIgnoreCase("1")){
				forward= "editar";
			}else{
				forward= "editarLetrado";
			}

		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
		return forward;
	}
	
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions {
		request.getSession().setAttribute("accionTurno","nuevo");
		return "nuevo";
	}
	
	protected String insertar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		request.getSession().setAttribute("accion","insertar");
		UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
		boolean ok = false;
		DefinirTurnosForm miform = (DefinirTurnosForm)formulario;
		Hashtable hash = miform.getDatos();
		Hashtable insercion = null;
		String forward = "";
		
		ScsTurnoAdm turno = new ScsTurnoAdm(this.getUserBean(request));
		
		try {
			//Chequeo que el IDMATERIA no sea nulo:
			if (miform.getMateria()==null || miform.getMateria().trim().equals(""))
				return exito("gratuita.definirTurnosIndex.errorMateria",request);
			
			hash.put("USUMOD",usr.getUserName());
			hash.put("IDINSTITUCION",usr.getLocation());
			turno.prepararInsert(hash);
			Hashtable haux = new Hashtable();
			Vector v = new Vector();
			String where =" where "+
			ScsOrdenacionColasBean.C_ALFABETICOAPELLIDOS+"="+(String)hash.get("ALFABETICOAPELLIDOS")+" and "+
			ScsOrdenacionColasBean.C_ANTIGUEDADCOLA+"="+(String)hash.get("ANTIGUEDADENCOLA")+" and "+
			ScsOrdenacionColasBean.C_FECHANACIMIENTO+"="+(String)hash.get("EDAD")+" and "+
			ScsOrdenacionColasBean.C_NUMEROCOLEGIADO+"="+(String)hash.get("ANTIGUEDAD")+" ";
			ScsOrdenacionColasAdm ordenacion = 	new ScsOrdenacionColasAdm(this.getUserBean(request));
			v=ordenacion.select(where);
			if (v.size()>0){
				ScsOrdenacionColasBean b = (ScsOrdenacionColasBean)v.get(0);
				hash.put("IDORDENACIONCOLAS",((Integer)b.getIdOrdenacionColas()).toString());
			}else{
				ordenacion.prepararInsert(hash);
/*				haux.put(ScsOrdenacionColasBean.C_ALFABETICOAPELLIDOS,(String)hash.get("ALFABETICOAPELLIDOS"));
				haux.put(ScsOrdenacionColasBean.C_ANTIGUEDADCOLA,(String)hash.get("ANTIGUEDADENCOLA"));
				haux.put(ScsOrdenacionColasBean.C_FECHANACIMIENTO,(String)hash.get("EDAD"));
				haux.put(ScsOrdenacionColasBean.C_NUMEROCOLEGIADO,(String)hash.get("ANTIGUEDAD"));
*/				
				haux.put(ScsOrdenacionColasBean.C_ALFABETICOAPELLIDOS,miform.getAlfabeticoApellidos());
				haux.put(ScsOrdenacionColasBean.C_ANTIGUEDADCOLA,miform.getAntiguedadEnCola());
				haux.put(ScsOrdenacionColasBean.C_FECHANACIMIENTO,miform.getEdad());
				haux.put(ScsOrdenacionColasBean.C_NUMEROCOLEGIADO,miform.getAntiguedad());
				
				haux.put(ScsOrdenacionColasBean.C_USUMODIFICACION,(String)usr.getUserName());
				haux.put(ScsOrdenacionColasBean.C_FECHAMODIFICACION,"sysdate");
				haux.put(ScsOrdenacionColasBean.C_IDORDENACIONCOLAS,hash.get("IDORDENACIONCOLAS"));
				ordenacion.insert(haux);
			}
			
			if (miform.getActivarRestriccionActuacion() != null && UtilidadesString.stringToBoolean(miform.getActivarRestriccionActuacion())) {
				hash.put(ScsTurnoBean.C_ACTIVARRETRICCIONACREDIT, ClsConstants.DB_TRUE);
			}
			else { 
				hash.put (ScsTurnoBean.C_ACTIVARRETRICCIONACREDIT, ClsConstants.DB_FALSE);
			}

			if (miform.getActivarActuacionesLetrado() != null && UtilidadesString.stringToBoolean(miform.getActivarActuacionesLetrado())) {
				hash.put(ScsTurnoBean.C_LETRADOACTUACIONES, ClsConstants.DB_TRUE);
			}
			else { 
				hash.put (ScsTurnoBean.C_LETRADOACTUACIONES, ClsConstants.DB_FALSE);
			}

			if (miform.getActivarAsistenciasLetrado() != null && UtilidadesString.stringToBoolean(miform.getActivarAsistenciasLetrado())) {
				hash.put(ScsTurnoBean.C_LETRADOASISTENCIAS, ClsConstants.DB_TRUE);
			}
			else { 
				hash.put (ScsTurnoBean.C_LETRADOASISTENCIAS, ClsConstants.DB_FALSE);
			}

			insercion = prepararHash(hash);
			if (turno.insert(insercion))
		    {
		        request.setAttribute("descOperation","messages.deleted.success");
		    }
		    
		    else
		    {
		        request.setAttribute("descOperation","error.messages.deleted");
		    }

			//request.setAttribute("idTurno",(String)hash.get("IDTURNO"));
			request.getSession().setAttribute("IDTURNOSESION",(String)insercion.get("IDTURNO"));
			//return this.editar(mapping, formulario, request, response);
			forward = exitoRefresco("messages.inserted.success",request);

		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
		
		return forward;
		
	}
	
	public Hashtable prepararHash(Hashtable hash){
		Hashtable aux = new Hashtable();
		//IDAREA:
		if ((UtilidadesHash.getInteger(hash,"IDAREA")).intValue()>0)aux.put("IDAREA",(hash.get("IDAREA")));
		//IDMATERIA:
		if ((UtilidadesHash.getInteger(hash,"IDMATERIA")).intValue()>0)aux.put("IDMATERIA",(hash.get("IDMATERIA")));
		//IDZONA:
		if ((UtilidadesHash.getInteger(hash,"IDZONA")).intValue()>0)aux.put("IDZONA",(hash.get("IDZONA")));
		//IDSUBZONA:
		try{
			if ((UtilidadesHash.getInteger(hash,"IDSUBZONA")).intValue()>0)aux.put("IDSUBZONA",(hash.get("IDSUBZONA")));
		}
		catch(Exception e){
			aux.put("IDSUBZONA","null");
		}
		//IDGRUPOFACTURACION:
		if ((UtilidadesHash.getInteger(hash,"IDGRUPOFACTURACION")).intValue()>0)aux.put("IDGRUPOFACTURACION",(hash.get("IDGRUPOFACTURACION")));
		
		//IDPERSONAULTIMO:
		try{
			if ((UtilidadesHash.getInteger(hash,"IDPERSONAULTIMO")).intValue()>0)aux.put("IDPERSONAULTIMO",(hash.get("IDPERSONAULTIMO")));
		}catch(Exception e){
			aux.put("IDPERSONAULTIMO","null");
		}
		//IDPARTIDAPRESUPUESTARIA:
		if ((UtilidadesHash.getInteger(hash,"IDPARTIDAPRESUPUESTARIA")).intValue()>0)aux.put("IDPARTIDAPRESUPUESTARIA",(hash.get("IDPARTIDAPRESUPUESTARIA")));
		//VALIDARJUSTIFICACIONES:
		try{
			if (((String)hash.get("VALIDARJUSTIFICACIONES")).equalsIgnoreCase("on"))
				aux.put("VALIDARJUSTIFICACIONES","S");
		}catch(Exception e){
			aux.put("VALIDARJUSTIFICACIONES","N");
		}
		//DESIGNADIRECTA:
		try{
			if (((String)hash.get("DESIGNADIRECTA")).equalsIgnoreCase("on"))
				aux.put("DESIGNADIRECTA","S");
		}catch(Exception e){
			aux.put("DESIGNADIRECTA","N");
		}
		//REPARTOPORPUNTOS
		try{
			if (((String)hash.get("REPARTOPORPUNTOS")).equalsIgnoreCase("on"))aux.put("REPARTOPORPUNTOS","S");
		}catch(Exception e){
			aux.put("REPARTOPORPUNTOS","N");
		}
		//VALIDARINSCRIPCIONES:
		try {
			if (((String)hash.get("VALIDARINSCRIPCIONES")).equalsIgnoreCase("on"))aux.put("VALIDARINSCRIPCIONES","S");
		}catch(Exception e){
			aux.put("VALIDARINSCRIPCIONES","N");
		}
		//IDPARTIDOJUDICIAL:
		try {
			aux.put("IDPARTIDOJUDICIAL",(hash.get("IDPARTIDOJUDICIAL")));
		}catch(Exception e){}
		aux.put("IDTURNO",(hash.get("IDTURNO")));
		aux.put("DESCRIPCION",(hash.get("DESCRIPCION")));
		aux.put("REQUISITOS",(hash.get("REQUISITOS")));
		aux.put("IDORDENACIONCOLAS",(hash.get("IDORDENACIONCOLAS")));
		aux.put("IDINSTITUCION",(hash.get("IDINSTITUCION")));
		aux.put("ABREVIATURA",(hash.get("ABREVIATURA")));
		aux.put("NOMBRE",(hash.get("NOMBRE")));
		aux.put("GUARDIAS",(hash.get("GUARDIAS")));
		aux.put("MODO",(hash.get("MODO")));

		aux.put(ScsTurnoBean.C_ACTIVARRETRICCIONACREDIT,(hash.get(ScsTurnoBean.C_ACTIVARRETRICCIONACREDIT)));
		aux.put(ScsTurnoBean.C_LETRADOACTUACIONES,(hash.get(ScsTurnoBean.C_LETRADOACTUACIONES)));
		aux.put(ScsTurnoBean.C_LETRADOASISTENCIAS,(hash.get(ScsTurnoBean.C_LETRADOASISTENCIAS)));
		return aux;
	}
	
	protected String modificar(	ActionMapping mapping, 
								MasterForm formulario, 
								HttpServletRequest request, 
								HttpServletResponse response) throws ClsExceptions, SIGAException {
		String forward="";
		boolean ok= false, paso = false;
		DefinirTurnosForm miform = (DefinirTurnosForm)formulario;
		UserTransaction tx =null;
		
		try {
			String validarAltasBajas=request.getParameter("validarAltas");
			Hashtable turnoElegido = (Hashtable)request.getSession().getAttribute("turnoElegido");
			String idturno =(String)turnoElegido.get("IDTURNO");
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			DefinirTurnosForm form = (DefinirTurnosForm) formulario;
			ScsTurnoAdm turno = new ScsTurnoAdm(usr);
			Hashtable hashBusq = new Hashtable();
			hashBusq.put("IDTURNO",idturno);
			hashBusq.put("IDINSTITUCION",usr.getLocation());
			ScsTurnoBean tAnt = (ScsTurnoBean)((Vector)turno.select(hashBusq)).get(0);
			Hashtable hash = (Hashtable)form.getDatos();
			Hashtable haux = new Hashtable();
			Vector v = new Vector();
			String where =" where "+
			ScsOrdenacionColasBean.C_ALFABETICOAPELLIDOS+"="+(String)hash.get("ALFABETICOAPELLIDOS")+" and "+
			ScsOrdenacionColasBean.C_ANTIGUEDADCOLA+"="+(String)hash.get("ANTIGUEDADENCOLA")+" and "+
			ScsOrdenacionColasBean.C_FECHANACIMIENTO+"="+(String)hash.get("EDAD")+" and "+
			ScsOrdenacionColasBean.C_NUMEROCOLEGIADO+"="+(String)hash.get("ANTIGUEDAD")+" ";
			ScsOrdenacionColasAdm ordenacion = new ScsOrdenacionColasAdm(this.getUserBean(request));
			
			if (form.getActivarRestriccionActuacion() != null && UtilidadesString.stringToBoolean(form.getActivarRestriccionActuacion())) {
				hash.put(ScsTurnoBean.C_ACTIVARRETRICCIONACREDIT, ClsConstants.DB_TRUE);
			}
			else { 
				hash.put (ScsTurnoBean.C_ACTIVARRETRICCIONACREDIT, ClsConstants.DB_FALSE);
			}
			
			if (miform.getActivarActuacionesLetrado() != null && UtilidadesString.stringToBoolean(miform.getActivarActuacionesLetrado())) {
				hash.put(ScsTurnoBean.C_LETRADOACTUACIONES, ClsConstants.DB_TRUE);
			}
			else { 
				hash.put (ScsTurnoBean.C_LETRADOACTUACIONES, ClsConstants.DB_FALSE);
			}

			if (miform.getActivarAsistenciasLetrado() != null && UtilidadesString.stringToBoolean(miform.getActivarAsistenciasLetrado())) {
				hash.put(ScsTurnoBean.C_LETRADOASISTENCIAS, ClsConstants.DB_TRUE);
			}
			else { 
				hash.put (ScsTurnoBean.C_LETRADOASISTENCIAS, ClsConstants.DB_FALSE);
			}

			//Chequeo que el IDMATERIA no sea nulo:
			if (miform.getMateria()==null || miform.getMateria().trim().equals(""))
				return exito("gratuita.definirTurnosIndex.errorMateria",request);
			
			v=ordenacion.select(where);
			if (v.size()>0){
				ScsOrdenacionColasBean b = (ScsOrdenacionColasBean)v.get(0);
				hash.put("IDORDENACIONCOLAS",((Integer)b.getIdOrdenacionColas()).toString());
			}else{
				ordenacion.prepararInsert(hash);
				paso = true;
//				haux.put(ScsOrdenacionColasBean.C_ALFABETICOAPELLIDOS,(String)hash.get("ALFABETICOAPELLIDOS"));
//				haux.put(ScsOrdenacionColasBean.C_ANTIGUEDADCOLA,(String)hash.get("ANTIGUEDADENCOLA"));
//				haux.put(ScsOrdenacionColasBean.C_FECHANACIMIENTO,(String)hash.get("EDAD"));
//				haux.put(ScsOrdenacionColasBean.C_NUMEROCOLEGIADO,(String)hash.get("ANTIGUEDAD"));
				haux.put(ScsOrdenacionColasBean.C_ALFABETICOAPELLIDOS,miform.getAlfabeticoApellidos());
				haux.put(ScsOrdenacionColasBean.C_ANTIGUEDADCOLA,miform.getAntiguedadEnCola());
				haux.put(ScsOrdenacionColasBean.C_FECHANACIMIENTO,miform.getEdad());
				haux.put(ScsOrdenacionColasBean.C_NUMEROCOLEGIADO,miform.getAntiguedad());
				
				haux.put(ScsOrdenacionColasBean.C_USUMODIFICACION,(String)usr.getUserName());
				haux.put(ScsOrdenacionColasBean.C_FECHAMODIFICACION,"sysdate");
				haux.put(ScsOrdenacionColasBean.C_IDORDENACIONCOLAS,hash.get("IDORDENACIONCOLAS"));
				
			}
			hash.put("USUMODIFICACION",usr.getUserName());
			hash.put("IDINSTITUCION",usr.getLocation());
			hash.put("IDTURNO",idturno);
			

			if (usr==null) throw new ClsExceptions("Sesión no válida.");
			tx = usr.getTransaction();
		    tx.begin();

		    if (paso)ok = ordenacion.insert(haux);
		    ok = turno.update(this.prepararHash(hash),tAnt.getOriginalHash());
		    
		    // Si validarAltasBajas=1 validamos las altas y bajas en el turno que están pendientes
		    
		    // validacion de Altas
		    if (validarAltasBajas!=null && validarAltasBajas.equals("1")){
		    String sqlAltas = 
				"SELECT "+ 
					"inscri."+ScsInscripcionTurnoBean.C_IDINSTITUCION+" IDINSTITUCION,"+
					"inscri."+ScsInscripcionTurnoBean.C_IDPERSONA+" IDPERSONA,"+
					"inscri."+ScsInscripcionTurnoBean.C_IDTURNO+" IDTURNO,"+
					"inscri."+ScsInscripcionTurnoBean.C_FECHASOLICITUD+" FECHASOLICITUD,"+
					"f_siga_calculoncolegiado (" + usr.getLocation() + "," + "inscri."+ScsInscripcionTurnoBean.C_IDPERSONA+") NCOLEGIADO," +
					"person."+CenPersonaBean.C_NOMBRE+" NOMBRE_PERSONA,"+
					"person."+CenPersonaBean.C_APELLIDOS1+" APELLIDO1_PERSONA,"+
					"person."+CenPersonaBean.C_APELLIDOS2+" APELLIDO2_PERSONA,"+
					"turnos."+ScsTurnoBean.C_NOMBRE+" NOMBRE_TURNO "+
				"FROM "+
					ScsInscripcionTurnoBean.T_NOMBRETABLA 	+" inscri, "+
					CenColegiadoBean.T_NOMBRETABLA 			+" colegi, "+
					CenPersonaBean.T_NOMBRETABLA 			+" person, "+
					ScsTurnoBean.T_NOMBRETABLA 				+" turnos  "+
				"WHERE "+
					"colegi.idinstitucion 	= inscri.idinstitucion and "+
					"colegi.idpersona 		= inscri.idpersona and "	+
					"person.idpersona		= inscri.idpersona and "	+
					"turnos.idinstitucion 	= inscri.idinstitucion and "+
					"turnos.idturno 	    = inscri.idturno and "+
					"turnos.idturno 		= "+idturno+" and "		+
					"inscri.idinstitucion 	= "+usr.getLocation()+" and " +
					"inscri.fechavalidacion is null and "				+
					"inscri.fechabaja 		is null "				+
					"order by inscri.fechasolicitud desc";
		    
		    RowsContainer rcAlta = null;
			rcAlta = new RowsContainer();
			rcAlta.query(sqlAltas);
			ScsInscripcionTurnoAdm insTurnoAdm=new ScsInscripcionTurnoAdm(this.getUserBean(request));
			ScsInscripcionGuardiaAdm insguardia = new ScsInscripcionGuardiaAdm(this.getUserBean(request));
			
			
			Hashtable hashOriginal=new Hashtable();
			Hashtable hashNew=new Hashtable();
		
			for (int i = 0; i < rcAlta.size(); i++)	{
				Row fila = (Row) rcAlta.get(i);	
				hashOriginal=fila.getRow();
				hashNew=(Hashtable)hashOriginal.clone();
				hashNew.put(ScsInscripcionTurnoBean.C_FECHASOLICITUDBAJA,"");
				hashNew.put(ScsInscripcionTurnoBean.C_FECHABAJA,"");
				hashNew.put(ScsInscripcionTurnoBean.C_FECHAVALIDACION,"sysdate");
				hashNew.put(ScsInscripcionTurnoBean.C_OBSERVACIONESBAJA,"");
				hashNew.put(ScsInscripcionTurnoBean.C_OBSERVACIONESSOLICITUD,"");
				hashNew.put(ScsInscripcionTurnoBean.C_OBSERVACIONESVALIDACION,".");
				
				
				insTurnoAdm.update(hashNew,hashOriginal);
			}
			
			String sqlBajas = 
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
				ScsTurnoBean.T_NOMBRETABLA 				+" turnos  "+
		
		
		     "WHERE colegi.idinstitucion 	= inscri.idinstitucion and "+
				"colegi.idpersona 		= inscri.idpersona and "	+
				"person.idpersona		= inscri.idpersona and "	+
				"turnos.idinstitucion 	= inscri.idinstitucion and "+
				"turnos.idturno 	    = inscri.idturno and "+
				"turnos.idturno 		= "+idturno+" and "+
				"inscri.idinstitucion 	= "+usr.getLocation()+" and " +
				"(inscri.fechasolicitudbaja is not nulL) and "+				
				"inscri.fechabaja 		is null "				+
				"order by inscri.fechasolicitud desc";
		
		    
		    RowsContainer rcBaja = null;
		    rcBaja = new RowsContainer();
		    rcBaja.query(sqlBajas);
			
			
			 hashOriginal=new Hashtable();
			 hashNew=new Hashtable();
		
			for (int i = 0; i < rcBaja.size(); i++)	{
				Row fila = (Row) rcBaja.get(i);	
				hashOriginal=fila.getRow();
				hashNew=(Hashtable)hashOriginal.clone();
				
				hashNew.put(ScsInscripcionTurnoBean.C_FECHABAJA,"sysdate");
				hashNew.put(ScsInscripcionTurnoBean.C_FECHAVALIDACION,"sysdate");
				hashNew.put(ScsInscripcionTurnoBean.C_OBSERVACIONESBAJA,".");
				hashNew.put(ScsInscripcionTurnoBean.C_OBSERVACIONESSOLICITUD,".");
				hashNew.put(ScsInscripcionTurnoBean.C_OBSERVACIONESVALIDACION,".");
				
				
				insTurnoAdm.update(hashNew,hashOriginal);
				
				
				
				
			}
			if (rcBaja.size()>0){
//			 Damos de baja tambien las guardias asociadas al turno
			Hashtable laHash = new Hashtable();
			laHash.put(ScsInscripcionGuardiaBean.C_IDINSTITUCION	,usr.getLocation());
			laHash.put(ScsInscripcionGuardiaBean.C_IDTURNO			,idturno);
			laHash.put(ScsInscripcionGuardiaBean.C_FECHABAJA		,(String)hashNew.get(ScsInscripcionTurnoBean.C_FECHABAJA));
			String[] claves = {ScsInscripcionGuardiaBean.C_IDINSTITUCION,
					           ScsInscripcionGuardiaBean.C_IDTURNO};
			String[] campos = {ScsInscripcionGuardiaBean.C_FECHABAJA};
			insguardia.updateDirect(laHash,claves,campos);
		    }
		    }
		    tx.commit();
		    
		    forward = exitoRefresco("messages.updated.success",request);

		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,tx);
		}
		
		return forward;
			
	}
	
	protected String borrar(ActionMapping mapping, 
							MasterForm formulario, 
							HttpServletRequest request, 
							HttpServletResponse response) throws ClsExceptions, SIGAException {
		
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		boolean ok = false;
		Hashtable hash = new Hashtable();
		UserTransaction tx =null;
		try{

			DefinirTurnosForm miForm = (DefinirTurnosForm)formulario;
			Vector vCampos, vOcultos= new Vector();
			vCampos = miForm.getDatosTabla();
			int tope = vCampos.size();
			vCampos = miForm.getDatosTablaVisibles(0);
			vOcultos = miForm.getDatosTablaOcultos(0);
			hash.put("IDTURNO",vOcultos.get(0));
			hash.put("IDINSTITUCION",(String)usr.getLocation());
			ScsTurnoAdm turno = new ScsTurnoAdm(this.getUserBean(request));
			tx = usr.getTransaction();
		    tx.begin();

		    if (turno.delete(hash))
		    {
		        request.setAttribute("descOperation","messages.deleted.success");
		    }
		    else
		    {
		        throw new Exception("");
		    }

		    tx.commit();
		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,tx);
		}
		
		return "refresh";

	}
	/**
	 * Crea una variable de sesión, con los registros que cumplan con la condición where
	 * 
	 * 
	 */
	protected String buscarPor(	ActionMapping mapping, 
								MasterForm formulario, 
								HttpServletRequest request, 
								HttpServletResponse response) throws ClsExceptions, SIGAException {
		
		request.getSession().removeAttribute("ocultos");
		request.getSession().removeAttribute("campos");
		DefinirTurnosForm form = (DefinirTurnosForm) formulario;
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		try {

			String entrada = (String)request.getSession().getAttribute("entrada");
			ScsTurnoAdm turno = new ScsTurnoAdm(usr);
			Hashtable hash = (Hashtable)form.getDatos();
			String where="  where scs_ordenacioncolas.idordenacioncolas = turnos.idordenacioncolas "+
			" AND area.idinstitucion    = materi.idinstitucion"+
			" AND area.idarea       = materi.idarea"+
			" AND materi.idinstitucion              = turnos.idinstitucion"+
			" AND materi.idarea                     = turnos.idarea"+
			" AND materi.idmateria                  = turnos.idmateria"+
			" AND zona.idinstitucion             	= turnos.idinstitucion"+
			" AND zona.idzona                    	= turnos.idzona"+
			" AND subzon.idinstitucion           (+)= turnos.idinstitucion"+
			" AND subzon.idzona              (+)= turnos.idzona"+
			" AND subzon.idsubzona           (+)= turnos.idsubzona"+
			" AND partid.idinstitucion           (+)= turnos.idinstitucion"+
			" AND partid.idpartidapresupuestaria (+)= turnos.idpartidapresupuestaria"+
			" AND grupof.idinstitucion              = turnos.idinstitucion"+
			" AND grupof.idgrupofacturacion         = turnos.idgrupofacturacion"+
			" AND parjud.idpartido               (+)= subzon.idpartido"+
			" AND turnos.idinstitucion =" + usr.getLocation();
			//this.prepararBusqueda(hash);
			try{
				Integer.parseInt((String)hash.get("IDPARTIDOJUDICIAL"));
			}catch(Exception e){hash.put("IDPARTIDOJUDICIAL","-1");}
							
			if(!((String)hash.get("ABREVIATURA")).equalsIgnoreCase("")){
				where += " AND "+ComodinBusquedas.prepararSentenciaCompleta(((String)hash.get("ABREVIATURA")).trim(),"turnos."+ScsTurnoBean.C_ABREVIATURA);
			}
			if(!((String)hash.get("NOMBRE")).equalsIgnoreCase("")){
				where += " AND "+ComodinBusquedas.prepararSentenciaCompleta(((String)hash.get("NOMBRE")).trim(),"turnos."+ScsTurnoBean.C_NOMBRE);
			}
			//if((ant)&&(Integer.parseInt((String)hash.get("IDAREA"))>0))where+=" and ";
			if(Integer.parseInt((String)hash.get("IDAREA"))>0){
				where+=	" AND area.idarea = "+(String)hash.get("IDAREA");
					//ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_IDAREA+" = "+((String)hash.get("IDAREA")).toUpperCase();
			}
			//if((ant)&&(Integer.parseInt((String)hash.get("IDMATERIA"))>0))where+=" and ";
			try{
				if(Integer.parseInt((String)hash.get("IDMATERIA"))>0){
				where+=	" AND materi.idmateria ="+(String)hash.get("IDMATERIA");
				// ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_IDAREA+" = "+((String)hash.get("IDAREA")).toUpperCase();
				}
			}catch(Exception e){}
			//if((ant)&&(Integer.parseInt((String)hash.get("IDZONA"))>0))where+=" and ";
			String idzon = "";
			if (hash.get("IDZONA")!=null && !hash.get("IDZONA").equals("0")&& !hash.get("IDZONA").equals("")) {
				idzon=(String)hash.get("IDZONA");
				//idzon=idzon.substring(idzon.indexOf(","),idzon.length());
				if(Integer.parseInt(idzon)>0){
					where+=	" AND zona.idzona ="+idzon;
					//ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_IDZONA+" = "+((String)hash.get("IDZONA")).toUpperCase();
				}
			}
			//if((ant)&&(Integer.parseInt((String)hash.get("IDSUBZONA"))>0))where+=" and ";
			try{
				if(Integer.parseInt((String)hash.get("IDSUBZONA"))>0){
				where+=	" AND subzon.idsubzona = "+(String)hash.get("IDSUBZONA");
				}
			}catch(Exception e){}
			//if((ant)&&(Integer.parseInt((String)hash.get("IDPARTIDAPRESUPUESTARIA"))>0))where+=" and ";
			//ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_IDSUBZONA+"="+form.getSubzona()+" and "+
			if(Integer.parseInt((String)hash.get("IDPARTIDAPRESUPUESTARIA"))>0){
				where+=	" AND partid.idpartidapresupuestaria ="+((String)hash.get("IDPARTIDAPRESUPUESTARIA")).toUpperCase();
					//ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_IDPARTIDAPRESUPUESTARIA+" = "+((String)hash.get("IDPARTIDAPRESUPUESTARIA")).toUpperCase();
			}
			//if((ant)&&(Integer.parseInt((String)hash.get("IDGRUPOFACTURACION"))>0))where+=" and ";
			if(Integer.parseInt((String)hash.get("IDGRUPOFACTURACION"))>0){
				where+=	" AND grupof.idgrupofacturacion        (+)= "+(String)hash.get("IDGRUPOFACTURACION");
				//ScsTurnoBean.T_NOMBRETABLA+"."+ScsTurnoBean.C_IDGRUPOFACTURACION+" LIKE "+((String)hash.get("IDGRUPOFACTURACION")).toUpperCase();
			}
			//if((ant)&&(Integer.parseInt((String)hash.get("IDPARTIDOJUDICIAL"))>0))where+=" and ";
	//		try{
	//			if(Integer.parseInt((String)hash.get("IDPARTIDOJUDICIAL"))>0){
	//			where+=	" AND parjud.idpartido               ="+(String)hash.get("IDPARTIDOJUDICIAL");
	//			}
	//		}catch(Exception e){}
						
			
			
			request.getSession().setAttribute("DATOSFORMULARIO",hash);
			request.getSession().setAttribute("BUSQUEDAREALIZADA","SI");
			Vector vTurno = turno.selectTabla(where,"1"); //el segundo parámetro sirve para indicarle al método que los campos a recuperar en el select son:
														  //abreviatura,nombre, area, materia, zona, subzona, partidoJudicial,partidaPresupuestaria, grupoFacturacion
			request.setAttribute("resultado",vTurno);
			request.setAttribute("mantTurnos","1");

		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
			
		return "listado";
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
	protected String abrir(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException {
		
		//return "edicion";
		//los campos del vector campos son: 
		//abreviatura , nombre , nombreArea , nombreMateria, nombreZona, nombreSubzona, nombrePartidoJudicial, nombrePartidaPresupuestaria, nombreGrupoFacturacion 
		//los campos del vector ocultos son: 
		//IDTURNO, GUARDIAS, VALIDARJUSTIFICACIONES, VALIDARINSCRIPCIONES, DESIGNADIRECTA, REPARTOPORPUNTOS, DESCRIPCION,
		//REQUISITOS,IDORDENACIONCOLAS,IDPERSONAULTIMO,IDAREA,IDMATERIA,IDZONA,IDSUBZONA,IDPARTIDAPRESUPUESTARIA,IDGRUPOFACTURACION,
		
		String forward="";
		Vector campos = new Vector();
		Vector ocultos = new Vector();
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		try {
			
			DefinirTurnosForm form = (DefinirTurnosForm) formulario;
			String entrada= (String) request.getSession().getAttribute("entrada");
			campos = (Vector)form.getDatosTablaVisibles(0);
			ocultos = (Vector)form.getDatosTablaOcultos(0);
			String accion = (String)request.getSession().getAttribute("accionTurno");
			String idTurno ="";
			try{
				idTurno = (String)ocultos.get(0);
			}catch(Exception e){
				idTurno =(String)request.getSession().getAttribute("IDTURNOSESION");
				//request.getSession().removeAttribute("IDTURNOSESION");
			}
			if ((idTurno==null)||(idTurno.equals(""))){
				try{
					Hashtable turnoElegido = (Hashtable)request.getSession().getAttribute("turnoElegido");
					idTurno =(String)turnoElegido.get("IDTURNO");
				}catch(Exception e){
					idTurno = "";
				}
			}
			
			//Datos del Turno:
			ScsTurnoAdm turnoAdm = new ScsTurnoAdm(this.getUserBean(request));
			Hashtable miTurno = turnoAdm.getDatosTurno(usr.getLocation(), idTurno);
			request.getSession().setAttribute("turnoElegido",miTurno);
			
			request.getSession().removeAttribute("accionTurno");
			request.getSession().setAttribute("accionTurno",accion);
			request.getSession().setAttribute("campos",campos);
			request.getSession().setAttribute("ocultos",ocultos);
			
			// RGG cambio para ordenacion
			ScsOrdenacionColasBean orden = new ScsOrdenacionColasBean();
			CenPersonaBean personaBean = new CenPersonaBean();
			ScsOrdenacionColasAdm ordenacion = new ScsOrdenacionColasAdm(this.getUserBean(request));
			String condicion =" where "+ScsOrdenacionColasBean.C_IDORDENACIONCOLAS+"="+(String)miTurno.get("IDORDENACIONCOLAS")+" ";
			Vector vOrdenacion = ordenacion.select(condicion);
			orden = (ScsOrdenacionColasBean) vOrdenacion.get(0);
			form.setAlfabeticoApellidos(orden.getAlfabeticoApellidos().toString());
			form.setAntiguedad(orden.getNumeroColegiado().toString());
			form.setAntiguedadEnCola(orden.getAntiguedadCola().toString());
			form.setEdad(orden.getFechaNacimiento().toString());
			
			if (entrada.trim().equals("1")) {
				forward = "edicion";
			} else {
				forward = "edicionLetrado";
			}

		}
		catch (Exception e) {
			throwExcp("messages.general.error",new String[] {"modulo.gratuita"},e,null);
		}
			
		return forward;
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
			
		//borrar variables de sesión
		request.getSession().removeAttribute("DATABACKUP");
		request.getSession().removeAttribute("DATOSFORMULARIO");
		request.setAttribute("BUSQUEDAREALIZADA",(String)request.getSession().getAttribute("BUSQUEDAREALIZADA"));
		request.getSession().removeAttribute("accionTurno");
		request.getSession().removeAttribute("pestanas");
		return "inicio";
	}
}