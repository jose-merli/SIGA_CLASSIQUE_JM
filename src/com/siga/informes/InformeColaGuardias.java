package com.siga.informes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.ScsGuardiasTurnoAdm;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.beans.ScsInscripcionGuardiaBean;
import com.siga.beans.ScsSaltosCompensacionesAdm;
import com.siga.gratuita.InscripcionGuardia;
import com.siga.gratuita.form.ColaGuardiasForm;
import com.siga.gratuita.util.calendarioSJCS.LetradoInscripcion;

/**
 * @author david.sanchezp
 * @since 23/05/2006
 */
public class InformeColaGuardias extends MasterReport {
	
	
	public InformeColaGuardias(UsrBean usuario) {
		super(usuario);
	}
	
	/**
	 * Invoca varias clases para obtener el ultimo letrado con numero de colegiado, nombre y apellidos
	 * @param usuario Codigo de usuario conectado
	 * @param institucion Codigo institucion seleccionada
	 * @param turno Codigo turno seleccionado
	 * @param turno Codigo guardia seleccionada
	 * @param coForm Formulario en que se cargan los valores
	 * @throws ClsExceptions Error interno
	 */	
	protected void cargarUltimoLetrado(
			UsrBean usuario, 
			String institucion, 
			String turno,
			String guardia,
			ColaGuardiasForm coForm) throws ClsExceptions{
		
		ScsGuardiasTurnoAdm guardiasTurnoAdm = new ScsGuardiasTurnoAdm(usuario);
		CenPersonaAdm personaAdm= new CenPersonaAdm(usuario);
		CenColegiadoAdm colegiadoAdm= new CenColegiadoAdm(usuario);
		
		//buscar persona ultimo turno 
		Hashtable hashTurno = new Hashtable();
		hashTurno.put(ScsGuardiasTurnoBean.C_IDTURNO,turno);
		hashTurno.put(ScsGuardiasTurnoBean.C_IDINSTITUCION,institucion);
		hashTurno.put(ScsGuardiasTurnoBean.C_IDGUARDIA,guardia);
		ScsGuardiasTurnoBean guardiasTurnoBean = (ScsGuardiasTurnoBean)((Vector)guardiasTurnoAdm.select(hashTurno)).get(0);
		Long ultimo=guardiasTurnoBean.getIdPersona_Ultimo();
		
		if(ultimo!=null){
			//buscar numero colegiado
			Hashtable hashColegiado = new Hashtable();
			hashColegiado.put(CenColegiadoBean.C_IDINSTITUCION,institucion);
			hashColegiado.put(CenColegiadoBean.C_IDPERSONA,ultimo);
			CenColegiadoBean colegiadoBean = (CenColegiadoBean)((Vector)colegiadoAdm.select(hashColegiado)).get(0);
			coForm.setNColegiado(colegiadoAdm.getIdentificadorColegiado (colegiadoBean));
			
			//buscar datos persona
			Hashtable hashPersona = new Hashtable();
			hashPersona.put(CenPersonaBean.C_IDPERSONA,ultimo);
			CenPersonaBean personaBean= (CenPersonaBean)((Vector)personaAdm.select(hashPersona)).get(0);
			coForm.setNombre(personaBean.getNombre());
			coForm.setApellido1(personaBean.getApellido1());
			coForm.setApellido2(personaBean.getApellido2());
		}
	}
	
	
	protected String reemplazarDatos(HttpServletRequest request, String plantillaFO) throws ClsExceptions{
		Hashtable htDatos= null;
		String plantilla=plantillaFO;
		
		ColaGuardiasForm coForm=(ColaGuardiasForm)request.getAttribute("ColaGuardiasForm");
		HttpSession ses = request.getSession();
		Hashtable turnoElegido = (Hashtable)ses.getAttribute("turnoElegido");
		
		UsrBean usr = (UsrBean)ses.getAttribute("USRBEAN"); 
		
		//Integer usuario=this.getUsuario();
		String institucion =usr.getLocation();
		String turno =(String)turnoElegido.get("IDTURNO");
		String nombreTurno =(String)turnoElegido.get("NOMBRE");
		String guardia=coForm.getIdGuardia();
		String fecha  = coForm.getFechaConsulta();
		fecha = (fecha!=null&&!fecha.trim().equals(""))?fecha:null;
		
		ScsGuardiasTurnoAdm guardiasTurnoAdm = new ScsGuardiasTurnoAdm(usr);
		ScsSaltosCompensacionesAdm saltosCompensacionesAdm = new ScsSaltosCompensacionesAdm(usr);
		
		//Cargar último letrado
		cargarUltimoLetrado(usr, institucion, turno, guardia, coForm);
		htDatos=coForm.getDatos();//contiene la descripcion de la guardia (DEFGUARDIA), la trae de la pagina jsp

		//PONEMOS EL NOMBRE DEL TURNO Y LA HORA DE GENERACION Y LA FECHA DE CONSULTA
		htDatos.put("NOMBRE_TURNO", nombreTurno);
		htDatos.put("FECHA_GENERACION",  new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
		htDatos.put("FECHA_CONSULTA", (fecha!=null&&!fecha.trim().equals(""))?fecha:"Muestra todas las inscripciones");
		
		
		//Cargar listado de letrados en cola
		ArrayList<LetradoInscripcion> alLetrados = InscripcionGuardia.getColaGuardia(new Integer(institucion),new Integer(turno), new Integer(guardia), fecha,fecha, usr);
		Vector vLetradosEnCola = new Vector();
		for(LetradoInscripcion letradoGuardia:alLetrados){
			Row row = new Row();
			Hashtable htRow = new Hashtable();
			
			htRow.put(CenPersonaBean.C_IDPERSONA, letradoGuardia.getIdPersona());
//			CenPersonaBean persona = admPersona.getPersonaColegiado(letradoGuardia.getIdPersona(), letradoGuardia.getIdInstitucion());
			htRow.put(CenPersonaBean.C_APELLIDOS1, letradoGuardia.getPersona().getApellido1());
			htRow.put(CenPersonaBean.C_APELLIDOS2, letradoGuardia.getPersona().getApellido2());
			htRow.put(CenPersonaBean.C_NOMBRE, letradoGuardia.getPersona().getNombre());
			htRow.put(CenColegiadoBean.C_NCOLEGIADO, letradoGuardia.getPersona().getColegiado().getNColegiado());
			htRow.put(ScsInscripcionGuardiaBean.C_FECHAVALIDACION, letradoGuardia.getInscripcionGuardia().getFechaValidacion());
			htRow.put(ScsInscripcionGuardiaBean.C_FECHABAJA, letradoGuardia.getInscripcionGuardia().getFechaBaja());
			row.setRow(htRow);
			vLetradosEnCola.add(row);
			
		}
		
		plantilla = this.reemplazaRegistros(plantilla, vLetradosEnCola, htDatos, "LETRADOS");

		//Cargar listado de compensaciones
		Vector vCompensaciones=saltosCompensacionesAdm.selectSaltosCompensaciones(institucion, turno, guardia, ClsConstants.COMPENSACIONES);
		plantilla = this.reemplazaRegistros(plantilla, vCompensaciones, htDatos, "COMPENSACIONES");
		
		//Cargar listado de saltos
		Vector vSaltos=saltosCompensacionesAdm.selectSaltosCompensaciones(institucion, turno, guardia, ClsConstants.SALTOS);
		plantilla = this.reemplazaRegistros(plantilla, vSaltos, htDatos, "SALTOS");
				
		plantilla = this.reemplazaVariables(htDatos, plantilla);
		return plantilla;
	}
	
	
}