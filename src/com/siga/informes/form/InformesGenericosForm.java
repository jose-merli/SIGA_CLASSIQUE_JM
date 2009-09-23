package com.siga.informes.form;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.MasterForm;

/**
 * @author RGG
 */
public class InformesGenericosForm extends MasterForm {

	/**
	 * @param valor
	 */
	public void setIdInstitucion(String valor){
		UtilidadesHash.set(this.datos,"IDINSTITUCION",valor);
	}
	/**
	 * @param valor
	 */
	public void setDatosInforme(String valor){
		UtilidadesHash.set(this.datos,"DATOSINFORME",valor);
	}
	/**
	 * @param valor
	 */
	public void setIdInforme(String valor){
		UtilidadesHash.set(this.datos,"IDINFORME",valor);
	}
	/**
	 * @param valor
	 */
	public void setIdTipoInforme(String valor){
		UtilidadesHash.set(this.datos,"IDTIPOINFORME",valor);
	}
	/**
	 * @param valor
	 */
	public void setSeleccionados(String valor){
		UtilidadesHash.set(this.datos,"SELECCIONADOS",valor);
	}
	
	/**
	 * @param valor
	 * @return
	 */
	public String getIdInstitucion(){
		return UtilidadesHash.getString(this.datos,"IDINSTITUCION");
	}
	/**
	 * @param valor
	 * @return
	 */
	public String getDatosInforme(){
		return UtilidadesHash.getString(this.datos,"DATOSINFORME");
	}
	/**
	 * @param valor
	 * @return
	 */
	public String getIdInforme(){
		return UtilidadesHash.getString(this.datos,"IDINFORME");
	}
	/**
	 * @param valor
	 * @return
	 */
	public String getIdTipoInforme(){
		return UtilidadesHash.getString(this.datos,"IDTIPOINFORME");
	}
	/**
	 * @param valor
	 * @return
	 */
	public String getSeleccionados(){
		return UtilidadesHash.getString(this.datos,"SELECCIONADOS");
	}
	
	public String getAsolicitantes() {
		return UtilidadesHash.getString(this.datos,"ASOLICITANTES");
	}
	public void setAsolicitantes(String solicitantes) {
		UtilidadesHash.set(this.datos,"ASOLICITANTES",solicitantes);
	}
	
	public String getIdTipoEJG() {
		return UtilidadesHash.getString(this.datos,"IDTIPOEJG");
	}

	public void setIdTipoEJG(String idTipoEJG) {
		UtilidadesHash.set(this.datos,"IDTIPOEJG", idTipoEJG);
	}
	
	public String getAnio() {
		return UtilidadesHash.getString(this.datos,"ANIO");
	}

	public void setAnio(String anio) {
		UtilidadesHash.set(this.datos,"ANIO", anio);
	}
	
	public String getNumero() {
		return UtilidadesHash.getString(this.datos,"NUMERO");
	}

	public void setNumero(String numero) {
		UtilidadesHash.set(this.datos,"NUMERO", numero);
	}
	public String getEnviar() {
		return UtilidadesHash.getString(this.datos,"ENVIAR");
	}

	public void setEnviar(String enviar) {
		UtilidadesHash.set(this.datos,"ENVIAR", enviar);
	}
	public String getDescargar() {
		return UtilidadesHash.getString(this.datos,"DESGARGAR");
	}

	public void setDescargar(String descargar) {
		UtilidadesHash.set(this.datos,"DESGARGAR", descargar);
	}
	public String getTipoPersonas() {
		return (String) this.datos.get("tipoPersonas");
	}
	public void setTipoPersonas(String tipoPersonas) {
		this.datos.put("tipoPersonas",tipoPersonas);
	}
	public String getClavesIteracion() {
		return (String) this.datos.get("clavesIteracion");
	}
	public void setClavesIteracion(String clavesIteracion) {
		this.datos.put("clavesIteracion",clavesIteracion);
	}

}
