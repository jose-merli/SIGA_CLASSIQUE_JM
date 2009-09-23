package com.siga.beans;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_LISTAGUARDIAS
 * 
 * @author david.sanchezp
 * @since 27/12/2004
 */

public class ScsListaGuardiasBean extends MasterBean{
	
	/* Variables */ 
	
	private Integer 	idLista;
	private String 		nombre;
	private String 		lugar;
	private String 		observaciones;
	private Integer 	idInstitucion;
	
	/* Nombre de Tabla */
	
	static public String T_NOMBRETABLA = "SCS_LISTAGUARDIAS";
	
	
	/* Nombre de campos de la tabla */
	
	static public final String 	C_IDLISTA = 				"IDLISTA";
	static public final String 	C_NOMBRE = 					"NOMBRE";
	static public final String 	C_LUGAR = 					"LUGAR";
	static public final String 	C_OBSERVACIONES = 			"OBSERVACIONES";	
	static public final String	C_IDINSTITUCION = 			"IDINSTITUCION";
	
	
	/* Metodos SET */
	
	public void setIdLista (Integer valor) 				{ this.idLista = valor;}
	public void setNombre (String  valor)				{ this.nombre = valor;}
	public void setLugar (String  valor)				{ this.lugar = valor;}
	public void setObservaciones (String  valor)		{ this.observaciones = valor;}
	public void setIdInstitucion (Integer valor) 		{ this.idInstitucion = valor;}

	
	/* Metodos GET */
	
	public Integer getIdLista () 					{ return this.idLista;}
	public String  getNombre ()						{ return this.nombre;}
	public String  getLugar ()						{ return this.lugar;}
	public String  getObservaciones ()				{ return this.observaciones;}
	public Integer getIdInstitucion () 				{ return this.idInstitucion;}

}