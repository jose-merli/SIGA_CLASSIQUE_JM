package com.siga.gratuita.form;

import java.util.ArrayList;
import java.util.List;

import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.beans.ScsTurnoBean;
import com.siga.general.MasterForm;

/**
 * @author ruben.fernandez
 * @version 08/03/2006 david.sanchezp: nuevos campos
 */

public class DefinirGuardiasTurnosForm extends MasterForm
{
	//////////////////// ATRIBUTOS DE CLASE ////////////////////
	//Ordenacion
	public static int ORDEN_ALFABETICO = 1;
	public static int ORDEN_ANTIGUEDAD = 2;
	public static int ORDEN_EDAD = 3;
	public static int ORDEN_ANTIGUEDAD_COLA = 4;
	public static int ORDEN_NUMERO = 4;
	
	
	//////////////////// ATRIBUTOS ////////////////////
	// Configuracion de guardia
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
	private String porGrupos;
	private String rotarComponentes;
	private ArrayList ordenacion = new ArrayList();
	
	// Guardias colegiado
	private String idPersona="";
	private String idPersonaSolicitante="";
	private String idInstitucion="";
	private String idTurno="";
	private String idGuardia="";
	private String checkGuardiaDeSustitucion="", guardiaDeSustitucion ="";
	private String comenSustitucion;
	
	// 
	private String diasASeparar;
	private String hayDiasASeparar;
	
	// Inscripciones
	private String validarInscripciones;
	private String observacionesValidacion;
	private String fechaValidacion;
	private String fechaSolicitudBaja;
	private String fechaConsulta;
	
	List<ScsTurnoBean> turnosPrincipales;
	List<ScsGuardiasTurnoBean> guardiasPrincipales;
	private String idInstitucionPrincipal;
	private String idTurnoPrincipal;
	private String idGuardiaPrincipal;
	List<ScsGuardiasTurnoBean> guardiasVinculadas;
	//////////////////// GETTERS ////////////////////
	// Configuracion de guardia
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
	public String getSustituta() {return sustituta;}
	public String getVg() {return this.vg;}
	public String getPorGrupos() {return this.porGrupos;}
	public String getRotarComponentes() {return this.rotarComponentes;}
	public String getCheckGuardiaDeSustitucion() {return checkGuardiaDeSustitucion;}
	public String getGuardiaDeSustitucion() {return guardiaDeSustitucion;}
	
	// Guardias colegiado
	public String getIdPersona() {return idPersona;}
	public String getIdTurno() {return idTurno;}
	public String getIdInstitucion() {return idInstitucion;}
	public String getIdGuardia() {return idGuardia;}
	public String getIdPersonaSolicitante() {return idPersonaSolicitante;}
	public String getComenSustitucion() {return comenSustitucion;}
	
	// 
	public String getDiasASeparar() {return diasASeparar;}
	public String getHayDiasASeparar() {return hayDiasASeparar;}
	
	// Inscripciones
	public String getValidarInscripciones() {return validarInscripciones;}
	public String getObservacionesValidacion() {return observacionesValidacion;}
	public String getFechaValidacion() {return fechaValidacion;}
	public String getFechaSolicitudBaja() {return fechaSolicitudBaja;}
	public String getFechaConsulta() {return fechaConsulta;}
	
