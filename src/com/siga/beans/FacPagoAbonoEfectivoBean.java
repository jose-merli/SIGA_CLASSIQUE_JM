/*
 * VERSIONES:
 * julio.vicente - 11-03-2005 - Creación
 */

package com.siga.beans;


public class FacPagoAbonoEfectivoBean extends MasterBean {

	/* Variables */
	private Integer idInstitucion;	
	private String 	fecha, contabilizado;	
	private Long 	idAbono, idPagoAbono;
	private Double  importe;	
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FAC_PAGOABONOEFECTIVO";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION 				= "IDINSTITUCION";
	static public final String C_CONTABILIZADO 				= "CONTABILIZADO";
	static public final String C_FECHA		 				= "FECHA";
	static public final String C_IDABONO	 				= "IDABONO";
	static public final String C_IDPAGOABONO 				= "IDPAGOABONO";
	static public final String C_IMPORTE	 				= "IMPORTE";
	
	
	/* Métodos get */

	public String getContabilizado() {
		return contabilizado;
	}
	public String getFecha() {
		return fecha;
	}
	public Long getIdAbono() {
		return idAbono;
	}
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public Long getIdPagoAbono() {
		return idPagoAbono;
	}
	public Double getImporte() {
		return importe;
	}
	
	/* Métodos set */
	
	public void setContabilizado(String contabilizado) {
		this.contabilizado = contabilizado;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public void setIdAbono(Long idAbono) {
		this.idAbono = idAbono;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public void setIdPagoAbono(Long idPagoAbono) {
		this.idPagoAbono = idPagoAbono;
	}
	public void setImporte(Double importe) {
		this.importe = importe;
	}
	}
