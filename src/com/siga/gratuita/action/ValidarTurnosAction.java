package com.siga.gratuita.action;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;

import com.siga.general.SIGAException;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.beans.ScsInscripcionGuardiaAdm;
import com.siga.beans.ScsInscripcionGuardiaBean;
import com.siga.beans.ScsInscripcionTurnoAdm;
import com.siga.beans.ScsInscripcionTurnoBean;
import com.siga.beans.ScsOrdenacionColasAdm;
import com.siga.beans.ScsOrdenacionColasBean;
import com.siga.beans.ScsTurnoAdm;
import com.siga.beans.ScsTurnoBean;
import com.siga.general.MasterAction;
import com.siga.general.MasterForm;
import com.siga.gratuita.form.DefinirTurnosForm;
import com.siga.gratuita.form.ValidarTurnosForm;


/**
 * @author carlos.vidal
 */

public class ValidarTurnosAction extends MasterAction {

	/**
	 * Esta clase se encarga de validar los turnos de los letrados
	 * El resutado lo manda como variable del request a la jsp que se encargará de mostrar una tabla.
	 * 
	 * @param mapping
	 * @param formulario 
	 * @param request
	 * @param response
	 * @return String
	 * @throws ClsExceptions
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
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	protected String buscarPor(ActionMapping mapping, 
				MasterForm formulario, 
				HttpServletRequest request, 
				HttpServletResponse response) throws SIGAException {
		String forward = "listado";
		try{
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			ScsInscripcionTurnoAdm turnos = new ScsInscripcionTurnoAdm(this.getUserBean(request));
			//Preparamos la select a ejecutar.
			String sql = 
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
					"turnos.idturno 		= inscri.idturno and "		+
					"inscri.idinstitucion 	= "+usr.getLocation()+" and " +
					"inscri.fechavalidacion is null and "				+
					"inscri.fechabaja 		is null "				+
					//"order by turnos.nombre, inscri.fechasolicitud";
					"order by inscri.fechasolicitud desc";
			Vector vTurno = turnos.selectTabla(sql);
			request.setAttribute("resultado",vTurno);
			request.setAttribute("localizacion","gratuita.listarTurnosLetrado.literal.localizacionA");
			request.setAttribute("titulo","gratuita.listarTurnosLetrado.literal.tituloA");
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
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
		
	protected String ver(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "";
		UsrBean usr;

		try {
			usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			ScsTurnoAdm turno = new ScsTurnoAdm(this.getUserBean(request));
			ValidarTurnosForm miForm = (ValidarTurnosForm) formulario;			
			String paso	= miForm.getPaso();			
			// Dependiendo de si se solicita la consulta del turno o de las guardias, se ejecuta
			// una opcion u otra.

			if(paso.equals("turno"))
			{

				// OBTENEMOS LOS VALORES PARA EL WHERE DE LA CONSULTA.
				// Dependiendo de si es alta o es validacion se obtinenen
				// los datos necesarios. 
				// Alta: idturno
				// Validacion: IDINSTITUCION,IDPERSONA,IDTURNO
				Integer IDTURNO			= miForm.getIdTurno();
				Integer IDINSTITUCION	= miForm.getIdInstitucion();			
				Integer IDPERSONA		= miForm.getIdPersona();
				String FECHASOLICITUD	= miForm.getFechaSolicitud();			

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
				" AND SCS_INSCRIPCIONTURNO.idturno ="+IDTURNO+
				" AND SCS_INSCRIPCIONTURNO.idinstitucion = "+IDINSTITUCION+
				" AND SCS_INSCRIPCIONTURNO.idpersona = "+IDPERSONA+" " +
				" AND SCS_INSCRIPCIONTURNO.FECHAVALIDACION IS NULL";
				// El 2 = letrado.
				Vector vTurno = turno.selectTabla(where,"2");
				request.setAttribute("titulo","gratuita.maestroTurnos.literal.validatitle1");
				request.setAttribute("action","JGR_ValidarTurnos.do");
				request.setAttribute("modo","ver");
				request.setAttribute("paso","guardia");
				request.setAttribute("idinstitucion",IDINSTITUCION);
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
			else if(paso.equals("guardia"))
			{
				Integer IDPERSONA		= miForm.getIdPersona();			
				Integer IDINSTITUCION	= miForm.getIdInstitucion();			
				Integer IDTURNO			= miForm.getIdTurno();			
				String FECHASOLICITUD	= miForm.getFechaSolicitud();			
				String OBSERVACIONESSOLICITUD	= miForm.getObservacionesSolicitud();			
				String sql=
					"Select DISTINCT a.IDGUARDIA IDGUARDIA,a.NOMBRE NOMBRE,a.NUMEROLETRADOSGUARDIA NUMEROLETRADOSGUARDIA,"+
					"a.NUMEROSUSTITUTOSGUARDIA NUMEROSUSTITUTOSGUARDIA,a.SELECCIONLABORABLES, a.SELECCIONFESTIVOS, a.TIPODIASGUARDIA TIPODIASGUARDIA,"+
					"a.DIASGUARDIA DIASGUARDIA,a.DIASPAGADOS DIASPAGADOS,"+
					"a.DIASSEPARACIONGUARDIAS DIASSEPARACIONGUARDIAS from "+ScsGuardiasTurnoBean.T_NOMBRETABLA+" a,"+
					ScsInscripcionGuardiaBean.T_NOMBRETABLA+" b"+
					" where b.IDINSTITUCION = " + IDINSTITUCION+
					" and b.IDTURNO = " + IDTURNO+
					" and b.IDPERSONA = " + IDPERSONA+
					" and a.idinstitucion = b.idinstitucion "+
					" and a.idturno = b.idturno "+
					" and a.idguardia = b.idguardia"+
					" and b.fechabaja is null";
				Vector vTurno = turno.ejecutaSelect(sql);
				request.setAttribute("resultado",vTurno);
				request.setAttribute("titulo","gratuita.maestroTurnos.literal.validatitle2");
				request.setAttribute("botones","X,S");
				request.setAttribute("action","/JGR_ValidarTurnos");
				request.setAttribute("modo","modificar");
				request.setAttribute("IDPERSONA",IDPERSONA);
				request.setAttribute("idturno",IDTURNO);
				request.setAttribute("IDINSTITUCION",IDINSTITUCION);
				request.setAttribute("FECHASOLICITUD",FECHASOLICITUD);
				request.setAttribute("OBSERVACIONESSOLICITUD",OBSERVACIONESSOLICITUD);
				forward = "verGuardia";
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
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
		
	protected String nuevo(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		String forward = "nuevo";
		try
		{
			UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
			ValidarTurnosForm form = (ValidarTurnosForm) formulario;
			Integer idTurno = (Integer)form.getIdTurno();
			//Validamos el paso en el que nos encontramos.
			if(form.getPaso().equals("1"))
			{
				request.setAttribute("botones","X,S");
				request.setAttribute("titulo","gratuita.altaTurnos.literal.title");
				//request.getSession().setAttribute("FECHASOLICITUD"			,(String)form.getFechaSolicitud());
				//request.getSession().setAttribute("OBSERVACIONESSOLICITUD"	,(String)form.getObservacionesSolicitud());
				// obtenemos las guardias para el turno.
				ScsTurnoAdm turno = new ScsTurnoAdm(this.getUserBean(request));
				// Se van a obtener los siguientes campos: 
				// Nombre,NºLetrado,NºSustitutos,Tipo Dia,Dias Guardia,
				// Dias Pagados,Dias de Separacion. !!Hacer como en el resto de los beans¡¡
				String sql=
					"Select IDGUARDIA,NOMBRE,NUMEROLETRADOSGUARDIA,NUMEROSUSTITUTOSGUARDIA,SELECCIONLABORABLES,SELECCIONFESTIVOS,DIASGUARDIA,DIASPAGADOS,DIASSEPARACIONGUARDIAS from "+ScsGuardiasTurnoBean.T_NOMBRETABLA+
					" where "+	ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_IDINSTITUCION +" = " + usr.getLocation()+
					" and "+ScsGuardiasTurnoBean.T_NOMBRETABLA+"."+ScsGuardiasTurnoBean.C_IDTURNO+" = " + idTurno.intValue();
				Vector vTurno = turno.ejecutaSelect(sql);
				request.setAttribute("idturno",idTurno);
				request.setAttribute("resultado",vTurno);
				forward = "nuevo";
			}
			else
			{
				request.setAttribute("idturno"	,(Integer)form.getIdTurno());
				request.setAttribute("IDPERSONA"	,(Integer)form.getIdPersona());
				request.setAttribute("IDINSTITUCION"	,(Integer)form.getIdInstitucion());
				request.setAttribute("FECHASOLICITUD"	,(String)form.getFechaSolicitud());
				request.setAttribute("OBSERVACIONESSOLICITUD"	,(String)form.getObservacionesSolicitud());
				request.setAttribute("botones","C,F");
				request.setAttribute("titulo","gratuita.maestroTurnos.literal.validatitle3");
				request.setAttribute("action","/JGR_ValidarTurnos");
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
				
				if(form.getPaso().equals("2")) forward = "nuevo2";
			}
			request.setAttribute("accion",form.getModo());
		}
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}

	/* (non-Javadoc)
	 * @see com.siga.general.MasterAction#modificar(org.apache.struts.action.ActionMapping, com.siga.general.MasterForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String modificar(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		ValidarTurnosForm miForm = (ValidarTurnosForm) formulario;			
		ScsInscripcionTurnoAdm scsInscripcionTurnoAdm = new ScsInscripcionTurnoAdm(this.getUserBean(request));
				
		Hashtable miHash = new Hashtable();
		Hashtable backup = new Hashtable();
		String forward = "error";
		UserTransaction tx = null;
		UsrBean usr;

		try {
			usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
			ScsInscripcionTurnoAdm insturno = new ScsInscripcionTurnoAdm(this.getUserBean(request));
			ScsInscripcionGuardiaAdm insguardia = new ScsInscripcionGuardiaAdm(this.getUserBean(request));
			// Cambiar posteriormente
			String sql = "Select * from "+ScsInscripcionTurnoBean.T_NOMBRETABLA+
						" where "+ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_IDINSTITUCION+" = "+miForm.getIdInstitucion()+
						" and "+ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_IDPERSONA+" = "+miForm.getIdPersona()+
						" and "+ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_FECHASOLICITUD+" = TO_DATE('"+miForm.getFechaSolicitud()+"','YYYY/MM/DD hh24:mi:ss')"+
						" and "+ScsInscripcionTurnoBean.T_NOMBRETABLA+"."+ScsInscripcionTurnoBean.C_IDTURNO+" = "+miForm.getIdTurno(); 
			Vector vTurno = insturno.selectTabla(sql);
			backup 	= (Hashtable)((Hashtable) vTurno.get(0)).clone();
			miHash  = (Hashtable) vTurno.get(0);
			
			tx=usr.getTransaction();
			if (usr == null)
					throw new ClsExceptions("Sesion no válida.");
			// Campos clave															
			// Campos a modificar
			if(miForm.getFechaValidacion()!=null && !miForm.getFechaValidacion().equals(""))
			{
				miHash.put(ScsInscripcionTurnoBean.C_FECHAVALIDACION,GstDate.getApplicationFormatDate(usr.getLanguage(),miForm.getFechaValidacion()));
				miHash.put(ScsInscripcionTurnoBean.C_OBSERVACIONESVALIDACION,miForm.getObservacionesValidacion());
			}
			if(miForm.getFechaBaja()!=null && !miForm.getFechaBaja().equals(""))
			{
				miHash.put(ScsInscripcionTurnoBean.C_FECHABAJA,GstDate.getApplicationFormatDate(usr.getLanguage(),miForm.getFechaBaja()));			
			}
			if(miForm.getObservacionesBaja()!=null && !miForm.getObservacionesBaja().equals(""))
			{
				miHash.put(ScsInscripcionTurnoBean.C_OBSERVACIONESBAJA,miForm.getObservacionesBaja());
			}
			if(miForm.getFechaSolicitudBaja()!=null && !miForm.getFechaSolicitudBaja().equals(""))
			{
				miHash.put(ScsInscripcionTurnoBean.C_FECHASOLICITUDBAJA,GstDate.getApplicationFormatDate(usr.getLanguage(),miForm.getFechaSolicitudBaja()));			
			}
			if(miForm.getObservacionesBaja()!=null && !miForm.getObservacionesBaja().equals(""))
			{
				miHash.put(ScsInscripcionTurnoBean.C_OBSERVACIONESBAJA,miForm.getObservacionesBaja());
			}
			//UPDATE (INICIO TRANSACCION)
            tx.begin();                 
			if (scsInscripcionTurnoAdm.update(miHash,backup))
				request.setAttribute("mensaje","messages.updated.success");
			else
				request.setAttribute("mensaje","messages.updated.error");
			// Si se ha validado la baja del turno actualizamos la fecha de baja de las guardias.
			if(miForm.getFechaBaja()!=null && !miForm.getFechaBaja().equals(""))
			{
				Hashtable laHash = new Hashtable();
				laHash.put(ScsInscripcionGuardiaBean.C_IDINSTITUCION	,usr.getLocation());
				laHash.put(ScsInscripcionGuardiaBean.C_IDPERSONA		,miForm.getIdPersona());
				laHash.put(ScsInscripcionGuardiaBean.C_IDTURNO			,miForm.getIdTurno());
				laHash.put(ScsInscripcionGuardiaBean.C_FECHABAJA		,GstDate.getApplicationFormatDate(usr.getLanguage(),miForm.getFechaBaja()));
				String[] claves = {ScsInscripcionGuardiaBean.C_IDINSTITUCION,
						ScsInscripcionGuardiaBean.C_IDPERSONA, 
						ScsInscripcionGuardiaBean.C_IDTURNO};
				String[] campos = {ScsInscripcionGuardiaBean.C_FECHABAJA};
				insguardia.updateDirect(laHash,claves,campos);
			}
			
			tx.commit();
			forward = "exito";
	        request.setAttribute("modal", "1");

		} 
		catch (Exception e) 
		{
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.gratuita"});
		} 
		return forward;
	}

}