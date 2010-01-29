package com.siga.beans.eejg;

import com.siga.beans.MasterBean;
import com.siga.beans.ScsUnidadFamiliarEJGBean;
/**
 * 
 * @author jorgeta
 *
 */
public class ScsEejgPeticionesBean extends MasterBean{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6457973849067549976L;
	Long idPeticion;
	Integer idInstitucion;
	String fechaPeticion;
	Integer estado;
	String idSolicitud;
	Integer idTipoEjg;
	Integer anio;
	Integer numero;
	Long idPersona;
	ScsUnidadFamiliarEJGBean unidadFamiliar;
	Integer numeroIntentosSolicitud;
	Integer numeroIntentosConsulta;
	Integer idXml;
	
	
	/* Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "SCS_EEJG_PETICIONES";
	
	
	
	/*Nombre de campos de la tabla*/
	static public final String 	C_IDPETICION = "IDPETICION";
	static public final String 	C_IDUSUARIOPETICION = "IDUSUARIOPETICION";
	static public final String 	C_FECHAPETICION = "FECHAPETICION";
	static public final String 	C_ESTADO = "ESTADO";
	static public final String 	C_IDSOLICITUD = "IDSOLICITUD";
	static public final String 	C_IDINSTITUCION = "IDINSTITUCION";
	static public final String 	C_IDTIPOEJG = "IDTIPOEJG";
	static public final String 	C_ANIO = "ANIO";
	static public final String 	C_NUMERO = "NUMERO";
	static public final String 	C_IDPERSONA = "IDPERSONA";
	static public final String 	C_NUMEROINTENTOSSOLICITUD = "NUMEROINTENTOSSOLICITUD";
	static public final String 	C_NUMEROINTENTOSCONSULTA = "NUMEROINTENTOSCONSULTA";
	static public final String 	C_IDXML = "IDXML";
	public Long getIdPeticion() {
		return idPeticion;
	}
	public void setIdPeticion(Long idPeticion) {
		this.idPeticion = idPeticion;
	}
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public String getFechaPeticion() {
		return fechaPeticion;
	}
	public void setFechaPeticion(String fechaPeticion) {
		this.fechaPeticion = fechaPeticion;
	}
	public Integer getEstado() {
		return estado;
	}
	public void setEstado(Integer estado) {
		this.estado = estado;
	}
	public String getIdSolicitud() {
		return idSolicitud;
	}
	public void setIdSolicitud(String idSolicitud) {
		this.idSolicitud = idSolicitud;
	}
	public Integer getIdTipoEjg() {
		return idTipoEjg;
	}
	public void setIdTipoEjg(Integer idTipoEjg) {
		this.idTipoEjg = idTipoEjg;
	}
	public Integer getAnio() {
		return anio;
	}
	public void setAnio(Integer anio) {
		this.anio = anio;
	}
	public Integer getNumero() {
		return numero;
	}
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	public Long getIdPersona() {
		return idPersona;
	}
	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}
	
	public void setUnidadFamiliar(ScsUnidadFamiliarEJGBean unidadFamiliar) {
		this.unidadFamiliar = unidadFamiliar;
		this.idInstitucion = unidadFamiliar.getIdInstitucion();
		this.idTipoEjg = unidadFamiliar.getIdTipoEJG();
		this.anio = unidadFamiliar.getAnio();
		this.numero = unidadFamiliar.getNumero();
		this.idPersona = Long.parseLong(unidadFamiliar.getIdPersona().toString());
		
	}
	public ScsUnidadFamiliarEJGBean getUnidadFamiliar() {
		unidadFamiliar.setIdInstitucion(idInstitucion);
		unidadFamiliar.setIdTipoEJG(idTipoEjg);
		unidadFamiliar.setAnio(anio);
		unidadFamiliar.setNumero(numero);
		unidadFamiliar.setIdPersona(Integer.parseInt(idPersona.toString()));
		return unidadFamiliar;
	}
	
	public Integer getNumeroIntentosSolicitud() {
		return numeroIntentosSolicitud;
	}
	public void setNumeroIntentosSolicitud(Integer numeroIntentosSolicitud) {
		this.numeroIntentosSolicitud = numeroIntentosSolicitud;
	}
	public Integer getNumeroIntentosConsulta() {
		return numeroIntentosConsulta;
	}
	public void setNumeroIntentosConsulta(Integer numeroIntentosConsulta) {
		this.numeroIntentosConsulta = numeroIntentosConsulta;
	}
	public Integer getIdXml() {
		return idXml;
	}
	public void setIdXml(Integer idXml) {
		this.idXml = idXml;
	}
	
}