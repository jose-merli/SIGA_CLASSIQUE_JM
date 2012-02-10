/*
 * VERSIONES:
 * miguel.villegas - 4-2-2005 - Creacion
 *	
 */

/**
 * Clase que recoge y establece los valores del bean PYSPRECIOSSERVICIOS <br/> 
 */
package com.siga.beans;

public class PysPreciosServiciosBean extends MasterBean{

	/* Variables */
	private Integer idInstitucion;
	private Integer idTipoServicios;
	private Long idServicio;
	private Long idServiciosInstitucion;
	private Integer idPeriodicidad;
	private Integer idPreciosServicios;
	private Double valor;
	private String 	criterios;
	private String 	porDefecto;
	private Long idConsulta;	
	private String descripcion;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "PYS_PRECIOSSERVICIOS";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDTIPOSERVICIOS = "IDTIPOSERVICIOS";
	static public final String C_IDSERVICIO = "IDSERVICIO";
	static public final String C_IDSERVICIOSINSTITUCION	= "IDSERVICIOSINSTITUCION";
	static public final String C_IDPERIODICIDAD	= "IDPERIODICIDAD";
	static public final String C_IDPRECIOSSERVICIOS	= "IDPRECIOSSERVICIOS";
	static public final String C_VALOR = "VALOR";
	static public final String C_CRITERIOS = "CRITERIOS";
	static public final String C_IDCONSULTA	= "IDCONSULTA";
	static public final String C_PORDEFECTO	= "PORDEFECTO";
	static public final String C_DESCRIPCION = "DESCRIPCION";
	

	// Metodos SET
	public void setIdInstitucion (Integer id)	{ this.idInstitucion=id;}
	public void setIdTipoServicios (Integer id)	{ this.idTipoServicios=id;}
	public void setIdServicio (Long id)	{ this.idServicio=id;}
	public void setIdServiciosInstitucion (Long id)	{ this.idServiciosInstitucion=id;}
	public void setIdPeriodicidad (Integer id)	{  this.idPeriodicidad=id;}
	public void setIdPreciosServicios (Integer id)	{ this.idPreciosServicios=id;}
	public void setValor (Double v)	{ this.valor=v;}
	public void setCriterios (String v)	{ this.criterios=v;}
	public void setIdConsulta (Long id)	{ this.idConsulta=id;}
	public void setPorDefecto (String id)	{ this.porDefecto=id;}
	public void setDescripcion(String descripcion) {this.descripcion = descripcion;}
	
	// Metodos GET
	public Integer getIdInstitucion() {return this.idInstitucion;}
	public Integer getIdTipoServicios()	{return this.idTipoServicios;}
	public Long getIdServicio() {return this.idServicio;}
	public Long getIdServiciosInstitucion()	{return this.idServiciosInstitucion;}
	public Integer getIdPeriodicidad() {return  this.idPeriodicidad;}
	public Integer getIdPreciosServicios() {return this.idPreciosServicios;}
	public Double getValor() {return this.valor;}
	public String getCriterios() {return this.criterios;}
	public Long getIdConsulta()	{return this.idConsulta;}
	public String getPorDefecto()	{return this.porDefecto;}
	public String getDescripcion() {return descripcion;}		
}
