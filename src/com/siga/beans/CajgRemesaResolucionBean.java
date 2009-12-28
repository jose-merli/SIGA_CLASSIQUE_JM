package com.siga.beans;


public class CajgRemesaResolucionBean extends MasterBean {
	
	public Integer idRemesaResolucion;
	public Integer idInstitucion;
	public String prefijo;
	public String numero;
	public String sufijo;
	public String nombreFichero;
	public String observaciones;
	public String fechaCarga;
	public String fechaCargaHasta;
	public String fechaResolucion;	
	public String fechaResolucionHasta;
	public String logGenerado;
	public Integer idTipoRemesa;
	
	
	/*
	 *  Nombre de Tabla
	 *  */
	
	static public String T_NOMBRETABLA = "CAJG_REMESARESOLUCION";
	
	/*
	 * Nombre de campos de la tabla
	 * */
	public static final String C_IDREMESARESOLUCION = "IDREMESARESOLUCION";
	public static final String C_IDINSTITUCION = "IDINSTITUCION";
	public static final String C_PREFIJO = "PREFIJO";
	public static final String C_NUMERO = "NUMERO";
	public static final String C_SUFIJO = "SUFIJO";
	public static final String C_NOMBREFICHERO = "NOMBREFICHERO";
	public static final String C_OBSERVACIONES = "OBSERVACIONES";
	public static final String C_FECHACARGA = "FECHACARGA";
	public static final String c_FECHACARGAHASTA = "FECHACARGAHASTA";
	public static final String C_FECHARESOLUCION = "FECHARESOLUCION";
	public static final String C_FECHARESOLUCIONHASTA = "FECHARESOLUCIONHASTA";
	public static final String C_LOGGENERADO = "LOGGENERADO";
	public static final String C_IDTIPOREMESA = "IDTIPOREMESA";
	
	
	/**
	 * @return the idResolucion
	 */
	public Integer getIdRemesaResolucion() {
		return idRemesaResolucion;
	}
	/**
	 * @param idResolucion the idResolucion to set
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
	 * @return the prefijo
	 */
	public String getPrefijo() {
		return prefijo;
	}
	/**
	 * @param prefijo the prefijo to set
	 */
	public void setPrefijo(String prefijo) {
		this.prefijo = prefijo;
	}
	/**
	 * @return the numero
	 */
	public String getNumero() {
		return numero;
	}
	/**
	 * @param numero the numero to set
	 */
	public void setNumero(String numero) {
		this.numero = numero;
	}
	/**
	 * @return the sufijo
	 */
	public String getSufijo() {
		return sufijo;
	}
	/**
	 * @param sufijo the sufijo to set
	 */
	public void setSufijo(String sufijo) {
		this.sufijo = sufijo;
	}
	/**
	 * @return the fechaResolucion
	 */
	public String getFechaResolucion() {
		return fechaResolucion;
	}
	/**
	 * @param fechaResolucion the fechaResolucion to set
	 */
	public void setFechaResolucion(String fechaResolucion) {
		this.fechaResolucion = fechaResolucion;
	}

	/**
	 * @return the fechaResolucionHasta
	 */
	public String getFechaResolucionHasta() {
		return fechaResolucionHasta;
	}
	/**
	 * @param fechaResolucionHasta the fechaResolucionHasta to set
	 */
	public void setFechaResolucionHasta(String fechaResolucionHasta) {
		this.fechaResolucionHasta = fechaResolucionHasta;
	}
	/**
	 * @return the observaciones
	 */
	public String getObservaciones() {
		return observaciones;
	}
	/**
	 * @param observaciones the observaciones to set
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
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
	 * @return the fechaCarga
	 */
	public String getFechaCarga() {
		return fechaCarga;
	}
	/**
	 * @param fechaCarga the fechaCarga to set
	 */
	public void setFechaCarga(String fechaCarga) {
		this.fechaCarga = fechaCarga;
	}
	/**
	 * @return the fechaCargaHasta
	 */
	public String getFechaCargaHasta() {
		return fechaCargaHasta;
	}
	/**
	 * @param fechaCargaHasta the fechaCargaHasta to set
	 */
	public void setFechaCargaHasta(String fechaCargaHasta) {
		this.fechaCargaHasta = fechaCargaHasta;
	}
	/**
	 * @return the logGenerado
	 */
	public String getLogGenerado() {
		return logGenerado;
	}
	/**
	 * @param logGenerado the logGenerado to set
	 */
	public void setLogGenerado(String logGenerado) {
		this.logGenerado = logGenerado;
	}	
	/**
	 * @return the idTipoRemesa
	 */
	public Integer getIdTipoRemesa() {
		return idTipoRemesa;
	}
	/**
	 * @param idTipoRemesa the idTipoRemesa to set
	 */
	public void setIdTipoRemesa(Integer idTipoRemesa) {
		this.idTipoRemesa = idTipoRemesa;
	}
	
	

	


		
}