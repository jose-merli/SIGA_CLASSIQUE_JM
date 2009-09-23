package com.siga.beans;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_INCLUSIONGUARDIASENLISTAS
 * 
 * @author david.sanchezp
 * @since 27/12/2004
 */

public class ScsInclusionGuardiasEnListasBean extends MasterBean{
	
	/* Variables */ 
	
	private Integer 	idInstitucion;
	private Integer 	idTurno;
	private Integer 	idGuardia;	
	private Integer 	idLista;
	private Integer 	orden;
	
	/* Nombre de Tabla */
	
	static public String T_NOMBRETABLA = "SCS_INCLUSIONGUARDIASENLISTAS";
	
	
	/* Nombre de campos de la tabla */
	
	static public final String	C_IDINSTITUCION = 			"IDINSTITUCION";
	static public final String	C_IDTURNO = 				"IDTURNO";
	static public final String	C_IDGUARDIA = 				"IDGUARDIA";
	static public final String 	C_IDLISTA = 				"IDLISTA";
	static public final String	C_ORDEN =		 			"ORDEN";
	
	
	/* Metodos SET */
	
	public void setIdInstitucion (Integer valor) 		{ this.idInstitucion = valor;}
	public void setIdTurno (Integer valor) 				{ this.idTurno = valor;}
	public void setIdGuardia (Integer valor) 			{ this.idGuardia = valor;}
	public void setIdLista (Integer valor) 				{ this.idLista = valor;}
	public void setOrden (Integer valor) 				{ this.orden = valor;}
	
	
	/* Metodos GET */
	
	public Integer getIdInstitucion () 				{ return this.idInstitucion;}
	public Integer getIdTurno () 					{ return this.idTurno;}
	public Integer getIdGuardia () 					{ return this.idGuardia;}
	public Integer getIdLista () 					{ return this.idLista;}
	public Integer getOrden () 						{ return this.orden;}

}