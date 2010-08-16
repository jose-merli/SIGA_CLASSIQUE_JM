package com.siga.beans;

public class AdmTipoFiltroInformeBean extends MasterBean {

	// Atributos
	private String idTipoInforme;
	private Integer idTipoFiltroInforme;
	private String nombreCampo;
	private String obligatorio;

	// Nombre tabla
	static public String T_NOMBRETABLA = "ADM_TIPOFILTROINFORME";

	// Nombre campos de la tabla
	static public final String C_IDTIPOINFORME = "IDTIPOINFORME";
	static public final String C_IDTIPOFILTROINFORME = "IDTIPOFILTROINFORME";
	static public final String C_NOMBRECAMPO = "NOMBRECAMPO";
	static public final String C_OBLIGATORIO = "OBLIGATORIO";

	// Metodos SET
	public void setIdTipoInforme		(String valor) {this.idTipoInforme = valor;}
	public void setIdTipoFiltroInforme	(Integer valor) {this.idTipoFiltroInforme = valor;}
	public void setNombreCampo			(String valor) {this.nombreCampo = valor;}
	public void setObligatorio			(String valor) {this.obligatorio = valor;}

	// Metodos GET
	public String getIdTipoInforme()		{return idTipoInforme;}
	public Integer getIdTipoFiltroInforme()	{return idTipoFiltroInforme;}
	public String getNombreCampo()			{return nombreCampo;}
	public String getObligatorio()			{return obligatorio;}
}
