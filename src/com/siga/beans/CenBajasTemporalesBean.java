/*
 * Created on Mar 24, 2009
 * @author jtacosta
 *
 */
package com.siga.beans;

public class CenBajasTemporalesBean extends MasterBean {

	//Variables

	/**
	 * 
	 */
	private static final long serialVersionUID = -2532876089544539337L;
	private Integer idInstitucion;
	private Long idPersona;
	private String tipo;
	private String fechaBT;
	private String fechaDesde;
	private String fechaHasta;
	private String fechaAlta;
	private String descripcion;
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDPERSONA = "IDPERSONA";
	static public final String C_FECHABT = "FECHABT";
	static public final String C_TIPO = "TIPO";
	static public final String C_FECHADESDE = "FECHADESDE";
	static public final String C_FECHAHASTA = "FECHAHASTA";
	static public final String C_FECHAALTA = "FECHAALTA";
	static public final String C_DESCRIPCION = "DESCRIPCION";

	static public final String T_NOMBRETABLA = "CEN_BAJASTEMPORALES";
	public static final String TIPO_COD_VACACION = "V";
	public static final String TIPO_COD_BAJA = "B";
	public static final String TIPO_DESC_VACACION = "censo.bajastemporales.tipo.vacaciones";
	public static final String TIPO_DESC_BAJA = "censo.bajastemporales.tipo.baja";
	
	public Integer getIdInstitucion() {
		return idInstitucion;
	}

	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}

	public Long getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	

	public String getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(String fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public String getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(String fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	public String getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(String fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public String getFechaBT() {
		return fechaBT;
	}

	public void setFechaBT(String fechaBT) {
		this.fechaBT = fechaBT;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	




}
