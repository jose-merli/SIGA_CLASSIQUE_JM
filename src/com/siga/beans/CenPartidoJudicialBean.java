package com.siga.beans;

/**
* @author ruben.fernandez
*
* TODO To change the template for this generated type comment go to
* Window - Preferences - Java - Code Style - Code Templates
*/

public class CenPartidoJudicialBean extends MasterBean
{
	/* Variables */ 

	private Integer	idPartido;
	private String	nombre;
	private String idInstitucionPropietario;

	/* Nombre de Tabla*/

	static public String T_NOMBRETABLA = "CEN_PARTIDOJUDICIAL";

	/*Nombre de campos de la tabla*/

	static public final String	C_IDPARTIDO = 				 "IDPARTIDO";
	static public final String 	C_NOMBRE = 					 "NOMBRE";
	static public final String 	C_IDINSTITUCIONPROPIETARIO = "IDINSTITUCIONPROPIETARIO";
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

	/*Metodos SET*/
	
	public void setIdPartido				(Integer valor)	{ this.idPartido = valor;}
	public void setNombre					(String valor)	{ this.nombre = valor;}
	public void setIdInstitucionPropietario (String valor)	{ this.idInstitucionPropietario = valor;}

	/*Metodos GET*/

	public Integer getIdPartido				  ()	{ return this.idPartido;}
	public String getNombre					  ()	{ return this.nombre;}
	public String getIdInstitucionPropietario ()	{ return this.idInstitucionPropietario;}
}