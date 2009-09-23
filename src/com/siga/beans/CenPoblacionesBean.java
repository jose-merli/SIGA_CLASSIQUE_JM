/*
 * Created on 22-oct-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

/**
 * @author daniel.campos
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CenPoblacionesBean extends MasterBean {

	/* Variables */
	private Integer idPartido;
	private String 	idPoblacion, idProvincia, nombre, ine, idPoblacionMunicipio;

	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_POBLACIONES";
	/* cambio para codigo ext */
	private String codigoExt;
	static public final String C_CODIGOEXT = "CODIGOEXT";
	public void setCodigoExt (String valor)
	{
		this.codigoExt = valor;
	}
	public String getCodigoExt ()
	{
		return codigoExt;
	}
	//////

	/* Nombre campos de la tabla */
	static public final String C_IDPARTIDO					= "IDPARTIDO";
	static public final String C_IDPOBLACION				= "IDPOBLACION";
	static public final String C_IDPROVINCIA				= "IDPROVINCIA";
	static public final String C_NOMBRE						= "NOMBRE";
	static public final String C_INE     					= "INE";
	static public final String C_IDPOBLACIONMUNICIPIO       = "IDPOBLACIONMUNICIPIO";
	
	// Metodos SET
	public void setIdPartido (Integer id) 			{ this.idPartido = id; }
	public void setIdPoblacion (String s)			{ this.idPoblacion = s; }
	public void setidProvincia (String s)			{ this.idProvincia = s; }
	public void setNombre (String s)				{ this.nombre = s; }
	public void setIne (String s)				    { this.ine = s; }
	public void setIdPoblacionMunicipio (String s)	{ this.idPoblacionMunicipio = s; }

	// Metodos GET
	public Integer getIdPartido 		  ()	{ return this.idPartido; }
	public String getIdPoblacion 		  ()	{ return this.idPoblacion; }
	public String getIdProvincia 		  ()	{ return this.idProvincia; }
	public String getNombre 			  ()	{ return this.nombre; }
	public String getIne    			  ()	{ return this.ine; }
	public String getIdPoblacionMunicipio ()	{ return this.idPoblacionMunicipio; }
}
