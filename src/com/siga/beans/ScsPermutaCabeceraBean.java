package com.siga.beans;


/**
 * Implementa las operaciones sobre el bean de la tabla SCS_PERMUTA_CABECERA
 * 
 * @author Jorge PT
 * @since 09-10-2013
 */

public class ScsPermutaCabeceraBean extends MasterBean{
	
	/* Variables */ 	
	private Integer idPermutaCabecera;
	private Integer idInstitucion;	
	private Integer idTurno;
	private Integer idGuardia;
	private Integer idCalendarioGuardias;
	private Integer idPersona;
	private String fecha;
	
	/* Nombre de Tabla */	
	static public String T_NOMBRETABLA = "SCS_PERMUTA_CABECERA";	
	
	/* Nombre de campos de la tabla */	
	static public final String C_IDPERMUTACABECERA = "ID_PERMUTA_CABECERA";
	static public final String C_IDINSTITUCION = "IDINSTITUCION";	
	static public final String C_IDTURNO = "IDTURNO";
	static public final String C_IDGUARDIA = "IDGUARDIA";
	static public final String C_IDCALENDARIOGUARDIAS = "IDCALENDARIOGUARDIAS";
	static public final String C_IDPERSONA = "IDPERSONA";
	static public final String C_FECHA = "FECHA";
		
	/* Metodos SET */
	public void setIdPermutaCabecera (Integer valor)	{ this.idPermutaCabecera = valor;}
	public void setIdInstitucion (Integer valor) 		{ this.idInstitucion = valor;}	
	public void setIdTurno (Integer valor) 				{ this.idTurno = valor;}
	public void setIdGuardia (Integer valor) 			{ this.idGuardia = valor;}
	public void setIdCalendarioGuardias (Integer valor) { this.idCalendarioGuardias = valor;}	
	public void setIdPersona (Integer valor) 			{ this.idPersona = valor;}
	public void setFecha (String  valor)				{ this.fecha = valor;}
	
	/* Metodos GET */	
	public Integer getIdInstitucion () 			{ return this.idInstitucion;}
	public Integer getIdPermutaCabecera () 		{ return this.idPermutaCabecera;}
	public Integer getIdTurno () 				{ return this.idTurno;}
	public Integer getIdGuardia () 				{ return this.idGuardia;}
	public Integer getIdCalendarioGuardias ()	{ return this.idCalendarioGuardias;}
	public String  getFecha ()					{ return this.fecha;}
	public Integer getIdPersona () 				{ return this.idPersona;}
}