/*
 * VERSIONES:
 * julio.vicente - 11-03-2005 - Creación
 */

package com.siga.beans;


public class FacLineaDevoluDisqBancoBean extends MasterBean {

	/* Variables */
	private Integer idInstitucion, idFacturaIncluidaEnDisquete;
	private String 	idRecibo, descripcionMotivos, cargarCliente, contabilizada;	
	private Long 	idDisqueteDevoluciones, idDisqueteCargos;;
	private Double 	gastosDevolucion;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FAC_LINEADEVOLUDISQBANCO";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION 				= "IDINSTITUCION";
	static public final String C_IDDISQUETECARGOS			= "IDDISQUETECARGOS";
	static public final String C_IDRECIBO			 		= "IDRECIBO";
	static public final String C_DESCRIPCIONMOTIVOS	 		= "DESCRIPCIONMOTIVOS";
	static public final String C_IDDISQUETEDEVOLUCIONES		= "IDDISQUETEDEVOLUCIONES";
	static public final String C_IDFACTURAINCLUIDAENDISQUETE	= "IDFACTURAINCLUIDAENDISQUETE";
	static public final String C_CARGARCLIENTE			 	= "CARGARCLIENTE";
	static public final String C_CONTABILIZADA			 	= "CONTABILIZADA";
	static public final String C_GASTOSDEVOLUCION			= "GASTOSDEVOLUCION";
	

	/* Métodos get */
	public String getCargarCliente() {
		return cargarCliente;
	}
	public String getContabilizada() {
		return contabilizada;
	}
	public String getDescripcionMotivos() {
		return descripcionMotivos;
	}
	public Double getGastosDevolucion() {
		return gastosDevolucion;
	}
	public Long getIdDisqueteCargos() {
		return idDisqueteCargos;
	}
	public Long getIdDisqueteDevoluciones() {
		return idDisqueteDevoluciones;
	}
	public Integer getIdFacturaIncluidaEnDisquete() {
		return idFacturaIncluidaEnDisquete;
	}
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public String getIdRecibo() {
		return idRecibo;
	}
	
	/* Métodos set */
	
	public void setCargarCliente(String cargarCliente) {
		this.cargarCliente = cargarCliente;
	}
	public void setContabilizada(String contabilizada) {
		this.contabilizada = contabilizada;
	}
	public void setDescripcionMotivos(String descripcionMotivos) {
		this.descripcionMotivos = descripcionMotivos;
	}
	public void setGastosDevolucion(Double gastosDevolucion) {
		this.gastosDevolucion = gastosDevolucion;
	}
	public void setIdDisqueteCargos(Long idDisqueteCargos) {
		this.idDisqueteCargos = idDisqueteCargos;
	}
	public void setIdDisqueteDevoluciones(Long idDisqueteDevoluciones) {
		this.idDisqueteDevoluciones = idDisqueteDevoluciones;
	}
	public void setIdFacturaIncluidaEnDisquete(Integer idFacturaIncluidaEnDisquete) {
		this.idFacturaIncluidaEnDisquete = idFacturaIncluidaEnDisquete;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public void setIdRecibo(String idRecibo) {
		this.idRecibo = idRecibo;
	}
}
