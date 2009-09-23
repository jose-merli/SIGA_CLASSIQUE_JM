/*
 * Created on 16-03-2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

/**
 * @author pilar.duran
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CenEstadoActividadPersonaBean extends MasterBean{

	/* Variables */
	private Integer idEstado;
	private String 	motivo;
	private Integer idPersona;
	private String 	fechaEstado;
	private Integer idCodigo;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_ESTADOACTIVIDADPERSONA";
	
	/* Nombre campos de la tabla */
	static public final String C_IDESTADO		= "IDESTADO";
	
	static public final String C_MOTIVO	= "MOTIVO";

	static public final String C_IDPERSONA	= "IDPERSONA";
	static public final String C_FECHAESTADO	= "FECHAESTADO";
	static public final String C_IDCODIGO	= "IDCODIGO";
	//////	
	// Metodos SET
	public void setIdEstado (Integer id) 	{ this.idEstado = id; }
	public void setMotivo (String s)		{ this.motivo= s; }
	public void setIdPersona (Integer id) 	{ this.idPersona = id; }
	public void setFechaEstado (String s)		{ this.fechaEstado = s; }
	public void setIdCodigo (Integer id)		{ this.idCodigo = id; }

	// Metodos GET
	public Integer getIdEstado()	    { return this.idEstado; }
	public String getMotivo    ()	    { return this.motivo; }
	public Integer getIdPersona   ()	{ return this.idPersona; }
	public String getfechaEstado   ()	{ return this.fechaEstado; }
	public Integer getIdCodigo()	    { return this.idCodigo; }
}
