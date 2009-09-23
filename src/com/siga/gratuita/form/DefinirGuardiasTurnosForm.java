package com.siga.gratuita.form;

import java.util.ArrayList;

import com.siga.general.MasterForm;

/**
 * @author ruben.fernandez
 * @version 08/03/2006 david.sanchezp: nuevos campos
 */

public class DefinirGuardiasTurnosForm extends MasterForm
{
	//////////////////// ATRIBUTOS DE CLASE ////////////////////
	//Ordenacion
	public static String cod_Alfabetico="1";
	public static String cod_Antig="2";
	public static String cod_Edad="3";
	public static String cod_AntigCola="4";
	public static int numCriterios=4;
	
	
	
	//////////////////// ATRIBUTOS ////////////////////
	private String tipoDiasGuardia="";
	private String checkDiasPeriodo="";
	private String diasPeriodo="";
	private String tipoDiasPeriodo="";
	private String seleccionLaborablesLunes;
	private String seleccionLaborablesMartes;
	private String seleccionLaborablesMiercoles;
	private String seleccionLaborablesJueves;
	private String seleccionLaborablesViernes;
	private String seleccionLaborablesSabado;
	private String seleccionFestivosLunes;
	private String seleccionFestivosMartes;
	private String seleccionFestivosMiercoles;
	private String seleccionFestivosJueves;
	private String seleccionFestivosViernes;
	private String seleccionFestivosSabado;
	private String seleccionFestivosDomingo;
	private String sustituta;
	private String vg;
	
	private String idPersona="";
	private String idPersonaSolicitante="";
	private String idInstitucion="";
	private String idTurno="";
	private String idGuardia="";
	private String checkGuardiaDeSustitucion="", guardiaDeSustitucion ="";
	
	//Ordenacion
	private ArrayList ordenacion = new ArrayList();
	
	
	
	//////////////////// GETTERS ////////////////////
	public String getIdPersona() {return idPersona;}
	public String getIdTurno() {return idTurno;}
	public String getIdInstitucion() {return idInstitucion;}
	public String getIdGuardia() {return idGuardia;}
	public String getIdPersonaSolicitante() {return idPersonaSolicitante;}
	
	public String getTipoDiasGuardia() {return tipoDiasGuardia;}
	public String getCheckDiasPeriodo() {return checkDiasPeriodo;}
	public String getDiasPeriodo() {return diasPeriodo;}
	public String getTipoDiasPeriodo() {return tipoDiasPeriodo;}
	public String getSeleccionLaborablesLunes() {return this.seleccionLaborablesLunes;}
	public String getSeleccionLaborablesMartes() {return this.seleccionLaborablesMartes;}
	public String getSeleccionLaborablesMiercoles() {return this.seleccionLaborablesMiercoles;}
	public String getSeleccionLaborablesJueves() {return this.seleccionLaborablesJueves;}
	public String getSeleccionLaborablesViernes() {return this.seleccionLaborablesViernes;}
	public String getSeleccionLaborablesSabado() {return this.seleccionLaborablesSabado;}
	public String getSeleccionFestivosLunes() {return this.seleccionFestivosLunes;}
	public String getSeleccionFestivosMartes() {return this.seleccionFestivosMartes;}
	public String getSeleccionFestivosMiercoles() {return this.seleccionFestivosMiercoles;}
	public String getSeleccionFestivosJueves() {return this.seleccionFestivosJueves;}
	public String getSeleccionFestivosViernes() {return this.seleccionFestivosViernes;}
	public String getSeleccionFestivosSabado() {return this.seleccionFestivosSabado;}
	public String getSeleccionFestivosDomingo() {return this.seleccionFestivosDomingo;}
	public String getVg() {return this.vg;}
	
