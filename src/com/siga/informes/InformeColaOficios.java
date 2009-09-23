package com.siga.informes;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.ScsSaltosCompensacionesAdm;
import com.siga.beans.ScsTurnoAdm;
import com.siga.beans.ScsTurnoBean;
import com.siga.gratuita.form.ColaOficiosForm;

/**
 * @author david.sanchezp
 * @since 23/05/2006
 */
public class InformeColaOficios extends MasterReport {

	public InformeColaOficios() {
		super();
	}
	
	public InformeColaOficios(UsrBean usuario) {
		super(usuario);
	}
	
	/**
	 * Invoca varias clases para obtener el ultimo letrado con numero de colegiado, nombre y apellidos
	 * @param usuario Codigo de usuario conectado
	 * @param institucion Codigo institucion seleccionada
	 * @param turno Codigo turno seleccionado
	 * @param coForm Formulario en que se cargan los valores
	 * @throws ClsExceptions Error interno
	 */	
	protected void cargarUltimoLetrado(
			UsrBean usuario, 
			String institucion, 
			String turno, 
			ColaOficiosForm coForm) throws ClsExceptions{
		
		ScsTurnoAdm turnoAdm = new ScsTurnoAdm(usuario);
		CenPersonaAdm personaAdm= new CenPersonaAdm(usuario);
		CenColegiadoAdm colegiadoAdm= new CenColegiadoAdm(usuario);
		
		//buscar persona ultimo turno 
		Hashtable hashTurno = new Hashtable();
		hashTurno.put(ScsTurnoBean.C_IDTURNO,turno);
		hashTurno.put(ScsTurnoBean.C_IDINSTITUCION,institucion);
		ScsTurnoBean turnoBean = (ScsTurnoBean)((Vector)turnoAdm.select(hashTurno)).get(0);
		Integer ultimo=turnoBean.getIdPersonaUltimo();
		
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
		
		HttpSession ses = request.getSession();
		Hashtable turnoElegido = (Hashtable)ses.getAttribute("turnoElegido");
		UsrBean usr = (UsrBean)ses.getAttribute("USRBEAN");
		
		String institucion =usr.getLocation();
		String turno =(String)turnoElegido.get("IDTURNO");
		
		ScsTurnoAdm turnoAdm = new ScsTurnoAdm(usr);
		ScsSaltosCompensacionesAdm saltosCompensacionesAdm = new ScsSaltosCompensacionesAdm(usr);
		
		//Cargar último letrado
		ColaOficiosForm coForm=new ColaOficiosForm();
		cargarUltimoLetrado(usr, institucion, turno, coForm);
		htDatos=coForm.getDatos();

		//Cargar listado de letrados en cola
		Vector vLetradosEnCola=turnoAdm.selectLetradosEnCola(institucion,turno);
		plantilla = this.reemplazaRegistros(plantilla, vLetradosEnCola, htDatos, "LETRADOS");

		//Cargar listado de compensaciones
		Vector vCompensaciones=saltosCompensacionesAdm.selectSaltosCompensaciones(institucion, turno, null, ClsConstants.COMPENSACIONES);
		plantilla = this.reemplazaRegistros(plantilla, vCompensaciones, htDatos, "COMPENSACIONES");
		
		//Cargar listado de saltos
		Vector vSaltos=saltosCompensacionesAdm.selectSaltosCompensaciones(institucion, turno, null, ClsConstants.SALTOS);
		plantilla = this.reemplazaRegistros(plantilla, vSaltos, htDatos, "SALTOS");
				
		
		plantilla = this.reemplazaVariables(htDatos, plantilla);
		return plantilla;
	}
	
}