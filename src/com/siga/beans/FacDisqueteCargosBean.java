/*
 * VERSIONES:
 * julio.vicente - 11-03-2005 - Creación
 */

package com.siga.beans;


public class FacDisqueteCargosBean extends MasterBean {

	/* Variables */
	private Integer idInstitucion;
	private String 	bancosCodigo, nombreFichero, fechaCreacion, fechaCargo;	
	private Long 	idDisqueteCargos, idSerieFacturacion, idProgramacion, numeroLineas;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FAC_DISQUETECARGOS";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION 			= "IDINSTITUCION";
	static public final String C_IDDISQUETECARGOS		= "IDDISQUETECARGOS";
	static public final String C_IDSERIEFACTURACION 	= "IDSERIEFACTURACION";
	static public final String C_NOMBREFICHERO		 	= "NOMBREFICHERO";
	static public final String C_FECHACREACION		 	= "FECHACREACION";
	static public final String C_BANCOS_CODIGO		 	= "BANCOS_CODIGO";
	static public final String C_IDPROGRAMACION		 	= "IDPROGRAMACION";
	static public final String C_FECHACARGO				= "FECHACARGO";	
	static public final String C_NUMEROLINEAS 			= "NUMEROLINEAS";			
	
	// Métodos get
		
	public String getBancosCodigo() {
		return bancosCodigo;
	}
	public String getFechaCreacion() {
		return fechaCreacion;
	}
	public Long getIdDisqueteCargos() {
		return idDisqueteCargos;
	}
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public Long getIdProgramacion() {
		return idProgramacion;
	}
	public Long getIdSerieFacturacion() {
		return idSerieFacturacion;
	}
	public String getNombreFichero() {
		return nombreFichero;
	}
	public String getFechaCargo() {
		return fechaCargo;
	}
	public Long getNumeroLineas() {
		return numeroLineas;
	}
	
	//	 Métodos set
	
	public void setBancosCodigo(String bancosCodigo) {
		this.bancosCodigo = bancosCodigo;
	}
	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public void setIdDisqueteCargos(Long idDisqueteCargos) {
		this.idDisqueteCargos = idDisqueteCargos;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public void setIdProgramacion(Long idProgramacion) {
		this.idProgramacion = idProgramacion;
	}
	public void setIdSerieFacturacion(Long idSerieFacturacion) {
		this.idSerieFacturacion = idSerieFacturacion;
	}
	public void setNombreFichero(String nombreFichero) {
		this.nombreFichero = nombreFichero;
	}
	public void setFechaCargo(String fechaCargo) {
		this.fechaCargo = fechaCargo;
	}
	public void setNumeroLineas(Long numeroLineas) {
		this.numeroLineas = numeroLineas;
	}
}
