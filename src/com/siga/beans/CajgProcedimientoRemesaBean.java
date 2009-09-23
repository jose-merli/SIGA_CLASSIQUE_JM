package com.siga.beans;

public class CajgProcedimientoRemesaBean extends MasterBean {
	 
	private Integer idInstitucion;
	private String consulta;
	private Integer cabecera;
	private Integer idProcRemesa;
	private String delimitador;
	private String nombreFichero;
	private String saltoLinea;
	private String subCabecera;	
	
	static public String T_NOMBRETABLA = "CAJG_PROCEDIMIENTOREMESA";	
	
	static public final String C_IDINSTITUCION	= "IDINSTITUCION";
	static public final String C_CONSULTA = "CONSULTA";
	static public final String C_CABECERA = "CABECERA";
	static public final String C_IDPROCREMESA = "IDPROCREMESA";	
	static public final String	C_DELIMITADOR = "DELIMITADOR";
	static public final String C_NOMBREFICHERO = "NOMBREFICHERO";
	static public final String	C_SALTOLINEA = "SALTOLINEA";
	static public final String	C_SUBCABECERA = "SUBCABECERA";
	
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
	 * @return the consulta
	 */
	public String getConsulta() {
		return consulta;
	}
	/**
	 * @param consulta the consulta to set
	 */
	public void setConsulta(String consulta) {
		this.consulta = consulta;
	}
	/**
	 * @return the cabecera
	 */
	public Integer getCabecera() {
		return cabecera;
	}
	/**
	 * @param cabecera the cabecera to set
	 */
	public void setCabecera(Integer cabecera) {
		this.cabecera = cabecera;
	}
	/**
	 * @return the idProcRemesa
	 */
	public Integer getIdProcRemesa() {
		return idProcRemesa;
	}
	/**
	 * @param idProcRemesa the idProcRemesa to set
	 */
	public void setIdProcRemesa(Integer idProcRemesa) {
		this.idProcRemesa = idProcRemesa;
	}
	/**
	 * @return the delimitador
	 */
	public String getDelimitador() {
		return delimitador;
	}
	/**
	 * @param delimitador the delimitador to set
	 */
	public void setDelimitador(String delimitador) {
		this.delimitador = delimitador;
	}
	/**
	 * @return the nombreFichero
	 */
	public String getNombreFichero() {
		return nombreFichero;
	}
	/**
	 * @param nombreFichero the nombreFichero to set
	 */
	public void setNombreFichero(String nombreFichero) {
		this.nombreFichero = nombreFichero;
	}
	/**
	 * @return the saltoLinea
	 */
	public String getSaltoLinea() {
		return saltoLinea;
	}
	/**
	 * @param saltoLinea the saltoLinea to set
	 */
	public void setSaltoLinea(String saltoLinea) {
		this.saltoLinea = saltoLinea;
	}
	/**
	 * @return the subCabecera
	 */
	public String getSubCabecera() {
		return subCabecera;
	}
	/**
	 * @param subCabecera the subCabecera to set
	 */
	public void setSubCabecera(String subCabecera) {
		this.subCabecera = subCabecera;
	}
	
	

	
}