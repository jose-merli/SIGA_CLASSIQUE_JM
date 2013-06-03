package com.siga.gratuita.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.ScsInscripcionTurnoAdm;
import com.siga.beans.ScsOrdenacionColasAdm;
import com.siga.beans.ScsOrdenacionColasBean;
import com.siga.beans.ScsTurnoAdm;
import com.siga.beans.ScsTurnoBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefinirTurnosForm;
import com.siga.ws.CajgConfiguracion;

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
			request.getSession().setAttribute("BAJALOGICATURNOS",form.getTurnosBajaLogica());

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
		//IDTURNO, GUARDIAS, VALIDARJUSTIFICACIONES, VALIDARINSCRIPCIONES, DESIGNADIRECTA, DESCRIPCION,
		//REQUISITOS,IDORDENACIONCOLAS,IDPERSONAULTIMO,IDAREA,IDMATERIA,IDZONA,IDSUBZONA,IDPARTIDAPRESUPUESTARIA,IDGRUPOFACTURACION,
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		Vector campos = new Vector(); 
		Vector ocultos = new Vector();
		String forward="";
		
		try {			
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
			request.getSession().setAttribute("FECHASOLICITUDTURNOSESION",(String)ocultos.get(19));
			request.getSession().setAttribute("BAJALOGICATURNOS",form.getTurnosBajaLogica());
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
								" turno.requisitos requisitos, turno.idordenacioncolas idordenacioncolas, turno.idpersona_ultimo idpersona_ultimo,turno.FECHASOLICITUD_ULTIMO FECHASOLICITUD_ULTIMO,turno.idsubzona idsubzona, area.nombre area,"+
								" materia.nombre materia, zona.nombre zona, subzona.nombre subzona, partida.nombrepartida partidapresupuestaria, turno.idordenacioncolas idordenacioncolas, turno.idturno idturno, turno.validarjustificaciones validarjustificaciones, turno.validarinscripciones validarinscripciones,"+
								" turno.designadirecta designadirecta, subzona.idpartido idpartidojudicial, " +
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
			
			// Hemos visto, que el valor que debe decidir a que pagina ir, seria el valor de si el usuario es letrado
			// String entrada= (String) request.getSession().getAttribute("entrada");
			if (usr.isLetrado()){
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
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		request.getSession().setAttribute("accionTurno","nuevo");
		int tipoCAJG = CajgConfiguracion.getTipoCAJG(new Integer(usr.getLocation()));					
			request.setAttribute("pcajgActivo", tipoCAJG);

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

			if (miform.getVisibilidad() != null && UtilidadesString.stringToBoolean(miform.getVisibilidad())) {
				hash.put(ScsTurnoBean.C_VISIBILIDAD, ClsConstants.DB_TRUE);
			}
			else { 
				hash.put (ScsTurnoBean.C_VISIBILIDAD, ClsConstants.DB_FALSE);
			}
			
			if (miform.getIdTipoTurno() != null){
				hash.put(ScsTurnoBean.C_IDTIPOTURNO, miform.getIdTipoTurno());
			}
			
			if (miform.getCodigoExterno() != null) {
				hash.put(ScsTurnoBean.C_CODIGOEXT,miform.getCodigoExterno());
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
		aux.put(ScsTurnoBean.C_VISIBILIDAD,(hash.get(ScsTurnoBean.C_VISIBILIDAD)));
		aux.put(ScsTurnoBean.C_IDTIPOTURNO,(hash.get(ScsTurnoBean.C_IDTIPOTURNO)));
		aux.put(ScsTurnoBean.C_CODIGOEXT,(hash.get(ScsTurnoBean.C_CODIGOEXT)));
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
			ScsTurnoAdm turno = new ScsTurnoAdm(usr);
			Hashtable hashBusq = new Hashtable();
			hashBusq.put("IDTURNO",idturno);
			hashBusq.put("IDINSTITUCION",usr.getLocation());
			ScsTurnoBean tAnt = (ScsTurnoBean)((Vector)turno.select(hashBusq)).get(0);
			Hashtable hash = (Hashtable)miform.getDatos();
			Hashtable haux = new Hashtable();
			Vector v = new Vector();
			String where =" where "+
			ScsOrdenacionColasBean.C_ALFABETICOAPELLIDOS+"="+(String)hash.get("ALFABETICOAPELLIDOS")+" and "+
			ScsOrdenacionColasBean.C_ANTIGUEDADCOLA+"="+(String)hash.get("ANTIGUEDADENCOLA")+" and "+
			ScsOrdenacionColasBean.C_FECHANACIMIENTO+"="+(String)hash.get("EDAD")+" and "+
			ScsOrdenacionColasBean.C_NUMEROCOLEGIADO+"="+(String)hash.get("ANTIGUEDAD")+" ";
			ScsOrdenacionColasAdm ordenacion = new ScsOrdenacionColasAdm(this.getUserBean(request));
			
			if (miform.getActivarRestriccionActuacion() != null && UtilidadesString.stringToBoolean(miform.getActivarRestriccionActuacion())) {
				hash.put(ScsTurnoBean.C_ACTIVARRETRICCIONACREDIT, ClsConstants.DB_TRUE);
				
			} else { 
				hash.put (ScsTurnoBean.C_ACTIVARRETRICCIONACREDIT, ClsConstants.DB_FALSE);
			}
			
			if (miform.getActivarActuacionesLetrado() != null && UtilidadesString.stringToBoolean(miform.getActivarActuacionesLetrado())) {
				hash.put(ScsTurnoBean.C_LETRADOACTUACIONES, ClsConstants.DB_TRUE);
				
			} else { 
				hash.put (ScsTurnoBean.C_LETRADOACTUACIONES, ClsConstants.DB_FALSE);
			}

			if (miform.getActivarAsistenciasLetrado() != null && UtilidadesString.stringToBoolean(miform.getActivarAsistenciasLetrado())) {
				hash.put(ScsTurnoBean.C_LETRADOASISTENCIAS, ClsConstants.DB_TRUE);
				
			} else { 
				hash.put (ScsTurnoBean.C_LETRADOASISTENCIAS, ClsConstants.DB_FALSE);
			}

			if (miform.getCodigoExterno() != null) {
				hash.put(ScsTurnoBean.C_CODIGOEXT,miform.getCodigoExterno());
			}
			
			if (miform.getVisibilidad() != null && UtilidadesString.stringToBoolean(miform.getVisibilidad())) {
				hash.put(ScsTurnoBean.C_VISIBILIDAD, ClsConstants.DB_TRUE);
				
			} else { 
				hash.put (ScsTurnoBean.C_VISIBILIDAD, ClsConstants.DB_FALSE);
			}
			
			if (miform.getIdTipoTurno() != null){
				hash.put(ScsTurnoBean.C_IDTIPOTURNO, miform.getIdTipoTurno());
			}
			
			//Chequeo que el IDMATERIA no sea nulo:
			if (miform.getMateria()==null || miform.getMateria().trim().equals(""))
				return exito("gratuita.definirTurnosIndex.errorMateria",request);
			
			v=ordenacion.select(where);
			if (v.size()>0){
				ScsOrdenacionColasBean b = (ScsOrdenacionColasBean)v.get(0);
				hash.put("IDORDENACIONCOLAS",((Integer)b.getIdOrdenacionColas()).toString());
				
			} else {
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
		    
		    ScsInscripcionTurnoAdm insTurnoAdm = new ScsInscripcionTurnoAdm(usr);
		    
		    /* 1. Compruebo que se ha modificado la configuracion del turno de alta a baja
		     * Pone todas las inscripciones de turno y guardia (toda inscripcion que no este confirmada de baja o alta denegada), como confirmadas de baja 
		     */
		    String visibilidadOld = tAnt.getVisibilidad();
		    String visibilidadNew = UtilidadesHash.getString(hash, ScsTurnoBean.C_VISIBILIDAD);
		    if (visibilidadNew.equalsIgnoreCase(ClsConstants.DB_FALSE) && visibilidadOld.equalsIgnoreCase(ClsConstants.DB_TRUE)) {
		    	
		    	// Cancela todas las inscripciones de turno de un turno que no estan de baja
		    	insTurnoAdm.cancelarInscripcionesTurnosTurno(usr.getLocation(), idturno);					
		    } else {
		    
			    /* 2. Compruebo que el turno previamente y posteriormente este en estado de alta,
			     * - Si estaba de baja, debe tener todas las inscripciones de turno y guardia confirmadas de baja o alta denegada
			     * - Si estaba de alta y pasa a baja, pone todas las inscripciones de turno y guardia (toda inscripcion que no este confirmada de baja o alta denegada), como confirmadas de baja 
			     */
			    if (visibilidadOld.equalsIgnoreCase(ClsConstants.DB_TRUE) && visibilidadNew.equalsIgnoreCase(ClsConstants.DB_TRUE)) {
			    	
				    // Si validarAltasBajas=1 validamos las altas y bajas de las inscripciones de turno y guardia pendientes				    				    
				    if (validarAltasBajas!=null && validarAltasBajas.equals("1")){		   		    	
						insTurnoAdm.validarInscripcionesPendientes(usr.getLocation(), idturno);
				    }		    			    	
			    }
			    
			    // 3. Tratamiento por el cambio de configuracion de un turno, en sus tipos de guardias
			    Integer iTipoGuardiasOld = tAnt.getGuardias();
			    Integer iTipoGuardiasNew = UtilidadesHash.getInteger(hash, ScsTurnoBean.C_GUARDIAS);			
			    
			    /*
			     * iTipoGuardias=0 Obligatorias
			     * iTipoGuardias=1 Todas o Ninguna
			     * iTipoGuardias=2 Eleccion
			     * 
			     *  Si (iTipoGuardiasOld=0) y (iTipoGuardiasNew=0) entonces no cambia de tipo
			     *  Si (iTipoGuardiasOld=0) y (iTipoGuardiasNew=1) entonces hacemos el tratamiento principal
			     *  Si (iTipoGuardiasOld=0) y (iTipoGuardiasNew=2) entonces no hace falta modificar las guardias (ya que los turnos por eleccion, no obligan a cumplir unos requisitos en sus guardias)
			     *  
			     *  Si (iTipoGuardiasOld=1) y (iTipoGuardiasNew=0) entonces hacemos el tratamiento principal 
			     *  Si (iTipoGuardiasOld=1) y (iTipoGuardiasNew=1) entonces no cambia de tipo
			     *  Si (iTipoGuardiasOld=1) y (iTipoGuardiasNew=2) entonces no hace falta modificar las guardias (ya que los turnos por eleccion, no obligan a cumplir unos requisitos en sus guardias)
			     *  
			     *  Si (iTipoGuardiasOld=2) y (iTipoGuardiasNew=0) entonces hacemos el tratamiento principal
			     *  Si (iTipoGuardiasOld=2) y (iTipoGuardiasNew=1) entonces hacemos el tratamiento secundario
			     *  Si (iTipoGuardiasOld=2) y (iTipoGuardiasNew=2) entonces no cambia de tipo
			     *  
			     *  Tratamiento principal:
			     *  a. Si la inscripcion de turno esta cancelada o denegada el alta o pendiente de alta, no hay que cambiar nada.
			     *  b. Si no se cumple la opciona a:
			     *  b.1. Doy de baja todas las guardias que no esten canceladas o denegadas de alta.
			     *  b.2. Doy de alta todas las guardias del turno.
			     *  
			     *  Tratamiento secundario:
			     *  a. Si la inscripcion de turno esta cancelada o denegada el alta o pendiente de alta, no hay que cambiar nada.
			     *  b. Si no se cumple la opciona a:
			     *  b.1. Doy de baja todas las guardias que no esten canceladas o denegadas de alta.
			     *  b.2. Si he borrado alguna guardia, doy de alta todas las guardias del turno.
			     *  
			     */
			    
			    if ((iTipoGuardiasOld==0 && iTipoGuardiasNew==1) || (iTipoGuardiasOld==1 && iTipoGuardiasNew==0) || (iTipoGuardiasOld==2 && iTipoGuardiasNew==0)) {
			    	// Tratamiento principal
			    	insTurnoAdm.cambiarConfiguracionTiposGuardias(true, usr.getLocation(), idturno);
			    	
			    } else if (iTipoGuardiasOld==2 && iTipoGuardiasNew==1) {
			    	// Tratamiento secundario
			    	insTurnoAdm.cambiarConfiguracionTiposGuardias(false, usr.getLocation(), idturno);
			    }
		    }
		    
		    tx.commit();
		    
		    forward = exitoRefresco("messages.updated.success", request);

		} catch (Exception e) {
			throwExcp("messages.general.error", new String[] {"modulo.gratuita"}, e, tx);
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
		//IDTURNO, GUARDIAS, VALIDARJUSTIFICACIONES, VALIDARINSCRIPCIONES, DESIGNADIRECTA, DESCRIPCION,
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
			
			int tipoCAJG = CajgConfiguracion.getTipoCAJG(new Integer(usr.getLocation()));
					
			request.setAttribute("pcajgActivo", tipoCAJG);

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