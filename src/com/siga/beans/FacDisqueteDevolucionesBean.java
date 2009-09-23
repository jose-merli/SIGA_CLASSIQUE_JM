/*
 * VERSIONES:
 * julio.vicente - 11-03-2005 - Creación
 */

package com.siga.beans;


public class FacDisqueteDevolucionesBean extends MasterBean {

	/* Variables */
	private Integer idInstitucion;
	private String 	fechaGeneracion, nombreFichero, bancosCodigo;	
	private Long 	idDisqueteDevoluciones;	
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FAC_DISQUETEDEVOLUCIONES";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION 				= "IDINSTITUCION";	
	static public final String C_FECHAGENERACION	 		= "FECHAGENERACION";
	static public final String C_NOMBREFICHERO		 		= "NOMBREFICHERO";
	static public final String C_BANCOS_CODIGO				= "BANCOS_CODIGO";
	static public final String C_IDDISQUETEDEVOLUCIONES		= "IDDISQUETEDEVOLUCIONES";
	
	
	/* Métodos get */

	public String getBancosCodigo() {
		return bancosCodigo;
	}
	public String getFechaGeneracion() {
		return fechaGeneracion;
	}
	public Long getIdDisqueteDevoluciones() {
		return idDisqueteDevoluciones;
	}
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public String getNombreFichero() {
		return nombreFichero;
	}
	
	/* Métodos set */
	
	public void setBancosCodigo(String bancosCodigo) {
		this.bancosCodigo = bancosCodigo;
	}
	public void setFechaGeneracion(String fechaGeneracion) {
		this.fechaGeneracion = fechaGeneracion;
	}
	public void setIdDisqueteDevoluciones(Long idDisqueteDevoluciones) {
		this.idDisqueteDevoluciones = idDisqueteDevoluciones;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public void setNombreFichero(String nombreFichero) {
		this.nombreFichero = nombreFichero;
	}
}