	//Metodos get de los campos del formulario
	public String getNombreGuardia() {return (String)this.datos.get("NOMBRE");}
	public String getTurno() {return (String)this.datos.get("TURNO");}
	public String getGuardia() {return (String)this.datos.get("GUARDIA");}
	public String getObligatoriedad() {return (String)this.datos.get("OBLIGATORIEDAD");}
	public String getDuracion() {return (String)this.datos.get("DIASGUARDIA");}
	public String getFechaInscripcion() {return (String)this.datos.get("FECHAINSCRIPCION");}
	public String getObservacionesInscripcion() {return (String)this.datos.get("OBSERVACIONESINSCRIPCION");}
	public String getObservacionesBaja() {return (String)this.datos.get("OBSERVACIONESBAJA");}
	public String getFechaBaja() {return (String)this.datos.get("FECHABAJA");}
	public String getDescripcion() {return (String) this.datos.get("DESCRIPCION");}
	public String getDescripcionFacturacion() {return (String) this.datos.get("DESCRIPCIONFACTURACION");}
	public String getDescripcionPago() {return (String) this.datos.get("DESCRIPCIONPAGO");}
	public String getPartidaPresupuestaria() {return (String) this.datos.get("IDPARTIDAPRESUPUESTARIA");}
	public String getLetradosGuardia() {return (String) this.datos.get("NUMEROLETRADOSGUARDIA");}
	public String getLetradosSustitutos() {return (String) this.datos.get("NUMEROSUSTITUTOSGUARDIA");}
	public String getDuracionGuardia() {return (String) this.datos.get("DURACIONGUARDIA");}
	public String getDiasPagados() {return (String) this.datos.get("DIASPAGADOS");}
	public String getDiasSeparacion() {return (String) this.datos.get("DIASSEPARACIONGUARDIAS");}
	public String getAlfabetico() {return (String) this.datos.get("ALFABETICOAPELLIDOS");}
	public String getNumeroColegiado() {return (String) this.datos.get("NUMEROCOLEGIADO");}
	public String getAntiguedadCola() {return (String) this.datos.get("ANTIGUEDADCOLA");}
	public String getAsistencias() {return (String) this.datos.get("NUMEROASISTENCIAS");}
	public String getActuaciones() {return (String) this.datos.get("NUMEROACTUACIONES");}
	public String getUltimoLetrado() {return (String) this.datos.get("IDPERSONA_ULTIMO");}
	public String getJustificaciones() {return (String) this.datos.get("VALIDARJUSTIFICACIONES");}
	public String getGuardiaElegida() {return (String) this.datos.get("GUARDIAELEGIDA");}
	public String getGuardias() {return (String) this.datos.get("GUARDIAS");}
	
