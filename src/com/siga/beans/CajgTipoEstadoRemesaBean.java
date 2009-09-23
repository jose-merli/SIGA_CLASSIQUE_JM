package com.siga.beans;

/**
 * Implementa las operaciones sobre el bean de la tabla CAJG_TIPOESTADOREMESA
 * 
 * @author fernando.gómez
 * @since 17/09/2008
 */

public class CajgTipoEstadoRemesaBean extends MasterBean{
	
	/*
	 *  Variables */ 
	private Integer idEstado;
	private String  descripcion;
	
	
	
	/*
	 *  Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "CAJG_TIPOESTADOREMESA";
	
	/*
	 * Nombre de campos de la tabla*/
	
	static public final String	C_IDESTADO	=					"IDESTADO";
	static public final String	C_DESCRIPCION=					"DESCRIPCION";
	
	public Integer getIdEstado() {
		return idEstado;
	}
	public void setIdEstado(Integer idEstado) {
		this.idEstado = idEstado;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	
	
	
}