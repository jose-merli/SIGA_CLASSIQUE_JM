package com.siga.gratuita.form;

/**
 * @author ruben.fernandez
 */
import com.siga.general.*;
import java.util.*;


public class DefinirTurnosForm extends MasterForm {

	private ArrayList ordenacion = new ArrayList();
	public static String cod_Alfabetico="1";
	public static String cod_Antig="2";
	public static String cod_Edad="3";
	public static String cod_AntigCola="4";
	public static int numCriterios=4;
	public String activarRestriccionActuacion = "";
	public String activarActuacionesLetrado = "";
	public String activarAsistenciasLetrado = "";
	public String incluirRegistrosConBajaLogica = "S";
	
	public DefinirTurnosForm() {
		//Creación del array de arrays para guardar los criterios de ordenación
		for (int i=0;i<numCriterios;i++) {
			ArrayList aux = new ArrayList();
			aux.add(""); // criterio
			aux.add(""); // ordenacion (A/D)
			ordenacion.add(aux);
		}
			
	}
	
	public String getActivarActuacionesLetrado() {
		return activarActuacionesLetrado;
	}
	public void setActivarActuacionesLetrado(String activarActuacionesLetrado) {
		this.activarActuacionesLetrado = activarActuacionesLetrado;
	}
	public String getActivarAsistenciasLetrado() {
		return activarAsistenciasLetrado;
	}
	public void setActivarAsistenciasLetrado(String activarAsistenciasLetrado) {
		this.activarAsistenciasLetrado = activarAsistenciasLetrado;
	}
	public String getActivarRestriccionActuacion() {
		return this.activarRestriccionActuacion;
	}
	public void setActivarRestriccionActuacion(String activarRestriccionActuacion) {
		this.activarRestriccionActuacion = activarRestriccionActuacion;
	}
//metodos set de los campos del formulario
    
	public void setnLetrados (String valor){ 
		this.datos.put("NLETRADOS", valor);
	}
	 
	public void setDescripcion (String valor){ 
		this.datos.put("DESCRIPCION", valor);
	}
	
	public void setRequisitos (String valor){ 
		this.datos.put("REQUISITOS", valor);
	}
	
	public void setAbreviatura (String valor){ 
		this.datos.put("ABREVIATURA", valor.toUpperCase());
	}
	
	public void setNombre (String valor){ 
		this.datos.put("NOMBRE", valor.toUpperCase());
	}
	
	
	
	public void setArea (String valor){ 
		String aux="";
		int longitud = valor.indexOf(","); 
		if (longitud>0){
			aux= valor.substring(longitud+1,valor.length());
			this.datos.put("IDAREA", aux);
		}
		else this.datos.put("IDAREA",valor);
	}
	
	public void setMateria (String valor){ 
		this.datos.put("IDMATERIA", valor);
	}
	
	public void setZona (String valor){ 
		String aux="";
		int longitud = valor.indexOf(","); 
		if (longitud>0){
			aux= valor.substring(longitud+1,valor.length());
			this.datos.put("IDZONA", aux);
		}
		else this.datos.put("IDZONA",valor);
	}
	
	public void setIdZona (String valor){ 
		String aux="";
		int longitud = valor.indexOf(","); 
		if (longitud>0){
			aux= valor.substring(longitud+1,valor.length());
			this.datos.put("IDIDZONA", aux);
		}
		else this.datos.put("IDIDZONA",valor);
	}
	
	public void setSubzona (String valor){ 
		this.datos.put("IDSUBZONA", valor);
	}
	
	public void setPartidoJudicial (String valor){ 
		this.datos.put("IDPARTIDOJUDICIAL", valor);
	}
	
	public void setPartidaPresupuestaria (String valor){ 
		this.datos.put("IDPARTIDAPRESUPUESTARIA", valor);
	}
	
	public void setGrupoFacturacion (String valor){ 
		this.datos.put("IDGRUPOFACTURACION", valor);
	}