	//Ordenacion - RGG 15-02-2006
	public String getCrit_1(){
		//return (String)this.datos.get("CRIT_1");
		//RGG
		String salida="";
		ArrayList auxi = (ArrayList)ordenacion.get(0);
		salida = (String)auxi.get(0);
		return salida;
	}
	public String getCrit_2(){
		//return (String)this.datos.get("CRIT_2");
		//RGG
		String salida="";
		ArrayList auxi = (ArrayList)ordenacion.get(1);
		salida = (String)auxi.get(0);
		return salida;
	}
	public String getCrit_3(){
		//return (String)this.datos.get("CRIT_3");
		//RGG
		String salida="";
		ArrayList auxi = (ArrayList)ordenacion.get(2);
		salida = (String)auxi.get(0);
		return salida;
	}
	public String getCrit_4(){
		//return (String)this.datos.get("CRIT_4");
		//RGG
		String salida="";
		ArrayList auxi = (ArrayList)ordenacion.get(3);
		salida = (String)auxi.get(0);
		return salida;
	}
	public String getOrd_1(){
		//return (String)this.datos.get("ORD_1");
		//RGG
		String salida="";
		ArrayList auxi = (ArrayList)ordenacion.get(0);
		salida = (String)auxi.get(1);
		return salida;
	}
	public String getOrd_2(){
		//return (String)this.datos.get("ORD_2");
		//RGG
		String salida="";
		ArrayList auxi = (ArrayList)ordenacion.get(1);
		salida = (String)auxi.get(1);
		return salida;
	}
	public String getOrd_3(){
		//return (String)this.datos.get("ORD_3");
		//RGG
		String salida="";
		ArrayList auxi = (ArrayList)ordenacion.get(2);
		salida = (String)auxi.get(1);
		return salida;
	}
	public String getOrd_4(){
		//return (String)this.datos.get("ORD_4");
		//RGG
		String salida="";
		ArrayList auxi = (ArrayList)ordenacion.get(3);
		salida = (String)auxi.get(1);
		return salida;
	}
	public String getAlfabeticoApellidos(){
		//return (String)this.datos.get("ALFABETICOAPELLIDOS");
		String salida = "0";
		//RGG
		for (int i=0;i<numCriterios;i++) {
			ArrayList auxi = (ArrayList)ordenacion.get(i);
			if (((String)auxi.get(0)).equals(DefinirGuardiasTurnosForm.cod_Alfabetico)) {
				if (((String)auxi.get(1)).equals("A")) {
					salida = new Integer(numCriterios-i).toString();
				} else {
					salida = "-" + new Integer(numCriterios-i).toString();
				}
			}
		}
		return salida;
	}
	public String getAntiguedad(){
		//return (String)this.datos.get("ANTIGUEDAD");
		String salida = "0";
		//RGG
		for (int i=0;i<numCriterios;i++) {
			ArrayList auxi = (ArrayList)ordenacion.get(i);
			if (((String)auxi.get(0)).equals(DefinirGuardiasTurnosForm.cod_Antig)) {
				if (((String)auxi.get(1)).equals("A")) {
					salida = new Integer(numCriterios-i).toString();
				} else {
					salida = "-" + new Integer(numCriterios-i).toString();
				}
			}
		}
		return salida;
	}
	public String getEdad(){
		String salida = "0";
		//RGG
		for (int i=0;i<numCriterios;i++) {
			ArrayList auxi = (ArrayList)ordenacion.get(i);
			if (((String)auxi.get(0)).equals(DefinirGuardiasTurnosForm.cod_Edad)) {
				if (((String)auxi.get(1)).equals("A")) {
					salida = new Integer(numCriterios-i).toString();
				} else {
					salida = "-" + new Integer(numCriterios-i).toString();
				}
			}
		}
		return salida;
		//return (String)this.datos.get("EDAD");
	}
	public String getAntiguedadEnCola(){
		//return (String)this.datos.get("ANTIGUEDADENCOLA");
		String salida = "0";
		//RGG
		for (int i=0;i<numCriterios;i++) {
			ArrayList auxi = (ArrayList)ordenacion.get(i);
			if (((String)auxi.get(0)).equals(DefinirGuardiasTurnosForm.cod_AntigCola)) {
				if (((String)auxi.get(1)).equals("A")) {
					salida = new Integer(numCriterios-i).toString();
				} else {
					salida = "-" + new Integer(numCriterios-i).toString();
				}
			}
		}
		return salida;
	}
 	
	//Para las pestanhas:
 	public String getIdInstitucionPestanha () {return ((String)this.datos.get("IDINSTITUCIONPESTAÑA"));}
 	public String getIdGuardiaPestanha () {return ((String)this.datos.get("IDGUARDIAPESTAÑA"));}
 	public String getIdTurnoPestanha () {return ((String)this.datos.get("IDTURNOPESTAÑA"));}
	
	
 	
	//////////////////// SETTERS ////////////////////
	public void setIdPersona (String idPersona) {this.idPersona = idPersona;}
	public void setIdInstitucion (String idInstitucion) {this.idInstitucion = idInstitucion;}
	public void setIdPersonaSolicitante (String idPersonaSolicitante) {this.idPersonaSolicitante = idPersonaSolicitante;}
	public void setIdGuardia(String idGuardia) {this.idGuardia = idGuardia;}
	public void setIdTurno(String idTurno) {this.idTurno = idTurno;}
	
