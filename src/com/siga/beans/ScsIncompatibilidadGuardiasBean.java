package com.siga.beans;

/**
 * Implementa las operaciones sobre el bean
 *   de la tabla SCS_INCOMPATIBILIDADGUARDIAS
 * Modificado el 17/01/2005 por david.sanchezp para incluir nuevos campos
 * Modificado el 18/03/2008 por adrian.ayala para incluir nuevos campos
 *   y limpiar
 * 
 * @author ruben.fernandez
 * @since 6/12/2004
 */
public class ScsIncompatibilidadGuardiasBean extends MasterBean
{
	////////// ATRIBUTOS //////////
	//Tabla
	static public String T_NOMBRETABLA = "SCS_INCOMPATIBILIDADGUARDIAS";
	
	//Campos de la tabla como atributos
	private Integer idInstitucion;
	private Integer idTurno;
	private Integer idGuardia;
	private String	motivos;
	private Integer	idTurnoIncompatible;
	private Integer	idGuardiaIncompatible;
	private Integer diasseparacionguardias;
	
	//Nombres de los campos de la tabla
	static public final String C_IDINSTITUCION =			"IDINSTITUCION";
	static public final String C_IDTURNO =					"IDTURNO";
	static public final String C_IDGUARDIA =				"IDGUARDIA";
	static public final String C_MOTIVOS =					"MOTIVOS";
	static public final String C_IDGUARDIA_INCOMPATIBLE =	"IDGUARDIA_INCOMPATIBLE";
	static public final String C_IDTURNO_INCOMPATIBLE =		"IDTURNO_INCOMPATIBLE";
	static public final String C_DIASSEPARACIONGUARDIAS =	"DIASSEPARACIONGUARDIAS";
	
	
	////////// SETTERS //////////
	public void setIdInstitucion 			(Integer valor) {this.idInstitucion				= valor;}
	public void setIdTurno 					(Integer valor) {this.idTurno					= valor;}
	public void setIdGuardia				(Integer valor) {this.idGuardia					= valor;}
	public void setMotivos 					(String  valor) {this.motivos					= valor;}
	public void setIdTurnoIncompatible		(Integer valor) {this.idTurnoIncompatible		= valor;}
	public void setIdGuardiaIncompatible	(Integer valor) {this.idGuardiaIncompatible		= valor;}
	public void setDiasseparacionguardias	(Integer valor) {this.diasseparacionguardias	= valor;}
	
	
	////////// GETTERS //////////
	public Integer getIdInstitucion				() {return this.idInstitucion;}
	public Integer getIdTurno					() {return this.idTurno;}
	public Integer getIdGuardia					() {return this.idGuardia;}
	public String  getMotivos					() {return this.motivos;}
	public Integer getIdTurnoIncompatible		() {return this.idTurnoIncompatible;}
	public Integer getIdGuardiaIncompatible		() {return this.idGuardiaIncompatible;}
	public Integer getDiasseparacionguardias	() {return this.diasseparacionguardias;}

}