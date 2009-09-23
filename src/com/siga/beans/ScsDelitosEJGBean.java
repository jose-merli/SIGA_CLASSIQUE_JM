package com.siga.beans;

/**
 * Bean de la tabla SCS_DELITOSEJG
 * 
 * @author david.sanchezp
 * @since 24/01/2006
 */
public class ScsDelitosEJGBean extends MasterBean{
	
	/* Variables */ 
	private Integer	idDelito, anio, numero, idTipoEJG, idInstitucion;
	
	/* Nombre de Tabla*/
	static public String T_NOMBRETABLA = "SCS_DELITOSEJG";
	
	/*Nombre de campos de la tabla*/
	static public final String 	C_IDINSTITUCION = "IDINSTITUCION";
	static public final String 	C_ANIO 		    = "ANIO";
	static public final String 	C_NUMERO  		= "NUMERO";
	static public final String 	C_IDTIPOEJG 	= "IDTIPOEJG";
	static public final String 	C_IDDELITO 		= "IDDELITO";
	
	

	/**
	 * @return Returns the anio.
	 */
	public Integer getAnio() {
		return anio;
	}
	/**
	 * @param anio The anio to set.
	 */
	public void setAnio(Integer anio) {
		this.anio = anio;
	}
	/**
	 * @return Returns the idDelito.
	 */
	public Integer getIdDelito() {
		return idDelito;
	}
	/**
	 * @param idDelito The idDelito to set.
	 */
	public void setIdDelito(Integer idDelito) {
		this.idDelito = idDelito;
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
	 * @return Returns the idTipoEJG.
	 */
	public Integer getIdTipoEJG() {
		return idTipoEJG;
	}
	/**
	 * @param idTipoEJG The idTipoEJG to set.
	 */
	public void setIdTipoEJG(Integer idTipoEJG) {
		this.idTipoEJG = idTipoEJG;
	}
	/**
	 * @return Returns the numero.
	 */
	public Integer getNumero() {
		return numero;
	}
	/**
	 * @param numero The numero to set.
	 */
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
}