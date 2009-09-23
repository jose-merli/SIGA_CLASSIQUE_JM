/*
 * Created on Nov 24, 2004
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
public class CenDireccionTipoDireccionBean extends MasterBean {
	/* Variables */
	private Integer idInstitucion;
	private Long idPersona;
	private Long idDireccion;	
	private Integer idTipoDireccion;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_DIRECCION_TIPODIRECCION";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION		= "IDINSTITUCION";
	static public final String C_IDPERSONA			= "IDPERSONA";
	static public final String C_IDDIRECCION		= "IDDIRECCION";	
	static public final String C_IDTIPODIRECCION	= "IDTIPODIRECCION";	

	//	 Metodos GET		
	public Long getIdDireccion() 			{	return idDireccion;	}	
	public Integer getIdInstitucion()		{	return idInstitucion; }	
	public Long getIdPersona()				{	return idPersona;}
	public Integer getIdTipoDireccion()		{	return idTipoDireccion;	}

	//	 Metodos SET
	public void setIdDireccion(Long idDireccion)			{this.idDireccion = idDireccion;}
	public void setIdInstitucion(Integer idInstitucion) 	{this.idInstitucion = idInstitucion;}
	public void setIdPersona(Long idPersona) 				{this.idPersona = idPersona;}
	public void setIdTipoDireccion(Integer idTipoDireccion) {this.idTipoDireccion = idTipoDireccion;}
}
