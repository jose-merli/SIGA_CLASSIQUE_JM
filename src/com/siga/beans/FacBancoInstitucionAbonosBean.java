/*
 * VERSIONES:
 * jose.barrientos - 24-02-2009 - Creacion
 */

package com.siga.beans;


public class FacBancoInstitucionAbonosBean extends MasterBean {

	/* Variables */
	private Integer idInstitucion, idPagosJG;
	private String 	bancosCodigo, concepto;	
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FCS_BANCOINSTITUCION_ABONOS";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION 				= "IDINSTITUCION";
	static public final String C_BANCOS_CODIGO				= "BANCOS_CODIGO";	
	static public final String C_CONCEPTO		 			= "CONCEPTO";
	static public final String C_IDPAGOSJG					= "IDPAGOSJG";
	
	/* Métodos get */
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public Integer getIdPagosJG() {
		return idPagosJG;
	}
	public String getBancosCodigo() {
		return bancosCodigo;
	}
	public String getConcepto() {
		return concepto;
	}
		
	/* Métodos set */
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public void setIdPagosJG(Integer idPagosJG) {
		this.idPagosJG = idPagosJG;
	}
	public void setBancosCodigo(String bancosCodigo) {
		this.bancosCodigo = bancosCodigo;
	}
	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

}
