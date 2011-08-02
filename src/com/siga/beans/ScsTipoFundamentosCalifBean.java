package com.siga.beans;

public class ScsTipoFundamentosCalifBean extends MasterBean{
	
	/* Variables */ 
	
	private Integer idFundamentoCalif;		
	private String  codigo;		
	private String 	descripcion;
	private String 	idInstitucion;
	private Integer	idTipoDictamenEJG;
	
	/* Nombre de Tabla*/
	static public String T_NOMBRETABLA = "SCS_TIPOFUNDAMENTOCALIF";
	
	/*Nombre de campos de la tabla*/
	static public final String	C_IDFUNDAMENTOCALIF	="IDFUNDAMENTOCALIF"; 
	static public final String	C_CODIGO 		="CODIGO"; 
	static public final String  C_IDINSTITUCION	="IDINSTITUCION";
	static public final String  C_DESCRIPCION	="DESCRIPCION";
	static public final String  C_IDTIPODICTAMENEJG	="IDTIPODICTAMENEJG";
	
	/*Metodos SET*/
	public void setIdFundamentoCalif	(Integer valor)	{ this.idFundamentoCalif = valor;}
	public void setCodigo		(String valor)	{ this.codigo = valor;}
	public void setDescripcion	(String valor)	{ this.descripcion = valor;}
	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
		
	public void setIdTipoDictamenEJG(Integer idTipoDictamenEJG) {
		this.idTipoDictamenEJG = idTipoDictamenEJG;
	}
	
	/*Metodos GET*/
	
	public Integer getIdFundamentoCalif		()	{ return this.idFundamentoCalif;}
	public String getCodigo				()	{ return this.codigo;}
	public String getDescripcion		()	{ return this.descripcion;}
	public String getIdInstitucion() {
		return idInstitucion;
	}
	public Integer getIdTipoDictamenEJG() {
		return idTipoDictamenEJG;
	}

	
	

	
}