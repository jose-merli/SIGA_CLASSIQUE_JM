/*
 * Created on 22-nov-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

/**
 * @author daniel.campos
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CenClienteBean extends MasterBean {
	/* Variables */
	private Integer idInstitucion, idTratamiento;
	Long idPersona;
	
	

	private String 	abonosBanco, asientoContable, caracter, cargosBanco, comisiones, fechaAlta, fotografia, 
					guiaJudicial, idLenguaje, publicidad,  letrado, fechaCarga,norevista,noredabogacia, exportarFoto;
	private boolean existeDatos;

	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_CLIENTE";
	
	/* Nombre campos de la tabla */
	static public final String C_ABONOSBANCO 		= "ABONOSBANCO";
	static public final String C_ASIENTOCONTABLE	= "ASIENTOCONTABLE";
	static public final String C_CARACTER			= "CARACTER";
	static public final String C_CARGOSBANCO		= "CARGOSBANCO";
	static public final String C_COMISIONES			= "COMISIONES";
	static public final String C_FECHAALTA			= "FECHAALTA";
	static public final String C_FOTOGRAFIA			= "FOTOGRAFIA";
	static public final String C_GUIAJUDICIAL		= "GUIAJUDICIAL";
	static public final String C_IDINSTITUCION		= "IDINSTITUCION";
	static public final String C_IDLENGUAJE			= "IDLENGUAJE";
	static public final String C_IDPERSONA			= "IDPERSONA";
	static public final String C_IDTRATAMIENTO		= "IDTRATAMIENTO";
	static public final String C_PUBLICIDAD			= "PUBLICIDAD";
	static public final String C_LETRADO			= "LETRADO";
	static public final String C_FECHACARGA			= "FECHACARGA";
	static public final String C_NOENVIARREVISTA			= "NOENVIARREVISTA";
	static public final String C_NOAPARECERREDABOGACIA			= "NOAPARECERREDABOGACIA";
	static public final String C_EXPORTARFOTO		= "EXPORTARFOTO";


	/**
	 * @return Returns the abonosBanco.
	 */
	public String getAbonosBanco() {
		return abonosBanco;
	}
	/**
	 * @param abonosBanco The abonosBanco to set.
	 */
	public void setAbonosBanco(String abonosBanco) {
		this.abonosBanco = abonosBanco;
	}
	/**
	 * @return Returns the asientoContable.
	 */
	public String getAsientoContable() {
		return asientoContable;
	}
	/**
	 * @param asientoContable The asientoContable to set.
	 */
	public void setAsientoContable(String asientoContable) {
		this.asientoContable = asientoContable;
	}
	/**
	 * @return Returns the caracter.
	 */
	public String getCaracter() {
		return caracter;
	}
	/**
	 * @param caracter The caracter to set.
	 */
	public void setCaracter(String caracter) {
		this.caracter = caracter;
	}
	/**
	 * @return Returns the cargosBanco.
	 */
	public String getCargosBanco() {
		return cargosBanco;
	}
	/**
	 * @param cargosBanco The cargosBanco to set.
	 */
	public void setCargosBanco(String cargosBanco) {
		this.cargosBanco = cargosBanco;
	}
	/**
	 * @return Returns the comisiones.
	 */
	public String getComisiones() {
		return comisiones;
	}
	/**
	 * @param comisiones The comisiones to set.
	 */
	public void setComisiones(String comisiones) {
		this.comisiones = comisiones;
	}
	/**
	 * @return Returns the fechaAlta.
	 */
	public String getFechaAlta() {
		return fechaAlta;
	}
	/**
	 * @param fechaAlta The fechaAlta to set.
	 */
	public void setFechaAlta(String fechaAlta) {
		this.fechaAlta = fechaAlta;
	}
	/**
	 * @return Returns the fotografia.
	 */
	public String getFotografia() {
		return fotografia;
	}
	/**
	 * @param fotografia The fotografia to set.
	 */
	public void setFotografia(String fotografia) {
		this.fotografia = fotografia;
	}
	/**
	 * @return Returns the guiaJudicial.
	 */
	public String getGuiaJudicial() {
		return guiaJudicial;
	}
	/**
	 * @param guiaJudicial The guiaJudicial to set.
	 */
	public void setGuiaJudicial(String guiaJudicial) {
		this.guiaJudicial = guiaJudicial;
	}
	/**
	 * @return Returns the idInstitucion.
	 */
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	/**
	 * @param idInstitucion The idInstitucion to set.
	 */
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	/**
	 * @return Returns the idLenguaje.
	 */
	public String getIdLenguaje() {
		return idLenguaje;
	}
	/**
	 * @param idLenguaje The idLenguaje to set.
	 */
	public void setIdLenguaje(String idLenguaje) {
		this.idLenguaje = idLenguaje;
	}
	/**
	 * @return Returns the idPersona.
	 */
	public Long getIdPersona() {
		return idPersona;
	}
	/**
	 * @param idPersona The idPersona to set.
	 */
	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}
	/**
	 * @return Returns the idTratamiento.
	 */
	public Integer getIdTratamiento() {
		return idTratamiento;
	}
	/**
	 * @param idTratamiento The idTratamiento to set.
	 */
	public void setIdTratamiento(Integer idTratamiento) {
		this.idTratamiento = idTratamiento;
	}
	/**
	 * @return Returns the publicidad.
	 */
	public String getPublicidad() {
		return publicidad;
	}
	/**
	 * @param publicidad The publicidad to set.
	 */
	public void setPublicidad(String publicidad) {
		this.publicidad = publicidad;
	}
	
	public String getLetrado() {
		return letrado;
	}

	public void setLetrado(String letrado) {
		this.letrado = letrado;
	}
	public String getFechaCarga() {
		return fechaCarga;
	}

	public void setFechaCarga(String fechaCarga) {
		this.fechaCarga = fechaCarga;
	}
	public String getNoEnviarRevista() {
		return norevista;
	}

	public void setNoEnviarRevista(String valor) {
		this.norevista= valor;
	}
	public String getNoAparacerRedAbogacia() {
		return noredabogacia;
	}

	public void setNoAparacerRedAbogacia(String valor) {
		this.noredabogacia = valor;
	}
	
	public boolean isExisteDatos() {
		return existeDatos;
	}
	public void setExisteDatos(boolean existeDatos) {
		this.existeDatos = existeDatos;
	}
	public String getExportarFoto() {
		return exportarFoto;
	}
	public void setExportarFoto(String exportarFoto) {
		this.exportarFoto = exportarFoto;
	}
	
	
}