	public void setGuardias (String valor){ 
		this.datos.put("GUARDIAS", valor);
	}
	
	public void setValidarJustificaciones (String valor){
		this.datos.put("VALIDARJUSTIFICACIONES",valor);
	}
	public void setDesignaDirecta (String valor){
		this.datos.put("DESIGNADIRECTA",valor);
	}
	public void setValidacionInscripcion(String valor){
		this.datos.put("VALIDARINSCRIPCIONES",valor);
	}
	
	public void setAlfabeticoApellidos(String valor){
//		this.datos.put("ALFABETICOAPELLIDOS",valor);
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
			auxi.set(0,cod_Alfabetico);
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
			auxi.set(0,cod_Antig);
			auxi.set(1,signo);
			ordenacion.set(numCriterios-new Integer(numero).intValue(),auxi);
		}
	}
	
	public void setEdad(String valor){
//		this.datos.put("EDAD",valor);
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
			auxi.set(0,cod_Edad);
			auxi.set(1,signo);
			ordenacion.set(numCriterios-new Integer(numero).intValue(),auxi);
		}		
	}
	
	public void setAntiguedadEnCola(String valor){
//		this.datos.put("ANTIGUEDADENCOLA",valor);
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
			auxi.set(0,cod_AntigCola);
			auxi.set(1,signo);
			ordenacion.set(numCriterios-new Integer(numero).intValue(),auxi);
		}		
	}
	
	public void setUltimoLetrado(String valor){
		this.datos.put("IDPERSONAULTIMO",valor);
	}
	
	public void setIdTurno(String valor){
		this.datos.put("IDTURNO",valor);
	}
	
	public void setOrigen (String valor){ 
		this.datos.put("ORIGEN", valor);
	}

	public void setModal (String valor){ 
		this.datos.put("MODAL", valor);
	}

