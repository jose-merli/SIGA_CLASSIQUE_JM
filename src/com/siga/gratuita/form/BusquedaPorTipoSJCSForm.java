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
}
