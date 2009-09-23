/*
 * Created on May 27, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.informes.form;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.MasterForm;

/**
 * @author david.sanchezp
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MantenimientoInformesForm extends MasterForm {

	/**
	 * @param valor
	 */
	public void setIdioma(String valor){
		UtilidadesHash.set(this.datos,"IDIOMA",valor);
	}
	/**
	 * @param valor
	 */
	public void setIdPago(String valor){
		UtilidadesHash.set(this.datos,"IDPAGO",valor);
	}
	/**
	 * @param valor
	 */
	public void setIdFacturacion(String valor){
		UtilidadesHash.set(this.datos,"IDFACTURACION",valor);
	}
	/**
	 * @param valor
	 */
	public void setNombreFichero(String valor){
		UtilidadesHash.set(this.datos,"NOMBREFICHERO",valor);
	}
	/**
	 * @param valor
	 */
	public void setRutaFichero(String valor){
		UtilidadesHash.set(this.datos,"RUTAFICHERO",valor);
	}
	/**
	 * @param valor
	 */
	public void setTipoInforme(String valor){
		UtilidadesHash.set(this.datos,"TIPOINFORME",valor);
	}	
	
	/**
	 * @param valor
	 * @return
	 */
	public String getTipoInforme(){
		return UtilidadesHash.getString(this.datos,"TIPOINFORME");
	}
	/**
	 * @param valor
	 * @return
	 */
	public String getIdioma(){
		return UtilidadesHash.getString(this.datos,"IDIOMA");
	}
	/**
	 * @param valor
	 * @return
	 */
	public String getIdPago(){
		return UtilidadesHash.getString(this.datos,"IDPAGO");
	}
	/**
	 * @param valor
	 * @return
	 */
	public String getIdFacturacion(){
		return UtilidadesHash.getString(this.datos,"IDFACTURACION");
	}
	/**
	 * @param valor
	 * @return
	 */
	public String getNombreFichero(){
		return UtilidadesHash.getString(this.datos,"NOMBREFICHERO");
	}
	/**
	 * @param valor
	 * @return
	 */
	public String getRutaFichero(){
		return UtilidadesHash.getString(this.datos,"RUTAFICHERO");
	}
	private String idInstitucion;
	private String letrado;
	private String numeroNifTagBusquedaPersonas;
	private String nombrePersona;
	private String  interesadoNombre;
	private String  interesadoApellido1;
	private String  interesadoApellido2;
	private String  interesadoNif;
	private String  idPersona;
	String[] parametrosComboPago = null;
	
	public String getLetrado() {
		return letrado;
	}
	public void setLetrado(String letrado) {
		this.letrado = letrado;
	}
	public String getNumeroNifTagBusquedaPersonas() {
		return numeroNifTagBusquedaPersonas;
	}
	public void setNumeroNifTagBusquedaPersonas(String numeroNifTagBusquedaPersonas) {
		this.numeroNifTagBusquedaPersonas = numeroNifTagBusquedaPersonas;
	}
	public String getNombrePersona() {
		return nombrePersona;
	}
	public void setNombrePersona(String nombrePersona) {
		this.nombrePersona = nombrePersona;
	}
	public String getInteresadoNombre() {
		return interesadoNombre;
	}
	public void setInteresadoNombre(String interesadoNombre) {
		this.interesadoNombre = interesadoNombre;
	}
	
	public String getInteresadoApellido1() {
		return interesadoApellido1;
	}
	public void setInteresadoApellido1(String interesadoApellido1) {
		this.interesadoApellido1 = interesadoApellido1;
	}
	public String getInteresadoApellido2() {
		return interesadoApellido2;
	}
	public void setInteresadoApellido2(String interesadoApellido2) {
		this.interesadoApellido2 = interesadoApellido2;
	}
	public String getInteresadoNif() {
		return interesadoNif;
	}
	public void setInteresadoNif(String interesadoNif) {
		this.interesadoNif = interesadoNif;
	}
	public String getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public String getIdPersona() {
		return idPersona;
	}
	public void setIdPersona(String idPersona) {
		this.idPersona = idPersona;
	}
	public String[] getParametrosComboPago() {
		return parametrosComboPago;
	}
	public void setParametrosComboPago(String[] parametrosComboPago) {
		this.parametrosComboPago = parametrosComboPago;
	}
}