	//Criterios de Ordenacion
	public String getCriterio(int i){
		String salida = (String)((ArrayList)ordenacion.get(i-1)).get(0);
		return salida;
	}
	public String getOrden(int i){
		String salida = (String)((ArrayList)ordenacion.get(i-1)).get(1);
		return salida;
	}
	public String getCrit_1(){return getCriterio(1);}
	public String getCrit_2(){return getCriterio(2);}
	public String getCrit_3(){return getCriterio(3);}
	public String getCrit_4(){return getCriterio(4);}
	public String getOrd_1(){return getOrden(1);}
	public String getOrd_2(){return getOrden(2);}
	public String getOrd_3(){return getOrden(3);}
	public String getOrd_4(){return getOrden(4);}
	public String getCriterioParaBD(String codigo) {
		String salida = "0";
		for (int i=0;i<ORDEN_NUMERO;i++) {
			ArrayList auxi = (ArrayList)ordenacion.get(i);
			if (((String)auxi.get(0)).equals(codigo)) {
				if (((String)auxi.get(1)).equals("A")) {
					salida = new Integer(ORDEN_NUMERO-i).toString();
				} else {
					salida = "-" + new Integer(ORDEN_NUMERO-i).toString();
				}
			}
		}
		return salida;
	}
	public String getAlfabeticoApellidos(){return getCriterioParaBD(Integer.toString(ORDEN_ALFABETICO));}
	public String getAntiguedad(){return getCriterioParaBD(Integer.toString(ORDEN_ANTIGUEDAD));}
	public String getEdad(){return getCriterioParaBD(Integer.toString(ORDEN_EDAD));}
	public String getAntiguedadEnCola(){return getCriterioParaBD(Integer.toString(ORDEN_ANTIGUEDAD_COLA));}
 	
	
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
	public String getUltimoGrupoGuardiaLetrado() {return (String) this.datos.get("IDGRUPOGUARDIACOLEGIADO_ULTIMO");}
	public String getJustificaciones() {return (String) this.datos.get("VALIDARJUSTIFICACIONES");}
	public String getGuardiaElegida() {return (String) this.datos.get("GUARDIAELEGIDA");}
	public String getGuardias() {return (String) this.datos.get("GUARDIAS");}
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
	public void setPorGrupos(String valor) {this.porGrupos = valor;}
	public void setRotarComponentes(String valor) {this.rotarComponentes = valor;}
	
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
	public void setUltimoGrupoGuardiaLetrado(String valor) {this.datos.put("IDGRUPOGUARDIACOLEGIADO_ULTIMO",valor);}
	public void setJustificaciones(String valor) {this.datos.put("VALIDARJUSTIFICACIONES",valor);}
	public void setGuardiaElegida(String valor) {this.datos.put("GUARDIAELEGIDA",valor);}
	public void setGuardias(String valor) {this.datos.put("GUARDIAS",valor);}

	public void setCheckGuardiaDeSustitucion(String checkGuardiaDeSustitucion) {this.checkGuardiaDeSustitucion = checkGuardiaDeSustitucion;}
	public void setGuardiaDeSustitucion(String guardiaDeSustitucion) {this.guardiaDeSustitucion = guardiaDeSustitucion;}
	public void setSustituta(String sustituta) {this.sustituta = sustituta;}
	public void setDiasASeparar(String diasASeparar) {this.diasASeparar = diasASeparar;}
	public void setHayDiasASeparar(String hayDiasASeparar) {this.hayDiasASeparar = hayDiasASeparar;}
	public void setComenSustitucion(String comenSustitucion) {this.comenSustitucion = comenSustitucion;}
	public void setValidarInscripciones(String validarInscripciones) {this.validarInscripciones = validarInscripciones;}
	public void setObservacionesValidacion(String observacionesValidacion) {this.observacionesValidacion = observacionesValidacion;}
	public void setFechaValidacion(String fechaValidacion) {this.fechaValidacion = fechaValidacion;}
	public void setFechaSolicitudBaja(String fechaSolicitudBaja) {this.fechaSolicitudBaja = fechaSolicitudBaja;}
	public void setFechaConsulta(String fechaConsulta) {this.fechaConsulta = fechaConsulta;}
	
