package com.siga.informes.form;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.MasterForm;

/**
 * @author JBD
 */
public class InformesFacturacionMultipleForm extends MasterForm {

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
	/**
	 * 
	 * @return
	 */
	public String getAsolicitantes() {
		return UtilidadesHash.getString(this.datos,"ASOLICITANTES");
	}
	/**
	 * @param solicitantes
	 */
	public void setAsolicitantes(String solicitantes) {
		UtilidadesHash.set(this.datos,"ASOLICITANTES",solicitantes);
	}
	
	/**
	 * @return
	 */
	public String getIdTipoEJG() {
		return UtilidadesHash.getString(this.datos,"IDTIPOEJG");
	}

	/**
	 * @param idTipoEJG
	 */
	public void setIdTipoEJG(String idTipoEJG) {
		UtilidadesHash.set(this.datos,"IDTIPOEJG", idTipoEJG);
	}
	
	/**
	 * @return
	 */
	public String getAnio() {
		return UtilidadesHash.getString(this.datos,"ANIO");
	}

	/**
	 * @param anio
	 */
	public void setAnio(String anio) {
		UtilidadesHash.set(this.datos,"ANIO", anio);
	}
	
	/**
	 * @return
	 */
	public String getNumero() {
		return UtilidadesHash.getString(this.datos,"NUMERO");
	}

	/**
	 * @param numero
	 */
	public void setNumero(String numero) {
		UtilidadesHash.set(this.datos,"NUMERO", numero);
	}

}
