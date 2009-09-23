package com.siga.beans;

public class ScsTipoFundamentosBean extends MasterBean{
	
	/* Variables */ 
	
	private Integer idFundamento;		
	private String  codigo;		
	private String 	descripcion;	
	
	/* Nombre de Tabla*/
	static public String T_NOMBRETABLA = "SCS_TIPOFUNDAMENTOS";
	
	/*Nombre de campos de la tabla*/
	static public final String	C_IDFUNDAMENTO	="IDFUNDAMENTO"; 
	static public final String	C_CODIGO 		="CODIGO"; 
	static public final String  C_DESCRIPCION	="DESCRIPCION";
	
	/*Metodos SET*/
	public void setIdFundamento	(Integer valor)	{ this.idFundamento = valor;}
	public void setCodigo		(String valor)	{ this.codigo = valor;}
	public void setDescripcion	(String valor)	{ this.descripcion = valor;}
		
	/*Metodos GET*/
	
	public Integer getIdFundamento		()	{ return this.idFundamento;}
	public String getCodigo				()	{ return this.codigo;}
	public String getDescripcion		()	{ return this.descripcion;}
	
}