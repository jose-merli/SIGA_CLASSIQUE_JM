package com.siga.censo.form;

import java.util.ArrayList;

import com.siga.general.MasterForm;

/**
 * 
 * @author josebd
 * 
 */
public class AlterMutuaHerederoForm extends MasterForm {

	private String nombre;
    private String apellidos;
    private String tipoIdentificador;
    private String identificador;
    private String fechaNacimiento;
    private int parentescoHeredero;
	private int sexo;

	private ArrayList tiposParentesco;
	private ArrayList tiposSexo;
	private ArrayList tiposIdentificacion;
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	public String getTipoIdentificador() {
		return tipoIdentificador;
	}
	public void setTipoIdentificador(String tipoIdentificador) {
		this.tipoIdentificador = tipoIdentificador;
	}
	public String getIdentificador() {
		return identificador;
	}
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}
	public String getFechaNacimiento() {
		return fechaNacimiento;
	}
	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	public int getParentescoHeredero() {
		return parentescoHeredero;
	}
	public void setParentescoHeredero(int parentescoHeredero) {
		this.parentescoHeredero = parentescoHeredero;
	}
	public int getSexoHeredero() {
		return sexo;
	}
	public void setSexoHeredero(int sexoHeredero) {
		this.sexo = sexoHeredero;
	}
	public ArrayList getTiposParentesco() {
		return tiposParentesco;
	}
	public void setTiposParentesco(ArrayList tiposParentesco) {
		this.tiposParentesco = tiposParentesco;
	}
	public ArrayList getTiposSexo() {
		return tiposSexo;
	}
	public void setTiposSexo(ArrayList tiposSexo) {
		this.tiposSexo = tiposSexo;
	}
	public ArrayList getTiposIdentificacion() {
		return tiposIdentificacion;
	}
	public void setTiposIdentificacion(ArrayList tiposIdentificacion) {
		this.tiposIdentificacion = tiposIdentificacion;
	}

}