	public void setTipoDiasGuardia(String tipoDiasGuardia) {this.tipoDiasGuardia = tipoDiasGuardia;}
	public void setCheckDiasPeriodo(String checkDiasPeriodo) {this.checkDiasPeriodo = checkDiasPeriodo;}
	public void setDiasPeriodo(String diasPeriodo) {this.diasPeriodo = diasPeriodo;}
	public void setTipoDiasPeriodo(String tipoDiasPeriodo) {this.tipoDiasPeriodo = tipoDiasPeriodo;}
	public void setSeleccionLaborablesLunes(String valor) {this.seleccionLaborablesLunes = valor;}
	public void setSeleccionLaborablesMartes(String valor) {this.seleccionLaborablesMartes = valor;}
	public void setSeleccionLaborablesMiercoles(String valor) {this.seleccionLaborablesMiercoles = valor;}
	public void setSeleccionLaborablesJueves(String valor) {this.seleccionLaborablesJueves = valor;}
	public void setSeleccionLaborablesViernes(String valor) {this.seleccionLaborablesViernes = valor;}
	public void setSeleccionLaborablesSabado(String valor) {this.seleccionLaborablesSabado = valor;}
	public void setSeleccionFestivosLunes(String valor) {this.seleccionFestivosLunes = valor;}
	public void setSeleccionFestivosMartes(String valor) {this.seleccionFestivosMartes = valor;}
	public void setSeleccionFestivosMiercoles(String valor) {this.seleccionFestivosMiercoles = valor;}
	public void setSeleccionFestivosJueves(String valor) {this.seleccionFestivosJueves = valor;}
	public void setSeleccionFestivosViernes(String valor) {this.seleccionFestivosViernes = valor;}
	public void setSeleccionFestivosSabado(String valor) {this.seleccionFestivosSabado = valor;}
	public void setSeleccionFestivosDomingo(String valor) {this.seleccionFestivosDomingo = valor;}
	public void setVg(String valor) {this.vg = valor;}
	
	//Metodos set de los campos del formulario
	public void setNombreGuardia(String valor) {this.datos.put("NOMBRE", valor);}
	public void setTurno(String valor) {this.datos.put("TURNO", valor);}
	public void setGuardia(String valor) {this.datos.put("GUARDIA", valor);}
	public void setObligatoriedad(String valor) {this.datos.put("OBLIGATORIEDAD", valor);}
	public void setFechaInscripcion(String valor) {this.datos.put("FECHAINSCRIPCION", valor);}
	public void setObservacionesInscripcion(String valor) {this.datos.put("OBSERVACIONESINSCRIPCION", valor);}
	public void setObservacionesBaja(String valor) {this.datos.put("OBSERVACIONESBAJA", valor);}
	public void setFechaBaja(String valor) {this.datos.put("FECHABAJA", valor);}
	public void setDescripcion(String valor) {this.datos.put("DESCRIPCION",valor);}
	public void setDescripcionFacturacion(String valor) {this.datos.put("DESCRIPCIONFACTURACION",valor);}
	public void setDescripcionPago(String valor) {this.datos.put("DESCRIPCIONPAGO",valor);}
	public void setPartidaPresupuestaria(String valor) {this.datos.put("IDPARTIDAPRESUPUESTARIA",valor);}
	public void setLetradosGuardia(String valor) {this.datos.put("NUMEROLETRADOSGUARDIA",valor);}
	public void setLetradosSustitutos(String valor) {this.datos.put("NUMEROSUSTITUTOSGUARDIA",valor);}
	public void setDuracion(String valor) {this.datos.put("DIASGUARDIA",valor);}
	public void setDiasPagados(String valor) {this.datos.put("DIASPAGADOS",valor);}
	public void setDiasSeparacion(String valor) {this.datos.put("DIASSEPARACIONGUARDIAS",valor);}
	public void setAlfabetico(String valor) {this.datos.put("ALFABETICOAPELLIDOS",valor);}
	public void setNumeroColegiado(String valor) {this.datos.put("NUMEROCOLEGIADO",valor);}
	public void setAntiguedadCola(String valor) {this.datos.put("ANTIGUEDADCOLA",valor);}
	public void setAsistencias(String valor) {this.datos.put("NUMEROASISTENCIAS",valor);}
	public void setActuaciones(String valor) {this.datos.put("NUMEROACTUACIONES",valor);}
	public void setUltimoLetrado(String valor) {this.datos.put("IDPERSONA_ULTIMO",valor);}
	public void setJustificaciones(String valor) {this.datos.put("VALIDARJUSTIFICACIONES",valor);}
	public void setGuardiaElegida(String valor) {this.datos.put("GUARDIAELEGIDA",valor);}
	public void setGuardias(String valor) {this.datos.put("GUARDIAS",valor);}
	
