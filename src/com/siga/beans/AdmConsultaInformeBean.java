/*
 * Created on Mar 9, 2005
 * @author emilio.grau
 *
 */
package com.siga.beans;

/**
 * Bean de la tabla de consultas
 */
public class AdmConsultaInformeBean extends MasterBean
{
	// Atributos
	private Integer idInstitucion;
	private String idPlantilla;
	private Integer idInstitucion_consulta;
	private Integer idConsulta;
	private String nombre;
	private String variasLineas;

	// Nombre tabla
	static public final String T_NOMBRETABLA = "ADM_CONSULTAINFORME";

	// Nombre campos de la tabla
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDPLANTILLA = "IDPLANTILLA";
	static public final String C_IDINSTITUCION_CONSULTA = "IDINSTITUCION_CONSULTA";
	static public final String C_IDCONSULTA = "IDCONSULTA";
	static public final String C_NOMBRE = "NOMBRE";
	static public final String C_VARIASLINEAS = "VARIASLINEAS";

	// Metodos SET
	public void setIdInstitucion			(Integer valor) {this.idInstitucion = valor;}
	public void setIdPlantilla				(String valor) {this.idPlantilla = valor;}
	public void setIdInstitucion_consulta	(Integer valor) {this.idInstitucion_consulta = valor;}
	public void setIdConsulta				(Integer valor) {this.idConsulta = valor;}
	public void setNombre					(String valor) {this.nombre = valor;}
	public void setVariasLineas				(String valor) {this.variasLineas = valor;}

	// Metodos GET
	public Integer getIdInstitucion()			{return idInstitucion;}
	public String getIdPlantilla()				{return idPlantilla;}
	public Integer getIdInstitucion_consulta()	{return idInstitucion_consulta;}
	public Integer getIdConsulta()				{return idConsulta;}
	public String getNombre()					{return nombre;}
	public String getVariasLineas()				{return variasLineas;}

}
