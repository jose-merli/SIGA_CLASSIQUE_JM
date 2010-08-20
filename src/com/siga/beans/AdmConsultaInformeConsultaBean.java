package com.siga.beans;

/**
 * Bean de consultas relacionadas con informes
 */
public class AdmConsultaInformeConsultaBean extends MasterBean
{
	// Atributos
	private Integer idInstitucion;
	private String idPlantilla;
	private Integer idInstitucion_consulta;
	private Integer idConsulta;
	private String nombre;
	private String variasLineas;
	private String descripcion;
	private String general;
	private String sentencia;
	private Integer idModulo;

	// Nombre campos de la tabla
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDPLANTILLA = "IDPLANTILLA";
	static public final String C_IDINSTITUCION_CONSULTA = "IDINSTITUCION_CONSULTA";
	static public final String C_IDCONSULTA = "IDCONSULTA";
	static public final String C_NOMBRE = "NOMBRE";
	static public final String C_VARIASLINEAS = "VARIASLINEAS";
	static public final String C_DESCRIPCION = "DESCRIPCION";
	static public final String C_GENERAL = "GENERAL";
	static public final String C_SENTENCIA = "SENTENCIA";
	static public final String C_IDMODULO = "IDMODULO";

	// Metodos SET
	public void setIdInstitucion			(Integer valor) {this.idInstitucion = valor;}
	public void setIdPlantilla				(String valor) {this.idPlantilla = valor;}
	public void setIdInstitucion_consulta	(Integer valor) {this.idInstitucion_consulta = valor;}
	public void setIdConsulta				(Integer valor) {this.idConsulta = valor;}
	public void setNombre					(String valor) {this.nombre = valor;}
	public void setVariasLineas				(String valor) {this.variasLineas = valor;}
	public void setDescripcion				(String valor) {this.descripcion = valor;}
	public void setGeneral					(String valor) {this.general = valor;}
	public void setSentencia				(String valor) {this.sentencia = valor;}
	public void setIdModulo					(Integer valor) {this.idModulo = valor;}

	// Metodos GET
	public Integer getIdInstitucion()			{return idInstitucion;}
	public String getIdPlantilla()				{return idPlantilla;}
	public Integer getIdInstitucion_consulta()	{return idInstitucion_consulta;}
	public Integer getIdConsulta()				{return idConsulta;}
	public String getNombre()					{return nombre;}
	public String getVariasLineas()				{return variasLineas;}
	public String getDescripcion()				{return descripcion;}
	public String getGeneral()					{return general;}
	public String getSentencia()				{return sentencia;}
	public Integer getIdModulo()				{return idModulo;}

}
