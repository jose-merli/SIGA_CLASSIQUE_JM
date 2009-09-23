package com.siga.beans;

/**
 * @author A203486/david.sanchezp
 * @since 05/04/2006  
 */
public class ScsActuacionAsistCosteFijoBean extends MasterBean {
	
	/* Variables */
	private String  facturado, pagado;
	private Integer idInstitucion, anio;
	private Integer idTipoAsistencia, idTipoActuacion, idCosteFijo;
	private Long numero, idActuacion;
	

	/* Nombre tabla */
	static public String T_NOMBRETABLA = "SCS_ACTUACIONASISTCOSTEFIJO";
	
	/* Nombre campos de la tabla */
	static public final String 	C_IDINSTITUCION="IDINSTITUCION";
	static public final String 	C_ANIO="ANIO";     
	static public final String 	C_NUMERO="NUMERO";   
	static public final String 	C_IDACTUACION="IDACTUACION";
	static public final String  C_IDTIPOASISTENCIA = "IDTIPOASISTENCIA";
	static public final String  C_IDTIPOACTUACION = "IDTIPOACTUACION";
	static public final String  C_IDCOSTEFIJO = "IDCOSTEFIJO";
	static public final String  C_FACTURADO = "FACTURADO";
	static public final String  C_PAGADO = "PAGADO";
	static public final String 	C_FECHAMODIFICACION="FECHAMODIFICACION";
	static public final String 	C_USUMODIFICACION="USUMODIFICACION";
	
	
	public Integer getAnio() {
		return anio;
	}
	public void setAnio(Integer anio) {
		this.anio = anio;
	}
	public String getFacturado() {
		return facturado;
	}
	public void setFacturado(String facturado) {
		this.facturado = facturado;
	}
	public Long getIdActuacion() {
		return idActuacion;
	}
	public void setIdActuacion(Long idActuacion) {
		this.idActuacion = idActuacion;
	}
	public Integer getIdCosteFijo() {
		return idCosteFijo;
	}
	public void setIdCosteFijo(Integer idCosteFijo) {
		this.idCosteFijo = idCosteFijo;
	}
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public Integer getIdTipoActuacion() {
		return idTipoActuacion;
	}
	public void setIdTipoActuacion(Integer idTipoActuacion) {
		this.idTipoActuacion = idTipoActuacion;
	}
	public Integer getIdTipoAsistencia() {
		return idTipoAsistencia;
	}
	public void setIdTipoAsistencia(Integer idTipoAsistencia) {
		this.idTipoAsistencia = idTipoAsistencia;
	}
	public Long getNumero() {
		return numero;
	}
	public void setNumero(Long numero) {
		this.numero = numero;
	}
	public String getPagado() {
		return pagado;
	}
	public void setPagado(String pagado) {
		this.pagado = pagado;
	}

	
	

}