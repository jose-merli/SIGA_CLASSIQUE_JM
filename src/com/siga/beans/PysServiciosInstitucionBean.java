/*
 * VERSIONES:
 * 
 * nuria.rgonzalez	- 1-2-2005 - Inicio
 *	
 */

/**
 * Clase que recoge y establece los valores del bean PYSSERVICIOSINSTITUCION <br/> 
 */
package com.siga.beans;

/**
 * @author nuria.rgonzalez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PysServiciosInstitucionBean extends MasterBean {

	/* Variables */
	private Integer idInstitucion, idTipoServicios;
	private Long 	idServicio, idServiciosInstitucion, idConsulta;
	private Float 	porcentajeIva;
	private String 	descripcion, cuentacontable, inicioFinalPonderado, momentoCargo, criterios;
	private String 	solicitarBaja, solicitarAlta, automatico, fechaBaja, sufijo=null, facturacionPonderada;
	
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "PYS_SERVICIOSINSTITUCION";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION			= "IDINSTITUCION";
	static public final String C_IDTIPOSERVICIOS		= "IDTIPOSERVICIOS";
	static public final String C_IDSERVICIO				= "IDSERVICIO";
	static public final String C_IDSERVICIOSINSTITUCION	= "IDSERVICIOSINSTITUCION";
	static public final String C_DESCRIPCION			= "DESCRIPCION";
	static public final String C_CUENTACONTABLE			= "CUENTACONTABLE";
	static public final String C_INICIOFINALPONDERADO	= "INICIOFINALPONDERADO";
	static public final String C_PORCENTAJEIVA			= "PORCENTAJEIVA";
	static public final String C_MOMENTOCARGO			= "MOMENTOCARGO";
	static public final String C_SOLICITARBAJA			= "SOLICITARBAJA";
	static public final String C_SOLICITARALTA			= "SOLICITARALTA";
	static public final String C_AUTOMATICO				= "AUTOMATICO";
	static public final String C_FECHABAJA				= "FECHABAJA";
	static public final String C_SUFIJO					= "SUFIJO";
	static public final String C_IDCONSULTA				= "IDCONSULTA";
	static public final String C_CRITERIOS				= "CRITERIOS";
	static public final String C_FACTURACIONPONDERADA	= "FACTURACIONPONDERADA";
	
	// Metodos SET

	public void setCuentacontable(String cuentacontable)	{this.cuentacontable = cuentacontable;}
	public void setDescripcion(String descripcion) 			{this.descripcion = descripcion;}
	public void setIdInstitucion(Integer idInstitucion) 	{this.idInstitucion = idInstitucion;}
	public void setIdServicio(Long idServicio) 				{this.idServicio = idServicio;}
	public void setIdServicioInstitucion(Long idServiciosInstitucion) {this.idServiciosInstitucion = idServiciosInstitucion;}
	public void setIdTipoServicios(Integer idTipoServicios) {this.idTipoServicios = idTipoServicios;}
	public void setInicioFinalPonderado(String inicioFinalPonderado) {this.inicioFinalPonderado = inicioFinalPonderado;}
	public void setMomentoCargo(String momentoCargo) 		{this.momentoCargo = momentoCargo;}
	public void setPorcentajeIva(Float porcentajeIva) 		{this.porcentajeIva = porcentajeIva;}
	public void setSolicitarAlta(String solicitarAlta) 		{this.solicitarAlta = solicitarAlta;}
	public void setSolicitarBaja(String solicitarBaja) 		{this.solicitarBaja = solicitarBaja;}
	public void setAutomatico(String automatico) 			{this.automatico = automatico;}
	public void setFechaBaja(String aux) 					{this.fechaBaja = aux;}
	public void setSufijo(String sufijo) 					{this.sufijo = sufijo;}
	public void setIdConsulta(Long d) 						{this.idConsulta = d;}
	public void setCriterios(String d) 						{this.criterios = d;}
	public void setFacturacionPonderada(String d) 						{this.facturacionPonderada = d;}

	// Metodos GET
		
	public String getCuentacontable()			{return this.cuentacontable;}
	public String getDescripcion() 				{return this.descripcion;}
	public Integer getIdInstitucion() 			{return this.idInstitucion;}
	public Long getIdServicio() 				{return this.idServicio;}
	public Long getIdServiciosInstitucion() 	{return this.idServiciosInstitucion;}
	public Integer getIdTipoServicios() 		{return this.idTipoServicios;}
	public String getInicioFinalPonderado() 	{return this.inicioFinalPonderado;}
	public String getMomentoCargo() 			{return this.momentoCargo;}
	public Float getPorcentajeIva() 			{return this.porcentajeIva;}
	public String getSolicitarAlta() 			{return this.solicitarAlta;}
	public String getSolicitarBaja() 			{return this.solicitarBaja;}
	public String getAutomatico() 				{return this.automatico;}
	public String getFechaBaja() 				{return this.fechaBaja;}
	public String getSufijo() 					{return this.sufijo;}
	public Long getIdConsulta() 				{return this.idConsulta;}
	public String getCriterios()				{return this.criterios;}
	public String getFacturacionPonderada()				{return this.facturacionPonderada;}
	

}