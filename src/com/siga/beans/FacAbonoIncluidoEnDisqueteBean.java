/*
 * VERSIONES:
 * julio.vicente - 11-03-2005 - Creación
 */

package com.siga.beans;


public class FacAbonoIncluidoEnDisqueteBean extends MasterBean {

	/* Variables */
	private Integer idInstitucion;
	private String 	fecha;
	private Long 	idAbono, idDisqueteAbono, contabilizado;
	private Double  importeAbonado;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FAC_ABONOINCLUIDOENDISQUETE";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION 				= "IDINSTITUCION";
	static public final String C_IDABONO					= "IDABONO";
	static public final String C_FECHA						= "FECHA";
	static public final String C_IDDISQUETEABONO			= "IDDISQUETEABONO";
	static public final String C_CONTABILIZADO	 			= "CONTABILIZADO";
	static public final String C_IMPORTEABONADO 			= "IMPORTEABONADO";
	
	/* Métodos get */
	
	public Long getContabilizado() {
		return contabilizado;
	}
	public String getFecha() {
		return fecha;
	}
	public Long getIdAbono() {
		return idAbono;
	}
	public Long getIdDisqueteAbono() {
		return idDisqueteAbono;
	}
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public Double getImporteAbonado() {
		return importeAbonado;
	}
	
	/* Métodos set */
	
	public void setContabilizado(Long contabilizado) {
		this.contabilizado = contabilizado;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public void setIdAbono(Long idAbono) {
		this.idAbono = idAbono;
	}
	public void setIdDisqueteAbono(Long idDisqueteAbono) {
		this.idDisqueteAbono = idDisqueteAbono;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public void setImporteAbonado(Double importeAbonado) {
		this.importeAbonado = importeAbonado;
	}
	}
