package com.siga.beans;

/**
 * Implementa las operaciones sobre el bean de la tabla CAJG_REMESAESTADOS
 * 
 * @author fernando.gómez
 * @since 17/09/2008
 */

public class CajgRemesaEstadosBean extends MasterBean{
	
	/*
	 *  Variables */ 
	private Integer idInstitucion;
	private Integer idRemesa;
	private Integer	idestado;
	private String  fecharemesa;
	
	
	
	/*
	 *  Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "CAJG_REMESAESTADOS";
	
	/*
	 * Nombre de campos de la tabla*/
	static public final String	C_IDINSTITUCION	=				"IDINSTITUCION";
	static public final String	C_IDREMESA =					"IDREMESA";
	static public final String	C_IDESTADO	=					"IDESTADO";
	static public final String	C_FECHAREMESA=					"FECHAREMESA";
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public Integer getIdRemesa() {
		return idRemesa;
	}
	public void setIdRemesa(Integer idRemesa) {
		this.idRemesa = idRemesa;
	}
	public Integer getIdestado() {
		return idestado;
	}
	public void setIdestado(Integer idestado) {
		this.idestado = idestado;
	}
	public String getFecharemesa() {
		return fecharemesa;
	}
	public void setFecharemesa(String fecharemesa) {
		this.fecharemesa = fecharemesa;
	}
	
	
	
	
	
}