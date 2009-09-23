package com.siga.beans;

public class PCAJGTablasMaestrasBean extends MasterBean
{
	/* Variables */
	private String identificador;
	private String aliasTabla;
	private String idTablaMaestraSIGA;	
	private String tablaRelacion;
	

	/* Nombre campos de la tabla */
	static public final String C_IDENTIFICADOR = "IDENTIFICADOR";	
	static public final String C_ALIAS_TABLA = "ALIASTABLA";
	static public final String C_IDTABLAMAESTRASIGA = "IDTABLAMAESTRASIGA";		
	static public final String C_TABLARELACION = "TABLARELACION";
	
	static public final String T_NOMBRETABLA = "PCAJG_TABLAS_MAESTRAS";

	/**
	 * @return the identificador
	 */
	public String getIdentificador() {
		return identificador;
	}

	/**
	 * @param identificador the identificador to set
	 */
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	/**
	 * @return the aliasTabla
	 */
	public String getAliasTabla() {
		return aliasTabla;
	}

	/**
	 * @param aliasTabla the aliasTabla to set
	 */
	public void setAliasTabla(String aliasTabla) {
		this.aliasTabla = aliasTabla;
	}

	/**
	 * @return the idTablaMaestraSIGA
	 */
	public String getIdTablaMaestraSIGA() {
		return idTablaMaestraSIGA;
	}

	/**
	 * @param idTablaMaestraSIGA the idTablaMaestraSIGA to set
	 */
	public void setIdTablaMaestraSIGA(String idTablaMaestraSIGA) {
		this.idTablaMaestraSIGA = idTablaMaestraSIGA;
	}

	/**
	 * @return the tablaRelacion
	 */
	public String getTablaRelacion() {
		return tablaRelacion;
	}

	/**
	 * @param tablaRelacion the tablaRelacion to set
	 */
	public void setTablaRelacion(String tablaRelacion) {
		this.tablaRelacion = tablaRelacion;
	}   
    
}
