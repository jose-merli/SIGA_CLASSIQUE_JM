package com.siga.censo.vos;

import java.util.Date;

import com.siga.comun.vos.Vo;

public class PersonaVO extends Vo{

	private Boolean busquedaExacta;
	private String idPersona;
	private String apellidos1;
	private String apellidos2;
	private String nif;
	private String nombre;
	private Date fechaNacimiento;
	private String noAparecerRedAbogacia;
	
	
	public String getId(){
		return getIdPersona();
	}
	public void setId(String id){
		setIdPersona(id);
	}
	
	public String getIdPersona(){
		return idPersona;
	}
	public void setIdPersona(String idPersona){
		this.idPersona = idPersona;
	}
	
	public String getApellidos1() {
		return apellidos1;
	}


	public void setApellidos1(String apellidos1) {
		this.apellidos1 = apellidos1;
	}


	public String getApellidos2() {
		return apellidos2;
	}


	public void setApellidos2(String apellidos2) {
		this.apellidos2 = apellidos2;
	}


	public String getNif() {
		return nif;
	}


	public void setNif(String nif) {
		this.nif = nif;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}


	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}


	public void setBusquedaExacta(Boolean busquedaExacta) {
		this.busquedaExacta = busquedaExacta;
	}


	public Boolean isBusquedaExacta() {
		return busquedaExacta;
	}


	public void setNoAparecerRedAbogacia(String noAparecerRedAbogacia) {
		this.noAparecerRedAbogacia = noAparecerRedAbogacia;
	}


	public String getNoAparecerRedAbogacia() {
		return noAparecerRedAbogacia;
	}

}