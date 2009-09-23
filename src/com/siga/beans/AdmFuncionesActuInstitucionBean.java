/*
 * Created on Nov 18, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

/**
 * @author nuria.rgonzalez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AdmFuncionesActuInstitucionBean extends MasterBean{
	
	/* Variables */
	private Integer idFuncion;
	private String 	automatico;
	private Integer idInstitucion;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "ADM_FUNCIONES_ACTU_INSTITUCION";
	
	/* Nombre campos de la tabla */
	static public final String C_IDFUNCION		= "IDFUNCION";
	static public final String C_AUTOMATICO		= "AUTOMATICO";
	static public final String C_IDINSTITUCION	= "IDINSTITUCION";

	// Metodos SET
	public void setIdFuncion (Integer id) 		{ this.idFuncion = id; }
	public void setAutomatico (String s)		{ this.automatico = s; }
	public void setIdInstitucion (Integer id) 	{ this.idInstitucion = id; }

	// Metodos GET
	public Integer getIdFuncion ()		{ return this.idFuncion; }
	public String getAutomatico ()		{ return this.automatico; }
	public Integer getIdInstitucion ()	{ return this.idInstitucion; }
}
