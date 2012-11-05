/**
 * Form que representa los campos de la ventana de busqueda modal de persona JG  
 * @author AtosOrigin SAE - S233735 
 * @since 31-04-2006
 */
package com.siga.gratuita.form;

import com.siga.general.MasterForm;


public class BusquedaPorTipoSJCSForm extends MasterForm 
{
	String tipo, anio, numero, idInstitucion, idTipoEJG, turnoDesigna,idTipoSOJ;
	
	private String nif, nombre, apellido1, apellido2;

	public String getIdTipoSOJ() {
		return idTipoSOJ;
	}
	public void setIdTipoSOJ(String idTipoSOJ) {
		this.idTipoSOJ = idTipoSOJ;
	}
	
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getAnio() {
		return anio;
	}
	public void setAnio(String anio) {
		this.anio = anio;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public String getIdTipoEJG() {
		return idTipoEJG;
	}
	public void setIdTipoEJG(String idTipoEJG) {
		this.idTipoEJG = idTipoEJG;
	}
	public String getTurnoDesigna() {
		return turnoDesigna;
	}
	public void setTurnoDesigna(String turnoDesigna) {
		this.turnoDesigna = turnoDesigna;
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
	public String getApellido1() {
		return apellido1;
	}
	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}
	public String getApellido2() {
		return apellido2;
	}
	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}
	
}
