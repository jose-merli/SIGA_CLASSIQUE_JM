package com.siga.beans;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_EJG
 * 
 * @author ruben.fernandez
 * @since 20/1/2005
 */

public class ScsEJGDESIGNABean extends MasterBean{
	
	/*
	 *  Variables */ 
	private Integer idInstitucion;
	private Integer idTipoEJG;
	private Integer	anioEJG;
	private Integer numeroEJG;
	private Integer idTurno;
	private Integer anioDesigna;
	private Integer numeroDesigna;
	
	/*
	 *  Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "SCS_EJGDESIGNA";
	
	/*
	 * Nombre de campos de la tabla*/
	static public final String	C_IDINSTITUCION	=				"IDINSTITUCION";
	static public final String	C_ANIOEJG =						"ANIOEJG";
	static public final String	C_NUMEROEJG	=					"NUMEROEJG";
	static public final String	C_IDTIPOEJG=					"IDTIPOEJG";
	static public final String	C_IDTURNO	=					"IDTURNO";
	static public final String	C_ANIODESIGNA	=				"ANIODESIGNA";
	static public final String	C_NUMERODESIGNA	=				"NUMERODESIGNA";
	
	
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	
	
	public Integer getIdTipoEJG() {
		return idTipoEJG;
	}
	
	public void setIdTipoEJG(Integer idTipoEJG) {
		this.idTipoEJG = idTipoEJG;
	}


	public Integer getAnioEJG() {
		return anioEJG;
	}
	
	public void setAnioEJG(Integer anioEJG) {
		this.anioEJG = anioEJG;
	}

	public Integer getNumeroEJG() {
		return numeroEJG;
	}
	
	public void setNumeroEJG(Integer numeroEJG) {
		this.numeroEJG = numeroEJG;
	}
	
	
	public Integer getAnioDesigna() {
		return anioDesigna;
	}

	public void setAnioDesigna(Integer anioDesigna) {
		this.anioDesigna = anioDesigna;
	}


	public Integer getIdTurno() {
		return idTurno;
	}

	public void setIdTurno(Integer idTurno) {
		this.idTurno = idTurno;
	}
	
	
	public Integer getNumeroDesigna() {
		return numeroDesigna;
	}

	public void setNumeroDesigna(Integer numeroDesigna) {
		this.numeroDesigna = numeroDesigna;
	}
	

	
}