	//Ordenacion - RGG 15-02-2006
	public void setCrit_1 (String valor){ 
		this.datos.put("CRIT_1", valor);
		// RGG
		ArrayList auxi = (ArrayList)ordenacion.get(0);
		auxi.set(0,valor);
		ordenacion.set(0,auxi);
	}
	public void setCrit_2 (String valor){ 
		this.datos.put("CRIT_2", valor);
		// RGG
		ArrayList auxi = (ArrayList)ordenacion.get(1);
		auxi.set(0,valor);
		ordenacion.set(1,auxi);
	}
	public void setCrit_3 (String valor){ 
		this.datos.put("CRIT_3", valor);
		// RGG
		ArrayList auxi = (ArrayList)ordenacion.get(2);
		auxi.set(0,valor);
		ordenacion.set(2,auxi);
	}
	public void setCrit_4 (String valor){ 
		this.datos.put("CRIT_4", valor);
		// RGG
		ArrayList auxi = (ArrayList)ordenacion.get(3);
		auxi.set(0,valor);
		ordenacion.set(3,auxi);
	}
	public void setOrd_1 (String valor){ 
		this.datos.put("ORD_1", valor);
		// RGG
		ArrayList auxi = (ArrayList)ordenacion.get(0);
		auxi.set(1,valor);
		ordenacion.set(0,auxi);
	}
	public void setOrd_2 (String valor){ 
		this.datos.put("ORD_2", valor);
		// RGG
		ArrayList auxi = (ArrayList)ordenacion.get(1);
		auxi.set(1,valor);
		ordenacion.set(1,auxi);
	}
	public void setOrd_3 (String valor){ 
		this.datos.put("ORD_3", valor);
		// RGG
		ArrayList auxi = (ArrayList)ordenacion.get(2);
		auxi.set(1,valor);
		ordenacion.set(2,auxi);
	}
	public void setOrd_4 (String valor){ 
		this.datos.put("ORD_4", valor);
		// RGG
		ArrayList auxi = (ArrayList)ordenacion.get(3);
		auxi.set(1,valor);
		ordenacion.set(3,auxi);
	}
	public void setAlfabeticoApellidos(String valor){
		//this.datos.put("ALFABETICOAPELLIDOS",valor);
		// RGG
		if (!valor.trim().equals("0")) {
			String signo = "";
			String numero = "";
			if (valor.indexOf("-")!=-1) {
				// tiene signo
				signo = "D";
				numero = valor.substring(valor.indexOf("-")+1,valor.length());
			} else {
				signo = "A";
				numero = valor;
			}
			ArrayList auxi = (ArrayList)ordenacion.get(numCriterios-new Integer(numero).intValue());
			auxi.set(0,DefinirGuardiasTurnosForm.cod_Alfabetico);
			auxi.set(1,signo);
			ordenacion.set(numCriterios-new Integer(numero).intValue(),auxi);
		}
	}
	public void setAntiguedad(String valor){
		//this.datos.put("ANTIGUEDAD",valor);
		// RGG
		if (!valor.trim().equals("0")) {
			String signo = "";
			String numero = "";
			if (valor.indexOf("-")!=-1) {
				// tiene signo
				signo = "D";
				numero = valor.substring(valor.indexOf("-")+1,valor.length());
			} else {
				signo = "A";
				numero = valor;
			}
			ArrayList auxi = (ArrayList)ordenacion.get(numCriterios-new Integer(numero).intValue());
			auxi.set(0,DefinirGuardiasTurnosForm.cod_Antig);
			auxi.set(1,signo);
			ordenacion.set(numCriterios-new Integer(numero).intValue(),auxi);
		}
	}
	public void setEdad(String valor){
		//this.datos.put("EDAD",valor);
		// RGG
		if (!valor.trim().equals("0")) {
			String signo = "";
			String numero = "";
			if (valor.indexOf("-")!=-1) {
				// tiene signo
				signo = "D";
				numero = valor.substring(valor.indexOf("-")+1,valor.length());
			} else {
				signo = "A";
				numero = valor;
			}
			ArrayList auxi = (ArrayList)ordenacion.get(numCriterios-new Integer(numero).intValue());
			auxi.set(0,DefinirGuardiasTurnosForm.cod_Edad);
			auxi.set(1,signo);
			ordenacion.set(numCriterios-new Integer(numero).intValue(),auxi);
		}		
	}
	public void setAntiguedadEnCola(String valor){
		//this.datos.put("ANTIGUEDADENCOLA",valor);
		// RGG
		if (!valor.trim().equals("0")) {
			String signo = "";
			String numero = "";
			if (valor.indexOf("-")!=-1) {
				// tiene signo
				signo = "D";
				numero = valor.substring(valor.indexOf("-")+1,valor.length());
			} else {
				signo = "A";
				numero = valor;
			}
			ArrayList auxi = (ArrayList)ordenacion.get(numCriterios-new Integer(numero).intValue());
			auxi.set(0,DefinirGuardiasTurnosForm.cod_AntigCola);
			auxi.set(1,signo);
			ordenacion.set(numCriterios-new Integer(numero).intValue(),auxi);
		}		
	}
	