	//Criterios de Ordenacion
	public void setCriterio(int i, String valor) {
		this.datos.put("CRIT_"+i, valor);
		ArrayList auxi = (ArrayList)ordenacion.get(i-1);
		auxi.set(0, valor);
		ordenacion.set(i-1, auxi);
	}
	public void setOrden(int i, String valor) {
		this.datos.put("ORD_"+i, valor);
		// RGG
		ArrayList auxi = (ArrayList)ordenacion.get(i-1);
		auxi.set(1, valor);
		ordenacion.set(i-1, auxi);
	}
	public void setCrit_1(String valor) {setCriterio(1, valor);}
	public void setCrit_2(String valor) {setCriterio(2, valor);}
	public void setCrit_3(String valor) {setCriterio(3, valor);}
	public void setCrit_4(String valor) {setCriterio(4, valor);}
	public void setOrd_1(String valor) {setOrden(1, valor);}
	public void setOrd_2(String valor) {setOrden(2, valor);}
	public void setOrd_3(String valor) {setOrden(3, valor);}
	public void setOrd_4(String valor) {setOrden(4, valor);}
	
	public void setCriterioParaBD(String codigo, String valor) {
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
			ArrayList auxi = (ArrayList)ordenacion.get(ORDEN_NUMERO-new Integer(numero).intValue());
			auxi.set(0, codigo);
			auxi.set(1, signo);
			ordenacion.set(ORDEN_NUMERO - new Integer(numero).intValue(), auxi);
		}		
	}
	public void setAlfabeticoApellidos(String valor) {setCriterioParaBD(Integer.toString(ORDEN_ALFABETICO), valor);}
	public void setAntiguedad(String valor) {setCriterioParaBD(Integer.toString(ORDEN_ANTIGUEDAD), valor);}
	public void setEdad(String valor) {setCriterioParaBD(Integer.toString(ORDEN_EDAD), valor);}
	public void setAntiguedadEnCola(String valor) {setCriterioParaBD(Integer.toString(ORDEN_ANTIGUEDAD_COLA), valor);}
	
	//Para las pestanhas:
 	public void setIdInstitucionPestanha (String valor) {this.datos.put("IDINSTITUCIONPESTAÑA",valor);}
 	public void setIdTurnoPestanha (String valor) {this.datos.put("IDTURNOPESTAÑA",valor);}
 	public void setIdGuardiaPestanha (String valor) {this.datos.put("IDGUARDIAPESTAÑA",valor);}
	
	
	//////////////////// CONSTRUCTORES ////////////////////
	//Ordenacion
	public DefinirGuardiasTurnosForm() {
		//Creación del array de arrays para guardar los criterios de ordenación
		for (int i=0; i<ORDEN_NUMERO; i++) {
			ArrayList aux = new ArrayList();
			aux.add(""); // criterio
			aux.add(""); // ordenacion (A/D)
			ordenacion.add(aux);
		}
	}
	public List<ScsTurnoBean> getTurnosPrincipales() {
		return turnosPrincipales;
	}
	public void setTurnosPrincipales(List<ScsTurnoBean> turnosPrincipales) {
		this.turnosPrincipales = turnosPrincipales;
	}
	public List<ScsGuardiasTurnoBean> getGuardiasPrincipales() {
		return guardiasPrincipales;
	}
	public void setGuardiasPrincipales(
			List<ScsGuardiasTurnoBean> guardiasPrincipales) {
		this.guardiasPrincipales = guardiasPrincipales;
	}
	public String getIdInstitucionPrincipal() {
		return idInstitucionPrincipal;
	}
	public void setIdInstitucionPrincipal(String idInstitucionPrincipal) {
		this.idInstitucionPrincipal = idInstitucionPrincipal;
	}
	public String getIdTurnoPrincipal() {
		return idTurnoPrincipal;
	}
	public void setIdTurnoPrincipal(String idTurnoPrincipal) {
		this.idTurnoPrincipal = idTurnoPrincipal;
	}
	public String getIdGuardiaPrincipal() {
		return idGuardiaPrincipal;
	}
	public void setIdGuardiaPrincipal(String idGuardiaPrincipal) {
		this.idGuardiaPrincipal = idGuardiaPrincipal;
	}
	public List<ScsGuardiasTurnoBean> getGuardiasVinculadas() {
		return guardiasVinculadas;
	}
	public void setGuardiasVinculadas(List<ScsGuardiasTurnoBean> guardiasVinculadas) {
		this.guardiasVinculadas = guardiasVinculadas;
	}

}