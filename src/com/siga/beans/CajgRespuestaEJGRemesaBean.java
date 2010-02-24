package com.siga.beans;

import es.satec.siga.util.SigaSequence;

/**
 * Implementa las operaciones sobre el bean de la tabla CAJG_RESPUESTA_EJGREMESA
 * 
 * @author angel corral
 * @since 17/09/2008
 */

public class CajgRespuestaEJGRemesaBean extends MasterBean{
	
	/*
	 *  Variables */ 
	private SigaSequence idRespuesta = new SigaSequence("SEQ_CAJG_RESPUESTA_EJGREMESA");
	private Integer idEjgRemesa;
	private String codigo;
	private String descripcion;
	private String abreviatura;
	private String fecha;
	
	
	
	/*
	 *  Nombre de Tabla*/
	
	
	static public String T_NOMBRETABLA = "CAJG_RESPUESTA_EJGREMESA";
	
	/*
	 * Nombre de campos de la tabla*/
	static public final String C_IDRESPUESTA = "IDRESPUESTA";
	static public final String C_IDEJGREMESA = "IDEJGREMESA"; 
	static public final String C_CODIGO = "CODIGO";
	static public final String C_DESCRIPCION = "DESCRIPCION"; 
	static public final String C_ABREVIATURA = "ABREVIATURA";
	static public final String C_FECHA = "FECHA";
	 
	
//	
//	/**
//	 * @return the idRespuesta
//	 */
//	public Integer getIdRespuesta() {
//		return this.idRespuesta.getValue();
//	}
//	/**
//	 * @param idRespuesta the idRespuesta to set
//	 */
//	public void setIdRespuesta(Integer idRespuesta) {		
//		this.idRespuesta.setValue(idRespuesta);
//	}
	/**
	 * @return the idEjgRemesa
	 */
	public Integer getIdEjgRemesa() {
		return idEjgRemesa;
	}
	/**
	 * @param idEjgRemesa the idEjgRemesa to set
	 */
	public void setIdEjgRemesa(Integer idEjgRemesa) {
		this.idEjgRemesa = idEjgRemesa;
	}
	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}
	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	/**
	 * @return the abreviatura
	 */
	public String getAbreviatura() {
		return abreviatura;
	}
	/**
	 * @param abreviatura the abreviatura to set
	 */
	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}
	/**
	 * @return the fecha
	 */
	public String getFecha() {
		return fecha;
	}
	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	/**
	 * @return the idRespuesta
	 */
	public SigaSequence getIdRespuesta() {
		return idRespuesta;
	}
	/**
	 * @param idRespuesta the idRespuesta to set
	 */
	public void setIdRespuesta(SigaSequence idRespuesta) {
		this.idRespuesta = idRespuesta;
	}
		
	
}