	//Para las pestanhas:
 	public void setIdInstitucionPestanha (String valor) {this.datos.put("IDINSTITUCIONPESTAÑA",valor);}
 	public void setIdTurnoPestanha (String valor) {this.datos.put("IDTURNOPESTAÑA",valor);}
 	public void setIdGuardiaPestanha (String valor) {this.datos.put("IDGUARDIAPESTAÑA",valor);}
	
	
 	
	//////////////////// CONSTRUCTORES ////////////////////
	//Ordenacion
	public DefinirGuardiasTurnosForm() {
		//Creación del array de arrays para guardar los criterios de ordenación
		for (int i=0;i<numCriterios;i++) {
			ArrayList aux = new ArrayList();
			aux.add(""); // criterio
			aux.add(""); // ordenacion (A/D)
			ordenacion.add(aux);
		}
	}
	
	
	public String getCheckGuardiaDeSustitucion() {
		return checkGuardiaDeSustitucion;
	}
	public void setCheckGuardiaDeSustitucion(String checkGuardiaDeSustitucion) {
		this.checkGuardiaDeSustitucion = checkGuardiaDeSustitucion;
	}
	public String getGuardiaDeSustitucion() {
		return guardiaDeSustitucion;
	}
	public void setGuardiaDeSustitucion(String guardiaDeSustitucion) {
		this.guardiaDeSustitucion = guardiaDeSustitucion;
	}
	public String getSustituta() {
		return sustituta;
	}
	public void setSustituta(String sustituta) {
		this.sustituta = sustituta;
	}
}