// RGG 15-02-2006
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
	
	
	//Metodos get
	
	
	public String getnLetrados (){ 
		return (String)this.datos.get("NLETRADOS");
	}
	
	public String getModal(){
		return (String)this.datos.get("MODAL");
	}
	
	public String getDescripcion(){
		return (String)this.datos.get("DESCRIPCION");
	}
	
	public String getRequisitos(){
		return (String)this.datos.get("REQUISITOS");
	}
	
	public String getIdTurno(){
		return (String)this.datos.get("IDTURNO");
	}
	
	public String getAbreviatura() 	{ 
 		return (String)this.datos.get("ABREVIATURA");
 	} 	
	
	public String getNombre(){
		return (String)this.datos.get("NOMBRE");
	}
	
	
	public String getArea(){
		return (String)this.datos.get("IDAREA");
	}
	
	public String getMateria(){
		return (String)this.datos.get("IDMATERIA");
	}
	
	public String getZona(){
		return (String)this.datos.get("IDZONA");
	}

	public String getIdZona(){
		return (String)this.datos.get("IDIDZONA");
	}

	public String getSubzona(){
		return (String)this.datos.get("IDSUBZONA");
	}
	
	public String getPartidoJudicial(){
		return (String)this.datos.get("IDPARTIDOJUDICIAL");
	}
	
	public String getPartidaPresupuestaria(){
		return (String)this.datos.get("IDPARTIDAPRESUPUESTARIA");
	}
	
	public String getGrupoFacturacion(){
		return (String)this.datos.get("IDGRUPOFACTURACION");
	}
	
	public String getGuardias (){ 
		return (String)this.datos.get("GUARDIAS");
	}
	
	public String getValidarJustificaciones (){
		return (String)this.datos.get("VALIDARJUSTIFICACIONES");
	}
	public String getDesignaDirecta (){
		return (String)this.datos.get("DESIGNADIRECTA");
	}
	public String getValidacionInscripcion(){
		return (String)this.datos.get("VALIDACIONINSCRIPCION");
	}

	
	public String getAlfabeticoApellidos(){
//		return (String)this.datos.get("ALFABETICOAPELLIDOS");
		String salida = "0";
//		 RGG
		for (int i=0;i<numCriterios;i++) {
			ArrayList auxi = (ArrayList)ordenacion.get(i);
			if (((String)auxi.get(0)).equals(cod_Alfabetico)) {
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
//		return (String)this.datos.get("ANTIGUEDAD");
		String salida = "0";
//		 RGG
		for (int i=0;i<numCriterios;i++) {
			ArrayList auxi = (ArrayList)ordenacion.get(i);
			if (((String)auxi.get(0)).equals(cod_Antig)) {
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
//		 RGG
		for (int i=0;i<numCriterios;i++) {
			ArrayList auxi = (ArrayList)ordenacion.get(i);
			if (((String)auxi.get(0)).equals(cod_Edad)) {
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
//		 RGG
		for (int i=0;i<numCriterios;i++) {
			ArrayList auxi = (ArrayList)ordenacion.get(i);
			if (((String)auxi.get(0)).equals(cod_AntigCola)) {
				if (((String)auxi.get(1)).equals("A")) {
					salida = new Integer(numCriterios-i).toString();
				} else {
					salida = "-" + new Integer(numCriterios-i).toString();
				}
			}
		}
		return salida;
	}
	
	public String getUltimoLetrado(){
		return (String)this.datos.get("IDPERSONAULTIMO");
	}
	
	public String getOrigen(){
		return (String)this.datos.get("ORIGEN");
	}

// RGG 15-02-2006
	public String getCrit_1(){
//		return (String)this.datos.get("CRIT_1");
//		RGG
		String salida="";
		ArrayList auxi = (ArrayList)ordenacion.get(0);
		salida = (String)auxi.get(0);
		return salida;
	}
	public String getCrit_2(){
//		return (String)this.datos.get("CRIT_2");
//		RGG
		String salida="";
		ArrayList auxi = (ArrayList)ordenacion.get(1);
		salida = (String)auxi.get(0);
		return salida;

	}
	public String getCrit_3(){
//		return (String)this.datos.get("CRIT_3");
//		RGG
		String salida="";
		ArrayList auxi = (ArrayList)ordenacion.get(2);
		salida = (String)auxi.get(0);
		return salida;
		
	}
	public String getCrit_4(){
//		return (String)this.datos.get("CRIT_4");
//		RGG
		String salida="";
		ArrayList auxi = (ArrayList)ordenacion.get(3);
		salida = (String)auxi.get(0);
		return salida;
		
	}

	public String getOrd_1(){
//		return (String)this.datos.get("ORD_1");
//		RGG
		String salida="";
		ArrayList auxi = (ArrayList)ordenacion.get(0);
		salida = (String)auxi.get(1);
		return salida;
		
	}
	public String getOrd_2(){
//		return (String)this.datos.get("ORD_2");
//		RGG
		String salida="";
		ArrayList auxi = (ArrayList)ordenacion.get(1);
		salida = (String)auxi.get(1);
		return salida;
	}
	public String getOrd_3(){
//		return (String)this.datos.get("ORD_3");
//		RGG
		String salida="";
		ArrayList auxi = (ArrayList)ordenacion.get(2);
		salida = (String)auxi.get(1);
		return salida;
		
	}
	public String getOrd_4(){
//		return (String)this.datos.get("ORD_4");
//		RGG
		String salida="";
		ArrayList auxi = (ArrayList)ordenacion.get(3);
		salida = (String)auxi.get(1);
		return salida;
		
	}

	public String getIncluirRegistrosConBajaLogica() {
		return this.incluirRegistrosConBajaLogica;
	}
	
	public void setIncluirRegistrosConBajaLogica(String s) {
		this.incluirRegistrosConBajaLogica = s;
	}
	
	
}