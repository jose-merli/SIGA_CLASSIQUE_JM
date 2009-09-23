package com.siga.beans;


public class CajgRemesaResolucionFicheroBean extends MasterBean {
	
	public Integer idRemesaResolucionFichero;
	public Integer idRemesaResolucion;
	public Integer idInstitucion;
	public Integer numeroLinea;
	public String linea;
	public Integer idErroresRemesaResol;
	public String parametrosError;
	
	
	/*
	 *  Nombre de Tabla
	 *  */
	
	static public String T_NOMBRETABLA = "CAJG_REMESARESOLUCIONFICHERO";
	
	/*
	 * Nombre de campos de la tabla
	 * */
	public static final String C_IDREMESARESOLUCIONFICHERO = "IDREMESARESOLUCIONFICHERO";
	public static final String C_IDREMESARESOLUCION = "IDREMESARESOLUCION";
	public static final String C_IDINSTITUCION = "IDINSTITUCION";
	public static final String C_NUMEROLINEA = "NUMEROLINEA";
	public static final String C_LINEA = "LINEA";
	public static final String C_IDERRORESREMESARESOL = "IDERRORESREMESARESOL";
	public static final String C_PARAMETROSERROR = "PARAMETROSERROR";
	/**
	 * @return the idRemesaResolucionFichero
	 */
	public Integer getIdRemesaResolucionFichero() {
		return idRemesaResolucionFichero;
	}
	/**
	 * @param idRemesaResolucionFichero the idRemesaResolucionFichero to set
	 */
	public void setIdRemesaResolucionFichero(Integer idRemesaResolucionFichero) {
		this.idRemesaResolucionFichero = idRemesaResolucionFichero;
	}
	/**
	 * @return the idRemesaResolucion
	 */
	public Integer getIdRemesaResolucion() {
		return idRemesaResolucion;
	}
	/**
	 * @param idRemesaResolucion the idRemesaResolucion to set
	 */
	public void setIdRemesaResolucion(Integer idRemesaResolucion) {
		this.idRemesaResolucion = idRemesaResolucion;
	}
	/**
	 * @return the idInstitucion
	 */
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	/**
	 * @param idInstitucion the idInstitucion to set
	 */
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	/**
	 * @return the linea
	 */
	public String getLinea() {
		return linea;
	}
	/**
	 * @param linea the linea to set
	 */
	public void setLinea(String linea) {
		this.linea = linea;
	}
	/**
	 * @return the idErroresRemesaResol
	 */
	public Integer getIdErroresRemesaResol() {
		return idErroresRemesaResol;
	}
	/**
	 * @param idErroresRemesaResol the idErroresRemesaResol to set
	 */
	public void setIdErroresRemesaResol(Integer idErroresRemesaResol) {
		this.idErroresRemesaResol = idErroresRemesaResol;
	}
	/**
	 * @return the parametrosError
	 */
	public String getParametrosError() {
		return parametrosError;
	}
	/**
	 * @param parametrosError the parametrosError to set
	 */
	public void setParametrosError(String parametrosError) {
		this.parametrosError = parametrosError;
	}
	/**
	 * @return the numeroLinea
	 */
	public Integer getNumeroLinea() {
		return numeroLinea;
	}
	/**
	 * @param numeroLinea the numeroLinea to set
	 */
	public void setNumeroLinea(Integer numeroLinea) {
		this.numeroLinea = numeroLinea;
	}	
		
}